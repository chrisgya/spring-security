package com.chrisgya.springsecurity.dao;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;


public class CustomPropertySqlParameterSource extends BeanPropertySqlParameterSource {
    private final BeanWrapper beanWrapper;

    public CustomPropertySqlParameterSource(Object object) {
        super(object);
        this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
    }

    public Object getValue(String paramName) throws IllegalArgumentException {
        try {
            Class<?> propType = this.beanWrapper.getPropertyType(paramName);
            if (LocalDateTime.class.equals(propType)) {
                LocalDateTime localDateTime = (LocalDateTime)this.beanWrapper.getPropertyValue(paramName);
                if (localDateTime != null) {
                    return Timestamp.valueOf(localDateTime);
                }
            } else if (LocalDate.class.equals(propType)) {
                LocalDate localDate = (LocalDate)this.beanWrapper.getPropertyValue(paramName);
                if (localDate != null) {
                    Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                    return Date.from(instant);
                }
            } else if (LocalTime.class.equals(propType)) {
                LocalTime localTime = (LocalTime)this.beanWrapper.getPropertyValue(paramName);
                if (localTime != null) {
                    return Time.valueOf(localTime);
                }
            }

            return this.beanWrapper.getPropertyValue(paramName);
        } catch (NotReadablePropertyException var5) {
            throw new IllegalArgumentException(var5.getMessage());
        }
    }
}
