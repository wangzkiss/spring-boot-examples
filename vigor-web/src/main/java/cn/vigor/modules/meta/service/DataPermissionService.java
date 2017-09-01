package cn.vigor.modules.meta.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.modules.meta.dao.DataPermissionDao;
import cn.vigor.modules.meta.entity.DataPermission;
import cn.vigor.modules.meta.entity.MetaRepo;

/**
 * 数据权限Service
 * 
 * @author kiss
 * @version 2016-05-31
 */
@Service
@Transactional(readOnly = true)
public class DataPermissionService extends CrudService<DataPermissionDao, DataPermission> {

	public DataPermission get(String id) {
		return super.get(id);
	}

	public List<DataPermission> findList(DataPermission dataPermission) {
		return super.findList(dataPermission);
	}

	public Page<DataPermission> findPage(Page<DataPermission> page, DataPermission dataPermission) {
		return super.findPage(page, dataPermission);
	}

	@Transactional(readOnly = false)
	public void save(DataPermission dataPermission) {
		int num = dao.update(dataPermission);
		if (num == 0) {
			dao.insert(dataPermission);
		}
	}

	@Transactional(readOnly = false)
	public void delete(DataPermission dataPermission) {
		super.delete(dataPermission);
	}

	public List<DataPermission> getDatapermissions(DataPermission param) {
		List<DataPermission> dataPermissions = dao.getDataPermissions(param);
		List<DataPermission> users = dao.getAllusers(param);
		for (DataPermission userPermission : users) {
			boolean ishave = false;
			flag: for (DataPermission dataPermission : dataPermissions) {
				if (dataPermission.getUser().getId().equals(userPermission.getUser().getId())) {
					ishave = true;
					break flag;
				}
			}
			if (!ishave) {
				userPermission.setDataId(param.getDataId());
				dataPermissions.add(userPermission);
			}
		}
		return dataPermissions;
	}

	public Page<MetaRepo> findPageBydataId(Page<MetaRepo> page, MetaRepo dataId) {
		dataId.setPage(page);
		page.setList(dao.findListBydataId(dataId));
		return page;
	}

}