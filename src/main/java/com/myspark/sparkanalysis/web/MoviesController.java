package com.myspark.sparkanalysis.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myspark.sparkanalysis.dto.ResponseStatus;
import com.myspark.sparkanalysis.pojo.Movie;
import com.myspark.sparkanalysis.service.JavaRDDOperate;
import com.myspark.sparkanalysis.service.JavaSparkSQLOperate;

/**
 * @date: 2019/06/26
 * @author: 范林茂
 */
@RestController
public class MoviesController {
	
	@Autowired
	private JavaRDDOperate rdd;
	
	@Autowired
	private JavaSparkSQLOperate sparkSql;
	
	@Autowired
	private SparkSession spark;
	
	private String inputPath = "hdfs://192.168.233.128:9000/dataset/movies.csv";
	
	/**
	 * 根据电影名称或电影类型搜索电影
	 * @param movieName
	 * @param movieType
	 * @return
	 */
	@PostMapping("/searchMovies")
	@CrossOrigin(origins="*",allowedHeaders="*",allowCredentials="true")//表示可跨域请求，下同
	public ResponseStatus filterMovieDate(String movieName, String movieType) {
		System.out.println("名称："+movieName+",类型："+movieType);
		if(movieName == null) {
			return new ResponseStatus("400", "错误请求！", null);
		}
		List<Movie> filterDate = rdd.filterSearch(spark, inputPath, movieName, movieType);
		if(filterDate != null) {
			return new ResponseStatus("200", "查询成功！", filterDate);
		}
		return new ResponseStatus("500", "查询失败！", null);
	}
	
	/**
	 * 上传文件到本地及HDFS
	 * @param request
	 * @param files
	 * @return
	 */
	@PostMapping("/doUpload")
	@CrossOrigin(origins="*",allowedHeaders="*",allowCredentials="true")
    public ResponseStatus uploadFileHandler(@RequestParam("file") MultipartFile[] files) {
		Configuration conf = new Configuration();
		conf.setBoolean("dfs.support.append", true);
	    String hdfsDir = "/flm";
	    //System.setProperty("hadoop.home.dir", "F:/hadoop-2.7.4");
		//System.setProperty("HADOOP_USER_NAME", "root"); 
        //本地项目名相对路径
		File uploadRootDir = new File("src/main/resources/upload");
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            //获取文件名
            String name = file.getOriginalFilename();
            System.out.println("Client File Name = " + name);
            if (name != null && name.length() > 0) {
                try {
                    byte[] bytes = file.getBytes();
                    
                    String filepath = uploadRootDir.getAbsolutePath()+ File.separator + name;
                    File serverFile = new File(filepath);
                    //先上传文件到本地项目文件夹中
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(bytes);
                    stream.close();
                    System.out.println("Write file: " + serverFile);
                    //再上传到HDFS
                    try{
                    	//指定本地路径
        	            Path localPath = new Path(filepath);
        	            //指定HDFS路径
        	            Path hdfsPath = new Path(hdfsDir);
        	            //获取HDFS系统环境
        	            FileSystem hdfs = FileSystem.get(new URI("hdfs://192.168.233.128:9000"), conf, "root");
        	            if(!hdfs.exists(hdfsPath)){
        	                 hdfs.mkdirs(hdfsPath);
        	             }
        	             //执行上传
        	             hdfs.copyFromLocalFile(localPath, hdfsPath);
        	             System.out.println("hdfs path: hdfs://192.168.233.128:9000" + hdfsDir);
        	             return new ResponseStatus("200","上传成功！",null);
        	         }catch(Exception e){
        	         e.printStackTrace();
        	         }
                } catch (Exception e) {
                    System.out.println("Error Write file: " + name);
                }
            }
        }
        return new ResponseStatus("200","上传失败！",null);
    }
	
	/**
	 * 根据性别、职业，对不同类型的电影平均评分。
	 * 就是通过Spark-SQL操作数据库求平均值 
	 * @param gender
	 * @param age
	 * @return
	 */
	@PostMapping("/getAllTypeAverageNum")
	@CrossOrigin(origins="*",allowedHeaders="*",allowCredentials="true")
	public ResponseStatus getAllTypeAverageNum(String gender, String age) {
		Map<String, Double> averageNum;
		try {
			averageNum = sparkSql.catogoryAvg(gender, age);
			if(averageNum != null) {
				return new ResponseStatus("200", "查询成功！", averageNum);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseStatus("500", "查询失败！", null);
	}
	
	
	
}
