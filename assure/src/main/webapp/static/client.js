var baseUrl = $("meta[name=baseUrl]").attr("content")
var regex = /^[a-zA-Z]{3,15}[-\\s]?[a-zA-Z0-9]{1,10}$/
var form_original_data;

function getClientUrl(){
	return baseUrl + "/api/client";
}

//BUTTON ACTIONS
function addClient(event){
    var jsonArray = {};
    if(validateFields()){
        var $form = $("#client-create-form");
        var formArray = ($form).serializeArray();
        console.log(formArray);
        var url = getClientUrl();
        var arr = [];
        arr.push(getFormData(formArray))
        console.log(arr);
        jsonArray.client = arr;
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
                    text: 'Client info added.',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 2000,
                    icon: 'success',
                    allowToastClose: true,
                    afterShown: function () {
                        document.getElementById("client-create-form").reset();
                        $('#add-client-modal').modal('toggle');
                        getAllClientList();
                    }
                });
            },
            error: function(jqXHR){
                errorData = handleAjaxError(jqXHR);
                console.log(errorData);
                if(errorData.length > 1){
                    document.getElementById('download-errors').focus();
                }
                document.getElementById("client-create-form").reset();
//                handleAjaxError(response);
//                $('#add-client-modal').modal('toggle');
//                document.getElementById("client-create-form").reset();
            }
        });
        arr = [];
        return;
    }
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

function updateClient(event){
    if ($("#client-update-form").serialize() == form_original_data) {
        $.toast({
            heading: 'Info',
            text: 'No data edited.',
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 2000,
            icon: 'info',
            allowToastClose: true,
            afterHidden: function () {
                document.getElementById("client-update-form").reset();
                $('#update-client-modal').modal('toggle');
                getAllClientList();
            }
        });
    }else {
        var id = $("#client-update-form input[name=id]").val();
        var url = getClientUrl() + "/" + id;
        var $form = $("#client-update-form");
        var json = toJson($form);
        console.log(json);
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
                    text: 'Client info updated.',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 2000,
                    icon: 'success',
                    allowToastClose: true,
                    afterShown: function () {
                        document.getElementById("client-update-form").reset();
                        $('#update-client-modal').modal('toggle');
                        getAllClientList();
                    }
                });
            },
            error: function(jqXHR){
                errorData = handleAjaxError(jqXHR);
                console.log(errorData);
                if(errorData.length > 1){
                    document.getElementById('download-errors').focus();
                }
                document.getElementById("client-update-form").reset();
//                handleAjaxError(response);
//                document.getElementById("client-update-form").reset();
//                $('#update-client-modal').modal('toggle');
            }
        });
    }
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

function getAllClientList(){
	var url = baseUrl + "/api/all-client";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Client list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayClientList(data);
                }
            });
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
            $.toast({
                heading: 'Info',
                text: 'Client list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayClientList(data);
                }
            });
        },
        error: handleAjaxError
	});
}

function getCustomerList(){
	var url = baseUrl + "/api/customer";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Client list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayClientList(data);
                }
            });
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
}

function uploadRows(){
	updateUploadDialog();
    var formData = {};
    var dataArr = [];
    var jsonArray = {};

	for (var i = 0; i < fileData.length; i++){
        if(i>=5000){
            return;
        }
        dataArr.push(fileData[i])
    }
    formData = dataArr;
    console.log(JSON.stringify(formData));
	jsonArray.client = formData;
	var json = JSON.stringify(jsonArray);
	console.log(json)
	var url = getClientUrl();

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
                text: 'Client info added.',
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
            $.toast({
                heading: 'Failed',
                text: 'Failed to add Client info. Please try again.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: false,
                icon: 'error',
                allowToastClose: true,
                afterShown: function () {
                    $('#upload-modal').modal('toggle');
                }
            });
        }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayClientList(data){
	var $thead = $('#client-table').find('thead');
	var $tbody = $('#client-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	var head = '<tr>'
    +   '<th scope="col">ID</th>'
    +   '<th scope="col">#</th>'
    +   '<th scope="col">Name</th>'
    +   '<th scope="col">Type</th>'
    +   '</tr>';
    $thead.append(head);
    var count = 1
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button type="button" class="btn btn-warning" onclick="displayEditClient(' + e.id + ')"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span>Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.type + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count++;
	}
}

function displayEditClient(id){
	var url = getClientUrl() + "/" + id;
	console.log(url);
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

function displayCreateClient(){
	$('#add-client-modal').modal('toggle');
	document.getElementById("client-create-form").reset();
}

function validateFields(){
   if (document.getElementById('inputName').value==null || document.getElementById('inputName').value.trim()=="") {
       infoToast("Please enter Client name value.");
       document.getElementById('inputName').focus();
       return false;
   }else if(!regex.test(document.getElementById('inputName').value)){
        infoToast("Please enter valid Client name.");
        document.getElementById('inputName').focus();
        return false;
   }
    var typeDropDown = $("#typeSelect");
    console.log(typeDropDown.val());
    if (typeDropDown.val() == '' || typeDropDown.val() == undefined || typeDropDown.val() == 'Select') {
      infoToast("Please select Client Type from the list and then proceed!");
      document.getElementById('typeSelect').focus();
      return false;
    }
   return true;
}

function displayClient(data){
	$("#client-update-form input[name=name]").val(data.name);
	$("#client-update-form select[name=type]").val(data.type);
	$("#client-update-form input[name=id]").val(data.id);
	$('#update-client-modal').modal('toggle');
	form_original_data = $("#client-update-form").serialize();
}

//INITIALIZATION CODE
function init(){
	$('#add-client').click(addClient);
	$('#add-client-info').click(displayCreateClient);
	$('#update-client').click(updateClient);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName);
    $('select[name="typeFilter"]').change(function(){
        var type = $(this).find("option:selected").val();
        if(type === "CLIENT") {
            getClientList();
        }else if(type === "CUSTOMER"){
            getCustomerList();
        }else if(type === "ALL") {
            getAllClientList();
        } else {
            return;
        }
    });
}

$(document).ready(init);
$(document).ready(getAllClientList);