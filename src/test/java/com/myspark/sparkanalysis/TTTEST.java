/*package com.myspark.sparkanalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

*//**
 * @date: 2019/07/05
 * @author: 范林茂
 *//*
public class TTTEST {
	
		
	   private static Map<String,Double> map = new HashMap<String,Double>();
	   public static void main(String[] args) throws InterruptedException {  
	         
	       final SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingOnHDFS");  
	       System.setProperty("hadoop.home.dir", "F:/hadoop-2.7.4");
	       System.setProperty("HADOOP_USER_NAME", "root"); 
	       
	       JavaStreamingContext jsc = new JavaStreamingContext(conf,Durations.seconds(30));  
	       
	       JavaDStream<String> lines = jsc.textFileStream("hdfs://192.168.233.128:9000/flm");
	       lines.foreachRDD(rdd->{
				rdd.foreach(d->{
					double rating = Double.parseDouble(d.split(",")[2]);
					map.put("rating", rating);
				});
			});
     
	        //wordsCount.print();  
	        //wordsCount.dstream().saveAsTextFiles("hdfs://192.168.233.128:9000/flm", "txt");
	        if(map  != null) {
	        	Double num = map.get("rating");
	        	System.out.println("rating:"+num);
	        }
	        
	        jsc.start();  
	  
	        jsc.awaitTermination();  
	        jsc.close();  
	  
	    }  
	  
}
*/