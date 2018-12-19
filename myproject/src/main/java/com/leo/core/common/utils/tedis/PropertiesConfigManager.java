package com.leo.core.common.utils.tedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.leo.core.common.utils.ConfigHelper;
import com.taobao.common.tedis.TedisConnectionException;
import com.taobao.common.tedis.config.ConfigManager;
import com.taobao.common.tedis.config.HAConfig;
import com.taobao.common.tedis.config.Router;
import com.taobao.common.tedis.config.HAConfig.ServerInfo;
import com.taobao.common.tedis.config.HAConfig.ServerProperties;
import com.taobao.common.tedis.group.MSRandomRouter;
import com.taobao.common.tedis.group.RandomRouter;

/**
 * 资源文件方式配置管理Tedis模式
 * @date 2013-10-23 下午05:28:57
 * @author leo
 * version 1
 */
public class PropertiesConfigManager implements ConfigManager {

	private static Logger logger = LoggerFactory.getLogger(PropertiesConfigManager.class);
	volatile Router router;
	int timeout = 3000;

	// 保存所有的配置
	public volatile HAConfig haConfig;
	Executor e = Executors.newSingleThreadExecutor();

	public PropertiesConfigManager(String defaultConfigFile, String configKey) {
		init(defaultConfigFile, configKey);
	}

	private void init(String defaultConfigFile, String configKey) {
//		ResourceBundle resource = ResourceBundle.getBundle(defaultConfigFile, Locale.getDefault());
		
		String configInfo = ConfigHelper.getInstance().getValue(configKey);

		this.haConfig = parseConfig(configInfo);
		if (this.haConfig.password != null) {
			for (ServerProperties sp : haConfig.groups) {
				sp.password = this.haConfig.password;
			}
		}
		
		try{
			if (haConfig.ms) {
				router = new MSRandomRouter(haConfig.groups, haConfig.failover);
			} else {
				router = new RandomRouter(haConfig.groups, haConfig.failover);
			}
		}catch(TedisConnectionException e){
			logger.error(e.getMessage());
		}
	}

	public void destroy() {
	}

	public static HAConfig parseConfig(String configString) {
		HAConfig config = new HAConfig();
		
		// timeout
		Pattern p_timeout = Pattern.compile("timeout=([\\s\\S]+?);");
		Matcher m_timeout = p_timeout.matcher(configString);
		if (m_timeout.find()) {
			String s_timeout = m_timeout.group(1);
			logger.info("timeout=" + s_timeout);
			try {
				config.timeout = Integer.parseInt(s_timeout.trim());
			} catch (Exception ex) {
				logger.error("timeout解析错误:", ex);
			}
		}
		
		// pool_size
		Pattern p_pool_size = Pattern.compile("pool_size=([\\s\\S]+?);");
		Matcher m_pool_size = p_pool_size.matcher(configString);
		if (m_pool_size.find()) {
			String s_pool_size = m_pool_size.group(1);
			logger.info("pool_size=" + s_pool_size);
			try {
				config.pool_size = Integer.parseInt(s_pool_size.trim());
			} catch (Exception ex) {
				logger.error("pool_size解析错误:", ex);
			}
		}

		// password
		Pattern p_password = Pattern.compile("password=([\\s\\S]+?);");
		Matcher m_password = p_password.matcher(configString);
		if (m_password.find()) {
			String s_password = m_password.group(1);
			logger.info("password=" + s_password);
			try {
				config.password = s_password.trim();
			} catch (Exception ex) {
				logger.error("password解析错误:", ex);
			}
		}

		// servers
		Pattern p = Pattern.compile("servers=([\\s\\S]+?);",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(configString);
		if (m.find()) {
			String s_servers = m.group(1);
			logger.info("servers=" + s_servers);
			String[] array = s_servers.trim().split(",");
			List<ServerProperties> servers = new ArrayList<ServerProperties>();
			for (String s : array) {
				ServerProperties sp = new ServerProperties();
				sp.server = new ServerInfo();
				String[] ss = s.split(":");
				if (ss.length >= 2) {
					sp.server.addr = ss[0];
					sp.server.port = Integer.parseInt(ss[1]);
					sp.pool_size = config.pool_size;
					sp.timeout = config.timeout;
					sp.password = config.password;
					if (ss.length == 3) {
						sp.readWeight = Integer.parseInt(ss[2].toLowerCase()
								.replace("r", "").trim());
					}
				} else {
					logger.error("配置错误:" + s);
				}
				servers.add(sp);
			}
			config.groups = servers;
		} else {
			logger.error("servers配置解析不到:" + configString);
		}
		
		// fail over开关
		Pattern p_failover = Pattern.compile("failover=([\\s\\S]+?);",
				Pattern.CASE_INSENSITIVE);
		Matcher m_failover = p_failover.matcher(configString);
		if (m_failover.find()) {
			try {
				String s_failover = m.group(1);
				config.failover = Boolean.parseBoolean(s_failover.trim());
			} catch (Throwable t) {
				logger.error("failover开关解析出错", t);
			}
		}
		return config;
	}

	public Router getRouter() {
		return router;
	}
}
