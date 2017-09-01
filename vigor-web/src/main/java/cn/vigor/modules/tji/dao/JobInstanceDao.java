package cn.vigor.modules.tji.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.tji.entity.JobInstance;

/**
 * 作业实例DAO接口
 * @author zhangfeng
 * @version 2016-06-24
 */
@MyBatisDao
public interface JobInstanceDao extends CrudDao<JobInstance>
{
    /**
     * 查看底层ETL平台日志
     * @param trackId
     * @return
     */
    public Map<String,Object> getEtlLog(int trackId);
    
    /**
     * 查看工作流子任务日志
     * @param trackId
     * @param tableName
     * @return
     */
    public List<JobInstance> findListForAct(@Param("trackId")String trackId,@Param("tableName")String tableName);
    
    /**
     * 
     * @param trackId
     * @param tableName
     * @return
     */
    public JobInstance getById(@Param("trackId")String trackId,@Param("tableName")String tableName);
    
    /**
     * 
     * @param instance
     * @return
     */
    public List<Map<String,Object>> statisticByTaskType(JobInstance instance);
    
    /**
     * 统计主页耗时流程top10
     * @param instance
     * @return
     */
    public List<Map<String,Object>> statistiTop10(JobInstance instance);
    
    /**
     * 修改任务实例状态执行码
     * @param execCode 执行码值
     * @param trackId 任务实例表主键ID
     */
    public void updateInstanceStatus(@Param("execCode")Integer execCode,@Param("trackId")String trackId);
}