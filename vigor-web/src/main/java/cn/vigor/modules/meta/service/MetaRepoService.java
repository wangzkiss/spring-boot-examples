package cn.vigor.modules.meta.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.pentaho.di.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.ketter.dao.RdatabaseDao;
import cn.vigor.modules.ketter.entity.Rdatabase;
import cn.vigor.modules.meta.dao.DataPermissionDao;
import cn.vigor.modules.meta.dao.MetaRepoDao;
import cn.vigor.modules.meta.dao.MetaResultDao;
import cn.vigor.modules.meta.dao.MetaSourceDao;
import cn.vigor.modules.meta.dao.MetaStoreDao;
import cn.vigor.modules.meta.entity.DataPermission;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaResult;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.util.DBUtils;
import cn.vigor.modules.meta.util.PrivilegeException;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 数据库连接信息Service
 * @author kiss
 * @version 2016-05-13
 */
@Service
@Transactional(readOnly = true)
public class MetaRepoService extends CrudService<MetaRepoDao, MetaRepo> {

	@Autowired
	private MetaResultDao metaResultDao;
	@Autowired
	private MetaSourceDao metaSourceDao;
	@Autowired
	private MetaStoreDao metaStoreDao;
	@Autowired
	private DataPermissionDao dataPermissionDao;
	@Autowired
	private RdatabaseDao rdatabaseDao;
    
	public MetaRepo get(String id,int type) {
		MetaRepo metaRepo = super.get(id);
		User user=UserUtils.getUser();
		if (metaRepo==null) return null;
		if(type==0)
		{
			MetaSource param=new MetaSource(metaRepo);
			param.setCreateBy(user);
			metaRepo.setMetaSourceList(metaSourceDao.findList(param));
		}else if(type==1)
		{
			MetaStore param=new MetaStore(metaRepo);
			param.setCreateBy(user);
			metaRepo.setMetaStoreList(metaStoreDao.findList(param));
		}else if(type==2){
			MetaResult param=new MetaResult(metaRepo);
			param.setCreateBy(user);
			metaRepo.setMetaResultList(metaResultDao.findList(param));
		}
		return metaRepo;
	}
	public MetaRepo get(String id) {
        MetaRepo metaRepo = super.get(id);
        User user=UserUtils.getUser();
        MetaStore param=new MetaStore(metaRepo);
        param.setCreateBy(user);
        metaRepo.setMetaStoreList(metaStoreDao.findList(param));
        MetaResult param2=new MetaResult(metaRepo);
        param2.setCreateBy(user);
        metaRepo.setMetaResultList(metaResultDao.findList(param2));
        return metaRepo;
    }
	
	/**
	 * 自助查询专用
	 * @param id
	 * @return
	 */
	public MetaRepo getCon(String id) {
        MetaRepo metaRepo = super.get(id);
        return metaRepo;
    }
    
    /**
     * 自助查询专用  查询此连接信息下的表结构
     * @param id
     * @return
     */
	public MetaRepo getTables(String id) {
        MetaRepo metaRepo = super.get(id);
        User user=UserUtils.getUser();
        MetaStore param=new MetaStore(metaRepo);
        param.setCreateBy(user);
        List<MetaStore> storlist = metaStoreDao.findList(param);
        /*for (MetaStore store : storlist)
        {
            store.setRepoId(dao.get(store.getRepoId()));
            store.setMetaStoreProList(metaStoreProDao.findList(new MetaStorePro(store)));
        }*/
        metaRepo.setMetaStoreList(storlist);
        MetaResult param2=new MetaResult(metaRepo);
        param2.setCreateBy(user);
        List<MetaResult> resultlist = metaResultDao.findList(param2);
       /* for (MetaResult result : resultlist)
        {
            result.setRepoId(dao.get(result.getRepoId()));
            result.setMetaResultProList(metaResultProDao.findList(new MetaResultPro(result)));
        }*/
        metaRepo.setMetaResultList(resultlist);
        return metaRepo;
    }
	public List<MetaRepo> findList(MetaRepo metaRepo) {
		return super.findList(metaRepo);
	}
	public List<MetaRepo> findList(Integer type) {
		MetaRepo metaRepo=new MetaRepo();
		metaRepo.setMetaType(type);
		metaRepo.preUpdate();
		return super.findList(metaRepo);
	}
	
