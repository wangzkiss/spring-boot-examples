package cn.vigor.modules.jars.entity;

import cn.vigor.common.persistence.DataEntity;

/**
 * 函数类型实体类
 * @author Hewei
 */
public class FunctionType extends DataEntity<FunctionType>{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 类型 ID　
     */
    private int typeId;
    
    /**
     * 类型名称 
     */
    private String typeName;
    
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
}
