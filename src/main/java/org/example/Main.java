package org.example;

import org.example.model.ReadFromFile;
import org.example.model.USER;
import org.example.service.USER_DB;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        while (true) {
            USER_DB userDAO = new USER_DB();
            ReadFromFile readFromFile = new ReadFromFile();
            USER newUser = new USER(readFromFile.readFile().get(0), readFromFile.readFile().get(1),
                    readFromFile.readFile().get(2), readFromFile.readFile().get(3));

            if (readFromFile.readFile().get(4).equals("s")) {
                newUser = userDAO.saveUser(newUser);
                System.out.println("Saved user: " + newUser);
            } else if (readFromFile.readFile().get(4).equals("u")) {
                newUser.setSurname("Kanybekov");
                newUser = userDAO.updateUser(newUser);
                System.out.println("Updated user: " + newUser);
            } else if (readFromFile.readFile().get(4).equals("f")) {
                Long userId = newUser.getId();
                USER retrievedUser = userDAO.getUser(userId);
                System.out.println("Retrieved user: " + retrievedUser);
            } else if (readFromFile.readFile().get(4).equals("d")) {
                Long userId = newUser.getId();
                userDAO.deleteUser(userId);
                System.out.println("Deleted user with ID: " + userId);
            } else {
                List<USER> allUsers2 = userDAO.getAllUsers();
                for (USER user : allUsers2) {
                    System.out.println("All users: " + user.getSurname());
                }
            }
            Thread.sleep(15000);
        }
    }
}