	public List<MetaRepo> findListBy(Integer datatype) {
        MetaRepo metaRepo=new MetaRepo();
        metaRepo.setRepoType(datatype);
        metaRepo.preUpdate();
        
        return dao.findListByType(metaRepo);
    }
	public Page<MetaRepo> findPage(Page<MetaRepo> page, MetaRepo metaRepo) {
	    if(page.getOrderBy()==null|| "".equals(page.getOrderBy()))
        {
            page.setOrderBy("id desc");
        }
		return super.findPage(page, metaRepo);
	}
	
	String repoId="";
	@Transactional(readOnly = false)
	public String saveTest(MetaRepo metaRepo) {
		save(metaRepo);
		return repoId;
	}
	/**
	 * 保存生成数据权限信息(e_data_permission表)
	 */
	@Transactional(readOnly = false)
	public void save(MetaRepo metaRepo) {
	    //对metaRepo的密码进行加密(模板的不处理)
	    if(StringUtils.isNotEmpty(metaRepo.getUserPwd()) && !metaRepo.getUserPwd().equals("${input_dbpwd}")
	    		&& !metaRepo.getUserPwd().endsWith("==")){
	        metaRepo.setUserPwd(AESUtil.encForTD(metaRepo.getUserPwd()));
	    }
		super.save(metaRepo);
		repoId=metaRepo.getId();
		//添加数据权限信息
		DataPermission datap=new DataPermission();
		datap.setAll("Y");//设置拥有所有
		datap.setUser(metaRepo.getCurrentUser());//得到创建者
		datap.setDataType("repo");//数据类型
		datap.setOfficeId(metaRepo.getCurrentUser().getOffice().getId());
		datap.setDataId(metaRepo.getId()); //设置数据
		if (metaRepo.getIsNewRecord()){//标示新增
			dataPermissionDao.insert(datap);//插入权限信息
		}else{
		    //更新r_database
		    Rdatabase rdatabase = new Rdatabase();
		    rdatabase.setUserName(metaRepo.getUserName());
		    rdatabase.setPassword(encryptPasswordIfNotUsingVariables(metaRepo.getUserPwd()));//TODO 对密码加密
		    rdatabase.setDatabaseName(metaRepo.getRepoName());
		    if(StringUtils.isNotEmpty(metaRepo.getPort()) && !metaRepo.getPort().contains("${")){
		        rdatabase.setPort(Integer.valueOf(metaRepo.getPort()));
		    }
		    if(StringUtils.isNotEmpty(metaRepo.getId())){
		        rdatabase.setRepoId(Integer.valueOf(metaRepo.getId()));
		    }
		    rdatabase.setHostName(metaRepo.getIp());
		    rdatabaseDao.updateByRepoIdAndMetaType(rdatabase, -1);
		}
	}
	
	/**
	 * 对密码加密
	 * @param password 密码明文
	 * @return 加密结果
	 */
	private String encryptPasswordIfNotUsingVariables( String password ) {
	    String encrPassword = "";
	    List<String> varList = new ArrayList<String>();
	    StringUtil.getUsedVariables( password, varList, true );
	    if(varList.isEmpty()){
	      encrPassword = KettleTwoWayPasswordEncoder.PASSWORD_ENCRYPTED_PREFIX + KettleTwoWayPasswordEncoder.encryptPassword(password);
	    }else{
	      encrPassword = password;
	    }
	    return encrPassword;
	  }
	
