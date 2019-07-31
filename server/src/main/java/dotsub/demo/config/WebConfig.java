package dotsub.demo.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application.properties")
@EnableSwagger2
@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private Environment environment;
	
	private static final int DEFAULT_MAX_FILE_SIZE = 50;
	private static Integer MAX_FILE_SIZE_PROP;
	private static int MAX_FILE_SIZE;
	
	public static Integer getMaxFileSize() {
		try {
			return Integer.valueOf(MAX_FILE_SIZE_PROP);
		}catch(NumberFormatException e) {
			return DEFAULT_MAX_FILE_SIZE;
		}
	}
	
	@PostConstruct
	public void init() {
		try {
			int value = Integer.valueOf(environment.getProperty("max-file-size-mb"));
			MAX_FILE_SIZE = value * 1024 * 1024;
			MAX_FILE_SIZE_PROP = Integer.valueOf(environment.getProperty("max-file-size-mb"));
		}catch(NumberFormatException e) {
			MAX_FILE_SIZE = DEFAULT_MAX_FILE_SIZE * 1024 * 1024;
		}
	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		var multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(MAX_FILE_SIZE);
	    return multipartResolver;
	}

	@Bean
    public TomcatServletWebServerFactory containerFactory() {
        return new CustomTomcatServletWebServerFactory(MAX_FILE_SIZE);

    }
	
	@Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/**")
                .addResourceLocations("classpath:/static/");
        
        registry.addResourceHandler("swagger-ui.html")
        		.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
		        .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
	
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		final var templateResolver = new SpringResourceTemplateResolver();
	    templateResolver.setPrefix("classpath:/static/");
	    templateResolver.setSuffix(".html");
	    templateResolver.setTemplateMode("HTML");
	    templateResolver.setOrder(1);
	 
	    return templateResolver;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
	 
      final var templateEngine = new SpringTemplateEngine(); 
      templateEngine.addTemplateResolver(templateResolver()); 
      final var resolver = new ThymeleafViewResolver();
      resolver.setTemplateEngine(templateEngine);
      registry.viewResolver(resolver);
	}
	
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()      
          .apis(RequestHandlerSelectors.any())              
          .paths(x -> x.contains("/files"))                          
          .build();                                           
    }
	 

}
