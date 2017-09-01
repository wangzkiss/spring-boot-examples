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
public class MetaStore extends DataEntity<MetaStore>
{
    
    private static final long serialVersionUID = 1L;
    
    private MetaRepo repoId; // 关联链接表信息id 父类
    
    private String storeName=""; // 名称
   
    /**
     * hdfs://192.168.12.188:9000/data/hewei/asdf
     */
    private String hdfsInfo;
    
    private int storeType; // 类型
    
    private String delimiter=""; // 分隔符
    
    private String storeDesc=""; // 描述
    
    private String storeFile=""; // 文件或目录
    
    private String storeExternal=""; // 存储数据扩展字段
    
    /**
    1.textfile
           为默认格式
            存储方式：行存储
            磁盘开销大 数据解析开销大
            压缩的text文件 hive无法进行合并和拆分
    2.sequencefile
            二进制文件,以<key,value>的形式序列化到文件中
            存储方式：行存储
            可分割 压缩
            一般选择block压缩
            优势是文件和Hadoop api中的mapfile是相互兼容的。       
    3.rcfile
            存储方式：数据按行分块 每块按照列存储
            压缩快 快速列存取
            读记录尽量涉及到的block最少
            读取需要的列只需要读取每个row group 的头部定义。
            读取全量数据的操作 性能可能比sequencefile没有明显的优势            
    4.orc
            存储方式：数据按行分块 每块按照列存储
            压缩快 快速列存取
            效率比rcfile高,是rcfile的改良版本  
    */
    private String storeWay;
    
    private List<HashMap<String, Object>> etlMaps = Lists.newArrayList();//记录数据etl任务流向
    
    private List<HashMap<String, Object>> calMaps = Lists.newArrayList();//记录数据计算任务流向
    
    private String typeString;
    
    public void setStoreWay(String storeWay)
    {
        this.storeWay = storeWay;
    }
    
    public MetaStore(MetaRepo repoId, String storeName, String hdfsInfo, int storeType, String delimiter,
			String storeDesc, String storeFile, String storeExternal, String storeWay,
			List<MetaStorePro> metaStoreProList) {
		super();
		this.repoId = repoId;
		this.storeName = storeName;
		this.hdfsInfo = hdfsInfo;
		this.storeType = storeType;
		this.delimiter = delimiter;
		this.storeDesc = storeDesc;
		this.storeFile = storeFile;
		this.storeExternal = storeExternal;
		this.storeWay = storeWay;
		this.metaStoreProList = metaStoreProList;
	}

	public String getTypeString()
    {
        typeString = MetaUtil.getTypeString(storeType);
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
    
    public void setStoreType(int storeType)
    {
        this.storeType = storeType;
    }
    
    
    
    public void setHdfsInfo(String hdfsInfo)
    {
        this.hdfsInfo = hdfsInfo;
    }
    
    private List<MetaStorePro> metaStoreProList = Lists.newArrayList(); // 子表列表
    
    public List<MetaStorePro> getMetaStoreProList()
    {
        return metaStoreProList;
    }
    
    public void setMetaStoreProList(List<MetaStorePro> metaStoreProList)
    {
        this.metaStoreProList = metaStoreProList;
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
    
    public MetaStore()
    {
        super();
    }
    
    public MetaStore(String id)
    {
        this.setId(id);
    }
    
    public MetaStore(MetaRepo repoId)
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
    

    
    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }
    

    
    public void setStoreType(Integer storeType)
    {
        this.storeType = storeType;
    }
    

    
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }
    

    
    public void setStoreDesc(String storeDesc)
    {
        this.storeDesc = storeDesc;
    }
    
 
    
    public void setStoreFile(String storeFile)
    {
        this.storeFile = storeFile;
    }
    
   
    
    public void setStoreExternal(String storeExternal)
    {
        this.storeExternal = storeExternal;
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
    
    @Length(min = 0, max = 256, message = "名称长度必须介于 0 和 64 之间")
    @ExcelField(title = "名称", align = 2, sort = 7)
    public String getStoreName()
    {
        return storeName;
    }
   
    @Length(min = 0, max = 64, message = "文件或目录长度必须介于 0 和 64 之间")
    @ExcelField(title = "文件或表", align = 2, sort = 8)
    public String getStoreFile()
    {
        return storeFile;
    }
    @ExcelField(title = "hdfs全地址", align = 2, sort = 9)
    public String getHdfsInfo()
    {
        return hdfsInfo;
    }
    
    @Length(min = 0, message = "存储数据扩展字段长度必须大于 0")
    @ExcelField(title = "扩展字段", align = 2, sort = 10)
    public String getStoreExternal()
    {
        return storeExternal;
    }
  
    public String getStoreWay()
    {
        return storeWay;
    }
    
    @Length(min = 0, max = 64, message = "分隔符长度必须介于 0 和 64 之间")
    @ExcelField(title = "分隔符", dictType = "delimiter", align = 2, sort = 11)
    public String getDelimiter()
    {
        return delimiter;
    }
    
    @ExcelField(title = "类型", dictType = "store_type", align = 2, sort = 12)
    public Integer getStoreType()
    {
        return storeType;
    }
    
    @Length(min = 0, max = 64, message = "描述长度必须介于 0 和 64 之间")
    @ExcelField(title = "描述", align = 2, sort = 13)
    public String getStoreDesc()
    {
        return storeDesc;
    }
}