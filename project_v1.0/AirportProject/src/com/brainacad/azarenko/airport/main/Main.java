package com.brainacad.azarenko.airport.main;
import com.brainacad.azarenko.airport.actions.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>Main</b> class performs authorization to divide application's functionality between admin and user.
 * After successful authorization program menu is shown allowing admin or user to select desirable option.
 * Depending on user's choice specific methods are invoked via instance of Admin or User classes.
 * @author Lyudmila Azarenko
 * @version 1.0
 * @see com.brainacad.azarenko.airport.actions.Admin
 * @see com.brainacad.azarenko.airport.actions.User*/
public class Main {
    public static void main(String[]   args){
        System.out.println("WELCOME TO AIRPORT MANAGEMENT SYSTEM!");
        System.out.println();
        System.out.println("You need to authorize to proceed.");
        System.out.println("Please enter your username:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        String usernameCheck = "(admin)?(user)?";
        Pattern pattern = Pattern.compile(usernameCheck);
        Matcher matcher = pattern.matcher(username);
        if (matcher.matches()){
            System.out.println("Enter your password:");
            String password = scanner.nextLine();
            String userPasswordCheck = "(user)";
            String adminPasswordCheck = "(admin)";

            Pattern patternUser = Pattern.compile(userPasswordCheck);
            Matcher matcherUser = patternUser.matcher(password);
            Pattern patternAdmin = Pattern.compile(adminPasswordCheck);
            Matcher matcherAdmin = patternAdmin.matcher(password);
            if (username.equals("user")&&matcherUser.matches()){
                System.out.println("Authorization successful");
                System.out.println();
                User user = new User();
                while(true) {
                    userActions();
                    Scanner userInput = new Scanner(System.in);
                    String userAction = userInput.nextLine();
                    if (userAction.equals("exit"))
                        break;
                    switch (userAction) {
                        case "1":
                            user.printArrivalInfo();
                            break;
                        case "2":
                            user.printDepartureInfo();
                            break;
                        case "3": {
                            System.out.println("Enter city/port:");
                            String city = userInput.nextLine();
                            user.searchFlightsByCity(city);
                            break;
                        }
                        case "4": {
                            System.out.println("Enter a price (i.e. 110.55):");
                            double price = userInput.nextDouble();
                            user.searchFlightsByPrice(price);
                            break;

                        }
                        case "5":{
                            System.out.println("Enter flight number:");
                            Scanner scanner1 = new Scanner(System.in);
                            String flightNumber = scanner1.nextLine();
                            user.searchFlightsByNumber(flightNumber);
                            break;
                        }
                        default:
                            System.out.println("Unavailable action. Please select one from the list above");
                    }
                }
            } else if (username.equals("admin")&&matcherAdmin.matches()){
                System.out.println("Authorization successful");
                Admin admin = new Admin();
                Scanner application = new Scanner(System.in);
                String adminAction;
                while(true){
                    adminActions();
                    adminAction = application.nextLine();
                    if (adminAction.equals("exit"))
                        break;
                    switch (adminAction) {
                        case "1": {
                            System.out.println("Creating new flight(arrival)...");
                            System.out.println("Enter date of the flight (i.e. 1 NOV 2016):");
                            Scanner scanner1 = new Scanner(System.in);
                            String date = scanner1.nextLine();
                            System.out.println("Enter city/port:");
                            String city = scanner1.nextLine();
                            System.out.println("Enter time of arrival:");
                            String time = scanner1.nextLine();
                            System.out.println("Enter flight number:");
                            String flightNumber = scanner1.nextLine();
                            System.out.println("Select flight status(CHECK_IN, GATE_CLOSED, ARRIVED, DEPARTED_AT, UNKNOWN, CANCELED, EXPECTED_AT, DELAYED, IN_FLIGHT):");
                            String status = scanner1.nextLine();
                            System.out.println("Enter a terminal:");
                            String terminal = scanner1.nextLine();
                            System.out.println("Enter a distance of the flight (in kilometers):");
                            Double distance = scanner1.nextDouble();
                            admin.createNewArrivalInfo(date, city, time, flightNumber, status, terminal, distance);
                            System.out.println("New flight information created.");
                            break;
                        }
                        case "2": {
                            System.out.println("Creating new flight(departure)...");
                            System.out.println("Enter date of the flight (i.e. 1 NOV 2016)");
                            Scanner scanner1 = new Scanner(System.in);
                            String date = scanner1.nextLine();
                            System.out.println("Enter city/port:");
                            String city = scanner1.nextLine();
                            System.out.println("Enter time of departure:");
                            String time = scanner1.nextLine();
                            System.out.println("Enter flight number:");
                            String flightNumber = scanner.nextLine();
                            System.out.println("Select flight status(CHECK_IN, GATE_CLOSED, ARRIVED, DEPARTED_AT, UNKNOWN, CANCELED, EXPECTED_AT, DELAYED, IN_FLIGHT):");
                            String status = scanner1.nextLine();
                            System.out.println("Enter a terminal:");
                            String terminal = scanner1.nextLine();
                            System.out.println("Enter a distance of the flight (in kilometers):");
                            Double distance = scanner1.nextDouble();
                            admin.createNewDepartureInfo(date, city, time, flightNumber, status, terminal, distance);
                            System.out.println("New flight information created.");
                            break;
                        }
                        case "3": {
                            System.out.println("Creating new passengers list...");
                            System.out.println("Enter flight number this list should be assigned to:");
                            Scanner scanner1 = new Scanner(System.in);
                            String flight = scanner1.nextLine();
                            System.out.println("Enter passenger's first name:");
                            String firstName = scanner1.nextLine();
                            System.out.println("Enter passenger's last name:");
                            String lastName = scanner1.nextLine();
                            System.out.println("Define passenger's sex (MALE, FEMALE):");
                            String sex = scanner1.nextLine();
                            System.out.println("Enter passenger's nationality:");
                            String nationality = scanner1.nextLine();
                            System.out.println("Enter passenger's date of birthday:");
                            String dateOfBirth = scanner1.nextLine();
                            System.out.println("Enter a number of the passenger's passport:");
                            String passport = scanner1.nextLine();
                            System.out.println("Define a class for the passenger(BUSINESS, ECONOMY):");
                            String classOfTrip = scanner1.nextLine();
                            admin.createListPassengers(flight, firstName, lastName, sex, nationality, dateOfBirth, passport, classOfTrip);
                            break;
                        }
                        case "4": {
                            System.out.println("Definition of search parameters required. Enter a flight number:");
                            Scanner scanner1 = new Scanner(System.in);
                            String flightNumber = scanner1.nextLine();
                            admin.searchPassengersByFlight(flightNumber);
                            break;
                        }
                        case "5": {
                            System.out.println("Definition of search parameters required. Enter passenger's first name:");
                            Scanner scanner1 = new Scanner(System.in);
                            String name = scanner1.nextLine();
                            admin.searchPassengersByName(name);
                            break;
                        }
                        case "6": {
                            System.out.println("Definition of search parameters required. Enter passenger's last name:");
                            Scanner scanner1 = new Scanner(System.in);
                            String surname = scanner1.nextLine();
                            admin.searchPassengersBySurname(surname);
                            break;
                        }
                        case "7": {
                            System.out.println("Definition of search parameters required. Enter passenger's passport number:");
                            Scanner scanner1 = new Scanner(System.in);
                            String passport = scanner1.nextLine();
                            admin.searchPassengersByPassport(passport);
                            break;
                        }
                        case "8": {
                            System.out.println("All available arrivals will be shown in a separate window.");
                            System.out.println();
                            admin.printArrivalInfo();
                            break;
                        }
                        case "9": {
                            System.out.println("All available departures will be shown in a separate window.");
                            System.out.println();
                            admin.printDepartureInfo();
                            break;
                        }
                        case "10": {
                            System.out.println("Definition of search parameters required. Enter city/port");
                            Scanner scanner1 = new Scanner(System.in);
                            String city = scanner1.nextLine();
                            admin.searchFlightsByCity(city);
                            break;
                        }
                        case "11": {
                            System.out.println("Definition of search parameters required. Enter a price:");
                            Scanner scanner1 = new Scanner(System.in);
                            Double price = scanner1.nextDouble();
                            admin.searchFlightsByPrice(price);
                            break;
                        }
                        case "12":{
                            System.out.println("Definition of search parameters required. Enter flight number:");
                            Scanner scanner1 = new Scanner(System.in);
                            String flightNumber = scanner1.nextLine();
                            admin.searchFlightsByNumber(flightNumber);
                            break;
                        }
                        case "13":{
                            admin.updatePassengersInfo();
                            break;
                        }
                        case "14":{
                            admin.updateArrivalsInfo();
                            break;
                        }
                        case "15":{
                            admin.updateDeparturesInfo();
                        }
                        case "16": {
                            System.out.println("Enter flight number to view passengers list:");
                            Scanner scanner1 = new Scanner(System.in);
                            String inputFlight = scanner1.nextLine();
                            admin.printPassengersList(inputFlight);
                            break;
                        }
                        default:
                            System.out.println("Unavailable action. Please select one from the list above");

                    }
                }

            } else System.out.println("Access denied. Entered password is incorrect.");
        } else {
            System.out.println("Wrong username. Try again later.");
            scanner.close();
        }

    }

    private static void adminActions(){
        System.out.println("Select action you would like to perform:");
        System.out.println("Create new flight information (arrival) - 1");
        System.out.println("Create new flight information (departure) - 2");
        System.out.println("Add new passenger to the list - 3");
        System.out.println("Search passengers by flight number - 4");
        System.out.println("Search passengers by first name - 5");
        System.out.println("Search passengers by second name - 6");
        System.out.println("Search passengers by passport number - 7");
        System.out.println("View all arrivals information - 8");
        System.out.println("View all departures information - 9");
        System.out.println("Search flights by city/port - 10");
        System.out.println("Search flights by price - 11");
        System.out.println("Search flights by flight number - 12");
        System.out.println("Update/delete records in Passengers list - 13");
        System.out.println("Update/delete arrival information - 14");
        System.out.println("Update/delete departure information - 15");
        System.out.println("View passengers list - 16");
        System.out.println("To shutdown the application enter \"exit\". Make sure that all information windows are closed.");
    }

    private static void userActions(){
        System.out.println("Select action you would like to perform:");
        System.out.println("View arrivals information - 1");
        System.out.println("View departures information - 2");
        System.out.println("Search flights by city/port - 3");
        System.out.println("Search flights by price - 4");
        System.out.println("Search flights by flight number - 5");
        System.out.println("To shutdown the application enter \"exit\". Make sure that all information windows are closed.");
    }
}
