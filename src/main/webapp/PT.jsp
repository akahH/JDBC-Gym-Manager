<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="classes.GestorSBD" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>PT</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
  <link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body>
  <div class="fixed-header">
    <div class="home-link">
      <a href="PT.jsp">
		<i class="fas fa-home" style="margin-right:10px"></i>
        <span class="username"><%= (String) session.getAttribute("username") %></span>
      </a>
    </div>
    <a href="GerenteServlet?flag=logout" class="btn btn-secondary logout-btn">Logout</a>
  </div>
  <div class="wrapper">
   
    <div class="title">
      FITNESS TIME
    </div>
    
    <div class="center-container">
      <div class="pt-profile">
        <%
          HashMap<String, byte[]> perfilPT = GestorSBD.getPT((String) session.getAttribute("username"));
          Map.Entry<String, byte[]> pt = perfilPT.entrySet().iterator().next(); // Get the first entry
          String name = pt.getKey();
          byte[] photo = pt.getValue();
        %>
        <img src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(photo) %>" alt="PT Profile Photo">
        <div class="name">
          <%= name %>
        </div>
      </div>
      <p class="text">Escolha uma opção:</p>
      <a href="ManchasPT.jsp" class="btn btn-secondary btn-lg btn-block mb-3">Gerir Manchas de Disponibilidade</a>
      <a href="GerirClientesPT.jsp" class="btn btn-secondary btn-lg btn-block mb-3">Gerir Clientes</a>
    </div>
  </div>
</body>

</html>
