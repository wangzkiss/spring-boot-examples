package cn.vigor.modules.tji.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.contants.Contants;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.tji.dao.TaskDao;
import cn.vigor.modules.tji.entity.Task;
import cn.vigor.modules.tji.entity.TaskDetail;
import cn.vigor.modules.tji.entity.TaskDetailBean;
import cn.vigor.modules.tji.entity.TaskFunction;
import cn.vigor.modules.tji.entity.TaskInput;
import cn.vigor.modules.tji.entity.TaskOutput;
import cn.vigor.modules.tji.util.XmlDataUtil;

/**
 * 任务相关Service
 * 
 * @author zhangfeng
 * @version 2016-06-03
 */
@Service
public class TasksService extends CrudService<TaskDao, Task> {
	public Task get(String id) {
		return super.get(id);
	}

	public List<Task> findList(Task task) {
		return super.findList(task);
	}

	public Page<Task> findPage(Page<Task> page, Task task) {
		return super.findPage(page, task);
	}

	@Transactional(readOnly = false)
	public void save(Task task) {
		super.save(task);
	}

	/**
	 * 获取任务，用户工作流设计查询
	 * 
	 * @param task
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<String> getTaskForAct(String taskName, int page, int pageSize, String userName, int taskType) {
		int rowNo = (page - 1) * pageSize;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", taskName);
		map.put("userName", userName);
		map.put("taskType", taskType);
		map.put("rowNo", rowNo);
		map.put("pageSize", pageSize);

		return dao.getTaskForAct(map);
	}

	/**
	 * 获取任务总数，用户工作流设计查询
	 * 
	 * @param task
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public int getTaskForActCount(String taskName, String userName, int taskType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskName", taskName);
		map.put("userName", userName);
		map.put("taskType", taskType);

		return dao.getTaskForActCount(map);
	}

	/**
	 * 根据任务名称获取任务
	 * 
	 * @param taskName
	 * @return
	 */
	public Task getTaskByName(String taskName) {
		return dao.getTaskByName(taskName);
	}

	/**
	 * 根据任务ID删除任务信息
	 * 
	 * @param taskName
	 * @return
	 */
	@Transactional(readOnly = false)
	public int deleteById(int taskId) {
		return dao.deleteById(taskId);
	}

	/**
	 * 插入子任务
	 * 
	 * @param taskName
	 * @return
	 */
	@Transactional(readOnly = false)
	public int insertTaskRelation(int taskId, String sTaskName) {
		return dao.insertTaskRelation(taskId, sTaskName);
	}

	/**
	 * 根据任务ID删除子任务
	 * 
	 * @param taskName
	 * @return
	 */
	@Transactional(readOnly = false)
	public int delTaskRelation(int taskId) {
		return dao.delTaskRelation(taskId);
	}

	/**
	 * 根据任务ID获取子任务数
	 * 
	 * @param taskName
	 * @return
	 */
	public int getSTaskCountByName(String taskName) {
		return dao.getSTaskCountByName(taskName);
	}

	/**
	 * 编辑任务分组
	 * 
	 * @param taskName
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateGroup(int taskId, int groupId) {
		dao.updateGroup(taskId, groupId);
		dao.updateJobGroup(taskId, groupId);
		return 1;
	}

	/**
	 * 根据分组id获取分组下的所有任务
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Task> getTaskByGroupId(int groupId) {
		return dao.getTaskByGroupId(groupId);
	}

	/**
	 * 插入Task表
	 * 
	 * @param taskName
	 * @return int
	 */
	@Transactional(readOnly = false)
	public int insertTask(Task task) {
		return dao.insert(task);
	}

	/**
	 * 插入输出表
	 * 
	 * @param taskName
	 * @return int
	 */
	@Transactional(readOnly = false)
	public int insertTaskOutput(Map<String, Object> map) {
		return dao.insertTaskOutput(map);
	}

