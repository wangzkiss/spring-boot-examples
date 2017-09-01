/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.tji.dao;

import java.util.List;
import java.util.Map;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.tji.entity.TaskGroup;

/**
 * 任务组相关DAO接口
 * @author zhangfeng
 * @version 2016-06-06
 */
@MyBatisDao
public interface TaskGroupDao extends CrudDao<TaskGroup>
{
    /**
     * 根据分组类型找到分组信息
     * @param groupType
     * @return List<TaskGroup> 
     */
    public List<TaskGroup> getGroupInfoByType(int groupType);
    
    /**
     * 获取任务组
     * @param groupType
     * @return List<TaskGroup> 
     */
    public List<Map<String, Object>> getGroups(int groupType);
    
    /**
     * 获取任务组
     * @param groupType
     * @return List<TaskGroup> 
     */
    public TaskGroup getGroupByName(String groupName);
    
}