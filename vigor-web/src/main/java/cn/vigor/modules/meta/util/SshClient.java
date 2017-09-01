package cn.vigor.modules.meta.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.oro.text.regex.MalformedPatternException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import cn.vigor.common.utils.AESUtil;
import expect4j.Closure;
import expect4j.Expect4j;
import expect4j.ExpectState;
import expect4j.matches.Match;
import expect4j.matches.RegExpMatch;

public class SshClient  {
	private static Logger log=Logger.getLogger(SshClient.class);
 	public static final String ENTER_CHARACTER = "\r";
	public static final String CTRL_C = "\003";
	//public static final String CENTOS_PROMPT = "(^\\[.*@.*\\][#,$] $|(?=.*)\\[.*@.*\\][#,$] $)";
	public static final String CENTOS_PROMPT = "^\\[.*@.*\\][#,$] $|(?=.*)\\[.*@.*\\][#,$] $";
   // public static final String CONTINUE_PROMPT = "(^> $|\n> $)";
	public static final String CONTINUE_PROMPT = "^> $|\n> $";
    // public static final String CONTINUE_PROMPT = "(^> $ $)";
	// 只能通过这些正则表达式判断现在是输入命令的状态，但这样无法100%正确,不支持行首行尾^$
	// 根据各自系统的提示添加匹配
	private static final String[] linuxPromptRegEx = new String[] { CENTOS_PROMPT ,CONTINUE_PROMPT};
 	private Session session = null;
	private ChannelShell channel = null;
	private ChannelSftp channelSftp = null;
	private Expect4j expect = null;
	
	//单位是秒
	private int waitTime=0;
  	boolean hasExecuted = false; // 用于判断是否有命令发送去执行，只有执行的返回才会加入outbuf
	boolean readyToInput = false; // 标记shell命令输入就绪，可以输入
	boolean longExecuted = false; // 操作时间很长命令是否结束
	
 	private String reqestOutmsg;
	private Closure closure = new Closure() {
		 private Pattern pattern = Pattern.compile(CENTOS_PROMPT);
		public void run(ExpectState expectState) throws Exception {
  				reqestOutmsg=expectState.getBuffer();
 				log.info(reqestOutmsg);
 				if(waitTime>0){
 					Matcher matcher =pattern.matcher( reqestOutmsg);
 					if(matcher.find()){
 					   waitTime=0;
 					   longExecuted=true;
 					}
 					
 				}
		}
	};
	

	// 所有要expect的pattern都添加到这单个listPattern中，默认有linux输出提示符号
	private List<Match> matchPattern = new ArrayList<Match>();


	// 用户自定义的提示输入信息，支持正则表达式
	private Map<String, String> matchInput = new HashMap<String, String>();
 
	private String userName;
 	private String password;
	private String host;
	private int port;
	private boolean isInit = false;
	
	private void init() throws IOException, Exception {
		addBindMatch(linuxPromptRegEx, closure);
 		session = new JSch().getSession(userName, host, port);
		if (password != null) {
			session.setPassword(password);
		}
		java.util.Properties config = new java.util.Properties();  
        config.put("StrictHostKeyChecking", "no");  
        session.setConfig(config);  
      
		session.connect(6000);
		session.setTimeout(6000);
		channel = (ChannelShell) session.openChannel("shell");
		//channel.
		channelSftp =(ChannelSftp) session.openChannel("sftp"); // 打开SFTP通道;
		channelSftp.connect();// 建立SFTP通道的连接
	
		expect = new Expect4j(channel.getInputStream(), channel
				.getOutputStream());
		// 取消超时机制
		expect.setDefaultTimeout(600000);
		channel.connect();
	}

	
	
	
	public SshClient(String host, int port, String userName, String password) throws PrivilegeException
			 {
		this.host = host;
		//判断是否为加密值
        if(userName!=null&& userName.length()==24 && userName.endsWith("=="))
        {
            userName=AESUtil.decForTD(userName);
        }
        if(password!=null&& password.length()==24 && password.endsWith("=="))
        {
            password=AESUtil.decForTD(password);
        }
		this.userName = userName;
		this.password = password;
		this.port = port;
		tryConnect();
	}


	/**
	 * 将promptRegEx对应的处理closure添加到listPattern中并返回
	 */

	private  void addBindMatch(String[] promptRegEx, Closure closure) {
		synchronized(linuxPromptRegEx){
			for (String regexEle : promptRegEx) {
				addBindMatch(regexEle, closure);
			}
		}
 	}

	private void addBindMatch(String promptRegEx, Closure closure) {
		try {
			matchPattern.add(new RegExpMatch(promptRegEx, closure));
 		} catch (MalformedPatternException e) {
			log.info(promptRegEx);
			log.info("addBindMatchError:",e);
		}
	}

