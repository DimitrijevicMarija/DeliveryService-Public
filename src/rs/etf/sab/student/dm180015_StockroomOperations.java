package rs.etf.sab.student;

import rs.etf.sab.operations.StockroomOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dm180015_StockroomOperations implements StockroomOperations {
    @Override
    public int insertStockroom(int idAddress) {

        //puci ce ako bi probao da dodas adresu koja ne postoji
        Connection connection = DB.getInstance().getConnection();
        String query = "if not exists(select *\n" +
                "from Magacin M, Adresa A\n" +
                "where M.IdAdresa = A.IdAdresa and A.IdGrad = (select IdGrad from Adresa A1 where A1.IdAdresa = ?))\n" +
                " insert into Magacin(IdAdresa) values(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, idAddress);
            preparedStatement.setInt(2, idAddress);
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
    public boolean deleteStockroom(int idStockroom) {
        Connection connection = DB.getInstance().getConnection();
        String query = "if not exists(select * from JeParkirano where IdMagacin = ?)\n" +
                "and not exists(select * from JeUMagacinu where IdMagacin = ?)\n" +
                "delete from Magacin where IdMagacin = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, idStockroom);
            preparedStatement.setInt(2, idStockroom);
            preparedStatement.setInt(3, idStockroom);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public int deleteStockroomFromCity(int idCity) {
        Connection connection = DB.getInstance().getConnection();
        String query = "select IdMagacin\n" +
                "from Magacin M, Adresa A\n" +
                "where M.IdAdresa = A.IdAdresa and A.IdGrad = ?";

        String queryDelete = "delete from Magacin where IdMagacin = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             PreparedStatement preparedStatementDelete = connection.prepareStatement(queryDelete)){
            preparedStatement.setInt(1, idCity);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int idStockroom = resultSet.getInt(1);
                preparedStatementDelete.setInt(1, idStockroom);
                preparedStatementDelete.executeUpdate();
                return idStockroom;
            }
            else return -1; //nema magacina

        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public List<Integer> getAllStockrooms() {
        List<Integer> allStockrooms = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Magacin";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allStockrooms.add(resultSet.getInt("IdMagacin"));
            }

        } catch (SQLException e) {
            return allStockrooms;
        }
        return allStockrooms;
    }

    public static void main(String[] args){

    }
}
