/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.dataservice.dao.EServiceFunctionDao;
import cn.vigor.modules.dataservice.dao.EServiceInfoDao;
import cn.vigor.modules.dataservice.dao.EServiceProDao;
import cn.vigor.modules.dataservice.entity.EServiceFunction;
import cn.vigor.modules.dataservice.entity.EServiceInfo;
import cn.vigor.modules.dataservice.entity.EServicePro;
import cn.vigor.modules.dataservice.utils.Constants;
import cn.vigor.modules.sys.utils.DictUtils;

/**
 * 数据服务管理Service
 * @author liminfang
 * @version 2016-06-28
 */
@Service
@Transactional(readOnly = true)
public class EServiceInfoService extends CrudService<EServiceInfoDao, EServiceInfo> {

	@Autowired
	private EServiceFunctionDao eServiceFunctionDao;
	@Autowired
	private EServiceProDao eServiceProDao;
	
	public EServiceInfo get(int serviceId) {
		EServiceInfo eServiceInfo = super.dao.get(serviceId);
		eServiceInfo.seteServiceFunctionList(eServiceFunctionDao.findList(new EServiceFunction(eServiceInfo.getServiceId())));
		eServiceInfo.seteServiceProList(eServiceProDao.findList(new EServicePro(eServiceInfo.getServiceId())));
		return eServiceInfo;
	}
	
	public List<EServicePro> getMetaProList(int fromType, int dataId) {
	    List<EServicePro> proList = Lists.newArrayList();
	    if(Constants.SERVICE_FROM_RESULT == fromType) {
	        proList = this.dao.selectResultProListByMetaId(dataId);
	    } else if(Constants.SERVICE_FROM_STORE == fromType) {
	        proList = this.dao.selectStoreProListByMetaId(dataId);
	    }
	    return proList;
	}
	
	public List<EServiceInfo> findList(EServiceInfo eServiceInfo) {
		return super.findList(eServiceInfo);
	}
	
	public Page<EServiceInfo> findPage(Page<EServiceInfo> page, EServiceInfo eServiceInfo) {
		return super.findPage(page, eServiceInfo);
	}
	
	private void setServiceType(EServiceInfo eServiceInfo) {
	    String dataType = "";
	    if(eServiceInfo.getFromType() == Constants.SERVICE_FROM_STORE) {
	        dataType = DictUtils.getDictLabel("" + eServiceInfo.getDataType(), Constants.DICT_STORE_TYPE, "hdfs");
	    } else if(eServiceInfo.getFromType() == Constants.SERVICE_FROM_RESULT) {
	        dataType = DictUtils.getDictLabel("" + eServiceInfo.getDataType(), Constants.DICT_RESULT_TYPE, "mysql");
	    }
	    if("hive".equalsIgnoreCase(dataType) || "hdfs".equalsIgnoreCase(dataType)) {
	        eServiceInfo.setServiceType(Constants.SERVICE_TYPE_OFFLINE);
	    } else if("hbase".equalsIgnoreCase(dataType) || "mysql".equalsIgnoreCase(dataType) || "ftp".equalsIgnoreCase(dataType)) {
	        eServiceInfo.setServiceType(Constants.SERVICE_TYPE_REALTIME);
	    } else {
            eServiceInfo.setServiceType(Constants.SERVICE_TYPE_REALTIME);
	    }
	}
	
	@Transactional(readOnly = false)
	public void save(EServiceInfo eServiceInfo) {
	    setServiceType(eServiceInfo);
		super.save(eServiceInfo);
		System.out.println("insert service: " + eServiceInfo.getServiceId());
		boolean flag = false;
		for(EServiceFunction func : eServiceInfo.geteServiceFunctionList()) {
		    if(func.getFlag() == 1) {
		        flag = true;
		        break;
		    }
		}
		if(flag) {
		    eServiceFunctionDao.deleteServiceFuncByServiceId(eServiceInfo.getServiceId());
		    for (EServiceFunction eServiceFunction : eServiceInfo.geteServiceFunctionList()){
	            if (eServiceFunction.getFlag() == 1){
	                if (StringUtils.isBlank(eServiceFunction.getId())){
	                    eServiceFunction.setServiceId(eServiceInfo.getServiceId());
	                    //eServiceFunction.preInsert();
	                    eServiceFunctionDao.insert(eServiceFunction);
	                }
	            }else{
	                eServiceFunctionDao.delete(eServiceFunction);
	            }
	        }
		}
		
        if(eServiceInfo.geteServiceProList().size() > 0) {
            eServiceProDao.deleteServiceProByServiceId(eServiceInfo.getServiceId());
            for (EServicePro eServicePro : eServiceInfo.geteServiceProList()){
                if (eServicePro.getId() == null){
                    continue;
                }
                eServicePro.setServiceId(eServiceInfo.getServiceId());
                eServiceProDao.insert(eServicePro);
            }
        }
		
	}
	
	@Transactional(readOnly = false)
	public void delete(EServiceInfo eServiceInfo) {
		super.delete(eServiceInfo);
		eServiceFunctionDao.deleteServiceFuncByServiceId(eServiceInfo.getServiceId());
		eServiceProDao.deleteServiceProByServiceId(eServiceInfo.getServiceId());
	}
	
}