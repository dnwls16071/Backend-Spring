package hello.apiexception.api;

import hello.apiexception.exception.BadRequestException;
import hello.apiexception.exception.UserException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ApiExceptionController {

	public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
	public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
	public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
	public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
	public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

	@GetMapping("/api/members/{id}")
	public MemberDto getMember(@PathVariable(name = "id") String id) {
		if (id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자입니다!");
		}

		if (id.equals("bad")) {
			throw new IllegalArgumentException("잘못된 입력 값");
		}

		if (id.equals("user-ex")) {
			throw new UserException("사용자 오류");
		}

		return new MemberDto(id, "hello " + id);
	}

	@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> errorPAge500Api(HttpServletRequest request, HttpServletResponse response) {
		log.info("API errorPage 500");
		Map<String, Object> result = new HashMap<>();
		Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
		result.put("status", request.getAttribute(ERROR_STATUS_CODE));
		result.put("message", ex.getMessage());

		Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
	}

	@RequestMapping(value = "/api/response-status-ex1")
	public String responseStatus() {
		throw new BadRequestException();
	}

	@GetMapping("/api/default-handler-ex")
	public String defaultHandler(@RequestParam(name = "data") Integer data) {
		return "ok";
	}

	@Data
	static class Member {
		private String memberId;
		private String name;
	}

	@Data
	@NoArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;

		public MemberDto(Member member) {
			this.memberId = member.getMemberId();
			this.name = member.getName();
		}

		public MemberDto(String memberId, String message) {
			this.memberId = memberId;
			this.name = message;
		}
	}
}
