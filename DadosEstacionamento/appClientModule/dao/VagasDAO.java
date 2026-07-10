package dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VagasDAO {

    public void exibirVagasDisponiveis() throws SQLException {
        String sql = """
                SELECT 
                    COUNT(CASE WHEN STATUSVAGA = 'LIVRE' THEN 1 END) as livres,
                    COUNT(CASE WHEN STATUSVAGA = 'OCUPADA' THEN 1 END) as ocupadas,
                    COUNT(*) as total
                FROM VAGA
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== VAGAS DISPONÍVEIS =====");

            if (rs.next()) {
                int livres = rs.getInt("livres");
                int ocupadas = rs.getInt("ocupadas");
                int total = rs.getInt("total");

                System.out.printf("Total de vagas: %d%n", total);
                System.out.printf("Ocupadas: %d%n", ocupadas);
                System.out.printf("Livres: %d%n", livres);
            }
            System.out.println("==============================");
            rs.close();

        }
    }

    public void listarVagasLivres() throws SQLException {
        String sql = """
                SELECT NUMEROVAGA 
                FROM VAGA 
                WHERE STATUSVAGA = 'LIVRE' 
                ORDER BY NUMEROVAGA
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== VAGAS LIVRES =====");
            boolean encontrou = false;

            while (rs.next()) {
                encontrou = true;
                System.out.println("Vaga: " + rs.getInt("NUMEROVAGA"));
            }

            if (!encontrou) {
                System.out.println("Nenhuma vaga disponível!");
            }
            System.out.println("=======================");
            rs.close();

        }
    }
}
