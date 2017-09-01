package cn.vigor.modules.meta.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.entity.MetaStorePro;
import cn.vigor.modules.meta.service.MetaRepoService;
import cn.vigor.modules.meta.service.MetaSourceService;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 存储数据信息Controller
 * 
 * @author kiss
 * @version 2016-05-17
 */
@Controller
@RequestMapping(value = "${adminPath}/store/metaStore")
public class MetaStoreController extends BaseController {
    @Autowired
    private MetaSourceService metaSourceService;
	@Autowired
	private MetaRepoService metaRepoService;
	@Autowired
	private MetaStoreService metaStoreService;
	
	@ModelAttribute
	public MetaStore get(@RequestParam(required=false) String id) {
		MetaStore entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = metaStoreService.get(id);
		}
		if (entity == null){
			entity = new MetaStore();
		}
		return entity;
	}
	
	/**
	 * 存储数据信息列表页面
	 */
	@RequiresPermissions("store:metaStore:list")
	@RequestMapping(value = {"list", ""})
	public String list(MetaStore metaStore, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MetaStore> page = metaStoreService.findPage(new Page<MetaStore>(request, response), metaStore); 
		page.setOrderBy("updateDate desc");
		model.addAttribute("page", page);
		User user = UserUtils.getUser();
        model.addAttribute("user", user);
		return "modules/meta/store/metaStoreList";
	}

	/**
	 * 查看，增加，编辑存储数据信息表单页面
	 */
	@RequiresPermissions(value={"store:metaStore:view","store:metaStore:add","store:metaStore:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MetaStore metaStore, Model model) {
		model.addAttribute("metaStore", metaStore);
		return "modules/meta/store/metaStoreForm";
	}
	/**
     * 查看
     */
    @RequiresPermissions(value="store:metaStore:view")
    @RequestMapping(value = "view")
    public String view(MetaStore metaStore, Model model) {
        model.addAttribute("metaStore", metaStore);
        return "modules/meta/store/metaStoreView";
    }
	/**
     * 获取数据流
     */
    @RequiresPermissions("store:metaStore:view")
    @RequestMapping(value = "flow")
    public String flow(String name,Model model) {
    	JSONObject data=metaStoreService.getFolwInfo();
        model.addAttribute("data", data); 
        model.addAttribute("name", name); 
        return "modules/meta/flow/metaDataFlow";
    }

	/**
	 * 保存存储数据信息
	 */
	@RequiresPermissions(value={"store:metaStore:add","store:metaStore:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MetaStore metaStore, Model model, RedirectAttributes redirectAttributes) throws Exception{
		try
        {
            if (!beanValidator(model, metaStore)){
            	return form(metaStore, model);
            }
            MetaRepo repo = metaRepoService.get(metaStore.getRepoId());
            if(repo.getRepoType()!=metaStore.getStoreType())
            {
                addMessage(redirectAttributes, "保存失败，连接类型不匹配");
            } else {
                if(!metaStore.getIsNewRecord()){//编辑表单保存
                    MetaSource tempSource=metaSourceService.findUniqueByProperty("source_name", metaStore.getStoreName());
                    if(tempSource!=null )
                    {
                        addMessage(redirectAttributes, "保存失败，数据源信息已存在"+metaStore.getStoreName());
                        return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
                    } 
                    MetaStore t = metaStoreService.get(metaStore.getId());//从数据库取出记录的值
                    if(t.getStoreExternal().contains(";")){
                        metaStore.setStoreExternal(metaStore.getStoreExternal()+t.getStoreExternal().substring(t.getStoreExternal().indexOf(";")));
                    }
                    MyBeanUtils.copyBeanNotNull2Bean(metaStore, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
                    metaStoreService.save(t);//保存
                   
                }else{//新增表单保存
                    MetaSource tempSource=metaSourceService.findUniqueByProperty("source_name", metaStore.getStoreName());
                    MetaStore tempStore= metaStoreService.findMetaStoreByName(metaStore.getStoreName());
                    //TODO 当一个库下的某个表已经被使用过了,那么再次创建平台存储信息时,提示失败
                    List<MetaStore> stores = metaStoreService.getByDbInfoAndStoreType(repo.getIp(), metaStore.getStoreFile(), metaStore.getStoreType(), repo.getRepoName());
                    if(tempSource!=null || tempStore!=null)
                    {
                        addMessage(redirectAttributes, "保存失败，数据源或者存储信息已存在"+metaStore.getStoreName());
                        return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
                    }else if(stores!=null && stores.size()>0){
                    	addMessage(redirectAttributes, "保存失败，存储信息"+stores.get(0).getStoreName()+"已使用表"+metaStore.getStoreFile());
                    	return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
                    }
                    metaStoreService.save(metaStore);//保存
                }
                addMessage(redirectAttributes, "保存存储数据信息成功");
            }
        }
        catch (Exception e)
        {
            String msg=e.getMessage();
            if(msg!=null&&msg.contains("Duplicate")&& msg.contains("storeproname"))
            {
                msg=metaStore.getStoreName()+"保存失败，此名称的存储信息已存在";
            }else  if(msg!=null&&msg.contains("Duplicate")&& msg.contains("storeproname")){
                msg="保存失败,属性名重复";
            }else if(msg!=null&&msg.contains("Duplicate")&& msg.contains("storeproindex")){
                msg="保存失败,属性索引重复";
            }else {
                msg="保存失败：失败原因："+e.getMessage();
            }
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
		return "redirect:"+Global.getAdminPath()+"/store/metaStore/?repage";
	}
	
	/**
	 * 删除存储数据信息
	 */
	@RequiresPermissions("store:metaStore:del")
	@RequestMapping(value = "delete")
	public String delete(MetaStore metaStore, RedirectAttributes redirectAttributes) {
	    MetaStore res=metaStoreService.getFolwInfo(metaStore.getId());
        if(res.getEtlMaps().size()==0 && res.getCalMaps().size()==0 )
        {
            metaStoreService.delete(metaStore);
            addMessage(redirectAttributes, "删除存储数据信息成功");
            
        }else{
            String etlnames="",calnames="";
            for (HashMap<String, Object> jobs : res.getEtlMaps())
            {
                etlnames+=jobs.get("taskName")+"<br/>";
            }
            for (HashMap<String, Object> jobs : res.getCalMaps())
            {
                calnames+=jobs.get("taskName")+"<br/>";
            }
            addMessage(redirectAttributes, "存在关联任务信息:<br/>"+etlnames+calnames+"不支持删除!");
        }
		return "redirect:"+Global.getAdminPath()+"/store/metaStore/?repage";
	}
	
	/**
	 * 批量删除存储数据信息
	 */
	@RequiresPermissions("store:metaStore:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			metaStoreService.delete(metaStoreService.get(id));
		}
		addMessage(redirectAttributes, "删除存储数据信息成功");
		return "redirect:"+Global.getAdminPath()+"/store/metaStore/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("store:metaStore:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MetaStore metaStore, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "存储数据信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<MetaStore> list = metaStoreService.findList(metaStore);
            for (MetaStore source : list)
            {
                source.setRepoId(metaRepoService.get(source.getRepoId()));
            }
    		new ExportExcel("存储数据信息", MetaStore.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出存储数据信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/metaStore/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("store:metaStore:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MetaStore> list = ei.getDataList(MetaStore.class);
			for (MetaStore metaStore : list){
				metaStoreService.save(metaStore);
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条存储数据信息记录");
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入存储数据信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/metaStore/?repage";
    }
	
	/**
	 * 下载导入存储数据信息数据模板
	 */
	@RequiresPermissions("store:metaStore:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "存储数据信息数据导入模板.xlsx";
    		List<MetaStore> list = Lists.newArrayList(); 
    		new ExportExcel("存储数据信息数据", MetaStore.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/store/metaStore/?repage";
    }
	
	/**disable
	 * 启用 
	 * @param id 表id
	 * @return
	 */
	@RequestMapping(value ="enable")
	@ResponseBody
	public JSONObject enable(String id)
	{
		JSONObject data = new JSONObject();
		//获取表信息
		MetaStore  store = metaStoreService.get(id);
		//获取表 或者目录 ip 用户名 密码 端口等  
		if(store.getStoreType()==4){
		    //判断如果是hbase的启用,需要判断其列簇信息是否完整,因为从抽取设置过来的hbase信息缺少列簇信息,这时候提示用户完善列簇信息再进行启用
		    String storeExternal = store.getStoreExternal();
		    if(storeExternal.startsWith(";")){//没有列簇信息,需要完善列簇信息
		        data.put("sucess", false);
		        data.put("msg", "列簇信息为空,请完善列簇信息");
		        return data;
		    }
		    //判断平台存储属性是否设置了列簇字段信息
		    List<MetaStorePro> list = store.getMetaStoreProList();
		    if(list!=null && list.size()>0){
		        for (MetaStorePro metaStorePro : list){
                    if(StringUtils.isEmpty(metaStorePro.getProExternal())){
	                        data.put("sucess", false);
	                        data.put("msg", "平台存储属性列簇信息为空,请完善.");
                        return data;
                    }
                }
		    }
		}
		MetaRepo repo=metaRepoService.get(store.getRepoId());	
		data=metaStoreService.enable(repo,store);					
		return data;
	}
	/**disable
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
        MetaStore  store = metaStoreService.get(id);
        //获取表 或者目录 ip 用户名 密码 端口等  
        MetaRepo repo=metaRepoService.get(store.getRepoId()); 
        data=metaStoreService.disable(repo,store);                   
        return data;
    }
    /**
     * 根据sourceID找到平台存储信息
     * @param id 表id
     * @return
     */
    @RequestMapping(value ="repoInfo")///store/metaStore/repoInfo?id=12
    @ResponseBody
    public JSONObject getMetaStoreById(String id)
    {
        JSONObject data= new JSONObject();
            MetaStore  metaStore = metaStoreService.get(id);
            data.put("data", metaStore);
        return data;
    }
    
    /**
     * 批量启用
     * @param ids 需要启用的id列表
     * @return
     */
    @RequestMapping(value="batchEnable")
    @ResponseBody
    public JSONObject batchEnable(String ids){
        JSONObject data = new JSONObject();
        if(ids!=null && !ids.equals("")){
            String[] repoIds = ids.split(",");
//            int s = 0;
//            int e = 0;
            Map<String,List<Object[]>> map = new HashMap<String,List<Object[]>>();
            for(String str : repoIds)
            {
//                //获取表信息
                MetaStore store = metaStoreService.get(str);
////                //获取表 或者目录 ip 用户名 密码 端口等  
                MetaRepo repo = metaRepoService.get(store.getRepoId());   
//                JSONObject dt = metaStoreService.enable(repo,store);
//                if(dt.getBooleanValue("sucess")){
//                    s = s + 1;
//                }else{
//                    e = e + 1;
//                }
                //对相同存储类型的进行分类
                Object[] obj = new Object[2];
                obj[0] = store;
                obj[1] = repo;
                if(map.get(store.getStoreType()+"")==null){
                    List<Object[]> list = new ArrayList<Object[]>();
                    list.add(obj);
                    map.put(store.getStoreType()+"", list);
                }else{
                    map.get(store.getStoreType()+"").add(obj);
                }
            }
            data = metaStoreService.batchEnable(map);
            //测试注释代码
//            data.put("sucess", true);
//            data.put("msg", "总共启用"+repoIds.length+"条记录,成功"+s+"条,失败"+e+"条");
        }else{
            data.put("sucess", true);
            data.put("msg", "无启用数据");
        }
        return data;
    }
    
    public static void main(String[] args)
    {
        System.out.println(AESUtil.decForTD("Co9ME/Agq664+ZimAcnckA=="));
    }
}