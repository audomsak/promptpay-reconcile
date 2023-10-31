package com.github.audomsak.poc.promptpay.reconcile.persistence.converter;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTimeConverterTest {

    TransactionTimeConverter converter = new TransactionTimeConverter();

    @Test
    void convertToDatabaseColumn() {
        LocalTime localTime = LocalTime.of(23, 59, 59);
        String time = converter.convertToDatabaseColumn(localTime);
        assertThat(time).isEqualTo("235959");

    }

    @Test
    void convertToEntityAttribute() {
        LocalTime localTime = converter.convertToEntityAttribute("235959");
        assertThat(localTime)
                .isNotNull()
                .hasToString("23:59:59");
    }

    @Test
    void shouldThrowExceptionWhenValueIsInvalid() {
        assertThrows(DateTimeException.class, () ->
                converter.convertToEntityAttribute("255959")
        );
    }
}