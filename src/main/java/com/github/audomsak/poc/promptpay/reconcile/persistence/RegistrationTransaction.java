package com.github.audomsak.poc.promptpay.reconcile.persistence;


import com.github.audomsak.poc.promptpay.reconcile.persistence.converter.TransactionDateConverter;
import com.github.audomsak.poc.promptpay.reconcile.persistence.converter.TransactionTimeConverter;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@NamedNativeQuery(name = RegistrationTransaction.GET_LATEST_STATUS_BY_PROXY_TYPE_AND_VALUE,
        query = "SELECT * FROM PPAR_REGISTRATION_TRANSACTION " +
                "WHERE PROXY_TYPE=:proxyType AND PROXY_VALUE=:proxyValue " +
                "ORDER BY TO_TIMESTAMP(CONCAT(TRANSACTION_DATE, TRANSACTION_TIME),'YYYYMMDDHH24MISS') DESC " +
                "LIMIT 1",
        resultClass = RegistrationTransaction.class)

@Entity(name = "RegistrationTransaction")
@Table(name = "PPAR_REGISTRATION_TRANSACTION")
public class RegistrationTransaction extends PanacheEntityBase {
    public static final String GET_LATEST_STATUS_BY_PROXY_TYPE_AND_VALUE = "getLatestStatusByProxyTypeAndValue";

    @Id
    @Column(name = "REFERENCE_NO", length = 14, unique = true, nullable = false)
    public String referenceNo;

    @Column(name = "TRANSACTION_DATE", length = 8)
    @Convert(converter = TransactionDateConverter.class)
    public LocalDate transactionDate;

    @Column(name = "TRANSACTION_TIME", length = 6)
    @Convert(converter = TransactionTimeConverter.class)
    public LocalTime transactionTime;

    @Column(name = "TRANSACTION_TYPE", length = 4)
    public String transactionType;

    @Column(name = "TRANSACTION_STATUS", length = 2)
    public String transactionStatus;

    @Column(name = "PROXY_TYPE", length = 12)
    public String proxyType;

    @Column(name = "PROXY_VALUE", length = 100)
    public String proxyValue;
}
