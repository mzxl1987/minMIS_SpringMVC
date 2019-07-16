package mis.li.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		return getCurrentLookupKey();
	}

	public static String getCurrentLookupKey() {

        return (String) contextHolder.get();  
    } 

	public static void setCurrentLookupKey(String currentLookupKey) {

        contextHolder.set(currentLookupKey);  
    } 
	
}
