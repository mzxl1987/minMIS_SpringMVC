package mis.li.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mis.li.entity.SysUser;
import mis.li.service.SysUserService;

@Controller
public class UserController extends BaseController {

	@Resource
	SysUserService sysUserService;
	
	/**
	 * 登录接口
	 * Web & Android
	 * @param sysUser
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "login",method=RequestMethod.POST)
	@ResponseBody
	public Object login(@ModelAttribute(value="sysUser") SysUser sysUser, ModelMap modelMap,
			HttpServletRequest request) {
		return sysUserService.login(sysUser, request);	
	}

	@Override
	@RequestMapping(value="user",method=RequestMethod.GET)
	public String index(ModelMap modelMap,HttpServletRequest request){
		return "user";
	}
	
	@RequestMapping(value="user/queryByPage")
	@ResponseBody
	public Object queryByPage(@ModelAttribute(value="sysUser") SysUser sysUser,ModelMap modelMap, HttpServletRequest request){
		return sysUserService.queryByPage(sysUser,request);
	}
	
	/**
	 * 对用户对象的操作:新建，编辑，删除，修改密码
	 * Web & Android
	 * @param sysUser
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value="user/cud")
	@ResponseBody
	public Object cud(@ModelAttribute(value="sysUser") SysUser sysUser,ModelMap modelMap,HttpServletRequest request){
		
		
		return sysUserService.cud(sysUser, request);
		
		
	}
	
}
