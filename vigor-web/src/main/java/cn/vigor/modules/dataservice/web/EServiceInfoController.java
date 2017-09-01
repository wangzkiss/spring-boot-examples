/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.dataservice.entity.EServiceInfo;
import cn.vigor.modules.dataservice.service.EServiceInfoService;

/**
 * 数据服务管理Controller
 * @author liminfang
 * @version 2016-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/dataservice/eServiceInfo")
public class EServiceInfoController extends BaseController {

	@Autowired
	private EServiceInfoService eServiceInfoService;
	
	@ModelAttribute
	public EServiceInfo get(@RequestParam(required=false) Integer serviceId) {
		EServiceInfo entity = null;
		if (null != serviceId){
			entity = eServiceInfoService.get(serviceId);
		}
		if (entity == null){
			entity = new EServiceInfo();
		}
		return entity;
	}

   @RequestMapping(value = {"metapro-list"})
    public String metapro_list(EServiceInfo eServiceInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
       String str = JSONObject.toJSONString(eServiceInfoService.getMetaProList(eServiceInfo.getFromType(), eServiceInfo.getDataId()));
       try {
           response.getOutputStream().write(str.getBytes("utf-8"));
       } catch(Exception e) {
           
       }
       return null;
    }
	
	/**
	 * 数据服务管理列表页面
	 */
	@RequiresPermissions("dataservice:eServiceInfo:list")
	@RequestMapping(value = {"list", ""})
	public String list(EServiceInfo eServiceInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("eServiceInfo", eServiceInfo);
		Page<EServiceInfo> page = eServiceInfoService.findPage(new Page<EServiceInfo>(request, response), eServiceInfo); 
		model.addAttribute("page", page);
		return "modules/dataservice/eServiceInfoList";
	}

	/**
     * 查看，数据服务管理表单页面
     */
    @RequiresPermissions(value={"dataservice:eServiceInfo:view"},logical=Logical.OR)
    @RequestMapping(value = "form_view")
    public String form_view(EServiceInfo eServiceInfo, Model model) {
        model.addAttribute("eServiceInfo", eServiceInfo);
        return "modules/dataservice/eServiceInfoForm_view";
    }
    
    /**
     * 为服务配置函数，数据服务管理表单页面
     */
    @RequiresPermissions(value={"dataservice:eServiceInfo:assign"},logical=Logical.OR)
    @RequestMapping(value = "form_assign")
    public String form_assign(EServiceInfo eServiceInfo, Model model) {
        model.addAttribute("eServiceInfo", eServiceInfo);
        return "modules/dataservice/eServiceInfoForm_assign";
    }

    /**
     * 编辑，数据服务管理表单页面
     */
    @RequiresPermissions(value={"dataservice:eServiceInfo:add", "dataservice:eServiceInfo:edit"},logical=Logical.OR)
    @RequestMapping(value = "form_add_edit")
    public String form_add_edit(EServiceInfo eServiceInfo, Model model) {
        model.addAttribute("eServiceInfo", eServiceInfo);
        return "modules/dataservice/eServiceInfoForm_add_edit";
    }

	
	/**
	 * 查看，增加，编辑数据服务管理表单页面
	 */
	@RequiresPermissions(value={"dataservice:eServiceInfo:view","dataservice:eServiceInfo:add","dataservice:eServiceInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(EServiceInfo eServiceInfo, Model model) {
		model.addAttribute("eServiceInfo", eServiceInfo);
		return "modules/dataservice/eServiceInfoForm";
	}

	/**
	 * 保存数据服务管理
	 */
	@RequiresPermissions(value={"dataservice:eServiceInfo:add","dataservice:eServiceInfo:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(EServiceInfo eServiceInfo, Model model, RedirectAttributes redirectAttributes) throws Exception{	 
	    /*if (!beanValidator(model, eServiceInfo)){
			return form(eServiceInfo, model);
		}*/
		try {
		    if(!eServiceInfo.getIsNewRecord()){//编辑表单保存
	            //EServiceInfo t = eServiceInfoService.get(eServiceInfo.getId());//从数据库取出记录的值
	            //MyBeanUtils.copyBeanNotNull2Bean(eServiceInfo, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
	            eServiceInfoService.save(eServiceInfo);//保存
	        }else{//新增表单保存
	            eServiceInfoService.save(eServiceInfo);//保存
	        }
	        addMessage(redirectAttributes, "保存数据服务成功");
		} catch(Exception e) {
		    addMessage(redirectAttributes, "保存数据服务异常，请查看系统日志");
		}
		return "redirect:"+Global.getAdminPath()+"/dataservice/eServiceInfo/?repage";
	}
	
	/**
	 * 删除数据服务管理
	 */
	@RequiresPermissions("dataservice:eServiceInfo:del")
	@RequestMapping(value = "delete")
	public String delete(EServiceInfo eServiceInfo, RedirectAttributes redirectAttributes) {
		try {
		    eServiceInfoService.delete(eServiceInfo);
	        addMessage(redirectAttributes, "删除数据服务成功");
		} catch(Exception e) {
		    addMessage(redirectAttributes, "删除数据服务异常，请查看系统日志");
		}
		return "redirect:"+Global.getAdminPath()+"/dataservice/eServiceInfo/?repage";
	}
	
	/**
	 * 批量删除数据服务管理
	 */
	@RequiresPermissions("dataservice:eServiceInfo:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		try {
		    String idArray[] =ids.split(",");
	        for(String id : idArray){
	            eServiceInfoService.delete(eServiceInfoService.get(id));
	        }
	        addMessage(redirectAttributes, "删除数据服务成功");
		} catch(Exception e) {
            addMessage(redirectAttributes, "删除数据服务异常，请查看系统日志");
        }
		return "redirect:"+Global.getAdminPath()+"/dataservice/eServiceInfo/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("dataservice:eServiceInfo:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(EServiceInfo eServiceInfo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "数据服务"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<EServiceInfo> page = eServiceInfoService.findPage(new Page<EServiceInfo>(request, response, -1), eServiceInfo);
    		new ExportExcel("数据服务", EServiceInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据服务记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dataservice/eServiceInfo/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("dataservice:eServiceInfo:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<EServiceInfo> list = ei.getDataList(EServiceInfo.class);
			for (EServiceInfo eServiceInfo : list){
				try{
					eServiceInfoService.save(eServiceInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条数据服务管理记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条数据服务管理记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入数据服务管理失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dataservice/eServiceInfo/?repage";
    }
	
	/**
	 * 下载导入数据服务管理数据模板
	 */
	@RequiresPermissions("dataservice:eServiceInfo:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "数据服务管理数据导入模板.xlsx";
    		List<EServiceInfo> list = Lists.newArrayList(); 
    		new ExportExcel("数据服务管理数据", EServiceInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dataservice/eServiceInfo/?repage";
    }
	
	
	

}