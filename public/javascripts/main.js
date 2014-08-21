var app = angular.module("app", ["ngResource", "ngRoute"])
    .constant("apiUrl", "http://localhost:9000/api")
    .config(["$routeProvider", function($routeProvider) {
        return $routeProvider.when("/", {
            templateUrl: "/views/main",
            controller: "ListCtrl"
        }).when("/create", {
                templateUrl: "/views/detail",
                controller: "CreateCtrl"
            }).when("/edit/:id", {
                templateUrl: "/views/detail",
                controller: "EditCtrl"
            }).otherwise({
                redirectTo: "/"
            });
    }
    ]).config([
        "$locationProvider", function($locationProvider) {
            return $locationProvider.html5Mode(true).hashPrefix("!");
        }
    ]);

// the global controller
app.controller("AppCtrl", ["$scope", "$location", function($scope, $location) {
    $scope.go = function (path) {
        $location.path(path);
    };
}]);

// the list controller
app.controller("ListCtrl", ["$scope", "$resource", "apiUrl", function($scope, $resource, apiUrl) {
    var Users = $resource(apiUrl + "/users");
    $scope.users = Users.query();
}]);

// the create controller
app.controller("CreateCtrl", ["$scope", "$resource", "$timeout", "apiUrl", function($scope, $resource, $timeout, apiUrl) {
    $scope.save = function() {
        var CreateUser = $resource(apiUrl + "/users/new");
        CreateUser.save($scope.user);
        $timeout(function() { $scope.go('/'); });
    };
}]);

// the edit controller
app.controller("EditCtrl", ["$scope", "$resource", "$routeParams", "$timeout", "apiUrl", function($scope, $resource, $routeParams, $timeout, apiUrl) {
    var ShowUser = $resource(apiUrl + "/users/:id", {id:"@id"});
    if ($routeParams.id) {
        $scope.user = ShowUser.get({id: $routeParams.id});
        $scope.dbContent = ShowUser.get({id: $routeParams.id});
    }

    $scope.noChange = function() {
        return angular.equals($scope.user, $scope.dbContent);
    };

    // to update a user
    $scope.save = function() {
        var UpdateUser = $resource(apiUrl + "/users/" + $routeParams.id);
        UpdateUser.save($scope.user);
        $timeout(function() { $scope.go('/'); });
    };

    // to delete a user
    $scope.delete = function() {
        var DeleteUser = $resource(apiUrl + "/users/" + $routeParams.id);
        DeleteUser.delete();
        $timeout(function() { $scope.go('/'); });
    };
}]);