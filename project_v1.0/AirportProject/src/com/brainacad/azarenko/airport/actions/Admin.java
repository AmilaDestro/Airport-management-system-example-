package com.brainacad.azarenko.airport.actions;

import com.brainacad.azarenko.airport.flights.FlightDirection;
import com.brainacad.azarenko.airport.passengers.PassengersList;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;


/**
* Class <b>Admin</b> contains the most wide set of actions by implementing all methods of
* <b>ManagementSystem</b> interface without throwing UnsupportedOperationException.
* @author Lyudmila Azarenko
* @version 1.0*
 * @see com.brainacad.azarenko.airport.actions.ManagementSystem
 */

public class Admin implements ManagementSystem {

    /**
     * Checking Set, contains instances of FlightDirection class. Used in methods that implement searching algorithm.
     * @see com.brainacad.azarenko.airport.flights.FlightDirection*/
    private static Set<FlightDirection> flights = new LinkedHashSet<>();

    /**
     * Checking Collection, contains instances of PassengersList class. Used in methods that implement searching algorithm.
     * @see com.brainacad.azarenko.airport.passengers.PassengersList*/
    private static Collection<PassengersList> passengers = new LinkedList<>();

    /**
     * Connection type variable is required for establishing a database connection.*/
    private Connection dbConnection = null;

    /**
     * Variable of primitive type is used for automatic counting of passenger ID*/
    private static int counter = 1;

    /**
     *Another counter variable of primitive type for methods which implement searching algorithm. */
    private static int matches = 0;

    /**
     * Checking collection consists of String one-dimensional array. The array contains records about arrivals.
     * Used to find a parameter of new JTable object.*/
    private Collection<String[]> structuredArrivals = new ArrayList<>();

    /**
     * Checking collection consists of String one-dimensional array. The array contains records about departures.
     * Used to find a parameter of new JTable object.*/
    private Collection<String[]> structuredDepartures = new ArrayList<>();

    /**
     * Checking collection consists of String one-dimensional array. The array contains records about passengers.
     * Used to find a parameter of new JTable object.*/
    private Collection<String[]> sortedByFlightPassengers = new ArrayList<>();



    /**
     * Establishes connection to the database*/
    private void connectToDB() {
        try {
            Class.forName("org.sqlite.core.DB");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:\\AirportProject\\src\\com\\brainacad\\azarenko\\airport\\actions\\database\\airport.db");
        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }

    }



