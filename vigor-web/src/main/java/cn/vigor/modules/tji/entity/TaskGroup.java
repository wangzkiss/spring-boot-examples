/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.tji.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 任务组相关Entity
 * @author zhangfeng
 * @version 2016-06-06
 */
public class TaskGroup extends DataEntity<TaskGroup>
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 组id
     */
    private Integer groupId;
    
    /**
     * 组名
     */
    private String groupName;
    
    /**
     * 任务类型 1:ETL 2:计算 3：工作流
     */
    private Integer groupType;
    
    /**
     * 分组描述
     */
    private String groupDesc;
    
    /**
     * 创建人
     */
    private String createUser;
    
    /**
     *  创建时间
     */
    private Date createTime;
    
    /**
     * 任务数
     */
    private int taskNum;
    
    public TaskGroup()
    {
        super();
    }
    
    public TaskGroup(String id)
    {
        super(id);
    }
    
    public Integer getGroupId()
    {
        return groupId;
    }
    
    public void setGroupId(Integer groupId)
    {
        this.groupId = groupId;
    }
    
    @Length(min = 1, max = 50, message = "组名长度必须介于 1 和 50 之间")
    @ExcelField(title = "组名", align = 2, sort = 1)
    public String getGroupName()
    {
        return groupName;
    }
    
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
    
    public String getGroupDesc()
    {
        return groupDesc;
    }
    
    public void setGroupDesc(String groupDesc)
    {
        this.groupDesc = groupDesc;
    }
    
    public Integer getGroupType()
    {
        return groupType;
    }
    
    public void setGroupType(Integer groupType)
    {
        this.groupType = groupType;
    }
    
    public String getCreateUser()
    {
        return createUser;
    }
    
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public int getTaskNum()
    {
        return taskNum;
    }

    public void setTaskNum(int taskNum)
    {
        this.taskNum = taskNum;
    }
}