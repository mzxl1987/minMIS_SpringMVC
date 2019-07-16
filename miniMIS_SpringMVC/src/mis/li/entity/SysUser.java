package mis.li.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import common.li.serializer.CustomDateSerializer;

/**
 * 
 * @author BENNY
 * 功能表
 */
@Entity(name="Sys_User")
public class SysUser implements Serializable{
 
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	private String id;//系统自动生成主键 length = 32
	
	private String userName;  //用户名  length = 50

	private String password; //用户密码 length = 50

	private String roleId;   //外键 length = 32
	
	private String realName;   //真实姓名
	
	private String dscCompanyId;   //外键 length = 32
	
	private String homePage;// 主页 length = 50
    
	private String recName;//创建的用户名 length = 20
	
	@Column(columnDefinition="datetime")
	private Date   recTime; //创建的时间

	
	/*******************************************************/
	@Transient
	private String sysRoleName;
	
	@Transient
	private String dscCompanyName;
	
	@Transient
	private String newPassword;
	
	@Transient
	private String groupByCompany;   //分组名称
	
	@Transient
	private String platformName;   //平台名称 length = 32
	
	@Transient
	private String userSource;     //用户来源,默认,0:太平洋机务用户,1:以项目编号命名的用户信息
	
	@Transient
	private String version;       //app版本
	
	@Transient
	private String sessionId;      //sessionId
	
	/*******************************************************/
	
	
	
	public String getHomePage() {
		return homePage;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}

	public String getGroupByCompany() {
		return groupByCompany;
	}

	public void setGroupByCompany(String groupByCompany) {
		this.groupByCompany = groupByCompany;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRecName() {
		return recName;
	}

	public void setRecName(String recName) {
		this.recName = recName;
	}

	@JsonSerialize(using=CustomDateSerializer.class)
	public Date getRecTime() {
		return recTime;
	}

	public void setRecTime(Date recTime) {
		this.recTime = recTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDscCompanyId() {
		return dscCompanyId;
	}

	public void setDscCompanyId(String dscCompanyId) {
		this.dscCompanyId = dscCompanyId;
	}

	public String getSysRoleName() {
		return sysRoleName;
	}

	public void setSysRoleName(String sysRoleName) {
		this.sysRoleName = sysRoleName;
	}

	public String getDscCompanyName() {
		return dscCompanyName;
	}

	public void setDscCompanyName(String dscCompanyName) {
		this.dscCompanyName = dscCompanyName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "SysUser [id=" + id + ", userName=" + userName + ", password="
				+ password + ", roleId=" + roleId + ", realName=" + realName
				+ ", dscCompanyId=" + dscCompanyId + ", homePage=" + homePage
				+ ", recName=" + recName + ", recTime=" + recTime
				+ ", sysRoleName=" + sysRoleName + ", dscCompanyName="
				+ dscCompanyName + ", newPassword=" + newPassword + "]";
	}
	
	

}
