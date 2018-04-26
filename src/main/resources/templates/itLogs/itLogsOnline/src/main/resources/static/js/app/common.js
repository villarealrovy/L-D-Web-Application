/**
 * ITLogs common function.
 * 
 * @desc common function.
 * @version 1.0.0
 * 
 * -----------------------------
 * 
 * History: Date(YYYY.MM.DD)|author|revision
 * 
 * 2017.03.07 | r.monte@ph.fujitsu.com | initial version.
 * 
 * 
 * ------------------------------------------------------------------------------
 * Copyright (C) 2017 FUJITSU All rights reserved.
 * ------------------------------------------------------------------------------
 */
var tmonth=new Array("January","February","March","April","May","June","July","August","September","October","November","December");
var isCardView = false;

$(document).ready(function(){
	var docWidth = $(window).width(); // returns width of HTML document

	if(docWidth < 900){
		isCardView = true;
	}else{
		isCardView = false;
	}
	
    GetClock();
    setInterval(GetClock, 1000);
});

function GetClock(){
    var d=new Date();
    var nmonth=d.getMonth(),ndate=d.getDate(),nyear=d.getYear();
    if(nyear<1000) nyear+=1900;

    var d=new Date();
    var nhour=d.getHours(),nmin=d.getMinutes(),nsec=d.getSeconds();
    if(nhour<=9) nhour="0"+nhour;
    if(nmin<=9) nmin="0"+nmin;
    if(nsec<=9) nsec="0"+nsec;

    $("#clockbox").text(tmonth[nmonth]+" "+ndate+", "+nyear+" "+nhour+":"+nmin+":"+nsec);
}

function GetTodayDate(){
    var d=new Date();
    var nmonth=d.getMonth(),ndate=d.getDate(),nyear=d.getYear();
    if(nyear<1000) nyear+=1900;
    var date = ndate + "-" + tmonth[nmonth].toUpperCase() + "-" + nyear;

    return date;
}

/****** FORM VALIDATIONS ********/

function isValidEmail(email){
    return /^[a-z0-9]+([-._][a-z0-9]+)*@([a-z0-9]+(-[a-z0-9]+)*\.)+[a-z]{2,4}$/.test(email)
        && /^(?=.{1,64}@.{4,64}$)(?=.{6,100}$).*/.test(email);
}

function isNumeric(value){
	return $.isNumeric(value);
}

function isEmpty(value){
	if($.trim(value) === ""){
		return true;
	}
	
	return false;
}
