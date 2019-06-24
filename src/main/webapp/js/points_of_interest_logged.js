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
                header_2.innerHTML = Response.locations[i].address + ":";
                div_2.appendChild(header_2);


                var header_3 = document.createElement("h4");
                header_2.style.textAlign = "left";
                header_2.innerHTML = Response.locations[i].latitude + ":";
                div_2.appendChild(header_3);


                var header_4 = document.createElement("h4");
                header_2.style.textAlign = "left";
                header_2.innerHTML = Response.locations[i].longitude + ":";
                div_2.appendChild(header_4);


                var header_5 = document.createElement("h4");
                header_2.style.textAlign = "left";
                header_2.innerHTML = Response.locations[i].region + ":";
                div_2.appendChild(header_5);


                var p = document.createElement("p");
                p.align = "right";
                div_2.appendChild(p);

                var button = document.createElement("button");
                button.dataToggle = "modal";
                button.dataTarget = "#loginForm";
                button.style.marginBottom = "3px";
                button.innerHTML = "Saber Mais";
                p.appendChild(button);
            }
            lastName = 0; //buscar o ultimo elemento
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
