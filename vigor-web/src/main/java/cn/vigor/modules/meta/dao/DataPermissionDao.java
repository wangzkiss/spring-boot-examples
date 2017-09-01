package cn.vigor.modules.meta.dao;


import java.util.List;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.DataPermission;
import cn.vigor.modules.meta.entity.MetaRepo;

/**
 * 数据权限DAO接口
 * @author kiss
 * @version 2016-05-31
 */
@MyBatisDao
public interface DataPermissionDao extends CrudDao<DataPermission> {

	public List<MetaRepo> findListBydataId(MetaRepo dataId);

	public List<DataPermission> getAllusers(DataPermission param);

	public List<DataPermission> getDataPermissions(DataPermission dataPermission);

	
}