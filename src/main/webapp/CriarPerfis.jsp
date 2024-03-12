<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Gerente</title>
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
         <a href="GerenteServlet?flag=logout" class="btn btn-secondary logout-btn">Logout</a>
    <div class="center-container">

      <p class="text">Escolha uma opção:</p>
      <a href="CriarPerfilPT.jsp" class="btn btn-secondary btn-lg btn-block mb-3">Criar Perfil PT</a>
      <a href="CriarCliente.jsp" class="btn btn-secondary btn-lg btn-block mb-3">Criar Perfil Cliente</a>
      <a href="ImportProfileXML.jsp" class="btn btn-secondary btn-lg btn-block mb-3">Criar Perfis via XML</a>
      <a href="ExportProfileXML.jsp" class="btn btn-secondary btn-lg btn-block mb-3">Exportar Perfis via XML</a>
    </div>
  </div>
</body>

</html>