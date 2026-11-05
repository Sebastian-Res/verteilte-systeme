package de.thm.mni.vs.gruppeF.client;

import java.util.Scanner;

import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

public class Client {
    private static final RestClient restClient = RestClient.create("http://localhost:8080");

    public static void main(String[] args) {
        System.out.println("Mögliche Commands:");
        System.out.println("\tp <nachricht>\tSendet eine Nachricht");
        System.out.println("\tl\tListet alle Nachrichten auf");
        System.out.println("\tl <id>\tListet die Nachricht mit der <id> auf");
        System.out.println("\tq\tBeendet den Client");

        Scanner inputScanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.print("$ ");
            String input = inputScanner.nextLine().strip();
            if (input.isEmpty()) {
                continue;
            }

            // Befehl aufteilen in den eigentlichen Befehl und Arguemnt
            String[] inputParts = input.split(" ", 2);
            String command = inputParts[0].toLowerCase();
            String argument = inputParts.length == 2 ? inputParts[1] : "";

            try {
                switch (command) {
                    case "q":
                        exit = true;
                        break;
                    case "p":
                        sendMessage(argument);
                        break;
                    case "l":
                        getMessages(argument);
                        break;
                    default:
                        System.out.println("Unbekannter Befehl");
                }
            } catch (RestClientResponseException exception) {
                System.out.println("Error: HTTP " + exception.getStatusCode().value());
            } catch (ResourceAccessException exception) {
                System.out.println("Server nicht erreichbar oder Verbindungsfehler");
            } catch (RestClientException exception) {
                System.out.println("Fehler bei der Anfrage: " + exception.getMessage());
            }
        }
        inputScanner.close();
    }

    // Methode um alle Nachrichten oder eine einzelne Nachricht bei der API
    // abzurufen
    private static void getMessages(String argument) {
        // Alle Nachrichten, da keine Id angegeben
        if (argument.isEmpty()) {
            String messages = restClient.get()
                    .uri("/messages")
                    .retrieve()
                    .body(String.class);

            System.out.println("Anwort: " + messages);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            System.out.println("Id muss eine ganze Zahl sein");
            return;
        }

        String message = restClient.get()
                .uri("/messages/{id}", id)
                .retrieve()
                .body(String.class);
        System.out.println("Anwort für id " + id + ": " + message);
    }

    // Methode um der API eine neue Nachricht als String zu schicken
    private static void sendMessage(String message) {
        if (message.isBlank()) {
            System.out.println("Bitte eine Nachricht angeben: p <nachricht>");
            return;
        }

        String response = restClient.post()
                .uri("/messages")
                .contentType(MediaType.TEXT_PLAIN)
                .body(message)
                .retrieve()
                .body(String.class);
        System.out.println("Anwort: " + response);
    }
}
