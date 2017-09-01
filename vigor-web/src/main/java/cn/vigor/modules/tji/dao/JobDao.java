package cn.vigor.modules.tji.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.tji.entity.Job;
import cn.vigor.modules.tji.entity.JobDetail;

/**
 * 作业相关DAO接口
 * @author zhangfeng
 * @version 2016-06-06
 */
@MyBatisDao
public interface JobDao extends CrudDao<Job>
{
    /**
     * 根据taskid获取任务信息
     * @param taskId
     * @return
     */
    public List<Job> getJobByTaskId(int taskId);
    
    /**
     * 根据taskid获取任务数
     * @param taskId
     * @return
     */
    public int getJobCountTaskId(int taskId);
    
    /**
     * 获取作业详情
     * @param taskId
     * @return
     */
    public JobDetail getJobDetailById(int jobId);
    
    /**
     * 获取调度集群ip port
     * @param taskId
     * @return
     */
    public List<Map<String, Object>> getSchCluster();
    
    /**
     * 修改作业状态 执行状态
     * @param taskId
     * @return
     */
    public int updateStatus(@Param("jobId") int jobId,
            @Param("status") int status, @Param("flag") int flag);
    
    /**
     * 获取节点list
     * @param platformId
     * @return
     */
    public List<Map<String, Object>> getPlatformNodeList(int platformId);
    
    /**
     * 获取分组下的所有作业,未审核的
     * @param taskId
     * @return
     */
    public List<Integer> getJobsByGroupId(int groupId);
    
    /**
     * 删除排班
     * @param jobId
     * @return
     */
    public int deleteJobSch(int jobId);
    
    /**
     * 删除实例
     * @param jobId
     * @return
     */
    public int deleteInstance(int jobId);
    
    public List<Map<String, Object>> getUserForEmail();
    
}