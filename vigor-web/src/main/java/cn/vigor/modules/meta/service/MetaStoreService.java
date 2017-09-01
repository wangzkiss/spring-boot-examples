package cn.vigor.modules.meta.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.ketter.dao.RdatabaseDao;
import cn.vigor.modules.ketter.entity.Rdatabase;
import cn.vigor.modules.meta.dao.DataPermissionDao;
import cn.vigor.modules.meta.dao.MetaRepoDao;
import cn.vigor.modules.meta.dao.MetaStoreDao;
import cn.vigor.modules.meta.dao.MetaStoreProDao;
import cn.vigor.modules.meta.entity.DataPermission;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.entity.MetaStorePro;
import cn.vigor.modules.meta.util.DBUtils;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.Encr;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 存储数据信息Service
 * @author kiss
 * @version 2016-05-17
 */
@Service
@Transactional(readOnly = true)
public class MetaStoreService extends CrudService<MetaStoreDao, MetaStore>
{
    
    @Autowired
    private MetaStoreProDao metaStoreProDao;
    
    @Autowired
    private MetaRepoDao metaRepoDao;
    
    @Autowired
    private DataPermissionDao dataPermissionDao;
    
    @Autowired
    private RdatabaseDao rdatabaseDao;
    
    @Override
    public MetaStore get(String id)
    {
        MetaStore metaStore = super.get(id);
        if(metaStore!=null){
            metaStore.setRepoId(metaRepoDao.get(metaStore.getRepoId()));
            metaStore.setMetaStoreProList(metaStoreProDao.findList(new MetaStorePro(metaStore)));
            return metaStore;
        }
        return new MetaStore();
    }
    
    public List<MetaStore> findList(MetaStore metaStore)
    {
        return super.findList(metaStore);
    }
    
    public Page<MetaStore> findPage(Page<MetaStore> page, MetaStore metaStore)
    {
        if (page.getOrderBy() == null || "".equals(page.getOrderBy()))
        {
            page.setOrderBy("id desc");
        }
        return super.findPage(page, metaStore);
    }
    
