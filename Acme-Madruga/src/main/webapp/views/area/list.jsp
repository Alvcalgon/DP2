<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="areas" id="row"
	requestURI="${requestURI }" class="displaytag" pagesize="5">

	<display:column>
		<a href="area/administrator/edit.do?areaId=${row.id}"><spring:message
				code="area.edit" /></a>
	</display:column>
	
	<display:column>
		<a href="area/administrator/delete.do?areaId=${row.id}"><spring:message
				code="area.delete" /></a>
	</display:column>

	<display:column	property="area.name" titleKey="area.name" />
	
	<display:column	property="area.pictures" titleKey="area.pictures" />

</display:table>

