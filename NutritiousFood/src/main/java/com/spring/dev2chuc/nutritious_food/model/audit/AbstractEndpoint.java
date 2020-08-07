//package com.spring.dev2chuc.nutritious_food.model.audit;
//
//import com.spring.dev2chuc.nutritious_food.exception.ErrorDetails;
//import com.spring.dev2chuc.nutritious_food.payload.response.NutritiousFoodAPIResponse;
//import io.reactivex.Observable;
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.NoSuchMessageException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.context.request.async.DeferredResult;
//
//import java.util.Locale;
//import java.util.stream.Collectors;
//
//public abstract class AbstractEndpoint {
//
//    @Autowired
//    private MessageSource messageSource;
//
//    private String getMessageLocalization(String message, Locale locale) {
//        try {
//            return messageSource.getMessage(message.replaceAll("[\\{|}|']", ""), null, locale);
//        } catch (NoSuchMessageException noSuchMessageException) {
//            noSuchMessageException.printStackTrace();
//            return message;
//        }
//    }
//
//    private String resolveBindingResultErrors(BindingResult bindingResult, Locale locale) {
//        return bindingResult.getFieldErrors().stream()
//                .map(fr -> {
//                    String field = fr.getField();
//                    String validationMessage = StringUtils.isBlank(fr.getDefaultMessage()) ? "" : fr.getDefaultMessage();
//                    return getMessageLocalization(validationMessage, locale);
//                    //String.format("%s: %s", field, messageSource.getMessage(validationMessage, null, locale));
//                }).findFirst().orElse(bindingResult.getAllErrors().stream().map(x -> {
//                    String validationMessage = StringUtils.isBlank(x.getDefaultMessage()) ? "" : x.getDefaultMessage();
//                    return getMessageLocalization(validationMessage, locale);
//                }).collect(Collectors.joining(", ")));
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <P, T, B extends BindingResult, L extends Locale> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
//                                                                                                   Observable<T> details,
//                                                                                                   B bindingResult,
//                                                                                                   L locale) {
//        details.subscribe(new Observer<T>() {
//
//            @Override
//            public void onSubscribe(Disposable disposable) {
//
//            }
//
//            @Override
//            public void onNext(T t) {
//                ResponseEntity<NutritiousFoodAPIResponse> response = new ResponseEntity<>(
//                        new NutritiousFoodAPIResponse<>(t, HttpStatus.OK), HttpStatus.OK);
//                deferredResult.setResult((P) response);
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                String validationMessage = StringUtils.isBlank(error.getMessage()) ? "" : error.getMessage();
//                ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),
//                        bindingResult.hasErrors() ? resolveBindingResultErrors(bindingResult, locale) :
//                                getMessageLocalization(validationMessage, locale));
//                ResponseEntity<ErrorDetails> response = new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//                deferredResult.setErrorResult(response);
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//        return deferredResult;
//    }
//
//    @SuppressWarnings("unchecked")
//    protected <P, T, L extends Locale> DeferredResult<P> toDeferredResult(DeferredResult<P> deferredResult,
//                                                                          Observable<T> details,
//                                                                          L locale) {
//        details.subscribe(new Observer<T>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(T t) {
//                ResponseEntity<NutritiousFoodAPIResponse> response = new ResponseEntity<>(
//                        new NutritiousFoodAPIResponse<>(t, HttpStatus.OK), HttpStatus.OK);
//                deferredResult.setResult((P) response);
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                //TODO
//                error.printStackTrace();
//                String validationMessage = StringUtils.isBlank(error.getMessage()) ? "" : error.getMessage();
//                ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(),
//                        getMessageLocalization(validationMessage, locale));
//                ResponseEntity<ErrorDetails> response = new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//                deferredResult.setErrorResult(response);
//            }
//
//            @Override
//            public void onComplete() {
//                deferredResult.onCompletion(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        });
//        return deferredResult;
//    }
//
//    protected <B extends BindingResult> Observable<B> toObservable(B bindingResult) {
//        return Observable.fromCallable(() -> bindingResult)
//                .flatMap(
//                        v -> {
//                            if (v.hasErrors()) {
//                                throw new RuntimeException(resolveBindingResultErrors(v, Locale.getDefault()));
//                            } else {
//                                return Observable.just(v);
//                            }
//                        },
//                        e -> Observable.error(e),
//                        () -> Observable.empty()
//                ).subscribeOn(Schedulers.io());
//    }
//}
