package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import connection.ConnectionFactory;
import model.Entrada;

public class EntradaDAO {

	public void inserir(Entrada entrada) throws SQLException {

		String sql = """
				INSERT INTO ENTRADA
				(NOMECLIENTE, PLACA, TIPOVEICULO, NUMEROVAGA)
				VALUES (?, ?, ?, ?)
				""";

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setString(1, entrada.getNomeCliente());

		ps.setString(2, entrada.getPlaca());

		ps.setString(3, entrada.getTipoVeiculo());

		ps.setInt(4, entrada.getNumeroVaga());

		ps.executeUpdate();

		ps.close();
		conn.close();

	}

	// METODO BUSCAR ENTRADA PELA PLACA

	public Entrada buscarEntradaAberta(String placa) throws SQLException {

		String sql = """
					SELECT
				      IDENTRADA
				    , NOMECLIENTE
				    , PLACA
				    , TIPOVEICULO
				    , NUMEROVAGA
				    , DTENTRADA
				    , STATUSVEICULO
				    , TIMESTAMPDIFF(MINUTE, DTENTRADA, NOW()) AS TEMPO_MINUTOS
				    , TIMEDIFF(NOW(), DTENTRADA) AS TEMPO_FORMATADO
				FROM
				    ENTRADA
				WHERE
				        PLACA = ?
				    AND STATUSVEICULO = 'ABERTO';
								""";

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setString(1, placa);

		ResultSet rs = ps.executeQuery();

		Entrada entrada = null;

		if (rs.next()) {

			entrada = new Entrada();

			entrada.setIdEntrada(rs.getInt("IDENTRADA"));

			entrada.setNomeCliente(rs.getString("NOMECLIENTE"));

			entrada.setPlaca(rs.getString("PLACA"));

			entrada.setTipoVeiculo(rs.getString("TIPOVEICULO"));

			Timestamp data = rs.getTimestamp("DTENTRADA");

			entrada.setDtEntrada(data.toLocalDateTime());

			entrada.setStatusVeiculo(rs.getString("STATUSVEICULO"));

			entrada.setNumeroVaga(rs.getInt("NUMEROVAGA"));

			entrada.setTempoFormatado(rs.getString("TEMPO_FORMATADO"));

		}

		rs.close();
		ps.close();
		conn.close();

		return entrada;

	}

	public void finalizarEntrada(int idEntrada) throws SQLException {

		String sql = """
				UPDATE ENTRADA
				SET STATUSVEICULO = 'FINALIZADO'
				WHERE IDENTRADA = ?
				""";

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setInt(1, idEntrada);

		ps.executeUpdate();

		ps.close();
		conn.close();
	}

	/**
	 * @throws SQLException
	 */
	public void listarPatio() throws SQLException {

		String sql = """
				SELECT *
				FROM ENTRADA
				WHERE STATUSVEICULO = 'ABERTO'
				ORDER BY NUMEROVAGA
				""";
		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();

		System.out.println("\n==== VEICULO NO PATIO ====");
		boolean encontrou = false;

		while (rs.next()) {

			encontrou = true;

			System.out.println("\n-----------------------------------");

			System.out.println("\nVaga: " + rs.getString("NUMEROVAGA"));

			System.out.println("Cliente: " + rs.getString("NOMECLIENTE"));

			System.out.println("Placa: " + rs.getString("PLACA"));

			System.out.println("Tipo: " + rs.getString("TIPOVEICULO"));

			System.out.println("ENTRADA: " + rs.getTimestamp("DTENTRADA"));

		}
		if (!encontrou) {

			System.out.println("\nNenhum veículo no pátio.");
		}

		rs.close();
		ps.close();
		conn.close();

	}

	// METODO LISTAR HISTORICO DE ENTRADA

	public void listarHistorico() throws SQLException {

		String sql = """
				SELECT
				            E.IDENTRADA,
				            E.NOMECLIENTE,
				            E.PLACA,
				            E.TIPOVEICULO,
				            E.NUMEROVAGA,
				            E.DTENTRADA,
				            E.STATUSVEICULO,
				            S.DTSAIDA,
				            S.TEMPO_MINUTOS,
				            S.VALOR_TOTAL
				        FROM ENTRADA E
				        LEFT JOIN SAIDA S
				            ON E.IDENTRADA = S.IDENTRADA
				        ORDER BY E.IDENTRADA DESC
				        """;

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();

		System.out.println("\n===== HISTORICO COMPLETO ====");

		boolean encontrou = false;

		while (rs.next()) {

			encontrou = true;

			System.out.println("\n-------------------------------------");

			System.out.println("ID: " + rs.getInt("IDENTRADA"));

			System.out.println("Cliente: " + rs.getString("NOMECLIENTE"));

			System.out.println("Placa: " + rs.getString("PLACA"));

			System.err.println("Tipo: " + rs.getString("TIPOVEICULO"));

			System.out.println("Vaga: " + rs.getInt("NUMEROVAGA"));

			System.out.println("Entrada: " + rs.getTimestamp("DTENTRADA"));

			System.out.println("Status: " + rs.getString("STATUSVEICULO"));

			if (rs.getTimestamp("DTSAIDA") != null) {

				System.out.println("Saída: " + rs.getTimestamp("DTSAIDA"));

				System.out.println("Tempo: " + rs.getInt("TEMPO_MINUTOS") + " minutos");

				System.out.printf("Valor Pago: R$ %.2f%n", rs.getDouble("VALOR_TOTAL"));

				System.out.println("\n");
			}
		}

		if (!encontrou) {

			System.out.println("\nNenhuma movimentação encontrada.");

		}

		rs.close();
		ps.close();
		conn.close();

	}

