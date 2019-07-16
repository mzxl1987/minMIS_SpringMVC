package mis.li.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.li.util.CommonMethod;
import common.li.util.ISQL;
import common.li.util.ISQLmini;
import mis.li.dao.BaseDao;
import mis.li.entity.SysResult;
import mis.li.entity.SysRole;
import mis.li.entity.SysUser;

@Service("sysRoleService")
public class SysRoleService extends BaseService {

	@SuppressWarnings("rawtypes")
	@Resource
	private BaseDao baseDao;

	@Resource
	private SysCompanyService dscCompanyService;

	private static Map<String, String> map_sysRole_idName = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	@Transactional
	public boolean IsExist(SysRole obj) {

		baseDao.createCriteria(SysRole.class).equal("name", obj.getName());

		if (null != obj.getId() && !"".equals(obj.getId())) {
			baseDao.notEqual("id", obj.getId());
		}

		List<SysUser> list_obj = baseDao.query();

		return null != list_obj && list_obj.size() > 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object queryByPage(SysRole sysRole, HttpServletRequest request) {

		SysResult sysResult = SysResult.newInstance();
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));

		ISQL iSQL_where = ISQL.newInstance();
		iSQL_where.setTableAlis("t_");
		
		ISQLmini iSQLmini_query = ISQLmini.newSQL();
		List<Object> listRoles = new ArrayList<Object>();
		
		if (noEmpty(sysRole)) {

			String name = sysRole.getName();
			String dscCompanyId = sysRole.getDscCompanyId();

			if (noEmpty(name)) {
				iSQL_where.where("and", "name", "like", name);
			}

			// 添加DscCompanyIds查询条件
			if (!noEmpty(dscCompanyId)) {
				dscCompanyId = currentUser(request).getDscCompanyId();
			}

			iSQL_where.in("and", "dscCompanyId", dscCompanyService.getRecurSQLById(dscCompanyId));
			
		}
		
		/**
		 * 尝试用SQL语句翻页查询
		 */
		String sql_role = iSQLmini_query.select("t_.id,t_.name,t_.dscCompanyId,t_.recName,date_format(t_.recTime,'%Y-%c-%d %h:%i:%s') as recTime,dc.name as dscCompanyName")
				.from("sys_role t_")
				.leftJoin("dsc_Company dc").on("t_.dscCompanyId = dc.id")
				.where(iSQL_where.getWhere())
				.order("t_.recTime desc")
				.toString();
		
		System.out.println(sql_role);
		
		int total = (int)baseDao.getTotalCountBySql(sql_role);
		List<Map> list_map = baseDao.queryPageBySql(sql_role, start, limit);
		
		listRoles = CommonMethod.mapToObject(SysRole.class, list_map);
		sysResult.setTotal(total);
		sysResult.setData(listRoles);
		sysResult.setSuccess(true);
		
		System.out.println(sysResult.toString());
		
		return sysResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object queryNoPage(SysRole sysRole, HttpServletRequest request) {

		SysResult sysResult = SysResult.newInstance();

		baseDao.createCriteria(SysRole.class);

		List<Object> list = new ArrayList<Object>();

		if (null != sysRole && sysRole != null) {

			String name = sysRole.getName();
			String dscCompanyId = sysRole.getDscCompanyId();

			/**
			 * 添加查询条件 名称
			 */
			if (null != name && !"".equals(name)) {
				baseDao.like("name", "%" + name + "%");
				list.add(name);
			}

			/**
			 * 添加查询条件 部门公司ID
			 */
			if (null == dscCompanyId && "".equals(dscCompanyId)) {
				dscCompanyId = currentUser(request).getDscCompanyId();
			}

			baseDao.equal("dscCompanyId", dscCompanyId);
			list.add(dscCompanyId);

			// TO DO

		}

		sysResult.setTotal(baseDao.getTotalCount(list.toArray()));
		sysResult.setData(baseDao.query());
		sysResult.setSuccess(true);
		return sysResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object cud(SysRole obj, HttpServletRequest request) {
		SysResult sysResult = SysResult.newInstance();
		String action = request.getParameter("action");

		if ("save".equals(action)) {
			if (IsExist(obj)) {
				sysResult.setSuccess(false);
				sysResult.setMsg("角色名称已存在");
			} else {

				obj.setId(CommonMethod.createID());
				obj.setRecName(currentUser(request).getUserName());
				obj.setRecTime(new Date());
				baseDao.save(obj);
				sysResult.setSuccess(true);
			}
		} else if ("update".equals(action)) {
			if (IsExist(obj)) {
				sysResult.setSuccess(false);
				sysResult.setMsg("角色名称已存在");
			} else {

				Object objRole = baseDao.createCriteria(SysRole.class)
						.equal("id", obj.getId()).queryUnique();
				if (null != objRole) {
					SysRole sysRole = (SysRole) objRole;
					sysRole.setName(obj.getName());
					sysRole.setDscCompanyId(obj.getDscCompanyId());
					sysRole.setRecName(currentUser(request).getUserName());
					sysRole.setRecTime(new Date());
				}

				sysResult.setSuccess(true);
			}
		} else if ("delete".equals(action)) {
			
			/**
			 * 是否是当前用户所在角色
			 */
			if(currentUser(request).getRoleId().equals(obj.getId())){
				sysResult.setMsg("当前用户所在角色,不可以删除!");
				sysResult.setSuccess(false);
				return sysResult;
			}
			
			/**
			 * 是否有用户
			 */
			List listUsers = baseDao.createCriteria(SysUser.class).equal("roleId", obj.getId()).query();
			if(noEmpty(listUsers) && listUsers.size() > 0){
				sysResult.setMsg("角色下面有相应用户,不可以删除!");
				sysResult.setSuccess(false);
				return sysResult;
			}
			
			baseDao.delete(obj);
			sysResult.setSuccess(true);
		}

		refresh_allIdName();
		
		return sysResult;
	}

	@SuppressWarnings("unchecked")
	public void refresh_allIdName() {
		List<Object> listObjs = baseDao.createCriteria(SysRole.class)
				.query();
		HashMap<String, String> result = new HashMap<String, String>();

		if (null != listObjs) {
			for (int i = 0, count = listObjs.size(); i < count; i++) {
				SysRole sysRole = (SysRole) listObjs.get(i);
				String name = sysRole.getName();
				String id = sysRole.getId();
				if (null != id && !"".equals(id)) {
					result.put(id, name);
				}
			}
		}

		map_sysRole_idName = result;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> allIdName() {
		if (!noEmpty(map_sysRole_idName) || map_sysRole_idName.size() == 0) {
			List<Object> listObjs = baseDao.createCriteria(SysRole.class)
					.query();
			HashMap<String, String> result = new HashMap<String, String>();

			if (null != listObjs) {
				for (int i = 0, count = listObjs.size(); i < count; i++) {
					SysRole sysRole = (SysRole) listObjs.get(i);
					String name = sysRole.getName();
					String id = sysRole.getId();
					if (null != id && !"".equals(id)) {
						result.put(id, name);
					}
				}
			}
			map_sysRole_idName = result;
		}

		return map_sysRole_idName;
	}

}
