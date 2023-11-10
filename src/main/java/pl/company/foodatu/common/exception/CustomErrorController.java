package pl.company.foodatu.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.company.foodatu.meals.domain.MealWithZeroProductsException;
import pl.company.foodatu.meals.domain.TooManyProductsInOneMealException;
import pl.company.foodatu.meals.dto.NullOrNegativeNutritionValuesException;
import pl.company.foodatu.meals.dto.NullOrNegativeProductsWeightException;
import pl.company.foodatu.plans.domain.TooManyMealsInDayPlanException;

@ControllerAdvice
class CustomErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        return buildCustomException(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler({TooManyMealsInDayPlanException.class, TooManyProductsInOneMealException.class, NullOrNegativeNutritionValuesException.class, NullOrNegativeProductsWeightException.class, MealWithZeroProductsException.class})
    ResponseEntity<Object> handleUnprocessableEntity(Exception ex, WebRequest request) {
        return buildCustomException(HttpStatus.UNPROCESSABLE_ENTITY, ex, request);
    }

    ResponseEntity<Object> buildCustomException(HttpStatus httpStatus, Exception ex, WebRequest request) {
        ErrorWrapper errorWrapper = new ErrorWrapper();
        errorWrapper.addError(new Error(
                ex.getMessage(),
                ex.getClass().getName(),
                ex.getCause() != null ? ex.getCause().getMessage() : null,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ex.getLocalizedMessage()
        ));
        return new ResponseEntity<>(errorWrapper, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        return buildCustomException(HttpStatus.valueOf(statusCode.value()), ex, request);
    }
}
