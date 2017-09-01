package cn.vigor.modules.server.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.vigor.API.AmbariTools;
import cn.vigor.API.hosts.HostsManager;
import cn.vigor.API.model.metrics.common.Path;
import cn.vigor.API.util.HttpTools;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.server.entity.Component;
import cn.vigor.modules.server.entity.ServerInfos;
import cn.vigor.modules.server.entity.ServiceConfigInfo;
import cn.vigor.modules.server.entity.ServiceConfigTemp;
import cn.vigor.modules.server.service.PlatformService;
import cn.vigor.modules.server.service.ServerInfosService;
import cn.vigor.modules.server.util.MetricsUtil;

/**
 * 集群信息Controller
 * 
 * @author kiss
 * @version 2016-06-30
 */
@Controller
@RequestMapping(value = "${adminPath}/server/platform")
public class CalClusterController extends BaseController {

	@Autowired
	private PlatformService platformService;

	@Autowired
	private ServerInfosService serverInfosService;

	/**
	 * 查看，增加，编辑集群信息表单页面
	 */
	@RequiresPermissions(value = { "server:platform:view", "server:platform:add",
			"server:platform:edit" }, logical = Logical.OR)
	@RequestMapping(value = "calform")
	public String form(String name, Model model) {
		List<ServerInfos> ips = serverInfosService.findNewByServerName(MetricsUtil.getAddNode(name.toLowerCase()));
		model.addAttribute("name", name);
		model.addAttribute("ips", ips);
		model.addAttribute("component", new Component());
		return "modules/cluster/calNodeAdd";
	}

	/**
	 * 查看，增加，编辑集群信息表单页面
	 */
	@RequiresPermissions(value = { "server:platform:view", "server:platform:add",
			"server:platform:edit" }, logical = Logical.OR)
	@RequestMapping(value = "calsave")
	public String calSave(Component component, Model model, RedirectAttributes redirectAttributes) {
		String msg = "";
		try {
			msg = AmbariTools.addCalNode(component.getHostName(), component.getComponentName());
		} catch (Exception e) {
			msg = "操作失败,失败原因：" + e.getMessage();
			e.printStackTrace();
		}
		component.setServiceName(MetricsUtil.getAddNode(component.getComponentName().toUpperCase()));
		addMessage(redirectAttributes, msg);
		model.addAttribute("platform", component);
		return "modules/cluster/calInfo";
	}

	/**
	 * 查询计算集群列表
	 */
	@RequiresPermissions("server:platform:list")
	@RequestMapping(value = "callist")
	public String callist(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Component> cluster = serverInfosService.findCluseServer();
		model.addAttribute("clusters", cluster);
		return "modules/cluster/calList";
	}

