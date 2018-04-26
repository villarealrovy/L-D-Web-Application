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

    <!-- Bootstrap CSS -->
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">  	
<!--  	<link rel="stylesheet" href="static/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"> -->

<title>Title sample</title>
</head>
<body>

	<h1 align="right">Sample text for welcome</h1>
	
	<nav class="nav nav-pills nav-justified">
	  <a class="nav-link active" href="/welcome">HOME</a>
	  <a class="nav-link" href="/register">Sample Form</a>
	  <a class="nav-link" href="/show-users">Sample table</a>
	  <a class="nav-link" href="/login-user">Sample Login</a>
	  <a class="nav-link disabled" href="#">Disabled</a>
	</nav>
	
	<c:choose>
	
	<c:when test="${mode =='MODE_HOME'}">
	<h3>Home Page</h3>
	</c:when>
	
	<c:when test="${mode =='MODE_REGISTER'}">
	<div class="container text-center">
		<h3>Sample Form</h3>
	<hr>
		
	<form method="POST" action="save-user">
  <div class="form-group">
  <input type="hidden" name="id" value="${user.id}">
    <label for="exampleInputEmail1">Username</label>
    <input value="${user.username}" name="username" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="username . . ">
   
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">First Name</label>
    <input value="${user.firstname}" name="firstname" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="first name . .">
   
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">Last Name</label>
    <input value="${user.lastname}" name="lastname" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="last name . .">
   
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">Age</label>
    <input value="${user.age}" name="age" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="age . .">
   
  </div>
  <div class="form-group">
    <label for="exampleInputPassword1">Password</label>
    <input value="${user.password}" name="password" type="password" class="form-control" id="exampleInputPassword1" placeholder="Password . .">
  </div>
 
  <button type="submit" class="btn btn-primary">Submit</button>
</form>

	</div>
	</c:when>
	
	<c:when test="${mode =='ALL_USER'}">
	<div class="container text-center">
		<h3>Sample Table</h3>
	<hr>
	<table class="table">
  <thead>
    <tr>
      <th scope="col">ID</th>
      <th scope="col">UserName</th>
      <th scope="col">First Name</th>
      <th scope="col">Last Name</th>
      <th scope="col">Age</th>
      <th scope="col">Edit</th>
      <th scope="col">Delete</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="user" items="${users}">
    <tr>
      <td>${user.id}</td>
      <td>${user.username}</td>
      <td>${user.firstname}</td>
      <td>${user.lastname}</td>
      <td>${user.age}</td>
      <td><a href="/edit-user?id=${user.id }" >edit</a></td>
      <td><a href="/delete-user?id=${user.id }" >delete</a></td>
    </tr>
 </c:forEach>
  </tbody>
</table>
</div>
</c:when>



	<c:when test="${mode =='MODE_UPDATE'}">
	<div class="container text-center">
		<h3>Sample Update Form</h3>
	<hr>
		
	<form method="POST" action="save-edit-user">
  <div class="form-group">
  <input type="hidden" name="id" value="${user.id}">
    <label for="exampleInputEmail1">Username</label>
    <input value="${user.username}" name="username" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="username . . ">
   
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">First Name</label>
    <input value="${user.firstname}" name="firstname" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="first name . .">
   
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">Last Name</label>
    <input value="${user.lastname}" name="lastname" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="last name . .">
   
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">Age</label>
    <input value="${user.age}" name="age" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="age . .">
   
  </div>
  <div class="form-group">
    <label for="exampleInputPassword1">Password</label>
    <input value="${user.password}" name="password" type="password" class="form-control" id="exampleInputPassword1" placeholder="Password . .">
  </div>
 
  <button type="submit" class="btn btn-primary">Submit</button>
</form>

	</div>
	</c:when>
	
	
	
	<c:when test="${mode =='MODE_LOGIN'}">
	<div class="container text-center">
		<h3>Sample Login</h3>
	<hr>
		
	<form method="POST" action="login-success">
	<c:if test="${not empty error }">
		<div class="alert alert-danger">
			<c:out value="${error}"></c:out>
		</div>
	</c:if>
  <div class="form-group">
    <label for="exampleInputEmail1">Username</label>
    <input value="${user.username}" name="username" type="text" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="username . . ">
   
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">Password</label>
    <input value="${user.password}" name="password" type="password" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="password . .">
   </div>
  <div class="form-group">
   <button type="submit" class="btn btn-primary">Login</button>
  </div>
  </form>
  </div>
  </c:when>
	
	</c:choose>
	
	
	
	  <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="static/js/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
	<script src="static/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	
</body>
</html>