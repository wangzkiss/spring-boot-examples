package cn.vigor.modules.server.util;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.vigor.API.model.metrics.common.Path;
import cn.vigor.API.util.JSONTools;

public class MetricsUtil
{
    public static Map<String, String> metrics = new HashMap<String, String>();
    public static Map<String, String> clusers = new HashMap<String, String>();
    public static Map<String, String> dateKey = new HashMap<String, String>();
    public static Map<String, String> addnode = new HashMap<String, String>();
    static
    {
        metrics.put("cpu",
                "metrics/cpu/cpu_user,metrics/cpu/cpu_wio,metrics/cpu/cpu_nice,metrics/cpu/cpu_aidle,metrics/cpu/cpu_system,metrics/cpu/cpu_idle");
        metrics.put("disk", "metrics/disk/disk_total,metrics/disk/disk_free");
        metrics.put("load",
                "metrics/load/load_fifteen,metrics/load/load_one,metrics/load/load_five");
        metrics.put("memory",
                "metrics/memory/swap_free,metrics/memory/mem_shared,metrics/memory/mem_free,metrics/memory/mem_cached,metrics/memory/mem_buffers");
        metrics.put("network",
                "metrics/network/bytes_in,metrics/network/bytes_out");
        metrics.put("process",
                "metrics/process/proc_total,metrics/process/proc_run");
        clusers.put("yarn", "metrics/yarn/ContainersFailed._rate,metrics/yarn/ContainersCompleted._rate,metrics/yarn/ContainersLaunched._rate,metrics/yarn/ContainersIniting._sum,metrics/yarn/ContainersKilled._rate,metrics/yarn/ContainersRunning._sum,metrics/memory/mem_total._avg,metrics/memory/mem_free._avg,metrics/disk/read_bps._sum,metrics/disk/write_bps._sum,metrics/network/pkts_in._avg,metrics/network/pkts_out._avg,metrics/cpu/cpu_system._sum,metrics/cpu/cpu_user._sum,metrics/cpu/cpu_nice._sum,metrics/cpu/cpu_idle._sum,metrics/cpu/cpu_wio._sum");
        clusers.put("hdfs", "metrics/cpu/cpu_system,metrics/cpu/cpu_user,metrics/cpu/cpu_nice,metrics/cpu/cpu_idle,metrics/cpu/cpu_wio,metrics/memory/mem_total,metrics/memory/mem_free,metrics/jvm/GcTimeMillisConcurrentMarkSweep._rate,metrics/rpc/client/NumOpenConnections,metrics/rpc/datanode/NumOpenConnections,metrics/jvm/memHeapCommittedM,metrics/jvm/memHeapUsedM,metrics/rpc/client/RpcQueueTime_avg_time,metrics/rpc/client/RpcProcessingTime_avg_time,metrics/rpc/datanode/RpcQueueTime_avg_time,metrics/rpc/datanode/RpcProcessingTime_avg_time");
        clusers.put("hbase", "metrics/hbase/regionserver/Server/Get_num_ops._rate,metrics/hbase/regionserver/Server/ScanNext_num_ops._rate,metrics/hbase/regionserver/Server/Append_num_ops._rate,metrics/hbase/regionserver/Server/Delete_num_ops._rate,metrics/hbase/regionserver/Server/Increment_num_ops._rate,metrics/hbase/regionserver/Server/Mutate_num_ops._rate,metrics/hbase/regionserver/Server/Get_95th_percentile._max,metrics/hbase/regionserver/Server/ScanNext_95th_percentile._max,metrics/hbase/regionserver/Server/Mutate_95th_percentile._max,metrics/hbase/regionserver/Server/Increment_95th_percentile._max,metrics/hbase/regionserver/Server/Append_95th_percentile._max,metrics/hbase/regionserver/Server/Delete_95th_percentile._max,metrics/hbase/ipc/IPC/numOpenConnections._sum,metrics/hbase/ipc/IPC/numActiveHandler._sum,metrics/hbase/ipc/IPC/numCallsInGeneralQueue._sum,metrics/hbase/regionserver/Server/updatesBlockedTime._rate,metrics/cpu/cpu_system._sum,metrics/cpu/cpu_user._sum,metrics/cpu/cpu_nice._sum,metrics/cpu/cpu_idle._sum,metrics/cpu/cpu_wio._sum,metrics/network/pkts_in._avg,metrics/network/pkts_out._avg,metrics/disk/read_bps._sum,metrics/disk/write_bps._sum");
   
        dateKey.put("second", "%Y-%m-%d %H:%i:%s");
        dateKey.put("minute", "%Y-%m-%d %H:%i");
        dateKey.put("hour", "%Y-%m-%d %H");
        dateKey.put("day", "%Y-%m-%d");
        dateKey.put("month", "%Y-%m");
        dateKey.put("year", "%Y");
        
        
        addnode.put("hdfs", "DATANODE");
        addnode.put("yarn", "NODEMANAGER");
        addnode.put("hbase", "HBASE_REGIONSERVER");
        
        addnode.put("DATANODE", "hdfs");
        addnode.put("NODEMANAGER", "yarn");
        addnode.put("HBASE_REGIONSERVER", "hbase");
    }
    
    /**
     * @param key
     * @return
     * 
     */
    public static String getKeys(String key)
    {
        return metrics.get(key);
    }
    
    /**
     * @param key
     * @return
     * 
     * datanode,nodemanager,regionserver
     */
    public static String getAddNode(String key)
    {
        return addnode.get(key);
    }
    /**
    /**
     * @param key
     * @return
     */
    public static String getClusterKeys(String key)
    {
        return clusers.get(key);
    }
    
    /**
     * @param key
     * @return
     */
    public static String getDateKey(String key)
    {
        String date= dateKey.get(key);
        date=date==null?"day":date;
        return date;
    }
    /**
     * @param key
     * @return
     */
    public static String getAllKeys()
    {
        String values = "";
        for (String key : metrics.keySet())
        {
            values += metrics.get(key) + ",";
        }
        values = values.substring(0, values.lastIndexOf(","));
        return values;
    }
    /**
     * 
     * @param host ambari ip地址
     * @param port  ambari 端口
     * @param clustername xdata2
     * @param service  集群名
     * @return
     */
    public static String returnServicesUrl(String host,String port,String clustername,String service){
        return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/services/"+service;
    }
    /**
     * 
     * @param host ambari ip地址
     * @param port  ambari 端口
     * @param clustername xdata2
     * @param service  集群名
     * @param name 组件名
     * @return
     */
    public static String returnServiceUrl(String host,String port,String clustername,String service,String name){
        return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/services/"+service+"/components/"+name;
    }
    
    /**
     * 
     * @param host ambari ip地址
     * @param port ambari 端口
     * @param clustername  xdata2
     * @param targethost 机器名
     * @param componenet 组件名
     * @return
     */
    public static String returnHostUrl(String host,String port,String clustername,String targethost,String name){
        return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/hosts/"+targethost+"/host_components/"+name;
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
        if(conmand!=null){
        	requestinfo.put("context", conmand);
        }
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
    /**
     * 
     * @param host ambari 主机ip
     * @param port 端口
     * @param clustername  集群默认为 xdata2
     * @param componentType  集群名
     * @param component  集群组件
     * @param ls 指标字段
     * @return
     */
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
    
    /**
     * 重启组件components的接口地址
     * @param host 主机
     * @param port 端口
     * @param clustername 集群名称
     * @return 结果
     */
    public static String returnRestartComponentUrl(String host,String port,String clustername){
    	return "http://"+host+":"+port+"/api/v1/clusters/"+clustername+"/requests";
    }
}