	/**
	 * 查询计算集群列表
	 */
	@RequiresPermissions("server:platform:list")
	@RequestMapping(value = "calnodes")
	public String calnodes(Component component, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Component> page = new Page<Component>(request, response);
		try {
			// 如果serviceName为hdfs,则需要知道namenode状态
			Map<String, String> map = null;
			if (component.getServiceName().toLowerCase().equals("hdfs")) {
				String content = HostsManager.getHdfsNameNodeStatus(ambHost, ambPort, clustername);
				if (content != null) {
					map = new HashMap<String, String>();
					JSONObject nameNodeStatus = JSONObject.parseObject(content);
					JSONArray jsonArray = nameNodeStatus.getJSONArray("items");
					if (jsonArray == null) {
						logger.warn("content:" + content);
					} else
						for (Object obj : jsonArray) {
							JSONObject jb = (JSONObject) obj;
							String hostName = jb.getJSONObject("HostRoles").getString("host_name");
							String HAState = jb.getJSONObject("metrics").getJSONObject("dfs")
									.getJSONObject("FSNamesystem").getString("HAState");
							map.put(hostName, HAState);
						}
				}
			} else if (component.getServiceName().toLowerCase().equals("hbase")) {
				String content = HostsManager.getHbaseMasterStatus(ambHost, ambPort, clustername);
				if (content != null) {
					map = new HashMap<String, String>();
					JSONObject nameNodeStatus = JSONObject.parseObject(content);
					JSONArray jsonArray = nameNodeStatus.getJSONArray("items");
					if (jsonArray == null) {
						logger.warn("content:" + content);
					} else
						for (Object obj : jsonArray) {
							JSONObject jb = (JSONObject) obj;
							String hostName = jb.getJSONObject("HostRoles").getString("host_name");
							String isActiveMaster = jb.getJSONObject("metrics").getJSONObject("hbase")
									.getJSONObject("master").getString("IsActiveMaster");
							map.put(hostName, isActiveMaster);
						}
				}
			} else if (component.getServiceName().toLowerCase().equals("yarn")) {
				String content = HostsManager.getYarnResourceManagerStatus(ambHost, ambPort, clustername);
				if (content != null) {
					map = new HashMap<String, String>();
					JSONObject nameNodeStatus = JSONObject.parseObject(content);
					JSONArray jsonArray = nameNodeStatus.getJSONArray("items");
					if (jsonArray == null) {
						logger.warn("content:" + content);
					} else
						for (Object obj : jsonArray) {
							JSONObject jb = (JSONObject) obj;
							String hostName = jb.getJSONObject("HostRoles").getString("host_name");
							String haState = jb.getJSONObject("HostRoles").getString("ha_state");
							map.put(hostName, haState.toLowerCase());
						}
				}
			}
			List<Component> list = null;
			String serviceName = component.getServiceName().toUpperCase();
			String str = HostsManager.getHostComponents(ambHost, ambPort, clustername);
			if (str != null) {
				JSONObject jsonObject = JSONObject.parseObject(str);
				JSONArray items = jsonObject.getJSONArray("items");
				if (items == null) {
					logger.warn("content:" + str);
				} else if (items != null && items.size() > 0) {
					list = new ArrayList<Component>();
					int i = 0;
					for (Object obj : items) {
						JSONObject object = (JSONObject) obj;
						JSONArray hostComponents = object.getJSONArray("host_components");
						if (hostComponents != null && hostComponents.size() > 0) {
							for (Object obj2 : hostComponents) {
								JSONObject object2 = (JSONObject) obj2;
								JSONObject jb = object2.getJSONObject("HostRoles");
								if (component.getComponentName() != null
										&& !jb.getString("display_name").contains(component.getComponentName())) {
									continue;
								}
								String href = object2.getString("href");
								href = href.substring(href.indexOf("/") + 2, href.lastIndexOf(":"));
								if (jb.getString("service_name").equals(serviceName)
										&& !jb.getString("state").equals("INSTALL_FAILED")) {
									i++;
									if (i > (page.getPageNo() - 1) * page.getPageSize()) {
										Component c = new Component();
										c.setHostName(jb.getString("host_name"));
										c.setServiceName(serviceName);
										c.setComponentName(jb.getString("component_name"));
										c.setCurrentState(jb.getString("state").toLowerCase());
										c.setDisplayName(jb.getString("display_name"));
										c.setIp(href.substring(0, href.lastIndexOf(".") + 1)
												+ jb.getString("host_name").replace("xdata", ""));
										if (map != null && map.get(jb.getString("host_name")) != null) {
											if (serviceName.equals("HDFS")
													&& jb.getString("component_name").equals("NAMENODE")) {
												c.setNameNodeStatus(map.get(jb.getString("host_name")));
											} else if (serviceName.equals("HBASE")
													&& jb.getString("component_name").equals("HBASE_MASTER")) {
												String s = map.get(jb.getString("host_name"));
												c.setNameNodeStatus(Boolean.valueOf(s) ? "Active" : "Standby");
											} else if (serviceName.equals("YARN")
													&& jb.getString("component_name").equals("RESOURCEMANAGER")) {
												String s = map.get(jb.getString("host_name"));
												c.setNameNodeStatus(s);
											}
										}
										if (list.size() < page.getPageSize()) {
											list.add(c);
										}
									}
								}
							}
						}
					}
					page.setCount(i);
				}
			}
			page.setList(list);
			model.addAttribute("component", component);
			model.addAttribute("page", page);
			List<Component> cluster = serverInfosService.findCluseServer();
			model.addAttribute("clusters", cluster);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return "modules/cluster/nodeList";
	}

	@RequestMapping(value = "clusterInfo")
	@ResponseBody
	public String clusterInfo(String serviceName, String name, String fields) {
		String data = "";
		if (name == null) {
			name = "";
		}
		if (fields != null && !fields.equals("")) {
			fields = "?fields=" + fields;
		} else {
			fields = "";
		}
		try {
			String url = MetricsUtil.returnServiceUrl(ambHost, ambPort, clustername, serviceName.toUpperCase(),
					name.toUpperCase()) + fields;
			/* System.out.println(url); */
			data = HttpTools.getInstance().getContent(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@RequestMapping(value = "componetInfo")
	@ResponseBody
	public String componetInfo(String hostName, String name, String fields) {
		String data = "";
		if (name == null) {
			name = "";
		}
		if (fields != null && !fields.equals("")) {
			fields = "?fields=" + fields;
		} else {
			fields = "";
		}
		try {
			String url = MetricsUtil.returnHostUrl(ambHost, ambPort, clustername, hostName, name.toUpperCase())
					+ fields;
			/* System.out.println(url); */
			data = HttpTools.getInstance().getContent(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@Value("${ambr_host}")
	String ambHost = "172.18.84.67";

	@Value("${ambr_port}")
	String ambPort = "8080";

	@Value("${ambr_cluster_name:xdata2}")
	String clustername = "xdata2";

	/**
	 * 
	 * @param serviceName
	 *            服务名称
	 * @param name
	 *            组件名
	 * @param hostName
	 *            主机名
	 * @param type
	 *            1 启动 2 停止
	 * @return
	 */
	@RequestMapping(value = "claClusterOp")
	@ResponseBody
	public String claClusterOp(String serviceName, String name, String hostName, int type) {
		String cmd = type == 1 ? "start " + name : "stop " + name;
		String state = type == 1 ? "STARTED" : "INSTALLED";
		String url = MetricsUtil.returnHostUrl(ambHost, ambPort, clustername, hostName, name.toUpperCase());
		String entityString = MetricsUtil.getEntity(cmd, hostName, clustername, serviceName, state);
		String msg = "";
		try {
			msg = HttpTools.getInstance().putContent(url, entityString);
		} catch (Exception e) {
			msg = "操作失败!失败原因：" + e.getMessage();
			e.printStackTrace();
		}
		System.out.println(msg);
		return msg;
	}

	@RequestMapping(value = "cluesterFields")
	@ResponseBody
	public String getCluesterFileds(String serviceName) {
		String data = "";
		String name = "DATANODE";
		if ("yarn".equals(serviceName.toLowerCase())) {
			name = "NODEMANAGER";
		} else if ("hdfs".equals(serviceName.toLowerCase())) {
			name = "DATANODE";
		} else if ("hbase".equals(serviceName.toLowerCase())) {
			name = "HBASE_REGIONSERVER";
		}
		try {
			long end = new Date().getTime() / 1000;
			long start = end - 1800;
			String keys = MetricsUtil.getClusterKeys(serviceName.toLowerCase());
			List<Path> hostURLs = new ArrayList<Path>();
			for (String key : keys.split(",")) {
				Path murl = new Path();
				murl.setPath(key);
				murl.setStarttime(start + "");
				murl.setEndtime(end + "");
				murl.setStep("15");
				hostURLs.add(murl);
			}
			String url = MetricsUtil.returnRangeComponentUrl(ambHost, ambPort, clustername, serviceName.toUpperCase(),
					name.toUpperCase(), hostURLs);
			data = HttpTools.getInstance().getContent(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@RequestMapping(value = "cluesterField")
	@ResponseBody
	public String getCluesterFiled(String serviceName, String name, String startDate, String endDate, String fields) {
		String data = "";
		if (name == null) {
			name = "";
		}
		if (fields == null || fields.equals("")) {
			fields = "";
		}
		try {
			long end = new Date().getTime() / 1000 - 600;
			long start = end - 1800;
			/*
			 * if(startDate!=null&&endDate!=null ) { end =
			 * DateUtil.string2long(endDate); start =
			 * DateUtil.string2long(startDate); }
			 */
			List<Path> hostURLs = new ArrayList<Path>();
			for (String key : fields.split(",")) {
				Path murl = new Path();
				murl.setPath(key);
				murl.setStarttime(start + "");
				murl.setEndtime(end + "");
				murl.setStep("15");
				hostURLs.add(murl);
			}
			String url = MetricsUtil.returnRangeComponentUrl(ambHost, ambPort, clustername, serviceName.toUpperCase(),
					name.toUpperCase(), hostURLs);
			data = HttpTools.getInstance().getContent(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private JSONObject clusterfiedsInfo(String serviceName, String name, String fields) {
		JSONObject data = null;
		if (name == null) {
			name = "";
		}
		if (fields != null && !fields.equals("")) {
			fields = "?fields=" + fields;
		} else {
			fields = "";
		}
		try {
			String url = MetricsUtil.returnServiceUrl(ambHost, ambPort, clustername, serviceName.toUpperCase(),
					name.toUpperCase()) + fields;
			data = JSONObject.parseObject(HttpTools.getInstance().getContent(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * HDFS Disk Usage
	 * serviceName=hdfs&name=namenode&fields=metrics/dfs/FSNamesystem/
	 * CapacityRemainingGB,metrics/dfs/FSNamesystem/CapacityTotalGB,metrics/dfs/
	 * FSNamesystem/CapacityUsedGB
	 * 
	 * Namenode Uptime
	 * serviceName=hdfs&name=NAMENODE&fields=ServiceComponentInfo/StartTime,
	 * ServiceComponentInfo/started_count,ServiceComponentInfo/total_count
	 * 
	 * @return
	 */
	@RequestMapping(value = "index")
	@ResponseBody
	public JSONObject index() {

		String serviceName;
		String name;
		String fields;
		JSONObject data = new JSONObject();

		/**
		 * HDFS Disk Usage
		 * serviceName=hdfs&name=namenode&fields=metrics/dfs/FSNamesystem/
		 * CapacityRemainingGB,metrics/dfs/FSNamesystem/CapacityTotalGB,metrics/
		 * dfs/FSNamesystem/CapacityUsedGB
		 * 
		 * Namenode Uptime
		 * serviceName=hdfs&name=NAMENODE&fields=ServiceComponentInfo/StartTime,
		 * ServiceComponentInfo/started_count,ServiceComponentInfo/total_count
		 * 
		 * @return
		 */
		serviceName = "hdfs";
		name = "namenode";
		fields = "metrics/dfs/FSNamesystem/CapacityRemainingGB,metrics/dfs/FSNamesystem/CapacityTotalGB,metrics/dfs/FSNamesystem/CapacityUsedGB";
		data.put("HDFS_Disk_Usage", clusterfiedsInfo(serviceName, name, fields));
		fields = "ServiceComponentInfo/StartTime,ServiceComponentInfo/started_count,ServiceComponentInfo/total_count";
		data.put("Namenode_Uptime", clusterfiedsInfo(serviceName, name, fields));

		/**
		 * Resourcemanager_Uptime
		 * serviceName=yarn&name=RESOURCEMANAGER&fields=ServiceComponentInfo/
		 * StartTime,ServiceComponentInfo/started_count,ServiceComponentInfo/
		 * total_count
		 */
		serviceName = "yarn";
		name = "RESOURCEMANAGER";
		data.put("Resourcemanager_Uptime", clusterfiedsInfo(serviceName, name, fields));

		/**
		 * Hbase_master Uptime
		 * serviceName=hbase&name=HBASE_MASTER&fields=ServiceComponentInfo/
		 * MasterStartTime,ServiceComponentInfo/started_count,
		 * ServiceComponentInfo/total_count
		 */
		serviceName = "hbase";
		name = "HBASE_MASTER";
		fields = "ServiceComponentInfo/MasterStartTime,ServiceComponentInfo/started_count,ServiceComponentInfo/total_count";
		data.put("Hbase_master_Uptime", clusterfiedsInfo(serviceName, name, fields));

		/**
		 * Datanode Live
		 * serviceName=hdfs&name=DATANODE&fields=ServiceComponentInfo/
		 * started_count,ServiceComponentInfo/total_count
		 */
		serviceName = "hdfs";
		name = "DATANODE";
		fields = "ServiceComponentInfo/started_count,ServiceComponentInfo/total_count";
		data.put("Datanode_Live", clusterfiedsInfo(serviceName, name, fields));

		/**
		 * Nodemanager Live
		 * serviceName=yarn&name=NODEMANAGER&fields=ServiceComponentInfo/
		 * started_count,ServiceComponentInfo/total_count
		 */
		serviceName = "yarn";
		name = "NODEMANAGER";
		fields = "ServiceComponentInfo/started_count,ServiceComponentInfo/total_count";
		data.put("Nodemanager_Live", clusterfiedsInfo(serviceName, name, fields));

		/**
		 * Hbase_regionserver Live
		 * serviceName=hbase&name=HBASE_REGIONSERVER&fields=ServiceComponentInfo
		 * /started_count,ServiceComponentInfo/total_count
		 */
		serviceName = "hbase";
		name = "HBASE_REGIONSERVER";
		fields = "ServiceComponentInfo/started_count,ServiceComponentInfo/total_count";
		data.put("Regionserver_Live", clusterfiedsInfo(serviceName, name, fields));

		data.put("Etlservers_Live", platformService.findClusterStatus("13"));

		data.put("JobInfo", platformService.findJobInfo());
		return data;
	}

	/**
	 * 获取服务配置信息
	 * 
	 * @param serviceName
	 *            服务名称
	 * @param model
	 * @return url
	 */
	@RequestMapping(value = "serviceConfig")
	public String findServiceConfigs(@RequestParam(value = "serviceName", required = true) String serviceName,
			String version, Model model) {
		try {
			String content = HostsManager.getServiceConfig(ambHost, ambPort, clustername, serviceName.toUpperCase(),
					null, null);
			List<Map<String, Object>> serviceVersionInfos = new ArrayList<Map<String, Object>>();
			JSONObject cversionInfo = null;
			if (StringUtils.isNotEmpty(content)) {
				Map map = JSONObject.parseObject(content, Map.class);
				JSONArray array = (JSONArray) map.get("items");
				if (array == null) {
					logger.warn("content:" + array);
				} else
					for (int i = 0; i < array.size(); i++) {
						Map<String, Object> versionInfo = new HashMap<String, Object>();
						JSONObject jj = (JSONObject) array.get(i);
						String groupName = jj.getString("group_name");
						if (groupName==null||!groupName.toLowerCase().equals("default")) {
							continue;
						}
						String vs = jj.getString("service_config_version");
						versionInfo.put("version", vs);
						String isC = jj.getString("is_current");
						if (StringUtils.isEmpty(version)) {
							if (Boolean.valueOf(isC)) {
								cversionInfo = jj;
							}
						} else {
							if (vs.equals(version)) {
								cversionInfo = jj;
							}
						}
						versionInfo.put("isCurrent", isC);
						String stci = jj.getString("stack_id");
						versionInfo.put("stackId", stci);
						String user = jj.getString("user");
						versionInfo.put("user", user);
						long ct = jj.getLongValue("createtime");
						String createtime = "";
						long c = ((new Date().getTime()) - ct) / (1000 * 3600 * 24);
						if (c == 0) {// 时间差小于一天
							c = ((new Date().getTime()) - ct) / (1000 * 3600);// 计算差几小时
							if (c > 0) {
								createtime = c + "小时前";
							} else {
								c = ((new Date().getTime()) - ct) / (1000 * 60);// 计算分钟差
								if (c == 0) {
									createtime = "刚刚";
								} else {
									createtime = c + "分钟前";
								}
							}
						} else if (c > 0 && c < 30) {
							createtime = c + "天前";
						} else {
							c = c / 30;
							if (c < 12) {
								createtime = c + "个月前";
							} else {
								createtime = c / 12 + "年前";
							}
						}
						versionInfo.put("createtime", createtime);
						serviceVersionInfos.add(versionInfo);
					}
			}

			// 对list map排序(根据version进行倒序排序)
			if (serviceVersionInfos != null && serviceVersionInfos.size() > 0) {
				Collections.sort(serviceVersionInfos, new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						@SuppressWarnings("unchecked")
						Map<String, Object> m1 = (Map<String, Object>) o1;
						@SuppressWarnings("unchecked")
						Map<String, Object> m2 = (Map<String, Object>) o2;
						if (Integer.valueOf(m1.get("version").toString()) > Integer
								.valueOf(m2.get("version").toString()))
							return -1;
						if (Integer.valueOf(m1.get("version").toString()) < Integer
								.valueOf(m2.get("version").toString()))
							return 1;
						return 0;
					}
				});
			} else {
				logger.warn("content:" + content);
			}
			if (cversionInfo == null) {
				logger.warn("content:" + content);
			} else {
				ServiceConfigInfo configInfo = handleServiceConf(serviceName.toUpperCase(),
						cversionInfo.toJavaObject(ServiceConfigInfo.class));
				model.addAttribute("data", configInfo);
			}
			model.addAttribute("svInfo", serviceVersionInfos);
			List<Component> cluster = serverInfosService.findCluseServer();
			model.addAttribute("clusters", cluster);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return "modules/cluster/serviceConfigDetail";
	}

	private static ServiceConfigInfo handleServiceConf(String serviceName, ServiceConfigInfo serviceConfigInfo) {
		if (serviceConfigInfo == null) {
			return serviceConfigInfo;
		}
		JSONArray jsonArray = serviceConfigInfo.getConfigurations();
		JSONArray hArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cluster_name", serviceConfigInfo.getCluster_name());
		map.put("stack_id", serviceConfigInfo.getStack_id());
		switch (serviceName) {
		case "YARN": {
			// 添加页面上显示的其他数据
			// 1.Resource Manager
			ServiceConfigTemp rmTemp = new ServiceConfigTemp(map);
			rmTemp.setType("Resource Manager");
			JSONObject rmProperties = rmTemp.getProperties();
			// 2.Node Manager
			ServiceConfigTemp nmTemp = new ServiceConfigTemp(map);
			nmTemp.setType("Node Manager");
			JSONObject nmProperties = nmTemp.getProperties();
			// 3.Application Timeline Server
			ServiceConfigTemp atTemp = new ServiceConfigTemp(map);
			atTemp.setType("Application Timeline Server");
			JSONObject atProperties = atTemp.getProperties();
			// 4.General
			ServiceConfigTemp gTemp = new ServiceConfigTemp(map);
			gTemp.setType("General");
			JSONObject gProperties = gTemp.getProperties();
			// 5.Fault Tolerance
			ServiceConfigTemp ftTemp = new ServiceConfigTemp(map);
			ftTemp.setType("Fault Tolerance");
			JSONObject ftProperties = ftTemp.getProperties();
			// 6.Isolation
			ServiceConfigTemp iTemp = new ServiceConfigTemp(map);
			iTemp.setType("Isolation");
			JSONObject iProperties = iTemp.getProperties();
			// 7.Custom yarn-site
			// ServiceConfigTemp cysTemp = new ServiceConfigTemp(map);
			// cysTemp.setType("Custom yarn-site");
			// JSONObject cysProperties = cysTemp.getProperties();
			// 过滤properties数据为空的配置
			JSONArray tempArray = new JSONArray();
			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object object : jsonArray) {
					JSONObject jo = (JSONObject) object;
					String type = jo.getString("type");
					JSONObject joProperties = jo.getJSONObject("properties");
					if (joProperties != null && joProperties.size() > 0) {
						tempArray.add(object);
						if (type.equals("yarn-site")) {
							rmProperties.put("yarn.acl.enable", joProperties.get("yarn.acl.enable"));
							joProperties.remove("yarn.acl.enable");
							rmProperties.put("yarn.admin.acl", joProperties.get("yarn.admin.acl"));
							joProperties.remove("yarn.admin.acl");
							rmProperties.put("yarn.log-aggregation-enable",
									joProperties.get("yarn.log-aggregation-enable"));
							joProperties.remove("yarn.log-aggregation-enable");

							nmProperties.put("yarn.nodemanager.aux-services",
									joProperties.get("yarn.nodemanager.aux-services"));
							joProperties.remove("yarn.nodemanager.aux-services");
							nmProperties.put("yarn.nodemanager.local-dirs",
									joProperties.get("yarn.nodemanager.local-dirs"));
							joProperties.remove("yarn.nodemanager.local-dirs");
							nmProperties.put("yarn.nodemanager.log-dirs",
									joProperties.get("yarn.nodemanager.log-dirs"));
							joProperties.remove("yarn.nodemanager.log-dirs");
							nmProperties.put("yarn.nodemanager.log.retain-second",
									joProperties.get("yarn.nodemanager.log.retain-second"));
							joProperties.remove("yarn.nodemanager.log.retain-second");
							nmProperties.put("yarn.nodemanager.remote-app-log-dir",
									joProperties.get("yarn.nodemanager.remote-app-log-dir"));
							joProperties.remove("yarn.nodemanager.remote-app-log-dir");
							nmProperties.put("yarn.nodemanager.remote-app-log-dir-suffix",
									joProperties.get("yarn.nodemanager.remote-app-log-dir-suffix"));
							joProperties.remove("yarn.nodemanager.remote-app-log-dir-suffix");
							nmProperties.put("yarn.nodemanager.vmem-pmem-ratio",
									joProperties.get("yarn.nodemanager.vmem-pmem-ratio"));
							joProperties.remove("yarn.nodemanager.vmem-pmem-ratio");

							atProperties.put("yarn.timeline-service.address",
									joProperties.get("yarn.timeline-service.address"));
							joProperties.remove("yarn.timeline-service.address");
							atProperties.put("yarn.timeline-service.enabled",
									joProperties.get("yarn.timeline-service.enabled"));
							joProperties.remove("yarn.timeline-service.enabled");
							atProperties.put("yarn.timeline-service.generic-application-history.store-class",
									joProperties.get("yarn.timeline-service.generic-application-history.store-class"));
							joProperties.remove("yarn.timeline-service.generic-application-history.store-class");
							atProperties.put("yarn.timeline-service.leveldb-state-store.path",
									joProperties.get("yarn.timeline-service.leveldb-state-store.path"));
							joProperties.remove("yarn.timeline-service.leveldb-state-store.path");
							atProperties.put("yarn.timeline-service.leveldb-timeline-store.path",
									joProperties.get("yarn.timeline-service.leveldb-timeline-store.path"));
							joProperties.remove("yarn.timeline-service.leveldb-timeline-store.path");
							atProperties.put("yarn.timeline-service.leveldb-timeline-store.ttl-interval-ms",
									joProperties.get("yarn.timeline-service.leveldb-timeline-store.ttl-interval-ms"));
							joProperties.remove("yarn.timeline-service.leveldb-timeline-store.ttl-interval-ms");
							atProperties.put("yarn.timeline-service.state-store-class",
									joProperties.get("yarn.timeline-service.state-store-class"));
							joProperties.remove("yarn.timeline-service.state-store-class");
							atProperties.put("yarn.timeline-service.store-class",
									joProperties.get("yarn.timeline-service.store-class"));
							joProperties.remove("yarn.timeline-service.store-class");
							atProperties.put("yarn.timeline-service.ttl-enable",
									joProperties.get("yarn.timeline-service.ttl-enable"));
							joProperties.remove("yarn.timeline-service.ttl-enable");
							atProperties.put("yarn.timeline-service.ttl-ms",
									joProperties.get("yarn.timeline-service.ttl-ms"));
							joProperties.remove("yarn.timeline-service.ttl-ms");
							atProperties.put("yarn.timeline-service.webapp.address",
									joProperties.get("yarn.timeline-service.webapp.address"));
							joProperties.remove("yarn.timeline-service.webapp.address");
							atProperties.put("yarn.timeline-service.webapp.https.address",
									joProperties.get("yarn.timeline-service.webapp.https.address"));
							joProperties.remove("yarn.timeline-service.webapp.https.address");

							ftProperties.put("yarn.nodemanager.recovery.enabled",
									joProperties.get("yarn.nodemanager.recovery.enabled"));
							joProperties.remove("yarn.nodemanager.recovery.enabled");
							ftProperties.put("yarn.resourcemanager.connect.max-wait.ms",
									joProperties.get("yarn.resourcemanager.connect.max-wait.ms"));
							joProperties.remove("yarn.resourcemanager.connect.max-wait.ms");
							ftProperties.put("yarn.resourcemanager.connect.retry-interval.ms",
									joProperties.get("yarn.resourcemanager.connect.retry-interval.ms"));
							joProperties.remove("yarn.resourcemanager.connect.retry-interval.ms");
							ftProperties.put("yarn.resourcemanager.ha.enabled",
									joProperties.get("yarn.resourcemanager.ha.enabled"));
							joProperties.remove("yarn.resourcemanager.ha.enabled");
							ftProperties.put("yarn.resourcemanager.recovery.enabled",
									joProperties.get("yarn.resourcemanager.recovery.enabled"));
							joProperties.remove("yarn.resourcemanager.recovery.enabled");
							ftProperties.put("yarn.resourcemanager.work-preserving-recovery.enabled",
									joProperties.get("yarn.resourcemanager.work-preserving-recovery.enabled"));
							joProperties.remove("yarn.resourcemanager.work-preserving-recovery.enabled");
							ftProperties.put("yarn.resourcemanager.zk-address",
									joProperties.get("yarn.resourcemanager.zk-address"));
							joProperties.remove("yarn.resourcemanager.zk-address");

							iProperties.put("yarn.nodemanager.container-executor.class",
									joProperties.get("yarn.nodemanager.container-executor.class"));
							joProperties.remove("yarn.nodemanager.container-executor.class");
							iProperties.put("yarn.nodemanager.linux-container-executor.cgroups.hierarchy",
									joProperties.get("yarn.nodemanager.linux-container-executor.cgroups.hierarchy"));
							joProperties.remove("yarn.nodemanager.linux-container-executor.cgroups.hierarchy");
							iProperties.put("yarn.nodemanager.linux-container-executor.cgroups.mount",
									joProperties.get("yarn.nodemanager.linux-container-executor.cgroups.mount"));
							joProperties.remove("yarn.nodemanager.linux-container-executor.cgroups.mount");
							iProperties.put("yarn.nodemanager.linux-container-executor.cgroups.strict-resource-usage",
									joProperties.get(
											"yarn.nodemanager.linux-container-executor.cgroups.strict-resource-usage"));
							joProperties
									.remove("yarn.nodemanager.linux-container-executor.cgroups.strict-resource-usage");
							iProperties.put("yarn.nodemanager.linux-container-executor.group",
									joProperties.get("yarn.nodemanager.linux-container-executor.group"));
							joProperties.remove("yarn.nodemanager.linux-container-executor.group");
							iProperties.put("yarn.nodemanager.linux-container-executor.resources-handler.class",
									joProperties
											.get("yarn.nodemanager.linux-container-executor.resources-handler.class"));
							joProperties.remove("yarn.nodemanager.linux-container-executor.resources-handler.class");

						} else if (type.equals("yarn-env")) {
							rmProperties.put("resourcemanager_heapsize", joProperties.get("resourcemanager_heapsize"));

							nmProperties.put("nodemanager_heapsize", joProperties.get("nodemanager_heapsize"));

							atProperties.put("apptimelineserver_heapsize",
									joProperties.get("apptimelineserver_heapsize"));

							gProperties.put("yarn_heapsize", joProperties.get("yarn_heapsize"));

						} else if (type.equals("capacity-scheduler")) {
							// jo.put("type", "Scheduler");
							// JSONObject jsonObject = new JSONObject();
							// jsonObject.put("Capacity Scheduler",
							// joProperties.toJSONString().replace("\"",
							// "").replace(",", "\n").replace("{",
							// "").replace("}", ""));
							// jsonObject.put("yarn.resourcemanager.scheduler.class",
							// "org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler");
							// jo.put("properties", jsonObject);
						}
					}
				}
			}
			rmTemp.setProperties(rmProperties);
			hArray.add(rmTemp);
			nmTemp.setProperties(nmProperties);
			hArray.add(nmTemp);
			atTemp.setProperties(atProperties);
			hArray.add(atTemp);
			gTemp.setProperties(gProperties);
			hArray.add(gTemp);
			ftTemp.setProperties(ftProperties);
			hArray.add(ftTemp);
			iTemp.setProperties(iProperties);
			hArray.add(iTemp);
			hArray.addAll(tempArray);
			serviceConfigInfo.setConfigurations(hArray);
		}
			break;
		case "HDFS": {
			// 1.NameNode
			ServiceConfigTemp nnTemp = new ServiceConfigTemp(map);
			nnTemp.setType("NameNode");
			JSONObject nnProperties = nnTemp.getProperties();
			// 2.DataNode
			ServiceConfigTemp dnTemp = new ServiceConfigTemp(map);
			dnTemp.setType("DataNode");
			JSONObject dnProperties = dnTemp.getProperties();
			// 3.General
			ServiceConfigTemp gTemp = new ServiceConfigTemp(map);
			gTemp.setType("General");
			JSONObject gProperties = gTemp.getProperties();
			// 4.NFS Gateway
			ServiceConfigTemp ngTemp = new ServiceConfigTemp(map);
			ngTemp.setType("NFS Gateway");
			JSONObject ngProperties = ngTemp.getProperties();
			// 过滤配置信息
			JSONArray tempArray = new JSONArray();
			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object object : jsonArray) {
					JSONObject jo = (JSONObject) object;
					String type = jo.getString("type");
					JSONObject joProperties = jo.getJSONObject("properties");
					if (joProperties != null && joProperties.size() > 0) {
						tempArray.add(object);
						if (type.equals("hadoop-env")) {
							nnProperties.put("namenode_opt_newsize", joProperties.get("namenode_opt_newsize"));
							joProperties.remove("namenode_opt_newsize");
							nnProperties.put("namenode_opt_maxnewsize", joProperties.get("namenode_opt_maxnewsize"));
							joProperties.remove("namenode_opt_maxnewsize");
							nnProperties.put("namenode_opt_permsize", joProperties.get("namenode_opt_permsize"));
							joProperties.remove("namenode_opt_permsize");
							nnProperties.put("namenode_opt_maxpermsize", joProperties.get("namenode_opt_maxpermsize"));
							joProperties.remove("namenode_opt_maxpermsize");

							gProperties.put("hadoop_heapsize", joProperties.get("hadoop_heapsize"));
							joProperties.remove("hadoop_heapsize");

							ngProperties.put("nfsgateway_heapsize", joProperties.get("nfsgateway_heapsize"));
							joProperties.remove("nfsgateway_heapsize");

						} else if (type.equals("hdfs-site")) {
							dnProperties.put("dfs.datanode.data.dir.perm",
									joProperties.get("dfs.datanode.data.dir.perm"));
							joProperties.remove("dfs.datanode.data.dir.perm");

							gProperties.put("dfs.namenode.accesstime.precision",
									joProperties.get("dfs.namenode.accesstime.precision"));
							joProperties.remove("dfs.namenode.accesstime.precision");
							gProperties.put("dfs.namenode.checkpoint.period",
									joProperties.get("dfs.namenode.checkpoint.period"));
							joProperties.remove("dfs.namenode.checkpoint.period");
							gProperties.put("dfs.datanode.du.reserved", joProperties.get("dfs.datanode.du.reserved"));
							joProperties.remove("dfs.datanode.du.reserved");
							gProperties.put("dfs.replication", joProperties.get("dfs.replication"));
							joProperties.remove("dfs.replication");

							ngProperties.put("nfs.file.dump.dir", joProperties.get("nfs.file.dump.dir"));
							joProperties.remove("nfs.file.dump.dir");
							ngProperties.put("nfs.exports.allowed.hosts",
									joProperties.get("nfs.exports.allowed.hosts"));
							joProperties.remove("nfs.exports.allowed.hosts");
						}
					}
				}
			}
			nnTemp.setProperties(nnProperties);
			hArray.add(nnTemp);
			dnTemp.setProperties(dnProperties);
			hArray.add(dnTemp);
			gTemp.setProperties(gProperties);
			hArray.add(gTemp);
			ngTemp.setProperties(ngProperties);
			hArray.add(ngTemp);
			hArray.addAll(tempArray);
			serviceConfigInfo.setConfigurations(hArray);
		}
			break;
		case "HIVE": {
			// 1.Hive Metastore
			ServiceConfigTemp hmTemp = new ServiceConfigTemp(map);
			hmTemp.setType("Hive Metastore");
			JSONObject hmProperties = hmTemp.getProperties();
			// 2.General
			ServiceConfigTemp gTemp = new ServiceConfigTemp(map);
			gTemp.setType("General");
			JSONObject gProperties = gTemp.getProperties();
			// 3.Performance
			ServiceConfigTemp pTemp = new ServiceConfigTemp(map);
			pTemp.setType("Performance");
			JSONObject pProperties = pTemp.getProperties();
			// 过滤配置信息
			JSONArray tempArray = new JSONArray();
			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object object : jsonArray) {
					JSONObject jo = (JSONObject) object;
					String type = jo.getString("type");
					JSONObject joProperties = jo.getJSONObject("properties");
					if (joProperties != null && joProperties.size() > 0) {
						tempArray.add(object);
						if (type.equals("hive-env")) {
							hmProperties.put("hive_database", joProperties.get("hive_database"));
							joProperties.remove("hive_database");
							hmProperties.put("hive_database_type", joProperties.get("hive_database_type"));
							joProperties.remove("hive_database_type");

						} else if (type.equals("hive-site")) {
							hmProperties.put("ambari.hive.db.schema.name",
									joProperties.get("ambari.hive.db.schema.name"));
							joProperties.remove("ambari.hive.db.schema.name");
							hmProperties.put("javax.jdo.option.ConnectionUserName",
									joProperties.get("javax.jdo.option.ConnectionUserName"));
							joProperties.remove("javax.jdo.option.ConnectionUserName");
							hmProperties.put("javax.jdo.option.ConnectionPassword",
									joProperties.get("javax.jdo.option.ConnectionPassword"));
							joProperties.remove("javax.jdo.option.ConnectionPassword");
							hmProperties.put("javax.jdo.option.ConnectionDriverName",
									joProperties.get("javax.jdo.option.ConnectionDriverName"));
							joProperties.remove("javax.jdo.option.ConnectionDriverName");
							hmProperties.put("javax.jdo.option.ConnectionURL",
									joProperties.get("javax.jdo.option.ConnectionURL"));
							joProperties.remove("javax.jdo.option.ConnectionURL");

							gProperties.put("datanucleus.cache.level2.type",
									joProperties.get("datanucleus.cache.level2.type"));
							joProperties.remove("datanucleus.cache.level2.type");
							gProperties.put("hive.compactor.check.interval",
									joProperties.get("hive.compactor.check.interval"));
							joProperties.remove("hive.compactor.check.interval");
							gProperties.put("hive.compactor.delta.num.threshold",
									joProperties.get("hive.compactor.delta.num.threshold"));
							joProperties.remove("hive.compactor.delta.num.threshold");
							gProperties.put("hive.compactor.delta.pct.threshold",
									joProperties.get("hive.compactor.delta.pct.threshold"));
							joProperties.remove("hive.compactor.delta.pct.threshold");
							gProperties.put("hive.compactor.worker.timeout",
									joProperties.get("hive.compactor.worker.timeout"));
							joProperties.remove("hive.compactor.worker.timeout");
							gProperties.put("hive.enforce.bucketing", joProperties.get("hive.enforce.bucketing"));
							joProperties.remove("hive.enforce.bucketing");
							gProperties.put("hive.exec.dynamic.partition",
									joProperties.get("hive.exec.dynamic.partition"));
							joProperties.remove("hive.exec.dynamic.partition");
							gProperties.put("hive.exec.dynamic.partition.mode",
									joProperties.get("hive.exec.dynamic.partition.mode"));
							joProperties.remove("hive.exec.dynamic.partition.mode");
							gProperties.put("hive.exec.failure.hooks", joProperties.get("hive.exec.failure.hooks"));
							joProperties.remove("hive.exec.failure.hooks");
							gProperties.put("hive.exec.max.created.files",
									joProperties.get("hive.exec.max.created.files"));
							joProperties.remove("hive.exec.max.created.files");
							gProperties.put("hive.exec.max.dynamic.partitions",
									joProperties.get("hive.exec.max.dynamic.partitions"));
							joProperties.remove("hive.exec.max.dynamic.partitions");
							gProperties.put("hive.exec.max.dynamic.partitions.pernode",
									joProperties.get("hive.exec.max.dynamic.partitions.pernode"));
							joProperties.remove("hive.exec.max.dynamic.partitions.pernode");
							gProperties.put("hive.exec.parallel.thread.number",
									joProperties.get("hive.exec.parallel.thread.number"));
							joProperties.remove("hive.exec.parallel.thread.number");
							gProperties.put("hive.exec.post.hooks", joProperties.get("hive.exec.post.hooks"));
							joProperties.remove("hive.exec.post.hooks");
							gProperties.put("hive.exec.pre.hooks", joProperties.get("hive.exec.pre.hooks"));
							joProperties.remove("hive.exec.pre.hooks");
							gProperties.put("hive.metastore.uris", joProperties.get("hive.metastore.uris"));
							joProperties.remove("hive.metastore.uris");
							gProperties.put("hive.metastore.warehouse.dir",
									joProperties.get("hive.metastore.warehouse.dir"));
							joProperties.remove("hive.metastore.warehouse.dir");
							gProperties.put("hive.security.authorization.enabled",
									joProperties.get("hive.security.authorization.enabled"));
							joProperties.remove("hive.security.authorization.enabled");
							gProperties.put("hive.security.authorization.manager",
									joProperties.get("hive.security.authorization.manager"));
							joProperties.remove("hive.security.authorization.manager");
							gProperties.put("hive.security.metastore.authenticator.manager",
									joProperties.get("hive.security.metastore.authenticator.manager"));
							joProperties.remove("hive.security.metastore.authenticator.manager");
							gProperties.put("hive.server2.thrift.http.path",
									joProperties.get("hive.server2.thrift.http.path"));
							joProperties.remove("hive.server2.thrift.http.path");
							gProperties.put("hive.server2.thrift.http.port",
									joProperties.get("hive.server2.thrift.http.port"));
							joProperties.remove("hive.server2.thrift.http.port");
							gProperties.put("hive.server2.thrift.max.worker.threads",
									joProperties.get("hive.server2.thrift.max.worker.threads"));
							joProperties.remove("hive.server2.thrift.max.worker.threads");
							gProperties.put("hive.server2.thrift.port", joProperties.get("hive.server2.thrift.port"));
							joProperties.remove("hive.server2.thrift.port");
							gProperties.put("hive.server2.thrift.sasl.qop",
									joProperties.get("hive.server2.thrift.sasl.qop"));
							joProperties.remove("hive.server2.thrift.sasl.qop");
							gProperties.put("hive.server2.transport.mode",
									joProperties.get("hive.server2.transport.mode"));
							joProperties.remove("hive.server2.transport.mode");
							gProperties.put("hive.tez.java.opts", joProperties.get("hive.tez.java.opts"));
							joProperties.remove("hive.tez.java.opts");
							gProperties.put("hive.tez.log.level", joProperties.get("hive.tez.log.level"));
							joProperties.remove("hive.tez.log.level");
							gProperties.put("hive.txn.manager", joProperties.get("hive.txn.manager"));
							joProperties.remove("hive.txn.manager");
							gProperties.put("hive.txn.max.open.batch", joProperties.get("hive.txn.max.open.batch"));
							joProperties.remove("hive.txn.max.open.batch");
							gProperties.put("hive.txn.timeout", joProperties.get("hive.txn.timeout"));
							joProperties.remove("hive.txn.timeout");

							pProperties.put("hive.auto.convert.join", joProperties.get("hive.auto.convert.join"));
							joProperties.remove("hive.auto.convert.join");
							pProperties.put("hive.auto.convert.join.noconditionaltask",
									joProperties.get("hive.auto.convert.join.noconditionaltask"));
							joProperties.remove("hive.auto.convert.join.noconditionaltask");
							pProperties.put("hive.compute.query.using.stats",
									joProperties.get("hive.compute.query.using.stats"));
							joProperties.remove("hive.compute.query.using.stats");
							pProperties.put("hive.fetch.task.aggr", joProperties.get("hive.fetch.task.aggr"));
							joProperties.remove("hive.fetch.task.aggr");
							pProperties.put("hive.fetch.task.conversion",
									joProperties.get("hive.fetch.task.conversion"));
							joProperties.remove("hive.fetch.task.conversion");
							pProperties.put("hive.fetch.task.conversion.threshold",
									joProperties.get("hive.fetch.task.conversion.threshold"));
							joProperties.remove("hive.fetch.task.conversion.threshold");
							pProperties.put("hive.limit.pushdown.memory.usage",
									joProperties.get("hive.limit.pushdown.memory.usage"));
							joProperties.remove("hive.limit.pushdown.memory.usage");
							pProperties.put("hive.map.aggr", joProperties.get("hive.map.aggr"));
							joProperties.remove("hive.map.aggr");
							pProperties.put("hive.map.aggr.hash.percentmemory",
									joProperties.get("hive.map.aggr.hash.percentmemory"));
							joProperties.remove("hive.map.aggr.hash.percentmemory");
							pProperties.put("hive.merge.mapfiles", joProperties.get("hive.merge.mapfiles"));
							joProperties.remove("hive.merge.mapfiles");
							pProperties.put("hive.merge.mapredfiles", joProperties.get("hive.merge.mapredfiles"));
							joProperties.remove("hive.merge.mapredfiles");
							pProperties.put("hive.merge.orcfile.stripe.level",
									joProperties.get("hive.merge.orcfile.stripe.level"));
							joProperties.remove("hive.merge.orcfile.stripe.level");
							pProperties.put("hive.merge.size.per.task", joProperties.get("hive.merge.size.per.task"));
							joProperties.remove("hive.merge.size.per.task");
							pProperties.put("hive.merge.smallfiles.avgsize",
									joProperties.get("hive.merge.smallfiles.avgsize"));
							joProperties.remove("hive.merge.smallfiles.avgsize");
							pProperties.put("hive.merge.tezfiles", joProperties.get("hive.merge.tezfiles"));
							joProperties.remove("hive.merge.tezfiles");
							pProperties.put("hive.optimize.bucketmapjoin.sortedmerge",
									joProperties.get("hive.optimize.bucketmapjoin.sortedmerge"));
							joProperties.remove("hive.optimize.bucketmapjoin.sortedmerge");
							pProperties.put("hive.optimize.index.filter",
									joProperties.get("hive.optimize.index.filter"));
							joProperties.remove("hive.optimize.index.filter");
							pProperties.put("hive.optimize.reducededuplication",
									joProperties.get("hive.optimize.reducededuplication"));
							joProperties.remove("hive.optimize.reducededuplication");
							pProperties.put("hive.optimize.reducededuplication.min.reducer",
									joProperties.get("hive.optimize.reducededuplication.min.reducer"));
							joProperties.remove("hive.optimize.reducededuplication.min.reducer");
							pProperties.put("hive.optimize.sort.dynamic.partition",
									joProperties.get("hive.optimize.sort.dynamic.partition"));
							joProperties.remove("hive.optimize.sort.dynamic.partition");
							pProperties.put("hive.orc.splits.include.file.footer",
									joProperties.get("hive.orc.splits.include.file.footer"));
							joProperties.remove("hive.orc.splits.include.file.footer");
							pProperties.put("hive.smbjoin.cache.rows", joProperties.get("hive.smbjoin.cache.rows"));
							joProperties.remove("hive.smbjoin.cache.rows");
							pProperties.put("hive.stats.autogather", joProperties.get("hive.stats.autogather"));
							joProperties.remove("hive.stats.autogather");
							pProperties.put("hive.tez.auto.reducer.parallelism",
									joProperties.get("hive.tez.auto.reducer.parallelism"));
							joProperties.remove("hive.tez.auto.reducer.parallelism");
							pProperties.put("hive.tez.dynamic.partition.pruning",
									joProperties.get("hive.tez.dynamic.partition.pruning"));
							joProperties.remove("hive.tez.dynamic.partition.pruning");
							pProperties.put("hive.tez.dynamic.partition.pruning.max.data.size",
									joProperties.get("hive.tez.dynamic.partition.pruning.max.data.size"));
							joProperties.remove("hive.tez.dynamic.partition.pruning.max.data.size");
							pProperties.put("hive.tez.dynamic.partition.pruning.max.event.size",
									joProperties.get("hive.tez.dynamic.partition.pruning.max.event.size"));
							joProperties.remove("hive.tez.dynamic.partition.pruning.max.event.size");
							pProperties.put("hive.tez.max.partition.factor",
									joProperties.get("hive.tez.max.partition.factor"));
							joProperties.remove("hive.tez.max.partition.factor");
							pProperties.put("hive.tez.min.partition.factor",
									joProperties.get("hive.tez.min.partition.factor"));
							joProperties.remove("hive.tez.min.partition.factor");
							pProperties.put("hive.tez.smb.number.waves", joProperties.get("hive.tez.smb.number.waves"));
							joProperties.remove("hive.tez.smb.number.waves");
							pProperties.put("hive.vectorized.execution.enabled",
									joProperties.get("hive.vectorized.execution.enabled"));
							joProperties.remove("hive.vectorized.execution.enabled");
							pProperties.put("hive.vectorized.execution.reduce.enabled",
									joProperties.get("hive.vectorized.execution.reduce.enabled"));
							joProperties.remove("hive.vectorized.execution.reduce.enabled");
							pProperties.put("hive.vectorized.groupby.checkinterval",
									joProperties.get("hive.vectorized.groupby.checkinterval"));
							joProperties.remove("hive.vectorized.groupby.checkinterval");
							pProperties.put("hive.vectorized.groupby.flush.percent",
									joProperties.get("hive.vectorized.groupby.flush.percent"));
							joProperties.remove("hive.vectorized.groupby.flush.percent");
						}
					}
				}
			}
			hmTemp.setProperties(hmProperties);
			hArray.add(hmTemp);
			gTemp.setProperties(gProperties);
			hArray.add(gTemp);
			pTemp.setProperties(pProperties);
			hArray.add(pTemp);
			hArray.addAll(tempArray);
			serviceConfigInfo.setConfigurations(hArray);
		}
			break;
		case "HBASE": {
			// 1.RegionServer
			ServiceConfigTemp rsTemp = new ServiceConfigTemp(map);
			rsTemp.setType("RegionServer");
			JSONObject rsProperties = rsTemp.getProperties();
			// 2.General
			ServiceConfigTemp gTemp = new ServiceConfigTemp(map);
			gTemp.setType("General");
			JSONObject gProperties = gTemp.getProperties();
			// 过滤配置信息
			JSONArray tempArray = new JSONArray();
			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object object : jsonArray) {
					JSONObject jo = (JSONObject) object;
					String type = jo.getString("type");
					JSONObject joProperties = jo.getJSONObject("properties");
					if (joProperties != null && joProperties.size() > 0) {
						tempArray.add(object);
						if (type.equals("hbase-env")) {
							rsProperties.put("hbase_regionserver_xmn_max",
									joProperties.get("hbase_regionserver_xmn_max"));
							joProperties.remove("hbase_regionserver_xmn_max");
							rsProperties.put("hbase_regionserver_xmn_ratio",
									joProperties.get("hbase_regionserver_xmn_ratio"));
							joProperties.remove("hbase_regionserver_xmn_ratio");
						} else if (type.equals("hbase-site")) {
							gProperties.put("hbase.hstore.compactionThreshold",
									joProperties.get("hbase.hstore.compactionThreshold"));
							joProperties.remove("hbase.hstore.compactionThreshold");
							gProperties.put("hbase.client.scanner.caching",
									joProperties.get("hbase.client.scanner.caching"));
							joProperties.remove("hbase.client.scanner.caching");
						}
					}
				}
			}

			rsTemp.setProperties(rsProperties);
			hArray.add(rsTemp);
			gTemp.setProperties(gProperties);
			hArray.add(gTemp);
			hArray.addAll(tempArray);
			serviceConfigInfo.setConfigurations(hArray);
		}
			break;
		case "ZOOKEEPER": {
			// 1.ZooKeeper Server
			ServiceConfigTemp zsTemp = new ServiceConfigTemp(map);
			zsTemp.setType("ZooKeeper Server");
			JSONObject zsProperties = zsTemp.getProperties();

			// 配置文件过滤
			JSONArray tempArray = new JSONArray();
			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object object : jsonArray) {
					JSONObject jo = (JSONObject) object;
					String type = jo.getString("type");
					JSONObject joProperties = jo.getJSONObject("properties");
					if (joProperties != null && joProperties.size() > 0) {
						tempArray.add(object);
						if (type.equals("zoo.cfg")) {
							zsProperties.put("dataDir", joProperties.get("dataDir"));
							joProperties.remove("dataDir");
							zsProperties.put("tickTime", joProperties.get("tickTime"));
							joProperties.remove("tickTime");
							zsProperties.put("initLimit", joProperties.get("initLimit"));
							joProperties.remove("initLimit");
							zsProperties.put("clientPort", joProperties.get("clientPort"));
							joProperties.remove("clientPort");
							zsProperties.put("syncLimit", joProperties.get("syncLimit"));
							joProperties.remove("syncLimit");
						}
					}
				}
			}
			zsTemp.setProperties(zsProperties);
			hArray.add(zsTemp);
			hArray.addAll(tempArray);
			serviceConfigInfo.setConfigurations(hArray);
		}
			break;
		case "MAPREDUCE2": {
			// 1.History Server
			ServiceConfigTemp hsTemp = new ServiceConfigTemp(map);
			hsTemp.setType("History Server");
			JSONObject hsProperties = hsTemp.getProperties();
			// 配置文件过滤
			JSONArray tempArray = new JSONArray();
			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object object : jsonArray) {
					JSONObject jo = (JSONObject) object;
					String type = jo.getString("type");
					JSONObject joProperties = jo.getJSONObject("properties");
					if (joProperties != null && joProperties.size() > 0) {
						tempArray.add(object);
						if (type.equals("mapred-site")) {

						} else if (type.equals("mapred-env")) {
							hsProperties.put("jobhistory_heapsize", joProperties.get("jobhistory_heapsize"));
							joProperties.remove("jobhistory_heapsize");
						}
					}
				}
			}
			hsTemp.setProperties(hsProperties);
			hArray.add(hsTemp);
			hArray.addAll(tempArray);
			serviceConfigInfo.setConfigurations(hArray);
		}
			break;
		case "TEZ": {
			// 1.General
			ServiceConfigTemp gTemp = new ServiceConfigTemp(map);
			gTemp.setType("General");
			JSONObject gProperties = gTemp.getProperties();
			// 配置文件过滤
			JSONArray tempArray = new JSONArray();
			if (jsonArray != null && jsonArray.size() > 0) {
				for (Object object : jsonArray) {
					JSONObject jo = (JSONObject) object;
					String type = jo.getString("type");
					JSONObject joProperties = jo.getJSONObject("properties");
					if (joProperties != null && joProperties.size() > 0) {
						tempArray.add(object);
						if (type.equals("tez-site")) {
							gProperties.put("tez.am.launch.cmd-opts", joProperties.get("tez.am.launch.cmd-opts"));
							joProperties.remove("tez.am.launch.cmd-opts");
							gProperties.put("tez.am.launch.env", joProperties.get("tez.am.launch.env"));
							joProperties.remove("tez.am.launch.env");
							gProperties.put("tez.am.log.level", joProperties.get("tez.am.log.level"));
							joProperties.remove("tez.am.log.level");
							gProperties.put("tez.am.resource.memory.mb", joProperties.get("tez.am.resource.memory.mb"));
							joProperties.remove("tez.am.resource.memory.mb");
							gProperties.put("tez.grouping.max-size", joProperties.get("tez.grouping.max-size"));
							joProperties.remove("tez.grouping.max-size");
							gProperties.put("tez.grouping.min-size", joProperties.get("tez.grouping.min-size"));
							joProperties.remove("tez.grouping.min-size");
							gProperties.put("tez.grouping.split-waves", joProperties.get("tez.grouping.split-waves"));
							joProperties.remove("tez.grouping.split-waves");
							gProperties.put("tez.runtime.compress", joProperties.get("tez.runtime.compress"));
							joProperties.remove("tez.runtime.compress");
							gProperties.put("tez.runtime.compress.codec",
									joProperties.get("tez.runtime.compress.codec"));
							joProperties.remove("tez.runtime.compress.codec");
							gProperties.put("tez.task.launch.cmd-opts", joProperties.get("tez.task.launch.cmd-opts"));
							joProperties.remove("tez.task.launch.cmd-opts");
							gProperties.put("tez.task.launch.env", joProperties.get("tez.task.launch.env"));
							joProperties.remove("tez.task.launch.env");
							gProperties.put("tez.task.resource.memory.mb",
									joProperties.get("tez.task.resource.memory.mb"));
							joProperties.remove("tez.task.resource.memory.mb");
						}
					}
				}
			}
			gTemp.setProperties(gProperties);
			hArray.add(gTemp);
			hArray.addAll(tempArray);
			serviceConfigInfo.setConfigurations(hArray);
		}
			break;
		default:
			break;
		}
		return serviceConfigInfo;
	}

	/**
	 * 
	 * @param serviceName
	 * @param version
	 * @param isCurrent
	 * @return
	 */
	@RequestMapping(value = "ajaxServiceConfig")
	@ResponseBody
	public ServiceConfigInfo ajaxServiceConfigs(
			@RequestParam(value = "serviceName", required = true) String serviceName,
			@RequestParam(value = "version", required = true) String version,
			@RequestParam(value = "isCurrent", required = false) Boolean isCurrent) {
		JSONObject cversionInfo = null;
		try {
			String content = HostsManager.getServiceConfig(ambHost, ambPort, clustername, serviceName.toUpperCase(),
					version, null);
			if (StringUtils.isNotEmpty(content)) {
				Map map = JSONObject.parseObject(content, Map.class);
				JSONArray array = (JSONArray) map.get("items");
				if (array == null) {
					logger.warn("content:" + content);
				} else
					cversionInfo = (JSONObject) array.get(0);
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		ServiceConfigInfo configInfo = handleServiceConf(serviceName.toUpperCase(),
				cversionInfo.toJavaObject(ServiceConfigInfo.class));
		return configInfo;
	}

	/**
	 * 保存集群配置信息
	 * 
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "saveSCInfo")
	@ResponseBody
	public JSONObject saveServiceConfig(@RequestParam(value = "data", required = true) String content,
			@RequestParam(value = "serviceName", required = true) String serviceName) {
		JSONObject jsonObject = new JSONObject();
		try {
			content = StringEscapeUtils.unescapeHtml(content);
			System.out.println("合并前:" + content);
			content = combineConfigInfo(serviceName, content).toJSONString();
			System.out.println("合并后:" + content);
			String result = HostsManager.updateAmbariConfigs(ambHost, ambPort, clustername, content);
			// 配置保存成功后,重启该服务,保证配置的修改生效
			String url = MetricsUtil.returnServicesUrl(ambHost, ambPort, clustername, serviceName);
			String entityString = MetricsUtil.getEntity("stop " + serviceName, null, clustername, serviceName,
					"INSTALLED");
			String msg = HttpTools.getInstance().putContent(url, entityString);
			entityString = MetricsUtil.getEntity("start " + serviceName, null, clustername, serviceName, "STARTED");
			msg = HttpTools.getInstance().putContent(url, entityString);
			jsonObject.put("status", 0);
			jsonObject.put("msg", "保存成功");
		} catch (Exception e) {
			jsonObject.put("status", 1);
			jsonObject.put("msg", "保存失败");
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 保存服务配置信息时,配置信息的合并操作
	 * 
	 * @param serviceName
	 *            服务名称
	 * @param content
	 *            配置信息
	 * @return
	 */
	private JSONObject combineConfigInfo(String serviceName, String content) {
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		JSONObject jsonObject = JSONObject.parseObject(content, JSONObject.class);
		JSONObject clusters = jsonObject.getJSONObject("Clusters");
		if (clusters != null) {
			JSONArray array = clusters.getJSONArray("desired_config");
			if (array != null && array.size() > 0) {
				JSONObject yarnEnv = null;
				JSONObject yarnSite = null;
				JSONObject hdfsSite = null;
				JSONObject hadoopEnv = null;
				JSONObject hiveSite = null;
				JSONObject hiveEnv = null;
				JSONObject hbaseSite = null;
				JSONObject hbaseEnv = null;
				JSONObject mapredEnv = null;
				JSONObject tezSite = null;
				JSONObject zooCfg = null;
				for (Object obj : array) {
					JSONObject jobject = (JSONObject) obj;
					String type = jobject.getString("type");
					switch (serviceName) {
					case "YARN": {
						if (type.equals("yarn-site")) {
							yarnSite = jobject;
						} else if (type.equals("yarn-env")) {
							yarnEnv = jobject;
						}
					}
						break;
					case "HDFS": {
						if (type.equals("hdfs-site")) {
							hdfsSite = jobject;
						} else if (type.equals("hadoop-env")) {
							hadoopEnv = jobject;
						}
					}
						break;
					case "HIVE": {
						if (type.equals("hive-site")) {
							hiveSite = jobject;
						} else if (type.equals("hive-env")) {
							hiveEnv = jobject;
						}
					}
						break;
					case "HBASE": {
						if (type.equals("hbase-site")) {
							hbaseSite = jobject;
						} else if (type.equals("hbase-env")) {
							hbaseEnv = jobject;
						}
					}
						break;
					case "MAPREDUCE2": {
						if (type.equals("mapred-env")) {
							mapredEnv = jobject;
						}
					}
						break;
					case "TEZ": {
						if (type.equals("tez-site")) {
							tezSite = jobject;
						}
					}
						break;
					case "ZOOKEEPER": {
						if (type.equals("zoo.cfg")) {
							zooCfg = jobject;
						}
					}
						break;
					default:
						break;
					}
				}
				for (Object obj : array) {
					JSONObject jobject = (JSONObject) obj;
					String type = jobject.getString("type");
					switch (serviceName) {
					case "YARN": {

						// 1.Resource Manager 2.Node Manager 3.Application
						// Timeline Server 4.General 5.Fault Tolerance
						// 6.Isolation
						JSONObject properties = jobject.getJSONObject("properties");
						if (type.equals("Resource Manager")) {
							// yarn-site（将yarn-site中的属性替换）
							String pram1 = properties.getString("yarn.acl.enable");
							String pram2 = properties.getString("yarn.admin.acl");
							String pram3 = properties.getString("yarn.log-aggregation-enable");
							if (yarnSite != null) {
								JSONObject jo = yarnSite.getJSONObject("properties");
								jo.put("yarn.acl.enable", pram1);
								jo.put("yarn.admin.acl", pram2);
								jo.put("yarn.log-aggregation-enable", pram3);
								yarnSite.put("properties", jo);
							}
							// yarn-env
							String pram4 = properties.getString("resourcemanager_heapsize");
							if (yarnEnv != null) {
								JSONObject jo = yarnEnv.getJSONObject("properties");
								jo.put("resourcemanager_heapsize", pram4);
								yarnEnv.put("properties", jo);
							}
						} else if (type.equals("Node Manager")) {
							// yarn-site
							String pram1 = properties.getString("yarn.nodemanager.aux-services");
							String pram2 = properties.getString("yarn.nodemanager.local-dirs");
							String pram3 = properties.getString("yarn.nodemanager.log-dirs");
							String pram4 = properties.getString("yarn.nodemanager.log.retain-second");
							String pram5 = properties.getString("yarn.nodemanager.remote-app-log-dir");
							String pram6 = properties.getString("yarn.nodemanager.remote-app-log-dir-suffix");
							String pram7 = properties.getString("yarn.nodemanager.vmem-pmem-ratio");
							if (yarnSite != null) {
								JSONObject jo = yarnSite.getJSONObject("properties");
								jo.put("yarn.nodemanager.aux-services", pram1);
								jo.put("yarn.nodemanager.local-dirs", pram2);
								jo.put("yarn.nodemanager.log-dirs", pram3);
								jo.put("yarn.nodemanager.log.retain-second", pram4);
								jo.put("yarn.nodemanager.remote-app-log-dir", pram5);
								jo.put("yarn.nodemanager.remote-app-log-dir-suffix", pram6);
								jo.put("yarn.nodemanager.vmem-pmem-ratio", pram7);
								yarnSite.put("properties", jo);
							}
							// yarn-env
							String pram8 = properties.getString("nodemanager_heapsize");
							if (yarnEnv != null) {
								JSONObject jo = yarnEnv.getJSONObject("properties");
								jo.put("nodemanager_heapsize", pram8);
								yarnEnv.put("properties", jo);
							}
						} else if (type.equals("Application Timeline Server")) {
							// yarn-site
							String pram1 = properties.getString("yarn.timeline-service.address");
							String pram2 = properties.getString("yarn.timeline-service.enabled");
							String pram3 = properties
									.getString("yarn.timeline-service.generic-application-history.store-class");
							String pram4 = properties.getString("yarn.timeline-service.leveldb-state-store.path");
							String pram5 = properties.getString("yarn.timeline-service.leveldb-timeline-store.path");
							String pram6 = properties
									.getString("yarn.timeline-service.leveldb-timeline-store.ttl-interval-ms");
							String pram7 = properties.getString("yarn.timeline-service.state-store-class");
							String pram8 = properties.getString("yarn.timeline-service.store-class");
							String pram9 = properties.getString("yarn.timeline-service.ttl-enable");
							String pram10 = properties.getString("yarn.timeline-service.ttl-ms");
							String pram11 = properties.getString("yarn.timeline-service.webapp.address");
							String pram12 = properties.getString("yarn.timeline-service.webapp.https.address");
							if (yarnSite != null) {
								JSONObject jo = yarnSite.getJSONObject("properties");
								jo.put("yarn.timeline-service.address", pram1);
								jo.put("yarn.timeline-service.enabled", pram2);
								jo.put("yarn.timeline-service.generic-application-history.store-class", pram3);
								jo.put("yarn.timeline-service.leveldb-state-store.path", pram4);
								jo.put("yarn.timeline-service.leveldb-timeline-store.path", pram5);
								jo.put("yarn.timeline-service.leveldb-timeline-store.ttl-interval-ms", pram6);
								jo.put("yarn.timeline-service.state-store-class", pram7);
								jo.put("yarn.timeline-service.store-class", pram8);
								jo.put("yarn.timeline-service.ttl-enable", pram9);
								jo.put("yarn.timeline-service.ttl-ms", pram10);
								jo.put("yarn.timeline-service.webapp.address", pram11);
								jo.put("yarn.timeline-service.webapp.https.address", pram12);
								yarnSite.put("properties", jo);
							}
							// yarn-env
							String pram13 = properties.getString("apptimelineserver_heapsize");
							if (yarnEnv != null) {
								JSONObject jo = yarnEnv.getJSONObject("properties");
								jo.put("apptimelineserver_heapsize", pram13);
								yarnEnv.put("properties", jo);
							}
						} else if (type.equals("General")) {
							// yarn-env
							String pram1 = properties.getString("yarn_heapsize");
							if (yarnEnv != null) {
								JSONObject jo = yarnEnv.getJSONObject("properties");
								jo.put("yarn_heapsize", pram1);
								yarnEnv.put("properties", jo);
							}
						} else if (type.equals("Fault Tolerance")) {
							// yarn-site
							String pram1 = properties.getString("yarn.nodemanager.recovery.enabled");
							String pram2 = properties.getString("yarn.resourcemanager.connect.max-wait.ms");
							String pram3 = properties.getString("yarn.resourcemanager.connect.retry-interval.ms");
							String pram4 = properties.getString("yarn.resourcemanager.ha.enabled");
							String pram5 = properties.getString("yarn.resourcemanager.recovery.enabled");
							String pram6 = properties
									.getString("yarn.resourcemanager.work-preserving-recovery.enabled");
							String pram7 = properties.getString("yarn.resourcemanager.zk-address");
							if (yarnSite != null) {
								JSONObject jo = yarnSite.getJSONObject("properties");
								jo.put("yarn.nodemanager.recovery.enabled", pram1);
								jo.put("yarn.resourcemanager.connect.max-wait.ms", pram2);
								jo.put("yarn.resourcemanager.connect.retry-interval.ms", pram3);
								jo.put("yarn.resourcemanager.ha.enabled", pram4);
								jo.put("yarn.resourcemanager.recovery.enabled", pram5);
								jo.put("yarn.resourcemanager.work-preserving-recovery.enabled", pram6);
								jo.put("yarn.resourcemanager.zk-address", pram7);
								yarnSite.put("properties", jo);
							}
						} else if (type.equals("Isolation")) {
							// yarn-site
							String pram1 = properties.getString("yarn.nodemanager.container-executor.class");
							String pram2 = properties
									.getString("yarn.nodemanager.linux-container-executor.cgroups.hierarchy");
							String pram3 = properties
									.getString("yarn.nodemanager.linux-container-executor.cgroups.mount");
							String pram4 = properties.getString(
									"yarn.nodemanager.linux-container-executor.cgroups.strict-resource-usage");
							String pram5 = properties.getString("yarn.nodemanager.linux-container-executor.group");
							String pram6 = properties
									.getString("yarn.nodemanager.linux-container-executor.resources-handler.class");
							if (yarnSite != null) {
								JSONObject jo = yarnSite.getJSONObject("properties");
								jo.put("yarn.nodemanager.container-executor.class", pram1);
								jo.put("yarn.nodemanager.linux-container-executor.cgroups.hierarchy", pram2);
								jo.put("yarn.nodemanager.linux-container-executor.cgroups.mount", pram3);
								jo.put("yarn.nodemanager.linux-container-executor.cgroups.strict-resource-usage",
										pram4);
								jo.put("yarn.nodemanager.linux-container-executor.group", pram5);
								jo.put("yarn.nodemanager.linux-container-executor.resources-handler.class", pram6);
								yarnSite.put("properties", jo);
							}
						} else if (type.equals("Scheduler")) {
							// String pram1 = properties.getString("Capacity
							// Scheduler");
							//// String pram2 =
							// properties.getString("yarn.resourcemanager.scheduler.class");
							// jobject.put("type", "capacity-scheduler");
							// JSONObject jsonObj = new JSONObject();
							// String str[] = pram1.split("\n");
							// if(str!=null && str.length>0){
							// for (String string : str) {
							// jsonObj.put(string.split("=")[0],
							// string.split("=")[1]);
							// }
							// }
							// jobject.put("properties", jsonObj);
						}
					}
						break;
					case "HDFS": {
						// 1.NameNode 2.DataNode 3.General 4.NFS Gateway
						JSONObject properties = jobject.getJSONObject("properties");
						if (type.equals("NameNode")) {
							String pram1 = properties.getString("namenode_opt_newsize");
							String pram2 = properties.getString("namenode_opt_maxnewsize");
							String pram3 = properties.getString("namenode_opt_permsize");
							String pram4 = properties.getString("namenode_opt_maxpermsize");
							if (hadoopEnv != null) {
								JSONObject jo = hadoopEnv.getJSONObject("properties");
								jo.put("namenode_opt_newsize", pram1);
								jo.put("namenode_opt_maxnewsize", pram2);
								jo.put("namenode_opt_permsize", pram3);
								jo.put("namenode_opt_maxpermsize", pram4);
								hadoopEnv.put("properties", jo);
							}
						} else if (type.equals("DataNode")) {
							String pram1 = properties.getString("dfs.datanode.data.dir.perm");
							if (hdfsSite != null) {
								JSONObject jo = hdfsSite.getJSONObject("properties");
								jo.put("dfs.datanode.data.dir.perm", pram1);
								hdfsSite.put("properties", jo);
							}
						} else if (type.equals("General")) {
							String pram1 = properties.getString("dfs.namenode.accesstime.precision");
							String pram2 = properties.getString("dfs.namenode.checkpoint.period");
							String pram3 = properties.getString("dfs.datanode.du.reserved");
							String pram4 = properties.getString("dfs.replication");
							String pram5 = properties.getString("hadoop_heapsize");
							if (hdfsSite != null) {
								JSONObject jo = hdfsSite.getJSONObject("properties");
								jo.put("dfs.namenode.accesstime.precision", pram1);
								jo.put("dfs.namenode.checkpoint.period", pram2);
								jo.put("dfs.datanode.du.reserved", pram3);
								jo.put("dfs.replication", pram4);
								hdfsSite.put("properties", jo);
							}
							if (hadoopEnv != null) {
								JSONObject jo = hadoopEnv.getJSONObject("properties");
								jo.put("hadoop_heapsize", pram5);
								hadoopEnv.put("properties", jo);
							}
						} else if (type.equals("NFS Gateway")) {
							String pram1 = properties.getString("nfs.file.dump.dir");
							String pram2 = properties.getString("nfs.exports.allowed.hosts");
							if (hdfsSite != null) {
								JSONObject jo = hdfsSite.getJSONObject("properties");
								jo.put("nfs.file.dump.dir", pram1);
								jo.put("nfs.exports.allowed.hosts", pram2);
								hdfsSite.put("properties", jo);
							}
						}
					}
						break;
					case "HIVE": {
						// 1.Hive Metastore 2.General 3.Performance
						JSONObject properties = jobject.getJSONObject("properties");
						if (type.equals("Hive Metastore")) {
							// hive-env
							String pram1 = properties.getString("hive_database");
							String pram2 = properties.getString("hive_database_type");
							if (hiveEnv != null) {
								JSONObject jo = hiveEnv.getJSONObject("properties");
								jo.put("hive_database", pram1);
								jo.put("hive_database_type", pram2);
								hiveEnv.put("properties", jo);
							}
							// hive-site
							String pram3 = properties.getString("ambari.hive.db.schema.name");
							String pram4 = properties.getString("javax.jdo.option.ConnectionUserName");
							String pram5 = properties.getString("javax.jdo.option.ConnectionPassword");
							String pram6 = properties.getString("javax.jdo.option.ConnectionDriverName");
							String pram7 = properties.getString("javax.jdo.option.ConnectionURL");
							if (hiveSite != null) {
								JSONObject jo = hiveSite.getJSONObject("properties");
								jo.put("ambari.hive.db.schema.name", pram3);
								jo.put("javax.jdo.option.ConnectionUserName", pram4);
								jo.put("javax.jdo.option.ConnectionPassword", pram5);
								jo.put("javax.jdo.option.ConnectionDriverName", pram6);
								jo.put("javax.jdo.option.ConnectionURL", pram7);
								hiveSite.put("properties", jo);
							}
						} else if (type.equals("General")) {
							// hive-site
							String pram1 = properties.getString("datanucleus.cache.level2.type");
							String pram2 = properties.getString("hive.compactor.check.interval");
							String pram3 = properties.getString("hive.compactor.delta.num.threshold");
							String pram4 = properties.getString("hive.compactor.delta.pct.threshold");
							String pram5 = properties.getString("hive.compactor.worker.timeout");
							String pram6 = properties.getString("hive.enforce.bucketing");
							String pram7 = properties.getString("hive.exec.dynamic.partition");
							String pram8 = properties.getString("hive.exec.dynamic.partition.mode");
							String pram9 = properties.getString("hive.exec.failure.hooks");
							String pram10 = properties.getString("hive.exec.max.created.files");
							String pram11 = properties.getString("hive.exec.max.dynamic.partitions");
							String pram12 = properties.getString("hive.exec.max.dynamic.partitions.pernode");
							String pram13 = properties.getString("hive.exec.parallel.thread.number");
							String pram14 = properties.getString("hive.exec.post.hooks");
							String pram15 = properties.getString("hive.exec.pre.hooks");
							String pram16 = properties.getString("hive.metastore.uris");
							String pram17 = properties.getString("hive.metastore.warehouse.dir");
							String pram18 = properties.getString("hive.security.authorization.enabled");
							String pram19 = properties.getString("hive.security.authorization.manager");
							String pram20 = properties.getString("hive.security.metastore.authenticator.manager");
							String pram21 = properties.getString("hive.server2.thrift.http.path");
							String pram22 = properties.getString("hive.server2.thrift.http.port");
							String pram23 = properties.getString("hive.server2.thrift.max.worker.threads");
							String pram24 = properties.getString("hive.server2.thrift.port");
							String pram25 = properties.getString("hive.server2.thrift.sasl.qop");
							String pram26 = properties.getString("hive.server2.transport.mode");
							String pram27 = properties.getString("hive.tez.java.opts");
							String pram28 = properties.getString("hive.tez.log.level");
							String pram29 = properties.getString("hive.txn.manager");
							String pram30 = properties.getString("hive.txn.max.open.batch");
							String pram31 = properties.getString("hive.txn.timeout");
							if (hiveSite != null) {
								JSONObject jo = hiveSite.getJSONObject("properties");
								jo.put("datanucleus.cache.level2.type", pram1);
								jo.put("hive.compactor.check.interval", pram2);
								jo.put("hive.compactor.delta.num.threshold", pram3);
								jo.put("hive.compactor.delta.pct.threshold", pram4);
								jo.put("hive.compactor.worker.timeout", pram5);
								jo.put("hive.enforce.bucketing", pram6);
								jo.put("hive.exec.dynamic.partition", pram7);
								jo.put("hive.exec.dynamic.partition.mode", pram8);
								jo.put("hive.exec.failure.hooks", pram9);
								jo.put("hive.exec.max.created.files", pram10);
								jo.put("hive.exec.max.dynamic.partitions", pram11);
								jo.put("hive.exec.max.dynamic.partitions.pernode", pram12);
								jo.put("hive.exec.parallel.thread.number", pram13);
								jo.put("hive.exec.post.hooks", pram14);
								jo.put("hive.exec.pre.hooks", pram15);
								jo.put("hive.metastore.uris", pram16);
								jo.put("hive.metastore.warehouse.dir", pram17);
								jo.put("hive.security.authorization.enabled", pram18);
								jo.put("hive.security.authorization.manager", pram19);
								jo.put("hive.security.metastore.authenticator.manager", pram20);
								jo.put("hive.server2.thrift.http.path", pram21);
								jo.put("hive.server2.thrift.http.port", pram22);
								jo.put("hive.server2.thrift.max.worker.threads", pram23);
								jo.put("hive.server2.thrift.port", pram24);
								jo.put("hive.server2.thrift.sasl.qop", pram25);
								jo.put("hive.server2.transport.mode", pram26);
								jo.put("hive.tez.java.opts", pram27);
								jo.put("hive.tez.log.level", pram28);
								jo.put("hive.txn.manager", pram29);
								jo.put("hive.txn.max.open.batch", pram30);
								jo.put("hive.txn.timeout", pram31);
								hiveSite.put("properties", jo);
							}
						} else if (type.equals("Performance")) {
							// hive-site
							String pram1 = properties.getString("hive.auto.convert.join");
							String pram2 = properties.getString("hive.auto.convert.join.noconditionaltask");
							String pram3 = properties.getString("hive.compute.query.using.stats");
							String pram4 = properties.getString("hive.fetch.task.aggr");
							String pram5 = properties.getString("hive.fetch.task.conversion");
							String pram6 = properties.getString("hive.fetch.task.conversion.threshold");
							String pram7 = properties.getString("hive.limit.pushdown.memory.usage");
							String pram8 = properties.getString("hive.map.aggr");
							String pram9 = properties.getString("hive.map.aggr.hash.percentmemory");
							String pram10 = properties.getString("hive.merge.mapfiles");
							String pram11 = properties.getString("hive.merge.mapredfiles");
							String pram12 = properties.getString("hive.merge.orcfile.stripe.level");
							String pram13 = properties.getString("hive.merge.size.per.task");
							String pram14 = properties.getString("hive.merge.smallfiles.avgsize");
							String pram15 = properties.getString("hive.merge.tezfiles");
							String pram16 = properties.getString("hive.optimize.bucketmapjoin.sortedmerge");
							String pram17 = properties.getString("hive.optimize.index.filter");
							String pram18 = properties.getString("hive.optimize.reducededuplication");
							String pram19 = properties.getString("hive.optimize.reducededuplication.min.reducer");
							String pram20 = properties.getString("hive.optimize.sort.dynamic.partition");
							String pram21 = properties.getString("hive.orc.splits.include.file.footer");
							String pram22 = properties.getString("hive.smbjoin.cache.rows");
							String pram23 = properties.getString("hive.stats.autogather");
							String pram24 = properties.getString("hive.tez.auto.reducer.parallelism");
							String pram25 = properties.getString("hive.tez.dynamic.partition.pruning");
							String pram26 = properties.getString("hive.tez.dynamic.partition.pruning.max.data.size");
							String pram27 = properties.getString("hive.tez.dynamic.partition.pruning.max.event.size");
							String pram28 = properties.getString("hive.tez.max.partition.factor");
							String pram29 = properties.getString("hive.tez.min.partition.factor");
							String pram30 = properties.getString("hive.tez.smb.number.waves");
							String pram31 = properties.getString("hive.vectorized.execution.enabled");
							String pram32 = properties.getString("hive.vectorized.execution.reduce.enabled");
							String pram33 = properties.getString("hive.vectorized.groupby.checkinterval");
							String pram34 = properties.getString("hive.vectorized.groupby.flush.percent");
							if (hiveSite != null) {
								JSONObject jo = hiveSite.getJSONObject("properties");
								jo.put("hive.auto.convert.join", pram1);
								jo.put("hive.auto.convert.join.noconditionaltask", pram2);
								jo.put("hive.compute.query.using.stats", pram3);
								jo.put("hive.fetch.task.aggr", pram4);
								jo.put("hive.fetch.task.conversion", pram5);
								jo.put("hive.fetch.task.conversion.threshold", pram6);
								jo.put("hive.limit.pushdown.memory.usage", pram7);
								jo.put("hive.map.aggr", pram8);
								jo.put("hive.map.aggr.hash.percentmemory", pram9);
								jo.put("hive.merge.mapfiles", pram10);
								jo.put("hive.merge.mapredfiles", pram11);
								jo.put("hive.merge.orcfile.stripe.level", pram12);
								jo.put("hive.merge.size.per.task", pram13);
								jo.put("hive.merge.smallfiles.avgsize", pram14);
								jo.put("hive.merge.tezfiles", pram15);
								jo.put("hive.optimize.bucketmapjoin.sortedmerge", pram16);
								jo.put("hive.optimize.index.filter", pram17);
								jo.put("hive.optimize.reducededuplication", pram18);
								jo.put("hive.optimize.reducededuplication.min.reducer", pram19);
								jo.put("hive.optimize.sort.dynamic.partition", pram20);
								jo.put("hive.orc.splits.include.file.footer", pram21);
								jo.put("hive.smbjoin.cache.rows", pram22);
								jo.put("hive.stats.autogather", pram23);
								jo.put("hive.tez.auto.reducer.parallelism", pram24);
								jo.put("hive.tez.dynamic.partition.pruning", pram25);
								jo.put("hive.tez.dynamic.partition.pruning.max.data.size", pram26);
								jo.put("hive.tez.dynamic.partition.pruning.max.event.size", pram27);
								jo.put("hive.tez.max.partition.factor", pram28);
								jo.put("hive.tez.min.partition.factor", pram29);
								jo.put("hive.tez.smb.number.waves", pram30);
								jo.put("hive.vectorized.execution.enabled", pram31);
								jo.put("hive.vectorized.execution.reduce.enabled", pram32);
								jo.put("hive.vectorized.groupby.checkinterval", pram33);
								jo.put("hive.vectorized.groupby.flush.percent", pram34);
								hiveSite.put("properties", jo);
							}
						}
					}
						break;
					case "HBASE": {
						// 1.RegionServer 2.General
						JSONObject properties = jobject.getJSONObject("properties");
						if (type.equals("RegionServer")) {
							String pram1 = properties.getString("hbase_regionserver_xmn_max");
							String pram2 = properties.getString("hbase_regionserver_xmn_ratio");
							// hbase-env
							if (hbaseEnv != null) {
								JSONObject jo = hbaseEnv.getJSONObject("properties");
								jo.put("hbase_regionserver_xmn_max", pram1);
								jo.put("hbase_regionserver_xmn_ratio", pram2);
								hbaseEnv.put("properties", jo);
							}
						} else if (type.equals("General")) {
							String pram1 = properties.getString("hbase.hstore.compactionThreshold");
							String pram2 = properties.getString("hbase.client.scanner.caching");
							// hbase-site
							if (hbaseSite != null) {
								JSONObject jo = hbaseSite.getJSONObject("properties");
								jo.put("hbase_regionserver_xmn_max", pram1);
								jo.put("hbase_regionserver_xmn_ratio", pram2);
								hbaseSite.put("properties", jo);
							}
						}
					}
						break;
					case "MAPREDUCE2": {
						// 1.History Server
						JSONObject properties = jobject.getJSONObject("properties");
						if (type.equals("History Server")) {
							String pram1 = properties.getString("jobhistory_heapsize");
							// mapred-env
							if (mapredEnv != null) {
								JSONObject jo = mapredEnv.getJSONObject("properties");
								jo.put("hbase_regionserver_xmn_max", pram1);
								mapredEnv.put("properties", jo);
							}
						}
					}
						break;
					case "TEZ": {
						// 1.General
						JSONObject properties = jobject.getJSONObject("properties");
						if (type.equals("General")) {
							String pram1 = properties.getString("tez.am.launch.cmd-opts");
							String pram2 = properties.getString("tez.am.launch.env");
							String pram3 = properties.getString("tez.am.log.level");
							String pram4 = properties.getString("tez.am.resource.memory.mb");
							String pram5 = properties.getString("tez.grouping.max-size");
							String pram6 = properties.getString("tez.grouping.min-size");
							String pram7 = properties.getString("tez.grouping.split-waves");
							String pram8 = properties.getString("tez.runtime.compress");
							String pram9 = properties.getString("tez.runtime.compress.codec");
							String pram10 = properties.getString("tez.task.launch.cmd-opts");
							String pram11 = properties.getString("tez.task.launch.env");
							String pram12 = properties.getString("tez.task.resource.memory.mb");
							if (tezSite != null) {
								JSONObject jo = tezSite.getJSONObject("properties");
								jo.put("tez.am.launch.cmd-opts", pram1);
								jo.put("tez.am.launch.env", pram2);
								jo.put("tez.am.log.level", pram3);
								jo.put("tez.am.resource.memory.mb", pram4);
								jo.put("tez.grouping.max-size", pram5);
								jo.put("tez.grouping.min-size", pram6);
								jo.put("tez.grouping.split-waves", pram7);
								jo.put("tez.runtime.compress", pram8);
								jo.put("tez.runtime.compress.codec", pram9);
								jo.put("tez.task.launch.cmd-opts", pram10);
								jo.put("tez.task.launch.env", pram11);
								jo.put("tez.task.resource.memory.mb", pram12);
								tezSite.put("properties", jo);
							}
						}
					}
						break;
					case "ZOOKEEPER": {
						// 1.ZooKeeper Server
						JSONObject properties = jobject.getJSONObject("properties");
						if (type.equals("ZooKeeper Server")) {
							String pram1 = properties.getString("dataDir");
							String pram2 = properties.getString("tickTime");
							String pram3 = properties.getString("initLimit");
							String pram4 = properties.getString("clientPort");
							String pram5 = properties.getString("syncLimit");
							if (zooCfg != null) {
								JSONObject jo = zooCfg.getJSONObject("properties");
								jo.put("dataDir", pram1);
								jo.put("tickTime", pram2);
								jo.put("initLimit", pram3);
								jo.put("clientPort", pram4);
								jo.put("syncLimit", pram5);
								zooCfg.put("properties", jo);
							}
						}
					}
						break;
					default:
						break;
					}
				}
				JSONArray reArray = new JSONArray();
				for (Object obj : array) {
					JSONObject jobject = (JSONObject) obj;
					String type = jobject.getString("type");
					switch (serviceName) {
					case "YARN": {
						if (type.equals("yarn-site")) {
							reArray.add(yarnSite);
						} else if (type.equals("yarn-env")) {
							reArray.add(yarnEnv);
						} else if (!type.equals("Resource Manager") && !type.equals("Node Manager")
								&& !type.equals("Application Timeline Server") && !type.equals("General")
								&& !type.equals("Fault Tolerance") && !type.equals("Isolation")) {
							reArray.add(jobject);
						}
					}
						break;
					case "HDFS": {
						if (type.equals("hdfs-site")) {
							reArray.add(hdfsSite);
						} else if (type.equals("hadoop-env")) {
							reArray.add(hadoopEnv);
						} else if (!type.equals("NameNode") && !type.equals("DataNode") && !type.equals("General")
								&& !type.equals("NFS Gateway")) {
							reArray.add(jobject);
						}
					}
						break;
					case "HIVE": {
						if (type.equals("hive-site")) {
							reArray.add(hiveSite);
						} else if (type.equals("hive-env")) {
							reArray.add(hiveEnv);
						} else if (!type.equals("Hive Metastore") && !type.equals("General")
								&& !type.equals("Performance")) {
							reArray.add(jobject);
						}
					}
						break;
					case "HBASE": {
						if (type.equals("hbase-site")) {
							reArray.add(hbaseSite);
						} else if (type.equals("hbase-env")) {
							reArray.add(hbaseEnv);
						} else if (!type.equals("RegionServer") && !type.equals("General")) {
							reArray.add(jobject);
						}
					}
						break;
					case "MAPREDUCE2": {
						if (type.equals("mapred-env")) {
							reArray.add(mapredEnv);
						} else if (!type.equals("History Server")) {
							reArray.add(jobject);
						}
					}
						break;
					case "TEZ": {
						if (type.equals("tez-site")) {
							reArray.add(tezSite);
						} else if (!type.equals("General")) {
							reArray.add(jobject);
						}
					}
						break;
					case "ZOOKEEPER": {
						if (type.equals("zoo.cfg")) {
							reArray.add(zooCfg);
						} else if (!type.equals("ZooKeeper Server")) {
							reArray.add(jobject);
						}
					}
						break;
					default:
						break;
					}
				}
				clusters.put("desired_config", reArray);
			}
		}
		jsonObject.put("Clusters", clusters);
		return jsonObject;
	}

	@RequestMapping(value = "saveHdfsConfig")
	@ResponseBody
	public JSONObject saveHdfsConfig(@RequestParam(value = "data", required = true) String content) {
		JSONObject jsonObject = new JSONObject();
		return jsonObject;
	}

	/**
	 * 切换配置信息到之前的版本
	 * 
	 * @param serviceName
	 *            服务名称
	 * @param version
	 *            版本号
	 * @param versionNote
	 *            该版本备注信息
	 * @return 结果
	 */
	@RequestMapping(value = "saveSCByVersion")
	@ResponseBody
	public JSONObject saveServiceConfigByV(@RequestParam(value = "serviceName", required = true) String serviceName,
			@RequestParam(value = "version", required = true) String version,
			@RequestParam(value = "versionNote", required = true) String versionNote) {
		JSONObject jsonObject = new JSONObject();
		try {
			HostsManager.rollbackServiceConfig(ambHost, ambPort, clustername, serviceName, version, versionNote);
			jsonObject.put("status", 0);
			jsonObject.put("msg", "操作成功");
		} catch (IOException e) {
			jsonObject.put("status", 1);
			jsonObject.put("msg", "操作失败");
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static void main(String[] args) {
		// long l = 1496283495565L;
		// long c = ((new Date().getTime())-l)/(1000*3600*24);
		// System.out.println(c);
		// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		// System.out.println(simpleDateFormat.format(new Date(l)));
		// String href =
		// "http://172.18.84.67:8080/api/v1/clusters/xdata2/requests?to=end&page_size=10&fields=Requests&_=1495010328320";
		// System.out.println(href.substring(href.indexOf("/")+2,
		// href.lastIndexOf(":")));
	}

}