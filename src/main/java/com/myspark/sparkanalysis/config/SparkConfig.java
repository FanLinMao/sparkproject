package com.myspark.sparkanalysis.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @date: 2019/07/06
 * @author: 范林茂
 */
@Configuration
public class SparkConfig {

	/**
	 * 创建SparkConf对象
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SparkConf.class)
	public SparkConf sparkConf() {
		SparkConf conf = new SparkConf();
		conf.setAppName("SparkConfig");
		conf.setMaster("local[*]");
		return conf;
	}
	
	/**
	 * 创建JavaSparkContext对象
	 * @param sparkconf
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(JavaSparkContext.class) //该注解默认： JVM 只允许存在一个SparkContext，同上同下。
	public JavaSparkContext javaSparkContext(@Autowired SparkConf sparkConf) {
		System.setProperty("hadoop.home.dir", "F:/hadoop-2.7.4");
		System.setProperty("HADOOP_USER_NAME", "root"); 
		return new JavaSparkContext(sparkConf);
	}
	
	/**
	 * 创建SparkSession对象
	 * @param sc
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SparkSession.class)
	public SparkSession sparkSession(@Autowired JavaSparkContext javaSparkContext) {
		return new SparkSession(javaSparkContext.sc());
	}
	
	
	
}
