package cn.vigor.modules.tji.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;
import cn.vigor.modules.meta.entity.MetaSourcePro;

/**
 * 任务相关Entity
 * @author zhangfeng
 * @version 2016-06-03
 */
public class Task extends DataEntity<Task>
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 任务id
     */
    private Integer taskId;
    
    /**
     * 任务名
     */
    private String taskName;
    
    /**
     * 任务描述
     */
    private String taskDesc;
    
    /**
     * 任务类型
     */
    private Integer taskType;
    
    /**
     * 类型名称
     */
    private String typeName;
    
    /**
     * 任务所属组
     */
    private Integer groupId;
    
    /**
     * 任务所属组名
     */
    private String groupName;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 创建人id
     */
    private String createUser;
    
    /**
     * 任务来源平台,1：配置工具； 2：数据服务
     */
    private Integer comeFrom;
    
    /**
     * 任务的xml信息，用于配置工具
     */
    private String xmlData;
    
    /**
     * 第三方任务id
     */
    private String thirdTaskId;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 任务标示，1：ETL(包含FLUME任务) 2：计算 3:工作流 4：ETL（不包括FLUME任务） 5:SPARK
     */
    private int taskFlag;
    
    /**
     * 工作流逻辑图
     */
    private String actUrl;
    
    private int functionId;
    
    private List<Job> jobList;
    
    private List<MetaSourcePro> conditions;
    
    public int getFunctionId()
    {
        return functionId;
    }
    
    public void setFunctionId(int functionId)
    {
        this.functionId = functionId;
    }
    
    private String functionParam;
    
    public String getFunctionParam()
    {
        return functionParam;
    }
    
    public void setFunctionParam(String functionParam)
    {
        this.functionParam = functionParam;
    }
    
    /**
     * 函数名称
     */
    private String functionName;
    
    public String getFunctionName()
    {
        return functionName;
    }
    
    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
    }
    
    public Task()
    {
        super();
    }
    
    public Task(String id)
    {
        super(id);
    }
    
    @NotNull(message = "任务id不能为空")
    @ExcelField(title = "任务id", align = 2, sort = 0)
    public Integer getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }
    
    @Length(min = 1, max = 100, message = "任务名长度必须介于 1 和 100 之间")
    @ExcelField(title = "任务名", align = 2, sort = 1)
    public String getTaskName()
    {
        return taskName;
    }
    
    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }
    
    @Length(min = 0, max = 500, message = "任务描述长度必须介于 0 和 500 之间")
    @ExcelField(title = "任务描述", align = 2, sort = 2)
    public String getTaskDesc()
    {
        return taskDesc;
    }
    
    public void setTaskDesc(String taskDesc)
    {
        this.taskDesc = taskDesc;
    }
    
    @NotNull(message = "任务类型不能为空")
    @ExcelField(title = "任务类型", align = 2, sort = 3)
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
    
    public String getGroupName()
    {
        return groupName;
    }
    
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "创建时间不能为空")
    @ExcelField(title = "创建时间", align = 2, sort = 6)
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    @Length(min = 1, max = 50, message = "创建人id长度必须介于 1 和 50 之间")
    @ExcelField(title = "创建人id", align = 2, sort = 7)
    public String getCreateUser()
    {
        return createUser;
    }
    
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }
    
    @NotNull(message = "任务来源平台,1：配置工具； 2：数据服务不能为空")
    @ExcelField(title = "任务来源平台,1：配置工具； 2：数据服务", align = 2, sort = 8)
    public Integer getComeFrom()
    {
        return comeFrom;
    }
    
    public void setComeFrom(Integer comeFrom)
    {
        this.comeFrom = comeFrom;
    }
    
    @ExcelField(title = "任务的xml信息，用于配置工具", align = 2, sort = 9)
    public String getXmlData()
    {
        return xmlData;
    }
    
    public void setXmlData(String xmlData)
    {
        this.xmlData = xmlData;
    }
    
    @Length(min = 0, max = 50, message = "third_task_id长度必须介于 0 和 50 之间")
    @ExcelField(title = "third_task_id", align = 2, sort = 10)
    public String getThirdTaskId()
    {
        return thirdTaskId;
    }
    
    public void setThirdTaskId(String thirdTaskId)
    {
        this.thirdTaskId = thirdTaskId;
    }
    
    public Date getStartTime()
    {
        return startTime;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "开始时间不能为空")
    @ExcelField(title = "开始时间", align = 2, sort = 6)
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }
    
    public Date getEndTime()
    {
        return endTime;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "结束时间不能为空")
    @ExcelField(title = "结束时间", align = 2, sort = 6)
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }
    
    public int getTaskFlag()
    {
        return taskFlag;
    }
    
    public void setTaskFlag(int taskFlag)
    {
        this.taskFlag = taskFlag;
    }
    
    public String getActUrl()
    {
        return actUrl;
    }
    
    public void setActUrl(String actUrl)
    {
        this.actUrl = actUrl;
    }
    
    public List<Job> getJobList()
    {
        return jobList;
    }
    
    public void setJobList(List<Job> jobList)
    {
        this.jobList = jobList;
    }

    public List<MetaSourcePro> getConditions()
    {
        return conditions;
    }

    public void setConditions(List<MetaSourcePro> conditions)
    {
        this.conditions = conditions;
    }
}