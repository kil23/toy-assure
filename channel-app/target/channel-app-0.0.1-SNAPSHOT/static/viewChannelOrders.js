var baseUrl = $("meta[name=baseUrl]").attr("content")
function getChannelOrderUrl(){
	return baseUrl + "/api/channel/order-lists";
}

var count = 1;

function getChannelOrderList(id){
	var url = baseUrl + "/api/"+id+"/order";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function getChannelOrderListByClientId(id){
	var url = baseUrl + "/api/order/client/"+id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function getChannelList(){
	var url = baseUrl + "/api/channel";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayChannelDropDownData(data);
        },
        error: handleAjaxError
	});
}

function displayChannelDropDownData(data){
    $('#channelSelect').empty();
    var options = '';
    options = '<option value="0">Select Channel</option>';
    $.each(data, function(index, value) {
        if(value.id!=1){
            options += '<option value="' + value.id + '">' + value.name + '</option>';
        }
    });
    $('#channelSelect').append(options);
}

function getClientList(){
	var url = baseUrl + "/api/client";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayClientDropDownList(data);
        },
        error: handleAjaxError
	});
}

function displayClientDropDownList(data){
    $('#clientSelect').empty();
    $('#clientSelectFilter').empty();
    var options = '<option value="0" selected>Select Client</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
    $('#clientSelectFilter').append(options);
}

function downloadPdf(id){
	var url = $("meta[name=baseUrl]").attr("content") + "/api/download/order" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		var sampleArr = base64ToArrayBuffer(data);
            saveByteArray("Sample Report", sampleArr);
	   },
	   error: handleAjaxError
	});
}
function saveByteArray(reportName, byte) {
    var blob = new Blob([byte], {type: "application/pdf"});
    var link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    var fileName = reportName;
    link.download = fileName;
    link.click();
}

function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
       var ascii = binaryString.charCodeAt(i);
       bytes[i] = ascii;
    }
    return bytes;
 }

function loadOrderItem(id,channelId) {
    $(location).attr('href', baseUrl+"/channel/"+channelId+"/order/"+id+"/item-list");
}

//UI DISPLAY METHODS

function displayChannelOrderList(data){
	var $thead = $('#order-table').find('thead');
	var $tbody = $('#order-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	var head = '<tr>'
	+   '<th scope="col"></th>'
	+   '<th scope="col">#</th>'
	+   '<th scope="col">CustomerId</th>'
	+   '<th scope="col">Channel</th>'
	+   '<th scope="col">Client</th>'
	+   '<th scope="col">Channel Order ID</th>'
	+   '<th scope="col">Status</th>'
	+   '<th scope="col">Invoice</th>'
	+   '<th scope="col">View</th>'
	+   '</tr>';
	$thead.append(head);
    var count = 1;
    var buttonHtml1;
	for(var i in data){
		var e = data[i];
		if(e.status === 'FULFILLED') {
            buttonHtml1 = '<button type="button" id="invoiceBtn'+count+'" class="btn btn-outline-warning" onclick="downloadPdf('+e.id+')">Download</button>'
        }else{
            buttonHtml1 = '<button type="button" id="invoiceBtn'+count+'" class="btn btn-outline-warning" disabled>Download</button>'
        }
        var buttonHtml2 = '<button type="button" class="btn btn-outline-secondary" onclick="loadOrderItem('+ e.id +','+ e.channelId +')">Order-Items</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.customerName + '</td>'
		+ '<td>' + e.channelName + '</td>'
		+ '<td>' + e.clientName + '</td>'
        + '<td>' + e.channelOrderId + '</td>'
        + '<td>' + e.status + '</td>'
        + '<td>' + buttonHtml1 + '</td>'
        + '<td>' + buttonHtml2 + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count++;
	}
}

function init(){
    $('select[name="channel"]').change(function(){
        $("#clientSelectFilter").prop('selectedIndex',0);
        channelId = $(this).find("option:selected").val();
        if(channelId==0){
            return;
        }
        getChannelOrderList(channelId);
    });
    $('select[name="clientDropdown"]').change(function(){
        $("#channelSelect").prop('selectedIndex',0);
        var clientID = $(this).find("option:selected").val();
        if(clientID==0){
            return;
        }
        getChannelOrderListByClientId(clientID);
    });
}

$(document).ready(init);
$(document).ready(getChannelList);
$(document).ready(getClientList);