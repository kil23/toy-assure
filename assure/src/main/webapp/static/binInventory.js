function getBinInventoryUrl(){
	var baseUrl = $(location).attr("href") //$("meta[name=baseUrl]").attr("content")
	return baseUrl + "-list";
}

function updateBinInventory(event){
	$('#update-bin-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#bin-inventory-update-form input[name=globalSkuId]").val();
	var url = getBinInventoryUrl() + "/" + id;

	//Set the values to update
	var $form = $("#bin-inventory-update-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBinInventoryList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function getBinInventoryList(){
	var url = getBinInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBinInventoryList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteBinInventory(id){
	var url = getBinInventoryUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getBinInventoryList();
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
	
	var json = JSON.stringify(row);
	var url = getBinInventoryUrl();

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
	   		$('#statusDiv').html('<span style="color:green;">!! Success !!</span>').show();
            setTimeout(function() { $('#statusDiv').hide();}, 5000);
            getBinInventoryList();
	   },
	   error: function(response){
	        $('#statusDiv').html('<span style="color:red;">!! Error !!</span>').show();
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

function displayBinInventoryList(data){
	var $tbody = $('#bin-inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button type="button" class="btn btn-danger" onclick="deleteBinInventory(' + e.binSkuId + ')">Delete</button>'
		buttonHtml += ' <button type="button" class="btn btn-warning" onclick="displayEditBinInventory(' + e.binSkuId + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.binSkuId + '</td>'
		+ '<td>' + e.binId + '</td>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditBinInventory(id){
	var url = getBinInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBinInventory(data);
	   },
	   error: handleAjaxError
	});	
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

function displayBinInventory(data){
	$("#bin-inventory-update-form input[name=quantity]").val(data.quantity);
	$("#bin-inventory-update-form input[name=originalQuantity]").val(data.quantity);
	$("#bin-inventory-update-form input[name=binId]").val(data.binId);
    $("#bin-inventory-update-form input[name=binSkuId]").val(data.binSkuId);
    $("#bin-inventory-update-form input[name=globalSkuId]").val(data.globalSkuId);
	$('#update-bin-inventory-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#update-bin-inventory').click(updateBinInventory);
	$('#refresh-data').click(getBinInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getBinInventoryList);