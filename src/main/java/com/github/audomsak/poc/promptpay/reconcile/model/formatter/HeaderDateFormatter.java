package com.github.audomsak.poc.promptpay.reconcile.model.formatter;

import com.github.audomsak.poc.promptpay.reconcile.util.DateTimeUtils;
import org.apache.camel.dataformat.bindy.Format;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.substringAfter;

public class HeaderDateFormatter implements Format<LocalDate> {

    @Override
    public String format(LocalDate date) throws Exception {
        return DateTimeUtils.format(date);
    }

    @Override
    public LocalDate parse(String date) throws Exception {
        return DateTimeUtils.toLocalDate(substringAfter(date, "head"));
    }
}
