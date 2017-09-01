package cn.vigor.modules.compute.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 数据输出
 * 
 * @author yangtao
 * 
 */
public class Schedule extends ModelObject {

	private int id;

	private String name="";

	private String desc="";

	private String rule="";

	/**
	 * 1=重试　2=邮件通知　3=忽略
	 */
	private int errorStrategy = 1;

	/**
	 * 1=执行一次　2=重复执行
	 */
	private int execTimes = 1;

	private int tryTimes = 0;

	private String email = "";

	/**
	 * 任务开始时间
	 */
	private Date startTime;

	private Integer execFreq=-1;

	private Integer execFreqValue=0;
	
	private String execField="";
	
	/**
	 * 标记是执行一次还是重复执行
	 */
	private Integer scheduleType=1;
	
	public Integer getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(Integer scheduleType) {
		this.scheduleType = scheduleType;
	}

	public Integer getExecFreq() {
		return execFreq;
	}

	public void setExecFreq(Integer execFreq) {
		this.execFreq = execFreq;
	}

	public Integer getExecFreqValue() {
		return execFreqValue;
	}

	public void setExecFreqValue(Integer execFreqValue) {
		this.execFreqValue = execFreqValue;
	}

	public String getExecField() {
		return execField;
	}

	public void setExecField(String execField) {
		this.execField = execField;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		firePropertyChange("desc", this.desc, this.desc = desc);
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		firePropertyChange("rule", this.rule, this.rule = rule);
	}

	public int getTryTimes() {
		return tryTimes;
	}

	public void setTryTimes(int tryTimes) {
		firePropertyChange("tryTimes", this.tryTimes, this.tryTimes = tryTimes);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		firePropertyChange("email", this.email, this.email = email);
	}

	public int getErrorStrategy() {
		return errorStrategy;
	}

	public void setErrorStrategy(int errorStrategy) {
		firePropertyChange("errorStrategy", this.errorStrategy,
				this.errorStrategy = errorStrategy);
	}

	public int getExecTimes() {
		return execTimes;
	}

	public void setExecTimes(int execTimes) {
		firePropertyChange("execTimes", this.execTimes,
				this.execTimes = execTimes);
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		firePropertyChange("startTime", this.startTime,
				this.startTime = startTime);
	}

	public static final String format2 = "yyyy-MM-dd HH:mm:ss";

	public Date convert(Date date) {
		if (date != null) {
			Date retValue = null;
			SimpleDateFormat sdf = new SimpleDateFormat(format2);
			try {
				String str = sdf.format(date);
				retValue = sdf.parse(str);
			} catch (ParseException e) {
			}
			return retValue;
		}
		return null;
	}

	public Date getDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		Date currentTime_2 = null;
		try {
			currentTime_2 = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentTime_2;
	}
}
