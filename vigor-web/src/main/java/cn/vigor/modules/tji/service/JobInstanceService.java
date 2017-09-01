package cn.vigor.modules.tji.service;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.contants.Contants;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.modules.sys.utils.UserUtils;
import cn.vigor.modules.tji.dao.JobInstanceDao;
import cn.vigor.modules.tji.entity.JobInstance;

/**
 * 作业实例Service
 * @author zhangfeng
 * @version 2016-06-24
 */
@Service
@Transactional(readOnly = true)
public class JobInstanceService extends CrudService<JobInstanceDao, JobInstance>
{
    
    public JobInstance get(String id,String execTime)
    {
    	Date date = DateUtils.parseDate(execTime);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int year = calendar.get(Calendar.YEAR);
    	int month = calendar.get(Calendar.MONTH)+1;
    	String s = month>9?String.valueOf(month):"0"+month;
    	String tableName = new JobInstance().getTableName();
    	if(!s.equals(DateUtils.getMonth())){
    		tableName = tableName + year + s;
    	}
        return dao.getById(id, tableName);
    }
    
    public List<JobInstance> findList(JobInstance jobInstance)
    {
        return super.findList(jobInstance);
    }
    
    public Page<JobInstance> findPage(Page<JobInstance> page,
            JobInstance jobInstance)
    {
        return super.findPage(page, jobInstance);
    }
    
    @Transactional(readOnly = false)
    public void save(JobInstance jobInstance)
    {
        super.save(jobInstance);
    }
    
    @Transactional(readOnly = false)
    public void delete(JobInstance jobInstance)
    {
        super.delete(jobInstance);
    }
    
    /**
     * 查看工作流子任务日志
     * @param trackId
     * @return
     */
    public List<JobInstance> findListForAct(String trackId,String execTime)
    {
    	Date date = DateUtils.parseDate(execTime);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int year = calendar.get(Calendar.YEAR);
    	int month = calendar.get(Calendar.MONTH)+1;
    	String s = month>9?String.valueOf(month):"0"+month;
    	String tableName = new JobInstance().getTableName();
    	if(!s.equals(DateUtils.getMonth())){
    		tableName = tableName + year + s;
    	}
        return dao.findListForAct(trackId,tableName);
    }
    
//    /**
//     * 日志
//     * @param trackId
//     * @param taskType
//     */
//    public String getLog(Integer trackId, Integer taskType)
//    {
//        String log = "";
//        
//        //etl任务
//        if (Contants.TASK_TYPE_ETL == taskType
//                || Contants.TASK_TYPE_ETL_TMP == taskType)
//        {
//            Map<String, Object> map = dao.getEtlLog(trackId);
//            
//            if (null != map && !map.isEmpty())
//            {
//                log = (String) map.get("logField");
//            }
//        }
//        else
//        {
//            log = dao.getPlatformLog(trackId);
//        }
//        
//        return log;
//    }
    
    /**
     * 获取详情
     * @param trackId
     */
    public JobInstance getDetail(String trackId,String execTime)
    {
    	Date date = DateUtils.parseDate(execTime);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int year = calendar.get(Calendar.YEAR);
    	int month = calendar.get(Calendar.MONTH)+1;
    	String s = month>9?String.valueOf(month):"0"+month;
    	String tableName = new JobInstance().getTableName();
    	if(!s.equals(DateUtils.getMonth())){
    		tableName = tableName + year + s;
    	}
        JobInstance entity = dao.getById(trackId, tableName);
        
        if (entity == null)
        {
            entity = new JobInstance();
        }
        
        //etl任务还需要封装源表与目标表记录数
        if (Contants.TASK_TYPE_ETL == entity.getTaskType()
                || Contants.TASK_TYPE_ETL_TMP == entity.getTaskType())
        {
            //计算平均速度
            long execS = 0;
            
            //任务实际执行秒数
            if (null != entity.getRespTime() && null != entity.getExecTime())
            {
                long zTime = entity.getRespTime().getTime()
                        - entity.getExecTime().getTime();
                execS = (zTime < 0 ? entity.getExecTime().getTime()
                        - entity.getRespTime().getTime() : zTime) / 1000;
            }
            
            DecimalFormat dcf = new DecimalFormat("#.##");
            String avgSpeed = 0 != entity.getInputNum() && 0 != execS
                    ? dcf.format((double) entity.getInputNum() / execS) + "条/秒"
                    : "";
            entity.setAvgSpeed(avgSpeed);
        }
        
        return entity;
    }
    
    /**
     *  统计主页耗时流程top10
     * @param hourRegion 时间段值
     * @return
     */
    public List<Map<String,Object>> statistiTop10(int hourRegion){
    	if(hourRegion==0){
    		hourRegion = 12;
    	}
    	Calendar calendar = Calendar.getInstance();
    	Date currentDate = new Date();
    	calendar.setTime(currentDate);
    	calendar.add(Calendar.HOUR_OF_DAY, 1-hourRegion);
//    	String d = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH");
//    	d = d + ":00:00";
    	JobInstance instance = new JobInstance();
    	instance.setcStartTime(calendar.getTime());
    	instance.setcEndTime(currentDate);
    	instance.setCurrentUser(UserUtils.getUser());
    	return dao.statistiTop10(instance);
    }
    
    public void updateJobInstanceStatus(Integer execCode,String trackId){
    	dao.updateInstanceStatus(execCode, trackId);
    }
}