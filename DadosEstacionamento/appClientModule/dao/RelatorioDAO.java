package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RelatorioDAO {

    public void exibirFaturamentoDia(LocalDate data) throws SQLException {
        String sql = """
                SELECT 
                    COUNT(DISTINCT E.IDENTRADA) as total_veiculos,
                    SUM(S.VALOR_TOTAL) as faturamento_total
                FROM ENTRADA E
                LEFT JOIN SAIDA S ON E.IDENTRADA = S.IDENTRADA
                WHERE DATE(E.DTENTRADA) = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(data));
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== FATURAMENTO DO DIA =====");
            System.out.println("Data: " + data);

            if (rs.next()) {
                int totalVeiculos = rs.getInt("total_veiculos");
                double faturamento = rs.getDouble("faturamento_total");

                System.out.printf("Total de veículos: %d%n", totalVeiculos);
                System.out.printf("Faturamento: R$ %.2f%n", faturamento);
            }
            System.out.println("================================");
            rs.close();

        }
    }

    public void exibirRelatorioGeral(LocalDate data) throws SQLException {
        String sql = """
                SELECT 
                    COUNT(DISTINCT E.IDENTRADA) as total_veiculos,
                    SUM(S.VALOR_TOTAL) as faturamento,
                    AVG(S.TEMPO_MINUTOS) as tempo_medio_minutos
                FROM ENTRADA E
                LEFT JOIN SAIDA S ON E.IDENTRADA = S.IDENTRADA
                WHERE DATE(E.DTENTRADA) = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(data));
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== RELATÓRIO GERAL =====");
            System.out.println("Data: " + data);

            if (rs.next()) {
                int veiculos = rs.getInt("total_veiculos");
                double faturamento = rs.getDouble("faturamento");
                double tempoMedio = rs.getDouble("tempo_medio_minutos");

                int horas = (int) tempoMedio / 60;
                int minutos = (int) tempoMedio % 60;

                System.out.printf("Veículos hoje: %d%n", veiculos);
                System.out.printf("Faturamento: R$ %.2f%n", faturamento);
                System.out.printf("Tempo médio: %dh%dmin%n", horas, minutos);
            }
            System.out.println("============================");
            rs.close();

        }
    }
}
