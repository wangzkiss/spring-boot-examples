/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.tji.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 排班及统计Entity
 * @author zhangfeng
 * @version 2016-06-24
 */
public class JobSchedule extends DataEntity<JobSchedule>
{
    
    private static final long serialVersionUID = 1L;
    
    private Integer schId; // 主键
    
    private Integer trackId; // 批次id
    
    private Integer jobId; // 作业id
    
    private String jobName; // 作业名称
    
    private Integer taskType; // 任务类型，为了方便统计，多存一个字段
    
    private String statisticsDay; // 统计的天
    
    private Date scheduleTime; // 任务具体执行的时间点
    
    private Integer againFlag; // 是否重跑，0否，1是
    
    private Integer scheduleLevel; // 优先级
    
    private Integer pauseFlag; // 是否暂停
    
    private Integer execCode; // 执行码，与e_instance的execcode同步
    
    private Integer flag;
    
    private String typeName;
    
    private String execType;
    
    private Date cStartTime;
    
    private Date cEndTime;
    
    public JobSchedule()
    {
        super();
    }
    
    public JobSchedule(String id)
    {
        super(id);
    }
    
    @NotNull(message = "主键不能为空")
    @ExcelField(title = "主键", align = 2, sort = 0)
    public Integer getSchId()
    {
        return schId;
    }
    
    public void setSchId(Integer schId)
    {
        this.schId = schId;
    }
    
    @ExcelField(title = "批次id", align = 2, sort = 1)
    public Integer getTrackId()
    {
        return trackId;
    }
    
    public void setTrackId(Integer trackId)
    {
        this.trackId = trackId;
    }
    
    @ExcelField(title = "作业id", align = 2, sort = 2)
    public Integer getJobId()
    {
        return jobId;
    }
    
    public void setJobId(Integer jobId)
    {
        this.jobId = jobId;
    }
    
    @Length(min = 0, max = 100, message = "作业名称长度必须介于 0 和 100 之间")
    @ExcelField(title = "作业名称", align = 2, sort = 3)
    public String getJobName()
    {
        return jobName;
    }
    
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }
    
    @ExcelField(title = "任务类型，为了方便统计，多存一个字段", align = 2, sort = 4)
    public Integer getTaskType()
    {
        return taskType;
    }
    
    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }
    
    @Length(min = 0, max = 10, message = "统计的天长度必须介于 0 和 10 之间")
    @ExcelField(title = "统计的天", align = 2, sort = 5)
    public String getStatisticsDay()
    {
        return statisticsDay;
    }
    
    public void setStatisticsDay(String statisticsDay)
    {
        this.statisticsDay = statisticsDay;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "任务具体执行的时间点", align = 2, sort = 6)
    public Date getScheduleTime()
    {
        return scheduleTime;
    }
    
    public void setScheduleTime(Date scheduleTime)
    {
        this.scheduleTime = scheduleTime;
    }
    
    @ExcelField(title = "是否重跑，0否，1是", align = 2, sort = 7)
    public Integer getAgainFlag()
    {
        return againFlag;
    }
    
    public void setAgainFlag(Integer againFlag)
    {
        this.againFlag = againFlag;
    }
    
    @ExcelField(title = "优先级", align = 2, sort = 8)
    public Integer getScheduleLevel()
    {
        return scheduleLevel;
    }
    
    public void setScheduleLevel(Integer scheduleLevel)
    {
        this.scheduleLevel = scheduleLevel;
    }
    
    public Integer getPauseFlag()
    {
        return pauseFlag;
    }
    
    public void setPauseFlag(Integer pauseFlag)
    {
        this.pauseFlag = pauseFlag;
    }
    
    @ExcelField(title = "执行码，与e_instance的execcode同步", align = 2, sort = 10)
    public Integer getExecCode()
    {
        return execCode;
    }
    
    public void setExecCode(Integer execCode)
    {
        this.execCode = execCode;
    }
    
    public Integer getFlag()
    {
        return flag;
    }
    
    public void setFlag(Integer flag)
    {
        this.flag = flag;
    }
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
    
    public String getExecType()
    {
        return execType;
    }
    
    public void setExecType(String execType)
    {
        this.execType = execType;
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
}