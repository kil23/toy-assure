var regex = /^[a-zA-Z]{3,15}[-\\s]?[a-zA-Z0-9]{1,10}$/

function getChannelUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/channel";
}

function validateFields(){
    if (document.getElementById('inputName').value.trim()=="") {
//        document.getElementById('errorDiv').innerHTML =
        infoToast("Name must be filled out");
        document.getElementById('inputName').focus();
        return false;
    }
    return true;
}

//BUTTON ACTIONS
function addChannel(event){
    var jsonArray = {};
	if(!validateFields()){
        return;
    }
	var $form = $("#channel-create-form");
	var formArray = ($form).serializeArray();
    console.log(formArray);
    var url = getChannelUrl();
    var arr = [];
    arr.push(getFormData(formArray));
    console.log(arr);
    jsonArray.channel = arr;
    console.log(jsonArray);
    var json = JSON.stringify(jsonArray);
    console.log(json);

	$.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },
        success: function(response) {
            $.toast({
                heading: 'Success',
                text: 'Channel info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 2000,
                icon: 'success',
                allowToastClose: true,
                afterShown: function () {
                    document.getElementById("channel-create-form").reset();
                    $('#add-channel-modal').modal('toggle');
                    getChannelList();
                }
            });
        },
        error: function(jqXHR) {
            handleAjaxError(jqXHR);
            document.getElementById("channel-create-form").reset();
        }
	});
    return;
}

//utility function
function getFormData(data) {
   var unindexed_array = data;
   var indexed_array = {};

   $.map(unindexed_array, function(n, i) {
    indexed_array[n['name']] = n['value'];
   });

   return indexed_array;
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
            $.toast({
                heading: 'Success',
                text: 'Channel info updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 2000,
                icon: 'success',
                allowToastClose: true,
                afterShown: function () {
                    $('#update-channel-modal').modal('toggle');
                    getChannelList();
                }
            });
        },
        error: function(jqXHR) {
            errorData = handleAjaxError(jqXHR);
            console.log(errorData);
            if(errorData.length > 1){
                document.getElementById('download-errors').focus();
            }
        }
	});

	return false;
}

function getChannelList(){
	var url = getChannelUrl();
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Channel list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayChannelList(data);
                }
            });
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

function infoToast(infoText) {
    $.toast({
        heading: 'Info',
        text: infoText,
        position: 'bottom-right',
        showHideTransition: 'fade',
        hideAfter: 5000,
        icon: 'info',
        allowToastClose: true
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
	getChannelList();
}

function uploadRows(){

	updateUploadDialog();

    var dataArr = [];
    var formData = {};

	for (var i = 0; i < fileData.length; i++){
        if(i>=5000){
            return;
        }
        dataArr.push(fileData[i])
    }
    formData = dataArr;
    console.log(JSON.stringify(formData));

    var json = JSON.stringify(formData);
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
            $.toast({
                heading: 'Success',
                text: 'Channel info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 2000,
                icon: 'success',
                allowToastClose: true,
                afterHidden: function () {
                    $('#upload-modal').modal('toggle');
                    getClientList();
                }
            });
        },
        error: function(response){
            $('#statusDiv').html('<span style="color:red;">!! Error !!</span>').show();
            return;
        }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayChannelList(data){
	var $thead = $('#channel-table').find('thead');
	var $tbody = $('#channel-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	var head = '<tr>'
	+   '<th scope="col">ID</th>'
	+   '<th scope="col">Name</th>'
	+   '<th scope="col">Type</th>'
	+   '<th scope="col">View</th>'
//	+   '<th scope="col">Actions</th>'
	+   '</tr>';
    $thead.append(head);
    var count = 1;
	for(var i in data){
		var e = data[i];
		if(e.name !==("INTERNAL")){
		    var buttonHtml1 = '<a href = "../channel/'+e.id+'/listing" ><button type="button" class="btn btn-outline-primary">Channel Listings</button></a>'
//            var buttonHtml2 = ' <button type="button" class="btn btn-outline-warning" onclick="displayEditChannel(' + e.id + ')">Edit</button>'
            var row = '<tr>'
            + '<td>' + count + '</td>'
            + '<td>' + e.name + '</td>'
            + '<td>' + e.invoiceType + '</td>'
            + '<td>' + buttonHtml1 + '</td>'
//            + '<td>' + buttonHtml2 + '</td>'
            + '</tr>';
            $tbody.append(row);
            count++;
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

function displayCreateClient(){
	$('#add-channel-modal').modal('toggle');
}

function validateFields(){
   if (document.getElementById('inputName').value==null || document.getElementById('inputName').value.trim()=="") {
       infoToast("Please enter Channel name value.");
       document.getElementById('inputName').focus();
       return false;
   }else if(!regex.test(document.getElementById('inputName').value)){
        infoToast("Please enter valid Channel name.");
        document.getElementById('inputName').focus();
        return false;
   }
    var typeDropDown = $("#typeSelect");
    console.log(typeDropDown.val());
    if (typeDropDown.val() == '' || typeDropDown.val() == undefined || typeDropDown.val() == 'Select') {
        infoToast("Please select Invoice Type from the list to proceed!");
        document.getElementById('typeSelect').focus();
        return false;
    }
   return true;
}

function displayChannel(data){
	$("#channel-update-form input[name=name]").val(data.name);
	$("#channel-update-form select[name=invoiceType]").val(data.invoiceType);
	$("#channel-update-form input[name=id]").val(data.id);
	$('#update-channel-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#add-channel').click(addChannel);
	$('#add-channel-info').click(displayCreateClient);
	$('#update-channel').click(updateChannel);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getChannelList);