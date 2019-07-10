captureDataListComments = function (evt) {
    var values = {};
    values['tokenID'] = localStorage.getItem('token');
    values['role'] = localStorage.getItem('role');
    values['usernameR'] = localStorage.getItem('username');
    $.each($('form[name="listar"]').serializeArray(), function (i, field) {
        values[field.name] = field.value;
    });

    localStorage.setItem('criador', values[routeCreatorUsername]);
    localStorage.setItem('nome', values[routeName]);

    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/comment/listcomments",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            console.log(Response);
            tabcontent = document.getElementsByClassName("tabcontent");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].style.display = "none";
            }

            for (i = 0; i < Response.comments[i].length; i++) {

                var div = document.createElement("div");
                div.style.marginLeft = "3px";
                div.setAttribute("class", "tabcontent");
                document.getElementById("berna").appendChild(div);

                var div_2 = document.createElement("div");
                div_2.style.textAlign = "center";
                div.appendChild(div_2);

                var header = document.createElement("h4");
                header.innerHTML = Response.comments[i].comment;
                div_2.appendChild(header);

                var header_2 = document.createElement("h4");
                header_2.style.textAlign = "left";
                header_2.style.fontSize = "16px"
                header_2.innerHTML = Response.comments[i].poster + ": " + Response.comments[i].date;
                div_2.appendChild(header_2);
            }

            var header_3 = document.createElement("h4");
            header_3.style.textAlign = "left";
            header_3.style.fontSize = "16px"
            header_3.innerHTML = "Faça um comentário:";
            document.getElementById("berna").appendChild(header_3);

            var form = document.createElement("form");
            form.setAttribute("name", "postar");
            form.setAttribute("class", "pure-form pure-form-stacked block");
            form.setAttribute("accept-charset", "utf-8");
            document.getElementById("berna").appendChild(form);

            var div_3 = document.createElement("div");
            div_3.setAttribute("class", "form-group row");
            form.appendChild(div_3);

            var label = document.createElement("label");
            label.setAttribute("class", "col-sm-2 col-form-label");
            div_3.appendChild(label);
            label.innerHTML = "Comentário: "

            var br = document.createElement("br");
            div_3.appendChild(br);

            var div_4 = document.createElement("div");
            div_4.setAttribute("class", "col-sm-10");
            div_3.appendChild(div_4);

            var textarea = document.createElement("textarea");
            textarea.setAttribute("class", "form-control validate");
            textarea.setAttribute("name", "comment");
            div_4.appendChild(textarea);

            var div_5 = document.createElement("div");
            div_5.setAttribute("class", "modal-footer d-flex justify-content-center");
            form.appendChild(div_5);

            var button = document.createElement("button");
            button.setAttribute("class", "btn btn-default");
            button.setAttribute("type", "submit");
            div_5.appendChild(button);
            
            var posts = $('form[name="postar"]');
            posts[0].onsubmit = captureDataPostComment;
        },
        error: function (Response) {
            if (Response.status == 404) {

                var header_3 = document.createElement("h4");
                header_3.style.textAlign = "left";
                header_3.style.fontSize = "16px"
                header_3.innerHTML = "Percurso sem comentários, seja o primeiro!";
                document.getElementById("berna").appendChild(header_3);

                var form = document.createElement("form");
                form.setAttribute("name", "postar");
                form.setAttribute("class", "pure-form pure-form-stacked block");
                form.setAttribute("accept-charset", "utf-8");
                document.getElementById("berna").appendChild(form);

                var div_3 = document.createElement("div");
                div_3.setAttribute("class", "form-group row");
                form.appendChild(div_3);

                var label = document.createElement("label");
                label.setAttribute("class", "col-sm-2 col-form-label");
                div_3.appendChild(label);
                label.innerHTML = "Comentário: "

                var br = document.createElement("br");
                div_3.appendChild(br);

                var div_4 = document.createElement("div");
                div_4.setAttribute("class", "col-sm-10");
                div_3.appendChild(div_4);

                var textarea = document.createElement("textarea");
                textarea.setAttribute("class", "form-control validate");
                textarea.setAttribute("name", "comment");
                div_4.appendChild(textarea);

                var div_5 = document.createElement("div");
                div_5.setAttribute("class", "modal-footer d-flex justify-content-center");
                form.appendChild(div_5);

                var button = document.createElement("button");
                button.setAttribute("class", "btn btn-default");
                button.setAttribute("type", "submit");
                div_5.appendChild(button);

                var posts = $('form[name="postar"]');
                posts[0].onsubmit = captureDataPostComment;

            } else {
                alert('erro');
            }
        },
        data: JSON.stringify(values) // post data || get data
    });
    evt.preventDefault();
};

captureDataPostComment = function (criador, nome, evt) {
    var values = {};
    values['tokenID'] = localStorage.getItem('token');
    values['role'] = localStorage.getItem('role');
    values['usernameR'] = localStorage.getItem('username');
    values['routeName'] = localStorage.getItem('nome');
    values['routeCreatorUsername'] = localStorage.getItem('criador');
    $.each($('form[name="postar"]').serializeArray(), function (i, field) {
        values[field.name] = field.value;
    });
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/comment/post",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            console.log(Response);
        },
        error: function (Response) {
            alert('erro');
        },
        data: JSON.stringify(values) // post data || get data
    });
    evt.preventDefault();
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
    var comments = $('form[name="listar"]');
    comments[0].onsubmit = captureDataListComments;

    document.getElementById("logout").addEventListener("click", function () {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    });
};