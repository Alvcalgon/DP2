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

<display:table name="socialProfiles" id="row" requestURI="${requestURI }"  class="displaytag" pagesize="5">
	
	
		<jstl:if test="${isAuthorized == true}">
		<display:column>
		<a href="socialProfile/administrator,brotherhood,member/edit.do?socialProfileId=${row.id}"><spring:message code="socialProfile.edit"/></a>
		</display:column>
		</jstl:if>
	
	
	
		<jstl:if test="${isAuthorized == true}">
		<display:column>
		<a href="socialProfile/administrator,brotherhood,member/delete.do?socialProfileId=${row.id}"><spring:message code="socialProfile.delete"/></a>
			</display:column>
		</jstl:if>

	
	<display:column>
			<a href="socialProfile/display.do?socialProfileId=${row.id}"><spring:message
					code="socialProfile.display" /></a>
		</display:column>
	
	<display:column property="nick" titleKey="socialProfile.nick"/>
	
	<display:column property="socialNetwork" titleKey="socialProfile.socialNetwork"/>
	
	<display:column property="linkProfile" titleKey="socialProfile.linkProfile"/>
	
</display:table>

	<jstl:if test="${isAuthorized == true}">
	<a href="socialProfile/administrator,brotherhood,member/create.do"><spring:message code="socialProfile.new"/></a>
	</jstl:if>