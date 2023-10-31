package com.github.audomsak.poc.promptpay.reconcile.model.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class RegistrationTransactionDto {

    private String referenceNo;

    private LocalDate transactionDate;

    private LocalTime transactionTime;

    private String transactionType;

    private String transactionStatus;

    private String proxyType;

    private String proxyValue;
}
