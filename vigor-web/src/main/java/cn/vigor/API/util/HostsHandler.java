package cn.vigor.API.util;




public class HostsHandler {
	
	
	
	
	public static String returnListHostsUrl(String host,String port){
		return "http://"+host+":"+port+"/api/v1/hosts";
	}
	
	public static String returnAddHostsUrl(String host,String port,String clustername,String newhost){
		return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+newhost;
	}
	
	
	public static String returnListComponentsUrl(String host,String port,String clustername,String targethost){
		return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+targethost+"/host_components";
	}
	
}
