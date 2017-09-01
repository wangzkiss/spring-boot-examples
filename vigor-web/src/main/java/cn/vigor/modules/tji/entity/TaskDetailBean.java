package cn.vigor.modules.tji.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.vigor.common.utils.SpringContextHolder;
import cn.vigor.modules.jars.dao.FunctionDao;
import cn.vigor.modules.jars.entity.Function;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.tji.bean.InputDsfRelations;

/**
 * 模板任务配置bean
 * @author huzeyuan
 * @version v1.0
 *
 */
public class TaskDetailBean extends TaskDetail
{
    /**
     * 
     */
    private static final long serialVersionUID = -5807872398528943314L;
    
    /**
     * 函数类型
     * 1 spark函数
     * 4 MR函数
     */
    private String functionType;
    
    /**
     * 输入资源库
     */
    private List<MetaRepo> inputResources;
    
    /**
     * 输入选择资源库Id
     */
    private String inputResourceId;
    
    /**
     * 输出资源库
     */
    private List<MetaRepo> outputResources;
    
    /**
     * 输出选择资源库Id
     */
    private String outputResourceId;
    
    /**
     * 结果类型(即计算任务输出类型,在flume任务中,可以同时输出多个类型,现将resultType支持多个输出类型,并用逗号隔开)
     */
    private String resultType;
    
    /**
     * 输出类型
     */
    private String outputType;
    
    /**
     * 输入类型
     */
    private String inputType = "1";//默认输入类型是平台存储(flume任务时,输入类型为外部数据源0)
    
    /**
     * 输出类型集合
     */
    private List<String> outputTypeList = Arrays.asList(new String[]{"FTP","HDFS"});
    
    /**
     * 数据源
     */
    private List<MetaStore> metaStores;
    
    /**
     * 输入数据源Id
     */
    private String inputDataSourceId;
    
    /**
     * 輸出数据源Id(一个输入可以有多个输出,在flume中体现了多个输出,多个输出id用逗号(,)分割,保证了之前计算任务的正常)
     */
    private String outputDataSourceId;
    
    /**
     * 函数类型
     */
    private List<Map<String,String>> functionTypes;
    
    /**
     * 任务分组列表
     */
    private List<TaskGroup> taskGroups;
    
    /**
     * MR/spark選擇的函數
     */
    private Function function;
    
    /**
     * 參數設置
     */
    private List<FunctionParamBean> functionParams;
    
    /**
     * 数据质量任务操作字段属性对象(支持多个)
     */
    private List<InputDsfRelations> inputDsfRelations;
    
    public String getFunctionType()
    {
        return functionType;
    }

    public List<MetaRepo> getInputResources()
    {
        return inputResources;
    }

    public List<MetaRepo> getOutputResources()
    {
        return outputResources;
    }

    public String getResultType()
    {
        return resultType;
    }

    public String getOutputType()
    {
        return outputType;
    }

    public void setFunctionType(String functionType)
    {
        this.functionType = functionType;
    }

    public void setInputResources(List<MetaRepo> inputResources)
    {
        this.inputResources = inputResources;
    }

    public void setOutputResources(List<MetaRepo> outputResources)
    {
        this.outputResources = outputResources;
    }

    public void setResultType(String resultType)
    {
        this.resultType = resultType;
    }

    public void setOutputType(String outputType)
    {
        this.outputType = outputType;
    }

    public List<MetaStore> getMetaStores()
    {
        return metaStores;
    }

    public void setMetaStores(List<MetaStore> metaStores)
    {
        this.metaStores = metaStores;
    }

    public List<Map<String,String>> getFunctionTypes()
    {
        return functionTypes;
    }

    public void setFunctionTypes(List<Map<String,String>> functionTypes)
    {
        this.functionTypes = functionTypes;
    }

    public List<TaskGroup> getTaskGroups()
    {
        return taskGroups;
    }

    public void setTaskGroups(List<TaskGroup> taskGroups)
    {
        this.taskGroups = taskGroups;
    }

    public String getInputResourceId()
    {
        return inputResourceId;
    }

    public void setInputResourceId(String inputResourceId)
    {
        this.inputResourceId = inputResourceId;
    }

    public String getOutputResourceId()
    {
        return outputResourceId;
    }

    public void setOutputResourceId(String outputResourceId)
    {
        this.outputResourceId = outputResourceId;
    }

    public String getInputDataSourceId()
    {
        return inputDataSourceId;
    }

    public void setInputDataSourceId(String inputDataSourceId)
    {
        this.inputDataSourceId = inputDataSourceId;
    }

    public List<String> getOutputTypeList()
    {
        return outputTypeList;
    }

    public void setOutputTypeList(List<String> outputTypeList)
    {
        this.outputTypeList = outputTypeList;
    }

    public Function getFunction()
    {
        if(getFunctionId()!=0){
            FunctionDao functionDao = SpringContextHolder.getBean(FunctionDao.class);
            return functionDao.get(getFunctionId()+"");
        }
        return function;
    }

    public void setFunction(Function function)
    {
        this.function = function;
    }

    public List<FunctionParamBean> getFunctionParams()
    {
        return functionParams;
    }

    public void setFunctionParams(List<FunctionParamBean> functionParams)
    {
        this.functionParams = functionParams;
    }

    public String getOutputDataSourceId()
    {
        return outputDataSourceId;
    }

    public void setOutputDataSourceId(String outputDataSourceId)
    {
        this.outputDataSourceId = outputDataSourceId;
    }

    public List<InputDsfRelations> getInputDsfRelations()
    {
        return inputDsfRelations;
    }

    public void setInputDsfRelations(List<InputDsfRelations> inputDsfRelations)
    {
        this.inputDsfRelations = inputDsfRelations;
    }

    public String getInputType()
    {
        return inputType;
    }

    public void setInputType(String inputType)
    {
        this.inputType = inputType;
    }
}