    /**
     * 找到所有平台存储信息
     * @param metaStore
     * @return
     */
    public List<MetaStore> findALL(MetaStore metaStore){
        Page<MetaStore> page = new Page<MetaStore>();
        if (page.getOrderBy() == null || "".equals(page.getOrderBy())){
            page.setOrderBy("id desc");
        }
        page.setPageSize(Integer.MAX_VALUE);
        metaStore.setPage(page);
        metaStore.preInsert();
        List<MetaStore> storlist = dao.findList(metaStore);
        for (MetaStore store : storlist){
            store.setRepoId(metaRepoDao.get(store.getRepoId()));
        }
        return storlist;
    }
    String storeId="";
    @Transactional(readOnly = false)
    public String saveTest(MetaStore metaStore){
    	save(metaStore);
    	return storeId;
    }
    @Transactional(readOnly = false)
    public void save(MetaStore metaStore)
    {
        MetaRepo repo = metaRepoDao.get(metaStore.getRepoId());
        metaStore.setRepoId(repo);
        if (metaStore.getStoreType() == 5)
        {
            metaStore.setStoreFile( metaStore.getStoreFile().toLowerCase());
            String hiveHdfsDir = Global.getConfig("hiveHdfsDir");
//            String hdfsPort = Global.getConfig("hdfsPort");
            String hdfs_service = Global.getConfig("hdfs_nameservices");
            // hdfs://192.168.12.188:9000/data/hewei/asdf/${kiss}/${test}
           
            if(repo.getPort().contains("${"))
            {
                hdfs_service="${output_hdfs_nameservices}";
            }
            repo.setRepoName(repo.getRepoName().toLowerCase());
            String dbdir = "default".equals(repo.getRepoName()) ? ""
                    :  repo.getRepoName() + ".db/";
            String hdfsUrl = "hdfs://" + hdfs_service +  hiveHdfsDir + dbdir + metaStore.getStoreFile().toLowerCase();
            String partPors = "/";
            if(repo.getPort().contains("${")){
                partPors += "${output_partition_path}/";
            }
            
           /* else{
                for (MetaStorePro storePro : metaStore.getMetaStoreProList())
                {
                    if (storePro.getType() == 1 && MetaStorePro.DEL_FLAG_NORMAL.equals(storePro.getDelFlag()))//分区字段
                    {
                        String dataFormat = storePro.getDataFormat();
                        //hive分区,当存储属性类型选择默认的分区格式时,以变量的方式存储,如果是自定义输入的,则以常量的方式存储
                        if(StringUtils.isNotEmpty(dataFormat) && dataFormat.equals("today")||dataFormat.equals("today_ep")||dataFormat.equals("lastday")
                                ||dataFormat.equals("lastday_ep")||dataFormat.equals("hour")){
                            partPors += storePro.getProName() + "=${" + storePro.getDataFormat()+ "}/";
                        }else{
                            partPors += storePro.getProName() + "=" + storePro.getDataFormat() + "/";
                        }
                    }
                }
            }*/
            hdfsUrl = hdfsUrl + partPors;
            metaStore.setHdfsInfo(hdfsUrl);
        }
        if (metaStore.getStoreType() == 3)
        {
//            String hdfsPort = Global.getConfig("hdfsPort");
            String hdfs_service = Global.getConfig("hdfs_nameservices");
            //MetaRepo repo = metaRepoDao.get(metaStore.getRepoId());
            if(repo.getPort().contains("${"))
            {
                hdfs_service = "${output_hdfs_nameservices}";
            }
            // hdfs://192.168.12.188:9000/data/hewei/asdf/${kiss}/${test}
            String hdfsUrl = "hdfs://" + hdfs_service + repo.getRepoName() + "/" + metaStore.getStoreFile().toLowerCase();
            metaStore.setHdfsInfo(hdfsUrl);
        }
        //自动生成mapping(编辑时不处理)
        if(metaStore.getStoreType()!=null && metaStore.getStoreType()==4 &&
                !metaStore.getStoreExternal().contains(";")){
            metaStore.setStoreExternal(metaStore.getStoreExternal()+";"+StringUtils.createRandomNumStr(6));
        }
        super.save(metaStore);
        storeId=metaStore.getId();
        //添加数据权限信息
        DataPermission datap = new DataPermission();
        datap.setAll("Y");//设置拥有所有
        datap.setUser(metaStore.getCurrentUser());//得到创建者
        datap.setDataType("store");//数据类型
        datap.setOfficeId(metaStore.getCurrentUser().getOffice().getId());
        datap.setDataId(metaStore.getId()); //设置数据
        if (metaStore.getIsNewRecord())
        {//标示新增
            dataPermissionDao.insert(datap);//插入权限信息
        }
        for (MetaStorePro metaStorePro : metaStore.getMetaStoreProList())
        {
            if (metaStorePro.getId() == null)
            {
                continue;
            }
            if (MetaStorePro.DEL_FLAG_NORMAL.equals(metaStorePro.getDelFlag()))
            {
                //如果长度没填写,则给默认长度,不需要长度的类型则将长度置为空
                String columnSize = handleColumnLength(metaStorePro.getProType(),metaStorePro.getColumnSize(),metaStore.getStoreType());
                metaStorePro.setColumnSize(columnSize);
                if(metaStore.getStoreType()==6 && metaStorePro.getProType().toLowerCase().equals("number")){//mysql没有number类型
                    metaStorePro.setProType("numeric");
                }else if(metaStore.getStoreType()==11){//trafodion
                    String cz = columnSize;
                    if(StringUtils.isNotEmpty(cz) && cz.contains(",")){
                        cz = cz.substring(0, cz.indexOf(","));
                    }
                    if(StringUtils.isNotEmpty(cz) && Integer.valueOf(cz)>18 && metaStorePro.getProType().toLowerCase().equals("number")){
                        metaStorePro.setProType("double");
                    }
                }
                if (StringUtils.isBlank(metaStorePro.getId()))
                {
                    metaStorePro.setStoreId(metaStore);
                    metaStorePro.preInsert();
                    metaStoreProDao.insert(metaStorePro);
                }
                else
                {
                    metaStorePro.preUpdate();
                    metaStoreProDao.update(metaStorePro);
                }
            }
            else
            {
                metaStoreProDao.delete(metaStorePro);
            }
        }
        if(!metaStore.getIsNewRecord()){
            //修改r_database表
            Rdatabase rdatabase = new Rdatabase();
            rdatabase.setRepoId(Integer.valueOf(metaStore.getRepoId().getId()));
            rdatabase.setStoreId(Integer.valueOf(metaStore.getId()));
            rdatabase.setHostName(metaStore.getIp());
            if(repo.getPort().contains("${")){
                rdatabase.setPort(-1);
            }else{
                rdatabase.setPort(Integer.valueOf(repo.getPort()));
            }
            rdatabase.setName(metaStore.getStoreName());
            rdatabase.setUserName(metaStore.getUserName());
            if(metaStore.getStoreType()==7){
                rdatabase.setDatabaseName(metaStore.getRepoId().getRepoInstance());
            }else{
                rdatabase.setDatabaseName(metaStore.getRepoName());
            }
            rdatabaseDao.updateByRepoIdAndMetaType(rdatabase, 1);
        }else{
            int databaseId = rdatabaseDao.getNextId();
            if(metaStore.getStoreType()!=3 && metaStore.getStoreType()!=4){//当平台存储的类型为hdfs,hbase时,不向r_database表插入数据
                //往r_database表insert数据
                Rdatabase entity = new Rdatabase();
                entity.setDatabaseId(databaseId);
                entity.setRepoId(Integer.valueOf(metaStore.getRepoId().getId()));
                entity.setStoreId(Integer.valueOf(metaStore.getId()));
                if(metaStore.getCreateBy()!=null){
                    entity.setLoginUserId(Integer.valueOf(metaStore.getCreateBy().getId()));
                }
                entity.setSourceId(0);
                entity.setHostName(metaStore.getIp());
                
                if(repo.getPort().contains("${")){
                    entity.setPort(-1);
                }else{
                    entity.setPort(Integer.valueOf(repo.getPort()));
                }
                entity.setName(metaStore.getStoreName());
                entity.setUserName(metaStore.getUserName());
                String password = metaStore.getUserPwd();
                if(StringUtils.isEmpty(password) || !password.startsWith("${")){
                    if(StringUtils.isNotEmpty(password) && password.endsWith("==")){
                        password = AESUtil.decForTD(password);
                    }
                    entity.setPassword(Encr.encryptPasswordIfNotUsingVariables(password));
                }else{
                    entity.setPassword(password);
                }
                entity.setDatabaseContypeId(1);
                int databaseTypeId = 0;
                if(metaStore.getStoreType()==5){//hive(对照r_database_type表进行映射)
                    databaseTypeId = 14;
                }else if(metaStore.getStoreType()==6){//mysql(对照r_database_type表进行映射)
                    databaseTypeId = 31;
                }else if(metaStore.getStoreType()==7){//oracle(对照r_database_type表进行映射)
                    databaseTypeId = 36;
                }else if(metaStore.getStoreType()==10){//sql server(对照r_database_type表进行映射)
                    databaseTypeId = 30;
                }else if(metaStore.getStoreType()==11){//trafodion(对照r_database_type表进行映射)
                    databaseTypeId = 9;
                }
                if(metaStore.getStoreType()==7){
                    //当平台存储到oracle时,databaseName使用repoInstance
                    entity.setDatabaseName(metaStore.getRepoId().getRepoInstance());
                }else{
                    entity.setDatabaseName(metaStore.getRepoName());
                }
                entity.setDatabaseTypeId(databaseTypeId);
                rdatabaseDao.insert(entity);
                int databaseAttriId = rdatabaseDao.getNextAttrId();
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                if(metaStore.getStoreType()==5 || metaStore.getStoreType()==6 ||
                        metaStore.getStoreType()==7 || metaStore.getStoreType()==10){//hive,mysql,oracle,sqlserver
                    Map<String,Object> param = new HashMap<String,Object>();
                    param.put("ID_DATABASE_ATTRIBUTE", databaseAttriId);
                    param.put("ID_DATABASE", entity.getDatabaseId());
                    param.put("CODE", "PORT_NUMBER");
                    param.put("VALUE_STR", metaStore.getPort());
                    list.add(param);
                }else if(metaStore.getStoreType()==11){//trafodion
                    Map<String,Object> param = new HashMap<String,Object>();
                    param.put("ID_DATABASE_ATTRIBUTE", databaseAttriId);
                    param.put("ID_DATABASE", entity.getDatabaseId());
                    param.put("CODE", "PORT_NUMBER");
                    param.put("VALUE_STR", metaStore.getPort());
                    list.add(param);
                    Map<String,Object> param1 = new HashMap<String,Object>();
                    param1.put("ID_DATABASE_ATTRIBUTE", databaseAttriId+1);
                    param1.put("ID_DATABASE", entity.getDatabaseId());
                    param1.put("CODE", "CUSTOM_DRIVER_CLASS");
                    param1.put("VALUE_STR", "org.trafodion.jdbc.t4.T4Driver");
                    list.add(param1);
                    Map<String,Object> param2 = new HashMap<String,Object>();
                    param2.put("ID_DATABASE_ATTRIBUTE", databaseAttriId+2);
                    param2.put("ID_DATABASE", entity.getDatabaseId());
                    param2.put("CODE", "CUSTOM_URL");
                    if(repo.getIp().startsWith("${")){
                        param2.put("VALUE_STR", "jdbc:t4jdbc://${output_ip}:${output_port}/:");
                    }else{
                        param2.put("VALUE_STR", "jdbc:t4jdbc://" + repo.getIp() + ":" + metaStore.getPort() + "/:");
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
    public void delete(MetaStore metaStore)
    {
        super.delete(metaStore);
       /* MetaStore mtemp = dao.getStoreExternalByStoreId(metaStore.getId());
        if(mtemp!=null){
   		 	super.delete(mtemp);
   	        DataPermission datap = new DataPermission();
   	        datap.setUser(metaStore.getCurrentUser());//得到创建者
   	        datap.setDataType("store");//数据类型
   	        datap.setDataId(mtemp.getId()); //设置数据
   	        dataPermissionDao.delete(datap);//插入权限信息
   	        metaStoreProDao.delete(new MetaStorePro(mtemp));
   	        //同时还需要删除r_database表和r_database_attribute表的数据
   	        Rdatabase rdatabase = new Rdatabase();
   	        rdatabase.setRepoId(Integer.valueOf(metaStore.getRepoId().getId()));
   	        rdatabase.setStoreId(Integer.valueOf(mtemp.getId()));
   	        rdatabase.setCurrentUser(metaStore.getCurrentUser());
   	        List<Rdatabase> rdatabases = rdatabaseDao.findList(rdatabase);
   	        if(rdatabases!=null && rdatabases.size()>0){
   	            for (Rdatabase rdb : rdatabases){
   	                rdatabaseDao.deleteAttrByRDId(rdb.getDatabaseId());
   	                rdatabaseDao.delete(rdb);
   	            }
   	        }
        }*/
        //添加数据权限信息
        DataPermission datap = new DataPermission();
        datap.setUser(metaStore.getCurrentUser());//得到创建者
        datap.setDataType("store");//数据类型
        datap.setDataId(metaStore.getId()); //设置数据
        dataPermissionDao.delete(datap);//插入权限信息
        metaStoreProDao.delete(new MetaStorePro(metaStore));
        //同时还需要删除r_database表和r_database_attribute表的数据
        Rdatabase rdatabase = new Rdatabase();
        rdatabase.setRepoId(Integer.valueOf(metaStore.getRepoId().getId()));
        rdatabase.setStoreId(Integer.valueOf(metaStore.getId()));
        rdatabase.setCurrentUser(metaStore.getCurrentUser());
        List<Rdatabase> rdatabases = rdatabaseDao.findList(rdatabase);
        if(rdatabases!=null && rdatabases.size()>0){
            for (Rdatabase rdb : rdatabases){
                rdatabaseDao.deleteAttrByRDId(rdb.getDatabaseId());
                rdatabaseDao.delete(rdb);
            }
        }
    }
    
    @Transactional(readOnly = false)
    public void deletePro(MetaStore metaStore)
    {
       metaStoreProDao.delete(new MetaStorePro(metaStore));
    }
    
    
    
    @Transactional(readOnly = false)
    public JSONObject enable(MetaRepo repo, MetaStore store)
    {
        JSONObject data = new JSONObject();
        //获取表信息
        boolean flag = false;
        String msg = "启用成功";
        try
        {
            //创建库  活目录
            flag = DBUtils.enable(repo, store, null, true);
            if (flag)
            {
                super.enable(store.getId());
                metaRepoDao.enable(repo.getId());
                data.put("sucess", true);
            }else{
                msg="启动失败！";
                data.put("sucess", false);
            }
        }
        catch (Exception e)
        {
            msg = "启动失败,失败原因：" + e.getMessage();
            if(msg.length()>50)
            {
                msg=msg.substring(0, 49) +"...";
            }
            e.printStackTrace();
        }
        data.put("msg", msg); 
        return data;
    }
    
    /**
     * 批量启用
     * @param map
     * @return
     */
    @Transactional(readOnly = false)
    public JSONObject batchEnable(Map<String,List<Object[]>> map){
        JSONObject data = new JSONObject();
        //获取表信息
        int success = 0;
        int error = 0;
        String msg = "启用成功";
        try
        {
            //创建库  活目录
            if(map!=null){
                Iterator<Entry<String,List<Object[]>>> iterator = map.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String, List<Object[]>> entry = iterator.next();
                    String key = entry.getKey();
                    List<Object[]> value = entry.getValue();
                    switch (Integer.valueOf(key)){
                        case 3:{
                            if(value!=null){
                                for(Object[] obj : value){
                                    MetaStore metaStore = (MetaStore)obj[0];
                                    MetaRepo metaRepo = (MetaRepo)obj[1];
                                    try
                                    {
                                        DBUtils.createHdfsDir(metaRepo, metaStore, true);
                                        super.enable(metaStore.getId());
                                        metaRepoDao.enable(metaRepo.getId());
                                        success = success + 1;
                                    }
                                    catch (Exception e)
                                    {
                                        error = error + 1;
                                        logger.info("启用异常:" + e);
                                        e.printStackTrace();
                                    }
                                }
                            }
                            break;
                        }
                        case 4:{
                            if(value!=null){
                                for(Object[] obj : value){
                                    MetaStore metaStore = (MetaStore)obj[0];
                                    MetaRepo metaRepo = (MetaRepo)obj[1];
                                    try
                                    {
                                        DBUtils.createHbaseTable(metaStore, true);
                                        success = success + 1;
                                        super.enable(metaStore.getId());
                                        metaRepoDao.enable(metaRepo.getId());
                                    }
                                    catch (Exception e)
                                    {
                                        error = error + 1;
                                        logger.info("启用异常:" + e);
                                        e.printStackTrace();
                                    }
                                }
                            }
                            break;
                        }
                        case 5:{
                            if(value!=null){
                                Map<String,List<Object[]>> mp = handleData(value);
                                Connection connection = null;
                                Iterator<Entry<String,List<Object[]>>> ite = mp.entrySet().iterator();
                                while(ite.hasNext()){
                                    Map.Entry<String, List<Object[]>> entry_1 = ite.next();
                                    List<Object[]> ll = entry_1.getValue();
                                    if(ll!=null && ll.size()>0){
                                        //相同类型,相同ip的外部数据源链接信息启用
                                        for (Object[] ob : ll)
                                        {
                                            try
                                            {
                                                MetaStore metaStore = (MetaStore)ob[0];
                                                MetaRepo metaRepo = (MetaRepo)ob[1];
                                                if(connection==null){
                                                    String ip = metaRepo.getIp();
                                                    String port = metaRepo.getPort().toString();
                                                    String hiveUrl = "jdbc:hive2://" + ip + ":" + port
                                                            + "/default?characterEncoding=UTF-8";
                                                    connection = DBUtils.getHiveConnection(hiveUrl);
                                                }
                                                DBUtils.excuteHiveSql(metaRepo, metaStore, true,connection);
                                                super.enable(metaStore.getId());
                                                metaRepoDao.enable(metaRepo.getId());
                                                success = success + 1;
                                            }catch (Exception e){
                                                error = error + 1;
                                                e.printStackTrace();
                                                logger.info("启用异常:" + e);
                                            }finally{
                                                if(connection!=null){
                                                    connection.close();
                                                    connection = null;
                                                }
                                            }
                                        }
                                        //一个批次执行结束之后,关闭连接
                                        if(connection!=null){
                                            connection.close();
                                            connection = null;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        case 6:{
                            if(value!=null){
                                Map<String,List<Object[]>> mp = handleData(value);
                                Connection connection = null;
                                Iterator<Entry<String,List<Object[]>>> ite = mp.entrySet().iterator();
                                while(ite.hasNext()){
                                    Map.Entry<String, List<Object[]>> entry_1 = ite.next();
                                    List<Object[]> ll = entry_1.getValue();
                                    if(ll!=null && ll.size()>0){
                                        for (Object[] ob : ll){
                                            try
                                            {
                                                MetaStore metaStore = (MetaStore)ob[0];
                                                MetaRepo metaRepo = (MetaRepo)ob[1];
                                                if(connection==null){
                                                    String ip = metaRepo.getIp();
                                                    String user = metaRepo.getUserName();
                                                    String pwd = metaRepo.getUserPwd();
                                                    if(StringUtils.isNotEmpty(pwd) && !pwd.equals("${input_dbpwd}")){
                                                        pwd = AESUtil.decForTD(pwd);
                                                    }
                                                    String port = metaRepo.getPort().toString();
                                                    String url = "jdbc:mysql://" + ip + ":" + port
                                                            + "/mysql?characterEncoding=UTF-8";
                                                    connection = DBUtils.getDBConnection(url, user, pwd);
                                                }
                                                DBUtils.excuteMysqlSql(metaRepo, metaStore, true,connection);
                                                super.enable(metaStore.getId());
                                                metaRepoDao.enable(metaRepo.getId());
                                                success = success + 1;
                                            }catch (Exception e){
                                                error = error + 1;
                                                e.printStackTrace();
                                                logger.info("启用异常:" + e);
                                            }finally{
                                                if(connection!=null){
                                                    connection.close();
                                                    connection = null;
                                                }
                                            }
                                        }
                                        //一个批次执行结束之后,关闭连接
                                        if(connection!=null){
                                            connection.close();
                                            connection = null;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        case 7:{
                            if(value!=null){
                                Map<String,List<Object[]>> mp = handleData(value);
                                Connection connection = null;
                                Iterator<Entry<String,List<Object[]>>> ite = mp.entrySet().iterator();
                                while(ite.hasNext()){
                                    Map.Entry<String, List<Object[]>> entry_1 = ite.next();
                                    List<Object[]> ll = entry_1.getValue();
                                    if(ll!=null && ll.size()>0){
                                        for (Object[] ob : ll){
                                            try
                                            {
                                                MetaStore metaStore = (MetaStore)ob[0];
                                                MetaRepo metaRepo = (MetaRepo)ob[1];
                                                if(connection==null){
                                                    String ip = metaRepo.getIp();
                                                    String user = metaRepo.getUserName();
                                                    String pwd = metaRepo.getUserPwd();
                                                    String port = metaRepo.getPort().toString();
                                                    String dataBase = metaRepo.getRepoName();
                                                    String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dataBase;
                                                    Class.forName("oracle.jdbc.driver.OracleDriver");
                                                    connection = DriverManager.getConnection(url,user,pwd);
                                                }
                                                DBUtils.excuteOracleSql(metaRepo, metaStore, true,connection);
                                                super.enable(metaStore.getId());
                                                metaRepoDao.enable(metaRepo.getId());
                                                success = success + 1;
                                            }catch (Exception e){
                                                error = error + 1;
                                                e.printStackTrace();
                                                logger.info("启用异常:" + e);
                                            }finally{
                                                if(connection!=null){
                                                    connection.close();
                                                    connection = null;
                                                }
                                            }
                                        }
                                        //一个批次执行结束之后,关闭连接
                                        if(connection!=null){
                                            connection.close();
                                            connection = null;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        case 9:{
                            if(value!=null){
//                                Map<String,List<Object[]>> mp = handleData(value);
                                //TODO kafka类型目前不支持,所以该段代码目前不实现,主要参照与DBUtil.createTopic并未实现
                            }
                            break;
                        }
                        case 10:{
                            if(value!=null){
                                Map<String,List<Object[]>> mp = handleData(value);
                                Connection connection = null;
                                Iterator<Entry<String,List<Object[]>>> ite = mp.entrySet().iterator();
                                while(ite.hasNext()){
                                    Map.Entry<String, List<Object[]>> entry_1 = ite.next();
                                    List<Object[]> ll = entry_1.getValue();
                                    if(ll!=null && ll.size()>0){
                                        for (Object[] ob : ll){
                                            try
                                            {
                                                MetaStore metaStore = (MetaStore)ob[0];
                                                MetaRepo metaRepo = (MetaRepo)ob[1];
                                                if(connection==null){
                                                    String ip = metaRepo.getIp();
                                                    String user = metaRepo.getUserName();
                                                    String pwd = metaRepo.getUserPwd();
                                                    String port = metaRepo.getPort().toString();
                                                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                                                    String URI = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName=master";
                                                    connection = DriverManager.getConnection(URI, user, pwd);
                                                }
                                                DBUtils.excuteSqlServer(metaRepo, metaStore, true,connection);
                                                super.enable(metaStore.getId());
                                                metaRepoDao.enable(metaRepo.getId());
                                                success = success + 1;
                                            }catch (Exception e){
                                                error = error + 1;
                                                e.printStackTrace();
                                                logger.info("启用异常:" + e);
                                            }finally{
                                                if(connection!=null){
                                                    connection.close();
                                                    connection = null;
                                                }
                                            }
                                        }
                                        //一个批次执行结束之后,关闭连接
                                        if(connection!=null){
                                            connection.close();
                                            connection = null;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        case 11:{
                            if(value!=null){
                                Map<String,List<Object[]>> mp = handleData(value);
                                Connection connection = null;
                                Iterator<Entry<String,List<Object[]>>> ite = mp.entrySet().iterator();
                                while(ite.hasNext()){
                                    Map.Entry<String, List<Object[]>> entry_1 = ite.next();
                                    List<Object[]> ll = entry_1.getValue();
                                    if(ll!=null && ll.size()>0){
                                        for (Object[] ob : ll){
                                            try
                                            {
                                                MetaStore metaStore = (MetaStore)ob[0];
                                                MetaRepo metaRepo = (MetaRepo)ob[1];
                                                if(connection==null){
                                                    String ip = metaRepo.getIp();
                                                    String user = metaRepo.getUserName();
                                                    String password = metaRepo.getUserPwd();
                                                    String port = metaRepo.getPort().toString();
                                                    Class.forName("org.trafodion.jdbc.t4.T4Driver");
                                                    String url = "jdbc:t4jdbc://" + ip + ":" + port + "/:";
                                                    connection = DriverManager.getConnection(url, user, password);
                                                }
                                                DBUtils.excuteTrafodion(metaRepo, metaStore, true,connection);
                                                super.enable(metaStore.getId());
                                                metaRepoDao.enable(metaRepo.getId());
                                                success = success + 1;
                                            }catch (Exception e){
                                                error = error + 1;
                                                logger.info("启用异常:" + e);
                                                e.printStackTrace();
                                            }finally{
                                                if(connection!=null){
                                                    connection.close();
                                                    connection = null;
                                                }
                                            }
                                        }
                                        //一个批次执行结束之后,关闭连接
                                        if(connection!=null){
                                            connection.close();
                                            connection = null;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        default:break;
                    }
                }
            }
//            flag = DBUtils.enable(repo, store, null, true);
//            if (flag)
//            {
//                super.enable(store.getId());
//                metaRepoDao.enable(repo.getId());
//                data.put("sucess", true);
//            }else{
//                msg="启动失败！";
//                data.put("sucess", false);
//            }
            data.put("sucess", true);
            msg = "启动成功"+success+"条,失败"+error+"条";
        }catch (Exception e){
            msg = "启动失败,失败原因：" + e.getMessage();
            if(msg.length()>50)
            {
                msg=msg.substring(0, 49) +"...";
            }
            e.printStackTrace();
        }
        data.put("msg", msg); 
        return data;
    }
    
    /**
     * 将list数组根据数组中的metaRepo对象中的ip值进行分类,并放入map中,ip作为key
     * @param value
     * @return
     */
    private Map<String,List<Object[]>> handleData(List<Object[]> value){
        Map<String,List<Object[]>> mp = new HashMap<String,List<Object[]>>();
        for(Object[] obj : value){
            MetaRepo metaRepo = (MetaRepo)obj[1];
            String ip = metaRepo.getIp();
            if(mp.get(ip)==null){
                List<Object[]> ls = new ArrayList<Object[]>();
                ls.add(obj);
                mp.put(ip,ls);
            }else{
                mp.get(ip).add(obj);
            }
        }
        return mp;
    }
    
    @Transactional(readOnly = false)
    public JSONObject disable(MetaRepo repo, MetaStore store)
    {
        JSONObject data = new JSONObject();
        //获取表信息
        boolean flag = false;
        String msg = "取消启用成功";
        try
        {
            //创建库  活目录
            flag = DBUtils.enable(repo, store, null, false);
            if (flag)
            {
                data.put("sucess", true);
                super.disable(store.getId());
            }else{
                if(store==null&& repo.getRepoType()==3)
                {
                    msg="取消启动失败！目录下存在文件，不支持删除";
                }else{
                    msg="取消启动失败！";
                }
                data.put("sucess", false);
            }
        }catch(SQLException e1){
            if(e1.getErrorCode()==-1389){
                //在取消启用时,如果catch的异常是表不存在,说明该表之前已经被drop了,这时候,我们需要将记录状态更新为取消启用
                super.disable(store.getId());
            }
            data.put("sucess", true);
        }catch (Exception e)
        {
            
            msg = "操作失败,失败原因：" + e.getMessage();
            if(msg.length()>50)
            {
                msg=msg.substring(0, 49) +"...";
            }
            e.printStackTrace();
        }
        data.put("msg", msg);
        return data;
    }
    
    public MetaStore getFolwInfo(String id)
    {
        MetaStore meta = dao.get(id);
        List<HashMap<String, Object>> etlMaps = dao.findEtlFlow(id);
        List<HashMap<String, Object>> calMaps = dao.findCalFlow(id);
        meta.setEtlMaps(etlMaps);
        meta.setCalMaps(calMaps);
        return meta;
    }
    @Transactional(readOnly = false)
    public JSONObject  getFolwInfo()
    {
    	JSONObject data=new JSONObject();
        List<HashMap<String, Object>> nodes = dao.findNodes();
        List<HashMap<String, Object>> links = dao.findLinks();
        data.put("nodes", nodes);
        data.put("links", links);
        return data;
    }
    /**
     * Hewei
     * 根据实体名称获取唯一记录
     * @param storeName 平台存储名称
     * @return MetaStore
     */
    public MetaStore findMetaStoreByName(String storeName)
    {
        return this.dao.findMetaStoreByName(storeName);
    }
    
    /**
     * Hewei 
     * 找到所有的平台存储信息
     * @param map
     * @return List<MetaStore>
     */
    public List<HashMap<String, Object>> findAllMetaStore(Integer storeType){
        User user = UserUtils.getUser();
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("createUserId", user.getId());
        map2.put("parKey", "%${%");
        map2.put("storeType", storeType);
        return this.dao.findAllMetaStore(map2);
    }
    
    /**
     * 根据实体类的中属性的名称以及属性名称对应的值找到 唯一的实体类
     * @param propertyName
     * @param value
     * @return MetaStore
     */
    public MetaStore findUniqueByProperty(String propertyName,String value){
        MetaStore metaStore = dao.findUniqueByProperty(propertyName, value);
        
        if(null != metaStore)
        {
            metaStore.setRepoId(metaRepoDao.get(metaStore.getRepoId()));
            metaStore.setMetaStoreProList(metaStoreProDao.findByStoreIds(Arrays.asList(Integer.valueOf(metaStore.getId()))));
        }
        
        return metaStore;
    }
    
    /**
     * 处理字段的长度问题
     * @param proType
     * @param columnSize
     * @return
     */
    private String handleColumnLength(String proType,String columnSize,Integer storeType){
        //针对带精度的字段size,将中文逗号分隔符自动修改为英文的,防止建表失败
        if(StringUtils.isNotEmpty(columnSize) && columnSize.contains("，")){
            columnSize = columnSize.replace("，", ",");
        }
        //如果字符串类型的属性没有给定长度,则给默认长度值(针对于手动创建的平台存储,默认值为500)
        proType = proType.toLowerCase();
        if(proType.contains("char")){
            return (StringUtils.isEmpty(columnSize)||columnSize.equals("0"))?"500":columnSize;
        }else if(proType.contains("date") || proType.equals("datetime")||proType.equals("time")
                ||proType.contains("text")||proType.contains("blob")||proType.equals("integer")){//这一类型字段都不给长度
            return null;
        }else if(proType.contains("int")){//int bigint smallint largeint
            if(StringUtils.isEmpty(columnSize)||(StringUtils.isNumeric(columnSize)&&Integer.valueOf(columnSize)<=0)){
                return null;
            }
        }
        return columnSize;
    }
    
    public List<Map<String,Object>> getMetaStoreByType(String repoId,List<Integer> types,User user){
        return this.dao.getMetaStoreByType(repoId, types,user);
    }
    
    public List<MetaStorePro> getMetaStorePros(List<Integer> storeIds){
        return metaStoreProDao.findByStoreIds(storeIds);
    }
    
    /**
     * 根据存储id和存储属性名称查询平台存储属性信息
     * @param storeId 平台存储id
     * @param proName 平台存储属性名称
     * @return
     */
    public MetaStorePro getByStoreIdAndProName(String storeId,String proName){
        MetaStorePro entity = new MetaStorePro();
        MetaStore metaStore = new MetaStore();
        metaStore.setId(storeId);
        entity.setStoreId(metaStore);
        entity.setProName(proName);
        List<MetaStorePro> list = metaStoreProDao.findList(entity);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Transactional(readOnly = false)
    public void updateMetaStore(MetaStore metaStore){
        this.dao.update(metaStore);
    }
    
    public List<MetaStore> getByDbInfoAndStoreType(String ip,String storeFile,
    		Integer storeType,String repoName){
    	return dao.getByDbInfoAndStoreType(ip, storeFile, storeType, repoName);
    }
    
/*    public MetaStore getStoreExternalByStoreId(String externalId)
    {
        return this.dao.getStoreExternalByStoreId(externalId);
    }*/
    
    
}