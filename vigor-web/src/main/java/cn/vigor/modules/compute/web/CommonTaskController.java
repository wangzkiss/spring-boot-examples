package cn.vigor.modules.compute.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.web.BaseController;
import cn.vigor.modules.compute.entity.ComputeTaskDetailBean;
import cn.vigor.modules.compute.service.ComputeTaskService;
/**
 * 任务配置接口Controller
 * @author huzeyuan-38342
 * @version v1.0
 * @date 2016-12-27
 *
 */
@Controller
@RequestMapping(value="${adminPath}/compute/computetask")
public class CommonTaskController extends BaseController{
    
    @Autowired
    private ComputeTaskService computeTaskService;

    
    /**
     * 跳转到链接的页面
     * @param taskDetail
     * @param model
     * @return
     * @throws Exception
     */
    
    @RequestMapping(value = "/showLink",method = RequestMethod.GET)
    public String showHiveTask(@RequestParam(value="pid",required=true)String pid,
            @RequestParam(value="pname",required=true)String pname, @RequestParam(value="type",required=true)int type,@RequestParam(value="tname",required=true)String tname,Model model) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", pid);
        jsonObject.put("pname", pname);
        jsonObject.put("tname", tname);
        jsonObject.put("type", type);
        model.addAttribute("data",jsonObject);
        return "modules/tji/taskconfigure/relationTable";
    }
    
    /**
     * 
     * @param fid
     * @param fname
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/showFunc",method = RequestMethod.GET)
    public String showFunc(@RequestParam(value="fid",required=true)String fid,
            @RequestParam(value="fname",required=true)String fname,@RequestParam(value="type",required=false)String type,Model model) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fid", fid);
        jsonObject.put("fname", fname);
        jsonObject.put("type", type);
        model.addAttribute("data",jsonObject);
        return "modules/tji/taskconfigure/funParamsAction";
    }
    
    
    /**
     * 
     * @param fid
     * @param fname
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/showQueryCon",method = RequestMethod.GET)
    public String showQueryCon(@RequestParam(value="queryOne",required=true)String[] queryOne,
            @RequestParam(value="queryTwo",required=true)String queryTwo,@RequestParam(value="queryThr",required=true)String queryThr,@RequestParam(value="queryFour",required=true)int queryFour,Model model) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("queryOne", queryOne);
        jsonObject.put("queryTwo", queryTwo);
        jsonObject.put("queryThr", queryThr);
        jsonObject.put("queryFour", queryFour);
        model.addAttribute("data",jsonObject);
        return "modules/tji/taskconfigure/setQueryCondition";
    }
    
    
    
    
    /**
     * 根据资源库查询数据源列表
     * @param resourceName 资源库名
     * @return 结果
     */
    @RequestMapping(value="/getTaskSameName",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getDataSourceByDatabase(@RequestParam(value="taskName",required=true)String task_name,ComputeTaskDetailBean detailBean ){
        JSONObject jsonObject = new JSONObject();
        try
        {
            int count  = computeTaskService.getTaskSameName(task_name);
            if(count>=1){
                jsonObject.put("success", true);
                jsonObject.put("msg", "任务名称重名，请重新命名！");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "失败");
        }
        return jsonObject;
    }
    
}
