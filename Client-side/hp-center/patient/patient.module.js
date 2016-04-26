module.exports = function() {
	var auth = require("../../Shared/authorization.interceptor");

	var main_ctrl = require(".//main.controller");
	var modal_dir = require("./../share/modal.directive");
	var models_service = require("./models.service");

	var patient_factory = require("./patient.factory");
	var profile_ctrl = require("./profile/profile.controller");
	var conditionList_ctrl = require("./conditions/activeConditionList.controller");
	var activeCondition_dir = require("./conditions/activeCondition.directive");
	var newActiveCondition_dir = require("./conditions/newActiveCondition.directive");
	var activeCondition_factory = require("./conditions/activeCondition.factory");
	var conditionSearch_dir = require("./../conditionSearch/conditionSearch.directive");
	var infermedicaConditions_serv = require("./../conditionSearch/infermedicaConditions.service");
	var observations_ctrl = require("./observations/observations.controller");
	var observation_factory = require("./observations/observation.factory");
	var observationForm_directive = require("./observations/observationForm.directive");
	var encounterList_factory = require("./encounters/encounterList.factory");
        var observationPicker_directive = require("./conditions/observationPicker.directive");

        // initialize angular module 
	var app = angular.module('hpPatient', ['ngRoute', 'ngResource']);
	app.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
		'use strict';
		$routeProvider
			.when('/patient/:patient_id/:tab?', {
				templateUrl: 'patient/',
				controller: 'profile_ctrl'
			});

		$httpProvider.interceptors.push(['$q', '$location', auth]);
	}]);

	// services
	app
		.service('models_service', ['$rootScope', 'patient_factory', models_service])
		.service('infermedicaConditions_serv', 
			 ['$resource', '$rootScope', infermedicaConditions_serv])
		.service('patient_factory', 
			['$resource', '$rootScope', 'activeCondition_factory', 'observation_factory', 'encounterList_factory'
			, patient_factory])
		.factory('activeCondition_factory', ['$resource', '$rootScope', activeCondition_factory])
		.factory('observation_factory', ['$resource', observation_factory])
		.factory('encounterList_factory', ['$resource', encounterList_factory])
	;

	// directives
	app
		.directive('mcConditionSearch', conditionSearch_dir)
		.directive('mcNewActiveCondition', newActiveCondition_dir)
		.directive('mcActiveCondition', activeCondition_dir)
		.directive('mcObservationForm', observationForm_directive)
		.directive('mcModal', modal_dir)
                .directive('mcObservationPicker', observationPicker_directive)
	;

	// controllers
	app
		.controller("profile_ctrl", ['$scope', 'models_service', '$route', '$routeParams', profile_ctrl])
		.controller("conditionList_ctrl", ['$scope', 'models_service', '$route', '$routeParams', conditionList_ctrl])
		.controller("observations_ctrl", ['$scope', 'models_service', '$route', '$routeParams', observations_ctrl])
		.controller("main_ctrl", ['$scope', 'models_service', '$route', '$routeParams', main_ctrl])
	;
}
