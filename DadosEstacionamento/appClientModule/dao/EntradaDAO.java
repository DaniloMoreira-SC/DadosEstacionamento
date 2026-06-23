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
					(NOMECLIENTE, PLACA, TIPOVEICULO)
					VALUES (?, ?, ?)
					""";
	
			Connection conn = ConnectionFactory.getConnection();
	
			PreparedStatement ps = conn.prepareStatement(sql);
	
			ps.setString(1, entrada.getNomeCliente());
	
			ps.setString(2, entrada.getPlaca());
	
			ps.setString(3, entrada.getTipoVeiculo());
	
			ps.executeUpdate();
	
			ps.close();
			conn.close();
	
		}
	
		// METODO BUSCAR ENTRADA PELA PLACA
	
		public Entrada buscarEntradaAberta(String placa) throws SQLException {
	
			String sql = """
					SELECT *
					FROM ENTRADA
					WHERE PLACA = ?
					AND STATUSVEICULO = 'ABERTO'
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
	
	}
