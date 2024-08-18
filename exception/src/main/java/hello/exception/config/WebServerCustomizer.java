package hello.exception.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// 스프링 부트에서 제공하는 tomcat의 오류 페이지 대신 사용할 오류 페이지 커스터마이징
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
		ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
		ErrorPage errorPage = new ErrorPage(RuntimeException.class, "/error-page/500");

		factory.addErrorPages(errorPage, errorPage404, errorPage500);
	}
}
