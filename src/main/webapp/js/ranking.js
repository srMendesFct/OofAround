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
             for (var i = 0; i < Response.scores.length; ++i) {
                 var li = document.createElement('li');
                 var a = document.createElement('a');
                 a.innerHTML = Response.scores[i].username + ": " + Response.scores[i].score;
                 li.appendChild(a);
                 ul.appendChild(li);
             }
             listDiv.appendChild(ul);
             last = Response.scores[request - 1].username;
             lastRequest = lastRequest + request;
         },
         error: function (Response) {},
         data: JSON.stringify(values) // post data || get data
     });
 };

 window.onload = function () {
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