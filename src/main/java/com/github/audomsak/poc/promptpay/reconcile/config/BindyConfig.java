package com.github.audomsak.poc.promptpay.reconcile.config;

import com.github.audomsak.poc.promptpay.reconcile.model.InputHeader;
import com.github.audomsak.poc.promptpay.reconcile.model.InputBody;
import org.apache.camel.dataformat.bindy.fixed.BindyFixedLengthDataFormat;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class BindyConfig {

    @Produces
    @Named("inputHeaderFormat")
    public BindyFixedLengthDataFormat headerFormat() {
        return new BindyFixedLengthDataFormat(InputHeader.class);
    }

    @Produces
    @Named("inputBodyFormat")
    public BindyFixedLengthDataFormat inputFormat() {
        return new BindyFixedLengthDataFormat(InputBody.class);
    }
}
