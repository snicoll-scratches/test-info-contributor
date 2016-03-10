package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.PropertySourcesBinder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public InfoContributor myInfoContributor(ConfigurableEnvironment env) {
		return b -> {
			PropertySourcesBinder binder = new PropertySourcesBinder(env);
			b.withDetail("custom", binder.extractAll("specific.app.Foo"));
		};

	}
}
