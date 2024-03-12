<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="classes.GestorSBD" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Edit Resource</title>
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
      <h2 style="text-align: center; font-family: 'designer', sans-serif;">Editar Sala</h2>
      <form enctype="multipart/form-data" action="GerenteServlet" method="post">
        <div class="form-group">
          <label for="designacao">Nova Designação:</label>
          <input type="text" class="form-control" id="designacao" name="designacao">
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

        <input type="hidden" name="flag" value="processEdit">
        <input type="hidden" name="tipoRecurso" value="<%= request.getParameter("tipoRecurso") %>">
		<input type="hidden" name="recursoID" value="<%= request.getParameter("recursoID") %>">
		
        <button type="submit" class="btn btn-orange">Guardar Alterações</button>
      </form>

      <div class="image-list">
      <h4>Multimédia Associada</h4>
        <table class="multimedia-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Multimedia</th>
              <th>Apagar</th>
            </tr>
          </thead>
          <tbody>
            <%
              GestorSBD gestorSBD = new GestorSBD();
              HashMap<Integer, byte[]> multimediaMap = new HashMap<Integer, byte[]>();

              if ("sala".equals(tipoRecurso)) {
                multimediaMap = gestorSBD.getDadosMultimediaSala(Integer.parseInt(recursoID));
              } else if ("equipamento".equals(tipoRecurso)) {
                multimediaMap = gestorSBD.getDadosMultimediaEquipamento(Integer.parseInt(recursoID));
              }

              for (Map.Entry<Integer, byte[]> entry : multimediaMap.entrySet()) {
                int multimediaID = entry.getKey();
                byte[] multimedia = entry.getValue();
            %>
              <tr>
                <td><%= multimediaID %></td>
                <td>
                  <img src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(multimedia) %>" alt="Multimedia">
                </td>
                <td>
                  <form action="GerenteServlet" method="post" style="padding:0px">
                    <input type="hidden" name="flag" value="apagarMultimedia">
                    <input type="hidden" name="tipoRecurso" value="<%= tipoRecurso %>">
                    <input type="hidden" name="recursoID" value="<%= recursoID %>">
                    <input type="hidden" name="multimediaID" value="<%= multimediaID %>">
                    <button type="submit" class="btn btn-danger">Delete</button>
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