	public void addBindMatch(String promptRegEx) {
		try {
			matchPattern.add(new RegExpMatch(promptRegEx, closure));
		} catch (Exception e) {
			log.info("addBindMatchError:",e);
		}
	}

	public void delBindMatch(String promptRegEx) {
		 List delList = new ArrayList();
		for (Match match : matchPattern) {
			//log.info("promptRegEx:"+((RegExpMatch) match).getPattern());
			if (((RegExpMatch) match).getPattern().getPattern().equals(promptRegEx)) {
				delList.add(match);
				
			}
		}
		matchPattern.removeAll(delList);
	}

	/**
	 * 用户自定义添加提示pattern和对应的输入
	 */
	public void addExpectInput(String patternRegEx, String input) {
		matchInput.put(patternRegEx, input);
		addBindMatch(patternRegEx);
	}

	/**
	 * 用户自定义删除提示pattern和对应的输入
	 */
	public void delExpectInput(String patternRegEx) {
		matchInput.remove(patternRegEx);
		delBindMatch(patternRegEx);
	}

	/**
	 * 清空用户自定义提示pattern
	 */
	public void clearExpectInput() {
 		for (String key : matchInput.keySet()) {
			delBindMatch(key);
			
		}
		matchInput.clear();
	}
	
	public void testConnect() throws Exception
	{
		init();
	}
	/**
	 * 单线程调用，返回null表示执行失败
	 * @throws Exception 
	 */
	public String execute(String cmd) throws Exception {
		log.info("["+host+"]---执行 cmd:"+cmd+"------start------");
		tryConnect();
		try {
			cmd = cmd == null ? "" : cmd;
			hasExecuted = false;
			boolean executeEnd = false;
			while (!executeEnd) {
				if (!readyToInput) {
					while(this.waitTime>0){
						 expect.expect(matchPattern);
						 waitTime--;
						Thread.sleep(3000);
						if(this.longExecuted){
							log.info("长时间安装退出");
							waitTime=0;
							readyToInput=true;
							return this.reqestOutmsg;
							
						}
					}
					int ret = expect.expect(matchPattern);
					if (ret < 0) {
						log.info("ret:" + ret);
						String pattern = expect.getLastState().getMatch();
						log.info("errro:"+pattern);
						return reqestOutmsg;
					}
					// 通过判断pattern来确定输入，如果在用户自定义的输入中
					String pattern = expect.getLastState().getMatch();
					log.info("命令返回输入提示:" + pattern);
			        boolean isPrompt = true;
			        //用户切rq
			         for (String key : matchInput.keySet()) {
						if (Pattern.compile(key).matcher(pattern).find()) {
							if(".*".equals(key)){
								break;
							}
							log.info("执行cmd:" + matchInput.get(key));
							expect.send(matchInput.get(key));
							expect.send(ENTER_CHARACTER);
							isPrompt = false;
							//delExpectInput(key);
							break;
						}
					}
					if (isPrompt) {
						readyToInput = true;
					}
				} else {
					if (hasExecuted) {
						executeEnd = true;
						this.clearExpectInput();
					} else {
						log.info("cmd:" + cmd);
						expect.send(cmd);
						expect.send(ENTER_CHARACTER);
						readyToInput = false; // 一次readyToInput发送一次命令
						hasExecuted = true;
					}
				}
			}
			log.info("["+host+"]---执行 cmd:"+cmd+"---end-----");
			
		} catch (Exception e) {
			 log.error("命令执行异常", e);
			 e.printStackTrace();
			 throw e;
		}
		return reqestOutmsg;
	}
	/**
	 * 将本地文件名为src的文件上传到目标服务器，目标文件名为dst，若dst为目录，则目标文件名将与src文件名相同。
	 * 采用默认的传输模式：OVERWRITE
	 * @param src
	 * @param dst
	 * @return
	 * @throws SftpException 
	 * @throws PrivilegeException 
	 * @throws Exception 
	 * @throws IOException 
	 */
	public void upFile(String src,String dstPath,String fileName) throws SftpException, PrivilegeException{
		tryConnect();
		try{
			channelSftp.cd(dstPath);
		}catch(SftpException sftpException){
			log.info(sftpException.toString());
			try {
				channelSftp.mkdir(dstPath);
			} catch (SftpException e) {
				e.printStackTrace();
			}
			log.info("mkdir："+dstPath);
 		}
		try {
			Vector<ChannelSftp.LsEntry> vector =  channelSftp.ls(dstPath);
			if(vector==null){
				return ;
			}
			
			ListIterator<ChannelSftp.LsEntry>  iterators= vector.listIterator();
			while(iterators.hasNext()){
				ChannelSftp.LsEntry lsEntry= iterators.next();
				String fileNameTemp=lsEntry.getLongname();
				//log.info(fileNameTemp+"  "+fileName);
				if(fileNameTemp.indexOf(fileName)>=0){
					return ;
				}
			}
			src = src+fileName;
			log.info("upFile,file："+src);
			long t1=System.currentTimeMillis();
			this.channelSftp.put(src, dstPath+fileName);	
			log.info("upFile succes time:"+(System.currentTimeMillis()-t1)+",targert:"+ dstPath+fileName);
		} catch (SftpException e) {
			 log.error("文件上传异常", e);
			 e.printStackTrace();
			 throw e;
		}
 	}
	
