/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.entity;

import java.util.List;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;

/**
 * 数据服务管理Entity
 * @author liminfang
 * @version 2016-06-28
 */
public class EServiceInfoMeta extends DataEntity<EServiceInfoMeta> {
	
	private static final long serialVersionUID = 1L;
	private int metaId;
    private String metaName;
    private String metaDesc;
    private String metaFile;
    private int metaType;
    private List<EServicePro> proList = Lists.newArrayList();
    private List<EServicePro> storeProList = Lists.newArrayList();
    private List<EServicePro> resultProList = Lists.newArrayList();
    
    public int getMetaId()
    {
        return metaId;
    }
    public void setMetaId(int metaId)
    {
        this.metaId = metaId;
    }
    public String getMetaName()
    {
        return metaName;
    }
    public void setMetaName(String metaName)
    {
        this.metaName = metaName;
    }
    public String getMetaDesc()
    {
        return metaDesc;
    }
    public void setMetaDesc(String metaDesc)
    {
        this.metaDesc = metaDesc;
    }
    public String getMetaFile()
    {
        return metaFile;
    }
    public void setMetaFile(String metaFile)
    {
        this.metaFile = metaFile;
    }
    public int getMetaType()
    {
        return metaType;
    }
    public void setMetaType(int metaType)
    {
        this.metaType = metaType;
    }
    public List<EServicePro> getProList()
    {
        return proList;
    }
    public void setProList(List<EServicePro> proList)
    {
        this.proList = proList;
    }
    public List<EServicePro> getStoreProList()
    {
        return storeProList;
    }
    public void setStoreProList(List<EServicePro> storeProList)
    {
        this.storeProList = storeProList;
    }
    public List<EServicePro> getResultProList()
    {
        return resultProList;
    }
    public void setResultProList(List<EServicePro> resultProList)
    {
        this.resultProList = resultProList;
    }
	
	
}