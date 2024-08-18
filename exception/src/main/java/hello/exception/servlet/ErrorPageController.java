package hello.exception.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class ErrorPageController {

	public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
	public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
	public static final String ERROR_MESSAGE = "javax.servlet.error.message";
	public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
	public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

	@RequestMapping("/error-page/404")
	public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 404");
		printErrorInfo(request);
		return "/error-page/404";
	}

	@RequestMapping("/error-page/500")
	public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 500");
		printErrorInfo(request);
		return "/error-page/500";
	}

	private void printErrorInfo(HttpServletRequest request) {
		log.info("ERROR EXCEPTION: {}", request.getAttribute(ERROR_EXCEPTION));
		log.info("dispatcher type: {}", request.getDispatcherType());
	}
}
