package mis.li.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * 该对象主要用于Extjs数据的返回,返回格式是JSON对象
 * {total:xxx,msg:xxx,success:xxx,result:[{xxx:xxxx,......},{xxx:xxx,......}]}
 * @author Administrator
 *
 * @param <T>
 */
public class SysResult<T> {
	
	/**
	 * 数据内容列表,结果集;存放对象Object类型
	 */
	private List<T> data;
	/**
	 * 查询数据总条数
	 */
	private long total ;
	/**
	 * 错误信息
	 */
	private String msg;
	/**
	 * 用于判断结果是否正确
	 * true:结果正确,
	 * false:结果错误
	 */
	private boolean success;
	
	private SysResult(){
		data = new ArrayList<T>();
	}
	
	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public static <T> SysResult<T> newInstance(){
		return new SysResult<T>();
	}
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
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
	
	@Override
	public String toString() {
		return "SysResult [result=" + data + ", total=" + total + ", msg="
				+ msg + ", success=" + success + "]";
	}
	
		
}
