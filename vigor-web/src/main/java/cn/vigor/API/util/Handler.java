package cn.vigor.API.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.vigor.API.model.metrics.common.Path;
import cn.vigor.API.model.metrics.common.Value;

public class Handler {
	
	
	
	
	
	
	public static String returnHostUrl(String host,String port,String targethost,String clustername,List<Path> ls){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+targethost+"?fields=");
		int size = ls.size();
		for(int i=0;i<size;i++){
			Path path = ls.get(i);
			if(i==size-1){
				sb.append(path.toPath());
			}else{
				sb.append(path.toPath()+",");
			}
		}
		sb.append("&_="+new Date().getTime());
		return sb.toString();
	}
	
	public static String returnRangeHostUrl(String host,String port,String targethost,String clustername,List<Path> ls){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+targethost+"?fields=");
		int size = ls.size();
		for(int i=0;i<size;i++){
			Path path = ls.get(i);
			if(i==size-1){
				sb.append(path.toRangePath());
			}else{
				sb.append(path.toRangePath()+",");
			}
		}
		sb.append("&_="+new Date().getTime());
		return sb.toString();
	}
	
	
	public static String returnHostComponentUrl(String host,String port,String targethost,String clustername,String component,List<Path> paths){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+targethost+"/host_components/"+component+"?fields=");
		int size = paths.size();
		for(int i=0;i<size;i++){
			Path url = paths.get(i);
			if(i==size-1){
				String param = url.toPath();
				sb.append(param);
			}else{
				String param = url.toPath();
				sb.append(param+",");
			}
		}
		sb.append("&_="+new Date().getTime());
		return sb.toString();
	}
	
	public static String returnRangeHostComponentUrl(String host,String port,String targethost,String clustername,String component,List<Path> ls){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+targethost+"/host_components/"+component+"?fields=");
		int size = ls.size();
		for(int i=0;i<size;i++){
			Path path = ls.get(i);
			if(i==size-1){
				String param = path.toRangePath();
				sb.append(param);
			}else{
				String param = path.toRangePath();
				sb.append(param+",");
			}
		}
		sb.append("&_="+new Date().getTime());
		return sb.toString();
	}
	
	
	public static String returnComponentUrl(String host,String port,String componentType,String clustername,String component,List<Path> ls){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/services/"+componentType+"/components/"+component+"?fields=");
		int size = ls.size();
		for(int i=0;i<size;i++){
			Path path = ls.get(i);
			if(i==size-1){
				sb.append(path.toPath());
			}else{
				sb.append(path.toPath()+",");
			}
		}
		sb.append("&_="+new Date().getTime());
		return sb.toString();
	}
	
	public static String returnRangeComponentUrl(String host,String port,String componentType,String clustername,String component,List<Path> ls){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/services/"+componentType+"/components/"+component+"?fields=");
		int size = ls.size();
		for(int i=0;i<size;i++){
			Path path = ls.get(i);
			if(i==size-1){
				sb.append(path.toRangePath());
			}else{
				sb.append(path.toRangePath()+",");
			}
		}
		sb.append("&_="+new Date().getTime());
		return sb.toString();
	}
	
	
	
	public static Map<String,String> handleResult(Map map,List<Path> urls){
		Map<String,String> resultMap = new HashMap<String,String>();
		for(Path path :urls){
			String[] keyarr = path.toPath().split("/");
			String v = JSONTools.search(map,keyarr,0,keyarr.length);
			resultMap.put(path.toPath(),v);
		}
		return resultMap;
	}
	
	public static Map<String,List<Value>> handleListResult(Map map,List<Path> urls){
		Map<String,List<Value>> resultMap = new HashMap<String,List<Value>>();
		for(Path path :urls){
			List<Value> values = new ArrayList<Value>();
			String[] keyarr = path.toPath().split("/");
			String v = JSONTools.search(map,keyarr,0,keyarr.length);
			if(v!=null){
				List<List> ls = JSONTools.fromJson(v, List.class);
				for(List ar:ls){
					Value mv = new Value();
					String k1 = ar.get(0).toString();
					String k2 = ar.get(1).toString();
					mv.setValue(k1);
					mv.setTime(k2);
					values.add(mv);
				}
				resultMap.put(path.toPath(),values);
			}
			
		}
		return resultMap;
	}

	public static String returnAlertsUrl(String host,String port,String clustername,Map<String,String> map){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/alerts?fields=*&");
		if(map!=null){
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append("Alert/" + key + "=" + value + "&");
			}
		}
		if(!sb.toString().endsWith("&")){
			sb.append("&_="+new Date().getTime());
		}else{
			sb.append("_="+new Date().getTime());
		}
		return sb.toString();
	}
	
	public static String returnCreateComponentUrl(String host,String port,String clustername,String hostName,String componentName){
		String url = "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+hostName+"/host_components/"+componentName;
		return url;
	}
	
	public static String returnHostsDiskInfoUrl(String host,String port,String clustername){
		String url = "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts?fields=metrics/disk,Hosts/host_status";
		return url;
	}
	
	public static String returnServiceComponentsUrl(String host,String port,String clustername){
		String url = "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/services?fields=components/ServiceComponentInfo";
		return url;
	}
	
	public static String returnServiceConfigUrl(String host,String port,String clustername,String serviceName,String version,Boolean isCurrent){
		String url = "http://" + host + ":"+port + "/api/v1/clusters/" + clustername + "/configurations/service_config_versions";
		if(serviceName!=null){
			url = url + (url.contains("?")?"&service_name=" + serviceName:"?service_name=" + serviceName);
		}
		if(version!=null){
			url = url + (url.contains("?")?"&service_config_version=" + version:"?service_config_version=" + version);
		}
		if(isCurrent!=null){
			url = url + (url.contains("?")?"&is_current=" + isCurrent:"?is_current=" + isCurrent);
		}
		return url;
	}
	
	public static String returnHostComponentsUrl(String host,String port,String clustername){
		return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts?fields=host_components/HostRoles/state,host_components/HostRoles/service_name,host_components/HostRoles/display_name";
	}
}
