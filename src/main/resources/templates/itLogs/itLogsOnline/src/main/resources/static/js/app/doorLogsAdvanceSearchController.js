/**
 * ITLogs Angular JS doorLogsAdvanceSearchController controller.
 * 
 * @desc doorLogsAdvanceSearchController controller function.
 * @version 1.0.0
 * 
 * -----------------------------
 * 
 * History: Date(YYYY.MM.DD)|author|revision
 * 
 * 2017.03.07 | r.monte@ph.fujitsu.com | initial version.
 * 2017.03.13 | y.umayam@ph.fujitsu.com | added search functionality.
 * 
 * 
 * ------------------------------------------------------------------------------
 * Copyright (C) 2017 FUJITSU All rights reserved.
 * ------------------------------------------------------------------------------
 */

app.controller("doorLogsAdvanceSearchController", ['$scope', '$mdDialog',
  '$location', '$anchorScroll', '$timeout', 'WebAPIDoorLogs', '$filter', 'Blob', 'FileSaver',
  function($scope, $mdDialog, $location, $anchorScroll, 
      $timeout, WebAPIDoorLogs, $filter, Blob, FileSaver) { 

    // Form Initialization
//	$scope.employeeName = "Gonzales, Arman";
	$scope.searchCategory = "Late Entries";
//	$scope.employeeNo = "000004";
	$scope.isWeekendsIncluded = false;
	
	$scope.hasInfo = false;
	$scope.hasError = false;
	$scope.dataLate = false;
	$scope.dataNo = false;
	$scope.errorMessages = [];
	
	$scope.search = function(employeeID, fromDate, fromTime, toDate, isWeekendsIncluded) {
		
		try {			

		if ((fromDate === undefined) || (toDate === undefined) || (fromTime === undefined)
				|| angular.equals(fromDate, "") || angular.equals(toDate, "") || angular.equals(fromTime, "")) {
			
			$scope.errorMessages = [];			
			var errorMsg = { "errorMessage":"Date/Time field is required." };
			
		      $scope.errorMessages.push(errorMsg);
		      
		      $scope.hasInfo = false;
		      $scope.hasError = true;
		      $scope.dataLate = false;
			  $scope.dataNo = false;
		      
		      return;
		}
			
			
		// For memory of the search action
		$scope.expName = $scope.employeeName;
		$scope.expFromTime = fromTime;
		
		console.log("Search -->" + "employeeID:" + employeeID + ",fromDate:" + fromDate + 
					",fromTime:" + fromTime + ",toDate:" + toDate + ",isWeekendsIncluded:" + isWeekendsIncluded);
		if(angular.equals($scope.searchCategory, "Late Entries")) {
			console.log("Searching Late Entries");
			
			$scope.showWait();
			
			WebAPIDoorLogs.getLateEntriesByEmpoyeeID(employeeID, fromDate, fromTime, toDate)
				.then(function(response) {
					
					$scope.redirectRequestIfSessionTimeout(response.headers('Content-Type'));					
					
					console.log(response.data);
					
					if(response.data.length <= 0) {
						$scope.infoMessage = "No Record Found."
						$scope.hasInfo = true;
						$scope.hasError = false;
						$scope.dataLate = false;
						$scope.dataNo = false;
					} else {
						$.each(response.data, function(key, value) {
							console.log('key:' + key + ' value:' + JSON.stringify(value));
						});
						
						$scope.lateEntries = [];
						var count = 0;
						for(var i=0; i<response.data.length; i++) {
							for(var j=0; j<response.data[i].doorLogEntriesDetails.length; j++) {
								
								var monthYear = null;
								var daysOfLates = null;
								var totalMinutesOfLates = null;
								var dayofTheWeek = null;
								var daysWithLateEntries = null;
								var includeDate = true;
								var daysWithLateEntriesDetail = null;
								if(j==0) {
									count++;
									monthYear = response.data[i].monthYear;
									daysOfLates = response.data[i].daysCount;
									totalMinutesOfLates = response.data[i].totalMinutes;
									
								} else {
									monthYear = null;
									daysOfLates = null;
									totalMinutesOfLates = null;
									
								}
								
								dayofTheWeek = response.data[i].doorLogEntriesDetails[j].dayofTheWeek;
								daysWithLateEntries = Object.keys(response.data[i].doorLogEntriesDetails[j].daysWithLateEntries)[j];
								daysWithLateEntriesDetail = response.data[i].doorLogEntriesDetails[j].daysWithLateEntries[daysWithLateEntries];
								
								var tempObj = {
										"count":count,
										"monthYear":monthYear,
										"daysOfLates":daysOfLates,
										"totalMinutesOfLates":totalMinutesOfLates,
										"dayofTheWeek":dayofTheWeek,
										"includeDate":includeDate,
										"daysWithLateEntries":daysWithLateEntries,
										"daysWithLateEntriesDetail":daysWithLateEntriesDetail
										};
								$scope.lateEntries.push(tempObj);
							}
						}
						
						for(var i=0; i<$scope.lateEntries.length; i++) {
							var data = "data_" + i;
							$scope[data] = false;
						}
					
						$scope.hasInfo = false;
						$scope.dataLate = true;
						$scope.dataNo = false;
					}
					
					$scope.hasError = false;
					$mdDialog.hide();
					
				}, function(response) {
		            var errorMsg = '';
		            console.log(response.data);
		            $scope.errorMessages = [];
		            if(response.data && response.data.length < 5){
		            	for(var i=0; i<response.data.length; i++) {
		            		var errorMsg = {
		            				"errorMessage":response.data[i].errorMessage
		            			};
		            		console.log(errorMsg);
		            		$scope.errorMessages.push(errorMsg);
		            	}
		            } else {
		              var errorMsg = {
	            				"errorMessage":"Cannot connect to WebAPI"
	            			};
		              $scope.errorMessages.push(errorMsg);
		            }
		            $scope.hasInfo = false;
		            $scope.hasError = true;
		            $scope.dataLate = false;
					$scope.dataNo = false;
					$mdDialog.hide();
		        });
			
		} else {
			console.log("Searching No Entries");
			
			$scope.showWait();
			
			WebAPIDoorLogs.getNoEntriesByEmployeeID(employeeID, fromDate, toDate, isWeekendsIncluded)
				.then(function(response) {
					
					$scope.redirectRequestIfSessionTimeout(response.headers('Content-Type'));
					
					if(response.data.length <= 0) {
						$scope.infoMessage = "No Record Found."
						$scope.hasInfo = true;
						$scope.hasError = false;
			            $scope.dataLate = false;
						$scope.dataNo = false;
					} else {
						$.each(response.data, function(key, value) {
							console.log('key:' + key + ' value:' + JSON.stringify(value));
						});
						
						$scope.noEntries = [] ;
						var count = 0;
						for(var i=0; i<response.data.length; i++) {
							for(var j=0; j<response.data[i].doorLogEntriesDetails.length; j++) {
								
								var monthYear = null;
								var dayofTheWeek = null;
								var daysWithNoEntry = null;
								
								if(j==0) {
									count++;
									monthYear = response.data[i].monthYear;
									
								} else {
									monthYear = null;
									
								}
								
								dayofTheWeek = response.data[i].doorLogEntriesDetails[j].dayofTheWeek;
								daysWithNoEntry = response.data[i].doorLogEntriesDetails[j].dayWithNoEntry;
								
								var tempObj = {
										"count":count,
										"monthYear":monthYear,
										"dayofTheWeek":dayofTheWeek,
										"daysWithNoEntry":daysWithNoEntry
										};
								$scope.noEntries.push(tempObj);
							}
						}
						$scope.hasInfo = false;
						$scope.dataLate = false;
						$scope.dataNo = true;
					}
					
					$scope.hasError = false;
					$mdDialog.hide();
					
				}, function(response) {
		            var errorMsg = '';
		            $scope.errorMessages = [];
		            if(response.data && response.data.length < 5) {
		            	for(var i=0; i<response.data.length; i++) {
		            		var errorMsg = {
		            				"errorMessage":response.data[i].errorMessage
		            			};
		            		console.log(errorMsg);
		            		$scope.errorMessages.push(errorMsg);
		            	}
		            } else {
		            	var errorMsg = {
	            				"errorMessage":"Cannot connect to WebAPI"
	            			};
		              $scope.errorMessages.push(errorMsg);
		            }
		            $scope.hasInfo = false;
		            $scope.hasError = true;
		            $scope.dataLate = false;
					$scope.dataNo = false;
					$mdDialog.hide();
		        });
		}
		
		
		} catch(err) {
	    	
		    console.log("doorLogsAdvanceSearchController->search encountered error :" + err);		    
		}
	};
	
	
	
	$scope.exportToFile = function(expName, dataLate, lateEntries, expFromTime) {
		
		try {
			
		
		if(dataLate) {
			
			var offenseDescription = "";
			var daysOfLate = 0;
			var minutesOfLate = 0;
			var report = [];
			console.log(expFromTime);
			var timeFrom = new Date("2017/01/01 " + expFromTime + ":00").getTime()/60000;
			console.log(timeFrom);
			
			for(var i=0; i<lateEntries.length; i++) {
				console.log(lateEntries[i].daysWithLateEntries + " : " + lateEntries[i].includeDate);
				if(lateEntries[i].includeDate) {
					report.push(lateEntries[i]);
		    	}
			}
			
			daysOfLate = report.length;
			for(var i=0; i<report.length; i++) {
				
				var timeIn = new Date("2017/01/01 " + report[i].daysWithLateEntriesDetail[0] + ":00").getTime()/60000;
				minutesOfLate = minutesOfLate + (timeIn - timeFrom);
				var dateLate = report[i].daysWithLateEntries.replace(/\//g, "-");
				offenseDescription = offenseDescription + dateLate + " " + report[i].dayofTheWeek + ": ";
				
				for(var j=0; j<report[i].daysWithLateEntriesDetail.length; j++) {
					
					if(j==0){
						offenseDescription = offenseDescription + " " + report[i].daysWithLateEntriesDetail[j];
					} else {
						offenseDescription = offenseDescription + " to " + report[i].daysWithLateEntriesDetail[j] + ",";
					}
					
				}
				
				
			}
			console.log(offenseDescription);
			console.log("Days of Late : " + daysOfLate);
			console.log("Total Minutes of Late : " + minutesOfLate);
			
			$scope.showWait();
			
			WebAPIDoorLogs.getExportLateEntries(expName, offenseDescription, daysOfLate, minutesOfLate, expFromTime)
				.then(function(response) {
					
					$scope.redirectRequestIfSessionTimeout(response.headers('Content-Type'));
					
					var blob = new Blob([response.data]);
			    	var fileNameToSaveAs = "NOTICE TO EXPLAIN_Tardiness.docx";
		            
		            // extract filename from response.headers('Content-Disposition')
		            var filename = response.headers('Content-Disposition');
		            
		            var filenameFromHeader = fileNameToSaveAs;
		            var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
		            var matches = filenameRegex.exec(filename);
		            if (matches != null && matches[1]) { 
		            	filenameFromHeader = matches[1].replace(/['"]/g, '');
		            }
		            
		            console.log("filename is:" + filenameFromHeader );
			    
					var downloadLink = document.createElement("a");
					//downloadLink.download = fileNameToSaveAs;
					downloadLink.download = filenameFromHeader;
					downloadLink.innerHTML = "Download File";
			
			        if (navigator.userAgent.indexOf('Android') < 
			            0 && window.webkitURL != null) {
			            // Chrome allows the link to be clicked
			            // without actually adding it to the DOM.
			            downloadLink.href = 
			              window.webkitURL.createObjectURL(blob);
			         }
			         else {
			            // Firefox requires the link to be added to the DOM
			            // before it can be clicked.
			            downloadLink.href = window.URL.createObjectURL(blob);
			            downloadLink.onclick = function(){};
			            downloadLink.style.display = "none";
			            document.body.appendChild(downloadLink);
			         }
			
			         downloadLink.click();
			         
			         $mdDialog.hide();
				
			}, function(response) {
				var errorMsg = '';
	            $scope.errorMessages = [];
	            var errorMsg = {
        				"errorMessage":"Cannot connect to WebAPI"
        			};
	            $scope.errorMessages.push(errorMsg);
	            $scope.hasInfo = false;
	            $scope.hasError = true;
	            $scope.dataLate = false;
				$scope.dataNo = false;
				
				$mdDialog.hide();
			});
		}
		
		
		} catch(err) {
	    	
		    console.log("doorLogsAdvanceSearchController->search encountered error :" + err);		    
		}
		
	};
	
	
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
	
	
    $scope.clear = function() {
    	$scope.searchCategory = "Late Entries";
    	$scope.hasInfo = false;
    	$scope.hasError = false;
    	$scope.dataLate = false;
    	$scope.dataNo = false;
    };
    
    
    $scope.toggle = false;
    
    
    $scope.displayDetail = function(j) {
    	var data = "data_" + j;
    	$scope[data] = $scope[data] == false ? true : false;
    };
    
    
    $scope.arrangeLate = function(includeDate, lateEntry) {
    	
    	if(!includeDate) {
    		var index = $scope.report.indexOf(lateEntry);
    		if(index > -1) {
    			$scope.report.splice(index, 1);
    			console.log("Remove : " + index);
    		}
    		
    	}
    };
    
    
    $scope.redirectRequestIfSessionTimeout =  function (contentTypeFromHeader) {
    	
    	try {
    		
			//var contentTypeFromHeader = jqXHR.getResponseHeader("Content-Type");
			if (contentTypeFromHeader.toLowerCase().indexOf("text/html") >= 0) {
				window.location.reload();
			}
			
    	} catch(err) {
	    	
		    console.log("doorLogsAdvanceSearchController->redirectRequestIfSessionTimeout encountered error :" + err);
		}

	};
	
	
	/**
	 * Function to update message area from html jquery script outside angularJS
	 * 
	 */
	$scope.displayMessageAlertFromJS = function(returnData) {
		
		try {

			if (returnData.status === true) {
				//this is info message
				$scope.infoMessage = returnData.message;
				$scope.hasInfo = true;
				$scope.hasError = false;
				
			} else {
				//this is error message
				$scope.errorMessages = [];
	            var errorMsg = {
	        				"errorMessage":returnData.message
	        			};
	            $scope.errorMessages.push(errorMsg);            

	            $scope.hasInfo = false;
	            $scope.hasError = true;
			}	

		} catch (err) {
			console.log("doorLogsNormalSearchController->displayMessageAlertFromJS encountered error :" + err);
		}        

	};
    
  }
]);
