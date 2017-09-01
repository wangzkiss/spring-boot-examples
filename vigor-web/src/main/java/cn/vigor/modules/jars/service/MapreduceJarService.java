package cn.vigor.modules.jars.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.modules.jars.dao.MapreduceJarDao;
import cn.vigor.modules.jars.entity.MapreduceJar;

/**
 * 执行包管理Service
 * @author kiss
 * @version 2016-06-13
 */
@Service
@Transactional(readOnly = true)
public class MapreduceJarService extends CrudService<MapreduceJarDao, MapreduceJar> {

	public MapreduceJar get(String id) {
		return super.get(id);
	}
	
	public List<MapreduceJar> findList(MapreduceJar mapreduceJar) {
		return super.findList(mapreduceJar);
	}
	
	public Page<MapreduceJar> findPage(Page<MapreduceJar> page, MapreduceJar mapreduceJar) {
	    if(page.getOrderBy()==null|| "".equals(page.getOrderBy()))
        {
            page.setOrderBy("id desc");
        }
	    mapreduceJar.setPage(page);
	    mapreduceJar.preInsert();
	    List <MapreduceJar> jarlist=new ArrayList <MapreduceJar> ();
	    for (MapreduceJar jar : dao.findList(mapreduceJar))
        {
	        int num=dao.isHaveFunction(jar.getId());
	        if(num>0){
	            jar.setDelFlag("1");  
	        }else{
	            jar.setDelFlag("2");
	        }
	        jarlist.add(jar);
        }
        page.setList(jarlist);
        return page;
	    
	}
	
	@Transactional(readOnly = false)
	public void save(MapreduceJar mapreduceJar) {
		super.save(mapreduceJar);
	}
	
	@Transactional(readOnly = false)
	public void delete(MapreduceJar mapreduceJar) {
		super.delete(mapreduceJar);
	}
	
	
	
	
}