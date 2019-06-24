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
            }
            lastName = Response.locations[i].name;
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
