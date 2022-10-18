package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import rs.etf.sab.operations.*;
import rs.etf.sab.tests.Pair;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;


public class StudentMain {

    /*static AddressOperations addressOperations = new dm180015_AddressOperations();
    static CityOperations cityOperations = new dm180015_CityOperations();
    static CourierOperations courierOperation = new dm180015_CourierOperations();
    static CourierRequestOperation courierRequestOperation = new dm180015_CourierRequestOperations();
    static DriveOperation driveOperation = new dm180015_DriveOperations();
    static GeneralOperations generalOperations = new dm180015_GeneralOperations();
    static PackageOperations packageOperations = new dm180015_PackageOperations();
    static StockroomOperations stockroomOperations = new dm180015_StockroomOperations();
    static UserOperations userOperations = new dm180015_UserOperations();
    static VehicleOperations vehicleOperations = new dm180015_VehicleOperations();

    static Map<Integer, Pair<Integer, Integer>> addressesCoords = new HashMap();
    static Map<Integer, BigDecimal> packagePrice = new HashMap();


    static int insertCity(String name, String postalCode) {
        int idCity = cityOperations.insertCity(name, postalCode);
        Assert.assertNotEquals(-1L, (long)idCity);
        Assert.assertTrue(cityOperations.getAllCities().contains(idCity));
        return idCity;
    }

    static int insertAddress(String street, int number, int idCity, int x, int y) {
        int idAddress = addressOperations.insertAddress(street, number, idCity, x, y);
        Assert.assertNotEquals(-1L, (long)idAddress);
        Assert.assertTrue(addressOperations.getAllAddresses().contains(idAddress));
        addressesCoords.put(idAddress, new Pair(x, y));
        return idAddress;
    }

    static String insertUser(String username, String firstName, String lastName, String password, int idAddress) {
        Assert.assertTrue(userOperations.insertUser(username, firstName, lastName, password, idAddress));
        Assert.assertTrue(userOperations.getAllUsers().contains(username));
        return username;
    }

    static String insertCourier(String username, String firstName, String lastName, String password, int idAddress, String driverLicenceNumber) {
        insertUser(username, firstName, lastName, password, idAddress);
        Assert.assertTrue(courierOperation.insertCourier(username, driverLicenceNumber));
        return username;
    }

    static public void insertAndParkVehicle(String licencePlateNumber, BigDecimal fuelConsumption, BigDecimal capacity, int fuelType, int idStockroom) {
        Assert.assertTrue(vehicleOperations.insertVehicle(licencePlateNumber, fuelType, fuelConsumption, capacity));
        Assert.assertTrue(vehicleOperations.getAllVehichles().contains(licencePlateNumber));
        Assert.assertTrue(vehicleOperations.parkVehicle(licencePlateNumber, idStockroom));
    }

    static public int insertStockroom(int idAddress) {
        int stockroomId = stockroomOperations.insertStockroom(idAddress);
        Assert.assertNotEquals(-1L, (long)stockroomId);
        Assert.assertTrue(stockroomOperations.getAllStockrooms().contains(stockroomId));
        return stockroomId;
    }

    static int insertAndAcceptPackage(int addressFrom, int addressTo, String userName, int packageType, BigDecimal weight) {
        int idPackage = packageOperations.insertPackage(addressFrom, addressTo, userName, packageType, weight);
        Assert.assertNotEquals(-1L, (long)idPackage);
        Assert.assertTrue(packageOperations.acceptAnOffer(idPackage));
        Assert.assertTrue(packageOperations.getAllPackages().contains(idPackage));
        Assert.assertEquals(1L, (long)packageOperations.getDeliveryStatus(idPackage));
        BigDecimal price = Util.getPackagePrice(packageType, weight, Util.getDistance(new Pair[]{(Pair)addressesCoords.get(addressFrom), (Pair)addressesCoords.get(addressTo)}));
        Assert.assertTrue(packageOperations.getPriceOfDelivery(idPackage).compareTo(price.multiply(new BigDecimal(1.05))) < 0);
        Assert.assertTrue(packageOperations.getPriceOfDelivery(idPackage).compareTo(price.multiply(new BigDecimal(0.95))) > 0);
        packagePrice.put(idPackage, price);
        return idPackage;
    }


    static public void publicOne() {
        int BG = insertCity("Belgrade", "11000");
        int KG = insertCity("Kragujevac", "550000");
        int VA = insertCity("Valjevo", "14000");
        int CA = insertCity("Cacak", "32000");
        int idAddressBG1 = insertAddress("Kraljice Natalije", 37, BG, 11, 15);
        int idAddressBG2 = insertAddress("Bulevar kralja Aleksandra", 73, BG, 10, 10);
        int idAddressBG3 = insertAddress("Vojvode Stepe", 39, BG, 1, -1);
        int idAddressBG4 = insertAddress("Takovska", 7, BG, 11, 12);
        insertAddress("Bulevar kralja Aleksandra", 37, BG, 12, 12);
        int idAddressKG1 = insertAddress("Daniciceva", 1, KG, 4, 310);
        int idAddressKG2 = insertAddress("Dure Pucara Starog", 2, KG, 11, 320);
        int idAddressVA1 = insertAddress("Cika Ljubina", 8, VA, 102, 101);
        insertAddress("Karadjordjeva", 122, VA, 104, 103);
        insertAddress("Milovana Glisica", 45, VA, 101, 101);
        int idAddressCA1 = insertAddress("Zupana Stracimira", 1, CA, 110, 309);
        insertAddress("Bulevar Vuka Karadzica", 1, CA, 111, 315);
        int idStockroomBG = insertStockroom(idAddressBG1);
        int idStockroomVA = insertStockroom(idAddressVA1);
        insertAndParkVehicle("BG1675DA", new BigDecimal(6.3), new BigDecimal(1000.5), 2, idStockroomBG);
        insertAndParkVehicle("VA1675DA", new BigDecimal(7.3), new BigDecimal(500.5), 1, idStockroomVA);
        String username = "crno.dete";
        insertUser(username, "Svetislav", "Kisprdilov", "Test_123", idAddressBG1);
        String courierUsernameBG = "postarBG";
        insertCourier(courierUsernameBG, "Pera", "Peric", "Postar_73", idAddressBG2, "654321");
        String courierUsernameVA = "postarVA";
        insertCourier(courierUsernameVA, "Pera", "Peric", "Postar_73", idAddressBG2, "123456");
        int type1 = 0;
        BigDecimal weight1 = new BigDecimal(2);
        int idPackage1 = insertAndAcceptPackage(idAddressBG2, idAddressCA1, username, type1, weight1);
        int type2 = 1;
        BigDecimal weight2 = new BigDecimal(4);
        int idPackage2 = insertAndAcceptPackage(idAddressBG3, idAddressVA1, username, type2, weight2);
        int type3 = 2;
        BigDecimal weight3 = new BigDecimal(5);
        int idPackage3 = insertAndAcceptPackage(idAddressBG4, idAddressKG1, username, type3, weight3);
        Assert.assertEquals(0L, (long)courierOperation.getCouriersWithStatus(1).size());
        driveOperation.planingDrive(courierUsernameBG);
        Assert.assertTrue(courierOperation.getCouriersWithStatus(1).contains(courierUsernameBG));
        int type4 = 3;
        BigDecimal weight4 = new BigDecimal(2);
        int idPackage4 = insertAndAcceptPackage(idAddressBG2, idAddressKG2, username, type4, weight4);
        Assert.assertEquals(4L, (long)packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage1));
        Assert.assertEquals(1L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertEquals(1L, (long)packageOperations.getDeliveryStatus(idPackage3));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage1));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage2));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage3));
        Assert.assertEquals(3L, (long)packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
        Assert.assertEquals(1L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage1));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertEquals(1L, (long)packageOperations.getDeliveryStatus(idPackage3));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage1));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage2));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage3));
        Assert.assertEquals(2L, (long)packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
        Assert.assertEquals(2L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage1));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage3));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage1));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage2));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage3));
        Assert.assertEquals(1L, (long)packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
        Assert.assertEquals(3L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertEquals((long)idPackage2, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage1));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage3));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage1));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage2));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage3));
        Assert.assertEquals(1L, (long)packageOperations.getAllPackagesCurrentlyAtCity(VA).size());
        Assert.assertEquals(2L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertEquals((long)idPackage1, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage1));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage3));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage1));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage2));
        Assert.assertEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage3));
        Assert.assertEquals(1L, (long)packageOperations.getAllPackagesCurrentlyAtCity(CA).size());
        Assert.assertEquals(1L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertEquals((long)idPackage3, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage1));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage3));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage1));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage2));
        Assert.assertNotEquals(-1L, (long)packageOperations.getCurrentLocationOfPackage(idPackage3));
        Assert.assertEquals(1L, (long)packageOperations.getAllPackagesCurrentlyAtCity(KG).size());
        Assert.assertEquals(0L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertEquals(-1L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(1L, (long)packageOperations.getDeliveryStatus(idPackage4));
        Assert.assertEquals(1L, (long)packageOperations.getAllUndeliveredPackages().size());
        Assert.assertTrue(packageOperations.getAllUndeliveredPackages().contains(idPackage4));
        Assert.assertEquals(2L, (long)courierOperation.getCouriersWithStatus(0).size());
        double distance = Util.getDistance(new Pair[]{(Pair)addressesCoords.get(idAddressBG1), (Pair)addressesCoords.get(idAddressBG2), (Pair)addressesCoords.get(idAddressBG3), (Pair)addressesCoords.get(idAddressBG4), (Pair)addressesCoords.get(idAddressVA1), (Pair)addressesCoords.get(idAddressCA1), (Pair)addressesCoords.get(idAddressKG1), (Pair)addressesCoords.get(idAddressBG1)});
        BigDecimal profit = ((BigDecimal)packagePrice.get(idPackage1)).add((BigDecimal)packagePrice.get(idPackage2)).add((BigDecimal)packagePrice.get(idPackage3));
        profit = profit.subtract((new BigDecimal(36)).multiply(new BigDecimal(6.3)).multiply(new BigDecimal(distance)));
        Assert.assertTrue(courierOperation.getAverageCourierProfit(3).compareTo(profit.multiply(new BigDecimal(1.05))) < 0);
        Assert.assertTrue(courierOperation.getAverageCourierProfit(3).compareTo(profit.multiply(new BigDecimal(0.95))) > 0);
    }
    public static void publicTwo() {
        int BG = insertCity("Belgrade", "11000");
        int KG = insertCity("Kragujevac", "550000");
        int VA = insertCity("Valjevo", "14000");
        int CA = insertCity("Cacak", "32000");
        int idAddressBG1 = insertAddress("Kraljice Natalije", 37, BG, 11, 15);
        int idAddressBG2 = insertAddress("Bulevar kralja Aleksandra", 73, BG, 10, 10);
        int idAddressBG3 = insertAddress("Vojvode Stepe", 39, BG, 1, -1);
        int idAddressBG4 = insertAddress("Takovska", 7, BG, 11, 12);
        insertAddress("Bulevar kralja Aleksandra", 37, BG, 12, 12);
        int idAddressKG1 = insertAddress("Daniciceva", 1, KG, 4, 310);
        int idAddressKG2 = insertAddress("Dure Pucara Starog", 2, KG, 11, 320);
        int idAddressVA1 = insertAddress("Cika Ljubina", 8, VA, 102, 101);
        int idAddressVA2 = insertAddress("Karadjordjeva", 122, VA, 104, 103);
        int idAddressVA3 = insertAddress("Milovana Glisica", 45, VA, 101, 101);
        int idAddressCA1 = insertAddress("Zupana Stracimira", 1, CA, 110, 309);
        int idAddressCA2 = insertAddress("Bulevar Vuka Karadzica", 1, CA, 111, 315);
        int idStockroomBG = insertStockroom(idAddressBG1);
        int idStockroomVA = insertStockroom(idAddressVA1);
        insertAndParkVehicle("BG1675DA", new BigDecimal(6.3), new BigDecimal(1000.5), 2, idStockroomBG);
        insertAndParkVehicle("VA1675DA", new BigDecimal(7.3), new BigDecimal(500.5), 1, idStockroomVA);
        String username = "crno.dete";
        insertUser(username, "Svetislav", "Kisprdilov", "Test_123", idAddressBG1);
        String courierUsernameBG = "postarBG";
        insertCourier(courierUsernameBG, "Pera", "Peric", "Postar_73", idAddressBG2, "654321");
        String courierUsernameVA = "postarVA";
        insertCourier(courierUsernameVA, "Pera", "Peric", "Postar_73", idAddressVA2, "123456");
        int type = 1;
        BigDecimal weight = new BigDecimal(4);
        int idPackage1 = insertAndAcceptPackage(idAddressBG2, idAddressKG1, username, type, weight);
        int idPackage2 = insertAndAcceptPackage(idAddressKG2, idAddressBG4, username, type, weight);
        int idPackage3 = insertAndAcceptPackage(idAddressVA2, idAddressCA1, username, type, weight);
        int idPackage4 = insertAndAcceptPackage(idAddressCA2, idAddressBG4, username, type, weight);
        Assert.assertEquals(0L, (long)courierOperation.getCouriersWithStatus(1).size());
        driveOperation.planingDrive(courierUsernameBG);
        driveOperation.planingDrive(courierUsernameVA);
        Assert.assertEquals(2L, (long)courierOperation.getCouriersWithStatus(1).size());
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals((long)idPackage1, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage1));
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameVA));
        Assert.assertEquals((long)idPackage3, (long)driveOperation.nextStop(courierUsernameVA));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage3));
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameVA));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage4));
        Assert.assertEquals(-1L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage2));
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(KG).contains(idPackage1));
        Assert.assertEquals(-1L, (long)driveOperation.nextStop(courierUsernameVA));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage4));
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(VA).contains(idPackage4));
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(CA).contains(idPackage3));
        int idPackage5 = insertAndAcceptPackage(idAddressVA2, idAddressCA1, username, type, weight);
        int idPackage6 = insertAndAcceptPackage(idAddressBG3, idAddressVA3, username, type, weight);
        driveOperation.planingDrive(courierUsernameBG);
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage6));
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage2));
        Assert.assertFalse(packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage6));
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertTrue(driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage2));
        Assert.assertTrue(driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage6));
        Assert.assertEquals((long)idPackage2, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage2));
        Assert.assertEquals((long)idPackage6, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage6));
        Assert.assertEquals(0L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage5));
        Assert.assertTrue(driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage5));
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)packageOperations.getDeliveryStatus(idPackage4));
        Assert.assertEquals(2L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertTrue(driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage4));
        Assert.assertTrue(driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage5));
        Assert.assertEquals(1L, (long)packageOperations.getAllPackagesCurrentlyAtCity(VA).size());
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(VA).contains(idPackage6));
        Assert.assertEquals(-1L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(0L, (long)packageOperations.getAllUndeliveredPackagesFromCity(BG).size());
        Assert.assertEquals(3L, (long)packageOperations.getAllPackagesCurrentlyAtCity(BG).size());
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage2));
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage4));
        Assert.assertTrue(packageOperations.getAllPackagesCurrentlyAtCity(BG).contains(idPackage5));
        driveOperation.planingDrive(courierUsernameBG);
        Assert.assertEquals(0L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertEquals(-2L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(2L, (long)driveOperation.getPackagesInVehicle(courierUsernameBG).size());
        Assert.assertTrue(driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage4));
        Assert.assertTrue(driveOperation.getPackagesInVehicle(courierUsernameBG).contains(idPackage5));
        Assert.assertEquals((long)idPackage4, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage4));
        Assert.assertEquals((long)idPackage5, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(3L, (long)packageOperations.getDeliveryStatus(idPackage5));
        Assert.assertEquals(-1L, (long)driveOperation.nextStop(courierUsernameBG));
        Assert.assertEquals(0L, (long)packageOperations.getAllUndeliveredPackages().size());
        Assert.assertEquals(2L, (long)courierOperation.getCouriersWithStatus(0).size());
        Assert.assertTrue(courierOperation.getAverageCourierProfit(1).compareTo(new BigDecimal(0)) > 0);
        Assert.assertTrue(courierOperation.getAverageCourierProfit(5).compareTo(new BigDecimal(0)) > 0);
    }*/

    public static void main(String[] args) {

        /*generalOperations.eraseAll();
        publicOne();
        generalOperations.eraseAll();
        publicTwo();*/

        AddressOperations addressOperations = new dm180015_AddressOperations();
        CityOperations cityOperations = new dm180015_CityOperations();
        CourierOperations courierOperations = new dm180015_CourierOperations();
        CourierRequestOperation courierRequestOperation = new dm180015_CourierRequestOperations();
        DriveOperation driveOperation = new dm180015_DriveOperations();
        GeneralOperations generalOperations = new dm180015_GeneralOperations();
        PackageOperations packageOperations = new dm180015_PackageOperations();
        StockroomOperations stockroomOperations = new dm180015_StockroomOperations();
        UserOperations userOperations = new dm180015_UserOperations();
        VehicleOperations vehicleOperations = new dm180015_VehicleOperations();


        TestHandler.createInstance(
                addressOperations,
                cityOperations,
                courierOperations,
                courierRequestOperation,
                driveOperation,
                generalOperations,
                packageOperations,
                stockroomOperations,
                userOperations,
                vehicleOperations);

        TestRunner.runTests();
    }
}
