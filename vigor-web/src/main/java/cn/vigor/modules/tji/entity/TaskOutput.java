package cn.vigor.modules.tji.entity;

import cn.vigor.common.persistence.DataEntity;

/**
 * 任务输出信息实体类
 * @author Hewei 
 *
 */
public class TaskOutput extends DataEntity<TaskOutput>{
    private static final long serialVersionUID = 1L;
    
    /**
     * 输出Id 平台存储Id
     */
    private int outputId;
    
    /**
     *任务 ID
     */
    private int taskId;
    
    /**
     * 输出类型
     */
    private int outputType;
    
    public int getOutputId()
    {
        return outputId;
    }

    public void setOutputId(int outputId)
    {
        this.outputId = outputId;
    }

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public int getOutputType()
    {
        return outputType;
    }

    public void setOutputType(int outputType)
    {
        this.outputType = outputType;
    }
}
