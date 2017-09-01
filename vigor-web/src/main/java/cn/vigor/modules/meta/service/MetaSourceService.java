package cn.vigor.modules.meta.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.ketter.dao.RdatabaseDao;
import cn.vigor.modules.ketter.entity.Rdatabase;
import cn.vigor.modules.meta.dao.DataPermissionDao;
import cn.vigor.modules.meta.dao.MetaRepoDao;
import cn.vigor.modules.meta.dao.MetaSourceDao;
import cn.vigor.modules.meta.dao.MetaSourceProDao;
import cn.vigor.modules.meta.entity.DataPermission;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaSourcePro;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.Encr;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 源数据表信息Service
 * @author kiss
 * @version 2016-05-17
 */
@Service
@Transactional(readOnly = true)
public class MetaSourceService extends CrudService<MetaSourceDao, MetaSource> {

	@Autowired
	private MetaSourceProDao metaSourceProDao;
	@Autowired
    private MetaRepoDao metaRepoDao;
	
	@Autowired
	private DataPermissionDao dataPermissionDao;
	@Autowired
	private RdatabaseDao rdatabaseDao;
	
	public MetaSource get(String id) {
		MetaSource metaSource = super.get(id);
		if(metaSource!=null){
		    metaSource.setRepoId(metaRepoDao.get(metaSource.getRepoId()));
		    metaSource.setMetaSourceProList(metaSourceProDao.findList(new MetaSourcePro(metaSource)));
		    return metaSource;
		}
		return new MetaSource();
	}
	
	public List<MetaSource> findList(MetaSource metaSource) {
		return super.findList(metaSource);
	}
	
