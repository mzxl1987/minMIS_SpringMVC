package mis.li.service;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import common.li.util.LoggerUtil;
import mis.li.entity.SysUser;
import mis.li.interceptor.SessionInterceptor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("baseService")
public class BaseService {
	
	/**
	 * 二进制，同带binary前缀的效果 : BINARY    
    字符型，可带参数 : CHAR()     
    日期 : DATE     
    时间: TIME     
    日期时间型 : DATETIME     
    浮点数 : DECIMAL      
    整数 : SIGNED     
    无符号整数 : UNSIGNED 
	 */
	public final String MYSQL_CONVERT_BINARY = "BINARY";
	public final String MYSQL_CONVERT_DATE = "DATE";
	public final String MYSQL_CONVERT_TIME = "TIME";
	public final String MYSQL_CONVERT_DATETIME = "DATETIME";
	public final String MYSQL_CONVERT_DECIMAL = "DECIMAL(20,2)";
	public final String MYSQL_CONVERT_SIGNED = "SIGNED";
	public final String MYSQL_CONVERT_UNSIGNED = "UNSIGNED";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public SysUser currentUser(HttpServletRequest request){
		return (SysUser) request.getSession().getAttribute(SessionInterceptor.LOGINFLAG);
	}
	
	/**
	 * 检查变量是否为空
	 * @param params
	 * @return
	 */
	public boolean noEmpty(String[] params) {
        
		if(null == params){
			return false;
		}
		
		for (String str : params) {
			if (null == str || "".equals(str) || str.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查变量是否为空
	 * @param params
	 * @return
	 */
	public boolean noEmpty(Object obj) {
		return null != obj && !"".equals(obj);
	}
	
	
	public void log(String msg){
		Logger log = LoggerUtil.getLog(this.getClass());
			
		log.info(new Date());
		log.info(msg);
	}
	
	/**
	 * 二进制，同带binary前缀的效果 : BINARY    
    字符型，可带参数 : CHAR()     
    日期 : DATE     
    时间: TIME     
    日期时间型 : DATETIME     
    浮点数 : DECIMAL      
    整数 : SIGNED     
    无符号整数 : UNSIGNED 
    CAST(value as type);  
CONVERT(value, type); 
	 * @param sortJsonStr
	 * @return
	 */
	public String order(JSONArray sortArray){
		String result = " ";
		
		if(noEmpty(sortArray) && sortArray.size() > 0){
			
			result += " order by ";
			
			for(int i=0,count = sortArray.size(); i < count; i++){
				
				JSONObject sort = sortArray.getJSONObject(i);
				
				String property = sort.getString("property");
				String direction = sort.getString("direction");
				String convert = null;
				if(sort.containsKey("convert")){
					convert = sort.getString("convert");
					property = String.format(" CAST(%s as %s) ",property,convert);
				}
				
				result += property + " " + direction +  " ";
				
				if(i + 1 != count){
					result += " , ";
				}
			}
			
		}
		
		return result;
	}
	
}
