package cn.vigor.modules.tji.web;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.Encodes;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.sys.utils.UserUtils;
import cn.vigor.modules.tji.entity.JobSchedule;
import cn.vigor.modules.tji.service.JobScheduleService;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 * 排班及统计Controller
 * 
 * @author zhangfeng
 * @version 2016-06-24
 */
@Controller
@RequestMapping(value = "${adminPath}/tji/jobschedule")
public class JobScheduleController extends BaseController {

	@Autowired
	private JobScheduleService jobScheduleService;

	// 表头
	private static final String[] tableHeader = { "任务类型", "实例总数", "成功数", "失败数", "平均运行时长（分钟）" };

	/**
	 * 排班及统计列表页面
	 */
	@RequiresPermissions("tji:jobschedule:list")
	@RequestMapping(value = { "list", "" })
	public String list(JobSchedule jobSchedule, HttpServletRequest request, HttpServletResponse response, Model model) {
		// 默认查待执行排班
		jobSchedule.setFlag(null == jobSchedule.getFlag() ? 1 : jobSchedule.getFlag());

		Page<JobSchedule> page = jobScheduleService.findPage(new Page<JobSchedule>(request, response), jobSchedule);

		model.addAttribute("page", page);
		model.addAttribute("flag", jobSchedule.getFlag());
		return "modules/tji/jobScheduleList";
	}

	/**
	 * 用于统计界面跳转
	 */
	@RequiresPermissions("tji:jobschedule:listforStatis")
	@RequestMapping(value = "list-statis")
	public String listForStatis(JobSchedule jobSchedule, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		// 默认查当天的数据
		jobSchedule.setcStartTime(
				null == jobSchedule.getcStartTime() ? DateUtils.getDayStart() : jobSchedule.getcStartTime());
		jobSchedule.setcEndTime(null == jobSchedule.getcEndTime() ? DateUtils.getDayEnd() : jobSchedule.getcEndTime());

		Page<JobSchedule> page = jobScheduleService.findPage(new Page<JobSchedule>(request, response), jobSchedule);

		model.addAttribute("page", page);
		return "modules/tji/jobSchForStatis";
	}

	/**
	 * 暂停恢复
	 */
	@RequiresPermissions("tji:jobschedule:resumepause")
	@RequestMapping("resume-pause")
	public @ResponseBody String edit(String ids, int pauseFlag) {
		String msg = "";
		StringBuffer jobNames = new StringBuffer();

		// 批量设置任务参数格式为，taskId,taskId
		String[] idArr = ids.split(",");

		if (null != idArr && idArr.length > 0) {
			// 批量循环设置
			for (String id : idArr) {
				jobScheduleService.updatePauseFlag(Integer.valueOf(id), pauseFlag);
			}
		}

		// 有失败
		if (StringUtils.isNotEmpty(jobNames)) {
			msg = jobNames + "操作失败！";
		} else {
			msg = "操作成功!";
		}

		return msg;
	}

	/**
	 * 任务统计
	 */
	@RequiresPermissions("tji:jobschedule:statis")
	@RequestMapping("statis")
	public String statis(JobSchedule jobSchedule, Model model) {
		// 默认查当天的数据
		jobSchedule.setcStartTime(
				null == jobSchedule.getcStartTime() ? DateUtils.getDayStart() : jobSchedule.getcStartTime());
		jobSchedule.setcEndTime(null == jobSchedule.getcEndTime() ? DateUtils.getDayEnd() : jobSchedule.getcEndTime());
		// 查询当前用户的作业统计数据
		jobSchedule.setCurrentUser(UserUtils.getUser());
		model.addAttribute("statisInfo", jobScheduleService.getJobStatis(jobSchedule));
		model.addAttribute("jobSchedule", jobSchedule);

		return "modules/tji/jobStatis";
	}

	/**
	 * 任务报表
	 */
	@RequiresPermissions("tji:jobschedule:report")
	@RequestMapping("report")
	public String report(int flag, @RequestParam(required = false) String date, Model model,String vtype) {
		if(StringUtils.isNotEmpty(vtype) && !vtype.startsWith("task_type_")){
			vtype = "task_type_" + vtype;
		}else{
			String sysFlag = Global.getConfig("sys_flag");
			vtype = "task_type_" + sysFlag;
		}
		// 第一次点击日志为空
		if (StringUtils.isEmpty(date)) {
			date = flag == 1 ? DateUtils.getYesterday() : DateUtils.getLastMonth();
		}
		model.addAttribute("reportInfo",
				JSONObject.fromObject(jobScheduleService.getJobReport(date, flag, UserUtils.getUser().getName(),vtype)));
		model.addAttribute("flag", flag);
		model.addAttribute("date", date);
		model.addAttribute("vtype", vtype);
		return "modules/tji/jobReport";
	}

	/**
	 * 任务报表，用于首页
	 * @param timeRegion 时间跨度(现页面有12,24小时两个跨度,以当前时间为起点,往前推)
	 * @return
	 */
	@RequiresPermissions("tji:jobschedule:report")
	@RequestMapping(value = "report-json")
	@ResponseBody
	public String getReportJson(@RequestParam(value="timeRegion",defaultValue="12")Integer timeRegion) {
		Object respData = null;
		boolean success = false;
		String msg = null;
		try {
			JobSchedule jobSchedule = new JobSchedule();
			Date currentDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			Date endTime = calendar.getTime();
			calendar.add(Calendar.HOUR_OF_DAY, -timeRegion);
			Date beginTime = calendar.getTime();
			// 默认查当天的数据
			jobSchedule.setcStartTime(beginTime);
			jobSchedule.setcEndTime(endTime);
			respData = jobScheduleService.getJobStatisForIndex(jobSchedule);
			success = true;
			msg = "查询成功";
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			msg = "查询失败";
		}
		return respData(success, msg, respData, null, false);
	}

