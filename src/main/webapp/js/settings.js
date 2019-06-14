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
             } else {
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
     $.ajax({
         type: "POST",
         url: "https://oofaround.appspot.com/rest/userinfo/self",
         contentType: "application/json;charset=utf-8",
         dataType: 'json', // data type        
         crossDomain: true,
         success: function (Response) {
             localStorage.setItem('email', Response.email);
             localStorage.setItem('country', Response.country);
             localStorage.setItem('cellphone', Response.cellphone);
             if (Response.privacy == "public") {
                 localStorage.setItem('privacy', "Público")
             } else {
                 localStorage.setItem('privacy', "Privado");
             }
         },
         error: function (Response) {},
         data: JSON.stringify(values) // post data || get data
     });
 };

 captureDataChangeUserInfo = function () {
     var values = {
         tokenID: localStorage.getItem('token'),
         role: localStorage.getItem('role'),
         usernameR: localStorage.getItem('username'),
     };
     $.each($('form[name="change"]').serializeArray(), function (i, field) {
         values[field.name] = field.value;
     });
     console.log(JSON.stringify(values));
     $.ajax({
         type: "POST",
         url: "https://oofaround.appspot.com/rest/userinfo/alterself",
         contentType: "application/json;charset=utf-8",
         dataType: 'json', // data type        
         crossDomain: true,
         success: function (Response) {
            console.log(Response.status);
            alert("Alteração efetuada com sucesso.");
         },
         error: function (Response) {
            console.log(Response.status);
            alert("Falha ao alterar os dados.");
         },
         data: JSON.stringify(values) // post data || get data
     });
 };

 window.onload = function (event) {
     captureDataGetUserInfo();
     var user = localStorage.getItem('username');
     var image = localStorage.getItem('image');
     var date = new Date();
     var token = localStorage.getItem('expiration');
     var longday = date.getTime();
     var email = localStorage.getItem('email');
     var country = localStorage.getItem('country');
     var cellphone = localStorage.getItem('cellphone');
     var privacy = localStorage.getItem('privacy');
     document.getElementById("userC").value = localStorage.getItem('username');;
     document.getElementById("emailC").value = email;
     document.getElementById("countryC").value = country;
     document.getElementById("cellphoneC").value = cellphone;
     document.getElementById("privacy").value = privacy;
     document.getElementById("profilePic").src = 'data:image/jpeg;base64, ' + image;
     document.getElementById("user").innerHTML = user;
     document.getElementById("profilePicBig").src = 'data:image/jpeg;base64, ' + image;
     if (longday > token) {
         localStorage.clear();
         window.location.href = "https://oofaround.appspot.com/";
     } else {
         localStorage.setItem('expiration', date.getTime() + 300000);
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
