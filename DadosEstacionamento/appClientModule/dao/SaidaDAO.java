package dao;

import connection.ConnectionFactory;
import model.Saida;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SaidaDAO {

	public void inserir(Saida saida) throws SQLException {

		String sql = """
				INSERT INTO SAIDA
				(IDENTRADA,
				 DTSAIDA,
				 TEMPO_MINUTOS,
				 VALOR_TOTAL)
				VALUES (?, ?, ?, ?)
				""";

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setInt(1, saida.getIdEntrada());

		ps.setTimestamp(2, Timestamp.valueOf(saida.getDtSaida()));

		ps.setInt(3, saida.getTempoMinutos());

		ps.setDouble(4, saida.getValorTotal());

		ps.executeUpdate();

		ps.close();
		conn.close();
	}
}
