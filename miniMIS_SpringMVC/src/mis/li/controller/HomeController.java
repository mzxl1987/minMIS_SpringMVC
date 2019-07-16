package mis.li.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mis.li.interceptor.SessionInterceptor;
import mis.li.service.SysHomeService;

@Controller
public class HomeController extends BaseController {
	
	@Resource
	SysHomeService sysHomeService;
	
	/***
	 * 跳转大Home页面
	 * @return
	 */
	
	@RequestMapping("helloWorld")
	public String helloWorld(ModelMap modelMap,HttpServletRequest request){
		return "helloWorld";
	}
	
	/***
	 * 跳转大Home页面
	 * @return
	 */
	@Override
	@RequestMapping("/")
	public String index(ModelMap modelMap,HttpServletRequest request){
		return "index";
	}
	
	/**
	 * 跳转到登录界面
	 * @param modelMap
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "",method=RequestMethod.GET)
	public String index(){
		return "login";
	}
	
	@RequestMapping(value = "login",method=RequestMethod.GET)
	public String login(ModelMap modelMap,HttpServletRequest request){
		return "login";
	}
	
	@RequestMapping(value = "go",method=RequestMethod.GET)
	public String go(ModelMap modelMap,HttpServletRequest request){
		
		return "redirect:/login";
	}
	
	/**
	 * 退出
	 * @param modelMap
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "logout")
	public String logout(ModelMap modelMap, HttpServletRequest req) {

		req.getSession().removeAttribute(SessionInterceptor.LOGINFLAG);
		req.getSession().removeAttribute(SessionInterceptor.DBNAME);
		
		return "login";
	}
	
	/**
	 * 创建左侧导航栏
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(value="nativeMenu")
	@ResponseBody
	public Object nativeMenu(ModelMap modelMap,HttpServletRequest request){
		return sysHomeService.createNativeMenu(request);
	}
	
}
