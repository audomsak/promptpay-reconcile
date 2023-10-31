package com.github.audomsak.poc.promptpay.reconcile.model;

import com.github.audomsak.poc.promptpay.reconcile.model.formatter.HeaderDateFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.dataformat.bindy.annotation.BindyConverter;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@FixedLengthRecord
public class InputHeader {
    @DataField(pos = 1, length = 12, trim = true)
    @BindyConverter(HeaderDateFormatter.class)
    private LocalDate transactionDate;
}
