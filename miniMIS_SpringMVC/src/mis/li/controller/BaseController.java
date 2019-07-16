package mis.li.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

@Controller
public abstract class BaseController {

	public boolean success;
	
	public String msg;

	abstract String index(ModelMap modelMap,HttpServletRequest	 request);
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public boolean checkParams(String[] params) {

		for (String str : params) {
			if ("".equals(str) || null == str || str.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
}
