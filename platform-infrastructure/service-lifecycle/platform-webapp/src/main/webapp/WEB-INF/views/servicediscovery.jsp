<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="xc" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Societies services - Service Discovery</title>
<style>
.error {
	color: #ff0000;
}
 
.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
</head>

<body>

	<h3>Service Discovery Service</h3>

		
<form:form method="POST" action="servicediscovery.html" commandName="sdForm">
		<form:errors path="*" cssClass="errorblock" element="div" />
		<table>
			<tr>
				<td>Service Discovery Methods :</td>
					<td><form:select path="method" >
					   <form:option value="NONE" label="--- Select ---" />
					   <form:options items="${methods}" />
					</form:select></td>
				<td><form:errors path="method" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td>Node :</td>
				<td><form:input path="node" />
				</td>
				<td><form:errors path="node" cssClass="error" />
				</td>
			</tr>	
			<tr>
				<td colspan="3"><input type="submit" /></td>
			</tr>
		</table>		
	</form:form>
	
	<br/>		
	<h4>Please click the service to use .....</h4>
		
	<table>	
		<tr>
			<td><a href="servicediscovery.html">Service Discovery Service</a></td>
			<td><a href="servicecontrol.html">Service Control Service</a></td>
		</tr>		
	</table>
		
</body>
</html>

