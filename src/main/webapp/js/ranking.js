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
         success: function (response) {
        	 console.log(response);
             var listDiv = document.getElementById('ranking');
             var ul = document.createElement('ul');
             ul.className = "container table table-danger";
             for (var i = 0; i < response.scores.length; ++i) {
                 var li = document.createElement('li');
                 li.className = "row";
                 var a = document.createElement('a');
                 var user = response.scores[i].username;
                 user.className = "col-xs-12 col-sm-6";
                 var score = response.scores[i].score;
                 score.className = "col-xs-12 col-sm-6";
                 a.innerHTML = user + "    " + score;
                 li.appendChild(a);
                 ul.appendChild(li);
             }
             listDiv.appendChild(ul);
             last = response.scores[request - 1].username;
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