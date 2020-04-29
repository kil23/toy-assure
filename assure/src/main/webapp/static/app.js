//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}


function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	document.getElementById('errorDiv').innerHTML = response.message;
	setTimeout(function () {
        document.getElementById('errorDiv').innerHTML = "";
    }, 3000);
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: ',',
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: ','
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/csv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'client.csv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'client.csv');
    tempLink.click(); 
}
