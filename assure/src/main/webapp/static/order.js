var baseUrl = $("meta[name=baseUrl]").attr("content")
var form_original_data;

function getOrderUrl(){
	return baseUrl + "/api/order";
}

function updateOrder(event){
    if ($("#order-update-form").serialize() == form_original_data) {
        $.toast({
            heading: 'Info',
            text: 'No data edited.',
            position: 'bottom-right',
            showHideTransition: 'fade',
            hideAfter: 3000,
            icon: 'info',
            allowToastClose: true,
            afterHidden: function () {
                document.getElementById("order-update-form").reset();
                $('#update-order-modal').modal('toggle');
                getBinInventoryList(getBinId());
            }
        });
    }else {
        var id = $("#order-update-form input[name=id]").val();
        var url = getOrderUrl() + "/" + id;

        var $form = $("#order-update-form");
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
                    text: 'Order info updated.',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 2000,
                    icon: 'success',
                    allowToastClose: true,
                    afterShown: function () {
                        document.getElementById("order-update-form").reset();
                        $('#update-order-modal').modal('toggle');
                        getOrderList();
                    }
                });
            },
            error: function(jqXHR){
                errorData = handleAjaxError(jqXHR);
                console.log(errorData);
                if(errorData.length > 1){
                    document.getElementById('download-errors').focus();
                }
                document.getElementById("order-update-form").reset();
            }
        });
    }
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
    $('#clientSelect').empty();
    $('#clientSelectFilter').empty();
    var options = '<option value="0" selected>Select Client</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
    $('#clientSelectFilter').append(options);
}

function getCustomerList(){
	var url = baseUrl + "/api/customer";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayCustomerDropDownList(data);
        },
        error: handleAjaxError
	});
}

function displayCustomerDropDownList(data){
    $('#customerSelect').empty();
    $('#customerSelectFilter').empty();
    var options = '<option value="0" selected>Select Customer</option>';
    $.each(data, function(index, value) {
        console.log(value.type);
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#customerSelect').append(options);
    $('#customerSelectFilter').append(options);
}

function getChannelList(){
	var url = baseUrl + "/api/channel";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayChannelDropDownData(data);
        },
        error: handleAjaxError
	});
}

function displayChannelDropDownData(data){
    $('#channelSelect').empty();
    var options = '';
    options = '<option value="0">Select Channel</option>';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#channelSelect').append(options);
}

function downloadPdf(id){
	var url = $("meta[name=baseUrl]").attr("content") + "/api/download/order" + id;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var sampleArr = base64ToArrayBuffer(data);
            $.toast({
                heading: 'Success',
                text: 'Downloading PDF.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'success',
                allowToastClose: true,
                afterShown: function () {
                    saveByteArray("Sample Report", sampleArr);
                }
            });
        },
        error: handleAjaxError
	});
}

function saveByteArray(reportName, byte) {
    var blob = new Blob([byte], {type: "application/pdf"});
    var link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    var fileName = reportName;
    link.download = fileName;
    link.click();
}

function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
       var ascii = binaryString.charCodeAt(i);
       bytes[i] = ascii;
    }
    return bytes;
 }

function allocateOrder(){
	var url = getOrderUrl()+"/allocate";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Order list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                   displayOrderList(data);
                }
            });
        },
        error: handleAjaxError
	});
}

function fulfillOrder(id,channelId){
	var url = getOrderUrl()+"/"+id+"/fulfill/"+channelId;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Order list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayOrderList(data);
                }
            });
        },
        error: handleAjaxError
	});
}

function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Order list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayOrderList(data);
                }
            });
        },
        error: handleAjaxError
	});
}

function getClientOrderList(id){
	var url = getOrderUrl()+"/client/"+id;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Order list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayOrderList(data);
                }
            });
        },
        error: handleAjaxError
	});
}

function getChannelOrderList(id){
	var url = getOrderUrl()+"/"+id;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Order list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayOrderList(data);
                }
            });
        },
        error: handleAjaxError
	});
}

