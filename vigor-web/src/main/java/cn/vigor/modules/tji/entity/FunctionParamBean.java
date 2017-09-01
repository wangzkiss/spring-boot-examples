package cn.vigor.modules.tji.entity;

import cn.vigor.modules.jars.entity.FunctionParam;

public class FunctionParamBean extends FunctionParam
{
    /**
     * 
     */
    private static final long serialVersionUID = 1237970052563330631L;
    
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
