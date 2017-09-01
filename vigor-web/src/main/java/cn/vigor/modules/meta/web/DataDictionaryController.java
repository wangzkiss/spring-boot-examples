package cn.vigor.modules.meta.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.DataDictionary;
import cn.vigor.modules.meta.service.DataDictionaryService;

/**
 * 数据字典Controller
 * @author kiss
 * @version 2016-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/dict/dataDictionary")
public class DataDictionaryController extends BaseController {

	@Autowired
	private DataDictionaryService dataDictionaryService;
	
	@Value("${jdbc.dbname}")
	private String dbname;
	
	
	
	
	/**
	 * 数据字典列表页面
	 */
	@RequiresPermissions("dict:dataDictionary:list")
	@RequestMapping(value = {"list", ""})
	public String list(DataDictionary dataDictionary, HttpServletRequest request, HttpServletResponse response, Model model) {
	    dataDictionary.setDataBaseName(dbname);
		Page<DataDictionary> page = dataDictionaryService.findPage(new Page<DataDictionary>(request, response), dataDictionary); 
		model.addAttribute("page", page);
		return "modules/dict/dataDictionaryList";    
		 
	}

	/**
	 * 查看，增加，编辑数据字典表单页面
	 */
	@RequiresPermissions("dict:dataDictionary:view")
	@RequestMapping(value = "view")
	public String view(DataDictionary dataDictionary, Model model) {
	    dataDictionary.setDataBaseName(dbname);
	    List<DataDictionary> clums= dataDictionaryService.findClums(dataDictionary);
	    DataDictionary table = dataDictionaryService.get(dataDictionary);
		model.addAttribute("clums", clums);
		model.addAttribute("table", table);
		return "modules/dict/dataDictionaryForm";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("dict:dataDictionary:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(DataDictionary dataDictionary, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "数据字典"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            dataDictionary.setDataBaseName(dbname);
            Page<DataDictionary> page = dataDictionaryService.findallPage(new Page<DataDictionary>(request, response, -1), dataDictionary);
    		new ExportExcel("数据字典", DataDictionary.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据字典记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/dict/dataDictionary/?repage";
    }

	
	
	

}
