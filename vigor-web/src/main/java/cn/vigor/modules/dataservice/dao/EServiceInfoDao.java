/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.dao;

import java.util.List;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.dataservice.entity.EServiceFunction;
import cn.vigor.modules.dataservice.entity.EServiceInfo;
import cn.vigor.modules.dataservice.entity.EServiceInfoMeta;
import cn.vigor.modules.dataservice.entity.EServicePro;
import cn.vigor.modules.partner.entity.PartnerServ;

/**
 * 数据服务管理DAO接口
 * @author liminfang
 * @version 2016-06-28
 */
@MyBatisDao
public interface EServiceInfoDao extends CrudDao<EServiceInfo> {

	public EServiceInfo get(int serviceId);
	public List<EServiceInfoMeta> getMetaStoreList();
	public List<EServiceInfoMeta> getMetaResultList();
	public List<EServicePro> selectStoreProListByMetaId(int dataId);
    public List<EServicePro> selectResultProListByMetaId(int dataId);
    public List<EServiceFunction> getFunctionListByTypes(List<Integer> types);
    public List<EServiceInfo> getServiceListByPartnerType(int partnerType);
    public List<PartnerServ> getRealTimeServiceList();
    public List<PartnerServ> getAllServiceList();
}