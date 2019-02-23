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
<form:form action="${Url}register${role}.do" modelAttribute="${role}" onsubmit="javascript:calcMD5();">
	<jstl:choose>
		<jstl:when test="${role == 'administrator'}">
			<h2><spring:message code="header.administrator"/></h2>
		</jstl:when>
		
		<jstl:when test="${role == 'brotherhood'}">
			<h2><spring:message code="header.brotherhood"/></h2>
		
			<form:hidden path="area"/>
			
		</jstl:when>
		
		<jstl:when test="${role == 'member'}">
			<h2><spring:message code="header.member"/></h2>
		</jstl:when>
		
		
	</jstl:choose>
		
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isSpammer"/>
	<form:hidden path="score"/>
	<form:hidden path="userAccount"/>
	
	<p><spring:message code="form.note"/></p>
	
	<fieldset>
		<legend><spring:message code="actor.legend"/></legend>
		
		<acme:textbox code="actor.name.requested" path="name"/>
	
		<br />
		
		<acme:textbox code="actor.middlename" path="middleName"/>
		
		<br />
		
		<acme:textbox code="actor.surname.requested" path="surname"/>
		
		<br />
		
		<acme:textbox code="actor.photo" path="photo"/>
		
		<br />
		
		<acme:textbox code="actor.email.requested" path="email"/> 
		
		<br />
		
		<acme:textbox code="actor.phoneNumber" path="phoneNumber" placeholder="+34 (111) 654654654" id="phoneNumber"/>
		
		<br />
		
		<acme:textbox code="actor.address" path="address"/>
		
		<br /> 
		 
		<jstl:if test="${role == 'brotherhood'}">
		
			<acme:textbox code="actor.brotherhood.title.requested" path="title"/>
			<br /> 
			
			<acme:textbox code="actor.brotherhood.establishmentDate.requested" path="establishmentDate"/>
			<br />
			
			<acme:textbox code="actor.brotherhood.pictures" path="pictures"/>
			<br />
		
		</jstl:if>
 		 
	</fieldset>
	
	<fieldset>
		<legend><spring:message code="userAccount.legend"/></legend>
		<!-- 
		<acme:textbox code="userAccount.username.requested" path="userAccount.username" id="usernameId" name="username"/>
		
		<br />
		
		<acme:password code="userAccount.password.requested" path="userAccount.password" id="passwordId" name="password"/>
		
		<br />
		
		<acme:password code="userAccount.confirmPassword.requested" path="userAccount.password" id="confirmPasswordId" name="confirmPassword"/>
		
		<br />
		 -->
		 
		 <label for="usernameId">
			<spring:message code="userAccount.username.requested" />
		</label>
		<input type="text" name="username" id="usernameId"/>
		<br />
		
		<label for="passwordId">
			<spring:message code="userAccount.password.requested" />
		</label>
		<input type="password" name="password" id="passwordId"/>
		<br />
		
		<label for="confirmPasswordId">
			<spring:message code="userAccount.confirmPassword.requested" />
		</label>
		<input type="password" name="confirmPassword" id="confirmPasswordId"/>
		<br />
		
		<security:authorize access="hasRole('ADMIN')" >
			
			<acme:textbox code="actor.authority" path="userAccount.authorities" readonly="true" value="ADMIN"/>
				
		</security:authorize>
 
 
		<security:authorize access="isAnonymous()" >
		
		<jstl:if test="${role == 'brotherhood'}">
		
			<acme:textbox code="actor.authority" path="userAccount.authorities" readonly="true" value="BROTHERHOOD"/>
			
		</jstl:if>
		
		<jstl:if test="${role == 'member'}">
		
			<acme:textbox code="actor.authority" path="userAccount.authorities" readonly="true" value="MEMBER"/>
		
		</jstl:if>
		
		</security:authorize>
		
		<input type="hidden" name="role" value="${role}"/>
	</fieldset>
	
	<acme:submit name="save" code="actor.save" onclick="javascript: return checkTelephone('${confirmTelephone}');"/>
	<acme:cancel url="welcome/index.do" code="actor.cancel"/>
	
</form:form>