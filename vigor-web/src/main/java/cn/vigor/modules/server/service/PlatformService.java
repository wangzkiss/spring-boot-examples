package cn.vigor.modules.server.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.API.util.HttpTools;
import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.meta.util.PrivilegeException;
import cn.vigor.modules.meta.util.SshClient;
import cn.vigor.modules.server.dao.PlatformDao;
import cn.vigor.modules.server.dao.PlatformNodeDao;
import cn.vigor.modules.server.entity.ClusterParam;
import cn.vigor.modules.server.entity.Platform;
import cn.vigor.modules.server.entity.PlatformNode;
import net.sf.json.JSONObject;

/**
 * 集群信息Service
 * @author kiss
 * @version 2016-06-30
 */
@Service
@Transactional(readOnly = true)
public class PlatformService extends CrudService<PlatformDao, Platform>
{
    
    @Autowired
    private PlatformNodeDao platformNodeDao;
    
    public Platform get(String id)
    {
        Platform platform = super.get(id);
        List<PlatformNode> nodeList = platformNodeDao.findList(new PlatformNode(
                platform));
        if(platform!=null)
        {
            platform.setPlatformNodeList(nodeList);
        }
        /*for (PlatformNode platformNode : nodeList)
        {
            platformNode.setPlatformId(platform);
        }*/
        return platform;
    }
    
    
    public Platform getOne(String clusterName)
    {
        Platform param =new  Platform();
        param.setPlatformName(clusterName);
        Platform platform = dao.getOne(param);
        return platform;
    }
    
    public List<Platform> findList(Platform platform)
    {
        return super.findList(platform);
    }
    
    public Page<Platform> findPage(Page<Platform> page, Platform platform)
    {
        return super.findPage(page, platform);
    }
    
    @Transactional(readOnly = false)
    public void saveCluster(Platform platform) throws Exception
    {
        super.save(platform);
        for (PlatformNode platformNode : platform.getPlatformNodeList())
        {
            if (platformNode.getId() == null)
            {
                continue;
            }
            if (PlatformNode.DEL_FLAG_NORMAL.equals(platformNode.getDelFlag()))
            {
                
                if (StringUtils.isBlank(platformNode.getId()))
                {
                    platformNode.setPlatformId(platform);
                    platformNode.preInsert();
                    if (2 == platform.getPlatformType())
                    {//ETL集群
                        platformNode.setNodeUrl("http://"
                                + platformNode.getNodeIp() + ":"
                                + platformNode.getNodePort());
                        addClusterNode(platformNode, platform);
                    }
                    String password=platformNode.getNodePassword();
                    if(password!=null && password.length()==24 && password.endsWith("=="))
                    {
                       
                    }else{//不是密文加密
                        password=AESUtil.encForTD(password);
                    }
                    platformNodeDao.insert(platformNode);
                }
                else
                {
                    if (2 == platform.getPlatformType())
                    {//ETL集群
                        platformNode.setNodeUrl("http://"
                                + platformNode.getNodeIp() + ":"
                                + platformNode.getNodePort());
                    }
                    platformNode.preUpdate();
                    String password=platformNode.getNodePassword();
                    if(password!=null && password.length()==24 && password.endsWith("=="))
                    {
                       
                    }else{//不是密文加密
                        password=AESUtil.encForTD(password);
                    }
                    platformNode.setNodePassword(password);
                    platformNodeDao.update(platformNode);
                }
            }
            else
            {
                if (2 == platform.getPlatformType())
                {//ETL集群
                    deleteClusterNode(platformNode, platform);
                }
                platformNodeDao.delete(platformNode);
                
            }
        }
    }
    
    @Transactional(readOnly = false)
    public void delete(Platform platform)
    {
        super.delete(platform);
        platformNodeDao.delete(new PlatformNode(platform));
    }
    
   
    
    public final Log log = LogFactory.getLog(this.getClass());
    
