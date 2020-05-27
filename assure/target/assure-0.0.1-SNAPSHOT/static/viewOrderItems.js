function getAllOrderItemsUrl(){
	var baseUrl = $(location).attr("href") //$("meta[name=baseUrl]").attr("content")
    	return baseUrl + "-list";
}

var currentUrl = $(location).attr("href");
var newUrl = currentUrl.replace('ui', 'api');
var apiUrl = newUrl+'-list';

function getAllOrderItemsList(){
	var url = apiUrl;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		$.toast({
                heading: 'Info',
                text: 'Order-Item list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
	   		        displayAllOrderItemsList(data);
                }
            });
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayAllOrderItemsList(data){
	var $thead = $('#order-item-table').find('thead');
	var $tbody = $('#order-item-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	var head = '<tr>'
	+   '<th scope="col">Global SKU Id</th>'
	+   '<th scope="col">#</th>'
	+   '<th scope="col">Client SKU ID</th>'
	+   '<th scope="col">Product</th>'
	+   '<th scope="col">Brand</th>'
	+   '<th scope="col">Ordered Qty</th>'
//	+   '<th scope="col">Allocated Qty</th>'
//	+   '<th scope="col">Fulfilled Qty</th>'
	+   '<th scope="col">Price Per Unit</th>'
	+   '</tr>';
    $thead.append(head);
    var count = 1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.skuId + '</td>'
        + '<td>' + e.name + '</td>'
        + '<td>' + e.brandId + '</td>'
		+ '<td>' + e.orderedQuantity + '</td>'
//		+ '<td>' + e.allocatedQuantity + '</td>'
//		+ '<td>' + e.fulfilledQuantity + '</td>'
		+ '<td>' + parseFloat(e.sellingPricePerUnit).toFixed(2) + '</td>'
		+ '</tr>';
        $tbody.append(row);
        console.log(e.sellingPricePerUnit)
        count++;
	}
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getAllOrderItemsList);
	$('#invoice-data').click(getAllOrderItemsList);
}

$(document).ready(init);
$(document).ready(getAllOrderItemsList);