function getCustomerOrderList(id){
	var url = getOrderUrl()+"/customer/"+id;
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            $.toast({
                heading: 'Info',
                text: 'Order list updated.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'info',
                allowToastClose: true,
                afterShown: function () {
                    displayOrderList(data);
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
var count = 1;

function processData(){
    if(!validateFields()){
        return;
    }
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
    var dataArr = [];
    var jsonArray = {};
	for (var i = 0; i < fileData.length; i++){
        if(i>=5000){
            return;
        }
        dataArr.push(fileData[i]);
    }

    jsonArray.order = dataArr;
	jsonArray.clientId = document.forms["add-order-form"]["client"].value;
    jsonArray.customerId = document.forms["add-order-form"]["customer"].value;
    jsonArray.channelOrderId = document.forms["add-order-form"]["channelOrderId"].value;
    jsonArray.channelId = document.forms["add-order-form"]["channelId"].value;
	console.log(dataArr);
	console.log(JSON.stringify(jsonArray));
	var json = JSON.stringify(jsonArray);
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
            $.toast({
                heading: 'Success',
                text: 'Order info added.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 3000,
                icon: 'success',
                allowToastClose: true,
                afterHidden: function () {
                    document.getElementById("add-order-form").reset();
                    $('#add-order-modal').modal('toggle');
                    getOrderList();
                }
            });
        },
        error: function(jqXHR){
//            handleAjaxError(jqXHR);
            errorData = handleAjaxError(jqXHR);
            console.log(errorData);
            if(errorData.length > 1){
                document.getElementById('download-errors').focus();
            }
            document.getElementById("add-order-form").reset();
        }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayOrderList(data){
	var $thead = $('#order-table').find('thead');
	var $tbody = $('#order-table').find('tbody');
    $thead.empty();
	$tbody.empty();
	 var head = '<tr>'
     +  '<th scope="col">Order ID</th>'
     +  '<th scope="col">#</th>'
     +  '<th scope="col">Client</th>'
     +  '<th scope="col">Customer</th>'
     +  '<th scope="col">Channel</th>'
     +  '<th scope="col">Channel Order ID</th>'
     +  '<th scope="col">Status</th>'
     +  '<th scope="col">Action</th>'
     +  '<th scope="col">Invoice</th>'
     +  '<th scope="col">View</th>'
     +  '</tr>';
     $thead.append(head);
    var count = 1 ;
	for(var i in data){
		var e = data[i];
		if(e.status === 'ALLOCATED') {
		    var buttonHtml1 = '<button type="button" id="invoiceBtn'+count+'" class="btn btn-outline-primary" onclick="fulfillOrder('+e.id+','+e.channelId+')">Fulfill Order</button>'
		} else{
            var buttonHtml1 = '<button type="button" id="invoiceBtn'+count+'" class="btn btn-outline-secondary" disabled>Fulfill Order</button>'
        }
		if(e.status === 'FULFILLED') {
		    var buttonHtml2 = '<button type="button" id="invoiceBtn'+count+'" class="btn btn-outline-warning" onclick="downloadPdf('+ e.id +')">Download</button>'
		}else{
		    var buttonHtml2 = '<button type="button" id="invoiceBtn'+count+'" class="btn btn-outline-primary" disabled>Fulfill Order</button>'
		}
		var buttonHtml3 = '<a href = "../ui/'+e.id+'/item" ><button type="button" class="btn btn-outline-primary">Order-Items</button></a>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + count + '</td>'
		+ '<td>' + e.clientName + '</td>'
		+ '<td>' + e.customerName + '</td>'
		+ '<td>' + e.channelName + '</td>'
        + '<td>' + e.channelOrderId + '</td>'
        + '<td>' + e.status + '</td>'
		+ '<td>' + buttonHtml1 + '</td>'
		+ '<td>' + buttonHtml2 + '</td>'
		+ '<td>' + buttonHtml3 + '</td>'
		+ '</tr>';
        $tbody.append(row);
        count++;
	}
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

function displayOrder(data){
	$("#order-update-form input[name=name]").val(data.name);
	$("#order-update-form select[name=type]").val(data.type);
	$("#order-update-form input[name=id]").val(data.id);
	$('#update-order-modal').modal('toggle');
	form_original_data = $("#order-update-form").serialize();
}

function validateFields(){
   var clientDropDown = $("#clientSelect");
   console.log(clientDropDown.val())
   if (clientDropDown.val() == '' || clientDropDown.val() == undefined || clientDropDown.val() == 0) {
       infoToast("Please select a client from the list and then proceed!");
       document.getElementById('clientSelect').focus();
       return false;
   }
   var customerDropDown = $("#customerSelect");
   console.log(customerDropDown.val())
   if (customerDropDown.val() == '' || customerDropDown.val() == undefined || customerDropDown.val() == 0) {
       infoToast("Please select a customer from the list and then proceed!");
       document.getElementById('customerSelect').focus();
       return false;
   }
   if (document.getElementById('inputChannelOrderId').value == null ||
       document.getElementById('inputChannelOrderId').value.trim() == "" ) {
       infoToast("Please enter Channel Order ID value.");
       document.getElementById('inputChannelOrderId').focus();
       return false;
   }
   return true;
}

function displayCreateClient(){
	$('#add-order-modal').modal('toggle');
    $(document).ready(getCustomerList);
	displayUploadData();
}

//INITIALIZATION CODE
function init(){
	$('#add-order-info').click(displayCreateClient);
	$('#allocate-order').click(allocateOrder);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#fileName').on('change', updateFileName);
    $('#clientSelectFilter').change(function(){
        $("#channelSelect").prop('selectedIndex',0);
        $("#customerSelectFilter").prop('selectedIndex',0);
        var clientID = $(this).find("option:selected").val();
        if(clientID==0){
            return;
        }
        console.log(clientID);
        getClientOrderList(clientID);
    });
    $('#customerSelectFilter').change(function(){
        $("#channelSelect").prop('selectedIndex',0);
        $("#clientSelectFilter").prop('selectedIndex',0);
        var customerID = $(this).find("option:selected").val();
        if(customerID==0){
            return;
        }
        console.log(customerID);
        getCustomerOrderList(customerID);
    });
    $('#channelSelect').change(function(){
        $("#clientSelectFilter").prop('selectedIndex',0);
        $("#customerSelectFilter").prop('selectedIndex',0);
        var channelID = $(this).find("option:selected").val();
        if(channelID==0){
            return;
        }
        console.log(channelID);
        getChannelOrderList(channelID);
    });
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(getClientList);
$(document).ready(getCustomerList);
$(document).ready(getChannelList);