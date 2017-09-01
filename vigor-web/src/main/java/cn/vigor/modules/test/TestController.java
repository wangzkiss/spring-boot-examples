package cn.vigor.modules.test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.vigor.common.config.Global;
import cn.vigor.common.contants.Contants;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.HttpUtil;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.compute.bean.HiveSql;
import cn.vigor.modules.compute.bean.Output;
import cn.vigor.modules.compute.entity.ComputeTaskDetailBean;
import cn.vigor.modules.compute.service.CommonService;
import cn.vigor.modules.compute.utils.TestUtil;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaResult;
import cn.vigor.modules.meta.entity.MetaResultPro;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaSourcePro;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.entity.MetaStorePro;
import cn.vigor.modules.meta.service.MetaRepoService;
import cn.vigor.modules.meta.service.MetaResultService;
import cn.vigor.modules.meta.service.MetaSourceService;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;
import cn.vigor.modules.tji.entity.Job;
import cn.vigor.modules.tji.entity.JobDetail;
import cn.vigor.modules.tji.entity.Task;
import cn.vigor.modules.tji.service.JobService;
import cn.vigor.modules.tji.service.TasksService;

/**
 * 数据库连接信息Controller
 * @author kiss
 * @version 2016-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/test/")
public class TestController extends BaseController {

	@Autowired
	private MetaRepoService metaRepoService;
    @Autowired
    private MetaSourceService metaSourceService;
    @Autowired
    private MetaStoreService metaStoreService;
    
    @Autowired
	private MetaResultService metaResultService;
    @Autowired
    private CommonService commonService;
    /**
     * 查询提交接口
     * @param id
     * @param sql
     * @param tableName
     * @param type (sparkBases tranfBases kylinBases)
     * @return
     */
    @RequestMapping(value ="check")//repo/RepoStore/query-submit
    @ResponseBody
    public JSONObject check(int functionId)
    {
        JSONObject data = new JSONObject();
        try
        {
        	 String tag = DateUtils.getDate("yyyymmdd_HHmmss");

             MetaRepo metaRepo = new MetaRepo("mysql_conn" + tag, "172.18.88.71", 6, "root", "123456", "3306", Integer.valueOf(0), "test" + tag, "测试数据");
             String repoIn = this.metaRepoService.saveTest(metaRepo);
             List <MetaSourcePro> metaSourceProList = Lists.newArrayList();
             metaSourceProList.add(new MetaSourcePro("id", Integer.valueOf(1), "int", "", "", "11"));
             metaSourceProList.add(new MetaSourcePro("name", Integer.valueOf(2), "name", "", "", "64"));
             metaRepo.setId(repoIn);
             MetaSource metaSource = new MetaSource(metaRepo, "mysql_source" + tag, 6, ";", "测试描述", "test" + tag, "", metaSourceProList);
             this.metaSourceService.save(metaSource);

             MetaRepo metaRepoStore = new MetaRepo("hive_conn_store" + tag, "172.18.88.68", 5, "hive", "hive", "10000", Integer.valueOf(1), "test" + tag, "测试数据");
             String repoOut = this.metaRepoService.saveTest(metaRepoStore);
             List<MetaStorePro> metaStoreProList = Lists.newArrayList();
             metaStoreProList.add(new MetaStorePro("id", Integer.valueOf(1), "", "int", "", "", "4"));
             metaStoreProList.add(new MetaStorePro("name", Integer.valueOf(2), "", "string", "", "", "64"));
             metaRepoStore.setId(repoOut);
             MetaStore metaStore = new MetaStore(metaRepoStore, "hive_store" + tag, "hdfs://xdata2/apps/hive/warehouse/test" + tag + ".db/test" + tag + "/", 5, ";", "", "test" + tag, "", "textfile", metaStoreProList);
             String sid = this.metaStoreService.saveTest(metaStore);
             metaStore.setId(sid);
             metaRepoStore.setId(repoOut);
             this.metaStoreService.enable(metaRepoStore, metaStore);

             MetaRepo metaRepoResult = new MetaRepo("hive_conn_result" + tag, "172.18.88.68", 5, "hive", "hive", "10000", Integer.valueOf(2), "result" + tag, "测试数据");
             String repoOut2 = this.metaRepoService.saveTest(metaRepoResult);
             List<MetaResultPro> metaResultProList = Lists.newArrayList();
             metaResultProList.add(new MetaResultPro("id", Integer.valueOf(1), "int", "", "", 2, "11"));
             metaResultProList.add(new MetaResultPro("name", Integer.valueOf(2), "string", "", "", 2, "64"));
             metaResultProList.add(new MetaResultPro("track_id", Integer.valueOf(3), "string", "", "", 2, "64"));
             metaRepoResult.setId(repoOut2);
             MetaResult metaResult = new MetaResult(metaRepoResult, "hive_result" + tag, 5, ";", "测试数据", "result" + tag, "textfile", "hdfs://xdata2/apps/hive/warehouse/result" + tag + ".db/result" + tag + "/", metaResultProList);
             String rid = this.metaResultService.saveTest(metaResult);
             metaResult.setId(rid);
             this.metaResultService.enable(metaRepoResult, metaResult);

             Task task = new Task();
             String functionParam = "{'input_source_name_mysql':'mysql_source" + tag + "'," + 
               "'input_table_file_name':'test'," + 
               "'input_ip':'172.18.88.71'," + 
               "'input_db_dir_name':'test'," + 
               "'input_username':'root'," + 
               "'input_dbpwd':'Co9ME/Agq664+ZimAcnckA=='," + 
               "'input_port':'3306'," + 
               "'output_store_name_hive':'hive_store" + tag + "'," + 
               "'output_db_dir_name':'test" + tag + "'," + 
               "'output_table_file_name':'test" + tag + "'," + 
               "'output_delimiters':';'}";
             String taskName = "testmysql2hive_job_" + DateUtils.getDate("yyyymmdd_HHmmss");
             functionId = functionId == 0 ? 1 : functionId;
             int groupId = 0;
             task.setFunctionParam(functionParam);
             task.setTaskName(taskName);

             task.setFunctionId(functionId);
             task.setGroupId(Integer.valueOf(groupId));

             int tsktid = setEtl(task);

             Job job = new Job();
             job.setPauseFlag(Integer.valueOf(1));
             job.setTaskIds(tsktid+"");
             job.setNotifyEmail("");
             job.setAgainFlag(0);
             job.setScheduleLevel(Integer.valueOf(5));
             int jobId = save(job);

             audit(jobId+"", Integer.valueOf(0));

             int hiveJobId = addHiveTask(metaStore, metaResult);

             Job hivejob = new Job();
             hivejob.setPauseFlag(Integer.valueOf(1));
             hivejob.setTaskIds(hiveJobId+"");
             hivejob.setNotifyEmail("");
             hivejob.setAgainFlag(0);
             hivejob.setScheduleLevel(Integer.valueOf(5));
             int hivejobId = save(hivejob);
             audit(hivejobId+"", Integer.valueOf(0));

             data.put("success", Boolean.valueOf(true));

             data.put("data", "自动测试成功！具体错误请查任务运行日志");
        }
        catch (Exception e)
        {
            if(e!=null&&e.getMessage().contains("statement: FAILED"))
            {
                data.put("msg", "sql语法错误,异常详情："+e.getMessage());
            }else if(e!=null&&e.getMessage().contains("syntax error")){
                data.put("msg", "sql语法错误,异常详情："+e.getMessage());
            } else{
                data.put("msg", e.getMessage());
            }
            data.put("success", false);
            
            data.put("data", null);
            e.printStackTrace();
        }
        return data;
    }
    @Autowired
    private TasksService taskService;
    @Autowired
    private JobService jobService;
    
    
    
    /**
     
repoId:1381
repoName:testhive_conn
selfSql:select * from test
btnHiveSql:2
otable:{"test.select":5220}
outRepoId:1381
groupId:10
outRepoName:testhive_conn
outputType:1
outputId:5220
     * 
     */
    public int  addHiveTask(MetaStore metaStore, MetaResult metaResult) throws Exception{
    	try {
    		
    		  int repoId = Integer.valueOf(metaStore.getRepoId().getId()).intValue();
    	      String inputDataBase = metaStore.getRepoId().getRepoName();
    	      int sid = Integer.valueOf(metaStore.getId()).intValue();
    	      int outRepoId = Integer.valueOf(metaResult.getRepoId().getId()).intValue();
    	      int outputId = Integer.valueOf(metaResult.getId()).intValue();
    	      Output params = new Output();
    	      ComputeTaskDetailBean detailBean = new ComputeTaskDetailBean();
    	      params.setTaskName("jechivejob_" + DateUtils.getDate("yyyymmdd_HHmmss"));
    	      params.setTaskDesc("jechivejob_" + DateUtils.getDate("yyyymmdd_HHmmss"));
    	      params.setRepoId(repoId+"");
    	      params.setOutputString("hadoop_hive");
    	      params.setSelfSql("select * from " + inputDataBase + "." + metaStore.getStoreFile());
    	      params.setOutputId(outputId);
    	      params.setOutputType(1);
    	      params.setBtnHiveSql(2);
    	      params.setTaskId(0);
    	      params.setInputDataBase(inputDataBase);
    	      params.setOtable("{'test.select':" + sid + "}");
    	      HiveSql hiveSql = new HiveSql();
    	      Output output = new Output();
    	      String tname = params.getTaskName();
    	      if (tname.contains("&mdash;")) {
    	        tname = tname.replaceAll("&mdash;", "—");
    	      }
    	      output.setTaskName(tname);
    	      output.setTaskDesc("测试任务");
    	      output.setTaskType(3);
    	      output.setRepoId(repoId+"");
    	      output.setOutRepoId(outRepoId);
    	      output.setGroupId(0);
    	      output.setOutputString("hadoop_hive");
    	      output.setStoragType(0);
    	      output.setOutputType(0);
    	      output.setOutputId(outputId);
    	      output.setCreateUser(detailBean.getLoginUser().getLoginName());
    	      output.setInputDataBase(inputDataBase);
    	      output.setOutputDataBase(metaResult.getRepoId().getRepoName());
    	      output.setTaskId(0);
    	      hiveSql.setDataBaseName(inputDataBase);
        if(params.getBtnHiveSql()==1){ 
        	
        }else if(params.getBtnHiveSql()==2){                        //自定义sql
            hiveSql.setType(2);                                     //类型为2
            
            String sqlSelf = params.getSelfSql();
            if(sqlSelf.contains("&gt;")){
                sqlSelf =  sqlSelf.replaceAll("&gt;", ">");
            }
            
            if(sqlSelf.contains("&lt;")){
                sqlSelf = sqlSelf.replaceAll("&lt;", "<");
            }
            
            hiveSql.setValue(TestUtil.repaceWhiteSapce(sqlSelf));
            hiveSql.setAssociationList(null); 
            output.setHiveSql(hiveSql);    //set到output中
            Map<String, Integer> otherTab = new HashMap<String,Integer>();
            String tabName = null;
            int tbId = 0;
            String os = params.getOtable().replaceAll("&quot;", "");
            if(os!=null&&!os.equals("")&&!os.equals("null")&&!os.equals("{}")){
                os = os.substring(os.indexOf("{")+1, os.lastIndexOf("}"));
                String[]  ots = os.split(",");
                for(String otss :  ots){
                    String[] ot= otss.split(":");
                    tabName = ot[0];
                    tbId = Integer.parseInt(ot[1].trim());
                    otherTab.put(tabName, tbId);
                }
                output.setOtherTable(otherTab);
            }
         } 
            output.getXmlString2();
            output.setTaskStatus(0);
            int taskId= commonService.createTask(output);
            return taskId;
        }  catch (Exception e) {
        	e.printStackTrace();
        }
        return 0;
    }
    
    
    /**
     * 审核作业
     * @param jobIds
     * @param groupId
     * @return
     */
    public String audit(String jobIds,Integer groupId)
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
     * 创建作业
     * @param job
     * @return
     * @throws Exception
     */
    public  int  save(Job job 
           ) throws Exception
    {
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
                                    jobIds.add(job.getJobId());
                                }
                                else
                                {
                                    failureMsg.put(taskName, "作业已审核，不支持修改");
                                }
                            }
                        }
                        //没有作业则新增
                        else
                        {
                            //保存新增
                            job.setIsNewRecord(true);
                            jobService.save(job);
                            jobIds.add(job.getJobId());
                        }
                    }
                }
                catch (Exception e)
                {
                    failureMsg.put(taskName, "系统异常");
                    logger.error("save===", e);
                }
            }
        }
        
         
        
        return  job.getJobId();
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
     *创建任务
     */
    private int setEtl(Task task) throws Exception
    {
        
        try
        {
            String functionParam = task.getFunctionParam();
            JSONObject jj = JSONObject.parseObject(functionParam);
            User user = UserUtils.getUser();
            task.setCreateUser(user.getLoginName());
            task.setComeFrom(1);
            
            jj.put("input_hdfs_nameservices",
                    Global.getConfig("hdfs_nameservices"));
            jj.put("output_hdfs_nameservices",
                    Global.getConfig("hdfs_nameservices"));
            
            String inputSourceName = "";
            String outputStoreName = "";
            Set<String> keys = jj.keySet();
            System.out.println("keys.size()=" + keys.size());
            for (String str : keys)
            {
                if (str.contains("input_source_name"))
                {
                    inputSourceName = jj.getString(str);
                }
                if (str.contains("output_store_name"))
                {
                    outputStoreName = jj.getString(str);
                }
                if (!inputSourceName.isEmpty() && !outputStoreName.isEmpty())
                {
                    break;
                }
            }
            if (inputSourceName.isEmpty())
            {
                inputSourceName = jj.getString("input_source_name") == null ? ""
                        : jj.getString("input_source_name");
            }
            
            if (outputStoreName.isEmpty())
            {
                outputStoreName = jj.getString("output_store_name") == null ? ""
                        : jj.getString("output_store_name");
            }
            
            if (task.getTaskId() == null)
            {
                task.setTaskName("TASK_"+task.getTaskName());
                task.setTaskType(2);
                taskService.insertTask(task);
                if (!inputSourceName.isEmpty())
                {
                    MetaSource ms = metaSourceService.findUniqueByProperty(
                            "source_name", inputSourceName);
                    int sourceId;
                    if (null != ms)
                    {
                        List<MetaSourcePro> proList = ms.getMetaSourceProList();
                        if (null != proList && !proList.isEmpty())
                        {
                            StringBuffer pros = new StringBuffer();
                            for (MetaSourcePro pro : proList)
                            {
                                pros.append(pro.getProName() + ","
                                        + pro.getProType() + ","
                                        + pro.getDataFormat() + ";");
                            }
                            
                            jj.put("input_fields",
                                    pros.substring(0, pros.length() - 1));
                            task.setFunctionParam(jj.toString());
                        }
                        
                        sourceId = Integer.parseInt(ms.getId());
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("inputId", sourceId);
                        map.put("taskId", task.getTaskId());
                        map.put("inputType", 0);
                        taskService.insertTaskInput(map);
                    }
                }
                if (!outputStoreName.isEmpty())
                {
                    MetaStore ms = metaStoreService.findUniqueByProperty(
                            "store_name", outputStoreName);
                    int storeId;
                    if (null != ms)
                    {
                        storeId = Integer.parseInt(ms.getId());
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("ouputId", storeId);
                        map.put("taskId", task.getTaskId());
                        map.put("outputType", 1);
                        taskService.insertTaskOutput(map);
                        
                        if (Contants.STORE_TYPE_HIVE == ms.getStoreType())
                        {
                            jj.put("output_port",
                                    String.valueOf(
                                            Global.getConfig("hdfsPort")));
                            jj.put("hive_db_dir_name",
                                    ms.getRepoId().getRepoName());
                            jj.put("hive_port",
                                    String.valueOf(ms.getRepoId().getPort()));
                            jj.put("hive_pwd", ms.getRepoId().getUserPwd());
                            jj.put("hive_username",
                                    ms.getRepoId().getUserName());
                            jj.put("hive_ip", ms.getRepoId().getIp());
                            
                            task.setFunctionParam(jj.toString());
                        }
                    }
                }
                //修改task_function表时,需要对functionParam中的密码进行加密之后再保存
                String funcParam = handleFunctionParam(task.getFunctionParam(),
                        0);
                task.setFunctionParam(funcParam);
                taskService.insertTaskFunction(task);
                
            }
            else
            {
                task.setTaskType(2);
                taskService.updateTask(task);
                if (!inputSourceName.isEmpty())
                {
                    MetaSource ms = metaSourceService.findUniqueByProperty(
                            "source_name", inputSourceName);
                    int sourceId;
                    if (null != ms)
                    {
                        List<MetaSourcePro> proList = ms.getMetaSourceProList();
                        if (null != proList && !proList.isEmpty())
                        {
                            StringBuffer pros = new StringBuffer();
                            for (MetaSourcePro pro : proList)
                            {
                                pros.append(pro.getProName() + ","
                                        + pro.getProType() + ","
                                        + pro.getDataFormat() + ";");
                            }
                            
                            jj.put("input_fields",
                                    pros.substring(0, pros.length() - 1));
                            task.setFunctionParam(jj.toString());
                        }
                        
                        sourceId = Integer.parseInt(ms.getId());
                        
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("inputId", sourceId);
                        map.put("taskId", task.getTaskId());
                        map.put("inputType", 0);
                        taskService.updateTaskInput(map);
                    }
                }
                if (!outputStoreName.isEmpty())
                {
                    MetaStore ms = metaStoreService.findUniqueByProperty(
                            "store_name", outputStoreName);
                    int storeId;
                    if (null != ms)
                    {
                        storeId = Integer.parseInt(ms.getId());
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("ouputId", storeId);
                        map.put("taskId", task.getTaskId());
                        map.put("outputType", 1);
                        taskService.updateTaskOutput(map);
                    }
                }
                String functionparam = handleFunctionParam(
                        task.getFunctionParam(), 0);
                task.setFunctionParam(functionparam);
                taskService.updateTaskFunction(task);
            }
        }
        catch (Exception e)
        {
            String msg = e.getMessage();
            if (msg.contains("Duplicate") && msg.contains("idx_task_name"))
            {
                msg = "任务新建失败，任务名" + task.getTaskName() + " 已存在！";
            }
            else
            {
                msg = "保存失败：失败原因：" + e.getMessage();
            }
            e.printStackTrace();
        }
        return task.getTaskId();
    }
    private String handleFunctionParam(String funcParam, int type)
    {
        if (funcParam != null)
        {
            Map map = JSONObject.parseObject(funcParam, Map.class);
            String inpwd = map.get("input_dbpwd") == null ? null
                    : map.get("input_dbpwd").toString();
            String outpwd = map.get("output_dbpwd") == null ? null
                    : map.get("output_dbpwd").toString();
            if (type == 1)
            {//解密
                if (StringUtils.isNotEmpty(inpwd) && inpwd.endsWith("=="))
                {
                    String dinpwd = AESUtil.decForTD(inpwd);
                    //替换funcParam中的密码密文
                    funcParam = funcParam.replace(
                            "\"input_dbpwd\":\"" + inpwd + "\"",
                            "\"input_dbpwd\":\"" + dinpwd + "\"");
                }
                if (StringUtils.isNotEmpty(outpwd) && outpwd.endsWith("=="))
                {
                    String doutpwd = AESUtil.decForTD(outpwd);
                    //替换funcParam中的密码密文
                    funcParam = funcParam.replace(
                            "\"output_dbpwd\":\"" + outpwd + "\"",
                            "\"output_dbpwd\":\"" + doutpwd + "\"");
                }
            }
            else if (type == 0)
            {//加密
                if (StringUtils.isNotEmpty(inpwd) && !inpwd.endsWith("=="))
                {
                    String dinpwd = AESUtil.encForTD(inpwd);
                    funcParam = funcParam.replace(
                            "\"input_dbpwd\":\"" + inpwd + "\"",
                            "\"input_dbpwd\":\"" + dinpwd + "\"");
                }
                if (StringUtils.isNotEmpty(outpwd) && !outpwd.endsWith("=="))
                {
                    String doutpwd = AESUtil.encForTD(outpwd);
                    funcParam = funcParam.replace(
                            "\"input_dbpwd\":\"" + outpwd + "\"",
                            "\"input_dbpwd\":\"" + doutpwd + "\"");
                }
            }
        }
        return funcParam;
    }
}