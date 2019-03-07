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

<display:table name="boxes" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">
	<display:column>
		<jstl:if test="${!row.isSystemBox}">
			<a href="box/administrator,brotherhood,member/edit.do?boxId=${row.id}">
				<spring:message code="box.edit" />
			</a>
		</jstl:if>
	</display:column>
	
	<display:column>
		<a href="box/administrator,brotherhood,member/display.do?boxId=${row.id}">
			<spring:message code="box.display" />
		</a>
	</display:column>
	
	<display:column property="name" titleKey="box.name"/>
	<display:column property="parent" titleKey="box.parent"/>
</display:table>

<!-- LINKS -->
<a href="box/administrator,brotherhood,member/create.do">
	<spring:message code="box.create" />
</a>
<br />

<a href="message/administrator,brotherhood,member/send.do">
	<spring:message code="message.send"/>
</a>
<br />

<security:authorize access="hasRole('ADMIN')">
	<a href="message/administrator/broadcast.do">
		<spring:message code="message.broadcast" />
	</a>
	<br />
	<a href="message/administrator/breachNotification.do" onclick="return confirm('<spring:message code="message.confirm"/>')">
		<spring:message code="message.breach" />
	</a>
</security:authorize>
	