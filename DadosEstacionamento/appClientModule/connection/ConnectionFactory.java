package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private static final String URL = "jdbc:mysql://localhost:3306/DADOS_ESTACIONAMENTO";

	private static final String USUARIO = "root";

	private static final String SENHA = "root";

	public static Connection getConnection() {

		try {
			return DriverManager.getConnection(URL, USUARIO, SENHA);
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao conectar com o banco de dados", e);
		}

	}

}
