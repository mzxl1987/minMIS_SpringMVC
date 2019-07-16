package mis.li.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import mis.li.dataSource.DynamicDataSource;

public class SessionInterceptor implements HandlerInterceptor {

	public static String LOGINFLAG = "LoginFlagID";
	public static String DBNAME = "DN";

	/**
	 * 58. * 在DispatcherServlet完全处理完请求后被调用 59. * 60. *
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion() 61.
	 */

	@Override
	public void afterCompletion(HttpServletRequest req,
			HttpServletResponse res, Object obj, Exception ex) throws Exception {


	}

	// 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj, ModelAndView mav) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 24. * 在业务处理器处理请求之前被调用 25. * 如果返回false 26. *
	 * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 27. * 28. * 如果返回true 29. *
	 * 执行下一个拦截器,直到所有的拦截器都执行完毕 30. * 再执行被拦截的Controller 31. * 然后进入拦截器链, 32. *
	 * 从最后一个拦截器往回执行所有的postHandle() 33. * 接着再从最后一个拦截器往回执行所有的afterCompletion() 34.
	 */

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object obj) throws Exception {
		
//		String url = req.getRequestURL().toString();
//
//		Object sAttr = req.getSession().getAttribute(LOGINFLAG);
//		Object dnAttr = req.getSession().getAttribute(DBNAME);
////		String dn = req.getParameter("dn");
////		String dn = "basicssh";
//		String dn = "obdcar";
//		String reqMethod = req.getMethod();
//
//		/**
//		 * 过滤URL
//		 */
//		System.out.println(url);
//		if(url.endsWith("/go") || url.indexOf("helloWorld") != -1){
//			DynamicDataSource.setCurrentLookupKey(dn);
//			req.getSession().setAttribute(DBNAME, dn);
//			System.out.println("通过session拦截器检测,URL白名单.");
//			return true;
//		}
//				
//		if (url.endsWith("/login")) {
//			
//			if("GET".equals(reqMethod)){
//				return true;
//			}else if("POST".equals(reqMethod)){
//				/**
//				 * 设置数据库连接 dn = DBName
//				 */
//				if (null == dn || "".equals(dn)) {
//					System.out.println("没有通过session拦截器,error:dn is null.");
//					req.getRequestDispatcher("/go").forward(req, res);
//					return false;
//				} else {
//					DynamicDataSource.setCurrentLookupKey(dn);
//					req.getSession().setAttribute(DBNAME, dn);
//					System.out.println("通过session拦截器检测");
//					return true;
//				}
//			}
//		}
//		
//		/**
//		 * 设置数据库连接 dn = DBName
//		 */
//		if (null == dnAttr || "".equals(dnAttr)) {
//			System.out.println("没有通过session拦截器,error:dnAttr is null.");
//			req.getRequestDispatcher("/go").forward(req, res);
//			
//			return false;
//		} else {
//			DynamicDataSource.setCurrentLookupKey((String) dnAttr);
//		}
//		
//		if (url.endsWith("/logout")) {
//			// 清除session变量
//			return true;
//		}
//
//		/**
//		 * 判断用户是否登录
//		 */
//		if (null == sAttr || req.getSession().isNew()) {
//			System.out.println("没有通过session拦截器,error:session is new or sAttr is null.");
//			req.getRequestDispatcher("/go").forward(req, res);
//			return false;
//		}
//
//		/**
//		 * 判断用户的权限是否正确
//		 */
//		// TODO
		
		return true;

	}

}
