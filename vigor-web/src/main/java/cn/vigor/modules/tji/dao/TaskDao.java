package cn.vigor.modules.tji.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.tji.entity.Task;
import cn.vigor.modules.tji.entity.TaskBaseType;
import cn.vigor.modules.tji.entity.TaskDetail;
import cn.vigor.modules.tji.entity.TaskFunction;
import cn.vigor.modules.tji.entity.TaskInput;
import cn.vigor.modules.tji.entity.TaskOutput;

/**
 * 任务相关DAO接口
 * @author zhangfeng
 * @version 2016-06-03
 */
@MyBatisDao
public interface TaskDao extends CrudDao<Task>
{
    /**
     * 获取任务，用户工作流设计查询
     * @param map
     * @return
     */
    public List<String> getTaskForAct(Map<String, Object> map);
    
    /**
     * 获取任务总数，用户工作流设计查询
     * @param map
     * @return
     */
    public int getTaskForActCount(Map<String, Object> map);
    
    /**
     * 根据任务名称获取任务
     * @param taskName
     * @return
     */
    public Task getTaskByName(String taskName);
    
    /**
     * 根据任务ID获取任务信息
     * @param taskName
     * @return
     */
    public TaskDetail getTaskInfoById(int taskId);
    
    /**
     * 根据任务ID删除任务信息
     * @param taskName
     * @return
     */
    public int deleteById(int taskId);
    
    /**
     * 插入子任务
     * @param taskName
     * @return
     */
    public int insertTaskRelation(@Param("taskId") int taskId,
            @Param("sTaskName") String sTaskName);
    
    /**
     * 根据任务ID删除子任务
     * @param taskName
     * @return
     */
    public int delTaskRelation(int taskId);
    
    /**
     * 根据任务ID获取子任务数
     * @param taskName
     * @return
     */
    public int getSTaskCountByName(String taskName);
    
    /**
     * 编辑任务分组
     * @param taskName
     * @return
     */
    public int updateGroup(@Param("taskId") int taskId,
            @Param("groupId") int groupId);
    
    /**
     * 编辑作业分组
     * @param taskName
     * @return
     */
    public int updateJobGroup(@Param("taskId") int taskId,
            @Param("groupId") int groupId);
    
    /**
     * 根据分组id获取分组下的所有任务
     * @param groupId
     * @return
     */
    public List<Task> getTaskByGroupId(int groupId);
    
    /**
     * 插入输出表
     * @return
     */
    public int insertTaskOutput(Map<String, Object> map);
    
    /**
     * 插入输入表
     * @return
     */
    public int insertTaskInput(Map<String, Object> map);
    
    /**
     * 更新Output表
     * @param map
     * @return int
     */
    public int updateTaskOutput(Map<String, Object> map);
    
    /**
     * 更新Input表
     * @param map
     * @return int
     */
    public int updateTaskInput(Map<String, Object> map);
    
    /**
     * 插入任务函数表
     * @return
     */
    public int insertTaskFunction(Task task);
    
    /**
     * 编辑任务函数表
     * @return int
     */
    public int updateTaskFunction(Task task);
    
    /**
     * 根据任务ID 找到 任务输出信息
     * @param taskId
     * @return
     */
    public List<TaskOutput> getTaskOutputByTaskId(int taskId);
    
    /**
     * 根据任务ID 找到 任务输入信息
     * @param taskId
     * @return
     */
    public List<TaskInput> getTaskInputByTaskId(int taskId);
    
    /**
     * 根据任务ID删除输出关系
     * @param taskName
     * @return
     */
    public int delTaskOutPut(int taskId);
    
    /**
     * 根据任务ID删除输入关系
     * @param taskName
     * @return
     */
    public int delTaskInPut(int taskId);
    
    /**
     * 根据任务ID删除函数关系
     * @param taskName
     * @return
     */
    public int delTaskFunction(int taskId);
    
    /**
     * 根据任务id获取输入数据源
     * @param taskId
     * @return
     */
    public List<Map<String, Object>> getISourceList(int taskId);
    
    /**
     * 根据任务id获取输出平台存储
     * @param taskId
     * @return
     */
    public List<Map<String, Object>> getOStoreList(int taskId);
    
    /**
     * 根据任务id获取函数
     * @param taskId
     * @return
     */
    public List<Map<String, Object>> getFunctionList(int taskId);
    
    /**
     * 根据任务id获取作业数
     * @param taskId
     * @return
     */
    public int getJobCountByTaskId(int taskId);
    
    /**
     * 根据任务id获取输入平台存储
     * @param taskId
     * @return
     */
    public List<Map<String, Object>> getIStoreList(int taskId);
    
    /**
     * 根据任务id获取输出结果集
     * @param taskId
     * @return
     */
    public List<Map<String, Object>> getOResultList(int taskId);
    
    /**
     * 获取任务输出的类型,一个任务只能有一种输出
     * @param taskId
     * @return
     */
    public Integer getOType(int taskId);
    
    /**
     * 获取所有的任务类型信息
     * @return List<TaskBaseType>
     */
    public List<TaskBaseType> getAllTaskType();
    
    /**
     * 根据属性找到相对应的实体类 E_TASK_FUNCTION
     * @param ss
     * @return
     */
    public TaskFunction findETFunctionByTaskId(Integer taskId);
    
    /**
     * 根据函数名获取id
     * @param taskId
     * @return
     */
    public Integer getFunIdByName(String functionName);
    
    /**
     * 判断是否有数据操作权限
     * @return
     */
    public String getDataPermission(@Param("dataType") String dataType,
            @Param("userId") String userId, @Param("dataId") String dataId);
    
    /**
     * 查询输出表
     * @param taskId 任务Id
     * @return
     */
    public int countTaskOutput(String taskId);
    
    /**
     * 查询输入表
     * @param taskId 任务ID
     * @return
     */
    public int countTaskInput(String taskId);
    
    /**
     * 查询任务函数
     * @param taskId 任务ID
     * @param functionId 函数ID
     * @return
     */
    public int countTaskFunction(@Param("taskId") int taskId,
            @Param("functionId") int functionId);
    
    /**
     * 根据模型id获取任务id（用户工作流）
     * @return
     */
    public int getTaskIdByXmlData(String xmlData);
    
    /**
     * 修改流程id（用户工作流）
     * @return
     */
    public int updateThirdTaskId(Map<String, Object> map);
    
}