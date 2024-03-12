<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<%@ page import="classes.GestorSBD" %>
<%@ page import="util.Cliente" %>
<%@ page import="java.util.*" %>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Atualizar Perfil Cliente</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <link rel="stylesheet" type="text/css" href="mainCSS.css">

</head>

<body style="height:auto">
<div class="fixed-header" >
    <div class="home-link">
      <a href="Cliente.jsp">
		<i class="fas fa-home" style="margin-right:10px"></i>
        <span class="username"><%= (String) session.getAttribute("username") %></span>
      </a>
    </div>
    <a href="ClienteServlet?flag=logout" class="btn btn-secondary logout-btn">Logout</a>
  </div>
  <div class="wrapper">
        <div class="title" style="margin-top: 100px;">
            FITNESS TIME
        </div>
        <div class="center-container">
            <h2 class="text">Atualizar Perfil</h2>
            <form action="ClienteServlet" method="post" onsubmit="encodeSpecialCharacters()">

                <% String username = (String) session.getAttribute("username");
                   Cliente cliente = GestorSBD.getClienteUsername(username);
                %>

                <div class="form-group">
                    <label for="nome">Nome:</label>
                    <input type="text" class="form-control" id="nome" name="nome" value="<%= cliente.getNome() %>" required>
                </div>

                <div class="form-group">
                    <label for="apelido">Apelido:</label>
                    <input type="text" class="form-control" id="apelido" name="apelido" value="<%= cliente.getApelido() %>" required>
                </div>

                <div class="form-group">
                    <label for="dataNascimento">Data de Nascimento:</label>
                    <input type="text" class="form-control" id="dataNasc" name="dataNasc" value="<%= cliente.getDataNasc() %>" readonly>
                </div>

                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" class="form-control" id="email" name="email" value="<%= cliente.getEmail() %>" required>
                </div>
                <div class="form-group">
                    <label for="email">Telemóvel:</label>
                    <input type="number" class="form-control" id="telemovel" name="telemovel" value="<%= cliente.getTelemovel() %>" required>
                </div>
                <input type="hidden" name="flag" value="atualizarPerfil">
                <button type="submit" class="btn btn-orange">Atualizar Perfil</button>
            </form>
<h3>Patologias</h3>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Descrição</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int clienteID = cliente.getIDCliente();
                        List<String> patologias = GestorSBD.getPatologias(clienteID);
                        for (String descricao : patologias) {
                    %>
                            <tr>
                                <td><%=  descricao  %></td>
                                <td>
                                <form action="ClienteServlet" method="post" onsubmit="encodeSpecialCharacters()">
                                <input type="hidden" name="clienteID" value="<%= cliente.getIDCliente() %>">
                                <input type="hidden" name="descricaoPatologiaTabela" id="descricaoPatologiaTabela" value="<%= descricao %>">
                                <input type="hidden" name="flag" value="apagarPatologia">
                                <button type="submit" class="btn btn-danger">Apagar</button>
                                </form>
                                </td>
                            </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>

            <h3>Objetivos</h3>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Descrição</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<String> objetivos = GestorSBD.getObjetivos(clienteID);
                        for (String descricao : objetivos) {
                    %>
                            <tr>
                                <td><%= descricao %></td>
                                <td>                                
                                <form action="ClienteServlet" method="post" onsubmit="encodeSpecialCharacters()">
                                <input type="hidden" name="clienteID" value="<%= cliente.getIDCliente() %>">
                                <input type="hidden" name="descricaoObjetivoTabela" id="descricaoObjetivoTabela" value="<%= descricao %>">
                                <input type="hidden" name="flag" value="apagarObjetivo">
                                <button type="submit" class="btn btn-danger">Apagar</button>
                                </form></td>
                            </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>

            <h3>Adicionar Patologia</h3>
            <form action="ClienteServlet" method="post" onsubmit="encodeSpecialCharacters()">
                <div class="form-group">
                    <label for="patologiaDescricao">Descrição:</label>
                    <input type="text" class="form-control" id="patologiaDescricao" name="patologiaDescricao" required>
                </div>
                <input type="hidden" name="clienteID" value="<%= cliente.getIDCliente() %>">
                <input type="hidden" name="flag" value="criarPatologia">
                <button type="submit" class="btn btn-primary">Adicionar Patologia</button>
            </form>

            <h3>Adicionar Objetivo</h3>
            <form action="ClienteServlet" method="post" onsubmit="encodeSpecialCharacters()">
                <div class="form-group">
                    <label for="objetivoDescricao">Descrição:</label>
                    <input type="text" class="form-control" id="objetivoDescricao" name="objetivoDescricao" required>
                </div>
                <input type="hidden" name="clienteID" value="<%= cliente.getIDCliente() %>">
                <input type="hidden" name="flag" value="criarObjetivo">
                <button type="submit" class="btn btn-primary">Adicionar Objetivo</button>
            </form>
        </div>
           
        </div>

    <script>
    function encodeSpecialCharacters() {
      var nomeInput = document.getElementById('nome');
      var apelidoInput = document.getElementById('apelido');
      
      var objetivoDescricaoInput = document.getElementById('objetivoDescricao');
      var patologiaDescricaoInput = document.getElementById('patologiaDescricao');
      
      var objetivoDescricaoTabelaInput = document.getElementById('descricaoObjetivoTabela');
      var patologiaDescricaoTabelaInput = document.getElementById('descricaoPatologiaTabela');
      
      nomeInput.value = encodeURIComponent(nomeInput.value);
      apelidoInput.value = encodeURIComponent(apelidoInput.value);
      
      objetivoDescricaoInput.value = encodeURIComponent(objetivoDescricaoInput.value);
      patologiaDescricaoInput.value = encodeURIComponent(patologiaDescricaoInput.value);
      
      objetivoDescricaoTabelaInput.value = encodeURIComponent(objetivoDescricaoTabelaInput.value);
      patologiaDescricaoTabelaInput.value = encodeURIComponent(patologiaDescricaoTabelaInput.value);
    }
  </script>
</body>

</html>
