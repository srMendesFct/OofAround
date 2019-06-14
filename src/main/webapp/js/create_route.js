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
            var m = document.getElementById('map');
            for(var i = 0; i < response.locations.length; i++) {
                console.log(response.locations[i].name);
                var pos = new google.maps.LatLng(response.locations[i].latitude, response.locations[i].longitude);
                var marker = new google.maps.Marker({
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
    captureDataMonuments();
}