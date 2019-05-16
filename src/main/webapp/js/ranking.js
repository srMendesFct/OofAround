var request = "5";
var lastRequest = "0";
var last = "";


captureDataR = function () {
    var values = {
        tokenID: localStorage.getItem('token'),
        usernameR: localStorage.getItem('username'),
        role: localStorage.getItem('role'),
        username: localStorage.getItem('username'),
        lastRequest: request,
        limit: lastRequest,
        lastUsername: last
    }
    console.log(JSON.stringify(values));

    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/list/publicranking",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {},
        error: function (Response) {
            console.log(Response.status);
            if (Response.status == 200) {
                listDiv = document.getElementById('ranking');
                var ul = document.createElement('ul');
                for (var i = 0; i < data.length; ++i) {
                    var li = document.createElement('li');
                    li.innerHTML = data[i];
                    ul.appendChild(li);
                }
                listDiv.appendChild(ul);
                last = data[request - 1];
                lastRequest = lastRequest + request;
                console.log(last);
                console.log(lastRequest);
            }

        },
        data: JSON.stringify(values) // post data || get data
    });
};

window.onload = captureDataR();