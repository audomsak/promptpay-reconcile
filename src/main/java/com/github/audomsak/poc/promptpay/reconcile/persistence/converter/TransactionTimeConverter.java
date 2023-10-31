package com.github.audomsak.poc.promptpay.reconcile.persistence.converter;

import com.github.audomsak.poc.promptpay.reconcile.util.DateTimeUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalTime;

@Converter
public class TransactionTimeConverter implements AttributeConverter<LocalTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalTime attribute) {
        return DateTimeUtils.format(attribute);
    }

    @Override
    public LocalTime convertToEntityAttribute(String dbData) {
        return DateTimeUtils.toLocalTime(dbData);
    }
}
