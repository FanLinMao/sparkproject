/*package com.myspark.sparkanalysis;

import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import com.myspark.sparkanalysis.service.JavaSparkSQLOperate;
import com.myspark.sparkanalysis.utils.SparkSqlUtils;

import scala.Tuple2;

*//**
 * @date: 2019/06/26
 * @author: 范林茂
 *//*
public class TestOne {
	
	
	private static String listenerFile = "F:/wordcount";
	private static JavaStreamingContext streamingContext = null;

	public static void main(String[] args) throws Exception {
		SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("JavaSparkStreamingTest");
		System.setProperty("hadoop.home.dir", "F:/hadoop-2.7.4");
		System.setProperty("HADOOP_USER_NAME", "root"); 
		streamingContext = new JavaStreamingContext(conf, Durations.seconds(10));
		
		JavaDStream<String> lines = streamingContext.textFileStream(listenerFile);
		JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split("\\s+")).iterator());
		JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);

		// Print the first ten elements of each RDD generated in this DStream to the console
		wordCounts.print();
		//提取rating
		JavaDStream<Double> map = lines.map(line ->{
			return Double.parseDouble(line.split(",")[2]);
		});
		map.print();
		streamingContext.start();
		streamingContext.awaitTermination();
	}
	
	
}
*/