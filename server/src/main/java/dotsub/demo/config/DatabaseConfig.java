package dotsub.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "dotsub.demo.repo")
public class DatabaseConfig {
	
}
