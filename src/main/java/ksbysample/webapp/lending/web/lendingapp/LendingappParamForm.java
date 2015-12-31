package ksbysample.webapp.lending.web.lendingapp;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
public class LendingappParamForm {

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "9223372036854775807")
    private Long lendingAppId;

}
