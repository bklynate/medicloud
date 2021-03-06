(function() {
    // patients_module = require("./patients/patients.module");
    var patient_module = require("./patient/patient.module");
    var hpPatientList_module = require("./patients/patients.module");

    var auth = require("../Shared/authorization.interceptor");
    var hpCalendar_module = require('./calendar/calendar.module');
    var errorModal_directive = require('./error/error.directive');

    var main_controller = require('./main.controller');

    patient_module();
    hpPatientList_module();
    hpCalendar_module();
    var app = new angular.module("hp-center", ['ngRoute', 'ngResource', 'ngAnimate', 'hpPatient', 'hpPatientList', 'hpCalendar']);
    // routing and navigation configuration

    app.config(['$routeProvider', '$httpProvider',
        function($routeProvider, $httpProvider) {
            'use strict';
            $routeProvider

    .otherwise({
        redirectTo: '/patients/'
    });

    $httpProvider.interceptors.push(['$q', '$location', auth]);
        }
        ]);

    // directives
    app
        .directive('mdErrorModal', errorModal_directive);

    // services and factories

    // controllers
    app
        .controller('hpCenter_main_controller', ['$scope', '$resource', main_controller])
        ;

    // initiate
    angular.bootstrap(window.document, ['hp-center']);

})();
