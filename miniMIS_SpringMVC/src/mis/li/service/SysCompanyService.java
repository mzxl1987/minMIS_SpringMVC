package mis.li.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.li.util.CommonMethod;
import common.li.util.ISQLmini;
import mis.li.dao.BaseDao;
import mis.li.entity.DscCompany;
import mis.li.entity.SysMenu;
import mis.li.entity.SysResult;
import mis.li.entity.SysRole;
import mis.li.entity.SysUser;

@Service("dscCompanyService")
public class SysCompanyService extends BaseService {

	@SuppressWarnings("rawtypes")
	@Resource
	private BaseDao dscCompanyDao;

	private static Map<String, String> map_dscCompany_idName = new HashMap<String, String>();
	private static List<DscCompany> list_allDscCompany = new ArrayList<DscCompany>();

	/**
	 * @author 朱露露
	 * 判断公司部门是否存在
	 * 
	 * @param obj
	 * 公司部门对象
	 * 
	 * @return
	 * 返回结果:
	 * true:存在,
	 * false:不存在
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean IsExist(DscCompany obj) {

		dscCompanyDao.createCriteria(DscCompany.class).equal("name",
				obj.getName());

		if (null != obj.getId() && !"".equals(obj.getId())) {
			dscCompanyDao.notEqual("id", obj.getId());
		}

		List<SysUser> list_obj = dscCompanyDao.query();

		return null != list_obj && list_obj.size() > 0;
	}

	/**
	 * 根据id获得部门递归的SQL语句,其中包含自身
	 * @param id
	 * @return
	 */
	public String getRecurSQLById(String id){
		return ISQLmini.newSQL()
				.select("t.id")
				.from("dsc_company t")
				.where("FIND_IN_SET(t.id, getSelfAndSubCompany('"+ id +"'))")
				.toString();
	}
	
	/**
	 * 分页查找公司部门列表
	 * @param dscCompany
	 * 查询条件:
	 * start:起始数量,
	 * limit:每页数量,
	 * DscCompany属性
	 * @param request
	 * @return
	 * 返回结果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object queryByPage(DscCompany dscCompany, HttpServletRequest request) {

		SysResult sysResult = SysResult.newInstance();
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));

		List<Object> listObjs = new ArrayList<Object>();

		dscCompanyDao.createCriteria(DscCompany.class);

		if (null != dscCompany && dscCompany != null) {

			// if (null != dscCompany.getName()
			// && !"".equals(dscCompany.getName())) {
			// dscCompanyDao.like("name", "%" + dscCompany.getName() + "%");
			// listObjs.add(dscCompany.getName());
			// }
		}

		sysResult.setTotal(dscCompanyDao.getTotalCount(listObjs.toArray()));
		sysResult.setData(dscCompanyDao.query(start, limit));
		sysResult.setSuccess(true);
		return sysResult;
	}

	/**
	 * 新建，编辑，删除功能
	 * @param
	 * action : save->保存, update->更新, delete->删除
	 * @param obj
	 * @param request
	 * @return
	 * 返回结果:
	 * SysResult
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object cud(DscCompany obj, HttpServletRequest request) {
		SysResult sysResult = SysResult.newInstance();
		String action = request.getParameter("action");

		if ("save".equals(action)) {
			if (IsExist(obj)) {
				sysResult.setSuccess(false);
				sysResult.setMsg("公司名称已存在");
			} else {

				obj.setId(CommonMethod.createID());

				dscCompanyDao.save(obj);
				sysResult.setSuccess(true);
			}
		} else if ("update".equals(action)) {
			if (IsExist(obj)) {
				sysResult.setSuccess(false);
				sysResult.setMsg("公司名称已存在");
			} else {

				DscCompany dscCompany = (DscCompany) dscCompanyDao
						.createCriteria(DscCompany.class)
						.equal("id", obj.getId()).queryUnique();
				dscCompany.setName(obj.getName());
				dscCompanyDao.saveOrUpdate(dscCompany);
				sysResult.setSuccess(true);
			}
		} else if ("delete".equals(action)) {

			/**
			 * 是否是当前用户所在部门
			 */
			if (currentUser(request).getDscCompanyId().equals(obj.getId())) {
				sysResult.setMsg("当前用户所在部门,不可以删除!");
				sysResult.setSuccess(false);
				return sysResult;
			}

			/**
			 * 判断公司部门下面是否有关联的数据
			 */
			List listselfAndSubCompanyId = getSelfAndSubIds(obj.getId());

			if (noEmpty(listselfAndSubCompanyId)
					&& listselfAndSubCompanyId.size() > 1) {
				sysResult.setMsg("部门下面有相应的子部门或者子公司,不可以删除!");
				sysResult.setSuccess(false);
				return sysResult;
			}

