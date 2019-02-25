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

<form:form action="box/administrator,brotherhood,member/edit.do" modelAttribute="box" >
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isSystemBox" />
	
	<acme:textbox code="box.name" path="name"/>
	<acme:select code="box.parent"
	 			 path="parent"
	 			 items="${parents}"
	 			 itemLabel="name" />
	
	<!-- Buttons -->
	<acme:submit name="save" code="box.save" />
	<jstl:if test="${box.id != 0}">
		<acme:submit name="delete"
		 			 code="box.delete" />
	</jstl:if>
	<acme:cancel code="box.cancel" url="box/administrator,brotherhood,member/list.do" />
</form:form>