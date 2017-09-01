package cn.vigor.modules.meta.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.MyBeanUtils;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.utils.excel.ExportExcel;
import cn.vigor.common.utils.excel.ImportExcel;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.service.MetaRepoService;
import cn.vigor.modules.meta.util.PrivilegeException;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 数据库连接信息Controller
 * @author kiss
 * @version 2016-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/repo/RepoResult")
public class MetaRepoResultController extends BaseController {

	@Autowired
	private MetaRepoService metaRepoService;
	
	@ModelAttribute
	public MetaRepo get(@RequestParam(required=false) String id) {
		MetaRepo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = metaRepoService.get(id,2);
			if(entity.getUserPwd()!=null && entity.getUserPwd().endsWith("==")){
				entity.setUserPwd(AESUtil.decForTD(entity.getUserPwd()));
			}
		}
		if (entity == null){
			entity = new MetaRepo();
		}
		return entity;
	}
	
	/**
	 * 数据库连接信息列表页面
	 */
	@RequiresPermissions("repo:repoResult:list")
	@RequestMapping(value = {"list", ""})
	public String list(MetaRepo metaRepo, HttpServletRequest request, HttpServletResponse response, Model model) {
		metaRepo.setMetaType(2);
		Page<MetaRepo> page = metaRepoService.findPage(new Page<MetaRepo>(request, response), metaRepo); 
		model.addAttribute("page", page);
		User user = UserUtils.getUser();
	    model.addAttribute("user", user);
		return "modules/meta/result/repo/metaRepoList";
	}

	/**
	 * 查看，增加，编辑数据库连接信息表单页面
	 */
	@RequiresPermissions(value={"repo:repoResult:view","repo:repoResult:add","repo:repoResult:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MetaRepo metaRepo, Model model) {
		model.addAttribute("metaRepo", metaRepo);
		return "modules/meta/result/repo/metaRepoForm";
	}
	
	/**
     * 查看
     */
    @RequiresPermissions(value="repo:repoResult:view")
    @RequestMapping(value = "view")
    public String view(MetaRepo metaRepo, Model model) {
        model.addAttribute("metaRepo", metaRepo);
        return "modules/meta/result/repo/metaRepoView";
    }

	/**
	 * 保存数据库连接信息
	 */
	@RequiresPermissions(value={"repo:repoResult:add","repo:repoResult:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MetaRepo metaRepo, Model model, RedirectAttributes redirectAttributes) throws Exception{
		try
        {
            if (!beanValidator(model, metaRepo)){
            	return form(metaRepo, model);
            }
            //根据e_meta_repo表唯一索引的约束,需要数据进行校验,并给出提示
            MetaRepo mr = metaRepoService.findExistRepo(metaRepo);
            if(mr!=null){
            	addMessage(redirectAttributes, "同类型结果集信息在相同ip和目录下只能存在一条记录");
            }else{
            	if(!metaRepo.getIsNewRecord()){//编辑表单保存
                	MetaRepo t = metaRepoService.get(metaRepo.getId());//从数据库取出记录的值
                	MyBeanUtils.copyBeanNotNull2Bean(metaRepo, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
                	t.setMetaType(2);
                	metaRepoService.save(t);//保存
                }else{//新增表单保存
                	metaRepo.setMetaType(2);//0 source  1 store 2 result 
                	metaRepoService.save(metaRepo);//保存
                }
            }
            addMessage(redirectAttributes, "保存数据库连接信息成功");
        }
        catch (Exception e)
        {
            String msg=e.getMessage();
            if(msg.contains("Duplicate")&& msg.contains("connname"))
            {
                msg="保存失败,"+metaRepo.getConnName()+"名已存在，";
            } else {
                msg="保存失败：失败原因："+e.getMessage();
            }
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
		return "redirect:"+Global.getAdminPath()+"/repo/RepoResult/?repage";
	}
	
	/**
	 * 删除数据库连接信息
	 */
	@RequiresPermissions("repo:repoResult:del")
	@RequestMapping(value = "delete")
	public String delete(MetaRepo metaRepo, RedirectAttributes redirectAttributes) {
		try
        {
            metaRepoService.delete(metaRepo,2);
            addMessage(redirectAttributes, "删除数据库连接信息成功");
        }
        catch (PrivilegeException e)
        {
            addMessage(redirectAttributes, e.getMessage());
        }
		return "redirect:"+Global.getAdminPath()+"/repo/RepoResult/?repage";
	}
	
	/**
	 * 批量删除数据库连接信息
	 */
	@RequiresPermissions("repo:repoResult:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			metaRepoService.delete(metaRepoService.get(id));
		}
		addMessage(redirectAttributes, "删除数据库连接信息成功");
		return "redirect:"+Global.getAdminPath()+"/repo/RepoResult/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("repo:repoResult:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MetaRepo metaRepo, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "数据库连接信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MetaRepo> page = metaRepoService.findPage(new Page<MetaRepo>(request, response, -1), metaRepo);
    		new ExportExcel("数据库连接信息", MetaRepo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据库连接信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/repo/RepoResult/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("repo:repoResult:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MetaRepo> list = ei.getDataList(MetaRepo.class);
			for (MetaRepo metaRepo : list){
				metaRepoService.save(metaRepo);
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条数据库连接信息记录");
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入数据库连接信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/repo/RepoResult/?repage";
    }
	
	/**
	 * 下载导入数据库连接信息数据模板
	 */
	@RequiresPermissions("repo:repoResult:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "数据库连接信息数据导入模板.xlsx";
    		List<MetaRepo> list = Lists.newArrayList(); 
    		new ExportExcel("数据库连接信息数据", MetaRepo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/repo/RepoResult/?repage";
    }
	

    /**
     * 启用 
     * @param id 表id
     * @return disable
     */
    @RequestMapping(value ="enable")
    @ResponseBody
    public JSONObject enable(String id)
    {
        JSONObject data = new JSONObject();
        //获取表信息
        MetaRepo  repo = metaRepoService.get(id);
        //获取表 或者目录 ip 用户名 密码 端口等  
        data=metaRepoService.enable(repo);                   
        return data;
    }

    /**
     * 启用 
     * @param id 表id
     * @return 
     */
    @RequestMapping(value ="disable")
    @ResponseBody
    public JSONObject disable(String id)
    {
        JSONObject data = new JSONObject();
        //获取表信息
        MetaRepo  repo = metaRepoService.get(id);
        //获取表 或者目录 ip 用户名 密码 端口等  
        data=metaRepoService.disable(repo);                   
        return data;
    }
}