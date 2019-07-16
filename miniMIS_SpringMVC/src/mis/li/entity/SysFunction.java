package mis.li.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 
 * @author 朱露露
 * 功能表
 */
@Entity(name="Sys_Function")
public class SysFunction implements Serializable{
	
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id",length=32)
	private String id;  //系统自身的编号

	@Column(name="name",length=32)
	private String name; //功能名称
	
	@Column(name="supId",length=32)
	private String supId;   //对应的模块编号
	
	@Column(name="action",length=32)
	private String action;  //对应的操作
	
	@Column(name="roleId",length=32)
	private String roleId;  //对应的角色
    
	@Column(name="mark")
    private int mark;//排序
    
	@Column(name="iconCls",length=100)
	private String iconCls;  //图标
	
	@Column(name="level")
	private String level;  //层级,一共3层,1:分类，2:功能,3:按钮
	
    /********************************预留字段**************************************/
    
    /********************************预留字段**************************************/
    

	public String getRoleId() {
		return roleId;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	
}
