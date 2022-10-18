package rs.etf.sab.student;

import rs.etf.sab.operations.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dm180015_PackageOperations implements PackageOperations {
    @Override
    public int insertPackage(int addressFrom, int addressTo, String username, int packageType,
                             BigDecimal weight) {

        if(weight == null) weight = new BigDecimal(10);
        Connection connection = DB.getInstance().getConnection();
        String query = "insert into Paket(IdAdresaPreuzimanje, IdAdresaIsporuke, KorisnickoIme, Tip, Tezina, Status, VremeKreiranja, Isplaniran) values(?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, addressFrom);
            preparedStatement.setInt(2, addressTo);
            preparedStatement.setString(3, username);
            preparedStatement.setInt(4, packageType);
            preparedStatement.setBigDecimal(5, weight);
            preparedStatement.setInt(6, 0);
            preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(8, 0);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                return  generatedKeys.getInt(1);
            }
            else return -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);

           // return -1;
        }
    }

    @Override
    public boolean acceptAnOffer(int packageId) {
        Connection connection = DB.getInstance().getConnection();
        String query = "update Paket set Status = 1, VremePrihvatanja = ? where Status = 0 and IdPaket = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(2, packageId);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean rejectAnOffer(int packageId) {
        Connection connection = DB.getInstance().getConnection();
        String query = "update Paket set Status = 4 where Status = 0 and IdPaket = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, packageId);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Integer> getAllPackages() {
        List<Integer> allPackages = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Paket";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allPackages.add(resultSet.getInt("IdPaket"));
            }

        } catch (SQLException e) {
            return allPackages;
        }
        return allPackages;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        List<Integer> allPackages = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Paket where Tip = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                allPackages.add(resultSet.getInt("IdPaket"));
            }

        } catch (SQLException e) {
            return allPackages;
        }
        return allPackages;
    }

    @Override
    public List<Integer> getAllUndeliveredPackages() {
        List<Integer> undeliveredPackages = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Paket where Status = 1 or Status = 2";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                undeliveredPackages.add(resultSet.getInt("IdPaket"));
            }

        } catch (SQLException e) {
            return undeliveredPackages;
        }
        return undeliveredPackages;
    }

    @Override
    public List<Integer> getAllUndeliveredPackagesFromCity(int idCity) {
        List<Integer> undeliveredPackages = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "select *\n" +
                " from Paket P join Adresa A on(P.IdAdresaPreuzimanje = A.IdAdresa)\n" +
                " where (Status = 1 or Status = 2) and A.IdGrad = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, idCity);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                undeliveredPackages.add(resultSet.getInt("IdPaket"));
            }

        } catch (SQLException e) {
            //ako ne postoji grad nista ne pise jel treba null ili empty listu da vrati
            return undeliveredPackages;
        }
        return undeliveredPackages;
    }

    @Override
    public List<Integer> getAllPackagesCurrentlyAtCity(int idCity) {
        //proveriti kasnije
        List<Integer> packagesInCity = new ArrayList<>();
        Connection connection = DB.getInstance().getConnection();
        String query = "{call getAllPackagesCurrentlyAtCity(?)}";
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setInt(1, idCity);

            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()){
                packagesInCity.add(resultSet.getInt("IdPaket"));
            }
            return packagesInCity;

        }catch (SQLException e) {
            return packagesInCity;
        }
    }

    @Override
    public boolean deletePackage(int packageId) {
        Connection connection = DB.getInstance().getConnection();
        String query = "delete from Paket where IdPaket = ? and (Status = 0 or Status = 4)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, packageId);
            int success = preparedStatement.executeUpdate();
            return success != 0;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean changeWeight(int packageId, BigDecimal weight) {
        Connection connection = DB.getInstance().getConnection();
        String query = "update Paket set Tezina = ? where Status = 0 and IdPaket = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setBigDecimal(1, weight == null ? new BigDecimal(10) : weight);
            preparedStatement.setInt(2, packageId);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean changeType(int packageId, int type) {
        Connection connection = DB.getInstance().getConnection();
        String query = "update Paket set Tip = ? where Status = 0 and IdPaket = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, type);
            preparedStatement.setInt(2, packageId);
            int success = preparedStatement.executeUpdate();
            return success == 1;

        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public int getDeliveryStatus(int packageId) {
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, packageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("Status");
            }
            else return -1;

        } catch (SQLException e) {
            return -1;
        }
    }

    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, packageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getBigDecimal("Cena");
            }
            else return null;

        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public int getCurrentLocationOfPackage(int packageId) {
        //proveriti kasnije
        //vraca idGrada ili -1 ako je u prevozu. Dodala sam da vrati -2 ako paket ne postoji
        Connection connection = DB.getInstance().getConnection();
        String query = "{? = call getCurrentLocationOfPackage(?)}";
        try (CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setInt(2, packageId);
            callableStatement.registerOutParameter(1, Types.INTEGER);

            callableStatement.execute();
            return callableStatement.getInt(1);

        }catch (SQLException e) {
            return -2;
        }

    }

    @Override
    public Date getAcceptanceTime(int packageId) {
        Connection connection = DB.getInstance().getConnection();
        String query = "select * from Paket where IdPaket = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, packageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Timestamp timestamp = resultSet.getTimestamp("VremePrihvatanja");
                return timestamp == null ? null : new Date(timestamp.getTime());
            }
            else return null;

        } catch (SQLException e) {
            return null;
        }
    }


    public static void main(String[] args){

        new dm180015_GeneralOperations().eraseAll();

        CityOperations cityOperations = new dm180015_CityOperations();
        int BG = cityOperations.insertCity("Beograd", "11000");
        int UE = cityOperations.insertCity("Uzice", "31000");

        AddressOperations addressOperations = new dm180015_AddressOperations();
        int BG_adr1 = addressOperations.insertAddress("Vojvode Micka", 1, BG, 1, 1);
        int BG_adr2 = addressOperations.insertAddress("Marka Oreskovica", 100, BG, 2, 2);
        int BG_adr3 = addressOperations.insertAddress("Cvijiceva", 100, BG, 3, 3);
        int UE_adr1 = addressOperations.insertAddress("Hercegovacka", 79, UE, 11, 11);
        int UE_adr2 = addressOperations.insertAddress("Bosanska", 19, UE, 12, 12);

        UserOperations userOperations = new dm180015_UserOperations();
        DriveOperation driveOperation = new dm180015_DriveOperations();
        CourierOperations courierOperations = new dm180015_CourierOperations();
        StockroomOperations stockroomOperations = new dm180015_StockroomOperations();
        VehicleOperations vehicleOperations = new dm180015_VehicleOperations();

        System.out.println(userOperations.insertUser("mika", "Marija", "Dimitrijevic", "Test_1234a!", BG_adr1));

        PackageOperations packageOperations = new dm180015_PackageOperations();
        int package1 = packageOperations.insertPackage(BG_adr1, UE_adr2, "mika", 1, new BigDecimal(4.5));
        System.out.println(package1);
        int package2 = packageOperations.insertPackage(UE_adr1, BG_adr2, "mika", 2, new BigDecimal(2));
        System.out.println(package2);
        int package3 = packageOperations.insertPackage(BG_adr2, BG_adr1, "mika", 0, new BigDecimal(1));
        System.out.println(package3);
        int package4 = packageOperations.insertPackage(UE_adr1, BG_adr2, "mika", 0, new BigDecimal(2));
        System.out.println(package4);

        vehicleOperations.insertVehicle("107-114", 1, new BigDecimal(5), new BigDecimal(10));
        int idStockroom = stockroomOperations.insertStockroom(BG_adr3);
        System.out.println("Parked vehicle " + vehicleOperations.parkVehicle("107-114", idStockroom));

        System.out.println(packageOperations.getAllPackages());
        System.out.println(packageOperations.getAllPackagesWithSpecificType(1));
        System.out.println("Cena za package1 je " + packageOperations.getPriceOfDelivery(package1));
        System.out.println("Cena za package2 je " + packageOperations.getPriceOfDelivery(package2));
        System.out.println("Status za package1 je " + packageOperations.getDeliveryStatus(package1));
        System.out.println("Status za package2 je " + packageOperations.getDeliveryStatus(package2));

        /*System.out.println(packageOperations.changeType(package1, 0));
        System.out.println(packageOperations.changeWeight(package2, new BigDecimal(5)));
        System.out.println("Cena za package1 je " + packageOperations.getPriceOfDelivery(package1));
        System.out.println("Cena za package2 je " + packageOperations.getPriceOfDelivery(package2));*/


        System.out.println(packageOperations.acceptAnOffer(package1));
        System.out.println(packageOperations.acceptAnOffer(package2));
        System.out.println(packageOperations.acceptAnOffer(package3));
        System.out.println(packageOperations.acceptAnOffer(package4));
        System.out.println("Status za package1 je " + packageOperations.getDeliveryStatus(package1));
        System.out.println("Status za package2 je " + packageOperations.getDeliveryStatus(package2));


        System.out.println(packageOperations.getAcceptanceTime(package1));
        System.out.println(packageOperations.getAcceptanceTime(package2));

        System.out.println("Undelivered packages from city BG: " + packageOperations.getAllUndeliveredPackagesFromCity(BG));

        System.out.println("Lokacija za package1 je " + packageOperations.getCurrentLocationOfPackage(package1));
        System.out.println("Lokacija za package2 je " + packageOperations.getCurrentLocationOfPackage(package2));
        System.out.println("Lokacija za package3 je " + packageOperations.getCurrentLocationOfPackage(package3));

        System.out.println("BG: " + packageOperations.getAllPackagesCurrentlyAtCity(BG));
        System.out.println("UE: " + packageOperations.getAllPackagesCurrentlyAtCity(UE));

        System.out.println(courierOperations.insertCourier("mika", "0123923"));
        System.out.println("Planning drive mika " + driveOperation.planingDrive("mika"));
        System.out.println("Status 1 " + courierOperations.getCouriersWithStatus(1));


        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));

        int package5 = packageOperations.insertPackage(BG_adr1, BG_adr2, "mika", 0, new BigDecimal(2));
        System.out.println(packageOperations.acceptAnOffer(package5));
        System.out.println("Planning drive mika " + driveOperation.planingDrive("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));
        System.out.println("Next stop " + driveOperation.nextStop("mika"));

        System.out.println("NumOfDeliveries 5: " + courierOperations.getAverageCourierProfit(5));
        System.out.println("NumOfDeliveries -1: " + courierOperations.getAverageCourierProfit(-1));
        System.out.println("NumOfDeliveries 6: " + courierOperations.getAverageCourierProfit(6));




    }
}