	/**
	 * 插入输入表
	 * 
	 * @param taskName
	 * @return int
	 */
	@Transactional(readOnly = false)
	public int insertTaskInput(Map<String, Object> map) {
		return dao.insertTaskInput(map);
	}

	/**
	 * 插入任务函数表
	 * 
	 * @return int
	 */
	@Transactional(readOnly = false)
	public int insertTaskFunction(Task task) {
		return dao.insertTaskFunction(task);
	}

	/**
	 * 编辑任务E_TASK表
	 * 
	 * @return int
	 */
	@Transactional(readOnly = false)
	public int updateTask(Task task) {
		task.preUpdate();
		return dao.update(task);
	}

	/**
	 * 编辑任务函数表
	 * 
	 * @return int
	 */
	@Transactional(readOnly = false)
	public int updateTaskFunction(Task task) {
		int c = dao.countTaskFunction(task.getTaskId(), task.getFunctionId());
		if (c > 0) {
			task.preUpdate();
			return dao.updateTaskFunction(task);
		} else {
			return dao.insertTaskFunction(task);
		}
	}

	/**
	 * 根据任务ID 找到 任务输出信息
	 * 
	 * @param taskId
	 * @return List<TaskOutput>
	 */
	public List<TaskOutput> getTaskOutputByTaskId(int taskId) {
		return this.dao.getTaskOutputByTaskId(taskId);
	}

	/**
	 * 根据任务ID 找到 任务输入信息
	 * 
	 * @param taskId
	 * @return List<TaskInput>
	 */
	public List<TaskInput> getTaskInputByTaskId(int taskId) {
		return this.dao.getTaskInputByTaskId(taskId);
	}

	/**
	 * 根据任务ID删除输出关系
	 * 
	 * @param taskName
	 * @return
	 */
	@Transactional(readOnly = false)
	public int delTaskOutPut(int taskId) {
		return dao.delTaskOutPut(taskId);
	}

	/**
	 * 根据任务ID删除输入关系
	 * 
	 * @param taskName
	 * @return
	 */
	@Transactional(readOnly = false)
	public int delTaskInPut(int taskId) {
		return dao.delTaskInPut(taskId);
	}

	/**
	 * 根据任务ID删除函数关系
	 * 
	 * @param taskName
	 * @return
	 */
	@Transactional(readOnly = false)
	public int delTaskFunction(int taskId) {
		return dao.delTaskFunction(taskId);
	}

	/**
	 * 根据任务id获取作业数
	 * 
	 * @param taskId
	 * @return
	 */
	public int getJobCountByTaskId(int taskId) {
		return dao.getJobCountByTaskId(taskId);
	}

	/**
	 * 获取任务详情
	 * 
	 * @return
	 */
	public TaskDetail getTaskDetail(int taskId) {
		// 获取任务基本信息
		TaskDetail taskDetail = dao.getTaskInfoById(taskId);
		int taskType = taskDetail.getTaskType();

		// ETL任务
		if (Contants.TASK_TYPE_ETL == taskType || Contants.TASK_TYPE_ETL_TMP == taskType) {
			// 输入输出及函数
			taskDetail.setInputMetaList(dao.getISourceList(taskId));
			taskDetail.setOutputMetaList(dao.getOStoreList(taskId));
			taskDetail.setFunctionList(dao.getFunctionList(taskId));
		}
		// 工作流
		else if (Contants.TASK_TYPE_ACT == taskType) {
			// 封装工作流模型图url
			// taskDetail.setActUrl(Global.getConfig("activiti_url")
			// + "diagram-viewer/index.html?processDefinitionId="
			// + taskDetail.getThirdTaskId());
			taskDetail.setTypeName("工作流任务");
		}
		// 计算
		else {
			// if (taskType == Contants.TASK_TYPE_KYLIN)
			// {
			// taskDetail.setActUrl(Global.getConfig("kylin_default_project") +
			// taskDetail.getTaskName());
			// taskDetail.setTypeName("X-Abase任务");
			// }
			// 输入输出
			if (taskType == 13) {// flume任务
				taskDetail.setInputMetaList(dao.getISourceList(taskId));
			} else {
				taskDetail.setInputMetaList(dao.getIStoreList(taskId));
			}

			Integer ot = dao.getOType(taskId);

			if (null != ot) {
				// 计算任务输出可能是store与result
				if (0 == dao.getOType(taskId)) {
					taskDetail.setOutputMetaList(dao.getOResultList(taskId));
				} else {
					taskDetail.setOutputMetaList(dao.getOStoreList(taskId));
				}
			}
		}

		return taskDetail;
	}

