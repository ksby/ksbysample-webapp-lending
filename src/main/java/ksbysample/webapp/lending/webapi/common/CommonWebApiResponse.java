package ksbysample.webapp.lending.webapi.common;

import lombok.Data;

@Data
public class CommonWebApiResponse<T> {

    private int errcode = 0;

    private String errmsg = "";

    private T content;
    
}
