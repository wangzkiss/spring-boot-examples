package cn.vigor.modules.meta.util;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.vigor.common.utils.SpringContextHolder;
import cn.vigor.modules.jars.dao.FunctionDao;
import cn.vigor.modules.jars.entity.Function;
import cn.vigor.modules.meta.dao.BaseDateParamsDao;
import cn.vigor.modules.meta.dao.MetaRepoDao;
import cn.vigor.modules.meta.entity.BaseDateParams;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.sys.dao.DictDao;
import cn.vigor.modules.sys.entity.Dict;
import cn.vigor.modules.tji.entity.Job;

public class MetaUtil
{
    private static MetaRepoDao repoDao = SpringContextHolder
            .getBean(MetaRepoDao.class);
    
    private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
    
    private static FunctionDao functDao = SpringContextHolder.getBean(FunctionDao.class);
    
    private static BaseDateParamsDao baseDateParamsDao = SpringContextHolder.getBean(BaseDateParamsDao.class);
    
    private static HashMap<String, String> dictMap = Maps.newHashMap();
    
    public static final String CACHE_DICT_MAP = "repoMap";
    
    public static List<MetaRepo> getRepoList1(Integer type)
    {
        MetaRepo param = new MetaRepo();
        param.setMetaType(type);
        return repoDao.findAllList(param);
    }
    public static String getHiveDataType(String dataType,int type)
    {
        String hievType="string";
        dictMap = Maps.newHashMap();
        if(dictMap.get(type+"_"+dataType)==null){
            hievType = repoDao.getHiveDataType(dataType.toLowerCase(),type);
            dictMap.put(type+"_"+dataType, hievType);
        }else{
            hievType=dictMap.get(type+"_"+dataType);
        }
        return hievType;
    } 
    
    
    public static List<MetaRepo> getRepoList(Integer type)
    {
        dictMap = Maps.newHashMap();
        MetaRepo param = new MetaRepo();
        param.preInsert();
        param.setMetaType(type);
        List<MetaRepo> dictList = repoDao.findAllList(param);
        if (dictList == null)
        {
            dictList = Lists.newArrayList();
        }
       
        return dictList;
    }
    
    public static List<MetaRepo> getRepoListByType(Integer type,Integer repoType)
    {
        dictMap = Maps.newHashMap();
        MetaRepo param = new MetaRepo();
        param.preInsert();
        param.setMetaType(type);
        param.setRepoType(repoType);
        List<MetaRepo> dictList = repoDao.findAllList(param);
        if (dictList == null)
        {
            dictList = Lists.newArrayList();
        }
       
        return dictList;
    }
    
    public static String getTypeString(Integer type)
    {
        if (!dictMap.containsKey(type + ""))
        {
            List<Dict> typelist = dictDao.findMetaTypes();
            for (Dict dict : typelist)
            {
                dictMap.put(dict.getValue(), dict.getLabel());
            }
        }
        return dictMap.get(type + "");
    }
    
    /**
     * 
     * @param type
     * @return
     */
    public static List<Function> getFunctionListByType(Integer type)
    {
        List<Function> dictList = functDao.findAllFunctionByType(type);
        if (dictList == null)
        {
            dictList = Lists.newArrayList();
        }
        return dictList;
    }
    
    public static List<Job> findEtlJobModule(String type){
        List<Job> jobs= functDao.findEtlJobModule(type);
        if (jobs == null)
        {
            jobs = Lists.newArrayList();
        }
        return jobs;
    }
    
    public static List<BaseDateParams> findBaseDateParams(Integer forPartition){
        BaseDateParams entity = new BaseDateParams();
        entity.setForPartition(forPartition);
        List<BaseDateParams> list = baseDateParamsDao.findList(entity);
        return list;
    }
}
