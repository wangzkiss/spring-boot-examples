package cn.vigor.modules.meta.entity;

import cn.vigor.common.persistence.DataEntity;

/**
 * 基础日期参数
 * @author huzeyuan
 * @version v3.0
 * @date 2016-10-28
 *
 */
public class BaseDateParams extends DataEntity<BaseDateParams>
{

    private static final long serialVersionUID = 7354770313112027877L;
    
    /**
     * 日期参数名称
     */
    private String name;
    
    /**
     * 格式
     */
    private String format;
    
    /**
     * 参数说明
     */
    private String desc;
    
    private int forPartition;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public int getForPartition()
    {
        return forPartition;
    }

    public void setForPartition(int forPartition)
    {
        this.forPartition = forPartition;
    }

    @Override
    public String toString()
    {
        return "BaseDateParams [id=" + id + ", name=" + name + ", format="
                + format + ", desc=" + desc + ", for_partition=" + forPartition
                + "]";
    }
    
}
