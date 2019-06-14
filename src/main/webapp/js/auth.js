window.onload = function () {
    var frmsr = $('form[name="register"]');
    var frmsl = $('form[name="login"]');
    frmsl[0].onsubmit = captureDataLogin;
    frmsr[0].onsubmit = captureDataRegister;
};

captureDataGetImage = function () {
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
            alert("Sessão iniciada.");
            window.location.href = "https://oofaround.appspot.com/homepage_logged.html";
        },
        error: function (Response) {
        },
        data: JSON.stringify(values) // post data || get data
    });
};

captureDataRegister = function (event) {
    var values = {};
    $.each($('form[name="register"]').serializeArray(), function (i, field) {
        values[field.name] = field.value;
    });
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/register/user",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {},
        error: function (Response) {
            if (Response.status == 200) {
                alert("Registo efetuado com sucesso.");
                window.location.href = "https://oofaround.appspot.com/";
            } else {
                alert("Registo falhado.");
                window.location.href = "https://oofaround.appspot.com/";
            }

        },
        data: JSON.stringify(values) // post data || get data
    });
    event.preventDefault();
};

captureDataLogin = function (event) {
    var values = {};
    $.each($('form[name="login"]').serializeArray(), function (i, field) {
        values[field.name] = field.value;
    });
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/login/",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            var date = new Date();
            localStorage.setItem('username', Response.username);
            localStorage.setItem('token', Response.tokenID);
            localStorage.setItem('role', Response.role);
            localStorage.setItem('expiration', date.getTime() + 300000);
            captureDataGetImage();
        },
        error: function (Response) {
            alert("Falha ao iniciar sessão.");
            window.location.href = "https://oofaround.appspot.com/";
        },
        data: JSON.stringify(values)
    });
    event.preventDefault();
};