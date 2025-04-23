package com.ruoyi.framework.config.battle;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.battle.Constant;
import com.ruoyi.common.battle.domain.ArgumentInvalidResult;
import com.ruoyi.common.battle.domain.BaseResponse;
import com.ruoyi.common.battle.exception.BayonetException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.beans.PropertyEditorSupport;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hongjiasen
 */
@RestControllerAdvice
public class GlobalControllerConfig {

    private Logger log = LoggerFactory.getLogger(getClass());

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DEFAULT_DATE_TIME_FORMAT);
        simpleDateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));

        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern(Constant.DEFAULT_DATE_FORMAT)));
            }
        });

        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern(Constant.DEFAULT_TIME_FORMAT)));
            }
        });

        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern(Constant.DEFAULT_DATE_TIME_FORMAT)));
            }
        });
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataAccessException.class)
    public BaseResponse sQLIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error("唯一性校验不通过", e);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(400)
                .msg("请检查唯一性参数是否重复")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("参数校验不通过", e);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(400)
                .msg(e.toString())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) throws JsonProcessingException {
        List<ArgumentInvalidResult> invalidArguments = new ArrayList<>(e.getBindingResult().getFieldErrorCount());
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            ArgumentInvalidResult invalidArgument = new ArgumentInvalidResult();
            invalidArgument.setDefaultMessage(error.getDefaultMessage());
            invalidArgument.setField(error.getField());
            invalidArgument.setRejectedValue(error.getRejectedValue());
            invalidArguments.add(invalidArgument);
        }
        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
        String msg = objectMapper.writeValueAsString(invalidArguments);
        log.error("参数校验不通过：{}", msg);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(400)
                .msg(msg)
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MqttException.class)
    public BaseResponse mqttExceptionHandler(MqttException e) {
        log.error("MQTT发生异常", e);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(500)
                .msg(e.toString())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SQLException.class)
    public BaseResponse sqlExceptionHandler(SQLException e) {
        log.error("数据库操作发生异常", e);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(500)
                .msg("后台操作发生异常，请联系管理员")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BayonetException.class)
    public BaseResponse bayonetExceptionHandler(BayonetException e) {
        log.error("刺杀系统异常", e);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(500)
                .msg(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResponse baseExceptionHandler(Exception e) {
        log.error("系统发生异常", e);
        return BaseResponse.BaseResponseBuilder
                .aBaseResponse()
                .code(500)
                .msg(e.getMessage())
                .build();
    }
}
