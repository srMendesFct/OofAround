captureDataG = function () {
        var values = {
            name: localStorage.getItem('username'),
            image: localStorage.getItem('username') + "_perfil",
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
                console.log(Response);
                localStorage.setItem('image', send);
            },
            error: function (Response) {
                console.log('erro');
            },
            data: JSON.stringify(values) // post data || get data
        });
};

var user = localStorage.getItem('username');
var image = localStorage.getItem('image');
window.onload = function init() {
    captureDataG();
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
}

setupCallback = function () {
    document.getElementById("logout").addEventListener("click", function () {
        localStorage.clear();
        alert("Sessão terminada.")
        window.location.href = "https://oofaround.appspot.com/";
    });
}