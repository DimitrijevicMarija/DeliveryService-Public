package rs.etf.sab.student;

import rs.etf.sab.operations.DriveOperation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dm180015_DriveOperations implements DriveOperation {
    @Override
    public boolean planingDrive(String courierUsername) {
        Connection connection = DB.getInstance().getConnection();
        String query = "{call planningDrive(?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setString(1, courierUsername);
            callableStatement.registerOutParameter(2, Types.INTEGER);

            callableStatement.execute();
            return callableStatement.getInt(2) == 1;

        }catch (SQLException e) {
            return false;
        }
    }

    @Override
    public int nextStop(String courierUsername) {
        Connection connection = DB.getInstance().getConnection();
        String query = "{call nextStop(?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setString(1, courierUsername);
            callableStatement.registerOutParameter(2, Types.INTEGER);

            callableStatement.execute();
            return callableStatement.getInt(2);

        }catch (SQLException e) {
            return -3;
        }
    }

    @Override
    public List<Integer> getPackagesInVehicle(String courierUsername) {
        List<Integer> packagesInVehicle = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from SePrevozi where KorisnickoIme = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, courierUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                packagesInVehicle.add(resultSet.getInt("IdPaket"));
            }

        } catch (SQLException e) {
            return packagesInVehicle;
        }
        return packagesInVehicle;
    }
}
