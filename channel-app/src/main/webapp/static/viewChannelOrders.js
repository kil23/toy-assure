function getChannelOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel/order-lists";
}

function getChannelOrderList(){
	var url = getChannelOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelOrderList(data);
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayChannelOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.customerId + '</td>'
		+ '<td>' + e.channelName + '</td>'
		+ '<td>' + e.clientName + '</td>'
        + '<td>' + e.channelOrderId + '</td>'
        + '<td>' + e.channelItem + '</td>'
        + '<td>' + e.quantity + '</td>'
        + '<td>' + e.status + '</td>'
        + '<td>' + e.invoicePdf + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getChannelOrderList);
}

$(document).ready(init);
$(document).ready(getChannelOrderList);