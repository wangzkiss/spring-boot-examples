package cn.vigor.modules.compute.dao;

import java.util.List;

import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.compute.bean.ComputeFunction;
import cn.vigor.modules.compute.bean.ComputeFunctionParam;
import cn.vigor.modules.compute.bean.ComputeTask;
import cn.vigor.modules.compute.bean.ComputeTaskGroup;
import cn.vigor.modules.compute.bean.InParam;
import cn.vigor.modules.compute.bean.Output;
import cn.vigor.modules.compute.bean.OutputField;
import cn.vigor.modules.compute.bean.OutputTemplate;
import cn.vigor.modules.compute.bean.SelfProgramArgument;
import cn.vigor.modules.compute.bean.Source;
import cn.vigor.modules.compute.bean.TaskType;

@MyBatisDao
public interface ComputeTaskDao {
	public List<Source> getSources();

	public List<Source> getSourcesByTypes(int[] type);

	public List<Source>getMongoSources();
	
	public List<Source> getSourcesByTypesWithoutHd();
	
	public List<Source> getETLSources();

	public List<Source> getFlumeETLSources();

	public List<Output> getOutputs();
	/**
	 * 获取所有的函数
	 * @return List<Function>
	 */
	public List<ComputeFunction> getFunctions();
	
	/**
	 * 根据 函数ID找到对应的函数参数集合
	 * @param FunctionId
	 * @return List<FunctionParam>
	 */
	public List<ComputeFunctionParam> getFunctionParams(int FunctionId);
	

	public List<ComputeFunction> getFunctionsByType(int type);
	
	public List<ComputeFunction> getFunctionsByTypeWithParams(InParam fstore);

	public Integer insertMetaResult(Output output);

	public Integer insertMetaStore(Output output);

	public Integer insertMetaResultPro(OutputField field);

	public Integer insertMetaStorePro(OutputField field);

	public List<TaskType> getTaskTypes();

	public Integer insertTask(Output output);

	public Integer insertTaskInput(Output output);

	public Integer insertETLTaskInput(Output output);

	public Integer insertTaskOutput(Output output);

	public Integer insertTaskPro(Output output);

	/**
	 * 判断e_meta_store 里是否插入了store_name复数的数据
	 * @param output
	 * @return
	 */
	public Integer getETLMetaStoreSameName(Output output);
	
	/**
	 * 判断e_meta_store 里是否插入了store_file复数的数据
	 * @param output
	 * @return
	 */
	public Integer getETLMetaStoreSameFile(Output output);

	public Integer getMetaResultFileSameName(Output output);
	
	public Integer getMetaResultSameName(Output output);
	
	public Integer getMetaResultSameNameWithoutType(Output output);

	public List<ComputeTask> getTaskList(InParam fstore);
	
	public ComputeTask getTaskById(int taskId);
	
	public String getTaskXmlData(int taskId);
	
	public List<OutputTemplate> getOutputTemplates();

	public List<OutputTemplate> getOutputTemplatesByTypes(int[] types);
	
	public List<OutputTemplate> getOutputTemplatesByTypesName(String[] types);

	public List<OutputTemplate> getHDFSOutputTemplates();
	
	public List<OutputTemplate> getHADOOPHIVEOutputTemplates();
	
	public List<OutputTemplate> getEtlOutputTemplates();
	
	public List<OutputTemplate> getFlumeEtlOutputTemplatesNew();
	
	public List<OutputTemplate> getFlumeEtlOutputTemplatesUpdate();

	public int deleteMetaStore(int storeId);

	public int deleteMetaStorePro(int storeId);

	public int deleteMetaResult(int resultId);

	public int deleteMetaResultPro(int resultId);

	public int deleteTask(int taskId);
	
	public int deleteTaskInput(int taskId);

	public int deleteTaskOutput(int taskId);

	public int deleteTaskPro(int taskId);

	
	public Object getTaskId(Output output);
	
	public List<SelfProgramArgument> getArgumentByFunctionID(int functionId);
	
	
	
	public int getTaskSameName(String taskName);
	
	
	/**
	 * 获取分组信息
	 * @return
	 */
	public List<ComputeTaskGroup> getTaskGroup();
	
	
	public int getSTaskCountByName(String taskName);
	
	public int getJobCountTaskId(int taskId);
}
