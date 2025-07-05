package org.example.websitetechworld.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.example.websitetechworld.Dto.Response.ExceptionResponse.ApiErrorResponse;
import org.example.websitetechworld.Dto.Response.ExceptionResponse.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Database Constraint Violation",
                ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllException(Exception e, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage())
//        );
//
//        ApiErrorResponse error = new ApiErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                "Lỗi Xác Thực",
//                errors,
//                request.getDescription(false).replace("uri=", "")
//        );
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        Locale locale = LocaleContextHolder.getLocale();

        ApiErrorResponse error = new ApiErrorResponse();
        error.setStatusCode(400);
        error.setError("Business Error");
        error.setPath(request.getRequestURI());

        // Lấy thông báo quốc tế hóa, có thể truyền args vào {0}
        String message = messageSource.getMessage(
                ex.getMessageCode(),
                ex.getArgs(),
                ex.getMessageCode(), // fallback nếu không tìm thấy
                locale
        );

        error.setMessage(message);
        error.setError(ex.getMessageCode()); // bạn có thể thêm thuộc tính errorCode vào response

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse();
        error.setStatusCode(404);
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
//        error.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String path = request.getRequestURI(); // lấy path thực sự từ HTTP request

        Map<String, String> errors = new HashMap<>();
        Locale locale = LocaleContextHolder.getLocale();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String field = error.getField();
            String messageKey = error.getDefaultMessage(); // ví dụ: "product.name.required"
            String resolvedMessage = messageSource.getMessage(
                    messageKey, null, messageKey, locale
            );
            errors.put(field, resolvedMessage); // ✅ gán message đã dịch
        }

        Map<String, Object> body = Map.of(
                "statusCode", 400,
                "error", "Lỗi Xác Thực",
                "message", errors,
                "path", path
        );

        return ResponseEntity.badRequest().body(body);
    }




}