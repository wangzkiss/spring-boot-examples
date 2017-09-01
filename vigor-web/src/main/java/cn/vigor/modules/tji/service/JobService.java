package cn.vigor.modules.tji.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.config.Global;
import cn.vigor.common.contants.Contants;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.HttpUtil;
import cn.vigor.modules.tji.dao.JobDao;
import cn.vigor.modules.tji.dao.TaskDao;
import cn.vigor.modules.tji.entity.Job;
import cn.vigor.modules.tji.entity.JobDetail;

/**
 * 作业相关Service
 * @author zhangfeng
 * @version 2016-06-06
 */
@Service
@Transactional(readOnly = true)
public class JobService extends CrudService<JobDao, Job>
{
    /**
     * 任务
     */
    @Autowired
    protected TaskDao taskDao;
    
    public Job get(String id)
    {
        return super.get(id);
    }
    
    public List<Job> findList(Job job)
    {
        return super.findList(job);
    }
    
    public Page<Job> findPage(Page<Job> page, Job job)
    {
        return super.findPage(page, job);
    }
    
    @Transactional(readOnly = false)
    public void save(Job job)
    {
        super.save(job);
    }
    
    @Transactional(readOnly = false)
    public void delete(Job job)
    {
        super.delete(job);
    }
    
    /**
     * 根据taskid获取任务信息
     * @param taskId
     * @return
     */
    public List<Job> getJobByTaskId(int taskId)
    {
        return dao.getJobByTaskId(taskId);
    }
    
    /**
     * 根据taskid获取任务数
     * @param taskId
     * @return
     */
    public int getJobCountTaskId(int taskId)
    {
        return dao.getJobCountTaskId(taskId);
    }
    
    /**
     * 修改作业状态
     * @param taskId
     * @return
     */
    @Transactional(readOnly = false)
    public int updateStatus(int jobId, int jobStatus, int flag)
    {
        return dao.updateStatus(jobId, jobStatus, flag);
    }
    
    /**
    * 修改作业状态
    * @param taskId
    * @return
    */
    public List<Integer> getJobsByGroupId(int groupId)
    {
        return dao.getJobsByGroupId(groupId);
    }
    
    public List<Map<String, Object>> getUserForEmail()
    {
        return dao.getUserForEmail();
    }
    
    /**
     * 删除作业
     * @param taskId
     * @return
     */
    @Transactional(readOnly = false)
    public int delJob(Job job)
    {
        //作业
        dao.delete(job);
        dao.deleteInstance(job.getJobId());
        dao.deleteJobSch(job.getJobId());
        
        return 1;
    }
    
    /**
     * 获取调度url
     * @param taskId
     * @return
     * @throws Exception 
     */
    public String getSchUrl()
    {
        String schUrl = null;
        
        //获取调度集群ip port
        List<Map<String, Object>> schList = dao.getSchCluster();
        
        //循环检查哪一台是活的
        for (Map<String, Object> map : schList)
        {
            //拼装url
            schUrl = "http://" + map.get("nodeIp") + ":" + map.get("nodePort")
                    + "/vigordata-scheduler";
            
            if (HttpUtil.isActive(schUrl))
            {
                break;
            }
        }
        
        return schUrl;
    }
    
    /**
     * 获取作业详情
     * @param jobId
     * @return
     */
    public JobDetail getJobDetail(int jobId)
    {
        //获取任务作业详情,不包括输入输出
        JobDetail jobDetail = dao.getJobDetailById(jobId);
        
        int taskType = jobDetail.getTaskType();
        int taskId = jobDetail.getTaskId();
        
        //ETL
        if (Contants.TASK_TYPE_ETL == taskType
                || Contants.TASK_TYPE_ETL_TMP == taskType
                || Contants.TASK_TYPE_FLUME == taskType)
        {
            //输入输出及函数
            jobDetail.setInputMetaList(taskDao.getISourceList(taskId));
            jobDetail.setOutputMetaList(taskDao.getOStoreList(taskId));
            jobDetail.setFunctionList(taskDao.getFunctionList(taskId));
        }
        //工作流
        else if (Contants.TASK_TYPE_ACT == taskType)
        {
            //封装工作流模型图url
            jobDetail.setActUrl(Global.getConfig("activiti_url")
                    + "diagram-viewer/index.html?processDefinitionId="
                    + jobDetail.getThirdTaskId());
        }
        //计算
        else
        {
            //输入输出
            jobDetail.setInputMetaList(taskDao.getIStoreList(taskId));
            jobDetail.setOutputType(taskDao.getOType(taskId));
            
            if (null != jobDetail.getOutputType())
            {
                //计算任务输出可能是store与result
                if (0 == jobDetail.getOutputType())
                {
                    jobDetail.setOutputMetaList(taskDao.getOResultList(taskId));
                }
                else
                {
                    jobDetail.setOutputMetaList(taskDao.getOStoreList(taskId));
                }
            }
            
            //平台list
            jobDetail.setNodeList(
                    dao.getPlatformNodeList(jobDetail.getPlatformId()));
        }
        
        return jobDetail;
    }
}