package com.gabia.weat.gcellexcelserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import com.gabia.weat.gcellcommonmodule.annotation.EnableCustomLog;

@EnableCustomLog
@SpringBootApplication
@ConfigurationPropertiesScan
public class GcellExcelServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcellExcelServerApplication.class, args);
	}

}