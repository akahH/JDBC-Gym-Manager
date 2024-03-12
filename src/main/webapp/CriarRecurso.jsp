<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="classes.GestorSBD" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Criar Recurso</title>
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
    <%
  String tipoRecurso = request.getParameter("tipoRecurso");
  String recursoID = request.getParameter("recursoID");
%>
    <a href="GerenteServlet?flag=logout" class="btn btn-secondary logout-btn">Logout</a>

    <div class="form-container">
      <h2 style="text-align: center; font-family: 'designer', sans-serif;">Novo Recurso</h2>
      <form enctype="multipart/form-data" action="GerenteServlet" method="post">
              <div class="form-group">
          <label for="tipoRecurso">Tipo de Recurso:</label>
          <select class="form-control" id="tipoRecurso" name="tipoRecurso" required>
            <option value="Sala">Sala</option>
            <option value="Equipamento">Equipamento</option>
          </select>
        </div>
        <div class="form-group">
          <label for="designacao">Nova Designação:</label>
          <input type="text" class="form-control" id="designacao" name="designacao" required>
        </div>
		
        <div class="form-group">
          <label for="estado">Novo Estado:</label>
          <select class="form-control" id="estado" name="estado" required>
            <option value="Disponivel">Disponível</option>
            <option value="Fora de Serviço">Fora de Serviço</option>
          </select>
        </div>

        <div class="form-group">
          <label for="tipoMultimedia">Tipo de Multimédia:</label>
          <select class="form-control" id="tipoMultimedia" name="tipoMultimedia" required>
            <option value="Fotografia">Fotografia</option>
            <option value="Video">Video</option>
          </select>
        </div>

        <div class="form-group">
          <label for="multimedia">Recursos Multimédia:</label>
          <input type="file" class="form-control" id="multimedia" name="multimedia">
        </div>

        <input type="hidden" name="flag" value="criarRecurso">
        <button type="submit" class="btn btn-orange">Criar Recurso</button>
      </form>
    </div>
  </div>
</body>

</html>