	// METODO SE JA CONSTA A PLACA NO SISTEMA

	public boolean veiculoEstaNoPatio(String placa) throws SQLException {

		String sql = """
				SELECT COUNT(*)
				FROM ENTRADA
				WHERE PLACA = ?
				AND STATUSVEICULO = 'ABERTO'
				""";

		Connection conn = ConnectionFactory.getConnection();

		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setString(1, placa.toUpperCase());

		ResultSet rs = ps.executeQuery();

		boolean existe = false;

		if (rs.next()) {

			existe = rs.getInt(1) > 0;

		}

		rs.close();
		ps.close();
		conn.close();

		return existe;

	}

	public void buscarVeiculoPorPlaca(String placa) throws SQLException {

		String sql = """
				SELECT
				    E.IDENTRADA,
				    E.NOMECLIENTE,
				    E.PLACA,
				    E.TIPOVEICULO,
				    E.NUMEROVAGA,
				    E.DTENTRADA,
				    E.STATUSVEICULO,
				    S.DTSAIDA,
				    S.TEMPO_MINUTOS,
				    S.VALOR_TOTAL
				FROM ENTRADA E
				LEFT JOIN SAIDA S ON E.IDENTRADA = S.IDENTRADA
				WHERE E.PLACA = ?
				ORDER BY E.IDENTRADA DESC
				""";

		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, placa.toUpperCase());

		ResultSet rs = ps.executeQuery();

		System.out.println("\n===== BUSCA POR PLACA =====");
		System.out.println("Placa: " + placa.toUpperCase());
		boolean encontrou = false;

		while (rs.next()) {

			encontrou = true;

			System.out.println("\n-------------------------------------");
			System.out.println("ID: " + rs.getInt("IDENTRADA"));
			System.out.println("Cliente: " + rs.getString("NOMECLIENTE"));
			System.out.println("Tipo: " + rs.getString("TIPOVEICULO"));
			System.out.println("Vaga: " + rs.getInt("NUMEROVAGA"));
			System.out.println("Entrada: " + rs.getTimestamp("DTENTRADA"));
			System.out.println("Status: " + rs.getString("STATUSVEICULO"));

			if (rs.getString("STATUSVEICULO").equals("ABERTO")) {
				System.out.println("✅ VEÍCULO AINDA NO PÁTIO!");
			} else {
				System.out.println("Saída: " + rs.getTimestamp("DTSAIDA"));
				System.out.println("Tempo: " + rs.getInt("TEMPO_MINUTOS") + " minutos");
				System.out.printf("Valor Pago: R$ %.2f%n", rs.getDouble("VALOR_TOTAL"));
			}

		}

		if (!encontrou) {
			System.out.println("\nNenhum veículo encontrado com essa placa.");
		}

		rs.close();
		ps.close();
		conn.close();

	}

	public void buscarHistoricoCliente(String nomeCliente) throws SQLException {

		String sql = """
				SELECT
				    E.IDENTRADA,
				    E.NOMECLIENTE,
				    E.PLACA,
				    E.TIPOVEICULO,
				    E.NUMEROVAGA,
				    E.DTENTRADA,
				    E.STATUSVEICULO,
				    S.DTSAIDA,
				    S.TEMPO_MINUTOS,
				    S.VALOR_TOTAL
				FROM ENTRADA E
				LEFT JOIN SAIDA S ON E.IDENTRADA = S.IDENTRADA
				WHERE E.NOMECLIENTE LIKE ?
				ORDER BY E.IDENTRADA DESC
				""";

		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, "%" + nomeCliente.toUpperCase() + "%");

		ResultSet rs = ps.executeQuery();

		System.out.println("\n===== HISTÓRICO DO CLIENTE =====");
		System.out.println("Cliente: " + nomeCliente);
		boolean encontrou = false;
		double totalGasto = 0;

		while (rs.next()) {

			encontrou = true;

			System.out.println("\n-------------------------------------");
			System.out.println("ID: " + rs.getInt("IDENTRADA"));
			System.out.println("Placa: " + rs.getString("PLACA"));
			System.out.println("Tipo: " + rs.getString("TIPOVEICULO"));
			System.out.println("Entrada: " + rs.getTimestamp("DTENTRADA"));
			System.out.println("Status: " + rs.getString("STATUSVEICULO"));

			if (!rs.getString("STATUSVEICULO").equals("ABERTO")) {
				System.out.println("Saída: " + rs.getTimestamp("DTSAIDA"));
				System.out.println("Tempo: " + rs.getInt("TEMPO_MINUTOS") + " minutos");
				double valor = rs.getDouble("VALOR_TOTAL");
				System.out.printf("Valor: R$ %.2f%n", valor);
				totalGasto += valor;
			}

		}

		if (encontrou) {
			System.out.println("\n-------------------------------------");
			System.out.printf("💰 TOTAL GASTO: R$ %.2f%n", totalGasto);
		} else {
			System.out.println("\nNenhum registro encontrado para esse cliente.");
		}

		System.out.println("==================================");
		rs.close();
		ps.close();
		conn.close();

	}
}
