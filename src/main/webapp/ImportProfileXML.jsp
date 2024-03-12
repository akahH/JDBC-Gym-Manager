<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Import Perfil XML</title>
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
      <form action="GerenteServlet" method="post" enctype="multipart/form-data">
        <div class="form-group">
          <label for="xmlFile">Escolha um ficheiro XML:</label>
          <input type="file" class="form-control" id="xmlFile" name="xmlFile" accept=".xml" required>
        </div>
        <div class="form-group">
          <label for="tipoPerfil">Tipo de Perfil:</label>
          <select class="form-control" id="tipoPerfil" name="tipoPerfil" required>
            <option value="pt">PT</option>
            <option value="cliente">Cliente</option>
          </select>
        </div>
        <input type="hidden" name="flag" value="importXML">
        <button type="submit" class="btn btn-secondary btn-lg btn-block mb-3">Importar Perfil via XML</button>
      </form>
    </div>
  </div>
</body>

</html>
