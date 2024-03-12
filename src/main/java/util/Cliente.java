package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;

public class Cliente {
    private int IDCliente;
    private int NIF;
    private String nome;
    private String apelido;
    private Date dataNasc;
    private String email;
    private int telemovel;
    private String username;
    private String pass_word;


    public Cliente() {
    }

    public Cliente(int NIF, String nome, String apelido, Date dataNasc, String email, int telemovel, String username, String pass_word) {
        this.NIF = NIF;
        this.nome = nome;
        this.apelido = apelido;
        this.dataNasc = dataNasc;
        this.email = email;
        this.telemovel = telemovel;
        this.username = username;
        this.pass_word = pass_word;
    }

    public int getIDCliente() {
        return IDCliente;
    }

    public void setIDCliente(int IDCliente) {
        this.IDCliente = IDCliente;
    }

    public int getNIF() {
        return NIF;
    }

    public void setNIF(int NIF) {
        this.NIF = NIF;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(int telemovel) {
        this.telemovel = telemovel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass_word() {
        return pass_word;
    }

    public void setPass_word(String pass_word) {
        this.pass_word = pass_word;
    }
    
	/**
	 * @param query - String que é comparado com o inicio
	 * @param nLetters - Quantidade de letras minimo exigido para preencher a lista
	 * @param nItems - Quantidade de sugestões devolvidas
	 * @return - Lista de items que satisfazem a query
	 */
	public static List<String> autocompleteClientes(String query, int nLetters, int nItems, List<Cliente> clientes) {
		List<String> nomeClientes =  new ArrayList<String>();
		for(Cliente cliente : clientes) {
			System.out.println(cliente.getNome() + " " + cliente.getApelido());
			nomeClientes.add(cliente.getNome() + " " + cliente.getApelido());
		}
		List<String> matched = new ArrayList<String>();
		if(query==null || query.length()<nLetters) // numero minimo de letras
			return matched;
		query = query.toLowerCase();
		for(int i=0; i<nomeClientes.size(); i++) {
			String item = nomeClientes.get(i).toLowerCase();
			if( item.toLowerCase().startsWith(query)) { 
				matched.add(nomeClientes.get(i));
			if( matched.size()>nItems)  // limita o numero de respostas
				break;
			}
		}
		System.out.println(Arrays.toString(matched.toArray()));
		return matched;
	}
}
