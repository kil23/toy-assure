package com.commons.service;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ApiException extends RuntimeException{

    public ApiException(Class clazz, String... searchParamsMap) {
        super(ApiException.generateMessage(clazz.getSimpleName(), toMap(String.class, String.class, searchParamsMap)));
    }

    public ApiException(String msg){
        super(msg);
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
                " was not found for parameters " +
                searchParams;
    }

    private static <K, V> Map<K, V> toMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }

//    private Errors errors;
//    private final List<CustomErrorResponse> errorList = new ArrayList<>();
//
//    public ApiException(Errors errors){
//        this.errors = errors;
//    }
//
//    public List<CustomErrorResponse> getFormattedErrors() {
//        if (errors != null) {
//            for (ObjectError objectError : errors.getAllErrors()) {
//                CustomErrorResponse customError = new CustomErrorResponse();
//                if (objectError instanceof FieldError) {
//                    FieldError fieldError = (FieldError) objectError;
//                    customError.setFieldName(fieldError.getField());
//                    customError.setFieldValue(fieldError.getRejectedValue());
//                    customError.setErrorCode(fieldError.getCode());
//                    customError.setMessage(fieldError.getCodes()[0]);
////                } else {
////                    customError.setErrorCode(objectError.getCode());
////                    customError.setMessage(objectError.getCodes()[0]);
//                }
//                errorList.add(customError);
//            }
//        }
//        return errorList;
//    }
//
//    public CustomErrorResponse getErrorDetails() {
//        CustomErrorResponse customError = new CustomErrorResponse();
//        customError.setErrorCode(HttpStatus.NOT_FOUND.toString());
//        customError.setErrorMsg();
//    }
//
//    public ApiException(String msg) {
//        super(msg);
//    }
}
