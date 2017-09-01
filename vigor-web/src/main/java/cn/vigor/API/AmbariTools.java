package cn.vigor.API;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.vigor.API.util.HttpTools;
import cn.vigor.common.config.Global;

public class AmbariTools
{
  
    public static final String ip=Global.getConfig("ambr_host");
    public static final String port=Global.getConfig("ambr_port");
    public static final String clustername=Global.getConfig("clustername");
    public static final String BASE_URL = "http://"+ip+":"+port+"/api/v1";
    public static final String CLUSTER_BASE_URL = BASE_URL+"/clusters/"+clustername;
    public static final String REQUEST_URL = BASE_URL + "/requests";
    public static final String CLUSTER_REQUEST_URL = CLUSTER_BASE_URL + "/requests";
    public static final String HOSTS_URL = BASE_URL + "/hosts";
    public static final String CLUSTER_HOSTS_URL = CLUSTER_BASE_URL + "/hosts";
    
    public static final String COMPONENT_METRICS_MONITOR = "METRICS_MONITOR";
    public static final String COMPONENT_NODEMANAGER = "NODEMANAGER";
    public static final String COMPONENT_DATANODE = "DATANODE";
    public static final String COMPONENT_HBASE_REGIONSERVER = "HBASE_REGIONSERVER";
    
    public static final String COMPONENT_ACTION_INSTALL = "Install";
    public static final String COMPONENT_ACTION_START = "Start";
    public static final String COMPONENT_ACTION_STOP = "Stop";
    public static final String COMPONENT_ACTION_RESTART = "Restart";
    
    public static Map<String, String> actionDesiredState = null;
    public static Map<String, Map<String, String>> componentContextAndService = null;
    public static void initMap() {
        if(null == actionDesiredState || null == componentContextAndService) {
            actionDesiredState = new HashMap<String, String>();
            componentContextAndService = new HashMap<String, Map<String, String>>();
            actionDesiredState.put("Install", "INSTALLED");
            actionDesiredState.put("Start", "STARTED");
            actionDesiredState.put("Stop", "INSTALLED");
            Map<String, String> map = new HashMap<String, String>();
            map.put("context", "DataNode");
            map.put("service", "HDFS");
            componentContextAndService.put(COMPONENT_DATANODE, map);
            map.put("context", "NodeManager");
            map.put("service", "YARN");
            componentContextAndService.put(COMPONENT_NODEMANAGER, map);
            map.put("context", "RegionServer");
            map.put("service", "HBASE");
            componentContextAndService.put(COMPONENT_HBASE_REGIONSERVER, map);
            map.put("context", "Metrics Monitor");
            map.put("service", "AMBARI_METRICS");
            componentContextAndService.put(COMPONENT_METRICS_MONITOR, map);
        }
    }
    
