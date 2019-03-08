<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<spring:message code="confirm.telephone" var="confirmTelephone"/>
<form:form action="actor/administrator,brotherhood,member/edit.do" modelAttribute="registrationForm" onsubmit="javascript:calcMD5();">
	<jstl:choose>
		<jstl:when test="${rol == 'Brotherhood'}">
			<h2>
				<spring:message code="header.brotherhood" />
			</h2>
		</jstl:when>
		<jstl:when test="${rol == 'Member'}">
			<h2>
				<spring:message code="header.member" />
			</h2>
		</jstl:when>
		<jstl:when test="${rol == 'Administrator'}">
			<h2>
				<spring:message code="header.administrator" />
			</h2>
		</jstl:when>

	</jstl:choose>

	<form:hidden path="id"/>
	
	<fieldset>
		<legend><spring:message code="actor.legend"/></legend>
	
		<acme:textbox code="actor.name" path="name"/>
		
		<br />
		
		<acme:textbox code="actor.middlename" path="middleName"/>
		
		
		<br />
		
		<acme:textbox code="actor.surname" path="surname"/>
		
	
		<br />
		
		<acme:textbox code="actor.photo" path="photo"/>
		
		<br />
		
		<acme:textbox code="actor.email" path="email" placeholder="mail@email.com"/>
		
		<br />
		
		<acme:textbox code="actor.phoneNumber" path="phoneNumber" placeholder="+34 (111) 654654654" id="phoneNumber"/>
		
		<br />
		
		<acme:textbox code="actor.address" path="address"/>
		
		<br /> 
		
		<jstl:if test="${rol == 'Brotherhood'}">
		
			<acme:textbox code="actor.brotherhood.title" path="title"/>
			<br /> 
			
			<acme:textbox code="actor.brotherhood.establishmentDate" path="establishmentDate"/>
			<br />
			
			<p style="color:blue;"><spring:message code="brotherhood.info.pictures"/></p>
			<acme:textarea code="actor.brotherhood.pictures" path="pictures"/>
			<br />
		
		</jstl:if>
	</fieldset>
 
	<fieldset>
		<legend><spring:message code="userAccount.legend"/></legend>
	
		<acme:textbox path="username" code="userAccount.username" />
		<br>

		<acme:password path="password" code="userAccount.password" id="passwordId" />
		<br>

		<acme:password path="confirmPassword" code="userAccount.confirmPassword" id="confirmPasswordId"/>
		<br>
		
	</fieldset>
	
 
 
 	<jstl:choose>
		<jstl:when test="${rol == 'Administrator'}">
			<acme:submit name="saveAdmin" code="actor.save" onclick="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
		<jstl:when test="${rol == 'Brotherhood'}">
			<acme:submit name="saveBrotherhood" code="actor.save" onclick="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
		<jstl:when test="${rol == 'Member'}">
			<acme:submit name="saveMember" code="actor.save" onclick="javascript: return checkTelephone('${confirmTelephone}');"/>
		</jstl:when>
	</jstl:choose>
	
	<acme:cancel url="actor/display.do" code="actor.cancel"/>
 
	
	<hr>
	
</form:form>