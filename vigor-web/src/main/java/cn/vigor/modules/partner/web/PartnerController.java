/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.partner.web;

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

import com.google.common.collect.Lists;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.partner.entity.Partner;
import cn.vigor.modules.partner.service.PartnerService;

/**
 * 接入方管理Controller
 * @author liminfang
 * @version 2016-07-06
 */
@Controller
@RequestMapping(value = "${adminPath}/partner/partner")
public class PartnerController extends BaseController {

	@Autowired
	private PartnerService partnerService;
	
	@ModelAttribute
	public Partner get(@RequestParam(required=false) String id) {
		Partner entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = partnerService.get(id);
		}
		if (entity == null){
			entity = new Partner();
		}
		return entity;
	}
	
	/**
	 * 接入方列表页面
	 */
	@RequiresPermissions("partner:partner:list")
	@RequestMapping(value = {"list", ""})
	public String list(Partner partner, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Partner> page = partnerService.findPage(new Page<Partner>(request, response), partner); 
		model.addAttribute("page", page);
		return "modules/partner/partnerList";
	}
	
    /**
     * 查看，接入方表单页面
     */
    @RequiresPermissions(value={"partner:partner:view"},logical=Logical.OR)
    @RequestMapping(value = "form_view")
    public String form_view(Partner partner, Model model) {
        model.addAttribute("partner", partner);
        return "modules/partner/partnerForm_view";
    }

    /**
     * 增加，编辑 接入方表单页面
     */
    @RequiresPermissions(value={"partner:partner:add","partner:partner:edit"},logical=Logical.OR)
    @RequestMapping(value = "form_add_edit")
    public String form_add_edit(Partner partner, Model model) {
        model.addAttribute("partner", partner);
        return "modules/partner/partnerForm_add_edit";
    }
    
    /**
     * 授权服务 接入方表单页面
     */
    @RequiresPermissions(value={"partner:partner:assign"},logical=Logical.OR)
    @RequestMapping(value = "form_assign")
    public String form_assign(Partner partner, Model model) {
        partner.setHandleType(2);
        model.addAttribute("partner", partner);
        return "modules/partner/partnerForm_assign";
    }
    
    /**
     * 设置IP白名单 接入方表单页面
     */
    @RequiresPermissions(value={"partner:partner:setWhiteIp"},logical=Logical.OR)
    @RequestMapping(value = "form_set_whiteip")
    public String form_set_whiteip(Partner partner, Model model) {
        partner.setHandleType(1);
        model.addAttribute("partner", partner);
        return "modules/partner/partnerForm_set_whiteip";
    }
    
    /**
     * 审核 接入方
     */
    @RequiresPermissions(value={"partner:partner:audit"},logical=Logical.OR)
    @RequestMapping(value = "audit")
    public String audit(Partner partner, RedirectAttributes redirectAttributes) {
        partnerService.audit(partner);
        addMessage(redirectAttributes, "接入方审核成功");
        return "redirect:"+Global.getAdminPath()+"/partner/partner/?repage";
    }
    
	/**
	 * 查看，增加，编辑接入方表单页面
	 */
	@RequiresPermissions(value={"partner:partner:view","partner:partner:add","partner:partner:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Partner partner, Model model) {
		model.addAttribute("partner", partner);
		return "modules/partner/partnerForm";
	}

	/**
	 * 保存接入方
	 */
	@RequiresPermissions(value={"partner:partner:add","partner:partner:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Partner partner, Model model, RedirectAttributes redirectAttributes) throws Exception{
		/*if (!beanValidator(model, partner)){
			return form(partner, model);
		}*/
		try {
		    if(!partner.getIsNewRecord()){//编辑表单保存
	            //Partner t = partnerService.get(partner.getId());//从数据库取出记录的值
	            //MyBeanUtils.copyBeanNotNull2Bean(partner, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
	            partnerService.save(partner);//保存
	        }else{//新增表单保存
	            partnerService.save(partner);//保存
	        }
	        addMessage(redirectAttributes, "保存接入方成功");
		} catch(Exception e) {
		    addMessage(redirectAttributes, "保存接入方失败，请查看系统日志");
		}
		return "redirect:"+Global.getAdminPath()+"/partner/partner/?repage";
	}
	
	/**
	 * 删除接入方
	 */
	@RequiresPermissions("partner:partner:del")
	@RequestMapping(value = "delete")
	public String delete(Partner partner, RedirectAttributes redirectAttributes) {
		try {
		    partnerService.delete(partner);
	        addMessage(redirectAttributes, "删除接入方成功");
		} catch(Exception e) {
		    addMessage(redirectAttributes, "删除接入方异常，请查看系统日志");
		}
		return "redirect:"+Global.getAdminPath()+"/partner/partner/?repage";
	}
	
	/**
	 * 批量删除接入方
	 */
	@RequiresPermissions("partner:partner:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		try {
		    String idArray[] =ids.split(",");
	        for(String id : idArray){
	            partnerService.delete(partnerService.get(id));
	        }
	        addMessage(redirectAttributes, "删除接入方成功");
		} catch(Exception e) {
		    addMessage(redirectAttributes, "删除接入方异常，请查看系统日志");
		}
		return "redirect:"+Global.getAdminPath()+"/partner/partner/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("partner:partner:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Partner partner, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "接入方"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Partner> page = partnerService.findPage(new Page<Partner>(request, response, -1), partner);
    		new ExportExcel("接入方", Partner.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出接入方记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/partner/partner/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("partner:partner:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Partner> list = ei.getDataList(Partner.class);
			for (Partner partner : list){
				try{
					partnerService.save(partner);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条接入方记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条接入方记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入接入方失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/partner/partner/?repage";
    }
	
	/**
	 * 下载导入接入方数据模板
	 */
	@RequiresPermissions("partner:partner:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "接入方数据导入模板.xlsx";
    		List<Partner> list = Lists.newArrayList(); 
    		new ExportExcel("接入方数据", Partner.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/partner/partner/?repage";
    }
	
	
	

}