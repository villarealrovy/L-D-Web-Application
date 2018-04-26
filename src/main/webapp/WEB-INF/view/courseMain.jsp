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
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Course Maintenance Page</title>

<style>
.w3-btn {margin-bottom:10px;}
#customers {
    font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
    border-collapse: collapse;
    width: 100%;
}

#customers td, #customers th {
    border: 1px solid #ddd;
    padding: 8px;
}

#customers tr:nth-child(even){background-color: #f2f2f2;}

#customers tr:hover {background-color: #ddd;}

#customers th {
    padding-top: 12px;
    padding-bottom: 12px;
    text-align: left;
    background-color: #4CAF50;
    color: white;
}

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
</style>
</head>
<body>

<body>
<div style="padding-right:16px">
  <h2 align="right">Title Logo</h2>
<div class="topnav">
  <a href="/home">Home</a>
  <a class="active" href="/courseMain">Course Maintenance</a>
  <a href="/userMain">User Maintenance</a>
  <a href="/report">Report</a>
  <a href="/">Logout</a>
</div>

<div style="padding-left:16px">
  <h2>Course Maintenance</h2>
</div>

<table id="customers">
  <tr>
    <th>Course</th>
    <th>Date</th>
    <th>Time</th>
    <th>Slot</th>
    <th>Instructor</th>
    <th>Location</th>
    <th>Mandatory</th>
  </tr>
  <tr>
    <td>Email Etiquitte</td>
	<td>April 1 - April 3 2018</td>
    <td>10:00 - 11:00</td>
    <td>15</td>
    <td>Heidi Del Rosario</td>
    <td>22nd Floor Eco Tower Bldg</td>
    <td align="center"><input type="checkbox" name="vehicle" value="Bike"> </td>
  </tr>
</table>

<div class="w3-container">
<p align="center">
  <button class="w3-button w3-white w3-border w3-round-large">Add Course</button>

</div>

</body>


</body>
</html>