<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="classes.GestorSBD" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Consultar Recurso</title>
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
      <div class="image-list" style="width:50%">
      <%if(tipoRecurso.equals("sala")){ %>
      <h4><%= GestorSBD.getDesignacaoSala(Integer.parseInt(recursoID)) %></h4>
      <%} else if (  (tipoRecurso.equals("equipamento")) ) {%>

      	<h4><%= GestorSBD.getDesignacaoEquipamento(Integer.parseInt(recursoID)) %></h4>
      	<%} %>
        <table>
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

                <td style="width:auto;">
                  <img src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(multimedia) %>" alt="Multimedia" style="height:auto;width:70%;">
                </td>

              </tr>
            <%
              }
            %>
          </tbody>
        </table>
      </div>
    </div>
</body>

</html>