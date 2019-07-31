package dotsub.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("dotsub.demo.endpoint")
public class WebTestConfig implements WebMvcConfigurer {

}
