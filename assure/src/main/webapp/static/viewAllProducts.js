function getAllProductsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

function getAllProductsList(){
	var url = getAllProductsUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayAllProductsList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayAllProductsList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>' + e.clientId + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.brandId + '</td>'
        + '<td>' + e.mrp + '</td>'
        + '<td>' + e.description + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getAllProductsList);
}

$(document).ready(init);
$(document).ready(getAllProductsList);