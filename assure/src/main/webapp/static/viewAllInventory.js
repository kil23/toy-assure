function getAllInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/all-inventories";
}

function getAllInventoryList(){
	var url = getAllInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayAllInventoryList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayAllInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.inventoryId + '</td>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + e.availableQuantity + '</td>'
        + '<td>' + e.allocatedQuantity + '</td>'
        + '<td>' + e.fulfilledQuantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getAllInventoryList);
}

$(document).ready(init);
$(document).ready(getAllInventoryList);