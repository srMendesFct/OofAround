captureDataR = function () {

    var values = {
        name: localStorage.getItem('username') + "_perfil",
        image: fileReader.result
    }
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/images/upload",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            var listDiv = document.getElementById('profile');
            var a = document.createElement('a');
            var img = document.createElement('img');
            img.innerHTML = "";
            a.appendChild(img);
            listDiv.appendChild(a);
            last = Response.scores[request - 1].username;
            lastRequest = lastRequest + request;
        },
        error: function (Response) {},
        data: JSON.stringify(values) // post data || get data
    });
};



var user = localStorage.getItem('username');
window.onload = function () {
    document.getElementById("user").innerHTML = user;
    var token = localStorage.getItem('expiration');
    var date = new Date();
    var longday = date.getTime();
    if (longday > token) {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    } else {
        var fileReader = new FileReader();
        var file = document.getElementById("fileID").files[0];
        console.log(file);
        setupCallback();
    }
};

setupCallback = function () {
    document.getElementById("upload").addEventListener("click", function () {
        fileReader.readAsArrayBuffer(file);
        captureDataR();
    });
};