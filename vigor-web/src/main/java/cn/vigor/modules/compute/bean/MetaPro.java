package cn.vigor.modules.compute.bean;

public class MetaPro
{
  private String name;
  private String proName;
  private int proIndex;
  private String proType;
  private String proDesc;
  private String dataFormat;

  public String getDataFormat()
{
    return dataFormat;
}
public void setDataFormat(String dataFormat)
{
    this.dataFormat = dataFormat;
}
public String getName()
  {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getProName() {
    return this.proName;
  }
  public void setProName(String proName) {
    this.proName = proName;
  }
  public int getProIndex() {
    return this.proIndex;
  }
  public void setProIndex(int proIndex) {
    this.proIndex = proIndex;
  }
  public String getProType() {
    return this.proType;
  }
  public void setProType(String proType) {
    this.proType = proType;
  }
  public String getProDesc() {
    return this.proDesc;
  }
  public void setProDesc(String proDesc) {
    this.proDesc = proDesc;
  }
}