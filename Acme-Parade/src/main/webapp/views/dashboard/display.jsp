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
		<td> <jstl:out value="${dataMembersPerBrotherhood[0]}" /> </td>
		<td> <jstl:out value="${dataMembersPerBrotherhood[1]}" /> </td>
		<td> <jstl:out value="${dataMembersPerBrotherhood[2]}" /> </td>
		<td> <jstl:out value="${dataMembersPerBrotherhood[3]}" /> </td>
	</tr>
</table>

<spring:message code="brotherhood.date.format" var="dateFormat"/>

<p><strong> <spring:message code="dashboard.two" />: </strong></p>
<display:table name="largestBrotherhoods" id="row1" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
	<display:column property="title" titleKey="brotherhood.title" />
	<display:column property="establishmentDate" titleKey="brotherhood.establishmentDate" format="${dateFormat}" />
</display:table>

<p><strong> <spring:message code="dashboard.three" />: </strong></p>
<display:table name="smallestBrotherhoods" id="row2" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
	<display:column property="title" titleKey="brotherhood.title" />
	<display:column property="establishmentDate" titleKey="brotherhood.establishmentDate" format="${dateFormat}" />
</display:table>

<p> <strong> <spring:message code="dashboard.four" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="parade.ticker" /> </th>
		<th> <spring:message code="dashboard.request.pending" /> </th>
		<th> <spring:message code="dashboard.request.approved" /> </th>
		<th> <spring:message code="dashboard.request.rejected" /> </th>
	</tr>
	<jstl:forEach var="fila" items="${mapa.keySet()}">
		<tr>
			<td> <jstl:out value="${fila}" /> </td>
			<td> <jstl:out value="${mapa.get(fila)[0]}" /> </td>
			<td> <jstl:out value="${mapa.get(fila)[1]}" /> </td>
			<td> <jstl:out value="${mapa.get(fila)[2]}" /> </td>
		</tr>
	</jstl:forEach>
</table>

<p> <strong> <spring:message code="dashboard.five" />: </strong> </p>
<display:table name="parades" id="row3" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="ticker" titleKey="parade.ticker" />
	<display:column property="title" titleKey="parade.title" />
</display:table>

<p> <strong> <spring:message code="dashboard.six" />: </strong> </p>
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.request.pending" />: </strong>
		<jstl:out value="${pendingRatio}" />
	</p>
	
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.request.approved" />: </strong>
		<jstl:out value="${approvedRatio}" />
	</p>
	
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.request.rejected" />: </strong>
		<jstl:out value="${rejectedRatio}" />
	</p>
	
<p> <strong> <spring:message code="dashboard.seven" />: </strong> </p>

<display:table name="members" id="row4" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
</display:table>

<p> <strong> <spring:message code="dashboard.eight" />: </strong> </p>
<jstl:set var="hLabels" value="${histogramLabels}" />
<jstl:set var="hValues" value="${histogramValues}" />

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>

<div id="container" style="width:800px; height:600px;">
	<canvas id="myChart"></canvas>
</div>

<script>
window.onload = function() {
	var columnas = new Array();
	<jstl:forEach var="w_label" items="${hLabels}">
		var etiqueta = '${w_label}';
		columnas.push(etiqueta);
	</jstl:forEach>
	
	var filas = new Array();
	<jstl:forEach var="w_value" items="${hValues}">
		var val = '${w_value}';
		filas.push(val);
	</jstl:forEach>	
	
	var myChart = document.getElementById("myChart").getContext("2d");
	var massPopChart = new Chart(myChart, {
		type: 'bar',
		data: {
			labels: columnas,
			datasets:[{
				label: 'Histogram',
				data: filas,
				backgroundColor: 'green',
				borderColor: 'green',
				borderWidth: 1
			}]
		},
		options: {
			scales: {
				yAxes: [{
					ticks: {
						beginAtZero: true
					}
				}]
			}
		}
	});
};
</script>

<p> <strong> <spring:message code="dashboard.nine.a" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="area.name" /> </th>
		<th> <spring:message code="area.ratio" /> </th>
	</tr>
		<jstl:forEach var="row" items="${ratioBrotherhoodsPerArea.keySet()}">
			<tr>
				<td> <jstl:out value="${row}" /> </td>
				<td> <jstl:out value="${ratioBrotherhoodsPerArea.get(row)}" /> </td>
			</tr>
		</jstl:forEach>
</table>

<p> <strong> <spring:message code="dashboard.nine.b" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="area.name" /> </th>
		<th> <spring:message code="area.count" /> </th>
	</tr>
		<jstl:forEach var="row" items="${countBrotherhoodsPerArea.keySet()}">
			<tr>
				<td> <jstl:out value="${row}" /> </td>
				<td> <jstl:out value="${countBrotherhoodsPerArea.get(row)}" /> </td>
			</tr>
		</jstl:forEach>
</table>

<p> <strong> <spring:message code="dashboard.nine.c" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataBrotherhoodPerArea[0]}" /> </td>
		<td> <jstl:out value="${dataBrotherhoodPerArea[1]}" /> </td>
		<td> <jstl:out value="${dataBrotherhoodPerArea[2]}" /> </td>
		<td> <jstl:out value="${dataBrotherhoodPerArea[3]}" /> </td>
	</tr>
