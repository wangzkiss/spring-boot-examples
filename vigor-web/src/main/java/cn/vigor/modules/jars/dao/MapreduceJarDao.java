package cn.vigor.modules.jars.dao;


import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.jars.entity.MapreduceJar;

/**
 * 执行包管理DAO接口
 * @author kiss
 * @version 2016-06-13
 */
@MyBatisDao
public interface MapreduceJarDao extends CrudDao<MapreduceJar> {

	public int isHaveFunction(String jarId);
}