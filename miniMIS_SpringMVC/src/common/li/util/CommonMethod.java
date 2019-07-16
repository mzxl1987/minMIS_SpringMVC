package common.li.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class CommonMethod {
	
	public static int index = 0;
	private static Object obj_lock = new Object();
	
	/**
	 * 格式化当前字符串
	 */
	public static String formatString(String d,int len){
		String result = d;
		if("".equals(d) || d.length() > len) return d;
		
		for(int i =0, max = len - d.length(); i < max; i++){
			result = "0" + result;
		}
		
		return result;
	}
	
	/**
	 * 获得当前时间的毫秒数
	 * @return
	 */
	public static String getTime(){
		return "" + System.currentTimeMillis();
	}
	
	public static String createID() {
		synchronized (obj_lock) {
			return "" + new Date().getTime() + (index++);
		}
	}

	public static boolean noEmpty(Object obj){
		return null != obj && obj != null && !"".equals(obj);
	}
	
	public static String formatDateToyyyyMMddHHmmss(Date obj) {
		if(null == obj)  return "";
		
		return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(obj);
	}
	
	public static String formatDateToyyyyMMddHHmmss_1(Date obj) {
		if(null == obj) return "";
		
		return new java.text.SimpleDateFormat("yyyyMMddHHmmss")
				.format(obj);
	}
	
	public static String formatDateToyyyyMMdd(Date obj) {
		return new java.text.SimpleDateFormat("yyyy-MM-dd")
				.format(obj);
	}

	public static Date formatyyyyMMddHHmmssToDate(String dateString) {
		try {
			return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date formatyyyyMMddToDate(String dateString) {
		try {
			return new java.text.SimpleDateFormat("yyyy-MM-dd")
					.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将秒格式化成小时
	 * @param time
	 * @return
	 */
	public static String timeSpan(long time){
		
		return  new DecimalFormat("##0.00").format( time / 3600.0);
		
	}
	
	/**
	 * 将秒格式化成时分秒
	 * @param time
	 * @return
	 */
	public static String timeSpan1(long time){
		
		long second = time % 60;
		long minu = time / 60 % 60;
		long hour = time / 3600;
		
		return String.format("%02d:%02d:%02d", hour,minu,second);
		
	}
	
public static long timeSpanLong(Date startTime,Date endTime){
		
		try {  
            return Math.abs(endTime.getTime()-startTime.getTime()) / 1000;
        } catch (Exception e) {    
            e.printStackTrace();
            return 0;
        }  
	}
	
	/**
	 * 将属性列表转换成相应的对象列表 Map<field,value> --> class { field = value }
	 * 
	 * @param clazz
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Object> mapToObject(Class clazz, List<Map> list) {
		List<Object> result = new ArrayList<Object>();

		for (Map map : list) {
			try {
//				System.out.println("--------------");
				Object obj = clazz.newInstance();                            //<--------------创建实例
				Class objClass = obj.getClass();                             //<--------------获取类
				Field[] fields = objClass.getDeclaredFields();               //<--------------获取类属性

				for (Field field : fields) {
					
						String fieldName = field.getName();                      //<---------------获得属性名称
						
						try {
						
	//					System.out.println(fieldName + ":" + field.getType() + ":" + map.keySet().toString() + " : " + map.get(fieldName) + " : " + map.containsKey(fieldName));
						
						if (map.containsKey(fieldName)) {          //<---------------判断Map中是否存在该属性
							PropertyDescriptor pd = new PropertyDescriptor(
									fieldName, objClass);
	
							Object value ;
							value = map.get(fieldName);
							// 获得set方法
							if (field.getType().equals(Date.class)) {             //<---------------对时间类型的属性特殊处理
								if(noEmpty(value)){
	//								System.out.println("value : " + value);
									value = formatyyyyMMddHHmmssToDate((String)value);
								}
							}else if(field.getType().equals(int.class)){
								
	//							System.out.println("Int");
								
								if(noEmpty(value)){
									value = (int) Double.parseDouble(value.toString());
								}else{
									value = 0;
								}
							}else if(field.getType().equals(String.class)){
								if(noEmpty(value)){
									value = new String("" + value);
								}else{
									value = "";
								}
							}
							
//							System.out.println(field.getName() + "#value:" + value + " #filed type: "+field.getType()+"#valueClass:" + (noEmpty(value) ? value.getClass() : null));
//							System.out.println(value);
							
							
							if(noEmpty(value)){
								
								Method method = pd.getWriteMethod();                     //<--------------获得属性的些方法
								method.invoke(obj, new Object[] { value });
							}
							// 获得get方法
	//						 Method get = pd.getReadMethod();
	//						 Object getValue = get.invoke(list.get(0), new
	//						 Object[]{});
	//						 System.out.println("field:"+field.getName()+"---getValue:"+getValue);
						}
					
					} catch (Exception e) {
						e.printStackTrace();
//						System.out.println("function mapToObject Exception----------------------");
//						System.out.println(fieldName + ":" + field.getType() + ":" + map.keySet().toString() + " : " + map.get(fieldName) + " : " + map.containsKey(fieldName));
					}
				}
				
				result.add(obj);

//				System.out.println(obj.toString());

			} catch (Exception e) {
				System.out.println("function mapToObject Exception----------------------");
				System.out.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	
	public static String getProperty(String fileName,String pro){
		String result = null;
		Properties prop = new Properties();     
        
		try{
            //读取属性文件a.properties
            InputStream in = new BufferedInputStream (new FileInputStream(fileName));
            prop.load(in);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                System.out.println(key+":"+prop.getProperty(key));
            }
            in.close();
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        return result;
	}
	

	 /**********************************************
	 * 异或 校验 创建校验位
	 **********************************************/
	 public static int createXor(int[] data, int start, int len){
		 int tmp_crc = 0x00;

	    for(int i = 0; i < len; i++){
	        tmp_crc += tmp_crc ^ data[start + i];
	    }
	    tmp_crc &= 0xFF;
	    return tmp_crc;
	 }
	 
	 public static String bytes2hex(int[] data){
		 String result = "";
		 String baseHex = "0123456789ABCDEF";
		 for(int i =0,max = data.length; i < max; i++){
			 int l = data[i] & 0x0F;
			 int h = data[i] >> 4;
			 result += "" + baseHex.charAt(h) + baseHex.charAt(l);
		 }
		 
		 return result;
	 }
	 
	 public static int[] hex2bytes(String hexStr){
		 if(null == hexStr || "".equals(hexStr) || hexStr.length() % 2 != 0){
			 return null;
		 }
		 
		 hexStr = hexStr.toUpperCase();
		 
		 String baseHex = "0123456789ABCDEF";
		 
		 char[] arr_char = hexStr.toCharArray();
		 int[] result = new int[arr_char.length / 2];
		 
		 for(int i =0,max = arr_char.length,j=0; i < max;){
			 result[j] = ((baseHex.indexOf(arr_char[i++]) << 4) + (baseHex.indexOf(arr_char[i++]))) ;
			 j++;
		 }
		 
		 return result;
		 
	 }
	

}
