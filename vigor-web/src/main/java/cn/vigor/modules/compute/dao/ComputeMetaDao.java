package cn.vigor.modules.compute.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.compute.bean.ComputeMetaResult;
import cn.vigor.modules.compute.bean.ComputeMetaSource;
import cn.vigor.modules.compute.bean.ComputeMetaStore;
import cn.vigor.modules.compute.bean.InParam;
import cn.vigor.modules.compute.bean.RDataBaseMeta;
import cn.vigor.modules.compute.bean.Repositories;
import cn.vigor.modules.compute.bean.TmpTable;


/**
 * 元数据处理Dao
 * @author Hewei
 *
 */
@MyBatisDao
public interface ComputeMetaDao {
	
	public List<Repositories> getAllRepo(InParam fstore);
	
	public List<ComputeMetaStore> getMetaStoresByTypes(InParam fstore);
	
	public List<ComputeMetaStore> getModelMetaStoresByTypes(InParam fstore);
	
	public List<ComputeMetaStore> getMetaStoresByTypesForKettle(InParam fstore);
	
	public List<ComputeMetaStore> getMetaStores(InParam fstore);
	
	public List<ComputeMetaResult> getMetaResultByTypes(InParam fstore);
	
	public List<ComputeMetaResult> getMetaResults(InParam fstore);
	
	public int isAdmin(int userId);
	
	public List<RDataBaseMeta> getAllRDataBases(InParam fstore);
	
	public List<ComputeMetaSource> getMetaSources(InParam fstore);
	
	public List<ComputeMetaSource>getModelMetaSourceByTypes(InParam fstore);
	
	public List<ComputeMetaSource>getMetaSourcesByTypes(InParam fstore);
	
	public List<Repositories> getDataBaseName(InParam fstore);
	
	public int getSameTable(InParam fstore);
	
	public void insertTmpTable(InParam fstore);
	
	public void deleteTabelName(InParam fstore);
	
	public int getSameTableAndUserid(InParam fstore);
	
	public List<TmpTable> getAllTableName(InParam fstore);
	
	public Integer getTaskId(@Param("dbname")String dbname,@Param("tbname")String tbname,@Param("userId")Integer userId,@Param("isAdmin")Boolean isAdmin);
}
