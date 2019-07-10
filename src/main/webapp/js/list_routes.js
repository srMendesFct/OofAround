captureDataGetUserInfo = function (values) {
    var values = {
        tokenID: localStorage.getItem('token'),
        role: localStorage.getItem('role'),
        usernameR: localStorage.getItem('username'),
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
            window.location.href = "https://oofaround.appspot.com/homepage_logged.html";
        },
        error: function (Response) {},
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
        dataType: 'json',
        crossDomain: true,
        success: function (Response) {
            var date = new Date();
            localStorage.setItem('username', Response.username);
            localStorage.setItem('token', Response.tokenID);
            localStorage.setItem('role', Response.role);
            localStorage.setItem('expiration', date.getTime() + 300000);
            if (localStorage.getItem('role') == "user") {
                captureDataGetImage();
                captureDataGetUserInfo();
            } else {
                window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
            }
        },
        error: function () {
            alert("Falha ao iniciar sessão.");
            window.location.href = "https://oofaround.appspot.com/";
        },
        data: JSON.stringify(values)
    });
    event.preventDefault();
};

var marosca = 100;

captureDataGetRoutes = function (event) {
    var x = document.getElementsByClassName("ed");
    var res = []
    var index = 0;
    for (var i = 0; i < x.length; i++) {
        if (x[i].checked) {
            res[index] = x[i].value;
            index++;
        }
    }
    var values = {
        region: document.getElementById("distrito").value,
        categories: res
    };
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/route/guest/listall",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {

            tabcontent = document.getElementsByClassName("tabcontent");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].style.display = "none";
            }

            for (i = 0; i < Response.routes.length; i++) {
                var x = [];
                var y = [];
                for (j = 0; j < Response.routes[i].categories.length; j++) {
                    var z = Response.routes[i].categories[j].category;

                    if (z != "não especificada") {
                        if (z == "Sport") {
                            z = "Desporto";
                        } else if (z == "Culture") {
                            z = "Cultura";
                        } else if (z == "NightLife") {
                            z = "Vida Noturna";
                        } else if (z == "Leisure") {
                            z = "Lazer";
                        } else if (z == "Animal & Wildlife") {
                            z = "Animais e Vida Selvagem";
                        } else if (z == "Outdoor & Pets") {
                            z = "Ar livre e Animais Domésticos";
                        } else if (z == "Beach") {
                            z = "Praias";
                        } else if (z == "Food & Drink") {
                            z = "Comes e Bebes";
                        } else if (z == "Landscaping") {
                            z = "Paisagens";
                        } else if (z == "Religion") {
                            z = "Religião";
                        }
                        x[j] = z;
                    }
                }
                for (j = 0; j < Response.routes[i].regions.length; j++) {
                    var z = Response.routes[i].regions[j].region;
                    if (z != "Portugal") {
                        y[j] = z;
                    }
                }

                var div = document.createElement("div");
                div.style.marginLeft = "3px";
                div.setAttribute("class", "tabcontent");
                document.getElementById("berna").appendChild(div);

                var div_2 = document.createElement("div");
                div_2.style.textAlign = "center";
                div.appendChild(div_2);

                var header = document.createElement("h4");
                header.innerHTML = Response.routes[i].name;
                div_2.appendChild(header);

                var header_2 = document.createElement("h4");
                header_2.style.textAlign = "left";
                header_2.style.fontSize = "16px"
                header_2.innerHTML = "Rating: " + Response.routes[i].rating;
                div_2.appendChild(header_2);

                var header_3 = document.createElement("h4");
                header_3.style.textAlign = "left";
                header_3.style.fontSize = "16px"
                header_3.innerHTML = "Categorias: " + x;
                div_2.appendChild(header_3);

                var header_4 = document.createElement("h4");
                header_4.style.textAlign = "left";
                header_4.style.fontSize = "16px"
                header_4.innerHTML = "Região: " + y;
                div_2.appendChild(header_4);

                var p = document.createElement("p");
                p.align = "right";
                div_2.appendChild(p);

                var img = document.createElement("img");
                img.src = "img/edit-pencil-icon-vector-13483315.jpg";
                img.setAttribute("class", "imgXS");
                p.appendChild(img);

                var button = document.createElement("button");
                button.setAttribute("data-toggle", "modal");
                button.setAttribute("data-target", "#loginForm" + marosca);
                button.style.marginBottom = "3px";
                button.innerHTML = "Saber Mais";
                p.appendChild(button);

                var div_3 = document.createElement("div");
                div_3.setAttribute("class", "modal fade");
                div_3.setAttribute("id", "loginForm" + marosca);
                div_3.setAttribute("tabindex", "-1");
                div_3.setAttribute("role", "dialog");
                div_3.setAttribute("aria-labelledby", "myModalLabel");
                div_3.setAttribute("aria-hidden", "true");
                document.getElementById("body").appendChild(div_3);

                marosca = marosca + 1;

                var div_4 = document.createElement("div");
                div_4.setAttribute("class", "modal-dialog");
                div_4.setAttribute("role", "document");
                div_3.appendChild(div_4);

                var div_5 = document.createElement("div");
                div_5.setAttribute("class", "modal-content");
                div_4.appendChild(div_5);

                var div_6 = document.createElement("div");
                div_6.setAttribute("class", "modal-header text-center");
                div_5.appendChild(div_6);

                var img_2 = document.createElement("img");
                img_2.src = "img/logo.png";
                div_6.appendChild(img_2);

                var button_2 = document.createElement("button");
                button_2.setAttribute("type", "button");
                button_2.setAttribute("class", "close");
                button_2.setAttribute("data-dismiss", "modal");
                button_2.setAttribute("aria-label", "Close");
                div_6.appendChild(button_2);

                var span = document.createElement("span");
                span.setAttribute("aria-hidden", "true");
                span.innerHTML = "&times";
                button_2.appendChild(span);

                var form = document.createElement("form");
                form.setAttribute("id", "login");
                form.setAttribute("name", "login");
                div_5.appendChild(form);

                var div_7 = document.createElement("div");
                div_7.setAttribute("class", "modal-body mx-3");
                form.appendChild(div_7);

                var div_8 = document.createElement("div");
                div_8.setAttribute("class", "md-form mb-5");
                div_7.appendChild(div_8);

                var label = document.createElement("label");
                label.setAttribute("data-error", "wrong");
                label.setAttribute("data-success", "right");
                label.innerHTML = Response.routes[i].name;
                div_8.appendChild(label);

                var br = document.createElement("br");
                div_8.appendChild(br);

                var br_2 = document.createElement("br");
                div_7.appendChild(br_2);

                var div_10 = document.createElement("div");
                div_10.setAttribute("class", "md-form mb-4");
                div_7.appendChild(div_10);

                var label_2 = document.createElement("label");
                label_2.setAttribute("data-error", "wrong");
                label_2.setAttribute("data-success", "right");
                label_2.innerHTML = "Categorias:";
                div_10.appendChild(label_2);

                var p_3 = document.createElement("p");
                p_3.innerHTML = x;
                div_10.appendChild(p_3);

                var div_13 = document.createElement("div");
                div_13.setAttribute("class", "md-form mb-4");
                div_7.appendChild(div_13);

                var label_6 = document.createElement("label");
                label_6.setAttribute("data-error", "wrong");
                label_6.setAttribute("data-success", "right");
                label_6.innerHTML = "Região:";
                div_13.appendChild(label_6);

                var p_4 = document.createElement("p");
                p_4.innerHTML = y;
                div_13.appendChild(p_4);

                var div_14 = document.createElement("div");
                div_14.setAttribute("class", "md-form mb-4");
                div_7.appendChild(div_14);

                var label_7 = document.createElement("label");
                label_7.setAttribute("data-error", "wrong");
                label_7.setAttribute("data-success", "right");
                label_7.innerHTML = "Descrição:";
                div_14.appendChild(label_7);

                var p_5 = document.createElement("p");
                p_5.innerHTML = Response.routes[i].description;
                div_14.appendChild(p_5);

                var div_15 = document.createElement("div");
                div_15.setAttribute("class", "md-form mb-4");
                div_7.appendChild(div_15);

                var label_8 = document.createElement("label");
                label_8.setAttribute("data-error", "wrong");
                label_8.setAttribute("data-success", "right");
                label_8.innerHTML = "Criado por:";
                div_15.appendChild(label_8);

                var p_6 = document.createElement("p");
                p_6.innerHTML = Response.routes[i].creatorUsername;
                div_15.appendChild(p_6);
            }
        },
        error: function (Response) {
            alert('Falha ao Pesquisar');
        },
        data: JSON.stringify(values) // post data || get data
    });
    event.preventDefault();
};

window.onload = function () {
    var frmsr = $('form[name="register"]');
    var frmsl = $('form[name="login"]');
    frmsl[0].onsubmit = captureDataLogin;
    frmsr[0].onsubmit = captureDataRegister;
    var pesquisa = $('form[name="categorias"]');
    pesquisa[0].onsubmit = captureDataGetRoutes;
};