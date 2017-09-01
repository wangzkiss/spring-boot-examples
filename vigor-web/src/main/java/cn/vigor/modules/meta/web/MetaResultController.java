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
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaResult;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.service.MetaRepoService;
import cn.vigor.modules.meta.service.MetaResultService;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 结果数据集Controller
 * @author kiss
 * @version 2016-05-17
 */
@Controller
@RequestMapping(value = "${adminPath}/result/metaResult")
public class MetaResultController extends BaseController {

	@Autowired
	private MetaResultService metaResultService;
	
	@ModelAttribute
	public MetaResult get(@RequestParam(required=false) String id) {
		MetaResult entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = metaResultService.get(id);
		}
		if (entity == null){
			entity = new MetaResult();
		}
		return entity;
	}
	
	/**
	 * 结果数据集列表页面
	 */
	@RequiresPermissions("result:metaResult:list")
	@RequestMapping(value = {"list", ""})
	public String list(MetaResult metaResult, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MetaResult> page = metaResultService.findPage(new Page<MetaResult>(request, response), metaResult); 
		model.addAttribute("page", page);
		User user = UserUtils.getUser();
        model.addAttribute("user", user);
		return "modules/meta/result/metaResultList";
	}

	/**
	 * 查看，增加，编辑结果数据集表单页面
	 */
	@RequiresPermissions(value={"result:metaResult:view","result:metaResult:add","result:metaResult:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MetaResult metaResult, Model model) {
		model.addAttribute("metaResult", metaResult);
		return "modules/meta/result/metaResultForm";
	}
	
	/**
     * 查看 
     */
    @RequiresPermissions(value="result:metaResult:view")
    @RequestMapping(value = "view")
    public String view(MetaResult metaResult, Model model) {
        model.addAttribute("metaResult", metaResult);
        return "modules/meta/result/metaResultView";
    }
    @RequestMapping(value ="repoInfo")///result/metaResult/repoInfo?id=12
    @ResponseBody
    public JSONObject getMetaStoreById(String id)
    {
        JSONObject data= new JSONObject();
            data.put("data", metaResultService.get(id));
        return data;
    }
	/**
     * 获取工作流
     */
    @RequiresPermissions("result:metaResult:view")
    @RequestMapping(value = "flow")
    public String flow(String id, Model model) {
        MetaResult metaResult=metaResultService.getFolwInfo(id);
        model.addAttribute("metaData", metaResult);
        return "modules/meta/flow/metaDataFlow";
    }
	/**
	 * 保存结果数据集
	 */
	@RequiresPermissions(value={"result:metaResult:add","result:metaResult:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MetaResult metaResult, Model model, RedirectAttributes redirectAttributes) throws Exception{
		try
        {
            if (!beanValidator(model, metaResult)){
            	return form(metaResult, model);
            }
            
            if(metaRepoService.get(metaResult.getRepoId()).getRepoType()!=metaResult.getResultType())
            {
                addMessage(redirectAttributes, "连接类型不匹配");
                
            }else{
                if(!metaResult.getIsNewRecord()){//编辑表单保存
                    MetaResult t = metaResultService.get(metaResult.getId());//从数据库取出记录的值
                    MyBeanUtils.copyBeanNotNull2Bean(metaResult, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
                    metaResultService.save(t);//保存
                }else{//新增表单保存
                    metaResultService.save(metaResult);//保存
                }
                addMessage(redirectAttributes, "保存结果数据集成功");
            }
        }
        catch (Exception e)
        {
            String msg=e.getMessage();
            if(msg.contains("Duplicate")&& msg.contains("result_name"))
            {
                msg="保存失败,"+metaResult.getResultName()+"名已存在";
            }else  if(msg.contains("Duplicate")&& msg.contains("resultproname")){
                msg="保存失败, 属性名重复";
            }else if(msg.contains("Duplicate")&& msg.contains("resultproindex")){
                msg="保存失败,属性索引重复";
            }else {
                msg="保存失败：失败原因："+e.getMessage();
            }
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
		return "redirect:"+Global.getAdminPath()+"/result/metaResult/?repage";
	}
	
	/**
	 * 删除结果数据集
	 */
	@RequiresPermissions("result:metaResult:del")
	@RequestMapping(value = "delete")
	public String delete(MetaResult metaResult, RedirectAttributes redirectAttributes) {
	    
	    MetaResult res=metaResultService.getFolwInfo(metaResult.getId());
	    if(res.getCalMaps().size()==0)
	    {
	        metaResultService.delete(metaResult);
	        addMessage(redirectAttributes, "删除结果数据集成功");
	    }else{
	        String names="";
            for (HashMap<String, Object> jobs : res.getCalMaps())
            {
                names+=jobs.get("taskName")+"<br/>";
            }
            addMessage(redirectAttributes, "存在关联计算任务信息:<br/>"+names+"不支持删除!");
	    }
		return "redirect:"+Global.getAdminPath()+"/result/metaResult/?repage";
	}
	
	/**
	 * 批量删除结果数据集
	 */
	@RequiresPermissions("result:metaResult:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			metaResultService.delete(metaResultService.get(id));
		}
		addMessage(redirectAttributes, "删除结果数据集成功");
		return "redirect:"+Global.getAdminPath()+"/result/metaResult/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("result:metaResult:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MetaResult metaResult, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "结果数据集"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MetaResult> page = metaResultService.findPage(new Page<MetaResult>(request, response, -1), metaResult);
    		new ExportExcel("结果数据集", MetaResult.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出结果数据集记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/result/metaResult/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("result:metaResult:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MetaResult> list = ei.getDataList(MetaResult.class);
			for (MetaResult metaResult : list){
				metaResultService.save(metaResult);
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条结果数据集记录");
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入结果数据集失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/result/metaResult/?repage";
    }
	
	/**
	 * 下载导入结果数据集数据模板
	 */
	@RequiresPermissions("result:metaResult:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "结果数据集数据导入模板.xlsx";
    		List<MetaResult> list = Lists.newArrayList(); 
    		new ExportExcel("结果数据集数据", MetaResult.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/result/metaResult/?repage";
    }
	
    @Autowired
    private MetaRepoService metaRepoService;
	/**
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
        MetaResult  result = metaResultService.get(id);
        //获取表 或者目录 ip 用户名 密码 端口等  
        MetaRepo repo=metaRepoService.get(result.getRepoId());   
        data=metaResultService.enable(repo,result);                   
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
        MetaResult  result = metaResultService.get(id);
        //获取表 或者目录 ip 用户名 密码 端口等  
        MetaRepo repo=metaRepoService.get(result.getRepoId());   
        data=metaResultService.disable(repo,result);                   
        return data;
    }
}