package mis.li.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mis.li.entity.DscCompany;
import mis.li.service.SysCompanyService;

@Controller
public class CompanyController extends BaseController {

	@Resource
	SysCompanyService dscCompanyService;
	
	public SysCompanyService getDscCompanyService() {
		return dscCompanyService;
	}

	public void setDscCompanyService(SysCompanyService dscCompanyService) {
		this.dscCompanyService = dscCompanyService;
	}

	@Override
	@RequestMapping(value="dscCompany",method=RequestMethod.GET)
	public String index(ModelMap modelMap,HttpServletRequest request){
		return "dscCompany";
	}
	
	@RequestMapping(value="dscCompany/queryByPage")
	@ResponseBody
	public Object queryByPage(@ModelAttribute(value="dscCompany") DscCompany dscCompany,ModelMap modelMap, HttpServletRequest request){
		return dscCompanyService.queryByPage(dscCompany,request);
	}
	
	@RequestMapping(value="dscCompany/combo",method=RequestMethod.GET)
	@ResponseBody
	public Object combo(ModelMap modelMap, HttpServletRequest request){
		return dscCompanyService.combo(request);
	}
	
	@RequestMapping(value="dscCompany/tpy/combo",method=RequestMethod.GET)
	@ResponseBody
	public Object combo_tpy(ModelMap modelMap, HttpServletRequest request){
		return dscCompanyService.combo_tpy(request);
	}
	
	@RequestMapping(value="dscCompany/cud")
	@ResponseBody
	public Object cud(@ModelAttribute(value="dscCompany") DscCompany dscCompany,ModelMap modelMap,HttpServletRequest request){
		return dscCompanyService.cud(dscCompany, request);
	}
	
	@RequestMapping(value="dscCompany/tree",method=RequestMethod.GET)
	@ResponseBody
	public Object tree(@ModelAttribute(value="dscCompany") DscCompany dscCompany,ModelMap modelMap,HttpServletRequest request){
		return dscCompanyService.tree(dscCompany, request);
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
