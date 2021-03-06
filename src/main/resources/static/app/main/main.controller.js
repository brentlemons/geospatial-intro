'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

(function () {
  var MainController = function () {
    function MainController($http, airports) {
      _classCallCheck(this, MainController);

      this.$http = $http;
      this.airports = airports;
      this.apts = [];
    }

    _createClass(MainController, [{
      key: '$onInit',
      value: function $onInit() {
        var sources = {
          airports: new mapboxgl.GeoJSONSource({
            data: { type: 'FeatureCollection', features: [] }
          })
        };

        mapboxgl.accessToken = 'pk.eyJ1IjoiYnJlbnRsZW1vbnMiLCJhIjoiOWxVeThWbyJ9.iGiQ6rMvAjDXVpyzjRabSg';
        var map = new mapboxgl.Map({
          container: 'map',
          style: 'mapbox://styles/brentlemons/ciqd5imkt0001d1ngbqqzf1bh',
          center: [-98.58333333, 39.83333333],
          zoom: 3
        });

        map.on('style.load', function () {
          map.addSource('airports', sources.airports);
          map.addLayer({
            'id': 'airports',
            'type': 'circle',
            'source': 'airports',
            'interactive': true,
          });
        });

        // When a click event occurs near a marker icon, open a popup at the location of
        // the feature, with description HTML from its properties.
        map.on('click', function (e) {
        	var features = map.queryRenderedFeatures(e.point, { layers: ['airports'] });

    	    if (!features.length) {
    	        return;
    	    }
        	
            var facility = features[0];

            console.log(facility.properties);
        });

        this.airports.list().then(function (airports) {
          sources.airports.setData(airports.data);
        }, function (error) {
          console.log('airports error');
        });
      }
    }]);

    return MainController;
  }();

  angular.module('mapboxAviationDemoApp').component('main', {
    templateUrl: 'app/main/main.html',
    controller: MainController
  });
})();
//# sourceMappingURL=main.controller.js.map
