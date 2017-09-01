package cn.vigor.API.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import cn.vigor.API.model.metrics.StateDic;
import cn.vigor.API.util.JSONTools;


public abstract class ManagerCommon {
	protected String host;
	protected String port;
	protected String clustername;
	protected String name;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getClustername() {
		return clustername;
	}

	public void setClustername(String clustername) {
		this.clustername = clustername;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public ManagerCommon(String host, String port, String clustername) {
		super();
		this.host = host;
		this.port = port;
		this.clustername = clustername;
	}
	
	public ManagerCommon(String host, String port, String clustername,
			String name) {
		super();
		this.host = host;
		this.port = port;
		this.clustername = clustername;
		this.name = name;
	}

	protected abstract String getContent(String component,String state)throws ClientProtocolException, UnsupportedEncodingException, IOException;
	
	/**
	 * 启动节点，启动完以后，需要监控状态 CheckState checkState = StatusManager.recheckStateById(host,port,clustername,datanode.getID(content)
	 * @return  可以调用对应组件的getID和getHref和getState再结合statusManager类获得状态
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public  String start() throws ClientProtocolException, UnsupportedEncodingException, IOException{
		 String ct =  getContent(this.getName(),StateDic.STATE_STARTED);
		 return ct;
	}
	
	/**
	 * 停止节点，停止完后，需要监控状态，CheckState checkState = StatusManager.recheckStateById(host,port,clustername,datanode.getID(content)
	 * @return  可以调用对应组件的getID和getHref和getState再结合statusManager类获得状态
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public  String stop() throws ClientProtocolException, UnsupportedEncodingException, IOException  {
		 String ct =  getContent(this.getName(),StateDic.STATE_INSTALLED);
		 return ct;
	}
	
	public  String getHref(String content){
		 Map m = JSONTools.fromJson(content, Map.class);
		 String[] keys = new String[1];
		 keys[0]="href";
		 String href = JSONTools.search(m, keys, 0, keys.length);
		 return href;
	}
	
	public String getState(String content){
		 Map m = JSONTools.fromJson(content, Map.class);
		 String[] keys = new String[2];
		 keys[0]="Requests";
		 keys[1]="status";
		 String href = JSONTools.search(m, keys, 0, keys.length);
		 return href;
	}
	
	public String getID(String content){
		 Map m = JSONTools.fromJson(content, Map.class);
		 String[] keys = new String[2];
		 keys[0]="Requests";
		 keys[1]="id";
		 String href = JSONTools.search(m, keys, 0, keys.length);
		 int id = (int) Float.parseFloat(href);
		 return String.valueOf(id);
	}

	public static void main(String[] args){
		String s = "1.0";
		int a = (int) Float.parseFloat(s);
		System.out.println(a);
	}
}
