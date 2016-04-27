!function a(b,c,d){function e(g,h){if(!c[g]){if(!b[g]){var i="function"==typeof require&&require;if(!h&&i)return i(g,!0);if(f)return f(g,!0);var j=new Error("Cannot find module '"+g+"'");throw j.code="MODULE_NOT_FOUND",j}var k=c[g]={exports:{}};b[g][0].call(k.exports,function(a){var c=b[g][1][a];return e(c?c:a)},k,k.exports,a,b,c,d)}return c[g].exports}for(var f="function"==typeof require&&require,g=0;g<d.length;g++)e(d[g]);return e}({1:[function(a,b,c){b.exports=function(a,b){return{request:function(b){var c=sessionStorage.getItem("medicloud_user_credentials");return c&&(b.headers.Authorization="Basic "+c),b||a.when(b)},responseError:function(b){return console.log("Found responseError: ",b),401==b.status&&(console.log("Access denied (error 401), please login again"),window.location.href="/sign-in/"),a.reject(b)}}}},{}],2:[function(a,b,c){!function(){var b=a("./patient/patient.module"),c=a("./patients/patients.module"),d=a("../Shared/authorization.interceptor"),e=a("./calendar/calendar.module"),f=a("./error/error.directive");b(),c(),e();var g=new angular.module("hp-center",["ngRoute","ngAnimate","hpPatient","hpPatientList","hpCalendar"]);g.config(["$routeProvider","$httpProvider",function(a,b){"use strict";a.when("/",{templateUrl:"dashboard/"}).otherwise({redirectTo:"/"}),b.interceptors.push(["$q","$location",d])}]),g.directive("mdErrorModal",f),angular.bootstrap(window.document,["hp-center"])}()},{"../Shared/authorization.interceptor":1,"./calendar/calendar.module":4,"./error/error.directive":8,"./patient/patient.module":23,"./patients/patients.module":26}],3:[function(a,b,c){b.exports=function(){return{link:function(a,b,c){function d(a){a.each(function(){var a={title:$.trim($(this).text())};$(this).data("eventObject",a),$(this).draggable({zIndex:1070,revert:!0,revertDuration:0})})}d($("#external-events div.external-event"));var e=new Date,f=e.getDate(),g=e.getMonth(),h=e.getFullYear();$("#calendar").fullCalendar({header:{left:"prev,next today",center:"title",right:"month,agendaWeek,agendaDay"},buttonText:{today:"today",month:"month",week:"week",day:"day"},events:[{title:"All Day Event",start:new Date(h,g,1),backgroundColor:"#f56954",borderColor:"#f56954"},{title:"Long Event",start:new Date(h,g,f-5),end:new Date(h,g,f-2),backgroundColor:"#f39c12",borderColor:"#f39c12"},{title:"Meeting",start:new Date(h,g,f,10,30),allDay:!1,backgroundColor:"#0073b7",borderColor:"#0073b7"},{title:"Lunch",start:new Date(h,g,f,12,0),end:new Date(h,g,f,14,0),allDay:!1,backgroundColor:"#00c0ef",borderColor:"#00c0ef"},{title:"Birthday Party",start:new Date(h,g,f+1,19,0),end:new Date(h,g,f+1,22,30),allDay:!1,backgroundColor:"#00a65a",borderColor:"#00a65a"},{title:"Click for Google",start:new Date(h,g,28),end:new Date(h,g,29),url:"http://google.com/",backgroundColor:"#3c8dbc",borderColor:"#3c8dbc"}],editable:!0,droppable:!0,drop:function(a,b){var c=$(this).data("eventObject"),d=$.extend({},c);d.start=a,d.allDay=b,d.backgroundColor=$(this).css("background-color"),d.borderColor=$(this).css("border-color"),$("#calendar").fullCalendar("renderEvent",d,!0),$("#drop-remove").is(":checked")&&$(this).remove()}});var i="#3c8dbc";$("#color-chooser-btn");$("#color-chooser > li > a").click(function(a){a.preventDefault(),i=$(this).css("color"),$("#add-new-event").css({"background-color":i,"border-color":i})}),$("#add-new-event").click(function(a){a.preventDefault();var b=$("#new-event").val();if(0!=b.length){var c=$("<div />");c.css({"background-color":i,"border-color":i,color:"#fff"}).addClass("external-event"),c.html(b),$("#external-events").prepend(c),d(c),$("#new-event").val("")}}),$("[data-date]").click(function(){a.modalControl.show=!a.modalControl.show,a.selectedDate=$(this).attr("data-date"),a.setSelectedDate(),a.$apply()})},controller:["$scope","$rootScope","calendarService",function(a,b,c){a.modalControl={show:!1},a.setSelectedDate=function(){c.selectedDate=a.selectedDate,b.$broadcast("dateSelected",{date:c.selectedDate})}}]}}},{}],4:[function(a,b,c){b.exports=function(){var b=a("./calendar.service.js"),c=a("../modalDialogue/modal.directive"),d=a("./calendar.js"),e=angular.module("hpCalendar",["ngRoute","hpPatientList"]);e.config(["$routeProvider",function(a){"use strict";a.when("/calendar/",{templateUrl:"calendar/calendar.html"})}]),e.service("calendarService",["$http","$rootScope","$resource",b]),e.directive("appointmentModalDirective",c),e.directive("calendarDirective",d)}},{"../modalDialogue/modal.directive":9,"./calendar.js":3,"./calendar.service.js":5}],5:[function(a,b,c){b.exports=function(a,b,c){var d,e,f,g,h=sessionStorage.getItem("medicloud_hp_id"),i="http://"+window.location.hostname+":8080/api/hp/:hpId/patients/:patientId",j="http://"+window.location.hostname+":8080/api/hp/:hpId/patients/availability?userDate=:selectedDate",k="http://"+window.location.hostname+":8080/api/hp/:hpId/patients/:patientId/appointment",l={times:[{appointmentTime:"9:00"},{appointmentTime:"10:00"}],getTimes:function(){g=this.selectedDate;var a=this,b=c(j,{hpId:h,selectedDate:g});console.log("service.getTimes() selectedDate is "+g);var f=b.query().$promise;return f.then(function(b){angular.extend(a.times,b),(d||angular.noop)(),d=null,e=null},function(a){(e||angular.noop)(a),d=null,e=null}),this.times},patients:[],getPatients:function(){var a=this,b=c(i,{hpId:h}),f=b.query().$promise;return f.then(function(b){angular.extend(a.patients,b),(d||angular.noop)(),d=null,e=null},function(a){(e||angular.noop)(a),d=null,e=null}),this.patients},addAppointment:function(a,d){f=d,console.log("In calendar service. patientId is "+d);var e=c(k,{hpId:h,patientId:d});e.save(a,function(a){a.personId?(b.$broadcast("patientAdded"),l.patients.push(a),$("#patientSuccessAlert").show(),setTimeout(function(){$("#patientSuccessAlert").fadeOut("slow")},3e3)):$("#patientFailureAlert").show(),console.log(a)})},onSuccess:function(a){return d=a,this},onFailure:function(a){return e=a,this}};return l}},{}],6:[function(a,b,c){b.exports=function(){var a=function(a,b){a.waiting=!0;var c=function(){a.suggestions=new Bloodhound({datumTokenizer:Bloodhound.tokenizers.obj.whitespace("name"),queryTokenizer:Bloodhound.tokenizers.whitespace,identify:function(a){return a.id},local:b.data.conditions}),setTimeout(function(){a.$broadcast("conditionSearch.suggestions.updated")},200),a.waiting=!1};b.ready?c():a.$on("infermedicaConditions_serv.data.updated",function(){c()})};return{templateUrl:"conditionSearch/conditionSearch.template.html",scope:{onSelect:"="},link:function(a,b,c){a.$on("conditionSearch.suggestions.updated",function(){b.find(".inputSearch").typeahead("destroy"),b.find(".inputSearch").typeahead({hint:!0,highlight:!0,minLength:1},{name:"conditions",source:a.suggestions,displayKey:"name",templates:{empty:['<div class="text-muted">',"No Suggestion","</div>"].join("\n"),suggestion:function(a){var b='<div class="list-group-item"><dl><dt>{{name}}<label class="label pull-right">{{severity}}</label></dt><dd><em>{{categories}}</em></dd></div>',c=b.replace(/{{name}}/g,a.name).replace(/{{severity}}/g,a.severity).replace(/{{categories}}/g,a.categories.join(", "));return c=$(c),"mild"==a.severity?c.find("label").toggleClass("label-info",!0):"moderate"==a.severity?c.find("label").toggleClass("label-warning",!0):"severe"==a.severity?c.find("label").toggleClass("label-danger",!0):c.find("label").toggleClass("label-default",!0),c}}}),b.find(".inputSearch").bind("typeahead:select",function(b,c){(a.onSelect||angular.noop)(c)})})},controller:["$scope","infermedicaConditions_serv",a]}}},{}],7:[function(a,b,c){b.exports=function(a,b){var c="http://"+window.location.hostname+"\\:8080/conditions",d=("http://"+window.location.hostname+"\\:8080/conditions/:condition_id",a(c,{condition_id:"@c_id"})),e={ready:!1,data:{conditions:[]},fetchAll:function(){var a=this,c=d.query({},function(){a.data.conditions.splice(0,a.data.conditions.length),angular.extend(a.data.conditions,c),b.$broadcast("infermedicaConditions_serv.data.updated"),a.ready=!0},function(a){b.$broadcast("infermedicaConditions_serv.data.error",a)});return d.$promise}};return e.fetchAll(),e}},{}],8:[function(a,b,c){b.exports=function(){return{scope:{error:"=",control:"="},templateUrl:"error/error.template.html",link:function(a,b,c){var d=$(b[0]).modal("hide");a.control={isShown:!1,show:function(b){a.error=b||a.error,d.modal("show"),d.on("modal.bs.shown",function(){a.isShown=!0})},hide:function(){d.modal("hide"),moda.on("modal.bs.hidden",function(){a.isShown=!1})}}}}}},{}],9:[function(a,b,c){b.exports=function(){return{restrict:"E",scope:{show:"=",submit:"="},replace:!0,transclude:!0,link:function(a,b,c){$(document).on("dblclick",function(){console.log("scope.show is "+a.show)}),a.$watch("show",function(a){a?$(b[0]).modal("show"):$(b[0]).modal("hide")})},templateUrl:"modalDialogue/modal.html",controller:["$scope","$rootScope","patientsListService","calendarService",function(a,b,c,d){var e,f={appointmentTime:"9:00",appointmentDate:"2000-01-01",reason:"N/A"};a.selectedPatient="Select a patient",a.selectedTime="Select a time",$('[data-toggle="tooltip"]').tooltip("disable"),b.$on("dateSelected",function(b,e){console.log("Broadcast received. Date is "+e.date),a.patientList=c.getPatients(),a.timesList=d.getTimes(),f.appointmentDate=e.date}),a.selectPatient=function(b){a.selectedPatient=b,e=a.selectedPatient.patientId},a.selectTime=function(b){a.selectedTime=b,f.appointmentTime=a.selectedTime},a.submitAppointment=function(){f.reason=a.reason,d.addAppointment(f,e),console.log("Appointment is "+f)}}]}}},{}],10:[function(a,b,c){b.exports=function(){return{templateUrl:"patient/conditions/activeCondition.template.html",scope:{activeCondition:"=",observationPicker:"="},link:function(a,b,c){},controller:["$scope","activeCondition_factory",function(a,b){a.pickStartObservation=function(){a.observationPicker.show(),a.observationPicker.onSelect=function(c){a.activeCondition.startObservation=c,a.activeCondition.startObsId=c.obsId,b.update({patientId:a.activeCondition.patientId,activeConditionId:a.activeCondition.activeConditionId},a.activeCondition)}},a.pickEndObservation=function(){a.observationPicker.show(),a.observationPicker.onSelect=function(c){a.activeCondition.endObservation=c,a.activeCondition.endObsId=c.obsId,b.update({patientId:a.activeCondition.patientId,activeConditionId:a.activeCondition.activeConditionId},a.activeCondition)}}}]}}},{}],11:[function(a,b,c){b.exports=function(a,b){var c=sessionStorage.getItem("medicloud_hp_id"),d="http://"+window.location.hostname+"\\:8080/api/hp/:hpId/patients/:patientId/conditions/:activeConditionId",e=a(d,{hpId:c,patientId:"@patientId",activeConditionId:"@activeConditionId"},{update:{method:"PUT"}});return e}},{}],12:[function(a,b,c){b.exports=function(a,b,c,d){a.patient=b.getPatient(d.patient_id),a.patient.fetchConditions(),a.newActiveConditionForms=[],a.observationPickerControl={},a.getNewActiveConditionForm=function(){a.newActiveConditionForms.unshift({})},a.removeForm=function(b){a.newActiveConditionForms.splice(b,1)}}},{}],13:[function(a,b,c){b.exports=function(){var a=function(a,b){a.mode="search",a.formatDate=function(a){return a?new Date(a):"none"},a.showSearch=function(){a.mode="search"},a.save=function(){a.patient.addActiveCondition(a.condition.name,a.condition.severity,a.condition.description,a.condition.inferCId),(a.onSaved||angular.noop)()},a.cancel=function(){(a.onCancelled||angular.noop)()},a.severityOptions=["mild","moderate","severe"],a.create=function(b){b&&(a.condition.name=b.name,a.condition.severity=b.severity,a.condition.inferCId=b.id,a.condition.description=""),a.mode="create",setTimeout(function(){a.$apply()},200)}};return{templateUrl:"patient/conditions/newActiveCondition.template.html",scope:{condition:"=condData",patient:"=",onSaved:"&",onCancelled:"&"},link:function(a,b,c){a.condition.name="",a.condition.severity="",a.condition.infer_c_id=null,a.condition.description="",a.confirm=function(){a.mode="confirm"}},controller:["$scope","models_service",a]}}},{}],14:[function(a,b,c){b.exports=function(){var a=function(a,b,c,d){a.patient=b.getPatient(d.patient_id),a.patient.fetchObservations()};return{templateUrl:"patient/conditions/observationPicker.template.html",scope:{control:"="},link:function(a,b,c){var d=$(b[0]).find(".modal");a.control.show=function(){return d.modal("show"),a.control},a.control.hide=function(){return d.modal("hide"),a.control},a.onSelect=angular.noop,a.selectObservation=function(b){return a.control.onSelect(b),a.control.onSelect=angular.noop,a.control.hide(),a.control}},controller:["$scope","models_service","$route","$routeParams",a]}}},{}],15:[function(a,b,c){b.exports=function(a,b){var c=(sessionStorage.getItem("medicloud_hp_id"),"http://"+window.location.hostname+"\\:8080/api/hp/:hpId/patients/:patientId/encounters"),d=a(c,{hpId:"@hpId",patientId:"@patientId"});return d}},{}],16:[function(a,b,c){b.exports=function(a,b,c,d){a.incConditionList="patient/conditions/activeConditionList.html",a.incObservationList="patient/observations/observations.html",a.incLabResults="patient/labs/labResults.html",a.patient=b.getPatient(d.patient_id),a.modalMessage={show:!1,title:"patient/message/formMessage.title.html",body:"patient/message/formMessage.html",footer:"patient/message/formMessage.footer.html",actions:{send:function(){console.log("Sent")}}}}},{}],17:[function(a,b,c){b.exports=function(a,b){var c={patients:[],patientIndex:{}},d={getPatient:function(d){if(c.patientIndex.hasOwnProperty(d)){var e=c.patientIndex[d];return c.patients[e]}var f=c.patients.length;return c.patientIndex[d]=f,c.patients.push(new b(d)),c.patients[f].onLoad(function(b){a.$broadcast("patients.updated",c.patients[f])}),c.patients[f].onFailure(function(a){c.patients[f]=null,delete c.patientIndex[d]}),c.patients[f]},getPatients:function(){return model.patients}};return d}},{}],18:[function(a,b,c){b.exports=function(a){var b=sessionStorage.getItem("medicloud_hp_id"),c="http://"+window.location.hostname+"\\:8080/api/hp/:hpId/patients/:patientId/observations",d=a(c,{hpId:b,patientId:"@patientId"},{save:{method:"POST"}});return d}},{}],19:[function(a,b,c){b.exports=function(){return{templateUrl:"patient/observations/addObservationForm.html",scope:{control:"="},link:function(a,b,c){var d=$(b[0]).find(".modal").modal("hide"),e=b.find("#btnPickEncounterDate");e.find("input").on("click",function(a){a.stopPropagation()}),a.control.show=function(){d.modal("show")},a.control.hide=function(){d.modal("hide")}},controller:["$scope","$filter","models_service","$route","$routeParams","observationSuggestions_factory",function(a,b,c,d,e,f){a.error="",a.waiting=!1,a.control={},a.form={encounterDate:"",encounterReason:"",state:"",comments:""},a.patient=c.getPatient(e.patient_id),a.setEncounterDate=function(c){var d;if("object"==typeof c&&null!=c)d=b("date")(c,"shortDate");else{if("number"!=typeof c)return;d=new Date,d.setDate(d.getDate()+c)}a.form.encounterDate=b("date")(d,"shortDate")},a.submitNewObservationForm=function(){a.waiting=!0,a.patient.addNewObservation(a.form).then(function(b){a.waiting=!0,a.control.hide()},function(a){g(a)})},a.queryObservationSuggestions=function(b){return a.obsSuggestions=f.query({keywords:b,gender:a.patient.gender||""}),a.obsSuggestions.$promise.then(function(){return a.obsSuggestions})},a.addToObservationComments=function(b){a.form.comments&&(a.form.comments+="\n"),a.form.comments+=b.label,a.selectedObsSuggestion=""};var g=function(b){a.error=b.error?b.error:b,a.error=angular.isArray(a.error)?a.error.join("\n"):a.error,a.waiting=!1}}]}}},{}],20:[function(a,b,c){b.exports=function(a){var b="http://"+window.location.hostname+"\\:8080/observations",c=a(b);return c}},{}],21:[function(a,b,c){b.exports=function(a,b,c,d){a.patient=b.getPatient(d.patient_id),a.waiting=!0,a.patient.fetchEncounters().then(function(){a.waiting=!1},function(a){e(a)}),a.openObservationForm=function(){a.observationForm.show()};var e=function(b){a.error=b.error?b.error:b,a.error=angular.isArray(a.error)?a.error.join("\n"):a.error,a.waiting=!1}}},{}],22:[function(a,b,c){b.exports=function(a,b,c,d,e){var f=sessionStorage.getItem("medicloud_hp_id"),g="http://"+window.location.hostname+"\\:8080/api/hp/:hpId/patients/:patientId",h=a(g,{hpId:f,patientId:"@patientId"}),i=g+"/conditions/:conditionId",j=a(i,{hpId:f,patientId:"@patientId",conditionId:"@conditionId"}),k=g+"/observations/:obsId",l=a(k,{hpId:f,patientId:"@patientId",obsId:"@obsId"}),m=function(a){this.patientId=a,this.onLoadCallback=angular.noop,this.onFailureCallback=angular.noop,this.conditions=[],this.observations=[],this.encounters=[],this.lastVisit=null,this.fetch()};return m.prototype={constructor:m,onLoad:function(a){this.onLoadCallback=a},onFailure:function(a){this.onFailureCallback=a},fetch:function(){var a=this,c=h.get({patientId:a.patientId},function(b,c){angular.extend(a,b),a.onLoadCallback.call(a,b)},function(c){b.$broadcast("error",c),a.onFailureCallback.call(a,c)});return c},fetchConditions:function(){var a=this;a.conditions.splice(0,a.conditions.length);var c=j.query({patientId:a.patientId},function(b){angular.copy(b,a.conditions)},function(a){b.$broadcast("error",a)}).$promise;return c},addActiveCondition:function(a,d,e,f){var g=this,h=c.save({name:a,severity:d,description:e,inferCId:f,patient:g,patientId:g.patientId});return h.$promise.then(function(a){g.conditions.unshift(h)},function(a){b.$broadcast("error",a)}),h.$promise},fetchObservations:function(){var a=this;a.observations.splice(0,a.observations.length);var c=l.query({patientId:a.patientId},function(b){angular.copy(b,a.observations)},function(a){b.$broadcast("error",a)});return c.$promise},addNewObservation:function(a){var c=this,e=d.save({hpId:f,patientId:c.patientId},a);return e.$promise.then(function(a){c.fetchObservations(),c.fetchEncounters()},function(a){b.$broadcast("error",a)}),e.$promise},fetchEncounters:function(){var a=this;a.encounters.splice(0,a.encounters.length);var c=e.query({hpId:f,patientId:a.patientId},function(b){angular.copy(b,a.encounters),a.encounters.length>0&&(a.lastVisit=a.encounters[0].encounterDateTime)},function(a){b.$broadcast("error",a)});return c.$promise}},m}},{}],23:[function(a,b,c){b.exports=function(){var b=a("../../Shared/authorization.interceptor"),c=a(".//main.controller"),d=a("./../share/modal.directive"),e=a("./models.service"),f=a("./patient.factory"),g=a("./profile/profile.controller"),h=a("./conditions/activeConditionList.controller"),i=a("./conditions/activeCondition.directive"),j=a("./conditions/newActiveCondition.directive"),k=a("./conditions/activeCondition.factory"),l=a("./../conditionSearch/conditionSearch.directive"),m=a("./../conditionSearch/infermedicaConditions.service"),n=a("./observations/observations.controller"),o=a("./observations/observation.factory"),p=a("./observations/observationForm.directive"),q=a("./encounters/encounterList.factory"),r=a("./conditions/observationPicker.directive"),s=a("./observations/observationSuggestions.factory"),t=angular.module("hpPatient",["ngRoute","ngResource","ui.bootstrap"]);t.config(["$routeProvider","$httpProvider",function(a,c){"use strict";a.when("/patient/:patient_id/:tab?",{templateUrl:"patient/",controller:"profile_ctrl"}),c.interceptors.push(["$q","$location",b])}]),t.service("models_service",["$rootScope","patient_factory",e]).service("infermedicaConditions_serv",["$resource","$rootScope",m]).service("patient_factory",["$resource","$rootScope","activeCondition_factory","observation_factory","encounterList_factory",f]),t.factory("activeCondition_factory",["$resource","$rootScope",k]).factory("observation_factory",["$resource",o]).factory("encounterList_factory",["$resource",q]).factory("observationSuggestions_factory",["$resource",s]),t.directive("mcConditionSearch",l).directive("mcNewActiveCondition",j).directive("mcActiveCondition",i).directive("mcObservationForm",p).directive("mcModal",d).directive("mcObservationPicker",r),t.controller("profile_ctrl",["$scope","models_service","$route","$routeParams",g]).controller("conditionList_ctrl",["$scope","models_service","$route","$routeParams",h]).controller("observations_ctrl",["$scope","models_service","$route","$routeParams",n]).controller("main_ctrl",["$scope","models_service","$route","$routeParams",c])}},{"../../Shared/authorization.interceptor":1,"./../conditionSearch/conditionSearch.directive":6,"./../conditionSearch/infermedicaConditions.service":7,"./../share/modal.directive":29,".//main.controller":16,"./conditions/activeCondition.directive":10,"./conditions/activeCondition.factory":11,"./conditions/activeConditionList.controller":12,"./conditions/newActiveCondition.directive":13,"./conditions/observationPicker.directive":14,"./encounters/encounterList.factory":15,"./models.service":17,"./observations/observation.factory":18,"./observations/observationForm.directive":19,"./observations/observationSuggestions.factory":20,"./observations/observations.controller":21,"./patient.factory":22,"./profile/profile.controller":24}],24:[function(a,b,c){b.exports=function(a,b,c,d){a.patient=b.getPatient(d.patient_id)}},{}],25:[function(a,b,c){b.exports=function(){return{restrict:"E",scope:{show:"=",submit:"="},replace:!0,transclude:!0,link:function(a,b,c){a.$watch("show",function(a){a?$(b[0]).modal("show"):$(b[0]).modal("hide")}),b.on("submit",function(){for(var c=b[0].querySelectorAll("input,select,textarea"),d=c.length;d--;)c[d].addEventListener("invalid",function(){this.scrollIntoView(!1)});var e=a.patient;e=angular.extend({},a.patient),e.birthdate=b.find("[type=date]").val(),a.submit(e)})},templateUrl:"patients/formAddPatient/formAddPatient.html",controller:["$scope",function(a){$('[data-toggle="tooltip"]').tooltip("disable"),a.patient={firstName:"",lastName:""}}]}}},{}],26:[function(a,b,c){b.exports=function(){var b=a("./patientsList/patientsList.controller"),c=a("./patientsList/patientsList.service"),d=a("./formAddPatient/formAddPatient.directive"),e=a("../../Shared/authorization.interceptor"),f=angular.module("hpPatientList",["ngRoute"]);f.config(["$routeProvider","$httpProvider",function(a,b){"use strict";a.when("/patients/",{templateUrl:"patients/",controller:"patientsList_ctrl"}),b.interceptors.push(["$q","$location",e])}]),f.service("patientsListService",["$http","$rootScope","$resource",c]),f.directive("modalDialog",d),f.controller("patientsList_ctrl",["$scope","$rootScope","patientsListService",b])}},{"../../Shared/authorization.interceptor":1,"./formAddPatient/formAddPatient.directive":25,"./patientsList/patientsList.controller":27,"./patientsList/patientsList.service":28}],27:[function(a,b,c){b.exports=function(a,b,c){function d(){a.waiting=!0,c.getPatients().onSuccess(function(b){a.waiting=!1,a.patientList=c.patients}).onFailure(function(b){a.waiting=!1,console.log(b)})}a.patientList=[],d(),a.status,a.modalShown=!1,a.patient={},a.toggleModal=function(){a.modalShown=!a.modalShown},a.contactClicked=!1,a.selectedPatient;$("#patientSuccessAlert").hide(),$("#patientFailureAlert").hide(),a.clicked=function(b){a.contactClicked=!0,a.selectedPatient=b},b.$on("patientAdded",function(){a.waiting=!1,$("#AddPatientForm")[0].reset()}),a.addPatient=function(b){a.waiting=!0,c.addPatient(b),a.modalShown=!1}}},{}],28:[function(a,b,c){b.exports=function(a,b,c){var d,e,f=sessionStorage.getItem("medicloud_hp_id"),g="http://"+window.location.hostname+":8080/api/hp/:hpId/patients/:patientId",h={patients:[{firstName:"Peter",lastName:"Parker",email:"peterparker@gmail.com"},{firstName:"Bruce",lastName:"Wayne",email:"brucewayne@gmail.com"}],getPatients:function(){var a=this,b=c(g,{hpId:f}),h=b.query().$promise;return h.then(function(b){a.patients=b,(d||angular.noop)(),d=null,e=null},function(a){(e||angular.noop)(a),d=null,e=null}),this},onSuccess:function(a){return d=a,this},onFailure:function(a){return e=a,this},addPatient:function(a){var d=c(g,{hpId:f});d.save(a,function(a){a.personId?(b.$broadcast("patientAdded"),h.patients.push(a),$("#patientSuccessAlert").show(),setTimeout(function(){$("#patientSuccessAlert").fadeOut("slow")},3e3)):$("#patientFailureAlert").show(),console.log(a)})}};return h}},{}],29:[function(a,b,c){b.exports=function(){return{templateUrl:"share/modal.template.html",scope:{show:"=show",title:"=mcTitle",body:"=mcBody",footer:"=mcFooter",actions:"=mcActions"},link:function(a,b,c){a.$watch("show",function(a,c){a?b.find(".modal").modal("show"):b.find(".modal").modal("hide")}),b.find(".modal").on("shown.bs.modal",function(){a.show=!0,a.$apply()}),b.find(".modal").on("hidden.bs.modal",function(){a.show=!1,a.$apply()})}}}},{}]},{},[2]);