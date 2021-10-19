package ksbysample.webapp.lending.web.sessionsample;

import lombok.Data;

import java.io.Serializable;

/**
 * ???
 */
@Data
public class SessionSampleForm implements Serializable {

    private static final long serialVersionUID = 1183516008630394266L;

    private String name;

    private String age;

    private String address;

    private String email;

}
