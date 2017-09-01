package cn.vigor.modules.tji.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.compute.bean.TaskType;
import cn.vigor.modules.compute.dao.ComputeTaskDao;
import cn.vigor.modules.sys.entity.Dict;
import cn.vigor.modules.sys.utils.DictUtils;
import cn.vigor.modules.tji.dao.JobScheduleDao;
import cn.vigor.modules.tji.entity.JobSchedule;

/**
 * 排班及统计Service
 * @author zhangfeng
 * @version 2016-06-24
 */
@Service
@Transactional(readOnly = true)
public class JobScheduleService extends CrudService<JobScheduleDao, JobSchedule>
{
    @Autowired
    private ComputeTaskDao computeTaskDao;
    
    public List<JobSchedule> findList(JobSchedule jobSchedule)
    {
        return super.findList(jobSchedule);
    }
    
    public Page<JobSchedule> findPage(Page<JobSchedule> page,
            JobSchedule jobSchedule)
    {
        return super.findPage(page, jobSchedule);
    }
    
    /**
     * 改变排班状态
     * @param schId
     * @param pauseFlag
     * @return
     */
    @Transactional(readOnly = false)
    public int updatePauseFlag(int schId, int pauseFlag)
    {
        return dao.updatePauseFlag(schId, pauseFlag);
    }
    
    /**
     * 按时段统计计算任务
     * @param jobSchedule 条件
     * @return 结果
     */
    public Map<String,Map<String,Integer>> getJobStatisByTime(JobSchedule jobSchedule){
    	Map<String, Map<String,Integer>> allMap = new HashMap<String, Map<String,Integer>>();
    	Date cStartTime = jobSchedule.getcStartTime();
    	Date cEndTime = jobSchedule.getcEndTime();
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(cStartTime);
    	int startHour = calendar.get(Calendar.HOUR_OF_DAY);
    	int sday = calendar.get(Calendar.DATE);
    	calendar.setTime(cEndTime);
    	int endHour = calendar.get(Calendar.HOUR_OF_DAY);
    	int eday = calendar.get(Calendar.DATE);
    	if(sday<eday){
    		for(int i=startHour;i<=24;i++){
    			Map<String,Integer> cmap = new HashMap<String,Integer>();
    			cmap.put("1", 0);//正在运行
    			cmap.put("2", 0);//运行成功
    			cmap.put("3", 0);//失败(3提交失败,4运行失败)
    			cmap.put("5", 0);//暂停
    			allMap.put(String.valueOf(i), cmap);
    		}
    		for(int i=1;i<=endHour;i++){
    			Map<String,Integer> cmap = new HashMap<String,Integer>();
    			cmap.put("1", 0);//正在运行
    			cmap.put("2", 0);//运行成功
    			cmap.put("3", 0);//失败(3提交失败,4运行失败)
    			cmap.put("5", 0);//暂停
    			allMap.put(String.valueOf(i), cmap);
    		}
    	}else{
    		for(int i=startHour;i<=endHour;i++){
    			Map<String,Integer> cmap = new HashMap<String,Integer>();
    			cmap.put("1", 0);//正在运行
    			cmap.put("2", 0);//运行成功
    			cmap.put("3", 0);//失败(3提交失败,4运行失败)
    			cmap.put("5", 0);//暂停
        		allMap.put(String.valueOf(i), cmap);
        	}
    	}
		List<JobSchedule> stList = dao.getJobByScheTime(jobSchedule);
		if(stList!=null && stList.size()>0){
			for(JobSchedule js : stList){
				Date st = js.getScheduleTime();
				Integer code = js.getExecCode();
				if(code==0){
					continue;
				}
				calendar.setTime(st);
				int hd = calendar.get(Calendar.HOUR_OF_DAY);
				Map<String,Integer> m = allMap.get(String.valueOf(hd));
				if(code==3 || code==4){
					code = 3;
				}
				Integer value = m.get(String.valueOf(code));
				if(value==null){
					m.put(String.valueOf(code),1);
				}else{
					m.put(String.valueOf(code),value + 1);
				}
				allMap.put(String.valueOf(hd), m);
			}
		}
    	return allMap;
    }
    
