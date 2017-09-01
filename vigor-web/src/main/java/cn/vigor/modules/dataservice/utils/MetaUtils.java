/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.utils;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import cn.vigor.common.utils.SpringContextHolder;
import cn.vigor.modules.dataservice.dao.EServiceInfoDao;
import cn.vigor.modules.dataservice.entity.EServiceFunction;
import cn.vigor.modules.dataservice.entity.EServiceInfoMeta;
import cn.vigor.modules.partner.entity.PartnerServ;
import cn.vigor.modules.sys.utils.DictUtils;

/**
 * 字典工具类
 * @author liminfang
 * @version 2016-6-29
 */
public class MetaUtils {
	
	private static EServiceInfoDao serviceDao = SpringContextHolder.getBean(EServiceInfoDao.class);

    public static List<EServiceInfoMeta> getMetaList(Integer fromType) {
        List<EServiceInfoMeta> metaList = Lists.newArrayList();
        if(fromType == Constants.SERVICE_FROM_STORE) {
            metaList = serviceDao.getMetaStoreList();
        } else if(fromType == Constants.SERVICE_FROM_RESULT) {
            metaList = serviceDao.getMetaResultList();
        }
        for(EServiceInfoMeta meta : metaList) {
            if(fromType == Constants.SERVICE_FROM_STORE) {
                meta.setProList(meta.getStoreProList());
            } else if(fromType == Constants.SERVICE_FROM_RESULT) {
                meta.setProList(meta.getResultProList());
            }
        }
        return metaList;
    }
	
	public static String getMetaListJSON(Integer fromType) {
        return JSONObject.toJSONString(getMetaList(fromType));
    }
	
	public static String getMetaStoreListJSON() {
	    return getMetaListJSON(Constants.SERVICE_FROM_STORE);
	}
	
	public static String getMetaResultListJSON() {
        return getMetaListJSON(Constants.SERVICE_FROM_RESULT);
	}
	
	public static String getAvailbleFuncListJSON(Integer fromType, Integer dataType) {
	    List<EServiceFunction> funcList = Lists.newArrayList();
	    String typeLabel = "";
	    if(Constants.SERVICE_FROM_RESULT == fromType) {
	        typeLabel = DictUtils.getDictLabel("" + dataType, Constants.DICT_RESULT_TYPE, "mysql");
	    } else if(Constants.SERVICE_FROM_STORE == fromType) {
	        typeLabel = DictUtils.getDictLabel("" + dataType, Constants.DICT_STORE_TYPE, "hdfs");
	    } else {
	        typeLabel = "hdfs";
	    }
	    List<Integer> types = Lists.newArrayList();
	    if("hdfs".equalsIgnoreCase(typeLabel)) {
	        types.add(Constants.FUNCTYPE_DQ);
            types.add(Constants.FUNCTYPE_RHADOOP);
	    } else if("hive".equalsIgnoreCase(typeLabel)) {
	        types.add(Constants.FUNCTYPE_HIVE);
	    }
	    if(types.size() > 0) {
	        funcList = serviceDao.getFunctionListByTypes(types);
	    }
	    return JSONObject.toJSONString(funcList);
	}
	
	public static String getServiceListJSONByPartnerType(Integer partnerType) {
	    List<PartnerServ> serviceList = Lists.newArrayList();
	    if(partnerType == Constants.PARTNER_TYPE_JTN) {
	        serviceList = serviceDao.getAllServiceList();
	    } else if(partnerType == Constants.PARTNER_TYPE_JTW) {
	        serviceList = serviceDao.getRealTimeServiceList();
	    }
	    return JSONObject.toJSONString(serviceList);
	}
	

}