	/**
	 * 更新 e_task_output 表
	 * 
	 * @return int 是否更新成功
	 */
	@Transactional(readOnly = false)
	public int updateTaskOutput(Map<String, Object> map) {
		// 更新之前判断是否有该记录,没有则添加,有则更新
		int c = dao.countTaskOutput(map.get("taskId").toString());
		if (c > 0) {
			return dao.updateTaskOutput(map);
		} else {
			return dao.insertTaskOutput(map);
		}
	}

	/**
	 * 更新 e_task_input 表
	 * 
	 * @return int 是否更新成功
	 */
	@Transactional(readOnly = false)
	public int updateTaskInput(Map<String, Object> map) {
		// 更新之前判断是否有该记录,没有则添加,有则更新
		int c = dao.countTaskInput(map.get("taskId").toString());
		if (c > 0) {
			return dao.updateTaskInput(map);
		} else {
			return dao.insertTaskInput(map);
		}
	}

	/**
	 * 根据属性找到相对应的实体类 E_TASK_FUNCTION
	 * 
	 * @param ss
	 * @return
	 */
	public TaskFunction findETFunctionByTaskId(Integer taskId) {
		return dao.findETFunctionByTaskId(taskId);
	}

	/**
	 * 根据函数名获取id
	 * 
	 * @param taskId
	 * @return
	 */
	public Integer getFunIdByName(String functionName) {
		return dao.getFunIdByName(functionName);
	}

	/**
	 * 判断是否有数据操作权限
	 * 
	 * @return
	 */
	public Boolean getDataPermission(String dataType, String userId, String dataId) {
		String query = dao.getDataPermission(dataType, userId, dataId);
		return null != query && "Y".equals(query);
	}

	/**
	 * 根据模型id获取任务id（用户工作流）
	 * 
	 * @return
	 */
	public int getTaskIdByXmlData(String xmlData) {
		return dao.getTaskIdByXmlData(xmlData);
	}

