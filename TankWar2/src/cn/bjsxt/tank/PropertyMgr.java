package cn.bjsxt.tank;
import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {
	static PropertyMgr mgr ;

	static Properties props = new Properties(); // 使用配置文件，增加程序灵活性

	static {
		try {
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private PropertyMgr(){};
	
	public static PropertyMgr getInstance(){
		if(mgr == null) {
			mgr = new PropertyMgr();
		}
		return mgr;
	}

	public static String getProperty(String key) {

		return props.getProperty(key);
	}
}
