package com.github.audomsak.poc.promptpay.reconcile.model;

import com.github.audomsak.poc.promptpay.reconcile.model.formatter.DateFormatter;
import com.github.audomsak.poc.promptpay.reconcile.model.formatter.TimeFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.dataformat.bindy.annotation.BindyConverter;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@FixedLengthRecord
public class InputBody {
    @DataField(pos = 1, length = 12, trim = true)
    private String itmxRegistrationId;

    @DataField(pos = 13, length = 12, trim = true)
    private String proxyType;

    @DataField(pos = 25, length = 100, trim = true)
    private String proxyValue;

    @DataField(pos = 125, length = 8, trim = true)
    @BindyConverter(DateFormatter.class)
    private LocalDate transactionDate;

    @DataField(pos = 133, length = 6, trim = true)
    @BindyConverter(TimeFormatter.class)
    private LocalTime transactionTime;

    @DataField(pos = 139, length = 4, trim = true)
    private String transactionType;
}
