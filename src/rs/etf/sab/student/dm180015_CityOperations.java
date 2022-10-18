package rs.etf.sab.student;

import rs.etf.sab.operations.CityOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dm180015_CityOperations implements CityOperations {
    @Override
    public int insertCity(String name, String postalCode) {
        Connection connection = DB.getInstance().getConnection();
        String query = "insert into Grad(Naziv, PostanskiBroj) values(?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, postalCode);
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
    public int deleteCity(String... names) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Grad where Naziv in ( ? ";
        for(int i = 1; i < names.length; i++) query += ", ?";
        query += ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            for(int i = 0; i < names.length; i++)
                preparedStatement.setString(i + 1, names[i]);

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public boolean deleteCity(int idCity) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Grad where IdGrad = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, idCity);
            int success = preparedStatement.executeUpdate();
            return success != 0;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Integer> getAllCities() {
        List<Integer> allCities = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Grad";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allCities.add(resultSet.getInt("IdGrad"));
            }

        } catch (SQLException e) {
            return allCities;
        }
        return allCities;
    }

    public static void main(String[] args) {
        /*
        CityOperations co = new dm180015_CityOperations();
        System.out.println(co.insertCity("Beograd", "11001"));
        System.out.println(co.insertCity("Beograd", "11002"));
        System.out.println(co.insertCity("Krusevac", "37001"));
        System.out.println(co.insertCity("Valjevo", "11005"));
        System.out.println(co.insertCity("Uzice", "66000"));

        System.out.println(co.deleteCity("Beograd", "Krusevac", "Valjevo"));
        System.out.println(co.deleteCity("Uzice"));
        System.out.println(co.deleteCity("Beograd", "Krusevac"));
        System.out.println(co.deleteCity("Njaam"));

        System.out.println(co.deleteCity(3));
        System.out.println(co.deleteCity(-1));
        System.out.println(co.getAllCities());

         */
    }
}
