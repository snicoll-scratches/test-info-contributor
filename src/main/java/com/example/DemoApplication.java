package com.example;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.info.AbstractEnvironmentInfoContributor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.SimpleInfoContributor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.validation.BindException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Configuration
	@Profile("extra")
	static class InfoContributorConfig {

		@Bean
		public InfoContributor gitFullInfoContributor() throws IOException, BindException {
			Resource resource = new ClassPathResource("git.properties");
			Map<String, Object> content = new LinkedHashMap<>();
			PropertiesConfigurationFactory<Map<String, Object>> factory
					= new PropertiesConfigurationFactory<>(content);
			factory.setTargetName("git");
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			factory.setProperties(properties);
			factory.bindPropertiesToTarget();
			return new SimpleInfoContributor("gitfull", content);
		}

		@Bean
		public InfoContributor fooInfoContributor(ConfigurableEnvironment env) {
			return new AbstractEnvironmentInfoContributor(env) {
				private Map<String, Object> content;

				@Override
				public void contribute(Info.Builder builder) {
					if (this.content == null) {
						this.content = new LinkedHashMap<>();
						bindEnvironmentTo("foo", this.content);
					}
					builder.withDetail("bar", this.content);
				}
			};
		}
	}
}
