package com.web.page;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;





public class UserAgentUtil {
	
	
	public static String getRandomUserAgent(){
		List<String> list=getUserAgent();
		Random random=new Random();
		int rand=random.nextInt(list.size());
		return list.get(rand);
	}
	
	public static List<String> getUserAgent(){
		List<String> list=new ArrayList<String>();
		InputStreamReader input=null;
		try {
			input=new InputStreamReader(UserAgentUtil.class.getResourceAsStream("/userAgent.log"));
			list=IOUtils.readLines(input);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(input);
		}
		return list;
	}
	
	public static void convert(){
		
		InputStreamReader input=null;
		BufferedReader reader=null;
		OutputStreamWriter output=null;
		
		try {
			input=new InputStreamReader(UserAgentUtil.class.getResourceAsStream("/access.log"));
			reader=new BufferedReader(input);
			
			output=new OutputStreamWriter(new FileOutputStream(new File(UserAgentUtil.class.getResource("/userAgent.log").getPath())));
			List<String> lines=new ArrayList<String>();
			
			String line;
			while((line=reader.readLine())!=null){
				int len=0;
				if((len=line.indexOf("Dalvik"))!=-1){
					lines.add(line.substring(len,line.length()-1));
				}
			}
			
			IOUtils.writeLines(lines, null, output);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(output);
		}
	}
	
	public static void main(String args[]){
	
		
	}

}
