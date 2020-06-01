/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;

import sun.security.ssl.Debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:14
 */
public class HttpUtils {
	
	public static String get(String url) {
		String result = "";
		BufferedReader in = null;
		
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = in.readLine()) != null) result += line;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		Debug.println("result=----", result);
		return result;
	}
	
}
