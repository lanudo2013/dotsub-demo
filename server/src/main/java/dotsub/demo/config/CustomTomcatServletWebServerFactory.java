package dotsub.demo.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize"})
class CustomTomcatServletWebServerFactory extends TomcatServletWebServerFactory {
	private int maxFileSize;
	
	CustomTomcatServletWebServerFactory(final int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	
	@Override
    protected void customizeConnector(final Connector connector) {
        super.customizeConnector(connector);
        connector.setMaxPostSize(maxFileSize);
        connector.setMaxSavePostSize(maxFileSize);
        if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol) {

            ((AbstractHttp11Protocol <?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            if (logger.isInfoEnabled()) {
            	logger.info("Set MaxSwallowSize "+ maxFileSize);
            }
            
        }
    }
}
