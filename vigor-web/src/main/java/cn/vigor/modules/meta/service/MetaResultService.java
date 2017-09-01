package cn.vigor.modules.meta.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.config.Global;
import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.meta.dao.DataPermissionDao;
import cn.vigor.modules.meta.dao.MetaRepoDao;
import cn.vigor.modules.meta.dao.MetaResultDao;
import cn.vigor.modules.meta.dao.MetaResultProDao;
import cn.vigor.modules.meta.entity.DataPermission;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaResult;
import cn.vigor.modules.meta.entity.MetaResultPro;
import cn.vigor.modules.meta.entity.MetaStorePro;
import cn.vigor.modules.meta.util.DBUtils;
import cn.vigor.modules.sys.entity.User;

/**
 * 结果数据集Service
 * @author kiss
 * @version 2016-05-17
 */
@Service
@Transactional(readOnly = true)
public class MetaResultService extends CrudService<MetaResultDao, MetaResult>
{
    
    @Autowired
    private MetaResultProDao metaResultProDao;
    @Autowired
    private MetaRepoDao metaRepoDao;
    @Autowired
    private DataPermissionDao dataPermissionDao;
    
    public MetaResult get(String id)
    {
        MetaResult metaResult = super.get(id);
        metaResult.setRepoId(metaRepoDao.get(metaResult.getRepoId()));
        metaResult.setMetaResultProList(
                metaResultProDao.findList(new MetaResultPro(metaResult)));
        return metaResult;
    }
    
    public List<MetaResult> findList(MetaResult metaResult)
    {
        return super.findList(metaResult);
    }
    
    public Page<MetaResult> findPage(Page<MetaResult> page,
            MetaResult metaResult)
    {
        if(page.getOrderBy()==null|| "".equals(page.getOrderBy()))
        {
            page.setOrderBy("id desc");
        }
        return super.findPage(page, metaResult);
    }
    String resultId="";
    @Transactional(readOnly = false)
    public String saveTest(MetaResult metaResult){
    	save(metaResult);
    	return resultId;
    }
    @Transactional(readOnly = false)
    public void save(MetaResult metaResult)
    {
        
        if (metaResult.getResultType() == 5)
        {
            metaResult.setResultFile(metaResult.getResultFile().toLowerCase());
            String hiveHdfsDir = Global.getConfig("hiveHdfsDir");
            String hdfsPort = Global.getConfig("hdfsPort");
            // hdfs://192.168.12.188:9000/data/hewei/asdf/${kiss}/${test}
            MetaRepo repo = metaRepoDao.get(metaResult.getRepoId());
            repo.setRepoName(repo.getRepoName().toLowerCase());
            String dbdir = "defualt".equals(repo.getRepoName()) ? ""
                    :  repo.getRepoName() + ".db/";
            String hdfsUrl = "hdfs://" + repo.getIp() + ":" + hdfsPort + 
                    hiveHdfsDir+dbdir + metaResult.getResultFile().toLowerCase();
            String partPors = "/";
            if(repo.getIp().contains("${")){
                partPors = partPors + "${output_partition_path}/";
            }else{
                for (MetaResultPro resultPro : metaResult.getMetaResultProList())
                {
                    if (resultPro.getType() == 1 && MetaStorePro.DEL_FLAG_NORMAL.equals(resultPro.getDelFlag()))//分区字段
                    {
                        String dataFormat = resultPro.getDataFormat();
                        if(StringUtils.isNotEmpty(dataFormat) && dataFormat.equals("today")||dataFormat.equals("today_ep")||dataFormat.equals("lastday")
                                ||dataFormat.equals("lastday_ep")||dataFormat.equals("hour")){
                            partPors += resultPro.getProName() + "=${" + dataFormat+ "}/";
                        }else{
                            partPors += resultPro.getProName() + "=" + dataFormat + "/";
                        }
                    }
                }
            }
            hdfsUrl = hdfsUrl + partPors;
            metaResult.setHdfsInfo(hdfsUrl);
        }
        
        
        super.save(metaResult);
        resultId=metaResult.getId();
        //添加数据权限信息
        DataPermission datap = new DataPermission();
        datap.setAll("Y");//设置拥有所有
        datap.setUser(metaResult.getCurrentUser());//得到创建者
        datap.setDataType("result");//数据类型
        datap.setOfficeId(metaResult.getCurrentUser().getOffice().getId());
        datap.setDataId(metaResult.getId()); //设置数据
        if (metaResult.getIsNewRecord())
        {//标示新增
            dataPermissionDao.insert(datap);//插入权限信息
        }
        int proIndex = 1;
        for (MetaResultPro metaResultPro : metaResult.getMetaResultProList())
        {
            if (metaResultPro.getId() == null)
            {
                continue;
            }
            if (MetaResultPro.DEL_FLAG_NORMAL
                    .equals(metaResultPro.getDelFlag()))
            {
                if (StringUtils.isBlank(metaResultPro.getId()))
                {
                    metaResultPro.setResultId(metaResult);
                    metaResultPro.preInsert();
                    //如果长度没填写,则给默认长度,不需要长度的类型则将长度置为空
                    String columnSize = handleColumnLength(metaResultPro.getProType(),metaResultPro.getColumnSize(),metaResult.getResultType());
                    metaResultPro.setColumnSize(columnSize);
                    if(metaResult.getResultType()==6 && metaResultPro.getProType().toLowerCase().equals("number")){//mysql没有number类型
                        metaResultPro.setProType("numeric");
                    }else if(metaResult.getResultType()==11){//trafodion
                        String cz = columnSize;
                        if(StringUtils.isNotEmpty(cz) && cz.contains(",")){
                            cz = cz.substring(0, cz.indexOf(","));
                        }
                        if(StringUtils.isNotEmpty(cz) && Integer.valueOf(cz)>18 && metaResultPro.getProType().toLowerCase().equals("number")){
                            metaResultPro.setProType("double");
                        }
                    }
                    metaResultProDao.insert(metaResultPro);
                    proIndex = proIndex + 1;
                }
                else
                {
                    metaResultPro.preUpdate();
                    metaResultProDao.update(metaResultPro);
                    proIndex = proIndex + 1;
                }
            }
            else
            {
                if(!metaResultPro.getProName().equals("track_id")){
                    metaResultProDao.delete(metaResultPro);
                }
            }
        }
        //添加track_id属性
        addDefaultTrackIdColumn(metaResult, proIndex);
    }
    
