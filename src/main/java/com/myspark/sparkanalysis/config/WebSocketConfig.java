package com.myspark.sparkanalysis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @date: 2019/07/05
 * @author: 范林茂
 */
@Configuration
public class WebSocketConfig {
	
	@Bean  
    public ServerEndpointExporter serverEndpointExporter() {  
        return new ServerEndpointExporter();  
    }  
	
}
