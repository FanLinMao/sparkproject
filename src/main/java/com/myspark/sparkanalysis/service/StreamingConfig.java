package com.myspark.sparkanalysis.service;
/**
 * @date: 2019/07/05
 * @author: 范林茂
 */

import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class StreamingConfig implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	
	private transient JavaSparkContext javaSparkContext; 
	private transient JavaStreamingContext streamingContext = null;
	
	
    public StreamingConfig(@Autowired JavaSparkContext javaSparkContext) {
    	this.javaSparkContext = javaSparkContext;
    }
 


	/**
     * 开启Stream任务
     * @param str
     * @param listenerDirectory 要监听的文件夹
     */
    public void startStreamTask(StreamingConsumer server, String listenerDirectory) {
    	streamingContext = new JavaStreamingContext(javaSparkContext, Durations.seconds(20));
    	JavaDStream<String> lines = streamingContext.textFileStream(listenerDirectory);
        //提取rating
		lines.map(line -> line.split(",")[2])
				.foreachRDD(rdd -> {
					List<String> collect = rdd.collect();
					for (String d : collect) {
						server.sendMessageToCient(d);
					}
					//rdd.saveAsTextFile("");
				});
		
		streamingContext.start();
		try {
			streamingContext.awaitTermination();
			streamingContext.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    /**
     * 手动关闭Stream
     */
    public void destroyStreamTask() {
    	if(streamingContext != null) {
    		streamingContext.stop();
		}	
    }



	
	
    
    

}
