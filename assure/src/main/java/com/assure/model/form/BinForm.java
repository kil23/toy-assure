package com.assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BinForm {

    @Min(value = 1, message = "Min Bin value should be 1.")
    @Max(value = 50, message = "Max Bin value should be 50.")
    @NotNull(message = "Number of Bins cannot be empty")
    private Integer noOfBins;
}