	public void upFile(InputStream is,String dstFile) throws Exception{
		tryConnect();
		try {
			channelSftp.put(is, dstFile);
		} catch (Exception e) {
			 log.error("文件上传异常", e);
			e.printStackTrace();
			throw e;
		}
	 }
	public void upFile(String context,String dstFile) throws Exception{
		ByteArrayInputStream byteStream = new ByteArrayInputStream(context.getBytes());
		upFile(byteStream,dstFile);
	 }

	public String getFile(String filePath) throws SftpException, PrivilegeException{
		InputStream io=getFileStream(filePath);
		String file=null;
		if(io!=null)
		{
			file=new String( getByteByStream(io));
		}
 		return  file;
	 }
	

	public InputStream getFileStream(String filePath) throws SftpException, PrivilegeException{
		tryConnect();
		filePath=filePath.trim();
		log.info("loadFile:"+filePath);
		InputStream io=null;
		try {
			io= channelSftp.get(filePath);
		} catch (SftpException e) {
		 log.info(filePath+":file is not exist！"+e.getMessage());
		}
		return io;
	}
	 private  byte[] getByteByStream(InputStream is)
	  {
		 
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        if(is!=null){
	        	try
	        	{
	        		byte[] buf = new byte[1024];
	        		int num;
	        		while ((num = is.read(buf, 0, buf.length)) != -1)
	        		{
	        			out.write(buf, 0, num);
	        		}
	        	}
	        	catch (IOException e)
	        	{
	        		log.error("Failed read data from inputstram.", e);
	        	}
	        	finally
	        	{
	        		if(is!=null){
	        			try {
	        				is.close();
	        			} catch (IOException e) {
	        				e.printStackTrace();
	        			}
	        		}
	        	} 	
	        }
	        return out.toByteArray();
	    }
	
	public void closeConnection() {
		if (expect != null) {
			expect.close();
			expect=null;
		}
		if (channel != null) {
			channel.disconnect();
			channel=null;
		}
		if (this.channelSftp != null) {
			channelSftp.disconnect();
			channelSftp=null;
		}
		if (session != null) {
			session.disconnect();
			session=null;
		}
		//log.info("close "+host);
	}
	
	/**
	 * 执行一行代码，然后关闭连接
	 * @throws Exception 
	 */
	public String executeOnce(String cmd) throws Exception{
		StringBuilder sb;
		int exitStatus;
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(userName, host, 22);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();  
			config.put("StrictHostKeyChecking", "no");  
			session.setConfig(config);  
			session.connect();
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(cmd);
			((ChannelExec) channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			channel.connect();
			 
			sb = new StringBuilder();
			byte[] buf = new byte[1024];
			exitStatus = 0;
			while (true) {
				while (in.available() > 0) {
					int len = in.read(buf, 0, buf.length);
					if (len < 0)
						break;
					String res=new String(buf, 0, len);
					res.trim();
				  //  log.info(res);
				}
				if (channel.isClosed()) {
					exitStatus = channel.getExitStatus();
					break;
				}
				// 异步io轮询
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			}
			channel.disconnect();
			session.disconnect();
			sb.append(exitStatus);
			if (exitStatus != 0) {
				return null;
			} else {
				return sb.toString();
			}
		} catch (Exception e) {
			 log.error("命令执行异常", e);
			 e.printStackTrace();
			 throw e;
		}
	}

