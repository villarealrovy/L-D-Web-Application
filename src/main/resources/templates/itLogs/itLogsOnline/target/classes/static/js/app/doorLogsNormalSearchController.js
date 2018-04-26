/**
 * ITLogs Angular JS doorLogsNormalSearchController controller.
 * 
 * @desc doorLogsNormalSearchController controller function.
 * @version 1.0.0
 * 
 * -----------------------------
 * 
 * History: Date(YYYY.MM.DD)|author|revision
 * 
 * 2017.03.07 | r.monte@ph.fujitsu.com | initial version.
 * 2017.03.13 | m.cahinod@ph.fujitsu.com | added mddialog functionality.
 * 2017.03.13 | t.alba@ph.fujitsu.com | added search functionality.
 * 2017.05.09 | r.monte@ph.fujitsu.com | add carriage return in csv writing.
 * 
 * 
 * ------------------------------------------------------------------------------
 * Copyright (C) 2017 FUJITSU All rights reserved.
 * ------------------------------------------------------------------------------
 */
app.controller("doorLogsNormalSearchController", ['$scope', '$mdDialog',
  '$location', '$anchorScroll', '$timeout', 'WebAPIDoorLogs', '$filter',
  'Blob', 'FileSaver', '$rootScope',
  function($scope, $mdDialog, $location, $anchorScroll, 
      $timeout, WebAPIDoorLogs, $filter, Blob, FileSaver, $rootScope) {
	
	$scope.employeeList = [];
	$scope.doorLogsData = [];
	$scope.searchCategory = "Pair Entry";
	$scope.hasInfo = false;
	$scope.hasError = false;
	$scope.dataTable = false;
	$scope.selectedAll = false;	
	

	/**
	 * Function to show door logs by Date Test
	 */
	$scope.showListByDate = function(fromDate, toDate, empNo, searchCategory) {

		try {
			
		
		console.log("From Date: " + fromDate + " To Date: " + toDate + " Employee No.: " + empNo + " Search Category: " + searchCategory); 
		$scope.errorMessages = [];
		
		if ((fromDate === undefined) || (toDate === undefined) 
				|| angular.equals(fromDate, "") || angular.equals(toDate, "")) {
			
			var errorMsg = '';
			
			var errorMsg = { "errorMessage":"Date field is required." };
			
		      $scope.errorMessages.push(errorMsg);
		      
		      $scope.hasInfo = false;
		      $scope.hasError = true;
		      $scope.dataTable = false;
		      $scope.selectedAll = false;
		      
		      return;
		}
		
		$scope.showWait();
		
		WebAPIDoorLogs.getLogsByDate(fromDate, toDate, empNo, searchCategory).then(function(response) {
	
			$scope.redirectRequestIfSessionTimeout(response.headers('Content-Type'));
			
			if(response.data.length <= 0) {
				$scope.infoMessage = "No Record Found."
				$scope.hasInfo = true;
				$scope.hasError = false;
				$scope.dataTable = false;
				$scope.selectedAll = false;
				
			} else {
				$scope.doorLogsData = [];
				
				for (var i = 0; i < response.data.length; i++) {
		
					var date = response.data[i].date;
					var time = response.data[i].time;
					var area = response.data[i].area;
					var floor = response.data[i].floor;
					var door = response.data[i].door;
					var reader = response.data[i].reader;
					var empNo = response.data[i].employee.empNo;
					var name = response.data[i].employee.fullname;
					var username = response.data[i].employee.username;
					var cardNo = response.data[i].cardNo;
					var company = response.data[i].company;
		
					var tempObj = {
						"date" : date,
						"time" : time,
						"area" : area,
						"floor" : floor,
						"door" : door,
						"reader" : reader,
						"empNo" : empNo,
						"name" : name,
						"username" : username,
						"cardNo" : cardNo,
						"company" : company,
						checked: false
					};
		
					$scope.doorLogsData.push(tempObj); 
				}
				
				$scope.hasInfo = false;
				$scope.hasError = false;
				$scope.dataTable = true;
				$scope.selectedAll = false;
			}
			
			$mdDialog.hide();
			
		}, function(response) {
	            var errorMsg = '';
	            console.log(response.data);
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
	            $scope.dataTable = false;
	            $scope.selectedAll = false;
	            $mdDialog.hide();
        });
		
		
		} catch(err) {
			
		    console.log("doorLogsNormalSearchController->showListByDate encountered error :" + err);
		    $mdDialog.hide();
		}
		
	};

    
    $scope.clear = function() {
    	
    	$scope.searchCategory = "Pair Entry";
    	$scope.hasInfo = false;
    	$scope.hasError = false;
    	$scope.dataTable = false;
    	$scope.selectedAll = false;
    	$scope.selectAll();
    }
    
	
    
    /**
     * Function to toggle 
     *       to calculate total hours
     *       before showing the selectedFtsDialog
     * @param ev event
     */    
    $scope.totalCalculatedTime = function(frmDateIn, endDateIn) {
       
    	var totalCalculatedTime = "ERROR";
    	
    	try {
	       //var frmDate = new Date("2017-03-30 8:15:00");
	       //var endDate = new Date("2017-03-31 8:21:00");
	       var frmDate = new Date(frmDateIn);
	       var endDate = new Date(endDateIn);    	
	    	
	       var diffMs = (endDate - frmDate);
	       //var totalMsAday = 86400000;
	       var diffMins = diffMs/1000/60;
	       //var diffDays = Math.floor(diffMs / totalMsAday);
	       
	       var diffHrs =  Math.floor(diffMins / 60);
	       var diffMinRem =  Math.floor(diffMins % 60);
	       
	       totalCalculatedTime = diffHrs + " hrs & " + diffMinRem + " mins";
	       
	       console.log(totalCalculatedTime);
	       
	       if (diffHrs < 0) {
	    	   
	    	   $scope.errorMessages = [];
	           var errorMsg = {
	       				"errorMessage":"Please Select Later Time Out Entry for " + frmDateIn
	       			};
	           $scope.errorMessages.push(errorMsg);
	           
	           $scope.hasInfo = false;
	           $scope.hasError = true;
	           totalCalculatedTime = "ERROR";
	       } else if (diffHrs > 24) {
	    	   
	    	   $scope.errorMessages = [];
	           var errorMsg = {
	       				"errorMessage":"Please Select Time Out Entry for " + frmDateIn + " that will not Exceed for 24 hours long."
	       			};
	           $scope.errorMessages.push(errorMsg);
	           
	           $scope.hasInfo = false;
	           $scope.hasError = true;
	           totalCalculatedTime = "ERROR";
	       } 
    	} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->totalCalculatedTime encountered error :" + err);
		}

       return totalCalculatedTime;
        
    };
    
    
    $scope.backFts = function(ev) {
    	
    	try {
    	
	    	$mdDialog.show({
				controller : 'DialogCtrl',
				templateUrl : "../../template/show_fts_table.html",
				parent : angular.element(document.body),
				targetEvent : ev,
				clickOutsideToClose : false,
				scope: $scope,        // use parent scope in template
		        preserveScope: true  // do not forget this if use parent scope	        
			});
	    	
	    	//adjustment of mddialog showing at the center screen	    	
	    	$timeout( function(){
	    		
    		    //Set the popup window to center
    		    $('.showFtsDialog').css("position", "absolute");
    		    $('.showFtsDialog').css('top',
    		    		$(window).height()/2 - $('.showFtsDialog').height()/2 + $(window).scrollTop() + "px");
	    		
	    	}, 600);
		    
    	} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->backFts encountered error :" + err);
		}
    	
    }
	
    /**
     * Function to toggle 
     *       to show FTS dialog
     * @param ev event
     */
    $scope.showFts = function(ev) {
    	
    	try {
    		
    	
        $scope.hasInfo = false;
        $scope.hasError = false;
    	
    	var myFilteredValues = $filter('filter')($scope.doorLogsData, { checked: true });
    	
    	var arrayLength = myFilteredValues.length;
    	
    	if (arrayLength == 0) {
    		
    		$scope.errorMessages = [];
            var errorMsg = {
        				"errorMessage":"Please Select Time Logs for FTS Filing."
        			};
            $scope.errorMessages.push(errorMsg);

            $scope.hasInfo = false;
            $scope.hasError = true;
    		return;
    		
    	} else if ((arrayLength % 2) != 0) {
    		
    		$scope.errorMessages = [];
            var errorMsg = {
        				"errorMessage":"Missing Pair for Selected Time Logs. Match time IN and time OUT."
        			};
            $scope.errorMessages.push(errorMsg);

            $scope.hasInfo = false;
            $scope.hasError = true;
    		return;
    	}   	
    	
    	$scope.selectedFts = [];
    	$scope.subjectFTS = "FTS - System Error";
    	$scope.emailApprover = "g.mabale@ph.fujitsu.com";
    	
    	for (var i = 0; i < arrayLength-1; i=i+2) {
    		
    		var frmDate = myFilteredValues[i].date + " " + myFilteredValues[i].time;
    	    var endDate = myFilteredValues[i+1].date + " " + myFilteredValues[i+1].time;
    	    
    	    if (myFilteredValues[i].reader.toUpperCase() === myFilteredValues[i+1].reader.toUpperCase()) {
    	    	$scope.errorMessages = [];
                var errorMsg = {
            				"errorMessage":"Missing Pair for " + frmDate + ". Match time IN and time OUT."
            			};
                $scope.errorMessages.push(errorMsg);

                $scope.hasInfo = false;
                $scope.hasError = true;
        		return;
    	    }
    	    
    	    //handle reverse selection
    	    if (myFilteredValues[i].reader.toUpperCase() === "OUT") {
    	    	endDate = myFilteredValues[i].date + " " + myFilteredValues[i].time;
        	    frmDate = myFilteredValues[i+1].date + " " + myFilteredValues[i+1].time;
    	    }    	    
    	    
    	    var timeDuration = $scope.totalCalculatedTime(frmDate, endDate);
    	    
    	    if (timeDuration === "ERROR") {
    	    	return;
    	    }
    	    
    	    var logDate = $filter('date')(new Date(myFilteredValues[i].date), 'MMMM d, y');
    	    
    	    if (myFilteredValues[i].date != myFilteredValues[i+1].date) {
    	    	var logDate2 = $filter('date')(new Date(myFilteredValues[i+1].date), 'MMMM d, y');
    	    	logDate += " - " + logDate2;
    	    }
    	    
    	    var logTime = {
    	    		"date" : logDate,
    				"timeIn" : myFilteredValues[i].time, //.substring(0, 5),
    				"timeOut" : myFilteredValues[i+1].time, //.substring(0, 5),
    				"totalHours" : timeDuration
    			};
    	    
    	    //search if date is already added
    	    var isError = false; 
    	    $scope.selectedFts.forEach(function(x) {
        		
    	    	if (x.date === logDate) {
    	    		isError = true;
    	    	}
        		
    		});
    	    
    	    if (isError) {
    	    	
    	    	$scope.errorMessages = [];
                var errorMsg = {
            				"errorMessage":"Duplicate Entry for " + logDate
            			};
                $scope.errorMessages.push(errorMsg);
                
                errorMsg = {
        				"errorMessage":"Please Select Only One Pair of IN and OUT per Date."
        			};
                $scope.errorMessages.push(errorMsg);                

                $scope.hasInfo = false;
                $scope.hasError = true;
        		return;
    	    	
    	    }
    	    
    	    $scope.selectedFts.push(logTime);
    	    console.log("selected["+i+"]: date:"+myFilteredValues[i].date + " time:"+myFilteredValues[i].time);
    	    console.log("return: " + timeDuration);
    	}
    	
    	$mdDialog.show({
			controller : 'DialogCtrl',
			templateUrl : "../../template/show_fts_table.html",
			parent : angular.element(document.body),
			targetEvent : ev,
			clickOutsideToClose : false,
			scope: $scope,        // use parent scope in template
	        preserveScope: true  // do not forget this if use parent scope
		});
    	
    	
    	//adjustment of mddialog showing at the center screen
    	$timeout( function(){
    		
		    //Set the popup window to center
		    $('.showFtsDialog').css("position", "absolute");
		    $('.showFtsDialog').css('top',
		    		$(window).height()/2 - $('.showFtsDialog').height()/2 + $(window).scrollTop() + "px");	   	    
    		
    	}, 600);
    	
    	
    	} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->showFts encountered error :" + err);
		}
    	
	};
	
	
	/**
	 * Function to toggle to show email FTS dialog
	 * 
	 * @param ev
	 *            event
	 */
	$scope.showEmailFts = function(ev) {
		
		try {			
		
			$scope.composeMail();
			
			$mdDialog.show({
				controller : 'DialogCtrl',
				templateUrl : "../../template/email_fts.html",
				parent : angular.element(document.body),
				targetEvent : ev,
				clickOutsideToClose : false,				
				scope: $scope,        // use parent scope in template
		        preserveScope: true  // do not forget this if use parent scope
			});
			
	    	//adjustment of mddialog showing at the center screen	    	
	    	$timeout( function(){
	    		
    		    //Set the popup window to center
    		    $('.emailFtsDialog').css("position", "absolute");
    		    $('.emailFtsDialog').css('top',
    		    		$(window).height()/2 - $('.emailFtsDialog').height()/2 + $(window).scrollTop() + "px");
	    		
	    	}, 600);
		    
	    	
		} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->showEmailFts encountered error :" + err);
		}

	};
	

	$scope.composeMail = function() {
	
		try {
			
			var dates = "\n\t -  ";
			var datesContent = "";
			
			for (var i = 0; i < $scope.selectedFts.length; i++) {
			
				datesContent += dates + $scope.selectedFts[i].date;
			}
	
			$scope.message_me = "Hi Ma'am Ge, \n\nAttached is my FTS application for the date/s: "
					+ datesContent + "\n\nFor your approval. \n\nThanks and Best Regards, \n" + $scope.employeeName;
			
			
			
		} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->composeMail encountered error :" + err);
		}

	};

	/**
	 * Function to close dialogs
	 * 
	 * @param ev
	 *            event
	 */
	$scope.hideFts = function(ev) {
		
		$mdDialog.hide();
	};
	
	
	/**
	 * Function to save export FTS dialogs
	 * 
	 * @param ev
	 *            event
	 */
	$scope.exportFts = function(ev) {
		
		try {
		
		 
	 	var joinStr = ',';
    	var csvContent = '';
		
    	$scope.selectedFts.forEach(function(x) {
    		var logDate;
    		var arr = x.date.split(" - ");
    		
    		//for first date
    		if (arr.length > 1) {
    			//console.log("this is array:" + arr);
    			logDate = $filter('date')(new Date(arr[0]), 'd/M/y');
    		} else {
    			//if date is only one
    			logDate = $filter('date')(new Date(x.date), 'd/M/y');
    		}    		

			csvContent += "FTS,I," + $scope.currentEmpNo 
						+ joinStr + logDate 
						+ joinStr + x.timeIn + '\r\n' ;
			
			//for second date
    		if (arr.length > 1) {
    			//console.log("this is array:" + arr);
    			logDate = $filter('date')(new Date(arr[1]), 'd/M/y');
    		}
			
			csvContent += "FTS,O," + $scope.currentEmpNo 
			+ joinStr + logDate 
			+ joinStr + x.timeOut + '\r\n' ;
		});
    	
    	var csvFileAsBlob = new Blob([csvContent], {
                type: "text/csv;charset=utf-8"
            });
          
    	var fileNameToSaveAs = "transaction_" + $scope.currentUserName + ".csv";
    
		var downloadLink = document.createElement("a");
		downloadLink.download = fileNameToSaveAs;
		downloadLink.innerHTML = "Download File";

        if (navigator.userAgent.indexOf('Android') < 
            0 && window.webkitURL != null) {
            // Chrome allows the link to be clicked
            // without actually adding it to the DOM.
            downloadLink.href = 
              window.webkitURL.createObjectURL(csvFileAsBlob);
         }
         else {
            // Firefox requires the link to be added to the DOM
            // before it can be clicked.
            downloadLink.href = window.URL.createObjectURL(csvFileAsBlob);
            downloadLink.onclick = function(){};
            downloadLink.style.display = "none";
            document.body.appendChild(downloadLink);
         }

         downloadLink.click();  		
		
		
		
		$mdDialog.hide();
		
		
		} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->exportFts encountered error :" + err);
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

	}
	
	
	/**
	 * Function to update message area from mddialog
	 * 
	 */
	$scope.displayMessageAlert = function(returnData) {
		
		try {

			if (returnData.status === true) {
				//this is info message
				$scope.$parent.infoMessage = returnData.message;
				$scope.$parent.hasInfo = true;
				$scope.$parent.hasError = false;
				
			} else {
				//this is error message
				$scope.$parent.errorMessages = [];
	            var errorMsg = {
	        				"errorMessage":returnData.message
	        			};
	            $scope.$parent.errorMessages.push(errorMsg);            

	            $scope.$parent.hasInfo = false;
	            $scope.$parent.hasError = true;
			}	

		} catch (err) {
			console.log("doorLogsNormalSearchController->displayMessageAlert encountered error :" + err);
		}        

	}
	
	
	
	/**
	 * Function to send email FTS dialogs
	 * 
	 * @param ev
	 *            event
	 */
	$scope.sendEmailFts = function(ev, message_me) {
		
		try {
			
		 
	 	var joinStr = ',';
    	var csvContent = '';
		
    	$scope.selectedFts.forEach(function(x) {    		
    		var logDate;
    		var arr = x.date.split(" - ");
    		
    		//for first date
    		if (arr.length > 1) {
    			//console.log("this is array:" + arr);
    			logDate = $filter('date')(new Date(arr[0]), 'd/M/y');
    		} else {
    			//if date is only one
    			logDate = $filter('date')(new Date(x.date), 'd/M/y');
    		}    		

			csvContent += "FTS,I," + $scope.currentEmpNo 
						+ joinStr + logDate 
						+ joinStr + x.timeIn + '\r\n' ;
			
			//for second date
    		if (arr.length > 1) {
    			console.log("this is array:" + arr);
    			logDate = $filter('date')(new Date(arr[1]), 'd/M/y');
    		}
			
			csvContent += "FTS,O," + $scope.currentEmpNo 
			+ joinStr + logDate 
			+ joinStr + x.timeOut + '\r\n' ;
		});
    	
    	var csvFileAsBlob = new Blob([csvContent], {
                type: "text/csv;charset=utf-8"
            });
          
    	var fileNameToSaveAs = "transaction_" + $scope.currentUserName + ".csv";
    	
    	var fileUpload = new File([csvFileAsBlob], fileNameToSaveAs);
    	var subjectMail = "[PRECEDA REQUEST] " + $scope.subjectFTS;
		
    	$mdDialog.hide();
    	
    	$scope.showWait();
    	
    	var emailApprover = $scope.emailApprover;
    	console.log("rom test: " + emailApprover);
    	
    	WebAPIDoorLogs.emailFileFTS(fileUpload, message_me, subjectMail, emailApprover)
			.then(function(response) {				
				
				$scope.redirectRequestIfSessionTimeout(response.headers('Content-Type'));
				console.log("success in emailFileFTS: " + response);
				
				var message = "FTS Email Request Sent.";
				if (response.data != null && response.data[0] != null) {
					message = response.data[0].errorMessage;
				}	
				
				var returnData = {
						"status" : true,
        				"message": message
        		};
				
				$scope.$parent.displayMessageAlert(returnData);
				
				$mdDialog.hide();
				
			}, function(response) {
				console.log("error in emailFileFTS: " + response);
		        
				var message = "FTS Email Request Error Occurred.";
				if (response.data != null && response.data[0] != null) {
					message = response.data[0].errorMessage;
				}				
				
				var returnData = {
						"status" : false,
        				"message": message
        		};
				
				$scope.$parent.displayMessageAlert(returnData);
				
				$mdDialog.hide();
			});
    	
    	
		} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->sendEmailFts encountered error :" + err);
		}
    	
	};
	

	/**
	 * Function to toggle 
	 *       progress circular
	 * @param ev event
	 */
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
	
	/**
	 * Function to toggle 
	 *       error dialog
	 * @param ev event
	 */
	$scope.errorModal = function(ev, errorMsg) {
      
      var errorModal = $mdDialog.confirm()
        .title('Error Encountered')
        .textContent(errorMsg)
        .ariaLabel('Error')
        .targetEvent(ev)
        .ok('はい')
        .cancel('いいえ');

      $mdDialog.show(errorModal);
    };
    
    
    $scope.exportDoorLogs = function (fromDate, toDate, username) {   	
    	
    	try {
    		
    		
        $scope.hasInfo = false;
        $scope.hasError = false;
    	console.log('fromDate:' + fromDate + " toDate:" + toDate + " username:" + username);
    	
    	var myFilteredValues = $filter('filter')($scope.doorLogsData, { checked: true });
    	
    	var arrayLength = myFilteredValues.length;
    	
    	if (arrayLength == 0) {
    		
    		$scope.errorMessages = [];
            var errorMsg = {
        				"errorMessage":"Please Select Time Logs to Export."
        			};
            $scope.errorMessages.push(errorMsg);

            $scope.hasInfo = false;
            $scope.hasError = true;
    		return;
    	}    	
    	
    	
        var joinStr = ',';
    	var csvContent = 'Date, Time, Area, Floor, Door, Reader, Employee No., Name, Username, Card No., Company\r\n';
		
    	myFilteredValues.forEach(function(x) {
			csvContent += x.date 
						+ joinStr + x.time 
						+ joinStr + x.area 
						+ joinStr + x.floor 
						+ joinStr + x.door 
						+ joinStr + x.reader 
						+ joinStr + x.empNo 
						+ joinStr + "\"" + x.name + "\"" 
						+ joinStr + x.username
						+ joinStr + x.cardNo 
						+ joinStr + x.company
						+ '\r\n' ;
		});
    	
    	var csvFileAsBlob = new Blob([csvContent], {
                type: "text/csv;charset=utf-8"
            });
          
    	
    	var fileNameToSaveAs = "IT_LOGS_" + username + "_" 
    							+ fromDate.replace(/\//g, "-")
    							+ "_to_" 
    							+ toDate.replace(/\//g, "-") 
    							+ ".csv";    	
    	
		var downloadLink = document.createElement("a");
		downloadLink.download = fileNameToSaveAs;
		downloadLink.innerHTML = "Download File";

        if (navigator.userAgent.indexOf('Android') < 
            0 && window.webkitURL != null) {
            // Chrome allows the link to be clicked
            // without actually adding it to the DOM.
            downloadLink.href = 
              window.webkitURL.createObjectURL(csvFileAsBlob);
         }
         else {
            // Firefox requires the link to be added to the DOM
            // before it can be clicked.
            downloadLink.href = window.URL.createObjectURL(csvFileAsBlob);
            downloadLink.onclick = function(){};
            downloadLink.style.display = "none";
            document.body.appendChild(downloadLink);
         }

         downloadLink.click();    	
    	
         
    	} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->exportDoorLogs encountered error :" + err);
		}

    };    
    
    
    
    $scope.updateSelectedAll = function($event) {
 
    	try {
    		
	    	var isAllChecked = true;
	    	
			//check if all items are selected
			$scope.doorLogsData.forEach(function(x) {
	      		
	        	if (x.checked === false){
	        		isAllChecked = false;
	            }
	    	});
	    		
			$scope.selectedAll = isAllChecked;
			
    	} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->updateSelectedAll encountered error :" + err);
		}
    };
    
    
    $scope.selectAll = function(){
    	
    	try {
    		
	      	$scope.doorLogsData.forEach(function(x) {
	      		
	        	if($scope.selectedAll){
	        		x.checked = true;
	            }
	        	else{
	        		x.checked = false;
	            }
	    	});
	      	
    	} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->selectAll encountered error :" + err);
		}
    };
    
    
    $scope.redirectRequestIfSessionTimeout =  function (contentTypeFromHeader) {
    	
    	try {
    		
			//var contentTypeFromHeader = jqXHR.getResponseHeader("Content-Type");
			if (contentTypeFromHeader.toLowerCase().indexOf("text/html") >= 0) {
				window.location.reload();
			}
			
    	} catch(err) {
	    	
		    console.log("doorLogsNormalSearchController->redirectRequestIfSessionTimeout encountered error :" + err);
		}

	};
    
    
    
}]);


app.controller('DialogCtrl', function($scope, $mdDialog) {

	//codes for additional logic here

});

