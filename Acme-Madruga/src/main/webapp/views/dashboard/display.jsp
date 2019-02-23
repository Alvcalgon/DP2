<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<p> <strong> <spring:message code="dashboard.one" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataFixUpTaskPerUser[0]}" /> </td>
		<td> <jstl:out value="${dataFixUpTaskPerUser[1]}" /> </td>
		<td> <jstl:out value="${dataFixUpTaskPerUser[2]}" /> </td>
		<td> <jstl:out value="${dataFixUpTaskPerUser[3]}" /> </td>
	</tr>
</table>

<p> <strong> <spring:message code="dashboard.two" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataApplicationPerTask[0]}" /> </td>
		<td> <jstl:out value="${dataApplicationPerTask[1]}" /> </td>
		<td> <jstl:out value="${dataApplicationPerTask[2]}" /> </td>
		<td> <jstl:out value="${dataApplicationPerTask[3]}" /> </td>
	</tr>
</table>

<p> <strong> <spring:message code="dashboard.three" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataMaximumPrice[0]}" /> </td>
		<td> <jstl:out value="${dataMaximumPrice[1]}" /> </td>
		<td> <jstl:out value="${dataMaximumPrice[2]}" /> </td>
		<td> <jstl:out value="${dataMaximumPrice[3]}" /> </td>
	</tr>
</table>

<p> <strong> <spring:message code="dashboard.four" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataOfApplicationPrice[0]}" /> </td>
		<td> <jstl:out value="${dataOfApplicationPrice[1]}" /> </td>
		<td> <jstl:out value="${dataOfApplicationPrice[2]}" /> </td>
		<td> <jstl:out value="${dataOfApplicationPrice[3]}" /> </td>
	</tr>
</table>

<p> <strong> <spring:message code="dashboard.eleven" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataComplaintsPerTask[0]}" /> </td>
		<td> <jstl:out value="${dataComplaintsPerTask[1]}" /> </td>
		<td> <jstl:out value="${dataComplaintsPerTask[2]}" /> </td>
		<td> <jstl:out value="${dataComplaintsPerTask[3]}" /> </td>
	</tr>
</table>

<p> <strong> <spring:message code="dashboard.twelve" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataNotesPerReport[0]}" /> </td>
		<td> <jstl:out value="${dataNotesPerReport[1]}" /> </td>
		<td> <jstl:out value="${dataNotesPerReport[2]}" /> </td>
		<td> <jstl:out value="${dataNotesPerReport[3]}" /> </td>
	</tr>
</table>

<p>
	<strong> <spring:message code="dashboard.five" /> </strong>:
	<jstl:out value="${ratPendingApp}" />
</p>
<p>
	<strong> <spring:message code="dashboard.six" /> </strong>:
	<jstl:out value="${ratAcceptedApp}" />
</p>
<p>
	<strong> <spring:message code="dashboard.seven" /> </strong>:
	<jstl:out value="${ratRejectedApp}" />
</p>
<p>
	<strong> <spring:message code="dashboard.eight" /> </strong>:
	<jstl:out value="${ratPendingPeriodApp}" />
</p>
<p>
	<strong> <spring:message code="dashboard.thirteen" /> </strong>:
	<jstl:out value="${ratTaskWithComplaints}" />
</p>

<p> <strong> <spring:message code="dashboard.nine" />: </strong> </p>
<display:table name="customers" id="row" requestURI="dashboard/administrator/listCustomers" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
</display:table>

<p> <strong> <spring:message code="dashboard.ten" />: </strong> </p>
<display:table name="handyWorkers" id="row" requestURI="dashboard/administrator/listHandyWorkers" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
</display:table>

<p> <strong> <spring:message code="dashboard.fourteen" />: </strong> </p>
<display:table name="topThreeC" id="row" requestURI="dashboard/administrator/listCustomers" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
</display:table>

<p> <strong> <spring:message code="dashboard.fifteen" />: </strong> </p>
<display:table name="topThreeHW" id="row" requestURI="dashboard/administrator/listHandyWorkers" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
</display:table>

<p>
	<a href="welcome/index.do"> <spring:message code="dashboard.return" /> </a>
</p>