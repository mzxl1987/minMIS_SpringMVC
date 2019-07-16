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
 * @author 朱露露
 * 角色
 */
@Entity(name="Sys_Role")
public class SysRole implements Serializable{
 
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	private String id;//系统自动生成主键 length = 32
	
	private String name;  //用户名  length = 50
	
	private String dscCompanyId;   //外键 length = 32,公司部门名称

    private String recName;//创建的用户名 length = 20
	
	@Column(columnDefinition="datetime")
	private Date   recTime; //创建的时间

	/*****************************************************/
	
	@Transient
	private String dscCompanyName;
	
	/*******************************************************/
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDscCompanyId() {
		return dscCompanyId;
	}

	public void setDscCompanyId(String dscCompanyId) {
		this.dscCompanyId = dscCompanyId;
	}

	public String getDscCompanyName() {
		return dscCompanyName;
	}

	public void setDscCompanyName(String dscCompanyName) {
		this.dscCompanyName = dscCompanyName;
	}

	@Override
	public String toString() {
		return "SysRole [id=" + id + ", name=" + name + ", dscCompanyId="
				+ dscCompanyId + ", recName=" + recName + ", recTime="
				+ recTime + ", dscCompanyName=" + dscCompanyName + "]";
	}

	

}
