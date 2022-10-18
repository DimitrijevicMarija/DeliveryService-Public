package rs.etf.sab.student;

import rs.etf.sab.operations.AddressOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.UserOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class dm180015_UserOperations implements UserOperations {
    @Override
    public boolean insertUser(String username, String firstName, String lastName, String password, int idAddress) {

        if ( !Pattern.matches("^[A-Z]+.*$", firstName) ||
             !Pattern.matches("^[A-Z]+.*$", lastName) ||
             !Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[_\\W]).{8,}$", password) ||
             password.length() < 8) {

            return false;
        }

        Connection connection = DB.getInstance().getConnection();
        String query = "insert into Korisnik(KorisnickoIme, Ime, Prezime, Sifra, IdAdresa) values(?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, password);
            preparedStatement.setInt(5, idAddress);
            int numRows = preparedStatement.executeUpdate();

            return numRows == 1;

        } catch (SQLException e) {
            //ako ne postoji idAdresa uci ce ovde
            return false;
        }
    }

    @Override
    public boolean declareAdmin(String username) {
        Connection connection = DB.getInstance().getConnection();
        String query = "if not exists(select * from Administrator where KorisnickoIme = ?) insert into Administrator(KorisnickoIme) values(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, username);
            int numRows = preparedStatement.executeUpdate();

            return numRows == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public int getSentPackages(String... usernames) {

        List<String> allUsers = getAllUsers();
        boolean existsAny = false;
        for (String username: usernames){
            if (allUsers.contains(username)) {
                existsAny = true;
                break;
            }
        }
        if (!existsAny) return -1;

        Connection connection = DB.getInstance().getConnection();
        String query = "select coalesce(count(*), 0)\n" +
                "  from Paket\n" +
                "  where KorisnickoIme in (?";
        for(int i = 1; i < usernames.length; i++) query += ", ?";
        query += ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            for(int i = 0; i < usernames.length; i++)
                preparedStatement.setString(i + 1, usernames[i]);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public int deleteUsers(String... usernames) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Korisnik where KorisnickoIme in ( ? ";
        for(int i = 1; i < usernames.length; i++) query += ", ?";
        query += ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            for(int i = 0; i < usernames.length; i++)
                preparedStatement.setString(i + 1, usernames[i]);

            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Korisnik";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allUsers.add(resultSet.getString("KorisnickoIme"));
            }

        } catch (SQLException e) {
            return allUsers;
        }
        return allUsers;
    }
    public static void main(String[] args){
        CityOperations co = new dm180015_CityOperations();
        int idCity = co.insertCity("Uzice", "31000");
        System.out.println(idCity);

        AddressOperations ao = new dm180015_AddressOperations();
        int idAddress = ao.insertAddress("Teraz", 1, idCity, 10, 10);
        System.out.println(idAddress);

        UserOperations userOperations = new dm180015_UserOperations();


        System.out.println(userOperations.insertUser("d", "SWEa", "Ss", "Test_123", idAddress));
        System.out.println(userOperations.insertUser("dd", "SWEa", "Ss", "Test=123", idAddress));
        System.out.println(userOperations.insertUser("ddd", "SWEa", "Ss", "Test!123", idAddress));
        System.out.println(userOperations.insertUser("dddd", "SWEa", "Ss", "Test<123", idAddress));
        System.out.println(userOperations.insertUser("ddddd", "SWEa", "Ss", "Test#123", idAddress));

        ao.deleteAdress(idAddress);
        co.deleteCity(idCity);
    }
}
