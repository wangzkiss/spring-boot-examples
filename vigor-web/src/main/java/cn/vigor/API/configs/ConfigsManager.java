package cn.vigor.API.configs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import cn.vigor.API.model.metrics.configs.Config;
import cn.vigor.API.model.metrics.configs.ConfigVersion;
import cn.vigor.API.util.ConfigsHandler;
import cn.vigor.API.util.HttpTools;
import cn.vigor.API.util.JSONTools;

public class ConfigsManager {
	
	/**
	 * 列出所有配置的version和tag
	 * @param host
	 * @param port
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static List<ConfigVersion> listAllConfigVersions(String host,String port) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = ConfigsHandler.returnAllLatestVersionConfigUrl(host, port);
		String content = HttpTools.getInstance().getContent(url);
		List<ConfigVersion> configs = parserLatestVersionConfig(content);
		return configs;
	}
	
	/**
	 * 列出指定配置的version和tag
	 * @param host
	 * @param port
	 * @param names [hdfs-site,mapred-site]
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static List<ConfigVersion> listAllConfigVersionsByNames(String host,String port,List<String> names) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = ConfigsHandler.returnLatestVersionConfigUrlByNames(host, port,names);
		String content = HttpTools.getInstance().getContent(url);
		List<ConfigVersion> configs = parserLatestVersionConfig(content);
		return configs;
	}
	
	
	
	/**
	 * 列出执行的配置信息
	 * @param host
	 * @param port
	 * @param clustername
	 * @param name hdfs-site
	 * @param tag  version1423232432523
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static List<Config> listConfig(String host,String port,String clustername,String name,String tag) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = ConfigsHandler.returnConfigUrl(host, port,clustername,name,tag);
		String content = HttpTools.getInstance().getContent(url);
		List<Config> configs = parserConfig(content);
		return configs;
	}
	
	/**
	 * 保存配置文件
	 * @param host
	 * @param port
	 * @param clustername
	 * @param name    配置对应的名字
	 * @param tag     配置对应的tag  这个tag可以从listAllConfigVersionsByNames里面取
	 * @param properties  要修改的配置   
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static String saveConfig(String host,String port,String clustername,String name,String tag,Map<String,String> properties) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = ConfigsHandler.returnSaveConfigUrl(host, port,clustername);
		String entityString = getEntity(name,tag,properties);
		String content = HttpTools.getInstance().putContent(url, entityString);
		return content;
	}
	
	
	
	private static String getEntity(String name, String tag,Map<String,String> properties) {
		Map<String, Map<String,Object>> etity = new HashMap<String, Map<String,Object>>();
		Map<String,Object> Clusters = new HashMap<String,Object>();
		Map<String,Object> desired_config = new HashMap<String,Object>();
		desired_config.put("type", name);
		desired_config.put("tag", tag);
		desired_config.put("properties", properties);
		Clusters.put("desired_config", desired_config);
		etity.put("Clusters", Clusters);
		String entityString = JSONTools.toJson(etity);
		return entityString;
	}
	
	
	/**
	 * 解析ConfigVersion
	 * @param content
	 * @return
	 */
	private static List<Config> parserConfig(String content) {
		Map m = JSONTools.fromJson(content, Map.class);
		Map properties = (Map) m.get("properties");
		Set<String> keyset = properties.keySet();
		List<Config> ls = new ArrayList<Config>();
		for(String key:keyset){
			String value = (String) properties.get(key);
			Config config = new Config();
			config.setKey(key);
			config.setValue(value);
			ls.add(config);
		}
		return ls;
	}
	
	/**
	 * 解析ConfigVersion
	 * @param content
	 * @return
	 */
	private static List<ConfigVersion> parserLatestVersionConfig(String content) {
		Map m = JSONTools.fromJson(content, Map.class);
		Map clusters = (Map) m.get("Clusters");
		Map desired_configs = (Map) clusters.get("desired_configs");
		Set<String> keyset = desired_configs.keySet();
		List<ConfigVersion> ls = new ArrayList<ConfigVersion>();
		for(String key:keyset){
			Map vm = (Map) desired_configs.get(key);
			ConfigVersion config = new ConfigVersion();
			String name = key;
			String tag = (String) vm.get("tag");
			String user = (String) vm.get("user");
			String version = (String) vm.get("version");
			config.setTag(tag);
			config.setUser(user);
			config.setVersion(version);
			ls.add(config);
		}
		return ls;
	}

}
