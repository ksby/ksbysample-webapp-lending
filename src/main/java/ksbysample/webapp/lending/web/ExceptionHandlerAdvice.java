package ksbysample.webapp.lending.web;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ???
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    /**
     * @param e        ???
     * @param request  ???
     * @param response ???
     * @return ???
     * @throws IOException
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e
            , HttpServletRequest request
            , HttpServletResponse response) throws IOException {
        String url;
        if (StringUtils.equals(request.getRequestURI(), "/error")) {
            url = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        } else {
            url = request.getRequestURL().toString();
        }
        url += (StringUtils.isNotEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "");
        logger.error("URL = {}", url, e);

        ModelAndView model = new ModelAndView("error");
        List<String> errorInfoList = new ArrayList<>();

        // エラーメッセージ
        if (e != null && StringUtils.isNotEmpty(e.getMessage())) {
            model.addObject("errorMessage", e.getMessage());

            // Spring Security の機能でアクセス不可と判断された場合に HTTPステータスコードが 403 になるようにする
            if (e instanceof org.springframework.security.access.AccessDeniedException) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
            }
        } else {
            model.addObject("errorMessage", response.getStatus()
                    + " " + HttpStatus.valueOf(response.getStatus()).getReasonPhrase());
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
            try (
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw)
            ) {
                e.printStackTrace(pw);
                pw.flush();
                String stacktrace = sw.toString();
                errorInfoList.add("　");
                Arrays.asList(stacktrace.split(System.lineSeparator())).stream()
                        .forEach(line -> errorInfoList.add(line));
            }
        }

        return model;
    }

}
