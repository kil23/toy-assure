var baseUrl = $(location).attr("href");

function getListingUrl(){
     //$("meta[name=baseUrl]").attr("content")
	return baseUrl+"s";
}

function getClientList(){
	var url = $("meta[name=baseUrl]").attr("content") + "/api/client";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
            displayClientDropDownList(data);
	   },
	   error: handleAjaxError
	});
}

function getListingClientList(){
	var url = getListingUrl()+"-client";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayListingList(data);
	   },
	   error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;
var clientSkuList = [];

function processData(){
	var file = $('#fileName')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
	setTimeout(function() { $('#upload-modal').modal('hide'); }, 5000);
	clientSkuList = [];
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
	processCount++;
	if(indexVal1 > -1 && indexVal2 > -1){
        console.log("Duplicate ClientSkuId("+row.clientSkuId+") found.");
        row.error = "Duplicate ClientSkuId found."
        errorData.push(row);
        uploadRows();
    }else{
        clientSkuList.push(row.clientSkuId);
        row["clientId"] = document.forms["client-form"]["client"].value;
        var json = JSON.stringify(row);
        var url = getListingUrl();

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
                getListingClientList();
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

function displayListingList(data){
	var $tbody = $('#listing-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<a href="./channel/'+e.id+'"><button type="button" class="btn btn-danger" >Listings</button></a>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.name + '</td>'
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

//INITIALIZATION CODE
function init(){
	$('#refresh-data').click(getListingClientList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getListingClientList);
$(document).ready(getClientList);