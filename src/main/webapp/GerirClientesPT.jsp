<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="classes.GestorSBD"%>
<%@ page import="util.Cliente"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Cliente Profile</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
	<link rel="stylesheet" type="text/css" href="mainCSS.css">
<style>
#fichaCliente > div {
    text-align: left; 
    max-width: 800px; 
    width: 100%;
}

        form {
            text-align: center;
            margin-top: 20px;
        }

        form input {
            margin-bottom: 10px;
            padding: 8px;
        }

        form button {
            padding: 10px;
            background-color: #007bff;
            color: #fff;
            border: none;
            cursor: pointer;
        }

</style>
</head>

<body>
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
	
	<div class="title" style="margin-top: 100px;">Gerir Clientes</div>
		
		
		<div class="center-container">
		<div id="searchClienteDiv" style="display: block;">
			<label style="text-align: center">Pesquisar Cliente: <br>
				<input autocomplete="off" list="clientes" id="cliente" type="search"
				name="clienteInput" oninput="showClientes(this.value);"
				placeholder="Pesquisar Cliente" style="width: 100%;">
			</label> <br>
			<div id="clientes"></div>
		</div>
		<form style="text-align: center;">
    <input type="hidden" id="clienteUsername" name="clienteUsername" value="">
    <button type="button" id="fichaClienteButton" disabled class="btn btn-primary" style="margin: 10px; width: 100%" onclick="toggleFichaCliente();">
        Abrir Ficha Cliente
    </button>
</form>
		<div id="fichaCliente">
		</div>
			</div>
	</div>

	<script>
		function getXHR() {
			var invocation = null;
			try {
				invocation = new XMLHttpRequest();
			} catch (e) {
				try {
					invocation = new ActiveXObject("Msxml2.XMLHTTP");
				} catch (e) {
					try {
						invocation = new ActiveXObject("Microsoft.XMLHTTP");
					} catch (e) {
						alert("Your browser broke!");
						return null;
					}
				}
			}
			return invocation;
		}

		var xmlHttp = getXHR();
		
		//Função que faz o pedido GET para retornar a lista de clientes (autocomplete)
		function showClientes(str) {
			if (event.key === 'Enter') {
				return;
			}
			var url = "ClientesAtividade";
			url += "?query=" + encodeURIComponent(str);
			url = encodeURI(url);
			xmlHttp.onreadystatechange = function() {
				if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
					console.log("enter");
					console.log(xmlHttp.responseText);
					document.getElementById("clientes").innerHTML = xmlHttp.responseText;
				}
			}
			xmlHttp.open("GET", url, true);
			xmlHttp.send();
		}
		
		//Apenas desbloquear o botão quando introduzido um nome válido
		function setClienteUsername(username) {
			var fichaClienteButton = document
					.getElementById("fichaClienteButton");
			fichaClienteButton.disabled = false;
			fichaClienteButton.innerText = "Abrir Ficha Cliente: "
					+ username;
			document.getElementById("clienteUsername").value = encodeURIComponent(username);
		}
		
		//Efectuar o pedido GET ao Servlet AtualizaFichaCliente para disponibilizar a ficha do cliente
		function toggleFichaCliente() {
			console.log("Enter");
			   var nome = document.getElementById("clienteUsername").value;
            var xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("fichaCliente").innerHTML = this.responseText;
                }
            };
            xhttp.open("GET", "AtualizaFichaCliente?nome=" + nome , true);
            xhttp.send();
			}
		

	</script>
</body>

</html>