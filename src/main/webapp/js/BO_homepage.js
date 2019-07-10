function openCity(evt, cityName) {
  // Declare all variables
  var i, tabcontent, tablinks;

  // Get all elements with class="tabcontent" and hide them
  tabcontent = document.getElementsByClassName("tabcontent");
  for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
  }

  // Get all elements with class="tablinks" and remove the class "active"
  tablinks = document.getElementsByClassName("tablinks");
  for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }

  // Show the current tab, and add an "active" class to the link that opened the tab
  document.getElementById(cityName).style.display = "block";
  evt.currentTarget.className += " active";
}

function home() {
  var i, tabcontent, tablinks;

  // Get all elements with class="tabcontent" and hide them
  tabcontent = document.getElementsByClassName("tabcontent");
  for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
  }

  // Get all elements with class="tablinks" and remove the class "active"
  tablinks = document.getElementsByClassName("tablinks");
  for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }
}

captureDataCreatePointOfInterest = function (event) {
  var fileReader = new FileReader();
  var file = document.getElementById("fileID").files[0];
  var s;
  fileReader.readAsBinaryString(file);
  fileReader.onload = function () {
    s = fileReader.result;
    var send = window.btoa(s);
    var values = {}
    values['image'] = send,
      values['usernameR'] = localStorage.getItem('username');
    values['tokenID'] = localStorage.getItem('token');
    values['role'] = localStorage.getItem('role');
    $.each($('form[name="Criar ponto"]').serializeArray(), function (i, field) {
      values[field.name] = field.value;
    });
    console.log(JSON.stringify(values));
    $.ajax({
      type: "POST",
      url: "https://oofaround.appspot.com/rest/location/create",
      contentType: "application/json;charset=utf-8",
      dataType: 'json', // data type        
      crossDomain: true,
      success: function (Response) {
        window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
      },
      error: function (Response) {
        alert('erro');
        window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
      },
      data: JSON.stringify(values) // post data || get data
    });
  }
  event.preventDefault();
};

captureDataCreateCupon = function (event) {
    var values = {}
    values['usernameR'] = localStorage.getItem('username');
    values['tokenID'] = localStorage.getItem('token');
    values['role'] = localStorage.getItem('role');
    $.each($('form[name="Criar cupao"]').serializeArray(), function (i, field) {
      values[field.name] = field.value;
    });
    console.log(JSON.stringify(values));
    $.ajax({
      type: "POST",
      url: "https://oofaround.appspot.com/rest/cupon/create",
      contentType: "application/json;charset=utf-8",
      dataType: 'json', // data type        
      crossDomain: true,
      success: function (Response) {
        window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
      },
      error: function (Response) {
        alert('erro');
        window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
      },
      data: JSON.stringify(values) // post data || get data
    });
  event.preventDefault();
};

captureDataAlterRole = function (event) {
  var values = {}
  values['usernameR'] = localStorage.getItem('username');
  values['tokenID'] = localStorage.getItem('token');
  values['role'] = localStorage.getItem('role');
  $.each($('form[name="Alterar role"]').serializeArray(), function (i, field) {
    values[field.name] = field.value;
  });
  console.log(JSON.stringify(values));
  $.ajax({
    type: "POST",
    url: "https://oofaround.appspot.com/rest/userinfo/alterotherrole",
    contentType: "application/json;charset=utf-8",
    dataType: 'json', // data type        
    crossDomain: true,
    success: function (Response) {
      window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
    },
    error: function (Response) {
      alert('erro');
      window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
    },
    data: JSON.stringify(values) // post data || get data
  });
event.preventDefault();
};

captureDataDeleteUser = function (event) {
  var values = {}
  values['usernameR'] = localStorage.getItem('username');
  values['tokenID'] = localStorage.getItem('token');
  values['role'] = localStorage.getItem('role');
  $.each($('form[name="Eliminar user"]').serializeArray(), function (i, field) {
    values[field.name] = field.value;
  });
  console.log(JSON.stringify(values));
  $.ajax({
    type: "POST",
    url: "https://oofaround.appspot.com/rest/delete/user",
    contentType: "application/json;charset=utf-8",
    dataType: 'json', // data type        
    crossDomain: true,
    success: function (Response) {
      window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
    },
    error: function (Response) {
      alert('erro');
      window.location.href = "https://oofaround.appspot.com/BO_homepage.html";
    },
    data: JSON.stringify(values) // post data || get data
  });
event.preventDefault();
};

function logout() {
  localStorage.clear();
  window.location.href = "https://oofaround.appspot.com/";
};

window.onload = function () {
  home();
  var date = new Date();
  var token = localStorage.getItem('expiration');
  var longday = date.getTime();
  if (longday > token) {
    localStorage.clear();
    window.location.href = "https://oofaround.appspot.com/";
  } else {
    localStorage.setItem('expiration', date.getTime() + 300000);
    var ponto = $('form[name="Criar ponto"]');
    ponto[0].onsubmit = captureDataCreatePointOfInterest;
    var cupao = $('form[name="Criar cupao"]');
    cupao[0].onsubmit = captureDataCreateCupon;
    var role = $('form[name="Alterar role"]');
    role[0].onsubmit = captureDataAlterRole;
    var del = $('form[name="Eliminar user"]');
    del[0].onsubmit = captureDataDeleteUser;
  }
};