    /**
     * 任务统计
     * @param schId
     * @param pauseFlag
     * @return
     */
    public Map<String, Object> getJobStatis(JobSchedule jobSchedule)
    {
        Map<String, Object> allMap = new HashMap<String, Object>();
        List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
        
        List<Map<String, Object>> stList = dao.getJobStatis(jobSchedule);
        //根据vtype属性值来对统计数据进行过滤
        String vtype = jobSchedule.getVtype();
        List<String> values = null;
        if(StringUtils.isNotEmpty(vtype)){
        	List<Dict> list = DictUtils.getDictList("task_type_"+vtype);
        	if(list!=null && list.size()>0){
        		values = new ArrayList<String>();
        		for (Dict dict : list) {
        			values.add(dict.getValue());
				}
        	}
        }
        if(values!=null){
        	List<Map<String, Object>> hstList = new ArrayList<Map<String, Object>>();
        	for(Map<String, Object> mp : stList){
        		if(values.contains(String.valueOf(mp.get("typeId")))){
        			hstList.add(mp);
        		}
        	}
        	stList = hstList;
        }
        
        //通过分组信息，计算所有任务的统计信息
        long allSum = 0;
        long runningSum = 0;
        long successSum = 0;
        long failSum = 0;
        long staySum = 0;
        
        //每个map添加待执行任务数
        for (Map<String, Object> map : stList)
        {
            long ac = (Long) map.get("ac");
            long rc = (Long) map.get("rc");
            long sc = (Long) map.get("sc");
            long fc = (Long) map.get("fc");
            long stayCount = ac - sc - fc - rc;
            
            allSum += ac;
            runningSum += rc;
            successSum += sc;
            failSum += fc;
            staySum += stayCount;
            
            String sr = "";
            
            if (0 != (sc + fc))
            {
                sr = new DecimalFormat("#.##").format((sc * 100) / (sc + fc))
                        + "%";
            }
            
            map.put("stc", stayCount);
            map.put("sr", sr);
            allList.add(map);
        }
        
        allMap.put("allSum", allSum);
        allMap.put("runningSum", runningSum);
        allMap.put("successSum", successSum);
        allMap.put("failSum", failSum);
        allMap.put("staySum", staySum);
        allMap.put("countList", allList);
        
        return allMap;
    }
    
    /**
     * 首页展示
     * @param schId
     * @param pauseFlag
     * @return
     */
    public Map<String, Object> getJobStatisForIndex(JobSchedule jobSchedule)
    {
        Map<String, Object> allMap = new HashMap<String, Object>();
        List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
        
        List<Map<String, Object>> stList = dao.getJobStatis(jobSchedule);
        
        //通过分组信息，计算所有任务的统计信息
        long successSum = 0;
        long failSum = 0;
        long pauseSum = 0;
        long runSum = 0;
        
        //每个map添加待执行任务数
        for (Map<String, Object> map : stList)
        {
            Map<String, Object> newMap = new HashMap<String, Object>();
            
            long sc = (Long) map.get("sc");
            long fc = (Long) map.get("fc");
            long pc = (Long) map.get("pc");
            long rc = (Long) map.get("rc");
            successSum += sc;
            failSum += fc;
            pauseSum += pc;
            runSum += rc;
            
            newMap.put("failCount", fc);
            newMap.put("successCount", sc);
            newMap.put("pasuseCount", pc);
            newMap.put("typeName", map.get("typeName"));
            allList.add(newMap);
        }
        
        allMap.put("allSuccessSum", successSum);
        allMap.put("allFailSum", failSum);
        allMap.put("allPauseSum", pauseSum);
        allMap.put("allRunningSum", runSum);
        allMap.put("totalSum", successSum+failSum+pauseSum+runSum);//任务总数未统计未运行的任务
        allMap.put("typeReport", allList);
        return allMap;
    }
    
    /**
     * 報表
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, Object> getJobReport(String date, int flag,String createUser,String vtype)
    {
        Map<String, Object> allMap = new HashMap<String, Object>();
        
        List<Map<String, Object>> rpList = 1 == flag ? dao.getDayReport(date, createUser)
                : dao.getMonthReport(date, createUser);
        
        List<String> values = null;
        if(StringUtils.isNotEmpty(vtype)){
        	List<Dict> list = DictUtils.getDictList(vtype);
        	if(list!=null && list.size()>0){
        		values = new ArrayList<String>();
        		for (Dict dict : list) {
        			values.add(dict.getValue());
				}
        	}
        }
        if(values!=null){
        	List<Map<String, Object>> hstList = new ArrayList<Map<String, Object>>();
        	for(Map<String, Object> mp : rpList){
        		if(values.contains(String.valueOf(mp.get("taskType")))){
        			hstList.add(mp);
        		}
        	}
        	rpList = hstList;
        }
        
        long successSum = 0;
        long failSum = 0;
        
        //计算所有成功失败数
        for (Map<String, Object> map : rpList)
        {
            long sc = 1 == flag ? (Integer) map.get("successCount")
                    : (Long) map.get("successCount");
            long fc = 1 == flag ? (Integer) map.get("failCount")
                    : (Long) map.get("failCount");
            
            successSum += sc;
            failSum += fc;
        }
        if(rpList==null || rpList.size()==0){
            rpList = getDefaultReport(rpList,values);
        }
        allMap.put("allSuccessSum", successSum);
        allMap.put("allFailSum", failSum);
        allMap.put("typeReport", rpList);
        
        return allMap;
    }
    
    private List<Map<String,Object>> getDefaultReport(List<Map<String,Object>> list,List<String> values){
        if(list==null){
            list = new ArrayList<Map<String,Object>>();
        }
        List<TaskType> taskTypes = computeTaskDao.getTaskTypes();
        for(TaskType taskType : taskTypes){
        	String id = String.valueOf(taskType.getId());
        	if(values.contains(id)){
        		Map<String,Object> map = new HashMap<String,Object>();
                map.put("typeName", taskType.getName());
                map.put("allCount", 0);
                map.put("runningCount", 0);
                map.put("successCount", 0);
                map.put("failCount", 0);
                map.put("avgTime", 0);
                list.add(map);
        	}
        }
        return list;
    }
}