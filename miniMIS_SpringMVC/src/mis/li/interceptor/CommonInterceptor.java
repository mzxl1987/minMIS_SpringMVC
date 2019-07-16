package mis.li.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import mis.li.service.SysFunctionService;


public class CommonInterceptor implements HandlerInterceptor {

	
	@Resource
	SysFunctionService sysFunctionService;
	
	public CommonInterceptor() {
		// TODO Auto-generated constructor stub
	}
	

	/** 
	58.     * 在DispatcherServlet完全处理完请求后被调用  
	59.     *  
	60.     *   当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 
	61.     */  

	@Override
	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse res, Object obj, Exception ex)
			throws Exception {
		
	}

	//在业务处理器处理请求执行完成后,生成视图之前执行的动作
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj, ModelAndView mav) throws Exception {
		// TODO Auto-generated method stub  
	}

	
	/** 
	24.     * 在业务处理器处理请求之前被调用 
	25.     * 如果返回false 
	26.     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
	27.     *  
	28.     * 如果返回true 
	29.     *    执行下一个拦截器,直到所有的拦截器都执行完毕 
	30.     *    再执行被拦截的Controller 
	31.     *    然后进入拦截器链, 
	32.     *    从最后一个拦截器往回执行所有的postHandle() 
	33.     *    接着再从最后一个拦截器往回执行所有的afterCompletion() 
	34.     */  

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj) throws Exception {
		
//		String url = req.getRequestURL().toString();
//		
//		String[] subUrls = url.split("/");
//		String newUrl = "";
//		
//		for(int i =0,end = 4; i < end;i++){
//			newUrl += subUrls[i] + "/";
//		}
//		
//		if(!sysFunctionService.hasAuthor(url, req)){
//			res.sendRedirect(newUrl);
////			req.getRequestDispatcher(newUrl).forward(req, res);
//			return false;
//		}
		
		return true;  

	}
	
}
