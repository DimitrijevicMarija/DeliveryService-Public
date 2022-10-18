package rs.etf.sab.student;

import rs.etf.sab.operations.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dm180015_VehicleOperations implements VehicleOperations {
    @Override
    public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumtion,
                                 BigDecimal capacity) {

        Connection connection = DB.getInstance().getConnection();
        String query = "insert into Vozilo(RegistracioniBroj, TipGoriva, Potrosnja, Nosivost) values(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, licencePlateNumber);
            preparedStatement.setInt(2, fuelType);
            preparedStatement.setBigDecimal(3, fuelConsumtion);
            preparedStatement.setBigDecimal(4, capacity);
            int numRows = preparedStatement.executeUpdate();

            return numRows == 1;

        } catch (SQLException e) {
            //ako nije unique registracija ili nije odgovarajuci tip goriva
            return false;
        }
    }

    @Override
    public int deleteVehicles(String... licencePlateNumbers) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Vozilo where RegistracioniBroj in ( ? ";
        for(int i = 1; i < licencePlateNumbers.length; i++) query += ", ?";
        query += ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            for(int i = 0; i < licencePlateNumbers.length; i++)
                preparedStatement.setString(i + 1, licencePlateNumbers[i]);

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> allVehicles = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Vozilo";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allVehicles.add(resultSet.getString("RegistracioniBroj"));
            }

        } catch (SQLException e) {
            return allVehicles;
        }
        return allVehicles;
    }

    @Override
    public boolean changeFuelType(String licensePlateNumber, int fuelType) {
        Connection connection = DB.getInstance().getConnection();
        String query = "if exists(select * from JeParkirano where RegistracioniBroj = ?)\n" +
                "update Vozilo set TipGoriva=? where RegistracioniBroj = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, licensePlateNumber);
            preparedStatement.setInt(2, fuelType);
            preparedStatement.setString(3, licensePlateNumber);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean changeConsumption(String licensePlateNumber, BigDecimal fuelConsumption) {
        Connection connection = DB.getInstance().getConnection();
        String query = "if exists(select * from JeParkirano where RegistracioniBroj = ?)\n" +
                "update Vozilo set Potrosnja=? where RegistracioniBroj = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, licensePlateNumber);
            preparedStatement.setBigDecimal(2, fuelConsumption);
            preparedStatement.setString(3, licensePlateNumber);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean changeCapacity(String licensePlateNumber, BigDecimal capacity) {
        Connection connection = DB.getInstance().getConnection();
        String query = "if exists(select * from JeParkirano where RegistracioniBroj = ?)\n" +
                "update Vozilo set Nosivost=? where RegistracioniBroj = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, licensePlateNumber);
            preparedStatement.setBigDecimal(2, capacity);
            preparedStatement.setString(3, licensePlateNumber);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean parkVehicle(String licencePlateNumber, int idStockroom) {
        // ne moram da proveravam da li je na nekom drugom parkingu parkirano
        // jer je identfikaciona zavisnost pa bi puklo
        Connection connection = DB.getInstance().getConnection();
        String query = "if not exists(select * from Vozi where RegistracioniBroj = ? )\n" +
                "insert into JeParkirano(RegistracioniBroj, IdMagacin) values(?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, licencePlateNumber);
            preparedStatement.setString(2, licencePlateNumber);
            preparedStatement.setInt(3, idStockroom);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }

    }

    public static void main(String[] args){
    }



}
