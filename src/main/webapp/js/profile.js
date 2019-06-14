captureDataChangePic = function () {
    var fileReader = new FileReader();
    var file = document.getElementById("fileID").files[0];
    var s;
    fileReader.readAsBinaryString(file);
    fileReader.onload = function () {
        s = fileReader.result;
        var send = window.btoa(s);
        var values = {
            name: localStorage.getItem('username'),
            image: send,
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
                localStorage.setItem('image', send);
                window.location.href = "https://oofaround.appspot.com/profile.html";
            },
            error: function (Response) {
                alert('Falha ao alterar imagem');
                window.location.href = "https://oofaround.appspot.com/profile.html";
            },
            data: JSON.stringify(values) // post data || get data
        });
    }
};

captureDataGetUserInfo = function (values) {
    var values = {
        tokenID: localStorage.getItem('token'),
        role: localStorage.getItem('role'),
        username: localStorage.getItem('username'),
    };
    console.log(JSON.stringify(values));
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/userinfo/self",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            console.log(Response);
            localStorage.setItem('email', Response.email);
            console.log(localStorage.getItem('email'));
            localStorage.setItem('country', Response.country);
            console.log(localStorage.getItem('country'));
            localStorage.setItem('cellphone', Response.cellphone);
            console.log(localStorage.getItem('cellphone'));
            localStorage.setItem('type', Response.privacy);
            console.log(localStorage.getItem('type'));
        },
        error: function (Response) {},
        data: JSON.stringify(values) // post data || get data
    });
};


window.onload = function () {
    var date = new Date();
    var email = localStorage.getItem('email');
    var country = localStorage.getItem('country');
    var cellphone = localStorage.getItem('cellphone');
    var type = localStorage.getItem('type');
    var user = localStorage.getItem('username');
    var image = localStorage.getItem('image');
    var token = localStorage.getItem('expiration');
    var longday = date.getTime();
    document.getElementById("userC").value = localStorage.getItem('username');;
    document.getElementById("emailC").value = email;
    document.getElementById("countryC").value = country;
    document.getElementById("teleC").value = cellphone;
    document.getElementById("typeC").value = type;
    document.getElementById("profilePic").src = 'data:image/jpeg;base64, ' + image;
    document.getElementById("user").innerHTML = user;
    document.getElementById("profilePicBig").src = 'data:image/jpeg;base64, ' + image;
    if (longday > token) {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    } else {
        localStorage.setItem('expiration', date.getTime() + 300000);
        captureDataGetUserInfo();
        setupCallback();
    }
};

setupCallback = function () {
    document.getElementById("upload").addEventListener("click", function () {
        captureDataChangePic();
    });
};