package com.data.contacts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactsRepository extends JpaRepository<Contacts, String>, JpaSpecificationExecutor<Contacts> {

    Contacts getContactsByDeviceId(String deviceId);

}
