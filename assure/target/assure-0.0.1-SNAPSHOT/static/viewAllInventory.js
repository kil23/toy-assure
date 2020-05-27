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
	        $.toast({
                heading: 'Info',
                text: 'Inventory list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
	   		        displayAllInventoryList(data);
                }
            });
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayAllInventoryList(data){
	var $thead = $('#inventory-table').find('thead');
	var $tbody = $('#inventory-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	var head = '<tr>'
	+   '<th scope="col">Inventory ID</th>'
	+   '<th scope="col">#</th>'
	+   '<th scope="col">Product</th>'
	+   '<th scope="col">Brand</th>'
	+   '<th scope="col">Available Qty</th>'
	+   '<th scope="col">Allocated Qty</th>'
	+   '<th scope="col">Fulfilled Qty</th>'
	+   '</tr>';
	$thead.append(head);
    var count = 1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.brandId + '</td>'
		+ '<td>' + e.availableQuantity + '</td>'
        + '<td>' + e.allocatedQuantity + '</td>'
        + '<td>' + e.fulfilledQuantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count++;
	}
}

//INITIALIZATION CODE
function init(){
    getAllInventoryList();
}

$(document).ready(init);