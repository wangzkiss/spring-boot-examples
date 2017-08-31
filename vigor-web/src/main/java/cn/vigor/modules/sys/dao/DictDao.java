/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.sys.dao;

import java.util.List;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface DictDao extends CrudDao<Dict> {

	public List<String> findTypeList(Dict dict);
    public List<Dict> findMetaTypes();
	
}
