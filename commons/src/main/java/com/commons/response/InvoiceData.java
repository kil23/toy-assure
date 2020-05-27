package com.commons.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "InvoiceData")
public class InvoiceData {

    public InvoiceData() { }

    private List<InvoiceOrderItemData> invoiceOrderItemDataList;
    private InvoiceOrderData orderData;

    @XmlElement(name = "orderDetails")
    public InvoiceOrderData getOrderData() {
        return orderData;
    }
    public void setOrderData(InvoiceOrderData orderData) {
        this.orderData = orderData;
    }

    @XmlElementWrapper(name="itemList")
    @XmlElement(name="item")
    public List<InvoiceOrderItemData> getInvoiceOrderItemDataList() {
        return invoiceOrderItemDataList;
    }
    public void setInvoiceOrderItemDataList(List<InvoiceOrderItemData> invoiceOrderItemDataList) {
        this.invoiceOrderItemDataList = invoiceOrderItemDataList;
    }
}
