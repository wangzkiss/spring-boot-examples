package cn.vigor.modules.jars.entity;

public class Expension
{
    private String dbname;
    private String dbType;
    private String port;
    private String shellPath;
    private String userName;
    private String password;
    private String ip;
    private String ruleType;
    private String ruleExpression;
    private String etlModeName;
    
    public String getEtlModeName()
    {
        return etlModeName;
    }
    public void setEtlModeName(String etlModeName)
    {
        this.etlModeName = etlModeName;
    }
    public String getDbname()
    {
        return dbname;
    }
    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }
    public String getDbType()
    {
        return dbType;
    }
    public void setDbType(String dbType)
    {
        this.dbType = dbType;
    }
    public String getPort()
    {
        return port;
    }
    public void setPort(String port)
    {
        this.port = port;
    }
    public String getShellPath()
    {
        return shellPath;
    }
    public void setShellPath(String shellPath)
    {
        this.shellPath = shellPath;
    }
    public String getUserName()
    {
        return userName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getIp()
    {
        return ip;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    public String getRuleType()
    {
        return ruleType;
    }
    public void setRuleType(String ruleType)
    {
        this.ruleType = ruleType;
    }
    public String getRuleExpression()
    {
        return ruleExpression;
    }
    public void setRuleExpression(String ruleExpression)
    {
        this.ruleExpression = ruleExpression;
    }
    
}