</table>

<p> <strong> <spring:message code="dashboard.ten" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataResultsPerFinder[0]}" /> </td>
		<td> <jstl:out value="${dataResultsPerFinder[1]}" /> </td>
		<td> <jstl:out value="${dataResultsPerFinder[2]}" /> </td>
		<td> <jstl:out value="${dataResultsPerFinder[3]}" /> </td>
	</tr>
</table>

<p>
	<strong> <spring:message code="dashboard.eleven" /> </strong>:
	<jstl:out value="${ratioEmptyVsNonEmpty}" />
</p>

<p> <strong> <spring:message code="dashboard.twelve" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${findDataNumberRecordsPerHistory[0]}" /> </td>
		<td> <jstl:out value="${findDataNumberRecordsPerHistory[1]}" /> </td>
		<td> <jstl:out value="${findDataNumberRecordsPerHistory[2]}" /> </td>
		<td> <jstl:out value="${findDataNumberRecordsPerHistory[3]}" /> </td>
	</tr>
</table>

<p><strong> <spring:message code="dashboard.thirteen" />: </strong></p>
<display:table name="findBrotherhoohLargestHistory" id="row13" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
	<display:column property="title" titleKey="brotherhood.title" />
	<display:column property="establishmentDate" titleKey="brotherhood.establishmentDate" format="${dateFormat}" />
</display:table>

<p><strong> <spring:message code="dashboard.fourteen" />: </strong></p>
<display:table name="findBrotherhoohsLargestHistoryAvg" id="row14" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="fullname" titleKey="actor.fullname" />
	<display:column property="title" titleKey="brotherhood.title" />
	<display:column property="establishmentDate" titleKey="brotherhood.establishmentDate" format="${dateFormat}" />
</display:table>

<p>
	<strong> <spring:message code="dashboard.fifteenth" /> </strong>:
	<jstl:out value="${ratioAreaWithoutChapter}" />
</p>

<p> <strong> <spring:message code="dashboard.sixteen" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${findDataNumerParadesCoordinatedByChapters[0]}" /> </td>
		<td> <jstl:out value="${findDataNumerParadesCoordinatedByChapters[1]}" /> </td>
		<td> <jstl:out value="${findDataNumerParadesCoordinatedByChapters[2]}" /> </td>
		<td> <jstl:out value="${findDataNumerParadesCoordinatedByChapters[3]}" /> </td>
	</tr>
</table>

<p><strong> <spring:message code="dashboard.seventeen" />: </strong></p>
<display:table name="chaptersCoordinateLeast10MoreParadasThanAverage" id="row14" requestURI="dashboard/administrator/display.do" pagesize="5" class="displaytag">
	<display:column property="name" titleKey="actor.name" />
	<display:column property="title" titleKey="brotherhood.title" />
</display:table>

<p>
	<strong> <spring:message code="dashboard.eighteen" /> </strong>:
	<jstl:out value="${findRatioParadesDraftModeVSParadesFinalMode}" />
</p>

<p> <strong> <spring:message code="dashboard.nineteen" />: </strong> </p>
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.parades.submitted" />: </strong>
		<jstl:out value="${findRatioSubmittedParadesFinalMode}" />
	</p>
	
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.parades.accepted" />: </strong>
		<jstl:out value="${findRatioAcceptedParadesFinalMode}" />
	</p>
	
	<p style="text-indent:10px;">
		<strong> <spring:message code="dashboard.parades.rejected" />: </strong>
		<jstl:out value="${findRatioRejectedParadesFinalMode}" />
	</p>
	
<p>
	<strong> <spring:message code="dashboard.twenty" /> </strong>:
	<jstl:out value="${ratioActiveSponsorship}" />
</p>

<p> <strong> <spring:message code="dashboard.twentyOne" />: </strong> </p>
<table>
	<tr>
		<th> <spring:message code="dashboard.average" /> </th>
		<th> <spring:message code="dashboard.min" /> </th>
		<th> <spring:message code="dashboard.max" /> </th>
		<th> <spring:message code="dashboard.deviation" /> </th>
	</tr>
	<tr>
		<td> <jstl:out value="${dataSponsorshipPerSponsor[0]}" /> </td>
		<td> <jstl:out value="${dataSponsorshipPerSponsor[1]}" /> </td>
		<td> <jstl:out value="${dataSponsorshipPerSponsor[2]}" /> </td>
		<td> <jstl:out value="${dataSponsorshipPerSponsor[3]}" /> </td>
	</tr>
</table>

<p><strong> <spring:message code="dashboard.twentyTwo" />: </strong></p>
<display:table name="topFiveSponsors" id="row22" requestURI="dashboard/administrator/display.do" class="displaytag">
	<display:column property="name" titleKey="actor.name" />
	<display:column property="email" titleKey="actor.email" />
</display:table>

<p>
	<a href="welcome/index.do"> <spring:message code="dashboard.return" /> </a>
</p>