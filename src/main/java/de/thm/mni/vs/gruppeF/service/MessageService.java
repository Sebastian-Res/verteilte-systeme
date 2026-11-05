package de.thm.mni.vs.gruppeF.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageService {
    private final List<String> messages = new ArrayList<>();

    // Gibt alle Nachrichten zurück
    public List<String> getAll() {
        return messages;
    }

    // Gibt eine Nachricht zurück, wenn eine Nachricht mit der angefragten Id in der
    // Liste ist
    public String getById(int id) {
        if (id < 0 || id >= messages.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Message with id " + id + " not found");
        }
        return messages.get(id);
    }

    // Fügt eine Nachricht der Liste hinzu und gibt deren Id zurück
    public int add(String message) {
        int id = messages.size();
        messages.add(message);
        return id;
    }
}
