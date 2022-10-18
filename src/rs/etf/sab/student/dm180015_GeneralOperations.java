package rs.etf.sab.student;

import rs.etf.sab.operations.GeneralOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dm180015_GeneralOperations implements GeneralOperations {
    @Override
    public void eraseAll() {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Administrator;\n" +
                "delete from Zahtev;\n" +
                "delete from Planiranje;\n" +
                "delete from JeParkirano;\n" +
                "delete from SePrevozi;\n" +
                "delete from Vozi;\n" +
                "delete from Voznja;\n" +
                "delete from Kurir;\n" +
                "delete from Vozilo;\n" +
                "delete from JeUMagacinu;\n" +
                "delete from Magacin;\n" +
                "delete from Paket;\n" +
                "delete from Korisnik;\n" +
                "delete from Adresa;\n" +
                "delete from Grad;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
