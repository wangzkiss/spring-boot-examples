/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.tji.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.modules.tji.dao.TaskGroupDao;
import cn.vigor.modules.tji.entity.TaskGroup;

/**
 * 任务组相关Service
 * @author zhangfeng
 * @version 2016-06-06
 */
@Service
@Transactional(readOnly = true)
public class TaskGroupService extends CrudService<TaskGroupDao, TaskGroup>
{
    
    public TaskGroup get(String id)
    {
        return super.get(id);
    }
    
    public List<TaskGroup> findList(TaskGroup taskGroup)
    {
        return super.findList(taskGroup);
    }
    
    public Page<TaskGroup> findPage(Page<TaskGroup> page, TaskGroup taskGroup)
    {
        return super.findPage(page, taskGroup);
    }
    
    @Transactional(readOnly = false)
    public void save(TaskGroup taskGroup)
    {
        super.save(taskGroup);
    }
    
    @Transactional(readOnly = false)
    public void delete(TaskGroup taskGroup)
    {
        super.delete(taskGroup);
    }
    
    /**
     * 根据分组类型找到分组信息
     * @param groupType
     * @return List<TaskGroup> 
     */
    public List<TaskGroup> getGroupInfoByType(int groupType)
    {
        return dao.getGroupInfoByType(groupType);
    }
    
    /**
     * 获取任务组
     * @param groupType
     * @return List<TaskGroup> 
     */
    public List<Map<String, Object>> getGroups(int groupType)
    {
        return dao.getGroups(groupType);
    }
    
    /**
     * 获取任务组
     * @param groupType
     * @return List<TaskGroup> 
     */
    public TaskGroup getGroupByName(String groupName)
    {
        return dao.getGroupByName(groupName);
    }
}