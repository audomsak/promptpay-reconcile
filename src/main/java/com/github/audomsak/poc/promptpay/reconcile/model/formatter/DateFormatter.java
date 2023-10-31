package com.github.audomsak.poc.promptpay.reconcile.model.formatter;

import com.github.audomsak.poc.promptpay.reconcile.util.DateTimeUtils;
import org.apache.camel.dataformat.bindy.Format;

import java.time.LocalDate;

public class DateFormatter implements Format<LocalDate> {
    @Override
    public String format(LocalDate date) throws Exception {
        return DateTimeUtils.format(date);
    }

    @Override
    public LocalDate parse(String date) throws Exception {
        return DateTimeUtils.toLocalDate(date);
    }
}
