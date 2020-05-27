package com.assure.model.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class BinInventoryCsvInputForm {

    @NotEmpty(message = "Inventory list cannot be empty.")
    @JsonProperty("inventory")
    @Valid
    private List<InventoryForm> inventoryList;

    @NotNull(message = "Client Id cannot be null.")
    @JsonProperty("clientId")
    @Min(value = 1, message = "Min Client-Id value should be 1.")
    private Long clientId;

//    @Override
//    public boolean supports(Class<?> clazz) {
//        return BinInventoryCsvInputForm.class.equals(clazz);
//    }
//
//    @Override
//    public void validate(Object target, Errors errors) {
//
//        BinInventoryCsvInputForm inputForm = (BinInventoryCsvInputForm) target;
//
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "clientId", "clientId.required");
//
//        if(inputForm.getClientId() == 0L){
//            errors.rejectValue("clientId", "field.required", "Client Id cannot b");
//        }
//    }
}
