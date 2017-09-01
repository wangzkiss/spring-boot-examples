package cn.vigor.modules.meta.dao;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.sys.entity.User;

/**
 * 数据库连接信息DAO接口
 * @author kiss
 * @version 2016-05-13
 */
@MyBatisDao
public interface MetaStoreDao extends CrudDao<MetaStore> {
    List<HashMap<String, Object>> findEtlFlow(String id);
    List<HashMap<String, Object>> findCalFlow(String id);
    
    /**
     * 根据实体名称获取唯一记录
     * @param storeName
     * @return
     */
    public MetaStore findMetaStoreByName(String storeName);
    
    /**
     * 找到所有的平台存储信息
     * @param map
     * @return
     */
    public List<HashMap<String, Object>> findAllMetaStore(Map<String, Object> map);
    
    public List<Map<String,Object>> getMetaStoreByType(@Param("repoId")String repoId,@Param("types")List<Integer> types,@Param("user")User user);
    
    public List<MetaStore> getByDbInfoAndStoreType(@Param("ip")String ip,@Param("storeFile")String storeFile,
    		@Param("storeType")Integer storeType,@Param("repoName")String repoName);
	public List<HashMap<String, Object>> findNodes();
	public List<HashMap<String, Object>> findLinks();
    
    /*public MetaStore getStoreExternalByStoreId(@Param("externalId")String externalId);*/
}