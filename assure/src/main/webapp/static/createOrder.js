var baseUrl = $("meta[name=baseUrl]").attr("content");
var regex = /^[a-zA-Z]{2,12}[-\s]?[0-9]{0,3}$/;

function getOrderUrl(){
	return baseUrl + "/api/order";
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
    var options = '';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
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
	var newFlag = 0;
	if(processCount==0){
	    newFlag = 1;
	}else{
	    newFlag = 0;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	row["clientId"] = document.forms["myForm"]["client"].value;
    row["customerId"] = document.forms["myForm"]["customerId"].value;
    row["channelOrderId"] = document.forms["myForm"]["channelOrderId"].value;
    row["channelId"] = document.forms["myForm"]["channelId"].value;
    row["newFlag"] = newFlag;
    console.log(row);
	var json = JSON.stringify(row);
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
	   		$('#statusDiv').html('<span style="color:green;">!! Success !!</span>').show();
            setTimeout(function() { $("#statusDiv").hide(); }, 5000);
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

function displayEditOrder(id){
	var url = getOrderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrder(data);
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
    if(!validateFields()){
        setTimeout(function () {
            document.getElementById('errorDiv').innerHTML = "";
        }, 3000);
        return;
    }
 	resetUploadDialog(); 	
	$('#upload-modal').modal('toggle');
}

function validateFields(){

   if (document.getElementById('clientSelect').value == '' || document.getElementById('clientSelect').value == undefined) {
       document.getElementById('errorDiv').innerHTML = "Please select a client from the list and then proceed!";
       document.getElementById('clientSelect').focus();
       return false;
   }
   if (document.getElementById('inputCustomerId').value==null || document.getElementById('inputCustomerId').value.trim()=="") {
       document.getElementById('errorDiv').innerHTML = "Customer-Id must be filled out";
       document.getElementById('inputCustomerId').focus();
       return false;
   }else if(document.getElementById('inputCustomerId').value <= 0){
        document.getElementById('errorDiv').innerHTML = "Customer-Id must a number greater than zero.";
        document.getElementById('inputCustomerId').focus();
        return false;
   }
   if (document.getElementById('inputChannelOrderId').value==null || document.getElementById('inputChannelOrderId').value.trim()=="") {
       document.getElementById('errorDiv').innerHTML = "Channel-Order-Id must be filled out";
       document.getElementById('inputChannelOrderId').focus();
       return false;
   }else if(!regex.test(document.getElementById('inputChannelOrderId').value)){
        document.getElementById('errorDiv').innerHTML = "Please enter valid Channel Order Id";
        document.getElementById('inputChannelOrderId').focus();
   }
   return true;
}

//INITIALIZATION CODE
function init(){
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getClientList);