<%@ include file="/header.jsp" %>

<div class="list">
	<table align="center">
		<c:forEach var="servico" items="${servicos}">
	  	<tr>
	  	    <td>${servico.name}</td>
		</tr>
		</c:forEach>
	</table>
</div>

<%@ include file="/footer.jsp" %>