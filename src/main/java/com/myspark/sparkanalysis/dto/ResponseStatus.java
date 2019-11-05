package com.myspark.sparkanalysis.dto;
/**
 * @date: 2019/06/28
 * @author: 范林茂
 */
public class ResponseStatus {
	
	/*
	 * 状态码: 200成功|500错误|404找不到
	 */
	private String status;
	/*
	 * 提示消息 
	 */
	private String message;
	/*
	 * 存放的数据 
	 */
	private Object data;
	
	
	
	public ResponseStatus() {
		
	}
	public ResponseStatus(String status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseStatus [status=" + status + ", message=" + message + ", data=" + data + "]";
	}
	
	

}
