package com.clark.daxian.wechat.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * <b>  </b>
 * <p>
 * 功能描述:http请求工具类
 * </p>
 * <p/>
 * @author 朱维
 * @date 2017年9月12日
 * @time 下午1:41:25
 * @Path: com.cti.util.HTTPUtil
 */
@SuppressWarnings("all")
public class HTTPUtil {
	
	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static SSLContextBuilder builder = null;
	private static PoolingHttpClientConnectionManager cm = null;
	private static SSLConnectionSocketFactory sslsf = null;
    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * 发送POST请求查询物流情况
	 * @param url
	 * @param orderNum
	 */
	public static JSONObject doPost(String url, String sid, String token, String appid, String templateid, String param, String mobile){
		HttpPost post = null;
		try{
			HttpClient httpClient = getHttpClient();
	        post = new HttpPost(url);
	        // 设置超时时间
 			RequestConfig requestConfig = RequestConfig.custom()
 	                .setConnectTimeout(50000).setConnectionRequestTimeout(10000)
 	                .setSocketTimeout(50000).build();
 			post.setConfig(requestConfig);
	        // 构造消息头
	        post.setHeader("Content-type", "application/json; charset=utf-8");
	        post.setHeader("Connection", "Close");
	        JSONObject jsonObj = new JSONObject();
	        jsonObj.put("sid", sid);
	        jsonObj.put("token", token);
	        jsonObj.put("appid", appid);
	        jsonObj.put("templateid", templateid);
	        jsonObj.put("param", param);
	        jsonObj.put("mobile", mobile);
	        // 构建消息实体
	        StringEntity entity = new StringEntity(jsonObj.toString(), Charset.forName("UTF-8"));
	        entity.setContentEncoding("UTF-8");
	        // 发送Json格式的数据请求
	        entity.setContentType("application/json");
	        post.setEntity(entity);
	            
	        HttpResponse response = httpClient.execute(post);
	        // 检验返回码
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != HttpStatus.SC_OK){
//	        	System.out.println("请求出错"+statusCode);
	        }else{
	        	HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    String result = EntityUtils.toString(resEntity,"UTF-8"); 
                    return JSONObject.parseObject(result);
                } 
	        }
		} catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	        if(post != null){
	            try {
	                post.releaseConnection();
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		return null;
	}
	/**
	 * 公共Post传参请求
	 * @param url
	 * @param orderNum
	 */
	public static JSONObject publicdoPost(String url,JSONObject param){
		HttpPost post = null;
		try{
			HttpClient httpClient = getHttpClient();
	        post = new HttpPost(url);
	        // 设置超时时间
 			RequestConfig requestConfig = RequestConfig.custom()
 	                .setConnectTimeout(50000).setConnectionRequestTimeout(10000)
 	                .setSocketTimeout(50000).build();
 			post.setConfig(requestConfig);
	        // 构造消息头
	        post.setHeader("Content-type", "application/json; charset=utf-8");
	        post.setHeader("Connection", "Close");
	        // 构建消息实体
	        StringEntity entity = new StringEntity(param.toString(), Charset.forName("UTF-8"));
	        entity.setContentEncoding("UTF-8");
	        // 发送Json格式的数据请求
	        entity.setContentType("application/json");
	        post.setEntity(entity);
//	        System.out.println(post);
//	        Header[] heads = post.getAllHeaders();
//	        StringBuffer sb = new StringBuffer();
//			for (Header header : heads) {
//				sb.append(header.getName()).append(" : ").append(header.getValue()).append("\n");
//			}
//			System.out.println(sb.toString());
	        HttpResponse response = httpClient.execute(post);
	        // 检验返回码
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode==302){
	        	String locationUrl=response.getLastHeader("Location").getValue();  
	        	post = new HttpPost(locationUrl);
	        	post.setHeader("Content-Type", "application/json;charset=UTF-8");
	        	post.setEntity(entity);
	        	response = httpClient.execute(post);
	        	statusCode = response.getStatusLine().getStatusCode();
	        }
	        if(statusCode != HttpStatus.SC_OK){
	        	System.out.println("请求出错"+statusCode);
	        }else{
	        	HttpEntity resEntity = response.getEntity();  
	        	System.out.println(resEntity);
                if(resEntity != null){  
                    String result = EntityUtils.toString(resEntity,"UTF-8");
                    return JSONObject.parseObject(result);
                } 
	        }
		} catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	        if(post != null){
	            try {
	                post.releaseConnection();
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		return null;
	}
	
	public static JSONObject CAASPost(String url,JSONObject param,String authon){
		HttpPost post = null;
		try{
			HttpClient httpClient = getHttpClient();
	        post = new HttpPost(url);
	        // 设置超时时间
 			RequestConfig requestConfig = RequestConfig.custom()
 	                .setConnectTimeout(50000).setConnectionRequestTimeout(10000)
 	                .setSocketTimeout(50000).build();
 			post.setConfig(requestConfig);
	        // 构造消息头
	        post.setHeader("Content-type", "application/json; charset=utf-8");
	        post.setHeader("Accept", "application/json");
	        post.setHeader("Authorization", authon);
	        // 构建消息实体
	        StringEntity entity = new StringEntity(param.toString(), Charset.forName("UTF-8"));
	        entity.setContentEncoding("UTF-8");
	        // 发送Json格式的数据请求
	        entity.setContentType("application/json");
	        post.setEntity(entity);
//	        System.out.println(post);
	        Header[] heads = post.getAllHeaders();
	        StringBuffer sb = new StringBuffer();
			for (Header header : heads) {
				sb.append(header.getName()).append(" : ").append(header.getValue()).append("\n");
			}
//			System.out.println(sb.toString());
//			System.out.println(post.getEntity());
	        HttpResponse response = httpClient.execute(post);
	        // 检验返回码
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != HttpStatus.SC_OK){
//	        	System.out.println("请求出错"+statusCode);
	        }else{
	        	HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    String result = EntityUtils.toString(resEntity,"UTF-8");
                    return JSONObject.parseObject(result);
                } 
	        }
		} catch (Exception e) {
	        e.printStackTrace();
	    }finally{
	        if(post != null){
	            try {
	                post.releaseConnection();
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		return null;
	}
	/**
	 * 发送POST请求
	 * @param url
	 * @param orderNum
	 */
	public static JSONObject publicPost(String url){
		HttpPost post = null;
		try{
			HttpClient httpClient = getHttpClient();
			post = new HttpPost(url);
			// 设置超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(50000).setConnectionRequestTimeout(10000)
					.setSocketTimeout(50000).build();
			post.setConfig(requestConfig);
			// 构造消息头
			post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("Connection", "Close");
			
			HttpResponse response = httpClient.execute(post);
			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
//				System.out.println("请求出错"+statusCode);
			}else{
				HttpEntity resEntity = response.getEntity();  
				if(resEntity != null){  
					String result = EntityUtils.toString(resEntity,"UTF-8"); 
					return JSONObject.parseObject(result);
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(post != null){
				try {
					post.releaseConnection();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * 发送GET请求
	 * @param url
	 * @param orderNum
	 */
	public static JSONObject publicGET(String url){
		HttpGet post = null;
		try{
			HttpClient httpClient = getHttpClient();
			post = new HttpGet(url);
			// 设置超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(50000).setConnectionRequestTimeout(10000)
					.setSocketTimeout(50000).build();
			post.setConfig(requestConfig);
			// 构造消息头
			post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("Connection", "Close");
			
			HttpResponse response = httpClient.execute(post);
			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
//				System.out.println("请求出错"+statusCode);
			}else{
				HttpEntity resEntity = response.getEntity();  
				if(resEntity != null){  
					String result = EntityUtils.toString(resEntity,"UTF-8"); 
					return JSONObject.parseObject(result);
				} 
			}
		} catch (Exception e) {
			
		}finally{
			if(post != null){
				try {
					post.releaseConnection();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 获取请求
	 * @return
	 * @throws Exception
	 */
	public static CloseableHttpClient getHttpClient() throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
        return httpClient;
    }
	
	public static String getWebcontent(String webUrl, String charest) {
        if (StringUtils.isEmpty(webUrl)) {
            return null;
        }
        int response = -1;
        HttpURLConnection conn = null;
        try {            
            URL url = new URL(webUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(60 * 2000);
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            conn.setDoOutput(true);
            conn.connect();
            response = conn.getResponseCode();
            if (response == 200) {
                InputStream im = null;                
                try {
                    im = conn.getInputStream();
                    return readInputStream(im, charest);
                } finally {
                    IOUtils.closeQuietly(im);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            if(conn != null) {
                conn.disconnect();
                conn = null;
            }
        } 
    }
    private static String readInputStream(InputStream instream, String charest) throws Exception {
        StringBuilder sb = new StringBuilder();
        try(
                InputStreamReader isr = new InputStreamReader(instream, charest);
                BufferedReader reader = new BufferedReader(isr);) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
        
    } 
	/**
	 * 发送POST请求
	 * @param downloadUrl 下载文件路径
	 * @return
	 */
	public static synchronized byte[] uploadTape(String downloadUrl){
		HttpPost post = null;
		byte[] byteArray = null;
		try{
			HttpClient httpClient = getHttpClient();
			post = new HttpPost(downloadUrl);
			// 设置超时时间
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(50000).setConnectionRequestTimeout(10000)
					.setSocketTimeout(50000).build();
			post.setConfig(requestConfig);
			// 构造消息头
			post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("Connection", "Close");
			
			HttpResponse response = httpClient.execute(post);
			// 检验返回码
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != HttpStatus.SC_OK){
				throw new RuntimeException("请求出错"+statusCode);
			}else{
				HttpEntity resEntity = response.getEntity();  
				if(resEntity != null){  
					InputStream is = resEntity.getContent();
					if(is!=null){
						Header[] contentHead = response.getHeaders("Content-Disposition");
						if(contentHead!=null&&contentHead.length>0){
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							byte[] buffer = new byte[4096];
							int r = 0;
							while ((r = is.read(buffer)) > 0) {
								output.write(buffer, 0, r);
							}
							byteArray = output.toByteArray();
							output.flush();
							output.close();
							EntityUtils.consume(resEntity);
						}else{
							String result = EntityUtils.toString(resEntity,"UTF-8"); 
							JSONObject json = JSONObject.parseObject(result);
							if(!"success".equals(json.getString("result"))) {
                                throw new RuntimeException("请求出错" + json.getString("msg"));
                            }
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}finally{
			if(post != null){
				try {
					post.releaseConnection();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getMessage());
				}
			}
		}
		return byteArray;
	}
}
