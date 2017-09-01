package cn.vigor.modules.tji.entity;

import cn.vigor.common.persistence.DataEntity;

public class TaskBaseType extends DataEntity<TaskBaseType>
{
    
    private static final long serialVersionUID = 1L;
    
    private int typeId;
    
    private String typeName;
    
    private int status;
    
    public int getTypeId()
    {
        return typeId;
    }
    
    public void setTypeId(int typeId)
    {
        this.typeId = typeId;
    }
    
    public String getTypeName()
    {
        return typeName;
    }
    
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
}
