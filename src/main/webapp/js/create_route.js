var map;
var directionsService;
var directionsDisplay;
var marker;
var pos;

function initMap() {
    directionsService = new google.maps.DirectionsService;
    directionsDisplay = new google.maps.DirectionsRenderer;
    map = new google.maps.Map(document.getElementById('map'), {
      zoom: 7,
      center: {lat: 38.71667, lng: -9.13333}
    });
    directionsDisplay.setMap(map);

   // var onChangeHandler = function() {
    //  calculateAndDisplayRoute(directionsService, directionsDisplay);
    //};
    //document.getElementById('start').addEventListener('change', onChangeHandler);
    //document.getElementById('end').addEventListener('change', onChangeHandler);
  }

  function calculateAndDisplayRoute(directionsService, directionsDisplay) {
    directionsService.route({
      origin: document.getElementById('start').value,
      waypoints: [
      {
      location: 'gallup, nm',
      stopover: false
      },
      {
      location: 'barstow, ca',
       stopover: true
      }],
      destination: document.getElementById('end').value,
      travelMode: 'DRIVING'
    }, function(response, status) {
      if (status === 'OK') {
        directionsDisplay.setDirections(response);
      } else {
        window.alert('Directions request failed due to ' + status);
      }
    });
  }

captureDataMonuments = function() {
    var values = { 
        tokenID: localStorage.getItem('token'),
        usernameR: localStorage.getItem('username'),
        role: localStorage.getItem('role'),
        limit: "",
        lastName: "",
        category: "",
        region: ""
    }
    
    $.ajax({
        type: "POST",
        url: "https://oofaround.appspot.com/rest/location/getcategoryregion",
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
        crossDomain: 'true',
        success: function(response) {
            for(var i = 0; i < response.locations.length; i++) {
                console.log(response.locations[i].name);
                pos = new google.maps.LatLng(response.locations[i].latitude, response.locations[i].longitude);
                console.log(response.locations[i].latitude);
                marker = new google.maps.Marker({
                    position: pos,
                    map: map
                });

            }
        },
        error: function (response) {},
        data: JSON.stringify(values)
    });
}

window.onload = function () {
    initMap();
    captureDataMonuments();
}