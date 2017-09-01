package cn.vigor.modules.ketter.entity;

import cn.vigor.common.persistence.BaseEntity;

public class Rdatabase extends BaseEntity<Rdatabase>{

    private static final long serialVersionUID = 24383692224965959L;
    
    /**
     * 数据库ID
     */
    private Integer databaseId;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 数据库类型Id
     */
    private int databaseTypeId;
    
    /**
     * 
     */
    private int databaseContypeId;
    
    /**
     * 主机名
     */
    private String hostName;
    
    /**
     * 数据库名
     */
    private String databaseName;
    
    /**
     * 端口
     */
    private int port;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 
     */
    private String dataTbs;
    
    /**
     * 
     */
    private String indexTbs;
    
    /**
     * 
     */
    private Integer repoId;
    
    /**
     * 登录用户iD
     */
    private Integer loginUserId;
    
    /**
     * 存储id
     */
    private Integer storeId;
    
    /**
     * 来源id
     */
    private Integer sourceId;
    
    @Override
    public void preInsert()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void preUpdate()
    {
        // TODO Auto-generated method stub
        
    }

    public Integer getDatabaseId()
    {
        return databaseId;
    }

    public String getName()
    {
        return name;
    }

    public int getDatabaseTypeId()
    {
        return databaseTypeId;
    }

    public int getDatabaseContypeId()
    {
        return databaseContypeId;
    }

    public String getHostName()
    {
        return hostName;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

    public int getPort()
    {
        return port;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }

    public String getServerName()
    {
        return serverName;
    }

    public String getDataTbs()
    {
        return dataTbs;
    }

    public String getIndexTbs()
    {
        return indexTbs;
    }

    public Integer getRepoId()
    {
        return repoId;
    }

    public Integer getLoginUserId()
    {
        return loginUserId;
    }

    public Integer getStoreId()
    {
        return storeId;
    }

    public Integer getSourceId()
    {
        return sourceId;
    }

    public void setDatabaseId(Integer databaseId)
    {
        this.databaseId = databaseId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDatabaseTypeId(int databaseTypeId)
    {
        this.databaseTypeId = databaseTypeId;
    }

    public void setDatabaseContypeId(int databaseContypeId)
    {
        this.databaseContypeId = databaseContypeId;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public void setDataTbs(String dataTbs)
    {
        this.dataTbs = dataTbs;
    }

    public void setIndexTbs(String indexTbs)
    {
        this.indexTbs = indexTbs;
    }

    public void setRepoId(Integer repoId)
    {
        this.repoId = repoId;
    }

    public void setLoginUserId(Integer loginUserId)
    {
        this.loginUserId = loginUserId;
    }

    public void setStoreId(Integer storeId)
    {
        this.storeId = storeId;
    }

    public void setSourceId(Integer sourceId)
    {
        this.sourceId = sourceId;
    }
    
}
