package cn.vigor.API.util;

import java.util.List;



public class ConfigsHandler {
	
	
	public static String returnLatestVersionConfigUrlByNames(String host,String port,List<String> names){
		int size = names.size();
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/?fields=");
		for(int i=0;i<size;i++){
			String path = names.get(i);
			if(i==size-1){
				sb.append("Clusters/desired_configs/"+path);
			}else{
				sb.append("Clusters/desired_configs/"+path+",");
			}
		}
		
		return sb.toString();
	}
	
	public static String returnAllLatestVersionConfigUrl(String host,String port){
		return "http://"+host+":"+port+"/api/v1/clusters/?fields=Clusters/desired_configs";
	}
	
	
	public static String returnConfigUrl(String host,String port,String clustername,String name,String tag){
		return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/configurations?type="+name+"&&tag="+tag;
	}
	
	
	public static String returnSaveConfigUrl(String host,String port,String clustername){
		return "http://"+host+":"+port+"/api/v1/clusters/"+clustername;
	}
	
}
