package de.thm.mni.vs.gruppeF.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thm.mni.vs.gruppeF.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService service;

    // Das Service-Objekt wird per Dependency-Injection eingebettet
    // Der Controller leitet nur die Requests an den Service weiter
    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping()
    // Gibt die komplette Liste zurück
    public List<String> getMessages() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public String getMessage(@PathVariable("id") int id) {
        return service.getById(id);
    }

    @PostMapping()
    // Post Mapping, wo die Nachricht über den Service hinzugefügt wird und dessen
    // Id als Antwort gesendet wird
    public ResponseEntity<Integer> addMessage(@RequestBody String message) {
        int id = service.add(message);
        return ResponseEntity.created(URI.create("/messages/" + id)).body(id);
    }
}
