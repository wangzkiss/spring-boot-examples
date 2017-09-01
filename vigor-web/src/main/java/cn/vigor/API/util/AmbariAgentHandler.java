package cn.vigor.API.util;

public class AmbariAgentHandler {
	
	public static String returnInstallAgentUrl(String host,String port){
		return "http://"+host+":"+port+"/api/v1/bootstrap";
	}
	
	public static String returnCheckStatusUrl(String host,String port,String id){
		return "http://"+host+":"+port+"/api/v1/bootstrap/"+id;
	}
	
	
}
