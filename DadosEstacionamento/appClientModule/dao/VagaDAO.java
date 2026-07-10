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

		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			Integer vaga = null;

			if (rs.next()) {
				vaga = rs.getInt("NUMEROVAGA");
			}
			rs.close();
			return vaga;

		}
	}

	public void ocuparVaga(int numeroVaga) throws SQLException {

		String sql = """
				UPDATE VAGA
				SET STATUSVAGA = 'OCUPADA'
				WHERE NUMEROVAGA = ?
				""";

		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, numeroVaga);
			int linhas = ps.executeUpdate();

			if (linhas == 0) {
				System.out.println("⚠️ Aviso: Vaga " + numeroVaga + " não foi encontrada!");
			}

		}
	}

	public void liberarVaga(int numeroVaga) throws SQLException {

		String sql = """
				UPDATE VAGA
				SET STATUSVAGA = 'LIVRE'
				WHERE NUMEROVAGA = ?
				""";

		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, numeroVaga);
			int linhas = ps.executeUpdate();

			if (linhas > 0) {
				System.out.println("✅ Vaga " + numeroVaga + " LIBERADA com sucesso!");
			} else {
				System.out.println("❌ ERRO: Vaga " + numeroVaga + " não foi liberada!");
			}

		}
	}
}