    /**
     * Creates a new record about arrival in the database*/
    public void createNewArrivalInfo(String date, String city, String time, String flightNumber, String flightStatus, String terminal, Double distance) {
        FlightDirection.Arrival newArrival = new FlightDirection().new Arrival(date, city, time, flightNumber, flightStatus, terminal, distance);
        connectToDB();
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute("insert into Arrival values(\'" + flightNumber + "\',\'" + date + "\',\'" + time + "\',\'" + city + "\'," + distance + ",\'" + flightStatus + "\',\'" + terminal + "\'," + newArrival.getPriceBusiness() + "," + newArrival.getPriceEconomy() + ")");
            System.out.println("New record created!");
            } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Creates a new record about departure in the database*/
    public void createNewDepartureInfo(String date, String city, String time, String flightNumber, String flightStatus, String terminal, Double distance) {
        FlightDirection.Departure newDeparture = new FlightDirection().new Departure(date, city, time, flightNumber, flightStatus, terminal, distance);
        connectToDB();
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute("insert into Departure values(\'" + flightNumber + "\',\'" + date + "\',\'" + time + "\',\'" + city + "\'," + distance + ",\'" + flightStatus + "\',\'" + terminal + "\'," + newDeparture.getPriceBusiness() + "," + newDeparture.getPriceEconomy() + ")");
            System.out.println("New record created!");
        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Allows user or administrator to view information about all arrivals*/
    public void printArrivalInfo() {
        connectToDB();
        JFrame frame = new JFrame("All arrivals");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute("select * from Arrival");
            ResultSet arrivalsTable = statement.getResultSet();
            String[] columnNames = {"Date", "Time", "FlightNumber", "City", "Status", "Terminal", "Price of business class", "Price of economy class"};
            while (arrivalsTable.next()) {
                String flightNumber = arrivalsTable.getString("FlightNumber");
                String date = arrivalsTable.getString("Date");
                String time = arrivalsTable.getString("Time");
                String city = arrivalsTable.getString("City");
                String status = arrivalsTable.getString("FlightStatus");
                String terminal = arrivalsTable.getString("Terminal");
                String business = arrivalsTable.getString("BusinessPrice");
                String economy = arrivalsTable.getString("EconomyPrice");
                String[] oneRow = {date, time, flightNumber, city, status, terminal, business, economy};
                structuredArrivals.add(oneRow);
            }
            String[][] rowsOfTable = structuredArrivals.toArray(new String[structuredArrivals.size()][]);
            JTable table = new JTable(rowsOfTable, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            frame.getContentPane().add(scrollPane);
            frame.setPreferredSize(new Dimension(450, 200));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Allows user or administrator to view information about all departures*/
    public void printDepartureInfo() {
        connectToDB();
        JFrame frame = new JFrame("All departures");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute("select * from Departure");
            ResultSet departuresTable = statement.getResultSet();
            String[] columnNames = {"Date", "Time", "FlightNumber", "City", "Status", "Terminal", "Price of business class", "Price of economy class"};
            while (departuresTable.next()) {
                String flightNumber = departuresTable.getString("FlightNumber");
                String date = departuresTable.getString("Date");
                String time = departuresTable.getString("Time");
                String city = departuresTable.getString("City");
                String status = departuresTable.getString("FlightStatus");
                String terminal = departuresTable.getString("Terminal");
                String business = departuresTable.getString("BusinessPrice");
                String economy = departuresTable.getString("EconomyPrice");
                String[] oneRow = {date, time, flightNumber, city, status, terminal, business, economy};
                structuredDepartures.add(oneRow);
            }
            String[][] rowsOfTable = structuredDepartures.toArray(new String[structuredArrivals.size()][]);
            JTable table = new JTable(rowsOfTable, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            frame.getContentPane().add(scrollPane);
            frame.setPreferredSize(new Dimension(450, 200));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**@return int
     * Method calculates passenger ID so that to create a new record in Passengers table of the database.
     * Administrator doesn't fill in a field with this information manually.*/
    private int numberOfPassengers() {
        connectToDB();
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute("select count (*) from Passengers");
            ResultSet numberOfRows = statement.getResultSet();
            counter = numberOfRows.getInt(1) + 1;
        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
        return counter;
    }



    /**
     * Creates new records about passengers. Before that makes sure that the passenger is registered on an existing flight number.*/
    public void createListPassengers(String flightNumber, String firstName, String lastName, String sex, String nationality, String dateOfBirthday, String passport, String classOfTrip) {
        connectToDB();
        try (Statement statement = dbConnection.createStatement()) {
            statement.execute("select * from Arrival where FlightNumber = \'" + flightNumber + "\'");
            ResultSet searchArrival = statement.getResultSet();
            if (!searchArrival.next()) {
                statement.execute("select * from Departure where FlightNumber = \'" + flightNumber + "\'");
                ResultSet searchDeparture = statement.getResultSet();
                if (!searchDeparture.next()) {
                    System.out.println("No flights with specified number");
                } else {
                    try {
                        PassengersList addPassenger = new PassengersList(flightNumber, firstName, lastName, sex, nationality, dateOfBirthday, passport, classOfTrip);
                        statement.execute("insert into Passengers values(" + numberOfPassengers() + ",\'" + addPassenger.getFlight() + "\',\'" + addPassenger.getFirstName() + "\',\'" + addPassenger.getLastName() + "\',\'" + addPassenger.getSex() + "\',\'" + addPassenger.getNationality() + "\',\'" + addPassenger.getDateOfBirthday() + "\',\'" + addPassenger.getPassport() + "\',\'" + addPassenger.getClassOfTrip() + "\')");
                        System.out.println("New record created!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Some information was entered incorrectly. Pay attention to input samples.");
                    }
                }
            } else {
                try {
                    PassengersList addPassenger = new PassengersList(flightNumber, firstName, lastName, sex, nationality, dateOfBirthday, passport, classOfTrip);
                    statement.execute("insert into Passengers values(" + numberOfPassengers() + ",\'" + addPassenger.getFlight() + "\',\'" + addPassenger.getFirstName() + "\',\'" + addPassenger.getLastName() + "\',\'" + addPassenger.getSex() + "\',\'" + addPassenger.getNationality() + "\',\'" + addPassenger.getDateOfBirthday() + "\',\'" + addPassenger.getPassport() + "\',\'" + addPassenger.getClassOfTrip() + "\')");
                    System.out.println("New record created!");
                } catch (IllegalArgumentException e) {
                    System.out.println("Some information was entered incorrectly. Pay attention to input samples.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Searches passenger by the specified flight number. Flight number is mentioned as method parameter.*/
    public void searchPassengersByFlight(String flightNumber) {
        connectToDB();
        try (Statement searchByFlight = dbConnection.createStatement()) {
            searchByFlight.execute("select * from Passengers where FlightNumber=\'" + flightNumber + "\'");
            ResultSet searchPassenger = searchByFlight.getResultSet();
            while (searchPassenger.next()) {
                String passengerId = searchPassenger.getString("Id");
                String flight = searchPassenger.getString("FlightNumber");
                String name = searchPassenger.getString("FirstName");
                String surname = searchPassenger.getString("LastName");
                String sex = searchPassenger.getString("Sex");
                String nationality = searchPassenger.getString("Nationality");
                String dateOfBirth = searchPassenger.getString("DateOfBirth");
                String passport = searchPassenger.getString("Passport");
                String classOfTrip = searchPassenger.getString("ClassOfTrip");
                System.out.print("Id: " + passengerId + "\n" + "FlightNumber: " + flight + "\n" + "FirstName: " + name + "\n" + "LastName: " + surname + "\n" + "Sex: " + sex + "\n" + "Nationality: " + nationality + "\n" + "DateOfBirth" + dateOfBirth + "\n" + "Passport: " + passport + "\n" + "ClassOfTrip: " + classOfTrip + "\n");
                System.out.println();
                passengers.add(new PassengersList(flight, name, surname, sex, nationality, dateOfBirth, passport, classOfTrip));

            }

            for (PassengersList throughPassengers : passengers) {
                if (throughPassengers.getFlight().equals(flightNumber)) {
                    matches++;
                }
            }
            if (matches == 0) {
                System.out.println("Flight number is invalid or no registered passengers on this flight.");
            }
            matches = 0;

        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Searches passenger by the first name. First name is mentioned as method parameter.*/
    public void searchPassengersByName(String firstName) {
        connectToDB();
        try (Statement searchByName = dbConnection.createStatement()) {
            searchByName.execute("select * from Passengers where FirstName=\'" + firstName + "\'");
            ResultSet searchPassenger = searchByName.getResultSet();
            while (searchPassenger.next()) {
                String passengerId = searchPassenger.getString("Id");
                String flight = searchPassenger.getString("FlightNumber");
                String name = searchPassenger.getString("FirstName");
                String surname = searchPassenger.getString("LastName");
                String sex = searchPassenger.getString("Sex");
                String nationality = searchPassenger.getString("Nationality");
                String dateOfBirth = searchPassenger.getString("DateOfBirth");
                String passport = searchPassenger.getString("Passport");
                String classOfTrip = searchPassenger.getString("ClassOfTrip");
                System.out.print("Id: " + passengerId + "\n" + "FlightNumber: " + flight + "\n" + "FirstName: " + name + "\n" + "LastName: " + surname + "\n" + "Sex: " + sex + "\n" + "Nationality: " + nationality + "\n" + "DateOfBirth" + dateOfBirth + "\n" + "Passport: " + passport + "\n" + "ClassOfTrip: " + classOfTrip + "\n");
                System.out.println();
                passengers.add(new PassengersList(flight, name, surname, sex, nationality, dateOfBirth, passport, classOfTrip));

            }

            for (PassengersList throughPassengers : passengers) {
                if (throughPassengers.getFirstName().equals(firstName)) {
                    matches++;
                }
            }
            if (matches == 0) {
                System.out.println("No registered passengers with this name.");
            }
            matches = 0;

        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Searches passenger by the last name. Last name is mentioned as method parameter.*/
    public void searchPassengersBySurname(String lastName) {
        connectToDB();
        try (Statement searchBySurname = dbConnection.createStatement()) {
            searchBySurname.execute("select * from Passengers where LastName=\'" + lastName + "\'");
            ResultSet searchPassenger = searchBySurname.getResultSet();
            while (searchPassenger.next()) {
                String passengerId = searchPassenger.getString("Id");
                String flight = searchPassenger.getString("FlightNumber");
                String name = searchPassenger.getString("FirstName");
                String surname = searchPassenger.getString("LastName");
                String sex = searchPassenger.getString("Sex");
                String nationality = searchPassenger.getString("Nationality");
                String dateOfBirth = searchPassenger.getString("DateOfBirth");
                String passport = searchPassenger.getString("Passport");
                String classOfTrip = searchPassenger.getString("ClassOfTrip");
                System.out.print("Id: " + passengerId + "\n" + "FlightNumber: " + flight + "\n" + "FirstName: " + name + "\n" + "LastName: " + surname + "\n" + "Sex: " + sex + "\n" + "Nationality: " + nationality + "\n" + "DateOfBirth" + dateOfBirth + "\n" + "Passport: " + passport + "\n" + "ClassOfTrip: " + classOfTrip + "\n");
                System.out.println();
                passengers.add(new PassengersList(flight, name, surname, sex, nationality, dateOfBirth, passport, classOfTrip));
            }

            for (PassengersList throughPassengers : passengers) {
                if (throughPassengers.getLastName().equals(lastName)) {
                    matches++;
                }
            }
            if (matches == 0) {
                System.out.println("No registered passengers with this surname.");
            }
            matches = 0;


        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Searches passenger by the passport ID. Passport ID is mentioned as method parameter.*/
    public void searchPassengersByPassport(String passportId) {
        connectToDB();
        try (Statement searchByPassport = dbConnection.createStatement()) {
            searchByPassport.execute("select * from Passengers where Passport=\'" + passportId + "\'");
            ResultSet searchPassenger = searchByPassport.getResultSet();
            while (searchPassenger.next()) {
                String passengerId = searchPassenger.getString("Id");
                String flight = searchPassenger.getString("FlightNumber");
                String name = searchPassenger.getString("FirstName");
                String surname = searchPassenger.getString("LastName");
                String sex = searchPassenger.getString("Sex");
                String nationality = searchPassenger.getString("Nationality");
                String dateOfBirth = searchPassenger.getString("DateOfBirth");
                String passport = searchPassenger.getString("Passport");
                String classOfTrip = searchPassenger.getString("ClassOfTrip");
                System.out.print("Id: " + passengerId + "\n" + "FlightNumber: " + flight + "\n" + "FirstName: " + name + "\n" + "LastName: " + surname + "\n" + "Sex: " + sex + "\n" + "Nationality: " + nationality + "\n" + "DateOfBirth" + dateOfBirth + "\n" + "Passport: " + passport + "\n" + "ClassOfTrip: " + classOfTrip + "\n");
                System.out.println();
                passengers.add(new PassengersList(flight, name, surname, sex, nationality, dateOfBirth, passport, classOfTrip));

            }

            for (PassengersList throughPassengers : passengers) {
                if (throughPassengers.getPassport().equals(passportId)) {
                    matches++;
                }
            }
            if (matches == 0) {
                System.out.println("No registered passengers with this passport ID.");
            }
            matches = 0;


        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Searches flights (arrivals and departures) by the city. City is mentioned as method parameter.*/
    public void searchFlightsByCity(String city) {
        System.out.println("Searching city..." + city);
        connectToDB();
        try (Statement searchCity = dbConnection.createStatement()) {
            ResultSet searchArrival = searchCity.executeQuery("select * from Arrival where City=\'" + city + "\'");
            while (searchArrival.next()) {
                String flightNumber = searchArrival.getString("FlightNumber");
                String date = searchArrival.getString("Date");
                String time = searchArrival.getString("Time");
                String cityPort = searchArrival.getString("City");
                String status = searchArrival.getString("FlightStatus");
                String terminal = searchArrival.getString("Terminal");
                double distance = searchArrival.getDouble("Distance");
                String business = searchArrival.getString("BusinessPrice");
                String economy = searchArrival.getString("EconomyPrice");
                String foundInArrival = "Flight number: " + flightNumber + "\nDate: " + date + "\nTime: " + time + "\nCity: " + cityPort + "\nFlight status: " + status + "\nTerminal: " + terminal + "\nBusiness class price: " + business + "\nEconomy class price: " + economy;
                System.out.println(foundInArrival);
                System.out.println();
                flights.add(new FlightDirection().new Arrival(date, cityPort, time, flightNumber, status, terminal, distance));
            }
            ResultSet searchDeparture = searchCity.executeQuery("select * from Departure where City=\'" + city + "\'");
            while (searchDeparture.next()) {
                String flightNumber = searchDeparture.getString("FlightNumber");
                String date = searchDeparture.getString("Date");
                String time = searchDeparture.getString("Time");
                String cityPort = searchDeparture.getString("City");
                String status = searchDeparture.getString("FlightStatus");
                String terminal = searchDeparture.getString("Terminal");
                double distance = searchDeparture.getDouble("Distance");
                String business = searchDeparture.getString("BusinessPrice");
                String economy = searchDeparture.getString("EconomyPrice");
                String foundInDeparture = "Flight number: " + flightNumber + "\nDate: " + date + "\nTime: " + time + "\nCity: " + cityPort + "\nFlight status: " + status + "\nTerminal: " + terminal + "\nBusiness class price: " + business + "\nEconomy class price: " + economy;
                System.out.println(foundInDeparture);
                System.out.println();
                flights.add(new FlightDirection().new Departure(date, cityPort, time, flightNumber, status, terminal, distance));
            }

            for (FlightDirection throughFlights : flights) {
                if (throughFlights.getCity().equals(city)) {
                    matches++;
                }
            }
            if (matches == 0) {
                System.out.println("No flights to this city");
            } else matches = 0;

        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Searches flights (arrivals and departures) by the flight number. Flight number is mentioned as method parameter.*/
    public void searchFlightsByNumber(String flightNumber) {
        System.out.println("Searching flight..." + flightNumber);
        connectToDB();
        try (Statement searchCity = dbConnection.createStatement()) {
            ResultSet searchArrival = searchCity.executeQuery("select * from Arrival where FlightNumber=\'" + flightNumber + "\'");
            while (searchArrival.next()) {
                String flight = searchArrival.getString("FlightNumber");
                String date = searchArrival.getString("Date");
                String time = searchArrival.getString("Time");
                String cityPort = searchArrival.getString("City");
                String status = searchArrival.getString("FlightStatus");
                String terminal = searchArrival.getString("Terminal");
                double distance = searchArrival.getDouble("Distance");
                String business = searchArrival.getString("BusinessPrice");
                String economy = searchArrival.getString("EconomyPrice");
                String foundInArrival = "\nFlight number: " + flight + "\nDate: " + date + "\nTime: " + time + "\nCity: " + cityPort + "\nFlight status: " + status + "\nTerminal: " + terminal + "\nBusiness class price: " + business + "\nEconomy class price: " + economy;
                System.out.println(foundInArrival);
                System.out.println();
                flights.add(new FlightDirection().new Arrival(date, cityPort, time, flightNumber, status, terminal, distance));
            }
            ResultSet searchDeparture = searchCity.executeQuery("select * from Departure where FlightNumber=\'" + flightNumber + "\'");
            while (searchDeparture.next()) {
                String flight = searchDeparture.getString("FlightNumber");
                String date = searchDeparture.getString("Date");
                String time = searchDeparture.getString("Time");
                String cityPort = searchDeparture.getString("City");
                String status = searchDeparture.getString("FlightStatus");
                String terminal = searchDeparture.getString("Terminal");
                double distance = searchDeparture.getDouble("Distance");
                String business = searchDeparture.getString("BusinessPrice");
                String economy = searchDeparture.getString("EconomyPrice");
                String foundInDeparture = "\nFlight number: " + flight + "\nDate: " + date + "\nTime: " + time + "\nCity: " + cityPort + "\nFlight status: " + status + "\nTerminal: " + terminal + "\nBusiness class price: " + business + "\nEconomy class price: " + economy;
                System.out.println(foundInDeparture);
                System.out.println();
                flights.add(new FlightDirection().new Departure(date, cityPort, time, flightNumber, status, terminal, distance));
            }

            for (FlightDirection throughFlights : flights) {
                if (throughFlights.getFlightNumber().equals(flightNumber)) {
                    matches++;
                }
            }
            if (matches == 0) {
                System.out.println("Entered flight number not found.");
            }
            matches = 0;

        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Searches flights (arrivals and departures) by the price. Price is mentioned as method parameter.
     * User or administrator should know the exact price in order to use this method effectively.*/
    public void searchFlightsByPrice(double price) {
        System.out.println("Searching flights by the price " + price + "...");
        connectToDB();
        try (Statement statement = dbConnection.createStatement()) {
            ResultSet searchByPrice = statement.executeQuery("select * from Arrival where BusinessPrice=" + price + " or EconomyPrice=" + price);
            while (searchByPrice.next()) {
                String flightNumber = searchByPrice.getString("FlightNumber");
                String date = searchByPrice.getString("Date");
                String time = searchByPrice.getString("Time");
                String cityPort = searchByPrice.getString("City");
                String status = searchByPrice.getString("FlightStatus");
                String terminal = searchByPrice.getString("Terminal");
                double distance = searchByPrice.getDouble("Distance");
                String business = searchByPrice.getString("BusinessPrice");
                String economy = searchByPrice.getString("EconomyPrice");
                String foundInArrival = "Flight number: " + flightNumber + "\n" + "Date: " + date + "\n" + "Time: " + time + "\n" + "City: " + cityPort + "\n" + "Flight status: " + status + "\n" + "Terminal: " + terminal + "\n" + "Business class price: " + business + "\n" + "Economy class price: " + economy + "\n";
                System.out.println(foundInArrival);
                System.out.println();
                flights.add(new FlightDirection().new Arrival(date, cityPort, time, flightNumber, status, terminal, distance));
            }
            ResultSet searchByPrice2 = statement.executeQuery("select * from Departure where BusinessPrice=" + price + " or EconomyPrice=" + price);
            while (searchByPrice2.next()) {
                String flightNumber = searchByPrice2.getString("FlightNumber");
                String date = searchByPrice2.getString("Date");
                String time = searchByPrice2.getString("Time");
                String cityPort = searchByPrice2.getString("City");
                String status = searchByPrice2.getString("FlightStatus");
                String terminal = searchByPrice2.getString("Terminal");
                double distance = searchByPrice2.getDouble("Distance");
                String business = searchByPrice2.getString("BusinessPrice");
                String economy = searchByPrice2.getString("EconomyPrice");
                String foundInDeparture = "Flight number: " + flightNumber + "\n" + "Date: " + date + "\n" + "Time: " + time + "\n" + "City: " + cityPort + "\n" + "Flight status: " + status + "\n" + "Terminal: " + terminal + "\n" + "Business class price: " + business + "\n" + "Economy class price: " + economy + "\n";
                System.out.println(foundInDeparture);
                System.out.println();
                flights.add(new FlightDirection().new Departure(date, cityPort, time, flightNumber, status, terminal, distance));
            }

            for (FlightDirection throughFlights : flights) {
                if (throughFlights.getPriceBusiness().equals(price)) {
                    matches++;
                }
                if (throughFlights.getPriceEconomy().equals(price)) {
                    matches++;
                }
            }
            if (matches == 0) {
                System.out.println("No flights found by the entered price");
            } else matches = 0;


        } catch (SQLException e) {
            System.out.println("Database error: no connection.");
        }
    }



    /**
     * Allows to update or delete records in Passengers table of the database.*/
    public void updatePassengersInfo() {
        while (true) {
            System.out.println("Specify search parameter:");
            System.out.println("Flight number - 1");
            System.out.println("First name - 2");
            System.out.println("Last name - 3");
            System.out.println("Passport ID - 4");
            System.out.println("To leave this menu enter \"q\"");
            Scanner selectSearchParameter = new Scanner(System.in);
            String search = selectSearchParameter.nextLine();
            if (search.equals("q")) {
                break;
            }
            switch (search) {
                case "1": {
                    System.out.println("Enter flight number");
                    Scanner flightInput = new Scanner(System.in);
                    String flightNumber = flightInput.nextLine();
                    searchPassengersByFlight(flightNumber);
                    System.out.println("What changes you would like to perform?");
                    System.out.println("Update record - 1");
                    System.out.println("Delete record - 2");
                    System.out.println("Return to the previous menu - 3");
                    Scanner changes = new Scanner(System.in);
                    int changeRecord = changes.nextInt();
                    switch (changeRecord) {
                        case 1: {
                            System.out.println("Which column needs to be updated?");
                            Scanner column = new Scanner(System.in);
                            String columnForUpdate = column.nextLine();
                            if (columnForUpdate.equals("Id")) {
                                System.out.println("Passenger Id cannot be changed.");
                                break;
                            }
                            System.out.println("Enter current value that must be replaced:");
                            String oldValue = column.nextLine();
                            System.out.println("Enter new value of selected column:");
                            String newValue = column.nextLine();
                            connectToDB();
                            try (Statement updateColumn = dbConnection.createStatement()) {
                                updateColumn.execute("update Passengers set " + columnForUpdate + "=\'" + newValue + "\' where " + columnForUpdate + "=\'" + oldValue + "\'");
                                System.out.println("Record updated.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;

                        }
                        case 2: {
                            System.out.println("Enter column name to find a record for deletion:");
                            Scanner column = new Scanner(System.in);
                            String columnId = column.nextLine();
                            System.out.println("Enter value of selected column to complete record removal:");
                            String valueToDelete = column.nextLine();
                            connectToDB();
                            try (Statement deleteRecord = dbConnection.createStatement()) {
                                deleteRecord.execute("delete from Passengers where " + columnId + "=\'" + valueToDelete + "\'");
                                System.out.println("Record deleted.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 3:
                            break;
                    }
                    break;
                }
                case "2": {
                    System.out.println("Enter passenger's first name:");
                    Scanner inputName = new Scanner(System.in);
                    String firstName = inputName.nextLine();
                    searchPassengersByName(firstName);
                    System.out.println("What changes you would like to perform?");
                    System.out.println("Update record - 1");
                    System.out.println("Delete record - 2");
                    System.out.println("Return to the previous menu - 3");
                    Scanner changes = new Scanner(System.in);
                    int changeRecord = changes.nextInt();
                    switch (changeRecord) {
                        case 1: {
                            System.out.println("Which column needs to be updated?");
                            Scanner column = new Scanner(System.in);
                            String columnForUpdate = column.nextLine();
                            if (columnForUpdate.equals("Id")) {
                                System.out.println("Passenger Id cannot be changed.");
                                break;
                            }
                            System.out.println("Enter current value that must be replaced:");
                            String oldValue = column.nextLine();
                            System.out.println("Enter new value of selected column:");
                            String newValue = column.nextLine();
                            connectToDB();
                            try (Statement updateColumn = dbConnection.createStatement()) {
                                updateColumn.execute("update Passengers set " + columnForUpdate + "=\'" + newValue + "\' where " + columnForUpdate + "=\'" + oldValue + "\'");
                                System.out.println("Record updated.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("Enter column name to find a record for deletion:");
                            Scanner column = new Scanner(System.in);
                            String columnId = column.nextLine();
                            System.out.println("Enter value of selected column to complete record removal:");
                            String valueToDelete = column.nextLine();
                            connectToDB();
                            try (Statement deleteRecord = dbConnection.createStatement()) {
                                deleteRecord.execute("delete from Passengers where " + columnId + "=\'" + valueToDelete + "\'");
                                System.out.println("Record deleted.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 3:
                            break;
                    }
                    break;
                }
                case "3": {
                    System.out.println("Enter passenger's last name:");
                    Scanner inputSurname = new Scanner(System.in);
                    String lastName = inputSurname.nextLine();
                    searchPassengersBySurname(lastName);
                    System.out.println("What changes you would like to perform?");
                    System.out.println("Update record - 1");
                    System.out.println("Delete record - 2");
                    System.out.println("Return to the previous menu - 3");
                    Scanner changes = new Scanner(System.in);
                    int changeRecord = changes.nextInt();
                    switch (changeRecord) {
                        case 1: {
                            System.out.println("Which column needs to be updated?");
                            Scanner column = new Scanner(System.in);
                            String columnForUpdate = column.nextLine();
                            if (columnForUpdate.equals("Id")) {
                                System.out.println("Passenger Id cannot be changed.");
                                break;
                            }
                            System.out.println("Enter current value that must be replaced:");
                            String oldValue = column.nextLine();
                            System.out.println("Enter new value of selected column:");
                            String newValue = column.nextLine();
                            connectToDB();
                            try (Statement updateColumn = dbConnection.createStatement()) {
                                updateColumn.execute("update Passengers set " + columnForUpdate + "=\'" + newValue + "\' where " + columnForUpdate + "=\'" + oldValue + "\'");
                                System.out.println("Record updated.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("Enter column name to find a record for deletion:");
                            Scanner column = new Scanner(System.in);
                            String columnId = column.nextLine();
                            System.out.println("Enter value of selected column to complete record removal:");
                            String valueToDelete = column.nextLine();
                            connectToDB();
                            try (Statement deleteRecord = dbConnection.createStatement()) {
                                deleteRecord.execute("delete from Passengers where " + columnId + "=\'" + valueToDelete + "\'");
                                System.out.println("Record deleted.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 3:
                            break;
                    }
                    break;

                }
                case "4": {
                    System.out.println("Enter passport ID to find a passenger:");
                    Scanner inputPassport = new Scanner(System.in);
                    String passport = inputPassport.nextLine();
                    searchPassengersByPassport(passport);
                    System.out.println("What changes you would like to perform?");
                    System.out.println("Update record - 1");
                    System.out.println("Delete record - 2");
                    System.out.println("Return to the previous menu - 3");
                    Scanner changes = new Scanner(System.in);
                    int changeRecord = changes.nextInt();
                    switch (changeRecord) {
                        case 1: {
                            System.out.println("Which column needs to be updated?");
                            Scanner column = new Scanner(System.in);
                            String columnForUpdate = column.nextLine();
                            if (columnForUpdate.equals("Id")) {
                                System.out.println("Passenger Id cannot be changed.");
                                break;
                            }
                            System.out.println("Enter current value that must be replaced:");
                            String oldValue = column.nextLine();
                            System.out.println("Enter new value of selected column:");
                            String newValue = column.nextLine();
                            connectToDB();
                            try (Statement updateColumn = dbConnection.createStatement()) {
                                updateColumn.execute("update Passengers set " + columnForUpdate + "=\'" + newValue + "\' where " + columnForUpdate + "=\'" + oldValue + "\'");
                                System.out.println("Record updated.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("Enter column name to find a record for deletion:");
                            Scanner column = new Scanner(System.in);
                            String columnId = column.nextLine();
                            System.out.println("Enter value of selected column to complete record removal:");
                            String valueToDelete = column.nextLine();
                            connectToDB();
                            try (Statement deleteRecord = dbConnection.createStatement()) {
                                deleteRecord.execute("delete from Passengers where " + columnId + "=\'" + valueToDelete + "\'");
                                System.out.println("Record deleted.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 3:
                            break;
                    }
                    break;
                }
                default: {
                    System.out.println("Select action from the list above.");
                    break;
                }
            }
        }
    }



    /**
     * Allows to update or delete records in Arrival table of the database.*/
    public void updateArrivalsInfo() {
        while (true) {
            System.out.println("Specify search parameter:");
            System.out.println("City - 1");
            System.out.println("Flight number - 2");
            System.out.println("To leave this menu enter \"q\"");
            Scanner searchParameter = new Scanner(System.in);
            String parameter = searchParameter.nextLine();
            if (parameter.equals("q"))
                break;
            switch (parameter) {
                case "1": {
                    System.out.println("Enter city/port:");
                    Scanner inputCity = new Scanner(System.in);
                    String city = inputCity.nextLine();
                    searchFlightsByCity(city);
                    System.out.println("What changes you would like to perform?");
                    System.out.println("Update record - 1");
                    System.out.println("Delete record - 2");
                    System.out.println("Return to the previous menu - 3");
                    Scanner changes = new Scanner(System.in);
                    int changeRecord = changes.nextInt();
                    switch (changeRecord) {
                        case 1: {
                            System.out.println("Which column needs to be updated?");
                            Scanner column = new Scanner(System.in);
                            String columnForUpdate = column.nextLine();
                            System.out.println("Enter current value that must be replaced:");
                            String oldValue = column.nextLine();
                            System.out.println("Enter new value of selected column:");
                            String newValue = column.nextLine();
                            connectToDB();
                            try (Statement updateColumn = dbConnection.createStatement()) {
                                updateColumn.execute("update Arrival set " + columnForUpdate + "=\'" + newValue + "\' where " + columnForUpdate + "=\'" + oldValue + "\'");
                                System.out.println("Record updated.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("Enter column name to find a record for deletion:");
                            Scanner column = new Scanner(System.in);
                            String columnId = column.nextLine();
                            System.out.println("Enter value of selected column to complete record removal:");
                            String valueToDelete = column.nextLine();
                            connectToDB();
                            try (Statement deleteRecord = dbConnection.createStatement()) {
                                deleteRecord.execute("delete from Arrival where " + columnId + "=\'" + valueToDelete + "\'");
                                System.out.println("Record deleted.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 3:
                            break;
                    }
                    break;
                }
                case "2": {
                    System.out.println("Enter flight number:");
                    Scanner inputFlight = new Scanner(System.in);
                    String flightNumber = inputFlight.nextLine();
                    searchFlightsByNumber(flightNumber);
                    System.out.println("What changes you would like to perform?");
                    System.out.println("Update record - 1");
                    System.out.println("Delete record - 2");
                    System.out.println("Return to the previous menu - 3");
                    Scanner changes = new Scanner(System.in);
                    int changeRecord = changes.nextInt();
                    switch (changeRecord) {
                        case 1: {
                            System.out.println("Which column needs to be updated?");
                            Scanner column = new Scanner(System.in);
                            String columnForUpdate = column.nextLine();
                            System.out.println("Enter current value that must be replaced:");
                            String oldValue = column.nextLine();
                            System.out.println("Enter new value of selected column:");
                            String newValue = column.nextLine();
                            connectToDB();
                            try (Statement updateColumn = dbConnection.createStatement()) {
                                updateColumn.execute("update Arrival set " + columnForUpdate + "=\'" + newValue + "\' where " + columnForUpdate + "=\'" + oldValue + "\'");
                                System.out.println("Record updated.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("Enter column name to find a record for deletion:");
                            Scanner column = new Scanner(System.in);
                            String columnId = column.nextLine();
                            System.out.println("Enter value of selected column to complete record removal:");
                            String valueToDelete = column.nextLine();
                            connectToDB();
                            try (Statement deleteRecord = dbConnection.createStatement()) {
                                deleteRecord.execute("delete from Arrival where " + columnId + "=\'" + valueToDelete + "\'");
                                System.out.println("Record deleted.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 3:
                            break;
                    }
                    break;
                }
                case "3": {
                    break;
                }
            }
        }
    }



    /**
     * Allows to update or delete records in Departure table of the database.*/
    public void updateDeparturesInfo(){
        while (true) {
            System.out.println("Specify search parameter:");
            System.out.println("City - 1");
            System.out.println("Flight number - 2");
            System.out.println("To leave this menu enter \"q\"");
            Scanner searchParameter = new Scanner(System.in);
            String parameter = searchParameter.nextLine();
            if (parameter.equals("q"))
                break;
            switch (parameter) {
                case "1": {
                    System.out.println("Enter city/port:");
                    Scanner inputCity = new Scanner(System.in);
                    String city = inputCity.nextLine();
                    searchFlightsByCity(city);
                    System.out.println("What changes you would like to perform?");
                    System.out.println("Update record - 1");
                    System.out.println("Delete record - 2");
                    System.out.println("Return to the previous menu - 3");
                    Scanner changes = new Scanner(System.in);
                    int changeRecord = changes.nextInt();
                    switch (changeRecord) {
                        case 1: {
                            System.out.println("Which column needs to be updated?");
                            Scanner column = new Scanner(System.in);
                            String columnForUpdate = column.nextLine();
                            System.out.println("Enter current value that must be replaced:");
                            String oldValue = column.nextLine();
                            System.out.println("Enter new value of selected column:");
                            String newValue = column.nextLine();
                            connectToDB();
                            try (Statement updateColumn = dbConnection.createStatement()) {
                                updateColumn.execute("update Departure set " + columnForUpdate + "=\'" + newValue + "\' where " + columnForUpdate + "=\'" + oldValue + "\'");
                                System.out.println("Record updated.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("Enter column name to find a record for deletion:");
                            Scanner column = new Scanner(System.in);
                            String columnId = column.nextLine();
                            System.out.println("Enter value of selected column to complete record removal:");
                            String valueToDelete = column.nextLine();
                            connectToDB();
                            try (Statement deleteRecord = dbConnection.createStatement()) {
                                deleteRecord.execute("delete from Departure where " + columnId + "=\'" + valueToDelete + "\'");
                                System.out.println("Record deleted.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 3:
                            break;
                    }
                    break;
                }
                case "2": {
                    System.out.println("Enter flight number:");
                    Scanner inputFlight = new Scanner(System.in);
                    String flightNumber = inputFlight.nextLine();
                    searchFlightsByNumber(flightNumber);
                    System.out.println("What changes you would like to perform?");
                    System.out.println("Update record - 1");
                    System.out.println("Delete record - 2");
                    System.out.println("Return to the previous menu - 3");
                    Scanner changes = new Scanner(System.in);
                    int changeRecord = changes.nextInt();
                    switch (changeRecord) {
                        case 1: {
                            System.out.println("Which column needs to be updated?");
                            Scanner column = new Scanner(System.in);
                            String columnForUpdate = column.nextLine();
                            System.out.println("Enter current value that must be replaced:");
                            String oldValue = column.nextLine();
                            System.out.println("Enter new value of selected column:");
                            String newValue = column.nextLine();
                            connectToDB();
                            try (Statement updateColumn = dbConnection.createStatement()) {
                                updateColumn.execute("update Departure set " + columnForUpdate + "=\'" + newValue + "\' where " + columnForUpdate + "=\'" + oldValue + "\'");
                                System.out.println("Record updated.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("Enter column name to find a record for deletion:");
                            Scanner column = new Scanner(System.in);
                            String columnId = column.nextLine();
                            System.out.println("Enter value of selected column to complete record removal:");
                            String valueToDelete = column.nextLine();
                            connectToDB();
                            try (Statement deleteRecord = dbConnection.createStatement()) {
                                deleteRecord.execute("delete from Departure where " + columnId + "=\'" + valueToDelete + "\'");
                                System.out.println("Record deleted.");
                            } catch (SQLException e) {
                                System.out.println("Database error: no connection.");
                            }
                            break;
                        }
                        case 3:
                            break;
                    }
                    break;
                }
                case "3": {
                    break;
                }
            }
        }
    }



    /**
     * Allows administrator to view passenger list of the specified flight number. Flight number is method parameter. */
    public void printPassengersList(String flightNumber){
        connectToDB();
        JFrame frame = new JFrame("Passengers registered for flight " + flightNumber);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try(Statement searchPassengers = dbConnection.createStatement()){
            ResultSet sortedByFlight = searchPassengers.executeQuery("select * from Passengers where FlightNumber=\'" + flightNumber + "\'");
            String[] columnNames = {"Id", "FlightNumber", "FirstName", "LastName", "Sex", "Nationality", "DateOfBirth", "Passport", "ClassOfTrip"};
            while(sortedByFlight.next()){
                String passengerId = sortedByFlight.getString("Id");
                String flight = sortedByFlight.getString("FlightNumber");
                String name = sortedByFlight.getString("FirstName");
                String surname = sortedByFlight.getString("LastName");
                String sex = sortedByFlight.getString("Sex");
                String nationality = sortedByFlight.getString("Nationality");
                String dateOfBirth = sortedByFlight.getString("DateOfBirth");
                String passport = sortedByFlight.getString("Passport");
                String classOfTrip = sortedByFlight.getString("ClassOfTrip");
                String[] oneRow = {passengerId, flight, name, surname, sex, nationality, dateOfBirth, passport, classOfTrip};
                sortedByFlightPassengers.add(oneRow);
                passengers.add(new PassengersList(flight,name,surname,sex,nationality,dateOfBirth,passport,classOfTrip));

            }
            for (PassengersList throughPassengers: passengers){
                if (throughPassengers.getFlight().equals(flightNumber)){
                    matches++;
                }

            }
            if (matches==0){
                System.out.println("No passengers registered on this flight.");
            }else{
            matches=0;
            String[][] rowsOfTable = sortedByFlightPassengers.toArray(new String[sortedByFlightPassengers.size()][]);
            JTable table = new JTable(rowsOfTable, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            frame.getContentPane().add(scrollPane);
            frame.setPreferredSize(new Dimension(450, 200));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            }
        }catch (SQLException e){
            System.out.println("Database error: no connection.");
        }
    }
}

