package cn.vigor.API.util;


public class ManagerHandler {
	
	
	public static String returnServicesUrl(String host,String port,String clustername,String service){
		return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/services/"+service;
	}
	
	
	public static String returndaemonsUrl(String host,String port,String clustername,String targethost,String componenet){
		return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+targethost+"/host_components/"+componenet;
	}

	
	public static String returnReqiestCheckUrl(String host,String port,String clustername,String id){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/requests/"+id);
		return sb.toString();
	}
	
	
	public static String returnDecommissionUrl(String host,String port,String clustername){
		StringBuffer sb = new StringBuffer();
		sb.append("http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/requests");
		return sb.toString();
	}
}
