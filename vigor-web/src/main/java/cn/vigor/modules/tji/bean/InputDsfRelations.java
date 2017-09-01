package cn.vigor.modules.tji.bean;

import java.io.Serializable;

public class InputDsfRelations implements Serializable
{

    private static final long serialVersionUID = -2809830205906945847L;
    
    /**
     * 操作函数id
     */
    private String functionId;
    
    /**
     * 操作函数名称
     */
    private String functionName;
    
    /**
     * 函数主类
     */
    private String functionClass;
    
    private String functionJarId;
    
    /**
     * 输入数据源id
     */
    private String dataSourceId;
    
    /**
     * 输入数据源操作属性id
     */
    private String dataSourceProId;
    
    /**
     * 输入数据源操作属性名称
     */
    private String dataSourceProName;
    
    /**
     * 输入数据源操作属性索引
     */
    private int dataSourceProIndex;
    
    /**
     * 输入数据源操作属性类型
     */
    private String dataSourceProType;
    
    private String functionJarName;
    
    private String functionJarPath;
    
    private String functionExpr;
    
    private String dataSourceName; 
    
    private String dataSourceProFormat;

    public String getDataSourceProType()
    {
        return dataSourceProType;
    }

    public String getFunctionJarName()
    {
        return functionJarName;
    }

    public String getFunctionJarPath()
    {
        return functionJarPath;
    }

    public String getFunctionExpr()
    {
        return functionExpr;
    }

    public String getDataSourceName()
    {
        return dataSourceName;
    }

    public void setDataSourceProType(String dataSourceProType)
    {
        this.dataSourceProType = dataSourceProType;
    }

    public void setFunctionJarName(String functionJarName)
    {
        this.functionJarName = functionJarName;
    }

    public void setFunctionJarPath(String functionJarPath)
    {
        this.functionJarPath = functionJarPath;
    }

    public void setFunctionExpr(String functionExpr)
    {
        this.functionExpr = functionExpr;
    }

    public void setDataSourceName(String dataSourceName)
    {
        this.dataSourceName = dataSourceName;
    }

    public String getFunctionId()
    {
        return functionId;
    }

    public String getFunctionName()
    {
        return functionName;
    }

    public String getFunctionClass()
    {
        return functionClass;
    }

    public String getDataSourceId()
    {
        return dataSourceId;
    }

    public String getDataSourceProId()
    {
        return dataSourceProId;
    }

    public String getDataSourceProName()
    {
        return dataSourceProName;
    }

    public int getDataSourceProIndex()
    {
        return dataSourceProIndex;
    }

    public void setFunctionId(String functionId)
    {
        this.functionId = functionId;
    }

    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
    }

    public void setFunctionClass(String functionClass)
    {
        this.functionClass = functionClass;
    }

    public void setDataSourceId(String dataSourceId)
    {
        this.dataSourceId = dataSourceId;
    }

    public void setDataSourceProId(String dataSourceProId)
    {
        this.dataSourceProId = dataSourceProId;
    }

    public void setDataSourceProName(String dataSourceProName)
    {
        this.dataSourceProName = dataSourceProName;
    }

    public void setDataSourceProIndex(int dataSourceProIndex)
    {
        this.dataSourceProIndex = dataSourceProIndex;
    }

    public String getDataSourceProFormat()
    {
        return dataSourceProFormat;
    }

    public void setDataSourceProFormat(String dataSourceProFormat)
    {
        this.dataSourceProFormat = dataSourceProFormat;
    }

    public String getFunctionJarId()
    {
        return functionJarId;
    }

    public void setFunctionJarId(String functionJarId)
    {
        this.functionJarId = functionJarId;
    }
}
