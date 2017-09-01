package cn.vigor.modules.meta.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.alibaba.fastjson.JSONArray;
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
import cn.vigor.modules.meta.entity.MetaColumn;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaSourcePro;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.entity.MetaStorePro;
import cn.vigor.modules.meta.entity.MetaTable;
import cn.vigor.modules.meta.service.MetaRepoService;
import cn.vigor.modules.meta.service.MetaSourceService;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.meta.util.ChineseToEnglish;
import cn.vigor.modules.meta.util.DBUtils;
import cn.vigor.modules.meta.util.FtpUtil;
import cn.vigor.modules.meta.util.MetaUtil;
import cn.vigor.modules.meta.util.PrivilegeException;
import cn.vigor.modules.server.entity.Platform;
import cn.vigor.modules.server.entity.PlatformNode;
import cn.vigor.modules.server.service.PlatformService;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.DictUtils;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 数据库连接信息Controller
 * 
 * @author kiss
 * @version 2016-05-13
 */
@Controller
@RequestMapping(value = "${adminPath}/repo/RepoSource")
public class MetaRepoSourceController extends BaseController
{
    
    @Autowired
    private MetaRepoService metaRepoService;
    
    @Autowired
    private MetaSourceService metaSourceService;
    
    @Autowired
    private MetaStoreService metaStoreService;
    
