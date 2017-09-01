package cn.vigor.modules.meta.entity;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotNull;

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
public class MetaSource extends DataEntity<MetaSource> {
	
	private static final long serialVersionUID = 1L;
	private MetaRepo repoId;		// 关联连接表信息repo_id 父类
	private String sourceName="";		// 名称
	private int sourceType;		// 类型
	private String delimiter="";		// 分隔符
	private String sourceDesc="";		// 描述
	private String sourceFile="";		// 文件名或表名
	  
    private String external=""; // 存储数据扩展字段
    
	private List<MetaSourcePro> metaSourceProList = Lists.newArrayList();// 子表列表
	private List<HashMap<String,Object>> etlMaps=Lists.newArrayList();//记录数据etl任务流向
	private List<HashMap<String,Object>> calMaps=Lists.newArrayList();//记录数据计算任务流向
    private String typeString;
    
    public String getExternal()
    {
        return external;
    }
    public void setExternal(String external)
    {
        this.external = external;
    }
    public String getTypeString()
    {
        typeString=MetaUtil.getTypeString(sourceType);
        return typeString;
    }
	public List<MetaSourcePro> getMetaSourceProList() {
		return metaSourceProList;
	}
	public void setMetaSourceProList(List<MetaSourcePro> metaSourceProList) {
		this.metaSourceProList = metaSourceProList;
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

    /**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert() {
		
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
		
	}

	
	public MetaSource() {
		super();
	}

	public MetaSource(MetaRepo repoId, String sourceName, int sourceType, String delimiter, String sourceDesc,
			String sourceFile, String external, List<MetaSourcePro> metaSourceProList) {
		super();
		this.repoId = repoId;
		this.sourceName = sourceName;
		this.sourceType = sourceType;
		this.delimiter = delimiter;
		this.sourceDesc = sourceDesc;
		this.sourceFile = sourceFile;
		this.external = external;
		this.metaSourceProList = metaSourceProList;
	}
	public MetaSource(String id){
		this.setId(id);
	}

	public MetaSource(MetaRepo repoId){
		this.repoId = repoId;
	}

	
	public MetaRepo getRepoId() {
		return repoId;
	}

	public void setRepoId(MetaRepo repoId) {
		this.repoId = repoId;
	}
	


	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	

    public void setSourceType(int sourceType)
    {
        this.sourceType = sourceType;
    }

    public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }
    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }
    
    
    @ExcelField(title="连接名称", align=2, sort=1)
    public String getConnName() {
        if(null!=repoId){
            return repoId.getConnName();
        }else{
            return "";
        }
    }
    @ExcelField(title="ip", align=2, sort=2)
    public String getIp() {
        if(null!=repoId){
            return repoId.getIp();
        }else{
            return "";
        }
    }
    @ExcelField(title="用户名", align=2, sort=3)
    public String getUserName() {
        if(null!=repoId){
            return repoId.getUserName();
        }else{
            return "";
        }
    }
    @ExcelField(title="密码", align=2, sort=4)
    public String getUserPwd() {
        if(null!=repoId){
            return repoId.getUserPwd();
        }else{
            return "";
        }
    }
    @ExcelField(title="端口", align=2, sort=5)
    public String getPort() {
        if(null!=repoId){
            return repoId.getPort();
        }else{
            return "0";
        }
    }
    
    @ExcelField(title="目录/数据库", align=2, sort=6)
    public String getRepoName() {
        if(null!=repoId){
            return repoId.getRepoName();
        }else{
            return "";
        }
    }
  
    @Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
    @ExcelField(title="名称", align=2, sort=7)
    public String getSourceName() {
        return sourceName;
    }
    
    @NotNull(message="类型不能为空")
    @ExcelField(title="类型", dictType="source_type", align=2, sort=8)
    public int getSourceType()
    {
        return sourceType;
    }
    @Length(min=0, max=64, message="分隔符长度必须介于 0 和 64 之间")
    @ExcelField(title="分隔符", dictType="delimiter", align=2, sort=9)
    public String getDelimiter() {
        return delimiter;
    }
	@Length(min=0, max=644, message="描述长度必须介于 0 和 644 之间")
	@ExcelField(title="描述", align=2, sort=10)
	public String getSourceDesc() {
		return sourceDesc;
	}
	@Length(min=0, max=64, message="文件名或表名长度必须介于 0 和 64 之间")
	@ExcelField(title="文件名或表名", align=2, sort=11)
	public String getSourceFile() {
	    return sourceFile;
	}
}