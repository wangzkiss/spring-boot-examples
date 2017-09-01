package cn.vigor.modules.compute.bean;

public class FuncParam
{
    private String paramName;
    
    private int paramIndex;
    
    private int paramSelect;
    
    private String paramType;
    
    private String functionClass;
    
    private String returnType;
    
    private String functionType;
    
    private String functionDesc;
    
    private int functionId;
    
    public String getParamName()
    {
        return this.paramName;
    }
    
    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }
    
    public int getParamIndex()
    {
        return this.paramIndex;
    }
    
    public void setParamIndex(int paramIndex)
    {
        this.paramIndex = paramIndex;
    }
    
    public int getParamSelect()
    {
        return this.paramSelect;
    }
    
    public void setParamSelect(int paramSelect)
    {
        this.paramSelect = paramSelect;
    }
    
    public String getParamType()
    {
        return this.paramType;
    }
    
    public void setParamType(String paramType)
    {
        this.paramType = paramType;
    }
    
    public String getFunctionClass()
    {
        return functionClass;
    }
    
    public void setFunctionClass(String functionClass)
    {
        this.functionClass = functionClass;
    }
    
    public String getReturnType()
    {
        return returnType;
    }
    
    public void setReturnType(String returnType)
    {
        this.returnType = returnType;
    }
    
    public String getFunctionType()
    {
        return functionType;
    }
    
    public void setFunctionType(String functionType)
    {
        this.functionType = functionType;
    }
    
    public String getFunctionDesc()
    {
        return functionDesc;
    }
    
    public void setFunctionDesc(String functionDesc)
    {
        this.functionDesc = functionDesc;
    }
    
    public int getFunctionId()
    {
        return functionId;
    }
    
    public void setFunctionId(int functionId)
    {
        this.functionId = functionId;
    }
}