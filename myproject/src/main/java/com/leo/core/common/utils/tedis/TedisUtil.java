package com.leo.core.common.utils.tedis;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.common.tedis.commands.DefaultTedisManager;
import com.taobao.common.tedis.commands.TedisManagerFactory;
import com.taobao.common.tedis.core.AtomicCommands;
import com.taobao.common.tedis.core.HashCommands;
import com.taobao.common.tedis.core.ListCommands;
import com.taobao.common.tedis.core.SetCommands;
import com.taobao.common.tedis.core.TedisManager;
import com.taobao.common.tedis.core.ValueCommands;
import com.taobao.common.tedis.core.ZSetCommands;
import com.taobao.common.tedis.group.TedisGroup;

/**
 * redis缓存工具类（使用Tedis作为java客户端）
 * @date 2013-10-23 上午09:09:07
 * @author leo
 * version 1
 */
public class TedisUtil extends TedisManagerFactory{
    
    private static Logger logger = LoggerFactory.getLogger(TedisUtil.class);
    
    private static ConcurrentHashMap<String, TedisManager> managers = new ConcurrentHashMap<String, TedisManager>();
	private PropertiesConfigManager propertiesConfigManager; //自定义配置管理类（通过资源文件配置）
	private int nameSpace;
    private String appName;
    private String version;
    
	public synchronized TedisManager create() {
        String key = appName + "-" + version;
        TedisManager manager = managers.get(key);
        if (manager == null) {
            TedisGroup tedisGroup = new TedisGroup(appName, version);
            tedisGroup.setConfigManager(propertiesConfigManager);
            tedisGroup.init();
            manager = new DefaultTedisManager(tedisGroup);
            managers.put(key, manager);
            
            logger.info("create new tedis manager (appname" + appName + ")");
        }
        return manager;
    }
	
	/**
	 * 销毁连接
	 
	public static synchronized void destroy(){
	    String key = appName + "-" + version;
	    TedisManager manager = managers.get(key);
	    if(manager != null){
	        manager.destroy();
	        logger.info("destroy current tedis manager (appname" + appName + ")");
	    }
	}*/
	
	/**
	 * 操作普通key-value入口
	 * @return
	 */
	private ValueCommands getValueCommands(){
	    return create().getValueCommands();
	}
	
	/**
	 * 通过string命令缓存单个对象json数据
	 * @param key
	 * @param value
	 */
	public void tedisSetString(String key, String value){
	    getValueCommands().set(nameSpace, key, value);
	}
	
	/**
	 * 带过期时间的设置
	 * @param key
	 * @param value
	 * @param expire 时间
	 * @param timeUnit 时间单位
	 */
	public void tedisSetString(String key, String value, long expire, TimeUnit timeUnit){
	    getValueCommands().set(nameSpace, key, value, expire, timeUnit);
	}
	
	/**
	 * 通过key返回缓存中的单个对象json数据
	 * @param key     
	 */
	public String tedisGetString(String key){
		String objectJson = getValueCommands().get(nameSpace, key); 
		
		return objectJson; 
	}
	
	/**
	 * 获得List类型操作命令工具
	 * @return
	 */
	public ListCommands getListCommands(){
		return create().getListCommands();
	}
	
	/**
	 * 获得Set类型操作命令工具
	 * @return
	 */
	public SetCommands getSetCommands(){
	    return create().getSetCommands();
	}
	
	/**
	 * 获得Hash类型操作命令工具
	 * @return
	 */
	public HashCommands getHashCommands(){
	    return create().getHashCommands();
	}
	
	/**
	 * 获得Zset有序Set类型操作命令
	 * @return
	 */
	public ZSetCommands getZSetCommands(){
	    return create().getZSetCommands();
	}
	
	/**
	 * 获得计数器操纵命令
	 * @return
	 */
	public AtomicCommands  getAtomicCommands(){
	    return create().getAtomicCommands();
	}
	
	/**
	 * 通过key删除缓存对象
	 * @param key
	 */
	public void tedisDelObj(String key){
		create().delete(nameSpace, key);
	}
	
	/**
	 * 通过类名称模糊查询所有该类下的缓存进行删除
	 * @param className
	 */
	public void batchDel(String className){
		Set<String> keys = create().keys(nameSpace, "*" + className + "*");
		for(String key : keys){
		    create().delete(nameSpace, key);
		}
	}
	
	/**
	 * 根据缓存key查看缓存数据是否存在
	 * @param key
	 * @return
	 */
	public boolean isExists(String key){
		return create().hasKey(nameSpace, key);
	}

    public int getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(int nameSpace) {
        this.nameSpace = nameSpace;
    }

    public PropertiesConfigManager getPropertiesConfigManager() {
        return propertiesConfigManager;
    }

    public void setPropertiesConfigManager(PropertiesConfigManager propertiesConfigManager) {
        this.propertiesConfigManager = propertiesConfigManager;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static ConcurrentHashMap<String, TedisManager> getManagers() {
        return managers;
    }

    public static void setManagers(ConcurrentHashMap<String, TedisManager> managers) {
        TedisUtil.managers = managers;
    }
	
}
