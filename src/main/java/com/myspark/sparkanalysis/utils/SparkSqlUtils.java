package com.myspark.sparkanalysis.utils;

import java.util.HashMap;
import java.util.Properties;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * @date: 2019/06/29
 * @author: 范林茂
 */
public class SparkSqlUtils {
	
	private static String movies = "hdfs://192.168.233.128:9000/dataset/movies.csv";
	private static String ratings = "hdfs://192.168.233.128:9000/dataset/ratings.csv";
	private static String users = "hdfs://192.168.233.128:9000/dataset/users.dat";
	private static String occupation = "hdfs://192.168.233.128:9000/dataset/occupation.txt";
	private static String commonDBConURL = "jdbc:mysql://localhost:3306/sparktest";
	
	private static Properties connectionProperties = null;
	
	static {
		
		connectionProperties = new Properties();
		connectionProperties.put("user", "root");
		connectionProperties.put("password", "admin");
		
	}

	/**
	 * 读取mysql数据库
	 * @param dbname 数据库名
	 * @param table 表名
	 * @return
	 */
	public static Dataset<Row> readMySQL(SparkSession sparkSession, String dbname, String table) {
		String url = "jdbc:mysql://localhost:3306/"+ dbname;
		Dataset<Row> dataset = sparkSession.read().jdbc(url, table, connectionProperties);
		return dataset;
	}
	
	/**
	 * 用sparksession读取默认数据库sparktest中的指定表
	 * @param sparkSession 操作读取数据库
	 * @param table 表名
	 * @return 返回数据集
	 */
	public static Dataset<Row> readMySQL(SparkSession sparkSession, String table) {
		//1.指定表读取
		//Dataset<Row> dataset = sparkSession.read().jdbc(commonDBConURL, table, connectionProperties);
		//2.自定义读取，写灵活的sql语句当成table
		String sql = "select * from occupation";
		HashMap<String, String> options = new HashMap<String,String>();
		options.put("url", "jdbc:mysql://localhost:3306/sparktest");
		options.put("user", "root");
		options.put("password", "admin");
		options.put("driver", "com.mysql.jdbc.Driver");
		options.put("dbtable", sql);
		Dataset<Row> dataset = sparkSession.read().format("jdbc").options(options).load();
		
		return dataset;
	}
	
	/**
	 * 将数据源的类容，连接查询后写入数据库中
	 * @param sparkSession
	 */
	public static void joinAfterWriteMySQL(SparkSession sparkSession) {
		
		//随机切片权重比例
		double[] weights = new double[] {0.1, 0.4, 0.5};
		
		//获取数据源
		Dataset<Row>[] ratingscsv = sparkSession.read().format("csv").option("header", "true").load(ratings).randomSplit(weights);
		Dataset<Row> usersjdbc = sparkSession.read().jdbc(commonDBConURL, "users", connectionProperties);
		Dataset<Row> moviescsv = sparkSession.read().jdbc(commonDBConURL, "movies", connectionProperties);
		
		//连接查询得到新的数据集
		Dataset<Row> newtable = ratingscsv[0].join(moviescsv, 
						ratingscsv[0].col("movieId").equalTo(moviescsv.col("movieId")))
					 .join(usersjdbc,
						ratingscsv[0].col("userId").equalTo(usersjdbc.col("userId")))
					 .select(usersjdbc.col("gender"),usersjdbc.col("age"),moviescsv.col("genres"),ratingscsv[0].col("rating"));
		//将新的数据集写入本地数据库中
		newtable.write().jdbc(commonDBConURL, "temp_analysis", connectionProperties);
		
	}
	
	
	/**
	 * 读取一个数据源的类容，复制到另外一个文件
	 * @param otherFilePath
	 */
	public static void copyFileToOtherFile(SparkSession sparkSession, String otherFilePath) {
		
		//随机切片权重比例
		double[] weights = new double[] {0.1, 0.4, 0.5};
		
		//获取数据源
		Dataset<Row>[] ratingscsv = sparkSession.read().format("csv").option("header", "true").load(ratings).randomSplit(weights);
		
		//转换成RDD写入文件
		ratingscsv[2].javaRDD().saveAsTextFile(otherFilePath);
		
		
		
	}

}
