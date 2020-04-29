var baseUrl = $("meta[name=baseUrl]").attr("content");

function getBinUrl(){
	return baseUrl + "/api/bin";
}

//BUTTON ACTIONS
function addBin(event){
	//Set the values to update
	if(!validateFields()){
	    setTimeout(function () {
            document.getElementById('errorDiv').innerHTML = "";
        }, 3000);
        return;
    }
	var $form = $("#bin-create-form");
	var json = toJson($form);
	var url = getBinUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBinList();
	   },
	   error: handleAjaxError
	});
	document.getElementById("bin-create-form").reset();
	return;
}

function getBinList(){
	var url = getBinUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBinList(data);
	   },
	   error: handleAjaxError
	});
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

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;
var clientSkuList = [];
var binList = [];

function processData(){
	var file = $('#fileName')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	console.log(fileData)
	uploadRows();
	setTimeout(function() { $('#upload-modal').modal('hide'); }, 5000);
	clientSkuList = [];
	binList = [];
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
	var indexVal1 = jQuery.inArray(row.clientSkuId, clientSkuList);
	var indexVal2 = jQuery.inArray(row.binId, binList);
	processCount++;
    if(indexVal1 > -1 && indexVal2 > -1){
        console.log("Duplicate ClientSkuId("+row.clientSkuId+") found.");
	    row.error = "Duplicate ClientSkuId found."
	    errorData.push(row);
	    uploadRows();
	}else{
	    clientSkuList.push(row.clientSkuId);
	    binList.push(row.binId);
	    row["clientId"] = document.forms["client-form"]["client"].value;
        var json = JSON.stringify(row);
        var url = getBinUrl();

        //Make ajax call
        $.ajax({
           url: url + "/inventory",
           type: 'POST',
           data: json,
           headers: {
            'Content-Type': 'application/json'
           },
           success: function(response) {
                uploadRows();
                $('#statusDiv').html('<span style="color:green;">!! Success !!</span>').show();
                setTimeout(function() { $("#statusDiv").hide();}, 5000);
                getBinList();
           },
           error: function(response){
                $('#statusDiv').html('<span style="color:red;">!! Error !!</span>').show();
                row.error=response.responseText
                errorData.push(row);
                uploadRows();
           }
        });
	}
}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayBinList(data){
	var $tbody = $('#bin-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<a href = "../api/'+e.binId+'/bin-inventory" ><button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>Inventory</button></a>';
		var row = '<tr>'
		+ '<td>' + e.binId + '</td>'
		+ '<td>' + e.inventoryCount + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayClientDropDownList(data){
    var options = '';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });

    $('#clientSelect').append(options);
}

function displayEditBin(id){
	var url = getBinUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBin(data);
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
    if (document.getElementById('clientSelect').value == '' || document.getElementById('clientSelect').value == undefined) {
       document.getElementById('errorDiv').innerHTML = "Please select a client from the list and then proceed!";
       document.getElementById('clientSelect').focus();
       setTimeout(function () {
            document.getElementById('errorDiv').innerHTML = "";
       }, 3000);
       return false;
    }
 	resetUploadDialog(); 	
	$('#upload-modal').modal('toggle');
}

function validateFields(){

   if (document.getElementById('inputBin').value.trim()=="") {
       document.getElementById('errorDiv').innerHTML = "'No. of bins' must be filled out";
       document.getElementById('inputBin').focus();
       return false;
   }else if(/\D/.test(document.getElementById('inputBin').value)){
        document.getElementById('errorDiv').innerHTML = "'No. of bins' must be a number only.";
        document.getElementById('inputBin').focus();
        return false;
   }
   return true;
}

//INITIALIZATION CODE
function init(){
	$('#add-bin').click(addBin);
	$('#refresh-data').click(getBinList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getBinList);
$(document).ready(getClientList);