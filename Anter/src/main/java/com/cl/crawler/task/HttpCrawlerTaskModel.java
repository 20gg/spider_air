package com.cl.crawler.task;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cl.crawler.cookie.CookieManager;
import com.cl.crawler.extract.ExtractorManager;
import com.cl.httpclient4.entity.ProxyParam;
import com.cl.httpclient4.util.ComUtil;
import com.cl.pool.task.CrawlertaskInterface;
import com.cl.proxy.fetcher.util.FetcherHandel;

/**
 * 基于Http协议的采集任务模板类
 * 
 * @author chenlei
 *
 */
public abstract class HttpCrawlerTaskModel implements CrawlertaskInterface {

	private static final Log logger = LogFactory.getLog(HttpCrawlerTaskModel.class);

	/** 反馈结果:OK,无需执行任何反爬虫验证程序 */
	public static final int CHECK_OK = 100;
	/** 反馈结果:LOGON,需执行登录操作 */
	public static final int CHECK_LOGON = 200;
	/** 反馈结果:VERI,需执行验证码操作 */
	public static final int CHECK_VERI = 300;
	/** 反馈结果:BLANK,无响应数据;如果抛连接超时异常,可能被封IP了,也可能仅仅是网络异常 */
	public static final int CHECK_BLANK = 500;

	/** 任务id */
	protected final String taskId;
	/** 任务类型(境内"0"/境外"1"/通用"2") */
	protected String inter;
	/** 任务采集的网站域名 */
	protected String domain;
	/** 任务的状态码(默认0,等待;1,执行失败;2,执行成功) */
	protected AtomicInteger statusCode = new AtomicInteger(TASK_WAIT);
	/** 请求类型 */
	protected String method;
	/** 判断任务是否需要加代理(默认false:不加代理) */
	protected Boolean isProxyNeed;
	/** 请求url */
	protected String url;
	/** 查询参数map集(带%s是指参数可变) */
	protected Map<String, String> queryParams;
	/** 请求头map集 */
	protected Map<String, String> headers;
	/** 所需的结果数据 */
	protected String dataStr;
	/** 结果数据类型<json/html> */
	protected String dataType;
	/** 数据抽取器的springBeanName */
	protected String exporterBeanName;
	// 反--反爬虫机制
	/** 检测结果状态码 */
	protected AtomicInteger checkCode = new AtomicInteger(CHECK_OK);

	/**
	 * 采集任务构造器
	 */
	public HttpCrawlerTaskModel() {
		super();
		this.taskId = ComUtil.getTaskid();

	}

	/**
	 * 处理结果数据
	 */
	public void doAfter(final String repStr) {
		String datastr = repStr;
		if ("json".equals(dataType) && repStr.startsWith("(")) {
			datastr = StringUtils.substringBetween(repStr, "(", ")");
		}
		setDataStr(datastr);
		/**
		 * 执行成功,数据解析输出
		 */
		try {
			int code = ExtractorManager.extractData(datastr, getDomain(), getInter(), getTaskId());
			this.checkCode.set(code);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (checkCode.get() == CHECK_OK) {
				setStatusCode(TASK_SUCCESS);
			} else {
				setStatusCode(TASK_FAILED);
				if (verify(checkCode.get())) {
					logger.info(" we success... ");
				} else {
					logger.warn(
							" our verify method is Failure ... please check... / <checkCode is> " + checkCode.get());
				}
			}
		}

		if (getStatusCode() == TASK_FAILED) {
			// 任务执行失败,处理异常
			handlexception();
		}

	}

	/**
	 * 输出任务异常信息
	 */
	protected void handlexception() {
		// TODO 待实现

	}

	/**
	 * 执行验证程序,破解对应网站的反爬虫策略
	 * 
	 * @param checkCode
	 * 
	 * @return
	 */
	protected Boolean verify(int checkCode) {
		// TODO 待实现
		boolean verify = true;
		// if (checkCode == CHECK_LOGON) {
		// verify = LogonManager.longon(dataType, dataStr, domain);
		// } else if (checkCode == CHECK_VERI) {
		// verify = VerifyManager.verify(dataType, dataStr, domain);
		// }
		return verify;

	}

	/**
	 * 获取代理的方法
	 * 
	 * @param verUrl
	 * @param method
	 * @return
	 */
	protected ProxyParam getProxy(String verUrl, Map<String, String> paraMap, String method) {
		return FetcherHandel.getProxy(verUrl, method);

	}

	/**
	 * 返回请求头的map集合
	 * 
	 * @return
	 */
	public Map<String, String> getHeaders() {
		getCookie();
		return headers;

	}

	/**
	 * 获取cookie的方法
	 */
	protected Map<String, String> getCookie() {
		CookieManager.updateCookieHeader(headers, domain, inter);
		return Collections.emptyMap();
	}

	/**
	 * 返回任务执行结果数据
	 */
	@Override
	public String getDataStr() {
		return dataStr;
	}

	/**
	 * 返回任务的当前状态
	 */
	@Override
	public Integer getStatusCode() {
		return statusCode.get();
	}

	/**
	 * 返回任务的id
	 */
	@Override
	public String getTaskId() {
		return taskId;
	}

	/**
	 * 任务信息输出
	 */
	@Override
	public String toString(Integer code) {
		return ComUtil.toJson(this);
	}

	// =================================get/set=================================

	/**
	 * 返回任务类型(境内"0"/境外"1"/通用"2")
	 * 
	 * @return
	 */
	public String getInter() {
		return inter;
	}

	/**
	 * spring注入
	 * 
	 * @param inter
	 */
	public void setInter(String inter) {
		this.inter = inter;
	}

	/**
	 * 获取当前任务采集的对象(网站域名)
	 * 
	 * @return
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * spring注入
	 * 
	 * @param domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * 返回请求的类型Get/Post
	 * 
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * spring注入
	 * 
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 判断是否需要代理
	 * 
	 * @return
	 */
	public Boolean getIsProxyNeed() {
		return isProxyNeed;
	}

	/**
	 * spring注入
	 * 
	 * @param isProxyNeed
	 */
	public void setIsProxyNeed(Boolean isProxyNeed) {
		this.isProxyNeed = isProxyNeed;
	}

	/**
	 * 返回请求所需的url
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * spring注入
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 返回参数的map集合
	 * 
	 * @return
	 */
	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	/**
	 * 注入参数的map集合
	 * 
	 * @param queryParams
	 */
	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * 注入请求头的map集合
	 * 
	 * @param headers
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * 对象内部自主调度,设置任务状态码
	 * 
	 * @param statusCode
	 */
	protected void setStatusCode(Integer statusCode) {
		if (statusCode != null) {
			this.statusCode.set(statusCode);
		}
	}

	/**
	 * 获取当前数据的类型
	 * 
	 * @return json/html
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * spring注入
	 * 
	 * @param dataStr
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	/**
	 * 返回数据抽取器的springBeanName
	 * 
	 * @return
	 */
	public String getExporterBeanName() {
		return exporterBeanName;
	}

	/**
	 * spring注入
	 * 
	 * @param exporterBeanName
	 */
	public void setExporterBeanName(String exporterBeanName) {
		this.exporterBeanName = exporterBeanName;
	}

}
