package mis.li.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.li.util.CommonMethod;
import mis.li.dao.BaseDao;
import mis.li.entity.CheckBoxMenu;
import mis.li.entity.SysFunction;
import mis.li.entity.SysModel;
import mis.li.entity.SysResult;
import mis.li.entity.SysRole;
import mis.li.entity.SysUser;

@Service("sysFunctionService")
public class SysFunctionService extends BaseService{

	@SuppressWarnings("rawtypes")
	@Autowired
	private BaseDao dao;

	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object queryByPage(HttpServletRequest request){
		
		SysResult sysResult = SysResult.newInstance();
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		Object[] objs = null;
		
		sysResult.setTotal(dao.createCriteria(SysRole.class).getTotalCount(objs));
		sysResult.setData(dao.query(start,limit));
		sysResult.setSuccess(true);
		return sysResult;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object queryNoPage(HttpServletRequest request){
		
		SysResult sysResult = SysResult.newInstance();
		Object[] objs = new Object[]{currentUser(request).getRoleId()};
		dao.createCriteria(SysFunction.class).equal("roleId", currentUser(request).getRoleId());
		sysResult.setTotal(dao.getTotalCount(objs));
		sysResult.setData(dao.query());
		sysResult.setSuccess(true);
		return sysResult;
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public Map<String, String> IdName(String roleId) {
		
		dao.createCriteria(SysFunction.class);
		
		if(null != roleId && !"".equals(roleId)){
			dao.equal("roleId", roleId);
		}
		
		List<Object> listObjs = dao.query();
		
		HashMap<String, String> result = new HashMap<String, String>();

		if (null != listObjs) {
			for (int i = 0, count = listObjs.size(); i < count; i++) {
				SysFunction sysFunction = (SysFunction) listObjs.get(i);
				String action = sysFunction.getAction();
				String id = sysFunction.getId();
				if (null != id && !"".equals(id)) {
					result.put(id, action);
				}
			}
		}

		
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object cud(SysRole obj,HttpServletRequest request){
		SysResult sysResult = SysResult.newInstance();
		String action = request.getParameter("action");
		
		if("save".equals(action)){
			
		}else if("update".equals(action)){
			
		}else if("delete".equals(action)){
			
		}else if("set".equals(action)){
			
			String roleId = obj.getId();
			
			SysUser sysUser = currentUser(request);
			if(sysUser.getRoleId().equals(roleId)){
				sysResult.setSuccess(false);
				sysResult.setMsg("不可以对当前自己所在角色设置权限!");
			}else{
				String Ids = request.getParameter("Ids");
				String[] idArray = Ids.split(",");
				
				List<Object> list_obj = dao.createCriteria(SysFunction.class).equal("roleId", roleId).query();
				
				for (Object object : list_obj) {
					dao.delete(object);
				}
				
				if(!"".equals(Ids)){
					for (Object object : idArray) {
						SysFunction sysOldFun = (SysFunction) dao.createCriteria(SysFunction.class).equal("id", object).queryUnique();
						SysFunction sysNewFun = new SysFunction();
						sysNewFun.setId(CommonMethod.createID());
						sysNewFun.setAction(sysOldFun.getAction());
						sysNewFun.setMark(sysOldFun.getMark());
						sysNewFun.setName(sysOldFun.getName());
						sysNewFun.setRoleId(roleId);
						sysNewFun.setSupId(sysOldFun.getSupId());
						dao.saveOrUpdate(sysNewFun);
					}
				}
				
				sysResult.setSuccess(true);
				
			}
			
			
		}
		
		return sysResult;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	// <----------------事务
	public List<Object> checkBoxTree(SysRole sysRole,HttpServletRequest request) {

		List<Object> result = new ArrayList<Object>();
		CheckBoxMenu sysMenu = new CheckBoxMenu();
		sysMenu.setId("1");
		sysMenu.setText("系统菜单");
		sysMenu.setLeaf(false);
		sysMenu.setExpanded(true);
		sysMenu.setChildren(new ArrayList());
//		result.add(sysMenu);
		
		SysUser sysUser = currentUser(request);
		
		List<SysFunction> list_fun = dao.createCriteria(SysFunction.class).equal("roleId", sysUser.getRoleId()).query();
		List<SysModel> list_model = dao.createCriteria(SysModel.class).query();
		List<SysFunction> list_funOld = dao.createCriteria(SysFunction.class).equal("roleId", sysRole.getId()).query();
		
		CheckBoxMenu menuF;
		CheckBoxMenu menuM;
		boolean flag;
		
		for (SysModel sysMo : list_model) {
			flag = false;
			menuM = new CheckBoxMenu();
			menuM.setId(sysMo.getId());
			menuM.setText(sysMo.getName());
			menuM.setLeaf(flag);
			menuM.setExpanded(true);
			
			for (SysFunction sysfun : list_fun) {
				if(sysfun.getSupId().equals(sysMo.getId())){
					flag = true;
					menuF = new CheckBoxMenu();
					menuF.setId(sysfun.getId());
					menuF.setText(sysfun.getName());
					menuF.setLeaf(true);
					menuF.setHrefTarget(sysfun.getAction());
					//判断该角色是否拥有功能，如果有，选中checkBox;反之不选中.
					SysFunction sysFun2;
					for(int k = 0,count=list_funOld.size(); k < count; k++){
						sysFun2 = (SysFunction)list_funOld.get(k);
						if(sysFun2.getName().equals(sysfun.getName())){
							menuF.setChecked(true);
							break;
						}
					}
					menuM.getChildren().add(menuF);
				}
			}
			
			if(flag){
				sysMenu.getChildren().add(menuM);
			}
			
		}
		
		result.add(sysMenu);
		
		return result;
	}
	
	/**
	 * 判断用户是否有权限
	 * @param url
	 * @return
	 */
	@Transactional
	public boolean hasAuthor(String url,HttpServletRequest request){
		
		String[] subURLs = url.split("/");
		
		//当前URL只到项目名称，没有到功能
		if(subURLs.length <= 4){
			return true;
		}
		
		String action = subURLs[4];
		
		SysUser sysUser = currentUser(request);
		
		if(null == sysUser){
			return false;
		}
		
		Map<String,String> allFunctions = IdName(null);
		Map<String, String> myFunctions = IdName(sysUser.getRoleId());
		
		if(allFunctions.containsValue(action) && !myFunctions.containsValue(action)){
			return false;
		}
		
		return true;
	}
	
}
