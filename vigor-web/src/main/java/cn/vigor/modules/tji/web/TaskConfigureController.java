package cn.vigor.modules.tji.web;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.jars.entity.Function;
import cn.vigor.modules.jars.entity.FunctionParam;
import cn.vigor.modules.jars.service.FunctionParamService;
import cn.vigor.modules.jars.service.FunctionService;
import cn.vigor.modules.meta.entity.MetaRepo;
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
import cn.vigor.modules.tji.bean.InputDsfRelations;
import cn.vigor.modules.tji.entity.FunctionParamBean;
import cn.vigor.modules.tji.entity.TaskDetail;
import cn.vigor.modules.tji.entity.TaskDetailBean;
import cn.vigor.modules.tji.entity.TaskGroup;
import cn.vigor.modules.tji.service.TaskGroupService;
import cn.vigor.modules.tji.service.TasksService;
import cn.vigor.modules.tji.util.XmlDataUtil;
import cn.vigor.modules.tji.util.XmlSAX;
/**
 * 任务配置接口Controller
 * @author huzeyuan-38342
 * @version v1.0
 * @date 2016-12-27
 *
 */
@Controller
@RequestMapping(value="${adminPath}/tji/taskconfigure")
public class TaskConfigureController extends BaseController{
    
    @Autowired
    private TasksService taskService;
    @Autowired
    private MetaRepoService metaRepoService;
    @Autowired
    private FunctionService functionService;
    @Autowired
    private TaskGroupService taskGroupService;
    @Autowired
    private MetaStoreService metaStoreService;
    @Autowired
    private FunctionParamService functionParamService;
    @Autowired
    private MetaResultService metaResultService;
    @Autowired
    private MetaSourceService metaSourceService;
    
    @ModelAttribute
    public TaskDetailBean get(@RequestParam(required = false) Integer id) throws Exception
    {
        TaskDetailBean entity = null;
        if (id!=null && id!=0)
        {
            TaskDetail detail = taskService.getTaskDetail(id);
            MyBeanUtils.copyBeanNotNull2Bean(detail, entity);
        }
        if (entity == null)
        {
            entity = new TaskDetailBean();
        }
        return entity;
    }
    
    /**
     * 添加自定义mr,spark任务
     */
    @RequestMapping(value = "/addCustomTask",method = RequestMethod.GET)
    public String addCustomTask(TaskDetailBean taskDetail,Model model) throws Exception{
        taskDetail = getBaseData(taskDetail);
        //默认获取资源库列表的第一条数据,并将资源库对应的数据源返回给页面
        List<MetaRepo> list = taskDetail.getInputResources();
        if(list!=null && list.size()>0){
            MetaRepo metaRepo = list.get(0);
            MetaStore metaStore = new MetaStore();
            metaStore.setRepoId(metaRepo);
            List<MetaStore> metaStores = metaStoreService.findList(metaStore);
            taskDetail.setMetaStores(metaStores);
        }
        model.addAttribute("data", taskDetail);
        return "modules/tji/taskconfigure/customComputeTaskForm";
    }
    
