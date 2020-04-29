package com.assure.service;

import com.assure.dao.OrderDao;
import com.assure.model.response.InvoiceData;
import com.assure.model.response.InvoiceOrderItemData;
import com.assure.pojo.*;
import com.assure.socket.Channel;
import com.commons.enums.OrderStatus;
import com.commons.form.OrderForm;
import com.commons.response.OrderDataResponse;
import com.commons.service.ApiException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderService.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private BinSkuService skuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderItemService itemService;

    public static OrderDataResponse convertPojoToFormData(Order pojo, String clientName, String channelName){
        logger.info("Converting Order-pojo to its form");
        OrderDataResponse data = new OrderDataResponse();
        data.setOrderId(pojo.getId());
        data.setClientId(pojo.getClientId());
        data.setClientName(clientName);
        data.setCustomerId(pojo.getCustomerId());
        data.setChannelId(pojo.getChannelId());
        data.setChannelName(channelName);
        data.setChannelOrderId(pojo.getChannelOrderId());
        data.setStatus(pojo.getStatus());
        return data;
    }


    public Order convertFormToOrderPojo(OrderForm form) throws ApiException {
        logger.info("Converting Order-form to its pojo");
        Client client = clientService.getClient(form.getClientId());
        Order order = new Order();
        order.setChannelId(client.getId());
        order.setChannelId(form.getChannelId());
        order.setCustomerId(form.getCustomerId());
        order.setChannelOrderId(form.getChannelOrderId());
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    @Transactional(rollbackOn = ApiException.class)
    public synchronized void insertOrder(OrderForm form) throws ApiException {
        OrderItem orderItem;
        Long newFlag = form.getNewFlag();
        logger.info("New-flag : "+newFlag);
        Order orderToInsert = convertFormToOrderPojo(form);
        Order order_db = orderDao.getOrderByChannelOrderId(orderToInsert.getChannelOrderId().trim());
        if (order_db == null) {
            logger.info("Adding new order record.");
            Order order = orderDao.insertOrder(orderToInsert);
            logger.info("Order inserted with id "+order.getId());
            if(newFlag>=2){
                logger.info("Channel Orders.");
                Product product = productService.getProductById(form.getGlobalSkuId());
                if(product !=null) {
                    orderItem = itemService.convertFormToOrderItemPojo(form, product.getMrp(), order.getId());
                }else{
                    logger.info("Product not found for globalSkuId("+form.getGlobalSkuId()+")");
                    throw new ApiException("Product not found. Please add Product first.");
                }
            }else{
                logger.info("UI Orders.");
                Product product = productService.getProduct(form.getClientSkuId(), order.getClientId());
                if(product!=null){
                    logger.info("Product found in db for new order : "+product.getGlobalSkuId());
                    orderItem = itemService.convertFormToOrderItemPojo(form, product.getGlobalSkuId(), order.getId());
                }else{
                    logger.info("Product not found for clientSkuId("+form.getClientSkuId()+") and clientId("+order.getClientId()+")");
                    throw new ApiException("Product not found for clientSkuId("+form.getClientSkuId()+")");
                }
            }
            itemService.insertOrderItems(orderItem);
        }else if(newFlag==0){
            // adding more Order line items to the table
            logger.info("Order record already present in db with id : "+ order_db.getId());
            Product product = productService.getProduct(form.getClientSkuId(), orderToInsert.getClientId());
            if(product!=null){
                logger.info("Product found in db for new order : "+product.getGlobalSkuId());
                orderItem = itemService.convertFormToOrderItemPojo(form, product.getGlobalSkuId(), order_db.getId());
                itemService.insertOrderItems(orderItem);
            }else{
                logger.info("Product not found for clientSkuId("+form.getClientSkuId()+") and clientId("+orderToInsert.getClientId()+")");
                throw new ApiException("Product not found for clientSkuId("+form.getClientSkuId()+")");
            }
        }else if (newFlag>=2){
            logger.info("Order record already present in db with id : "+ order_db.getId());
            Product product = productService.getProductById(form.getGlobalSkuId());
            if(product !=null) {
                orderItem = itemService.convertFormToOrderItemPojo(form, product.getMrp(), order_db.getId());
                logger.info("inserting-channel-order");
                itemService.insertOrderItems(orderItem);
            }else{
                logger.info("Product not found for globalSkuId("+form.getGlobalSkuId()+")");
                throw new ApiException("Product not found. Please add Product first.");
            }
        }else{
            logger.info("Duplicate channelOrderId("+orderToInsert.getChannelOrderId()+") found.");
            throw new ApiException("Duplicate channelOrderId("+orderToInsert.getChannelOrderId()+") found.");
        }
    }

    public List<OrderDataResponse> getOrderDataForChannel(Long channelId) throws ApiException {
        logger.info("get-order-for-channel");
        List<Order> pojoList = orderDao.getOrderByChannelId(channelId);
        List<OrderDataResponse> list2 = new ArrayList<>();
        if(pojoList.isEmpty()){
            logger.info("No Order found with channelId : "+ channelId);
            return null;
        }else{
            for (Order pojo : pojoList) {
                String clientName = clientService.getClient(pojo.getClientId()).getName();
                logger.info("ClientName : "+clientName);
                list2.add(convertPojoToFormData(pojo, clientName, null));
            }
            logger.info("returning..");
            return list2;
        }
    }

    public List<OrderDataResponse> getAllOrderData() throws ApiException {
        logger.info("get-All-orders-data");
        List<Order> list1 = orderDao.getAllOrders();
        List<OrderDataResponse> list2 = new ArrayList<>();
        if(list1.isEmpty()){
            logger.info("No Orders found.");
            throw new ApiException("No Order found.");
        }else{
            for (Order pojo : list1) {
                String clientName = clientService.getClient(pojo.getClientId()).getName();
                String channelName = Objects.requireNonNull(Channel.getChannelDetails(pojo.getChannelId())).getName();
                logger.info("ClientName : "+clientName+" and ChannelName : "+channelName);
                list2.add(convertPojoToFormData(pojo, clientName, channelName));
            }
            return list2;
        }
    }

    public Order getOrderById(Long orderId) throws ApiException {
        logger.info("get-order-by-id");
        Order order = orderDao.getOrderById(orderId);
        if(order==null){
            logger.info("No Order found.");
            throw new ApiException("No Order found.");
        }
        return order;
    }

    public List<OrderDataResponse> getInternalChannelAllOrders() throws ApiException {
        logger.info("get-All-orders");
        List<Order> list1 = orderDao.getAllOrders();
        List<OrderDataResponse> list2 = new ArrayList<>();
        if(list1.isEmpty()){
            logger.info("No Orders found.");
            return null;
        }else{
            for (Order pojo : list1) {
                String clientName = clientService.getClient(pojo.getClientId()).getName();
                String channelName = "Internal";
                logger.info("ClientName : "+clientName+" and ChannelName : "+channelName);
                list2.add(convertPojoToFormData(pojo, clientName, channelName));
            }
            return list2;
        }
    }

    @Transactional(rollbackOn  = ApiException.class)
    public List<OrderDataResponse> allocateOrders() throws ApiException {
        logger.info("Allocate orders and return all orders");
        int recordsUpdated = orderAllocationLogic();
        logger.info("Count of Newly Allocated Orders : "+recordsUpdated);
        return getAllOrderData();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public List<OrderDataResponse> fulfillOrders() throws ApiException {
        logger.info("Fulfill allocated orders and return all orders");
        int recordsUpdated = orderFulfillmentLogic();
        logger.info("Count of Newly Fulfilled Orders : "+recordsUpdated);
        return getAllOrderData();
    }

    @Transactional(rollbackOn  = ApiException.class)
    private int orderFulfillmentLogic() throws ApiException {
        logger.info("Order Fulfillment logic method...");
        int count = 0;
        List<Order> pojoList = orderDao.getOrdersByStatus(OrderStatus.ALLOCATED);
        if(pojoList.isEmpty()){
            logger.info("No Orders found with status : 'Allocated'");
            throw new ApiException("No Orders found with status : 'Allocated'");
        }else{
            for(Order order : pojoList){
                boolean result = orderItemFulfillmentLogic(order);
                if(result){
                    logger.info("Changing the status of the order("+order.getId()+") to Fulfilled.");
                    order.setStatus(OrderStatus.FULFILLED);
                    orderDao.updateOrder(order);
                    createFulfilledOrderInvoice(order);
                    count++;
                }
            }
        }
        return count;
    }

    @Transactional(rollbackOn  = ApiException.class)
    public int orderAllocationLogic() throws ApiException {
        logger.info("Order Allocation logic method...");
        int count = 0;
        List<Order> orderList = orderDao.getOrdersByStatus(OrderStatus.CREATED);
        if(orderList.isEmpty()){
            logger.info("No Orders found in state : 'Created'");
        }else{
            for(Order order : orderList){
                boolean result = checkOrderInventoryStatus(order);
                if(result){
                    logger.info("Changing the status of the order(" + order.getId() + ") to Allocated.");
                    order.setStatus(OrderStatus.ALLOCATED);
                    orderDao.updateOrder(order);
                    count++;
                }else{
                    logger.info("Status of Order with Id("+order.getId()+") is not changed due to lacking inventory.");
                }
            }
        }
        return count;
    }

    @Transactional(rollbackOn  = ApiException.class)
    public boolean checkOrderInventoryStatus(Order order) throws ApiException {
        List<OrderItem> itemPojoList = itemService.getOrderItemByOrderId(order.getId());
        logger.info("size of itemPojoList: "+itemPojoList.size());
        for(OrderItem item : itemPojoList){
            logger.info("globalSkuId : "+item.getGlobalSkuId());
            Long orderedQuantity = item.getOrderedQuantity();
            Long availQuantity = inventoryService.getInventoryByGlobalSkuId(item.getGlobalSkuId()).getAvailableQuantity();
            logger.info("OQ : "+ orderedQuantity +" and AQ : "+availQuantity);
            if(orderedQuantity>availQuantity){
                logger.info("OrderedQuantity is more than available quantity for GlobalSkuId : "+item.getGlobalSkuId()+" and item Id : "+ item.getId());
                return false;
            }
        }
        for(OrderItem item : itemPojoList){
            boolean check = orderItemAllocationLogic(item);
            if(!check) {
                logger.info("Not Enough inventory to allot all orderItems for orderId :" + order.getId());
                throw new ApiException("Not Enough inventory to allot all orderItems for orderId :" + order.getId());
            }
        }
        return true;
    }


    public boolean orderItemAllocationLogic(OrderItem orderItem) throws ApiException {
        logger.info("Order-item Allocation logic method...");

        Inventory inventory = inventoryService.getInventoryByGlobalSkuId(orderItem.getGlobalSkuId());
        List<BinSku> skuList = skuService.getAllBinSkuByGlobalSkuId(orderItem.getGlobalSkuId());

        Long remaining_available_quantity = inventory.getAvailableQuantity() - orderItem.getOrderedQuantity();
        Long ordered_quantity = orderItem.getOrderedQuantity();
        if (remaining_available_quantity >= 0) {
            //Case where AvailableQuantity is more than OrderedQuantity
            inventory.setAvailableQuantity(remaining_available_quantity);
            inventory.setAllocatedQuantity(inventory.getAllocatedQuantity() + orderItem.getOrderedQuantity());
            orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() + ordered_quantity);
            Long min_Val;
            for (BinSku binSku : skuList) {
                if (ordered_quantity > 0) {
                    if (binSku.getQuantity() >= ordered_quantity) {
                        binSku.setQuantity(binSku.getQuantity() - ordered_quantity);
                        ordered_quantity = 0L;
                    } else {
                        min_Val = Math.min(binSku.getQuantity(), ordered_quantity);
                        ordered_quantity = ordered_quantity - min_Val;
                        binSku.setQuantity(0L);
                    }
                }
                logger.info("Updating binSku record : "+binSku.getId());
                skuService.updateBinSku(binSku);
            }
        } else {
            //Case where OrderedQuantity is more than AvailableQuantity
            logger.info("Available-Quantity is less than the Ordered-Quantity for the OrderItem Id :"+ orderItem.getId());
            logger.warn("No inventory is being allocated then for OrderId : "+ orderItem.getOrderId());
        }
        //updating inventory table with new values
        logger.info("Updating inventory record : "+ inventory.getId());
        inventoryService.updateInventory(inventory);

        //updating OrderItem table with new values
        logger.info("Updating orderItem record : "+ orderItem.getId());
        itemService.updateOrderItem(orderItem);
        if(orderItem.getAllocatedQuantity() != orderItem.getOrderedQuantity()){
            logger.info("AllocatedQuantity and OrderedQuantity does not match.");
            return false;
        }
        return true;
    }

    @Transactional(rollbackOn  = ApiException.class)
    public boolean orderItemFulfillmentLogic(Order order) throws ApiException {
        logger.info("Order-item Fulfillment logic method");
        List<OrderItem> itemPojoList = itemService.getOrderItemByOrderId(order.getId());
        for(OrderItem orderItem : itemPojoList) {
            Inventory inventory = inventoryService.getInventoryByGlobalSkuId(orderItem.getGlobalSkuId());

            Long ordered_quantity = orderItem.getOrderedQuantity();
            if(inventory.getAllocatedQuantity() - ordered_quantity >= 0){
                inventory.setAllocatedQuantity(inventory.getAllocatedQuantity() - ordered_quantity);
            }else{
                throw new ApiException("Inventory Allocated Quantity is less than Ordered Quantity.");
            }
            if(orderItem.getAllocatedQuantity() - ordered_quantity >= 0){
                orderItem.setAllocatedQuantity(orderItem.getAllocatedQuantity() - ordered_quantity);
            }else{
                throw new ApiException("Order-Item Allocated Quantity is less than Ordered Quantity.");
            }
            inventory.setFulfilledQuantity(inventory.getFulfilledQuantity() + ordered_quantity);
            orderItem.setFulfilledQuantity(orderItem.getFulfilledQuantity() + ordered_quantity);

            //updating inventory table with new values
            logger.info("Updating inventory record : "+ inventory.getId());
            inventoryService.updateInventory(inventory);

            //updating OrderItem table with new values
            logger.info("Updating orderItemPojo record : "+ orderItem.getId());
            itemService.updateOrderItem(orderItem);
        }
        return true;
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void createFulfilledOrderInvoice(Order order) throws ApiException {
        logger.info("Creating pdf for Fulfilled Order.");
        List<InvoiceOrderItemData> invoiceItemList = new ArrayList<>();
        List<OrderItem> itemList = itemService.getOrderItemByOrderId(order.getId());
        for(OrderItem item : itemList){
            InvoiceOrderItemData invoiceItem = new InvoiceOrderItemData();
            Product product = productService.getProductById(item.getGlobalSkuId());
            invoiceItem.setProductName(product.getName());
            invoiceItem.setBrandId(product.getBrandId());
            invoiceItem.setClientSkuId(product.getClientSkuId());
            invoiceItem.setOrderId(item.getOrderId());
            invoiceItem.setFulfilledQuantity(item.getFulfilledQuantity());
            invoiceItem.setSellingPricePerUnit(item.getSellingPricePerUnit());
            invoiceItemList.add(invoiceItem);
        }
        logger.info("Invoice Item list : "+ invoiceItemList.size());
        pdfCreator(order, invoiceItemList);
    }

    public void pdfCreator(Order order, List<InvoiceOrderItemData> invoiceOrderItemDataList) throws ApiException {
        InvoiceData data = new InvoiceData();
        data.setOrder(order);
        data.setInvoiceOrderItemDataList(invoiceOrderItemDataList);
        logger.info("InvoiceData is updated with values.");

        byte[] byteData = PDFHandler.createInvoicePdf(data);
    }
}
