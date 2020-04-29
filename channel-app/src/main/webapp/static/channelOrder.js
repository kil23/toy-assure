var baseUrl = $("meta[name=baseUrl]").attr("content");
var counter = 0;
var regex = /^[a-zA-Z]{2,12}[-\s]?[0-9]{0,3}$/;

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

function displayClientDropDownList(data){
    var options = '';
    $.each(data, function(index, value) {
        options += '<option value="' + value.id + '">' + value.name + '</option>';
    });
    $('#clientSelect').append(options);
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
	var url = baseUrl + "/api/"+id+"/listings";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
            displayChannelListingDropDownData(data);
	   },
	   error: handleAjaxError
	});
}

function displayChannelListingDropDownData(data){
    var options = '';
    options = '<option value="">Select ChannelSkuId</option>';
    $.each(data, function(index, value) {
         options += '<option value="' + value.globalSkuId + '">' + value.channelSkuId + '</option>';
    });
    console.log(options);
    for(var i=0; i<counter; i++){
        $('#channelSkuIdSelect'+i+'').empty().append(options);
    }

//    $('#channelSkuIdSelect'+(counter-1)).append(options);
}

var channelNameId;
var clientId;

function submitData(){
    if(!validateFields()){
        setTimeout(function () {
            document.getElementById('errorDiv').innerHTML = "";
        }, 3000);
        return;
    }

    var tableData = {};
    var tbl2 = $('#item-table > tbody > tr').each(function(i) {
        if ($(this).find('option:selected').val() == '' || $(this).find('option:selected').val() == undefined) {
            document.getElementById('errorDiv').innerHTML = "Please select channel-item from the list and then proceed!";
            $(this).find('option:selected').focus();
            return false;
        }
        if($(this).find('input[name="orderedQuantity'+i+'"]').val() == '' || $(this).find('input[name="orderedQuantity'+i+'"]').val() == undefined){
            document.getElementById('errorDiv').innerHTML = "Please add Order Quantity and then proceed!";
            $(this).find('input[name="orderedQuantity'+i+'"]').focus();
            return false;
        }else if($(this).find('input[name="orderedQuantity'+i+'"]').val() <= 0){
             document.getElementById('errorDiv').innerHTML = "Ordered Quantity must be greater than zero.";
             $(this).find('input[name="orderedQuantity'+i+'"]').focus();
             return false;
        }
//        tableData["globalSkuId"] = $(this).find('select[name="channelSkuId'+i+'"]:selected').val();
        tableData["globalSkuId"] = $(this).find('option:selected').val();
        tableData["orderedQuantity"] = $(this).find('input[name="orderedQuantity'+i+'"]').val();
//        tableData["sellingPricePerUnit"] = $(this).find('input[name="sellingPricePerUnit'+i+'"]').val();
        tableData["clientId"] = document.forms["myForm"]["client"].value;
        tableData["channelId"] = document.forms["myForm"]["channel"].value;
        tableData["customerId"] = document.forms["myForm"]["customerId"].value;
        tableData["channelOrderId"] = document.forms["myForm"]["channelOrderId"].value;
        tableData["newFlag"] = (i + 2);
        var json = JSON.stringify(tableData);
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
                $('#successDiv').html('<span style="color:green;">!! Order Placed Successfully !!</span>').show();
                setTimeout(function() { $("#successDiv").hide(); }, 5000);
            },
            error : function(response) {
                $('#errorDiv').html('<span style="color:red;">!! Some Error Occurred !!</span>').show();
                handleAjaxError(response)
            }
         });
    })
    document.getElementById("order-create-form").reset();
}

function addRows(){

    if (document.getElementById('channelSelect').value == '' || document.getElementById('channelSelect').value == undefined) {
        document.getElementById('errorDiv').innerHTML = "Please select channel from the list and then proceed!";
        document.getElementById('channelSelect').focus();
        return false;
    }

    var newRow = $("<tr>");
    var cols = "";

    cols += '<td><select class="form-control" id="channelSkuIdSelect'+counter+'" name="channelSkuId'+ counter+'" required=""><option value="">Select ChannelSkuId</option></select></td>';
    cols += '<td><input type="number" class="form-control" name="orderedQuantity'+ counter +'"/></td>';
//    cols += '<td><input type="number" class="form-control" name="sellingPricePerUnit'+ counter +'"/></td>';
    cols += '<td><button type="button" class="ibtnDel btn btn-md btn-danger" >Delete</button></td>';
    newRow.append(cols);
    $("#item-table").append(newRow);
    getChannelListingData(channelNameId);
    console.log(counter);
    counter++;
}

function validateFields(){

   if (document.getElementById('clientSelect').value == '' || document.getElementById('clientSelect').value == undefined) {
        document.getElementById('errorDiv').innerHTML = "Please select a client from the list and then proceed!";
        document.getElementById('clientSelect').focus();
        return false;
   }
   if (document.getElementById('channelSelect').value == '' || document.getElementById('channelSelect').value == undefined) {
        document.getElementById('errorDiv').innerHTML = "Please select channel from the list and then proceed!";
        document.getElementById('channelSelect').focus();
        return false;
   }
   if (document.getElementById('inputCustomerId').value==null || document.getElementById('inputCustomerId').value.trim()=="") {
        document.getElementById('errorDiv').innerHTML = "Customer-Id must be filled out";
        document.getElementById('inputCustomerId').focus();
        return false;
   }else if(document.getElementById('inputCustomerId').value <= 0){
        document.getElementById('errorDiv').innerHTML = "Customer-Id must a number greater than zero.";
        document.getElementById('inputCustomerId').focus();
        return false;
   }
   if (document.getElementById('inputChannelOrderId').value==null || document.getElementById('inputChannelOrderId').value.trim()=="") {
        document.getElementById('errorDiv').innerHTML = "Channel-Order-Id must be filled out";
        document.getElementById('inputChannelOrderId').focus();
        return false;
   } else if(!regex.test(document.getElementById('inputChannelOrderId').value)){
        document.getElementById('errorDiv').innerHTML = "Please enter valid Channel Order Id";
        document.getElementById('inputChannelOrderId').focus();
   }
   if(counter==0){
        document.getElementById('errorDiv').innerHTML = "Order-items must be filled out. Please click 'Add' button below.";
        document.getElementById('addRow').focus();
        return false;
   }
   return true;
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
        setTimeout(function () {
            document.getElementById('errorDiv').innerHTML = "";
        }, 3000);
        channelNameId = $(this).find("option:selected").val();
        getChannelListingData(channelNameId);
    });

    $('select[name="client"]').change(function(){
        setTimeout(function () {
            document.getElementById('errorDiv').innerHTML = "";
        }, 3000);
        clientId = $(this).find("option:selected").val();
        getChannelData(clientId);
    });
}

$(document).ready(init);
$(document).ready(getClientList);