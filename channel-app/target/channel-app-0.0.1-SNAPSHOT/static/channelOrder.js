var baseUrl = $("meta[name=baseUrl]").attr("content");
var counter = 0;
var regex = /^[a-zA-Z]{2,12}[-\s]?[0-9]{0,3}$/;
var listingData;
var errorArray = [];

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

function displayClientDropDownList(data){
    $('#clientSelect').empty();
    var options = '<option value="" selected>Select Client</option>';
    $.each(data, function(index, value) {
        console.log(value.type);
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
}

function displayCustomerDropDownList(data){
    $('#customerSelect').empty();
    var options = '<option value="" selected>Select Customer</option>';
    $.each(data, function(index, value) {
        console.log(value.type);
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#customerSelect').append(options);
}

function getChannelData(id){
	var url = baseUrl + "/api/channels/"+id;
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
    options = '<option value="">Select Channel</option>';
    $.each(data, function(index, value) {
        if(value.id!=1){
            options += '<option value="' + value.id + '">' + value.name + '</option>';
        }
    });
    $('#channelSelect').append(options);
}

function getChannelListingData(id){
	var url = baseUrl + "/api/"+id+"/listing";
	$.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            displayChannelListingDropDownData(data);
            listingData = data;
        },
        error: handleAjaxError
	});
}

function displayChannelListingDropDownData(data){
    var options = '';
    options = '<option value="">Select Channel SKU ID</option>';
    $.each(data, function(index, value) {
         options += '<option value="' + value.globalSkuId + '">' + value.channelSkuId + '</option>';
    });
    console.log(options);
    for(var i=0; i<counter; i++){
        $('#channelSkuIdSelect'+i+'').empty().append(options);
    }

//    $('#channelSkuIdSelect'+(counter-1)).append(options);
}

function addChannelListingDropDown(data) {
    $('#channelSkuIdSelect'+(counter)).empty();
    var options = '';
    options = '<option value="">Select Channel SKU ID</option>';
    $.each(data, function(index, value) {
         options += '<option value="' + value.globalSkuId + '">' + value.channelSkuId + '</option>';
    });
    console.log(options);
    console.log(counter);
    $('#channelSkuIdSelect'+(counter)).append(options);
}

var channelNameId;
var clientId;

function submitData(){
    if(!validateFields()){
        return;
    }

    var tableData = {};
    var dataArr = [];
    var jsonArray = {};
    var tbl2 = $('#item-table > tbody > tr').each(function(i) {
        if ($(this).find('option:selected').val() == '' || $(this).find('option:selected').val() == undefined) {
            infoToast("Please select channel-item from the list and then proceed!");
            $(this).find('option:selected').focus();
            return false;
        }
        if($(this).find('input[name="orderedQuantity'+i+'"]').val() == '' ||
           $(this).find('input[name="orderedQuantity'+i+'"]').val() == undefined){
            infoToast("Please add Order Quantity and then proceed!");
            $(this).find('input[name="orderedQuantity'+i+'"]').focus();
            return false;
        }else if($(this).find('input[name="orderedQuantity'+i+'"]').val() <= 0){
             infoToast("Ordered Quantity must be greater than zero.");
             $(this).find('input[name="orderedQuantity'+i+'"]').focus();
             return false;
        }
        tableData.globalSkuId = $(this).find('option:selected').val();
        tableData.orderedQuantity = $(this).find('input[name="orderedQuantity'+i+'"]').val();
        dataArr.push(tableData);
        tableData = {};
    });

    jsonArray.order = dataArr;
    jsonArray.clientId = document.forms["myForm"]["client"].value;
    jsonArray.channelId = document.forms["myForm"]["channel"].value;
    jsonArray.customerId = document.forms["myForm"]["customer"].value;
    jsonArray.channelOrderId = document.forms["myForm"]["channelOrderId"].value;
    var json = JSON.stringify(jsonArray);
    console.log(json);
    var url = getOrderUrl();

    $.ajax({
        type : "POST",
        url : url,
        data : json,
        headers: {
           'Content-Type': 'application/json'
        },
        success: function(response) {
            $.toast({
                heading: 'Success',
                text: 'Order placed.',
                position: 'bottom-right',
                showHideTransition: 'fade',
                hideAfter: 2000,
                icon: 'success',
                allowToastClose: true,
                afterHidden: function () {
                    tableData = {};
                    dataArr = {};
                    $(location).attr('href', baseUrl+"/channel/order-list");
                }
            });
        },
        error : function(jqXHR) {
            errorArray = {};
            errorArray = handleAjaxError(jqXHR);
            if(errorArray.length > 1){
                document.getElementById('download-errors').focus();
            }
            document.getElementById("order-create-form").reset();
            tableData = {};
            dataArr = {};
        }
     });
}

function addRows(){
    if (document.getElementById('clientSelect').value == '' || document.getElementById('clientSelect').value == undefined) {
//        document.getElementById('errorDiv').innerHTML =
        infoToast("Please select a client from the list and then proceed!");
        document.getElementById('clientSelect').focus();
        return false;
    }
    if (document.getElementById('channelSelect').value == '' || document.getElementById('channelSelect').value == undefined) {
//        document.getElementById('errorDiv').innerHTML =
        infoToast("Please select channel from the list and then proceed!");
        document.getElementById('channelSelect').focus();
        return false;
    }
    if(counter>0){
        if (document.getElementById('channelSkuIdSelect'+(counter-1)).value == '' || document.getElementById('channelSkuIdSelect'+(counter-1)).value == undefined) {
//            document.getElementById('errorDiv').innerHTML =
            infoToast("Please add Channel SKU ID value first and then proceed!");
            document.getElementById('channelSkuIdSelect'+(counter-1)).focus();
            return false;
        }
    }
    var newRow = $("<tr>");
    var cols = "";
    cols += '<td></td>'
    cols += '<td><select class="form-control" id="channelSkuIdSelect'+counter+'" name="channelSkuId'+ counter+'" required=""><option value="">Select ChannelSkuId</option></select></td>';
    cols += '<td><input type="number" min="0" step="0.01" max="50" class="form-control" id="quantity'+counter+'" placeholder="Enter Quantity" name="orderedQuantity'+ counter +'"/></td>';
    cols += '<td><button type="button" class="ibtnDel btn btn-md btn-outline-danger" >Delete</button></td>';
    newRow.append(cols);
    $("#item-table").append(newRow);
    if(counter == 0) {
        getChannelListingData(channelNameId);
    }else {
        addChannelListingDropDown(listingData);
    }
    console.log(counter);
    counter++;
}

function validateFields(){

   if (document.getElementById('clientSelect').value == '' || document.getElementById('clientSelect').value == undefined) {
       infoToast("Please select a client from the list and then proceed!");
       document.getElementById('clientSelect').focus();
       return false;
   }
   if (document.getElementById('channelSelect').value == '' || document.getElementById('channelSelect').value == undefined) {
       infoToast("Please select channel from the list and then proceed!");
       document.getElementById('channelSelect').focus();
       return false;
   }
   if (document.getElementById('customerSelect').value == '' || document.getElementById('customerSelect').value == undefined) {
       infoToast("Please select customer from the list and then proceed!");
       document.getElementById('customerSelect').focus();
       return false;
   }
   if (document.getElementById('inputChannelOrderId').value==null || document.getElementById('inputChannelOrderId').value.trim()=="") {
       infoToast("Channel Order ID must be filled out");
       document.getElementById('inputChannelOrderId').focus();
       return false;
   }
   if(counter==0){
       infoToast("Order-items must be filled out. Please click 'Add' button below.");
       document.getElementById('addRow').focus();
       return false;
   }
   if(counter>1){
       var arr = [];
       for(var i=0; i<counter; i++){
           var firstVal = $('select[name="channelSkuId'+i+'"]').find("option:selected").val();
           for(var j=i+1; j<counter; j++) {
              var secondVal = $('select[name="channelSkuId'+j+'"]').find("option:selected").val();
              if(firstVal == secondVal) {
                  infoToast("Duplicate Channel items found. Please add unique items.");
                  document.getElementById('channelSkuIdSelect'+j).focus();
                  return false;
              }
           }
       }
   }
   return true;
}

function infoToast(infoText) {
    $.toast({
        heading: 'Info',
        text: infoText,
        position: 'bottom-right',
        showHideTransition: 'fade',
        hideAfter: 5000,
        icon: 'error',
        allowToastClose: true
    });
}

//INITIALIZATION CODE
function init(){
    $('#addRow').click(addRows);
	$('#submit-data').click(submitData);
    $("#item-table").on("click", ".ibtnDel", function (event) {
        $(this).closest("tr").remove();
        counter -= 1
    });
    $('select[name="channel"]').change(function(){
        channelNameId = $(this).find("option:selected").val();
        getChannelListingData(channelNameId);
    });

    $('select[name="client"]').change(function(){
        clientId = $(this).find("option:selected").val();
        getChannelData(clientId);
    });
}

$(document).ready(init);
$(document).ready(getClientList);
$(document).ready(getCustomerList);