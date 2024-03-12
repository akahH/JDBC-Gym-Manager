<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="classes.GestorSBD" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dados Clube</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <link rel="stylesheet" type="text/css" href="mainCSS.css">
</head>

<body style="height:auto">
  <div class="wrapper">
    <div class="title">
      FITNESS TIME
    </div>
    <a href="GerenteServlet?flag=logout" class="btn btn-secondary logout-btn">Logout</a>
    <div class="center-container">
      <div class="schedule-container">
        <h2 style="text-align: center;">Horário do Clube</h2>
        <table class="schedule-table">
          <thead>
            <tr>
              <th>Dia da Semana</th>
              <th>Hora de Abertura</th>
              <th>Hora de Fecho</th>
            </tr>
          </thead>
          <tbody>
            <% 
              HttpSession sessao = request.getSession();
              String usernameGerente = (String) sessao.getAttribute("username");
              int clubeID = GestorSBD.getClubeID(usernameGerente);
              //lista com todos os dias da semana
              List<String> diasSemana = new ArrayList<>(List.of("Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado", "Domingo"));
              //ordenar a lista obtida do gestor na ordem correcta do decorrer de uma semana
              for (String dia : diasSemana) {
                String[] horario = GestorSBD.horarioClube(clubeID).get(dia);
                if (horario != null) {
            %>
            <tr>
              <td><%= dia %></td>
              <td><%= horario[0] %></td>
              <td><%= horario[1] %></td>
            </tr>
            <% }} %>
          </tbody>
        </table>
      </div>
      <div class="alterarHorario">
        <h4>Alterar Horário</h4>
        <form action="GerenteServlet" method="post" style="text-align: center;">
          <label for="dia">Escolha o Dia:</label>
          <select name="dia" id="dia">
            <option value="Segunda">Segunda</option>
            <option value="Terça">Terça</option>
            <option value="Quarta">Quarta</option>
            <option value="Quinta">Quinta</option>
            <option value="Sexta">Sexta</option>
            <option value="Sabado">Sabado</option>
            <option value="Domingo">Domingo</option>
          </select>

          <label for="horaAbertura">Nova Hora de Abertura:</label>
          <input type="time" name="horaAbertura" id="horaAbertura" required>

          <label for="horaFecho">Nova Hora de Fecho:</label>
          <input type="time" name="horaFecho" id="horaFecho" required>

          <input type="hidden" name="flag" value="alterarHorario">
          <button type="submit" class="btn btn-secondary">Alterar</button>
        </form>
      </div>

      <div class="orange-line"></div>

      <div class="contacts-container">
            </div>
      <div class="alterarContatos">
          <% 
        HashMap<String, String> contatosClube = GestorSBD.getContatosClube(clubeID);
        String telemovel = contatosClube.get("telefone");
        String email = contatosClube.get("email");
    %>
        <h2 style="text-align: center;">Contactos do Clube</h2>
    <p style="text-align: center;">Telefone: <%= telemovel %></p>
    <p style="text-align: center;">Email: <%= email %></p>

        <h4 style="text-align: center;">Alterar Contactos</h4>
        <form action="GerenteServlet" method="post" style="text-align: center;">
          <label for="tipoContacto">Escolha uma opção:</label>
          <select name="tipoContacto" id="tipoContacto">
            <option value="telefone">Telefone</option>
            <option value="email">Email</option>
          </select>

          <label for="novoContacto">Novo Contacto:</label>
          <input type="number" name="novoContacto" id="novoContacto" maxlength="9" required>

          <input type="hidden" name="flag" value="alterarContactos">
          <button type="submit" class="btn btn-secondary">Alterar</button>
        </form>
      </div>
    </div>
  </div>
</body>

</html>

