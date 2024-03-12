<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<%@ page import="classes.GestorSBD" %>
<%@ page import="java.util.*" %>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Criar Perfil PT</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body>
  <div class="wrapper">
    <div class="title">
      FITNESS TIME
    </div>
    <div class="center-container">
      <h2 class="text">Criar Conta Cliente</h2>
      <form action="ClienteServlet" method="post" onsubmit="return validarFormulario();">
        <div class="form-group">
          <label for="nome">Nome:</label>
          <input type="text" class="form-control" id="nome" name="nome" required>
        </div>

        <div class="form-group">
          <label for="apelido">Apelido:</label>
          <input type="text" class="form-control" id="apelido" name="apelido" required>
        </div>
        
        <div class="form-group">
  			<label for="dataNascimento">Data de Nascimento:</label>
  			<input type="date" class="form-control" id="dataNasc"  name="dataNasc" required>
		</div>
		
		<div class="form-group">
          <label for="nome">NIF:</label>
          <input type="number" class="form-control" id="nif" name="nif" maxlength="9" required>
        </div>
		
        <div class="form-group">
          <label for="email">Email:</label>
          <input type="email" class="form-control" id="email" name="email" required>
        </div>

        <div class="form-group">
          <label for="telemovel">Telemóvel:</label>
          <input type="number" class="form-control" id="telemovel" name="telemovel" maxlength="9" required>
        </div>

        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" class="form-control" id="username" name="username" required>
        </div>

        <div class="form-group">
          <label for="pass_word">Password:</label>
          <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="form-group">
          <label for="idPT">Escolher PT:</label>
          <select class="form-control" id="idPT" name="idPT" required>
            <% 
            GestorSBD gestor = new GestorSBD();
                HashMap<Integer, String> pts = GestorSBD.getPTs();
                for (Map.Entry<Integer, String> entry : pts.entrySet()) {
                  int ptID = entry.getKey();
                  String ptName = entry.getValue();
            %>
                  <option value="<%= ptID %>"><%= ptName %></option>
            <%
                }

            %>
          </select>
        </div>
        <input type="hidden" name="flag" value="criarConta">
        <button type="submit" class="btn btn-orange">Criar Conta</button>
      </form>
    </div>
  </div>
    <script>

    function validarNIF() {
      var nifInput = document.getElementById("nif");
      var nifValue = nifInput.value.trim();

      if (!/^\d{9}$/.test(nifValue)) {
        alert("NIF Inválido");
        nifInput.value = "";
        nifInput.focus();
        return false;
      }
      return true;
    }

    function validarTelemovel() {
      var phoneInput = document.getElementById("telemovel");
      var phoneValue = phoneInput.value.trim();
      if (!/^\d{9}$/.test(phoneValue)) {
        alert("Número de telemóvel inválido");
        phoneInput.value = "";
        phoneInput.focus();
        return false;
      }
      return true;
    }

    function validarFormulario() {
      return validarNIF() && validarTelemovel();
    }
  </script>
</body>

</html>