    /**
     * 
     * @param platform
     * @param op 1 启动  2 停止
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject start(Platform platform, int op)
    {
        
        String startEtl=Global.getConfig("etl_server_home")+Global.ETL_FILENAME+"/startClusterAll.sh";
        String stopEtl=Global.getConfig("etl_server_home")+Global.ETL_FILENAME+"/stopClusterAll.sh";
        JSONObject data = new JSONObject();
        String shell = "";
        String opdesc = "";
        if (1 == op)
        {
            shell = startEtl;
            opdesc = "启动";
        }
        else
        {
            shell = stopEtl;
            opdesc = "停止";
        }
        boolean flag = true;
        String msg = "";
        try
        {
            SshClient sshClient = null;
            for (PlatformNode node : platform.getPlatformNodeList())
            {
                sshClient = new SshClient(node.getNodeIp(), 22,
                        node.getNodeUser(), node.getNodePassword());
                log.info("操作命令：" + startEtl);
                msg = sshClient.executeOnce("source /etc/profile;" + shell);
                if ("0".equals(msg))
                {
                    flag = true;
                    msg = node.getNodeName() + opdesc + "成功";
                }
                else
                {
                    flag = false;
                    msg = node.getNodeName() + opdesc + "失败";
                }
                flag = flag & flag;
                String nodeIp = node.getNodeIp();
                int port = node.getNodePort();
                String url = "http://" + nodeIp + ":" + port + "/";//"kettle/status/?xml=y";
                String content = HttpTools.postCluster(url, "");
                int num = 10;
                if (op == 1)
                {
                    while (StringUtils.isEmpty(content) && num > 0)
                    {
                        content = HttpTools.postCluster(url, "");
                        Thread.sleep(1000);
                        num--;
                    }
                } else {
                    while (!StringUtils.isEmpty(content) && num > 0)
                    {
                        content = HttpTools.postCluster(url, "");
                        Thread.sleep(1000);
                        num--;
                    }
                }
                
                if (StringUtils.isEmpty(content))
                {
                    node.setNodeState(0);
                    platformNodeDao.updateStatus(node);
                    platform.setPlatformState(0);
                    dao.updateStatus(platform);
                }
                else
                {
                    node.setNodeState(1);
                    platformNodeDao.updateStatus(node);
                    platform.setPlatformState(1);
                    dao.updateStatus(platform);
                }
            }
            
        }
        catch (Exception e)
        {
            data.put("msg", e.getMessage());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        data.put("msg", msg);
        data.put("sucess", flag);
        
        return data;
    }
    
    public PlatformNode getNode(String clasterId)
    {
        return platformNodeDao.get(clasterId);
    }
    
    public List<Map<String, String>> findEtlMetrics(ClusterParam param)
    {
        return dao.etlclusterFields(param);
    }
    

     
   
    
 
    private String serverHome=Global.getConfig("etl_server_home");
    private String serverFile=Global.ETL_FILENAME;
    private String rootpwd=Global.getConfig("etl_server_rootpwd");
    
    private void deleteClusterNode(PlatformNode node, Platform platform)
            throws Exception
    {
        if (node.getNodeType() == 2)
        {
            SshClient nodeClient = null;
            try
            {
                if (serverHome != null && serverFile != null)
                {
                    String nodeUser = node.getNodeUser();
                    String nodePwd = node.getNodePassword();
                    String nodeIp = node.getNodeIp();
                    
                    String addshell = "rm -rf " + serverHome + serverFile;
                    log.info("shell:" + addshell);
                    nodeClient = new SshClient(nodeIp, 22, nodeUser, nodePwd);
                    String res = nodeClient.executeOnce(addshell);
                    if (!"0".equals(res))
                    {
                        throw new PrivilegeException("cluster:", "集群"
                                + platform.getPlatformName() + "的节点文件拷贝失败");
                    }
                }
                else
                {
                    throw new PrivilegeException("error:", "集群"
                            + platform.getPlatformName() + "缺少基础配置，暂不支持此操作!");
                }
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                
                if (nodeClient != null)
                {
                    nodeClient.closeConnection();
                }
            }
        }
        else
        {
            throw new PrivilegeException("cluster:", "集群"
                    + platform.getPlatformName() + "，不支持删除主节点！");
        }
    }
    
    private void addClusterNode(PlatformNode node, Platform platform)
            throws Exception
    {
        if (node.getNodeType() == 2)
        {
            String user = Global.getConfig("etl_root");
            String pwd = Global.getConfig("etl_server_rootpwd");
            String serviceIp= Global.getConfig("etl_master_ip");
            SshClient sshClient = null;
            SshClient nodeClient = null;
            SshClient sshClient2 = null;
            try
            {
                if (serviceIp != null && serverHome != null
                        && serverFile != null)
                {
                    sshClient = new SshClient(serviceIp, 22, user, pwd);
                    String shelldir = PlatformService.class.getResource("/")
                            .getPath();
                    log.info("path:" + shelldir);
                    
                    sshClient.upFile(shelldir, serverHome, "addclusternode.sh");
                    sshClient.upFile(shelldir, serverHome, "scp-files.exp");
                    sshClient.execute("cd " + serverHome);
                    sshClient.execute("chmod u+x scp-files.exp addclusternode.sh");
                    String nodeUser = node.getNodeUser();
                    String nodePwd = node.getNodePassword();
                    String nodeIp = node.getNodeIp();
                    
                    String addshell = "./addclusternode.sh " + nodeIp + " "
                            + nodeUser + " " + nodePwd + " " + serverFile + " "
                            + serverHome;
                    
                    sshClient2 = new SshClient(nodeIp, 22, "root", rootpwd);
                    sshClient2.execute("mkdir -p " + serverHome);
                    sshClient2.execute("chown " + nodeUser + ":" + nodeUser
                            + " " + serverHome);
                    log.info("shell:" + addshell);
                    
                    nodeClient = new SshClient(nodeIp, 22, nodeUser, nodePwd);
                    
                    String res = sshClient.executeOnce("source /etc/profile;"
                            + "cd " + serverHome + "\n" + addshell);
                    if (!"0".equals(res))
                    {
                        throw new PrivilegeException("cluster:", "集群"
                                + platform.getPlatformName() + "的节点文件拷贝失败");
                    }
                }
                else
                {
                    throw new PrivilegeException("error:", "集群"
                            + platform.getPlatformName() + "缺少基础配置，暂不支持此操作!");
                }
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                if (sshClient != null)
                {
                    sshClient.closeConnection();
                    nodeClient.closeConnection();
                }
                if (nodeClient != null)
                {
                    sshClient.closeConnection();
                    nodeClient.closeConnection();
                }
                if (sshClient2 != null)
                {
                    sshClient2.closeConnection();
                }
            }
        }
        else
        {
            throw new PrivilegeException("cluster:", "集群"
                    + platform.getPlatformName() + "不支持增加主节点！");
        }
    }
    
    public Page<PlatformNode> findPage(Page<PlatformNode> page,
            PlatformNode platform)
    {
        platform.setPage(page);
        platform.preInsert();
        page.setList(platformNodeDao.findList(platform));
        return page;
    }
    @Transactional(readOnly = false)
    public void delete(PlatformNode platform)
    {
        platformNodeDao.delete(platform);
    }
    @Transactional(readOnly = false)
    public void saveEtlNode(Platform platform, PlatformNode platformNode) throws Exception
    {
        addClusterNode(platformNode, platform);
        platformNode.preInsert();
        platformNodeDao.insert(platformNode);
    }

    public PlatformNode getEtlNodeById(String id)
    {
        return  platformNodeDao.get(id);
    }
    public JSONObject findClusterStatus(String clsterId)
    {
        JSONObject data=new JSONObject();
        data.put("ServiceComponentInfo", JSONObject.fromObject(dao.findClusterStatus(clsterId)));
        return data;
    }
    public JSONObject findJobInfo()
    {
        Platform entity=new Platform();
        entity.preInsert();
        return JSONObject.fromObject(dao.findJobStatus(entity));
    }
    
    public void savePlatformNode(PlatformNode platformNode){
    	if(platformNode.getId()!=null){
    		platformNode.preUpdate();
    		platformNodeDao.update(platformNode);
    	}else{
    		platformNode.preInsert();
            platformNodeDao.insert(platformNode);
    	}
    }
}
