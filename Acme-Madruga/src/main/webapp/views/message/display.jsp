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

	<a href="box/administrator,brotherhood,member/display.do?boxId=${boxId}">
		<spring:message code="message.button.return" />
	</a>

	<spring:message code="message.format.date" var="formatDate" />
	<p> 
		<strong> <spring:message code="message.display.sentMoment" />: </strong>
		<fmt:formatDate value="${messageToDisplay.sentMoment}" pattern="${formatDate}"/>
	</p>
		
	<p> 
		<strong> <spring:message code="message.display.subject" />: </strong>
	 	<jstl:out value="${messageToDisplay.subject}" />
	</p>

	<p> 
		<strong> <spring:message code="message.display.body" />: </strong>
		<jstl:out value="${messageToDisplay.body}" />
	</p>

	<p>
		<strong> <spring:message code="message.display.priority" />: </strong>
	  	<jstl:out value="${messageToDisplay.priority}" />
	</p>
	
	<jstl:if test="${not empty tags}">
		<strong><spring:message code="message.display.tags"/>:</strong>
		<ul>
			<jstl:forEach var="tag" items="${tags}">
				<li><jstl:out value="${tag}" /> </li>
			</jstl:forEach>
		</ul>
	</jstl:if>
	
	<p>
		<strong> <spring:message code="message.display.sender" />: </strong>
		<jstl:out value="${messageToDisplay.sender.fullname}" />
	</p>

	<strong> <spring:message code="message.display.recipients"/> </strong>
	<display:table name="${messageToDisplay.recipients}" id="row" requestURI="message/administrator,brotherhood,member/display.do?messageId=${messageToDisplay.id}" pagesize="5" class="displaytag">
		
		<display:column property="fullname" titleKey="message.recipient.name"/>
	
		<display:column property="email" titleKey="message.recipient.email"/>
	
	</display:table>
	
<input type="submit"
	   name="move"
	   value="<spring:message code="message.button.move" />" onclick="javascript: relativeRedir('message/administrator,brotherhood,member/move.do?messageId=${messageToDisplay.id}');" />
	   
	   