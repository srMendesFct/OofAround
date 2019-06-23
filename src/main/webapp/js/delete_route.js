captureDeleteRoute = function () {
    var values = {};
    values['tokenID'] = localStorage.getItem('token');
    values['usernameR'] = localStorage.getItem('username');
    values['role'] = localStorage.getItem('role');
  
    $.each($('form[name="deleteCourseForm"]').serializeArray(), function (i, field) {
      values[field.name] = field.value;
    });
  
    $.ajax({
      type: "POST",
      url: "https://oofaround.appspot.com/rest/route/delete",
      contentType: "application/json;charset=utf-8",
      dataType: 'json',
      crossDomain: 'true',
      success: function (response) {
        alert('Percurso eliminado!');
      },
      error: function (response) {
        alert('Erro!')
      },
      data: JSON.stringify(values)
    });
  
  }

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
    }

    document.getElementById('submitD').addEventListener('click', function () {
        captureDeleteRoute();
      });
  }