	@Transactional(readOnly = false)
	public void delete(MetaRepo metaRepo,int type) throws PrivilegeException {
	    MetaRepo repo=get(metaRepo.getId(),type);
	    
	    if(repo.getMetaResultList().size()>0 )
	    {
	        String names="";
	        for (MetaResult res : repo.getMetaResultList())
            {
                names+=res.getResultName()+"<br/>";
            }
	       throw new PrivilegeException("001","此连接信息存在关联以下结果集信息：<br/>"+names+"不可以删除！");
	    }else if( repo.getMetaSourceList().size()>0 ){
	        String names="";
            for (MetaSource res : repo.getMetaSourceList())
            {
                names+=res.getSourceName()+"<br/>";
            }
           throw new PrivilegeException("001","此连接信息存在关联以下外部源信息：<br/>"+names+"不可以删除！");
	    }if( repo.getMetaStoreList().size()>0 ){
	        String names="";
            for (MetaStore res : repo.getMetaStoreList())
            {
                names+=res.getStoreName()+"<br/>";
            }
           throw new PrivilegeException("001","此连接信息存在关联以下存储信息：<br/>"+names+"不可以删除！");
	        }
		super.delete(metaRepo);
		//添加数据权限信息
		DataPermission datap=new DataPermission();
		datap.setAll("Y");//设置拥有所有
		datap.setOfficeId(metaRepo.getCurrentUser().getOffice().getId());
		datap.setDataId(metaRepo.getId());
		datap.setUser(metaRepo.getCurrentUser());//得到创建者
		datap.setDataType("source");//数据类型
		datap.setDataId(metaRepo.getId()); //设置数据
		dataPermissionDao.delete(datap);//插入权限信息
		metaResultDao.delete(new MetaResult(metaRepo));
		metaSourceDao.delete(new MetaSource(metaRepo));
		metaStoreDao.delete(new MetaStore(metaRepo));
	}
	
	@Transactional(readOnly = false)
    public JSONObject  enable( MetaRepo repo)
    {     
        JSONObject data = new JSONObject();
        //获取表信息
        boolean flag=false;
        String msg="启用成功";
        try {
            //创建库  活目录
            flag=DBUtils.enable(repo, null, null,true);
            if(flag)
            {
                super.enable(repo.getId());
            }else{
                msg="启动失败";
            }   
        } catch (Exception e) {
            msg="启动失败,失败原因："+e.getMessage();
            e.printStackTrace();
        }
        data.put("msg", msg);
        data.put("sucess",flag);
        return data;  
    }   
	
	@Transactional(readOnly = false)
    public JSONObject  disable( MetaRepo repo)
    {     
        JSONObject data = new JSONObject();
        //获取表信息
        boolean flag=false;
        String msg="取消成功";
        try {
            //创建库  活目录
            flag=DBUtils.enable(repo, null, null,false);
            if(flag)
            {
                super.disable(repo.getId());
                for (MetaResult result : repo.getMetaResultList())
                {
                    metaResultDao.disable(result.getId());
                }
                for (MetaStore store : repo.getMetaStoreList())
                {
                    metaStoreDao.disable(store.getId());
                }
            }else{
                if( repo.getRepoType()==2)
                {
                    msg="取消启动失败！目录下存在文件，不支持删除";
                }else{
                    msg="取消启动失败！";
                }
            }       
        } catch (Exception e) {
            msg="操作失败,失败原因："+e.getMessage();
            e.printStackTrace();
        }
        data.put("msg", msg);
        data.put("sucess",flag);
        return data;  
    }

    public List<HashMap<String,String>> findListByRepoType(int repoType,int metaType)
    {
        MetaRepo repo=new  MetaRepo();
        repo.setMetaType(metaType);
        repo.setRepoType(repoType);
        repo.preInsert();
        return  dao.findListByRepoType(repo);
    }
    
    /**
     * 根据资源类型metaType和存放类型repoType查询
     * @param metaTypes
     * @param repoTypes
     * @return 结果
     */
    public List<MetaRepo> getHdfsAndFtpSources(List<Integer> metaTypes,List<Integer> repoTypes,int isAdmin,User user){
        return dao.getHdfsAndFtpSources(metaTypes, repoTypes,isAdmin,user);
    }
    
    
    public List<MetaRepo> getAllRepo(List<Integer> metaTypes,List<Integer> repoTypes,int isAdmin){
        return dao.getAllRepo(metaTypes, repoTypes,isAdmin);
    }
    public MetaRepo findExistRepo(MetaRepo repo)
    {
        MetaRepo metaRepo=dao.findExistRepo(repo);
        if(metaRepo!=null){
            User user=UserUtils.getUser();
            MetaStore param=new MetaStore(metaRepo);
            param.setCreateBy(user);
            metaRepo.setMetaStoreList(metaStoreDao.findList(param));
        }
        return metaRepo;
    }
}