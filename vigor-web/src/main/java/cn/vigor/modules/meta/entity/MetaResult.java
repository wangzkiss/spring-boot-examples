package cn.vigor.modules.meta.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;
import cn.vigor.modules.meta.util.MetaUtil;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 数据库连接信息Entity
 * @author kiss
 * @version 2016-05-13
 */
public class MetaResult extends DataEntity<MetaResult>
{
    
    private static final long serialVersionUID = 1L;
    
    private MetaRepo repoId; // 关联链接表信息id 父类
    
    private String resultName; // 名称
    
    private int resultType; // 类型
    
    private String delimiter; // 分隔符
    
    private String resultDesc; // 描述
    
    private String resultFile; // 文件名表名
    
    private String storeWay;
    private String hdfsInfo;
    
    public String getHdfsInfo()
    {
        return hdfsInfo;
    }
    public void setHdfsInfo(String hdfsInfo)
    {
        this.hdfsInfo = hdfsInfo;
    }
    public String getStoreWay()
    {
        return storeWay;
    }
    public void setStoreWay(String storeWay)
    {
        this.storeWay = storeWay;
    }

    private List<MetaResultPro> metaResultProList = Lists.newArrayList(); // 子表列表
    
    private List<HashMap<String, Object>> etlMaps = Lists.newArrayList();//记录数据etl任务流向
    
    private List<HashMap<String, Object>> calMaps = Lists.newArrayList();//记录数据计算任务流向
    private String typeString;
    public String getTypeString()
    {
        typeString=MetaUtil.getTypeString(resultType);
        return typeString;
    }
    public List<HashMap<String, Object>> getEtlMaps()
    {
        return etlMaps;
    }

    public void setEtlMaps(List<HashMap<String, Object>> etlMaps)
    {
        this.etlMaps = etlMaps;
    }

    public List<HashMap<String, Object>> getCalMaps()
    {
        return calMaps;
    }

    public void setCalMaps(List<HashMap<String, Object>> calMaps)
    {
        this.calMaps = calMaps;
    }

    public List<MetaResultPro> getMetaResultProList()
    {
        return metaResultProList;
    }
    
    public void setMetaResultProList(List<MetaResultPro> metaResultProList)
    {
        this.metaResultProList = metaResultProList;
    }
    
    /**
     * 插入之前执行方法，需要手动调用
     */
    @Override
    public void preInsert()
    {
        
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId()))
        {
            this.updateBy = user;
            this.createBy = user;
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
        
    }
    
    public MetaResult()
    {
        super();
    }
    
    public MetaResult(MetaRepo repoId, String resultName, int resultType, String delimiter, String resultDesc,
			String resultFile, String storeWay, String hdfsInfo, List<MetaResultPro> metaResultProList) {
		super();
		this.repoId = repoId;
		this.resultName = resultName;
		this.resultType = resultType;
		this.delimiter = delimiter;
		this.resultDesc = resultDesc;
		this.resultFile = resultFile;
		this.storeWay = storeWay;
		this.hdfsInfo = hdfsInfo;
		this.metaResultProList = metaResultProList;
	}
	public MetaResult(String id)
    {
        this.setId(id);
    }
    
    public MetaResult(MetaRepo repoId)
    {
        this.repoId = repoId;
    }
    
    public MetaRepo getRepoId()
    {
        return repoId;
    }
    
    public void setRepoId(MetaRepo repoId)
    {
        this.repoId = repoId;
    }
    
    @Length(min = 0, max = 64, message = "名称长度必须介于 0 和 64 之间")
    @ExcelField(title = "名称", align = 2, sort = 2)
    public String getResultName()
    {
        return resultName;
    }
    
    public void setResultName(String resultName)
    {
        this.resultName = resultName;
    }
    
    @ExcelField(title = "类型", dictType = "result_type", align = 2, sort = 3)
    public int getResultType()
    {
        return resultType;
    }
    
    public void setResultType(int resultType)
    {
        this.resultType = resultType;
    }
    
    @Length(min = 0, max = 64, message = "分隔符长度必须介于 0 和 64 之间")
    @ExcelField(title = "分隔符", dictType = "delimiter", align = 2, sort = 4)
    public String getDelimiter()
    {
        return delimiter;
    }
    
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }
    
    @Length(min = 0, max = 64, message = "描述长度必须介于 0 和 64 之间")
    @ExcelField(title = "描述", align = 2, sort = 5)
    public String getResultDesc()
    {
        return resultDesc;
    }
    
    public void setResultDesc(String resultDesc)
    {
        this.resultDesc = resultDesc;
    }
    
    @Length(min = 0, max = 64, message = "文件名表名长度必须介于 0 和 64 之间")
    @ExcelField(title = "文件名表名", align = 2, sort = 6)
    public String getResultFile()
    {
        return resultFile;
    }
    
    public void setResultFile(String resultFile)
    {
        this.resultFile = resultFile;
    }
    
}