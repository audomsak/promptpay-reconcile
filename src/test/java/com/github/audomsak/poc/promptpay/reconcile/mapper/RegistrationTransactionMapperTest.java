package com.github.audomsak.poc.promptpay.reconcile.mapper;

import com.github.audomsak.poc.promptpay.reconcile.model.dto.RegistrationTransactionDto;
import com.github.audomsak.poc.promptpay.reconcile.persistence.RegistrationTransactionEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class RegistrationTransactionMapperTest {
    @Inject
    RegistrationTransactionMapper mapper;

    @Test
    @Transactional
    void toPojo() {
        RegistrationTransactionEntity rte = new RegistrationTransactionEntity();
        rte.referenceNo = "test";
        rte.proxyType = "MSISDN";
        rte.proxyValue = "1234567890";
        rte.transactionStatus = "PD";
        rte.transactionType = "DGIS";
        rte.transactionDate = LocalDate.of(2023, 12, 31);
        rte.transactionTime = LocalTime.of(23, 59, 59);
        rte.persist();

        RegistrationTransactionEntity entity = RegistrationTransactionEntity.findById("test");
        RegistrationTransactionDto dto = mapper.toPojo(entity);

        assertThat(dto.getReferenceNo()).isEqualTo(rte.referenceNo);
        assertThat(dto.getProxyType()).isEqualTo(rte.proxyType);
        assertThat(dto.getProxyValue()).isEqualTo(rte.proxyValue);
        assertThat(dto.getTransactionStatus()).isEqualTo(rte.transactionStatus);
        assertThat(dto.getTransactionType()).isEqualTo(rte.transactionType);
        assertThat(dto.getTransactionDate()).isEqualTo(rte.transactionDate);
        assertThat(dto.getTransactionTime()).isEqualTo(rte.transactionTime);

        rte.delete(); // clean up
    }
}