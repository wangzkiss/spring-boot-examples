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
 * 作业实例Entity
 * @author zhangfeng
 * @version 2016-06-24
 */
public class JobInstance extends DataEntity<JobInstance>
{
    
    private static final long serialVersionUID = 1L;
    
    private Integer trackId; // 日志唯一标识id，自增主键
    
    private Integer jobId; // 作业id
    
    private String jobName; // 作业名称
    
    private Integer taskType; // 任务类型id，参照表t_base_task_type中type_id值说明
    
    private Date scheduleTime; // 调度时间
    
    private Date execTime; // 实际开始时间
    
    /**
     * 任务执行结果代号 (0未提交 1正在执行 2成功 3提交失败 4执行失败 5暂停 6停止)
     */
    private Integer execCode;
    
    private String execDesc; // 结果描述
    
    private Date respTime; // 结束时间
    
    private String dateParams; // 保存当前的日期参数，用于重跑
    
    private Integer actTrackId; // 工作流的track_id
    
    private Integer againFlag; // 是否是重跑 0：否 1：是
    
    private String execType;
    
    /**
     * 类型名称
     */
    private String typeName;
    
    /**
     * 创建时间开始
     */
    private Date cStartTime;
    
    /**
     * 创建时间结束
     */
    private Date cEndTime;
    
    private Long inputNum;
    
    private String inputMeta;
    
    private Long outputNum;
    
    private String outputMeta;
    
    private String avgSpeed;
    
    private Integer isFirst = 1;
    
    private Integer verify;
    
    private String jobProgressInfo;
    
    public String tableName = "e_job_instance";
    
    /**
     *跨表时使用
     */
    public String secTableName;
    
    public JobInstance()
    {
        super();
    }
    
    public JobInstance(String id)
    {
        super(id);
    }
    
    @NotNull(message = "日志唯一标识id，自增主键不能为空")
    @ExcelField(title = "日志唯一标识id，自增主键", align = 2, sort = 0)
    public Integer getTrackId()
    {
        return trackId;
    }
    
    public void setTrackId(Integer trackId)
    {
        this.trackId = trackId;
    }
    
    @NotNull(message = "作业id不能为空")
    @ExcelField(title = "作业id", align = 2, sort = 1)
    public Integer getJobId()
    {
        return jobId;
    }
    
    public void setJobId(Integer jobId)
    {
        this.jobId = jobId;
    }
    
    @Length(min = 1, max = 100, message = "作业名称长度必须介于 1 和 100 之间")
    @ExcelField(title = "作业名称", align = 2, sort = 2)
    public String getJobName()
    {
        return jobName;
    }
    
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }
    
    @NotNull(message = "任务类型id，参照表t_base_task_type中type_id值说明不能为空")
    @ExcelField(title = "任务类型id，参照表t_base_task_type中type_id值说明", align = 2, sort = 3)
    public Integer getTaskType()
    {
        return taskType;
    }
    
    public void setTaskType(Integer taskType)
    {
        this.taskType = taskType;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "调度时间不能为空")
    @ExcelField(title = "调度时间", align = 2, sort = 4)
    public Date getScheduleTime()
    {
        return scheduleTime;
    }
    
    public void setScheduleTime(Date scheduleTime)
    {
        this.scheduleTime = scheduleTime;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "实际开始时间", align = 2, sort = 5)
    public Date getExecTime()
    {
        return execTime;
    }
    
    public void setExecTime(Date execTime)
    {
        this.execTime = execTime;
    }
    
    @ExcelField(title = "任务执行结果代号", align = 2, sort = 6)
    public Integer getExecCode()
    {
        return execCode;
    }
    
    public void setExecCode(Integer execCode)
    {
        this.execCode = execCode;
    }
    
    @Length(min = 0, max = 3000, message = "结果描述长度必须介于 0 和 3000 之间")
    @ExcelField(title = "结果描述", align = 2, sort = 7)
    public String getExecDesc()
    {
        return execDesc;
    }
    
    public void setExecDesc(String execDesc)
    {
        this.execDesc = execDesc;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "结束时间", align = 2, sort = 8)
    public Date getRespTime()
    {
        return respTime;
    }
    
    public void setRespTime(Date respTime)
    {
        this.respTime = respTime;
    }
    
    @Length(min = 0, max = 500, message = "保存当前的日期参数，用于重跑长度必须介于 0 和 500 之间")
    @ExcelField(title = "保存当前的日期参数，用于重跑", align = 2, sort = 9)
    public String getDateParams()
    {
        return dateParams;
    }
    
    public void setDateParams(String dateParams)
    {
        this.dateParams = dateParams;
    }
    
    @ExcelField(title = "工作流的track_id", align = 2, sort = 10)
    public Integer getActTrackId()
    {
        return actTrackId;
    }
    
    public void setActTrackId(Integer actTrackId)
    {
        this.actTrackId = actTrackId;
    }
    
    @ExcelField(title = "是否是重跑 0：否 1：是", align = 2, sort = 11)
    public Integer getAgainFlag()
    {
        return againFlag;
    }
    
    public void setAgainFlag(Integer againFlag)
    {
        this.againFlag = againFlag;
    }
    
    public String getExecType()
    {
        return execType;
    }
    
    public void setExecType(String execType)
    {
        this.execType = execType;
    }
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
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
    
    public Long getInputNum()
    {
        return inputNum;
    }
    
    public void setInputNum(Long inputNum)
    {
        this.inputNum = inputNum;
    }
    
    public Long getOutputNum()
    {
        return outputNum;
    }
    
    public void setOutputNum(Long outputNum)
    {
        this.outputNum = outputNum;
    }
    
    public String getAvgSpeed()
    {
        return avgSpeed;
    }
    
    public void setAvgSpeed(String avgSpeed)
    {
        this.avgSpeed = avgSpeed;
    }
    
    public Integer getIsFirst()
    {
        return isFirst;
    }
    
    public void setIsFirst(Integer isFirst)
    {
        this.isFirst = isFirst;
    }
    
    public Integer getVerify()
    {
        return verify;
    }
    
    public void setVerify(Integer verify)
    {
        this.verify = verify;
    }
    
    public String getInputMeta()
    {
        return inputMeta;
    }
    
    public void setInputMeta(String inputMeta)
    {
        this.inputMeta = inputMeta;
    }
    
    public String getOutputMeta()
    {
        return outputMeta;
    }
    
    public void setOutputMeta(String outputMeta)
    {
        this.outputMeta = outputMeta;
    }

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSecTableName() {
		return secTableName;
	}

	public void setSecTableName(String secTableName) {
		this.secTableName = secTableName;
	}

	public String getJobProgressInfo() {
		return jobProgressInfo;
	}

	public void setJobProgressInfo(String jobProgressInfo) {
		this.jobProgressInfo = jobProgressInfo;
	}
}