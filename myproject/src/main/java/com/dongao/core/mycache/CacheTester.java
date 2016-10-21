package com.dongao.core.mycache;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.dongao.core.myutil.JedisTemplate;

/**
 * 缓存测试入口
 * @author zhangzhen
 */
public class CacheTester {
	public static void main(String[] args) {
		CacheChannel cache = CacheChannel.getInstance();
		JedisTemplate jedisTemplate = JedisTemplate.getInstance();
		jedisTemplate.set("efwefew3", "0");
		jedisTemplate.setnxex("efwefew34", "10", 10);
		jedisTemplate.incr("efwefew34");
		System.out.println(jedisTemplate.ttl("efwefew34"));
		jedisTemplate.expire("efwefew34", 44);
		jedisTemplate.incr("efwefew34");
		System.out.println(jedisTemplate.ttl("efwefew34"));
		System.out.println(jedisTemplate.incr("efwefew34"));
		
		
	}

	public static void main1(String[] args) {
		
		System.setProperty("java.net.preferIPv4Stack", "true"); //Disable IPv6 in JVM
		
		CacheChannel cache = CacheChannel.getInstance();
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));

	    do{
	        try {
	            System.out.print("> "); 
	            System.out.flush();
	            
	            String line=in.readLine().trim();
	            if(line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit"))
	                break;

	            String[] cmds = line.split(" ");
	            if("get".equalsIgnoreCase(cmds[0])){
	            	CacheObject obj = cache.get(cmds[1], cmds[2]);
	            	System.out.printf("[%s,%s,L%d]=>%s\n", obj.getRegion(), obj.getKey(), obj.getLevel(), obj.getValue());
	            }
	            else
	            if("set".equalsIgnoreCase(cmds[0])){
	            	cache.set(cmds[1], cmds[2],cmds[3]);
	            	System.out.printf("[%s,%s]<=%s\n",cmds[1], cmds[2], cmds[3]);
	            }
	            else
	            if("evict".equalsIgnoreCase(cmds[0])){
	            	cache.evict(cmds[1], cmds[2]);
	            	System.out.printf("[%s,%s]=>null\n",cmds[1], cmds[2]);
	            }
	            else
	            if("clear".equalsIgnoreCase(cmds[0])){
	            	cache.clear(cmds[1]);
	            	System.out.printf("Cache [%s] clear.\n" , cmds[1]);
	            }
	            else
	            if("help".equalsIgnoreCase(cmds[0])){
	            	printHelp();
	            }
	            else
	            if("keys".equalsIgnoreCase(cmds[0])){
	            	System.err.println(cache.keys(cmds[1]));
	            }
	            else{
	            	System.out.println("Unknown command.");
	            	printHelp();
	            }
	        }
	        catch(ArrayIndexOutOfBoundsException e) {
            	System.out.println("Wrong arguments.");
	        	printHelp();
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
	    }while(true);
	    
	    cache.close();
	    
	    System.exit(0);
	}
	
	private static void printHelp() {
		System.out.println("Usage: [cmd] region key [value]");
		System.out.println("cmd: get/set/evict/quit/exit/help");
	}

}
