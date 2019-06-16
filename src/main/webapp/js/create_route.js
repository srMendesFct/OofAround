var map;
var directionsService, directionsDisplay;
var geocoder;
var allMarkers = [];

//qd for necessario criar marker pelo nome
function codeAddress(addr) {
    geocoder.geocode({ address: addr}, function(results, status) {
        if(status == 'OK') {
            map.setCenter(results[0].geometry.location);
            var marker = new google.maps.Marker({ position: results[0].geometry.location, map: map});
        }
        else {
          alert('Error');
        }
    });
}

function initMap() {
    directionsService = new google.maps.DirectionsService;
    directionsDisplay = new google.maps.DirectionsRenderer;
    map = new google.maps.Map(document.getElementById('map'), {
      zoom: 7,
      center: {lat: 38.71667, lng: -9.13333}
    });
    
    //geocoder = new google.maps.Geocoder();

    google.maps.event.addListener(map, 'click', function(event) {
      var marker = new google.maps.Marker({
        position: event.latLng, 
        map: map,
        icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
       });
    });

    //directionsDisplay.setMap(map);

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
            for(i = 0; i < response.locations.length; i++) {
                var pos = new google.maps.LatLng(response.locations[i].latitude, response.locations[i].longitude);

                var marker = new google.maps.Marker({
                   position: pos, 
                   map: map
                  });

                  allMarkers.push(marker);
                  setInfo(i, response.locations[i].name, response.locations[i].address, response.locations[i].latitude, response.locations[i].longitude);
            }
        },
        error: function (response) {},
        data: JSON.stringify(values)
    });
}

function setInfo(markerNumber, name, address, latitude, longitude) {
  var m = allMarkers[markerNumber];
  var contentString = '<div id="content">'+
                '<div id="siteNotice">'+
                '</div>'+
                '<h1 id="firstHeading" class="firstHeading"><b>' + name + '</b></h1>'+
                '<div id="bodyContent">'+
                '<p>Endereço: ' + address + '</p>'+
                '<p>Coordenadas:' + latitude +  ' , ' + longitude + '</p>'+
                '</div>'+
                '</div>';

                m.addListener('click', function() {
                  var infowindow = new google.maps.InfoWindow({
                    content: contentString
                  });
                  infowindow.open(map, m);
                });

}

window.onload = function() {
  captureDataMonuments();
}