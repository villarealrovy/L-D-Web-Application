/**
 * ITLogs WebAPIDoorLogs application factory.
 * 
 * @desc API for RESTful web service call.
 * @version 1.0.0
 * 
 * -----------------------------
 * History:
 * Date(YYYY.MM.DD)|author|revision
 * 2017.03.07 | r.monte@ph.fujitsu.com | added getLogsByEmpNo()
 *
 *
 *------------------------------------------------------------------------------
 * Copyright (C) 2017 FUJITSU All rights reserved.
 *------------------------------------------------------------------------------
 */

app.factory('WebAPIDoorLogs', ['$http', '$q', function ($http, $q) {

  var WebAPIDoorLogs = {};
  
  var serverHost = window.location.host;
  
  
  /**
   * REST API call for getting door logs by employee number
   */
  WebAPIDoorLogs.getLogsByEmpNo = function (empNo) {
	  
	try {
		
	  var urlToConnect = 'http://' + serverHost + '/itLogs/rest/search/listAll/' + empNo;
	  console.log("WebAPIDoorLogs->getLogsByEmpNo connecting GET:" + urlToConnect);	  
	  
      var request = $http({
        method: 'GET',
        url: urlToConnect,
        headers: {'Accept': 'application/json'}
      });
         
      return(request);
      
    } catch(err) {
    	
      console.log("WebAPIDoorLogs->getLogsByEmpNo encountered error :" + err);
    }
    
  };
  
  
  /**
   * REST API call for getting Late Entries in door logs by employee ID, date range and start time
   */
  WebAPIDoorLogs.getLateEntriesByEmpoyeeID  = function (employeeID, fromDate, fromTime, toDate) {
	
	try {
		
	  fromDate = fromDate.split("/").join("");
	  toDate = toDate.split("/").join("");
	  
	  var urlToConnect = 'http://' + serverHost + '/itLogs/rest/advanceSearch/listLateEntries/' 
      					+ employeeID + '/' + fromDate + '/' + fromTime + '/' + toDate;
	  
	  console.log("WebAPIDoorLogs->getLateEntriesByEmpoyeeID connecting GET:" + urlToConnect);	  
	    
	  var request = $http({
	        method: 'GET',
	        url: urlToConnect,
	        headers: {'Accept': 'application/json'}
	      });
	         
	  return(request);
	      
	} catch(err) {
		
	  console.log("WebAPIDoorLogs->getLateEntriesByEmpoyeeID encountered error :" + err);
	}
	
  };
  

  /**
   * REST API call for getting Absent or No Entries in door logs by employee ID, date range and isWkendsInc
   */
  WebAPIDoorLogs.getNoEntriesByEmployeeID = function (employeeID, fromDate, toDate, isWeekendsIncluded) {
	  
	try {
	  
	  fromDate = fromDate.split("/").join("");
	  toDate = toDate.split("/").join("");
	  
	  var urlToConnect = 'http://' + serverHost + '/itLogs/rest/advanceSearch/listNoEntries/' 
      					+ employeeID + '/' + fromDate + '/' + toDate + '/' + isWeekendsIncluded;

	  console.log("WebAPIDoorLogs->getNoEntriesByEmployeeID connecting GET:" + urlToConnect);
	    
	  var request = $http({
	        method: 'GET',
	        url: urlToConnect,
	        headers: {'Accept': 'application/json'}
	      });
	         
	  return(request);
	      
    } catch(err) {
    	
      console.log("WebAPIDoorLogs->getNoEntriesByEmployeeID encountered error :" + err);
    }
    
  };


  /**
   * REST API call for getting door logs by Date range, employee No. and searchCategory:pair/all
   */
  WebAPIDoorLogs.getLogsByDate = function (fromDate, toDate, empNo, searchCategory) {
		  
	try {
		
	  fromDate = fromDate.split("/").join("");
	  toDate = toDate.split("/").join("");
	  
	  var urlToConnect = 'http://' + serverHost + '/itLogs/rest/search/listAllByDate/' 
      					+ fromDate + '/' + toDate  + '/' + empNo + '/' + searchCategory;

	  console.log("WebAPIDoorLogs->getLogsByDate connecting GET:" + urlToConnect);	  	
    
      var request = $http({
		        method: 'GET',
		        url: urlToConnect,
		        headers: {'Accept': 'application/json'}
		      });
         
      return(request);
      
    } catch(err) {
    	
      console.log("WebAPIDoorLogs->getLogsByDate encountered error :" + err);
    }

  };
	  
	  
  /**
   * REST API call for posting FTS email request with CSV file, message body and email subject
   */
  WebAPIDoorLogs.emailFileFTS = function (file, messageBody, subjectMail, emailApprover) {
	  
    try {
    	
      var formData = new FormData();
	  formData.append("file", file);
	  formData.append("messageBody", messageBody);
	  formData.append("subjectMail", subjectMail);
	  formData.append("emailApprover", emailApprover);
	  
	  console.log('emailApprover:' + emailApprover);
	  
	  var urlToConnect = 'http://' + serverHost + '/itLogs/rest/search/emailFTS/';

	  console.log("WebAPIDoorLogs->emailFileFTS connecting POST:" + urlToConnect);
		
	  var request = $http({
		    method: 'POST',
		    data: formData,
		    transformRequest: angular.identity,
		    headers: {'Content-Type': undefined},
		    url: urlToConnect
		  });
         
	  return(request);
      
    } catch(err) {
    	
      console.log("WebAPIDoorLogs->emailFileFTS encountered error :" + err);
    }
    
  };	  
	  

  /**
   * REST API call for getting word doc Late Entries report to download
   */
  WebAPIDoorLogs.getExportLateEntries = function (expName, offenseDescription, daysOfLate, minutesOfLate, expFromTime) {
	  
    try {
    	
      var urlToConnect = 'http://' + serverHost + '/itLogs/rest/advanceSearch/exportLateEntries/' 
      					+ expName + '/'+ offenseDescription + '/' + daysOfLate + '/' + minutesOfLate + '/' + expFromTime;

      console.log("WebAPIDoorLogs->getExportLateEntries connecting GET:" + urlToConnect);

      var request = $http({
			        method: 'GET',
			        url: urlToConnect,
			        responseType: "blob"
			      });      
         
      return(request);
      
    } catch(err) {
    	
      console.log("WebAPIDoorLogs->getExportLateEntries encountered error :" + err);
    }

  };
	  
  
	  
  return WebAPIDoorLogs;
  
}]);
