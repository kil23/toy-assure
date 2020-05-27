var baseUrl = $("meta[name=baseUrl]").attr("content");
var binsID = 0;
var form_original_data;
var errorArray = [];

//function getBinInventoryUrl(){
//	var baseUrl = $(location).attr("href") //$("meta[name=baseUrl]").attr("content")
//	var newUrl = baseUrl.replace('ui', 'api');
//	return newUrl;
//}

function getBinId(){
    return $( "#bins option:selected" ).val();
}

function getClientId(){
    return $( "#clientSelectFilter option:selected" ).val();
}

function addBin(event){
	if(!validateFields()){
        return;
    }
	var $form = $("#bin-create-form");
	var json = toJson($form);
	var url = baseUrl + "/api/bin";

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
                text: 'Bin added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter:2000,
                icon: 'success',
                allowToastClose: true,
                afterShown: function () {
                    document.getElementById("bin-create-form").reset();
                    $('#add-bins-modal').modal('toggle');
                    getBinList();
                }
            });
        },
        error: handleAjaxError
	});
}

function updateBinInventory(event){
    if ($("#inventory-update-form").serialize() == form_original_data) {
        $.toast({
            heading: 'Info',
            text: 'No data edited.',
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 2000,
            icon: 'info',
            allowToastClose: true,
            afterHidden: function () {
                document.getElementById("inventory-update-form").reset();
                $('#update-inventory-modal').modal('toggle');
                getBinInventoryList(getBinId());
            }
        });
    }else {
        if(!validateUpdateInventoryFields()){
            return;
        }
        var id = $("#inventory-update-form input[name=globalSkuId]").val();
        var bin_id = $("#inventory-update-form input[name=binId]").val();
        var url = baseUrl+ "/api/"+ bin_id +"/inventory/" + id;

        var $form = $("#inventory-update-form");
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
                    text: 'Inventory info updated.',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 2000,
                    icon: 'success',
                    allowToastClose: true,
                    afterShown: function () {
                        document.getElementById("inventory-update-form").reset();
                        $('#update-inventory-modal').modal('toggle');
                        if(getBinId == 0){
                            getBinInventoryList(getBinId());
                        }else {
                            getInventoryList(getClientId());
                        }
                    }
                });
            },
            error: handleAjaxError
//            document.getElementById("inventory-update-form").reset();
        });
    }
}

function getBinInventoryList(binid){
    if(binid == 0) {
        return;
    }
	var url = baseUrl+ "/api/"+ binid + "/inventory"
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Inventory list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayBinInventoryList(data);
                }
            });
        },
        error: handleAjaxError
	});
}

function getInventoryList(clientID){
    if(clientID == 0) {
        return;
    }
	var url = baseUrl+ "/api/inventory/" + clientID;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Inventory list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 1000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayBinInventoryList(data);
                }
            });
        },
        error: handleAjaxError
	});
}

function deleteBinInventory(id){
	var url = baseUrl+ "/api/"+ getBinId() +"/inventory/" + id;
    console.log(url);
	$.ajax({
        url: url,
        type: 'DELETE',
        success: function(data) {
            getBinInventoryList(getBinId());
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

function getBinList(){
	var url = baseUrl + "/api/bin";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayBinDropDownList(data);
        },
        error: handleAjaxError
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;
var dataArr = [];

function processData(){
    if(!validateAddInventoryModal()){
        return;
    }
	var file = $('#fileName')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	updateUploadDialog();
    var clientSkuList = [];
    var binList = [];
    var dataArr = [];
    var jsonArray = {};

    for (var i = 0; i < fileData.length; i++){
        if(i>=5000){
            return;
        }
        processCount++;
        var row = fileData[i];
//        var indexVal1 = jQuery.inArray(row.clientSkuId, clientSkuList);
//        var indexVal2 = jQuery.inArray(row.binId, binList);
//
//        if(indexVal1 > -1 && indexVal2 > -1){
//            console.log("Duplicate ClientSkuId("+row.clientSkuId+") found.");
//            row.error = "Duplicate ClientSkuId found."
//            errorData.push(row);
//        }else{
//            clientSkuList.push(row.clientSkuId);
//            binList.push(row.binId);
//        }
          dataArr.push(fileData[i]);
    }
    jsonArray.inventory = dataArr;
    jsonArray.clientId = document.forms["add-inventory-form"]["client"].value;
    console.log(JSON.stringify(jsonArray));
    var json = JSON.stringify(jsonArray);

    $.ajax({
        url: baseUrl + "/api/inventory",
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
                    document.getElementById("add-inventory-form").reset();
                    $('#add-inventory-modal').modal('toggle');
                }
            });
        },
        error: function(jqXHR){
            errorArray = [];
            errorArray = handleAjaxError(jqXHR);
            if(errorArray != null && errorArray.length > 1){
                document.getElementById('download-errors').focus();
            }
            document.getElementById("add-inventory-form").reset();
            return;
        }
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

function downloadErrors(){
	writeErrors(errorArray);
}

//UI DISPLAY METHODS

function displayBinInventoryList(data){
	var $thead = $('#inventory-table').find('thead');
    var $tbody = $('#inventory-table').find('tbody');
    $thead.empty();
    $tbody.empty();
	var head = '<tr>'
	+   '<th scope="col">Bin SKU ID</th>'
	+   '<th scope="col">#</th>'
	+   '<th scope="col">Bin ID</th>'
	+   '<th scope="col">Product</th>'
	+   '<th scope="col">Brand</th>'
	+   '<th scope="col">Quantity</th>'
	+   '<th scope="col">Actions</th>'
	+   '</tr>';
	$thead.append(head);
    var count = 1;
	for(var i in data){
		var e = data[i];
//		var buttonHtml = '<button type="button" class="btn btn-danger" onclick="deleteBinInventory(' + e.id + ')">Delete</button>'
		var buttonHtml = ' <button type="button" class="btn btn-outline-primary" onclick="displayEditBinInventory(' + e.id + ')">Edit Info</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.binId + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.brandId + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count++;
	}
}

function displayEditBinInventory(id){
    if(id == 0) {
        return;
    }
	var url = baseUrl+ "/api/"+ getBinId() +"/inventory/" + id;
	console.log(url);
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayBinInventory(data);
        },
        error: handleAjaxError
	});	
}

