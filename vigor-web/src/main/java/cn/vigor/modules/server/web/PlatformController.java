package cn.vigor.modules.server.web;

import java.util.List;

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

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.server.entity.Component;
import cn.vigor.modules.server.entity.Platform;
import cn.vigor.modules.server.service.PlatformService;
import cn.vigor.modules.server.service.ServerInfosService;

/**
 * 集群信息Controller
 * @author kiss
 * @version 2016-06-30
 */
@Controller
@RequestMapping(value = "${adminPath}/server/platform")
public class PlatformController extends BaseController
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
     * 基础服务信息
     */
    @RequiresPermissions("server:platform:list")
    @RequestMapping(value = {"list",""})
    public String basiclist(Platform platform, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        String url="modules/server/platformList";
        if(3!=platform.getPlatformType())
        {
            Page<Platform> page = platformService.findPage(new Page<Platform>(
                    request, response), platform);
            model.addAttribute("page", page);
            model.addAttribute("platform", platform); 
        }else if(3==platform.getPlatformType()){
            List<Component> cluster = serverInfosService.findCluseServer();
            if(cluster!=null && cluster.size()>0)
            model.addAttribute("platform", cluster.get(0));
            url="modules/cluster/calInfo";
        }
        return url;
    }    

    /**
     * 查看，增加，编辑集群信息表单页面
     */
    @RequiresPermissions(value = { "server:platform:view",
            "server:platform:add", "server:platform:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Platform platform, Model model)
    {
        model.addAttribute("platform", platform);
        return "modules/server/platformForm";
    } 
    
    /**
     * 查看，增加，编辑集群信息表单页面
     */
    @RequiresPermissions(value = { "server:platform:view",
            "server:platform:add", "server:platform:edit" }, logical = Logical.OR)
    @RequestMapping(value = "view")
    public String view(Platform platform, Model model)
    {
        model.addAttribute("platform", platform);
        return "modules/server/platformView";
    }  
    /**
     * 保存集群信息
     */
    @RequiresPermissions(value = { "server:platform:add",
            "server:platform:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
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
            String msg=e.getMessage();//IDplamnode
            if(msg.contains("Duplicate") && msg.contains("platformname"))
            {
                msg="保存失败："+platform.getPlatformName()+"名已存在!";
            } else if (msg.contains("Duplicate") && msg.contains("IDplamnode"))
            {
                msg="保存失败：节点名，端口，ip存重复！";
            }else {
                msg="保存失败：失败原因："+e.getMessage();
            }
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
        return "redirect:" + Global.getAdminPath() +"/server/platform/?platformType="+platform.getPlatformType()+"&&repage";
    }
    
    /**
     * 删除集群信息
     */
    @RequiresPermissions("server:platform:del")
    @RequestMapping(value = "delete")
    public String delete(Platform platform,
            RedirectAttributes redirectAttributes)
    {
        platformService.delete(platform);
        addMessage(redirectAttributes, "删除集群信息成功");
        return "redirect:" + Global.getAdminPath() +"/server/platform/?platformType="+platform.getPlatformType()+"&&repage";
    }
    
    @RequestMapping("getPlatformByName")
    @ResponseBody
    public JSONObject getPlatformById(@RequestParam(value="platformName",defaultValue = "") String platformName){
        JSONObject data = new JSONObject();
        try
        {
            Platform platform = platformService.getOne(platformName);
            if(platform!=null){
                platform = platformService.get(platform.getId());
            }
            data.put("data", platform);
            data.put("success", true);
            data.put("msg", "成功");
        }
        catch (Exception e)
        {
            data.put("success", false);
            data.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return data;
    }
}