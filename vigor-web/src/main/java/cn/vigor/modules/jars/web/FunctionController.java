package cn.vigor.modules.jars.web;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.kylin.cube.CubeInstance;
import org.apache.kylin.metadata.model.DataModelDesc;
import org.apache.kylin.metadata.model.TableDesc;
import org.apache.kylin.metadata.project.ProjectInstance;
import org.apache.kylin.metadata.realization.RealizationStatusEnum;
import org.apache.kylin.rest.request.ModelRequest;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.jars.entity.Expension;
import cn.vigor.modules.jars.entity.Function;
import cn.vigor.modules.jars.entity.MapreduceJar;
import cn.vigor.modules.jars.service.FunctionService;
import cn.vigor.modules.jars.service.MapreduceJarService;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;
import cn.vigor.modules.tji.util.KylinHttpRequest;

/**
 * 函数管理Controller
 * @author kiss
 * @version 2016-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/tunc/function")
public class FunctionController extends BaseController {
    
    private static Logger log = Logger.getLogger(FunctionController.class);

	@Autowired
	private FunctionService functionService;
	
	@Autowired
	private MetaStoreService metaStoreService;
	
	@ModelAttribute
	public Function get(@RequestParam(value="id",required=false) String id) {
		Function entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = functionService.get(id);
			Expension expansion;
            try
            {
                expansion = JSONObject.toJavaObject(JSONObject.parseObject(entity.getExpansionField()), Expension.class);
                if(expansion==null)
                {
                    expansion=new Expension();
                }else{
                    if(expansion.getPassword()!=null && expansion.getPassword().endsWith("==")){
                        expansion.setPassword(AESUtil.decForTD(expansion.getPassword()));
                    }
                }
            }
            catch (Exception e)
            {
                expansion=new Expension();
            }
            entity.setExpansion(expansion);
		}
		if (entity == null){
			entity = new Function();
		}
		return entity;
	}
	
	/**
	 * 函数管理列表页面
	 */
	@RequiresPermissions("tunc:function:list")
	@RequestMapping(value = {"list", ""})
	public String list(Function function, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Function> page = functionService.findPage(new Page<Function>(request, response), function); 
		model.addAttribute("page", page);
		return "modules/tunc/functionList";
	}

	/**
	 * 查看，增加，编辑函数管理表单页面
	 */
	@RequiresPermissions(value={"tunc:function:view","tunc:function:add","tunc:function:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Function function, Model model) {
	    try
        {
            if(function.getFunctionType()!=null && function.getFunctionType()==12){
                String result = getKylinModel(function.getFunctionName());
                if(StringUtils.isNotEmpty(result)){
                    result = handleResult(result);
                }
                DataModelDesc dataModelDesc = JSONObject.parseObject(result, DataModelDesc.class);
                model.addAttribute("kylinModel", dataModelDesc);
            }
            model.addAttribute("function", function);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
		return "modules/tunc/functionForm";
	}
	/**
     * 查看，增加，编辑函数管理表单页面
     */
    @RequiresPermissions(value="tunc:function:view")
    @RequestMapping(value = "view")
    public String view(Function function, Model model) {
        try
        {
            function.setParamNums(function.getFunctionParamList().size());
            if(function!=null && function.getFunctionType()==12){
                String result = getKylinModel(function.getFunctionName());
                if(StringUtils.isNotEmpty(result)){
                    result = handleResult(result);
                }
                if(StringUtils.isNotEmpty(result) && result.contains("{")){
                    DataModelDesc dataModelDesc = JSONObject.parseObject(result, DataModelDesc.class);
                    model.addAttribute("kylinModel", dataModelDesc);
                }
            }
            model.addAttribute("function", function);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "modules/tunc/functionView";
    }
	/**
	 * 保存函数管理
	 */
	@RequiresPermissions(value={"tunc:function:add","tunc:function:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Function function, ModelRequest modelRequest, Model model, RedirectAttributes redirectAttributes) throws Exception{
		try
        {
		    if (!beanValidator(model, function)){
                return form(function, model);
            }
		    if(function.getFunctionType()==12){//X-Abase类型
		        String method = "";
		        if(!function.getIsNewRecord()){
		            String uuid = JSONObject.parseObject(function.getExpansionField()).getString("uuid");
		            modelRequest.setUuid(uuid);
		            method = "PUT";
		        }else{
		            //获取project
		            Map<String,String> map = createKylinDefalutProject();
		            if(map.get("code").equals("0")){//创建project成功
		                modelRequest.setProject(map.get("name"));
		            }else{//创建project失败
		                addMessage(redirectAttributes, map.get("msg"));
		            }
		            method = "POST";
		        }
		        String result = saveKylinModel(modelRequest, method);
		        if(result.contains("{")){
		            ModelRequest request = JSONObject.parseObject(result, ModelRequest.class);
	                if(function.getIsNewRecord()){
	                    function.setFunctionStatus(2);//新增默认为自定义的
	                    Map<String,Object> map = new HashMap<String,Object>();
	                    map.put("uuid", request.getUuid());
	                    map.put("project", modelRequest.getProject());
	                    function.setExpansionField(JSONObject.toJSONString(map));
	                    functionService.save(function);//保存
	                    addMessage(redirectAttributes, "保存函数成功");
	                }else{
	                    Function t = functionService.get(function.getId());//从数据库取出记录的值
	                    MyBeanUtils.copyBeanNotNull2Bean(function, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
	                    functionService.save(t);//保存
	                    addMessage(redirectAttributes, "保存函数成功");
	                }
		        }else{
		            addMessage(redirectAttributes, result);
		        }
		    }else{
	            if(function.getExpansion()!=null)
	            {
	                Expension expension = function.getExpansion();
	                if(StringUtils.isNotEmpty(expension.getPassword())){
	                    String password = expension.getPassword();
	                    expension.setPassword(AESUtil.encForTD(password));
	                }
	              function.setExpansionField(JSONObject.toJSONString(function.getExpansion()));
	            }
	            if(!function.getIsNewRecord()){//编辑表单保存
	                Function t = functionService.get(function.getId());//从数据库取出记录的值
	                MyBeanUtils.copyBeanNotNull2Bean(function, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
	                functionService.save(t);//保存
	            }else{//新增表单保存
	                function.setFunctionStatus(2);//新增默认为自定义的
	                functionService.save(function);//保存
	            }
	            addMessage(redirectAttributes, "保存函数管理成功");
		    }
        }
        catch (Exception e)
        {
            String msg=e.getMessage();
            if(msg.contains("Duplicate") && msg.contains("functionname"))
            {
                msg=function.getFunctionName()+"名已存在，请重新命名！";
            } else {
                msg="保存失败：失败原因："+e.getMessage();
            }
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
		return "redirect:"+Global.getAdminPath()+"/tunc/function/?repage";
	}
	
	/**
	 * 删除函数管理
	 */
	@RequiresPermissions("tunc:function:del")
	@RequestMapping(value = "delete")
	public String delete(Function function, RedirectAttributes redirectAttributes) {
	    try
        {
            if(function!=null){
                Function func = functionService.get(function);
                if(func!=null && func.getFunctionType()==12 && func.getFunctionName()!=null){
                    delKylinModel(func.getFunctionName());
                    functionService.delete(function);
                }else{
                    functionService.delete(function);
                }
            }
            addMessage(redirectAttributes, "删除函数成功");
        }
        catch (IOException e)
        {
            addMessage(redirectAttributes, "删除函数失败");
            logger.info(e.getMessage());
            e.printStackTrace();
        }
	    return "redirect:"+Global.getAdminPath()+"/tunc/function/?repage";
	}
	
	/**
	 * 批量删除函数管理
	 */
	@RequiresPermissions("tunc:function:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		try
        {
            String idArray[] =ids.split(",");
            for(String id : idArray){
                Function function = functionService.get(id);
                if(function!=null && function.getFunctionType()==12 && function.getFunctionName()!=null){
                    String result = delKylinModel(function.getFunctionName());
                    if(result.equals("success")){
                        functionService.delete(function);
                    }
                }else{
                    functionService.delete(function);
                }
            }
            addMessage(redirectAttributes, "删除函数成功");
        }
        catch (IOException e)
        {
            addMessage(redirectAttributes, "删除函数失败");
            e.printStackTrace();
        }
		return "redirect:"+Global.getAdminPath()+"/tunc/function/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("tunc:function:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Function function, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "函数管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Function> page = functionService.findPage(new Page<Function>(request, response, -1), function);
    		new ExportExcel("函数管理", Function.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出函数管理记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tunc/function/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("tunc:function:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Function> list = ei.getDataList(Function.class);
			for (Function function : list){
				try{
					functionService.save(function);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条函数管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条函数管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入函数管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tunc/function/?repage";
    }
	
	/**
	 * 下载导入函数管理数据模板
	 */
	@RequiresPermissions("tunc:function:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "函数管理数据导入模板.xlsx";
    		List<Function> list = Lists.newArrayList(); 
    		new ExportExcel("函数管理数据", Function.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tunc/function/?repage";
    }
	
	@Autowired
	MapreduceJarService mapreduceJarService;
	/**
	 * 选择关联jar包，端口
	 */
	@RequestMapping(value = "selectjarId")
	public String selectjarId(MapreduceJar jarId, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MapreduceJar> page = mapreduceJarService.findPage(new Page<MapreduceJar>(request, response),  jarId);
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", jarId);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	
    @RequestMapping(value = "addLookUp")
	public String addLookup(String tableName,String type,String conditions,String mTableName,Model model){
	    model.addAttribute("tableName", tableName);
	    model.addAttribute("type", type);
	    model.addAttribute("mTableName", mTableName);
	    model.addAttribute("conditions", conditions);
	    return "modules/tunc/addLookUp";
	}
	
	/**
	 * 保存kylin函数
	 * @param function 函数信息
	 * @param method http请求方式
	 * @throws IOException 
	 */
	private String saveKylinModel(ModelRequest modelRequest,String method) throws IOException{
	    String baseUrl = Global.getConfig("kylin_base_api_url") + "models";
	    Map<String,Object> mp = new HashMap<String,Object>();
	    String modelDescData = StringEscapeUtils.unescapeHtml(modelRequest.getModelDescData());
//	    if(method.equals("POST")){
//	        DataModelDesc dataModelDesc = JSONObject.parseObject(modelDescData, DataModelDesc.class);
//	        PartitionDesc partitionDesc = dataModelDesc.getPartitionDesc();
//	        partitionDesc.setPartitionTimeFormat("HH:mm:ss");
//	        dataModelDesc.setPartitionDesc(partitionDesc);
//	    }
	    mp.put("modelDescData", modelDescData);
	    mp.put("modelName", modelRequest.getModelName());
	    mp.put("project", modelRequest.getProject());
	    String params = JSONObject.toJSONString(mp);
        return KylinHttpRequest.httpPost(params, baseUrl, method);
	}
	
	/**
	 * 根据modelName删除model
	 * @param modelName 
	 * @throws IOException 
	 */
	public String delKylinModel(String modelName) throws IOException{
	    String baseUrl = Global.getConfig("kylin_base_api_url") + "models/" + modelName;
	    return KylinHttpRequest.httpGet(baseUrl, "DELETE");
	}
	
	/**
	 * 根据modelName获取model详情
	 * @param modelName
	 * @param projectName
	 * @return
	 */
	public static String getKylinModel(String modelName) throws IOException{
	    String baseUrl = Global.getConfig("kylin_base_api_url") + "model/" + modelName;
	    return KylinHttpRequest.httpGet(baseUrl,"GET");
	}
	
	private String getKylinModels() throws IOException{
	    List<ProjectInstance> list = getKylinProjects();
	    if(list!=null && list.size()>0){
	        List<String> projectNames = new ArrayList<String>();
	        for (ProjectInstance projectInstance : list)
            {
	            projectNames.add(projectInstance.getName());
            }
	        if(projectNames.contains(Global.getConfig("kylin_default_project"))){
	            String baseUrl = Global.getConfig("kylin_base_api_url") + "models?projectName="+Global.getConfig("kylin_default_project");
	            return KylinHttpRequest.httpGet(baseUrl,"GET");
	        }else{
	            return "项目tospur-admin下没有函数,请先创建函数";
	        }
	    }else{
	        return "获取kylin项目列表失败";
	    }
	}
	
	/**
	 * 根据函数名称获取函数信息
	 * @param modelName 函数名称
	 * @return
	 */
	@RequestMapping(value="getModels",method=RequestMethod.GET)
	@ResponseBody
	public JSONObject getModels(){
	    JSONObject jsonObject = new JSONObject();
	    try
        {
            String result = getKylinModels();
            if(StringUtils.isNotEmpty(result) && result.contains("{")){
                result = handleResult(result);
                List<DataModelDesc> dataModelDescs = JSONObject.parseArray(result, DataModelDesc.class);
                jsonObject.put("dataModelDescs", dataModelDescs);
                jsonObject.put("code", 0);
            }else{
                jsonObject.put("code", 1);
                jsonObject.put("msg", result);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            jsonObject.put("code", 1);
            jsonObject.put("msg", "获取函数信息失败");
        }
	    return jsonObject;
	}
	
	/**
	 * 创建kylin project
	 * @return
	 */
	public Map<String,String> createKylinDefalutProject() throws IOException{
	    //创建之前先获取该project是否存在,不存在时,再创建
	    Map<String,String> rp = new HashMap<String,String>();
	    String baseUrl = Global.getConfig("kylin_base_api_url") + "projects";
	    //获取kylin所有的project
	    String re = KylinHttpRequest.httpGet(baseUrl, "GET");
	    List<String> projects = null;
	    if(re.contains("{")){
	        projects = new ArrayList<String>();
	        List<ProjectInstance> list = JSONObject.parseArray(re, ProjectInstance.class);
	        if(list!=null && list.size()>0){
	            for (ProjectInstance projectInstance : list)
                {
	                projects.add(projectInstance.getName());
                }
	        }
	    }
	    if(!projects.contains(Global.getConfig("kylin_default_project"))){
	        Map<String,String> map = new HashMap<String,String>();
	        map.put("name", Global.getConfig("kylin_default_project"));
	        map.put("description", "数据中心管理平台");
	        String params = JSONObject.toJSONString(map);
	        String result = KylinHttpRequest.httpPost(params, baseUrl, "POST");
	        if(result.contains("{")){
	            ProjectInstance instance = JSONObject.parseObject(result, ProjectInstance.class);
	            rp.put("name", instance.getName());
	            rp.put("code", "0");
	            //在创建初次创建project时load加载hive表数据
	            loadHiveTableds(instance.getName());
	        }else{
	            rp.put("msg", result);
	            rp.put("code", "1");
	        }
	    }else{
	        rp.put("name", Global.getConfig("kylin_default_project"));
            rp.put("code", "0");
	    }
	    return rp;
	}
	
	/**
	 * 根据用户权限获取hive表并load到kylin
	 * @param projectName 项目名称
	 */
	private String loadHiveTableds(String projectName) throws IOException{
	    String result = null;
	    String baseUrl = Global.getConfig("kylin_base_api_url") + "tables/";
	    MetaStore metaStore = new MetaStore();
	    User user = UserUtils.getUser();
	    metaStore.setStoreType(5);//hive类型
	    metaStore.setIsAdmin(Integer.valueOf(user.getId())==1?1:0);
	    List<MetaStore> list = metaStoreService.findALL(metaStore);
	    List<String> tables = new ArrayList<String>();
	    List<String> loadedTables = new ArrayList<String>();
	    //获取kylin平台上有哪些已经load的hive表
	    String hiveTablesUrl = Global.getConfig("kylin_base_api_url") + "tables?project=" + projectName + "&ext=" + false;
        String rt = KylinHttpRequest.httpGet(hiveTablesUrl,"GET");
        if(rt.contains("{")){
            rt = rt.replace("source_type", "sourceType");
            rt = rt.replace("table_type", "tableType");
            rt = rt.replace("last_modified", "lastModified");
            List<TableDesc> tableDescs = JSONObject.parseArray(rt, TableDesc.class);
            if(tableDescs!=null && tableDescs.size()>0){
                for (TableDesc tableDesc : tableDescs){
                    loadedTables.add(tableDesc.getDatabase() + "." + tableDesc.getName());
                }
            }
        }
	    if(list!=null && list.size()>0){
	        for (MetaStore metaStore2 : list){
	            if(metaStore2.getStoreFile().contains("${")){
	                continue;
	            }
                String table = metaStore2.getRepoName() + "." + metaStore2.getStoreFile();
                if(!loadedTables.contains(table) && !tables.contains(table)){
                    tables.add(table);
                }
            }
	    }
	    //将已经loaded的hive表unload
	    if(loadedTables.size()>0){
	        int num = loadedTables.size()/20+(loadedTables.size()%20==0?0:1);
	        for(int j=0;j<num;j++){
	            String unloadUrl = baseUrl;
	            for(int i=0;i<20;i++){
                    if(i+20*j>=loadedTables.size()){
                        break;
                    }
                    unloadUrl = unloadUrl + loadedTables.get(i+20*j) + ",";
                }
	            unloadUrl = unloadUrl + "/" + projectName;
	            KylinHttpRequest.httpGet(unloadUrl, "DELETE");
	        }
	    }
	    //将新的hive表重新load到kylin
	    if(tables.size()>0){
	        int num = tables.size()/20+(tables.size()%20==0?0:1);
	        for(int j=0;j<num;j++){
	            String url = baseUrl;
	            for(int i=0;i<20;i++){
	                if(i+20*j>=tables.size()){
	                    break;
	                }
	                url = url + tables.get(i+20*j) + ",";
	            }
	            url = url + "/" + projectName;
	            Map<String,Object> map = new HashMap<String,Object>();
	            map.put("calculate", true);
	            String params = JSONObject.toJSONString(map);
	            KylinHttpRequest.httpPost(params,url, "POST");
	        }
	    }
	    return result;
	}
	
	/**
	 * 获取kylin项目列表
	 * @return
	 * @throws IOException
	 */
	public List<ProjectInstance> getKylinProjects() throws IOException{
	   String baseUrl = Global.getConfig("kylin_base_api_url") + "projects";
	   String result = KylinHttpRequest.httpGet(baseUrl, "GET");
	   if(result.contains("{")){
	       List<ProjectInstance> list = JSONObject.parseArray(result, ProjectInstance.class);
	       return list;
	   }else{
	       log.error("获取kylin项目列表失败:" + result);
	   }
	   return null;
	}
	
	/**
	 * 获取hive表信息
	 * @param tableName 表名
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="getHiveTableColumns",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getHiveTableColumns(String tableName,Model model) throws IOException{
	    JSONObject jsonObject = new JSONObject();
	    String baseUrl = Global.getConfig("kylin_base_api_url") + "tables/" + tableName;
        String result = KylinHttpRequest.httpGet(baseUrl,"GET");
        TableDesc desc = JSONObject.parseObject(result, TableDesc.class);
        jsonObject.put("tableDesc", desc);
        return jsonObject;
    }
	
	@RequestMapping(value="getHiveTables",method = RequestMethod.GET)
    @ResponseBody
	public JSONObject getHiveTables(@RequestParam(value="project",required=false)String project,
	        @RequestParam(value="ext",defaultValue="false",required=false)Boolean ext) throws IOException{
	    JSONObject jsonObject = new JSONObject();
	    if(StringUtils.isEmpty(project)){
	        project = Global.getConfig("kylin_default_project");
	    }
	    String baseUrl = Global.getConfig("kylin_base_api_url") + "tables?project="+project+"&ext="+false;
	    String result = KylinHttpRequest.httpGet(baseUrl,"GET");
	    if(result.contains("{")){
	        result = result.replace("source_type", "sourceType");
	        result = result.replace("table_type", "tableType");
	        result = result.replace("last_modified", "lastModified");
	        List<TableDesc> list = JSONObject.parseArray(result, TableDesc.class);
	        jsonObject.put("tables", list);
	    }
	    return jsonObject;
	} 
	
	/**
	 * 编辑前获取函数状态
	 * @param modelName 函数名称
	 * @return 结果
	 */
	@RequestMapping(value="modelStatus/{modelName}",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getModelStatus(@PathVariable("modelName")String modelName){
	    JSONObject jsonObject = new JSONObject();
	    String projectName = Global.getConfig("kylin_default_project");
	    String baseUrl = Global.getConfig("kylin_base_api_url") + "cubes?modelName=" + modelName + "&projectName=" + projectName;
	    try
        {
            String result = KylinHttpRequest.httpGet(baseUrl, "GET");
            if(result.contains("{")){
                List<CubeInstance> list = JSONObject.parseArray(result, CubeInstance.class);
                if(list!=null && list.size()>0){
                    for (CubeInstance cubeInstance : list){
                        RealizationStatusEnum statusEnum = cubeInstance.getStatus();
                        if(String.valueOf(statusEnum).equals("READY")){
                            jsonObject.put("msg", "这个函数已经被"+cubeInstance.getName()+"任务使用了,不能编辑");
                            jsonObject.put("code", 1);
                            break;
                        }
                    }
                    jsonObject.put("code", 0);
                }else{
                    jsonObject.put("code", 0);
                }
            }else{
                jsonObject.put("msg", result);
                jsonObject.put("code", 1);
            }
        }
        catch (IOException e)
        {
            jsonObject.put("msg", "获取函数状态失败");
            jsonObject.put("code", 1);
            e.printStackTrace();
        }
	    return jsonObject;
	}
	
	private String handleResult(String result){
	    if(StringUtils.isNotEmpty(result)){
	        result = result.replace("fact_table", "factTable");
            result = result.replace("foreign_key", "foreignKey");
            result = result.replace("primary_key", "primaryKey");
            result = result.replace("filter_condition", "filterCondition");
            result = result.replace("partition_time_column", "partitionTimeColumn");
            result = result.replace("partition_desc", "partitionDesc");
            result = result.replace("partition_date_start", "partitionDateStart");
            result = result.replace("partition_date_format", "partitionDateFormat");
            result = result.replace("partition_time_format", "partitionTimeFormat");
            result = result.replace("partition_date_column", "partitionDateColumn");
            result = result.replace("last_modified", "lastModified");
            result = result.replace("partition_type", "partitionType");
            result = result.replace("partition_condition_builder", "partitionConditionBuilder");
	    }
	    return result;
	}
	
	public static void main(String[] args) throws IOException
    {
//	    String method = "POST";
//	    ModelRequest modelRequest = new ModelRequest();
//	    modelRequest.setModelName("hzy_model_1");
//	    modelRequest.setProject("learn_kylin");
//	    String modelDescData = "{\n  \"version\": \"1.5.3\"" +
//	            ",\n  \"name\": \"hzy_model_1\",\n  \"description\": \"草泥马草泥马草泥马草泥马\",\n  \"lookups\": [],\n  \"dimensions\""+
//	            ": [\n    {\n      \"table\": \"DATA_CENTER.ALL_MACS\",\n      \"columns\": [\n        \"USER_MAC\"\n"+
//	            "      ]\n    }\n  ],\n  \"metrics\": [\n    \"USER_MAC\"\n  ],\n  \"capacity\": \"MEDIUM\",\n  "+
//	            "\n  \"fact_table\": \"DATA_CENTER.ALL_MACS\",\n  \"filter_condition\": \"\",\n  \"partition_desc"+
//	            "\": {\n    \"partition_date_column\": \"DATA_CENTER.ALL_MACS.USER_MAC\",\n    \"partition_time_column"+
//	            "\": null,\n    \"partition_date_start\": 0,\n    \"partition_date_format\": \"yyyy-MM-dd\",\n    \"partition_time_format"+
//	            "\": \"HH:mm:ss\",\n    \"partition_type\": \"APPEND\",\n    \"partition_condition_builder\": \"org.apache.kylin.metadata.model.PartitionDesc$DefaultPartitionConditionBuilder\"\n  }\n}";
////	    System.out.println(modelDescData);
//	    modelRequest.setModelDescData(modelDescData);
//        String result = new FunctionController().saveKylinModel(modelRequest, method);
//        System.out.println(result);
//            ModelRequest request = JSONObject.parseObject(result, ModelRequest.class);
//            System.out.println("uuid:"+request.getUuid());
//	    String s = new FunctionController().delKylinModel("test_name_func");
//	    System.out.println(s);
//	    String result = new FunctionController().getKylinModels();
//	    System.out.println(result);
//	    if(StringUtils.isNotEmpty(result)){
//            result = result.replace("fact_table", "factTable");
//            result = result.replace("filter_condition", "filterCondition");
//            result = result.replace("partition_desc", "partitionDesc");
//            result = result.replace("partition_date_column", "partitionDateColumn");
//            result = result.replace("last_modified", "lastModified");
//        }
//	    DataModelDesc dataModelDesc = JSONObject.parseObject(result, DataModelDesc.class);
//	    System.out.println(dataModelDesc.getPartitionDesc());
//	    System.out.println(new FunctionController().delKylinModel("gdfgd"));
//	    List<ProjectInstance> list = new FunctionController().getKylinProjects();
//	    System.out.println(list);
    }

}