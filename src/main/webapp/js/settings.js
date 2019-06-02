var user = localStorage.getItem('username');

captureDataD = function (values) {
    var values = {
        tokenID: localStorage.getItem('token'),
        usernameR: localStorage.getItem('username'),
        role: localStorage.getItem('role'),
        username: localStorage.getItem('username'),
    };

    console.log(JSON.stringify(values));

    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/delete/self",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
        },
        error: function (Response) {
            console.log(Response.status);
            if (Response.status == 200) {
                localStorage.clear();
                alert("Conta eliminada com sucesso.")
            }
        },
        data: JSON.stringify(values) // post data || get data
    });
};
var user = localStorage.getItem('username');
var image = localStorage.getItem('image');
window.onload = function () {
    document.getElementById("profilePic").src = 'data:image/jpeg;base64, ' + image;
    document.getElementById("user").innerHTML = user;
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

setupCallback = function () {
    document.getElementById("delete").addEventListener("click", function () {
        captureDataD();
        //window.location.href = "https://oofaround.appspot.com/";
    });

    document.getElementById("logout").addEventListener("click", function () {
        localStorage.clear();
        alert("Sessão terminada.")
        window.location.href = "https://oofaround.appspot.com/";
    });
};
