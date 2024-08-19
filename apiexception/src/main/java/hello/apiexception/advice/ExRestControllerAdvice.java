package hello.apiexception.advice;

import hello.apiexception.error.ErrorResult;
import hello.apiexception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExRestControllerAdvice {

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
}
