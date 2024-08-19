package hello.apiexception.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			if (ex instanceof UserException) {
				log.error("UserException resolver to 400");

				// [1]. application/json
				String a = request.getHeader("accept");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);

				if ("application/json".equals(a)) {
					Map<String, Object> errorResult = new HashMap<>();
					errorResult.put("ex", ex.getClass());
					errorResult.put("message", ex.getMessage());

					// errorResult(객체) → 문자
					String result = objectMapper.writeValueAsString(errorResult);

					response.setContentType("application/json");
					response.setCharacterEncoding("utf-8");
					response.getWriter().write(result);
					return new ModelAndView();
				} else {
					// [2]. text/html
					ModelAndView modelAndView = new ModelAndView();
					// Thymeleaf 의존성 있어야 ModelAndView로 뷰 네임을 찾을 수 있음
					modelAndView.setViewName("error/404");
					return modelAndView;
				}
			}
		} catch (IOException e) {
			log.error("resolver ex", e);
		}
		return null;
	}
}
