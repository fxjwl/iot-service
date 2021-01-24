package com.data.base;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

import java.io.Serializable;

public class UUIDGenerater extends UUIDGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (object instanceof BaseEntity) {
            String id = ((BaseEntity) object).getId();
            if (id != null)
                return id;
        }

        String uuid = (String) super.generate(session, object);
        return uuid.replaceAll("-", "");
    }
}