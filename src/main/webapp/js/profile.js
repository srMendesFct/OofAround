captureDataR = function () {
    var fileReader = new FileReader();
    var file = document.getElementById("fileID").files[0];
    var s;
    fileReader.readAsBinaryString(file);
    fileReader.onload = function () {
        s = fileReader.result;
        var send = window.btoa(s);
        localStorage.setItem('image', send);
        var values = {
            name: localStorage.getItem('username'),
            image: localStorage.setItem('image', send),
            usernameR: localStorage.getItem('username'),
            tokenID: localStorage.getItem('token'),
            role: localStorage.getItem('role')
        }
        $.ajax({
            type: "POST",
            url: "https://oofaround.appspot.com/rest/images/uploadprofile",
            contentType: "application/json;charset=utf-8",
            dataType: 'json', // data type        
            crossDomain: true,
            success: function (Response) {
                alert('Imagem alterada');
                window.location.href = "https://oofaround.appspot.com/profile.html";
            },
            error: function (Response) {
                alert('Falha ao alterar imagem');
                localStorage.removeItem('image');
                window.location.href = "https://oofaround.appspot.com/profile.html";
            },
            data: JSON.stringify(values) // post data || get data
        });
    }
};

captureDataG = function () {
        var values = {
            name: localStorage.getItem('username'),
            image: localStorage.getItem('image'),
            usernameR: localStorage.getItem('username'),
            tokenID: localStorage.getItem('token'),
            role: localStorage.getItem('role')
        }
        $.ajax({
            type: "POST",
            url: "https://oofaround.appspot.com/rest/images/uploadprofile",
            contentType: "application/json;charset=utf-8",
            dataType: 'json', // data type        
            crossDomain: true,
            success: function (Response) {
                alert('work');
                console.log(Response);
                window.location.href = "https://oofaround.appspot.com/profile.html";
            },
            error: function (Response) {
                alert('Falha ao alterar imagem');
                window.location.href = "https://oofaround.appspot.com/profile.html";
            },
            data: JSON.stringify(values) // post data || get data
        });
};

var user = localStorage.getItem('username');
window.onload = function () {
    document.getElementById("profilePic").src = 'data:image/jpeg;base64, send';
    document.getElementById("user").innerHTML = user;
    var token = localStorage.getItem('expiration');
    var date = new Date();
    var longday = date.getTime();
    if (longday > token) {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    } else {
        captureDataG();
        setupCallback();
    }
};

setupCallback = function () {
    document.getElementById("upload").addEventListener("click", function () {
        captureDataR();
    });
};