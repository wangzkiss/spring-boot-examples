/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.tji.entity;

import java.util.Date;

import cn.vigor.common.persistence.DataEntity;

/**
 * 作业相关Entity
 * @author zhangfeng
 * @version 2016-06-06
 */
public class Job extends DataEntity<Job>
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 作业id
     */
    private Integer jobId;
    
    /**
     * 任务id
     */
    private Integer taskId;
    
    /**
     * 任务类型
     */
    private Integer taskType;
    
    /**
     * 类型名称
     */
    private String typeName;
    
    /**
     * 分组
     */
    private Integer groupId;
    
    /**
     * 作业名称，暂与task一样
     */
    private String jobName;
    
    /**
     * 作业状态，值说明：0：未审核；1：审核通过；2：审核不通过；
     */
    private Integer jobStatus;
    
    /**
     * 0:无 1：未运行 2：运行中 3：运行完成 4：停止 5：终止
     */
    private Integer execStatus;
    
    /**
     * 调度规则表达式
     */
    private String scheduleRule;
    
    /**
     * 失败后是否暂定，0：否，1：是
     */
    private Integer pauseFlag;
    
    /**
     * 通知邮箱地址
     */
    private String notifyEmail;
    
    /**
     * 任务开始时间，任务调度开始执行时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 级别，1-10
     */
    private Integer scheduleLevel;
    
    /**
     * 执行类型，1：一次，2：天，3：周，4：月，5：年
     */
    private String execType;
    
    /**
     * // 创建时间
     */
    private Date createTime;
    
    /**
     * 创建人名称
     */
    private String createUser;
    
    /**
     * 创建时间开始
     */
    private Date cStartTime;
    
    /**
     * 创建时间结束
     */
    private Date cEndTime;
    
    /**
     * 用于批量操作
     */
    private String taskIds;
    
    /**
     * 任务标志
     */
    private int taskFlag;
    
    /**
     * 组名
     */
    private String groupName;
    
    private int opFlag;
    
    private int againFlag;
    
    private String jobIds;
    
    public Job()
    {
        super();
    }
    
    public Job(String id)
    {
        super(id);
    }
    
    public Integer getJobId()
    {
        return jobId;
    }
    
    public void setJobId(Integer jobId)
    {
        this.jobId = jobId;
    }
    
    public Integer getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }
    
    public Integer getTaskType()
    {
        return taskType;
    }
    
    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
    
    public Integer getGroupId()
    {
        return groupId;
    }
    
    public void setGroupId(Integer groupId)
    {
        this.groupId = groupId;
    }
    
    public String getJobName()
    {
        return jobName;
    }
    
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }
    
    public Integer getJobStatus()
    {
        return jobStatus;
    }
    
    public void setJobStatus(Integer jobStatus)
    {
        this.jobStatus = jobStatus;
    }
    
    public Integer getExecStatus()
    {
        return execStatus;
    }
    
    public void setExecStatus(Integer execStatus)
    {
        this.execStatus = execStatus;
    }
    
    public String getScheduleRule()
    {
        return scheduleRule;
    }
    
    public void setScheduleRule(String scheduleRule)
    {
        this.scheduleRule = scheduleRule;
    }
    
    public Integer getPauseFlag()
    {
        return pauseFlag;
    }
    
    public void setPauseFlag(Integer pauseFlag)
    {
        this.pauseFlag = pauseFlag;
    }
    
    public String getNotifyEmail()
    {
        return notifyEmail;
    }
    
    public void setNotifyEmail(String notifyEmail)
    {
        this.notifyEmail = notifyEmail;
    }
    
    public Date getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }
    
    public Date getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }
    
    public Integer getScheduleLevel()
    {
        return scheduleLevel;
    }
    
    public void setScheduleLevel(Integer scheduleLevel)
    {
        this.scheduleLevel = scheduleLevel;
    }
    
    public String getExecType()
    {
        return execType;
    }
    
    public void setExecType(String execType)
    {
        this.execType = execType;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public String getCreateUser()
    {
        return createUser;
    }
    
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }
    
    public String getTaskIds()
    {
        return taskIds;
    }
    
    public void setTaskIds(String taskIds)
    {
        this.taskIds = taskIds;
    }
    
    public Date getcStartTime()
    {
        return cStartTime;
    }
    
    public void setcStartTime(Date cStartTime)
    {
        this.cStartTime = cStartTime;
    }
    
    public Date getcEndTime()
    {
        return cEndTime;
    }
    
    public void setcEndTime(Date cEndTime)
    {
        this.cEndTime = cEndTime;
    }
    
    public int getTaskFlag()
    {
        return taskFlag;
    }
    
    public void setTaskFlag(int taskFlag)
    {
        this.taskFlag = taskFlag;
    }
    
    public String getGroupName()
    {
        return groupName;
    }
    
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
    
    public int getOpFlag()
    {
        return opFlag;
    }
    
    public void setOpFlag(int opFlag)
    {
        this.opFlag = opFlag;
    }
    
    public int getAgainFlag()
    {
        return againFlag;
    }
    
    public void setAgainFlag(int againFlag)
    {
        this.againFlag = againFlag;
    }
    
    public String getJobIds()
    {
        return jobIds;
    }
    
    public void setJobIds(String jobIds)
    {
        this.jobIds = jobIds;
    }
}