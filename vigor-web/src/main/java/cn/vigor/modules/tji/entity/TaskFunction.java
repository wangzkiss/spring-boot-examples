package cn.vigor.modules.tji.entity;

import cn.vigor.common.persistence.DataEntity;

public class TaskFunction extends DataEntity<TaskFunction>{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 任务id
     */
    private Integer taskId;
    
    /**
     * 函数Id 
     */
    private Integer functionId;
    
    /**
     * 函数参数
     */
    private String functionParam;
    
    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Integer getFunctionId()
    {
        return functionId;
    }

    public void setFunctionId(Integer functionId)
    {
        this.functionId = functionId;
    }

    public String getFunctionParam()
    {
        return functionParam;
    }

    public void setFunctionParam(String functionParam)
    {
        this.functionParam = functionParam;
    }
}
