package com.github.audomsak.poc.promptpay.reconcile.persistence.converter;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionDateConverterTest {
    TransactionDateConverter converter = new TransactionDateConverter();

    @Test
    void convertToDatabaseColumn() {
        LocalDate localDate = LocalDate.of(2023, 12, 31);
        String date = converter.convertToDatabaseColumn(localDate);
        assertThat(date).isEqualTo("20231231");
    }

    @Test
    void convertToEntityAttribute() {
        LocalDate localDate = converter.convertToEntityAttribute("20231231");
        assertThat(localDate)
                .isNotNull()
                .hasToString("2023-12-31");
    }

    @Test
    void shouldThrowExceptionWhenValueIsInvalid() {
        assertThrows(DateTimeException.class, () ->
                converter.convertToEntityAttribute("20131332")
        );
    }
}