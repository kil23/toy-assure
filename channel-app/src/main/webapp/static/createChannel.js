function getChannelUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel";
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
function addChannel(event){
	//Set the values to update
	if(!validateFields()){
	    setTimeout(function () {
            document.getElementById('errorDiv').innerHTML = "";
        }, 3000);
        return;
    }
	var $form = $("#channel-create-form");
	var json = toJson($form);
	var url = getChannelUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getChannelList();
	   },
	   error: handleAjaxError
	});
    document.getElementById("channel-create-form").reset();
    return;
}

function updateChannel(event){
	$('#update-channel-modal').modal('toggle');
	//Get the ID
	var id = $("#channel-update-form input[name=id]").val();
	var url = getChannelUrl() + "/" + id;

	//Set the values to update
	var $form = $("#channel-update-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getChannelList();
	   },
	   error: handleAjaxError
	});

	return false;
}

function getChannelList(){
	var url = getChannelUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannelList(data);
	   },
	   error: handleAjaxError
	});
}

function deleteChannel(id){
	var url = getChannelUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getChannelList();
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
	var url = getChannelUrl();

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
	   		getChannelList();
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

function displayChannelList(data){
	var $tbody = $('#channel-table').find('tbody');
	$tbody.empty();
    var count = 1;
	for(var i in data){
		var e = data[i];
		if(e.name !==("INTERNAL")){
		    var buttonHtml = '<a href = "../api/'+e.id+'/listing" ><button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>Channel-Listings</button></a>'
            buttonHtml += ' <button type="button" class="btn btn-warning" onclick="displayEditChannel(' + e.id + ')"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>Edit</button>'
            var row = '<tr>'
            + '<td>' + count + '</td>'
            + '<td>' + e.name + '</td>'
            + '<td>' + e.type + '</td>'
            + '<td>' + buttonHtml + '</td>'
            + '</tr>';
            $tbody.append(row);
            count = count + 1;
		}

	}
}

function displayEditChannel(id){
	var url = getChannelUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayChannel(data);
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

function displayChannel(data){
	$("#channel-update-form input[name=name]").val(data.name);
	$("#channel-update-form select[name=type]").val(data.type);
	$("#channel-update-form input[name=id]").val(data.id);
	$('#update-channel-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-channel').click(addChannel);
	$('#update-channel').click(updateChannel);
	$('#refresh-data').click(getChannelList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getChannelList);