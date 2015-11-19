package ksbysample.webapp.lending.web.lendingapp;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Data
public class LendingappParamForm {

    @DecimalMin(value = "1")
    @DecimalMax(value = "9223372036854775807")
    private Long lendingAppId;

}
