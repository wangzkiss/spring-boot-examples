package cn.vigor.API.ambariAgent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import cn.vigor.API.model.ambariAgent.CheckAgentStatus;
import cn.vigor.API.model.ambariAgent.InstallAgentStatus;
import cn.vigor.API.util.AmbariAgentHandler;
import cn.vigor.API.util.HttpTools;
import cn.vigor.API.util.JSONTools;

public class AmbariAgentManager {
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @param sshKey sshkey
	 * @param user   sshkey 对应的user
	 * @param hosts  需要安装agent的机器列表
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static InstallAgentStatus installAgents(String host,String port,String sshKey,String user,List<String> hosts) throws ClientProtocolException, UnsupportedEncodingException, URISyntaxException, IOException{
		String url = AmbariAgentHandler.returnInstallAgentUrl(host, port);
		Map<String,String> header = new HashMap<String,String>();
		header.put("Content-Type", "application/json");
		String entityString = getEntity(sshKey,user,hosts);
		String content = HttpTools.getInstance().postContent(url, entityString, header);
		InstallAgentStatus installAgentStatus = parserInstallAgentStatus(content);
		return installAgentStatus;
	}
	
	
	
	/**
	 * 查看安装进度和状态
	 * 
	 * @param host
	 * @param port
	 * @param id  requestid
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static CheckAgentStatus recheckStateById(String host, String port, String id) throws ClientProtocolException,
			UnsupportedEncodingException, URISyntaxException, IOException {
		String url = AmbariAgentHandler.returnCheckStatusUrl(host, port,id);
		String content = HttpTools.getInstance().getContent(url);
		CheckAgentStatus checkAgentStatus = parserCheckAgentStatus(content);
		return checkAgentStatus;
	}
	
	
	private static CheckAgentStatus parserCheckAgentStatus(String content){
		CheckAgentStatus m = JSONTools.fromJson(content, CheckAgentStatus.class);
		return m;
	}
	
	
	private static InstallAgentStatus parserInstallAgentStatus(String content){
		Map m = JSONTools.fromJson(content, Map.class);
		String status = (String) m.get("status");
		String log = (String) m.get("log");
		String requestId = (String) m.get("requestId");
		InstallAgentStatus installAgentStatus = new InstallAgentStatus();
		installAgentStatus.setStatus(status);
		installAgentStatus.setLog(log);
		installAgentStatus.setRequestId(requestId);
		return installAgentStatus;
	}
	
	private static String getEntity(String sshKey, String user,List<String> hosts) {
		Map<String,Object> etity = new HashMap<String,Object>();
		etity.put("verbose", "true");
		etity.put("sshKey", sshKey);
		etity.put("hosts", hosts);
		etity.put("user", user);
		String entityString = JSONTools.toJson(etity);
		return entityString;
	}

}
