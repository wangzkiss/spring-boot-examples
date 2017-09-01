package cn.vigor.modules.monitor.web;


import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hyperic.sigar.Sigar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.vigor.common.config.Global;
import cn.vigor.common.json.AjaxJson;
import cn.vigor.common.mail.MailSendUtils;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.monitor.entity.Monitor;
import cn.vigor.modules.monitor.service.MonitorService;
import cn.vigor.modules.monitor.utils.SystemInfo;
import cn.vigor.modules.sys.entity.SystemConfig;
import cn.vigor.modules.sys.service.SystemConfigService;


/**
 * 系统监控Controller
 * @author liugf
 * @version 2016-02-07
 */
@Controller
@RequestMapping(value = "${adminPath}/monitor")
public class MonitorController extends BaseController {
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private SystemConfigService systemConfigService;
	
	@ModelAttribute
	public Monitor get(@RequestParam(required=false) String id) {
		Monitor entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = monitorService.get(id);
		}
		if (entity == null){
			entity = new Monitor();
		}
		return entity;
	}
	
	@RequestMapping("info")
	public String info(Model model) throws Exception {
		Monitor monitor = monitorService.get("1");
		model.addAttribute("cpu", monitor.getCpu());
		model.addAttribute("jvm", monitor.getJvm());
		model.addAttribute("ram", monitor.getRam());
		model.addAttribute("toEmail", monitor.getToEmail());
		return  "modules/monitor/info";
	}
	
	@RequestMapping("monitor")
	public String monitor() throws Exception {
		return "modules/monitor/monitor";
	}
	
	@RequestMapping("systemInfo")
	public String systemInfo(Model model) throws Exception {
		model.addAttribute("systemInfo", SystemInfo.SystemProperty());
		return "modules/monitor/systemInfo";
	}
	
	@ResponseBody
	@RequestMapping("usage")
	public Map usage(Model model) throws Exception {
		SystemConfig config = systemConfigService.get("1");
		Monitor monitor = monitorService.get("1");
		Map<?, ?> sigar = SystemInfo.usage(new Sigar());
		String content="";
		content += "您预设的cpu使用率警告线是"+monitor.getCpu()+"%, 当前使用率是"+sigar.get("cpuUsage")+"%";
		content += "您预设的jvm使用率警告线是"+monitor.getJvm()+"%, 当前使用率是"+sigar.get("jvmUsage")+"%";
		content += "您预设的ram使用率警告线是"+monitor.getRam()+"%, 当前使用率是"+sigar.get("ramUsage")+"%";
		if(Float.valueOf(sigar.get("cpuUsage").toString()) >= Float.valueOf(monitor.getCpu())
				||Float.valueOf(sigar.get("jvmUsage").toString()) >= Float.valueOf(monitor.getJvm())
				||Float.valueOf(sigar.get("ramUsage").toString()) >= Float.valueOf(monitor.getRam())){
			MailSendUtils.sendEmail(config.getSmtp(), config.getPort(), config.getMailName(), config.getMailPassword(), monitor.getToEmail(), "服务器监控预警", content, "0");
			
		};
		return sigar;
	}
	/**
	 * 修改配置　
	 * @param request
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
    @ResponseBody
	@RequestMapping("modifySetting")
    public AjaxJson save(Monitor monitor, Model model) {
    	AjaxJson j = new AjaxJson();
		String message = "保存成功";
		Monitor t = monitorService.get("1");
		try {
			monitor.setId("1");
			MyBeanUtils.copyBeanNotNull2Bean(monitor, t);
			monitorService.save(t);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			message = "保存失败";
		}
		j.setMsg(message);
		return j;
    }
    
    /**
     * 资源监控
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("resourceMonitor")
    public void resourceMonitor(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException{
    	String ip = Global.getConfig("ambr_host");
    	String port = Global.getConfig("ambr_port");
    	response.sendRedirect("http://" + ip + ":" + port + "/#/main/dashboard/metrics?loginName=admin&password=admin");
    }
    
    /**
     * 主机列表
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("hosts")
    public void hosts(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException{
    	String ip = Global.getConfig("ambr_host");
    	String port = Global.getConfig("ambr_port");
    	String redirectUrl = "http://" + ip + ":" + port + "/#/main/hosts";
    	response.sendRedirect(redirectUrl);
    }
    
    /**
     * 通知警告
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("reportNotice")
    public void reportNotice(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException{
    	String ip = Global.getConfig("ambr_host");
    	String port = Global.getConfig("ambr_port");
    	String redirectUrl = "http://" + ip + ":" + port + "/#/main/alerts";
    	response.sendRedirect(redirectUrl);
    }
    
    /**
     * 集群管理
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("clusterManager")
    public void clusterManager(HttpServletRequest request, HttpServletResponse response,Model model) throws IOException{
    	String ip = Global.getConfig("ambr_host");
    	String port = Global.getConfig("ambr_port");
    	String redirectUrl = "http://" + ip + ":" + port + "/#/main/admin/stack/services";
    	response.sendRedirect(redirectUrl);
    }
}