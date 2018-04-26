/**
 * ITLogs Angular JS login controller.
 * 
 * @desc login controller function.
 * @version 1.0.0
 * 
 * -----------------------------
 * 
 * History: Date(YYYY.MM.DD)|author|revision
 * 
 * 2017.04.21 | m.cahinod@ph.fujitsu.com | initial version.
 * 
 * 
 * ------------------------------------------------------------------------------
 * Copyright (C) 2017 FUJITSU All rights reserved.
 * ------------------------------------------------------------------------------
 */
app.controller("loginController", ['$scope', '$mdDialog', function($scope, $mdDialog){

    $scope.showWait = function(){
        $mdDialog.show({
          template: '<md-dialog style="background-color:transparent;box-shadow:none;height:200px;">' +
                      '<div layout="row" layout-sm="column" layout-align="center center" aria-label="wait">' +
                          '<md-progress-circular md-mode="indeterminate" class="md-warn md-hue-1" md-diameter="100"></md-progress-circular>' +
                      '</div>' +
                   '</md-dialog>',
          parent: angular.element(document.body),
          clickOutsideToClose:false,
          fullscreen: false
        })
        .then(function(answer) {

        });
 	};
                    
}]);
