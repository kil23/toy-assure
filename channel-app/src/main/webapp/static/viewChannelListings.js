var baseUrl = $(location).attr("href");
var newUrl = baseUrl.replace('channel', 'api');

function getChannelListingUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

function getListingList(){
	var url = newUrl
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
                    hideAfter: 1000,
                    icon: 'info',
                    allowToastClose: true,
                    afterShown: function () {
                        displayListingList(data);
                    }
                });
            }else {
                $.toast({
                    heading: 'Info',
                    text: 'No Data Found',
                    position: 'bottom-right',
                    showHideTransition: 'fade',
                    hideAfter: 1000,
                    icon: 'info',
                    allowToastClose: true
                });
            }
        },
        error: handleAjaxError
	});
}

//INITIALIZATION CODE
function init(){
	getListingList();
}