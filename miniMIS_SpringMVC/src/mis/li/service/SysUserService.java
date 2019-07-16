package mis.li.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.li.util.CommonMethod;
import common.li.util.LoggerUtil;
import mis.li.dao.BaseDao;
import mis.li.entity.SysResult;
import mis.li.entity.SysUser;
import mis.li.interceptor.SessionInterceptor;

@Service("sysUserService")
public class SysUserService extends BaseService{

	private final Logger log = LoggerUtil.getLog(this.getClass());
	
	@SuppressWarnings("rawtypes")
	@Resource					
	private BaseDao baseDao;

	@Resource
	private SysRoleService sysRoleService;
	
	@Resource
	private SysCompanyService dscCompanyService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	// <----------------事务
	public Object login(SysUser obj,HttpServletRequest request) {
		
		log.info("login");
		
		SysResult result = SysResult.newInstance();
		String userName = obj.getUserName();
		String password = obj.getPassword();
		
		if (noEmpty(new String[] { userName, password })) {
			
			Object sysUser = baseDao
					.createCriteria(SysUser.class)
					.equal("userName", userName)
					.equal("password", password).queryUnique();
			
			if(null == sysUser){
				result.setMsg("用户名密码不正确!");
				result.setSuccess(false);
			}else{
				
				request.getSession().setAttribute(SessionInterceptor.LOGINFLAG,	sysUser);
				SysUser user = (SysUser)sysUser;
				user.setSessionId(request.getSession().getId());
				
				List list = new ArrayList<Object>();
				list.add(user);
				
				result.setData(list);
				result.setSuccess(true);
			}
			
		}else{
			result.setMsg("用户名或密码为空!");
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean IsExist(SysUser sysUser) {

		String id = sysUser.getId();
		
		baseDao.createCriteria(SysUser.class)
				.equal("userName", sysUser.getUserName());

		if(null != id && !"".equals(id)){
			baseDao.notEqual("id", id);
		}
		
		List<SysUser> list_user = baseDao.query();
		
		return null != list_user && list_user.size() > 0;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object queryByPage(SysUser sysUser,HttpServletRequest request){
		
		SysResult sysResult = SysResult.newInstance();
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		List<Object> listObjs = new ArrayList<Object>();
		
		baseDao.createCriteria(SysUser.class);
		
		if(noEmpty(sysUser)){
			
			String userName = sysUser.getUserName();
			String dscCompanyId = sysUser.getDscCompanyId();
			String roleId = sysUser.getRoleId();
			
			if(noEmpty(userName)){
				baseDao.like("userName", "%"+userName+"%");
				listObjs.add(userName);
			}
			
			// 添加DscCompanyIds查询条件
			if (!noEmpty(dscCompanyId)) {
				dscCompanyId = currentUser(request).getDscCompanyId();
			}

//			List<String> listDscCompanyIds = dscCompanyService
//					.getSelfAndSubIds(dscCompanyId);
//
//			if (listDscCompanyIds.size() > 0) {
//
//				for (int i = 0, count = listDscCompanyIds.size(); i < count; i++) {
//					listObjs.add(listDscCompanyIds.get(i));
//				}
//
//				baseDao.in("dscCompanyId", listDscCompanyIds);
//			}
			
			if(null != roleId && !"".equals(roleId)){
				baseDao.equal("roleId", roleId);
				listObjs.add(roleId);
			}
			
		}
		
		baseDao.orderDesc("recTime");
		
		sysResult.setTotal(baseDao.getTotalCount(listObjs.size() > 0? listObjs.toArray():null));
		
		List<Object> listUsers = baseDao.query(start, limit); 
		
//		//格式化用户角色及部门公司名称
//		Map<String,String> mapRoleIdNames = sysRoleService.allIdName();
//		Map<String,String> mapDscCompanyIdNames = dscCompanyService.allIdName();
//		
//		for (Object object : listUsers) {
//			SysUser sUser = (SysUser)object;
//			String sysRoleId = sUser.getRoleId();
//			String dscCompanyId = sUser.getDscCompanyId();
//			
//			if(mapRoleIdNames.containsKey(sUser.getRoleId())){
//				sUser.setSysRoleName(mapRoleIdNames.get(sysRoleId));
//			}
//			
//			if(mapDscCompanyIdNames.containsKey(dscCompanyId)){
//				sUser.setDscCompanyName(mapDscCompanyIdNames.get(dscCompanyId));
//			}
//			
//		}
		
		sysResult.setData(listUsers);
		sysResult.setSuccess(true);
		return sysResult;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object cud(SysUser obj,HttpServletRequest request){
		SysResult sysResult = SysResult.newInstance();
		String action = request.getParameter("action");
		SysUser currentUser = currentUser(request);
		
		if("save".equals(action)){
			if(IsExist(obj)){
				sysResult.setSuccess(false);
				sysResult.setMsg("用户名称已存在");
			}else{
				obj.setId(CommonMethod.createID());
				obj.setRecName(currentUser.getUserName());
				obj.setRecTime(new Date());
				baseDao.save(obj);
				
				System.out.println(obj.toString());
				
				sysResult.setSuccess(true);
			}
		}else if("update".equals(action)){
			if(IsExist(obj)){
				sysResult.setMsg("用户名已存在");
				sysResult.setSuccess(false);
			}else{
				
				obj.setRecName(currentUser.getUserName());
				obj.setRecTime(new Date());
				baseDao.saveOrUpdate(obj);
				sysResult.setSuccess(true);
			}
		}else if("delete".equals(action)){
			
			/**
			 * 如果是当前用户不可以删除
			 */
			if(obj.getId().equals(currentUser.getId())){
				sysResult.setMsg("用户不可以删除自身!");
				sysResult.setSuccess(false);
				return sysResult;
			}
			
			baseDao.delete(obj);
			sysResult.setSuccess(true);
		}else if("changePassword".equals(action)){
			
			String oldPassword = obj.getPassword();
			String newPassword = obj.getNewPassword();
			
			if(noEmpty(oldPassword) && !oldPassword.equals(currentUser.getPassword())){
				sysResult.setMsg("旧密码不正确!");
				sysResult.setSuccess(false);
				return sysResult;
			}
			
			if(noEmpty(oldPassword) && noEmpty(newPassword) && oldPassword.equals(newPassword)){
				sysResult.setMsg("新旧密码相同!");
				sysResult.setSuccess(false);
				return sysResult;
			}
			
			if(noEmpty(oldPassword) && oldPassword.equals(currentUser.getPassword())){
				
				Object obj_user = baseDao.createCriteria(SysUser.class).equal("userName", currentUser.getUserName()).equal("password", currentUser.getPassword()).queryUnique();
				
				if(noEmpty(obj_user)){
					
					SysUser sysUser = (SysUser)obj_user;
					sysUser.setPassword(newPassword);
					
					request.getSession().setAttribute(SessionInterceptor.LOGINFLAG,	sysUser);
					
					sysResult.setSuccess(true);
					return sysResult;
				}else{
					sysResult.setMsg("用户不存在!");
					sysResult.setSuccess(false);
					return sysResult;
				}
				
			}
			
		}
		
		return sysResult;
	}
	
}
