package cn.vigor.modules.tji.entity;

import cn.vigor.common.persistence.DataEntity;

/**
 * 任务输入实体类
 * @author Hewei
 *
 */
public class TaskInput extends DataEntity<TaskInput>{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 外部数据源Id
     */
    private int inputId;
    
    /**
     * 任务Id
     */
    private int taskId;
    
    /**
     * 输入类型
     */
    private int intputType;
    
    public int getInputId()
    {
        return inputId;
    }

    public void setInputId(int inputId)
    {
        this.inputId = inputId;
    }

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public int getIntputType()
    {
        return intputType;
    }

    public void setIntputType(int intputType)
    {
        this.intputType = intputType;
    }
}
