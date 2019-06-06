captureDataG = function () {
        var values = {
            name: localStorage.getItem('username') + "_profile",
            usernameR: localStorage.getItem('username'),
            tokenID: localStorage.getItem('token'),
            role: localStorage.getItem('role')
        }
        $.ajax({
            type: "POST",
            url: "https://oofaround.appspot.com/rest/images/get",
            contentType: "application/json;charset=utf-8",
            dataType: 'json', // data type        
            crossDomain: true,
            success: function (Response) {
                localStorage.setItem('image', Response.image);
            },
            error: function (Response) {
            },
            data: JSON.stringify(values) // post data || get data
        });
};


window.onload = function init() {
    var token = localStorage.getItem('expiration');
    var date = new Date();
    var longday = date.getTime();
    if (longday > token) {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    } else {
        captureDataG();
        setupCallback();
        var user = localStorage.getItem('username');
        var image = localStorage.getItem('image');
        document.getElementById("profilePic").src = 'data:image/jpeg;base64, ' + image;
        document.getElementById("user").innerHTML = user;
    }
}

setupCallback = function () {
    document.getElementById("logout").addEventListener("click", function () {
        localStorage.clear();
        alert("Sessão terminada.")
        window.location.href = "https://oofaround.appspot.com/";
    });
}