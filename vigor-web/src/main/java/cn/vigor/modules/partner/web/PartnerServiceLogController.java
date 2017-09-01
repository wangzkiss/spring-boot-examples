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
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.partner.entity.PartnerServiceLog;
import cn.vigor.modules.partner.service.PartnerServiceLogService;

/**
 * 接入方管理Controller
 * @author liminfang
 * @version 2016-07-06
 */
@Controller
@RequestMapping(value = "${adminPath}/partner/partnerServiceLog")
public class PartnerServiceLogController extends BaseController {

	@Autowired
	private PartnerServiceLogService partnerServiceLogService;
	
	@ModelAttribute
	public PartnerServiceLog get(@RequestParam(required=false) String id) {
		PartnerServiceLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = partnerServiceLogService.get(id);
		}
		if (entity == null){
			entity = new PartnerServiceLog();
		}
		return entity;
	}
	
	/**
	 * 接入方列表页面
	 */
	@RequiresPermissions("partner:partnerServiceLog:list")
	@RequestMapping(value = {"list", ""})
	public String list(PartnerServiceLog partnerServiceLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PartnerServiceLog> page = partnerServiceLogService.findPage(new Page<PartnerServiceLog>(request, response), partnerServiceLog); 
		model.addAttribute("page", page);
		return "modules/partner/partnerServiceLogList";
	}

	/**
	 * 查看，增加，编辑接入方表单页面
	 */
	@RequiresPermissions(value={"partner:partnerServiceLog:view","partner:partnerServiceLog:add","partner:partnerServiceLog:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PartnerServiceLog partnerServiceLog, Model model) {
		model.addAttribute("partnerServiceLog", partnerServiceLog);
		return "modules/partner/partnerServiceLogForm";
	}

	/**
	 * 保存接入方
	 */
	@RequiresPermissions(value={"partner:partnerServiceLog:add","partner:partnerServiceLog:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(PartnerServiceLog partnerServiceLog, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, partnerServiceLog)){
			return form(partnerServiceLog, model);
		}
		if(!partnerServiceLog.getIsNewRecord()){//编辑表单保存
			PartnerServiceLog t = partnerServiceLogService.get(partnerServiceLog.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(partnerServiceLog, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			partnerServiceLogService.save(t);//保存
		}else{//新增表单保存
			partnerServiceLogService.save(partnerServiceLog);//保存
		}
		addMessage(redirectAttributes, "保存接入方成功");
		return "redirect:"+Global.getAdminPath()+"/partner/partnerServiceLog/?repage";
	}
	
	/**
	 * 删除接入方
	 */
	@RequiresPermissions("partner:partnerServiceLog:del")
	@RequestMapping(value = "delete")
	public String delete(PartnerServiceLog partnerServiceLog, RedirectAttributes redirectAttributes) {
		partnerServiceLogService.delete(partnerServiceLog);
		addMessage(redirectAttributes, "删除接入方成功");
		return "redirect:"+Global.getAdminPath()+"/partner/partnerServiceLog/?repage";
	}
	
	/**
	 * 批量删除接入方
	 */
	@RequiresPermissions("partner:partnerServiceLog:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			partnerServiceLogService.delete(partnerServiceLogService.get(id));
		}
		addMessage(redirectAttributes, "删除接入方成功");
		return "redirect:"+Global.getAdminPath()+"/partner/partnerServiceLog/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("partner:partnerServiceLog:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(PartnerServiceLog partnerServiceLog, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "接入方"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PartnerServiceLog> page = partnerServiceLogService.findPage(new Page<PartnerServiceLog>(request, response, -1), partnerServiceLog);
    		new ExportExcel("接入方", PartnerServiceLog.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出接入方记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/partner/partnerServiceLog/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("partner:partnerServiceLog:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PartnerServiceLog> list = ei.getDataList(PartnerServiceLog.class);
			for (PartnerServiceLog partnerServiceLog : list){
				try{
					partnerServiceLogService.save(partnerServiceLog);
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
		return "redirect:"+Global.getAdminPath()+"/partner/partnerServiceLog/?repage";
    }
	
	/**
	 * 下载导入接入方数据模板
	 */
	@RequiresPermissions("partner:partnerServiceLog:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "接入方数据导入模板.xlsx";
    		List<PartnerServiceLog> list = Lists.newArrayList(); 
    		new ExportExcel("接入方数据", PartnerServiceLog.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/partner/partnerServiceLog/?repage";
    }
	
	
	

}