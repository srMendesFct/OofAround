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
        alert('Ponto criado');
        //localStorage.setItem('image', send);
        //window.location.href = "https://oofaround.appspot.com/profile.html";
      },
      error: function (Response) {
        alert('Falha ao criar ponto');
        //window.location.href = "https://oofaround.appspot.com/profile.html";
      },
      data: JSON.stringify(values) // post data || get data
    });
  }
  event.preventDefault();
};

window.onload = function () {
  openCity();
  var date = new Date();
  var token = localStorage.getItem('expiration');
  var longday = date.getTime();
  if (longday > token) {
    localStorage.clear();
    window.location.href = "https://oofaround.appspot.com/";
  } 
  else {
    localStorage.setItem('expiration', date.getTime() + 300000);
    var frmsl = $('form[name="Criar ponto"]');
    frmsl[0].onsubmit = captureDataCreatePointOfInterest;
  }
}