package cn.vigor.modules.ketter.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.ketter.entity.Rdatabase;

@MyBatisDao
public interface RdatabaseDao extends CrudDao<Rdatabase>{
    
    /**
     * 
     * @param metaType
     * @param value
     */
    public void updateByRepoIdAndMetaType(@Param("rdatabase")Rdatabase rdatabase,@Param("metaType")int metaType);
    
    
    public void delByRepoIdAndMetaType(@Param("rdatabase")Rdatabase rdatabase,@Param("metaType")int metaType);
    
    public void insertRdatabaseAttr(@Param("list")List<Map<String,Object>> list);
    
    public int getNextId();
    
    public Integer getNextAttrId();
    
    public List<Rdatabase> findList(Rdatabase rdatabase);
    
    public void deleteAttrByRDId(int rdatabaseId);
}