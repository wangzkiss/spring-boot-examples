package cn.vigor.API;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.vigor.API.model.metrics.common.Path;
import cn.vigor.API.util.JSONTools;
import cn.vigor.modules.iim.utils.DateUtil;

/**
 * Hello world!
 *
 */
public class App 
{
    
    public static String returnServicesUrl(String host,String port,String clustername,String service){
        return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/services/"+service;
    }
    
    
    public static String returnHostUrl(String host,String port,String clustername,String targethost,String componenet){
        return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+targethost+"/host_components/"+componenet;
    }

    /**
     * {
          "href" : "http://172.18.84.67:8080/api/v1/clusters/xdata2/requests/297",
          "Requests" : {
            "id" : 297,
            "status" : "Accepted"
          }
        }
     * 
     * http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts/xdata69/host_components/DATANODE?
    {
    "RequestInfo": 
        {
          "context":"Stop DataNode",
         "operation_level":{"level":"HOST_COMPONENT","cluster_name":"xdata2","host_name":"xdata69","service_name":"HDFS"}
        },
    "Body":{"HostRoles":{"state":"INSTALLED"}}
    }:
    
    {"RequestInfo":
        {  "context":"Start DataNode",
           "operation_level":{"level":"HOST_COMPONENT","cluster_name":"xdata2","host_name":"xdata69","service_name":"HDFS"}
        },
      "Body":{"HostRoles":{"state":"STARTED"}}
    }:
    
    Request URL:http://172.18.84.67:8080/api/v1/clusters/xdata2/services/HBASE
    {
      "RequestInfo":
        {
        "context":"_PARSE_.STOP.HBASE",
        "operation_level":{"level":"SERVICE","cluster_name":"xdata2","service_name":"HBASE"}
        },
      "Body": {"ServiceInfo":{"state":"INSTALLED"}}
    }:
    
    {
        "RequestInfo":{"context":"_PARSE_.START.HBASE",
        "operation_level":{"level":"SERVICE","cluster_name":"xdata2","service_name":"HBASE"}},
        "Body":{"ServiceInfo":{"state":"STARTED"}}
    }:
     * @param content
     * @param hosts
     * @return
     */
    public static String getEntity(String conmand, String hostName,String clusterName,String serviceName,String state) {
        Map <String , Object> etity = new LinkedHashMap<String , Object>();
      
        
        Map <String , Object> requestinfo = new LinkedHashMap<String , Object>();
        requestinfo.put("context", conmand);
        Map<String, String> operation_level = new LinkedHashMap<String, String>();
        if(hostName!=null){
            operation_level.put("level", "HOST_COMPONENT");
            operation_level.put("cluster_name", clusterName);
            operation_level.put("host_name", hostName);
        }else{
            operation_level.put("level", "SERVICE");
            operation_level.put("cluster_name", clusterName);
        }
        operation_level.put("service_name", serviceName);
        requestinfo.put("operation_level", operation_level);      
        etity.put("RequestInfo", requestinfo);
     
        Map<String , String> statemap = new  LinkedHashMap<String , String>();
        Map<String , Object> hostRoles = new  LinkedHashMap<String , Object>();
        statemap.put("state", state);
        if(hostName!=null){
            hostRoles.put("HostRoles",statemap);
        }else{
            hostRoles.put("ServiceInfo",statemap);
        }
        etity.put("Body", hostRoles);
        
        String entityString = JSONTools.toJson(etity);
        return entityString;
    }
 
    public static String returnRangeComponentUrl(String host,String port,String clustername,String componentType,String component,List<Path> ls){
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
	 public static void main(String[] args) throws Exception{
	       
	        System.out.println( DateUtil.long2string(1468464120));//
	        System.out.println( DateUtil.long2string(1468464072));
	     //{"RequestInfo":{"context":"Start DataNode","operation_level":{"level":"HOST_COMPONENT","cluster_name":"xdata2","host_name":"xdata71","service_name":"HDFS"}},"Body":{"HostRoles":{"state":"STARTED"}}}:
	     //{"RequestInfo":{"context":"Start DataNode","operation_level":{"level":"HOST_COMPONENT","cluster_name":"xdata2","host_name":"xdata71","service_name":"HDFS"}},"Body":{"HostRoles":{"state":"STARTED"}}}
	     //{"RequestInfo":{"context":"Start DataNode","operation_level":{"level":"HOST_COMPONENT","cluster_name":"xdata2","host_name":"xdata71","service_name":"HDFS"}},"Body":{"HostRoles":{"state":"STARTED"}}}:
	     //http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts/xdata71/host_components/DATANODE?    INSTALLED
	     //http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts/xdata71/host_components/DATANODE
	     //http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts/xdata71/host_components/DATANODE?
	     //{"RequestInfo":{"context":"Stop DataNode","operation_level":{"level":"HOST_COMPONENT","cluster_name":"xdata2","host_name":"xdata71","service_name":"HDFS"}},"Body":{"HostRoles":{"state":"INSTALLED"}}}:
	   /*   
	     String url=returnHostUrl("172.18.84.67", "8080", "xdata2", "xdata71", "DATANODE");
	     System.out.println(url);
	     String entityString=getEntity("Stop DataNode", "xdata71","xdata2", "HDFS", "INSTALLED");
	     System.out.println(entityString);
	     String c=HttpTools.getInstance().postContent(url, entityString);
	     System.out.println(c);*/
	     
	    /* long end = new Date().getTime() / 1000;
	     long start = end - 1800;
	     String keys = MetricsUtil.getClusterKeys("yarn");
	     List<Path> hostURLs = new ArrayList<Path>();
        for (String key : keys.split(","))
        {
            Path murl = new Path();
            murl.setPath(key);
            murl.setStarttime(start + "");
            murl.setEndtime(end + "");
            murl.setStep("15");
            hostURLs.add(murl);
        }
	     String url=returnRangeComponentUrl("172.18.84.67","8080", "xdata2", "HDFS","DATANODE",hostURLs);
	     String c="";
	     try
	        {
	            url=MetricsUtil.returnServiceUrl("172.18.84.67","8080", "xdata2", "HBASE","");
	            c=HttpTools.getInstance().getContent(url);
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	     
	     System.out.println(c);*/
	}
}
