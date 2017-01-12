'use strict';

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var NavbarController = function NavbarController() {
  _classCallCheck(this, NavbarController);
};

//end-non-standard

angular.module('mapboxAviationDemoApp').controller('NavbarController', function ($scope, $timeout, $q, $log) {
  var self = this;
  self.simulateQuery = false;
  self.isDisabled = false;
  self.repos = loadAll();
  self.querySearch = querySearch;
  self.selectedItemChange = selectedItemChange;
  self.searchTextChange = searchTextChange;

  $scope.activeMenu = '';
  $scope.menu = [{
    'title': 'Mapping',
    'id': 'mapping',
    'active': false,
    'icon': ['fa-map-o'],
    'toggle': function toggle() {
      console.log(this);
      $scope.toggleActive(this);
    }
  }];

  $scope.filters = [{
    'name': 'ownership_type',
    'label': 'Ownership Type',
    'field': 'ownership.ownership_type_filter',
    'position': 1,
    'strategy': 'or',
    'terms': []
  }, {
    'name': 'fuel_type',
    'label': 'Fuel Types',
    'field': 'services.fuel_types_available_filter',
    'position': 4,
    'strategy': 'and',
    'terms': []
  }, {
    'name': 'type',
    'label': 'Facility Type',
    'field': 'type',
    'position': 5,
    'strategy': 'or',
    'terms': []
  }, {
    'name': 'runway_length',
    'label': 'Runway Length',
    'field': 'runways.length',
    'position': 2,
    'strategy': 'and',
    'range': {
      'floor': 0,
      'min': 8000,
      'max': 14000,
      'ceil': 15000,
      'step': 100
    }
  }, {
    'name': 'runway_width',
    'label': 'Runway Width',
    'field': 'runways.width',
    'position': 3,
    'strategy': 'and',
    'range': {
      'floor': 0,
      'min': 50,
      'max': 200,
      'ceil': 500,
      'step': 10
    }
  }];

  $scope.mine = 'fa-bell';
  //$scope.

  $scope.isCollapsed = false;

  $scope.$on('cache.filters.returned', function (e, kvp) {
    $scope.filters = kvp.value;
  });

  $scope.$on('cache.model.returned', function (e, kvp) {
    $scope.model = kvp.value;
  });

  $scope.selectedItems = function () {
    airports.setFilters($scope.filters);
  };

  $scope.selectedModel = function (model, level) {
    models.setModel(model, level);
  };

  $scope.toggleActive = function (item) {
    $scope.activeMenu = '';
    var _iteratorNormalCompletion = true;
    var _didIteratorError = false;
    var _iteratorError = undefined;

    try {
      for (var _iterator = $scope.menu[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
        var menu = _step.value;

        if (menu.title !== item.title) {
          menu.active = false;
        } else {
          menu.active = !menu.active;
          if (menu.active) {
            $scope.activeMenu = menu.id;
          }
        }
      }
    } catch (err) {
      _didIteratorError = true;
      _iteratorError = err;
    } finally {
      try {
        if (!_iteratorNormalCompletion && _iterator.return) {
          _iterator.return();
        }
      } finally {
        if (_didIteratorError) {
          throw _iteratorError;
        }
      }
    }

    console.log('--> ' + item);
  };

  $scope.isActive = function (route) {
    return route === $location.path();
  };

  function querySearch(query) {
    var results = query ? self.repos.filter(createFilterFor(query)) : self.repos,
        deferred;
    if (self.simulateQuery) {
      deferred = $q.defer();
      $timeout(function () {
        deferred.resolve(results);
      }, Math.random() * 1000, false);
      return deferred.promise;
    } else {
      return results;
    }
  }

  function searchTextChange(text) {
    $log.info('Text changed to ' + text);
  }

  function selectedItemChange(item) {
    $log.info('Item changed to ' + JSON.stringify(item));
  }

  /**
   * Build `components` list of key/value pairs
   */
  function loadAll() {
    var repos = [{
      'name': 'Angular 1',
      'url': 'https://github.com/angular/angular.js',
      'watchers': '3,623',
      'forks': '16,175'
    }, {
      'name': 'Angular 2',
      'url': 'https://github.com/angular/angular',
      'watchers': '469',
      'forks': '760'
    }, {
      'name': 'Angular Material',
      'url': 'https://github.com/angular/material',
      'watchers': '727',
      'forks': '1,241'
    }, {
      'name': 'Bower Material',
      'url': 'https://github.com/angular/bower-material',
      'watchers': '42',
      'forks': '84'
    }, {
      'name': 'Material Start',
      'url': 'https://github.com/angular/material-start',
      'watchers': '81',
      'forks': '303'
    }];
    return repos.map(function (repo) {
      repo.value = repo.name.toLowerCase();
      return repo;
    });
  }
  /**
   * Create filter function for a query string
   */
  function createFilterFor(query) {
    var lowercaseQuery = angular.lowercase(query);
    return function filterFn(item) {
      return item.value.indexOf(lowercaseQuery) === 0;
    };
  }
});
//# sourceMappingURL=navbar.controller.js.map
