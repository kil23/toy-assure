package com.assure.controller;

import com.commons.service.ApiException;
import com.commons.service.CustomErrorResponse;
import com.commons.service.CustomValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class AppRestControllerAdvice extends ResponseEntityExceptionHandler {

//	private static final String BAD_REQUEST = "BAD_REQUEST";

//	@ExceptionHandler(ApiException.class)
//	@ResponseStatus(NOT_FOUND)
//	public MessageData handleException(ApiException e) {
//		MessageData data = new MessageData();
//		data.setCode(NOT_FOUND);
//		data.setMessage(e.getMessage());
//		return data;
//	}

//
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Object> handleValidationExceptions(
//			MethodArgumentNotValidException ex) {
////		Map<String, String> errors = new HashMap<>();
////		ex.getBindingResult().getAllErrors().forEach((error) -> {
////			String fieldName = ((FieldError) error).getField();
////			String errorMessage = error.getDefaultMessage();
////			errors.put(fieldName, errorMessage);
////		});
////		return errors;
//
//		CustomErrorResponse apiError = new CustomErrorResponse(BAD_REQUEST);
//		apiError.setMessage("Validation error");
//		apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
//		apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
//		return buildResponseEntity(apiError);
//	}
//
//	@ExceptionHandler(ApiException.class)
//	protected ResponseEntity<Object> handleEntityNotFound(
//			ApiException ex) {
//		CustomErrorResponse apiError = new CustomErrorResponse(NOT_FOUND);
//		apiError.setMessage(ex.getMessage());
//		return buildResponseEntity(apiError);
//	}
//
//	@ExceptionHandler(HttpMessageNotReadableException.class)
//	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
//		String error = "Malformed JSON request";
//		return buildResponseEntity(new CustomErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
//	}
//
//	private ResponseEntity<Object> buildResponseEntity(CustomErrorResponse apiError) {
//		return new ResponseEntity<>(apiError, apiError.getStatus());
//	}
//
////	@ExceptionHandler(ApiException.class)
////	@ResponseStatus(HttpStatus.BAD_REQUEST)
////	public Failure handle(ApiException e) {
////		return new Failure(e.getFormattedErrors());
////	}
//
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> handle(Throwable e) {
		if (e.getCause() instanceof ConstraintViolationException) {
			return buildResponseEntity(new CustomErrorResponse(HttpStatus.CONFLICT, "Database error", e.getCause()));
		}
		CustomErrorResponse apiError = new CustomErrorResponse(INTERNAL_SERVER_ERROR);
		apiError.setMessage("Server Error occurred.");
		apiError.setMessage("An unknown internal error has occurred - " + e.getMessage());
		apiError.setDebugMessage(Arrays.toString(e.getStackTrace()));
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the CustomErrorResponse object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		return buildResponseEntity(new CustomErrorResponse(BAD_REQUEST, error, ex));
	}


	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
	 *
	 * @param ex      HttpMediaTypeNotSupportedException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the CustomErrorResponse object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
			HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		return buildResponseEntity(new CustomErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the CustomErrorResponse object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request) {
		CustomErrorResponse apiError = new CustomErrorResponse(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
		apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the CustomErrorResponse object
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		CustomErrorResponse apiError = new CustomErrorResponse(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(CustomValidationException.class)
	protected ResponseEntity<Object> handleCustomConstraintViolation(
			CustomValidationException ex) {
		CustomErrorResponse apiError = new CustomErrorResponse(BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getFormattedErrors());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handles ApiException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
	 *
	 * @param ex the ApiException
	 * @return the CustomErrorResponse object
	 */
	@ExceptionHandler(ApiException.class)
	protected ResponseEntity<Object> handleEntityNotFound(ApiException ex) {
		CustomErrorResponse apiError = new CustomErrorResponse(NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the CustomErrorResponse object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		String error = "Malformed JSON request";
		return buildResponseEntity(new CustomErrorResponse(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the CustomErrorResponse object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Error writing JSON output";
		return buildResponseEntity(new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		CustomErrorResponse apiError = new CustomErrorResponse(BAD_REQUEST);
		apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	/**
	 * Handle javax.persistence.EntityNotFoundException
	 */
	@ExceptionHandler(javax.persistence.EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
		return buildResponseEntity(new CustomErrorResponse(HttpStatus.NOT_FOUND, ex));
	}

	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
	 *
	 * @param ex the DataIntegrityViolationException
	 * @return the CustomErrorResponse object
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		if (ex.getCause() instanceof ConstraintViolationException) {
			return buildResponseEntity(new CustomErrorResponse(HttpStatus.CONFLICT, "Database error", ex.getCause()));
		}
		return buildResponseEntity(new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex the Exception
	 * @return the CustomErrorResponse object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
																	  WebRequest request) {
		CustomErrorResponse apiError = new CustomErrorResponse(BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(CustomErrorResponse apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}