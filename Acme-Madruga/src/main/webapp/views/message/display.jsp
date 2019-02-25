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

	<spring:message code="message.format.date" var="formatDate" />
	<p> <strong> <spring:message code="message.display.sendMoment" />: </strong>  <fmt:formatDate value="${messageToDisplay.sendMoment}" pattern="${formatDate}"/> </p>
	<br />
	
	<p> <strong> <spring:message code="message.display.subject" />: </strong> <jstl:out value="${messageToDisplay.subject}" /> </p>
	<br />

	<p> <strong> <spring:message code="message.display.body" />: </strong> <jstl:out value="${messageToDisplay.body}" /> </p>
	<br />

	<p> <strong> <spring:message code="message.display.priority" />: </strong>  <jstl:out value="${messageToDisplay.priority}" /> </p>
	<br />
	
	<jstl:if test="${not empty tags}">
		<strong><spring:message code="message.display.tags"/>:</strong>
		<br>
		<ul>
			<jstl:forEach var="tag" items="${tags}">
				<li><jstl:out value="${tag}" /> </li>
			</jstl:forEach>
		</ul>
	</jstl:if>
	
	<p> <strong> <spring:message code="message.display.sender" />: </strong>  <jstl:out value="${messageToDisplay.sender.name} ${messageToDisplay.sender.middleName} ${messageToDisplay.sender.surname} ${messageToDisplay.sender.email}" /> </p>
	<br />

 <fieldset>
	<legend><spring:message code="message.display.recipients"/></legend>
	
	<display:table name="${messageToDisplay.recipients }" id="row" requestURI="message/administrator,customer,handyWorker,referee,sponsor/display.do?messageId=${messageToDisplay.id }" pagesize="5" class="displaytag">
		
		<display:column property="name" titleKey="message.recipient.name"/>
		
		<display:column property="surname" titleKey="message.recipient.surname"/>
	
		<display:column property="email" titleKey="message.recipient.email"/>
	
	</display:table>
</fieldset> 


<input type="button" name="return" value="<spring:message code="message.button.return" />" 
				onclick="javascript: relativeRedir('box/administrator,customer,handyWorker,referee,sponsor/display.do?boxId=${boxId}');" />
<!-- 
<jstl:if test="${messageToDisplay.id != 0}">
		<input type="submit" name="delete" value="<spring:message code="message.button.delete"/>" 
			onclick="return confirm('<spring:message code="message.confirm.delete" />')" />

</jstl:if>
 -->					
<input type="submit" name="move" value="<spring:message code="message.button.move" />"
				onclick="javascript: relativeRedir('message/administrator,customer,handyWorker,referee,sponsor/move.do?messageId=${messageToDisplay.id}');" />