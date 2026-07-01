package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.ConnectionFactory;

public class VagaDAO {

	public Integer buscarVagaLivre() throws SQLException {

		String sql = """
				SELECT NUMEROVAGA
				FROM VAGA
				WHERE STATUSVAGA = 'LIVRE'
				ORDER BY NUMEROVAGA
				LIMIT 1
				""";

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();

		Integer vaga = null;

		if (rs.next()) {
			vaga = rs.getInt("NUMEROVAGA");
		}

		rs.close();
		ps.close();
		conn.close();

		return vaga;

	}

	public void ocuparVaga(int numeroVaga) throws SQLException {

		String sql = """
				UPDATE VAGA
				SET STATUSVAGA = 'OCUPADA'
				WHERE NUMEROVAGA = ?
				""";

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setInt(1, numeroVaga);

		ps.executeUpdate();

		ps.close();
		conn.close();

	}

	public void liberarVaga(int numeroVaga) throws SQLException {

		String sql = """
				UPDATE VAGA
				SET STATUSVAGA = 'LIVRE'
				WHERE NUMEROVAGA = ?
				""";

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setInt(1, numeroVaga);

		ps.close();
		conn.close();
	}
}
