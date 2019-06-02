window.onload = function () {
    var frmsr = $('form[name="register"]');
    var frmsl = $('form[name="login"]');
    frmsl[0].onsubmit = captureDataL;
    frmsr[0].onsubmit = captureDataR;
};

captureDataR = function (event) {
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
            console.log(Response.status);
            if (Response.status == 200) {
                alert("Registo efetuado com sucesso.");
                var image = 'image/png;base64,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';
                localStorage.setItem('image', image);
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

captureDataL = function (event) {
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
            localStorage.setItem('expiration', date.getTime() + 300000 );
            alert("Sessão iniciada.");
            window.location.href = "https://oofaround.appspot.com/homepage_logged.html";

        },
        error: function (Response) {
            alert("Falha ao iniciar sessão.");
            window.location.href = "https://oofaround.appspot.com/";
        },
        data: JSON.stringify(values)
    });
    event.preventDefault();
};