    /**
     * 在生成结果集属性信息时,默认加上trackId属性
     * @param metaResult
     */
    private void addDefaultTrackIdColumn(MetaResult metaResult,int proIndex){
        String resultId = metaResult.getId();
        MetaResultPro pro = metaResultProDao.getByResultIdAndProName(resultId, "track_id");
        if(pro!=null){
            return;
        }
        String proType = null;
        switch (metaResult.getResultType())
        {
            case 3:
                proType = "varchar";//hdfs
                break;
            case 4:
                proType = "String";//hbase
                break;
            case 5:
                proType = "varchar";//hive
                break;
            case 6:
                proType = "varchar";//mysql
                break;
            case 7:
                proType = "varchar";//oracle
                break;
            case 10:
                proType = "varchar";//sqlserver
                break;
            case 11:
                proType = "varchar";//x-abase
                break;
            default:
                break;
        }
        MetaResultPro metaResultPro = new MetaResultPro();
        metaResultPro.setResultId(metaResult);
        metaResultPro.setProName("track_id");
        metaResultPro.setProIndex(proIndex);
        metaResultPro.setProType(proType);//根据到不同的存储类型,其字段类型不同
        metaResultPro.setProDesc("批次id");
        metaResultPro.setDelFlag("0");//默认生成的trackId在页面上不显示,故值设置为1
        metaResultPro.setType(2);
        metaResultPro.setDataFormat("");
        metaResultPro.setColumnSize("64");
        Date date = new Date();
        metaResultPro.setCreateDate(date);
        metaResultPro.setUpdateDate(date);
        metaResultPro.preInsert();
        metaResultProDao.insert(metaResultPro);
    }
    
    @Transactional(readOnly = false)
    public void delete(MetaResult metaResult)
    {
        super.delete(metaResult);
        //添加数据权限信息
        DataPermission datap = new DataPermission();
        datap.setUser(metaResult.getCurrentUser());//得到创建者
        datap.setDataType("result");//数据类型
        datap.setDataId(metaResult.getId()); //设置数据
        dataPermissionDao.delete(datap);//插入权限信息
        metaResultProDao.delete(new MetaResultPro(metaResult));
    }
    
    @Transactional(readOnly = false)
    public JSONObject enable(MetaRepo repo, MetaResult result)
    {
        JSONObject data = new JSONObject();
        //获取表信息
        boolean flag = false;
        String msg = "启用成功";
        try
        {
            //创建库  活目录
            flag = DBUtils.enable(repo, null, result,true);
            if (flag)
            {
                super.enable(result.getId());
                metaRepoDao.enable(repo.getId());
                data.put("sucess", true);
            }else{
                data.put("sucess", false);
                msg = "启动失败";
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
    
    @Transactional(readOnly = false)
    public JSONObject disable(MetaRepo repo, MetaResult result)
    {
        JSONObject data = new JSONObject();
        //获取表信息
        boolean flag = false;
        String msg = "取消启用成功";
        try
        {
            //创建库  活目录
            flag = DBUtils.enable(repo, null, result,false);
            if (flag)
            {
                super.disable(result.getId());
                data.put("sucess", true);
            }else{
              
                data.put("sucess", false);
                msg = "取消启动失败";
            }
        }
        catch (Exception e)
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
    public MetaResult getFolwInfo(String id)
    {
        MetaResult metaResult = dao.get(id);
        List<HashMap<String, Object>> etlMaplists = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> calMaps = dao.findCalFlow(id);
        List <String> ids =new ArrayList<String>();
        for (HashMap<String, Object> hashMap : calMaps)
        {
            if (null != hashMap.get("inputId"))
            {
                String storeId = hashMap.get("inputId").toString();
               if(!ids.contains(storeId)){
                   List<HashMap<String, Object>> etlMaps = dao
                           .findEtlFlow(storeId);
                   etlMaplists.addAll(etlMaps);
                   ids.add(storeId);
               }
            }
        }
        metaResult.setEtlMaps(etlMaplists);
        metaResult.setCalMaps(calMaps);
        return metaResult;
    }
    
    public List<Map<String,Object>> getMetaResultByType(String repoId,List<Integer> types,User user){
        return this.dao.getMetaResultByType(repoId, types, user);
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
}