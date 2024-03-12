<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">

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
    <a href="GerenteServlet?flag=logout" class="btn btn-secondary logout-btn">Logout</a>
    <div class="center-container">
      <h2 class="text">Criar Perfil PT</h2>
      <form enctype="multipart/form-data" action="GerenteServlet" method="post">
        <div class="form-group">
          <label for="nome">Nome:</label>
          <input type="text" class="form-control" id="nome" name="nome" required>
        </div>

        <div class="form-group">
          <label for="apelido">Apelido:</label>
          <input type="text" class="form-control" id="apelido" name="apelido" required>
        </div>

        <div class="form-group">
          <label for="email">Email:</label>
          <input type="email" class="form-control" id="email" name="email" required>
        </div>

        <div class="form-group">
          <label for="telemovel">Telemóvel:</label>
          <input type="tel" class="form-control" id="telemovel" name="telemovel" required>
        </div>

        <div class="form-group">
          <label for="fotografia">Fotografia:</label>
          <input type="file" class="form-control" id="fotografia" name="fotografia" required>
        </div>

        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" class="form-control" id="username" name="username" required>
        </div>

        <div class="form-group">
          <label for="pass_word">Password:</label>
          <input type="password" class="form-control" id="password" name="password" required>
        </div>

        <input type="hidden" name="flag" value="criarPT">
        <button type="submit" class="btn btn-orange">Criar Perfil PT</button>
      </form>
    </div>
  </div>
</body>

</html>
