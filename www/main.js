var myModule = angular.module('myModule', []);

function mainController($scope, $http) {
    $scope.formData = {};
    $http.get('http://147.83.7.200:8080/pandora-api/subjects/')
        .success(function(data){
           $scope.subjects=data;
            console.log(data);
        })
        .error(function(data) {
            console.log('Error:' + data);
        });

}