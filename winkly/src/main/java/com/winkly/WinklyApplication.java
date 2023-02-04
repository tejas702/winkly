package com.winkly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = {"com.winkly.*"})
@EntityScan(basePackages = {"com.winkly"})
@EnableJpaRepositories(basePackages = {"com.winkly*"})
public class WinklyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WinklyApplication.class, args);
	}

}
