package cn.vigor.modules.tji.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.kylin.cube.model.CubeDesc;
import org.apache.kylin.rest.request.CubeRequest;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import cn.vigor.common.config.Global;
import cn.vigor.common.contants.Contants;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.jars.service.FunctionParamService;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaSourcePro;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.entity.MetaStorePro;
import cn.vigor.modules.meta.service.MetaSourceService;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;
import cn.vigor.modules.tji.entity.Task;
import cn.vigor.modules.tji.entity.TaskDetail;
import cn.vigor.modules.tji.entity.TaskFunction;
import cn.vigor.modules.tji.entity.TaskGroup;
import cn.vigor.modules.tji.service.JobService;
import cn.vigor.modules.tji.service.TaskGroupService;
import cn.vigor.modules.tji.service.TasksService;
import cn.vigor.modules.tji.util.KylinHttpRequest;
import net.sf.json.JSONArray;

/**
 * 任务相关Controller
 * @author zhangfeng
 * @version 2016-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/tji/task")
public class TaskController extends BaseController
{
    public static Logger log = Logger.getLogger(TaskController.class);
    
    /**
     * 任务
     */
    @Autowired
    private TasksService taskService;
    
    /**
     * 作业
     */
    @Autowired
    private JobService jobService;
    
    /**
     * 数据源
     */
    @Autowired
    private MetaSourceService metaSourceService;
    
    /**
     * 平台存储
     */
    @Autowired
    private MetaStoreService metaStoreService;
    
    /**
     * 工作流
     */
    /*@Autowired
    RepositoryService repositoryService;*/
    
    @Autowired
    private TaskGroupService taskGroupService;
    
    /**
     * 提示信息
     */
    private String msg;
    
    @ModelAttribute
    public Task get(@RequestParam(required = false) String id)
    {
        Task entity = null;
        if (StringUtils.isNotBlank(id))
        {
            entity = taskService.get(id);
        }
        if (entity == null)
        {
            entity = new Task();
        }
        return entity;
    }
    
    /**
     * 任务列表页面
     */
    @RequiresPermissions("tji:task:list")
    @RequestMapping(value = { "list", "" })
    public String list(Task task, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        //默认按创建时间倒序
        Page<Task> paging = new Page<Task>(request, response);
        
        if (StringUtils.isBlank(paging.getOrderBy()))
        {
            paging.setOrderBy("taskId desc");
        }
        
        String groupName = task.getGroupName();
        if (StringUtils.isNotEmpty(groupName) && groupName.contains("%"))
        {
            try
            {
                groupName = URLDecoder.decode(groupName, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                groupName = "";
            }
            
            task.setGroupName(groupName);
        }
        
        //任务列表
        Page<Task> page = taskService.findPage(paging, task);
        
        //重新封装
        List<Task> ntList = new ArrayList<Task>();
        List<Task> taskList = page.getList();
        
        for (Task nTask : taskList)
        {
            nTask.setJobList(jobService.getJobByTaskId(nTask.getTaskId()));
            ntList.add(nTask);
        }
        
        page.setList(ntList);
        
        model.addAttribute("page", page);
        return "modules/tji/taskList";
    }
    
    /**
     * 查看任务详情
     * @throws Exception 
     */
    @RequiresPermissions("tji:task:view")
    @RequestMapping(value = "view")
    public String view(int taskId, Model model) throws Exception
    {
        TaskDetail taskDetail = taskService.getTaskDetail(taskId);
        if (taskDetail.getTaskType() == 12)
        {//kylin任务
            String result = getKylinCubeInfo(taskDetail.getTaskName());
            if (result.contains("{"))
            {
                CubeDesc cubeDesc = JSONObject.parseObject(result,
                        CubeDesc.class);
                model.addAttribute("cubeInfo", cubeDesc);
            }
            else
            {
                log.info(result);
                addMessage(model, result);
            }
        }
        model.addAttribute("taskDetail", taskDetail);
        return "modules/tji/taskDetail";
    }
    
    /**
     * 保存任务
     */
    @RequiresPermissions(value = { "tji:task:add",
            "tji:task:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Task task, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        if (!task.getIsNewRecord())
        {//编辑表单保存
            Task t = taskService.get(task.getId());//从数据库取出记录的值
            MyBeanUtils.copyBeanNotNull2Bean(task, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
            taskService.save(t);//保存
        }
        else
        {//新增表单保存
            taskService.save(task);//保存
        }
        addMessage(redirectAttributes, "保存任务成功");
        return "redirect:" + Global.getAdminPath() + "/tji/task/?repage";
    }
    
    @RequestMapping(value = "formKylin")
    public String formKylinTask(CubeRequest cubeRequest, Task task, Model model)
    {
        try
        {
            List<TaskGroup> groups = taskGroupService.getGroupInfoByType(2);
            model.addAttribute("groups", groups);
            TaskDetail taskDetail = null;
            if (task.getTaskId() != null && task.getTaskId() != 0)
            {
                taskDetail = taskService.getTaskDetail(task.getTaskId());
                model.addAttribute("taskDetail", taskDetail);
            }
            if (taskDetail != null && taskDetail.getTaskType() == 12)
            {//kylin任务
                String result = getKylinCubeInfo(taskDetail.getTaskName());
                if (result.contains("{"))
                {
                    CubeDesc cubeInstance = JSONObject.parseObject(result,
                            CubeDesc.class);
                    model.addAttribute("cubeInfo", cubeInstance);
                }
                else
                {
                    log.info(result);
                    addMessage(model, result);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "modules/tji/taskconfigure/kylinTask";
    }
    
    /**
     * 保存kylin任务
     * @param cubeRequest
     * @param task
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "saveKylin")
    @ResponseBody
    public JSONObject saveKylinTask(CubeRequest cubeRequest, Task task,
            Model model, RedirectAttributes redirectAttributes)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            String method = "";
            if (task.getTaskId() != null)
            {
                Task tk = taskService.get(task.getTaskId().toString());
                task.setId(tk.getId());
                if (tk != null)
                {
                    cubeRequest.setUuid(tk.getThirdTaskId());
                }
                method = "PUT";
            }
            else
            {
                method = "POST";
            }
            if (StringUtils.isEmpty(cubeRequest.getProject()))
            {
                cubeRequest
                        .setProject(Global.getConfig("kylin_default_project"));
            }
            String result = saveKylinCube(cubeRequest, method);
            if (result.contains("{"))
            {
                CubeRequest request = JSONObject.parseObject(result,
                        CubeRequest.class);
                if (StringUtils.isEmpty(request.getUuid()))
                {
                    jsonObject.put("msg", request.getMessage());
                    jsonObject.put("code", 1);
                }
                else
                {
                    if (method.equals("POST"))
                    {
                        task.setTaskName("TASK_" + request.getCubeName());
                    }
                    if (task.getTaskId() == null)
                    {
                        String uuid = request.getUuid();
                        task.setThirdTaskId(uuid);
                        task.setComeFrom(1);
                        if (UserUtils.getUser() != null)
                        {
                            task.setCreateUser(UserUtils.getUser().getName());
                        }
                        else
                        {
                            task.setCreateUser("admin");
                        }
                        taskService.save(task);//保存
                    }
                    else
                    {
                        Task t = taskService.get(task.getTaskId().toString());//从数据库取出记录的值
                        MyBeanUtils.copyBeanNotNull2Bean(task, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
                        taskService.save(t);//保存
                    }
                    jsonObject.put("msg", "保存任务成功");
                    jsonObject.put("code", 0);
                }
            }
            else
            {
                jsonObject.put("msg", result);
                jsonObject.put("code", 1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jsonObject.put("msg", "保存任务失败");
            jsonObject.put("code", 1);
        }
        return jsonObject;
    }
    
    /**
     * 删除任务，支持批量
     */
    @RequiresPermissions("tji:task:del")
    @RequestMapping(value = "del")
    public @ResponseBody String delete(String ids,
            @RequestParam(required = false) Integer taskFlag,
            @RequestParam(required = false) Integer taskType)
    {
        //失败任务
        StringBuffer taskNames = new StringBuffer();
        
        String idArray[] = ids.split(",");
        
        if (null != idArray && idArray.length > 0)
        {
            for (String id : idArray)
            {
                int taskId = Integer.valueOf(id);
                Task task = taskService.get(id);
                
                //先判断任务下有没有作业，有作业或者被工作流引用则不能删除
                if (0 == jobService.getJobCountTaskId(taskId)
                        && 0 == taskService
                                .getSTaskCountByName(task.getTaskName()))
                {
                    taskService.delTaskFunction(taskId);
                    taskService.delTaskRelation(taskId);
                    taskService.delTaskOutPut(taskId);
                    taskService.delTaskInPut(taskId);
                    taskService.delete(taskService.get(id));
                    
                    //工作流任务删除模型
                    if (9 == task.getTaskType())
                    {
                       // repositoryService.deleteModel(task.getXmlData());
                    }
                    if (12 == task.getTaskType())
                    {
                        try
                        {
                            String result = delKylinCube(task.getTaskName());
                            log.info("删除kylin任务:" + result);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            log.error("kylin任务从kylin平台删除失败:" + e.getMessage());
                        }
                    }
                }
                //获取任务名用于提示
                else
                {
                    taskNames.append(task.getTaskName() + ",");
                }
            }
        }
        
        //有失败的任务
        if (StringUtils.isNotEmpty(taskNames))
        {
            msg = taskNames + "删除失败；已存在作业或被工作流引用！";
        }
        else
        {
            msg = "删除任务成功!";
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
     * 下载任务模板
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("tji:task:import")
    @RequestMapping(value = "import-template")
    public @ResponseBody String importTemplate(int functionId,
            String functionName, HttpServletResponse response,
            RedirectAttributes redirectAttributes)
    {
        try
        {
            //获取任务参数
            String fileName = functionName + "任务导入模板.xlsx";
            
            List<String> pmList = functionParamService
                    .findParamsNameByFunctionId(functionId, 1);
            pmList.add(0, "任务名称");
            pmList.add(1, "任务描述");
            
            new ExportExcel(functionName + "模板", pmList)
                    .write(response, fileName).dispose();
            
            return null;
        }
        catch (Exception e)
        {
            log.error("importTemplate===", e);
        }
        
        return "";
    }
    
    /**
     * 导入模板任务
     */
    @RequiresPermissions("tji:task:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public @ResponseBody String importFile(MultipartFile file, int groupId,
            RedirectAttributes redirectAttributes)
    {
        Map<String, Object> resultMsg = new LinkedHashMap<String, Object>();
        int successNum = 0;
        int failureNum = 0;
        Map<String, String> failureMsg = new LinkedHashMap<String, String>();
        Integer funId = 0;
        String taskName = "";
        
        try
        {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            
            String funName = (String) ei.getCellValue(ei.getRow(0), 0);
            funName = funName.substring(0, funName.indexOf("模板"));
            funId = taskService.getFunIdByName(funName);
            
            if (null != funId)
            {
                //解析文件，跳过第一行
                for (int i = 2; i < ei.getLastDataRowNum(); i++)
                {
                    try
                    {
                        Row row = ei.getRow(i);
                        
                        //任务名及描述
                        taskName = (String) ei.getCellValue(row, 0);
                        String taskDesc = (String) ei.getCellValue(row, 1);
                        
                        if (null != taskName && !"".equals(taskName.trim()))
                        {
                            Task oTask = taskService
                                    .getTaskByName("TASK_" + taskName);
                            
                            //已存在重名任务
                            if (null != oTask && null != oTask.getTaskId())
                            {
                                failureMsg.put(taskName, "已存在重名任务");
                                failureNum++;
                            }
                            else
                            {
                                //输入输出
                                String inputName = null;
                                String outputName = null;
                                String ititileName = null;
                                String otitileName = null;
                                
                                String inSqlName = null;
                                String inSqlValue = null;
                                
                                //函数参数
                                Map<Object, Object> funMap = new HashMap<Object, Object>();
                                
                                for (int j = 2; j < ei.getLastCellNum(); j++)
                                {
                                    //封装函数参数名与值,参数名固定在第二行
                                    Object colName = ei
                                            .getCellValue(ei.getRow(1), j);
                                    Object colValue = ei.getCellValue(row, j);
                                    
                                    if (!StringUtils.isEmpty(
                                            colValue.toString().trim()))
                                    {
                                        funMap.put(colName,
                                                colValue.toString().trim());
                                    }
                                    
                                    //输入
                                    if (colName.toString()
                                            .contains("input_source_name"))
                                    {
                                        ititileName = colName.toString();
                                        inputName = (String) colValue;
                                    }
                                    //输出
                                    else if (colName.toString()
                                            .contains("output_store_name"))
                                    {
                                        otitileName = colName.toString();
                                        outputName = (String) colValue;
                                    }
                                    else if (colName.toString()
                                            .contains("input_sql"))
                                    {
                                        inSqlName = colName.toString();
                                        inSqlValue = (String) colValue;
                                    }
                                }
                                
                                //判断有没有操作输入输出文件的权限
                                MetaSource inms = null;
                                if (StringUtils.isNotEmpty(inputName))
                                {
                                    inms = metaSourceService
                                            .findUniqueByProperty("source_name",
                                                    inputName.trim());
                                }
                                
                                MetaStore otms = null;
                                if (StringUtils.isNotEmpty(outputName))
                                {
                                    otms = metaStoreService
                                            .findUniqueByProperty("store_name",
                                                    outputName.trim());
                                }
                                
                                if (null != inms && null != otms)
                                {
                                    //判断输入输出与填的类型是否对应
                                    if (((ititileName.startsWith(
                                            "input_source_name_hdfs")
                                            && Contants.SOURCE_TYPE_HDFS == inms
                                                    .getSourceType())
                                            || (ititileName.startsWith(
                                                    "input_source_name_hive")
                                                    && Contants.SOURCE_TYPE_HIVE == inms
                                                            .getSourceType())
                                            || (ititileName.startsWith(
                                                    "input_source_name_ftp")
                                                    && Contants.SOURCE_TYPE_FTP == inms
                                                            .getSourceType())
                                            || (ititileName.startsWith(
                                                    "input_source_name_hbase")
                                                    && Contants.SOURCE_TYPE_HBASE == inms
                                                            .getSourceType())
                                            || (ititileName.startsWith(
                                                    "input_source_name_mysql")
                                                    && Contants.SOURCE_TYPE_MYSQL == inms
                                                            .getSourceType())
                                            || (ititileName.startsWith(
                                                    "input_source_name_oracle")
                                                    && Contants.SOURCE_TYPE_ORACLE == inms
                                                            .getSourceType())
                                            || (ititileName.startsWith(
                                                    "input_source_name_sqlserver")
                                                    && Contants.SOURCE_TYPE_SQLSERVER == inms
                                                            .getSourceType()))
                                            && ((otitileName.startsWith(
                                                    "output_store_name_hbase")
                                                    && Contants.STORE_TYPE_HBASE == otms
                                                            .getStoreType())
                                                    || (otitileName.startsWith(
                                                            "output_store_name_hdfs")
                                                            && Contants.STORE_TYPE_HDFS == otms
                                                                    .getStoreType())
                                                    || (otitileName.startsWith(
                                                            "output_store_name_hive")
                                                            && Contants.STORE_TYPE_HIVE == otms
                                                                    .getStoreType())
                                                    || (otitileName.startsWith(
                                                            "output_store_name_mysql")
                                                            && Contants.STORE_TYPE_MYSQL == otms
                                                                    .getStoreType())
                                                    || (otitileName.startsWith(
                                                            "output_store_name_oracle")
                                                            && Contants.STORE_TYPE_ORACLE == otms
                                                                    .getStoreType())
                                                    || (otitileName.startsWith(
                                                            "output_store_name_sqlserver")
                                                            && Contants.STORE_TYPE_SQLSERVER == otms
                                                                    .getStoreType())
                                                    || (otitileName.startsWith(
                                                            "output_store_name_trafodion")
                                                            && Contants.STORE_TYPE_TRAFODION == otms
                                                                    .getStoreType())))
                                    {
                                        //判断操作权限
                                        User user = UserUtils.getUser();
                                        if (1 == user.getIsAdmin()
                                                || (taskService
                                                        .getDataPermission(
                                                                "source",
                                                                user.getId(),
                                                                inms.getId())
                                                        && taskService
                                                                .getDataPermission(
                                                                        "store",
                                                                        user.getId(),
                                                                        otms.getId())))
                                        {
                                            //继续封装默认参数
                                            funMap.put("input_ip",
                                                    inms.getRepoId().getIp());
                                            funMap.put("input_port",
                                                    String.valueOf(inms
                                                            .getRepoId()
                                                            .getPort()));
                                            funMap.put("input_dbpwd",
                                                    inms.getRepoId()
                                                            .getUserPwd());
                                            funMap.put("input_username",
                                                    inms.getRepoId()
                                                            .getUserName());
                                            funMap.put("input_delimiters",
                                                    (inms.getDelimiter()
                                                            .equals("\\t"))
                                                                    ? "\t"
                                                                    : inms.getDelimiter());
                                            funMap.put("input_db_dir_name",
                                                    inms.getRepoId()
                                                            .getRepoName());
                                            funMap.put("input_table_file_name",
                                                    inms.getSourceFile());
                                            funMap.put("output_ip",
                                                    otms.getRepoId().getIp());
                                            funMap.put("output_username",
                                                    otms.getRepoId()
                                                            .getUserName());
                                            funMap.put("output_pwd",
                                                    otms.getRepoId()
                                                            .getUserPwd());
                                            funMap.put("output_db_dir_name",
                                                    otms.getRepoId()
                                                            .getRepoName());
                                            funMap.put("output_table_file_name",
                                                    otms.getStoreFile());
                                            funMap.put("output_delimiters",
                                                    (otms.getDelimiter()
                                                            .equals("\\t"))
                                                                    ? "\t"
                                                                    : otms.getDelimiter());
                                            
                                            if (Contants.STORE_TYPE_HIVE == otms
                                                    .getStoreType())
                                            {
                                                funMap.put("output_port",
                                                        String.valueOf(
                                                                Global.getConfig(
                                                                        "hdfsPort")));
                                                funMap.put("hive_db_dir_name",
                                                        otms.getRepoId()
                                                                .getRepoName());
                                                funMap.put("hive_port",
                                                        String.valueOf(otms
                                                                .getRepoId()
                                                                .getPort()));
                                                funMap.put("hive_pwd",
                                                        otms.getRepoId()
                                                                .getUserPwd());
                                                funMap.put("hive_username",
                                                        otms.getRepoId()
                                                                .getUserName());
                                                funMap.put("hive_ip",
                                                        otms.getRepoId()
                                                                .getIp());
                                            }
                                            else
                                            {
                                                funMap.put("output_port",
                                                        String.valueOf(otms
                                                                .getRepoId()
                                                                .getPort()));
                                            }
                                            
                                            //hbase 封装liecu与mapname
                                            if (Contants.STORE_TYPE_HBASE == otms
                                                    .getStoreType())
                                            {
                                                //hbase表名加上了库名
                                                funMap.put(
                                                        "output_table_file_name",
                                                        otms.getRepoId()
                                                                .getRepoName()
                                                                + ":"
                                                                + otms.getStoreFile());
                                                
                                                String exrernal = otms
                                                        .getStoreExternal();
                                                
                                                if (!StringUtils
                                                        .isEmpty(exrernal))
                                                {
                                                    String[] els = exrernal
                                                            .split(";");
                                                    
                                                    if (1 == els.length)
                                                    {
                                                        funMap.put(
                                                                "output_column_family",
                                                                els[0]);
                                                        funMap.put(
                                                                "output_hbase_map",
                                                                "");
                                                    }
                                                    else if (2 == els.length)
                                                    {
                                                        funMap.put(
                                                                "output_column_family",
                                                                els[0]);
                                                        funMap.put(
                                                                "output_hbase_map",
                                                                els[1]);
                                                    }
                                                }
                                                else
                                                {
                                                    funMap.put(
                                                            "output_column_family",
                                                            "");
                                                    funMap.put(
                                                            "output_hbase_map",
                                                            "");
                                                }
                                            }
                                            
                                            if (Contants.SOURCE_TYPE_HBASE == inms
                                                    .getSourceType())
                                            {
                                                
                                                //hbase表名加上了库名
                                                funMap.put(
                                                        "input_table_file_name",
                                                        inms.getRepoId()
                                                                .getRepoName()
                                                                + ":"
                                                                + inms.getSourceFile());
                                                
                                                String exrernal = inms
                                                        .getExternal();
                                                
                                                if (!StringUtils
                                                        .isEmpty(exrernal))
                                                {
                                                    String[] els = exrernal
                                                            .split(";");
                                                    
                                                    if (1 == els.length)
                                                    {
                                                        funMap.put(
                                                                "input_column_family",
                                                                els[0]);
                                                        funMap.put(
                                                                "input_hbase_map",
                                                                "");
                                                    }
                                                    else if (2 == els.length)
                                                    {
                                                        funMap.put(
                                                                "input_column_family",
                                                                els[0]);
                                                        funMap.put(
                                                                "input_hbase_map",
                                                                els[1]);
                                                    }
                                                }
                                                else
                                                {
                                                    funMap.put(
                                                            "input_column_family",
                                                            "");
                                                    funMap.put(
                                                            "input_hbase_map",
                                                            "");
                                                }
                                            }
                                            
                                            //默认加上nameservices与输入字段
                                            funMap.put(
                                                    "input_hdfs_nameservices",
                                                    Global.getConfig(
                                                            "hdfs_nameservices"));
                                            funMap.put(
                                                    "output_hdfs_nameservices",
                                                    Global.getConfig(
                                                            "hdfs_nameservices"));
                                            
                                            List<MetaSourcePro> proList = inms
                                                    .getMetaSourceProList();
                                            if (null != proList
                                                    && !proList.isEmpty())
                                            {
                                                StringBuffer pros = new StringBuffer();
                                                String sql = "select ";
                                                
                                                if (Contants.SOURCE_TYPE_HIVE == inms
                                                        .getSourceType())
                                                {
                                                    
                                                    for (MetaSourcePro pro : proList)
                                                    {
                                                        pros.append(
                                                                pro.getProName()
                                                                        + ","
                                                                        + pro.getProType()
                                                                        + ","
                                                                        + pro.getDataFormat()
                                                                        + ";");
                                                        
                                                        sql += inms
                                                                .getSourceFile()
                                                                + ".`"
                                                                + pro.getProName()
                                                                + "`,";
                                                    }
                                                    sql = sql.substring(0,
                                                            sql.lastIndexOf(
                                                                    ","))
                                                            + " from "
                                                            + inms.getSourceFile();
                                                }
                                                else
                                                {
                                                	sql = "select ";
                                                    for (MetaSourcePro pro : proList)
                                                    {
                                                        pros.append(
                                                                pro.getProName()
                                                                        + ","
                                                                        + pro.getProType()
                                                                        + ","
                                                                        + pro.getDataFormat()
                                                                        + ";");
                                                        sql=sql + inms.getSourceFile()+".`"+pro.getProName() + "`,";
                                                    }
                                                	sql=sql.substring(0,sql.lastIndexOf(","))+" from " + inms.getSourceFile();
                                                	
                                                }
                                                if (inSqlName != null
                                                        && !inSqlName.equals("")
                                                        && inSqlValue
                                                                .equals(""))
                                                {
                                                    funMap.put("input_sql",
                                                            sql);
                                                }
                                                
                                                funMap.put("input_fields",
                                                        pros.substring(0,
                                                                pros.length()
                                                                        - 1));
                                            }
                                            
                                            //封装任务信息
                                            Task task = new Task();
                                            task.setTaskName(
                                                    "TASK_" + taskName);
                                            task.setTaskDesc(taskDesc);
                                            task.setTaskType(2);
                                            task.setCreateUser(UserUtils
                                                    .getUser().getLoginName());
                                            task.setGroupId(groupId);
                                            task.setComeFrom(1);
                                            taskService.insertTask(task);
                                            
                                            //函数
                                            task.setFunctionId(funId);
                                            task.setFunctionParam(JSONObject
                                                    .toJSONString(funMap));
                                            taskService
                                                    .insertTaskFunction(task);
                                            
                                            //输入
                                            HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("inputId",
                                                    Integer.valueOf(
                                                            inms.getId()));
                                            map.put("taskId", task.getTaskId());
                                            map.put("inputType", 0);
                                            taskService.insertTaskInput(map);
                                            
                                            //输出
                                            map = new HashMap<String, Object>();
                                            map.put("ouputId",
                                                    Integer.valueOf(
                                                            otms.getId()));
                                            map.put("taskId", task.getTaskId());
                                            map.put("outputType", 1);
                                            taskService.insertTaskOutput(map);
                                            
                                            successNum++;
                                        }
                                        else
                                        {
                                            failureMsg.put(taskName,
                                                    "没有操作输入或输出的权限");
                                            failureNum++;
                                        }
                                    }
                                    else
                                    {
                                        failureMsg.put(taskName,
                                                "输入或输出类型与函数不匹配");
                                        failureNum++;
                                    }
                                }
                                else
                                {
                                    failureMsg.put(taskName, "输入或输出不存在");
                                    failureNum++;
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        logger.error("导入任务异常：", e);
                        failureMsg.put(taskName, "系统异常");
                        failureNum++;
                        addMessage(redirectAttributes,
                                "导入任务失败！失败信息：" + e.getMessage());
                    }
                }
            }
            else
            {
                failureMsg.put(taskName, "函数不存在");
            }
        }
        catch (Exception e1)
        {
            logger.error("导入任务异常：", e1);
            addMessage(redirectAttributes, "导入任务失败！失败信息：" + e1.getMessage());
        }
        
        resultMsg.put("failureNum", failureNum);
        resultMsg.put("failureMsg", failureMsg);
        resultMsg.put("successNum", successNum);
        
        return JSONObject.toJSONString(resultMsg);
    }
    
    /**
     * 任务编辑任务组
     */
    @RequiresPermissions("tji:task:editgroup")
    @RequestMapping(value = "edit-group")
    public @ResponseBody String addGroup(int groupId, String taskIds,
            int taskFlag, RedirectAttributes redirectAttributes)
            throws Exception
    {
        
        //操作失败
        StringBuffer taskNames = new StringBuffer();
        
        String[] taskIdArr = taskIds.split(",");
        
        //批量修改任务的所属组
        if (null != taskIdArr && taskIdArr.length > 0)
        {
            for (String taskIdStr : taskIdArr)
            {
                if (0 == taskService.updateGroup(Integer.valueOf(taskIdStr),
                        groupId))
                {
                    taskNames.append(
                            taskService.get(taskIdStr).getTaskName() + ",");
                }
            }
        }
        
        //有失败的任务
        if (StringUtils.isNotEmpty(taskNames))
        {
            msg = taskNames + "操作失败！";
        }
        else
        {
            msg = "操作成功!";
        }
        
        return msg.toString();
    }
    
    /**
     * 显示导入界面
     * @return
     * @throws UnsupportedEncodingException 
     * @throws ServletException
     */
    @RequiresPermissions("tji:task:import")
    @RequestMapping(value = "show-import")
    public String showImpor()
    {
        return "modules/tji/etlTemplate";
    }
    
    /**
     * 显示工作流新增界面
     * @throws UnsupportedEncodingException 
     * @throws ServletException
     */
    @RequiresPermissions("tji:task:showact")
    @RequestMapping(value = "show-activiti")
    public String showActiviti()
    {
        return "modules/tji/addActiveForm";
    }
    
    /**
     * 查询任务，用于工作流设计界面查询
     * @param taskName
     * @param page
     * @param pageSize
     * @param userName
     * @param taskType
     * @return
     */
    @RequestMapping(value = "task-for-act")
    public @ResponseBody String getTaskForAct(
            @RequestParam(required = false) String jobName, int page,
            int pageSize, int taskType)
    {
        Object respData = null;
        Integer total = null;
        boolean success = false;
        String msg = null;
        String taskName = null == jobName ? "" : jobName;
        String userName = UserUtils.getPrincipal().getLoginName();
        
        try
        {
            
            //数据行
            respData = JSONArray.fromObject(taskService.getTaskForAct(taskName,
                    page,
                    pageSize,
                    userName,
                    taskType));
            
            //总数
            total = taskService.getTaskForActCount(taskName,
                    userName,
                    taskType);
            
            success = true;
            msg = "查询成功";
        }
        catch (Exception e)
        {
            log.error("getTaskForAct===", e);
            success = false;
            msg = "查询失败";
        }
        
        return respData(success, msg, respData, total, false);
    }
    
    /**
     * Kylin单点登录
     * @return
     * @throws UnsupportedEncodingException 
     * @throws ServletException
     */
    @RequiresPermissions("tji:task:kylin")
    @RequestMapping(value = "to-kylin")
    public void toKylin(Task task, HttpServletResponse response)
            throws Exception
    {
        //后台跳转到工作流
        String url = Global.getConfig("kylin_url") + "?token="
                + URLEncoder.encode(AESUtil.encForTD("ADMIN"), "utf-8");
        
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", url);
    }
    
    /**
     * 保存ETL任务
     */
    // @RequiresPermissions(value = "tji:task:add")
    @RequestMapping(value = "set-etl")
    public String getEtl(Task task, Model model) throws Exception
    {
        //编辑
        if (null != task.getTaskId())
        {
            TaskDetail td = taskService.getTaskDetail(task.getTaskId());
            TaskFunction tf = taskService
                    .findETFunctionByTaskId(task.getTaskId());
            if (td != null)
            {
                task.setTaskName(td.getTaskName());
                task.setTaskDesc(td.getTaskDesc());
                task.setGroupId(td.getGroupId());
            }
            if (tf != null)
            {
                String functionParam = tf.getFunctionParam();
                task.setFunctionId(tf.getFunctionId());
                task.setFunctionParam(functionParam);
                //从functionParam中提取出input_source_name_mysql的值,并根据该值获取外部数据源信息中proType为日期类型的属性
                if (functionParam != null)
                {
                    JSONObject jsonObject = JSONObject
                            .parseObject(functionParam);
                    Set<Entry<String, Object>> set = jsonObject.entrySet();
                    Iterator<Entry<String, Object>> iterator = set.iterator();//input_source_name
                    String value = null;
                    while (iterator.hasNext())
                    {
                        Entry<String, Object> entry = iterator.next();
                        if (entry.getKey().contains("input_source_name_"))
                        {
                            value = entry.getValue().toString();
                            break;
                        }
                    }
                    if (value != null)
                    {
                        List<MetaSourcePro> conditions = metaSourceService
                                .findSourceProBySourceName(value);
                        model.addAttribute("conditions",
                                new Gson().toJson(conditions));
                    }
                }
            }
            //密码解密
            String funcParam = handleFunctionParam(task.getFunctionParam(), 1);
            //由于页面在处理\t时进行了转义,导致\t变成了空白,所以在页面显示前需要对output_delimeter进行处理
            //先将正常的分隔符转成\t,在后面的转化过程中才能保证全部是正常的分隔符
            if (StringUtils.isNotEmpty(funcParam)
                    && funcParam.contains("\\\\t"))
            {
                funcParam = funcParam.replace("\\\\t", "\\t");
            }
            if (StringUtils.isNotEmpty(funcParam) && funcParam.contains("\\t"))
            {
                funcParam = funcParam.replace("\\t", "\\\\t");
            }
            task.setFunctionParam(funcParam);
            model.addAttribute("task", task);
            return "modules/tji/etlTaskForm";
        }
        else
        {
            
            task = new Task();
            task.setFunctionParam("add");
            model.addAttribute("task", task);
            return "modules/tji/etlTaskForm";
        }
    }
    
    private String handleFunctionParam(String funcParam, int type)
    {
        if (funcParam != null)
        {
            Map map = JSONObject.parseObject(funcParam, Map.class);
            String inpwd = map.get("input_dbpwd") == null ? null
                    : map.get("input_dbpwd").toString();
            String outpwd = map.get("output_pwd") == null ? null
                    : map.get("output_pwd").toString();
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
                            "\"output_pwd\":\"" + outpwd + "\"",
                            "\"output_pwd\":\"" + doutpwd + "\"");
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
                            "\"output_pwd\":\"" + outpwd + "\"",
                            "\"output_pwd\":\"" + doutpwd + "\"");
                }
            }
        }
        return funcParam;
    }
    
    @Autowired
    private FunctionParamService functionParamService;
    
    /**
     * 保存ETL任务
     */
    // @RequiresPermissions(value = "tji:task:add")
    @RequestMapping(value = "save-etl")
    public String setEtl(Task task, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        
        try
        {
            String functionParam = task.getFunctionParam();
            JSONObject jj = JSONObject.parseObject(functionParam);
            //处理etl模板任务输出分隔符被转义的问题
            //            Object outputDelimiters = jj.get("output_delimiters");
            //            if(outputDelimiters!=null && outputDelimiters.equals("\t")){
            //                jj.put("output_delimiters", "\\t");
            //            }
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
                task.setTaskName("TASK_" + task.getTaskName());
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
                            String sql = "select ";
                            if (Contants.SOURCE_TYPE_HIVE == ms.getSourceType())
                            {
                                for (MetaSourcePro pro : proList)
                                {
                                    pros.append(pro.getProName() + ","
                                            + pro.getProType() + ","
                                            + pro.getDataFormat() + ";");
                                    
                                    sql += ms.getSourceFile() + ".`"
                                            + pro.getProName() + "`,";
                                }
                                sql = sql.substring(0, sql.lastIndexOf(","))
                                        + " from " + ms.getSourceFile();
                            }
                            else
                            {
                                for (MetaSourcePro pro : proList)
                                {
                                    pros.append(pro.getProName() + ","
                                            + pro.getProType() + ","
                                            + pro.getDataFormat() + ";");
                                    
                                }
                                sql = "select * from " + ms.getSourceFile();
                            }
                            
                            if (("").equals(jj.getString("input_sql"))
                                    || null == jj.getString("input_sql"))
                            {
                                jj.put("input_sql", sql);
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
                            
                            String partition = "";
                            String hive_sql = "";
                            String tablename = ms.getStoreFile();
                            String hivehdfsdir = ms.getHdfsInfo().substring(0,
                                    ms.getHdfsInfo().lastIndexOf("/"));
                            
                            List<MetaStorePro> proList = ms
                                    .getMetaStoreProList();
                            if (null != proList && !proList.isEmpty())
                            {
                                for (MetaStorePro pro : proList)
                                {
                                    if (pro.getType() == 1)
                                    {
                                        if (pro.getProType()
                                                .equalsIgnoreCase("int")
                                                || pro.getProType()
                                                        .equalsIgnoreCase(
                                                                "integer"))
                                        {
                                            partition += pro.getProName() + "="
                                                    + pro.getDataFormat() + ",";
                                        }
                                        else
                                        {
                                            partition += pro.getProName() + "='"
                                                    + pro.getDataFormat()
                                                    + "',";
                                        }
                                        
                                    }
                                }
                                jj.put("output_table_file_name",
                                        tablename + "_temp");
                                
                                if (!"textfile".equals(ms.getStoreWay()))
                                {
                                    if (!partition.equals(""))
                                    {
                                        partition = partition.substring(0,
                                                partition.lastIndexOf(","));
                                        hive_sql = "insert into table "
                                                + tablename + " partition ("
                                                + partition + ") select * from "
                                                + tablename + "_temp"
                                                + ";drop table " + tablename
                                                + "_temp";
                                        jj.put("hive_sql", hive_sql);
                                    }
                                    else
                                    {
                                        hive_sql = "insert into table "
                                                + tablename + " select * from "
                                                + tablename + "_temp"
                                                + ";drop table " + tablename
                                                + "_temp";
                                        jj.put("hive_sql", hive_sql);
                                    }
                                    
                                } else {
                                    if (!partition.equals(""))
                                    {
                                        partition = partition.substring(0,
                                                partition.lastIndexOf(","));
                                        hive_sql = "load data inpath '"
                                                + hivehdfsdir + "_temp/"
                                                + tablename + "_temp"
                                                + "' into table " + tablename
                                                + " partition (" + partition
                                                + ");drop table " + tablename
                                                + "_temp";
                                        jj.put("hive_sql", hive_sql);
                                    }
                                    else
                                    {
                                        hive_sql = "load data inpath '"
                                                + hivehdfsdir + "_temp/"
                                                + tablename + "_temp"
                                                + "' into table " + tablename
                                                + ";drop table " + tablename
                                                + "_temp";
                                        jj.put("hive_sql", hive_sql);
                                    }
                                }
                            }
                            
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
                    //               System.out.println("TaskUitl.findAllMetaStore().size()="+TaskUitl.findAllMetaStore().size());
                    //               System.out.println("TaskUitl.findAllMetaSource().size()="+TaskUitl.findAllMetaSource().size());
                    MetaSource ms = metaSourceService.findUniqueByProperty(
                            "source_name", inputSourceName);
                    int sourceId;
                    if (null != ms)
                    {
                        List<MetaSourcePro> proList = ms.getMetaSourceProList();
                        if (null != proList && !proList.isEmpty())
                        {
                            StringBuffer pros = new StringBuffer();
                            String sql = "select ";
                            
                            if (Contants.SOURCE_TYPE_HIVE == ms.getSourceType())
                            {
                                for (MetaSourcePro pro : proList)
                                {
                                    pros.append(pro.getProName() + ","
                                            + pro.getProType() + ","
                                            + pro.getDataFormat() + ";");
                                    sql += ms.getSourceFile() + ".`"
                                            + pro.getProName() + "`,";
                                }
                                sql = sql.substring(0, sql.lastIndexOf(","))
                                        + " from " + ms.getSourceFile();
                            }
                            else
                            {
                                for (MetaSourcePro pro : proList)
                                {
                                    pros.append(pro.getProName() + ","
                                            + pro.getProType() + ","
                                            + pro.getDataFormat() + ";");
                                }
                                sql = "select * from " + ms.getSourceFile();
                            }
                            if (("").equals(jj.getString("input_sql"))
                                    || null == jj.getString("input_sql"))
                            {
                                jj.put("input_sql", sql);
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
                            
                            String partition = "";
                            String hive_sql = "";
                            String tablename = ms.getStoreFile();
                            String hivehdfsdir = ms.getHdfsInfo().substring(0,
                                    ms.getHdfsInfo().lastIndexOf("/"));
                            
                            List<MetaStorePro> proList = ms
                                    .getMetaStoreProList();
                            if (null != proList && !proList.isEmpty())
                            {
                                for (MetaStorePro pro : proList)
                                {
                                    if (pro.getType() == 1)
                                    {
                                        if (pro.getProType()
                                                .equalsIgnoreCase("int")
                                                || pro.getProType()
                                                        .equalsIgnoreCase(
                                                                "integer"))
                                        {
                                            partition += pro.getProName() + "="
                                                    + pro.getDataFormat() + ",";
                                        }
                                        else
                                        {
                                            partition += pro.getProName() + "='"
                                                    + pro.getDataFormat()
                                                    + "',";
                                        }
                                    }
                                }
                                jj.put("output_table_file_name",
                                        tablename + "_temp");
                                if (!ms.getStoreWay().equals("textfile"))
                                {
                                    if (!partition.equals(""))
                                    {
                                        partition = partition.substring(0,
                                                partition.lastIndexOf(","));
                                        hive_sql = "insert into table "
                                                + tablename + " partition ("
                                                + partition + ") select * from "
                                                + tablename + "_temp"
                                                + ";drop table " + tablename
                                                + "_temp";
                                        jj.put("hive_sql", hive_sql);
                                    }
                                    else
                                    {
                                        hive_sql = "insert into table "
                                                + tablename + " select * from "
                                                + tablename + "_temp"
                                                + ";drop table " + tablename
                                                + "_temp";
                                        jj.put("hive_sql", hive_sql);
                                    }
                                }
                                else
                                {
                                    if (!partition.equals(""))
                                    {
                                        partition = partition.substring(0,
                                                partition.lastIndexOf(","));
                                        hive_sql = "load data inpath '"
                                                + hivehdfsdir + "_temp/"
                                                + tablename + "_temp"
                                                + "' into table " + tablename
                                                + " partition (" + partition
                                                + ");drop table " + tablename
                                                + "_temp";
                                        jj.put("hive_sql", hive_sql);
                                    }
                                    else
                                    {
                                        hive_sql = "load data inpath '"
                                                + hivehdfsdir + "_temp/"
                                                + tablename + "_temp"
                                                + "' into table " + tablename
                                                + ";drop table " + tablename
                                                + "_temp";
                                        jj.put("hive_sql", hive_sql);
                                    }
                                }
                            }
                            
                            task.setFunctionParam(jj.toString());
                        }
                        
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
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
        return "redirect:" + Global.getAdminPath()
                + "/tji/task/?repage&taskFlag=1";
    }
    
    ///tji/task/find-function-Param
    @RequestMapping(value = "find-function-Param")
    @ResponseBody
    public JSONObject extractTables(String funId)
    {
        JSONObject rstMap = new JSONObject();
        /// Map<String,Object> rstMap = new HashMap<String,Object>();
        try
        {
            List<HashMap<String, Object>> listFunctionParam = functionParamService
                    .findParamsByFunctionId(funId);
            rstMap.put("success", true);
            rstMap.put("msg", "成功");
            rstMap.put("data", listFunctionParam);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            rstMap.put("success", false);
            rstMap.put("msg", "失败");
        }
        return rstMap;
    }
    
    ///tji/task/find-data
    @RequestMapping(value = "find-data")
    @ResponseBody
    public JSONObject findSourceOrStore(String paramName)
    {
        JSONObject data = new JSONObject();
        try
        {
            List<HashMap<String, Object>> metaData = Lists.newArrayList();
            if (paramName != null)
            {
                if (paramName.startsWith("input_source_name_mysql"))
                {
                    metaData = metaSourceService
                            .findAllMetaSource(new Integer(6));
                }
                else if (paramName.startsWith("input_source_name_sqlserver"))
                {
                    metaData = metaSourceService
                            .findAllMetaSource(new Integer(10));
                }
                else if (paramName.startsWith("input_source_name_hive"))
                {
                    metaData = metaSourceService
                            .findAllMetaSource(new Integer(5));
                }
                else if (paramName.startsWith("input_source_name_ftp"))
                {
                    metaData = metaSourceService
                            .findAllMetaSource(new Integer(2));
                }
                else if (paramName.startsWith("input_source_name_oracle"))
                {
                    metaData = metaSourceService
                            .findAllMetaSource(new Integer(7));
                }
                else if (paramName.startsWith("input_source_name_hbase"))
                {
                    metaData = metaSourceService
                            .findAllMetaSource(new Integer(4));
                }
                else if (paramName.startsWith("input_source_name_hdfs"))
                {
                    metaData = metaSourceService
                            .findAllMetaSource(new Integer(3));
                }
                else if (paramName.startsWith("output_store_name_hive"))
                {
                    metaData = metaStoreService
                            .findAllMetaStore(new Integer(5));
                    String port = Global.getConfig("hdfsPort");
                    for (HashMap<String, Object> store : metaData)
                    {
                        store.put("port", port);//hive对应的hdfs端口
                    }
                }
                else if (paramName.startsWith("output_store_name_hdfs"))
                {
                    metaData = metaStoreService
                            .findAllMetaStore(new Integer(3));
                }
                else if (paramName.startsWith("output_store_name_hbase"))
                {
                    metaData = metaStoreService
                            .findAllMetaStore(new Integer(4));
                }
                else if (paramName.startsWith("output_store_name_mysql"))
                {
                    metaData = metaStoreService
                            .findAllMetaStore(new Integer(6));
                }
                else if (paramName.startsWith("output_store_name_oracle"))
                {
                    metaData = metaStoreService
                            .findAllMetaStore(new Integer(7));
                }
                else if (paramName.startsWith("output_store_name_sqlserver"))
                {
                    metaData = metaStoreService
                            .findAllMetaStore(new Integer(10));
                }
                else if (paramName.startsWith("output_store_name_trafodion"))
                {
                    metaData = metaStoreService
                            .findAllMetaStore(new Integer(11));
                }
                //对数据源中的密码进行解密
                if(metaData!=null && metaData.size()>0){
                	for (HashMap<String, Object> hashMap : metaData) {
                		String userPwd = hashMap.get("userPwd")==null?null:String.valueOf(hashMap.get("userPwd"));
                		if (userPwd!=null && userPwd.endsWith("==")){
                			String dinpwd = AESUtil.decForTD(userPwd);
                			hashMap.put("userPwd", dinpwd);
                		}
					}
                }
                data.put("success", true);
                data.put("data", metaData);
                data.put("msg", "成功");
            }
            else
            {
                data.put("success", false);
                data.put("msg", "参数名不能为空！");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            data.put("success", false);
            data.put("msg", "查询失败！" + e.getMessage());
            data.put("data", null);
        }
        return data;
    }
    
    /**
     * Get detail information of the "Cube ID"
     * 
     * @param cubeDescName
     *            Cube ID
     * @return
     * @throws IOException
     */
    private String getKylinCubeInfo(String cubeDescName) throws IOException
    {
        String baseUrl = Global.getConfig("kylin_base_api_url") + "cube_desc/"
                + cubeDescName + "/desc";
        String result = KylinHttpRequest.httpGet(baseUrl, "GET");
        if (result.contains("{"))
        {
            result = result.replace("last_modified", "lastModified");
            result = result.replace("model_name", "modelName");
            result = result.replace("null_string", "nullString");
            result = result.replace("next_parameter", "nextParameter");
            result = result.replace("dependent_measure_ref",
                    "dependentMeasureRef");
            result = result.replace("rowkey_columns", "rowkeyColumns");
            result = result.replace("hbase_mapping", "hbaseMapping");
            result = result.replace("measure_refs", "measureRefs");
            result = result.replace("aggregation_groups", "aggregationGroups");
            result = result.replace("select_rule", "selectRule");
            result = result.replace("joint_dims", "jointDims");
            result = result.replace("notify_list", "notifyList");
            result = result.replace("status_need_notify", "statusNeedNotify");
            result = result.replace("partition_date_start",
                    "partitionDateStart");
            result = result.replace("partition_date_end", "partitionDateEnd");
            result = result.replace("auto_merge_time_ranges",
                    "auto_mergeTimeRanges");
            result = result.replace("retention_range", "retentionRange");
            result = result.replace("engine_type", "engineType");
            result = result.replace("storage_type", "storageType");
            result = result.replace("override_kylin_properties",
                    "overrideKylinProps");
        }
        return result;
    }
    
    public String getKylinJobInfo(String cubeDescName) throws IOException
    {
        String baseUrl = Global.getConfig("kylin_base_api_url") + "cubes/"
                + cubeDescName;
        return KylinHttpRequest.httpGet(baseUrl, "GET");
    }
    
    /**
     * 保存cube（update/add）
     * @param cubeRequest
     * @param method
     * @return
     * @throws IOException
     */
    private String saveKylinCube(CubeRequest cubeRequest, String method)
            throws IOException
    {
        String baseUrl = Global.getConfig("kylin_base_api_url") + "cubes";
        Map<String, Object> mp = new HashMap<String, Object>();
        String cubeDescData = StringEscapeUtils
                .unescapeHtml(cubeRequest.getCubeDescData());
        if (method.equals("POST"))
        {
            cubeDescData = cubeDescData.replace(
                    "\"name\":\"" + cubeRequest.getCubeName() + "\"",
                    "\"name\":\"TASK_" + cubeRequest.getCubeName() + "\"");
        }
        //        CubeDesc cubeDesc = JSONObject.parseObject(cubeDescData, CubeDesc.class);
        cubeDescData = cubeDescData.replace("\"status_need_notify\":[]",
                "\"status_need_notify\":[\"ERROR\",\"DISCARDED\",\"SUCCEED\"]");
        mp.put("cubeDescData", cubeDescData);
        mp.put("cubeName", cubeRequest.getCubeName());
        mp.put("project", cubeRequest.getProject());
        mp.put("uuid", cubeRequest.getUuid());
        String params = JSONObject.toJSONString(mp);
        String result = KylinHttpRequest.httpPost(params, baseUrl, method);
        return result;
    }
    
    /**
     * drop cube删除cube
     * @param cubeDescName
     * @return
     * @throws Exception
     */
    private String delKylinCube(String cubeDescName) throws Exception
    {
        String baseUrl = Global.getConfig("kylin_base_api_url") + "cubes/"
                + cubeDescName;
        String result = KylinHttpRequest.httpGet(baseUrl, "DELETE");
        if (result.contains("{"))
        {
            Map<String, String> map = JSONObject.parseObject(result, Map.class);
            result = map.get("message");
        }
        return result;
    }
    
    /**
     * 麒麟新增多维数据集时,生成e_task表记录信息
     * @param task 任务信息
     * @return 结果
     */
    @RequestMapping(value = "/kylin-save-task", method = RequestMethod.GET)
    public String kylinSaveTask(Task task)
    {
        task.setGroupId(2);
        taskService.save(task);
        return null;
    }
    
    /**
     * 添加cube的dimension页面
     * @param name
     * @param tableName
     * @param columnName
     * @param model
     * @return
     */
    @RequestMapping(value = "kylinDimension")
    public String kylinDimension(String name, String tableName,
            String columnName, String dimensionName, Model model)
    {
        model.addAttribute("name", name);
        model.addAttribute("tableName", tableName);
        model.addAttribute("columnName", columnName);
        model.addAttribute("dimensionName", dimensionName);
        return "modules/tji/taskconfigure/kylinDimension";
    }
    
    /**
     * 添加cube的measure页面
     * @param name
     * @param expression
     * @param paramType
     * @param paramValue
     * @param returnType
     * @param paramTypelist
     * @param model
     * @return
     */
    @RequestMapping(value = "kylinMeasure")
    public String kylinMeasure(String name, String expression, String paramType,
            String paramValue, String returnType, String paramTypelist,
            Model model)
    {
        model.addAttribute("name", name);
        model.addAttribute("expression", expression);
        model.addAttribute("paramType", paramType);
        model.addAttribute("paramValue", paramValue);
        model.addAttribute("returnType", returnType);
        model.addAttribute("paramTypelist", paramTypelist);
        return "modules/tji/taskconfigure/kylinMeasure";
    }
    
    /**
     * 封装返回json
     * @param success
     * @param msg
     * @param respData
     * @param total
     * @param expried
     * @return
     */
    private String respData(boolean success, String msg, Object respData,
            Integer total, boolean expried)
    {
        JSONObject data = new JSONObject();
        data.put("success", success);
        data.put("msg", msg);
        data.put("data", respData);
        data.put("sessionExpired", expried);
        if (total != null)
        {
            data.put("total", total);
        }
        
        return data.toString();
    }
}