package dotsub.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"dotsub.demo.repo"})
@EnableAutoConfiguration
@EntityScan("dotsub.demo.model")
public class DatabaseTestConfig {

	
}
