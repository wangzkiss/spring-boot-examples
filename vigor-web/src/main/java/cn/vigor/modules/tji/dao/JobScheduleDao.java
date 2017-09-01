/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.tji.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.tji.entity.JobSchedule;

/**
 * 排班及统计DAO接口
 * @author zhangfeng
 * @version 2016-06-24
 */
@MyBatisDao
public interface JobScheduleDao extends CrudDao<JobSchedule>
{
    /**
     * 改变排班状态
     * @param schId
     * @param pauseFlag
     * @return
     */
    public int updatePauseFlag(@Param("schId") int schId,
            @Param("pauseFlag") int pauseFlag);
    
    /**
     * 作业统计
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> getJobStatis(JobSchedule jobSchedule);
    
    /**
     * 作业统计
     * @param jobSchedule
     * @return
     */
    public List<JobSchedule> getJobByScheTime(JobSchedule jobSchedule);
    
    /**
     * 日报
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> getDayReport(@Param("date") String date, @Param("currentUser")String currentUser);
    
    /**
     * 月报
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> getMonthReport(@Param("date") String date, @Param("currentUser")String currentUser);
    
}