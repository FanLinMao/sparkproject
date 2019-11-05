package com.myspark.sparkanalysis.service;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import com.myspark.sparkanalysis.pojo.Movie;

import scala.Tuple2;

/**
 * Java SparkRDD操作
 * @date: 2019/06/26
 * @author: 范林茂
 */
@Service
public class JavaRDDOperate {
	
	
	/**
	 * 过滤查询
	 * @param filePath 本地文件路径或HDFS路径
	 * @param movieName 电影名称
	 * @param movieType 电影类型
	 * @return
	 */
	public List<Movie> filterSearch(SparkSession spark, String filePath, String movieName,String movieType) {
		List<Movie> list = new ArrayList<Movie>();
		//用SparkSession直接可以读取csv格式类容，自动识别表头。
		spark.read().csv(filePath)
				.cache()
				.collectAsList()
				.forEach(row-> {
					if(movieType != null || !movieType.equals("")) {//如果电影类型名不为空
						//同时查询电影名和电影类型
						if(row.getString(1).contains(movieName) && row.getString(2).contains(movieType)) {
							//将过滤的内容添加到list并返回
							list.add(new Movie(row.getString(0),row.getString(1),row.getString(2)));
						}
					}
					if(movieType == null || movieType.equals("")) {//如果电影类型为空，则查询全部
						if(row.getString(1).contains(movieName)) {
							list.add(new Movie(row.getString(0),row.getString(1),row.getString(2)));
						}
					}
				});
		
		return list;
				
	}
	/**
	 * 获取电影的总数
	 * @param filePath
	 * @return
	 */
	public long getMoviesCount(JavaSparkContext jsc, String filePath) {
		JavaRDD<String> textFile = jsc.textFile(filePath).cache();
		String firstline = textFile.first();
		//先过滤掉第一行标题，然后获取电影的总数
		long count = textFile.filter(line -> !line.contentEquals(firstline)).count(); 
		return count;
		
	}
	/**
	 * 获取所有电影类型的电影数量
	 * 调用者可以使用统计Map的key个数获得电影类型的个数
	 * @param filePath
	 * @return  Map<电影类型，类型统计数量>
	 */
	public Map<String,Integer> getAllMoviesTypeNumbers(JavaSparkContext jsc, String filePath, String outputPath){
		JavaRDD<String> textFile = jsc.textFile(filePath).cache();
		Map<String,Integer> map = wordcount(textFile, outputPath);
		return map;
	}
	/**
	 * 查询指定类型的电影
	 * @param filePath
	 * @param typename
	 * @return
	 */
	public List<Movie> findMoviesByTypeNames(JavaSparkContext jsc, String filePath, String typeName) {
		JavaRDD<String> textFile = jsc.textFile(filePath).cache();
		String firstline = textFile.first();
		List<Movie> collect = textFile.filter(line -> !line.contentEquals(firstline))
							.map(s -> {
								String[] str = s.split(",");
								return new Movie(str[0], str[1], str[2]);
							})
							.filter(movie -> movie.getGenres().contains(typeName))
							.collect();
		
		return collect;
	}
	
	/**
	 * 根据电影名称查询电影
	 * @param filePath
	 * @param filmName
	 * @return
	 */
	public List<Movie> findMoviesByFilmName(JavaSparkContext jsc, String filePath, String filmName){
		JavaRDD<String> textFile = jsc.textFile(filePath).cache();
		String firstline = textFile.first();
		//先过滤掉第一行
		List<Movie> collect = textFile.filter(line -> !line.contentEquals(firstline))
								.map(s -> {
									String[] str = s.split(",");
									return new Movie(str[0], str[1], str[2]);
								})
								.filter(movie -> {
									String newName = filmName.trim().replaceAll("\\s*", "").toLowerCase();//将输入的电影名去空格转换为小写匹配
									return movie.getTitle().replaceAll("\\s*", "").toLowerCase().contains(newName);
								})
								.collect();
		return collect;
	}
	
	
	/**
	 * 电影类型的关键字统计
	 * @param lines
	 * @param outputPath 
	 * @return
	 */
	public Map<String,Integer> wordcount(JavaRDD<String> lines, String outputPath) {
		String firstline = lines.first();
		//同样的，先过滤掉第一行标题
		JavaRDD<String> words = lines.filter(line -> !line.contentEquals(firstline))
								.map(rdd -> rdd.split(")")[1])
								.flatMap(line -> Arrays.asList(line.split("\\|")).iterator());  
		  
        JavaPairRDD<String, Integer> counts = words.mapToPair(w -> new Tuple2<String, Integer>(w, 1))  
                				.reduceByKey((x, y) -> x + y);  
  
       List<Tuple2<String, Integer>> output = counts.collect();  
       Map<String, Integer> map = new HashMap<String,Integer>();
        for (Tuple2<String, Integer> tuple : output) {  
        	//将数据放入Map中，数据内容为：Map<电影类型：电影类型数量>
        	map.put(tuple._1(), tuple._2());
       }  
       
       counts.saveAsTextFile(outputPath);
       return map;

	}

}