    @ModelAttribute
    public MetaRepo get(@RequestParam(required = false) String id)
    {
        MetaRepo entity = null;
        if (StringUtils.isNotBlank(id))
        {
            entity = metaRepoService.get(id, 0);
            String pwd = entity.getUserPwd();
            if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
                entity.setUserPwd(AESUtil.decForTD(pwd));
            }
        }
        if (entity == null)
        {
            entity = new MetaRepo();
        }
        return entity;
    }
    
    /**
     * 数据库连接信息列表页面
     */
    @RequiresPermissions("repo:repoSource:list")
    @RequestMapping(value = { "list", "" })
    public String list(MetaRepo metaRepo, HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        metaRepo.setMetaType(0);
        Page<MetaRepo> page = metaRepoService
                .findPage(new Page<MetaRepo>(request, response), metaRepo);
        model.addAttribute("page", page);
        User user = UserUtils.getUser();
        model.addAttribute("user", user);
        return "modules/meta/source/repo/metaRepoList";
    }
    
    /**
     * 查看，增加，编辑数据库连接信息表单页面
     */
    @RequiresPermissions(value = { "repo:repoSource:view",
            "repo:repoSource:add",
            "repo:repoSource:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(MetaRepo metaRepo, Model model)
    {
        model.addAttribute("metaRepo", metaRepo);
        return "modules/meta/source/repo/metaRepoForm";
    }
    
    /**
     * 查看，增加，编辑数据库连接信息表单页面
     */
    @RequiresPermissions(value ="repo:repoSource:view")
    @RequestMapping(value = "view")
    public String view(MetaRepo metaRepo, Model model)
    {
        model.addAttribute("metaRepo", metaRepo);
        return "modules/meta/source/repo/metaRepoView";
    }
    
    /**
     * 保存数据库连接信息
     */
    @RequiresPermissions(value = { "repo:repoSource:add",
            "repo:repoSource:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(MetaRepo metaRepo, Model model,
            RedirectAttributes redirectAttributes) throws Exception
    {
        try
        {
            if (!beanValidator(model, metaRepo))
            {
                return form(metaRepo, model);
            }
            //根据e_meta_repo表唯一索引的约束,需要数据进行校验,并给出提示
            MetaRepo mr = metaRepoService.findExistRepo(metaRepo);
            if(mr!=null){
            	addMessage(redirectAttributes, "同类型数据源信息在相同ip和目录下只能存在一条记录");
            }else{
            	if (!metaRepo.getIsNewRecord())
                {// 编辑表单保存
                    MetaRepo t = metaRepoService.get(metaRepo.getId());// 从数据库取出记录的值
                    MyBeanUtils.copyBeanNotNull2Bean(metaRepo, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
                    t.setMetaType(0);
                    metaRepoService.save(t);// 保存
                }
                else
                {// 新增表单保存
                    metaRepo.setMetaType(0);// 0 source 1 store 2 result
                    metaRepoService.save(metaRepo);// 保存
                }
                addMessage(redirectAttributes, "保存数据库连接信息成功");
            }
        }
        catch (Exception e)
        {
            String msg=e.getMessage();
            if(msg.contains("Duplicate")&& msg.contains("connname"))
            {
                msg="保存失败,"+metaRepo.getConnName()+"名已存在";
            } else {
                msg="保存失败：失败原因："+e.getMessage();
            }
            addMessage(redirectAttributes, msg);
            e.printStackTrace();
        }
        return "redirect:" + Global.getAdminPath() + "/repo/RepoSource/?repage";
    }
    
    /**
     * 删除数据库连接信息
     */
    @RequiresPermissions("repo:repoSource:del")
    @RequestMapping(value = "delete")
    public String delete(MetaRepo metaRepo,
            RedirectAttributes redirectAttributes)
    {
        try
        {
            metaRepoService.delete(metaRepo,0);
            addMessage(redirectAttributes, "删除连接信息成功");
        }
        catch (PrivilegeException e)
        {
            addMessage(redirectAttributes, e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/repo/RepoSource/?repage";
    }
    
    /**
     * 批量删除数据库连接信息
     */
    @RequiresPermissions("repo:repoSource:del")
    @RequestMapping(value = "deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes)
    {
        String idArray[] = ids.split(",");
        for (String id : idArray)
        {
            metaRepoService.delete(metaRepoService.get(id));
        }
        addMessage(redirectAttributes, "删除数据库连接信息成功");
        return "redirect:" + Global.getAdminPath() + "/repo/RepoSource/?repage";
    }
    
    /**
     * 导出excel文件
     */
    @RequiresPermissions("repo:repoSource:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(MetaRepo metaRepo, HttpServletRequest request,
            HttpServletResponse response, RedirectAttributes redirectAttributes)
    {
        try
        {
            String fileName = "数据库连接信息" + DateUtils.getDate("yyyyMMddHHmmss")
                    + ".xlsx";
            Page<MetaRepo> page = metaRepoService.findPage(
                    new Page<MetaRepo>(request, response, -1), metaRepo);
            new ExportExcel("数据库连接信息", MetaRepo.class)
                    .setDataList(page.getList())
                    .write(response, fileName)
                    .dispose();
            return null;
        }
        catch (Exception e)
        {
            addMessage(redirectAttributes,
                    "导出数据库连接信息记录失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/repo/RepoSource/?repage";
    }
    
    /**
     * 导入Excel数据
     * 
     */
    @RequiresPermissions("repo:repoSource:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file,
            RedirectAttributes redirectAttributes)
    {
        try
        {
            int successNum = 0;
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<MetaRepo> list = ei.getDataList(MetaRepo.class);
            for (MetaRepo metaRepo : list)
            {
                metaRepoService.save(metaRepo);
            }
            addMessage(redirectAttributes,
                    "已成功导入 " + successNum + " 条数据库连接信息记录");
        }
        catch (Exception e)
        {
            addMessage(redirectAttributes,
                    "导入数据库连接信息失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/repo/RepoSource/?repage";
    }
    
    /**
     * 下载导入数据库连接信息数据模板
     */
    @RequiresPermissions("repo:repoSource:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response,
            RedirectAttributes redirectAttributes)
    {
        try
        {
            String fileName = "数据库连接信息数据导入模板.xlsx";
            List<MetaRepo> list = Lists.newArrayList();
            new ExportExcel("数据库连接信息数据", MetaRepo.class, 1).setDataList(list)
                    .write(response, fileName)
                    .dispose();
            return null;
        }
        catch (Exception e)
        {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/repo/RepoSource/?repage";
    }
    
    @RequestMapping(value = "extract-get")
    public String extractSet(MetaRepo metaRepo, Model model, RedirectAttributes redirectAttributes)
    {
        try
        {
            String password = metaRepo.getUserPwd();
            List<MetaTable> tables=null;
            if(5==metaRepo.getRepoType()){
                tables = new DBUtils().getHiveTables(
                        metaRepo.getUserName(), 
                        password, 
                        metaRepo.getPort(), 
                        metaRepo.getIp(), 
                        metaRepo.getRepoName());
            }else if(7==metaRepo.getRepoType())
            {
                tables = new DBUtils().getOralceTables(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName(),
                        metaRepo.getRepoInstance());
            }else if(10==metaRepo.getRepoType())
            {
                tables = new DBUtils().getSqlserverTables(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName());
            }else if(11==metaRepo.getRepoType()){
                tables = new DBUtils().getTrafodionTables(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName());
            }else{
                tables = new DBUtils().getMysqlTables(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName());
            }
            
            for (MetaTable metaTable : tables)
            {
                 bk: for (MetaSource source : metaRepo.getMetaSourceList())
                  {
                    if(source.getSourceFile().equals(metaTable.getTableName()))
                    {
                        metaTable.setTablestatus("已抽取");
                        metaTable.setExistId(source.getId());
                        metaTable.setOperator(source.getCreateBy().getName());
                        break bk;
                    }
                  }
            }
            
            metaRepo.setTables(tables);
        }
        catch (Exception e)
        {
            addMessage(redirectAttributes,
                    "表信息加载失败！失败信息：" + e.getMessage());
            e.printStackTrace();
          //  return "redirect:" + Global.getAdminPath() + "/repo/RepoSource/?repage";
        }
        model.addAttribute("metaRepo", metaRepo);
        return "modules/meta/source/repo/metaRepoExtract";
    }
    
    @Autowired
    private PlatformService platformService;
    /**
     * 抽取数据表信息信息
     */
    @RequestMapping(value = "extract-save")
    public String saveTable(MetaRepo param,String [] types,RedirectAttributes redirectAttributes) throws Exception
    {
        MetaRepo metaRepo = metaRepoService.get(param.getId(), -1);
        
        metaRepo.setTables(param.getTables());
        if (metaRepo.getRepoType() != 5 && metaRepo.getRepoType() != 6 && metaRepo.getRepoType() != 7
                && metaRepo.getRepoType() != 10 && metaRepo.getMetaType() != 11)
        {
            addMessage(redirectAttributes, "其他类型不支持抽取！");
            return "redirect:" + Global.getAdminPath()
                    + "/repo/RepoSource/?repage";
        }
        if (metaRepo.getTables().size() > 0)
        {
            try
            {
                if(types==null)//默认值生成外部数据源信息  
                {
                    saveSourceInfo(metaRepo, redirectAttributes);
                }else{
                    saveStoreInfo(metaRepo, types, redirectAttributes);
                }
                String names="";
                for (MetaTable table : metaRepo.getTables())
                {
                    if(table.getTableName()!=null)
                    {
                        names+=table.getTableName()+",";
                    }
                }
                if(names.length()>50)
                {
                    names=names.substring(0, 50)+"...";
                }else{
                    names=names.substring(0, names.lastIndexOf(","));
                }
                addMessage(redirectAttributes, "抽取成功，表名如下："+names);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                addMessage(redirectAttributes, "生成失败:"+e.getMessage());
            }
        } else
        {
            addMessage(redirectAttributes, "没有选择表");
        }
        return "redirect:" + Global.getAdminPath() + "/repo/RepoSource/?repage";
    }
 
    /**
     * 
     * @param metaRepo
     * @param redirectAttributes
     * @throws Exception
     */
    private void saveSourceInfo(MetaRepo metaRepo,RedirectAttributes redirectAttributes) throws Exception{
        String userName=metaRepo.getCurrentUser().getName();
        String type = DictUtils.getDictLabel("source_type", metaRepo.getRepoType()+"", "");
        for (MetaTable table : metaRepo.getTables())
            {
                List<MetaSourcePro> metaSourcePros = null;
                if (table == null || null == table.getTableName())
                {
                    continue;
                }
                String password = metaRepo.getUserPwd();
                if(StringUtils.isNotEmpty(password) && !password.equals("${input_dbpwd}")){
                    password = AESUtil.decForTD(password);
                }
                if (metaRepo.getRepoType() == 5){
                    metaSourcePros = new DBUtils().getHiveMetaSourcePro(
                            metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName());
                }
                else if (metaRepo.getRepoType() == 6)//mysql
                {
                    metaSourcePros = new DBUtils().getMetaSourcePro(
                            metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName());
                }
                else if (metaRepo.getRepoType() == 7)
                {
                    metaSourcePros = new DBUtils().getOracleMetaSourcePro(
                            metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName(),
                            metaRepo.getRepoInstance());
                }else if (metaRepo.getRepoType() ==10)
                {
                    metaSourcePros = new DBUtils().getSqlServerMetaSourcePro(
                            metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName());
                }else if(metaRepo.getRepoType() == 11){
                    metaSourcePros = new DBUtils().getTrafodionMetaSourcePro(metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName());
                }
              
            MetaSource medaSource = new MetaSource();
            if (metaSourcePros != null)
            {
                medaSource.setMetaSourceProList(metaSourcePros);
            }
            else
            {
                addMessage(redirectAttributes,
                        table.getTableName() + "表没有字段信息,");
                
            }
          
            medaSource.setSourceName(
                    metaRepo.getRepoName() + "_" + table.getTableName()+"_"+type+"_source_"+userName);
            medaSource.setSourceType(metaRepo.getRepoType());
            medaSource.setRepoId(metaRepo);
            medaSource.setSourceFile(table.getTableName());
            medaSource.setRemarks(table.getTableDesc());
            medaSource.setSourceDesc(table.getTableDesc());
            medaSource.setSourceType(metaRepo.getRepoType());
            
            
            if(StringUtils.isEmpty(table.getExistId())){
               medaSource.setIsNewRecord(true);
               metaSourceService.save(medaSource);// 保存
            }else{
                if(userName.equals(table.getOperator())){//已抽取 的操作人  和当前操作人一致  才可以更新字段属性
                    medaSource.setId(table.getExistId()); 
                    medaSource.setIsNewRecord(false);
                    metaSourceService.deletePro(medaSource);
                    metaSourceService.save(medaSource);// 保存
                }
            }
            
        }       
    }
    
    /**
     * 
     * @param metaRepo
     * @param ops     3  hdfs   4  hbase  5 hive  6 msyql 7 oracle  10 sqlserver   默认 hive   逗号隔开
     * @param redirectAttributes
     * @throws Exception
     */
    private void saveStoreInfo(MetaRepo metaRepo,String []ops,RedirectAttributes redirectAttributes) throws Exception{
        Date now = new Date();
        String userName=metaRepo.getCurrentUser().getName();
        SimpleDateFormat formatter = new SimpleDateFormat("YYMMdd_hhmmss");
        String condate = formatter.format(now);
        //String [] optype=ops.split(",");
        boolean isSave=false; //标示外部数据源是否以保存
        for (String op : ops)
        {
            
            MetaRepo repo = new MetaRepo();
            String port="10000";
            int  repoType=5;
            switch (op)
            {
                case "hdfs": repoType=3; break;
                case "hbase":  {repoType=4;op="zookeeper";};  break;
                case "hive": repoType=5;  break;
                case "mysql": repoType=6; break;
                case "oracle":  repoType=7;  break;
                case "sqlserver": repoType=10;  break;
                case "X-OLTP" : repoType=11; break;
                default:repoType=5;  break;
            }
            String conaname=op+metaRepo.getRepoName()+condate;
            String ip=Global.getConfig("hive_master_ip");
            Platform  p = platformService.getOne(op);
            
            if(p!=null)
            {
                p = platformService.get(p.getId());
                port=p.getPlatformPort()+"";
                if(repoType==4 && p.getPlatformNodeList()!=null && p.getPlatformNodeList().size()>1){
                    List<PlatformNode> nodes = p.getPlatformNodeList();
                    String cip = "";
                    for (int i=0;i<nodes.size();i++){
                        cip = cip + nodes.get(i).getNodeIp() + ((i<nodes.size()-1)?",":"");
                    }
                    ip = cip;
                }else{
                    ip=p.getPlatformIp();
                }
            }else{
                throw new RuntimeException(op+"这类型的存储,在系统中未找到ip和端口!");
            }
            repo.setIp(ip);
            repo.setConnName(conaname);
            repo.setPort(port);
            repo.setMetaType(1);
            repo.setRepoType(repoType);
            repo.setRepoName(metaRepo.getRepoName());
            MetaRepo temp = metaRepoService.findExistRepo(repo);
            if(temp==null){//新的录入信息
                repo.setIsNewRecord(true);
                repo.setId("");
                metaRepoService.save(repo);
            }else{
                repo=temp;//存在沿用旧的
            }            
            for (MetaTable table : metaRepo.getTables())
            {
                List<MetaSourcePro> metaSourcePros = null;
                if (table == null || null == table.getTableName())
                {
                    continue;
                }
                String password = metaRepo.getUserPwd();
                if(StringUtils.isNotEmpty(password) && password.endsWith("==")){
                    password = AESUtil.decForTD(password);
                }
                //根据资源类型(repoType)获取
                if (metaRepo.getRepoType() == 5){//hive
                    metaSourcePros = new DBUtils().getHiveMetaSourcePro(
                            metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName());
                }else if (metaRepo.getRepoType() == 6)//mysql
                {
                    metaSourcePros = new DBUtils().getMetaSourcePro(
                            metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName());
                }
                else if (metaRepo.getRepoType() == 7)
                {
                    metaSourcePros = new DBUtils().getOracleMetaSourcePro(
                            metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName(),
                            metaRepo.getRepoInstance());
                }else if(metaRepo.getRepoType() == 11){
                    metaSourcePros = new DBUtils().getTrafodionMetaSourcePro(metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName());
                } if (metaRepo.getRepoType() ==10)
                {
                    metaSourcePros = new DBUtils().getSqlServerMetaSourcePro(
                            metaRepo.getUserName(),
                            password,
                            metaRepo.getPort(),
                            metaRepo.getIp(),
                            metaRepo.getRepoName(),
                            table.getTableName());
                }
                if(!isSave) ///保存外部数据源
                {
                    MetaSource medaSource = new MetaSource();
                    if (metaSourcePros != null)
                    {
                        medaSource.setMetaSourceProList(metaSourcePros);
                    }
                    else
                    {
                        addMessage(redirectAttributes,
                                table.getTableName() + "表没有字段信息,");
                        
                    }
                    medaSource.setSourceName(
                            metaRepo.getRepoName() + "_" + table.getTableName()+"_"+op+"_source_"+userName+repo.getId());
                    medaSource.setSourceType(metaRepo.getRepoType());
                    medaSource.setRepoId(metaRepo);
                    medaSource.setSourceFile(table.getTableName());
                    medaSource.setRemarks(table.getTableDesc());
                    medaSource.setSourceDesc(table.getTableDesc());
                    medaSource.setSourceType(metaRepo.getRepoType());
                    
                    if(StringUtils.isEmpty(table.getExistId())){
                        medaSource.setIsNewRecord(true);
                        metaSourceService.save(medaSource);// 保存
                    }else{
                        if(userName.equals(table.getOperator())){//已抽取 的操作人  和当前操作人一致  才可以更新字段属性
                            medaSource.setId(table.getExistId()); 
                            medaSource.setIsNewRecord(false);
                            metaSourceService.deletePro(medaSource);
                            metaSourceService.save(medaSource);// 保存
                        }
                    }
                   
                } 
                
                 //保存平台存储
                    List<MetaStorePro> metaStorePros = new ArrayList<MetaStorePro>();
                    if (metaSourcePros != null)
                    {
                        for (MetaSourcePro metaSourcePro : metaSourcePros)
                        {
                            MetaStorePro storePro = new MetaStorePro();
                            storePro.setProDesc(metaSourcePro.getRemarks());
                            storePro.setProName(ChineseToEnglish.getPinYin(metaSourcePro.getProName()));
                            String type= metaSourcePro.getProType();
                            String type_1= MetaUtil.getHiveDataType(type,repoType);
                            if(StringUtils.isNotEmpty(type_1)){
                                type = type_1;
                            }
                            storePro.setProType(type);
                            storePro.setColumnSize(metaSourcePro.getColumnSize());
                            storePro.setId("");
                            storePro.setType(metaSourcePro.getType());//普通字段or主键字段
                            storePro.setProIndex(metaSourcePro.getProIndex());
                            metaStorePros.add(storePro);
                        }
                    }
                  
                    MetaStore metaStore = new MetaStore();
                    metaStore.setStoreName(metaRepo.getRepoName() + "_"
                            + table.getTableName()+"_"+op+"_store_"+userName+repo.getId());
                    metaStore.setStoreType(repoType);
                    metaStore.setRepoId(repo);
                    metaStore.setDelimiter(";");
                    metaStore.setStoreFile(ChineseToEnglish.getPinYin(table.getTableName()));
                    metaStore.setRemarks(table.getTableDesc());
                    metaStore.setStoreDesc(table.getTableDesc());
                    if(repoType==4){//抽取到hbase时,添加默认的列簇信息
                        String storeExternal = StringUtils.createRandomNumStr(4);
                        metaStore.setStoreExternal(storeExternal);
                    }
                    if (metaSourcePros != null)
                    {
                        metaStore.setMetaStoreProList(metaStorePros);
                    }
                    /**
                     * 查看是否存在相同的表
                     */
                    int enable=0;//标示当前表对应的平台存储是否被启用
                    MetaStore updateMetaStore = null;
                    for (MetaStore store : repo.getMetaStoreList())
                    {
                        if(table.getTableName().equals(store.getStoreFile())){
                            enable=store.getEnable();
                           table.setExistId(store.getId()); 
                           table.setOperator(store.getCreateBy().getName()); 
                           updateMetaStore = store;
                           break;
                        }                    
                    }
                    
                    if(StringUtils.isEmpty(table.getExistId())){
                        metaStore.setIsNewRecord(true);
                        metaStoreService.save(metaStore);
                     }else{
                          //已抽取 的操作人  和当前操作人一致    并未启用  才可以更新字段属性
                         if(userName.equals(table.getOperator())&&enable==0){
                             metaStore.setId(table.getExistId()); 
                             metaStore.setIsNewRecord(false);
                             metaStoreService.deletePro(metaStore);
                             metaStoreService.save(metaStore);
                         }else if(updateMetaStore!=null){
                             //跟新记录的修改时间,让用户可以在列表的第一条记录看到
                             updateMetaStore.setUpdateDate(new Date());
                             metaStoreService.updateMetaStore(updateMetaStore);
                         }
                     }
            }
            isSave=true;
        }  
    }
    
    @RequestMapping(value = "find-clums")
    @ResponseBody
    public JSONObject extractTables(String id, String tableName)
    {
        JSONObject data = new JSONObject();
        MetaRepo metaRepo = metaRepoService.get(id);
        List<MetaSourcePro> metaSourcePros = null;
        try
        {
            String password = metaRepo.getUserPwd();
            if(StringUtils.isNotEmpty(password) && password.endsWith("==")){
                password = AESUtil.decForTD(password);
            }
            List<MetaColumn> list = new ArrayList<MetaColumn>();
            if(metaRepo.getRepoType() == 5){//hive
                metaSourcePros = new DBUtils().getHiveMetaSourcePro(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName(),
                        tableName);
            }
            else if (metaRepo.getRepoType() == 6)//mysql
            {
                metaSourcePros = new DBUtils().getMetaSourcePro(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName(),
                        tableName);
            }
            else if (metaRepo.getRepoType() == 10)
            {
                metaSourcePros = new DBUtils().getSqlServerMetaSourcePro(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName(),
                        tableName);
            }
            
            else if (metaRepo.getRepoType() == 7)
            {
                metaSourcePros = new DBUtils().getOracleMetaSourcePro(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName(),
                        tableName,
                        metaRepo.getRepoInstance());
            }
            
            else if(metaRepo.getRepoType() == 11){
                metaSourcePros = new DBUtils().getTrafodionMetaSourcePro(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName(),
                        tableName);
            }
            
            for (MetaSourcePro metaColumn : metaSourcePros)
            {
                MetaColumn colum = new MetaColumn();
                colum.setColumnName(metaColumn.getProName());
                colum.setColumnComment(metaColumn.getRemarks());
                colum.setColumnType(metaColumn.getProType());
                list.add(colum);
            }
            JSONArray json = (JSONArray) JSONArray.toJSON(list);
            data.put("success", true);
            data.put("msg", "成功");
            data.put("data", json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            data.put("success", false);
            data.put("msg", "失败");
        }
        return data;
    }
    
    @RequestMapping(value = "testConData")
    @ResponseBody
    public JSONObject testCon(MetaRepo metaRepo)
    {
        JSONObject data = new JSONObject();
        String url = "";
        String msg = "连接成功！";
        Connection con = null;
        try
        {
            String password = metaRepo.getUserPwd();
//            if(StringUtils.isNotEmpty(password) && !password.equals("${input_dbpwd}")){
//                password = AESUtil.decForTD(password);
//            }
            if (metaRepo.getRepoType() == 6)//mysql
            {
                url = "jdbc:mysql://" + metaRepo.getIp() + ":"
                        + metaRepo.getPort() + "/"+metaRepo.getRepoName()+"?characterEncoding=UTF-8";
                con = DBUtils.getDBConnection(url,
                        metaRepo.getUserName(),
                        password);
            }
            else if (metaRepo.getRepoType() == 7)
            {//oracle
                url = "jdbc:oracle:thin:@" + metaRepo.getIp() + ":"
                        + metaRepo.getPort() + ":" + metaRepo.getRepoName();
                con = DBUtils.getOrcleConnection(url,
                        metaRepo.getUserName(),
                        password);
            }
            else if (metaRepo.getRepoType() == 5)
            {//hive
                url = "jdbc:hive2://" + metaRepo.getIp() + ":"
                        + metaRepo.getPort()
                        + "/"+metaRepo.getRepoName()+"?characterEncoding=UTF-8";
                con = DBUtils.getHiveConnection2(url,
                        metaRepo.getUserName(),
                        password);
            }else if(metaRepo.getRepoType() == 10)
            {
                String ip = metaRepo.getIp();
                String user = metaRepo.getUserName();
//                String pwd = metaRepo.getUserPwd();
                String port = metaRepo.getPort().toString();
                String dataBase = metaRepo.getRepoName();
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String URI = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName="
                        + dataBase;
                DriverManager.getConnection(URI, user, password);
            }else if(metaRepo.getRepoType() == 2)
            {
                FtpUtil t = new FtpUtil();
                t.connect("/",  metaRepo.getIp() , Integer.parseInt(metaRepo.getPort()),  metaRepo.getUserName(),password);
               
            } else
            {
                msg = "不支持此类型连接！";
            }
            data.put("success", true);
            data.put("msg", msg);
        }
        catch (Exception e)
        {
            data.put("success", false);
            data.put("msg", "连接失败,失败原因：" + e.getMessage());
        }
        finally
        {
            if (con != null)
            {
                try
                {
                    con.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
    
    @RequestMapping(value = "testCon")
    @ResponseBody
    public JSONObject testCon(String id)
    {
        JSONObject data = new JSONObject();
        MetaRepo metaRepo = metaRepoService.get(id, 3);
        String url = "";
        String msg = "连接成功！";
        Connection con = null;
        String ip = metaRepo.getIp();
        String user = metaRepo.getUserName();
        String password = metaRepo.getUserPwd();
        if(StringUtils.isNotEmpty(password) && password.endsWith("==")){
            password = AESUtil.decForTD(password);
        }
        String port = metaRepo.getPort().toString();
        String dataBase = metaRepo.getRepoName();
        try
        {
            if (metaRepo.getRepoType() == 6)//mysql
            {
                url = "jdbc:mysql://" + metaRepo.getIp() + ":"
                        + metaRepo.getPort() + "/"+ metaRepo.getRepoName()+"?characterEncoding=UTF-8";
                con = DBUtils.getDBConnection(url,
                        metaRepo.getUserName(),
                        password);
            }
            else if (metaRepo.getRepoType() == 7)
            {//oracle
                url = "jdbc:oracle:thin:@" + metaRepo.getIp() + ":"
                        + metaRepo.getPort() + ":" + metaRepo.getRepoName();
                con = DBUtils.getOrcleConnection(url,
                        metaRepo.getUserName(),
                        password);
            }
            else if (metaRepo.getRepoType() == 5)
            {//hive
                url = "jdbc:hive2://" + metaRepo.getIp() + ":"
                        + metaRepo.getPort()
                        + "/"+ metaRepo.getRepoName()+"?characterEncoding=UTF-8";
                con = DBUtils.getHiveConnection2(url,
                        metaRepo.getUserName(),
                        password);
            }
            else if(metaRepo.getRepoType() == 2)
            {
                FtpUtil t = new FtpUtil();
                t.connect("/",  metaRepo.getIp() , Integer.parseInt(metaRepo.getPort()),  metaRepo.getUserName(),password);
               
            }else if(metaRepo.getRepoType() == 10)
            {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String URI = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName="
                        + dataBase;
                DriverManager.getConnection(URI, user, password);
            }else if(metaRepo.getRepoType() == 11){//trafodion
                Class.forName("org.trafodion.jdbc.t4.T4Driver");
                String trafo_url = "jdbc:t4jdbc://" + ip + ":" + port + "/:";
                con = DriverManager.getConnection(trafo_url, user, password);
                Statement preparedStatement = con.createStatement();
                preparedStatement.execute("set schema " + dataBase);
            }else {
                msg = "不支持此类型连接！";
            }
            data.put("success", true);
            data.put("msg", msg);
        }
        catch (Exception e)
        {
            data.put("success", false);
            data.put("msg", "连接失败,失败原因：" + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            if (con != null)
            {
                try
                {
                    con.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
    ///repo/RepoSource/getRepoList
    @RequestMapping(value = "getRepoList")
    @ResponseBody
    public JSONObject getRepoList(int type,Integer metaType)
    {
        JSONObject data = new JSONObject();
        try
        { 
            metaType=null==metaType?0:metaType;
            List<HashMap<String, String>> repoList= metaRepoService.findListByRepoType(type,metaType);
            JSONArray json = (JSONArray) JSONArray.toJSON(repoList);
            data.put("success", true);
            data.put("msg", "成功");
            data.put("data", json);
        }
        catch (Exception e)
        {
            data.put("success", false);
            data.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return data;
    }
    
    @RequestMapping(value = "getTables")
    @ResponseBody
    public JSONObject getTables(String id )
    {
        JSONObject data = new JSONObject();
        try
        {
            MetaRepo metaRepo = metaRepoService.get(id, 0);
            String password = metaRepo.getUserPwd();
            if(StringUtils.isNotEmpty(password) && !password.equals("${input_dbpwd}")){
                password = AESUtil.decForTD(password);
            }
            List<MetaTable> tables=null;
            if(7==metaRepo.getRepoType())
            {
                 tables = new DBUtils().getOralceTables(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName(),
                        metaRepo.getRepoInstance());
            }else if(10==metaRepo.getRepoType())
            {
                tables = new DBUtils().getSqlserverTables(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName());
            }else{
                tables = new DBUtils().getMysqlTables(
                        metaRepo.getUserName(),
                        password,
                        metaRepo.getPort(),
                        metaRepo.getIp(),
                        metaRepo.getRepoName());
            }   
            for (MetaTable metaTable : tables)
            {
                 bk: for (MetaSource source : metaRepo.getMetaSourceList())
                  {
                    if(source.getSourceFile().equals(metaTable.getTableName()))
                    {
                        metaTable.setTablestatus("已存在");
                        break bk;
                    }
                  }
            }
            JSONArray json = (JSONArray) JSONArray.toJSON(tables);
            data.put("success", true);
            data.put("msg", "成功");
            data.put("data", json);
            metaRepo.setTables(tables);
        }
        catch (Exception e)
        {
            data.put("success", false);
            data.put("msg", e.getMessage());
            e.printStackTrace();
        }
       
        return data;
    }
}
