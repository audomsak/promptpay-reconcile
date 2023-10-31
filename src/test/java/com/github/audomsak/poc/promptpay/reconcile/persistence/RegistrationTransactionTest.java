package com.github.audomsak.poc.promptpay.reconcile.persistence;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.persistence.Query;
import java.util.List;

import static com.github.audomsak.poc.promptpay.reconcile.persistence.RegistrationTransaction.GET_LATEST_STATUS_BY_PROXY_TYPE_AND_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class RegistrationTransactionTest {

    @Test
    void shouldReturnAllEntities() {
        List<RegistrationTransaction> rts = RegistrationTransaction.listAll();
        assertThat(rts)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
    }

    @Test
    void shouldConvertTransactionDate() {
        RegistrationTransaction rt = RegistrationTransaction.findById("1");
        assertThat(rt.transactionDate)
                .isNotNull()
                .hasToString("2023-12-01");
    }

    @Test
    void shouldConvertTransactionTime() {
        RegistrationTransaction rt = RegistrationTransaction.findById("1");
        assertThat(rt.transactionTime)
                .isNotNull()
                .hasToString("22:30:59");
    }

    @Test
    void shouldAscendingOrderByTransactionDate() {
        List<RegistrationTransaction> rts = RegistrationTransaction.list("order by transactionDate desc");
        assertThat(rts).hasSize(5);
        assertThat(rts.get(0).transactionDate).hasToString("2023-12-05");
        assertThat(rts.get(1).transactionDate).hasToString("2023-12-04");
        assertThat(rts.get(2).transactionDate).hasToString("2023-12-03");
        assertThat(rts.get(3).transactionDate).hasToString("2023-12-02");
        assertThat(rts.get(4).transactionDate).hasToString("2023-12-01");
    }

    @Test
    void shouldAscendingOrderByTransactionDateAndTransactionTime() {
        Query nativeQuery = RegistrationTransaction.getEntityManager()
                .createNativeQuery("select * " +
                                "from ppar_registration_transaction " +
                                "order by to_timestamp(concat(transaction_date, transaction_time),'YYYYMMDDHH24MISS') desc",
                        RegistrationTransaction.class);

        List<RegistrationTransaction> results = nativeQuery.getResultList();
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).transactionDate).hasToString("2023-12-05");
        assertThat(results.get(1).transactionDate).hasToString("2023-12-04");
        assertThat(results.get(2).transactionDate).hasToString("2023-12-03");
        assertThat(results.get(3).transactionDate).hasToString("2023-12-02");
        assertThat(results.get(4).transactionDate).hasToString("2023-12-01");
    }

    @Test
    void shouldReturnLatestRecordOfProxyTypeAndProxyValueProvided() {
        List<RegistrationTransaction> results = RegistrationTransaction.getEntityManager()
                .createNamedQuery(GET_LATEST_STATUS_BY_PROXY_TYPE_AND_VALUE, RegistrationTransaction.class)
                .setParameter("proxyType", "MSISDN")
                .setParameter("proxyValue", "0876543210")
                .getResultList();

        assertThat(results).hasSize(1);
    }
}
