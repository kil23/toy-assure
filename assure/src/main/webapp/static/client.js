function getClientUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/client";
}

function validateFields(){
    if (document.getElementById('inputName').value.trim()=="") {
        document.getElementById('errorDiv').innerHTML = "Name must be filled out";
        document.getElementById('inputName').focus();
        return false;
    }
    return true;
}

//BUTTON ACTIONS
function addClient(event){
	//Set the values to update
	if(!validateFields()){
	    setTimeout(function () {
            document.getElementById('errorDiv').innerHTML = "";
        }, 3000);
        return;
    }
	var $form = $("#client-create-form");
	var json = toJson($form);
	var url = getClientUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getClientList();
	   },
	   error: handleAjaxError
	});
    document.getElementById("client-create-form").reset();
	return;
}

function updateClient(event){
	$('#update-client-modal').modal('toggle');
	//Get the ID
	var id = $("#client-update-form input[name=id]").val();
	var url = getClientUrl() + "/" + id;

	//Set the values to update
	var $form = $("#client-update-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getClientList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function getClientList(){
	var url = getClientUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayClientList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteClient(id){
	var url = getClientUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getClientList();
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
	var url = getClientUrl();

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
	   		getClientList();
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

function displayClientList(data){
	var $tbody = $('#client-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<a href = "../api/'+e.id+'/product" ><button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>Products</button></a>'
		buttonHtml += ' <button type="button" class="btn btn-warning" onclick="displayEditClient(' + e.id + ')"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.type + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditClient(id){
	var url = getClientUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayClient(data);
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

function displayClient(data){
	$("#client-update-form input[name=name]").val(data.name);
	$("#client-update-form select[name=type]").val(data.type);
	$("#client-update-form input[name=id]").val(data.id);
	$('#update-client-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-client').click(addClient);
	$('#update-client').click(updateClient);
	$('#refresh-data').click(getClientList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getClientList);