var map;
var directionsService, directionsDisplay;
var geocoder;
var presetMarkers = [];
var routePoints = [];
var waypts = [];
var locationNames = [];
var flag = false;

//qd for necessario criar marker pelo nome
function codeAddress(addr) {
  geocoder.geocode({
    address: addr
  }, function (results, status) {
    if (status == 'OK') {
      map.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
        position: results[0].geometry.location,
        map: map
      });
    } else {
      alert('Error');
    }
  });
}

function initMap() {
  directionsService = new google.maps.DirectionsService;
  directionsDisplay = new google.maps.DirectionsRenderer;
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 9,
    center: {
      lat: 38.71667,
      lng: -9.13333
    }
  });

  geocoder = new google.maps.Geocoder();

  google.maps.event.addListener(map, 'click', function (event) {
    var marker = new google.maps.Marker({
      position: event.latLng,
      map: map,
      icon: 'https://maps.google.com/mapfiles/ms/icons/green-dot.png'
    });
    routePoints.push(marker);

    var newLoc = {
      name: "",
      category: "undefined",
      placeId: "",
      region: "Portugal",
      latitude: event.latLng.lat(),
      longitude: event.latLng.lng()
    }
    locationNames.push(newLoc);
  });

  directionsDisplay.setMap(map);

  document.getElementById('submitF').addEventListener('click', function () {
    createWaypoints();
    calculateAndDisplayRoute(directionsService, directionsDisplay);
  });
}

function createWaypoints() {
  for (var i = 1; i < routePoints.length - 1; i++) {
    waypts.push({
      location: routePoints[i].position,
      stopover: true
    });
  }
}

function calculateAndDisplayRoute(directionsService, directionsDisplay) {
  directionsService.route({
    origin: routePoints[0].position,
    waypoints: waypts,
    destination: routePoints[routePoints.length - 1].position,
    travelMode: 'WALKING'
  }, function (response, status) {
    if (status === 'OK') {
      directionsDisplay.setDirections(response);
    } else {
      window.alert('Directions request failed due to ' + status);
    }
  });
}

captureDataCreateCourse = function () {
  var values = {};
  values['tokenID'] = localStorage.getItem('token');
  values['usernameR'] = localStorage.getItem('username');
  values['role'] = localStorage.getItem('role');
  values['creatorUsername'] = localStorage.getItem('username');
  values['locationNames'] = locationNames;
  $.each($('form[name="courseForm"]').serializeArray(), function (i, field) {
    values[field.name] = field.value;
  });
  $.ajax({
    type: "POST",
    url: "https://oofaround.appspot.com/rest/route/create",
    contentType: "application/json;charset=utf-8",
    dataType: 'json',
    crossDomain: 'true',
    success: function (response) {},
    error: function (response) {
      alert(locationNames)
    },
    data: JSON.stringify(values)
  });
}

captureDataMonuments = function (event) {
  var limit = 5;
  var lastName = "";
  var x = document.getElementsByClassName("ed");
  var res = []
  var index = 0;
  for (var i = 0; i < x.length; i++) {
    if (x[i].checked) {
      res[index] = x[i].value;
      index++;
    }
  }
  var values = {
    tokenID: localStorage.getItem('token'),
    role: localStorage.getItem('role'),
    usernameR: localStorage.getItem('username'),
    limit: limit,
    lastName: lastName,
    region: document.getElementById("distrito").value,
    categoriesGet: res
  };
  $.ajax({
    type: "POST",
    url: "https://oofaround.appspot.com/rest/location/getcategoryregion",
    contentType: "application/json;charset=utf-8",
    dataType: 'json',
    crossDomain: 'true',
    success: function (response) {

      setMapOnAll(null);
      presetMarkers = [];

      console.log(presetMarkers);

      for (i = 0; i < response.locations.length; i++) {
        var pos = new google.maps.LatLng(response.locations[i].latitude, response.locations[i].longitude);
        var marker = new google.maps.Marker({
          position: pos,
          map: map
        });

        presetMarkers.push(marker);
        setInfo(i, response.locations[i].name, response.locations[i].address, response.locations[i].latitude, response.locations[i].longitude, response.locations[i].region, response.locations[i].category, response.locations[i].placeID);
      }
    },
    error: function (response) {},
    data: JSON.stringify(values)
  });
  event.preventDefault();
}

function setInfo(markerNumber, name, address, latitude, longitude, region, category, placeId) {
  var m = presetMarkers[markerNumber];

  //fazer isto mais bonito
  var contentString = '<div id="content">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h2 id="firstHeading" class="firstHeading"><b>' + name + '</b></h2>' +
    '<div id="bodyContent">' +
    '<p>Morada: ' + address + '</p>' +
    '<p>Coordenadas: ' + latitude + ' , ' + longitude + '</p>' +
    '<p align = "right"> <button onclick="addPreset(\'' + name + '\',\'' + address + '\',\'' + latitude + '\',\'' + longitude + '\',\'' + region + '\',\'' + category + '\',\'' + placeId + '\')">Adicionar ao percurso</button></p>' +
    '</div>' +
    '</div>';

  m.addListener('click', function () {
    var infowindow = new google.maps.InfoWindow({
      content: contentString
    });
    infowindow.open(map, m);
  });
}

function addPreset(name, address, latitude, longitude, region, category, placeId) {
  var posi = new google.maps.LatLng(latitude, longitude);
  var marker = new google.maps.Marker({
    position: posi,
    map: map,
    icon: 'https://maps.google.com/mapfiles/ms/icons/green-dot.png'
  });

  flag = false;

  for (j = 0; j < routePoints.length; j++) {
    console.log(routePoints[j]);
    console.log(marker);
    if (routePoints[j] == marker) {
      flag = true;
    }
  }

  if (flag == false) {
    routePoints.push(marker);
    var newLoc = {
      name: name,
      category: category,
      placeId: placeId,
      region: region,
      latitude: latitude,
      longitude: longitude
    }
    locationNames.push(newLoc);
  }
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
    var form_c = $('form[name="courseForm"]');
    form_c[0].onsubmit = captureDataCreateCourse;
    var pesquisa = $('form[name="categorias"]');
    pesquisa[0].onsubmit = captureDataMonuments;;
  }
};