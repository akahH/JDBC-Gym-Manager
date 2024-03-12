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
    <div class="center-container">
      <div class="rooms-container">
        <h2 style="text-align: center;">Salas do Clube</h2>
        <table class="rooms-table">
          <tbody>
            <%
            int clubeID = 1;
			GestorSBD gestor = new GestorSBD();
            Map<String, HashMap<String, String>> salas = gestor.getSala(clubeID);
            List<String> salasOrdenadas = new ArrayList<>(salas.keySet());
    
  
            for (String salaID : salasOrdenadas){
                HashMap<String, String> salaInfo = salas.get(salaID);
            %>
            <tr>
              <td class="edit-delete-col">
				<form action="GerenteServlet" method="post">
    				<input type="hidden" name="flag" value="consultarRecurso">
    				<input type="hidden" name="tipoRecurso" value="sala">
    				<input type="hidden" name="recursoID" value="<%= salaID %>">
    				<button type="submit" class="btn btn-primary create-resource-btn"><%= salaInfo.get("designacao") %></button>
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
          <tbody>
            <%
     
            Map<String, HashMap<String, String>> equipamentos = gestor.getEquipamentos(clubeID);
            List<String> equipamentosOrdenados = new ArrayList<>(equipamentos.keySet());
  
            for (String equipamentoID : equipamentosOrdenados){
                HashMap<String, String> equipamentoInfo = equipamentos.get(equipamentoID);
            %>
            <tr>
              <td class="edit-delete-col">
				<form action="GerenteServlet" method="post">
    				<input type="hidden" name="flag" value="consultarRecurso">
    				<input type="hidden" name="tipoRecurso" value="equipamento">
    				<input type="hidden" name="recursoID" value="<%= equipamentoID %>">
    				<button type="submit" class="btn btn-primary create-resource-btn"><%= equipamentoInfo.get("designacao") %></button>
			</form>
              </td>
            </tr>
            <%
              }
            %>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</body>

</html>