	/**
	 * 报表导出
	 */
	@RequiresPermissions("tji:jobschedule:export")
	@RequestMapping("export")
	public @ResponseBody String exportReport(int flag, String date, String pie, String histogram,
			String vtype,HttpServletRequest request, HttpServletResponse response) {
		// 拼装路径
		//TODO
		String webPath = request.getContextPath();
		String piePath = webPath + "/report/pie" + System.currentTimeMillis() + ".png";
		String htgPath = webPath + "/report/htg" + System.currentTimeMillis() + ".png";
		String fileName = date + (1 == flag ? "日报" : "月报");

		// 图片生成成功
		if (generateImage(piePath, pie) && generateImage(htgPath, histogram)) {
			response.reset();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName + ".pdf"));

			OutputStream os = null;
			Document document = null;
			PdfWriter writer = null;

			try {
				os = response.getOutputStream();
				document = new Document(PageSize.A4, 50, 50, 50, 50);
				writer = PdfWriter.getInstance(document, os);
				document.open();

				// 换行
				Paragraph line = new Paragraph("\n");

				// 标题
				BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
				Font titleChinese = new Font(bfChinese, 20, Font.BOLD);
				Font xTitileChinese = new Font(bfChinese, 14, Font.BOLD);

				Paragraph title = new Paragraph(fileName, titleChinese);
				title.setAlignment(Element.ALIGN_CENTER);
				title.setLeading(1f);
				document.add(title);
				document.add(line);
				document.add(line);

				document.add(new Paragraph("任务总体情况", xTitileChinese));
				Image img1 = Image.getInstance(piePath);
				img1.scalePercent(50f);
				img1.setAlignment(Element.ALIGN_CENTER);
				document.add(img1);
				document.add(line);

				document.add(new Paragraph("任务分类情况", xTitileChinese));
				Image img2 = Image.getInstance(htgPath);
				img2.scalePercent(50f);
				img2.setAlignment(Element.ALIGN_CENTER);
				document.add(img2);
				document.add(line);

				document.add(new Paragraph("任务分类明细", xTitileChinese));
				document.add(line);
				// 查询报表，用于后端生成列表
				if(StringUtils.isNotEmpty(vtype)){
					vtype = "task_type_" + vtype;
				}
				Map<String, Object> allMap = jobScheduleService.getJobReport(date, flag, UserUtils.getUser().getName(),vtype);

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> rpList = (List<Map<String, Object>>) allMap.get("typeReport");

				// 生成表格
				Font fontChinese = new Font(bfChinese, 12, Font.NORMAL);
				Font header = new Font(bfChinese, 12, Font.BOLD);
				PdfPTable datatable = new PdfPTable(5);
				datatable.setWidths(new int[] { 6, 3, 3, 3, 8 });
				datatable.setWidthPercentage(100);
				datatable.getDefaultCell().setPadding(1);
				datatable.getDefaultCell().setBorderWidth(1);
				datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
				datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				datatable.getDefaultCell().setMinimumHeight(25);
				datatable.getDefaultCell().setBorderColor(BaseColor.GRAY);

				// 添加表头元素
				for (int i = 0; i < tableHeader.length; i++) {
					datatable.addCell(new Paragraph(tableHeader[i], header));
				}

				// 添加数据
				for (Map<String, Object> rpMap : rpList) {
					PdfPCell blankCell = new PdfPCell(new Paragraph(rpMap.get("typeName").toString(), fontChinese));
					blankCell.setHorizontalAlignment(Element.ALIGN_LEFT);
					blankCell.setPadding(1);
					blankCell.setBorderWidth(1);
					blankCell.setBorderColor(BaseColor.GRAY);

					datatable.addCell(blankCell);
					datatable.addCell(new Paragraph(rpMap.get("allCount").toString(), fontChinese));
					datatable.addCell(new Paragraph(rpMap.get("successCount").toString(), fontChinese));
					datatable.addCell(new Paragraph(rpMap.get("failCount").toString(), fontChinese));
					datatable.addCell(new Paragraph(rpMap.get("avgTime").toString(), fontChinese));
				}

				document.add(datatable);

				document.close();
				os.close();
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "";
	}

	/**
	 * base64字符串转化成图片
	 * 
	 * @param imgStr
	 * @return
	 */
	public static boolean generateImage(String path, String imgStr) {
		// 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;

		BASE64Decoder decoder = new BASE64Decoder();

		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);

			for (int i = 0; i < b.length; ++i) {
				// 调整异常数据
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 封装返回json
	 * 
	 * @param success
	 * @param msg
	 * @param respData
	 * @param total
	 * @param expried
	 * @return
	 */
	private String respData(boolean success, String msg, Object respData, Integer total, boolean expried) {
		JSONObject data = new JSONObject();
		data.put("success", success);
		data.put("msg", msg);
		data.put("data", respData);
		data.put("sessionExpired", expried);
		if (total != null) {
			data.put("total", total);
		}

		return data.toString();
	}
}