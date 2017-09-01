package cn.vigor.API.hosts;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.API.model.host.HostComponent;
import cn.vigor.API.model.host.HostList;
import cn.vigor.API.model.metrics.common.Path;
import cn.vigor.API.model.metrics.common.Value;
import cn.vigor.API.util.Handler;
import cn.vigor.API.util.HostsHandler;
import cn.vigor.API.util.HttpTools;
import cn.vigor.API.util.JSONTools;

public class HostsManager {
	
	
	/**
	 * 列出所有的host
	 * @param host
	 * @param port
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static HostList listHosts(String host,String port) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = HostsHandler.returnListHostsUrl(host, port);
		String content = HttpTools.getInstance().getContent(url);
		HostList hostList = parserHostList(content);
		return hostList;
	}
	
	/**
	 * 添加新host
	 * @param host
	 * @param port
	 * @param clustername
	 * @param newhost
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static String addHosts(String host,String port,String clustername,String newhost) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = HostsHandler.returnAddHostsUrl(host, port, clustername, newhost);
		String content = HttpTools.getInstance().getContent(url);
		return content;
	}
	
	
	/**
	 * 获取对应主机的组件列表
	 * @param host
	 * @param port
	 * @param clustername
	 * @param targethost
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static HostComponent listHostComponents(String host,String port,String clustername,String targethost) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = HostsHandler.returnListComponentsUrl(host, port, clustername,targethost);
		String content = HttpTools.getInstance().getContent(url);
		HostComponent hostComponent = parserHostComponents(content);
		return hostComponent;
	}
	
	
	/**
	 * 获取host接口相关指标
	 * @param host
	 * @param port
	 * @param clustername
	 * @param targethost
	 * @param hostURLs
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static Map<String,String>  getHostMetric(String host,String port,String clustername,String targethost,List<Path> hostURLs) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url =  Handler.returnHostUrl(host, port, targethost, clustername, hostURLs);
		 String content =  HttpTools.getInstance().getContent(url);
		 Map map =  JSONTools.fromJson(content,HashMap.class);
		 Map<String,String> mr = Handler.handleResult(map, hostURLs);
		 return mr;
	}
	
	
	/**
	 * 获取host接口相关指标
	 * @param host
	 * @param port
	 * @param clustername
	 * @param targethost
	 * @param hostURLs
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static Map<String,List<Value>>  getRangeHostMetric(String host,String port,String clustername,String targethost,List<Path> hostURLs) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url =  Handler.returnRangeHostUrl(host, port, targethost, clustername, hostURLs);
		 String content =  HttpTools.getInstance().getContent(url);
		 Map map =  JSONTools.fromJson(content,HashMap.class);
		 Map<String,List<Value>> mr = Handler.handleListResult(map, hostURLs);
		 return mr;
	}
	

    /**
     * 获取host接口相关指标
     * @param host
     * @param port
     * @param clustername
     * @param targethost
     * @param hostURLs
     * @return
     * @throws ClientProtocolException
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String getHostMetricJson(String host,String port,String clustername,String targethost,List<Path> hostURLs) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
        String url =  Handler.returnRangeHostUrl(host, port, targethost, clustername, hostURLs);
         String content =  HttpTools.getInstance().getContent(url);
       
         return content;
    }
    
	
	private static HostComponent parserHostComponents(String content){
		HostComponent hostComponent = JSONTools.fromJson(content, HostComponent.class);
		return hostComponent;
	}
	
	private static HostList parserHostList(String content){
		HostList hostList = JSONTools.fromJson(content, HostList.class);
		return hostList;
	}
	
	public static String getAlerts(String host,String port,String clustername,Map<String,String> params) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String alertsUrl = Handler.returnAlertsUrl(host, port, clustername, params);
		String content =  HttpTools.getInstance().getContent(alertsUrl);
		return content;
	}
	
	public static String createComponent(String host,String port,String clustername,String hostName,String componentName) throws ClientProtocolException, UnsupportedEncodingException, IOException{
		String url = Handler.returnCreateComponentUrl(host, port, clustername, hostName, componentName);
		String content = HttpTools.getInstance().postContent(url);
		return content;
	}
	
	public static String removeComponent(String host,String port,String clustername,String hostName,String componentName) throws ClientProtocolException, UnsupportedEncodingException, IOException, URISyntaxException{
		String url = Handler.returnCreateComponentUrl(host, port, clustername, hostName, componentName.toUpperCase());
		String content = HttpTools.getInstance().delete(url);
		return content;
	}
	
	public static String getHostComponentsDetail(String host,String port,String clustername,String hostName) throws ClientProtocolException, UnsupportedEncodingException, IOException, URISyntaxException{
		String url = "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+hostName+"?fields=host_components/HostRoles/display_name";
		String content = HttpTools.getInstance().getContent(url);
		return content;
	}
	
	public static String getHostsDiskInfo(String host,String port,String clustername) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		return HttpTools.getInstance().getContent(Handler.returnHostsDiskInfoUrl(host, port, clustername)); 
	}
	
	public static String getServiceComponents(String host,String port,String clustername) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		return HttpTools.getInstance().getContent(Handler.returnServiceComponentsUrl(host, port, clustername));
	}
	
	public static String getServiceConfig(String host,String port,String clustername,String serviceName,String version,Boolean isCurrent) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String content = HttpTools.getInstance().getContent(Handler.returnServiceConfigUrl(host, port, clustername, serviceName, version, isCurrent));
		return content;
	}
	
	public static String rollbackServiceConfig(String host,String port,String clustername,String serviceName,String version,String versionNote) throws ClientProtocolException, UnsupportedEncodingException, IOException{
		String url = "http://" + host + ":" + port + "/api/v1/clusters/" + clustername;
		Map<String,Object> mp = new HashMap<String,Object>();
		Map<String,Object> mp1 = new HashMap<String,Object>();
		Map<String,Object> mp2 = new HashMap<String,Object>();
		mp1.put("service_name", serviceName);
		mp1.put("service_config_version", version);
		mp1.put("service_config_version_note", versionNote);
		mp2.put("desired_service_config_versions",mp1);
		mp.put("Clusters", mp2);
		String content = HttpTools.getInstance().putContent(url, JSONObject.toJSONString(mp));
		return content;
	}
	
	public static String updateAmbariConfigs(String host,String port,String clustername,String content) throws ClientProtocolException, UnsupportedEncodingException, IOException{
		String url = "http://" + host + ":" + port + "/api/v1/clusters/" + clustername;
		String result = HttpTools.getInstance().putContent(url, content);
		return result;
	}
	
	public static String getHostComponents(String host,String port,String clustername) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String str = HttpTools.getInstance().getContent(Handler.returnHostComponentsUrl(host, port, clustername));
		return str;
	}
	
	public static String getHdfsNameNodeStatus(String host,String port,String clustername) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = "http://" + host + ":" + port + "/api/v1/clusters/" + clustername+"/host_components?HostRoles/component_name=NAMENODE&metrics/dfs/FSNamesystem/HAState.in(standby,active)";
		String content = HttpTools.getInstance().getContent(url);
		return content;
	}
	
	public static String getHbaseMasterStatus(String host,String port,String clustername) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = "http://" + host + ":" + port + "/api/v1/clusters/" + clustername+"/host_components?HostRoles/component_name=HBASE_MASTER&metrics/hbase/master/IsActiveMaster.in(true,false)";
		String content = HttpTools.getInstance().getContent(url);
		return content;
	}
	
	public static String getYarnResourceManagerStatus(String host,String port,String clustername) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = "http://" + host + ":" + port + "/api/v1/clusters/" + clustername+"/host_components?HostRoles/component_name=RESOURCEMANAGER&HostRoles/ha_state.in(STANDBY,ACTIVE)";
		String content = HttpTools.getInstance().getContent(url);
		return content;
	}
	
	public static void main(String[] args) {
		try {
			String s = rollbackServiceConfig("172.18.84.67", "8080", "xdata2", "SQOOP", "1", "make v1 config note");
			System.out.println(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
