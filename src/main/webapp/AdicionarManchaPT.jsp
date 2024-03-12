<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="util.ManchaPT"%>
<%@ page import="java.util.*"%>
<%@ page import="classes.GestorSBD"%>
<%@ page import="java.sql.Time"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Adicionar ManchaPT</title>
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
        <a href="PT?flag=logout" class="btn btn-secondary logout-btn">Logout</a>
    </div>

    <div class="wrapper">
        <div class="title" style="margin-top: 100px;">FITNESS TIME</div>
        <div class="center-container">
            <h2 style="text-align: center;">Nova Mancha</h2>
            <form action="PTServlet" method="post" style="text-align: center;" onsubmit="encodeSpecialCharacters()">
                <div class="form-group">
                    <label for="diaSemana">Dia de Semana:</label>
                    <select class="form-control" id="diaSemana" name="diaSemana">
                        <option value="Segunda-Feira">Segunda-Feira</option>
                        <option value="Terça-Feira">Terça-Feira</option>
                        <option value="Quarta-Feira">Quarta-Feira</option>
                        <option value="Quinta-Feira">Quinta-Feira</option>
                        <option value="Sexta-Feira">Sexta-Feira</option>
                        <option value="Sábado">Sábado</option>
                        <option value="Domingo">Domingo</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="horaInicio">Hora de Inicio:</label>
                    <input type="time" class="form-control" id="horaInicio" name="horaInicio" required>
                </div>
                <div class="form-group">
                    <label for="horaFim">Hora de Fim:</label>
                    <input type="time" class="form-control" id="horaFim" name="horaFim" required>
                </div>
                <div class="form-group">
                    <label for="idadeMin">Idade Minima:</label>
                    <input type="number" class="form-control" id="idadeMin" name="idadeMin" required>
                </div>
                <div class="form-group">
                    <label for="idadeMax">Idade Máxima:</label>
                    <input type="number" class="form-control" id="idadeMax" name="idadeMax" required>
                </div>
                <input type="hidden" id="encodedDiaSemana" name="encodedDiaSemana">
                <input type="hidden" name="flag" value="adicionarManchaPT">
                <button type="submit" class="btn btn-primary create-activity-btn">Criar ManchaPT</button>
            </form>
        </div>
    </div>
        <script>
        //Codifica dia de semana
        document.querySelector('form').addEventListener('submit', function() {
            var diaSemanaInput = document.getElementById('diaSemana');
            var encodedDiaSemanaInput = document.getElementById('encodedDiaSemana');
            encodedDiaSemanaInput.value = encodeURIComponent(diaSemanaInput.value);
        });
  </script>
</body>

</html>
