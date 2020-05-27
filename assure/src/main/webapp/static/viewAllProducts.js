function getAllProductsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products/list";
}

function getAllProductsList(){
	var url = getAllProductsUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		$.toast({
                heading: 'Info',
                text: 'Product list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
	   		        displayAllProductsList(data);
                }
            });
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayAllProductsList(data){
	var $thead = $('#product-table').find('thead');
	var $tbody = $('#product-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	var head = '<tr>'
	+   '<th scope="col">GlobalSkuId</th>'
	+   '<th scope="col">#</th>'
	+   '<th scope="col">Client SKU ID</th>'
	+   '<th scope="col">Client</th>'
	+   '<th scope="col">Product</th>'
	+   '<th scope="col">Brand</th>'
	+   '<th scope="col">MRP</th>'
	+   '<th scope="col">Description</th>'
	+   '</tr>';
	$thead.append(head);
    var count = 1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<tr class="no-data">'
        + '<td colspan="4">No data</td>'
        + '</tr>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.brandId + '</td>'
        + '<td>' + e.mrp + '</td>'
        + '<td>' + e.description + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count++;
	}
}

//INITIALIZATION CODE
function init(){
}

$(document).ready(init);
$(document).ready(getAllProductsList);