package com.github.audomsak.poc.promptpay.reconcile.persistence;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.github.audomsak.poc.promptpay.reconcile.persistence.RegistrationTransactionEntity.FIND_LATEST_BY_PROXY_TYPE_AND_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class RegistrationTransactionEntityTest {

    @Test
    void shouldReturnAllEntities() {
        List<RegistrationTransactionEntity> rts = RegistrationTransactionEntity.listAll();
        assertThat(rts)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    @Transactional
    void shouldPersistEntity() {
        RegistrationTransactionEntity rte = new RegistrationTransactionEntity();
        rte.referenceNo = "test";
        rte.proxyType = "MSISDN";
        rte.proxyValue = "1234567890";
        rte.transactionStatus = "PD";
        rte.transactionDate = LocalDate.of(2023, 12, 31);
        rte.transactionTime = LocalTime.of(23, 59, 59);
        rte.persist();

        RegistrationTransactionEntity rte2 = RegistrationTransactionEntity.findById(rte.referenceNo);
        assertThat(rte).isEqualTo(rte2);

        rte.delete(); // clean up
    }

    @Test
    @Transactional
    void shouldUpdateEntity() {
        RegistrationTransactionEntity rte = RegistrationTransactionEntity.findById("1");
        rte.transactionStatus = "PD";

        RegistrationTransactionEntity rte2 = RegistrationTransactionEntity.findById("1");
        assertThat(rte2.transactionStatus).isEqualTo("PD");
    }

    @Test
    void shouldConvertTransactionDate() {
        RegistrationTransactionEntity rt = RegistrationTransactionEntity.findById("1");
        assertThat(rt.transactionDate)
                .isNotNull()
                .hasToString("2023-12-01");
    }

    @Test
    void shouldConvertTransactionTime() {
        RegistrationTransactionEntity rt = RegistrationTransactionEntity.findById("1");
        assertThat(rt.transactionTime)
                .isNotNull()
                .hasToString("22:30:59");
    }

    @Test
    void shouldAscendingOrderByTransactionDate() {
        List<RegistrationTransactionEntity> rts = RegistrationTransactionEntity.list("order by transactionDate desc");
        assertThat(rts).hasSize(5);
        assertThat(rts.get(0).transactionDate).hasToString("2023-12-05");
        assertThat(rts.get(1).transactionDate).hasToString("2023-12-04");
        assertThat(rts.get(2).transactionDate).hasToString("2023-12-03");
        assertThat(rts.get(3).transactionDate).hasToString("2023-12-02");
        assertThat(rts.get(4).transactionDate).hasToString("2023-12-01");
    }

    @Test
    void shouldAscendingOrderByTransactionDateAndTransactionTimeWithNativeQuery() {
        String sql = "SELECT * FROM PPAR_REGISTRATION_TRANSACTION " +
                "ORDER BY TO_TIMESTAMP(CONCAT(TRANSACTION_DATE, TRANSACTION_TIME),'YYYYMMDDHH24MISS') DESC";

        Query nativeQuery = RegistrationTransactionEntity.getEntityManager()
                .createNativeQuery(sql, RegistrationTransactionEntity.class);

        List<RegistrationTransactionEntity> results = nativeQuery.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).transactionDate).hasToString("2023-12-05");
        assertThat(results.get(1).transactionDate).hasToString("2023-12-04");
        assertThat(results.get(2).transactionDate).hasToString("2023-12-03");
        assertThat(results.get(3).transactionDate).hasToString("2023-12-02");
        assertThat(results.get(4).transactionDate).hasToString("2023-12-01");
    }

    @Test
    void shouldReturnLatestRecordOfProxyTypeAndProxyValueProvidedWithNamedQuery() {
        List<RegistrationTransactionEntity> results = RegistrationTransactionEntity.getEntityManager()
                .createNamedQuery(FIND_LATEST_BY_PROXY_TYPE_AND_VALUE, RegistrationTransactionEntity.class)
                .setParameter("proxyType", "MSISDN")
                .setParameter("proxyValue", "0876543210")
                .getResultList();

        assertThat(results).hasSize(1);
    }
}
