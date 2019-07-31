package dotsub.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages={"dotsub.demo"})
public class Application extends SpringBootServletInitializer {
	
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
		
	}
	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder) {
	    return builder.sources(Application.class);
	}
}
