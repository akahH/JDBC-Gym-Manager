package classes;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.time.LocalDate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jakarta.servlet.http.Part;
import util.Atividade;
import util.Cliente;
import util.Data;
import util.ManchaPT;
import util.Recomendacao;
import util.RecursosReservados;
import util.SalaReservada;

public class GestorSBD {

	static Connection con;

	static Statement stmt;

	static ResultSet rs;

	static String database = "clubesFitness";
	static String serverName = "localhost";
	static String DRV = "com.mysql.cj.jdbc.Driver";
	static String URL = "jdbc:mysql://" + serverName + "/" + database
			+ "?useLegacyDatetimeCode=false&serverTimezone=Europe/Lisbon";
	static String USR = "root";
	static String PWD = "sn6352";

	static Document items = null;
	static String ficheiro = null;

	public GestorSBD() {
		makeConnection();
	}

	public static void main(String[] args) throws SQLException {

		con = getConn();
		// createCliente("270249540", "Miguel", "Ginga","1995-10-14",
		// "miguelginga@gmail.com","918051114", "ginga", "password01" );
		// loginGerente("admin", "password01");
		// horarioClube(1);
		// System.out.println(countMultimediaSala(1));
		// getAtividadesSemanais(1);
		// apagarManchaPT(11);
	}

	static Connection makeConnection() {
		try {
			Class.forName(DRV);
			System.out.println("Carregou o driver: " + DRV);
			con = DriverManager.getConnection(URL, USR, PWD);
			System.out.println("Connection to DB made");
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			System.err.println("database connection: " + ex.getMessage());

		}

		return con;
	}

	public static Connection getConn() {
		String databaseName = database;
		String url = null;
		Connection conn = null;
		try {
			url = "jdbc:mysql://" + serverName + "/" + databaseName
					+ "?useLegacyDatetimeCode=false&serverTimezone=Europe/Lisbon";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, USR, PWD);
			System.out.println("Connection successful..");
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found!");
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.err.println("Connection failed!");
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static boolean executeCommand(String table) {
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(table);
			return true;

		} catch (SQLException ex) {
			System.err.println("executeCommand: " + ex.getMessage());
		}

		return false;

	}

	/**
	 * createCliente Recebe os argumentos relativos a um cliente e acrescenta-o à
	 * tabela correspondente Quando criado o cliente é seleccionado também o PT
	 * correspondente e guardado na DB a associação
	 * 
	 * @throws SQLException
	 * @throws NumberFormatException
	 **/
	public static boolean criarCliente(String nifCliente, String nomeProprio, String apelido, String dataNasc,
			String email, String telemovel, String username, String password, int idPT)
			throws NumberFormatException, SQLException {

		String gdta = "INSERT INTO Cliente (NIF, nome, apelido, dataNasc, email, telemovel, username, pass_word) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = con.prepareStatement(gdta, Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, Integer.parseInt(nifCliente));
		pstmt.setString(2, nomeProprio);
		pstmt.setString(3, apelido);
		pstmt.setString(4, dataNasc);
		pstmt.setString(5, email);
		pstmt.setInt(6, Integer.parseInt(telemovel));
		pstmt.setString(7, username);
		pstmt.setString(8, password);

		if (pstmt.executeUpdate() > 0) {
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				int clienteID = generatedKeys.getInt(1);
				insertPTIDcliente(clienteID, idPT);
				return true;
			}
		}

