package com.github.audomsak.poc.promptpay.reconcile.route;

import com.github.audomsak.poc.promptpay.reconcile.model.InputBody;
import com.github.audomsak.poc.promptpay.reconcile.model.InputHeader;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.github.audomsak.poc.promptpay.reconcile.util.TestUtils.getMockedEndpoint;
import static com.github.audomsak.poc.promptpay.reconcile.util.TestUtils.getTestData;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class PromptpayReconcileRouteBuilderTest extends CamelQuarkusTestSupport {
    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Override
    protected void doAfterConstruct() throws Exception {
        AdviceWith.adviceWith(PromptpayReconcileRouteBuilder.SFTP_CONSUMER_ROUTE, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:input");
                mockEndpointsAndSkip(PromptpayReconcileRouteBuilder.DIRECT_PROCESS_INPUT);
                //interceptSendToEndpoint(PromptpayReconcileRouteBuilder.DIRECT_PROCESS_INPUT)
                //.skipSendToOriginalEndpoint().to("mock:process-input");
            }
        });

        super.doAfterConstruct();
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldParseInputAndSendToProcessInputRoute() throws Exception {
        MockEndpoint processInputMock = getMockEndpoint(getMockedEndpoint(PromptpayReconcileRouteBuilder.DIRECT_PROCESS_INPUT));
        processInputMock.expectedMessageCount(1);

        String input = getTestData("test-data/input/ADITMX_RECONCILE.txt", StandardCharsets.UTF_8);

        template.sendBody("direct:input", input);
        processInputMock.assertIsSatisfied();

        Exchange exchange = processInputMock.getExchanges().get(0);
        assertThat(exchange.getMessage().getBody()).isInstanceOf(Map.class);

        Map<InputHeader, List<InputBody>> body = (Map<InputHeader, List<InputBody>>) exchange.getMessage().getBody();
        assertThat(body).hasSize(2);
    }

}