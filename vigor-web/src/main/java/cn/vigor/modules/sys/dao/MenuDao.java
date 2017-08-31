/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.sys.dao;

import java.util.List;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.sys.entity.Menu;

/**
 * 菜单DAO接口
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface MenuDao extends CrudDao<Menu> {

	public List<Menu> findByParentIdsLike(Menu menu);

	public List<Menu> findByUserId(Menu menu);
	
	public int updateParentIds(Menu menu);
	
	public int updateSort(Menu menu);
	
}
