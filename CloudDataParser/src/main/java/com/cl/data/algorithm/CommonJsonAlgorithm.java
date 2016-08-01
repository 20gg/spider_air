package com.cl.data.algorithm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

/**
 * 通用json节点抽取算法实现
 * 
 * @author chenlei
 *
 */
public class CommonJsonAlgorithm implements Cloneable {

	public CommonJsonAlgorithm() {
		super();
	}

	@SuppressWarnings("unchecked")
	public Object computeTargetNode(Object root, String targetNodeName) {
		Object targetNode = null;
		if (root == null || StringUtils.isEmpty(targetNodeName)) {
			return targetNode;
		} else if (root instanceof Map<?, ?>) {
			targetNode = computeTargetNode((Map<String, Object>) root, targetNodeName);
		} else if (root instanceof List<?>) {
			targetNode = computeTargetNode((List<?>) root, targetNodeName);
		} else {
			targetNode = null;
		}
		return targetNode;

	}

	/**
	 * 计算目标节点value
	 * 
	 * @param rootMap
	 *            json根map
	 * @param targetNodeName
	 *            目标节点名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object computeTargetNode(Map<String, Object> rootMap,
			String targetNodeName) {

		// 迭代算法:
		Object targetNode = null;
		if (rootMap == null || rootMap.isEmpty() || StringUtils.isEmpty(targetNodeName)) {
			return targetNode;
		}
		targetNode = rootMap.get(targetNodeName);
		if (targetNode != null) {
			return targetNode;
		} else {
			Set<Entry<String, Object>> rootSet = rootMap.entrySet();
			Iterator<Entry<String, Object>> rootSetIte = rootSet.iterator();
			while (rootSetIte.hasNext()) {
				Entry<String, Object> entry = rootSetIte.next();
				Object obj = entry.getValue();
				if (obj instanceof Map<?, ?>) {
					targetNode = computeTargetNode((Map<String, Object>) obj,
							targetNodeName);
				} else if (obj instanceof List<?>) {
					targetNode = computeTargetNode((List<?>) obj,
							targetNodeName);
				}
				if (targetNode != null) {
					return targetNode;
				}
			}
		}

		return targetNode;
	}

	/**
	 * 计算目标节点value
	 * 
	 * @param rootList
	 *            json根list
	 * @param targetNodeName
	 *            目标节点名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object computeTargetNode(List<?> rootList, String targetNodeName) {

		// 迭代算法:
		Object targetNode = null;
		if (rootList == null || rootList.isEmpty() || StringUtils.isEmpty(targetNodeName)) {
			return targetNode;
		}
		Iterator<?> ite = rootList.iterator();
		while (ite.hasNext()) {
			Object obj = ite.next();
			if (obj instanceof Map) {
				targetNode = computeTargetNode((Map<String, Object>) obj,
						targetNodeName);
			} else if (obj instanceof List) {
				targetNode = computeTargetNode((List<?>) obj, targetNodeName);
			}
			if (targetNode != null) {
				return targetNode;
			}

		}

		return targetNode;
	}

}