	/**
	 * 尝试连接.......
	 * @throws PrivilegeException 
	 */
	private void tryConnect() throws PrivilegeException{
		for (int i = 1; i < 3; i++) {
			if(isInit)
			{
				break;
			}
			try {
				init();
				isInit = true;
			} catch (Exception e) {
				 log.error("连接异常，网络通信异常，3秒后尝试重新连接第"+i+"次", e);
				 closeConnection();
				 try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(!isInit){
			 throw new PrivilegeException("link_error:","服务器连接不上，可能服务器不存在，或用户,密码错误!");
		}
	}
	
	
	
	public static void main(String[] args)  {
		SshClient sshClient = null;
		
		
		try {
			//第一种方式
			sshClient = new SshClient("192.168.7.22", 22, "yarn",
					"yarn");
			String hivesql="source /etc/profile;hive -e \" create table if not exists t_sys_permission (permission_id int,action_name string,action_class string,permission_alias string,menu_name string,menu_url string,father_name string,father_url string,permission_type int,permission_name string,menu_index int,menu_type int,create_time date,create_user string,update_time date)ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\;' STORED AS TEXTFILE \" " ;
			 String res=sshClient.executeOnce(hivesql);
			 System.out.println("result:"+res);
			//sshClient.execute("ll");
			//sshClient.executeOnce("cd /home/richdata_etl/richdata_etlserver/\n./stopClusterAll.sh");
			/*cd /home/yarn/storm-script/storm-script/user/\n./stopClusterAll.sh
			//E:\MyTools\CodeTools\apache-tomcat-7.0.28\webapps\richdata_install2.6\WEB-INF\classes\..\..\..\..\resource\richdata_basic\vsftpd-2.2.2-11.el6.x86_64.rpm
			
			String file="E:/MyTools/CodeTools/apache-tomcat-7.0.28/webapps/richdata_install2.6/WEB-INF/classes/../../../../../resource/richdata_basic/";
			String realpath=new File(file).getCanonicalPath();
			System.out.println(realpath);*/
		/*	String desfile="vsftpd-2.2.2-11.el6.x86_64.rpm";
			InstallMsgBean InstallMsgBean=new InstallMsgBean();
			sshClient.setInstallMsgBean(InstallMsgBean);
			sshClient.upFile(file, "/root/", desfile);*/
			/*sshClient.execute("cd /home/kettle/etlserver");
			sshClient.execute("nohup ./carte.sh 192.168.7.128 15100 >startkittle.log 2>&1 &");*/
			/* sshClient.execute("cd /home/yarn/");
			 String folename=IntallUtil.unzip(sshClient, "rscript.tar.gz");
			 String bindir="cd /home/yarn/"+folename;
			 String setenv=bindir+"\n./sethost.sh root 123456 rhel212";
			 sshClient.executeOnce(setenv);
			 
			 sshClient.executeOnce(bindir+"\n./install-deps.sh rpmlist");
			 sshClient.executeOnce(bindir+"\n./install-r.sh");
			//第二种方式 
			sshClient = new SshClient("192.168.40.213", 22, "root",
					"123456");
			sshClient.execute("cd /home/yarn/");
			
			String folename=IntallUtil.unzip(sshClient, "rscript.tar.gz");
			String bindir="cd /home/yarn/"+folename;
			String setenv=bindir+"\n./sethost.sh root 123456 rhel213";
			sshClient.executeOnce(setenv);
			sshClient.executeOnce(bindir+"\n./install-jdk.sh");
			sshClient.executeOnce(bindir+"\n./install-deps.sh rpmlist");
			sshClient.executeOnce(bindir+"\n./install-r.sh");
			sshClient.executeOnce(setenv+"\n./install-rattle.sh"); */
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
				// 正常命令测试
				//System.out.print(ssh.execute("sqoop import --connect jdbc:mysql://192.168.9.29:3306/piwik --username piwiki --password  piwiki_user --table piwik_log_action -m -1"));
		//		System.out.print(ssh.execute("echo hello world"));
		//		System.out.print(ssh.execute("echo -n hello world"));
		//
		//		// 连接测试
		//		ssh.addExpectInput("(yes/no)", "yes");
		//		ssh.addExpectInput(".*password", "123456");
		//		System.out.print(ssh.execute("ssh root@localhost"));
		//
		//		// 睡眠等待测试
		//		System.out.print(ssh.execute("sleep 2"));
		//
		//		// 多行命令测试
		//		System.out.print(ssh.execute("for((i=0;i<3;i++))"));
		//		System.out.print(ssh.execute("do"));
		//		System.out.print(ssh.execute("  echo $i"));
		//		System.out.print(ssh.execute("done"));
		//
		//		// 错误信息测试
		//		System.out.print(ssh.execute("zz"));
		
				// 关闭连接
		sshClient.closeConnection();	
	}

	public Expect4j getExpect() {
		return expect;
	}

	public void setExpect(Expect4j expect) {
		this.expect = expect;
	}
	
	public String getPassword() {
		return password;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}
	
	public void setLog(Logger log) {
 		this.log = log;
	}
	public Map<String, String> getMatchInput() {
		return matchInput;
	}
}

