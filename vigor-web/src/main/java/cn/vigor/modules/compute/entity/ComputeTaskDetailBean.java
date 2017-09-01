package cn.vigor.modules.compute.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.vigor.modules.compute.bean.ComputeFunction;
import cn.vigor.modules.compute.bean.ComputeMetaStore;
import cn.vigor.modules.compute.bean.ComputeTaskGroup;
import cn.vigor.modules.compute.bean.InParam;
import cn.vigor.modules.compute.bean.MetaStoreField;
import cn.vigor.modules.compute.bean.Repositories;
import cn.vigor.modules.tji.entity.TaskDetail;

/**
 * 模板任务配置bean
 * @author huzeyuan
 * @version v1.0
 *
 */
public class ComputeTaskDetailBean extends TaskDetail
{
    /**
     * 
     */
    private static final long serialVersionUID = -5807872398528943314L;
    
    
    /**
     * 输入资源库
     */
    private List<Repositories> inputResources;
    
    /**
     * 输入选择资源库Id
     */
    private String inputResourceId;
    
    /**
     * 输出资源库
     */
    private List<Repositories> outputResources;
    
    /**
     * 输出选择资源库Id
     */
    private String outputResourceId;
    
    /**
     * 结果类型
     */
    private String resultType;
    
    /**
     * 输出类型
     */
    private String outputType;
    
    /**
     * 输出类型集合
     */
    private List<String> outputTypeList = Arrays.asList(new String[]{"FTP","HDFS"});
    
    /**
     * 输入数据源
     */
    private List<ComputeMetaStore> inputMetaStores;
    
    /**
     * 输出数据源
     */
    private List<ComputeMetaStore> outputMetaStores;
    
    
    /**
     * 输入数据源Id
     */
    private String inputDataSourceId;
    
    /**
     * 函数类型
     */
    private List<Map<String,String>> functionTypes;
    
    /**
     * 任务分组列表
     */
    private List<ComputeTaskGroup> taskGroups;
    
    private Map<String, ComputeMetaStore> storeMapFields;
    
    public Map<String, ComputeMetaStore> getStoreMapFields()
    {
        return storeMapFields;
    }

    public void setStoreMapFields(Map<String, ComputeMetaStore> storeMapFields)
    {
        this.storeMapFields = storeMapFields;
    }

    private List<MetaStoreField> metaStoreField;
    
    private Map<String, MetaStoreField> mapTableToStoreField ;
    
    public Map<String, MetaStoreField> getMapTableToStoreField()
    {
        return mapTableToStoreField;
    }

    public void setMapTableToSourceField(
            Map<String, MetaStoreField> mapTableToStoreField)
    {
        this.mapTableToStoreField = mapTableToStoreField;
    }

    public List<MetaStoreField> getMetaStoreField()
    {
        return metaStoreField;
    }

    public void setMetaStoreField(List<MetaStoreField> metaStoreField)
    { 
        this.metaStoreField = metaStoreField;
    }

    /**
     * 查询hive spark函数
     */
    List<ComputeFunction> functions;
    
    public List<ComputeFunction> getFunctions()
    {
        return functions;
    }

    public void setFunctions(List<ComputeFunction> functions)
    {
        this.functions = functions;
    }

    /**
     *sql语句的表list
     */
    private List<InParam> flist  ;
    
    

    public List<InParam> getFlist()
    {
        return flist;
    }

    public void setFlist(List<InParam> flist)
    {
        this.flist = flist;
    }

    public List<Repositories> getInputResources()
    {
        return inputResources;
    }

    public List<Repositories> getOutputResources()
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

    public void setInputResources(List<Repositories> inputResources)
    {
        this.inputResources = inputResources;
    }

    public void setOutputResources(List<Repositories> outputResources)
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

    public List<ComputeMetaStore> getInputMetaStores()
    {
        return inputMetaStores;
    }

    public void setInputMetaStores(List<ComputeMetaStore> inputMetaStores)
    {
        this.inputMetaStores = inputMetaStores;
    }

    public List<Map<String,String>> getFunctionTypes()
    {
        return functionTypes;
    }

    public void setFunctionTypes(List<Map<String,String>> functionTypes)
    {
        this.functionTypes = functionTypes;
    }

    public List<ComputeTaskGroup> getTaskGroups()
    {
        return taskGroups;
    }

    public void setTaskGroups(List<ComputeTaskGroup> taskGroups)
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

    public List<ComputeMetaStore> getOutputMetaStores()
    {
        return outputMetaStores;
    }

    public void setOutputMetaStores(List<ComputeMetaStore> outputMetaStores)
    {
        this.outputMetaStores = outputMetaStores;
    }

}