	/**
	 * 修改流程id（用户工作流）
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateThirdTaskId(int taskId, String processId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processId", processId);
		map.put("taskId", taskId);

		return dao.updateThirdTaskId(map);
	}

	/**
	 * 保存配置任务信息
	 * 
	 * @param taskDetailBean
	 *            配置任务详情
	 * @exception @return
	 */
	@Transactional(readOnly = false)
	public void saveConfigureTaskInfo(TaskDetailBean taskDetailBean) throws Exception {
		Task task = new Task();
		MyBeanUtils.copyBeanNotNull2Bean(taskDetailBean, task);
		task.setCreateUser(task.getLoginUser().getLoginName());
		task.setComeFrom(Integer.valueOf(task.getLoginUser().getId()));
		task.setThirdTaskId("");
		// 针对所有的task,后台默认加上前缀"TASK_"
		task.setTaskName("TASK_" + task.getTaskName());
		super.save(task);
		if (taskDetailBean.getTaskType() != Contants.TASK_TYPE_PROCEDURE
				&& taskDetailBean.getTaskType() != Contants.TASK_TYPE_SHELL) {
			Integer taskId = task.getTaskId();
			// 生成e_task_input,e_task_output信息
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("inputId", taskDetailBean.getInputDataSourceId());
			inputMap.put("taskId", taskId);
			inputMap.put("inputType", taskDetailBean.getInputType());

			if (StringUtils.isNotEmpty(taskDetailBean.getInputDataSourceId())
					&& !"0".equals(taskDetailBean.getInputDataSourceId())) {
				dao.insertTaskInput(inputMap);
			}

			if (StringUtils.isNotEmpty(taskDetailBean.getOutputDataSourceId())
					&& !"0".equals(taskDetailBean.getOutputDataSourceId())) {

				String outPutId = taskDetailBean.getOutputDataSourceId();
				String resultType = taskDetailBean.getResultType();
				if (outPutId.contains(",")) {
					String[] opIds = outPutId.split(",");
					for (int i = 0; i < opIds.length; i++) {
						Map<String, Object> outputMap = new HashMap<String, Object>();
						outputMap.put("ouputId", opIds[i]);
						outputMap.put("taskId", taskId);
						outputMap.put("outputType", resultType);
						dao.insertTaskOutput(outputMap);
					}
				} else {
					Map<String, Object> outputMap = new HashMap<String, Object>();
					outputMap.put("ouputId", outPutId);
					outputMap.put("taskId", taskId);
					outputMap.put("outputType", taskDetailBean.getResultType());
					dao.insertTaskOutput(outputMap);
				}
			}

		}
		// 修改xmldata字段
		// 根据taskDetail生成xmldata字段信息(无论新增还是修改,都对xmldata进行更新)
		taskDetailBean.setTaskId(task.getTaskId());
		String xmldata = XmlDataUtil.getCustomComXmlData(taskDetailBean);
		task.setXmlData(xmldata);
		task.preUpdate();
		dao.update(task);
	}

	/**
	 * 保存配置任务信息
	 * 
	 * @param taskDetailBean
	 *            配置任务详情
	 * @exception @return
	 */
	@Transactional(readOnly = false)
	public void editConfigureTaskInfo(TaskDetailBean taskDetailBean) throws Exception {
		Task task = dao.get(taskDetailBean.getTaskId() + "");// 从数据库取出记录的值
		MyBeanUtils.copyBeanNotNull2Bean(taskDetailBean, task);
		task.preUpdate();
		dao.update(task);
		if (taskDetailBean.getTaskType() != Contants.TASK_TYPE_PROCEDURE
				&& taskDetailBean.getTaskType() != Contants.TASK_TYPE_SHELL) {
			// 修改输入输出信息
			Map<String, Object> inputMap = new HashMap<String, Object>();
			inputMap.put("inputId", taskDetailBean.getInputDataSourceId());
			inputMap.put("taskId", taskDetailBean.getTaskId());
			inputMap.put("inputType", 1);

			if (StringUtils.isNotEmpty(taskDetailBean.getInputDataSourceId())
					&& !"0".equals(taskDetailBean.getInputDataSourceId())) {
				dao.updateTaskInput(inputMap);
			}

			if (StringUtils.isNotEmpty(taskDetailBean.getOutputDataSourceId())
					&& !"0".equals(taskDetailBean.getOutputDataSourceId())) {
				String outputId = taskDetailBean.getOutputDataSourceId();
				if (taskDetailBean.getTaskType() == 13) {// flume任务类型
					// 先删除flume任务的输出
					delTaskOutPut(taskDetailBean.getTaskId());
					String[] opts = outputId.split(",");
					for (int i = 0; i < opts.length; i++) {
						Map<String, Object> outputMap = new HashMap<String, Object>();
						outputMap.put("ouputId", opts[i]);
						outputMap.put("taskId", taskDetailBean.getTaskId());
						outputMap.put("outputType", taskDetailBean.getResultType());
						dao.insertTaskOutput(outputMap);
					}
				} else {// 其他任务
					Map<String, Object> outputMap = new HashMap<String, Object>();
					outputMap.put("ouputId", outputId);
					outputMap.put("taskId", taskDetailBean.getTaskId());
					outputMap.put("outputType", taskDetailBean.getResultType());
					dao.updateTaskOutput(outputMap);
				}
			}
		}
	}
}