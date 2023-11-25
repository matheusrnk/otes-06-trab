package org.contacts.server_api_contacts.services;

import org.contacts.server_api_contacts.entities.Contact;
import org.contacts.server_api_contacts.entities.Person;
import org.contacts.server_api_contacts.repositories.ContactRepository;
import org.contacts.server_api_contacts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PersonRepository personRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public Contact createContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteContactById(Long id) {
        contactRepository.deleteById(id);
    }

    public Contact createContactForPerson(Long personId, Contact contact) {
        Optional<Person> optionalPerson = personRepository.findById(personId);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            contact.setPerson(person);
            return contactRepository.save(contact);
        } 
        return null;
    }
    
}

