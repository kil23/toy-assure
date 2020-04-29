function getProductUrl(){
	var baseUrl = $(location).attr("href") //$("meta[name=baseUrl]").attr("content")
	console.log(baseUrl);
	return baseUrl+"s";
}

function updateProduct(event){
	$('#update-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-update-form input[name=globalSkuId]").val();
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-update-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();
	   },
	   error: handleAjaxError
	});
    document.getElementById("product-create-form").reset();
	return false;
}

function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteProduct(id){
	var url = getProductUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();
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
	var url = getProductUrl();

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
            setTimeout(function() { $("#statusDiv").hide();}, 5000);
            getProductList();
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

function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button type="button" class="btn btn-danger" onclick="deleteProduct(' + e.globalSkuId + ')">Delete</button>'
		buttonHtml += ' <button type="button" class="btn btn-warning" onclick="displayEditProduct(' + e.globalSkuId + ')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>' + e.clientId + '</td>'
		+ '<td>' + e.name + '</td>'
        + '<td>' + e.brandId + '</td>'
        + '<td>' + e.mrp + '</td>'
        + '<td>' + e.description + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditProduct(id){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);
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

function displayProduct(data){
	$("#product-update-form input[name=name]").val(data.name);
	$("#product-update-form input[name=brandId]").val(data.brandId);
	$("#product-update-form input[name=mrp]").val(data.mrp);
	$("#product-update-form input[name=description]").val(data.description);
    $("#product-update-form input[name=clientSkuId]").val(data.clientSkuId);
    $("#product-update-form input[name=globalSkuId]").val(data.globalSkuId);
	$('#update-product-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getProductList);