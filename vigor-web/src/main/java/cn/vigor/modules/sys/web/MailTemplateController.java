package cn.vigor.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.sys.entity.MailTemplate;
import cn.vigor.modules.sys.service.MailTemplateService;

/**
 * 邮件模板Controller
 * @author zhangfeng
 * @version 2016-08-12
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/mailtemplate")
public class MailTemplateController extends BaseController
{
    
    @Autowired
    private MailTemplateService mailTemplateService;
    
    /**
     * 邮件模板列表页面
     */
    @RequiresPermissions("sys:mailtemplate:list")
    @RequestMapping(value = { "list", "" })
    public String list(MailTemplate mailTemplate, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        Page<MailTemplate> page = mailTemplateService.findPage(
                new Page<MailTemplate>(request, response), mailTemplate);
        model.addAttribute("page", page);
        return "modules/sys/mailTemplateList";
    }
    
    /**
     * 保存邮件模板
     */
    @RequiresPermissions(value = { "sys:mailtemplate:add",
            "sys:mailTemplate:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(MailTemplate mailTemplate, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        if (!mailTemplate.getIsNewRecord())
        {//编辑表单保存
            MailTemplate t = mailTemplateService.get(mailTemplate.getId());//从数据库取出记录的值
            MyBeanUtils.copyBeanNotNull2Bean(mailTemplate, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
            mailTemplateService.save(t);//保存
        }
        else
        {//新增表单保存
            mailTemplateService.save(mailTemplate);//保存
        }
        addMessage(redirectAttributes, "保存邮件模板成功");
        return "redirect:" + Global.getAdminPath()
                + "/sys/mailTemplate/?repage";
    }
    
    /**
     * 删除邮件模板
     */
    @RequiresPermissions("sys:mailtemplate:del")
    @RequestMapping(value = "delete")
    public @ResponseBody String delete(int mtId)
    {
        mailTemplateService
                .delete(mailTemplateService.get(String.valueOf(mtId)));
        return "删除成功";
    }
    
    /**
     * 邮件模板列表页面
     */
    @RequiresPermissions("sys:mailtemplate:pitchon")
    @RequestMapping(value = "pitchon")
    public @ResponseBody String pitchOn(int mtType, int mtId)
    {
        //将选中的置为被选中
        mailTemplateService.pitchOn(mtType, mtId);
        
        return "操作成功";
    }
}