package common.li.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class LoggerUtil {
	
	public static Logger getLog(Class<?> clazz) {  
        
        Logger logger = Logger.getLogger(clazz);  // 生成新的Logger
        
        logger.removeAllAppenders(); // 清空Appender，特別是不想使用現存實例時一定要初期化  
        logger.setLevel(Level.ALL); // 设定Logger級別。  
        logger.setAdditivity(true); // 设定是否继承父Logger。默认为true，继承root输出；设定false后将不出书root。  
          
        FileAppender appender = new RollingFileAppender(); // 生成新的Appender  
        PatternLayout layout = new PatternLayout();  
//        layout.setConversionPattern("[%d{yyyy-MM-dd HH:mm:ss}] %p %l : %m%n"); // log的输出形式
        layout.setConversionPattern("[%d{yyyy-MM-dd HH:mm:ss}] %p > %m%n"); // log的输出形式
        appender.setLayout(layout);  
          
        appender.setFile("\\logs\\" + getTime("yyyy-MM-dd")  + " " + clazz.getName() + ".log");  // log输出路径
        
//        System.out.println(appender.getFile());
        
        appender.setEncoding("UTF-8"); // log的字符编码  
        appender.setAppend(true);  //日志合并方式： true:在已存在log文件后面追加 false:新log覆盖以前的log  
        appender.activateOptions();  // 适用当前配置  
          
        logger.addAppender(appender); // 将新的Appender加到Logger中  
        return logger;  
    }  
      
    private static String getTime(String format) {  
        SimpleDateFormat sdf = new SimpleDateFormat(format);  
        return sdf.format(new Date());  
    }  
    
    
    public static Logger getLoggerByName(String name) {  
        // 生成新的Logger  
        // 如果已經有了一個Logger實例返回現有的  
        Logger logger = Logger.getLogger(name);  
        // 清空Appender。特別是不想使用現存實例時一定要初期化  
        logger.removeAllAppenders();  
        // 設定Logger級別。  
        logger.setLevel(Level.DEBUG);  
        // 設定是否繼承父Logger。  
        // 默認為true。繼承root輸出。  
        // 設定false後將不輸出root。  
        logger.setAdditivity(true);  
        // 生成新的Appender  
        FileAppender appender = new RollingFileAppender();  
        PatternLayout layout = new PatternLayout();  
        // log的输出形式  
        String conversionPattern = "[%d] %p %t %c - %m%n";  
        layout.setConversionPattern(conversionPattern);  
        appender.setLayout(layout);  
        // log输出路径  
        // 这里使用了环境变量[catalina.home]，只有在tomcat环境下才可以取到  
        String tomcatPath = java.lang.System.getProperty("catalina.home");  
        appender.setFile(tomcatPath + "/logs/" + getTime("yyyy-MM-dd") + " " + name + ".log");  
        // log的文字码  
        appender.setEncoding("UTF-8");  
        // true:在已存在log文件后面追加 false:新log覆盖以前的log  
        appender.setAppend(true);  
        // 适用当前配置  
        appender.activateOptions();  
        // 将新的Appender加到Logger中  
        logger.addAppender(appender);  
        return logger;  
    }  
	
}
