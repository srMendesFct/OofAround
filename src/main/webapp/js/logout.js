var user = localStorage.getItem('username');
var image = localStorage.getItem('image');
window.onload = function init() {
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