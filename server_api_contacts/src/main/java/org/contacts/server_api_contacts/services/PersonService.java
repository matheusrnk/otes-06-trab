package org.contacts.server_api_contacts.services;

import org.contacts.server_api_contacts.entities.Person;
import org.contacts.server_api_contacts.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    public Person createPerson(Person person) {
        String hashedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(hashedPassword);
        
        return personRepository.save(person);
    }

    public Optional<Person> authenticatePerson(String phoneNumber, String password) {
        Optional<Person> optionalPerson = personRepository.findByPhoneNumber(phoneNumber);

        if(optionalPerson.isPresent()){
            Person person = optionalPerson.get();
            if(passwordEncoder.matches(password, person.getPassword())){
                return optionalPerson;
            }
        }

        return Optional.empty();
    }

    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

}


