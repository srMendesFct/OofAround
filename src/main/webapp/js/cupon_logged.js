var marosca = 100;

captureDataGetCupon = function () {
    var values = {
        tokenID: localStorage.getItem('token'),
        role: localStorage.getItem('role'),
        usernameR: localStorage.getItem('username'),
        region: document.getElementById("distrito").value,
    };
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/cupon/list",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            console.log(Response);

            for (i = 0; i < Response.cupons.length; i++) {

                var div = document.createElement("div");
                div.style.marginLeft = "3px";
                div.setAttribute("class", "tabcontent");
                document.getElementById("berna").appendChild(div);

                var div_2 = document.createElement("div");
                div_2.style.textAlign = "center";
                div.appendChild(div_2);

                var header = document.createElement("h4");
                header.innerHTML = Response.cupons[i].description;
                div_2.appendChild(header);

                var header_2 = document.createElement("h4");
                header_2.style.textAlign = "left";
                header_2.style.fontSize = "16px"
                header_2.innerHTML = "Local: " + Response.cupons[i].locationName;
                div_2.appendChild(header_2);

                var header_4 = document.createElement("h4");
                header_4.style.textAlign = "left";
                header_4.style.fontSize = "16px"
                header_4.innerHTML = "Região: " + Response.cupons[i].region;
                div_2.appendChild(header_4);

                var p = document.createElement("p");
                p.align = "right";
                div_2.appendChild(p);

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

                var img_3 = document.createElement("img");
                img_3.src = "img/logo.png";
                div_6.appendChild(img_3);

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
                label.innerHTML = Response.cupons[i].description;
                div_8.appendChild(label);

                var br = document.createElement("br");
                div_8.appendChild(br);

                var br_2 = document.createElement("br");
                div_7.appendChild(br_2);

                var div_10 = document.createElement("div");
                div_10.setAttribute("class", "md-form mb-4");
                div_7.appendChild(div_10);

                var label_3 = document.createElement("label");
                label_3.setAttribute("data-error", "wrong");
                label_3.setAttribute("data-success", "right");
                label_3.innerHTML = "Local:";
                div_10.appendChild(label_3);

                var p_3 = document.createElement("p");
                p_3.innerHTML = Response.cupons[i].locationName;
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
                p_4.innerHTML = Response.cupons[i].region;
                div_13.appendChild(p_4);

                var div_14 = document.createElement("div");
                div_14.setAttribute("class", "md-form mb-4");
                div_7.appendChild(div_14);

                var label_7 = document.createElement("label");
                label_7.setAttribute("data-error", "wrong");
                label_7.setAttribute("data-success", "right");
                label_7.innerHTML = "Valor:";
                div_14.appendChild(label_7);

                var p_5 = document.createElement("p");
                p_5.innerHTML = Response.cupons[i].value;
                div_14.appendChild(p_5);
            }
        },
        error: function (Response) {
            alert('Falha ao Pesquisar');
        },
        data: JSON.stringify(values) // post data || get data
    });
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
        document.getElementById("searchP").addEventListener('click', function () {
            captureDataGetCupon();   
        });
    }
}