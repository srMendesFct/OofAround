var request = 5;
var lastRequest = 0;
var last = "";
var values = {
    tokenID: localStorage.getItem('token'),
    usernameR: localStorage.getItem('username'),
    role: localStorage.getItem('role'),
    username: localStorage.getItem('username'),
    lastRequest: lastRequest,
    limit: request,
    lastUsername: last
}

captureDataR = function (values) {

    console.log(JSON.stringify(values));

    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/list/publicranking",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            alert("aaaa");
            var listDiv = document.getElementById('ranking');
            var ul = document.createElement('ul');
            for (var i = 0; i < data.length; ++i) {
                var li = document.createElement('li');
                li.innerHTML = data[i];
                ul.appendChild(li);
            }
            listDiv.appendChild(ul);
            var vermais = document.createElement('button');
            vermais.setAttribute("id", "vermais");
            listDiv.appendChild(vermais);
            last = data[request - 1];
            lastRequest = lastRequest + request;
            console.log(last);
            console.log(lastRequest);
        },
        error: function (Response) {},
        data: JSON.stringify(values) // post data || get data
    });
};

window.onload = function captureDataR(values) {
    var token = localStorage.getItem('expiration');
    var date = new Date();
    var longday = date.getTime();
    if (longday > token) {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    } else {
        setupCallback();
    }
};

setupCallback = function (values) {
    document.getElementById("vermais").addEventListener("click", function () {
        captureDatar(values);
    });
};