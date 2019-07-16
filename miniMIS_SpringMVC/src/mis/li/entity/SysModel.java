package mis.li.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 
 * @author 朱露露
 * 模块表
 */
@Entity(name="Sys_Model")
public class SysModel implements Serializable{
	
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id",length=32)
	private String id;  //系统自身的编号

	@Column(name="name",length=32)
	private String name; //模块名称

	@Column(name="iconCls",length=100)
	private String iconCls;  //图标

	@Column(name="action")
	private String action;  //操作
	
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
