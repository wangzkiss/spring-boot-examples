package cn.vigor.modules.meta.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.DataPermission;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaResult;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.service.DataPermissionService;
import cn.vigor.modules.meta.service.MetaResultService;
import cn.vigor.modules.meta.service.MetaSourceService;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 数据权限Controller
 * @author kiss
 * @version 2016-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/data/dataPermission")
public class DataPermissionController extends BaseController {

	@Autowired
	private DataPermissionService dataPermissionService;
	

	/**
	 * 获取连接权限信息
	 */
	@RequestMapping(value = "share-get")
	public String repoShareGet(DataPermission param, Model model) {
	
		User user = UserUtils.getUser();//获取登录用户
		List<DataPermission> dataPermissions=dataPermissionService.getDatapermissions(param);
		List<DataPermission> dataPermissionsnew=new ArrayList<DataPermission>();
		int index=0;
		for (DataPermission dataPermission : dataPermissions) {
			if(user!=null&&user.getId().equals(dataPermission.getUser().getId())) {
				continue;
			}
			dataPermission.setIndex(index);
			dataPermission.setDataType(param.getDataType());
			index++;
			dataPermissionsnew.add(dataPermission);
		}
		model.addAttribute("dataPermissions", dataPermissionsnew);
		model.addAttribute("url", param.getUrl());
		return "modules/meta/dataper/dataPermissionForm";
	}
	
	/**
     * 获取连接权限信息
     */
    @RequestMapping(value = "users")
    @ResponseBody
    public JSONObject userlist(DataPermission param, Model model) {
        JSONObject data=new JSONObject();
        User user = UserUtils.getUser();//获取登录用户
        List<DataPermission> dataPermissions=dataPermissionService.getDatapermissions(param);
        List<DataPermission> dataPermissionsnew=new ArrayList<DataPermission>();
        int index=0;
        for (DataPermission dataPermission : dataPermissions) {
            if(user!=null&&user.getId().equals(dataPermission.getUser().getId())) {
                continue;
            }
            dataPermission.setIndex(index);
            dataPermission.setDataType(param.getDataType());
            index++;
            dataPermissionsnew.add(dataPermission);
        }
        data.put("data", dataPermissionsnew);
        return data;
    }
    
	
	@Autowired
    private MetaSourceService metaSourceService;
    @Autowired
    private MetaStoreService metaStoreService;
    @Autowired
    private MetaResultService metaResultService;
	    
	/**
	 * 分配页面
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "share-save")
	public String repoShareSave(DataPermission param, Model model,RedirectAttributes redirectAttributes) throws InterruptedException {
		for (DataPermission dataper : param.getDataper()) {
			if(dataper.getDataId()==null||dataper.getDataId()=="")
				continue;
			  dataPermissionService.save(dataper);
			if("source".equals(dataper.getDataType())&&"Y".equals(dataper.getQuery().toUpperCase()))
			{
			    MetaSource source= metaSourceService.get(dataper.getDataId());
			    dataper.setDataId(source.getRepoId().getId());
			    dataper.setDataType("repo");
			    dataPermissionService.save(dataper);
			}else if("store".equals(dataper.getDataType())&&"Y".equals(dataper.getQuery().toUpperCase()))
            {
                MetaStore source= metaStoreService.get(dataper.getDataId());
                dataper.setDataId(source.getRepoId().getId());
                dataper.setDataType("repo");
                dataPermissionService.save(dataper);
            }else if("result".equals(dataper.getDataType())&&"Y".equals(dataper.getQuery().toUpperCase()))
            {
                MetaResult source= metaResultService.get(dataper.getDataId());
                dataper.setDataId(source.getRepoId().getId());
                dataper.setDataType("repo");
                dataPermissionService.save(dataper);
            }
		}
		addMessage(redirectAttributes, "共享成功");
		return "redirect:"+Global.getAdminPath()+param.getUrl()+"?repage";
	}
	
	/**
	 * 选择数据id，关联数据表的id
	 */
	@RequestMapping(value = "selectdataId")
	public String selectdataId(MetaRepo dataId, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MetaRepo> page = dataPermissionService.findPageBydataId(new Page<MetaRepo>(request, response),  dataId);
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", dataId);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}