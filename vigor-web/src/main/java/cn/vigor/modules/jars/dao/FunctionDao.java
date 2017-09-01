package cn.vigor.modules.jars.dao;


import java.util.List;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.jars.entity.Function;
import cn.vigor.modules.jars.entity.FunctionType;
import cn.vigor.modules.jars.entity.MapreduceJar;
import cn.vigor.modules.tji.entity.Job;

/**
 * 函数管理DAO接口
 * @author kiss
 * @version 2016-06-13
 */
@MyBatisDao
public interface FunctionDao extends CrudDao<Function> {

	public List<MapreduceJar> findListByjarId(MapreduceJar jarId);
	
	/**
	 * 找到所有的函数类型信息
	 * @return List<FunctionType>
	 */
	public List<FunctionType> getFunctionType();
	
	public List<Function> findAllFunctionByType(int type);
	
	public List<Job>findEtlJobModule(String type);
}