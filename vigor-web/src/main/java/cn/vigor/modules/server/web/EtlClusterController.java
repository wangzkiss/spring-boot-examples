package cn.vigor.modules.server.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.server.entity.ClusterParam;
import cn.vigor.modules.server.entity.Platform;
import cn.vigor.modules.server.entity.PlatformNode;
import cn.vigor.modules.server.entity.ServerInfos;
import cn.vigor.modules.server.service.PlatformService;
import cn.vigor.modules.server.service.ServerInfosService;
import cn.vigor.modules.server.util.MetricsUtil;
import net.sf.json.JSONObject;

/**
 * 集群信息Controller
 * @author kiss
 * @version 2016-06-30
 */
@Controller
@RequestMapping(value = "${adminPath}/server/platform")
public class EtlClusterController extends BaseController
{
    
    @Autowired
    private PlatformService platformService;
    
    @Autowired
    private ServerInfosService serverInfosService;
    
     
     @ModelAttribute
     public Platform get(@RequestParam(required = false)
     String id)
     {
         Platform entity = null;
         if (StringUtils.isNotBlank(id))
         {
             entity = platformService.get(id);
         }
         if (entity == null)
         {
             entity = new Platform();
         }
         return entity;
     }
    /**
     * 查看，增加，编辑集群信息表单页面
     */
    @RequiresPermissions(value = { "server:platform:view",
            "server:platform:add", "server:platform:edit" }, logical = Logical.OR)
    @RequestMapping(value = "etlform")
    public String form(Platform platform, Model model)
    {
        model.addAttribute("platform", platform);
        return "modules/cluster/etlForm";
    }
    
    /**
     * 查看，增加，编辑集群信息表单页面
     */
    @RequiresPermissions(value = { "server:platform:view",
            "server:platform:add", "server:platform:edit" }, logical = Logical.OR)
    @RequestMapping(value = "etlNodeform")
    public String nodeform(PlatformNode platformNode, Model model)
    {
        model.addAttribute("platform", platformNode);
        List<ServerInfos>  newips=new ArrayList<ServerInfos>();
        List<ServerInfos>  ips=serverInfosService.findNewByServerName("etl_cluster");
        Platform platform =platformService.get("13");
        if(platform!=null&&platform.getPlatformNodeList()!=null)
        {
            for (ServerInfos serverInfos : ips)
            {
              boolean flag =true; 
              b1:  for (PlatformNode node : platform.getPlatformNodeList())
                {
                    if(serverInfos.getIpv4().equals(node.getNodeIp()))
                    {
                        flag=false;
                        break b1;
                    }
                }
              if(flag){
                  newips.add(serverInfos); 
              }
            }
           
        }
        model.addAttribute("ips", newips);
        return "modules/cluster/etlNodeAdd";
    }
    
    /**
     * 保存集群信息
     */
    @RequiresPermissions(value = { "server:platform:add",
            "server:platform:edit" }, logical = Logical.OR)
    @RequestMapping(value = "nodeSave")
    public String nodeSave(PlatformNode platformNode, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        
        Platform platform = platformService.get("13");
        ServerInfos serverInfos=serverInfosService.get(platformNode.getPhysicalId());
        platformNode.setNodeIp(serverInfos.getIpv4());
        platformNode.setPlatformId(platform);
        platformNode.setNodeState(0);
        platformNode.setNodePort(15100);
        platformNode.setNodeUrl("http://"+platformNode.getNodeIp()+":15100");
        platformNode.setNodeName(serverInfos.getHostName());
        platformService.saveEtlNode(platform,platformNode);//保存
        addMessage(redirectAttributes, "保存集群节点成功");
        
        return "redirect:" + Global.getAdminPath()
                + "/server/platform/etl?repage";
    }
    
    //
    /**
     * 基础服务信息
     */
    @RequiresPermissions("server:platform:list")
    @RequestMapping(value = "etl")
    public String etlclist(PlatformNode platformNode,
            HttpServletRequest request, HttpServletResponse response,
            Model model)
    {
        Platform platform = platformService.get("13");
        platformNode.setPlatformId(platformService.get("13"));//获取etl集群
        Page<PlatformNode> page = platformService.findPage(new Page<PlatformNode>(
                request, response),
                platformNode);
        model.addAttribute("page", page);
        model.addAttribute("platform", platform);
        return "modules/cluster/etlInfo";
    }
    
    /**
     * 保存集群信息
     */
    @RequiresPermissions(value = { "server:platform:add",
            "server:platform:edit" }, logical = Logical.OR)
    @RequestMapping(value = "etlsave")
    public String save(Platform platform, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        if (!beanValidator(model, platform))
        {
            return form(platform, model);
        }
        try
        {
            if (!platform.getIsNewRecord())
            {//编辑表单保存
                Platform t = platformService.get(platform.getId());//从数据库取出记录的值
                MyBeanUtils.copyBeanNotNull2Bean(platform, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
                platformService.saveCluster(t);//保存
            }
            else
            {//新增表单保存
                platformService.saveCluster(platform);//保存
            }
            addMessage(redirectAttributes, "保存集群信息成功");
        }
        catch (Exception e)
        {
            addMessage(redirectAttributes, "操作失败，" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath()
                + "/server/platform/etl?repage";
    }
    
    /**
     * 删除集群信息
     */
    @RequiresPermissions("server:platform:del")
    @RequestMapping(value = "etldelete")
    public String delete(PlatformNode platform,
            RedirectAttributes redirectAttributes)
    {
        platformService.delete(platform);
        addMessage(redirectAttributes, "删除集群信息成功");
        return "redirect:" + Global.getAdminPath()
                + "/server/platform/etl?repage";
    }
    
    /**
     * 启动接口
     * @param clasterId  
     * @param type  1 集群   2 节点
     * @return
     */
    @RequestMapping(value = "etlstart")
    @ResponseBody
    public JSONObject start(String id, int type)
    {
        if (type == 1)//
        {
            return platformService.start(platformService.get(id), 1);
        }
        else
        {
            Platform cluster = new Platform();
            cluster.getPlatformNodeList().add(platformService.getNode(id));
            return platformService.start(cluster, 1);
        }
    }
    
    /**
     * 停止接口
     * @param clasterId  
     * @param type  1 集群   2 节点
     * @return
     */
    @RequestMapping(value = "etlstop")
    @ResponseBody
    public JSONObject stop(String id, int type)
    {
        if (type == 1)//
        {
            return platformService.start(platformService.get(id), 2);
        }
        else
        {
            Platform cluster = new Platform();
            cluster.getPlatformNodeList().add(platformService.getNode(id));
            return platformService.start(cluster, 2);
        }
    }
    
    /**
     * 停止接口
     * @param clasterId  
     * @param type  1 集群   2 节点
     * @return
     */
    @RequestMapping(value = "etlMetrics")
    @ResponseBody
    public JSONObject findEtlMetric(ClusterParam param)
    {
        
        String key = param.getKey() == null ? "day" : param.getKey();
        String keyDate = MetricsUtil.getDateKey(key);
        param.setKeyDate(keyDate);
        JSONObject data = new JSONObject();
        List<Map<String, String>> metrics = platformService.findEtlMetrics(param);
        JSONArray json = (JSONArray) JSONArray.toJSON(metrics);
        data.put("sucess", true);
        data.put("data", json);
        return data;
    }
    
    /* public static void main(String[] args)
     {
         long start= new Date().getTime();
        String s= DateUtils.formatDateTime(new Date());
        System.out.println(s);
     }*/
}