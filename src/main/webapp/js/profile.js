captureDataR = function () {
    var fileReader = new FileReader();
    var file = document.getElementById("fileID").files[0];
    var s;
    fileReader.readAsBinaryString(file);
    fileReader.onload = function () {
        s = fileReader.result;
        var send = window.btoa(s);
        var values = {
            name: localStorage.getItem('username') + "_perfil",
            image: send
        }
        $.ajax({
            type: "POST",
            url: "https://oofaround.appspot.com/rest/images/upload",
            contentType: "application/json;charset=utf-8",
            dataType: 'json', // data type        
            crossDomain: true,
            success: function (Response) {

            },
            error: function (Response) {
                alert('Imagem alterada');
                window.location.href = "https://oofaround.appspot.com/profile.html";
            },
            data: JSON.stringify(values) // post data || get data
        });
    }
};

var user = localStorage.getItem('username');
window.onload = function () {
    var $ANSWER = 'cat';
    //document.getElementById("profilePic").src = 'https://storage.googleapis.com/oofaround.appspot.com/' + localStorage.getItem('username') + '_perfil';
    document.getElementById("profilePic").src = "https://oofaround.appspot.com/img/silhouette-profile-pic-profile-user-silhouette-318-40557.jpg"
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
    document.getElementById("upload").addEventListener("click", function () {
        captureDataR();
    });
};