package cn.vigor.modules.meta.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.DataExploreLog;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.TaskHourReport;
import cn.vigor.modules.meta.service.DataExploreLogService;
import cn.vigor.modules.meta.service.MetaRepoService;
import cn.vigor.modules.meta.util.DBUtils;
import cn.vigor.modules.meta.util.SqlBasicFormatterUtil;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/del/dataExploreLog")
public class DataExploreLogController extends BaseController {
	
	@Autowired
	private DataExploreLogService dataExploreLogService;
	
	@Autowired
	private MetaRepoService metaRepoService;
	
	@ModelAttribute
	public DataExploreLog get(@RequestParam(required=false) String id) {
		DataExploreLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dataExploreLogService.get(id);
		}
		if (entity == null){
			entity = new DataExploreLog();
		}
		return entity;
	}
	
	
	/**
	 * 根据主键ID获取信息
	 * @param id 主键
	 * @return 结果
	 */
	@RequestMapping(value = "getById")
	@ResponseBody
	public String getById(@RequestParam(value="id",required=true)String id){
		DataExploreLog dataExploreLog = dataExploreLogService.get(id);
		return JSONObject.toJSONString(dataExploreLog);
	}
	
	/**
	 * 数据探索记录列表
	 * @param dataExploreLog 查询条件
	 * @param request
	 * @param response
	 * @return 结果
	 */
	@RequestMapping(value = {"list", ""})
	public String list(DataExploreLog dataExploreLog, HttpServletRequest request, HttpServletResponse response,Model model) {
		Page<DataExploreLog> p = new Page<DataExploreLog>(request, response);
		p.setPageSize(5);
		if(StringUtils.isEmpty(dataExploreLog.getSearchSql())){
			dataExploreLog.setSearchSql(null);
		}
		Page<DataExploreLog> page = dataExploreLogService.findPage(p, dataExploreLog); 
		User user = UserUtils.getUser();
		model.addAttribute("page", page);
		model.addAttribute("user", user);
		model.addAttribute("dataExploreLog", dataExploreLog);
		return "modules/tji/dataExpLogList";
	}
	
	/**
	 * 美化sql
	 * @param sql sql语句
	 * @return 结果
	 */
	@RequestMapping(value = {"formatSql"})
	@ResponseBody
	public String formatSql(@RequestParam(value="sql",required=true)String sql){
		sql = SqlBasicFormatterUtil.format(StringEscapeUtils.unescapeHtml(sql));
		return JSONObject.toJSONString(sql);
	}
	
	/**
	 * 导出数据探索结果信息到excel
	 * @param data 查询结果
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "export", method=RequestMethod.GET)
	public String exportExcel(@RequestParam(value="id",required=true)String id,
    		@RequestParam(value="sql",required=true)String sql,
    		@RequestParam(value="tableName",required=true)String tableName,
    		@RequestParam(value="type",required=true)String type,HttpServletRequest request, 
			HttpServletResponse response, RedirectAttributes redirectAttributes){
		MetaRepo repo = null;
		try {
			if(id.contains(",")){
				id = id.split(",")[0];
			}
			repo = metaRepoService.getCon(id);
			List<Map<String, String>> beans = Lists.newArrayList();
            String pwd = repo.getUserPwd();
            if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
                pwd = AESUtil.decForTD(pwd);
            }
            sql = StringEscapeUtils.unescapeHtml(sql);
            if(StringUtils.isNotEmpty(type) && type.equals("kylinBases")){
                beans = DBUtils.executeKylinSql(sql);
            }else{
                if (repo.getRepoType() == 5){
                    beans = DBUtils.excuteSparkSql(repo.getIp(),
                            repo.getPort(),
                            repo.getRepoName(),
                            tableName,
                            sql,
                            repo.getUserName(),
                            pwd);
                }else if (repo.getRepoType() == 11){
                    beans = DBUtils.excuteTrafodionSql(repo, tableName, sql);
                }
            }
            String fileName = "dataExploreResult"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            //获取查询列表的字段key列表
            List<String> keys = new ArrayList<String>();
            Set<Entry<String,String>> set = beans.get(0).entrySet();
            for (Entry<String, String> entry : set) {
            	keys.add(entry.getKey());
			}
            ExportExcel excel = new ExportExcel("数据探索结果信息", keys);
            for(Map<String,String> m : beans){
            	Row row = excel.addRow();
            	int clomn = 0;
            	for (Entry<String, String> entry : m.entrySet()) {
            		excel.addCell(row, clomn, entry.getValue());
            		clomn++;
            	}
            }
            excel.write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, "导出数据探索结果信息记录失败！失败信息："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 跳转到查询结果页面
	 * @param data
	 * @param model
	 * @return
	 */
	@RequestMapping(value="showQueryResult")
	public String showQueryResult(String data,Model model){
		data = StringEscapeUtils.unescapeHtml(data);
		model.addAttribute("data", data);
		return "modules/tji/showQueryResult";
	}
	
	@RequestMapping(value="test")
	public String test(){
		TaskHourReport hourReport = new TaskHourReport();
		hourReport.setShour(24);
		return "";
	}
}
