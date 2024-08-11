package com.valr.api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {

    //handles MethodArgumentNotValidException
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ModelAndView handleMethodArgumentNotValidException(
//            MethodArgumentNotValidException ex) {
//        List<String> errors = ex.getBindingResult()
//                .getAllErrors()
//                .stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
//
//        final ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("message", errors);
//        modelAndView.setViewName("error-book");
//        return modelAndView;
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    protected ModelAndView handleConstraintViolationException(ConstraintViolationException ex) {
//        List<String> errors = new ArrayList<>();
//
//        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
//
//        final ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("message", errors);
//        modelAndView.setViewName("error-book");
//        return modelAndView;
//    }
}
