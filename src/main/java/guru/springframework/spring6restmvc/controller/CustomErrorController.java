package guru.springframework.spring6restmvc.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomErrorController {

	@ExceptionHandler(TransactionSystemException.class)
	ResponseEntity handleJPAViolations(TransactionSystemException ex) {
		if (ex.getRootCause() instanceof ConstraintViolationException cex) {
			var errorList = cex.getConstraintViolations().stream()
				.map(violation -> {
					return Map.of(violation.getPropertyPath().toString(), violation.getMessage());
				})
				.toList();
			return ResponseEntity.badRequest().body(errorList);
		}
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity handleBindErrors(MethodArgumentNotValidException ex) {
		var errorList = ex.getFieldErrors().stream()
		.map(fieldError -> {
			return Map.of(fieldError.getField(), fieldError.getDefaultMessage());
		})
		.toList();

		return ResponseEntity.badRequest().body(errorList);
	}
}
