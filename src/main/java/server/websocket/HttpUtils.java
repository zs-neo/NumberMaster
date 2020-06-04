/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:14
 */
public class HttpUtils {
	
	private final static String appId = "wx31f786831ddf95fc";
	private final static String appSecret = "25760b74b30baac60992ca4793cc46f4";
	
	public static String getOpenid(String code) {
		String urlPath = "https://api.weixin.qq.com/sns/jscode2session?appid=wx31f786831ddf95fc&secret=25760b74b30baac60992ca4793cc46f4&js_code=" + code + "&grant_type=authorization_code";
		BufferedReader in = null;
		HttpURLConnection connection = null;
		try {
			// 1.URL类封装了大量复杂的实现细节，这里将一个字符串构造成一个URL对象
			URL url = new URL(urlPath);
			System.out.println(url.toString());
			// 2.获取HttpURRLConnection对象
			connection = (HttpURLConnection) url.openConnection();
			// 3.调用connect方法连接远程资源
			connection.connect();
			// 4.访问资源数据，使用getInputStream方法获取一个输入流用以读取信息
			in = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "UTF-8"));
			
			// 对数据进行访问
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = in.readLine()) != null) {
				stringBuilder.append(line);
			}
			
			// 打印获取的结果
			System.out.println(stringBuilder.toString());
			return stringBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			// 关闭链接
			connection.disconnect();
		}
		return "";
	}
	
}
