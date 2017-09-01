package cn.vigor.modules.server.entity;

import java.util.Date;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.DateUtils;

public class ClusterParam extends DataEntity<ClusterParam>
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String startDate = "2016-01-01 00:00:00";
    
    private String endDate = DateUtils.formatDateTime(new Date());
    
    private String key;
    
    // 按秒 date_format('2008-08-08 22:23:01', '%Y-%m-%d %H:%i:%s 秒') 
    
    // 按分 date_format('2008-08-08 22:23:01', '%Y-%m-%d %H:%i 分') 
    
    // 按时 date_format('2008-08-08 22:23:01', '%Y-%m-%d %H 是') 
    
    // 按天 date_format('2008-08-08 22:23:01', '%Y-%m-%d 日') 
    
    // 按天 date_format('2008-08-08 22:23:01', '%Y-%m 月')
    
    // 按天 date_format('2008-08-08 22:23:01', '%Y 年') 
    
    private String keyDate;
    
    public String getKey()
    {
        
        return key;
    }
    
    public void setKey(String key)
    {
        
        this.key = key;
    }
    
    public String getStartDate()
    {
        return startDate;
    }
    
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    
    public String getKeyDate()
    {
        return keyDate;
    }
    
    public void setKeyDate(String keyDate)
    {
        this.keyDate = keyDate;
    }
    
}
