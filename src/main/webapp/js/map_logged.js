var map, infoWindow;

function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: {
      lat: -34.397,
      lng: 150.644
    },
    zoom: 6
  });
  infoWindow = new google.maps.InfoWindow;

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function (position) {
      var pos = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };

      infoWindow.setPosition(pos);
      infoWindow.setContent('Você encontra-se aqui.');
      infoWindow.open(map);
      map.setCenter(pos);
      map.setContent
    }, function () {
      handleLocationError(true, infoWindow, map.getCenter());
    });
  } else {
    // Browser doesn't support Geolocation
    handleLocationError(false, infoWindow, map.getCenter());
  }
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
  infoWindow.setPosition(pos);
  infoWindow.setContent(browserHasGeolocation ?
    'Error: The Geolocation service failed.' :
    'Error: Your browser doesn\'t support geolocation.');
  infoWindow.open(map);
}

var user = localStorage.getItem('username');
var image = localStorage.getItem('image');
window.onload = function initMap() {
    document.getElementById("user").innerHTML = user;
    document.getElementById("profilePic").src = 'data:image/jpeg;base64, ' + image;
    var token = localStorage.getItem('expiration');
    var date = new Date();
    var longday = date.getTime();
    if (longday > token) {
        localStorage.clear();
        window.location.href = "https://oofaround.appspot.com/";
    } else {
        setupCallback();
    }
}

setupCallback = function () {
    document.getElementById("logout").addEventListener("click", function () {
        localStorage.clear();
        alert("Sessão terminada.")
        window.location.href = "https://oofaround.appspot.com/";
    });
}