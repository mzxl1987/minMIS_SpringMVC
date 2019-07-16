package mis.li.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mis.li.dao.BaseDao;
import mis.li.entity.SysFunction;
import mis.li.entity.SysMenu;
import mis.li.entity.SysModel;
import mis.li.entity.SysUser;


@Service("sysHomeService")
public class SysHomeService extends BaseService{

	@SuppressWarnings("rawtypes")
	@Resource
	private BaseDao baseDao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	// <----------------事务
	public List<Object> createNativeMenu(HttpServletRequest request) {

		List<Object> result = new ArrayList<Object>();
		SysMenu sysMenu = new SysMenu();
		sysMenu.setId("1");
		sysMenu.setText("系统菜单");
		sysMenu.setLeaf(false);
		sysMenu.setExpanded(true);		
		sysMenu.setChildren(new ArrayList());
		
		SysUser sysUser = currentUser(request);
		
		List<SysFunction> list_fun = new ArrayList<SysFunction>();
		List<SysModel> list_model = new ArrayList<SysModel>();
		
		list_fun = baseDao.createCriteria(SysFunction.class).equal("roleId", "1").query();
		list_model = baseDao.createCriteria(SysModel.class).query();
		
		
		SysMenu menuF;
		SysMenu menuM;
		boolean flag;
		
		for (SysModel sysMo : list_model) {
			flag = false;
			menuM = new SysMenu();
			menuM.setId(sysMo.getId());
			menuM.setText(sysMo.getName());
			menuM.setIconCls(sysMo.getIconCls());
			menuM.setViewType(sysMo.getAction());
			menuM.setExpanded(true);
			
			for (SysFunction sysfun : list_fun) {
				if(sysfun.getSupId().equals(sysMo.getId())){
					flag = true;
					menuF = new SysMenu();
					menuF.setId(sysfun.getId());
					menuF.setText(sysfun.getName());
					menuF.setLeaf(true);
					menuF.setViewType(sysfun.getAction());
					menuF.setIconCls(sysfun.getIconCls());
					menuF.setSelectable(true);
					
					menuM.getChildren().add(menuF);
					
				}
			}
			
			if(flag || noEmpty(menuM.getViewType())){
				if(noEmpty(menuM.getViewType())) {
					menuM.setSelectable(true);
				}
				menuM.setLeaf(!flag);
				result.add(menuM);
			}
			
		}
		
		return result;
	}
	
}
