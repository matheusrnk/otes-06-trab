package org.contacts.server_api_contacts.repositories;

import org.contacts.server_api_contacts.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    //Adicionar queries personalizadas se necessário
}

