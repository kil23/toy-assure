package com.assure.validators;

import com.assure.dto.BinInventoryDto;
import com.assure.model.form.BinInventoryCsvInputForm;
import com.assure.model.form.InventoryForm;
import com.assure.pojo.Bin;
import com.assure.pojo.Client;
import com.assure.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class BinInventoryCsvInputFormValidator implements Validator {

    @Autowired private BinInventoryDto binInventoryDto;

    @Override
    public boolean supports(Class<?> clazz) {
        return BinInventoryCsvInputFormValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        BinInventoryCsvInputForm inputForm = (BinInventoryCsvInputForm) target;

        Long clientId = inputForm.getClientId();
        Client client = binInventoryDto.getClientDetails(clientId);
        if(client == null){
            errors.rejectValue("clientId", "ClientId.invalid","Client not found with ID : "+clientId);
        }

        List<InventoryForm> inventoryList = inputForm.getInventoryList();
        int index = 0;
        for (InventoryForm inventoryForm : inventoryList){
            String clientSkuId = inventoryForm.getClientSkuId();
            Long binId = inventoryForm.getBinId();

            Product product = binInventoryDto.getProductIfPresentByClientSkuId(clientSkuId, clientId);
            if(product == null){
                errors.pushNestedPath("inventoryList["+index+"]");
                errors.rejectValue("clientSkuId", "not found","Product not found with Client SKU ID : "+clientSkuId);
                errors.popNestedPath();
            }

            Bin bin = binInventoryDto.getBinData(binId);
            if(bin == null) {
                errors.pushNestedPath("inventoryList["+index+"]");
                errors.rejectValue("binId", "not found","Bin not found with ID : "+binId);
                errors.popNestedPath();
            }
            index++;
        }
    }
}
