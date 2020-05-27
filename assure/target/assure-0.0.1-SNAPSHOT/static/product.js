var regexNum = /^[0-9]*\.[0-9]*$/
var regexStr = /^[a-zA-Z]{3,15}[-\\s]?[a-zA-Z0-9]{1,10}$/
var errorArray = [];

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

var currentUrl = $(location).attr("href");
var newUrl = currentUrl.replace('ui', 'api');
var apiUrl = newUrl+'s';
var form_original_data;

function getClientId(){
    return $( "#clientSelected option:selected" ).val();
}

function updateProduct(event){
    if ($("#product-update-form").serialize() == form_original_data) {
        $.toast({
            heading: 'Info',
            text: 'No data edited.',
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 2000,
            icon: 'info',
            allowToastClose: true,
            afterShown: function () {
                document.getElementById("product-update-form").reset();
                $('#update-product-modal').modal('toggle');
            },
            afterHidden: function () {
                getProductList(getClientId());
            }
        });
    }else {
        if(!validateUpdateFields()){
            return;
        }
        var id = $("#product-update-form input[name=globalSkuId]").val();
        var url = apiUrl+'/'+id;

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
                $.toast({
                    heading: 'Success',
                    text: 'Product info updated.',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 2000,
                    icon: 'success',
                    allowToastClose: true,
                    afterHidden: function () {
                        document.getElementById("product-update-form").reset();
                        $('#update-product-modal').modal('toggle');
                        getProductList(getClientId());
                    }
                });
            },
            error: function(jqXHR) {
                errorArray = [];
                errorArray = handleAjaxError(jqXHR);
                if(errorArray.length > 1){
                    document.getElementById('download-errors').focus();
                }
                document.getElementById("product-update-form").reset();
            }
        });
        return false;
    }
}

function getClientList(){
	var url = getProductUrl() + "/api/client";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayClientDropDownList(data);
        },
        error: handleAjaxError
	});
}

function getProductList(id){
	var url = apiUrl+"/list/"+id
	console.log(url);
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Product list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayProductList(data);
                }
            });
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
    if(validateFields()){
        var file = $('#fileName')[0].files[0];
        readFileData(file, readFileDataCallback);
	}
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	updateUploadDialog();
    var client_id = document.forms["add-product-form"]["client"].value;
    var dataArr = [];
    var jsonArray = {};
    for (var i = 0; i < fileData.length; i++){
        if(i>=5000){
            return;
        }
        fileData[i].clientId = client_id;
        dataArr.push(fileData[i])
    }
    console.log(dataArr);
    jsonArray.product = dataArr;
//    jsonArray.clientId = client_id;
	var json = JSON.stringify(jsonArray);
	console.log(json);
	var url = apiUrl;

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
                text: 'Product info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 2000,
                icon: 'success',
                allowToastClose: true,
                afterHidden: function () {
                    document.getElementById("add-product-form").reset();
                    $('#add-product-modal').modal('toggle');
                    getProductList(client_id);
                }
            });
        },
        error: function(jqXHR){
            errorArray = [];
            errorArray = handleAjaxError(jqXHR);
            console.log(errorArray);
            if(errorArray != null && errorArray.length > 1){
                document.getElementById('download-errors').focus();
            }
            document.getElementById("add-product-form").reset();
        }
	});
}

function downloadErrors(){
	writeErrors(errorArray);
}

//UI DISPLAY METHODS

function displayProductList(data){
	var $thead = $('#product-table').find('thead');
	var $tbody = $('#product-table').find('tbody');
	$thead.empty();
	$tbody.empty();
	var head = '<tr>'
	+   '<th scope="col">GlobalSkuId</th>'
	+   '<th scope="col">#</th>'
	+   '<th scope="col">Client SKU ID</th>'
	+   '<th scope="col">Product</th>'
	+   '<th scope="col">Brand</th>'
	+   '<th scope="col">MRP</th>'
	+   '<th scope="col">Description</th>'
	+   '<th scope="col">Actions</th>'
	+   '</tr>';
    $thead.append(head);
    var count = 1;
	for(var i in data){
		var e = data[i];
//		var buttonHtml = '<button type="button" class="btn btn-danger" onclick="deleteProduct(' + e.globalSkuId + ')">Delete</button>'
		var buttonHtml = ' <button type="button" class="btn btn-outline-warning" onclick="displayEditProduct(' + e.globalSkuId +')">Edit</button>'
		var row = '<tr>'
		+ '<td>' + e.globalSkuId + '</td>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.clientSkuId + '</td>'
		+ '<td>' + e.name + '</td>'
        + '<td>' + e.brandId + '</td>'
        + '<td>' + parseFloat(e.mrp).toFixed(2) + '</td>'
        + '<td>' + e.description + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count++;
	}
}

function displayEditProduct(id){
	var url = apiUrl + "/" + id;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayProduct(data);
        },
        error: handleAjaxError
	});	
}

function displayClientDropDownList(data){
    $('#clientSelect').empty();
    $('#clientSelected').empty();
    var options = '<option value="0" selected>Select Client</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
    $('#clientSelected').append(options);
    document.getElementById('clientSelected').focus();
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
}

function displayAddProduct() {
	$('#add-product-modal').modal('toggle');
    document.getElementById("add-product-form").reset();
	getClientList();
	displayUploadData();
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

function validateUpdateFields() {
    if (document.getElementById('inputName').value==null || document.getElementById('inputName').value.trim()=="") {
       infoToast("Please enter Product name value.");
       document.getElementById('inputName').focus();
       return false;
    }else if(!regexStr.test(document.getElementById('inputName').value)){
         infoToast("Please enter valid Product name value.");
         document.getElementById('inputName').focus();
         return false;
    }
    if (document.getElementById('inputBrandId').value==null || document.getElementById('inputBrandId').value.trim()=="") {
       infoToast("Please enter Brand ID value.");
       document.getElementById('inputBrandId').focus();
       return false;
    }
    if(!regexNum.test(document.getElementById('inputMrp').value)){
        document.getElementById('inputMrp').value = '0.00';
        infoToast("Please enter valid Product Mrp value.");
        document.getElementById('inputMrp').focus();
        return false;
    }
    return true;
}

function displayProduct(data){
	$("#product-update-form input[name=name]").val(data.name);
	$("#product-update-form input[name=brandId]").val(data.brandId);
	$("#product-update-form input[name=mrp]").val(parseFloat(data.mrp).toFixed(2));
	$("#product-update-form input[name=description]").val(data.description);
	$("#product-update-form input[name=clientId]").val(data.clientId);
    $("#product-update-form input[name=clientSkuId]").val(data.clientSkuId);
    $("#product-update-form input[name=globalSkuId]").val(data.globalSkuId);
	$('#update-product-modal').modal('toggle');
	form_original_data = $("#product-update-form").serialize();
}

//INITIALIZATION CODE
function init(){
	$('#add-product-info').click(displayAddProduct);
	$('#update-product').click(updateProduct);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName)
    $('select[name="clientDropdown"]').change(function(){
        var clientId = $(this).find("option:selected").val();
        if(clientId==0) {
            return;
        }
        console.log(clientId);
        getProductList(clientId);
    });
    infoToast("Please select Client to view Product record.");
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
    $("#inputMrp").change(function() {
        $(this).val(parseFloat($(this).val()).toFixed(2));
    });
}

$(document).ready(init);
$(document).ready(getClientList);