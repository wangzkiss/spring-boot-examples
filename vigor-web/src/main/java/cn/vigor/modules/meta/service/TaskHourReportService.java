package cn.vigor.modules.meta.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.modules.meta.dao.TaskHourReportDao;
import cn.vigor.modules.meta.entity.TaskHourReport;
import cn.vigor.modules.tji.dao.JobInstanceDao;
import cn.vigor.modules.tji.entity.JobInstance;

@Service
@Transactional(readOnly = true)
public class TaskHourReportService extends CrudService<TaskHourReportDao, TaskHourReport>  {
	
	@Autowired
	private JobInstanceDao jobInstanceDao;
	
	/**
	 * 根据时间点进行统计
	 * @param hourReport 条件
	 * @return 结果
	 * @throws ParseException 
	 */
	public List<TaskHourReport> statisticTask(TaskHourReport hourReport) throws ParseException{
		int hour = hourReport.getShour();
		if(hour==0){
			hour = 12;//默认取12小时这个时间段
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		String currentDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH");
		Date cStartTime = DateUtils.parseDate(currentDate + ":00:00", "yyyy-MM-dd HH:mm:ss");
		Date cEndTime = calendar.getTime();
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		String cEndDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH");
		calendar.add(Calendar.HOUR_OF_DAY, 1-(hour-1));
		String cBeginDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH");
		hourReport.setcBeginDate(cBeginDate);
		hourReport.setcEndDate(cEndDate);
		List<TaskHourReport> hourReports = dao.statisticTask(hourReport);
		//当前时刻所在的这个小时需要实时统计(查询e_jobinstance表进行统计)
		JobInstance jobInstance = new JobInstance();
		jobInstance.setcStartTime(cStartTime);
		jobInstance.setcEndTime(cEndTime);
		List<Map<String,Object>> list = jobInstanceDao.statisticByTaskType(jobInstance);
		TaskHourReport report = new TaskHourReport();
		for (Map<String, Object> map : list) {
			Integer code = (Integer)map.get("execCode");
			if(code==2){
				report.setSuccessCount(Integer.valueOf(map.get("num").toString()));
			}else if(code==3 || code==4){
				report.setFailCount(report.getFailCount() + Integer.valueOf(map.get("num").toString()));
			}else if(code==5){
				report.setPauseCount(Integer.valueOf(map.get("num").toString()));
			}
		}
		report.setSdate(currentDate);
		report.setShour(currentHour);
		//当前时间点运行中的任务包括这次时间区域的所有运行中的任务,所以开始时间和结束时间要单独处理
		jobInstance.setcStartTime(DateUtils.parseDate(cBeginDate + ":00:00", "yyyy-MM-dd HH:mm:ss"));
		jobInstance.setExecCode(1);
		List<Map<String,Object>> list2 = jobInstanceDao.statisticByTaskType(jobInstance);
		if(list2!=null && list2.size()==1){
			report.setRunningCount(Integer.valueOf(list2.get(0).get("num").toString()));
		}
		hourReports.add(report);
		return hourReports;
	}
	
	/**
	 * 根据类型统计
	 * @param hourReport 条件
	 * @return 结果
	 */
	public Map<String,Integer> statistictaskByType(TaskHourReport hourReport) throws ParseException{
		Map<String,Integer> map = new HashMap<String,Integer>();
		int hour = hourReport.getShour();
		if(hour==0){
			hour = 12;//默认取12小时这个时间段
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String currentDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH");
		Date cStartTime = DateUtils.parseDate(currentDate + ":00:00", "yyyy-MM-dd HH:mm:ss");
		Date cEndTime = calendar.getTime();
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		String cEndDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH");
		calendar.add(Calendar.HOUR_OF_DAY, 1-(hour-1));
		String cBeginDate = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH");
		hourReport.setcBeginDate(cBeginDate);
		hourReport.setcEndDate(cEndDate);
		List<TaskHourReport> hourReports = dao.statistictaskByType(hourReport);
		for (TaskHourReport taskHourReport : hourReports) {
			map.put(String.valueOf(taskHourReport.getTaskType()), taskHourReport.getTimeConsuming());
		}
		//当前时刻所在的这个小时需要实时统计(查询e_jobinstance表进行统计)
		JobInstance jobInstance = new JobInstance();
		jobInstance.setcStartTime(cStartTime);
		jobInstance.setcEndTime(cEndTime);
		List<JobInstance> instances = jobInstanceDao.findList(jobInstance);
		if(instances!=null && instances.size()>0){
			for(JobInstance instance : instances){
				Date respt = instance.getRespTime();
				Date exect = instance.getExecTime();
				Integer timeConsuming = map.get(String.valueOf(instance.getTaskType()));
				if(timeConsuming==null){
					timeConsuming = 0;
				}
				if(respt!=null && exect!=null){
					long tt = respt.getTime()-exect.getTime();
					timeConsuming = timeConsuming + (tt==0?0:(int)tt/1000);
				}
				map.put(String.valueOf(instance.getTaskType()), timeConsuming);
			}
		}
		return map;
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println(DateUtils.parseDate("2017-07-17 19:00:00", "yyyy-MM-dd HH:mm:ss"));
	}
	
}
