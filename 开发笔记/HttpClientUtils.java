package com.lenovo.xdash.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lizhang1
 */
public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static class LazyHolder {
        private static final HttpClientUtil INSTANCE = new HttpClientUtil();
    }

    public static HttpClientUtil getInstance() {
        return LazyHolder.INSTANCE;
    }

    private HttpClientUtil() {
    }

    private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(60000)
            .setConnectTimeout(60000)
            .setConnectionRequestTimeout(60000)
            .build();

    /**
     * 发送 post请求
     *
     * @param url 地址
     */
    public String sendHttpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl 地址
     * @param maps    参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps, Header header) {
        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.setHeader(header);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            logger.error("http post failed.", e);
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送Post请求
     *
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            httpClient = HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            logger.error("http post failed.", e);
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error("http post failed.", e);
            }
        }
        return responseContent;
    }

    /**
     * 发送 get请求
     *
     * @param httpUrl
     */
    public String sendHttpGet(String httpUrl, Header header) {
        HttpGet httpGet = new HttpGet(httpUrl);
        httpGet.setHeader(header);
        httpGet.setConfig(requestConfig);
        return sendHttpGet(httpGet);
    }

    /**
     * 发送Get请求
     *
     * @param httpGet
     * @return
     */
    private String sendHttpGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            httpClient = HttpClients.createDefault();
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error("http get failed.", e);
            }
        }
        return responseContent;
    }
}
