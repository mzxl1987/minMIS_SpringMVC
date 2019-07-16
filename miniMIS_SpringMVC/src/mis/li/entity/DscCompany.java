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
 * 企业，部门
 */
@Entity(name="Dsc_Company")
public class DscCompany implements Serializable{
 
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	private String id;//系统自动生成主键 length = 32

	private String name;  //公司名  length = 50

	private String lastId; //外键，上一级的ID, 这样构成了树的层级关系 length = 32
	
	private int clevel;   //公司等级 length = 32
	
	private String city;// 城市 length = 50
    
    private String recName;//创建的用户名 length = 20
	
	@Column(columnDefinition="datetime")
	private Date   recTime; //创建的时间

	/****** 保留字段 ******************************************************/
	
	@Transient
	private String serviceAgents;                  //服务代理商
	
	/*************************************************************/
	
	public int getClevel() {
		return clevel;
	}

	public void setClevel(int clevel) {
		this.clevel = clevel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		this.lastId = lastId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getServiceAgents() {
		return serviceAgents;
	}

	public void setServiceAgents(String serviceAgents) {
		this.serviceAgents = serviceAgents;
	}

	@Override
	public String toString() {
		return "DscCompany [id=" + id + ", name=" + name + ", lastId=" + lastId
				+ ", clevel=" + clevel + ", city=" + city + ", recName="
				+ recName + ", recTime=" + recTime + ", serviceAgents="
				+ serviceAgents + "]";
	}
	
	

}
