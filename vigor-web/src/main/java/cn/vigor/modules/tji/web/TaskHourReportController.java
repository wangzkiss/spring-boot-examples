package cn.vigor.modules.tji.web;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.TaskHourReport;
import cn.vigor.modules.meta.service.TaskHourReportService;

@RequestMapping("${adminPath}/trc/taskHourReport")
@Controller
public class TaskHourReportController extends BaseController{
	
	@Autowired
	private TaskHourReportService taskHourReportService;
	
	/**
	 * 
	 * @param hourReport
	 * @return
	 */
	@RequestMapping("statistic")
	@ResponseBody
	public JSONObject statisticTask(TaskHourReport hourReport){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			List<TaskHourReport> list = taskHourReportService.statisticTask(hourReport);
			jsonObject.put("reportPerHourData", list);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 
	 * @param hourReport
	 * @return
	 */
	@RequestMapping("statisticByType")
	@ResponseBody
	public JSONObject statistictaskByType(TaskHourReport hourReport){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			Map<String,Integer> map = taskHourReportService.statistictaskByType(hourReport);
			jsonObject.put("timeConsumingData", map);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
}
