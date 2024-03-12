<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="classes.GestorSBD" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Salas e Equipamentos do Clube</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body>
  <div class="wrapper">
    <div class="title" style="margin-top:100px;">
      FITNESS TIME
    </div>
    <a href="GerenteServlet?flag=logout" class="btn btn-secondary logout-btn">Logout</a>
    <div class="center-container">
      <div class="rooms-container">
        <h2 style="text-align: center;">Salas do Clube</h2>
        <table class="rooms-table">
          <thead>
            <tr>
              <th>ID Sala</th>
              <th>Designação</th>
              <th>Recursos Multimédia</th>
              <th>Estado</th>
              <th>Editar</th>
            </tr>
          </thead>
          <tbody>
            <%
            HttpSession sessao = request.getSession();
            String usernameGerente = (String) sessao.getAttribute("username");
            int clubeID = GestorSBD.getClubeID(usernameGerente);

            Map<String, HashMap<String, String>> salas = GestorSBD.getSala(clubeID);
            List<String> salasOrdenadas = new ArrayList<>(salas.keySet());
    
  
            for (String salaID : salasOrdenadas){
                HashMap<String, String> salaInfo = salas.get(salaID);
            %>
            <tr>
              <td><%= salaID %></td>
              <td><%= salaInfo.get("designacao") %></td>
              <td><%= GestorSBD.countMultimediaSala(Integer.parseInt(salaID)) %></td>
              <td><%= salaInfo.get("estado") %></td>
              <td class="edit-delete-col">
				<form action="GerenteServlet" method="post">
    				<input type="hidden" name="flag" value="editRecurso">
    				<input type="hidden" name="tipoRecurso" value="sala">
    				<input type="hidden" name="recursoID" value="<%= salaID %>">
    				<button type="submit" class="btn btn-primary create-resource-btn">Editar</button>
			</form>
              </td>
            </tr>
            <%
              }
            %>
          </tbody>
        </table>
      </div>
      <div class="equipment-container">
        <h2 style="text-align: center;">Equipamentos do Clube</h2>
        <table class="equipment-table">
          <thead>
            <tr>
              <th>ID Equipamento</th>
              <th>Designação</th>
              <th>Recursos Multimédia</th>
              <th>Estado</th>
              <th>Editar</th>
            </tr>
          </thead>
          <tbody>
            <%
     
            Map<String, HashMap<String, String>> equipamentos = GestorSBD.getEquipamentos(clubeID);
            List<String> equipamentosOrdenados = new ArrayList<>(equipamentos.keySet());
  
            for (String equipamentoID : equipamentosOrdenados){
                HashMap<String, String> equipamentoInfo = equipamentos.get(equipamentoID);
            %>
            <tr>
              <td><%= equipamentoID %></td>
              <td><%= equipamentoInfo.get("designacao") %></td>
              <td><%= GestorSBD.countMultimediaEquipamento(Integer.parseInt(equipamentoID))%></td>
              <td><%= equipamentoInfo.get("estado") %></td>
              <td class="edit-delete-col">
				<form action="GerenteServlet" method="post">
    				<input type="hidden" name="flag" value="editRecurso">
    				<input type="hidden" name="tipoRecurso" value="equipamento">
    				<input type="hidden" name="recursoID" value="<%= equipamentoID %>">
    				<button type="submit" class="btn btn-primary create-resource-btn">Editar</button>
			</form>
              </td>
            </tr>
            <%
              }
            %>
          </tbody>
        </table>
        <a href="CriarRecurso.jsp" class="btn btn-primary create-resource-btn">Criar Recurso</a>
      </div>
    </div>
  </div>
</body>

</html>
