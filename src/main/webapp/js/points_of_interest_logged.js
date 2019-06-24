captureDataGetPointsOfInterest = function (event) {
    var limit = 5;
    var lastName = "";
    var x = document.getElementsByClassName("ed");
    var res = []
    var index = 0;
    for (var i = 0; i < x.length; i++) {
        if (x[i].checked) {
            res[index] = x[i].value;
            index ++;
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

            for(i = 0; i < Response.locations.length; i++) {
                var z = Response.locations[i].category;
                if( z == "Sport") {
                    z = "Desporto";
                }
                else if (z == "Culture") {
                    z = "Cultura";
                }
                else if (z == "NightLife") {
                    z = "Vida Noturna";
                }
                else if (z == "Leisure") {
                    z = "Lazer";
                }
                else if (z == "Animal & WildLife") {
                    z = "Animais e Vida Selvagem";
                }
                else if (z == "Outdoor & Pets") {
                    z = "Ar livre e Animais Domésticos";
                }
                else if (z == "Beach") {
                    z = "Praias";
                }
                else if (z == "Food & Drink") {
                    z = "Comes e Bebes";
                }
                else if (z == "Landscaping") {
                    z = "Paisagens";
                }
            
                var div = document.createElement("div");
                div.style.marginLeft = "3px";
                div.setAttribute("class", "tabcontent");
                document.getElementById("berna").appendChild(div);

                var div_2 = document.createElement("div");
                div_2.style.textAlign = "center";
                div.appendChild(div_2);

                var header = document.createElement("h4");
                header.innerHTML = Response.locations[i].name;
                div_2.appendChild(header);

                var header_2 = document.createElement("h4");
                header_2.style.textAlign = "left";
                header_2.style.fontSize = "16px"
                header_2.innerHTML = "Morada: " + Response.locations[i].address;
                div_2.appendChild(header_2);

                var header_3 = document.createElement("h4");
                header_3.style.textAlign = "left";
                header_3.style.fontSize = "16px"
                header_3.innerHTML = "Categoria: " + z;
                div_2.appendChild(header_3);

                var header_4 = document.createElement("h4");
                header_4.style.textAlign = "left";
                header_4.style.fontSize = "16px"
                header_4.innerHTML = "Região: " + Response.locations[i].region;
                div_2.appendChild(header_4);


                var p = document.createElement("p");
                p.align = "right";
                div_2.appendChild(p);

                var button = document.createElement("button");
                button.setAttribute("data-toggle", "modal");
                button.setAttribute("data-target", "#loginForm");
                button.style.marginBottom = "3px";
                button.innerHTML = "Saber Mais";
                p.appendChild(button);

                var div_3 = document.createElement("div");
                div_3.setAttribute("class", "md-form mb-5");
                document.getElementById("berna_modal").appendChild(div_3);

                var label = document.createElement("label");
                label.setAttribute("data-error", "wrong");
                label.setAttribute("data-success", "right");
                label.innerHTML = Response.locations[i].name;
                div_3.appendChild(label);

                var br = document.createElement("br");
                document.getElementById("berna_modal").appendChild(br);

                var div_4 = document.createElement("div");
                div_4.setAttribute("class", "md-form mb-4");
                document.getElementById("berna_modal").appendChild(div_4);

                var label_2 = document.createElement("label");
                label_2.setAttribute("data-error", "wrong");
                label_2.setAttribute("data-success", "right");
                label_2.innerHTML = "Morada:";
                div_4.appendChild(label_2);

                var p_2 = document.createElement("p");
                p_2.innerHTML = Response.locations[i].address;
                div_4.appendChild(p_2);

                var div_5 = document.createElement("div");
                div_5.setAttribute("class", "md-form mb-4");
                document.getElementById("berna_modal").appendChild(div_5);

                var label_3 = document.createElement("label");
                label_3.setAttribute("data-error", "wrong");
                label_3.setAttribute("data-success", "right");
                label_3.innerHTML = "Categoria:";
                div_5.appendChild(label_3);

                var p_3 = document.createElement("p");
                p_3.innerHTML = Response.locations[i].category;
                div_5.appendChild(p_3);

                var div_6 = document.createElement("div");
                div_6.setAttribute("class", "md-form mb-4");
                document.getElementById("berna_modal").appendChild(div_6);

                var label_4 = document.createElement("label");
                label_4.setAttribute("data-error", "wrong");
                label_4.setAttribute("data-success", "right");
                label_4.innerHTML = "Latitude:";
                div_6.appendChild(label_4);

                var p_4 = document.createElement("p");
                p_4.innerHTML = Response.locations[i].latitude;
                div_6.appendChild(p_4);

                var div_7 = document.createElement("div");
                div_7.setAttribute("class", "md-form mb-4");
                document.getElementById("berna_modal").appendChild(div_7);

                var label_5 = document.createElement("label");
                label_5.setAttribute("data-error", "wrong");
                label_5.setAttribute("data-success", "right");
                label_5.innerHTML = "Longitude:";
                div_7.appendChild(label_5);

                var p_5 = document.createElement("p");
                p_5.innerHTML = Response.locations[i].longitude;
                div_7.appendChild(p_5);

                var div_8 = document.createElement("div");
                div_8.setAttribute("class", "md-form mb-4");
                document.getElementById("berna_modal").appendChild(div_8);

                var label_6 = document.createElement("label");
                label_6.setAttribute("data-error", "wrong");
                label_6.setAttribute("data-success", "right");
                label_6.innerHTML = "Região:";
                div_8.appendChild(label_6);

                var p_6 = document.createElement("p");
                p_6.innerHTML = Response.locations[i].region;
                div_8.appendChild(p_6);

                var div_9 = document.createElement("div");
                div_9.setAttribute("class", "md-form mb-4");
                document.getElementById("berna_modal").appendChild(div_9);

                var label_7 = document.createElement("label");
                label_7.setAttribute("data-error", "wrong");
                label_7.setAttribute("data-success", "right");
                label_7.innerHTML = "Descrição:";
                div_9.appendChild(label_7);

                var p_7 = document.createElement("p");
                p_7.innerHTML = Response.locations[i].description;
                div_9.appendChild(p_7);


                lastName = Response.locations[i].name;
            }
            console.log(Response);
            alert("Pesquisa com Sucesso");
        },
        error: function (Response) {
            alert('Falha ao Pesquisar');
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