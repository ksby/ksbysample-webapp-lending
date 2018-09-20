package ksbysample.webapp.lending.webapi.common;

import lombok.Data;

/**
 * ???
 */
@SuppressWarnings("TypeParameterUnusedInFormals")
@Data
public class CommonWebApiResponse<T> {

    private int errcode;

    private String errmsg = "";

    private T content;

}
