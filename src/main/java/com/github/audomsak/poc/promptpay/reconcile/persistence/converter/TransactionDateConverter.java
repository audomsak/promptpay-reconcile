package com.github.audomsak.poc.promptpay.reconcile.persistence.converter;

import com.github.audomsak.poc.promptpay.reconcile.util.DateTimeUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;

@Converter
public class TransactionDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        return DateTimeUtils.format(attribute);
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return DateTimeUtils.toLocalDate(dbData);
    }
}
