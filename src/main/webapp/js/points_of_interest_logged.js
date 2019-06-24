captureDataGetPointsOfInterest = function (event) {
    var limit = 5;
    var lastName = "";
    var x = document.getElementsByClassName("ed");
    var res = {}
    for (var i = 0; i < x.length; i++) {
        if (x[i].checked) {
            res[i] = x[i].value;
        }
    }
    var values = {
        tokenID: localStorage.getItem('token'),
        role: localStorage.getItem('role'),
        usernameR: localStorage.getItem('username'),
        limit: limit,
        lastName: lastName,
        region: document.getElementById("distrito").value,
        categoriesGet: res
    };
    console.log(JSON.stringify(values));
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/location/getcategoryregion",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            lastName = 0; //buscar o ultimo elemento
            alert("nice");
        },
        error: function (Response) {
            alert('merda');
        },
        data: JSON.stringify(values) // post data || get data
    });
    event.preventDefault();
};

window.onload = function () {
    var user = localStorage.getItem('username');
    var image = localStorage.getItem('image');
    var date = new Date();
    var token = localStorage.getItem('expiration');
    var longday = date.getTime();
    document.getElementById("profilePic").src = 'data:image/jpeg;base64, ' + image;
    document.getElementById("user").innerHTML = user;
    if (longday > token) {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    } else {
        localStorage.setItem('expiration', date.getTime() + 300000);
        setupCallback();
    }
};

setupCallback = function () {
    var frmsl = $('form[name="categorias"]');
    frmsl[0].onsubmit = captureDataGetPointsOfInterest;

    document.getElementById("logout").addEventListener("click", function () {
        localStorage.clear();
        alert("Sessão terminada.")
        window.location.href = "https://oofaround.appspot.com/";
    });
};
