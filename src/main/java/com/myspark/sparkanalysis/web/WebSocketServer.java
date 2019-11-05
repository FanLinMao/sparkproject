package com.myspark.sparkanalysis.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.myspark.sparkanalysis.service.StreamingConfig;

/**
 * @date: 2019/07/05
 * @author: 范林茂
 */
@ServerEndpoint("/websocket/getStreamingAnalysis")
@Controller
public class WebSocketServer implements Serializable{
	
	/*
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
    private Session session;
    
    /*
     * 保证线程安全，用来存放每个客户端对应的WebSocketServer对象。
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();
    
    
    /*
     * 提供一个spring context上下文(解决方案)
     */
    private static ApplicationContext applicationContext;
    
    /*
     * 要注入的StreamingConfig对象
     */
    private static  StreamingConfig streamingConfig;
    
    
    public static void setApplicationContext(ApplicationContext applicationContext) {
    	WebSocketServer.applicationContext = applicationContext;
    }

    

    
    
    
    /**
     * 连接建立成功调用的方法wq
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        
        if(streamingConfig == null) {
        	//通过spring上下文获取StreamingConfig对象
        	streamingConfig = applicationContext.getBean(StreamingConfig.class);
        	//指定要监听的文件夹
        	String listenerDirectory = "hdfs://192.168.233.128:9000/flm";
        	//开始执行任务
        	streamingConfig.startStreamTask(message -> {
        		try {
        			
        			for (WebSocketServer ws : webSocketSet) {
        				//停留1秒再发送
        				Thread.sleep(1000);
        				session.getBasicRemote().sendText(message);
					}
        			
        		} catch (IOException e) {
        			e.printStackTrace();
        		}catch(InterruptedException e) {
        			e.printStackTrace();
        		}
        	}, listenerDirectory);
        }
       
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
    	//config.destroyStreamTask();
    	webSocketSet.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
       
        
    }

	/**
	 * 
	 * @param session
	 * @param error
	 */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
	/**
	 * 实现服务器主动推送
	 */
    public void sendMessage(String message) throws Exception {
        
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws Exception {
        
    }
}
