package com.github.audomsak.poc.promptpay.reconcile.model.formatter;

import com.github.audomsak.poc.promptpay.reconcile.util.DateTimeUtils;
import org.apache.camel.dataformat.bindy.Format;

import java.time.LocalTime;

public class TimeFormatter implements Format<LocalTime> {
    @Override
    public String format(LocalTime time) throws Exception {
        return DateTimeUtils.format(time);
    }

    @Override
    public LocalTime parse(String time) throws Exception {
        return DateTimeUtils.toLocalTime(time);
    }
}
