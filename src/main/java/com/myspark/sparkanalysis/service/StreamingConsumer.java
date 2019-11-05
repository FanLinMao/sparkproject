package com.myspark.sparkanalysis.service;
/**
 * @date: 2019/07/05
 * @author: 范林茂
 */
@FunctionalInterface
public interface StreamingConsumer {
	
	public void sendMessageToCient(String message);

}
