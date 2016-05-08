package net.flyingfat.page;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class Flush {
	
	
		private static Config config=new Config();;
		
		private static Logger logger=LoggerFactory.getLogger(Flush.class);
		
		private static volatile Gson gson=new Gson();
	
	    static{
	    	Properties pro=new Properties();
	    	try {
				pro.load(Flush.class.getResourceAsStream("/config.properties"));
				config.setFirstUrl(pro.getProperty("first.url"));
				config.setSecondUrl(pro.getProperty("second.url"));
				config.setFirstCount(Integer.parseInt(pro.getProperty("first.count")));
				config.setSecondCount(Integer.parseInt(pro.getProperty("second.count")));
				config.setSleepTime(Integer.parseInt(pro.getProperty("sleep.time")));
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

		public static void main(String[] args) {
				try {
					int interval=config.getFirstCount()/config.getSecondCount();
					for(int i=0;i<config.getFirstCount();i++){
						
						String userAgent=UserAgentUtil.getRandomUserAgent();
						sendReq(config.getFirstUrl(),userAgent);
						logger.info("start first req, current i {}",i);
						if(i%interval==0){
							sendSecReq(config.getSecondUrl(),userAgent);
							logger.info("start second req, current i {}",i);
						}
						Random random=new Random();
						int rand=random.nextInt(config.getSleepTime());
						logger.info("sleep time {}, current i {}",rand,i);
						Thread.sleep(rand);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		
		public static void sendReq(String url,String userAgent){
			
			InputStream input = null;
			ByteArrayOutputStream out = null;
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			try {
				
				get.addHeader("Accept-Language", "zh-CN");
				get.addHeader("Accept-Charset", "utf-8, iso-8859-1, utf-16, *;q=0.7");
				get.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				get.setHeader("user-agent", userAgent);
				get.addHeader("Accept-Encoding","gzip");
				//get.addHeader("Connection","keep-alive");
				//get.addHeader("Host","117.135.137.190:9098");
				
				HttpResponse resp=client.execute(get);
				if(resp.getStatusLine().getStatusCode()==200){
					/*HttpEntity content = resp.getEntity();
					input = content.getContent();
					out = new ByteArrayOutputStream();
					byte by[] = new byte[1024];
					int len = 0;
					while ((len = input.read(by)) != -1) {
						out.write(by, 0, len);
					}*/
					logger.info("resp size {}",resp.getEntity().getContent().available());
				}else{
					logger.info("访问网页异常");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(out);
				client.getConnectionManager().shutdown();
			}
		}
		
		
		public static void sendSecReq(String url,String userAgent){
			
			InputStream input = null;
			ByteArrayOutputStream out = null;
			HttpClient client = new DefaultHttpClient();
			Random random=new Random();
			url=url+random.nextFloat();
			HttpGet get = new HttpGet(url);
			try {
				
				get.addHeader("Accept-Language", "zh-CN");
				get.addHeader("Accept-Charset", "utf-8, iso-8859-1, utf-16, *;q=0.7");
				get.addHeader("Origin", "http://121.40.51.121:8080");
				get.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
				get.setHeader("user-agent", userAgent);
				//get.addHeader("Connection","keep-alive");
				//get.addHeader("Host","117.135.137.190:9098");
				
				HttpResponse resp=client.execute(get);
				if(resp.getStatusLine().getStatusCode()==200){
					HttpEntity content = resp.getEntity();
					input = content.getContent();
					out = new ByteArrayOutputStream();
					byte by[] = new byte[1024];
					int len = 0;
					while ((len = input.read(by)) != -1) {
						out.write(by, 0, len);
					}
					String json=new String(out.toByteArray());
					BaiduAdsDomain domain=gson.fromJson(json,BaiduAdsDomain.class);
					logger.info("resp click url {}",domain.getClickUrl());
					
				}else{
					logger.info("访问网页异常");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(out);
				client.getConnectionManager().shutdown();
			}
		}

		public Config getConfig() {
			return config;
		}

		public void setConfig(Config config) {
			this.config = config;
		}
		
		
		
}
