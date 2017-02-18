package ksbysample.webapp.lending.webapi.common;

import lombok.Data;

@SuppressWarnings("TypeParameterUnusedInFormals")
@Data
public class CommonWebApiResponse<T> {

    private int errcode = 0;

    private String errmsg = "";

    private T content;

}
