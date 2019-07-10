captureDataGetRecoverCode = function (evt) {
    var values = {};
    $.each($('form[name="user"]').serializeArray(), function (i, field) {
        values[field.name] = field.value;
    });
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/userinfo/getrecovercode",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {

        },
        error: function (Response) {
            if (Response.status == 200) {
                localStorage.setItem('username', values['usernameR']);
            }
            else {
                alert("Erro");
            }
        },
        data: JSON.stringify(values) // post data || get data
    });
    evt.preventDefault();
};

captureDataChangePassword = function (evt) {
    var values = {};
    values ['usernameR'] = localStorage.getItem('username');
    $.each($('form[name="pass"]').serializeArray(), function (i, field) {
        values[field.name] = field.value;
    });
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/userinfo/recoverpassword",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            window.location.href = "https://oofaround.appspot.com";
        },
        error: function (Response) {
            alert("Falha ao alterar a password");
        },
        data: JSON.stringify(values) // post data || get data
    });
    evt.preventDefault();
};

window.onload = function () {
    localStorage.clear();
    var code = $('form[name="user"]');
    var change = $('form[name="pass"]');
    change[0].onsubmit = captureDataChangePassword;
    code[0].onsubmit = captureDataGetRecoverCode;
};