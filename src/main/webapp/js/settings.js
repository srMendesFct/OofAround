 captureDataDeleteUser = function (values) {
     var values = {
         tokenID: localStorage.getItem('token'),
         usernameR: localStorage.getItem('username'),
         role: localStorage.getItem('role'),
         username: localStorage.getItem('username'),
     };
     $.ajax({
         type: "POST",
         url: "https://oofaround.appspot.com/rest/delete/self",
         contentType: "application/json;charset=utf-8",
         dataType: 'json', // data type        
         crossDomain: true,
         success: function (Response) {},
         error: function (Response) {
            if (Response.status == 200) {
                localStorage.clear();
                alert("Conta eliminada com sucesso.")
            }
            else {
                alert("Falha ao eliminar conta");
            }
         },
         data: JSON.stringify(values) // post data || get data
     });
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
        success: function (Response) {},
        error: function (Response) {
            console.log(Response.status);
            if (Response.status == 200) {
                console.log(Response);
            }
        },
        data: JSON.stringify(values) // post data || get data
    });
};

 captureDataChangeUserInfo = function () {
     var values = {};
     $.each($('form[name="change"]').serializeArray(), function (i, field) {
         values[field.name] = field.value;
     });
     $.ajax({
         type: "POST",
         url: "https://oofaround.appspot.com/rest/userinfo/alterself",
         contentType: "application/json;charset=utf-8",
         dataType: 'json', // data type        
         crossDomain: true,
         success: function (Response) {},
         error: function (Response) {
             if (Response.status == 200) {
                 alert("Alteração efetuada com sucesso.");
                 //window.location.href = "https://oofaround.appspot.com/";
             } else {
                 alert("Falha ao alterar os dados.");
                 //window.location.href = "https://oofaround.appspot.com/";
             }

         },
         data: JSON.stringify(values) // post data || get data
     });
 };

 window.onload = function (event) {
     var user = localStorage.getItem('username');
     var image = localStorage.getItem('image');
     var date = new Date();
     localStorage.setItem('expiration', date.getTime() + 300000);
     document.getElementById("profilePic").src = 'data:image/jpeg;base64, ' + image;
     document.getElementById("user").innerHTML = user;
     document.getElementById("profilePicBig").src = 'data:image/jpeg;base64, ' + image;
     var token = localStorage.getItem('expiration');
     var date = new Date();
     var longday = date.getTime();
     if (longday > token) {
         localStorage.clear();
         window.location.href = "https://oofaround.appspot.com/";
     } else {
        captureDataGetUserInfo();
        setupCallback();
     }
 };

 setupCallback = function () {
     document.getElementById("delete").addEventListener("click", function () {
         captureDataDeleteUser();
         window.location.href = "https://oofaround.appspot.com/";
     });

     document.getElementById("logout").addEventListener("click", function () {
         localStorage.clear();
         alert("Sessão terminada.")
         window.location.href = "https://oofaround.appspot.com/";
     });

     document.getElementById("altera").addEventListener("click", function () {
         captureDataChangeUserInfo();
         //window.location.href = "https://oofaround.appspot.com/";
     });
 };
