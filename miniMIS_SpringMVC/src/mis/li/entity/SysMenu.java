package mis.li.entity;

import java.util.ArrayList;
import java.util.List;

public class SysMenu {

	private String id;    
	private String text;    
	private boolean leaf;
	private boolean expanded;
	private String iconCls;
	private List<SysMenu> children;
	private String viewType;
	private boolean selectable;
	
	
	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public SysMenu(){
		children = new ArrayList<SysMenu>();
	}
	
	@SuppressWarnings("rawtypes")
	public List getChildren() {
		return children;
	}
	
	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}
	
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	
}
