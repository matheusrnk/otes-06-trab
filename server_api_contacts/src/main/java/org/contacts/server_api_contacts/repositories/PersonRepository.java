package org.contacts.server_api_contacts.repositories;

import org.contacts.server_api_contacts.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    //Adicionar queries personalizadas se necessário
}
