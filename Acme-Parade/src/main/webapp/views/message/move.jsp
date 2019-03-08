<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="message/administrator,brotherhood,member/move.do" modelAttribute="messageForm">
	<form:hidden path="messageId" />
	<form:hidden path="originBoxId" />

	<acme:select path="destinationBoxId" code="message.move.destinationBox" items="${destinationBoxes}" itemLabel="name" />

	<acme:submit name="move" code="message.button.move" />
	<acme:cancel url="box/administrator,brotherhood,member/list.do" code="message.button.cancel" />
</form:form>

