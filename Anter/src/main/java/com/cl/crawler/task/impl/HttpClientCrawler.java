package com.cl.crawler.task.impl;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;

import com.cl.crawler.task.HttpCrawlerTaskModel;
import com.cl.httpclient4.entity.ProxyParam;
import com.cl.httpclient4.util.HttpClientFactory;

/**
 * 使用HttpClient工具执行http请求的采集任务模型
 * 
 * @author chenlei
 *
 */
public class HttpClientCrawler extends HttpCrawlerTaskModel {
	
	private static final Log logger = LogFactory.getLog(HttpClientCrawler.class);

	/** HttpClient的执行http请求的实例 */
	protected CloseableHttpClient client;

	/**
	 * HttpClientCrawler构造器
	 */
	public HttpClientCrawler() {
		super();
	}

	/**
	 * 任务的执行方法
	 */
	@Override
	public void excute() {
		logger.info(" 当前任务 " + getTaskId() + " 的采集对象: " + domain);
		// 请求构建
		HttpRequestBase request = newMethod();
		String repStr = null;
		// 执行http请求
		try {
			repStr = HttpClientFactory.doexcute4str(getClient(), request,
					new BasicHttpContext());
		} catch (ClientProtocolException e) {
			logger.error(" id=" + getTaskId() + " : ", e);
		} catch (IOException e) {
			logger.error(" id=" + getTaskId() + " : ", e);
		} finally {
			try {
				HttpClientFactory.shutdown(null, request, null, null, null);
			} catch (IOException e) {
				logger.error(" id=" + getTaskId() + " : ", e);
			} finally {
				if (repStr == null) {
					setStatusCode(TASK_FAILED);
					checkCode.set(CHECK_BLANK);
				} else {
					doAfter(repStr);
				}
				logger.info("纪录任务的结束状态 : " + domain + "; id=" + getTaskId()
						+ " ;statusCode=" + getStatusCode());
			}

		}

	}

	/**
	 * 新建请求method方法
	 * 
	 * @return
	 */
	protected HttpRequestBase newMethod() {
		ProxyParam proxyParam = null;
		if (isProxyNeed) {
			proxyParam = getProxy(getUrl(), getQueryParams(), method);
		}
		// 境外请求带getHeaders()会抛异常
		return HttpClientFactory.getRequest(getUrl(), this.method,
				getHeaders(), getQueryParams(), 15000, proxyParam);
	}

	/**
	 * 返回client实例
	 * 
	 * @return
	 */
	public CloseableHttpClient getClient() {
		if (client == null) {
			throw new NullPointerException(" client can not be null... ");
		}
		return client;
	}

	/**
	 * 注入client实例
	 * 
	 * @param client
	 */
	public void setClient(CloseableHttpClient client) {
		this.client = client;
	}

}
