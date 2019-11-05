package com.myspark.sparkanalysis.service;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.myspark.sparkanalysis.utils.JavaDBCon;

/**
 * Java SparkSQL操作 
 * @date: 2019/06/28
 * @author: 范林茂
 */
@Service
public class JavaSparkSQLOperate {
	
	private JavaDBCon jdbc = new JavaDBCon();
	
	/**
	 * 获取类型平均值
	 * @return Map<类型，平均值>
	 * @throws Exception 
	 */
	public Map<String, Double> catogoryAvg(String gender, String age) throws Exception {
		String[] types = new String[] {"Action",
				"Adventure","Animation","Children","Comedy","Crime",
				"Documentary","Drama","Fantasy","Film-Noir",
				"Horror","Musical","Mystery","Romance","Sci-Fi",
				"Thriller","War","Western","(no genres listed)"};
		
		Map<String, Double> map = new HashMap<String,Double>();
	
		if(!"".equals(gender)) {
			gender = "gender = '"+ gender +"' AND ";
		}
		if(!"".equals(age)) {
			age = "age = '" + age +"' AND ";
		}
		
		String sqlText = "select AVG(rating) as average from temp_analysis WHERE " + gender + age;
		for (String type : types) {
			String sql = sqlText + "genres LIKE '%"+ type.trim() +"%'";
			//Dataset<Row> result = SparkSqlUtils.readMySQL(sql);
			ResultSet result = jdbc.doQuery(sql, null);//不用预编译
			while(result.next()) {
				map.put(type, result.getDouble(1));
			}
		}
		jdbc.close();
		return map;
	}
	
	

}
