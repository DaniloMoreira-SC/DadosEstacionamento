package teste;

import java.sql.Connection;

import connection.ConnectionFactory;

public class TestConnection {

	public static void main (String[] args) {
		
		try {
			
			Connection conn = ConnectionFactory.getConnection();
			
			System.out.println("Conexão realizada com sucesso!");
			
			conn.close();
			
		} catch(Exception e) {
			
			System.out.println("Erro ao conectar!");
			
			e.printStackTrace();
			
		}
	}

}
