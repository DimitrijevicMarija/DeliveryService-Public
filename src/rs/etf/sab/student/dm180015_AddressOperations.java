package rs.etf.sab.student;

import rs.etf.sab.operations.AddressOperations;
import rs.etf.sab.operations.CityOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dm180015_AddressOperations implements AddressOperations {
    @Override
    public int insertAddress(String street, int number, int cityId, int xCoord, int yCoord) {
        Connection connection = DB.getInstance().getConnection();
        String query = "if exists(select * from Grad where IdGrad = ? ) insert into Adresa(Ulica, Broj, x, y, IdGrad) values(?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, cityId);
            preparedStatement.setString(2, street);
            preparedStatement.setInt(3, number);
            preparedStatement.setInt(4, xCoord);
            preparedStatement.setInt(5, yCoord);
            preparedStatement.setInt(6, cityId);
            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                return  generatedKeys.getInt(1);
            }
            else return -1;

        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public int deleteAddresses(String name, int number) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Adresa where Ulica = ? and Broj = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, number);

            int numRows = preparedStatement.executeUpdate();
            return numRows;

        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public boolean deleteAdress(int idAddress) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Adresa where IdAdresa = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, idAddress);
            int success = preparedStatement.executeUpdate();
            return success != 0;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public int deleteAllAddressesFromCity(int idCity) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Adresa where IdGrad = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, idCity);
            int numRows = preparedStatement.executeUpdate();
            return numRows;

        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public List<Integer> getAllAddresses() {
        List<Integer> allAddresses = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Adresa";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allAddresses.add(resultSet.getInt("IdAdresa"));
            }

        } catch (SQLException e) {
            return allAddresses;
        }
        return allAddresses;
    }

    @Override
    public List<Integer> getAllAddressesFromCity(int idCity) {
        List<Integer> allAddressesFromCity = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement("select * from Grad where IdGrad = ?");) {
            ps.setInt(1, idCity);
            ResultSet resultSet = ps.executeQuery();
            if (! resultSet.next()) return null;
        }
        catch (SQLException e) {
            return null;
        }

        String query = "select * from Adresa where IdGrad = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, idCity);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allAddressesFromCity.add(resultSet.getInt("IdAdresa"));
            }

        } catch (SQLException e) {
            return allAddressesFromCity;
        }
        return allAddressesFromCity;
    }

    public static void main(String[] args){
        CityOperations co = new dm180015_CityOperations();
        int idCity = co.insertCity("Uzice", "31000");
        System.out.println(idCity);

        AddressOperations ao = new dm180015_AddressOperations();
        System.out.println(ao.getAllAddresses());
        System.out.println(ao.getAllAddressesFromCity(183)); // normalno
        System.out.println(ao.getAllAddressesFromCity(idCity)); // empty
        System.out.println(ao.getAllAddressesFromCity(1)); // ne postoji, null vraca

        System.out.println(ao.insertAddress("Teraz", 1, idCity, 10, 10));
        System.out.println(ao.insertAddress("Ttt", 2, idCity, 10, 10));
        System.out.println(ao.insertAddress("Teraz", 1, idCity, 10, 10));
        System.out.println(ao.insertAddress("Nikole Vukotica", 8, 183, 10, 10));

        System.out.println(ao.getAllAddresses());
        System.out.println(ao.getAllAddressesFromCity(183)); // normalno
        System.out.println(ao.getAllAddressesFromCity(idCity)); // empty
        System.out.println(ao.getAllAddressesFromCity(1)); // ne postoji, null vraca

        System.out.println(co.deleteCity("Uzice"));

        System.out.println(ao.deleteAllAddressesFromCity(idCity));
        System.out.println(ao.getAllAddressesFromCity(idCity));

        System.out.println(co.deleteCity("Uzice"));
        // System.out.println(ao.deleteAdress(1));
        // System.out.println(ao.deleteAddresses("Vojvode Micka", 1));
        // System.out.println(ao.deleteAddresses("Nikole Vukotica", 1));
        // System.out.println(ao.deleteAddresses("Vojvode Mickaaaaaaa", 1));
    }
}
