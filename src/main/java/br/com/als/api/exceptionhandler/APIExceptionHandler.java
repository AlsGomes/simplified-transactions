package br.com.als.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.als.domain.exceptions.IdentifierAlreadyExistsException;
import br.com.als.domain.exceptions.ObjectNotFoundException;
import br.com.als.domain.exceptions.UnathorizedTransactionException;
import br.com.als.domain.exceptions.WalletException;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<?> handleObjectNotFoundException(ObjectNotFoundException ex, WebRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND;
		ErrorType errorType = ErrorType.OBJECT_NOT_FOUND;
		String detail = ex.getMessage();
		
		DefaultError error = createDefaultErrorBuilder(status, errorType, detail).build();		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(IdentifierAlreadyExistsException.class)
	public ResponseEntity<?> handleIdentifierAlreadyExistsException(IdentifierAlreadyExistsException ex, WebRequest request){
		HttpStatus status = HttpStatus.CONFLICT;
		ErrorType errorType = ErrorType.IDENTIFIER_ALREADY_EXISTS;
		String detail = ex.getMessage();
		
		DefaultError error = createDefaultErrorBuilder(status, errorType, detail).build();		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(WalletException.class)
	public ResponseEntity<?> handleWalletException(WalletException ex, WebRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorType errorType = ErrorType.WALLET;
		String detail = ex.getMessage();
		
		DefaultError error = createDefaultErrorBuilder(status, errorType, detail).build();		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(UnathorizedTransactionException.class)
	public ResponseEntity<?> handleUnathorizedTransactionException(UnathorizedTransactionException ex, WebRequest request){
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ErrorType errorType = ErrorType.UNATHORIZED_TRANSACTION;
		String detail = ex.getMessage();
		
		DefaultError error = createDefaultErrorBuilder(status, errorType, detail).build();		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ErrorType errorType = ErrorType.MESSAGE_NOT_READABLE;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		DefaultError error = createDefaultErrorBuilder(status, errorType, detail).build();		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ErrorType errorType = ErrorType.OBJECT_NOT_FOUND;
		String detail = String.format("O recurso %s ,que você tentou acessar, é inexistente.", ex.getRequestURL());
		
		DefaultError error = createDefaultErrorBuilder(status, errorType, detail).build();		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		BindingResult bindingResult = ex.getBindingResult();
		
		List<DefaultError.Field> fields = bindingResult.getFieldErrors().stream()
				.map(fieldError -> {
					String msg = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
					
					return DefaultError.Field.builder()
							.propertyName(fieldError.getField())
							.message(msg)
							.build();
				})
				.collect(Collectors.toList());
		
		ErrorType errorType = ErrorType.INVALID_DATA;
		String detail = "Um ou mais campos foram preenchidos de forma inválida";
		
		DefaultError error = createDefaultErrorBuilder(status, errorType, detail)
				.fields(fields)
				.build();		
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body == null) {
			body = DefaultError.builder()
					.timestamp(LocalDateTime.now())
					.title(status.getReasonPhrase())
					.status(status.value())
					.build();
		} else if (body instanceof String) {
			body = DefaultError.builder()
					.timestamp(LocalDateTime.now())
					.title(body.toString())
					.status(status.value())
					.build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
	private DefaultError.DefaultErrorBuilder createDefaultErrorBuilder(HttpStatus status, ErrorType errorType, String detail){
		return DefaultError.builder()
				.timestamp(LocalDateTime.now())
				.status(status.value())
				.type(errorType.getUri())
				.title(errorType.getTitle())
				.detail(detail);
	}
}
