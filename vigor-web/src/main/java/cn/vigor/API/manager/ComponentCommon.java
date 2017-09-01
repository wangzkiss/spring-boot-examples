package cn.vigor.API.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import cn.vigor.API.model.metrics.common.Path;
import cn.vigor.API.model.metrics.common.Value;
import cn.vigor.API.util.Handler;
import cn.vigor.API.util.HttpTools;
import cn.vigor.API.util.JSONTools;
import cn.vigor.API.util.ManagerHandler;

public abstract class ComponentCommon extends ManagerCommon {
	
	public ComponentCommon(String host, String port, String clustername,
			String name,String componentType) {
		super(host, port, clustername, name);
		this.componentType = componentType;
	}

	public ComponentCommon(String host, String port, String clustername,
			String name, String targethost,String componentType) {
		super(host, port, clustername, name);
		this.targethost = targethost;
		this.componentType = componentType;
	}
	
	protected String targethost;
	protected String componentType;

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getTargethost() {
		return targethost;
	}

	public void setTargethost(String targethost) {
		this.targethost = targethost;
	}

	protected String getEntity(String st){
		 Map<String,Map> hostRoles = new HashMap<String,Map>();
		 Map<String,String> state = new HashMap<String,String>();
		 state.put("state", st);
		 hostRoles.put("HostRoles", state);
		 String entityString = JSONTools.toJson(hostRoles);
		 return entityString;
	}
	
	protected String getContent(String component,String state) throws ClientProtocolException, UnsupportedEncodingException, IOException{
		 String url = ManagerHandler.returndaemonsUrl(host, port, clustername, targethost,component);
		 String entityString = getEntity(state);
		 String ct = HttpTools.getInstance().putContent(url, entityString);
		 return ct;
	}
	
	/**
	 * 添加组件，add之后还要调用stop，start一下。 
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public  String add() throws ClientProtocolException, UnsupportedEncodingException, IOException{
		 String url =  ManagerHandler.returndaemonsUrl(host, port, clustername, targethost, this.getName());
		 String content = HttpTools.getInstance().postContent(url);
		 return content;
	}
	
	/**
	 * 
	 * @param lspath
	 * 	Path p1 = new Path();
		p1.setPath("metrics/rpc/client/NumOpenConnections");
		p1.setStarttime(starttime);
		p1.setEndtime(endtime);
		p1.setStep(point);
		lspath.add(p1);
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Map<String,List<Value>> getRangeComponnetMetric(List<Path> lspath) throws ClientProtocolException, UnsupportedEncodingException, IOException, URISyntaxException{
		 String url =  Handler.returnRangeComponentUrl(host, port, componentType, clustername, this.getName(), lspath);
		 String content =  HttpTools.getInstance().getContent(url);
		 Map map =  JSONTools.fromJson(content,HashMap.class);
		 Map<String,List<Value>> mr = Handler.handleListResult(map, lspath);
		 return mr;
	}
	
	/**
	 * 	Path p1 = new Path();
		p1.setPath("metrics/rpc/client/NumOpenConnections");
		Path p2 = new Path();
		p2.setPath("metrics/rpc/client/RpcProcessingTimeAvgTime");
	 * @param lspath
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Map<String,String> getComponnetMetric(List<Path> lspath) throws ClientProtocolException, UnsupportedEncodingException, IOException, URISyntaxException{
		 String url =  Handler.returnRangeComponentUrl(host, port, componentType, clustername, this.getName(), lspath);
		 String content =  HttpTools.getInstance().getContent(url);
		 Map map =  JSONTools.fromJson(content,HashMap.class);
		 Map<String,String> mr = Handler.handleResult(map, lspath);
		 return mr;
	}
	
	/**
	 * Path p1 = new Path();
		p1.setPath("metrics/dfs/FSNamesystem/CapacityUsedGB");
		p1.setStarttime(starttime);
		p1.setEndtime(endtime);
		p1.setStep(point);
		Path p2 = new Path();
		p2.setPath("metrics/dfs/FSNamesystem/BlocksTotal");
		p2.setStarttime(starttime);
		p2.setEndtime(endtime);
		p2.setStep(point);
		List<Path> lspath = new ArrayList<Path>();
	 * @param lspath
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Map<String,List<Value>> getRangeHostComponentMetric(List<Path> lspath) throws ClientProtocolException, UnsupportedEncodingException, IOException, URISyntaxException{
		 String url =  Handler.returnRangeHostComponentUrl(host, port, targethost, clustername, this.getName(), lspath);
		 String content =  HttpTools.getInstance().getContent(url);
		 Map map =  JSONTools.fromJson(content,HashMap.class);
		 Map<String,List<Value>> mr = Handler.handleListResult(map, lspath);
		 return mr;
	}
	
	/**
	 * 
	 * @param lspath
	 * 	Path p1 = new Path();
		p1.setPath("metrics/dfs/FSNamesystem/CapacityUsedGB");
		Path p2 = new Path();
		p2.setPath("metrics/dfs/FSNamesystem/BlocksTotal");
		
		List<Path> lspath = new ArrayList<Path>();
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Map<String,String> getHostComponentMetric(List<Path> lspath) throws ClientProtocolException, UnsupportedEncodingException, IOException, URISyntaxException{
		 String url =  Handler.returnHostComponentUrl(host, port, targethost, clustername, this.getName(), lspath);
		 String content =  HttpTools.getInstance().getContent(url);
		 Map map =  JSONTools.fromJson(content,HashMap.class);
		 Map<String,String> mr = Handler.handleResult(map, lspath);
		 return mr;
	}
}
