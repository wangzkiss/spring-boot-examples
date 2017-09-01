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
public class JobLog extends DataEntity<JobLog>
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 作业id
     */
    private Integer jobId;
    
    /**
     * 作业名称
     */
    private String jobName;
    
    /**
     * 日志生成时间
     */
    private Date logTime;
    
    private String logMessage;
    
    private Integer trackId;
    
    private String sysName;
    
    private Date startTime;
    
    private Date endTime;
    
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

    public JobLog()
    {
        super();
    }

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

    public Date getLogTime()
    {
        return logTime;
    }

    public void setLogTime(Date logTime)
    {
        this.logTime = logTime;
    }

    public String getLogMessage()
    {
        return logMessage;
    }

    public void setLogMessage(String logMessage)
    {
        this.logMessage = logMessage;
    }

    public Integer getTrackId()
    {
        return trackId;
    }

    public void setTrackId(Integer trackId)
    {
        this.trackId = trackId;
    }

    public String getSysName()
    {
        return sysName;
    }

    public void setSysName(String sysName)
    {
        this.sysName = sysName;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("JobLog [jobId=");
        builder.append(jobId);
        builder.append(", jobName=");
        builder.append(jobName);
        builder.append(", logTime=");
        builder.append(logTime);
        builder.append(", logMessage=");
        builder.append(logMessage);
        builder.append(", trackId=");
        builder.append(trackId);
        builder.append(", sysName=");
        builder.append(sysName);
        builder.append("]");
        return builder.toString();
    }

}