	public Page<MetaSource> findPage(Page<MetaSource> page, MetaSource metaSource) {
	    if(page.getOrderBy()==null|| "".equals(page.getOrderBy()))
        {
            page.setOrderBy("id desc");
        }
		return super.findPage(page, metaSource);
	}
	
	
	@Transactional(readOnly = false)
	public void save(MetaSource metaSource){
	    //自动生成mapping(编辑时不处理)
        if(metaSource.getSourceType()==4 &&!metaSource.getExternal().contains(";")){
            metaSource.setExternal(metaSource.getExternal()+";"+StringUtils.createRandomNumStr(6));
        }
		super.save(metaSource);
		//添加数据权限信息
		DataPermission datap=new DataPermission();
		datap.setAll("Y");//设置拥有所有
		datap.setUser(metaSource.getCurrentUser());//得到创建者
		datap.setOfficeId(metaSource.getCurrentUser().getOffice().getId());
		datap.setDataType("source");//数据类型
		datap.setDataId(metaSource.getId()); //设置数据
		if (metaSource.getIsNewRecord()){//标示新增
			dataPermissionDao.insert(datap);//插入权限信息
		}
		for (MetaSourcePro metaSourcePro : metaSource.getMetaSourceProList()){
			if (metaSourcePro.getId() == null){
				continue;
			}
			if (MetaSourcePro.DEL_FLAG_NORMAL.equals(metaSourcePro.getDelFlag())){
				if (StringUtils.isBlank(metaSourcePro.getId())){
					metaSourcePro.setSourceId(metaSource);
					metaSourcePro.preInsert();
					metaSourceProDao.insert(metaSourcePro);
				}else{
					metaSourcePro.preUpdate();
					metaSourceProDao.update(metaSourcePro);
				}
			}else{
				metaSourceProDao.delete(metaSourcePro);
			}
		}
		
		MetaRepo metaRepo = metaRepoDao.get(metaSource.getRepoId().getId());
		metaSource.setRepoId(metaRepo);
		if(!metaSource.getIsNewRecord()){
		    //修改r_database表
		    Rdatabase rdatabase = new Rdatabase();
		    rdatabase.setRepoId(Integer.valueOf(metaSource.getRepoId().getId()));
		    rdatabase.setSourceId(Integer.valueOf(metaSource.getId()));
		    rdatabase.setHostName(metaRepo.getIp());
		    rdatabase.setPort(Integer.valueOf(metaRepo.getPort()));
		    rdatabase.setName(metaSource.getSourceName());
		    rdatabase.setUserName(metaRepo.getUserName());
		    if(metaSource.getSourceType()==7){
                //当外部数据源到oracle时,databaseName使用repoInstance
                rdatabase.setDatabaseName(metaRepo.getRepoInstance());
            }else{
                rdatabase.setDatabaseName(metaRepo.getRepoName());
            }
            rdatabaseDao.updateByRepoIdAndMetaType(rdatabase, 0);
		}else{
		    int databaseId = rdatabaseDao.getNextId();
		    //外部数据源类型为hdfs,hbase,flume时,不往r_database表插入数据
		    if(metaSource.getSourceType()!=3 && metaSource.getSourceType()!=4 && metaSource.getSourceType()!=12){
		        Rdatabase rdatabase = new Rdatabase();
		        rdatabase.setCurrentUser(metaSource.getCurrentUser());
	            rdatabase.setRepoId(Integer.valueOf(metaSource.getRepoId().getId()));
	            rdatabase.setSourceId(Integer.valueOf(metaSource.getId()));
	            rdatabase.setHostName(metaRepo.getIp());
	            if(!metaRepo.getPort().startsWith("${")){
	                rdatabase.setPort(Integer.valueOf(metaRepo.getPort()));
	            }else{
	                rdatabase.setPort(-1);
	            }
	            rdatabase.setName(metaSource.getSourceName());
	            rdatabase.setUserName(metaRepo.getUserName());
	            if(metaSource.getSourceType()==7){
                    //当外部数据源到oracle时,databaseName使用repoInstance
	                rdatabase.setDatabaseName(metaRepo.getRepoInstance());
                }else{
                    rdatabase.setDatabaseName(metaRepo.getRepoName());
                }
	            rdatabase.setDatabaseContypeId(1);
	            String password = metaSource.getUserPwd();
	            if(StringUtils.isEmpty(password) || !password.startsWith("${")){
	                if(StringUtils.isNotEmpty(password) && password.endsWith("==")){
	                    password = AESUtil.decForTD(password);
	                }
	                rdatabase.setPassword(Encr.encryptPasswordIfNotUsingVariables(password));
                }else{
                    rdatabase.setPassword(metaRepo.getUserPwd());
                }
	            int databaseTypeId = 0;
                if(metaSource.getSourceType()==5){//hive(对照r_database_type表进行映射)
                    databaseTypeId = 14;
                }else if(metaSource.getSourceType()==6){//mysql(对照r_database_type表进行映射)
                    databaseTypeId = 31;
                }else if(metaSource.getSourceType()==7){//oracle(对照r_database_type表进行映射)
                    databaseTypeId = 36;
                }else if(metaSource.getSourceType()==10){//sql server(对照r_database_type表进行映射)
                    databaseTypeId = 30;
                }else if(metaSource.getSourceType()==11){//trafodion(对照r_database_type表进行映射)
                    databaseTypeId = 9;
                }
                rdatabase.setDatabaseTypeId(databaseTypeId);
                rdatabase.setDatabaseId(databaseId);
                rdatabaseDao.insert(rdatabase);
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                Integer databaseAttriId = rdatabaseDao.getNextAttrId();
                if(databaseAttriId==null){
                	databaseAttriId = 1;
                }
                if(metaSource.getSourceType()==5 || metaSource.getSourceType()==6 ||
                        metaSource.getSourceType()==7 || metaSource.getSourceType()==10){//hive,mysql,oracle,sqlserver
                    Map<String,Object> param = new HashMap<String,Object>();
                    param.put("ID_DATABASE_ATTRIBUTE", databaseAttriId);
                    param.put("ID_DATABASE", databaseId);
                    param.put("CODE", "PORT_NUMBER");
                    param.put("VALUE_STR", metaRepo.getPort());
                    list.add(param);
                }else if(metaSource.getSourceType()==11){//trafodion
                    Map<String,Object> param = new HashMap<String,Object>();
                    param.put("ID_DATABASE_ATTRIBUTE", databaseAttriId);
                    param.put("ID_DATABASE", databaseId);
                    param.put("CODE", "PORT_NUMBER");
                    param.put("VALUE_STR", metaRepo.getPort());
                    list.add(param);
                    Map<String,Object> param1 = new HashMap<String,Object>();
                    param1.put("ID_DATABASE_ATTRIBUTE", databaseAttriId+1);
                    param1.put("ID_DATABASE", rdatabase.getDatabaseId());
                    param1.put("CODE", "CUSTOM_DRIVER_CLASS");
                    param1.put("VALUE_STR", "org.trafodion.jdbc.t4.T4Driver");
                    list.add(param1);
                    Map<String,Object> param2 = new HashMap<String,Object>();
                    param2.put("ID_DATABASE_ATTRIBUTE", databaseAttriId+2);
                    param2.put("ID_DATABASE", rdatabase.getDatabaseId());
                    param2.put("CODE", "CUSTOM_URL");
                    if(metaSource.getIp().startsWith("${")){
                        param2.put("VALUE_STR", "jdbc:t4jdbc://${output_ip}:${output_port}/:");
                    }else{
                        param2.put("VALUE_STR", "jdbc:t4jdbc://" + metaRepo.getIp() + ":" + metaRepo.getPort() + "/:");
                    }
                    list.add(param2);
                }
                if(list.size()>0){
                	rdatabaseDao.insertRdatabaseAttr(list);
                }
		    }
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(MetaSource metaSource) {
		super.delete(metaSource);
		//添加数据权限信息
		DataPermission datap=new DataPermission();
		datap.setAll("Y");//设置拥有所有
		datap.setUser(metaSource.getCurrentUser());//得到创建者
		datap.setDataType("source");//数据类型
		datap.setDataId(metaSource.getId()); //设置数据
		dataPermissionDao.delete(datap);//插入权限信息
		metaSourceProDao.delete(new MetaSourcePro(metaSource));
		//同时还需要删除r_database表和r_database_attribute表的数据
		Rdatabase rdatabase = new Rdatabase();
        rdatabase.setRepoId(Integer.valueOf(metaSource.getRepoId().getId()));
        rdatabase.setStoreId(Integer.valueOf(metaSource.getId()));
        rdatabase.setCurrentUser(metaSource.getCurrentUser());
        List<Rdatabase> rdatabases = rdatabaseDao.findList(rdatabase);
        if(rdatabases!=null && rdatabases.size()>0){
            for (Rdatabase rdb : rdatabases){
                rdatabaseDao.deleteAttrByRDId(rdb.getDatabaseId());
                rdatabaseDao.delete(rdb);
            }
        }
	}
	
	@Transactional(readOnly = false)
    public void deletePro(MetaSource metaSource) {
        metaSourceProDao.delete(new MetaSourcePro(metaSource));
    }
	
    public MetaSource getFolwInfo(String id)
    {
        MetaSource metaSource=dao.get(id);
        List<HashMap<String,Object>> calMaplists=new ArrayList<HashMap<String,Object>>();
        List<HashMap<String,Object>> etlMaps= dao.findEtlFlow(id);
        List <String> ids =new ArrayList<String>();
        for (HashMap<String, Object> hashMap : etlMaps)
        {
            if(null!=hashMap.get("outputId")){
                String storeId=hashMap.get("outputId").toString();
                if(!ids.contains(storeId)){
                    List<HashMap<String,Object>> calMaps= dao.findCalFlow(storeId);
                    calMaplists.addAll(calMaps);
                    ids.add(storeId);
                }
            }
        }
        metaSource.setEtlMaps(etlMaps);
        metaSource.setCalMaps(calMaplists);
        return metaSource;
    }
	
    /**
     * 根据实体类的中属性的名称以及属性名称对应的值找到 唯一的实体类
     * @param propertyName
     * @param value
     * @return 
     */
    public MetaSource findUniqueByProperty(String propertyName,String value){
        MetaSource metaSource = dao.findUniqueByProperty(propertyName, value);
        
        if(null != metaSource)
        {
            metaSource.setRepoId(metaRepoDao.get(metaSource.getRepoId()));
            metaSource.setMetaSourceProList(metaSourceProDao.findList(new MetaSourcePro(metaSource)));
        }
       
       return metaSource;
    }
    
    /**
     * 找到所有的数据源信息
     * @param map 参数
     * @return List<MetaSource>
     */
    public List<HashMap<String, Object>> findAllMetaSource(Integer sourceType){
       User user = UserUtils.getUser();
       HashMap<String, Object> map2 = new HashMap<String, Object>();
       map2.put("createUserId", user.getId());
       map2.put("parKey", "%${%");
       map2.put("sourceType", sourceType);
       //input_source_name
       List<HashMap<String, Object>> sources = this.dao.findAllMetaSource(map2);
       for (HashMap<String, Object> source : sources) {
           if(source.get("id")!=null){
               int id = Integer.valueOf(String.valueOf(source.get("id")));
               List<MetaSourcePro>  pros = metaSourceProDao.findSourceProBySourceId(id);
               List<MetaSourcePro> ls = new ArrayList<MetaSourcePro>();
               for (MetaSourcePro metaPro : pros) {
                  if(metaPro.getProType().contains("date"))
                   {
                      ls.add(metaPro);
                   }
               }
               source.put("condition", ls);
           }
       }
       return sources;
    }
    
    /**
     * 根据数据源名称sourceName查询(绝对匹配)对于proType包含date的属性字段
     * @param sourceName
     * @return
     */
    public List<MetaSourcePro> findSourceProBySourceName(String sourceName){
        MetaSource metaSource = this.dao.getBySourceName(sourceName);
        List<MetaSourcePro> relist = new ArrayList<MetaSourcePro>();
        if(metaSource!=null){
            List<MetaSourcePro> list = metaSourceProDao.findSourceProBySourceId(Integer.valueOf(metaSource.getId()));
            if(list!=null && list.size()>0){
                for (MetaSourcePro metaSourcePro : list)
                {
                    relist.add(metaSourcePro);
                }
            }
        }
        return relist;
    }
}