<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login</title>
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
      <form id="loginForm" method="post">
        <p class="text">Faça login para continuar:</p>
        <div class="dropdown">
          <select class="form-control" id="role" name="role" onchange="updateFormAction()">
            <option value="Gerente">Gerente</option>
            <option value="PT">PT</option>
            <option value="Cliente">Cliente</option>
          </select>
        </div>
        <div class="input-group">
          <input type="text" class="form-control" name="username" placeholder="Nome de usuário">
        </div>
        <div class="input-group">
          <input type="password" class="form-control" name="password" placeholder="Password">
        </div>
        <input type="hidden" name="flag" value="login">
        <button type="submit" class="btn btn-secondary btn-lg btn-block mb-3">Entrar</button>
      </form>
      <a href="CriarCliente.jsp" class="btn btn-secondary btn-lg btn-block mb-3">Criar Conta Cliente</a>
    </div>
  </div>
</body>
<script>
  document.addEventListener('DOMContentLoaded', function() {
    updateFormAction();
    document.getElementById('role').addEventListener('change', updateFormAction);
  });

  function updateFormAction() {
    var role = document.getElementById('role').value;
    var form = document.getElementById('loginForm');
    var newAction;

    switch (role) {
      case "PT":
        newAction = "PTServlet";
        break;
      case "Cliente":
        newAction = "ClienteServlet";
        break;
      case "Gerente":
        newAction = "GerenteServlet";
        break;
      default:
        newAction = "GerenteServlet";
    }

    form.action = newAction;
  }
</script>



</html>

