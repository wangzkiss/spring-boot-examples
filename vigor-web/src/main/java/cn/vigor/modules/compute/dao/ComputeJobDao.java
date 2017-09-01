package cn.vigor.modules.compute.dao;

import java.util.List;

import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.compute.bean.ComputeJob;
import cn.vigor.modules.compute.bean.ComputeTask;
import cn.vigor.modules.compute.bean.JobType;
import cn.vigor.modules.compute.bean.Output;
import cn.vigor.modules.compute.bean.TableDependency;

@MyBatisDao
public interface ComputeJobDao {
	/**
	 * 获取任务列表
	 * @return
	 */
	public List<ComputeTask> getTasks();
	/**
	 * 通过jobType去获取任务列表
	 * @param jobType
	 * @return
	 */

	public List<ComputeTask> getTasksByJobType(int jobType);
	/**
	 * 获取作业类型列表
	 * @return
	 */

	public List<JobType> getJobTypes();
	
	/**
	 * 获取作业列表
	 * @return
	 */

	public List<ComputeJob> getJobList();
	
	/**
	 * 通过jobId去获取作业列表
	 * @param jobId
	 * @return
	 */

	public ComputeJob getJobListByJobId(int jobId);
	
	/**
	 * 通过job_id去获取依赖任务列表
	 * @return
	 */
	public List<ComputeJob> getDependJobById(int jobId);
	
	/**
	 * 新增一条任务依赖关系到 e_job_rely_relation 表中.
	 */
	public void insertTJobRelyRelation(TableDependency td);
	
	/**
	 * 
	 * @param jobId
	 * @return
	 */
	
	public Output getETLJobDetailById(int jobId);

	public Output getJobDetailById(int jobId);
	
	public Output getScriptJobDetailById(int jobId);
	
	public Output getJobDetailInMetaStoreById(int jobId);

	public int getJobTypeByTaskType(int taskType);

	public int insertJob(ComputeJob job);

	public int insertJobSchedule(ComputeJob job);

	public int addJobIdToTask(ComputeJob job);

	public int deleteJob(int jobId);

	/**
	 * 删除任务依赖关系
	 * @param jobId
	 * @return
	 */
	public int deleteRelyRelation(int jobId);
	
	public int deleteJobSchedule(int jobId);
	
	public String getTaskPro(int taskId);
}