function displayClientDropDownList(data){
    $('#clientSelect').empty();
    $('#clientSelectFilter').empty();
    var options = '';
    options = '<option value="0" selected>Select Client</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
    $('#clientSelectFilter').append(options);
}

function displayBinDropDownList(data){
    $('#bins').empty();
    var options = '';
    options = '<option value="0" selected>Select Bin</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.binId + '">' + value.binId + '</option>';
    });
    $('#bins').append(options);
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
//       document.getElementById('errorDiv').innerHTML =
       infoToast("Please select a client from the list and then proceed!");
       document.getElementById('clientSelect').focus();
       return false;
    }
 	resetUploadDialog();
}

function validateUpdateInventoryFields(){
   if (document.getElementById('inputQty').value.trim()=="") {
       infoToast("Please enter Quantity value.");
       document.getElementById('inputQty').focus();
       return false;
   }else if(/\D/.test(document.getElementById('inputQty').value)){
        infoToast("Quantity value needs to be a number only.");
        document.getElementById('inputQty').focus();
        return false;
   }
   return true;
}

function validateFields(){
   if (document.getElementById('inputBin').value.trim()=="") {
       infoToast("Please enter No. of bins value.");
       document.getElementById('inputBin').focus();
       return false;
   }else if(/\D/.test(document.getElementById('inputBin').value)){
        infoToast("No. of bins must be a number only.");
        document.getElementById('inputBin').focus();
        return false;
   }
   return true;
}

function validateAddInventoryModal(){
   var typeDropDown = $("#clientSelect");
   if (typeDropDown.val() == '' || typeDropDown.val() == undefined || typeDropDown.val() == 0) {
       infoToast("Please select Client from the list and then proceed!");
       document.getElementById('clientSelect').focus();
       return false;
   }
   if( document.getElementById("fileName").files.length == 0 ){
       infoToast("No file selected.Please choose a file to proceed.");
       return false;
   }
   return true;
}

function displayAddInventory() {
	$('#add-inventory-modal').modal('toggle');
	displayUploadData();
}

function displayBinInventory(data){
	$("#inventory-update-form input[name=quantity]").val(data.quantity);
	$("#inventory-update-form input[name=originalQuantity]").val(data.quantity);
	$("#inventory-update-form input[name=binId]").val(data.binId);
    $("#inventory-update-form input[name=binSkuId]").val(data.id);
    $("#inventory-update-form input[name=globalSkuId]").val(data.globalSkuId);
    $("#inventory-update-form input[name=productName]").val(data.name);
    $("#inventory-update-form input[name=brandId]").val(data.brandId);
	$('#update-inventory-modal').modal('toggle');
	form_original_data = $("#inventory-update-form").serialize();
}

function displayCreateBin() {
	$('#add-bins-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#add-bins-info').click(displayCreateBin);
    $('#add-bins').click(addBin);
	$('#add-inventory-info').click(displayAddInventory);
	$('#update-inventory').click(updateBinInventory);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName);
    $('select[name="binsDropdown"]').change(function(){
        $("#clientSelectFilter").prop('selectedIndex',0);
        var binID = $(this).find("option:selected").val();
        if(binID==0){
            return;
        }
        console.log(binID);
        getBinInventoryList(binID);
    });
    $('select[name="clientDropdown"]').change(function(){
        $("#bins").prop('selectedIndex',0);
        var clientID = $(this).find("option:selected").val();
        if(clientID==0){
            return;
        }
        console.log(clientID);
        getInventoryList(clientID);
    });
    infoToast("Please select Bin or Client to see Inventory record.");
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
    }
}

$(document).ready(init);
$(document).ready(getBinList);
$(document).ready(getClientList);