			/**
			 * 是否有角色
			 */
			List listRoles = dscCompanyDao.createCriteria(SysRole.class)
					.equal("dscCompanyId", obj.getId()).query();
			if (noEmpty(listRoles) && listRoles.size() > 0) {
				sysResult.setMsg("部门下面有相应角色,不可以删除!");
				sysResult.setSuccess(false);
				return sysResult;
			}

			/**
			 * 是否有用户
			 */
			List listUsers = dscCompanyDao.createCriteria(SysUser.class)
					.equal("dscCompanyId", obj.getId()).query();
			if (noEmpty(listUsers) && listUsers.size() > 0) {
				sysResult.setMsg("部门下面有相应用户,不可以删除!");
				sysResult.setSuccess(false);
				return sysResult;
			}

			//TODO  
			//TODO 删除相关的数据
			
			dscCompanyDao.delete(obj);
			sysResult.setSuccess(true);
		}

		refresh_allIdName();
		refresh_allDscCompany();

		return sysResult;
	}

	/**
	 * 按照公司部门的层级递归生成公司或者部门的树
	 * @param dscCompany
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object tree(DscCompany dscCompany, HttpServletRequest request) {

		List<Object> result = null;
		SysUser sysUser = this.currentUser(request);

		dscCompanyDao.createCriteria(DscCompany.class).equal("id",
				sysUser.getDscCompanyId());

		DscCompany model = (DscCompany) dscCompanyDao.queryUnique();
		if (null != model && model != null) {
			result = new ArrayList<Object>();
			SysMenu sysMenu = new SysMenu();
			sysMenu.setId(model.getId());
			sysMenu.setText(model.getName());
			sysMenu.setLeaf(false);
			sysMenu.setExpanded(true);
			sysMenu.setChildren(new ArrayList());
			result.add(sysMenu);

			List<DscCompany> list_allDscCompany = allDscCompany();

			creatCompanyTree(list_allDscCompany, model.getId(),
					sysMenu.getChildren());

		}

		return result;
	}

	/**
	 * 获取所有部门及公司的ID-Name键值组合
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void refresh_allIdName() {
		List<Object> listObjs = dscCompanyDao.createCriteria(DscCompany.class)
				.query();
		HashMap<String, String> result = new HashMap<String, String>();

		if (null != listObjs) {
			for (int i = 0, count = listObjs.size(); i < count; i++) {
				DscCompany dscCompany = (DscCompany) listObjs.get(i);
				String name = dscCompany.getName();
				String id = dscCompany.getId();
				if (null != id && !"".equals(id)) {
					result.put(id, name);
				}
			}
		}

		map_dscCompany_idName = result;
	}

	/**
	 * 获取所有部门及公司的ID-Name键值组合
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> allIdName() {

		if (!noEmpty(map_dscCompany_idName)
				|| map_dscCompany_idName.size() == 0) {

			List<Object> listObjs = dscCompanyDao.createCriteria(
					DscCompany.class).query();
			HashMap<String, String> result = new HashMap<String, String>();

			if (null != listObjs) {
				for (int i = 0, count = listObjs.size(); i < count; i++) {
					DscCompany dscCompany = (DscCompany) listObjs.get(i);
					String name = dscCompany.getName();
					String id = dscCompany.getId();
					if (null != id && !"".equals(id)) {
						result.put(id, name);
					}
				}
			}

			map_dscCompany_idName = result;

		}

		return map_dscCompany_idName;
	}

	/**
	 * 获取所有部门及公司
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void refresh_allDscCompany() {

		List<Object> listObjs = dscCompanyDao.createCriteria(DscCompany.class)
				.query();
		List<DscCompany> result = new ArrayList<DscCompany>();

		if (noEmpty(listObjs)) {
			for (int i = 0, count = listObjs.size(); i < count; i++) {
				DscCompany dscCompany = (DscCompany) listObjs.get(i);
				if (noEmpty(dscCompany)) {
					result.add(dscCompany);
				}
			}
		}

		list_allDscCompany = result;
	}

	/**
	 * 获取所有部门及公司
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DscCompany> allDscCompany() {

		if (!noEmpty(list_allDscCompany) || list_allDscCompany.size() == 0) {

			List<Object> listObjs = dscCompanyDao.createCriteria(
					DscCompany.class).query();
			List<DscCompany> result = new ArrayList<DscCompany>();

			if (noEmpty(listObjs)) {
				for (int i = 0, count = listObjs.size(); i < count; i++) {
					DscCompany dscCompany = (DscCompany) listObjs.get(i);
					if (noEmpty(dscCompany)) {
						result.add(dscCompany);
					}
				}
			}

			list_allDscCompany = result;

		}

		return list_allDscCompany;
	}

	/**
	 * 递归算法生成部门功能菜单 add 朱露露
	 * 
	 * @param id
	 * @param children
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void creatCompanyTree(List<DscCompany> source, String lastId,
			List children) {

		for (DscCompany model : source) {

			String childLastId = model.getLastId();

			if (null == lastId || "".equals(lastId) || null == childLastId
					|| "".equals(childLastId) || !childLastId.equals(lastId)) {
				continue;
			}

			SysMenu menuDepart = new SysMenu();
			menuDepart.setId(model.getId());// 部门id
			menuDepart.setText(model.getName());// 部门名称
			menuDepart.setExpanded(false);// 是否展开子树
			menuDepart.setChildren(new ArrayList());// 子节点list
			children.add(menuDepart);// 表示此节点为上一节点的子节点
			creatCompanyTree(source, model.getId(), menuDepart.getChildren());
		}
	}

	/**
	 * 为Extjs.Combo组件生成数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object combo(HttpServletRequest request) {

		SysResult sysResult = SysResult.newInstance();
		SysUser sysUser = this.currentUser(request);
		List<Object> list = new ArrayList<Object>();

		sysResult.setTotal(dscCompanyDao.createCriteria(DscCompany.class)
				.getTotalCount(null));

		DscCompany dscCompany = (DscCompany) dscCompanyDao.equal("id",
				sysUser.getDscCompanyId()).queryUnique();
		
		
		if (null != dscCompany && dscCompany != null) {
			list.add(dscCompany);

			List<DscCompany> list_allDscCompany = allDscCompany();
			createComboItem(list_allDscCompany, dscCompany.getId(), list,
					"----");
		}

		sysResult.setData(list);
		sysResult.setSuccess(true);
		return sysResult;
	}
	
	/**
	 * 为Extjs.Combo组件生成数据
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public Object combo_tpy(HttpServletRequest request) {

		SysResult sysResult = SysResult.newInstance();
		SysUser sysUser = new SysUser();
		sysUser.setDscCompanyId("B00001");
		List<Object> list = new ArrayList<Object>();

		sysResult.setTotal(dscCompanyDao.createCriteria(DscCompany.class)
				.getTotalCount(null));

		DscCompany dscCompany = (DscCompany) dscCompanyDao.equal("id",
				sysUser.getDscCompanyId()).queryUnique();
		
		
		if (null != dscCompany && dscCompany != null) {
			list.add(dscCompany);

			List<DscCompany> list_allDscCompany = allDscCompany();
			createComboItem(list_allDscCompany, dscCompany.getId(), list,
					"----");
		}

		sysResult.setData(list);
		sysResult.setSuccess(true);
		return sysResult;
	}

	/**
	 * 递归算法生成部门 add 朱露露
	 * 
	 * @param id
	 * @param children
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void createComboItem(List<DscCompany> source, String lastId,
			List children, String tabStr) {

		String newTabStr = "----" + tabStr;

		for (DscCompany model : source) {
			String childLastId = model.getLastId();

			if (null == lastId || "".equals(lastId) || null == childLastId
					|| "".equals(childLastId) || !childLastId.equals(lastId)) {
				continue;
			}

			DscCompany tmp = new DscCompany();
			tmp.setId(model.getId());
			tmp.setName(tabStr + model.getName());
			children.add(tmp);

			createComboItem(source, model.getId(), children, newTabStr);
		}
	}

	/**
	 * 提取子公司部门的ID
	 * 
	 * @param lastId
	 * @return
	 */
	@Transactional
	public List<String> getSelfAndSubIds(String lastId) {

		List<String> ids = new ArrayList<String>();

		if (null == lastId || "".equals(lastId)) {
			return ids;
		}

		/**
		 * 递归查询SQL语句
		 */
		// String sql_query =
		// "select * from dsc_company t connect by prior t.id = t.lastid start with t.id = '"+
		// lastId +"'";
		// List<DscCompany> listDscCompanys =
		// dscCompanyDao.queryAllBySql(DscCompany.class, sql_query);

		/**
		 * 递归查询
		 */
		List<DscCompany> listDscCompanys = new ArrayList<DscCompany>();
		List<DscCompany> list_allDscCompany = allDscCompany();
		createComboItem(list_allDscCompany, lastId, listDscCompanys, "");

		for (int i = 0, count = listDscCompanys.size(); i < count; i++) {
			ids.add(listDscCompanys.get(i).getId());
		}

		ids.add(lastId);

		return ids;

	}

}
