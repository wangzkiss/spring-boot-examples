package cn.vigor.modules.tji.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.vigor.common.utils.DateUtils;

/**
 * 作业详情,用于查看及审核
 * @author zhangfeng
 */
public class JobDetail extends TaskDetail
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 作业id
     */
    private Integer jobId;
    
    /**
     * 作业名称，暂与task一样
     */
    private String jobName;
    
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
     * 开始
     */
    private String startTimeStr;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 结束
     */
    private String endTimeStr;
    
    /**
     * 级别，1-10
     */
    private Integer scheduleLevel;
    
    /**
     * 审核状态
     */
    private Integer jobStatus;
    
    /**
     * 执行状态
     */
    private Integer execStatus;
    
    /**
     * 执行类型
     */
    private String execType;
    
    /**
     * 执行平台id
     */
    private Integer platformId;
    
    /**
     * 执行平台url
     */
    private String platformUrl;
    
    /**
     * 执行平台列表
     */
    private List<Map<String, Object>> nodeList;
    
    private Integer outputType;
    
    private String dateParams;
    
    private Integer againFlag;
    
    public Integer getJobId()
    {
        return jobId;
    }
    
    public void setJobId(Integer jobId)
    {
        this.jobId = jobId;
    }
    
    public String getJobName()
    {
        return jobName;
    }
    
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
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
    
    public String getStartTimeStr()
    {
        if (null != startTime)
        {
            startTimeStr = DateUtils.formatDateTime(startTime);
        }
        
        return startTimeStr;
    }
    
    public void setStartTimeStr(String startTimeStr)
    {
        this.startTimeStr = startTimeStr;
    }
    
    public String getEndTimeStr()
    {
        if (null != endTime)
        {
            endTimeStr = DateUtils.formatDateTime(endTime);
        }
        
        return endTimeStr;
    }
    
    public void setEndTimeStr(String endTimeStr)
    {
        this.endTimeStr = endTimeStr;
    }
    
    public Integer getScheduleLevel()
    {
        return scheduleLevel;
    }
    
    public void setScheduleLevel(Integer scheduleLevel)
    {
        this.scheduleLevel = scheduleLevel;
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
    
    public String getExecType()
    {
        return execType;
    }
    
    public void setExecType(String execType)
    {
        this.execType = execType;
    }
    
    public String getPlatformUrl()
    {
        return platformUrl;
    }
    
    public void setPlatformUrl(String platformUrl)
    {
        this.platformUrl = platformUrl;
    }
    
    public Integer getPlatformId()
    {
        return platformId;
    }
    
    public void setPlatformId(Integer platformId)
    {
        this.platformId = platformId;
    }
    
    public List<Map<String, Object>> getNodeList()
    {
        return nodeList;
    }
    
    public void setNodeList(List<Map<String, Object>> nodeList)
    {
        this.nodeList = nodeList;
    }
    
    public Integer getOutputType()
    {
        return outputType;
    }
    
    public void setOutputType(Integer outputType)
    {
        this.outputType = outputType;
    }
    
    public String getDateParams()
    {
        return dateParams;
    }
    
    public void setDateParams(String dateParams)
    {
        this.dateParams = dateParams;
    }
    
    public Integer getAgainFlag()
    {
        return againFlag;
    }
    
    public void setAgainFlag(Integer againFlag)
    {
        this.againFlag = againFlag;
    }
}
