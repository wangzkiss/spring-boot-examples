package cn.vigor.modules.tji.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.config.Global;
import cn.vigor.common.contants.Contants;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.HttpUtil;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.TaskHourReport;
import cn.vigor.modules.meta.service.TaskHourReportService;
import cn.vigor.modules.meta.util.DBUtils;
import cn.vigor.modules.sys.entity.Dict;
import cn.vigor.modules.sys.utils.DictUtils;
import cn.vigor.modules.tji.entity.Job;
import cn.vigor.modules.tji.entity.JobDetail;
import cn.vigor.modules.tji.entity.JobInstance;
import cn.vigor.modules.tji.entity.Task;
import cn.vigor.modules.tji.service.JobInstanceService;
import cn.vigor.modules.tji.service.JobService;
import cn.vigor.modules.tji.service.TasksService;
import cn.vigor.modules.tji.util.KylinHttpRequest;

/**
 * 作业相关Controller
 * @author zhangfeng
 * @version 2016-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/tji/job")
public class JobController extends BaseController
{
    /**
     * 作业
     */
    @Autowired
    private JobService jobService;
    
    /**
     * 任务
     */
    @Autowired
    private TasksService taskService;
    
    /**
     * 返回提示
     */
    private String msg;
    
    @Autowired
    private JobInstanceService jobInstanceService;
    
    @Autowired
    private TaskHourReportService taskHourReportService;
    
    /**
     * 点设置调度规则按钮,弹出框
     */
    @RequiresPermissions("tji:job:get")
    @RequestMapping(value = "get")
    public String get(Job job, Model model)
    {
        model.addAttribute("job", job);
        model.addAttribute("mails", jobService.getUserForEmail());
        return "modules/tji/jobForm";
    }
    
    /**
     * 点击规则，查询
     */
    @RequiresPermissions("tji:job:get")
    @RequestMapping(value = "get-for-single")
    public @ResponseBody String getForSingle(int jobId)
    {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> jobMap = new HashMap<String, Object>();
        
        Job job = jobService.get(String.valueOf(jobId));
        List<Map<String, Object>> mails = jobService.getUserForEmail();
        jobMap.put("notifyEmail", job.getNotifyEmail());
        jobMap.put("scheduleLevel", job.getScheduleLevel());
        jobMap.put("scheduleRule", job.getScheduleRule());
        jobMap.put("pauseFlag", job.getPauseFlag());
        jobMap.put("startTime", job.getStartTime());
        
        resultMap.put("job", jobMap);
        resultMap.put("mails", mails);
        
        net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject
                .fromObject(JSONObject.toJSONString(resultMap));
        
        return jsonObj.toString();
    }
    
    /**
     * 作业列表页面
     */
    @RequiresPermissions("tji:job:list")
    @RequestMapping(value = { "list", "" })
    public String list(Job job, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        //转换作业状态
        int opFlag = job.getOpFlag();
        
        if (opFlag > 0)
        {
            //审核状态
            if (1 == opFlag || 2 == opFlag)
            {
                job.setJobStatus(opFlag);
                job.setExecStatus(-1);
            }
            //执行状态
            else
            {
                job.setJobStatus(-1);
                job.setExecStatus(opFlag - 2);
            }
        }
        else
        {
            job.setJobStatus(-1);
            job.setExecStatus(-1);
        }
        
        //默认按创建时间倒序
        Page<Job> paging = new Page<Job>(request, response);
        
        if (StringUtils.isBlank(paging.getOrderBy()))
        {
            paging.setOrderBy("createTime desc");
        }
        
        Page<Job> page = jobService.findPage(paging, job);
        model.addAttribute("page", page);
        return "modules/tji/jobList";
    }
    
    /**
     * 作业查看
     */
    @RequiresPermissions("tji:job:view")
    @RequestMapping(value = "view")
    public String view(int jobId, Model model)
    {
        model.addAttribute("jobDetail", jobService.getJobDetail(jobId));
        return "modules/tji/jobDetail";
    }
    
    /**
     * 设置调度规则，保存修改作业
     */
    @RequiresPermissions("tji:job:add")
    @RequestMapping(value = "save")
    public @ResponseBody String save(Job job, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        Map<String, Object> resultMsg = new LinkedHashMap<String, Object>();
        int successNum = 0;
        int failureNum = 0;
        Map<String, String> failureMsg = new LinkedHashMap<String, String>();
        List<Integer> jobIds = new ArrayList<Integer>();
        String taskName = "";
        
        //批量设置任务参数格式为，taskId,taskId
        String[] taskIdArr = job.getTaskIds().split(",");
        
        if (null != taskIdArr && taskIdArr.length > 0)
        {
            //批量循环设置
            for (String taskIdStr : taskIdArr)
            {
                //查询任务信息
                Task task = taskService.get(taskIdStr);
                
                int taskId = Integer.valueOf(taskIdStr);
                
                try
                {
                    //生成作业名
                    taskName = task.getTaskName();
                    job.setJobName(taskName.replaceFirst("TASK", "JOB") + "_v"
                            + (jobService.getJobCountTaskId(task.getTaskId())
                                    + 1));
                    
                    //解析执行频率
                    job.setExecType(getExecType(job.getScheduleRule()));
                    
                    job.setTaskId(taskId);
                    job.setTaskType(task.getTaskType());
                    job.setGroupId(task.getGroupId());
                    job.setAgainFlag(0);
                    job.setJobStatus(1);
                    job.setCreateUser(task.getCreateUser());
                    
                    //新增
                    if (1 == job.getOpFlag())
                    {
                        //保存新增
                        job.setIsNewRecord(true);
                        jobService.save(job);
                        
                        successNum++;
                        jobIds.add(job.getJobId());
                    }
                    //修改
                    else
                    {
                        //从数据库取出记录
                        List<Job> oldList = jobService.getJobByTaskId(taskId);
                        
                        if (null != oldList && !oldList.isEmpty())
                        {
                            //有多个规则，则不支持批量修改
                            if (oldList.size() > 1)
                            {
                                failureMsg.put(taskName, "任务存在多个作业，不支持批量修改");
                                failureNum++;
                            }
                            else
                            {
                                Job old = oldList.get(0);
                                
                                //如果作业未审核，可以修改
                                if (1 == old.getJobStatus())
                                {
                                    //为了save方法指向udpate
                                    job.setId("1");
                                    
                                    job.setJobId(old.getJobId());
                                    job.setJobStatus(1);
                                    jobService.save(job);
                                    successNum++;
                                    jobIds.add(job.getJobId());
                                }
                                else
                                {
                                    failureMsg.put(taskName, "作业已审核，不支持修改");
                                    failureNum++;
                                }
                            }
                        }
                        //没有作业则新增
                        else
                        {
                            //保存新增
                            job.setIsNewRecord(true);
                            jobService.save(job);
                            successNum++;
                            jobIds.add(job.getJobId());
                        }
                    }
                }
                catch (Exception e)
                {
                    failureMsg.put(taskName, "系统异常");
                    failureNum++;
                    logger.error("save===", e);
                }
            }
        }
        
        resultMsg.put("failureNum", failureNum);
        resultMsg.put("failureMsg", failureMsg);
        resultMsg.put("successNum", successNum);
        resultMsg.put("jobIds", jobIds);
        
        return JSONObject.toJSONString(resultMsg);
    }
    
    /**
     * 设置调度规则，用于按组设置
     */
    @RequiresPermissions("tji:job:add-for-group")
    @RequestMapping(value = "save-for-group")
    public @ResponseBody String saveForGroup(Job job, int groupId, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        Map<String, Object> resultMsg = new LinkedHashMap<String, Object>();
        int successNum = 0;
        int failureNum = 0;
        Map<String, String> failureMsg = new LinkedHashMap<String, String>();
        List<Integer> jobIds = new ArrayList<Integer>();
        String taskName = "";
        
        //获取分组的所有任务
        List<Task> taskList = taskService.getTaskByGroupId(groupId);
        
        if (null != taskList && !taskList.isEmpty())
        {
            //批量循环设置
            for (Task task : taskList)
            {
                try
                {
                    //生成作业名
                    taskName = task.getTaskName();
                    job.setJobName(taskName.replaceFirst("TASK", "JOB") + "_v"
                            + (jobService.getJobCountTaskId(task.getTaskId())
                                    + 1));
                    
                    //解析执行频率
                    job.setExecType(getExecType(job.getScheduleRule()));
                    
                    job.setTaskId(task.getTaskId());
                    job.setTaskType(task.getTaskType());
                    job.setGroupId(task.getGroupId());
                    job.setAgainFlag(0);
                    job.setJobStatus(1);
                    job.setCreateUser(task.getCreateUser());
                    
                    //新增
                    if (1 == job.getOpFlag())
                    {
                        //保存新增
                        job.setIsNewRecord(true);
                        jobService.save(job);
                        
                        successNum++;
                        jobIds.add(job.getJobId());
                    }
                    //修改
                    else
                    {
                        //从数据库取出记录
                        List<Job> oldList = jobService
                                .getJobByTaskId(task.getTaskId());
                        
                        if (null != oldList && !oldList.isEmpty())
                        {
                            //有多个规则，则不支持批量修改
                            if (oldList.size() > 1)
                            {
                                failureMsg.put(taskName, "任务存在多个作业，不支持批量修改");
                                failureNum++;
                            }
                            else
                            {
                                Job old = oldList.get(0);
                                
                                //如果作业未审核，可以修改
                                if (1 == old.getJobStatus())
                                {
                                    //为了save方法指向udpate
                                    job.setId("1");
                                    job.setJobId(old.getJobId());
                                    job.setJobStatus(1);
                                    jobService.save(job);
                                    
                                    successNum++;
                                    jobIds.add(job.getJobId());
                                }
                                else
                                {
                                    failureMsg.put(taskName, "作业已审核，不支持修改");
                                    failureNum++;
                                }
                            }
                        }
                        //没有作业则新增
                        else
                        {
                            //保存新增
                            job.setIsNewRecord(true);
                            jobService.save(job);
                            
                            successNum++;
                            jobIds.add(job.getJobId());
                        }
                    }
                }
                catch (Exception e)
                {
                    failureMsg.put(taskName, "系统异常");
                    failureNum++;
                    logger.error("save===", e);
                }
            }
        }
        
        resultMsg.put("failureNum", failureNum);
        resultMsg.put("failureMsg", failureMsg);
        resultMsg.put("successNum", successNum);
        resultMsg.put("jobIds", jobIds);
        
        return JSONObject.toJSONString(resultMsg);
    }
    
    /**
     * 设置调度规则，保存修改作业
     */
    @RequiresPermissions("tji:job:add")
    @RequestMapping(value = "save-single")
    public @ResponseBody String saveSingle(Job job, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        String resultMsg = "设置成功！";
        
        try
        {
            Job old = jobService.get(String.valueOf(job.getJobId()));
            
            //如果作业未审核，可以修改
            if (1 == old.getJobStatus())
            {
                //为了save方法指向udpate
                job.setId("1");
                job.setJobStatus(1);
                job.setGroupId(old.getGroupId());
                job.setExecType(getExecType(job.getScheduleRule()));
                job.setJobName(old.getJobName());
                
                jobService.save(job);
            }
            else
            {
                resultMsg = "设置失败，作业已审核通过不能修改！";
            }
        }
        catch (Exception e)
        {
            resultMsg = "设置失败，系统异常！";
            logger.error("saveSingle===", e);
        }
        
        return job.getJobId() + ";" + resultMsg;
    }
    
    /**
     * 审核作业，支持批量
     */
    @RequiresPermissions("tji:job:audit")
    @RequestMapping(value = "audit")
    public @ResponseBody String audit(String jobIds,
            @RequestParam(required = false) Integer groupId)
    {
        Map<String, Object> resultMsg = new LinkedHashMap<String, Object>();
        int successNum = 0;
        int failureNum = 0;
        Map<String, String> failureMsg = new LinkedHashMap<String, String>();
        
        //审核分组
        if (null != groupId && groupId != 0)
        {
            List<Integer> ids = jobService.getJobsByGroupId(groupId);
            
            if (null != ids && !ids.isEmpty())
            {
                for (int jobId : ids)
                {
                    //发送给调度
                    Map<String, String> jobMap = auditJob(
                            Integer.valueOf(jobId));
                    
                    //有失败的任务
                    if (!jobMap.isEmpty())
                    {
                        failureMsg.put(jobMap.get("jobName"),
                                jobMap.get("failDesc"));
                        failureNum++;
                    }
                    else
                    {
                        successNum++;
                    }
                }
                
            }
            else
            {
                failureMsg.put("", "该分组下没有未审核的作业");
            }
            
        }
        //作业界面审核
        else
        {
            String idArray[] = jobIds.split(",");
            
            //批量审核
            if (null != idArray && idArray.length > 0)
            {
                for (String id : idArray)
                {
                    //发送给调度
                    Map<String, String> jobMap = auditJob(Integer.valueOf(id));
                    
                    //有失败的任务
                    if (!jobMap.isEmpty())
                    {
                        failureMsg.put(jobMap.get("jobName"),
                                jobMap.get("failDesc"));
                        failureNum++;
                    }
                    else
                    {
                        successNum++;
                    }
                }
            }
        }
        
        resultMsg.put("failureNum", failureNum);
        resultMsg.put("failureMsg", failureMsg);
        resultMsg.put("successNum", successNum);
        
        return JSONObject.toJSONString(resultMsg);
    }
    
    /**
     * 更改作业执行状态
     */
    @RequiresPermissions("tji:job:execstatus")
    @RequestMapping(value = "exec-status")
    public @ResponseBody String execStatus(int jobId, int opFlag)
    {
        boolean flag = false;
        
        //获取任务
        Job job = jobService.get(String.valueOf(jobId));
        job.setOpFlag(opFlag);
        
        //发送参数
        Map<String, String> reqData = new HashMap<String, String>();
        reqData.put("jobInfo", JSONObject.toJSONString(job));
        
        //终止
        if (1 == opFlag || 2 == opFlag)
        {
            //已审核，未执行与执行中
            if (2 == job.getJobStatus()
                    && (job.getExecStatus() == 1 || job.getExecStatus() == 2))
            {
                flag = true;
            }
            else
            {
                msg = "操作失败，该状态不支持" + (1 == opFlag ? "终止" : "暂停") + "！";
            }
        }
        //恢复
        else if (3 == opFlag)
        {
            //已审核，暂停中
            if (2 == job.getJobStatus() && job.getExecStatus() == 4)
            {
                flag = true;
            }
            else
            {
                msg = "操作失败，该状态不支持恢复！";
            }
        }
        
        if (flag)
        {
            //发送请求给调度
            if (HttpUtil.sendSchedPost(jobService.getSchUrl() + "/job-st-re",
                    reqData))
            {
                //修改审核状态
                jobService.updateStatus(jobId,
                        1 == opFlag ? 5 : 2 == opFlag ? 4 : 2,
                        2);
                
                msg = "操作成功！";
                logger.info(job.getJobName() + "操作成功！");
            }
            else
            {
                msg = "操作失败，调度异常！";
                logger.info(job.getJobName() + "操作失败！");
            }
        }
        
        return msg;
    }
    
    /**
     * 删除作业,支持批量
     */
    @RequiresPermissions("tji:job:del")
    @RequestMapping(value = "del")
    public @ResponseBody String delete(String ids,
            @RequestParam(required = false) Integer taskFlag,
            @RequestParam(required = false) Integer taskType)
    {
        //失败任务
        StringBuffer jobNames = new StringBuffer();
        
        String idArray[] = ids.split(",");
        
        if (null != idArray && idArray.length > 0)
        {
            for (String id : idArray)
            {
                Job job = jobService.get(id);
                
                //未审核与终止的作业可删除
                if (1 == job.getJobStatus() || 3 == job.getExecStatus()
                        || 5 == job.getExecStatus())
                {
                    //删除作业、实例与排班
                    jobService.delJob(job);
                }
                else
                {
                    jobNames.append(job.getJobName() + ",");
                }
                
            }
        }
        
        //有失败的任务
        if (StringUtils.isNotEmpty(jobNames))
        {
            msg = jobNames + "删除失败；已审核并未终止！";
        }
        else
        {
            msg = "删除作业成功!";
        }
        
        //taskFlag用于标示跳转到对应的界面中
        //单个删除，生成taskFlag
        if (null != taskType && 0 != taskType)
        {
            if (Contants.TASK_TYPE_ACT == taskType)
            {
                taskFlag = 3;
            }
            else if (Contants.TASK_TYPE_ETL == taskType
                    || Contants.TASK_TYPE_ETL_TMP == taskType)
            {
                taskFlag = 1;
            }
            else
            {
                taskFlag = 2;
            }
        }
        
        return msg;
    }
    
    /**
     * 审核作业
     * @param jobId
     * @return
     */
    private Map<String, String> auditJob(int jobId)
    {
        Map<String, String> map = new HashMap<String, String>();
        
        Map<String, String> reqData = new HashMap<String, String>();
        
        //获取任务作业详情,不包括输入输出
        JobDetail jobDetail = jobService.getJobDetail(jobId);
        //不需要传输的信息
        jobDetail.setCurrentUser(null);
        jobDetail.setPage(null);
        jobDetail.setCreateBy(null);
        jobDetail.setUpdateBy(null);
        
        //未审核,审核通过的不管
        if (1 == jobDetail.getJobStatus())
        {
            
            net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject
                    .fromObject(JSONObject.toJSONString(jobDetail));
            
            //不需要传输的信息
            jsonObj.remove("currentUser");
            jsonObj.remove("loginUser");
            jsonObj.remove("page");
            jsonObj.remove("createBy");
            jsonObj.remove("updateBy");
            jsonObj.remove("global");
            
            //任务详情json串，包括输入输出
            reqData.put("jobInfo", jsonObj.toString());
            
            //发送请求给调度
            if (HttpUtil.sendSchedPost(jobService.getSchUrl() + "/job-call",
                    reqData))
            {
                //修改状态
                jobService.updateStatus(jobId, 2, 1);
                jobService.updateStatus(jobId, 2, 2);
                //打印日志
                logger.info(jobDetail.getJobName() + "作业审核成功！");
            }
            else
            {
                logger.info(jobDetail.getJobName() + "作业审核失败！");
                map.put("jobName", jobDetail.getJobName());
                map.put("failDesc", "提交调度失败");
            }
        }
        else
        {
            logger.info(jobDetail.getJobName() + "作业已审核通过，不要重复审核！");
            map.put("jobName", jobDetail.getJobName());
            map.put("failDesc", "已审核通过，不能重复审核");
        }
        
        return map;
    }
    
    /**
     * 根据调度规则，解析执行类型
     * @param taskName
     * @param taskFlag
     * @return
     */
    private String getExecType(String scheduleRule)
    {
        //一次执行
        if (StringUtils.isBlank(scheduleRule))
        {
            return "一次";
        }
        else
        {
            //解析cron表达式
            String[] schArr = scheduleRule.trim().split(" ");
            
            //分钟
            if ("*".equals(schArr[1]))
            {
                return "分";
            }
            
            //小时
            if ("*".equals(schArr[2]))
            {
                return "小时";
            }
            
            //天
            if ("*".equals(schArr[3]))
            {
                return "天";
            }
            
            //周
            if ("*".equals(schArr[4]))
            {
                return "周";
            }
            
            //月
            if ("*".equals(schArr[5]))
            {
                return "月";
            }
            
            //年
            if ("*".equals(schArr[6]))
            {
                return "年";
            }
            
            return "一次";
        }
    }
    
    /**
     * 终止/暂停job
     * @param jobId jobId
     * @return
     */
    private String cancelKylinCube(String jobId) throws Exception
    {
        String baseUrl = Global.getConfig("kylin_base_api_url") + "jobs/"
                + jobId + "/cancel";
        return KylinHttpRequest.httpGet(baseUrl, "PUT");
    }
    
    /**
     * 恢复job
     * @param jobId jobId
     * @return
     */
    @SuppressWarnings("unused")
    private String resumeKylinCube(String jobId) throws Exception
    {
        String baseUrl = Global.getConfig("kylin_base_api_url") + "jobs/"
                + jobId + "/resume";
        return KylinHttpRequest.httpGet(baseUrl, "PUT");
    }
    
    /**
     * 创建临时表
     */
    @RequestMapping(value = "createHiveTemp")
    public @ResponseBody String createHiveTemp(int jobId) throws Exception
    {
        boolean flag = true;
        try
        {
            JobDetail jobDetail = jobService.getJobDetail(jobId);
            List<Map<String, Object>> outputList = jobDetail
                    .getOutputMetaList();
            for (int i = 0; i < outputList.size(); i++)
            {
                Map<String, Object> output = outputList.get(i);
                if (output.get("type") != null
                        && "5".equals(output.get("type").toString()))
                {
                    String sql = (String) output.get("external");
                    String ip = (String) output.get("ip");
                    String user = (String) output.get("user");
                    String pwd = (String) output.get("password");
                    String database = (String) output.get("dir");
                    String port = (String) output.get("port");
                    try
                    {
                        boolean flag1 = DBUtils.excuteHiveSql(ip,
                                port,
                                user,
                                pwd,
                                database,
                                sql);
                        flag = flag && flag1;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        flag = false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            flag = false;
            logger.error("createHiveTemp===", e);
        }
        
        return "success";
    }
    
    @RequestMapping("test")
    public void test(HttpServletRequest request, HttpServletResponse response)
            throws IOException
    {
        response.sendRedirect("http://172.18.88.67:8080/#/main/dashboard/metrics");
    }
    
    /**
     * 测试统计主页计算任务数据接口
     * @throws ParseException
     */
    @RequestMapping("test/test")
    public void tt() throws ParseException{
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int hour = 13;
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
//        String date = dateFormat.format(calendar.getTime());
        String date = "2017-08-09 12";
        String beginTime = date + ":00:00";
        String endTime = date + ":59:59";
        JobInstance instance = new JobInstance();
        instance.setcStartTime(DateUtils.parseDate(beginTime, "yyyy-MM-dd HH:mm:ss"));
        instance.setcEndTime(DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"));
        List<JobInstance> list = jobInstanceService.findList(instance);
        List<Dict> taskTypes = DictUtils.getDictList("task_type");
        Map<String,TaskHourReport> mp = new HashMap<String,TaskHourReport>();
        if(taskTypes!=null && taskTypes.size()>0){
            for (Dict dict : taskTypes){
                TaskHourReport hourReport = new TaskHourReport();
                hourReport.setSdate(date);
                hourReport.setShour(hour==0?23:(hour-1==0?24:hour-1));
                hourReport.setTaskType(Integer.valueOf(dict.getValue()));
                mp.put(dict.getValue(), hourReport);
            }
        }
        if(list!=null && list.size()>0){
            for (JobInstance jobInstanceBean : list){
                String taskType = String.valueOf(jobInstanceBean.getTaskType());
                int code = jobInstanceBean.getExecCode();
                TaskHourReport hourReport = mp.get(taskType);
                if(code!=1){
                	Long a = jobInstanceBean.getExecTime().getTime()/1000L;
                    Long b = jobInstanceBean.getRespTime().getTime()/1000L;
                    int tc = hourReport.getTimeConsuming();
                    tc = tc + Integer.valueOf(b.toString())-Integer.valueOf(a.toString());
                    hourReport.setTimeConsuming(tc);
                }
                int num = 0;
                if(code==1){//运行中
                    num = hourReport.getRunningCount() + 1;
                    hourReport.setRunningCount(num);
                }else if(code==2){//成功
                    num = hourReport.getSuccessCount() + 1;
                    hourReport.setSuccessCount(num);
                }else if(code==3 || code==4){//失败
                    num = hourReport.getFailCount() + 1;
                    hourReport.setFailCount(num);
                }else if(code==5){//暂停
                    num = hourReport.getPauseCount() + 1;
                    hourReport.setPauseCount(num);
                }
                mp.put(taskType, hourReport);
            }
        }
        Set<Entry<String,TaskHourReport>> set = mp.entrySet();
        if(set!=null){
            for (Entry<String, TaskHourReport> entry : set){
            	taskHourReportService.save(entry.getValue());
            }
        }
    }
}