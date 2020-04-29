function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function updateOrder(event){
	$('#update-order-modal').modal('toggle');
	//Get the ID
	var id = $("#order-update-form input[name=id]").val();
	var url = getOrderUrl() + "/" + id;

	//Set the values to update
	var $form = $("#order-update-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList();
	   },
	   error: handleAjaxError
	});
    document.getElementById("order-create-form").reset();
	return false;
}

function allocateOrder(){
	var url = getOrderUrl()+"/allocate";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function fulfillOrder(){
	var url = getOrderUrl()+"/fulfill";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}

function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#fileName')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	console.log(fileData)
	uploadRows();
    setTimeout(function() { $('#upload-modal').modal('hide'); }, 5000);
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	console.log(row)
	
	var json = JSON.stringify(row);
	console.log(json)
	var url = getOrderUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();
	   		$('#statusDiv').html('<span style="color:green;">!! Success !!</p>').show();
            setTimeout(function() { $("#statusDiv").hide();}, 2000);
	   		getOrderList();
	   },
	   error: function(response){
	        $('#statusDiv').html('<span style="color:red;">!! Error !!</p>').show();
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<a href = "../api/'+e.orderId+'/item" ><button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>Order-Items</button></a>'
		var row = '<tr>'
		+ '<td>' + e.orderId + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>' + e.customerId + '</td>'
		+ '<td>' + e.channelName + '</td>'
        + '<td>' + e.channelOrderId + '</td>'
        + '<td>' + e.status + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#fileName');
	$file.val('');
	$('#csvFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#fileName');
	var fileName = $file.val();
	$('#csvFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-modal').modal('toggle');
}

function displayOrder(data){
	$("#order-update-form input[name=name]").val(data.name);
	$("#order-update-form select[name=type]").val(data.type);
	$("#order-update-form input[name=id]").val(data.id);
	$('#update-order-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#allocate-order').click(allocateOrder);
	$('#fulfill-order').click(fulfillOrder);
	$('#refresh-data').click(getOrderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getOrderList);