package cn.vigor.modules.tji.web;

import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.SearchIndex;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.server.entity.Platform;
import cn.vigor.modules.server.service.PlatformService;
import cn.vigor.modules.tji.entity.JobLog;

/**
 * 作业相关Controller
 * @author zhangfeng
 * @version 2016-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/tji/joblog")
public class JobLogController extends BaseController
{

    @Autowired
    private PlatformService platformService;
    /**
     * 根据
     */
 //   @RequiresPermissions("tji:joblog:list")
    @RequestMapping(value = { "list", "" })
    public String list(JobLog jobLog, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        Page<JobLog> page = new Page<JobLog>(request, response);
        try{
            Platform platform  =  platformService.getOne("elasticsearch");
            SearchIndex search = new SearchIndex(platform.getPlatformIp(), 
                    platform.getPlatformPort(), page.getPageSize(), page.getPageNo());
            search.buildClientByTransport();
            page.setCount(search.getJobLogCount(jobLog));
            List<JobLog> list = search.getJobLogs(jobLog);
            page.setList(list);
            search.close();
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
        model.addAttribute("page", page);
        return "modules/tji/joblogList";
    }

}