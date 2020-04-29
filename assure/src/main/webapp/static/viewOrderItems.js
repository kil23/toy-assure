function getAllOrderItemsUrl(){
	var baseUrl = $(location).attr("href") //$("meta[name=baseUrl]").attr("content")
    	return baseUrl + "-list";
}

function getAllOrderItemsList(){
	var url = getAllOrderItemsUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayAllOrderItemsList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayAllOrderItemsList(data){
	var $tbody = $('#order-item-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + e.clientSkuId + '</td>'
        + '<td>' + e.name + '</td>'
        + '<td>' + e.brandId + '</td>'
		+ '<td>' + e.orderedQuantity + '</td>'
		+ '<td>' + e.allocatedQuantity + '</td>'
		+ '<td>' + e.fulfilledQuantity + '</td>'
		+ '<td>' + e.sellingPricePerUnit + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getAllOrderItemsList);
}

$(document).ready(init);
$(document).ready(getAllOrderItemsList);