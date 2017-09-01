package cn.vigor.modules.meta.web;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.kylin.rest.model.TableMeta;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.vigor.common.config.Global;
import cn.vigor.common.contants.Contants;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.compute.bean.ComputeFunction;
import cn.vigor.modules.compute.bean.ComputeTaskGroup;
import cn.vigor.modules.compute.bean.InParam;
import cn.vigor.modules.compute.bean.Repositories;
import cn.vigor.modules.compute.entity.ComputeTaskDetailBean;
import cn.vigor.modules.compute.service.ComputeMetaService;
import cn.vigor.modules.compute.service.ComputeTaskService;
import cn.vigor.modules.compute.utils.TestUtil;
import cn.vigor.modules.meta.entity.DataExploreLog;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaResult;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.entity.MetaTable;
import cn.vigor.modules.meta.service.DataExploreLogService;
import cn.vigor.modules.meta.service.MetaRepoService;
import cn.vigor.modules.meta.service.MetaResultService;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.meta.util.DBUtils;
import cn.vigor.modules.meta.util.PrivilegeException;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;
import cn.vigor.modules.tji.util.KylinHttpRequest;

/**
 * 数据库连接信息Controller
 * @author kiss
 * @version 2016-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/repo/RepoStore")
public class MetaRepoStoreController extends BaseController {

	@Autowired
	private MetaRepoService metaRepoService;
	
	@Autowired
	private MetaStoreService metaStoreService;
	
	@Autowired
	private MetaResultService metaResultService;
	
	@Autowired
	private DataExploreLogService dataExploreLogService;
	
	@ModelAttribute
	public MetaRepo get(@RequestParam(required=false) String id) {
		MetaRepo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = metaRepoService.get(id,1);
			if(StringUtils.isNotEmpty(entity.getUserPwd()) && entity.getUserPwd().endsWith("==")){
			    entity.setUserPwd(AESUtil.decForTD(entity.getUserPwd()));
			}
		}
		if (entity == null){
			entity = new MetaRepo();
		}
		return entity;
	}
	
	/**
	 * 数据库连接信息列表页面
	 */
	@RequiresPermissions("repo:repoStore:list")
	@RequestMapping(value = {"list", ""})
	public String list(MetaRepo metaRepo, HttpServletRequest request, HttpServletResponse response, Model model) {
		metaRepo.setMetaType(1);
		Page<MetaRepo> page = metaRepoService.findPage(new Page<MetaRepo>(request, response), metaRepo); 
		model.addAttribute("page", page);
	    User user = UserUtils.getUser();
	    model.addAttribute("user", user);
		return "modules/meta/store/repo/metaRepoList";
	}

	/**
	 * 查看，增加，编辑数据库连接信息表单页面
	 */
	@RequiresPermissions(value={"repo:repoStore:view","repo:repoStore:add","repo:repoStore:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MetaRepo metaRepo, Model model) {
		model.addAttribute("metaRepo", metaRepo);
		return "modules/meta/store/repo/metaRepoForm";
	}

	/**
     * 查看
     */
    @RequiresPermissions(value="repo:repoStore:view")
    @RequestMapping(value = "view")
    public String view(MetaRepo metaRepo, Model model) {
        model.addAttribute("metaRepo", metaRepo);
        return "modules/meta/store/repo/metaRepoView";
    }

	/**
	 * 保存数据库连接信息
	 */
	@RequiresPermissions(value={"repo:repoStore:add","repo:repoStore:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MetaRepo metaRepo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		try
        {
            if (!beanValidator(model, metaRepo)){
            	return form(metaRepo, model);
            }
            //根据e_meta_repo表唯一索引的约束,需要数据进行校验,并给出提示
            MetaRepo mr = metaRepoService.findExistRepo(metaRepo);
            if(mr!=null){
            	addMessage(redirectAttributes, "同类型存储信息在相同ip和目录下只能存在一条记录");
            }else{
            	if(!metaRepo.getIsNewRecord()){//编辑表单保存
                	MetaRepo t = metaRepoService.get(metaRepo.getId());//从数据库取出记录的值
                	MyBeanUtils.copyBeanNotNull2Bean(metaRepo, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
                	t.setMetaType(1);
                	metaRepoService.save(t);//保存
                }else{//新增表单保存
                	metaRepo.setMetaType(1);//0 source  1 store 2 result 
                	metaRepoService.save(metaRepo);//保存
                }
                addMessage(redirectAttributes, "保存数据库连接信息成功");
            }
        }
        catch (Exception e)
        {
            String msg=e.getMessage();
            if(msg.contains("Duplicate")&& msg.contains("connname"))
            {
                msg="保存失败,"+metaRepo.getConnName()+"名已存在，请重新命名！";
            } else {
                msg="保存失败：失败原因："+e.getMessage();
            }
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
		return "redirect:"+Global.getAdminPath()+"/repo/RepoStore/?repage";
	}
	
	/**
	 * 删除数据库连接信息
	 */
	@RequiresPermissions("repo:repoStore:del")
	@RequestMapping(value = "delete")
	public String delete(MetaRepo metaRepo, RedirectAttributes redirectAttributes) {
	    try
        {
            metaRepoService.delete(metaRepo,1);
            addMessage(redirectAttributes, "删除连接信息成功");
        }
        catch (PrivilegeException e)
        {
            addMessage(redirectAttributes, e.getMessage());
        }
		return "redirect:"+Global.getAdminPath()+"/repo/RepoStore/?repage";
	}
	
	/**
	 * 批量删除数据库连接信息
	 */
	@RequiresPermissions("repo:repoStore:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			metaRepoService.delete(metaRepoService.get(id));
		}
		addMessage(redirectAttributes, "删除数据库连接信息成功");
		return "redirect:"+Global.getAdminPath()+"/repo/RepoStore/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("repo:repoStore:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MetaRepo metaRepo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "数据库连接信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MetaRepo> page = metaRepoService.findPage(new Page<MetaRepo>(request, response, -1), metaRepo);
    		new ExportExcel("数据库连接信息", MetaRepo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据库连接信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/repo/RepoStore/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("repo:repoStore:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MetaRepo> list = ei.getDataList(MetaRepo.class);
			for (MetaRepo metaRepo : list){
				metaRepoService.save(metaRepo);
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条数据库连接信息记录");
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入数据库连接信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/repo/RepoStore/?repage";
    }
	
	/**
	 * 下载导入数据库连接信息数据模板
	 */
	@RequiresPermissions("repo:repoStore:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "数据库连接信息数据导入模板.xlsx";
    		List<MetaRepo> list = Lists.newArrayList(); 
    		new ExportExcel("数据库连接信息数据", MetaRepo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/repo/RepoStore/?repage";
    }
	
	
	/**
     * 启用 disable
     * @param id 表id
     * @return
     */
    @RequestMapping(value ="enable")
    @ResponseBody
    public JSONObject enable(String id)
    {
        JSONObject data = new JSONObject();
        //获取表信息
        MetaRepo  repo = metaRepoService.get(id);
        //获取表 或者目录 ip 用户名 密码 端口等  
        data=metaRepoService.enable(repo);                   
        return data;
    }

    /**
     * 启用 
     * @param id 表id
     * @return
     */
    @RequestMapping(value ="disable")
    @ResponseBody
    public JSONObject disable(String id)
    {
        JSONObject data = new JSONObject();
        //获取表信息
        MetaRepo  repo = metaRepoService.get(id);
        //获取表 或者目录 ip 用户名 密码 端口等  
        data=metaRepoService.disable(repo);                   
        return data;
    }
    
    @RequestMapping(value = "getTables")
    @ResponseBody
    public JSONObject getTables(String id )
    {
        JSONObject data = new JSONObject();
        try
        {
            MetaRepo metaRepo = metaRepoService.get(id, 1);
            String pwd = metaRepo.getUserPwd();
            if(StringUtils.isNotEmpty(pwd) && !pwd.equals("${input_dbpwd}")){
                pwd = AESUtil.encForTD(pwd);
            }
            List<MetaTable> tables=null;
            if(7==metaRepo.getRepoType())
            {
               
            }if(10==metaRepo.getRepoType())
            {
                tables = new DBUtils().getSqlserverTables(
                        metaRepo.getUserName(),
                        pwd,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName());
            }else{
                tables = new DBUtils().getMysqlTables(
                        metaRepo.getUserName(),
                        pwd,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName());
            }   
            for (MetaTable metaTable : tables)
            {
                 bk: for (MetaSource source : metaRepo.getMetaSourceList())
                  {
                    if(source.getSourceFile().equals(metaTable.getTableName()))
                    {
                        metaTable.setTablestatus("已存在");
                        break bk;
                    }
                  }
            }
            JSONArray json = (JSONArray) JSONArray.toJSON(tables);
            data.put("success", true);
            data.put("msg", "成功");
            data.put("data", json);
            metaRepo.setTables(tables);
        }
        catch (Exception e)
        {
            data.put("success", false);
            data.put("msg", e.getMessage());
            e.printStackTrace();
        }
       
        return data;
    }
    
    
    /**
     * 自助查询数据
     */
    @RequestMapping(value = "selfQuery")///repo/RepoStore/selfQuery
    public String selfQuery(Model model) {
        return "modules/tji/selfQuery";
    }
    
    /**
     * 数据探索页面分为两部分(此为上部分子页面接口)
     * @param model
     * @return
     */
    @RequestMapping(value="selfQueryConf")
    public String selfQueryConf(@RequestParam(value="delId",required=false)String delId,Model model){
    	if(StringUtils.isNotEmpty(delId)){
    		DataExploreLog dataExploreLog = dataExploreLogService.get(delId);
    		String searchType = dataExploreLog.getSearchType();
    		if(searchType.equals("sparkBases")){
    			List<MetaRepo> sparkBases = metaRepoService.findListBy(5);
    			model.addAttribute("sparkBases", sparkBases);
    		}else if(searchType.equals("tranfBases")){
    			List<MetaRepo> tranBases = metaRepoService.findListBy(11);
    			model.addAttribute("tranBases", tranBases);
    		}else if(searchType.equals("kylinBases")){
    			//kylin的连接信息是获取的hive连接信息
        		List<MetaRepo> sparkBases = metaRepoService.findListBy(5);
        		//获取kylin query库和表的信息
                List<MetaRepo> kylinBases = null;
                try {
                    List<TableMeta> list = getKylinQueryTables(Global.getConfig("kylin_default_project"));
                    if(list!=null && list.size()>0){
                        //数据库名称
                        Map<String,List<String>> map = new HashMap<String,List<String>>();
                        for(TableMeta tableMeta : list){
                            if(!map.containsKey(tableMeta.getTABLE_SCHEM())){
                                List<String> lt = new ArrayList<String>();
                                lt.add(tableMeta.getTABLE_NAME());
                                map.put(tableMeta.getTABLE_SCHEM(), lt);
                            }else{
                                map.get(tableMeta.getTABLE_SCHEM()).add(tableMeta.getTABLE_NAME());
                            }
                        }
                        if(sparkBases!=null && sparkBases.size()>0){
                            kylinBases = new ArrayList<MetaRepo>();
                            for (MetaRepo metaRepo : sparkBases){
                                String dbName = metaRepo.getRepoName();
                                if(map.containsKey(dbName.toUpperCase())){
                                    kylinBases.add(metaRepo);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.addAttribute("kylinBases", kylinBases);
    		}
    		model.addAttribute("dataExploreLog", dataExploreLog);
    	}
    	return "modules/tji/dataQueryConfig";
    }
    
    /**
     * 根据库类型查询输入连接信息
     * @param type 库类型
     * @return 结果
     */
    @RequestMapping(value = "/selectQueryType")
    @ResponseBody
    public Map<String,Object> selectQueryType(@RequestParam(value="type",required=true)String type){
    	Map<String,Object> mp = new HashMap<String,Object>();
    	if(type.equals("sparkBases")){
    		List<MetaRepo> sparkBases = metaRepoService.findListBy(5);
    		mp.put("sparkBases", sparkBases);
    	}else if(type.equals("tranfBases")){
    		List<MetaRepo> tranBases = metaRepoService.findListBy(11);
    		mp.put("tranfBases", tranBases);
    	}else if(type.equals("kylinBases")){
    		//kylin的连接信息是获取的hive连接信息
    		List<MetaRepo> sparkBases = metaRepoService.findListBy(5);
    		//获取kylin query库和表的信息
            List<MetaRepo> kylinBases = null;
            try {
                List<TableMeta> list = getKylinQueryTables(Global.getConfig("kylin_default_project"));
                if(list!=null && list.size()>0){
                    //数据库名称
                    Map<String,List<String>> map = new HashMap<String,List<String>>();
                    for(TableMeta tableMeta : list){
                        if(!map.containsKey(tableMeta.getTABLE_SCHEM())){
                            List<String> lt = new ArrayList<String>();
                            lt.add(tableMeta.getTABLE_NAME());
                            map.put(tableMeta.getTABLE_SCHEM(), lt);
                        }else{
                            map.get(tableMeta.getTABLE_SCHEM()).add(tableMeta.getTABLE_NAME());
                        }
                    }
                    if(sparkBases!=null && sparkBases.size()>0){
                        kylinBases = new ArrayList<MetaRepo>();
                        for (MetaRepo metaRepo : sparkBases){
                            String dbName = metaRepo.getRepoName();
                            if(map.containsKey(dbName.toUpperCase())){
                                kylinBases.add(metaRepo);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.put("kylinBases", kylinBases);
    	}
    	return mp;
    }
    
    /**
     * 查询kylin查询表元数据信息
     * @param projectName 项目名称
     * @return
     * @throws IOException
     */
    private List<TableMeta> getKylinQueryTables(String projectName) throws IOException{
        String baseUrl = Global.getConfig("kylin_base_api_url") + "tables_and_columns?project=" + projectName;
        String result = KylinHttpRequest.httpGet(baseUrl, "GET");
        if(result.contains("{")){
            List<TableMeta> list = JSONObject.parseArray(result, TableMeta.class);
            return list;
        }else{
            logger.error("获取kylin表和字段信息失败:" + result);
        }
        return null;
    }
    
    /**
     * 查询表信息
     * @param id 表id
     * @return
     */
    @RequestMapping(value ="tables-get")//repo/RepoStore/tables-get
    @ResponseBody
    public JSONObject gettables(String id)
    {
        JSONObject data = new JSONObject();
        try
        {
            data.put("success", true);
            data.put("msg", "成功");
            data.put("data", metaRepoService.getTables(id));
        }
        catch (Exception e)
        {
            data.put("success", false);
            data.put("msg", e.getMessage());
            data.put("data", null);
            e.printStackTrace();
        }
        return data;
    }
    
    public static void main(String[] args)
    {
        String s="asdf&gt;32".replaceAll("&gt;", ">");
        System.out.println(s);
    }
    /**
     * 查询提交接口
     * @param id
     * @param sql
     * @param tableName
     * @param type (sparkBases tranfBases kylinBases)
     * @return
     */
    @RequestMapping(value ="query-submit")//repo/RepoStore/query-submit
    @ResponseBody
    public JSONObject gettables(@RequestParam(value="id",required=true)String id,
    		@RequestParam(value="sql",required=true)String sql,
    		@RequestParam(value="tableName",required=true)String tableName,
    		@RequestParam(value="type",required=true)String type)
    {
        JSONObject data = new JSONObject();
        MetaRepo repo = null;
        String ts = "";
        try
        {
        	sql = StringEscapeUtils.unescapeHtml(sql);
            List<String> tables = TestUtil.getAllTableNames(sql);
            if(tables==null || tables.size()==0){
            	data.put("success", false);
                data.put("msg", "sql语句中不存在表信息!");
                data.put("data", null);
                saveDataExploreLog(id, sql, type, data,"");
                return data;
            }else{
            	for(String tb : tables){
            		tb = tb.substring(0, tb.lastIndexOf("."));
            		ts = ts + tb + ",";
            	}
            	ts = ts.substring(0, ts.length()-1);
            }
            String[] ids = {};
        	if(id.contains(",")){
        		ids = id.split(",");
        	}else{
        		ids = new String[]{id};
        	}
        	//used database name
        	List<String> dbNames = new ArrayList<String>();
        	for(String str : tables){
        		String[] s = str.split("\\.");
        		dbNames.add(s[0]);
        	}
        	List<String> inputDbNames = new ArrayList<String>();
        	List<String> resourceTables = new ArrayList<String>(); 
        	for(String repoId : ids){
    			repo = metaRepoService.getCon(repoId);
    			if(repo==null){
    				data.put("success", false);
                    data.put("msg", "输入连接信息不存在");
                    data.put("data", null);
                    saveDataExploreLog(id, sql, type, data,"");
                    return data;
    			}
    			inputDbNames.add(repo.getRepoName());
    			//判断sql语句中的表是否属于输入连接数据源
                Integer metaType = repo.getMetaType();
                if(metaType==1){
                	MetaStore metaStore = new MetaStore();
                	metaStore.setRepoId(repo);
                	metaStore.setCurrentUser(UserUtils.getUser());
                	List<MetaStore> metaStores = metaStoreService.findList(metaStore);
                	if(metaStores!=null && metaStores.size()>0){
                		for (MetaStore ms : metaStores) {
                			resourceTables.add(ms.getStoreFile());
						}
                	}
                }else if(metaType==2){
                	MetaResult metaResult = new MetaResult();
                	metaResult.setRepoId(repo);
                	metaResult.setCurrentUser(UserUtils.getUser());
                	List<MetaResult> metaResults = metaResultService.findList(metaResult);
                	if(metaResults!=null && metaResults.size()>0){
                		for (MetaResult mr : metaResults) {
                			resourceTables.add(mr.getResultFile());
						}
                	}
                }
    		}
        	if(!inputDbNames.containsAll(dbNames)){
    			data.put("success", false);
    			data.put("msg", "sql中的数据库与选择的输入连接数据库信息不一致!");
    			data.put("data", null);
    			saveDataExploreLog(id, sql, type, data,ts);
    			return data;
    		}
        	if(resourceTables.size()==0){
           	 	data.put("success", false);
                data.put("msg", repo.getRepoName() + "库不存在表信息!");
                data.put("data", null);
                saveDataExploreLog(id, sql, type, data,ts);
                return data;
           }else{
               	for(String str : tables){
               		String[] s = str.split("\\.");
               		if(s.length==2){
               			str = s[0];
               		}else if(s.length==3){
               			str = s[1];
               		}
               		if(!resourceTables.contains(str)){
               			data.put("success", false);
               			data.put("msg", "sql中的表信息" + str + "无操作权限");
                       	data.put("data", null);
                       	saveDataExploreLog(id, sql, type, data,ts);
                       	return data;
               		}
               	}
           }
            List<Map<String, String>> beans = Lists.newArrayList();
            String pwd = repo.getUserPwd();
            if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
                pwd = AESUtil.decForTD(pwd);
            }
            if(StringUtils.isNotEmpty(type) && type.equals("kylinBases")){
                beans = DBUtils.executeKylinSql(sql);
            }else{
                if (repo.getRepoType() == 5)
                {
                    beans = DBUtils.excuteSparkSql(repo.getIp(),
                            repo.getPort(),
                            repo.getRepoName(),
                            tableName,
                            sql,
                            repo.getUserName(),
                            pwd);
                    data.put("success", true);
                    data.put("msg", "hive查询成功！");
                    data.put("data", beans);
                }
                else if (repo.getRepoType() == 11)
                {
                    beans = DBUtils.excuteTrafodionSql(repo, tableName, sql);
                    data.put("success", true);
                    data.put("msg", "OLTP查询成功！");
                    data.put("data", beans);
                }else{
                    data.put("success", false);
                    data.put("msg", "不支持此类型数据查询");
                    data.put("data", null);
                }
            }
        }catch (Exception e){
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
        //将查询结果日志更新到data_explore_log表
        saveDataExploreLog(id, sql, type, data,ts);
        return data;
    }
    
    private void saveDataExploreLog(String repoId,String sql,String type,JSONObject data,String tables){
    	DataExploreLog entity = new DataExploreLog();
        entity.setRepoId(repoId);
        entity.setCreateUser(UserUtils.getUser().getLoginName());
        entity.setSearchSql(sql);
        entity.setSearchType(type);
        entity.setSearchStatus(data.getBoolean("success")==true?0:1);
        entity.setSearchTime(DateUtils.getDateTime());
        entity.setDelFlag("0");
        entity.setErrorMsg(data.getString("msg"));
        entity.setRelationTables(tables);
        dataExploreLogService.save(entity);
    }
    
    /**
     * 自助查询专用
     */
    @Autowired
    private ComputeTaskService computeTaskService;
    @Autowired
    private ComputeMetaService computeMetaService;
    /**
     * 自助查询基础数据
     * @param detailBean
     * @return
     */
    @SuppressWarnings("unused")
	private ComputeTaskDetailBean getBaseData(){
        ComputeTaskDetailBean detailBean=new ComputeTaskDetailBean();
        InParam  param = new InParam();
        param.setRepoArray(new int[] { Contants.SOURCE_TYPE_HIVE });
        param.setRepoMetaTypeArray(new int[]{Contants.STORE});
        if(detailBean.getIsAdmin()==1){
            param.setAdmin(true); 
        }else{
            param.setAdmin(false);
        }
        param.setUserid(Integer.parseInt(detailBean.getLoginUser().getId()));
        List<Repositories> list = computeMetaService.getAllRepo(param);
        detailBean.setInputResources(list);
        //根据组的类型获取分组(这里获取计算任务的分组)
        List<ComputeTaskGroup> groups = computeTaskService.getTaskGroup();
        detailBean.setTaskGroups(groups);
        //函数类型列表
        List<ComputeFunction> functions = computeTaskService.getFunctionsByType(Contants.HIVE_FUNCTION_TYPE);
        detailBean.setFunctions(functions);
        
        UserUtils.getSession().setAttribute("detailBean", detailBean);
        return detailBean;
    }
    
}