    /**
     * 获取资源库列表
     * @param detailBean
     * @return
     */
    @RequestMapping(value="/getResourceBases",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getResourceBases(Integer repoType,Integer metaType,TaskDetailBean detailBean){
        JSONObject jsonObject = new JSONObject();
        List<Integer> repoTypes = null;
        List<Integer> metaTypes = null;
        if(repoType!=null){
            repoTypes = Arrays.asList(repoType);
        }else{
            repoTypes = Arrays.asList(new Integer[]{2,3});
        }
        if(metaType!=null){
            metaTypes = Arrays.asList(metaType==0?2:metaType);
        }else{
            metaTypes = Arrays.asList(new Integer[]{1});
        }
        User user = UserUtils.getUser();
        List<MetaRepo> list = metaRepoService.getHdfsAndFtpSources(metaTypes,repoTypes ,user.getIsAdmin(),user);
        jsonObject.put("data", list);
        return jsonObject;
    }
    
    /**
     * 根据资源库查询数据源列表
     * @param resourceName 资源库id
     * @return 结果
     */
    @RequestMapping(value="/getDataSourceByResource",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getDataSourceByDatabase(@RequestParam(value="resourceId",required=true)String resourceId,
            @RequestParam(value="metaType",defaultValue="1")Integer metaType){
        JSONObject jsonObject = new JSONObject();
        try
        {
            MetaRepo metaRepo = new MetaRepo();
            metaRepo.setId(resourceId);
            if(metaType==1){
                List<MetaStore> reMetaStores = null;
                MetaStore metaStore = new MetaStore();
                metaStore.setRepoId(metaRepo);
                List<MetaStore> metaStores = metaStoreService.findList(metaStore);
                if(metaStores!=null  && metaStores.size()>0){
                    reMetaStores = new ArrayList<MetaStore>();
                    for (MetaStore metaStore2 : metaStores)
                    {
                        List<MetaStorePro> list = metaStoreService.getMetaStorePros(Arrays.asList(Integer.valueOf(metaStore2.getId())));
                        if(list!=null && list.size()>0){
                            metaStore2.setMetaStoreProList(list);
                            reMetaStores.add(metaStore2);
                        }
                    }
                }
                jsonObject.put("data", reMetaStores);
            }else if(metaType==0){
                MetaSource metaSource = new MetaSource();
                metaSource.setRepoId(metaRepo);
                List<MetaSource> metaSources = metaSourceService.findList(metaSource);
                if(metaSources!=null && metaSources.size()>0){
                    for (MetaSource ms : metaSources){
                        List<MetaSourcePro> metaSourcePros = metaSourceService.findSourceProBySourceName(ms.getSourceName());
                        ms.setMetaSourceProList(metaSourcePros);
                    }
                }
                jsonObject.put("data", metaSources);
            }
            jsonObject.put("success", true);
            jsonObject.put("msg", "成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "失败");
        }
        return jsonObject;
    }
    
    /**
     * 根据输出资源库id,输出类型,结果类型获取输出
     * @param outputSourceName
     * @param typeId 2 ftp 3 htfs 4 hbase 11 X-OLTP
     * @param resultType
     * @return
     */
    @RequestMapping(value="/getOutputs",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getOutputs(@RequestParam(value="outputSourceId",required=true)String outputSourceId,
            @RequestParam(value="typeId",required=true)Integer typeId,
            @RequestParam(value="resultType",required=true)Integer resultType){
        JSONObject jsonObject = new JSONObject();
        try
        {
            User user = UserUtils.getUser();
            List<Map<String,Object>> list = null;
            if(resultType==1){
                list = metaStoreService.getMetaStoreByType(outputSourceId, Arrays.asList(typeId),user);
            }else if(resultType==0){
                list = metaResultService.getMetaResultByType(outputSourceId, Arrays.asList(typeId),user);
            }else{//flume计算任务
                list = metaStoreService.getMetaStoreByType(outputSourceId, Arrays.asList(typeId), user);
            }
            jsonObject.put("data", list);
            jsonObject.put("success", true);
            jsonObject.put("msg", "成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jsonObject.put("success", true);
            jsonObject.put("msg", "失敗");
        }
        return jsonObject;
    }
    
    /**
     * 添加自定义mr,spark任务的基础数据
     * @param detailBean
     * @return
     */
    private TaskDetailBean getBaseData(TaskDetailBean detailBean){
        //获取添加自定义mr,spark任务时的资源库列表(metaType=1,repoType={2,3})
        User user = UserUtils.getUser();
        List<MetaRepo> list = metaRepoService.getHdfsAndFtpSources(Arrays.asList(new Integer[]{1}), Arrays.asList(new Integer[]{2,3}),user.getIsAdmin(),user);
        detailBean.setInputResources(list);
        //获取输出资源库
        List<MetaRepo> outputSources = metaRepoService.getHdfsAndFtpSources(Arrays.asList(new Integer[]{1,2}), Arrays.asList(new Integer[]{2,3}),user.getIsAdmin(),user);
        detailBean.setOutputResources(outputSources);
        //根据组的类型获取分组(这里获取计算任务的分组,即groupType=2)
        List<TaskGroup> groups = taskGroupService.getGroupInfoByType(2);
        detailBean.setTaskGroups(groups);
        //函数类型列表
        List<Map<String, String>> functionTypes = new ArrayList<Map<String, String>>();
        Map<String,String> map = new HashMap<String, String>();
        map.put("1", "spark函数");
        functionTypes.add(map);
        Map<String,String> map1 = new HashMap<String, String>();
        map1.put("4", "MR函数");
        functionTypes.add(map1);
        detailBean.setFunctionTypes(functionTypes);
        return detailBean;
    }
    
    /**
     * 根据函数类型查询函数列表
     * @param functionType 函数类型
     * 0 全部
     * 1 spark函数
     * 2 hive函数
     * 4 MapReduce函数
     * 6 数据质量函数
     * 7 X-Tbase函数
     * 8 存储过程
     * 9 shell脚本
     * 11 etl模板函数
     * @return
     */
    @RequestMapping(value="/getFuncionByType",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getFuncionByType(@RequestParam(value="functionType",required=true)Integer functionType){
        JSONObject rstMap = new JSONObject();
        try
        {
            Function function = new Function();
            function.setFunctionType(functionType);
            List<Function> list = functionService.findList(function);
            //如果函数列表不为空,则将默认第一条函数对应的函数参数返回给页面
            if(list!=null && list.size()>0){
                for (Function fc : list)
                {
                    List<HashMap<String, Object>> params = functionParamService.getParamsByFunctionId(fc.getId());
                    fc.setFunctionParamMaps(params);
                }
            }
            rstMap.put("success", true);
            rstMap.put("msg", "成功");
            rstMap.put("data", list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            rstMap.put("success", false);
            rstMap.put("msg", "失败");
        }
        return rstMap;
    }
    
    /**
     * 根据函数id获取函数参数
     * @param functionId 函数Id
     * @return 结果
     */
    @RequestMapping(value="getFunctionParams/{functionId}",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getFunctionParams(@PathVariable("functionId")String functionId){
        JSONObject rstMap = new JSONObject();
        try
        {
            List<HashMap<String, Object>> params = functionParamService.getParamsByFunctionId(functionId);
            rstMap.put("data", params);
            rstMap.put("msg", "成功");
            rstMap.put("success", true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            rstMap.put("msg", "失败");
            rstMap.put("success", false);
        }
        return rstMap;
    }
    
    /**
     * 编辑自定义mr,spark任务
     * @param taskDetail
     * @param model
     * @return
     */
    @RequestMapping(value="/editCustomTask/{taskId}",method = RequestMethod.GET)
    public String editCustomTask(@PathVariable("taskId")String taskId,TaskDetailBean taskDetail,
            Model model){
        try
        {
            TaskDetail detail = taskService.getTaskDetail(Integer.valueOf(taskId));
            if(detail!=null){
            MyBeanUtils.copyBeanNotNull2Bean(detail, taskDetail);
            taskDetail = getBaseData(taskDetail);
            //解析xmldata字段,获取部分参数
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            saxParser.parse(new InputSource(new StringReader(taskDetail.getXmlData())), new XmlSAX("arg"));
            String repoId = XmlSAX.map.get("repoId");
            String outRepoId = XmlSAX.map.get("outRepoId");
            String outputString = XmlSAX.map.get("outputString");
            String storeId = XmlSAX.map.get("storeId");
            String storagType = XmlSAX.map.get("storagType");
            String outputId = XmlSAX.map.get("outputId");
            //输入资源库id
            taskDetail.setInputResourceId(repoId);
            //输入数据源id
            taskDetail.setInputDataSourceId(storeId);
            //输出资源库id
            taskDetail.setOutputResourceId(outRepoId);
            //输出类型
            taskDetail.setOutputType(outputString);
            if(outputString.equals("FTP")){
                taskDetail.setOutputType("2");
            }else if(outputString.equals("HDFS")){
                taskDetail.setOutputType("3");
            }
            //结果类型
            taskDetail.setResultType(storagType);
            //函数类型
            String functionType = XmlSAX.mrAttrs.get("functionType");
            if(functionType!=null && functionType.equals("MR")){
                taskDetail.setFunctionType("4");
            }else if(functionType!=null && functionType.equals("SPARK")){
                taskDetail.setFunctionType("1");
            }else{
                taskDetail.setFunctionType(functionType);
            }
            taskDetail.setOutputDataSourceId(outputId);
            //函数ID
            taskDetail.setFunctionId(Integer.valueOf(XmlSAX.mrAttrs.get("functionId")));
            List<HashMap<String, String>> functionParams = XmlSAX.mrlist;
            List<FunctionParamBean> beans = null;
            if(functionParams!=null){
                beans = new ArrayList<FunctionParamBean>();
                for (HashMap<String, String> hashMap : functionParams)
                {
                    //如果index和name为null,则说明该记录是空的,忽略
                    if(hashMap.get("index").equals("null"))
                        continue;
                    FunctionParamBean bean = new FunctionParamBean();
                    bean.setParamIndex(Integer.valueOf(hashMap.get("index")));
                    bean.setParamName(hashMap.get("name"));
                    bean.setValue(hashMap.get("value"));
                    bean.setParamType(hashMap.get("paramType"));
                    bean.setParamSelect(Integer.valueOf(hashMap.get("paramSelect")));
                    beans.add(bean);
                }
            }
            taskDetail.setFunctionParams(beans);
            model.addAttribute("data", taskDetail);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "modules/tji/taskconfigure/customComputeTaskForm";
    }
    
    /**
     * 保存配置任务(自定义mr,spark,数据质量,shell脚本,存储过程)
     * @param taskDetail 任务详情
     * @param model
     * @return
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveCustomTask(TaskDetailBean taskDetail,
            RedirectAttributes redirectAttributes) throws Exception{
        JSONObject jsonObject = new JSONObject();
        try
        {
            if(taskDetail.getTaskType()==null){
                int taskType = 0;
                switch (taskDetail.getFunctionType())
                {
                    case "1":
                        taskType = 5;
                        break;
                    case "4":
                        taskType = 6;
                        break;
                    case "6":
                        taskType = 7;
                        break;
                    case "8":
                        taskType = 8;
                        break;
                    case "9":
                        taskType = 11;
                        break;
                    default:
                        break;
                }
                taskDetail.setTaskType(taskType);
            }
            if(taskDetail.getTaskId()!=null && taskDetail.getTaskId()!=0){//编辑
                //根据taskDetail生成xmldata字段信息(无论新增还是修改,都对xmldata进行更新)
                String xmldata = XmlDataUtil.getCustomComXmlData(taskDetail);
                taskDetail.setXmlData(xmldata);
                taskService.editConfigureTaskInfo(taskDetail);
            }else{//新增
                taskService.saveConfigureTaskInfo(taskDetail);
            }
            jsonObject.put("success", true);
            jsonObject.put("msg", "保存成功");
        }
        catch (Exception e)
        {
            jsonObject.put("success", false);
            jsonObject.put("msg", "保存失败");
            e.printStackTrace();
        }
        return jsonObject;
    }
    
    /**
     * 添加数据质量任务
     * @param taskDetail
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addDataCheckTask",method = RequestMethod.GET)
    public String addDataCheckTask(TaskDetailBean taskDetailBean,Model model) throws Exception{
        taskDetailBean = getBaseData(taskDetailBean);
        model.addAttribute("data", taskDetailBean);
        Function function = new Function();
        //数据质量任务
        function.setFunctionType(6);
        List<Function> functions = functionService.findList(function);
        if(functions!=null && functions.size()>0){
            for (Function f : functions)
            {
                List<FunctionParam> functionParamList = null;
                List<HashMap<String, Object>> list = functionParamService.findParamsByFunctionId(f.getId());
                if(list!=null && list.size()>0){
                    functionParamList = new ArrayList<FunctionParam>();
                    for (HashMap<String, Object> hashMap : list)
                    {
                        FunctionParam param = new FunctionParam();
                        param.setFunctionId(f);
                        if(hashMap.get("paramIndex")!=null){
                            param.setParamIndex(Integer.valueOf(hashMap.get("paramIndex").toString()));
                        }
                        if(hashMap.get("paramName")!=null){
                            param.setParamName(hashMap.get("paramName").toString());
                        }
                        if(hashMap.get("paramDesc")!=null){
                            param.setParamDesc(hashMap.get("paramDesc").toString());
                        }
                        if(hashMap.get("id")!=null){
                            param.setParamId(hashMap.get("id").toString());
                        }
                        if(hashMap.get("paramType")!=null){
                            param.setParamType(hashMap.get("paramType").toString());
                        }
                        if(hashMap.get("delFlag")!=null){
                            param.setDelFlag(hashMap.get("delFlag").toString());
                        }
                        if(hashMap.get("paramSelect")!=null){
                            param.setParamSelect(Integer.valueOf(hashMap.get("paramSelect").toString()));
                        }
                        functionParamList.add(param);
                    }
                }
                f.setFunctionParamList(functionParamList);
            }
        }
        model.addAttribute("functions", functions);
        return "modules/tji/taskconfigure/dataCheckTaskForm";
    }
    
    /**
     * 编辑数据质量任务
     * @param taskDetailBean 任务详情
     * @param model
     * @return 结果
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    @RequestMapping(value="/editDataCheckTask/{taskId}",method = RequestMethod.GET)
    public String editDataCheckTask(@PathVariable("taskId")Integer taskId
            ,Model model) throws Exception{
        TaskDetail detail = taskService.getTaskDetail(taskId);
        if(detail!=null){
            TaskDetailBean detailBean = new TaskDetailBean();
            MyBeanUtils.copyBeanNotNull2Bean(detail, detailBean);
            detailBean = getBaseData(detailBean);
            //解析xmldata字段,获取部分参数
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            saxParser.parse(new InputSource(new StringReader(detailBean.getXmlData())), new XmlSAX("arg"));
            String repoId = XmlSAX.map.get("repoId");
            String outRepoId = XmlSAX.map.get("outRepoId");
            String outputString = XmlSAX.map.get("outputString");
            String storeId = XmlSAX.map.get("storeId");
            String storagType = XmlSAX.map.get("storagType");
            String outputId = XmlSAX.map.get("outputId");
            //输入资源库id
            detailBean.setInputResourceId(repoId);
            //输入数据源id
            detailBean.setInputDataSourceId(storeId);
            //输出资源库id
            detailBean.setOutputResourceId(outRepoId);
            //输出类型
            if(outputString.equals("FTP")){
                detailBean.setOutputType("2");
            }else if(outputString.equals("HDFS")){
                detailBean.setOutputType("3");
            }
            //结果类型
            detailBean.setResultType(storagType);
            //函数类型
            detailBean.setFunctionType(XmlSAX.dqsAttrs.get("functionType"));
            detailBean.setOutputDataSourceId(outputId);
            //函数ID
            detailBean.setFunctionId(Integer.valueOf(XmlSAX.dqsAttrs.get("functionId")));
            List<HashMap<String, String>> functionParams = XmlSAX.dqslist;
            List<FunctionParamBean> beans = null;
            if(functionParams!=null){
                beans = new ArrayList<FunctionParamBean>();
                for (HashMap<String, String> hashMap : functionParams)
                {
                    FunctionParamBean bean = new FunctionParamBean();
                    bean.setParamIndex(Integer.valueOf(hashMap.get("index")));
                    bean.setParamName(hashMap.get("name"));
                    bean.setValue(hashMap.get("value"));
                    beans.add(bean);
                }
            }
            detailBean.setFunctionParams(beans);
            List<HashMap<String, String>> list = XmlSAX.dqmlist;
            if(list!=null){
                List<InputDsfRelations> ls = new ArrayList<InputDsfRelations>();
                for (HashMap<String, String> hashMap : list)
                {
                    InputDsfRelations relations = new InputDsfRelations();
                    relations.setDataSourceId(hashMap.get("sourceId"));
                    relations.setDataSourceName(hashMap.get("sourceName"));
                    relations.setDataSourceProFormat(hashMap.get("fieldFormat"));
                    MetaStorePro metaStorePro = metaStoreService.getByStoreIdAndProName(hashMap.get("sourceId"), hashMap.get("fieldName"));
                    relations.setDataSourceProId(metaStorePro.getId());
                    relations.setDataSourceProIndex(Integer.valueOf(hashMap.get("fieldIndex")));
                    relations.setDataSourceProName(hashMap.get("fieldName"));
                    relations.setDataSourceProType(hashMap.get("fieldType"));
                    relations.setFunctionClass(hashMap.get("functionClass"));
                    relations.setFunctionName(hashMap.get("functionName"));
                    relations.setFunctionJarPath(hashMap.get("functionJarPath"));
                    relations.setFunctionJarName(hashMap.get("functionJarName"));
                    relations.setFunctionId(hashMap.get("functionId"));
                    relations.setFunctionExpr(hashMap.get("functionExpr"));
                    ls.add(relations);
                }
                detailBean.setInputDsfRelations(ls);
            }
            model.addAttribute("data", detailBean);
        }
        return "modules/tji/taskconfigure/dataCheckTaskForm";
    }
    
    
    /**
     * 添加脚本任务
     * @param taskDetail
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addScriptTask",method = RequestMethod.GET)
    public String addScriptTask(TaskDetailBean taskDetail,Model model) throws Exception{
        List<TaskGroup> groups = taskGroupService.getGroupInfoByType(2);
        taskDetail.setTaskGroups(groups);
        model.addAttribute("data", taskDetail);
        return "modules/tji/taskconfigure/scriptTaskForm";
    }
    
    /**
     * 编辑脚本任务
     * @param taskId 任务id
     * @param taskDetail
     * @param model
     * @return
     */
    @RequestMapping(value = "/editScriptTask/{taskId}",method = RequestMethod.GET)
    public String editScriptTask(@PathVariable("taskId")Integer taskId
            ,Model model) throws Exception{
        TaskDetail taskDetail = taskService.getTaskDetail(taskId);
        if(taskDetail!=null){
            TaskDetailBean detailBean = new TaskDetailBean();
            MyBeanUtils.copyBeanNotNull2Bean(taskDetail, detailBean);
            //根据组的类型获取分组(这里获取计算任务的分组,即groupType=2)
            List<TaskGroup> groups = taskGroupService.getGroupInfoByType(2);
            detailBean.setTaskGroups(groups);
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = parserFactory.newSAXParser();
            saxParser.parse(new InputSource(new StringReader(detailBean.getXmlData())), new XmlSAX("arg"));
            List<HashMap<String, String>> functionParams = XmlSAX.mrlist;
            List<FunctionParamBean> beans = null;
            if(functionParams!=null){
                beans = new ArrayList<FunctionParamBean>();
                for (HashMap<String, String> hashMap : functionParams)
                {
                    FunctionParamBean bean = new FunctionParamBean();
                    bean.setParamIndex(Integer.valueOf(hashMap.get("index")));
                    bean.setParamName(hashMap.get("name"));
                    bean.setValue(hashMap.get("value"));
                    bean.setParamType(hashMap.get("paramType"));
                    if(!hashMap.get("paramSelect").equals("null")){
                        bean.setParamSelect(Integer.valueOf(hashMap.get("paramSelect")));
                    }else{
                        bean.setParamSelect(0);
                    }
                    beans.add(bean);
                }
            }
            Map<String, String> map = XmlSAX.mrAttrs;
            detailBean.setFunctionId(Integer.valueOf(map.get("functionId")));
            detailBean.setFunctionName(map.get("functionName"));
            if(map.get("functionType") != null && map.get("functionType").equals("Shell")){
                detailBean.setFunctionType("9");
            }else if(map.get("functionType")!=null && map.get("functionType").equals("SQL")){
                detailBean.setFunctionType("8");
            }else{
                detailBean.setFunctionType(map.get("functionType"));
            }
            detailBean.setFunctionParams(beans);
            model.addAttribute("data", detailBean);
        }
        return "modules/tji/taskconfigure/scriptTaskForm";
    }
    
    @RequestMapping(value = "/handleDcColumn")
    public String handleDcColumn(String dataOne,String dataTwo,String dList,
            String dataThr,String dataFour,
            Model model){
        model.addAttribute("dataOne", dataOne);
        model.addAttribute("dataTwo", dataTwo);
        model.addAttribute("dList", dList);
        model.addAttribute("dataFour", dataFour);
        model.addAttribute("dataThr", dataThr);
        return "modules/tji/taskconfigure/filedFunAction";
    }
    
    /**
     * 添加flumn任务
     * @param taskDetail 任务详情
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addFlumnTask",method = RequestMethod.GET)
    public String addFlumnTask(TaskDetailBean taskDetail,Model model) throws Exception{
        List<TaskGroup> groups = taskGroupService.getGroupInfoByType(1);
        taskDetail.setTaskGroups(groups);
        //获取输入资源库列表(flume任务)
        User user = UserUtils.getUser();
        List<MetaRepo> inlist = metaRepoService.getHdfsAndFtpSources(Arrays.asList(new Integer[]{0}), Arrays.asList(new Integer[]{9,12}),user.getIsAdmin(),user);
        taskDetail.setInputResources(inlist);
        //获取输出资源库列表(flume输出到hdfs,hbase,x-oltp)
        List<MetaRepo> outlist = metaRepoService.getHdfsAndFtpSources(Arrays.asList(new Integer[]{1}), Arrays.asList(new Integer[]{3,4,11}),user.getIsAdmin(),user);
        taskDetail.setOutputResources(outlist);
        model.addAttribute("data", taskDetail);
        return "modules/tji/taskconfigure/flumeTask";
    }
    
    /**
     * 编辑flume任务
     * @param taskId 任务id
     * @param model
     * @return
     */
    @RequestMapping(value="/editFlumeTask/{taskId}")
    public String editFlumeTask(@PathVariable("taskId")Integer taskId,Model model){
        try
        {
            TaskDetailBean detailBean = new TaskDetailBean();
            TaskDetail taskDetail = taskService.getTaskDetail(taskId);
            if(taskDetail!=null){
                MyBeanUtils.copyBeanNotNull2Bean(taskDetail, detailBean);
                List<Map<String,Object>> mlist = taskDetail.getInputMetaList();
                if(mlist!=null){
                    //输入数据源id
                    detailBean.setInputDataSourceId(mlist.get(0).get("id").toString());
                    //输入资源库id
                    detailBean.setInputResourceId(mlist.get(0).get("repoId").toString());
                }
                List<Map<String,Object>> molist = taskDetail.getOutputMetaList();
                String outputDatasourceId = "";
                String outputResourceId = "";
                if(molist!=null && molist.size()>0){
                    for (int i=0;i<molist.size();i++){
                        outputDatasourceId = outputDatasourceId + String.valueOf(molist.get(i).get("id") + ((molist.size()==i+1)?"":","));
                        outputResourceId = outputResourceId + String.valueOf(molist.get(i).get("repoId") + ((molist.size()==i+1)?"":","));
                    }
                }
                //输出数据源id
                detailBean.setOutputDataSourceId(outputDatasourceId);
                //输出资源库id
                detailBean.setOutputResourceId(outputResourceId);
            }
            List<TaskGroup> groups = taskGroupService.getGroupInfoByType(1);
            detailBean.setTaskGroups(groups);
            //获取输入资源库列表(flume任务)
            User user = UserUtils.getUser();
            List<MetaRepo> inlist = metaRepoService.getHdfsAndFtpSources(Arrays.asList(new Integer[]{0}), Arrays.asList(new Integer[]{9}),user.getIsAdmin(),user);
            detailBean.setInputResources(inlist);
            //获取输出资源库列表(flume输出到hdfs,hbase,x-oltp)
            List<MetaRepo> outlist = metaRepoService.getHdfsAndFtpSources(Arrays.asList(new Integer[]{1}), Arrays.asList(new Integer[]{3,4,11}),user.getIsAdmin(),user);
            detailBean.setOutputResources(outlist);
            model.addAttribute("data", detailBean);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "modules/tji/taskconfigure/flumeTask";
    }
}
