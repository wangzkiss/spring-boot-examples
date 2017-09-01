package cn.vigor.modules.tji.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.config.Global;
import cn.vigor.common.contants.Contants;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.HttpUtil;
import cn.vigor.common.utils.SearchIndex;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.jars.service.FunctionService;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.entity.MetaStorePro;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.server.entity.Platform;
import cn.vigor.modules.server.service.PlatformService;
import cn.vigor.modules.tji.entity.Job;
import cn.vigor.modules.tji.entity.JobDetail;
import cn.vigor.modules.tji.entity.JobInstance;
import cn.vigor.modules.tji.entity.TaskFunction;
import cn.vigor.modules.tji.service.JobInstanceService;
import cn.vigor.modules.tji.service.JobService;
import cn.vigor.modules.tji.service.TasksService;
import cn.vigor.modules.tji.util.KylinHttpRequest;

/**
 * 作业实例Controller
 * 
 * @author zhangfeng
 * @version 2016-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/tji/jobinstance")
public class JobInstanceController extends BaseController
{
    /**
     * 实例
     */
    @Autowired
    private JobInstanceService jobInstanceService;
    
    /**
     * 作业
     */
    @Autowired
    private JobService jobService;
    
    /**
     * 平台
     */
    @Autowired
    private PlatformService platformService;
    
    /**
     * 平台存储
     */
    @Autowired
    private MetaStoreService metaStoreService;
    
    @Autowired
    private FunctionService functionService;
    
    @Autowired
    private TasksService tasksService;
    
    @ModelAttribute
    public JobInstance get(@RequestParam(required = false) String id)
    {
        JobInstance entity = null;
        if (StringUtils.isNotBlank(id))
        {
            entity = jobInstanceService.get(id);
        }
        if (entity == null)
        {
            entity = new JobInstance();
        }
        return entity;
    }
    
    /**
     * 作业实例列表页面
     */
    @RequiresPermissions("tji:jobinstance:list")
    @RequestMapping(value = { "list", "" })
    public String list(JobInstance jobInstance, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        // 默认查当天的数据
        if (1 == jobInstance.getIsFirst())
        {
            jobInstance.setcStartTime(DateUtils.getDayStart());
            jobInstance.setcEndTime(DateUtils.getDayEnd());
        }
        else
        {
            String tnname = jobInstance.getTableName();
            Date cStartTime = jobInstance.getcStartTime();
            Calendar calendar = Calendar.getInstance();
            if (cStartTime != null)
            {
                calendar.setTime(cStartTime);
                int cStartYear = calendar.get(Calendar.YEAR);
                int cStartMonth = calendar.get(Calendar.MONTH) + 1;
                // 如果开始查询日期为上个月,则需要设置tableName
                calendar.setTime(new Date());
                int curMonth = calendar.get(Calendar.MONTH) + 1;
                if (cStartMonth < curMonth)
                {
                    jobInstance
                            .setTableName(tnname + cStartYear + (cStartMonth > 9
                                    ? cStartMonth : "0" + cStartMonth));
                    jobInstance.setSecTableName(tnname);
                }
            }
        }
        
        // 默认按创建时间倒序
        Page<JobInstance> paging = new Page<JobInstance>(request, response);
        
        if (StringUtils.isBlank(paging.getOrderBy()))
        {
            paging.setOrderBy("trackId desc");
        }
        
        Page<JobInstance> page = jobInstanceService.findPage(paging,
                jobInstance);
        model.addAttribute("page", page);
        model.addAttribute("enable", jobInstance.getEnable());
        return "modules/tji/jobInstanceList";
    }
    
    /**
     * 作业实例列表页面，增加源表及目标表信息
     */
    @RequiresPermissions("tji:jobinstance:listmore")
    @RequestMapping("list-more")
    public String listMore(JobInstance jobInstance, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        // 默认查当天的数据
        if (1 == jobInstance.getIsFirst())
        {
            jobInstance.setcStartTime(DateUtils.getDayStart());
            jobInstance.setcEndTime(DateUtils.getDayEnd());
            jobInstance.setTableName(jobInstance.getTableName()
                    + DateUtils.getYear() + DateUtils.getMonth());
        }
        else
        {
            String tnname = jobInstance.getTableName();
            Date cStartTime = jobInstance.getcStartTime();
            Calendar calendar = Calendar.getInstance();
            if (cStartTime != null)
            {
                calendar.setTime(cStartTime);
                int cStartYear = calendar.get(Calendar.YEAR);
                int cStartMonth = calendar.get(Calendar.MONTH) + 1;
                // 如果开始查询日期为上个月,则需要设置tableName
                calendar.setTime(new Date());
                int curMonth = calendar.get(Calendar.MONTH) + 1;
                if (cStartMonth < curMonth)
                {
                    jobInstance
                            .setTableName(tnname + cStartYear + (cStartMonth > 9
                                    ? cStartMonth : "0" + cStartMonth));
                    jobInstance.setSecTableName(tnname);
                }
            }
        }
        
        // 默认按创建时间倒序
        Page<JobInstance> paging = new Page<JobInstance>(request, response);
        
        if (StringUtils.isBlank(paging.getOrderBy()))
        {
            paging.setOrderBy("trackId desc");
        }
        
        Page<JobInstance> page = jobInstanceService.findPage(paging,
                jobInstance);
        
        // 增加输入输出信息
        List<JobInstance> jiList = page.getList();
        List<JobInstance> newJiList = new ArrayList<JobInstance>();
        
        for (JobInstance ji : jiList)
        {
            JobDetail jobDetail = jobService.getJobDetail(ji.getJobId());
            ji.setInputMeta(listToString(jobDetail.getInputMetaList()));
            ji.setOutputMeta(listToString(jobDetail.getOutputMetaList()));
            newJiList.add(ji);
        }
        
        page.setList(newJiList);
        model.addAttribute("page", page);
        return "modules/tji/jobInstanceListM";
    }
    
    /**
     * 子任务列表，用于工作流
     */
    @RequiresPermissions("tji:jobinstance:listforact")
    @RequestMapping(value = "list-for-act")
    public String listForAct(
            @RequestParam(value = "trackId", required = true) String trackId,
            @RequestParam(value = "execTime", required = true) String execTime,
            Model model)
    {
        model.addAttribute("jiList",
                jobInstanceService.findListForAct(trackId, execTime));
        model.addAttribute("actInfo",
                jobInstanceService.get(trackId, execTime));
        
        return "modules/tji/jiListForAct";
    }
    
    /**
     * 作业实例日志
     */
    @RequiresPermissions("tji:jobinstance:log")
    @RequestMapping(value = "log")
    public @ResponseBody String log(Integer trackId)
    {
        Platform platform = platformService
                .findUniqueByProperty("platform_name", "elasticsearch");
        SearchIndex search = new SearchIndex(platform.getPlatformIp(),
                platform.getPlatformPort());
        
        return search.getJobLogs(String.valueOf(trackId));
    }
    
    /**
     * 作业实例详情
     */
    @RequiresPermissions("tji:jobinstance:view")
    @RequestMapping(value = "view")
    public String view(
            @RequestParam(value = "trackId", required = true) String trackId,
            @RequestParam(value = "execTime", required = true) String execTime,
            Model model)
    {
        model.addAttribute("jobInstance",
                jobInstanceService.getDetail(trackId, execTime));
        return "modules/tji/jobInstanceDetail";
    }
    
    /**
     * 弹出重跑框
     */
    @RequiresPermissions("tji:jobinstance:showagain")
    @RequestMapping(value = "showagain")
    public String showAgain(@RequestParam(required = false) Integer taskType,
            @RequestParam(required = false) Integer execCode, Model model)
    {
        model.addAttribute("taskType", taskType);
        model.addAttribute("execCode", execCode);
        return "modules/tji/batchAgainTime";
    }
    
    /**
     * 作业实例重跑
     */
    @RequiresPermissions("tji:jobinstance:again")
    @RequestMapping(value = "again")
    public @ResponseBody String again(String ids, Date startTime,
            String execTime, @RequestParam(required = false) Integer againType)
    {
        String msg = "";
        
        // 审核任务
        String jobNames = againJob(ids.split(","),
                startTime,
                execTime.split(","),
                null == againType ? 1 : againType);
        
        // 有失败
        if (StringUtils.isNotEmpty(jobNames))
        {
            msg = jobNames + "重跑失败！";
        }
        else
        {
            msg = "重跑成功!";
        }
        
        return msg;
    }
    
    /**
     * 重跑实例
     * 
     * @param jobId
     * @return
     */
    private String againJob(String[] idArr, Date startTime,
            String[] scheduleTime, int againType)
    {
        // 审核失败任务
        StringBuffer jobNames = new StringBuffer();
        
        if (null != idArr && idArr.length > 0)
        {
            // 批量循环设置
            for (int i = 0; i < idArr.length; i++)
            {
                // 根据trackId获取作业新
                JobInstance jobInstance = jobInstanceService.get(idArr[i],
                        scheduleTime[i]);
                
                // 获取作业信息，重新生成一个新作业
                Job job = jobService
                        .get(String.valueOf(jobInstance.getJobId()));
                job.setJobId(null);
                job.setJobName(job.getJobName() + System.currentTimeMillis());
                job.setStartTime(null == startTime ? new Date() : startTime);
                job.setJobStatus(2);
                job.setScheduleRule(null);
                job.setPauseFlag(0);
                job.setEndTime(null);
                job.setExecType("一次");
                job.setAgainFlag(1);
                job.setIsNewRecord(true);
                
                jobService.save(job);
                
                Map<String, String> reqData = new HashMap<String, String>();
                
                // 获取任务作业详情,不包括输入输出
                JobDetail jobDetail = jobService.getJobDetail(job.getJobId());
                // jobDetail.setDateParams(jobInstance.getDateParams());
                
                //重跑重新封装inputsql与deletesql
                setJobDetail(jobDetail, jobInstance, againType);
                
                net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject
                        .fromObject(JSONObject.toJSONString(jobDetail));
                jsonObj.put("dateParams", jobInstance.getDateParams());
                
                // 不需要传输的信息
                jsonObj.remove("currentUser");
                jsonObj.remove("loginUser");
                jsonObj.remove("page");
                jsonObj.remove("createBy");
                jsonObj.remove("updateBy");
                jsonObj.remove("global");
                
                // 任务详情json串，包括输入输出
                reqData.put("jobInfo", jsonObj.toString());
                
                // 发送请求给调度
                if (HttpUtil.sendSchedPost(jobService.getSchUrl() + "/job-call",
                        reqData))
                {
                    // 打印日志
                    logger.info(jobDetail.getJobName() + "作业审核成功！");
                }
                else
                {
                    jobService.delete(
                            jobService.get(String.valueOf(job.getJobId())));
                    logger.info(jobDetail.getJobName() + "作业审核失败！");
                    jobNames.append(jobDetail.getJobName() + ",");
                }
            }
        }
        
        return jobNames.toString();
    }
    
    /**
     * list转string
     * 
     * @param taskId
     * @return
     * @throws Exception
     */
    private String listToString(List<Map<String, Object>> list)
    {
        StringBuffer sb = new StringBuffer();
        
        if (null != list && !list.isEmpty())
        {
            int i = 0;
            for (Map<String, Object> map : list)
            {
                sb.append(map.get("name"));
                if (i != list.size() - 1)
                {
                    sb.append("|");
                }
                i++;
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 获取kylin任务日志详情信息
     * 
     * @param jobId
     *            任务id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "getKylinJobLogs")
    @ResponseBody
    public JSONObject getKylinJobLogs(
            @RequestParam(value = "jobId", required = true) String jobId)
            throws IOException
    {
        JSONObject jsonObject = new JSONObject();
        String baseUrl = Global.getConfig("kylin_base_api_url") + "jobs/"
                + jobId;
        String result = KylinHttpRequest.httpGet(baseUrl, "GET");
        if (result.contains("{"))
        {
            org.apache.kylin.job.JobInstance instance = JSONObject.parseObject(
                    result, org.apache.kylin.job.JobInstance.class);
            jsonObject.put("kylinJobInfo", instance);
            jsonObject.put("code", 0);
        }
        else
        {
            jsonObject.put("msg", result);
            jsonObject.put("code", 1);
        }
        return jsonObject;
    }
    
    /**
     * 主页耗时top10
     * 
     * @param hourRegion
     * @return
     */
    @RequestMapping("statistiTop10")
    @ResponseBody
    public JSONObject statistiTop10(
            @RequestParam(value = "hourRegion", required = true, defaultValue = "12") Integer hourRegion)
    {
        JSONObject jsonObject = new JSONObject();
        List<Map<String, Object>> list = jobInstanceService
                .statistiTop10(hourRegion);
        jsonObject.put("top10", list);
        return jsonObject;
    }
    
    /**
     * 停止任务实例
     * @param trackId 实例主键id
     * @return
     */
    @RequestMapping("stopJob")
    @ResponseBody
    public String stopJob(@RequestParam(value="trackId",required=true)String trackId){
    	JobInstance jobDetail = jobInstanceService.get(trackId);
    	if(jobDetail==null){
    		return "停止成功";
    	}
    	Integer taskType = jobDetail.getTaskType();
    	Job job = jobService.get(String.valueOf(jobDetail.getJobId()));
    	if(job==null){
    		return "数据异常,停止失败";
    	}
    	TaskFunction taskFunction = tasksService.findETFunctionByTaskId(job.getTaskId());
    	if(taskFunction==null){
    		return "数据异常,停止失败";
    	}
    	String val = "";
    	if(taskType==1 || taskType ==2){
    		val = "etlagent";
    	}else if(taskType==13){
    		val = "streamingagent";
    	}else{
    		val = "hadoop_hive";
    	}
    	Platform platform = platformService.findUniqueByProperty("platform_name", val);
    	String jobProgressInfo = jobDetail.getJobProgressInfo();
    	if(jobProgressInfo!=null){
    		Map<String,String> map = JSONObject.parseObject(jobProgressInfo, Map.class);
    		map.put("reqMsgType", "CancelEtlTaskRequest");
    		map.put("etlModeName", functionService.get(taskFunction.getFunctionId() + "").getFunctionName());
    		boolean bol = HttpUtil.sendSchedPost(platform.getPlatformUrl(),map);
    		if (bol)
            {
    			//更新e_job_instance表状态信息(将状态更改为停止)
        		jobInstanceService.updateJobInstanceStatus(6, trackId);
	            // 打印日志
	            logger.info(jobDetail.getJobName() + "作业停止成功！");
	            return jobDetail.getJobName() + "作业停止成功";
            }else{
            	return jobDetail.getJobName() + "作业停止失败";
            }
    	}
    	return null;
    }
    
    /**
     * 重新设置任务详情
     * @param jobDetail
     */
    private void setJobDetail(JobDetail jobDetail, JobInstance jobInstance,
            int againType)
    {
        //etl任务设置inputsql与deletesql
        if (Contants.TASK_TYPE_ETL_TMP == jobDetail.getTaskType())
        {
            JSONObject functionParam = JSONObject.parseObject((String) jobDetail
                    .getFunctionList().get(0).get("functionParam"));
            String inputSql = functionParam.getString("input_sql");
            functionParam.put("againType", againType + "");
            
            //完全重跑，拼装delete_sql
            if (1 == againType)
            {
                
                functionParam.put("delete_sql", tranDeleteSql(functionParam));
                functionParam.put("fileAppended", "1");
                jobDetail.getFunctionList().get(0).put("functionParam",
                        functionParam.toJSONString());
            }
            //断点续跑,拼装input_sql
            else
            {
                long startNum = null == jobInstance.getOutputNum() ? 0
                        : jobInstance.getOutputNum();
                
                //非表输入
                if (StringUtils.isEmpty(inputSql))
                {
                    functionParam.put("ignoreRows", startNum);
                }
                else
                {
                    functionParam.put("input_sql",
                            0 == startNum ? inputSql
                                    : tranInputSql(functionParam, startNum));
                }
                
                functionParam.put("fileAppended", "0");
                jobDetail.getFunctionList().get(0).put("functionParam",
                        functionParam.toJSONString());
            }
        }
    }
    
    /**
     * 转换sql
     * @param functionParam
     * @return
     */
    private String tranInputSql(JSONObject functionParam, long startNum)
    {
        String inputSql = functionParam.getString("input_sql");
        
        //mysql
        if (StringUtils
                .isNotEmpty(functionParam.getString("input_source_name_mysql")))
        {
            inputSql = inputSql + " limit " + startNum + ",9999999999999999999";
        }
        //oracle
        else if (StringUtils.isNotEmpty(
                functionParam.getString("input_source_name_oracle")))
        {
            String preSql = inputSql.substring(0, inputSql.indexOf(" from"));
            String endSql = inputSql.substring(inputSql.indexOf(" from"));
            inputSql = "select a1.* from (" + preSql + ",rownum as rn " + endSql
                    + ") a1 where rn >" + startNum;
        }
        //sqlserver  or  hive
        else if (StringUtils.isNotEmpty(
                functionParam.getString("input_source_name_sqlserver"))
                || StringUtils.isNotEmpty(
                        functionParam.getString("input_source_name_hive")))
        {
            String preSql = inputSql.substring(0, inputSql.indexOf(" from"));
            String endSql = inputSql.substring(inputSql.indexOf(" from"));
            inputSql = "select a1.* from (" + preSql
                    + ",row_number() over() as rn " + endSql + ") a1 where rn >"
                    + startNum;
        }
        
        return inputSql;
    }
    
    /**
     * 生成
     * @param functionParam
     * @return
     */
    private String tranDeleteSql(JSONObject functionParam)
    {
        String deleteSql = "";
        
        //hive
        if (StringUtils
                .isNotEmpty(functionParam.getString("output_store_name_hive")))
        {
            MetaStore ms = metaStoreService.findUniqueByProperty("store_name",
                    functionParam.getString("output_store_name_hive"));
            
            List<MetaStorePro> proList = ms.getMetaStoreProList();
            
            String partition = "";
            for (MetaStorePro pro : proList)
            {
                if (pro.getType() == 1)
                {
                    if (pro.getProType().equalsIgnoreCase("int")
                            || pro.getProType().equalsIgnoreCase("integer"))
                    {
                        partition += pro.getProName() + "="
                                + pro.getDataFormat() + ",";
                    }
                    else
                    {
                        partition += pro.getProName() + "='"
                                + pro.getDataFormat() + "',";
                    }
                    
                }
            }
            
            // 有分区的情况
            if (StringUtils.isNotEmpty(partition))
            {
                deleteSql = " alter table " + ms.getStoreFile()
                        + " drop if exists partition ("
                        + partition.substring(0, partition.lastIndexOf(","))
                        + ")";
            }
            // 无分区的
            else
            {
                deleteSql = "truncate table  " + ms.getStoreFile();
            }
        }
        // 其他关系型数据库 删除语句
        else
        {
            String inputSql = functionParam.getString("input_sql");
            String inputCondition = inputSql.contains("where")
                    ? inputSql.substring(inputSql.lastIndexOf("where") + 5)
                    : "";
            
            deleteSql = " delete from "
                    + functionParam.getString("output_table_file_name")
                    + (StringUtils.isEmpty(inputCondition) ? ""
                            : " where " + inputCondition);
        }
        
        return deleteSql;
    }
}