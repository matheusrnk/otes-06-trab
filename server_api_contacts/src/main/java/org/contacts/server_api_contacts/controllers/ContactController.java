package org.contacts.server_api_contacts.controllers;

import org.contacts.server_api_contacts.entities.Contact;
import org.contacts.server_api_contacts.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable("id") Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        return contact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/persons/{personId}/contacts")
    public ResponseEntity<Contact> createContactForPerson(@PathVariable("personId") Long personId, @RequestBody Contact contact) {
        Contact createdContact = contactService.createContactForPerson(personId, contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable("id") Long id) {
        contactService.deleteContactById(id);
        return ResponseEntity.noContent().build();
    }
}

