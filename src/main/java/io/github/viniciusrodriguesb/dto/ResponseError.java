package io.github.viniciusrodriguesb.dto;

import jakarta.validation.ConstraintViolation;
import lombok.Data;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ResponseError {
    private String message;
    private Collection<FieldError> erros;

    public ResponseError(String message, Collection<FieldError> erros) {
        this.message = message;
        this.erros = erros;
    }

    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations){
        var errors = violations.stream()
                .map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());

        String message = "Validation Error";
        return new ResponseError(message, errors);
    }
}
