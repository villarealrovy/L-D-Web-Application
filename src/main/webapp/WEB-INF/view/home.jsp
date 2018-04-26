<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <!-- Required meta tags -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Home Page</title>

<link href='static/calendar/calendar css/fullcalendar.min.css' rel='stylesheet' />
<link href='static/calendar/calendar css/fullcalendar.print.min.css' rel='stylesheet' media='print' />
<script src='static/calendar/lib/moment.min.js'></script>
<script src='static/calendar/lib/jquery.min.js'></script>
<script src='static/calendar/fullcalendar.min.js'></script>

<style>
body {
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
}

.topnav {
  overflow: hidden;
  background-color: #333;
}

.topnav a {
  float: left;
  color: #f2f2f2;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 14px;
}

.topnav a:hover {
  background-color: #ddd;
  color: black;
}

.topnav a.active {
  background-color: #4CAF50;
  color: white;
}




  #calendar {
    max-width: 900px;
    margin: 0 auto;
  }


</style>



<script>

  $(document).ready(function() {

    $('#calendar').fullCalendar({
      header: {
        left: 'prev,next today',
        center: 'title',
        right: 'month,agendaWeek,agendaDay,listWeek'
      },
      defaultDate: new Date(),
      navLinks: true, // can click day/week names to navigate views

      weekNumbers: true,
      weekNumbersWithinDays: true,
      weekNumberCalculation: 'ISO',

      editable: true,
      eventLimit: true, // allow "more" link when too many events
      events: [
        {
          title: 'All Day Event',
          start: '2018-04-01'
        },
        {
          title: 'Long Event',
          start: '2018-04-07',
          end: '2018-04-10'
        },
      ]
    });

  });

</script>


</head>
<body>

<div style="padding-right:16px">
  <h2 align="right">Title Logo</h2>
  </div>
<div class="topnav">
  <a class="active" href="/home">Home</a>
  <a href="/courseMain">Course Maintenance</a>
  <a href="/userMain">User Maintenance</a>
  <a href="/report">Report</a>
  <a href="/">Logout</a>
</div>

<div style="padding-left:16px">
  <h2>Course Calendar</h2>
</div>

<div id='calendar'></div>



</body>

</html>