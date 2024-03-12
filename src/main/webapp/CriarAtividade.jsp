<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="util.ManchaPT"%>
<%@ page import="java.util.*"%>
<%@ page import="classes.GestorSBD"%>
<%@ page import="util.RecursosReservados"%>
<%@ page import="java.sql.Time"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Criar Atividade</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
<link rel="stylesheet" type="text/css" href="mainCSS.css">

</head>

<body>
	<%
	String manchaID = request.getParameter("manchaID");
	%>
	<div class="fixed-header">
		<div class="home-link">
			<a href="PT.jsp"> <i class="fas fa-home"
				style="margin-right: 10px"></i> <span class="username"><%=(String) session.getAttribute("username")%></span>
			</a>
		</div>
		<a href="GerenteServlet?flag=logout"
			class="btn btn-secondary logout-btn">Logout</a>
	</div>

	<div class="wrapper">
		<div class="title" style="margin-top: 100px;">FITNESS TIME</div>
		<div class="center-container">
			<h2 style="text-align: center;">Criar Atividade</h2>
			<form action="PTServlet" method="post" style="text-align: center;">
				<div class="form-group">
					<label for="designacao">Designação:</label> <input type="text"
						class="form-control" id="designacao" name="designacao" required>
				</div>
				<div class="form-group">
					<label for="tipo">Tipo:</label> <select class="form-control"
						id="tipo" name="tipo" onchange="toggleData()">
						<option value="Semanal">Semanal</option>
						<option value="Data">Data</option>
					</select>
				</div>

				<div class="form-group" id="dataDiv" style="display: none;">
					<label for="dataReal">Data:</label> <input type="date"
						class="form-control" id="dataReal" name="dataReal">
				</div>
				<div class="form-group">
					<label for="tipoParticipantes">Tipo de Participantes:</label> <select
						class="form-control" id="tipoParticipantes"
						name="tipoParticipantes" onchange="toggleParticipantes()">
						<option value="Individual">Individual</option>
						<option value="Grupo">Grupo</option>
					</select>
				</div>
				<div class="form-group" id="partMinDiv">
					<label for="partMin">Participantes Mínimos:</label> <input
						type="number" class="form-control" id="partMin" name="partMin">
				</div>
				<div class="form-group" id="partMaxDiv">
					<label for="partMax">Participantes Máximos:</label> <input
						type="number" class="form-control" id="partMax" name="partMax">
				</div>


				<div class="d-flex align-items-center justify-content-center">
					<button type="button" class="btn btn-secondary"
						onclick="toggleReserva(<%=manchaID%>)">Adicionar Reserva</button>
				</div>
				<div id="recursosReservados"></div>


				<input type="hidden" name="flag" value="processCriarAtividade">
				<input type="hidden" id="encodedDesignacao" name="encodedDesignacao">
				<input type="hidden" name="manchaID" value="<%=manchaID%>">
				<button type="submit" class="btn btn-primary create-activity-btn">Criar
					Atividade</button>
			</form>
		</div>
	</div>

	<script>
		
		var equipamentosReservados = [];
		// função para adicionar ou remover equipamentos num array
		// muda também o estilo do botão associado ao id do equipamento para reflectir se estamos a adicionar ou remover
		function toggleEquipamento(equipamentoID) {
			var equipamentosReservadosInput = document
					.getElementById("equipamentosReservadosInput");
			var equipamentoRow = document.getElementById(equipamentoID);
			var addBtn = equipamentoRow.querySelector(".add-btn");
			var removeBtn = equipamentoRow.querySelector(".remove-btn");

			var index = equipamentosReservados.indexOf(equipamentoID);
			if (index === -1) {
				equipamentosReservados.push(equipamentoID);
				addBtn.style.backgroundColor = "red";
				addBtn.textContent = "Remover";
			} else {
				equipamentosReservados.splice(index, 1);
				addBtn.style.backgroundColor = "green";
				addBtn.textContent = "Adicionar";
			}
			console.log(equipamentosReservados);
			equipamentosReservadosInput.value = equipamentosReservados.join(',');
		}

		function toggleParticipantes() {
			var tipoParticipantes = document
					.getElementById("tipoParticipantes").value;
			var partMinDiv = document.getElementById("partMinDiv");
			var partMaxDiv = document.getElementById("partMaxDiv");

			if (tipoParticipantes === "Grupo") {
				partMinDiv.style.display = "block";
				partMaxDiv.style.display = "block";
			} else {
				partMinDiv.style.display = "none";
				partMaxDiv.style.display = "none";
			}
		}

		function toggleData() {
			var tipo = document.getElementById("tipo").value;
			var dataDiv = document.getElementById("dataDiv");

			if (tipo === "Data") {
				dataDiv.style.display = "block";
			} else {
				dataDiv.style.display = "none";
			}
		}

		function toggleReserva(manchaID, dataReal) {
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("recursosReservados").innerHTML = this.responseText;
                    document.getElementById("recursosReservados").style.display = "block";
                }
            };
            xhttp.open("GET", "AtualizaRecursosServlet?manchaID=" + manchaID + "&dateReal=" + document.getElementById("dataReal").value
            		+"&tipo=" + document.getElementById("tipo").value, true);
            xhttp.send();
			}
		

		function showErrorPopUp() {

			const urlParams = new URLSearchParams(window.location.search);
			const errorParam = urlParams.get('error');

			if (errorParam === 'true') {

				alert("Data Inválida. Tem de introduzir uma data com pelo menos 3 dias de antecedência da realização.");
			}
		}

		window.onload = function() {
			toggleParticipantes();
			toggleData();
			showErrorPopUp;
		};
		
        document.querySelector('form').addEventListener('submit', function() {
            var designacaoInput = document.getElementById('designacao');
            var encodedDesignacao = document.getElementById('encodedDesignacao');
            encodedDesignacao.value = encodeURIComponent(designacaoInput.value);
        });
	</script>
</body>
</html>

