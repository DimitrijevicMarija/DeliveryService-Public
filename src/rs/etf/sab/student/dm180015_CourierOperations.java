package rs.etf.sab.student;

import rs.etf.sab.operations.CourierOperations;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dm180015_CourierOperations implements CourierOperations {
    @Override
    public boolean insertCourier(String username, String driverLicenceNumber) {
        Connection connection = DB.getInstance().getConnection();
        String query = "insert into Kurir(KorisnickoIme, BrojVozackeDozvole, BrojIsporucenihPaketa, Profit, Status)\n" +
                "values(?, ?, 0, 0, 0)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, driverLicenceNumber);
            int numRows = preparedStatement.executeUpdate();

            return numRows == 1;

        } catch (SQLException e) {
            //ako ne postoji taj korisnik ili broj vozacke dozvole nije jedinstven
            return false;
        }
    }

    @Override
    public boolean deleteCourier(String username) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Kurir where KorisnickoIme = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<String> getCouriersWithStatus(int statusOfCourier) {
        List<String> couriers = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Kurir where Status = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, statusOfCourier);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                couriers.add(resultSet.getString("KorisnickoIme"));
            }

        } catch (SQLException e) {
            return couriers;
        }
        return couriers;
    }

    @Override
    public List<String> getAllCouriers() {
        List<String> allCouriers = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Kurir";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allCouriers.add(resultSet.getString("KorisnickoIme"));
            }

        } catch (SQLException e) {
            return allCouriers;
        }
        return allCouriers;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int numberOfDeliveries) {
        Connection connection = DB.getInstance().getConnection();
        String query = "select coalesce(AVG(Profit),0)\n" +
                        "from Kurir";
        if (numberOfDeliveries != -1) query = "select coalesce(AVG(Profit),0)\n" +
                                                "from Kurir\n" +
                                                "where BrojIsporucenihPaketa = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            if (numberOfDeliveries != -1) preparedStatement.setInt(1, numberOfDeliveries);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getBigDecimal(1);
            }
            else return null;

        } catch (SQLException e) {
            return null;
        }

    }
}
