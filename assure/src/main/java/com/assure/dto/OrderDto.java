package com.assure.dto;

import com.assure.api.*;
import com.assure.client.ChannelClient;
import com.assure.client.ChannelInvoicePdfClient;
import com.assure.model.form.OrderForm;
import com.assure.model.form.OrderItemForm;
import com.assure.pojo.*;
import com.commons.enums.InvoiceType;
import com.commons.enums.OrderStatus;
import com.commons.form.ChannelOrderForm;
import com.commons.form.ChannelOrderItemForm;
import com.commons.response.*;
import com.commons.service.ApiException;
import com.commons.util.ConvertUtil;
import com.commons.util.PDFHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class OrderDto {

    @Autowired private OrderApi orderApi;
    @Autowired private OrderItemApi itemApi;
    @Autowired private ClientApi clientApi;
    @Autowired private ProductApi productApi;
    @Autowired private InventoryApi inventoryApi;
    @Autowired private BinSkuApi binSkuApi;
    @Autowired private ChannelClient channelClient;
    @Autowired private ChannelInvoicePdfClient invoicePdfClient;

    private static final Logger logger = Logger.getLogger(OrderDto.class);
    private static final String PDF_PATH = "src/main/resources/com/assure/";
    private static final String INVOICE_TEMPLATE_XSL = "src/main/resources/com/assure/invoiceTemplate.xsl";

    @Transactional(rollbackFor = ApiException.class)
    public void addOrder(OrderForm form) {
        Order orderToInsert = convertFormToOrderPojo(form);
        Order order = orderApi.addOrder(orderToInsert);
        for (OrderItemForm itemForm : form.getOrderItemFormList()) {
            Product product = productApi.getProductDetails(itemForm.getClientSkuId(), order.getClientId());
            OrderItem orderItem = convertFormToOrderItemPojo(itemForm, product.getGlobalSkuId(), order.getId());
            itemApi.addOrderItems(orderItem);
        }
        logger.info("Calling allocate orders");
        allocateOrders();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void addChannelOrder(ChannelOrderForm formData) {
        logger.info("here");
        Order orderToInsert = convertChannelFormToOrderPojo(formData);
        Order order = orderApi.addOrder(orderToInsert);
        for (ChannelOrderItemForm itemForm : formData.getChannelOrderItemFormList()) {
            logger.info(itemForm.getGlobalSkuId());
            Product product = productApi.getProductById(itemForm.getGlobalSkuId());
            logger.info(product.getGlobalSkuId());
            OrderItem orderItem = convertChannelFormToOrderItemPojo(itemForm, product.getMrp(), order.getId());
            logger.info(orderItem.getGlobalSkuId());
            itemApi.addOrderItems(orderItem);
        }
        allocateOrders();
    }

    @Transactional(readOnly = true)
    public List<OrderDataResponse> getOrderDataForCustomer(long customerId) {
        List<OrderDataResponse> orderDataResponseList = new ArrayList<>();
        List<Order> orderList = orderApi.getOrderDataForCustomer(customerId);
        if (orderList.isEmpty()) {
//            throw new ApiException("No Order found.");
            return null;
        }
        for (Order order : orderList) {
            OrderDataResponse orderDataResponse = ConvertUtil.convert(order, OrderDataResponse.class);
            if (orderDataResponse != null) {
                ChannelDataResponse channel = channelClient.getChannelDetails(order.getChannelId());
                Client client = clientApi.getClientDetails(order.getClientId());
                Client customer = clientApi.getClientDetails(customerId);
                orderDataResponse.setClientName(client.getName());
                orderDataResponse.setCustomerName(customer.getName());
                orderDataResponse.setChannelName(channel.getName());
                orderDataResponseList.add(orderDataResponse);
            }
        }
        return orderDataResponseList;
    }

    @Transactional(readOnly = true)
    public List<OrderDataResponse> getOrderDataForClient(long clientId) {
        List<OrderDataResponse> orderDataResponseList = new ArrayList<>();
        List<Order> orderList = orderApi.getOrderDataForClient(clientId);
        if (orderList.isEmpty()) {
//            throw new ApiException("No Order found.");
            return null;
        }
        for (Order order : orderList) {
            OrderDataResponse orderDataResponse = ConvertUtil.convert(order, OrderDataResponse.class);
            if (orderDataResponse != null) {
                ChannelDataResponse channel = channelClient.getChannelDetails(order.getChannelId());
                Client client = clientApi.getClientDetails(order.getClientId());
                Client customer = clientApi.getClientDetails(order.getCustomerId());
                orderDataResponse.setClientName(client.getName());
                orderDataResponse.setCustomerName(customer.getName());
                orderDataResponse.setChannelName(channel.getName());
                orderDataResponseList.add(orderDataResponse);
            }
        }
        return orderDataResponseList;
    }

    @Transactional(readOnly = true)
    public List<OrderDataResponse> getOrderDataForChannel(long channelId) {
        List<OrderDataResponse> orderDataResponseList = new ArrayList<>();
        List<Order> orderList = orderApi.getOrderDataForChannel(channelId);
        if (orderList.isEmpty()) {
//            throw new ApiException("No Order found.");
            return null;
        }
        for (Order order : orderList) {
            OrderDataResponse orderDataResponse = ConvertUtil.convert(order, OrderDataResponse.class);
            if (orderDataResponse != null) {
                ChannelDataResponse channel = channelClient.getChannelDetails(channelId);
                Client client = clientApi.getClientDetails(order.getClientId());
                Client customer = clientApi.getClientDetails(order.getCustomerId());
                orderDataResponse.setClientName(client.getName());
                orderDataResponse.setCustomerName(customer.getName());
                orderDataResponse.setChannelId(channelId);
                orderDataResponse.setChannelName(channel.getName());
                orderDataResponseList.add(orderDataResponse);
            }
        }
        return orderDataResponseList;
    }

    @Transactional(readOnly = true)
    public List<OrderDataResponse> getAllOrderData() {
        logger.info("Getting order data.");
        List<OrderDataResponse> orderDataResponseList = new ArrayList<>();
        List<Order> orderList = orderApi.getAllOrderData();
        if (orderList.isEmpty()) {
            throw new ApiException("No Order found.");
        }
        for (Order order : orderList) {
            OrderDataResponse orderDataResponse = ConvertUtil.convert(order, OrderDataResponse.class);
            if (orderDataResponse != null) {
                Client client = clientApi.getClientDetails(order.getClientId());
                Client customer = clientApi.getClientDetails(order.getCustomerId());
                ChannelDataResponse channel = channelClient.getChannelDetails(order.getChannelId());
                orderDataResponse.setClientName(client.getName());
                orderDataResponse.setCustomerName(customer.getName());
                orderDataResponse.setChannelName(channel.getName());
                orderDataResponseList.add(orderDataResponse);
            }
        }
        logger.info("order data response : " + orderDataResponseList.size());
        return orderDataResponseList;
    }

    @Transactional(readOnly = true)
    public List<OrderDataResponse> getInternalChannelAllOrders() {
        List<OrderDataResponse> orderDataResponseList = new ArrayList<>();
        List<Order> orderList = orderApi.getAllOrderData();
        if (orderList.isEmpty()) {
            throw new ApiException("No Order found.");
        }
        for (Order order : orderList) {
            OrderDataResponse orderDataResponse = ConvertUtil.convert(order, OrderDataResponse.class);
            if (orderDataResponse != null) {
                String channelName = "INTERNAL";
                Client client = clientApi.getClientDetails(order.getClientId());
                Client customer = clientApi.getClientDetails(order.getCustomerId());
                orderDataResponse.setClientName(client.getName());
                orderDataResponse.setCustomerName(customer.getName());
                orderDataResponse.setChannelId(1L);
                orderDataResponse.setChannelName(channelName);
            }
            orderDataResponseList.add(orderDataResponse);
        }
        return orderDataResponseList;
    }

    @Transactional(readOnly = true)
    public List<OrderItemDataResponse> getAllOrderItemDetails(Long orderId) {
        List<OrderItem> orderItemList = itemApi.getOrderItemByOrderId(orderId);
        List<OrderItemDataResponse> listingDataResponseList = new ArrayList<>();
        for (OrderItem item : orderItemList) {
            Product product = productApi.getProductById(item.getGlobalSkuId());
            OrderItemDataResponse itemDataResponse = ConvertUtil.convert(product, OrderItemDataResponse.class);
            if (itemDataResponse != null) {
                itemDataResponse.setSkuId(product.getClientSkuId());
                BeanUtils.copyProperties(item, itemDataResponse);
                listingDataResponseList.add(itemDataResponse);
            }
        }
        return listingDataResponseList;
    }

    @Transactional
    public List<OrderDataResponse> allocateOrders() {
        int recordsUpdated = orderAllocationLogic();
        logger.info("Count of Newly Allocated Orders : " + recordsUpdated);
        return getAllOrderData();
    }

    @Transactional(readOnly = true)
    public List<OrderItemDataResponse> getAllOrderItemsData(Long orderId) {
        List<OrderItem> orderItemList = itemApi.getOrderItemByOrderId(orderId);
        return ConvertUtil.convert(orderItemList, OrderItemDataResponse.class);
    }

    @Transactional
    public List<OrderDataResponse> fulfillOrders(Order order, ChannelDataResponse channel) {
        List<InvoiceOrderItemData> invoiceItemList;
        boolean result = orderItemFulfillmentLogic(order);
        if (!result) {
            return getAllOrderData();
        }
        order.setStatus(OrderStatus.FULFILLED);
        orderApi.updateOrder(order);
        logger.info("here");
        if(channel.getInvoiceType() == InvoiceType.SELF){
            logger.info("self here");
            invoiceItemList = getFulfilledOrderItemDataForInvoice(order.getId());
            InvoiceData data = populateInvoiceData(order, invoiceItemList);
            pdfCreator(data);
            logger.info("self done here");
        }else {
            logger.info("channel here");
            OrderDataResponse orderData = ConvertUtil.convert(order, OrderDataResponse.class);
            if (orderData != null) {
                Client client = clientApi.getClientDetails(order.getClientId());
                Client customer = clientApi.getClientDetails(order.getCustomerId());
                orderData.setChannelId(channel.getId());
                orderData.setChannelName(channel.getName());
                orderData.setCustomerName(customer.getName());
                orderData.setClientName(client.getName());
                logger.info("making channel call here");
                channelClient.postOrderData(orderData);
                logger.info("channel call done");
            }
        }
        return getAllOrderData();
    }

    @Transactional
    public int orderAllocationLogic() {
        int count = 0;
        List<Order> orderList = orderApi.getOrdersByStatus(OrderStatus.CREATED);
        if (orderList.isEmpty()) {
            return 0;
        }
        for (Order order : orderList) {
            boolean result = checkOrderInventoryStatus(order);
            if (result) {
                logger.info("Changing the status of the order(" + order.getId() + ") to Allocated.");
                order.setStatus(OrderStatus.ALLOCATED);
                orderApi.updateOrder(order);
                count++;
            }
        }
        return count;
    }

    @Transactional(rollbackFor = ApiException.class)
    public boolean checkOrderInventoryStatus(Order order) {
        List<OrderItem> itemPojoList = itemApi.getOrderItemByOrderId(order.getId());
        for (OrderItem item : itemPojoList) {
            Long orderedQuantity = item.getOrderedQuantity();
            Long availQuantity = inventoryApi.getInventoryDetails(item.getGlobalSkuId()).getAvailableQuantity();
            if (orderedQuantity > availQuantity) {
                logger.info("OrderedQuantity is more than available quantity for GlobalSkuId : " + item.getGlobalSkuId() + " and item Id : " + item.getId());
                return false;
            }
        }
        for (OrderItem item : itemPojoList) {
            boolean check = orderItemAllocationLogic(item);
            if (!check) {
                throw new ApiException("Not Enough inventory to allot all orderItems for orderId :" + order.getId());
            }
        }
        return true;
    }

    @Transactional(rollbackFor = ApiException.class)
    public boolean orderItemAllocationLogic(OrderItem orderItem) {
        Inventory inventory = inventoryApi.getInventoryDetails(orderItem.getGlobalSkuId());
        List<BinSku> skuList = binSkuApi.getAllBinSkuDetails(orderItem.getGlobalSkuId());
        if (skuList == null) {
            throw new ApiException("No BinSku records found.");
        }
        Long remaining_available_quantity = Math.subtractExact(inventory.getAvailableQuantity(), orderItem.getOrderedQuantity());
        Long ordered_quantity = orderItem.getOrderedQuantity();
        if (remaining_available_quantity >= 0) {
            inventory.setAvailableQuantity(remaining_available_quantity);
            inventory.setAllocatedQuantity(inventory.getAllocatedQuantity() + orderItem.getOrderedQuantity());
            orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() + ordered_quantity);
            Long min_Val;
            for (BinSku binSku : skuList) {
                if (ordered_quantity > 0) {
                    if (binSku.getQuantity() >= ordered_quantity) {
                        binSku.setQuantity(Math.subtractExact(binSku.getQuantity(), ordered_quantity));
                        ordered_quantity = 0L;
                    } else {
                        min_Val = Math.min(binSku.getQuantity(), ordered_quantity);
                        ordered_quantity = Math.subtractExact(ordered_quantity, min_Val);
                        binSku.setQuantity(0L);
                    }
                }
                binSkuApi.updateBinSku(binSku);
            }
        }
        inventoryApi.updateInventory(inventory);
        itemApi.updateOrderItem(orderItem);
        return orderItem.getAllocatedQuantity().equals(orderItem.getOrderedQuantity());
    }

    @Transactional(rollbackFor = ApiException.class)
    public boolean orderItemFulfillmentLogic(Order order) {
        List<OrderItem> itemList = itemApi.getOrderItemByOrderId(order.getId());
        for (OrderItem orderItem : itemList) {
            Inventory inventory = inventoryApi.getInventoryDetails(orderItem.getGlobalSkuId());
            Long ordered_quantity = orderItem.getOrderedQuantity();
            if (orderItem.getAllocatedQuantity() - ordered_quantity < 0) {
                throw new ApiException("Order-Item Allocated Quantity is less than Ordered Quantity.");
            }
            inventory.setAllocatedQuantity(inventory.getAllocatedQuantity() - ordered_quantity);
            inventory.setFulfilledQuantity(inventory.getFulfilledQuantity() + ordered_quantity);
            inventoryApi.updateInventory(inventory);
            orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() - ordered_quantity);
            orderItem.setFulfilledQuantity(orderItem.getFulfilledQuantity() + ordered_quantity);
            itemApi.updateOrderItem(orderItem);
        }
        return true;
    }

    @Transactional(rollbackFor = ApiException.class)
    public List<InvoiceOrderItemData> getFulfilledOrderItemDataForInvoice(Long orderId) {
        logger.info("invoice method..");
        Order order = orderApi.getOrderDetails(orderId);
        List<InvoiceOrderItemData> invoiceItemList = new ArrayList<>();
        List<OrderItem> itemList = itemApi.getOrderItemByOrderId(order.getId());
        for (OrderItem item : itemList) {
            logger.info("OrderItem Id : " + item.getId());
            Product product = productApi.getProductById(item.getGlobalSkuId());
            InvoiceOrderItemData invoiceItem = ConvertUtil.convert(product, InvoiceOrderItemData.class);
            if (invoiceItem != null) {
                BeanUtils.copyProperties(item, invoiceItem);
                invoiceItem.setProductName(product.getName());
                invoiceItemList.add(invoiceItem);
                logger.info("OQty : "+invoiceItem.getOrderedQuantity());
            }
        }
        logger.info("list: " + invoiceItemList.size());
        return invoiceItemList;
    }

    public void pdfCreator(InvoiceData data) {
        try {
            PDFHandler.createInvoicePdf(data, PDF_PATH, INVOICE_TEMPLATE_XSL);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getClass());
        }
    }

    public OrderItem convertFormToOrderItemPojo(OrderItemForm form, Long globalSkuId, Long orderId) {
        OrderItem orderItem = ConvertUtil.convert(form, OrderItem.class);
        if (orderItem != null) {
            orderItem.setGlobalSkuId(globalSkuId);
            orderItem.setOrderId(orderId);
        }
        return orderItem;
    }

    public OrderItem convertChannelFormToOrderItemPojo(ChannelOrderItemForm form, Double mrp, Long orderId) {
        OrderItem orderItem = ConvertUtil.convert(form, OrderItem.class);
        if (orderItem != null) {
            orderItem.setSellingPricePerUnit(mrp);
            orderItem.setOrderId(orderId);
        }
        return orderItem;
    }

    public InvoiceData populateInvoiceData(Order order, List<InvoiceOrderItemData> invoiceItemList) {
        InvoiceData data = new InvoiceData();
        Client client = clientApi.getClientDetails(order.getClientId());
        Client customer = clientApi.getClientDetails(order.getCustomerId());
        InvoiceOrderData orderData = new InvoiceOrderData();
        orderData.setOrderId(order.getId());
        orderData.setChannelOrderId(order.getChannelOrderId());
        orderData.setClientName(client.getName());
        orderData.setCustomerName(customer.getName());
        orderData.setChannelId(order.getChannelId());
        data.setOrderData(orderData);
        data.setInvoiceOrderItemDataList(invoiceItemList);
        return data;
    }

    public Order convertFormToOrderPojo(OrderForm form) {
        Order order = ConvertUtil.convert(form, Order.class);
        if (order != null) {
            Client client = clientApi.getClientDetails(form.getClientId());
            order.setClientId(client.getId());
            order.setStatus(OrderStatus.CREATED);
        }
        return order;
    }

    public Order convertChannelFormToOrderPojo(ChannelOrderForm form) {
        Order order = ConvertUtil.convert(form, Order.class);
        if (order != null) {
            Client client = clientApi.getClientDetails(form.getClientId());
            order.setClientId(client.getId());
            order.setStatus(OrderStatus.CREATED);
        }
        return order;
    }

    @Transactional(readOnly = true)
    public Client getClientDetails(Long clientId) {
        return clientApi.getClientDetails(clientId);
    }

    @Transactional(readOnly = true)
    public Order getOrderDetails(String channelOrderId) {
        return orderApi.getOrderDetails(channelOrderId);
    }

    @Transactional(readOnly = true)
    public Product getProduct(String clientSkuId, Long clientId) {
        return productApi.getProductDetails(clientSkuId, clientId);
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long globalSkuId) {
        return productApi.getProductById(globalSkuId);
    }

    public byte[] getPDfInBytes(String fileName) throws IOException {
        byte[] fileInBytes;
        File file = new File(String.valueOf(Paths.get(PDF_PATH+fileName+".pdf")));
        if(!(file.exists() && file.isFile())) {
            byte[] clientByteResponse = invoicePdfClient.getInvoicePDF(fileName);
            if(clientByteResponse == null) {
                throw new ApiException("Invoice Pdf not found with name : "+ fileName);
            }
            logger.info("got pdf "+ clientByteResponse.length);
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(clientByteResponse);
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            bos.write(clientByteResponse);
//            bos.flush();
//            fos.close();
//            logger.info(file.exists());
            return Base64.getEncoder().encode(clientByteResponse);
        }
        fileInBytes = Files.readAllBytes(Paths.get(PDF_PATH+fileName+".pdf"));
        logger.info(fileInBytes.length);
        return Base64.getEncoder().encode(fileInBytes);
    }

    public Order getOrderDetails(Long orderId) {
        return orderApi.getOrderDetails(orderId);
    }

    public ChannelDataResponse getChannelDetails(Long channelId) {
        return channelClient.getChannelDetails(channelId);
    }

    public List<ChannelDataResponse> getAllChannelData() {
        return channelClient.getAllChannelDetails();
    }
}
