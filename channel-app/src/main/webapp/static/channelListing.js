var baseUrl = $(location).attr("href");

function getListingUrl(){
     //$("meta[name=baseUrl]").attr("content")
    var newUrl = baseUrl.replace('channel', 'api');
	return newUrl;
}

function getClientId(){
    return $( "#clientSelected option:selected" ).val();
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

function deleteListing(id){
	var url = getListingUrl() + "/" + getClientId() + "/"+ id;
    console.log(url);
	$.ajax({
        url: url,
        type: 'DELETE',
        success: function(data) {
            getListings(getClientId());
        },
        error: handleAjaxError
	});
}

function getListings(id){
	var url = getListingUrl()+"/"+id;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            if(data.length > 0){
                $.toast({
                    heading: 'Info',
                    text: 'Channel list updated.',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 2000,
                    icon: 'info',
                    allowToastClose: true,
                    afterShown: function () {
                        displayListings(data);
                    }
                });
            }else {
                $.toast({
                    heading: 'Info',
                    text: 'No Data Found',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 5000,
                    icon: 'info',
                    allowToastClose: true
                });
            }
        },
        error: function(data) {
            $.toast({
                heading: 'Info',
                text: 'No Data Found',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 5000,
                icon: 'info',
                allowToastClose: true
            });
        }
    });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var errorArray = [];
var processCount = 0;

function processData(){
    if(!validateFields()) {
        return;
    }
	var file = $('#fileName')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
	clientSkuList = [];
}

function uploadRows(){

	updateUploadDialog();

	var clientSkuList = [];
    var dataArr = [];
    var jsonArray = {};

	for (var i = 0; i < fileData.length; i++){
        if(i>=5000){
            return;
        }
//        var row = fileData[i];
//        var indexVal1 = jQuery.inArray(row.clientSkuId, clientSkuList);
//        processCount++;
//        if(indexVal1 > -1){
//            console.log("Duplicate ClientSkuId("+row.clientSkuId+") found.");
//            row.error = "Duplicate ClientSkuId found."
//            errorData.push(row);
//        }else{
//            clientSkuList.push(row.clientSkuId);
//            dataArr.push(fileData[i]);
//        }
        dataArr.push(fileData[i]);
    }
    console.log(dataArr);
    jsonArray.listing = dataArr;
    var client_Id = document.forms["add-listing-form"]["client"].value;
    jsonArray.clientId = client_Id;
    console.log(JSON.stringify(jsonArray));
    var json = JSON.stringify(jsonArray);
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
            $.toast({
                heading: 'Success',
                text: 'Channel info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 2000,
                icon: 'success',
                allowToastClose: true,
                afterHidden: function () {
                    $('#add-listing-modal').modal('toggle');
                    document.getElementById("add-listing-form").reset();
                    getListings(client_Id);
                }
            });
        },
        error: function(jqXHR){
            errorArray = [];
            errorArray = handleAjaxError(jqXHR);
            if(errorArray != null && errorArray.length > 1){
                document.getElementById('download-errors').focus();
            }
            document.getElementById("add-listing-form").reset();
        }
    });
}

function downloadErrors(){
	writeErrors(errorArray);
}

//UI DISPLAY METHODS

function displayListings(data){

    var $thead = $('#listing-table').find('thead');
    var $tbody = $('#listing-table').find('tbody');
    $tbody.empty();
    $thead.empty();
    var head = '<tr>'
    +   '<th scope="col">Listing Id</th>'
    +   '<th scope="col">#</th>'
    +   '<th scope="col">Channel SKU ID</th>'
    +   '<th scope="col">Product</th>'
    +   '<th scope="col">Brand</th>'
//    +   '<th scope="col">Actions</th>'
    +   '</tr>';
    $thead.append(head);
    var count = 1;
    for(var i in data){
        var e = data[i];
        var buttonHtml = '<button type="button" class="btn btn-danger" onclick="deleteListing(' + e.id + ')">Delete</button>'
        var row = '<tr>'
        + '<td>' + e.id + '</td>'
        + '<td>' + count + '</td>'
        + '<td>' + e.channelSkuId + '</td>'
        + '<td>' + e.name + '</td>'
        + '<td>' + e.brandId + '</td>'
//        + '<td>' + buttonHtml + '</td>'
        + '</tr>';
        $tbody.append(row);
        count++;
    }
}

function displayClientDropDownList(data){
    $('#clientSelect').empty();
    $('#clientSelected').empty();
    var options = '<option value="" selected>Select Client</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
    $('#clientSelected').append(options);
}

function resetUploadDialog(){
	var $file = $('#fileName');
	$file.val('');
	$('#csvFileName').html("Choose File");
	processCount = 0;
	fileData = [];
	errorData = [];
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

function infoToast(errorText) {
    $.toast({
        heading: 'Info',
        text: errorText,
        position: 'bottom-right',
        showHideTransition: 'fade',
        hideAfter: 5000,
        icon: 'info',
        allowToastClose: true
    });
}

function validateFields(){
   var typeDropDown = $("#clientSelect");
   if (typeDropDown.val() == '' || typeDropDown.val() == undefined || typeDropDown.val() == 0) {
       infoToast("Please select Client from the dropdown to proceed!");
       document.getElementById('clientSelect').focus();
       return false;
   }
   if( document.getElementById("fileName").files.length == 0 ){
       infoToast("No file selected.Please choose a file to proceed.");
       return false;
   }
   return true;
}

function displayUploadData(){
 	resetUploadDialog();
}

function displayAddListing() {
	$('#add-listing-modal').modal('toggle');
	displayUploadData();
}

//INITIALIZATION CODE
function init(){
    $('#add-listing-info').click(displayAddListing);
	$('#upload-data').click(processData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName);
    $('select[name="clientDropdown"]').change(function(){
        var clientId = $(this).find("option:selected").val();
        if(clientId==0) {
            return;
        }
        console.log(clientId);
        getListings(clientId);
    });
    infoToast("Please select Client to view Channel listings.");
    var selectedFile = document.getElementById('fileName');
    selectedFile.onchange = function(e){
       switch ((this.value.match(/\.([^\.]+)$/i)[1]).toLowerCase()) {
           case 'csv':
           case 'txt':
           return true;
           default:
               /* Here notify user that this file extension is not suitable */
               infoToast("Please select csv or text file.");
               this.value=''; /* ... and erase the file */
               $('#csvFileName').html('');
               return false;
       }
    };
}

$(document).ready(init);
$(document).ready(getClientList);