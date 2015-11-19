package ksbysample.webapp.lending.web;

import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e
            , HttpServletRequest request
            , HttpServletResponse response) {
        String url = request.getRequestURL().toString()
                + (StringUtils.isNotEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "");
        logger.error("URL = {}", url, e);

        ModelAndView model = new ModelAndView("error");
        List<String> errorInfoList = new ArrayList<>();

        // エラーメッセージ
        if (e != null && StringUtils.isNotEmpty(e.getMessage())) {
            model.addObject("errorMessage", e.getMessage());
        } else {
            model.addObject("errorMessage", response.getStatus() + " " + HttpStatus.valueOf(response.getStatus()).getReasonPhrase());
        }
        // エラー発生日時
        model.addObject("currentdt", LocalDateTime.now());
        // URL
        errorInfoList.add("　");
        errorInfoList.add("エラーが発生したURL：　" + url);
        // URLパラメータ
        errorInfoList.add("URLパラメータ一覧：");
        Map<String, String[]> params = request.getParameterMap();
        params.entrySet().stream()
                .forEach(param -> errorInfoList.add(param.getKey() + "：" + Joiner.on(", ").join(param.getValue())));
        model.addObject("errorInfoList", errorInfoList);
        // スタックトレース
        if (e != null) {
            errorInfoList.add("　");
            for (StackTraceElement trace : e.getStackTrace()) {
                errorInfoList.add(trace.toString());
            }
        }
        
        return model;
    }

}
