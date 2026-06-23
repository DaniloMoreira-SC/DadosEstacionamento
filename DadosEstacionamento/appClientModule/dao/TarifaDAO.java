package dao;

import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarifaDAO {

    public double buscarValorHora()
            throws SQLException {

    		 
        String sql = """
                SELECT VALORHORA
                FROM TARIFA
                LIMIT 1
                """;

        Connection conn =
                ConnectionFactory.getConnection();

        PreparedStatement ps =
                conn.prepareStatement(sql);

        ResultSet rs =
                ps.executeQuery();

        double valor = 0;

        if (rs.next()) {

            valor =
                    rs.getDouble("VALORHORA");
        }

        rs.close();
        ps.close();
        conn.close();

        return valor;
    }
}