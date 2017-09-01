package cn.vigor.modules.meta.web;


import java.util.HashMap;
import java.util.List;

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
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.Encodes;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.service.MetaRepoService;
import cn.vigor.modules.meta.service.MetaSourceService;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.meta.util.DBUtils;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 源数据表信息Controller
 * @author kiss
 * @version 2016-05-17
 */
@Controller
@RequestMapping(value = "${adminPath}/source/metaSource")
public class MetaSourceController extends BaseController {

	@Autowired
	private MetaSourceService metaSourceService;
	@Autowired
    private MetaStoreService metaStoreService;
    @Autowired
    private MetaRepoService metaRepoService;
	@ModelAttribute
	public MetaSource get(@RequestParam(required=false) String id) {
		MetaSource entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = metaSourceService.get(id);
		}
		if (entity == null){
			entity = new MetaSource();
		}
		return entity;
	}
	
	/**
	 * 源数据表信息列表页面
	 */
	@RequiresPermissions("source:metaSource:list")
	@RequestMapping(value = {"list", ""})
	public String list(MetaSource metaSource, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MetaSource> page = metaSourceService.findPage(new Page<MetaSource>(request, response), metaSource); 
		model.addAttribute("page", page);
	      User user = UserUtils.getUser();
	        model.addAttribute("user", user);
		return "modules/meta/source/metaSourceList";
	}

	/**
	 * 查看，增加，编辑源数据表信息表单页面
	 */
	@RequiresPermissions(value={"source:metaSource:view","source:metaSource:add","source:metaSource:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MetaSource metaSource, Model model) {
	    
		model.addAttribute("metaSource", metaSource);
		return "modules/meta/source/metaSourceForm";
	}

	/**
     * 查看，增加，编辑源数据表信息表单页面
     */
    @RequiresPermissions(value="source:metaSource:view")
    @RequestMapping(value = "view")
    public String view(String id, Model model) {
        MetaSource metaSource = null;
        if (StringUtils.isNotBlank(id)){
            metaSource = metaSourceService.get(id);
        }
        if (metaSource == null){
            metaSource = new MetaSource();
        }
        model.addAttribute("metaSource", metaSource);
        return "modules/meta/source/metaSourceView";
    }
	/**
	 * 保存源数据表信息
	 */
	@RequiresPermissions(value={"source:metaSource:add","source:metaSource:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MetaSource metaSource, Model model, RedirectAttributes redirectAttributes) throws Exception{
		try
        {
            if (!beanValidator(model, metaSource)){
            	return form(metaSource, model);
            }
            if(metaRepoService.get(metaSource.getRepoId()).getRepoType()!=metaSource.getSourceType())
            {
                addMessage(redirectAttributes, "保存失败，连接类型不匹配");  
            } else {
                
                if(!metaSource.getIsNewRecord()){//编辑表单保存
                    MetaStore tempStore= metaStoreService.findMetaStoreByName(metaSource.getSourceName());
                    if(tempStore!=null)
                    {
                        addMessage(redirectAttributes, "保存失败，存储信息已存在"+metaSource.getSourceName());
                        return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
                    } 
                    MetaSource t = metaSourceService.get(metaSource.getId());//从数据库取出记录的值
                    if(t.getExternal().contains(";")){
                        metaSource.setExternal(metaSource.getExternal()+t.getExternal().substring(t.getExternal().indexOf(";")));
                    }
                    MyBeanUtils.copyBeanNotNull2Bean(metaSource, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
                    metaSourceService.save(t);//保存
                } else {//新增表单保存
                    MetaSource tempSource=metaSourceService.findUniqueByProperty("source_name", metaSource.getSourceName());
                    MetaStore tempStore= metaStoreService.findMetaStoreByName(metaSource.getSourceName());
                    if(tempSource!=null || tempStore!=null)
                    {
                        addMessage(redirectAttributes, "保存失败，数据源或者存储信息已存在"+metaSource.getSourceName());
                        return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
                    } 
                    String mappingStr = StringUtils.createRandomNumStr(6);
                    if(metaSource.getSourceType()==4 && !metaSource.getExternal().contains(";")){
                        metaSource.setExternal(metaSource.getExternal() + ";" + mappingStr);
                    }
                    metaSourceService.save(metaSource);//保存
                    if(metaSource.getSourceType()==4 && !metaSource.getSourceName().contains("${")){
                    	//如果是新建hbase,需要在创建时即启用表(外部数据源没有启用功能,这时候就涉及到hbase的mapping的问题)
                        DBUtils.createHbaseTable(metaSource);
                    }
                }
                addMessage(redirectAttributes, "保存源数据表信息成功");
            }
        }
        catch (Exception e)
        {
            String msg=e.getMessage();
            if(msg.contains("Duplicate")&& msg.contains("sourcename"))
            {
                msg=metaSource.getSourceName()+"保存失败，此名称的数据源已存在";
            }else  if(msg.contains("Duplicate")&& msg.contains("sourceproname")){
                msg="保存失败,属性名重复";
            }else if(msg.contains("Duplicate")&& msg.contains("sourceproindex")){
                msg="保存失败,属性索引重复";
            }else {
                msg="保存失败：失败原因："+e.getMessage();
            }
            logger.info("结果:"+msg);
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
		return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
	}
	
	/**
	 * 删除源数据表信息
	 */
	@RequiresPermissions("source:metaSource:del")
	@RequestMapping(value = "delete")
	public String delete(MetaSource metaSource, RedirectAttributes redirectAttributes) {
	    MetaSource res=metaSourceService.getFolwInfo(metaSource.getId());
        if(res.getEtlMaps().size()==0)
        {
            metaSourceService.delete(metaSource);
            addMessage(redirectAttributes, "删除源数据表信息成功");
        }else{
            String names="";
            for (HashMap<String, Object> jobs : res.getEtlMaps())
            {
                names+=jobs.get("taskName")+"<br/>";
            }
            addMessage(redirectAttributes, "存在关联ETL任务信息:<br/>"+names+"不支持删除!");
        }
		return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
	}
	
	/**
	 * 批量删除源数据表信息
	 */
	@RequiresPermissions("source:metaSource:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			metaSourceService.delete(metaSourceService.get(id));
		}
		addMessage(redirectAttributes, "删除源数据表信息成功");
		return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("source:metaSource:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MetaSource metaSource, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "源数据表信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<MetaSource> list = metaSourceService.findList(metaSource);
            for (MetaSource source : list)
            {
                source.setRepoId(metaRepoService.get(source.getRepoId()));
            }
    		new ExportExcel("源数据表信息", MetaSource.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出源数据表信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
    }

	/**
     * 导出excel文件
     */
    @RequiresPermissions("source:metaSource:export")
    @RequestMapping(value = "dataPro")
    @ResponseBody
    public void exportDataPro(MetaSource metaSource, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "datapro.txt";
            String content="#####说明##########################\r\n"
                    + "#######数据属性文件以逗号隔开#######\r\n"
                    + "##序号,名称,数据类型,数据格式,备注信息\r\n"
                    + "1,id,int,dd,id主键\r\n"
                    + "2,name,varchar,,名称\r\n"
                    + "3,date,datetime,YYYY-dd-mm hh:mm:ss,时间\r\n"
                    + "";
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename="+Encodes.urlEncode(fileName));
            response.getOutputStream().write(content.getBytes());
            
        } catch (Exception e) {
            
        }
    }
	
	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("source:metaSource:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MetaSource> list = ei.getDataList(MetaSource.class);
			for (MetaSource metaSource : list){
				metaSourceService.save(metaSource);
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条源数据表信息记录");
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入源数据表信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
    }
	
	/**
	 * 下载导入源数据表信息数据模板
	 */
	@RequiresPermissions("source:metaSource:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "源数据表信息数据导入模板.xlsx";
    		List<MetaSource> list = Lists.newArrayList(); 
    		new ExportExcel("源数据表信息数据", MetaSource.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/source/metaSource/?repage";
    }
	
	/**
     * 获取数据流向
     */
    @RequiresPermissions("source:metaSource:view")
    @RequestMapping(value = "flow")
    public String flow(String id, Model model) {
        MetaSource metaSource =metaSourceService.getFolwInfo(id);
        model.addAttribute("metaData", metaSource);
        return "modules/meta/flow/metaDataFlow";
    }
    
    /**
     * 根据storeID找到平台存储信息
     * @param id 表id
     * @return
     */
    @RequestMapping(value ="repoInfo")///source/metaSource/repoInfo?id=12
    @ResponseBody
    public JSONObject getMetaSourceById(String id)
    {
        JSONObject data= new JSONObject();
        MetaSource  metaSource = metaSourceService.get(id);
        data.put("data", metaSource);
        return data;
    }
}