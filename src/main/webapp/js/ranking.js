captureDataRanking = function () {
    var values = {
        tokenID: localStorage.getItem('token'),
        usernameR: localStorage.getItem('username'),
        role: localStorage.getItem('role'),
    }
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/ranking/getAll",
        contentType: "application/json;charset=utf-8",
        dataType: 'json', // data type        
        crossDomain: true,
        success: function (Response) {
            console.log(Response);
            var rank = 0;
            for (var i = 0; i < Response.ranks.length; i++) {
                rank = parseInt(Response.ranks[i].rank)++;
                var div = document.createElement("div");
                div.style.marginLeft = "3px";
                div.setAttribute("class", "tabcontent");
                document.getElementById("berna").appendChild(div);

                var div_2 = document.createElement("div");
                div.appendChild(div_2);

                var header = document.createElement("h4");
                header.innerHTML = "Posição: " + rank;
                div_2.appendChild(header);

                var header_2 = document.createElement("h4");
                header_2.style.textAlign = "left";
                header_2.style.fontSize = "16px"
                header_2.innerHTML = Response.ranks[i].username;
                div_2.appendChild(header_2);

                var header_3 = document.createElement("h4");
                header_3.style.textAlign = "left";
                header_3.style.fontSize = "16px"
                header_3.innerHTML = "Pontos: " + Response.ranks[i].score;
                div_2.appendChild(header_3);
            }

        },
        error: function (response) {},
        data: JSON.stringify(values) // post data || get data
    });
};
var user = localStorage.getItem('username');
var image = localStorage.getItem('image');
window.onload = function () {
    var date = new Date();
    document.getElementById("user").innerHTML = user;
    document.getElementById("profilePic").src = 'data:image/jpeg;base64, ' + image;
    var token = localStorage.getItem('expiration');
    var date = new Date();
    var longday = date.getTime();
    if (longday > token) {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    } else {
        localStorage.setItem('expiration', date.getTime() + 300000);
        captureDataRanking();
        setupCallback();
    }
};

setupCallback = function () {
    document.getElementById("logout").addEventListener("click", function () {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    });
};