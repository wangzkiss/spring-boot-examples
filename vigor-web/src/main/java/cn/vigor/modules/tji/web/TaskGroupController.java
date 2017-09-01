/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.tji.web;

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

import cn.vigor.common.config.Global;
import cn.vigor.common.contants.Contants;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.tji.entity.TaskGroup;
import cn.vigor.modules.tji.service.TaskGroupService;
import cn.vigor.modules.tji.service.TasksService;

/**
 * 任务组相关Controller
 * @author zhangfeng
 * @version 2016-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/tji/taskgroup")
public class TaskGroupController extends BaseController
{
    
    @Autowired
    private TaskGroupService taskGroupService;
    
    /**
     * 任务
     */
    @Autowired
    private TasksService taskService;
    
    private String msg;
    
    @ModelAttribute
    public TaskGroup get(@RequestParam(required = false) String id)
    {
        TaskGroup entity = null;
        if (StringUtils.isNotBlank(id))
        {
            entity = taskGroupService.get(id);
        }
        if (entity == null)
        {
            entity = new TaskGroup();
        }
        return entity;
    }
    
    /**
     * 任务组列表页面
     */
    @RequiresPermissions("tji:taskgroup:list")
    @RequestMapping(value = { "list", "" })
    public String list(TaskGroup taskGroup, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        //默认按创建时间倒序
        Page<TaskGroup> paging = new Page<TaskGroup>(request, response);
        
        if (StringUtils.isBlank(paging.getOrderBy()))
        {
            paging.setOrderBy("createTime desc");
        }
        
        Page<TaskGroup> page = taskGroupService.findPage(paging, taskGroup);
        model.addAttribute("page", page);
        return "modules/tji/taskGroupList";
    }
    
    /**
     * 任务组列表，用于任务列表编辑
     */
    @RequiresPermissions("tji:taskgroup:listForTask")
    @RequestMapping("list-for-task")
    public String listForTask(String taskIds,
            @RequestParam(required = false) Integer taskType,
            @RequestParam(required = false) Integer taskFlag,
            @RequestParam(required = false) String groupName,
            HttpServletRequest request, HttpServletResponse response,
            Model model)
    {
        TaskGroup taskGroup = new TaskGroup();
        
        Integer groupType = null;
        
        if (null != taskFlag)
        {
            groupType = taskFlag;
        }
        else if (Contants.TASK_TYPE_ACT == taskType)
        {
            groupType = 3;
        }
        else if (Contants.TASK_TYPE_ETL == taskType
                || Contants.TASK_TYPE_ETL_TMP == taskType)
        {
            groupType = 1;
        }
        else
        {
            groupType = 2;
        }
        
        taskGroup.setGroupType(groupType);
        taskGroup.setGroupName(groupName);
        taskFlag = groupType;
        
        Page<TaskGroup> page = taskGroupService
                .findPage(new Page<TaskGroup>(request, response), taskGroup);
        
        model.addAttribute("page", page);
        model.addAttribute("taskFlag", taskFlag);
        model.addAttribute("groupName", groupName);
        model.addAttribute("taskIds", taskIds);
        return "modules/tji/groupForTask";
    }
    
    /**
     * 增加，编辑任务组表单页面
     */
    @RequiresPermissions(value = { "tji:taskgroup:add",
            "tji:taskgroup:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TaskGroup taskGroup, int flag, Model model)
    {
        taskGroup = taskGroupService
                .get(String.valueOf(taskGroup.getGroupId()));
        
        if (null == taskGroup)
        {
            taskGroup = new TaskGroup();
        }
        
        //flag用于标识是新增还是编辑,1:新增 2：编辑
        model.addAttribute("flag", flag);
        model.addAttribute("taskGroup", taskGroup);
        return "modules/tji/taskGroupForm";
    }
    
    /**
     * 保存任务组
     */
    @RequiresPermissions(value = { "tji:taskgroup:add",
            "tji:taskgroup:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(TaskGroup taskGroup, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        msg = "保存任务组成功";
        
        //编辑表单保存
        if (null != taskGroup.getGroupId())
        {
            taskGroup.setId("1");
            taskGroupService.save(taskGroup);
            msg = "修改任务组成功";
        }
        //新增表单保存
        else
        {
            if (null != taskGroupService
                    .getGroupByName(taskGroup.getGroupName()))
            {
                msg = "已存在重名任务组！";
            }
            else
            {
                taskGroupService.save(taskGroup);//保存
            }
        }
        addMessage(redirectAttributes, msg);
        return "redirect:" + Global.getAdminPath() + "/tji/taskgroup/?repage";
    }
    
    /**
     * 删除任务组
     */
    @RequiresPermissions("tji:taskgroup:del")
    @RequestMapping(value = "delete")
    public @ResponseBody String delete(TaskGroup taskGroup)
    {
        if (taskService.getTaskByGroupId(taskGroup.getGroupId()).size() > 0)
        {
            msg = "删除任务组失败，已存在任务！";
        }
        else
        {
            taskGroupService.delete(taskGroup);
            msg = "删除任务组成功";
        }
        
        return msg;
    }
    
}