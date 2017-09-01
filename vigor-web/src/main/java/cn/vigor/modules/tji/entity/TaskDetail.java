package cn.vigor.modules.tji.entity;

import java.util.List;
import java.util.Map;

/**
 * 任务详情
 * @author DELL
 */
public class TaskDetail extends Task
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 工作流逻辑图
     */
    private String actUrl;
    
    /**
     * 输入
     */
    private List<Map<String, Object>> inputMetaList;
    
    /**
     * 输出
     */
    private List<Map<String, Object>> outputMetaList;
    
    /**
     * 函数
     */
    private List<Map<String, Object>> functionList;
    
    public String getActUrl()
    {
        return actUrl;
    }
    
    public void setActUrl(String actUrl)
    {
        this.actUrl = actUrl;
    }
    
    public List<Map<String, Object>> getInputMetaList()
    {
        return inputMetaList;
    }
    
    public void setInputMetaList(List<Map<String, Object>> inputMetaList)
    {
        this.inputMetaList = inputMetaList;
    }
    
    public List<Map<String, Object>> getOutputMetaList()
    {
        return outputMetaList;
    }
    
    public void setOutputMetaList(List<Map<String, Object>> outputMetaList)
    {
        this.outputMetaList = outputMetaList;
    }
    
    public List<Map<String, Object>> getFunctionList()
    {
        return functionList;
    }
    
    public void setFunctionList(List<Map<String, Object>> functionList)
    {
        this.functionList = functionList;
    }
}
