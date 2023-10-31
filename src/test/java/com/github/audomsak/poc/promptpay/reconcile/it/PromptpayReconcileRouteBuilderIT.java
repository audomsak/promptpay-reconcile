package com.github.audomsak.poc.promptpay.reconcile.it;

import com.github.audomsak.poc.promptpay.reconcile.testcontainer.SftpServerTestResource;
import io.quarkiverse.wiremock.devservice.WireMockServerTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusIntegrationTest
@QuarkusTestResource(value = SftpServerTestResource.class, restrictToAnnotatedClass = true)
@QuarkusTestResource(value = WireMockServerTestResource.class, restrictToAnnotatedClass = true)

class PromptpayReconcileRouteBuilderIT {


    @Test
    void test() {
        // TODO: implement integration test code
    }


}