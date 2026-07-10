package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarifaDAO {

    public double buscarValorHora() throws SQLException {
        String sql = "SELECT VALORHORA FROM TARIFA LIMIT 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            double valor = 0;

            if (rs.next()) {
                valor = rs.getDouble("VALORHORA");
            }
            rs.close();
            return valor;
        }
    }

    public double buscarValorHoraExtra() throws SQLException {
        String sql = "SELECT VALORHORAEXTRA FROM TARIFA LIMIT 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            double valor = 0;

            if (rs.next()) {
                valor = rs.getDouble("VALORHORAEXTRA");
            }
            rs.close();
            return valor;
        }
    }

    public double buscarValorDiaria() throws SQLException {
        String sql = "SELECT VALORDIARIA FROM TARIFA LIMIT 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            double valor = 0;

            if (rs.next()) {
                valor = rs.getDouble("VALORDIARIA");
            }
            rs.close();
            return valor;
        }
    }

    public void atualizarTarifas(double valorHora, double valorHoraExtra, double valorDiaria) 
            throws SQLException {

        String sql = "UPDATE TARIFA SET VALORHORA = ?, VALORHORAEXTRA = ?, VALORDIARIA = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, valorHora);
            ps.setDouble(2, valorHoraExtra);
            ps.setDouble(3, valorDiaria);
            ps.executeUpdate();

            System.out.println("✅ Tarifas atualizadas com sucesso!");

        }
    }

    public void exibirTarifasAtuais() throws SQLException {
        System.out.println("\n===== TABELA DE PREÇOS ATUAL =====");
        System.out.printf("Até 1 hora: R$ %.2f%n", buscarValorHora());
        System.out.printf("Hora extra: R$ %.2f%n", buscarValorHoraExtra());
        System.out.printf("Diária: R$ %.2f%n", buscarValorDiaria());
        System.out.println("===================================");
    }
}
