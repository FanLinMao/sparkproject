package com.myspark.sparkanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.myspark.sparkanalysis.web.WebSocketServer;

@SpringBootApplication
public class SparkanalysisApplication {

	public static void main(String[] args) {
		//解决springboot和websocket之间使用@autowired注入为空问题
		ConfigurableApplicationContext applicationContext = SpringApplication.run(SparkanalysisApplication.class, args);
		WebSocketServer.setApplicationContext(applicationContext);
	}

}
