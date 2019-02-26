<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="message/administrator,brotherhood,member/send.do" modelAttribute="message" >
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="sentMoment"/>
	<form:hidden path="sender"/>
	<form:hidden path="isSpam" />
	
	<acme:textbox path="subject" code="message.display.subject" />
	<acme:textarea path="body" code="message.display.body" />
	<acme:select path="priority" code="message.display.priority" items="${priorities}" itemLabel="${priorities.iterator}" />
	<acme:select path="recipients" code="message.display.recipients" items="${actors}" itemLabel="fullname" />
	<acme:textarea path="tags" code="message.display.tags" />	
 	<br />
	
	<!-- Buttons -->
	<acme:submit name="send" code="message.button.send" />
	<acme:cancel url="box/administrator,brotherhood,member/list.do" code="message.button.cancel" />
</form:form>