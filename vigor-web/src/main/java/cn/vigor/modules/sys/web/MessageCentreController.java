package cn.vigor.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.sys.entity.MessageCentre;
import cn.vigor.modules.sys.service.MessageCentreService;

/**
 * 邮件Controller
 * @author zhangfeng
 * @version 2016-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/messagecentre")
public class MessageCentreController extends BaseController
{
    
    @Autowired
    private MessageCentreService messageCentreService;
    
    /**
     * 邮件列表页面
     */
    @RequiresPermissions("sys:messagecentre:list")
    @RequestMapping(value = "list")
    public String list(MessageCentre messageCentre, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        //默认查当天的数据
        messageCentre.setStartTime(null == messageCentre.getStartTime()
                ? DateUtils.getDayStart() : messageCentre.getStartTime());
        messageCentre.setEndTime(null == messageCentre.getEndTime()
                ? DateUtils.getDayEnd() : messageCentre.getEndTime());
        
        Page<MessageCentre> page = messageCentreService.findPage(
                new Page<MessageCentre>(request, response), messageCentre);
        model.addAttribute("page", page);
        return "modules/sys/messageCentreList";
    }
    
    /**
     * 重发
     */
    @RequiresPermissions("sys:messagecentre:status")
    @RequestMapping(value = "status")
    public @ResponseBody String status(String ids)
    {
        String[] msgIdArr = ids.split(",");
        
        if (null != msgIdArr && msgIdArr.length > 0)
        {
            //批量循环设置
            for (String msgId : msgIdArr)
            {
                messageCentreService.updateStatus(Integer.valueOf(msgId));
            }
        }
        
        return "操作成功！";
    }
}