		return false;
	}

	public static boolean criarCliente(String nifCliente, String nomeProprio, String apelido, String dataNasc,
			String email, String telemovel, String username, String password)
			throws NumberFormatException, SQLException {

		String gdta = "INSERT INTO Cliente (NIF, nome, apelido, dataNasc, email, telemovel, username, pass_word) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setInt(1, Integer.parseInt(nifCliente));
		pstmt.setString(2, nomeProprio);
		pstmt.setString(3, apelido);
		pstmt.setString(4, dataNasc);
		pstmt.setString(5, email);
		pstmt.setInt(6, Integer.parseInt(telemovel));
		pstmt.setString(7, username);
		pstmt.setString(8, password);

		if (pstmt.executeUpdate() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * método para criar a associação entre PT e cliente na BD
	 * 
	 * @param clienteID
	 * @param idPT
	 * @throws SQLException
	 */
	public static void insertPTIDcliente(int clienteID, int idPT) throws SQLException {
		String gdta = "INSERT INTO Cliente_PT (IDCliente, IDPT) VALUES (?, ?)";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setInt(1, clienteID);
		pstmt.setInt(2, idPT);
		pstmt.executeUpdate();

	}

	/**
	 * Método getClientes, retorna um mapa com os clientes
	 * 
	 * @return Mapa com a lista de clientes onde a chave é o ID e a entrada é a
	 *         junção do Nome com o Apelido
	 * @throws SQLException
	 */
	public static HashMap<Integer, String> getClientes() throws SQLException {
		HashMap<Integer, String> clientes = new HashMap<>();
		String sql = "SELECT * FROM Cliente";
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			int clienteID = rs.getInt("IDCliente");
			String nome = rs.getString("nome") + " " + rs.getString("apelido");
			clientes.put(clienteID, nome);
		}
		return clientes;
	}

	/**
	 * Método que retorna a lista dos nomes de PT
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static HashMap<Integer, String> getPTs() throws SQLException {
		HashMap<Integer, String> pts = new HashMap<>();
		stmt = con.createStatement();
		String sql = "SELECT * FROM PT";
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			int ptID = rs.getInt("IDPT");
			String nome = rs.getString("nome") + " " + rs.getString("apelido");
			pts.put(ptID, nome);
		}
		return pts;
	}

	/**
	 * Método para criar um PT
	 * 
	 * @param nome
	 * @param apelido
	 * @param email
	 * @param telemovel
	 * @param fotografiaPart
	 * @param username
	 * @param password
	 * @param clubeID
	 */
	public void criarPT(String nome, String apelido, String email, int telemovel, Part fotografiaPart, String username,
			String password, int clubeID) {
		try {

			String sql = "INSERT INTO PT (nome, apelido, email, telemovel, fotografia, username, pass_word) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			// converter o objeto fotografia de Part para um stream de bytes que possa ser
			// guardado na BD
			InputStream fotografiaStream = fotografiaPart.getInputStream();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nome);
			pstmt.setString(2, apelido);
			pstmt.setString(3, email);
			pstmt.setInt(4, telemovel);
			pstmt.setBinaryStream(5, fotografiaStream);
			pstmt.setString(6, username);
			pstmt.setString(7, password);
			pstmt.executeUpdate();
			associarPTaClube(username, clubeID);
		} catch (SQLException | IOException e) {
			e.printStackTrace();

		}
	}

	/**
	 * Método que retorna o ID do PT através do username
	 * 
	 * @param username
	 * @return
	 */
	public static int getIDPT(String username) {
		try {
			stmt = con.createStatement();
			String gdta = "SELECT IDPT FROM PT WHERE username='" + username + "';";
			rs = stmt.executeQuery(gdta);
			rs.next();
			return rs.getInt("IDPT");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Método que cria um PT recebendo uma fotografia em base64
	 * 
	 * @param nome
	 * @param apelido
	 * @param email
	 * @param telemovel
	 * @param fotografiaBytes
	 * @param username
	 * @param password
	 * @param clubeID
	 */
	public static void criarPTbase64(String nome, String apelido, String email, int telemovel, byte[] fotografiaBytes,
			String username, String password, int clubeID) {
		try {
			String sql = "INSERT INTO PT (nome, apelido, email, telemovel, fotografia, username, pass_word) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			InputStream fotografiaStream = new ByteArrayInputStream(fotografiaBytes);
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nome);
			pstmt.setString(2, apelido);
			pstmt.setString(3, email);
			pstmt.setInt(4, telemovel);
			pstmt.setBinaryStream(5, fotografiaStream);
			pstmt.setString(6, username);
			pstmt.setString(7, password);
			pstmt.executeUpdate();
			associarPTaClube(username, clubeID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que cria a associação na BD entre um PT e o clube
	 * 
	 * @param username
	 * @param clubeID
	 * @throws SQLException
	 */
	public static void associarPTaClube(String username, int clubeID) throws SQLException {
		String sql = "SELECT IDPT FROM PT WHERE username ='" + username + "';";
		rs = stmt.executeQuery(sql);
		rs.next();
		int ptID = rs.getInt("IDPT");
		executeCommand("INSERT INTO Clube_PT (IDClube, IDPT) VALUES (" + clubeID + "," + ptID + ");");

	}

	/**
	 * Método que retorna informação do PT para ser colocada na sua página JSP
	 * 
	 * @param username
	 * @return
	 */
	public static HashMap<String, byte[]> getPT(String username) {

		HashMap<String, byte[]> perfilPT = new HashMap<>();
		try {

			String sql = "SELECT nome, apelido, fotografia FROM PT WHERE username ='" + username + "';";
			rs = stmt.executeQuery(sql);

			if (rs.next()) {
				String nome = rs.getString("nome") + " " + rs.getString("apelido");
				byte[] fotografia = rs.getBytes("fotografia");
				perfilPT.put(nome, fotografia);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return perfilPT;
	}

	/**
	 * loginGerente Recebe um username e uma password e verifica se ambos coincidem
	 * 
	 * @param nome
	 * @return
	 */
	public boolean loginGerente(String username, String password) {
		try {
			stmt = con.createStatement();
			String gdta = "select * From Gerente WHERE username = '" + username + "'";
			rs = stmt.executeQuery(gdta);
			if (!rs.next())
				return false;
			else if (rs.getString("pass_word").equals(password))
				return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * loginPT Recebe um username e uma password e verifica se ambos coincidem
	 * 
	 * @param nome
	 * @return
	 */
	public boolean loginPT(String username, String password) {
		try {
			stmt = con.createStatement();
			String gdta = "select * From PT WHERE username = '" + username + "'";
			rs = stmt.executeQuery(gdta);
			if (!rs.next())
				return false;
			else if (rs.getString("pass_word").equals(password))
				return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * loginCliente Recebe um username e uma password e verifica se ambos coincidem
	 * 
	 * @param nome
	 * @return
	 */
	public boolean loginCliente(String username, String password) {
		try {
			stmt = con.createStatement();
			String gdta = "select * From Cliente WHERE username = '" + username + "'";
			rs = stmt.executeQuery(gdta);
			if (!rs.next())
				return false;
			else if (rs.getString("pass_word").equals(password))
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * horarioClube Carrega para um HashMap os horarios correspondentes a cada dia
	 * da semana de um determinado clube
	 **/
	public static HashMap<String, String[]> horarioClube(int idClube) {
		HashMap<String, String[]> horarioClube = new HashMap<>();
		try {
			stmt = con.createStatement();
			String gdta = "select * from horarioclube where IDClube=" + idClube;
			rs = stmt.executeQuery(gdta);
			while (rs.next()) {
				String diaSemana = rs.getString("diaSemana");
				String horaAbertura = rs.getString("horaAbertura");
				String horaFecho = rs.getString("horaFecho");

				String[] horario = { horaAbertura, horaFecho };
				horarioClube.put(diaSemana, horario);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return horarioClube;
	}

	/**
	 * getClubeID retorna o id do clube do gerente loggado através do seu username
	 */
	public static int getClubeID(String usernameGerente) {
		try {
			stmt = con.createStatement();
			String gdta = "SELECT Clube.IDClube\r\n" + "FROM Clube\r\n"
					+ "JOIN Gerente ON Clube.IDGerente = Gerente.IDGerente\r\n" + "WHERE Gerente.username = '"
					+ usernameGerente + "';";
			rs = stmt.executeQuery(gdta);
			rs.next();
			return rs.getInt("IDClube");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Método que altera a hora de abertura/fecho do clube num determinado dia
	 * 
	 * @param diaSemana
	 * @param horaAbertura
	 * @param horaFecho
	 * @param clubeID
	 * @return
	 */
	public boolean alteraHorarioClube(String diaSemana, String horaAbertura, String horaFecho, int clubeID) {

		return executeCommand("UPDATE HorarioClube SET horaAbertura = '" + horaAbertura + "', horaFecho = '" + horaFecho
				+ "'" + "WHERE IDClube = " + clubeID + " AND diaSemana = '" + diaSemana + "';");
	}

	public static HashMap<String, String> getContatosClube(int clubeID) {
		HashMap<String, String> contatosClube = new HashMap<>();
		try {
			stmt = con.createStatement();
			String gdta = "SELECT * FROM clube where IDClube=" + clubeID;
			rs = stmt.executeQuery(gdta);
			rs.next();
			contatosClube.put("email", rs.getString("email"));
			contatosClube.put("telefone", String.valueOf(rs.getString("telefone")));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return contatosClube;
	}

	/**
	 * Método para alterar o email do clube
	 * 
	 * @param clubeID
	 * @param email
	 * @return
	 */
	public boolean alterarEmailClube(int clubeID, String email) {

		return executeCommand("UPDATE Clube SET email = '" + email + "' WHERE IDClube = " + clubeID + ";");
	}

	/**
	 * Método para alterar o numero de telefone do clube
	 * 
	 * @param clubeID
	 * @param telefone
	 * @return
	 */
	public boolean alterarTelefoneClube(int clubeID, String telefone) {

		return executeCommand(
				"UPDATE Clube SET telefone = " + Integer.parseInt(telefone) + " WHERE IDClube = " + clubeID + ";");
	}

	/**
	 * método para criar uma sala no clube e recebe a multimédia correspondente
	 * 
	 * @param IDClube
	 * @param designacao
	 * @param estado
	 * @param multimedia
	 * @param tipoFicheiro
	 * @return
	 */
	public boolean criarSala(int IDClube, String designacao, String estado, Part multimedia, String tipoFicheiro) {

		String gdta = "INSERT INTO Sala (IDClube, designacao, estado) VALUES (?, ?, ?)";

		try (PreparedStatement pstmt = con.prepareStatement(gdta)) {
			pstmt.setInt(1, IDClube);
			pstmt.setString(2, designacao);
			pstmt.setString(3, estado);
			int rowsAffected = pstmt.executeUpdate();
			// se a inserção na tabela foi válida proceder para a inserção da multimédia
			if (rowsAffected > 0) {
				String sala = "SELECT * FROM sala where designacao='" + designacao + "'";
				rs = stmt.executeQuery(sala);
				rs.next();
				int salaID = rs.getInt("IDSala");
				setMultimedia(tipoFicheiro, "sala", salaID, multimedia);
			}
			return rowsAffected > 0;
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Método que criar um equipamento no clube
	 * 
	 * @param IDClube
	 * @param designacao
	 * @param estado
	 * @param multimedia
	 * @param tipoFicheiro
	 * @return
	 */
	public boolean criarEquipamento(int IDClube, String designacao, String estado, Part multimedia,
			String tipoFicheiro) {

		String gdta = "INSERT INTO Equipamento (IDClube, designacao, estado) VALUES (?, ?, ?)";

		try (PreparedStatement pstmt = con.prepareStatement(gdta)) {
			pstmt.setInt(1, IDClube);
			pstmt.setString(2, designacao);
			pstmt.setString(3, estado);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				String equipamento = "SELECT * FROM equipamento where designacao='" + designacao + "'";
				rs = stmt.executeQuery(equipamento);
				rs.next();
				int equipamentoID = rs.getInt("IDEquipamento");
				setMultimedia(tipoFicheiro, "equipamento", equipamentoID, multimedia);
			}
			return rowsAffected > 0;
		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Método para guardar ficheiros multimédia correspondentes a equipamentos e
	 * salas
	 * 
	 * @param tipoFicheiro
	 * @param tipoRecurso
	 * @param recursoID
	 * @param multimedia
	 * @return
	 */
	public boolean setMultimedia(String tipoFicheiro, String tipoRecurso, int recursoID, Part multimedia) {
		try (InputStream inputfile = multimedia.getInputStream()) {
			String gdta = "";
			if (tipoRecurso.equals("sala"))
				gdta = "INSERT INTO Multimedia (tipo, dados, IDSala ) VALUES (?,?,?)";
			if (tipoRecurso.equals("equipamento"))
				gdta = "INSERT INTO Multimedia (tipo, dados, IDEquipamento ) VALUES (?,?,?)";

			try (PreparedStatement pstmt = con.prepareStatement(gdta)) {
				pstmt.setString(1, tipoFicheiro);
				pstmt.setBinaryStream(2, inputfile);
				pstmt.setInt(3, recursoID);
				int rowsAffected = pstmt.executeUpdate();

				return rowsAffected > 0;
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método que retorna a lista de salas do clube juntamente com o seu estado
	 */
	public static HashMap<String, HashMap<String, String>> getSala(int clubeID) {
		// System.out.println("Clube ID: " + clubeID);
		HashMap<String, HashMap<String, String>> salas = new HashMap<>();
		try {
			stmt = con.createStatement();
			String gdta = "SELECT * FROM sala where IDClube=" + clubeID;
			rs = stmt.executeQuery(gdta);
			while (rs.next()) {
				HashMap<String, String> salaInfo = new HashMap<>();
				salaInfo.put("designacao", rs.getString("designacao"));
				salaInfo.put("estado", rs.getString("estado"));
				salas.put(String.valueOf(rs.getInt("IDSala")), salaInfo);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return salas;
	}

	/**
	 * Método que retorna a lista de Equipamentos do clube juntamente com a sua
	 * informação
	 * 
	 * @param clubeID
	 * @return
	 */
	public static HashMap<String, HashMap<String, String>> getEquipamentos(int clubeID) {

		HashMap<String, HashMap<String, String>> salas = new HashMap<>();
		try {
			stmt = con.createStatement();
			String gdta = "SELECT * FROM equipamento where IDClube=" + clubeID;
			rs = stmt.executeQuery(gdta);
			while (rs.next()) {
				HashMap<String, String> salaInfo = new HashMap<>();
				salaInfo.put("designacao", rs.getString("designacao"));
				salaInfo.put("estado", rs.getString("estado"));
				salas.put(String.valueOf(rs.getInt("IDEquipamento")), salaInfo);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return salas;
	}

	public boolean alterarSalaDesignacao(String designacao, int salaID) {
		return executeCommand("UPDATE Sala SET designacao = '" + designacao + "' where IDSala =" + salaID + " ;");
	}

	public boolean alterarSalaEstado(String estado, int salaID) {
		return executeCommand("UPDATE Sala SET estado = '" + estado + "' where IDSala =" + salaID + " ;");
	}

	public boolean alterarEquipamentoDesignacao(String designacao, int equipamentoID) {
		return executeCommand("UPDATE Equipamento SET designacao = '" + designacao + "' where IDEquipamento ="
				+ equipamentoID + " ;");
	}

	public boolean alterarEquipamentoEstado(String estado, int equipamentoID) {
		return executeCommand(
				"UPDATE Equipamento SET estado = '" + estado + "' where IDEquipamento =" + equipamentoID + " ;");
	}

	/**
	 * Método que retorna a quantidade de items multimédia associado a uma sala
	 * 
	 * @param salaID
	 * @return
	 */
	public static int countMultimediaSala(int salaID) {

		try {
			stmt = con.createStatement();
			String gdta = "SELECT COUNT(*) AS RecursosMultimedia\r\n" + "FROM Multimedia\r\n" + "WHERE IDSala = "
					+ salaID + ";";
			rs = stmt.executeQuery(gdta);
			rs.next();
			return rs.getInt("RecursosMultimedia");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Método que retorna a quantidade de items multimédia associado a um clube
	 * 
	 * @param equipamentoID
	 * @return
	 */
	public static int countMultimediaEquipamento(int equipamentoID) {

		try {
			stmt = con.createStatement();
			String gdta = "SELECT COUNT(*) AS RecursosMultimedia\r\n" + "FROM Multimedia\r\n" + "WHERE IDEquipamento = "
					+ equipamentoID + ";";
			rs = stmt.executeQuery(gdta);
			rs.next();
			return rs.getInt("RecursosMultimedia");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Método que retorna os bytes dos items multimedia e coloca-os numa lista para
	 * que posteriormente sejam convertidos e exibidos na página JSP
	 * 
	 * @param salaID
	 * @return
	 */
	public HashMap<Integer, byte[]> getDadosMultimediaSala(int salaID) {
		HashMap<Integer, byte[]> multimedia = new HashMap<>();

		try {
			stmt = con.createStatement();
			String gdta = "SELECT IDMultimedia, dados FROM Multimedia WHERE IDSala = " + salaID;
			rs = stmt.executeQuery(gdta);

			while (rs.next()) {
				int multimediaID = rs.getInt("IDMultimedia");
				byte[] multimediaData = rs.getBytes("dados");
				multimedia.put(multimediaID, multimediaData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return multimedia;
	}

	public HashMap<Integer, byte[]> getDadosMultimediaEquipamento(int equipamentoID) {
		HashMap<Integer, byte[]> multimedia = new HashMap<>();

		try {
			stmt = con.createStatement();
			String gdta = "SELECT IDMultimedia, dados FROM Multimedia WHERE IDEquipamento = " + equipamentoID;
			rs = stmt.executeQuery(gdta);

			while (rs.next()) {
				int multimediaID = rs.getInt("IDMultimedia");
				byte[] multimediaData = rs.getBytes("dados");
				multimedia.put(multimediaID, multimediaData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return multimedia;
	}

	public boolean apagarMultimedia(int multimediaID) {
		return executeCommand("DELETE FROM Multimedia WHERE	 IDMultimedia = " + multimediaID + " ;");
	}

	/**
	 * Método que criar uma lista de ManchaPT guardando todas as informações
	 * associadas a estas num determinado dia da semana
	 * 
	 * @param diaSemana
	 * @param idPT
	 * @return
	 * @throws SQLException
	 */
	public static List<ManchaPT> getManchaPT(String diaSemana, int idPT) throws SQLException {
		List<ManchaPT> manchasPT = new ArrayList<>();

		String gdta = "SELECT * FROM ManchaPT WHERE diaSemana='" + diaSemana + "' and IDPT =" + idPT + ";";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			ManchaPT manchaPT = new ManchaPT();
			manchaPT.setManchaID(rs.getInt("IDManchaPT"));
			manchaPT.setIDPT(rs.getInt("IDPT"));
			manchaPT.setDiaSemana(rs.getString("diaSemana"));
			manchaPT.setHoraInicio(rs.getString("horaInicio"));
			manchaPT.setHoraFim(rs.getString("horaFim"));
			manchaPT.setIdadeMax(rs.getInt("idadeMax"));
			manchaPT.setIdadeMin(rs.getInt("idadeMin"));
			manchasPT.add(manchaPT);
		}

		return manchasPT;
	}

	/**
	 * Método que retorna um objeto ManchaPT através do id da mancha
	 * 
	 * @param manchaID
	 * @return
	 * @throws SQLException
	 */
	public static ManchaPT getManchaPT(int manchaID) throws SQLException {

		String gdta = "SELECT * FROM ManchaPT WHERE IDManchaPT=" + manchaID + ";";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		ResultSet rs = pstmt.executeQuery();
		ManchaPT manchaPT = new ManchaPT();
		rs.next();
		manchaPT.setManchaID(rs.getInt("IDManchaPT"));
		manchaPT.setIDPT(rs.getInt("IDPT"));
		manchaPT.setDiaSemana(rs.getString("diaSemana"));
		manchaPT.setHoraInicio(rs.getString("horaInicio"));
		manchaPT.setHoraFim(rs.getString("horaFim"));
		manchaPT.setIdadeMax(rs.getInt("idadeMax"));
		manchaPT.setIdadeMin(rs.getInt("idadeMin"));
		return manchaPT;
	}

	/**
	 * Método que adiciona uma ManchaPT à BD
	 * 
	 * @param mancha
	 * @param clubID
	 * @return
	 */
	public boolean adicionarManchaPT(ManchaPT mancha, int clubID) {
		try {
			if (!validarHoras(mancha, clubID)) {
				String gdta = "INSERT INTO ManchaPT (IDPT, diaSemana, horaInicio, horaFim, idadeMin, idadeMax) "
						+ "VALUES (?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(gdta);
				pstmt.setInt(1, mancha.getIDPT());
				pstmt.setString(2, mancha.getDiaSemana());
				pstmt.setString(3, mancha.getHoraInicio());
				pstmt.setString(4, mancha.getHoraFim());
				pstmt.setInt(5, mancha.getIdadeMin());
				pstmt.setInt(6, mancha.getIdadeMax());

				int rowsAffected = pstmt.executeUpdate();
				return rowsAffected > 0;

			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método que apaga uma mancha e todas tabelas onde esta está referida
	 * 
	 * @param manchaID
	 * @throws SQLException
	 */
	public static void apagarManchaPT(int manchaID) throws SQLException {
		// linhas de instrução para apagar uma mancha, é necessário certificar que todos
		// os sitios onde esta esteja referida são apagados primeiro
		String[] instrucoes = { "DELETE FROM Equipamento_Reserva\r\n"
				+ "WHERE IDReserva IN (SELECT IDReserva FROM Reserva WHERE IDAti IN (SELECT IDAti FROM Atividade WHERE IDManchaPT ="
				+ manchaID + " ));\r\n",
				"DELETE FROM Sala_Reserva\r\n"
						+ "WHERE IDReserva IN (SELECT IDReserva FROM Reserva WHERE IDAti IN (SELECT IDAti FROM Atividade WHERE IDManchaPT = "
						+ manchaID + "));",
				"SET SQL_SAFE_UPDATES = 0;",
				"DELETE FROM Atividade_Cliente\r\n"
						+ "WHERE IDCliente IN (SELECT IDCliente FROM Atividade WHERE IDManchaPT = " + manchaID + ");",
				"SET SQL_SAFE_UPDATES = 1;",
				"DELETE FROM Reserva\r\n" + "WHERE IDAti IN (SELECT IDAti FROM Atividade WHERE IDManchaPT = " + manchaID
						+ ");",
				"DELETE FROM Atividade WHERE IDManchaPT =" + manchaID + ";",
				"DELETE FROM ManchaPT WHERE IDManchaPT =" + manchaID + ";" };

		for (String instrucao : instrucoes) {
			System.out.println(instrucao);
			stmt = con.createStatement();
			stmt.executeUpdate(instrucao);

		}
	}

	/**
	 * Método que retorna o id do clube do PT recebido em parametro
	 * 
	 * @param idPT
	 * @return
	 * @throws SQLException
	 */
	public static int getPTClubeID(int idPT) throws SQLException {
		stmt = con.createStatement();
		String gdta = "SELECT IDClube FROM Clube_PT WHERE IDPT = " + idPT;
		rs = stmt.executeQuery(gdta);
		rs.next();
		int clubeID = rs.getInt("IDClube");
		return clubeID;
	}

	/**
	 * Método que verifica se uma mancha tem uma atividade associada
	 * 
	 * @param manchaID
	 * @return
	 * @throws SQLException
	 */
	public static boolean checkIfManchaHasAtividade(int manchaID) throws SQLException {
		String gtda = "SELECT * FROM Atividade WHERE IDManchaPT=" + manchaID;
		rs = stmt.executeQuery(gtda);
		if (rs.next())
			return true;
		else
			return false;
	}

	/**
	 * Método que retorna a deseignacao da Atividade associada a uma mancha
	 * 
	 * @param manchaID
	 * @return
	 * @throws SQLException
	 */
	public static String getDesignacaoAtividadeOfMancha(int manchaID) throws SQLException {
		String gtda = "SELECT * FROM Atividade WHERE IDManchaPT=" + manchaID;
		rs = stmt.executeQuery(gtda);
		rs.next();
		return rs.getString("Designacao");
	}

	/**
	 * Método que valida se as horas da nova mancha não estão sobrepostas a outras
	 * manchas do PT e se estão dentro das horas de funcionamento do clube
	 * 
	 * @param newMancha
	 * @param clubID
	 * @return
	 * @throws SQLException
	 */
	private boolean validarHoras(ManchaPT newMancha, int clubID) throws SQLException {

		String horasDeManchas = "SELECT * FROM ManchaPT WHERE IDPT = ? AND diaSemana = ? AND "
				+ "((horaInicio <= ? AND horaFim >= ?) OR (horaInicio <= ? AND horaFim >= ?))";

		String horasDoClube = "SELECT * FROM HorarioClube WHERE IDClube = ? AND diaSemana = ? "
				+ "AND ? >= horaAbertura AND ? <= horaFecho";

		PreparedStatement pstmtManchas = con.prepareStatement(horasDeManchas);
		pstmtManchas.setInt(1, newMancha.getIDPT());
		pstmtManchas.setString(2, newMancha.getDiaSemana());
		pstmtManchas.setString(3, newMancha.getHoraInicio());
		pstmtManchas.setString(4, newMancha.getHoraInicio());
		pstmtManchas.setString(5, newMancha.getHoraFim());
		pstmtManchas.setString(6, newMancha.getHoraFim());

		PreparedStatement pstmtClube = con.prepareStatement(horasDoClube);
		pstmtClube.setInt(1, clubID);
		pstmtClube.setString(2, newMancha.getDiaSemana());
		pstmtClube.setString(3, newMancha.getHoraInicio());
		pstmtClube.setString(4, newMancha.getHoraFim());
		return pstmtManchas.executeQuery().next() && !pstmtClube.executeQuery().next();

	}

	/**
	 * Método que cria uma atividade para uma determinada ManchaPT
	 * 
	 * @param idManchaPT
	 * @param designacao
	 * @param tipo
	 * @param tipoParticipantes
	 * @param partMin
	 * @param partMax
	 * @param dataReal
	 * @param confirmacaoPT
	 * @return
	 */
	public static boolean criarAtividade(int idManchaPT, String designacao, String tipo, String tipoParticipantes,
			int partMin, int partMax, String dataReal, String confirmacaoPT) {
		try {
			String gdta = "INSERT INTO Atividade (IDManchaPT, designacao, tipo, tipoParticipantes, partMin, partMax, dataReal, confirmacaoPT) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(gdta);
			pstmt.setInt(1, idManchaPT);
			pstmt.setString(2, designacao);
			pstmt.setString(3, tipo);
			pstmt.setString(4, tipoParticipantes);
			pstmt.setInt(5, partMin);
			pstmt.setInt(6, partMax);
			pstmt.setString(7, dataReal);
			pstmt.setString(8, confirmacaoPT);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método que verifica se o dia de semana escolhido para a mancha é válido sendo
	 * que o PT tem 48h para fazer uma confirmação, a mancha deve ser criada com
	 * pelo menos 72h antes da realização da mesma
	 * 
	 * @param idManchaPT
	 * @return
	 * @throws SQLException
	 */
	public static boolean tipoSemanalValido(int idManchaPT) throws SQLException {

		String gdta = "SELECT diaSemana FROM ManchaPT WHERE IDManchaPT = ?";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setInt(1, idManchaPT);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			String diaSemana = rs.getString("diaSemana");
			// converte a string para o enum DayOfWeek de java.time na classe auxiliar Data
			DayOfWeek diaMancha = Data.covertDiaSemanaToDayOfWeek(diaSemana);
			// System.out.println("diaMancha :" + diaMancha);
			// retorna o dia de semana atual
			DayOfWeek diaCurr = LocalDate.now().getDayOfWeek();
			// System.out.println("curr Dia :" + diaCurr);
			// System.out.println(diaCurr.plus(3));
			// System.out.println((diaMancha.compareTo(diaCurr)));
			// confirma se existem pelo menos 72h entre os dias e ao mesmo tempo evitando
			// valores negativos
			return diaMancha.compareTo(diaCurr) >= 3 || diaMancha.compareTo(diaCurr) < 0;
		}
		return false;
	}

	/**
	 * Método que retorna o dia da semana de uma determianda mancha
	 * 
	 * @param idManchaPT
	 * @return
	 * @throws SQLException
	 */
	public static String diaSemanalMancha(int idManchaPT) throws SQLException {

		String gdta = "SELECT diaSemana FROM ManchaPT WHERE IDManchaPT = ?";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setInt(1, idManchaPT);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			String diaSemana = rs.getString("diaSemana");
			return diaSemana;
		}
		return null;
	}

	/**
	 * Retorna o dia da semana de uma mancha em Enum java.time DayOfWeek
	 * 
	 * @param idManchaPT
	 * @return
	 * @throws SQLException
	 */
	public static DayOfWeek getDayOfWeekMancha(int idManchaPT) throws SQLException {

		String gdta = "SELECT diaSemana FROM ManchaPT WHERE IDManchaPT = ?";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setInt(1, idManchaPT);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			String diaSemana = rs.getString("diaSemana");
			return Data.covertDiaSemanaToDayOfWeek(diaSemana);
		}
		return null;
	}

	/**
	 * Retorna um objeto do tipo RecursosReservados que contém a lista de todos os
	 * elementos reservados no dia da semana e data introduzidos
	 * 
	 * @param idManchaPT
	 * @param dataReal
	 * @param diaSemana
	 * @return
	 * @throws SQLException
	 */
	public static RecursosReservados getRecursosReservados(int idManchaPT, String dataReal, String diaSemana)
			throws SQLException {

		List<Integer> idSalas = new ArrayList<>();
		List<Integer> idEquipamentos = new ArrayList<>();
		String gtda = "SELECT * FROM ManchaPT WHERE IDManchaPT = ?";
		PreparedStatement psmtHorasMancha = con.prepareStatement(gtda);
		psmtHorasMancha.setInt(1, idManchaPT);
		ResultSet horasMancha = psmtHorasMancha.executeQuery();
		horasMancha.next();
		String horaInicio = horasMancha.getString("horaInicio");
		String horaFim = horasMancha.getString("horaFim");

		// conforme a tabela se tem dia de semana, a data é null, se tem data o dia de
		// semana é null
		if (diaSemana != null) {
			// query para verificar não só no dia de semana introduzido, mas também nas
			// horas de realização da mancha
			String checkReservaQuery = "SELECT r.IDReserva, er.IDEquipamento, sr.IDSala FROM Reserva r "
					+ "JOIN Equipamento_Reserva er ON r.IDReserva = er.IDReserva "
					+ "JOIN Sala_Reserva sr ON r.IDReserva = sr.IDReserva "
					+ "WHERE r.diaSemana = ? AND ((r.horaInicio <= ? AND r.horaFim >= ?)"
					+ "OR (r.horaInicio <= ? AND r.horaFim >= ?))";

			PreparedStatement pstmt = con.prepareStatement(checkReservaQuery);
			pstmt.setString(1, diaSemana);
			pstmt.setString(2, horaInicio);
			pstmt.setString(3, horaInicio);
			pstmt.setString(4, horaFim);
			pstmt.setString(5, horaFim);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				idEquipamentos.add(rs.getInt("IDEquipamento"));
				idSalas.add(rs.getInt("IDSala"));
			}

		} else if (!dataReal.equals(null)) {
			String checkReservaQuery = "SELECT r.IDReserva, er.IDEquipamento, sr.IDSala FROM Reserva r "
					+ "JOIN Equipamento_Reserva er ON r.IDReserva = er.IDReserva "
					+ "JOIN Sala_Reserva sr ON r.IDReserva = sr.IDReserva "
					+ "WHERE r.dataReal = ? AND ((r.horaInicio <= ? AND r.horaFim >= ?)"
					+ "OR (r.horaInicio <= ? AND r.horaFim >= ?))";

			PreparedStatement pstmt = con.prepareStatement(checkReservaQuery);
			pstmt.setString(1, dataReal);
			pstmt.setString(2, horaInicio);
			pstmt.setString(3, horaInicio);
			pstmt.setString(4, horaFim);
			pstmt.setString(5, horaFim);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				idEquipamentos.add(rs.getInt("IDEquipamento"));
				idSalas.add(rs.getInt("IDSala"));
			}
		}

		return new RecursosReservados(idSalas, idEquipamentos); // If next is true, there is a conflicting reservation
	}

	/**
	 * Método que retorna os recursos reservados para uma determinada atividade
	 * 
	 * @param atividadeID
	 * @return
	 * @throws SQLException
	 */
	public static RecursosReservados getRecursosReservadosForAtividade(int atividadeID) throws SQLException {
		List<Integer> idSalas = new ArrayList<>();
		List<Integer> idEquipamentos = new ArrayList<>();

		String salaQuery = "SELECT IDSala FROM Sala_Reserva WHERE IDReserva IN (SELECT IDReserva FROM Reserva WHERE IDAti = ?)";
		PreparedStatement salaPstmt = con.prepareStatement(salaQuery);
		salaPstmt.setInt(1, atividadeID);
		ResultSet salaResult = salaPstmt.executeQuery();

		while (salaResult.next()) {
			int idSala = salaResult.getInt("IDSala");
			idSalas.add(idSala);
		}

		String equipamentoQuery = "SELECT IDEquipamento FROM Equipamento_Reserva WHERE IDReserva IN (SELECT IDReserva FROM Reserva WHERE IDAti = ?)";
		PreparedStatement equipamentoPstmt = con.prepareStatement(equipamentoQuery);
		equipamentoPstmt.setInt(1, atividadeID);
		ResultSet equipamentoResult = equipamentoPstmt.executeQuery();

		while (equipamentoResult.next()) {
			int idEquipamento = equipamentoResult.getInt("IDEquipamento");
			idEquipamentos.add(idEquipamento);
		}

		return new RecursosReservados(idSalas, idEquipamentos);
	}

	/**
	 * Método que retorna o id da atividade da mancha recebida como parametro
	 * 
	 * @param manchaID
	 * @return
	 * @throws SQLException
	 */
	public static int getAtividadeID(int manchaID) throws SQLException {
		String gdta = "SELECT IDAti FROM Atividade WHERE IDManchaPT = ?";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setInt(1, manchaID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			int idAti = rs.getInt("IDAti");
			return idAti;
		}
		return 0;
	}

	/**
	 * Método para criar uma reserva com os equipamentos e sala identificados e
	 * disponiveis
	 * 
	 * @param idAtividade
	 * @param equipamentos
	 * @param idSala
	 * @param data
	 * @throws SQLException
	 */
	public static void criarReserva(int idAtividade, List<Integer> equipamentos, int idSala, boolean data)
			throws SQLException {
		String getManchaInfoQuery = "SELECT * FROM ManchaPT WHERE IDManchaPT = (SELECT IDManchaPT FROM Atividade WHERE IDAti = ?)";
		PreparedStatement pstmtMancha = con.prepareStatement(getManchaInfoQuery);
		pstmtMancha.setInt(1, idAtividade);
		ResultSet rsMancha = pstmtMancha.executeQuery();
		if (rsMancha.next()) {
			// se for uma atividade semanal
			if (!data) {
				String diaSemana = rsMancha.getString("diaSemana");
				String horaInicio = rsMancha.getString("horaInicio");
				String horaFim = rsMancha.getString("horaFim");
				String insertReservaQuery = "INSERT INTO Reserva (IDAti, diaSemana, horaInicio, horaFim) VALUES (?, ?, ?, ?)";
				PreparedStatement pstmtReserva = con.prepareStatement(insertReservaQuery,
						Statement.RETURN_GENERATED_KEYS);
				pstmtReserva.setInt(1, idAtividade);
				pstmtReserva.setString(2, diaSemana);
				pstmtReserva.setString(3, horaInicio);
				pstmtReserva.setString(4, horaFim);
				pstmtReserva.executeUpdate();

				ResultSet generatedKeys = pstmtReserva.getGeneratedKeys();
				if (generatedKeys.next()) {
					int idReserva = generatedKeys.getInt(1);
					String insertEquipamentoReservaQuery = "INSERT INTO Equipamento_Reserva (IDReserva, IDEquipamento) VALUES (?, ?)";
					PreparedStatement pstmtEquipamentoReserva = con.prepareStatement(insertEquipamentoReservaQuery);
					for (int idEquipamento : equipamentos) {
						pstmtEquipamentoReserva.setInt(1, idReserva);
						pstmtEquipamentoReserva.setInt(2, idEquipamento);
						pstmtEquipamentoReserva.executeUpdate();
					}

					if (idSala != 0) {
						String insertSalaReservaQuery = "INSERT INTO Sala_Reserva (IDReserva, IDSala) VALUES (?, ?)";
						PreparedStatement pstmtSalaReserva = con.prepareStatement(insertSalaReservaQuery);
						pstmtSalaReserva.setInt(1, idReserva);
						pstmtSalaReserva.setInt(2, idSala);
						pstmtSalaReserva.executeUpdate();
					}

				}
				// se for uma atividade numa determinada data
			} else {
				String ativData = "SELECT * FROM Atividade WHERE IDAti =" + idAtividade;
				ResultSet rs = stmt.executeQuery(ativData);
				rs.next();
				String dataReal = rs.getString("dataReal");
				String horaInicio = rsMancha.getString("horaInicio");
				String horaFim = rsMancha.getString("horaFim");
				String insertReservaQuery = "INSERT INTO Reserva (IDAti, dataReal, horaInicio, horaFim) VALUES (?, ?, ?, ?)";
				PreparedStatement pstmtReserva = con.prepareStatement(insertReservaQuery,
						Statement.RETURN_GENERATED_KEYS);
				pstmtReserva.setInt(1, idAtividade);
				pstmtReserva.setString(2, dataReal);
				pstmtReserva.setString(3, horaInicio);
				pstmtReserva.setString(4, horaFim);
				pstmtReserva.executeUpdate();

				ResultSet generatedKeys = pstmtReserva.getGeneratedKeys();
				if (generatedKeys.next()) {
					int idReserva = generatedKeys.getInt(1);
					String insertEquipamentoReservaQuery = "INSERT INTO Equipamento_Reserva (IDReserva, IDEquipamento) VALUES (?, ?)";
					PreparedStatement pstmtEquipamentoReserva = con.prepareStatement(insertEquipamentoReservaQuery);
					for (int idEquipamento : equipamentos) {
						pstmtEquipamentoReserva.setInt(1, idReserva);
						pstmtEquipamentoReserva.setInt(2, idEquipamento);
						pstmtEquipamentoReserva.executeUpdate();
					}

					if (idSala != 0) {
						String insertSalaReservaQuery = "INSERT INTO Sala_Reserva (IDReserva, IDSala) VALUES (?, ?)";
						PreparedStatement pstmtSalaReserva = con.prepareStatement(insertSalaReservaQuery);
						pstmtSalaReserva.setInt(1, idReserva);
						pstmtSalaReserva.setInt(2, idSala);
						pstmtSalaReserva.executeUpdate();
					}

				}
			}

		}
	}

	/**
	 * Método que retorna um objeto Atividade relativa à mancha recebida como
	 * parametro
	 * 
	 * @param idManchaPT
	 * @return
	 * @throws SQLException
	 */
	public static Atividade getAtividade(int idManchaPT) throws SQLException {

		String query = "SELECT * FROM Atividade WHERE IDManchaPT = ?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, idManchaPT);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int idAtiv = rs.getInt("IDAti");
		String designacao = rs.getString("designacao");
		String tipo = rs.getString("tipo");
		String tipoParticipantes = rs.getString("tipoParticipantes");
		int partMin = rs.getInt("partMin");
		int partMax = rs.getInt("partMax");
		java.sql.Date dataReal = rs.getDate("dataReal");
		String confirmacaoPT = rs.getString("confirmacaoPT");

		return new Atividade(idAtiv, idManchaPT, designacao, tipo, tipoParticipantes, partMin, partMax, dataReal,
				confirmacaoPT);
	}

	/**
	 * Método que retorna uma lista de objetos Cliente de um determinado PT
	 * 
	 * @param idPT
	 * @return
	 * @throws SQLException
	 */
	public static List<Cliente> getClientesPT(int idPT) throws SQLException {
		List<Cliente> clientes = new ArrayList<>();

		String gtda = "SELECT c.* FROM Cliente c INNER JOIN Cliente_PT cp ON c.IDCliente = cp.IDCliente "
				+ "WHERE cp.IDPT = ?";

		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setInt(1, idPT);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Cliente cliente = new Cliente();
			cliente.setIDCliente(rs.getInt("IDCliente"));
			cliente.setNIF(rs.getInt("NIF"));
			cliente.setNome(rs.getString("nome"));
			cliente.setApelido(rs.getString("apelido"));
			cliente.setDataNasc(rs.getDate("dataNasc"));
			clientes.add(cliente);
		}

		return clientes;
	}

	/**
	 * Método que retorna um Cliente dado o seu nome (nome + apelido)
	 * 
	 * @param nome
	 * @return
	 * @throws SQLException
	 */
	public static Cliente getCliente(String nome) throws SQLException {

		String gtda = "SELECT * FROM Cliente WHERE CONCAT(nome, ' ', apelido) = ?";
		System.out.println("Nome query: " + gtda);
		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setString(1, nome);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		Cliente cliente = new Cliente();
		cliente.setIDCliente(rs.getInt("IDCliente"));
		cliente.setNome(rs.getString("nome"));
		cliente.setApelido(rs.getString("apelido"));
		cliente.setDataNasc(rs.getDate("dataNasc"));
		cliente.setEmail(rs.getString("email"));
		return cliente;
	}

	/**
	 * Método que retorna um Cliente dado o seu username
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public static Cliente getClienteUsername(String username) throws SQLException {

		String gtda = "SELECT * FROM Cliente WHERE username = ?";
		System.out.println("Nome query: " + gtda);
		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setString(1, username);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		Cliente cliente = new Cliente();
		cliente.setIDCliente(rs.getInt("IDCliente"));
		cliente.setNome(rs.getString("nome"));
		cliente.setApelido(rs.getString("apelido"));
		cliente.setDataNasc(rs.getDate("dataNasc"));
		cliente.setEmail(rs.getString("email"));
		cliente.setTelemovel(rs.getInt("telemovel"));
		return cliente;
	}

	/**
	 * Método para atualizar o perfil de um cliente
	 * 
	 * @param username
	 * @param nome
	 * @param apelido
	 * @param email
	 * @param telemovel
	 * @return
	 * @throws SQLException
	 */
	public static boolean atualizarPerfilCliente(String username, String nome, String apelido, String email,
			String telemovel) throws SQLException {

		String sql = "UPDATE Cliente SET nome=?, apelido=?, email=?, telemovel=? WHERE username=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		System.out.println(nome);
		pstmt.setString(1, nome);
		pstmt.setString(2, apelido);
		pstmt.setString(3, email);
		pstmt.setString(4, telemovel);
		pstmt.setString(5, username);

		int rowsAffected = pstmt.executeUpdate();
		return rowsAffected > 0;

	}

	/**
	 * Método que retorna a lista dos clientes inscritos numa atividade
	 * 
	 * @param idAtividade
	 * @return
	 * @throws SQLException
	 */
	public static List<Cliente> getClientesAtividade(int idAtividade) throws SQLException {
		List<Cliente> clientes = new ArrayList<>();

		String gtda = "SELECT c.* " + "FROM Cliente c INNER JOIN Atividade_Cliente ac ON c.IDCliente = ac.IDCliente "
				+ "WHERE ac.IDAti = ?";

		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setInt(1, idAtividade);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Cliente cliente = new Cliente();
			cliente.setIDCliente(rs.getInt("IDCliente"));
			cliente.setNIF(rs.getInt("NIF"));
			cliente.setNome(rs.getString("nome"));
			cliente.setApelido(rs.getString("apelido"));
			cliente.setDataNasc(rs.getDate("dataNasc"));
			clientes.add(cliente);
		}

		return clientes;
	}

	/**
	 * Método addClienteToAtividade, adiciona um cliente a uma atividade, verifica
	 * no entanto se a atividade ainda tem vagas baseado no número de participantes
	 * e se o cliente tem idade dentro da faixa etária da Mancha
	 * 
	 * @param clienteName
	 * @param atividadeID
	 * @return
	 */
	public static boolean addClienteToAtividade(String nome, int manchaID) {
		try {
			// System.out.println("ManchaID: " + manchaID);
			Atividade atividade = getAtividade(manchaID);
			ManchaPT mancha = getManchaPT(manchaID);
			int atividadeID = atividade.getIdAti();
			// Primeiro verificar o ID do cliente baseado no seu nome
			System.out.println("Nome Cliente: " + nome);
			System.out.println("atividadeID: " + atividadeID);
			String clienteIDQuery = "SELECT IDCliente FROM Cliente WHERE CONCAT(nome, ' ', apelido) = ?";
			PreparedStatement clienteIDStmt = con.prepareStatement(clienteIDQuery);
			clienteIDStmt.setString(1, nome);
			ResultSet clienteIDResult = clienteIDStmt.executeQuery();

			if (clienteIDResult.next()) {

				int clienteID = clienteIDResult.getInt("IDCliente");
				System.out.println("clienteID: " + clienteID);
				// Verificar quantas vagas estão ocupadas
				String vagasOcupadasQuery = "SELECT COUNT(*) AS vagasOcupadas FROM Atividade_Cliente WHERE IDAti = ?";
				PreparedStatement vagasStmt = con.prepareStatement(vagasOcupadasQuery);
				vagasStmt.setInt(1, atividadeID);
				ResultSet vagasOcupadasResult = vagasStmt.executeQuery();
				vagasOcupadasResult.next();
				int vagasOcupadas = vagasOcupadasResult.getInt("vagasOcupadas");
				System.out.println("Vagas Ocupadas: " + vagasOcupadas);

				// Verificar o número de participantes máximo que a atividade permite
				String atividadeQuery = "SELECT * FROM Atividade WHERE IDAti = ?";
				PreparedStatement atividadeStmt = con.prepareStatement(atividadeQuery);
				atividadeStmt.setInt(1, atividadeID);
				ResultSet atividadeResult = atividadeStmt.executeQuery();
				atividadeResult.next();
				int partMax = atividadeResult.getInt("partMax");
				System.out.println("PartMax: " + partMax);
				if (vagasOcupadas < partMax && !validarHorasClienteAtividade(nome, mancha)) {

					String manchaPTQuery = "SELECT * FROM ManchaPT WHERE IDManchaPT = ?";
					PreparedStatement manchaPTStmt = con.prepareStatement(manchaPTQuery);
					manchaPTStmt.setInt(1, atividadeResult.getInt("IDManchaPT"));
					ResultSet manchaPTResult = manchaPTStmt.executeQuery();
					manchaPTResult.next();
					int idadeMin = manchaPTResult.getInt("idadeMin");
					int idadeMax = manchaPTResult.getInt("idadeMax");

					// Verificar a idade do cliente para determinar se está dentro da faixa etária
					String clienteQuery = "SELECT dataNasc FROM Cliente WHERE IDCliente = ?";
					PreparedStatement clienteStmt = con.prepareStatement(clienteQuery);
					clienteStmt.setInt(1, clienteID);
					ResultSet clienteResult = clienteStmt.executeQuery();
					clienteResult.next();
					LocalDate dataNasc = clienteResult.getDate("dataNasc").toLocalDate();
					System.out.println("Idade Cliente " + Data.idade(dataNasc));
					if (Data.idadeValida(dataNasc, idadeMin, idadeMax)) {

						String addClienteQuery = "INSERT INTO Atividade_Cliente (IDAti, IDCliente) VALUES (?, ?)";
						PreparedStatement addClienteStmt = con.prepareStatement(addClienteQuery);
						addClienteStmt.setInt(1, atividadeID);
						addClienteStmt.setInt(2, clienteID);
						addClienteStmt.executeUpdate();
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método que valida se o cliente não se encontra em escrito em atividades que
	 * se sobreponham à nova atividade que pretende subscrever
	 * 
	 * @param nome
	 * @param mancha
	 * @return
	 */
	public static boolean validarHorasClienteAtividade(String nome, ManchaPT mancha) {
		try {
			String gdta = "SELECT AC.* " + "FROM Atividade_Cliente ac " + "JOIN Atividade a ON ac.IDAti = a.IDAti "
					+ "JOIN ManchaPT m ON a.IDManchaPT = m.IDManchaPT "
					+ "WHERE ac.IDCliente = (SELECT IDCliente FROM Cliente WHERE CONCAT(nome, ' ', apelido) = ?) "
					+ "AND m.diaSemana = ? "
					+ "AND ((m.horaInicio <= ? AND m.horaFim >= ?) OR (m.horaInicio <= ? AND m.horaFim >= ?))";

			PreparedStatement pstmt = con.prepareStatement(gdta);
			pstmt.setString(1, nome);
			pstmt.setString(2, mancha.getDiaSemana());
			pstmt.setString(3, mancha.getHoraInicio());
			pstmt.setString(4, mancha.getHoraInicio());
			pstmt.setString(5, mancha.getHoraFim());
			pstmt.setString(6, mancha.getHoraFim());

			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método que cancela a inscrição de um cliente numa dada atividade
	 * 
	 * @param idAtividade
	 * @param idCliente
	 * @return
	 * @throws SQLException
	 */
	public static boolean cancelarInscricaoAtividade(int idAtividade, int idCliente) throws SQLException {

		String gdta = "DELETE FROM Atividade_Cliente WHERE IDAti = ? AND IDCliente = ?";
		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setInt(1, idAtividade);
		pstmt.setInt(2, idCliente);
		return pstmt.executeUpdate() > 0;
	}

	/**
	 * Método que verifica se uma ManchaPT tem vagas disponiveis
	 * 
	 * @param manchaID
	 * @return
	 */
	public static boolean manchaTemVagas(int manchaID) {
		try {
			Atividade atividade = getAtividade(manchaID);
			int atividadeID = atividade.getIdAti();

			String vagasOcupadasQuery = "SELECT COUNT(*) AS vagasOcupadas FROM Atividade_Cliente WHERE IDAti = ?";
			PreparedStatement vagasStmt = con.prepareStatement(vagasOcupadasQuery);
			vagasStmt.setInt(1, atividadeID);
			ResultSet vagasOcupadasResult = vagasStmt.executeQuery();
			vagasOcupadasResult.next();
			int vagasOcupadas = vagasOcupadasResult.getInt("vagasOcupadas");

			String atividadeQuery = "SELECT * FROM Atividade WHERE IDAti = ?";
			PreparedStatement atividadeStmt = con.prepareStatement(atividadeQuery);
			atividadeStmt.setInt(1, atividadeID);
			ResultSet atividadeResult = atividadeStmt.executeQuery();
			atividadeResult.next();
			int partMax = atividadeResult.getInt("partMax");

			return vagasOcupadas < partMax;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método que verifica se um cliente está registado numa determinada atividade
	 * 
	 * @param atividadeID
	 * @param clienteID
	 * @return
	 */
	public static boolean clienteRegistadoEmAtividade(int atividadeID, int clienteID) {
		try {
			String gdta = "SELECT * FROM Atividade_Cliente WHERE IDAti = ? AND IDCliente = ?";
			PreparedStatement stmt = con.prepareStatement(gdta);
			stmt.setInt(1, atividadeID);
			stmt.setInt(2, clienteID);

			ResultSet resultSet = stmt.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método para o PT confirmar uma atividade
	 * 
	 * @param idAtividade
	 * @return
	 */
	public static boolean confirmarAtividade(int idAtividade) {
		try {
			String gtda = "SELECT * FROM Atividade WHERE IDAti = ?";
			PreparedStatement pstmt = con.prepareStatement(gtda);
			pstmt.setInt(1, idAtividade);
			ResultSet rs = pstmt.executeQuery();
			// System.out.println("Enter confirmarAtividade");
			if (rs.next()) {
				if (rs.getDate("dataReal") != null) {
					LocalDate dataAtividade = rs.getDate("dataReal").toLocalDate();
					if (Data.confirmacaoPTData(dataAtividade)) {
						gtda = "UPDATE Atividade SET confirmacaoPT = 'Confirmado' WHERE IDAti = ?";
						pstmt = con.prepareStatement(gtda);
						pstmt.setInt(1, idAtividade);
						pstmt.executeUpdate();
						return true;
					}
				} else {
					int manchaID = rs.getInt("IDManchaPT");
					System.out.println("manchaID: " + manchaID);
					String manchaQuery = "SELECT * FROM ManchaPT WHERE IDManchaPT = ?";
					PreparedStatement pstmtMancha = con.prepareStatement(manchaQuery);
					pstmtMancha.setInt(1, manchaID);
					ResultSet rsMancha = pstmtMancha.executeQuery();
					rsMancha.next();
					if (rsMancha.getString("diaSemana") != null) {
						System.out.println("Enter diaSemana");
						String diaSemana = rsMancha.getString("diaSemana");
						DayOfWeek diaSemanaConvertido = Data.covertDiaSemanaToDayOfWeek(diaSemana);
						// confirma se o PT tem pelo menos 48h para confirmar a atividade
						if (Data.confirmacaoPTDiaSemana(diaSemanaConvertido)) {
							gtda = "UPDATE Atividade SET confirmacaoPT = 'Confirmado' WHERE IDAti = ?";
							PreparedStatement pstmtDiaSemana = con.prepareStatement(gtda);
							pstmtDiaSemana = con.prepareStatement(gtda);
							pstmtDiaSemana.setInt(1, idAtividade);
							pstmtDiaSemana.executeUpdate();
							return true;
						}

					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Método para o PT cancelar a atividade visto que não há aviso prévio pode
	 * cancelar sem validação de datas
	 * 
	 * @param atividadeID
	 * @throws SQLException
	 */
	public static void cancelarAtividade(int atividadeID) throws SQLException {

		String updateQuery = "UPDATE Atividade SET confirmacaoPT = 'Cancelado' WHERE IDAti = ?";
		PreparedStatement pstmt = con.prepareStatement(updateQuery);
		pstmt.setInt(1, atividadeID);
		pstmt.executeUpdate();

	}

	/**
	 * Método que retorna as recomendacoes de um determinado cliente usando o objeto
	 * Recomendacao
	 * 
	 * @param idCliente
	 * @return
	 * @throws SQLException
	 */
	public static List<Recomendacao> getRecomendacoesByCliente(int idCliente) throws SQLException {
		List<Recomendacao> recomendacoes = new ArrayList<>();

		String gtda = "SELECT * FROM Recomendacao WHERE IDCliente = ?";
		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setInt(1, idCliente);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int idPT = rs.getInt("IDPT");
			int idEquipamento = rs.getInt("IDEquipamento");
			String notas = rs.getString("notas");
			java.sql.Date dataInicio = rs.getDate("dataInicio");
			java.sql.Date dataFim = rs.getDate("dataFim");
			Recomendacao recomendacao = new Recomendacao(idPT, idCliente, idEquipamento, notas, dataInicio, dataFim);
			recomendacoes.add(recomendacao);
		}

		return recomendacoes;
	}

	/**
	 * Método que retorna a designacao de um Equipamento
	 * 
	 * @param equipamentoID
	 * @return
	 * @throws SQLException
	 */
	public static String getDesignacaoEquipamento(int equipamentoID) throws SQLException {
		String gtda = "SELECT * FROM Equipamento WHERE IDEquipamento = ?";
		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setInt(1, equipamentoID);
		ResultSet rs = pstmt.executeQuery();
		rs.next();

		return rs.getString("designacao");
	}

	/**
	 * Método que retorna a designacao de uma sala
	 * 
	 * @param salaID
	 * @return
	 * @throws SQLException
	 */
	public static String getDesignacaoSala(int salaID) throws SQLException {
		String gtda = "SELECT * FROM Sala WHERE IDSala = ?";
		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setInt(1, salaID);
		ResultSet rs = pstmt.executeQuery();
		rs.next();

		return rs.getString("designacao");
	}

	/**
	 * Método para criar uma Recomendacao para um determinado cliente usando um
	 * objeto do tipo Recomendacao
	 * 
	 * @param recomendacao
	 * @throws SQLException
	 */
	public static void createRecomendacao(Recomendacao recomendacao) throws SQLException {
		String insertQuery = "INSERT INTO Recomendacao (IDPT, IDCliente, IDEquipamento, notas, dataInicio, dataFim) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement pstmt = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, recomendacao.getIdPT());
		pstmt.setInt(2, recomendacao.getIdCliente());
		pstmt.setInt(3, recomendacao.getIdEquipamento());
		pstmt.setString(4, recomendacao.getNotas());
		pstmt.setDate(5, recomendacao.getDataInicio());
		pstmt.setDate(6, recomendacao.getDataFim());
		pstmt.executeUpdate();
		ResultSet generatedKeys = pstmt.getGeneratedKeys();
		generatedKeys.next();
		int idRecomendacao = generatedKeys.getInt(1);
		recomendacao.setIdRecomendacao(idRecomendacao);
	}

	/**
	 * Método que retorna as patologias de um cliente
	 * 
	 * @param clienteID
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getPatologias(int clienteID) throws SQLException {
		List<String> patologias = new ArrayList<>();

		String gtda = "SELECT descricao FROM Patologias WHERE IDCliente = ?";
		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setInt(1, clienteID);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			String descricao = rs.getString("descricao");
			patologias.add(descricao);
		}

		return patologias;
	}

	/**
	 * Método que retorna a lista de objetivos de um cliente
	 * 
	 * @param clienteID
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getObjetivos(int clienteID) throws SQLException {
		List<String> objetivos = new ArrayList<>();

		String gtda = "SELECT descricao FROM Objetivos WHERE IDCliente = ?";
		PreparedStatement pstmt = con.prepareStatement(gtda);
		pstmt.setInt(1, clienteID);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			String descricao = rs.getString("descricao");
			objetivos.add(descricao);
		}
		return objetivos;
	}

	/**
	 * Método para criar uma patologia
	 * 
	 * @param clienteID
	 * @param descricao
	 * @throws SQLException
	 */
	public static void criarPatologia(int clienteID, String descricao) throws SQLException {
		String query = "INSERT INTO Patologias (IDCliente, descricao) VALUES (?, ?)";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, clienteID);
		pstmt.setString(2, descricao);
		pstmt.executeUpdate();

	}

	/**
	 * Método para criar um objetivo
	 * 
	 * @param clienteID
	 * @param descricao
	 * @throws SQLException
	 */
	public static void criarObjetivo(int clienteID, String descricao) throws SQLException {
		String query = "INSERT INTO Objetivos (IDCliente, descricao) VALUES (?, ?)";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, clienteID);
		pstmt.setString(2, descricao);
		pstmt.executeUpdate();

	}

	/**
	 * Método para apagar uma patologia
	 * 
	 * @param clienteID
	 * @param descricao
	 * @throws SQLException
	 */
	public static void apagarPatologia(int clienteID, String descricao) throws SQLException {

		String query = "DELETE FROM Patologias WHERE IDCliente = ? AND descricao = ?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, clienteID);
		pstmt.setString(2, descricao);
		pstmt.executeUpdate();

	}

	/**
	 * Método para apagar um objetivo
	 * 
	 * @param idObjetivo
	 * @param descricao
	 * @throws SQLException
	 */
	public static void apagarObjetivo(int idCliente, String descricao) throws SQLException {
		String query = "DELETE FROM Objetivos WHERE IDCliente = ? AND descricao = ?";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1, idCliente);
		pstmt.setString(2, descricao);
		pstmt.executeUpdate();

	}

	/**
	 * Método que retorna uma lista das manchas que possuiem atividades individuais
	 * de um PT
	 * 
	 * @param idPT
	 * @param diaSemanaMancha dia da Semana da realização mancha
	 * @return
	 * @throws SQLException
	 */
	public static List<ManchaPT> getAtividadesIndividuais(int idPT, String diaSemanaMancha) throws SQLException {
		List<ManchaPT> manchaPTList = new ArrayList<>();

		String gdta = "SELECT m.* " + "FROM ManchaPT m " + "JOIN Atividade a ON m.IDManchaPT = a.IDManchaPT "
				+ "WHERE a.tipoParticipantes = 'individual' AND m.IDPT = ? AND diaSemana= ? ORDER BY horaInicio";

		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setInt(1, idPT);
		pstmt.setString(2, diaSemanaMancha);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int manchaID = rs.getInt("IDManchaPT");
			String diaSemana = rs.getString("diaSemana");
			String horaInicio = rs.getString("horaInicio");
			String horaFim = rs.getString("horaFim");
			int idadeMax = rs.getInt("idadeMax");
			int idadeMin = rs.getInt("idadeMin");
			ManchaPT manchaPT = new ManchaPT(manchaID, idPT, diaSemana, horaInicio, horaFim, idadeMax, idadeMin);
			manchaPTList.add(manchaPT);
		}

		return manchaPTList;
	}

	/**
	 * Método que retorna as manchas possuam atividades de grupo de todos os PT
	 * 
	 * @param diaSemanaMancha
	 * @return
	 * @throws SQLException
	 */
	public static List<ManchaPT> getAtividadesGrupo(String diaSemanaMancha) throws SQLException {
		List<ManchaPT> manchaPTList = new ArrayList<>();

		String gdta = "SELECT m.* " + "FROM ManchaPT m " + "JOIN Atividade a ON m.IDManchaPT = a.IDManchaPT "
				+ "WHERE a.tipoParticipantes = 'grupo' AND diaSemana= ?  ORDER BY horaInicio";

		PreparedStatement pstmt = con.prepareStatement(gdta);
		pstmt.setString(1, diaSemanaMancha);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int idPT = rs.getInt("IDPT");
			int manchaID = rs.getInt("IDManchaPT");
			String diaSemana = rs.getString("diaSemana");
			String horaInicio = rs.getString("horaInicio");
			String horaFim = rs.getString("horaFim");
			int idadeMax = rs.getInt("idadeMax");
			int idadeMin = rs.getInt("idadeMin");
			ManchaPT manchaPT = new ManchaPT(manchaID, idPT, diaSemana, horaInicio, horaFim, idadeMax, idadeMin);
			manchaPTList.add(manchaPT);
		}

		return manchaPTList;
	}

	/**
	 * Método que obtem o calendário de Semanal (da semana atual) para um cliente
	 * Retorna as atividades individuais do seu PT e de grupo de qualquer PT Retorna
	 * apenas as atividades pendentes ou confirmadas
	 * 
	 * @param idPT
	 * @param diaSemanaMancha
	 * @return
	 * @throws SQLException
	 */
	public static List<ManchaPT> getAtividadesSemanais(int idPT, String diaSemanaMancha) throws SQLException {
		List<ManchaPT> manchasPTSemana = new ArrayList<>();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		java.util.Date inicioSemana = calendar.getTime();
		calendar.add(Calendar.DAY_OF_WEEK, 6);
		java.util.Date fimSemana = calendar.getTime();

		String query = "SELECT m.* FROM ManchaPT m " + "JOIN Atividade a ON m.IDManchaPT = a.IDManchaPT "
				+ "WHERE (a.confirmacaoPT = 'Pendente' OR a.confirmacaoPT = 'Confirmado') "
				+ "AND ((a.tipo = 'Data' AND a.dataReal BETWEEN ? AND ? AND m.diaSemana= ?) OR (a.tipo = 'Semanal' AND m.diaSemana= ?))"
				+ "AND (a.tipoParticipantes != 'individual' OR (a.tipoParticipantes = 'individual' AND m.IDPT = ?))  ORDER BY horaInicio";

		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setDate(1, new java.sql.Date(inicioSemana.getTime()));
		pstmt.setDate(2, new java.sql.Date(fimSemana.getTime()));
		pstmt.setString(3, diaSemanaMancha);
		pstmt.setString(4, diaSemanaMancha);
		pstmt.setInt(5, idPT);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int manchaID = rs.getInt("IDManchaPT");
			System.out.println(manchaID);
			String diaSemana = rs.getString("diaSemana");
			String horaInicio = rs.getString("horaInicio");
			String horaFim = rs.getString("horaFim");
			int idadeMax = rs.getInt("idadeMax");
			int idadeMin = rs.getInt("idadeMin");
			int idPTmancha = rs.getInt("IDPT");
			ManchaPT manchaPT = new ManchaPT(manchaID, idPTmancha, diaSemana, horaInicio, horaFim, idadeMax, idadeMin);
			manchasPTSemana.add(manchaPT);
		}

		return manchasPTSemana;
	}

	/**
	 * Retorna o id do cliente recebido em parametro
	 * 
	 * @param clienteID
	 * @return
	 * @throws SQLException
	 */
	public static int getPTIDcliente(int clienteID) throws SQLException {
		stmt = con.createStatement();
		String gdta = "SELECT IDPT FROM Cliente_PT WHERE IDCliente = " + clienteID;
		rs = stmt.executeQuery(gdta);
		rs.next();
		int idPT = rs.getInt("IDPT");
		return idPT;
	}

	/**
	 * Retorna o nome do PT através do seu id
	 * 
	 * @param idPT
	 * @return
	 * @throws SQLException
	 */
	public static String getNomePT(int idPT) throws SQLException {
		stmt = con.createStatement();
		String gdta = "SELECT * FROM PT WHERE IDPT = " + idPT;
		rs = stmt.executeQuery(gdta);
		rs.next();
		String nome = rs.getString("nome");
		String apelido = rs.getString("apelido");
		return nome + " " + apelido;

	}

	/**
	 * Retorna um mapa com os 5 equipamentos menos usados, verificado através das
	 * suas reservas
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static LinkedHashMap<Integer, Integer> equipamentosMenosUsados() throws SQLException {
		// LinkedHashMap é um hashmap que mantém a ordem dos elementos conforme estes
		// são adicionados
		// é usado porque o query já trata da ordenação
		LinkedHashMap<Integer, Integer> equipamentosID = new LinkedHashMap<Integer, Integer>();

		String gdta = "SELECT e.IDEquipamento, COUNT(r.IDReserva) AS numReservas " + "FROM Equipamento e "
				+ "LEFT JOIN Equipamento_Reserva r ON e.IDEquipamento = r.IDEquipamento " + "GROUP BY e.IDEquipamento "
				+ "ORDER BY numReservas ASC " + "LIMIT 5";

		PreparedStatement pstmt = con.prepareStatement(gdta);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			int idEquipamento = rs.getInt("IDEquipamento");
			int numReservas = rs.getInt("numReservas");

			equipamentosID.put(idEquipamento, numReservas);
		}
		return equipamentosID;
	}

	/**
	 * Método que retorna uma lista de SalaReservada com todas as salas reservadas
	 * durante a semana atual
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static List<SalaReservada> getSalaOccupation() throws SQLException, ParseException {
		List<SalaReservada> salaReservadas = new ArrayList<>();

		String gtda = "SELECT\r\n" + "    sr.IDReserva AS SalaReservaID,\r\n" + "    sr.IDSala,\r\n" + "    r.*\r\n"
				+ "FROM\r\n" + "    Sala_Reserva sr\r\n" + "    JOIN Reserva r ON sr.IDReserva = r.IDReserva\r\n"
				+ "WHERE\r\n"
				+ "    (r.dataReal IS NULL OR (r.dataReal IS NOT NULL AND YEARWEEK(r.dataReal, 1) = YEARWEEK(CURDATE(), 1)))\r\n"
				+ "ORDER BY horaInicio;";

		PreparedStatement pstmt = con.prepareStatement(gtda);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int idSala = rs.getInt("IDSala");
			int idAti = rs.getInt("IDAti");
			String diaSemana = rs.getString("diaSemana");
			String data = rs.getString("dataReal");
			String horaInicio = rs.getString("horaInicio");
			String horaFim = rs.getString("horaFim");
			SalaReservada salaReservada = new SalaReservada(idSala, idAti, diaSemana, data, horaInicio, horaFim);
			salaReservadas.add(salaReservada);
		}

		return ordenarSalasOcupadas(salaReservadas);
	}

	/**
	 * Método auxiliar para ordernar as salas Ocupadas por dia da semana
	 * 
	 * @param salasReservadas
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static List<SalaReservada> ordenarSalasOcupadas(List<SalaReservada> salasReservadas)
			throws SQLException, ParseException {
		List<SalaReservada> salaReservadasOrdenadas = new ArrayList<>();
		String[] diasSemana = { "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira", "Sábado",
				"Domingo" };
		for (String diaSemana : diasSemana) {
			for (SalaReservada sala : salasReservadas) {
				if (!(sala.getDiaSemana() == null)) {
					if (sala.getDiaSemana().equals(diaSemana))
						salaReservadasOrdenadas.add(sala);
				} else if (sala.getDiaSemana() == null) {
					String dataReal = sala.getData();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					java.util.Date date = sdf.parse(dataReal);
					LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
					System.out.println(localDate);
					String diaSemanaData = Data.covertDayOfWeekToDiaSemana(localDate.getDayOfWeek());
					if (diaSemanaData.equals(diaSemana)) {
						sala.setDiaSemana(diaSemanaData);
						salaReservadasOrdenadas.add(sala);
					}

				}
			}
		}
		return salaReservadasOrdenadas;

	}

	/**
	 * Método processXMLStream, processa o stream de dados provenients do ficheiro
	 * importado em GerenteServlet e cosntrói um documento com estes de modo a serem
	 * novamente processados e inseridos na base de dados através de
	 * importClienteFromXML
	 * 
	 * @param xmlStream stream de dados processado em GerenteServlet
	 * @return
	 */
	public boolean processXMLStream(InputStream xmlStream, String tipoPerfil, int clubeID) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlStream);
			if (tipoPerfil.equals("cliente"))
				return importClienteFromXML(document);
			if (tipoPerfil.equals("pt"))
				return importPTFromXML(document, clubeID);
			else
				return false;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método importClienteFromXML, usa matéria já leccionada em IECD para recuperar
	 * o conteúdo referente a cada entrada no documento XML que posteriormente é
	 * inserido e processado para a base de dados com createCliente
	 * 
	 * @param XMLdoc documento com os dados a serem importados
	 * @return
	 */
	public static boolean importClienteFromXML(Document XMLdoc) {
		try {

			NodeList clientes = XMLdoc.getElementsByTagName("Cliente");
			for (int i = 0; i < clientes.getLength(); i++) {
				Element clienteElement = (Element) clientes.item(i);
				String nif = clienteElement.getAttribute("NIF");
				String nome = clienteElement.getElementsByTagName("Nome").item(0).getTextContent();
				String apelido = clienteElement.getElementsByTagName("Apelido").item(0).getTextContent();
				String dataNasc = clienteElement.getElementsByTagName("DataNasc").item(0).getTextContent();
				String email = clienteElement.getElementsByTagName("Email").item(0).getTextContent();
				String telemovel = clienteElement.getElementsByTagName("Telemovel").item(0).getTextContent();
				String username = clienteElement.getElementsByTagName("Username").item(0).getTextContent();
				String password = clienteElement.getElementsByTagName("Password").item(0).getTextContent();
				criarCliente(nif, nome, apelido, dataNasc, email, telemovel, username, password);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean importPTFromXML(Document XMLdoc, int clubeID) {
		try {
			NodeList pts = XMLdoc.getElementsByTagName("PT");
			for (int i = 0; i < pts.getLength(); i++) {
				Element ptElement = (Element) pts.item(i);
				String nome = ptElement.getElementsByTagName("Nome").item(0).getTextContent();
				String apelido = ptElement.getElementsByTagName("Apelido").item(0).getTextContent();
				String email = ptElement.getElementsByTagName("Email").item(0).getTextContent();
				String telemovel = ptElement.getElementsByTagName("Telemovel").item(0).getTextContent();
				String username = ptElement.getElementsByTagName("Username").item(0).getTextContent();
				String password = ptElement.getElementsByTagName("Password").item(0).getTextContent();

				String base64Photo = ptElement.getElementsByTagName("Fotografia").item(0).getTextContent();
				byte[] photoBytes = Base64.getDecoder().decode(base64Photo);

				criarPTbase64(nome, apelido, email, Integer.parseInt(telemovel), photoBytes, username, password,
						clubeID);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método que exporta o perfil de um cliente para XML, baseado em matéria
	 * leccionada em IECD
	 * 
	 * @param clienteID
	 * @return
	 */
	public static String exportClienteToXML(int clienteID) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Clientes");
			doc.appendChild(rootElement);

			ResultSet rs = stmt.executeQuery("SELECT * FROM Cliente where IDCliente=" + clienteID);
			while (rs.next()) {
				Element clienteElement = doc.createElement("Cliente");
				clienteElement.setAttribute("NIF", rs.getString("NIF"));
				childElement(doc, clienteElement, "Nome", rs.getString("nome"));
				childElement(doc, clienteElement, "Apelido", rs.getString("apelido"));
				childElement(doc, clienteElement, "DataNasc", rs.getString("dataNasc"));
				childElement(doc, clienteElement, "Email", rs.getString("email"));
				childElement(doc, clienteElement, "Telemovel", rs.getString("telemovel"));
				childElement(doc, clienteElement, "Username", rs.getString("username"));
				childElement(doc, clienteElement, "Password", rs.getString("pass_word"));

				rootElement.appendChild(clienteElement);
			}

			StringWriter stringWriter = new StringWriter();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));

			return stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String exportPTToXML(int ptID) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("PTs");
			doc.appendChild(rootElement);

			ResultSet rs = stmt.executeQuery("SELECT * FROM PT WHERE IDPT=" + ptID);
			while (rs.next()) {
				Element ptElement = doc.createElement("PT");
				ptElement.setAttribute("IDPT", rs.getString("IDPT"));
				childElement(doc, ptElement, "Nome", rs.getString("nome"));
				childElement(doc, ptElement, "Apelido", rs.getString("apelido"));
				childElement(doc, ptElement, "Email", rs.getString("email"));
				childElement(doc, ptElement, "Telemovel", rs.getString("telemovel"));
				byte[] photoBytes = rs.getBytes("fotografia");
				String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
				childElement(doc, ptElement, "Fotografia", base64Photo);
				childElement(doc, ptElement, "Username", rs.getString("username"));
				childElement(doc, ptElement, "Password", rs.getString("pass_word"));

				rootElement.appendChild(ptElement);
			}

			StringWriter stringWriter = new StringWriter();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));

			return stringWriter.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Método auxiliar para criar elementos XML e anexar ao parent Evita repetição
	 * do mesmo código no método exportClienteToXML
	 * 
	 * @param doc      documento XML a ser gerado
	 * @param parent   elemento parent neste caso Cliente
	 * @param tagName  tag do elemento associado a cliente, ex: nome, apelido,
	 *                 dataNasc, etc.
	 * @param conteudo conteudo do elemento, ex: Nome --> Miguel, Apelido ---> Ginga
	 */
	private static void childElement(Document doc, Element parent, String tagName, String conteudo) {
		Element element = doc.createElement(tagName);
		element.setTextContent(conteudo);
		parent.appendChild(element);
	}

}
