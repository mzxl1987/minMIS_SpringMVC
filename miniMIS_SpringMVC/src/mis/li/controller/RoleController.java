package mis.li.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mis.li.entity.SysRole;
import mis.li.service.SysRoleService;

@Controller
public class RoleController extends BaseController {

	@Resource
	SysRoleService sysRoleService;
	
	@Override
	@RequestMapping(value="role",method=RequestMethod.GET)
	public String index(ModelMap modelMap,HttpServletRequest request){
		return "role";
	}
	
	@RequestMapping(value="role/queryByPage")
	@ResponseBody
	public Object queryByPage(@ModelAttribute(value="sysRole") SysRole sysRole,ModelMap modelMap, HttpServletRequest request){
		return sysRoleService.queryByPage(sysRole,request);
	}
	
	@RequestMapping(value="role/queryNoPage",method=RequestMethod.GET)
	@ResponseBody
	public Object queryNoPage(@ModelAttribute(value="sysRole") SysRole sysRole,ModelMap modelMap, HttpServletRequest request){
		return sysRoleService.queryNoPage(sysRole,request);
	}
	
	@RequestMapping(value="role/cud")
	@ResponseBody
	public Object cud(@ModelAttribute(value="sysRole") SysRole sysRole,ModelMap modelMap,HttpServletRequest request){
		return sysRoleService.cud(sysRole, request);
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}
	
	
	
	
	
	
	
	
	
}
