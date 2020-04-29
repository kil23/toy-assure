package com.assure.model.response;

import com.assure.pojo.Order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "InvoiceData")
public class InvoiceData {

    public InvoiceData() { }

    private List<InvoiceOrderItemData> invoiceOrderItemDataList;
    private Order order;

    @XmlElement(name = "orderDetails")
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
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
