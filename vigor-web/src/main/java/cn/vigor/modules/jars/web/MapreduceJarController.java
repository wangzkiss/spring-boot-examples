package cn.vigor.modules.jars.web;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.jars.entity.MapreduceJar;
import cn.vigor.modules.jars.service.MapreduceJarService;
import cn.vigor.modules.meta.util.PrivilegeException;
import cn.vigor.modules.meta.util.SshClient;


/**
 * 执行包信息Controller
 * @author kiss
 * @version 2016-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/jars/mapreduceJar")
public class MapreduceJarController extends BaseController
{
    public Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
    private MapreduceJarService mapreduceJarService;
    
    @ModelAttribute
    public MapreduceJar get(@RequestParam(required = false) String id)
    {
        MapreduceJar entity = null;
        if (StringUtils.isNotBlank(id))
        {
            entity = mapreduceJarService.get(id);
        }
        if (entity == null)
        {
            entity = new MapreduceJar();
        }
        return entity;
    }
    
    /**
     * 执行包信息列表页面
     */
    @RequiresPermissions("jars:mapreduceJar:list")
    @RequestMapping(value = { "list", "" })
    public String list(MapreduceJar mapreduceJar, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        Page<MapreduceJar> page = mapreduceJarService.findPage(
                new Page<MapreduceJar>(request, response), mapreduceJar);
        model.addAttribute("page", page);
        return "modules/jars/mapreduceJarList";
    }
    
    /**
     * 查看，增加，编辑执行包信息表单页面
     */
    @RequiresPermissions(value = { "jars:mapreduceJar:view",
            "jars:mapreduceJar:add",
            "jars:mapreduceJar:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(MapreduceJar mapreduceJar, Model model)
    {
        model.addAttribute("mapreduceJar", mapreduceJar);
        return "modules/jars/mapreduceJarForm";
    }
    
    /**
     * 保存执行包信息
     */
    @RequiresPermissions(value = { "jars:mapreduceJar:add",
            "jars:mapreduceJar:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(MapreduceJar mapreduceJar, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        if (!beanValidator(model, mapreduceJar))
        {
            return form(mapreduceJar, model);
        }
        if (!mapreduceJar.getIsNewRecord())
        {//编辑表单保存
            MapreduceJar t = mapreduceJarService.get(mapreduceJar.getId());//从数据库取出记录的值
            MyBeanUtils.copyBeanNotNull2Bean(mapreduceJar, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
            mapreduceJarService.save(t);//保存
        }
        else
        {//新增表单保存
            mapreduceJarService.save(mapreduceJar);//保存
        }
        addMessage(redirectAttributes, "保存执行包信息成功");
        return "redirect:" + Global.getAdminPath()
                + "/jars/mapreduceJar/?repage";
    }
    
    /**
     * 删除执行包信息
     */
    @RequiresPermissions("jars:mapreduceJar:del")
    @RequestMapping(value = "delete")
    public String delete(MapreduceJar mapreduceJar,
            RedirectAttributes redirectAttributes)
    {
        try
        {
            if(deleteJar(mapreduceJar)){
                mapreduceJarService.delete(mapreduceJar);
                addMessage(redirectAttributes, "删除执行包信息成功");
            }
        }
        catch (Exception e)
        {
            addMessage(redirectAttributes, "删除执行包信息失败，失败原因："+e.getMessage());
        }
        return "redirect:" + Global.getAdminPath()
                + "/jars/mapreduceJar/?repage";
    }
  
  
    @Value("${jar.path}")
    private String path;
    /**
     * 上传jar包
     */
    @RequiresPermissions("jars:mapreduceJar:import")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String uploadFile(MultipartFile file,String remarks,
            RedirectAttributes redirectAttributes)
    {
        String fileName = file.getOriginalFilename();
        try
        {
            MapreduceJar mapreduceJar = new MapreduceJar();
            mapreduceJar.setJarName(fileName);
            mapreduceJar.setJarPath(path);
            mapreduceJar.setJarType(2);
            mapreduceJar.setRemarks(remarks);
            if (StringUtils.isBlank(fileName))
            {
                throw new RuntimeException("导入文档为空!");
            }
            else if (fileName.toLowerCase().endsWith("jar"))
            {
                InputStream is = file.getInputStream();
                if (uploadJar(is, file.getOriginalFilename(), path, path))
                {
                    mapreduceJarService.save(mapreduceJar);
                    addMessage(redirectAttributes, "上传成功！");
                }
            }
            else
            {
                throw new RuntimeException("文档格式不正确!");
            }
        }
        catch (Exception e)
        {
            String msg=e.getMessage();
            if(msg.contains("Duplicate")&& msg.contains("jarname"))
            {
                msg=fileName+"文件已存在，请不要重复导入";
            } else {
                msg="入执行包信息失败：失败原因："+e.getMessage();
            }
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
        return "redirect:" + Global.getAdminPath()
                + "/jars/mapreduceJar/?repage";
    }
    
    
    private boolean uploadJar(InputStream is, String fileName, String jarPath,
            String logPath) throws Exception
    {
       
        String hadoopMaster=Global.getConfig("hadoopMaster");
        String sshuser=Global.getConfig("ssh_username");
        String sshpwd=Global.getConfig("ssh_username");
        String hadoopBackMaster=Global.getConfig("hadoopBackMaster");
        boolean flag = false;
        SshClient sshClient = null;
        try
        {
            sshClient = new SshClient(hadoopMaster, 22, sshuser, sshpwd);
            sshClient.execute("mkdir -p " + jarPath);
            sshClient.execute("mkdir -p " + logPath);
            sshClient.upFile(is, jarPath +"/"+ fileName);
            if (!hadoopMaster.equals(hadoopBackMaster))
            {
                InputStream jarios = sshClient
                        .getFileStream(jarPath +"/"+ fileName);
                sshClient = new SshClient(hadoopBackMaster, 22, sshuser,
                        sshpwd);
                sshClient.execute("mkdir -p " + logPath);
                sshClient.execute("mkdir -p " + jarPath);
                sshClient.upFile(jarios, jarPath +"/"+ fileName);
            }
            flag = true;
        }
        catch (PrivilegeException e)
        {
            throw new PrivilegeException("link_error",
                    "主节点服务器连接不上，请检查配置hadoopMaster值是否正确！");
        }
        catch (Exception e)
        {
            log.error(this.getClass().getName(), e);
            throw e;
        }
        finally
        {
            if (sshClient != null)
            {
                sshClient.closeConnection();
            }
        }
        return flag;
    }
    private boolean deleteJar(MapreduceJar mapreduceJar) throws Exception{
        boolean flag = false;
        SshClient sshClient = null;
        try {
            String hadoopMaster=Global.getConfig("hadoopMaster");
            String sshuser=Global.getConfig("ssh_username");
            String sshpwd=Global.getConfig("ssh_username");
            String hadoopBackMaster=Global.getConfig("hadoopBackMaster");
            sshClient = new SshClient(hadoopMaster, 22, sshuser, sshpwd);
            String rmjar="rm -rf "+mapreduceJar.getJarPath()+"/"+mapreduceJar.getJarName();
            sshClient.execute(rmjar);
            sshClient = new SshClient(hadoopBackMaster, 22, sshuser, sshpwd);
            sshClient.execute(rmjar);
            flag = true;
        }  catch (PrivilegeException e) {
            throw new PrivilegeException("link_error","主节点服务器连接不上，请检查配置hadoopMaster值是否正确！");
        } catch (Exception e) {
            log.error(this.getClass().getName(), e);
        }finally{
            if(sshClient!=null)
            {
                sshClient.closeConnection();
            }
        }
        return flag;
    }
    
}