    public static String getComponentService(String componentName) {
        initMap();
        return componentContextAndService.get(componentName).get("service");
    }
    public static String getComponentContext(String componentName) {
        initMap();
        return componentContextAndService.get(componentName).get("context");
    }

    
    public static String  addServer(String hostName , String sshKey ,String userName) throws Exception {
        int requestId;
        String msg="";
        //完整安装示例
        if(boostrap(hostName, sshKey, userName) && addHost(hostName)) {
            msg=("服务器信息注册成功！");
            hostComponentInit(hostName, COMPONENT_METRICS_MONITOR);
            requestId = hostComponentAction(hostName, COMPONENT_ACTION_INSTALL, COMPONENT_METRICS_MONITOR, "INSTALLED");
            if(requestId > 0) {
                if("COMPLETED".equals(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId))) {
                    requestId = hostComponentAction(hostName, "Start", COMPONENT_METRICS_MONITOR, "STARTED");
                    msg=(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId));
                }
            }
        }else{
            msg=("服务器信息注册失败！");
        }
        return msg;
    }
    
    public static void main(String args[]) throws Exception {
        String hostName = "xdata29";
        int requestId;
        
        String sshKey = "-----BEGIN RSA PRIVATE KEY-----\nMIIEogIBAAKCAQEAk6YkUgXy1HNRH+lfzUkzWabOAQ+NboHFDy66IiAoZuG9FGy6\nfj/9uSqRyddg42axLoqNZqJujQDZrrB+60VmzS/55gaoVd2q8QQqoyHKc0fGNiGC\nzyhF/koV2PUL4Nts68mJyzqI+6kTP5NeGDFtY/9r1nc1BjpCvRLM1RLDygaPMkIX\n7zwYLGY5Rs97pF5/lU7Jf+1ti4hqJahYD+Fje7xwN8CKbi4y/5s1JYTMty33k5W8\nm6MO+GPVmki46sAZkzmpmWFuL7ak3L6DYxglhNBdkQKUTXFgQa3ulnGXgIQqjd03\nwMeOqd/eu/dN3BM1zHjqHLjTOTUtfse2H/A+6wIBIwKCAQBDfyaL5XZSfdvxVL4U\ns7+z9HtuMv7TboX4T+APmawRxkfO0p5lmZiPKWc3sup2lVhPyk9E3IpPFlTiJMxO\nS5y1kkZahrNac/ZfjOBKkxq4W1NL8g/prAK9Y7I3S3Mk9pgxRjBc548T9YUkYKAL\nD0fzMu93+/r7iFkF+fc81WDRYWPSWyHxTD5rYVrn4momMJmeDmx2bfNFs26pPLXt\nQjWPy1yKQpQsr02VvqhMryMwh/xigX5Bm1pzLHDO5pqa5ScO7UMmZRluRFW0X6b+\ncHoyMyXBCe6f7SMpnrrY5+FPK+ABZ9ejHBLugEA/5UOUv1bNp6IsJji7HD/dCcXt\nSI6LAoGBAMLzfCkyhb4r0ldugGvf/MA2AYy2BSaTiOqe43esqKWWCZLt9OVxp7h7\nQaa3U3/DYO289SjlbU5D+sr9ycNn0KLp8zyUMY1+VTr46DSQDVfFiTRD63RY8w3s\nBe/5r9PohHwCr7iv7wkuj8IrOG/NVOyEXoPRrEHmtJhmcQdOIX0pAoGBAMHinpR8\n35F1rur9D8eQ129Vja+G18LPhv10kBJsN+i7JgLbWgDaxwDMyH5Gci/5o5G/JU+G\nserYd9PX5oNQGjE4n2ohMrkQK+5pRRGGdMJxd0a20WX2p5VqnMXiRoa6z7SIGldc\nCxwRl5AE+Vo5W7tmO7OKePU5Mslk0U3awAnzAoGBAJZkD1L7FrdGYG9VPn8agSaQ\nD9Ly0MX8yLT+OnJDXYcNV9e+4X3MtJWg6YCcDTasqdv4KtZn1/qMNn9WEKyvK+tk\nBMhVEEiUqCYtunGw9FmnA3F2OUsgC/TMBJSNamGkvflD5qRqdpIN+d7uJDj9fAbp\nxUEeFyuGFkmuHK3dL8bzAoGACxRDk3TZk0iNpwcleR44Mj9nLpn9s1xQ20h9QuGc\ny3hoknLvMz+zmaVNSQtW+21ZzdBoiDOVI1zTpbSQ1E24WpWFc8dh+/JLpzk3JZKn\nlhUcwjZVG8TzoiNZamSzkquW1x24XMNuWWAXSg7prWJdA2Ttd/lIvY49apgL9dH8\nWFcCgYEAlkCCmMHZGOji1ulogyRSAq5J0E4UMrLkK4ab3u6wIF1gBGuFZGrk9qdz\nM/Y9UK9pI42NygGdw9HcuuozUX8oYAdrFXdeEkBzr7z0BSnh3T1FwXgQY4FXY3kw\nm+teSKO4ju9qRP3uHw2Suz0BkVt6kVdT77o/rGyT+IEnVvV6wF8=\n-----END RSA PRIVATE KEY-----\n";
        
        //完整安装示例
        if(boostrap(hostName, sshKey, "root") && addHost(hostName)) {
            System.out.println("host regiseter success");
            hostComponentInit(hostName, COMPONENT_METRICS_MONITOR);
            requestId = hostComponentAction(hostName, COMPONENT_ACTION_INSTALL, COMPONENT_METRICS_MONITOR, "INSTALLED");
            if(requestId > 0) {
                if("COMPLETED".equals(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId))) {
                    requestId = hostComponentAction(hostName, "Start", COMPONENT_METRICS_MONITOR, "STARTED");
                    System.out.println(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId));
                }
            }
        }
        
        
        //服务安装、启停示例
        hostComponentInit(hostName, COMPONENT_HBASE_REGIONSERVER);
        requestId = hostComponentAction(hostName, "Install", COMPONENT_HBASE_REGIONSERVER, "INSTALLED");
        if(requestId > 0) {
            if("COMPLETED".equals(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId))) {
                requestId = hostComponentAction(hostName, "Start", COMPONENT_HBASE_REGIONSERVER, "STARTED");
                System.out.println(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId));
            }
        }
        requestId = hostComponentAction(hostName, COMPONENT_ACTION_STOP, COMPONENT_HBASE_REGIONSERVER, "INSTALLED");
        if(requestId > 0) {
            System.out.println(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId));
        }
        requestId = hostComponentAction(hostName, COMPONENT_ACTION_RESTART, COMPONENT_HBASE_REGIONSERVER, "STARTED");
        if(requestId > 0) {
            System.out.println(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId));
        }
        
    }
    
    public static String  addCalNode(String hostName ,String componment) throws Exception {
        int requestId;
        String msg="";
        //服务安装、启停示例  
       /* String componment=COMPONENT_DATANODE;
        if(1==type)
        {
            componment=COMPONENT_DATANODE;
        }else if(2 == type)
        {
            componment=COMPONENT_NODEMANAGER;
        }else {
            componment=COMPONENT_HBASE_REGIONSERVER;
        }*/
        hostComponentInit(hostName, componment);
        requestId = hostComponentAction(hostName, "Install", componment, "INSTALLED");
        if(requestId > 0) {
            if("COMPLETED".equals(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId))) {
                requestId = hostComponentAction(hostName, "Start", componment, "STARTED");
                msg=(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId));
            }
        }
       /* requestId = hostComponentAction(hostName, COMPONENT_ACTION_STOP, componment, "INSTALLED");
        if(requestId > 0) {
            msg=(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId));
        }
        requestId = hostComponentAction(hostName, COMPONENT_ACTION_RESTART, componment, "STARTED");
        if(requestId > 0) {
            msg=(recheckRequest(CLUSTER_REQUEST_URL + "/" + requestId));
        }*/
        return msg;
    }
    
    
    
    /*
     * 安装条件：
     * 1、新增的节点的openssl 版本与ambari服务器一样;还有gcc版本：gcc-4.4.7-17.el6.x86_64
     * 2、集群所有服务器上配置好host映射 /etc/hosts
     * 3、提供ambari服务器上的免密码登录的私钥：免密码登录做好
     * 4、新增的节点jdk安装并配置好环境变量
     */
    //安装第一步:agent注册
    /* post http://172.18.84.67:8080/api/v1
     *  {
     *      "hosts":["xdata49"],
     *      "sshKey":"-----BEGIN RSA PRIVATE KEY-----\nMIIEogIBAAKCAQEAk6YkUgXy1HNRH+lfzUkzWabOAQ+NboHFDy66IiAoZuG9FGy6\nfj/9uSqRyddg42axLoqNZqJujQDZrrB+60VmzS/55gaoVd2q8QQqoyHKc0fGNiGC\nzyhF/koV2PUL4Nts68mJyzqI+6kTP5NeGDFtY/9r1nc1BjpCvRLM1RLDygaPMkIX\n7zwYLGY5Rs97pF5/lU7Jf+1ti4hqJahYD+Fje7xwN8CKbi4y/5s1JYTMty33k5W8\nm6MO+GPVmki46sAZkzmpmWFuL7ak3L6DYxglhNBdkQKUTXFgQa3ulnGXgIQqjd03\nwMeOqd/eu/dN3BM1zHjqHLjTOTUtfse2H/A+6wIBIwKCAQBDfyaL5XZSfdvxVL4U\ns7+z9HtuMv7TboX4T+APmawRxkfO0p5lmZiPKWc3sup2lVhPyk9E3IpPFlTiJMxO\nS5y1kkZahrNac/ZfjOBKkxq4W1NL8g/prAK9Y7I3S3Mk9pgxRjBc548T9YUkYKAL\nD0fzMu93+/r7iFkF+fc81WDRYWPSWyHxTD5rYVrn4momMJmeDmx2bfNFs26pPLXt\nQjWPy1yKQpQsr02VvqhMryMwh/xigX5Bm1pzLHDO5pqa5ScO7UMmZRluRFW0X6b+\ncHoyMyXBCe6f7SMpnrrY5+FPK+ABZ9ejHBLugEA/5UOUv1bNp6IsJji7HD/dCcXt\nSI6LAoGBAMLzfCkyhb4r0ldugGvf/MA2AYy2BSaTiOqe43esqKWWCZLt9OVxp7h7\nQaa3U3/DYO289SjlbU5D+sr9ycNn0KLp8zyUMY1+VTr46DSQDVfFiTRD63RY8w3s\nBe/5r9PohHwCr7iv7wkuj8IrOG/NVOyEXoPRrEHmtJhmcQdOIX0pAoGBAMHinpR8\n35F1rur9D8eQ129Vja+G18LPhv10kBJsN+i7JgLbWgDaxwDMyH5Gci/5o5G/JU+G\nserYd9PX5oNQGjE4n2ohMrkQK+5pRRGGdMJxd0a20WX2p5VqnMXiRoa6z7SIGldc\nCxwRl5AE+Vo5W7tmO7OKePU5Mslk0U3awAnzAoGBAJZkD1L7FrdGYG9VPn8agSaQ\nD9Ly0MX8yLT+OnJDXYcNV9e+4X3MtJWg6YCcDTasqdv4KtZn1/qMNn9WEKyvK+tk\nBMhVEEiUqCYtunGw9FmnA3F2OUsgC/TMBJSNamGkvflD5qRqdpIN+d7uJDj9fAbp\nxUEeFyuGFkmuHK3dL8bzAoGACxRDk3TZk0iNpwcleR44Mj9nLpn9s1xQ20h9QuGc\ny3hoknLvMz+zmaVNSQtW+21ZzdBoiDOVI1zTpbSQ1E24WpWFc8dh+/JLpzk3JZKn\nlhUcwjZVG8TzoiNZamSzkquW1x24XMNuWWAXSg7prWJdA2Ttd/lIvY49apgL9dH8\nWFcCgYEAlkCCmMHZGOji1ulogyRSAq5J0E4UMrLkK4ab3u6wIF1gBGuFZGrk9qdz\nM/Y9UK9pI42NygGdw9HcuuozUX8oYAdrFXdeEkBzr7z0BSnh3T1FwXgQY4FXY3kw\nm+teSKO4ju9qRP3uHw2Suz0BkVt6kVdT77o/rGyT+IEnVvV6wF8=\n-----END RSA PRIVATE KEY-----\n",
     *      "user":"root",
     *      "userRunAs":"root",
     *      "verbose":true
     *  }
     */
    public static boolean boostrap(String hostName, String sshKey, String user) throws Exception {
        String hostBootstrap = BASE_URL + "/bootstrap";
        Map<String, Object> request = new HashMap<String, Object>();
        List<String> hostList = Lists.newArrayList();
        hostList.add(hostName);
        //String sshKey = "-----BEGIN RSA PRIVATE KEY-----\nMIIEogIBAAKCAQEAk6YkUgXy1HNRH+lfzUkzWabOAQ+NboHFDy66IiAoZuG9FGy6\nfj/9uSqRyddg42axLoqNZqJujQDZrrB+60VmzS/55gaoVd2q8QQqoyHKc0fGNiGC\nzyhF/koV2PUL4Nts68mJyzqI+6kTP5NeGDFtY/9r1nc1BjpCvRLM1RLDygaPMkIX\n7zwYLGY5Rs97pF5/lU7Jf+1ti4hqJahYD+Fje7xwN8CKbi4y/5s1JYTMty33k5W8\nm6MO+GPVmki46sAZkzmpmWFuL7ak3L6DYxglhNBdkQKUTXFgQa3ulnGXgIQqjd03\nwMeOqd/eu/dN3BM1zHjqHLjTOTUtfse2H/A+6wIBIwKCAQBDfyaL5XZSfdvxVL4U\ns7+z9HtuMv7TboX4T+APmawRxkfO0p5lmZiPKWc3sup2lVhPyk9E3IpPFlTiJMxO\nS5y1kkZahrNac/ZfjOBKkxq4W1NL8g/prAK9Y7I3S3Mk9pgxRjBc548T9YUkYKAL\nD0fzMu93+/r7iFkF+fc81WDRYWPSWyHxTD5rYVrn4momMJmeDmx2bfNFs26pPLXt\nQjWPy1yKQpQsr02VvqhMryMwh/xigX5Bm1pzLHDO5pqa5ScO7UMmZRluRFW0X6b+\ncHoyMyXBCe6f7SMpnrrY5+FPK+ABZ9ejHBLugEA/5UOUv1bNp6IsJji7HD/dCcXt\nSI6LAoGBAMLzfCkyhb4r0ldugGvf/MA2AYy2BSaTiOqe43esqKWWCZLt9OVxp7h7\nQaa3U3/DYO289SjlbU5D+sr9ycNn0KLp8zyUMY1+VTr46DSQDVfFiTRD63RY8w3s\nBe/5r9PohHwCr7iv7wkuj8IrOG/NVOyEXoPRrEHmtJhmcQdOIX0pAoGBAMHinpR8\n35F1rur9D8eQ129Vja+G18LPhv10kBJsN+i7JgLbWgDaxwDMyH5Gci/5o5G/JU+G\nserYd9PX5oNQGjE4n2ohMrkQK+5pRRGGdMJxd0a20WX2p5VqnMXiRoa6z7SIGldc\nCxwRl5AE+Vo5W7tmO7OKePU5Mslk0U3awAnzAoGBAJZkD1L7FrdGYG9VPn8agSaQ\nD9Ly0MX8yLT+OnJDXYcNV9e+4X3MtJWg6YCcDTasqdv4KtZn1/qMNn9WEKyvK+tk\nBMhVEEiUqCYtunGw9FmnA3F2OUsgC/TMBJSNamGkvflD5qRqdpIN+d7uJDj9fAbp\nxUEeFyuGFkmuHK3dL8bzAoGACxRDk3TZk0iNpwcleR44Mj9nLpn9s1xQ20h9QuGc\ny3hoknLvMz+zmaVNSQtW+21ZzdBoiDOVI1zTpbSQ1E24WpWFc8dh+/JLpzk3JZKn\nlhUcwjZVG8TzoiNZamSzkquW1x24XMNuWWAXSg7prWJdA2Ttd/lIvY49apgL9dH8\nWFcCgYEAlkCCmMHZGOji1ulogyRSAq5J0E4UMrLkK4ab3u6wIF1gBGuFZGrk9qdz\nM/Y9UK9pI42NygGdw9HcuuozUX8oYAdrFXdeEkBzr7z0BSnh3T1FwXgQY4FXY3kw\nm+teSKO4ju9qRP3uHw2Suz0BkVt6kVdT77o/rGyT+IEnVvV6wF8=\n-----END RSA PRIVATE KEY-----\n";
        request.put("sshKey", sshKey);
        request.put("hosts", hostList);
        request.put("user", user);
        request.put("userRunAs", "root");
        request.put("verbose", true);
        Map<String,String> header = new HashMap<String,String>();
        header.put("Content-Type", "application/json");
        String response = HttpTools.getInstance().postContent(hostBootstrap, JSONObject.toJSONString(request), header);
        System.out.println("register response:" + response);
        JSONObject respJson = JSONObject.parseObject(response);
        if("OK".equals(respJson.getString("status"))) {
            int requestId = respJson.getIntValue("requestId");
            String hostBootstrapStatus = hostBootstrap + "/" + requestId, status = "";
            do {
                response = HttpTools.getInstance().getContent(hostBootstrapStatus);
                try {
                    Thread.sleep(1000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                respJson = JSONObject.parseObject(response);
                status = (String) respJson.get("status");
            } while("RUNNING".equals(status));
            System.out.println("bootstrap=" + response);
            if("SUCCESS".equalsIgnoreCase(status)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
 
    //安装第2步:添加节点到hdp集群
    /* post http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts
     *  [
     *      {
     *          "Hosts":{"host_name":"xdata49"}
     *      }
     *  ]
     */
    public static boolean addHost(String hostName) throws Exception {
        List<Map<String, Object>> hostsList = Lists.newArrayList();
        Map<String, Object> hostMap = new HashMap<String, Object>(), hostsMap = new HashMap<String, Object>();
        hostMap.put("host_name", hostName);
        hostsMap.put("Hosts", hostMap);
        hostsList.add(hostsMap);
        String response = HttpTools.getInstance().postContent(CLUSTER_HOSTS_URL, JSONObject.toJSONString(hostsList));
        if(!StringUtils.isEmpty(response)) {
            JSONObject respJson = JSONObject.parseObject(response);
            if(200 != respJson.getIntValue("status")) {
                System.out.println("add host msg:" + respJson.getString("message"));
                return false;
            }
        }
        return true;
    }
    
    public static int hostComponentAction(String hostName, String action, String componentName, String desireState) throws Exception {
      //安装/启动/停止/重启某个组件, 安装前请先init：
        /*put  http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts/xdata49/host_components/METRICS_MONITOR
         *  {
         *      "RequestInfo":{
         *          "context":"Install Metrics Monitor",
         *          "operation_level":{
         *              "level":"HOST_COMPONENT",
         *              "cluster_name":"xdata2",
         *              "host_name":"xdata49",
         *              "service_name":"AMBARI_METRICS"
         *          }
         *      },
         *      "Body":{
         *          "HostRoles":{"state":"INSTALLED"}
         *      }
         *  }
         */
        String url = CLUSTER_HOSTS_URL + "/" + hostName + "/host_components/" + componentName;
        Map<String, Object> entity = new HashMap<String, Object>(), requestInfo = new HashMap<String, Object>(), operationLevel = new HashMap<String, Object>(), body = new HashMap<String, Object>(), hostRoles = new HashMap<String, Object>();
        operationLevel.put("level", "HOST_COMPONENT");operationLevel.put("cluster_name", "xdata2");operationLevel.put("host_name", hostName);operationLevel.put("service_name", getComponentService(componentName));
        requestInfo.put("operation_level", operationLevel);
        requestInfo.put("context", action + " " + getComponentContext(componentName));
        entity.put("RequestInfo", requestInfo);
        hostRoles.put("state", desireState);
        body.put("HostRoles", hostRoles);
        entity.put("Body", body);
        String response = HttpTools.getInstance().putContent(url, JSONObject.toJSONString(entity));
        JSONObject respJson = JSONObject.parseObject(response);
        System.out.println(action + " " + getComponentService(componentName) + "." + componentName + " response: " + response);
        if(null != respJson && null != respJson.get("Requests") && "Accepted".equals(respJson.getJSONObject("Requests").getString("status"))) {
            return JSONObject.parseObject(response).getJSONObject("Requests").getIntValue("id");            
        } else {
            return 0;
        }
    }
    
    //给新增节点初始化组件，必须安装METRICS_MONITOR,其他可选
    /*post http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts
     * 请求参数:
     *  {
     *      "RequestInfo":{"query":"Hosts/host_name=xdata49"},
     *      "Body":{
     *          "host_components":
     *          [
     *              {
     *                  "HostRoles":{"component_name":"METRICS_MONITOR"}
     *              }
     *          ]
     *      }
     *  }
     */
    public static void hostComponentInit(String hostName, String componentName) throws Exception {
        Map<String, Object> entity = new HashMap<String, Object>(), requestInfo = new HashMap<String, Object>(), body = new HashMap<String, Object>(), component = new HashMap<String, Object>(), hostRoles = new HashMap<String, Object>();
        requestInfo.put("query", "Hosts/host_name=" + hostName);
        entity.put("RequestInfo", requestInfo);
        List<Map<String, Object>> hostComponentList = Lists.newArrayList();
        hostRoles.put("component_name", componentName);
        component.put("HostRoles", hostRoles);
        hostComponentList.add(component);
        body.put("host_components", hostComponentList);
        entity.put("Body", body);
        String response = HttpTools.getInstance().postContent(CLUSTER_HOSTS_URL, JSONObject.toJSONString(entity));
        System.out.println("init " + hostName + "." + componentName + " response:" + response);
    }
    
    public static String recheckRequest(String url) throws Exception {
        String status;
        do {
            try {
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            }
            String response = HttpTools.getInstance().getContent(url);
            JSONObject respJson = JSONObject.parseObject(response);
            status = (String)((JSONObject)respJson.get("Requests")).get("request_status");
            System.out.println("request url: " + url + "\n status=" + status);
        }while(("IN_PROGRESS".equals(status)) || ("QUEUED".equals(status)));
        return status;
    }
   
    //删除集群节点服务 
    //delete http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts/xdata49/host_components/DATANODE
    public void delete(String hostName, String componentName) throws Exception {
        String url = "http://172.18.84.67:8080/api/v1/clusters/xdata2/hosts/%s/host_components/%s";
        HttpTools.getInstance().delete(String.format(url, hostName, componentName));
    }
    
}
