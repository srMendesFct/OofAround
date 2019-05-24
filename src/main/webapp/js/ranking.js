 var request = 5;
 var lastRequest = 0;
 var last = "";
 

 captureDataR = function () {
    var values = {
        tokenID: localStorage.getItem('token'),
        usernameR: localStorage.getItem('username'),
        role: localStorage.getItem('role'),
        username: localStorage.getItem('username'),
        lastRequest: lastRequest,
        limit: request,
        lastUsername: last
    }
     $.ajax({
         type: "POST",
         url: "https://oofaround.appspot.com/rest/list/publicranking",
         contentType: "application/json;charset=utf-8",
         dataType: 'json', // data type        
         crossDomain: true,
         success: function (Response) {
             var listDiv = document.getElementById('ranking');
             var ul = document.createElement('ul');
             ul.className = "list-group list-group-flush";
             for (var i = 0; i < Response.scores.length; ++i) {
                 var li = document.createElement('li');
                 li.className = "list-group-item d-flex align-items-center";
                 li.setAttribute("style", "background-color:#ff4d4d");
                 var a = document.createElement('a');
                 var user = Response.scores[i].username;
                 var score = Response.scores[i].score;
                 score.setAttribute("style", "color:red")
                 a.innerHTML = user + "                                                      " + score;
                 li.appendChild(a);
                 ul.appendChild(li);
             }
             listDiv.appendChild(ul);
             last = Response.scores[request - 1].username;
             lastRequest = lastRequest + request;
         },
         error: function (response) {},
         data: JSON.stringify(values) // post data || get data
     });
 };
 var user = localStorage.getItem('username');

 window.onload = function () {
    document.getElementById("user").innerHTML = user;
    document.getElementById("profilePic").src = 'https://storage.googleapis.com/oofaround.appspot.com/' + localStorage.getItem('username') + '_perfil';
     var token = localStorage.getItem('expiration');
     var date = new Date();
     var longday = date.getTime();
     if (longday > token) {
         localStorage.clear();
         window.location.href = "https://oofaround.appspot.com/";
     } else {
         captureDataR();
         setupCallback();
     }
 };

 setupCallback = function () {
     document.getElementById("vermais").addEventListener("click", function () {
         captureDataR();
     });
 };