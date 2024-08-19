package hello.apiexception.api;

import hello.apiexception.error.ErrorResult;
import hello.apiexception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionControllerV2 {

	@GetMapping("/api/v2/members/{id}")
	public ApiExceptionController.MemberDto getMember(@PathVariable(name = "id") String id) {
		if (id.equals("ex")) {
			throw new RuntimeException("잘못된 사용자입니다!");
		}

		if (id.equals("bad")) {
			throw new IllegalArgumentException("잘못된 입력 값");
		}

		if (id.equals("user-ex")) {
			throw new UserException("사용자 오류");
		}

		return new ApiExceptionController.MemberDto(id, "hello " + id);
	}

	/*
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResult illegalExceptionHandler(IllegalArgumentException e) {
		log.error("exceptionHandler", e);
		return new ErrorResult("BAD_REQUEST", e.getMessage());
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> userExceptionHandler(UserException e) {
		log.error("exceptionHandler", e);
		ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ErrorResult exceptionHandler(Exception e) {
		log.error("exceptionHandler", e);
		return new ErrorResult("EX", "내부 오류");
	}
	 */
}
