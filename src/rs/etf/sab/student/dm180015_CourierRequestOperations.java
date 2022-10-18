package rs.etf.sab.student;

import rs.etf.sab.operations.CourierRequestOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dm180015_CourierRequestOperations implements CourierRequestOperation {
    @Override
    public boolean insertCourierRequest(String username, String driverLicenceNumber) {
        Connection connection = DB.getInstance().getConnection();
        String query = "if not exists(select * from Kurir where KorisnickoIme=? or BrojVozackeDozvole=?)\n" +
                "insert into Zahtev(KorisnickoIme, BrojVozackeDozvole) values(?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, driverLicenceNumber);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, driverLicenceNumber);
            int numRows = preparedStatement.executeUpdate();

            return numRows == 1;

        } catch (SQLException e) {
            //ako vec ima zahtev za tog kurira ili nije unique vozackaDozvola
            //ili ne postoji korisnik
            return false;
        }
    }

    @Override
    public boolean deleteCourierRequest(String username) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Zahtev where KorisnickoIme = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean changeDriverLicenceNumberInCourierRequest(String username, String driverLicenceNumber) {
        Connection connection = DB.getInstance().getConnection();
        String query = "update Zahtev set BrojVozackeDozvole=? where KorisnickoIme = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, driverLicenceNumber);
            preparedStatement.setString(2, username);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<String> getAllCourierRequests() {
        List<String> allRequests = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Zahtev";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allRequests.add(resultSet.getString("KorisnickoIme"));
            }

        } catch (SQLException e) {
            return allRequests;
        }
        return allRequests;
    }

    @Override
    public boolean grantRequest(String username) {

        if (!getAllCourierRequests().contains(username)) return false;

        Connection connection = DB.getInstance().getConnection();
        String query = "insert into Kurir(KorisnickoIme, BrojVozackeDozvole, BrojIsporucenihPaketa, Profit, Status)\n" +
                "values(?, (select BrojVozackeDozvole from Zahtev where KorisnickoIme = ?), 0, 0, 0)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            int success = preparedStatement.executeUpdate();
            if (success == 1){
                deleteCourierRequest(username);
            }
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    public static void main(String[] args){

    }
}
