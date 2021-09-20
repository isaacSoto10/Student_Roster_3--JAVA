<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<title>All Classes</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	<script src = "https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js">
	</script>
	<script>
		$(document).ready(function(){
		    $("#newform").click(function(){
		        $("#newclass").slideToggle();
		    });
		    $("#secretbox").click(function () {
		    	document.getElementById("secretbox").innerHTML = "<h3>Scroll to the top!</h3>";
		        $("#secrettext").hide(function () {
		            $("#secrettext").text(($("#secrettext").text() == 'Classes List') ? 'You found the secret message!' : 'Classes List').show();
		        });
		    });
		});
	</script>