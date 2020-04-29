var baseUrl = $(location).attr("href");

function getChannelListingUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

function getListingList(){
	var url = baseUrl+"/listings"
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayListingList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteListing(id){
	var url = baseUrl + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getListingList();
	   },
	   error: handleAjaxError
	});
}

//UI DISPLAY METHODS

function displayListingList(data){
	var $tbody = $('#listing-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button type="button" class="btn btn-danger" onclick="deleteListing(' + e.listingId + ')">Delete</button>'
		var row = '<tr>'
		+ '<td>' + e.listingId + '</td>'
		+ '<td>' + e.channelSkuId + '</td>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getListingList);
}

$(document).ready(init);
$(document).ready(getListingList);