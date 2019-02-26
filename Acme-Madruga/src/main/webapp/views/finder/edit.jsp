<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="finder/member/edit.do" modelAttribute="finder">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<acme:textbox path="keyword" code="finder.keyword"/>
	<div>
		<form:label path="area">
			<spring:message code="finder.area" />
		</form:label>	
		<form:select path="area">
			<form:option value="" label="----" />		
			<form:options items="${areas}"/>
		</form:select>
		<form:errors path="area" cssClass="error" />
	</div>
	<acme:textbox path="minimumDate" code="finder.minimum.date" placeholder="dd/mm/yyyy"/>
	<acme:textbox path="maximumDate" code="finder.maximum.date" placeholder="dd/mm/yyyy"/>
	
	
	<!-- Buttons -->
	
	<div>
		<acme:submit name="save" code="finder.save"/>
		&nbsp;
		<acme:cancel code="finder.clear" url="finder/member/clear.do"/>
		&nbsp;
		<acme:cancel code="finder.cancel" url="procession/member/listFinder.do"/>
	</div>

</form:form>