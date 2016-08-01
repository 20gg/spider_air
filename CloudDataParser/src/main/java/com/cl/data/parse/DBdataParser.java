package com.cl.data.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cl.data.entity.db.DBData;
import com.cl.data.entity.ori.OriAirData;
import com.cl.data.entity.ori.OriAirData.Cabin;
import com.cl.httpclient4.util.ComUtil;

/**
 * 入库机票数据抽取器
 * 
 * @author chenlei
 *
 */
public abstract class DBdataParser extends DataParser {
	
	private final static Log logger = LogFactory.getLog(DBdataParser.class);

	/** 机票来源 */
	protected String source;
	/** json转map */
	protected Map<String, Object> rootMap;

	public DBdataParser() {
		super();
	}

	/**
	 * 从dataCheck抽取json转换后的rootMap集合
	 */
	@Override
	protected List<OriAirData> parse() {
		// 获取根map
		rootMap = dataCheck.getRootMap();
		return new LinkedList<OriAirData>();

	}

	/**
	 * 提取机票信息生成最低票价数据
	 */
	@Override
	protected void daAfter(List<OriAirData> oridatalist) {
		if (oridatalist.isEmpty()) {
			throw new NullPointerException(
					" extract oridatas is failed, please check why?...  ");
		} else {
			DBData dbData = new DBData();
			OriAirData[] oridatas = oridatalist
					.toArray(new OriAirData[oridatalist.size()]);
			for (int index = 0; index < oridatas.length; index++) {
				DBData dbDataCopy = null;
				try {
					dbDataCopy = dbData.clone();
				} catch (CloneNotSupportedException e) {
					logger.error("", e);
				}
				dbDataCopy.setBirthtime(System.currentTimeMillis());
				dbDataCopy.setDt(oridatas[index].getDepDate());
				dbDataCopy.setFlightNos(oridatas[index].getFlightNos());
				dbDataCopy.setFromAirport(oridatas[index].getDepCode());
				dbDataCopy.setSource(source);
				dbDataCopy.setToAirport(oridatas[index].getArrCode());
				List<Cabin> cabins = oridatas[index].getCabins();
				Integer loweastPrice = cabins.get(0).getPrice();
				String count = cabins.get(0).getCabinQuantity();
				Integer tax = cabins.get(0).getTax();
				for (Cabin cabin : cabins) {
					if (cabin.getPrice() < loweastPrice) {
						loweastPrice = cabin.getPrice();
						count = cabin.getCabinQuantity();
						tax = cabin.getTax();
					}
				}
				dbDataCopy.setLoweastPrice(loweastPrice);
				dbDataCopy.setTax(tax);
				dbDataCopy.setQuanty(count);
				export2file(dbDataCopy, DBData.class, index);
			}

		}

	}

	/**
	 * 数据以xml格式输出
	 * 
	 * @param obj
	 * @param clazz
	 * @param index
	 */
	protected void export2file(Object obj, Class<?> clazz, int index) {
		StringBuilder filepathSB = new StringBuilder(outputdir);
		filepathSB.append(taskId);
		filepathSB.append("_");
		filepathSB.append(index);
		filepathSB.append(".xml");
		OutputStream os = null;
		try {
			File file = new File(filepathSB.toString());
			if (!file.exists()) {
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			ComUtil.ToXml(obj, clazz, os);
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} catch (JAXBException e) {
			logger.error("", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}

	}

	/**
	 * spring注入
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}

}
