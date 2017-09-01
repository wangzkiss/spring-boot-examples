package cn.vigor.modules.meta.dao;

import java.util.List;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.TaskHourReport;

@MyBatisDao
public interface TaskHourReportDao extends CrudDao<TaskHourReport> {
	
	/**
	 * 根据时间点进行统计
	 * @param hourReport 条件
	 * @return 结果
	 */
	public List<TaskHourReport> statisticTask(TaskHourReport hourReport);
	
	/**
	 * 根据类型统计
	 * @param hourReport 条件
	 * @return 结果
	 */
	public List<TaskHourReport> statistictaskByType(TaskHourReport hourReport);
}
