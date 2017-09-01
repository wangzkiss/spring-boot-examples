package cn.vigor.modules.server.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.iim.utils.DateUtil;
import cn.vigor.modules.server.entity.Alerts;
import cn.vigor.modules.server.entity.ClusterServices;
import cn.vigor.modules.server.entity.Platform;
import cn.vigor.modules.server.entity.PlatformNode;
import cn.vigor.modules.server.entity.ServerInfos;
import cn.vigor.modules.server.service.PlatformService;
import cn.vigor.modules.server.service.ServerInfosService;
import cn.vigor.modules.server.util.AmbariComStatusUtil;
import cn.vigor.modules.server.util.MetricsUtil;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 物理节点Controller
 * 
 * @author kiss
 * @version 2016-06-21
 */
@Controller
@RequestMapping(value = "${adminPath}/server/serverInfos")
public class ServerInfosController extends BaseController {

	@Autowired
	private ServerInfosService serverInfosService;

	@Autowired
	private PlatformService platformService;

	@ModelAttribute
	public ServerInfos get(@RequestParam(required = false) String id) {
		ServerInfos entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = serverInfosService.get(id);
			// 获取硬盘信息
			String value = getMetitcValue("metrics/disk", entity.getHostName());
			if (StringUtils.isNotEmpty(value)) {
				value = value.replace("=", ":");
				Map<String, Object> map = JSONObject.parseObject(value, Map.class);
				String disk_total = map.get("disk_total").toString();
				String disk_free = map.get("disk_free").toString();
				double used = Double.valueOf(disk_total) - Double.valueOf(disk_free);
				double diskusedBit = used * 100 / Double.valueOf(disk_total);
				DecimalFormat df = new DecimalFormat("#.00");
				double dd = Double.valueOf(df.format(diskusedBit));
				entity.setDiskUsedBit(dd);
				// 根据要求,保留一位小数
				DecimalFormat df2 = new DecimalFormat("#.0");
				entity.setDiskTotal(df2.format(Double.valueOf(disk_total)));
				entity.setDiskFree(df2.format(Double.valueOf(disk_free)));
			} else {
				logger.warn("监控信息缺少！ambri metrics组件可能缺少！");
			}
		}
		if (entity == null) {
			entity = new ServerInfos();
		}
		return entity;
	}

	/**
	 * 查看，增加，编辑物理节点信息表单页面
	 */
	@RequiresPermissions(value = { "server:serverInfos:view", "server:serverInfos:add",
			"server:serverInfos:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(ServerInfos serverInfos, Model model) {
		model.addAttribute("serverInfos", serverInfos);
		return "modules/server/serverInfoAdd";
	}

	/**
	 * 保存物理节点信息
	 */
	@RequiresPermissions(value = { "server:serverInfos:add", "server:serverInfos:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(ServerInfos serverInfos, Model model, RedirectAttributes redirectAttributes) throws Exception {
		String msg = "";
		String sshKey = serverInfos.getSshKey();
		String userName = serverInfos.getUserName();
		if (serverInfos.getHostName() != null) {
			for (String hostName : serverInfos.getHostName().split(",")) {
				msg = AmbariTools.addServer(hostName, sshKey, userName);
			}
			addMessage(redirectAttributes, msg);
		}
		return "redirect:" + Global.getAdminPath() + "/server/serverInfos/?repage";
	}

	/**
	 * 物理节点信息列表页面
	 */
	@RequiresPermissions("server:serverInfos:list")
	@RequestMapping(value = { "list", "" })
	public String list(ServerInfos serverInfos, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ServerInfos> page = serverInfosService.findPage(new Page<ServerInfos>(request, response), serverInfos);
		// 获取硬盘信息
		if (page.getList() != null && page.getList().size() > 0) {
			List<ServerInfos> list = new ArrayList<ServerInfos>();
			Map<String, String> hostsUsedBitMap = getHostsUsedBit();
			for (ServerInfos sf : page.getList()) {
				if (hostsUsedBitMap.get(sf.getHostName()) != null) {
					sf.setDiskUsedBit(Double.valueOf(hostsUsedBitMap.get(sf.getHostName())));
					sf.setHostStatus(hostsUsedBitMap.get(sf.getHostName() + ":host_status"));
					sf.setDiskTotal(hostsUsedBitMap.get(sf.getHostName() + ":diskTotal"));
					sf.setDiskUsed(hostsUsedBitMap.get(sf.getHostName() + ":diskUsed"));
					list.add(sf);
				}
			}
			page.setList(list);
		}
		model.addAttribute("page", page);
		if (serverInfos != null && serverInfos.getRemarks() != null) {
			model.addAttribute("message", serverInfos.getRemarks());
		}
		return "modules/server/serverInfosList";
	}

	private Map<String, String> getHostsUsedBit() {
		Map<String, String> mp = new HashMap<String, String>();
		try {
			String content = HostsManager.getHostsDiskInfo(ambHost, ambPort, clustername);
			Map<String, Object> map = JSONObject.parseObject(content, Map.class);
			JSONArray jsonArray = (JSONArray) map.get("items");
			if (jsonArray == null) {
				logger.warn("items data is null ! \n" + content);
			} else
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jb = (JSONObject) jsonArray.get(i);
					String hostName = jb.getJSONObject("Hosts").getString("host_name");
					JSONObject mjsonObject = jb.getJSONObject("metrics");
					String diskFree = null;
					String diskTotal = null;
					if (mjsonObject != null) {
						JSONObject dJsonObject = mjsonObject.getJSONObject("disk");
						diskFree = dJsonObject == null ? null : dJsonObject.getString("disk_free");
						diskTotal = dJsonObject == null ? null : dJsonObject.getString("disk_total");
					}
					String hostStatus = jb.getJSONObject("Hosts").getString("host_status");
					mp.put(hostName + ":host_status", hostStatus);
					double used = 0;
					if (StringUtils.isNotEmpty(diskFree) && StringUtils.isNotEmpty(diskTotal)) {
						used = Double.valueOf(diskTotal) - Double.valueOf(diskFree);
					}
					double diskusedBit = 0;
					if (StringUtils.isNotEmpty(diskTotal)) {
						diskusedBit = (used / Double.valueOf(diskTotal)) * 100;
					}
					DecimalFormat df = new DecimalFormat("#.00");
					double dd = Double.valueOf(df.format(diskusedBit));
					mp.put(hostName, String.valueOf(dd));
					mp.put(hostName + ":diskTotal", diskTotal);
					mp.put(hostName + ":diskUsed", String.valueOf(Double.valueOf(df.format(used))));
				}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return mp;
	}

	/**
	 * 物理节点信息列表页面
	 */
	@RequiresPermissions("server:serverInfos:list")
	@RequestMapping(value = "info")
	public String info(ServerInfos serverInfos, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ServerInfos> serverlist = serverInfosService.findList(serverInfos);
		if (serverlist != null && serverlist.size() > 0)
			model.addAttribute("serverInfos", serverlist.get(0));
		return "modules/server/serverInfo";
	}

	/**
	 * 查看，增加，编辑物理节点信息表单页面
	 * 
	 */
	@RequiresPermissions("server:serverInfos:view")
	@RequestMapping(value = "view")
	public String view(ServerInfos serverInfos, Model model) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> scInfoMap = new ArrayList<Map<String, String>>();
		try {
			long end = new Date().getTime() / 1000;
			long start = end - 1800;
			String keys = MetricsUtil.getAllKeys();
			serverInfos.setMetrics(getMetitcValuesJson(keys, start, end, serverInfos.getHostName(), "15"));
			List<Map<String, String>> componetList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> cMaps = serverInfos.getComponetList();
			// 已经添加的服务
			List<String> hasAddComs = new ArrayList<String>();
			Map<String, String> hcd = new HashMap<String, String>();
			String content = HostsManager.getHostComponentsDetail(ambHost, ambPort, clustername,
					serverInfos.getHostName());
			if (content != null) {
				Map<String, Object> mp = JSONObject.parseObject(content, Map.class);
				JSONArray hostComponents = (JSONArray) mp.get("host_components");
				for (int i = 0; i < hostComponents.size(); i++) {
					JSONObject jb = (JSONObject) hostComponents.get(i);
					hcd.put(((JSONObject) jb.get("HostRoles")).get("component_name").toString(),
							((JSONObject) jb.get("HostRoles")).get("display_name").toString());
				}
			}
			if (cMaps != null && cMaps.size() > 0) {
				for (Map<String, String> map : cMaps) {
					String displayComponentName = "";
					String status = map.get("status");
					String componentName = map.get("componentName");
					hasAddComs.add(componentName);
					Iterator iter = hcd.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						Object key = entry.getKey();
						Object val = entry.getValue();
						if (componentName.toUpperCase().equals(key.toString())) {
							displayComponentName = val.toString();
							break;
						}
					}
					map.put("displayName", displayComponentName);
					// 获取components的详情,并获取display_name的值
					if (displayComponentName.toLowerCase().endsWith("client")
							|| componentName.toLowerCase().endsWith("_client")) {
						list.add(map);
					} else if (status.equals("install_failed") && componentName.toLowerCase().endsWith("_client")) {
						list.add(map);
					} else {
						componetList.add(map);
					}
				}
				serverInfos.setComponetList(componetList);
			}
			// 获取可安装的服务
			String serviceComStr = HostsManager.getServiceComponents(ambHost, ambPort, clustername);
			Map<String, Object> servicecomsMap = JSONObject.parseObject(serviceComStr, Map.class);
			JSONArray scJsonArray = (JSONArray) servicecomsMap.get("items");
			if (scJsonArray == null) {
				logger.warn("items data is null ! \n" + serviceComStr);
			} else {
				for (int i = 0; i < scJsonArray.size(); i++) {
					JSONArray jsonArray2 = scJsonArray.getJSONObject(i).getJSONArray("components");
					for (int j = 0; j < jsonArray2.size(); j++) {
						String displayName = jsonArray2.getJSONObject(j).getJSONObject("ServiceComponentInfo")
								.getString("display_name");
						String componentName = jsonArray2.getJSONObject(j).getJSONObject("ServiceComponentInfo")
								.getString("component_name");
						String category = jsonArray2.getJSONObject(j).getJSONObject("ServiceComponentInfo")
								.getString("category");
						String totalCount = jsonArray2.getJSONObject(j).getJSONObject("ServiceComponentInfo")
								.getString("total_count");
						if (!hasAddComs.contains(componentName.toLowerCase())) {
							if (!category.equals("MASTER")) {
								Map<String, String> p = new HashMap<String, String>();
								p.put("displayName", displayName);
								p.put("componentName", componentName);
								scInfoMap.add(p);
							} else if (totalCount.equals("0")) {
								Map<String, String> p = new HashMap<String, String>();
								p.put("displayName", displayName);
								p.put("componentName", componentName);
								scInfoMap.add(p);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("unAddSers", scInfoMap);
		model.addAttribute("uninstallSers", list);
		return "modules/server/serverInfosView";
	}

	/**
	 * 
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @param key
	 *            cpu disk network memory
	 * @return
	 */
	@RequiresPermissions("server:serverInfos:view")
	@RequestMapping(value = "metrics")
	@ResponseBody
	public String metrics(String startDate, String endDate, String key, String hostName) {
		long end = new Date().getTime() / 1000 - 600;
		long start = end - 1800;
		if (startDate != null && endDate != null) {
			end = DateUtil.string2long(endDate);
			start = DateUtil.string2long(startDate);
		}
		String keys = MetricsUtil.getKeys(key);
		String content = getMetitcValuesJson(keys, start, end, hostName, "15");
		return content;
	}

	/**
	 * 处理节点服务状态
	 * 
	 * @param type
	 *            1 启动所有服务 2 停止所有服务 3 重启所有服务 4 重装失败服务
	 * @param id
	 *            节点id
	 * @param nodeName
	 *            节点名称
	 * @return 结果
	 */
	@RequestMapping(value = "handleAmrSer")
	@ResponseBody
	public String handleAmbariService(ServerInfos serverInfos, String nodeName, int type) {
		// 过滤节点下安装失败的服务
		List<Map<String, String>> ifaileds = new ArrayList<Map<String, String>>();
		// 过滤节点下启动的服务
		List<Map<String, String>> istarteds = new ArrayList<Map<String, String>>();
		// 过滤节点下安装成功但未启动的服务
		List<Map<String, String>> iinstalleds = new ArrayList<Map<String, String>>();
		List<Map<String, String>> cMaps = serverInfos.getComponetList();
		if (cMaps != null && cMaps.size() > 0) {
			for (Map<String, String> map : cMaps) {
				String status = map.get("status");
				if (status.equals("install_failed")) {
					ifaileds.add(map);
				} else if (status.equals("started")) {
					istarteds.add(map);
				} else if (status.equals("installed")) {
					iinstalleds.add(map);
				}
			}
		}
		String msg = "";
		Map<String, String> mp = new HashMap<String, String>();
		switch (type) {
		case 1:
			// 启动所有服务
			if (iinstalleds.size() > 0) {
				for (Map<String, String> map : iinstalleds) {
					String componentName = map.get("componentName").toUpperCase();
					// 针对于已经安装的client,不需要启动操作,安装即可使用
					if (componentName.contains("CLIENT")) {
						continue;
					}
					String url = MetricsUtil.returnHostUrl(ambHost, ambPort, clustername, serverInfos.getHostName(),
							componentName);
					String entityString = MetricsUtil.getEntity("start " + componentName, serverInfos.getHostName(),
							clustername, map.get("serviceName"), "STARTED");
					try {
						msg = HttpTools.getInstance().putContent(url, entityString);
						// 根据启动服务返回的日志获取服务的状态,成功之后再启动下一个服务
						getComponentStatus(msg, type);
					} catch (Exception e) {
						mp.put("Requests", "启动" + componentName + "失败!");
						e.printStackTrace();
						break;
					}
				}
			}
			if (!mp.containsKey("Requests")) {
				mp.put("Requests", "操作成功!");
			}
			break;
		case 2:
			// 停止所有服务
			if (istarteds.size() > 0) {
				for (Map<String, String> map : istarteds) {
					try {
						String componentName = map.get("componentName").toUpperCase();
						if (componentName.contains("CLIENT")) {
							continue;
						}
						String url = MetricsUtil.returnHostUrl(ambHost, ambPort, clustername, serverInfos.getHostName(),
								componentName);
						String entityString = MetricsUtil.getEntity("stop " + componentName, serverInfos.getHostName(),
								clustername, map.get("serviceName"), "INSTALLED");
						msg = HttpTools.getInstance().putContent(url, entityString);
						getComponentStatus(msg, type);
					} catch (Exception e) {
						mp.put("Requests", "操作失败!");
						e.printStackTrace();
						break;
					}
				}
			}
			if (!mp.containsKey("Requests")) {
				mp.put("Requests", "操作成功!");
			}
			break;
		case 3:
			// 重启所有服务(先stop,然后start)
			if (istarteds.size() > 0) {
				for (Map<String, String> map : istarteds) {
					try {
						String componentName = map.get("componentName").toUpperCase();
						if (componentName.contains("CLIENT")) {
							continue;
						}
						String hostName = serverInfos.getHostName();
						String serviceName = map.get("serviceName").toUpperCase();
						String url = MetricsUtil.returnRestartComponentUrl(ambHost, ambPort, clustername);
						if (StringUtils.isNotEmpty(componentName) && StringUtils.isNotEmpty(hostName)
								&& StringUtils.isNotEmpty(serviceName)) {
							String entityString = "{\"RequestInfo\":{\"command\":\"RESTART\",\"context\":\"Restart components with Stale Configs on "
									+ hostName + "\"},\"Requests/resource_filters\":[{\"service_name\":\""
									+ serviceName.toUpperCase() + "\",\"component_name\":\""
									+ componentName.toUpperCase() + "\",\"hosts\":\"" + hostName + "\"}]}";
							msg = HttpTools.getInstance().postContent(url, entityString);
							getComponentStatus(msg, type);
						}
					} catch (Exception e) {
						mp.put("Requests", "操作失败!");
						e.printStackTrace();
						break;
					}
				}
			}
			if (!mp.containsKey("Requests")) {
				mp.put("Requests", "操作成功!");
			}
			break;
		case 4:
			// 重装失败服务
			if (ifaileds.size() > 0) {
				for (Map<String, String> map : ifaileds) {
					try {
						String componentName = map.get("componentName").toUpperCase();
						msg = HostsManager.createComponent(ambHost, ambPort, clustername, serverInfos.getHostName(),
								componentName);
						String state = "INSTALLED";
						String url = MetricsUtil.returnHostUrl(ambHost, ambPort, clustername, serverInfos.getHostName(),
								map.get("componentName").toUpperCase());
						String entityString = MetricsUtil.getEntity(null, serverInfos.getHostName(), clustername,
								map.get("componentName").toUpperCase(), state);
						msg = HttpTools.getInstance().putContent(url, entityString);
						getComponentStatus(msg, type);
					} catch (Exception e) {
						mp.put("Requests", "操作失败!");
						e.printStackTrace();
						break;
					}
				}
			}
			if (!mp.containsKey("Requests")) {
				mp.put("Requests", "操作成功!");
			}
			break;
		default:
			break;
		}
		return JSONObject.toJSONString(mp);
	}

	/**
	 * 获取组件的状态
	 * 
	 * @param content
	 *            服务操作之后返回的信息(添加,停止,启动,重启)
	 * @throws Exception
	 */
	private void getComponentStatus(String content, int type) throws Exception {
		String status = null;
		JSONObject object = JSONObject.parseObject(content);
		System.out.println(object);
		String ourl = object.getString("href");
		String taskObject = HttpTools.getInstance().getContent(ourl);
		if (taskObject != null) {
			JSONObject jo = JSONObject.parseObject(taskObject);
			String href = jo.getJSONArray("tasks").getJSONObject(0).getString("href");
			href = href + "?fields=Tasks/status";
			String msg = HttpTools.getInstance().getContent(href);
			if (StringUtils.isNotEmpty(msg)) {
				status = JSONObject.parseObject(msg).getJSONObject("Tasks").getString("status");
			}
		}
		if (status != null) {
			switch (type) {
			case 1: {
				if (AmbariComStatusUtil.STARTING.equals(status)) {
					Thread.sleep(2000);
					getComponentStatus(content, type);
				}
			}
				break;
			case 2: {
				if (AmbariComStatusUtil.STOPPING.equals(status)) {
					Thread.sleep(2000);
					getComponentStatus(content, type);
				}
			}
				break;
			case 4: {
				if (AmbariComStatusUtil.INSTALLING.equals(status)) {
					Thread.sleep(2000);
					getComponentStatus(content, type);
				}
			}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 通知列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "alerts")
	public String alerts(HttpServletRequest request, HttpServletResponse response, Model model, Alerts alerts) {
		try {
			Page<Alerts> page = new Page<Alerts>(request, response);
			int pageNo = page.getPageNo();
			Map<String, String> params = new HashMap<String, String>();
			if (StringUtils.isNotEmpty(alerts.getHost_name())) {
				params.put("host_name", alerts.getHost_name());
			}
			if (StringUtils.isNotEmpty(alerts.getService_name()) && !alerts.getService_name().equals("All")) {
				params.put("service_name", alerts.getService_name());
			}
			if (StringUtils.isNotEmpty(alerts.getState()) && !alerts.getState().equals("All")) {
				params.put("state", alerts.getState());
			}
			String content = HostsManager.getAlerts(ambHost, ambPort, clustername, params);
			List<Alerts> list = new ArrayList<Alerts>();
			if (content != null) {
				Map<String, Object> map = JSONObject.parseObject(content, Map.class);
				JSONArray object = (JSONArray) map.get("items");
				if (object != null && object.size() > 0) {
					for (Object obj : object) {
						JSONObject jb = (JSONObject) obj;
						Alerts alert = JSONObject.parseObject(jb.get("Alert").toString(), Alerts.class);
						list.add(alert);
					}
					page.setCount(list.size());
					if (list.size() < page.getPageSize()) {
						page.setList(list);
					} else {
						List<Alerts> list2 = new ArrayList<Alerts>();
						if (pageNo > 0) {
							pageNo = pageNo - 1;
						}
						int end = pageNo * page.getPageSize() + page.getPageSize();
						if (list.size() < end) {
							end = list.size();
						}
						for (int i = pageNo * page.getPageSize(); i < end; i++) {
							JSONObject jb = (JSONObject) object.get(i);
							Alerts alert = JSONObject.parseObject(jb.get("Alert").toString(), Alerts.class);
							list2.add(alert);
						}
						page.setList(list2);
					}
				} else {
					logger.warn("items data is null ! \n" + content);
				}
			}
			List<ClusterServices> clusterServices = serverInfosService.findAllClusterServices();
			model.addAttribute("page", page);
			model.addAttribute("clusterServices", clusterServices);
			model.addAttribute("alerts", alerts);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "modules/server/notificationList";
	}

	/**
	 * 创建component
	 * 
	 * @param serverInfos
	 * @param componentName
	 * @param type
	 *            5 create 4 remove 3 restart
	 * @return
	 */
	@RequestMapping(value = "updateHostComponent")
	@ResponseBody
	public String updateHostComponent(@RequestParam(value = "hostName", required = true) String hostName,
			@RequestParam(value = "componentName", required = true) String componentName,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "serviceName", required = true) String serviceName) {
		String content = null;
		try {
			if (type.equals(5) || type.equals(4)) {
				if (type.equals(5)) {// 创建
					content = HostsManager.createComponent(ambHost, ambPort, clustername, hostName,
							componentName.toUpperCase());
					String state = "INSTALLED";
					String url = MetricsUtil.returnHostUrl(ambHost, ambPort, clustername, hostName,
							componentName.toUpperCase());
					String entityString = MetricsUtil.getEntity(null, hostName, clustername, componentName, state);
					content = HttpTools.getInstance().putContent(url, entityString);
					// 将content中的url替换成获取安装components进度状态的接口url,方便前端页面实时获取安装进度
					JSONObject object = JSONObject.parseObject(content);
					String ourl = object.getString("href");
					String taskObject = HttpTools.getInstance().getContent(ourl);
					if (taskObject != null) {
						JSONObject jo = JSONObject.parseObject(taskObject);
						String href = jo.getJSONArray("tasks").getJSONObject(0).getString("href");
						href = href + "?fields=Tasks/status";
						content = content.replace("\"href\" : \"" + ourl + "\"", "\"href\" : \"" + href + "\"");
					}
					// TODO 添加组件时,需要将Components的信息同步到平台的集群和节点表
					createPlatformInfo(hostName, componentName);
				} else if (type.equals(4)) {// 移除
					content = HostsManager.removeComponent(ambHost, ambPort, clustername, hostName, componentName);
					if (StringUtils.isEmpty(content)) {
						// 防止页面提示不正确
						Map<String, String> p = new HashMap<String, String>();
						p.put("Requests", "操作成功");
						content = JSONObject.toJSONString(p);
					}
				}
			} else if (type.equals(3)) { // 重启
				// 先stop,然后start
				// String cmd = "stop " + componentName;
				// String state = "INSTALLED";
				// String url = MetricsUtil.returnHostUrl(ambHost,
				// ambPort,
				// clustername,
				// hostName,
				// componentName.toUpperCase());
				// String entityString = MetricsUtil.getEntity(cmd,
				// hostName,
				// clustername,
				// componentName,
				// state);
				// content = HttpTools.getInstance().putContent(url,
				// entityString);
				// //调用停止服务后,延迟三秒之后再启用服务(调用太快会导致服务还没停止就开始启用了)
				// Thread.sleep(5000);
				// cmd = "start " + componentName;
				// state = "STARTED";
				// entityString = MetricsUtil.getEntity(cmd,
				// hostName,
				// clustername,
				// componentName,
				// state);
				String url = MetricsUtil.returnRestartComponentUrl(ambHost, ambPort, clustername);
				String entityString = "{\"RequestInfo\":{\"command\":\"RESTART\",\"context\":\"Restart components with Stale Configs on "
						+ hostName + "\"},\"Requests/resource_filters\":[{\"service_name\":\""
						+ serviceName.toUpperCase() + "\",\"component_name\":\"" + componentName.toUpperCase()
						+ "\",\"hosts\":\"" + hostName + "\"}]}";
				content = HttpTools.getInstance().postContent(url, entityString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.isEmpty(content)) {
				content = "操作失败";
			}
		}
		return content;
	}

	/**
	 * 安装ambari服务客户端client
	 * 
	 * @param serviceNames
	 *            服务名称
	 * @param componentNames
	 *            客户端组件名称
	 * @param hostName
	 *            主机名称
	 * @return
	 */
	@RequestMapping(value = "installClients")
	@ResponseBody
	public String installClients(@RequestParam(value = "serviceNames", required = true) String serviceNames,
			String componentNames, String hostName) {
		String content = "";
		String[] sns = {};
		if (serviceNames.contains(",")) {
			sns = serviceNames.split(",");
		} else {
			sns = new String[] { serviceNames };
		}
		for (int i = 0; i < sns.length; i++) {
			try {
				content = HostsManager.createComponent(ambHost, ambPort, clustername, hostName,
						componentNames.split(",")[i].toUpperCase());
				String state = "INSTALLED";
				String url = MetricsUtil.returnHostUrl(ambHost, ambPort, clustername, hostName,
						componentNames.split(",")[i].toUpperCase());
				String entityString = MetricsUtil.getEntity(null, hostName, clustername, sns[i].toUpperCase(), state);
				content = HttpTools.getInstance().putContent(url, entityString);
				getComponentStatus(content, 4);
				content = "安装成功";
			} catch (Exception e) {
				e.printStackTrace();
				content = "安装" + componentNames.split(",")[i].toUpperCase() + "失败";
				return content;
			}
		}
		return JSONObject.toJSONString(content);
	}

	/**
	 * 同步到平台的集群和节点表
	 * 
	 * @param hostName
	 *            主机名称
	 * @param componentName
	 *            组件名称
	 */
	private void createPlatformInfo(String hostName, String componentName) {
		ServerInfos serverInfos = serverInfosService.findHostInfoByHostName(hostName);
		if (serverInfos != null) {
			Platform platform = platformService.getOne(componentName);
			Date date = new Date();
			if (platform == null) {
				platform = new Platform();
				platform.setPlatformIp(serverInfos.getIpv4());
				platform.setPlatformName(componentName);
				// platform.setPlatformPort(platformPort);//TODO 端口未知
				platform.setPlatformState(1);
				platform.setPlatformType(3);
				platform.setCreateDate(date);
				platform.setCurrentUser(UserUtils.getUser());
				platform.setPlatformUrl("");
				platformService.save(platform);
				PlatformNode platformNode = new PlatformNode();
				platformNode.setCreateDate(date);
				platformNode.setUpdateDate(date);
				platformNode.setNodeIp(serverInfos.getIpv4());
				platformNode.setNodeName(hostName);
				platformNode.setNodeState(1);
				platformNode.setPhysicalId(serverInfos.getId());
				platformNode.setNodeType(2);
				platformNode.setCreateBy(UserUtils.getUser());
				platformNode.setPlatformId(platform);
				platformService.savePlatformNode(platformNode);
			} else {
				platform = platformService.get(platform.getId());
				PlatformNode platformNode = null;
				List<PlatformNode> list = platform.getPlatformNodeList();
				boolean bol = true;
				if (list != null && list.size() > 0) {
					for (PlatformNode pn : list) {
						if (pn.getNodeName().equals(hostName)) {
							platformNode = pn;
							platformNode.setUpdateDate(date);
							platformNode.setUpdateBy(UserUtils.getUser());
							bol = false;
							break;
						}
					}
				}
				if (bol) {
					platformNode = new PlatformNode();
					platformNode.setCreateDate(date);
					platformNode.setUpdateDate(date);
					platformNode.setNodeIp(serverInfos.getIpv4());
					platformNode.setNodeName(hostName);
					platformNode.setNodeState(1);
					platformNode.setPhysicalId(serverInfos.getId());
					platformNode.setNodeType(2);
					platformNode.setCreateBy(UserUtils.getUser());
					platformNode.setPlatformId(platform);
				}
				platformService.savePlatformNode(platformNode);
			}
		}
	}

	/**
	 * 获取单个指标
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// System.out.println("aa.vv".split("\\.")[0]);
			// String taskObject =
			// HttpTools.getInstance().getContent("http://172.18.88.67:8080/api/v1/clusters/xdata2/requests/1471");
			// if(taskObject!=null){
			// JSONObject jo = JSONObject.parseObject(taskObject);
			// String href =
			// jo.getJSONArray("tasks").getJSONObject(0).getString("href");
			// System.out.println(href+"?fields=Tasks/status");
			// }
			// List<String> l = new ArrayList<String>();
			// String hostName = "xdata67";
			// String serviceName = "HBASE";
			// String componentName = "HBASE_REGIONSERVER";
			// String entityString =
			// "{\"RequestInfo\":{\"command\":\"RESTART\",\"context\":\"Restart
			// components with Stale Configs on
			// "+hostName+"\"},\"Requests/resource_filters\":[{\"service_name\":\""+serviceName.toUpperCase()+"\",\"component_name\":\""+componentName.toUpperCase()+"\",\"hosts\":\""+hostName+"\"}]}";
			// System.out.println(entityString);
			// String url =
			// "http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts/xdata69/host_components/FLUME_HANDLER";
			// String str = HttpTools.getInstance().postContent(url);
			// System.out.println(str);
			// DecimalFormat decimalFormat = new DecimalFormat("#.00");
			// double d = Double.valueOf(decimalFormat.format(0.24455555));
			// System.out.println(1-d);
			// System.out.println(Math.round(33f/100));
			// System.out.println(new
			// ServerInfosController().getHostsUsedBit());

			// String s =
			// "http://172.18.84.67:8080/api/v1/clusters/xdata2/services?fields=components/ServiceComponentInfo";
			// String content = HttpTools.getInstance().getContent(s);
			// Map<String,Object> m = JSONObject.parseObject(content,
			// Map.class);
			// JSONArray jsonArray = (JSONArray)m.get("items");
			// for(int i=0;i<jsonArray.size();i++){
			// JSONArray jsonArray2 =
			// jsonArray.getJSONObject(i).getJSONArray("components");
			// for(int j=0;j<jsonArray2.size();j++){
			// String ss =
			// jsonArray2.getJSONObject(j).getJSONObject("ServiceComponentInfo").getString("display_name");
			// System.out.println(ss);
			// }
			// }

			// String content = HttpTools.getInstance().postContent(url);
			// System.out.println(content);
			// String cmd = "start DATANODE";
			// String state = "STARTED";
			// String url = MetricsUtil.returnHostUrl("172.18.84.67",
			// "8080",
			// "xdata2",
			// "xdata68",
			// "FLUME_HANDLER");
			// String entityString = MetricsUtil.getEntity(cmd,
			// "xdata68",
			// "xdata2",
			// "FLUME_HANDLER",
			// state);
			// String s = HttpTools.getInstance().putContent(url, entityString);
			// System.out.println(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ServerInfos serverInfos = new ServerInfos();
		// //
		// long end=new Date().getTime()/1000;
		// System.out.println(end);
		// long start=end-1800;
		// serverInfos.setHostName("xdata67");
		// String cpukeys="：metrics/cpu/cpu_idle";
		// String
		// diskeys="metrics/disk/disk_free,metrics/disk/read_count,metrics/disk/read_bytes,metrics/disk/write_bytes";
		// String
		// networkkeys="metrics/network/bytes_out,metrics/network/bytes_in,metrics/network/pkts_out,metrics/network/pkts_in";
		// String
		// memorykeys="metrics/memory/mem_used,metrics/memory/mem_free,metrics/memory/mem_cached,metrics/memory/swap_free,metrics/memory/swap_total";
		// serverInfos.setCpuValues(new
		// ServerInfosController().getMetitcValuesJson(cpukeys, start, end,
		// serverInfos.getHostName()));
		// serverInfos.setDiscValues(new
		// ServerInfosController().getMetitcValuesJson(diskeys, start, end,
		// serverInfos.getHostName()));
		// serverInfos.setNetworkValues(new
		// ServerInfosController().getMetitcValuesJson(networkkeys, start, end,
		// serverInfos.getHostName()));
		// serverInfos.setMemoryValues(new
		// ServerInfosController().getMetitcValuesJson(memorykeys, start, end,
		// serverInfos.getHostName()));
	}

	/**
	 * 
	 * @param key
	 * @param hostName
	 * @return
	 */
	private String getMetitcValue(String key, String hostName) {
		String value = "";
		List<Path> hostURLs = new ArrayList<Path>();
		Path murl = new Path();
		murl.setPath(key);
		hostURLs.add(murl);
		try {
			Map<String, String> result = HostsManager.getHostMetric(ambHost, ambPort, clustername, hostName, hostURLs);
			System.out.println("getMetitcValue:" + result);
			value = result.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 
	 * @param keys
	 * @param start
	 * @param end
	 * @param hostName
	 * @return
	 */
	private String getMetitcValuesJson(String keys, long start, long end, String hostName, String step) {
		List<Path> hostURLs = new ArrayList<Path>();
		for (String key : keys.split(",")) {
			Path murl = new Path();
			murl.setPath(key);
			murl.setStarttime(start + "");
			murl.setEndtime(end + "");
			murl.setStep(step);
			hostURLs.add(murl);
		}
		try {
			String contents = HostsManager.getHostMetricJson(ambHost, ambPort, clustername, hostName, hostURLs);
			return contents;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Value("${ambr_host}")
	String ambHost = "172.18.84.67";

	@Value("${ambr_port}")
	String ambPort = "8080";

	@Value("${ambr_cluster_name:xdata2}")
	String clustername = "xdata2";

	/**
	 * 查看，增加，编辑物理节点信息表单页面
	 * 
	 */
	@RequiresPermissions("server:serverInfos:view")
	@RequestMapping(value = "clusterIndicators")
	public String clusterIndicators(String serviceName, Model model) {
		return "modules/cluster/clusterIndicators";
	}

}