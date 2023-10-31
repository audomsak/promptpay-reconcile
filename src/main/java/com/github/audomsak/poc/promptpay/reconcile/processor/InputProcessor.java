package com.github.audomsak.poc.promptpay.reconcile.processor;

import com.github.audomsak.poc.promptpay.reconcile.model.InputBody;
import com.github.audomsak.poc.promptpay.reconcile.model.InputHeader;
import com.github.audomsak.poc.promptpay.reconcile.model.InputKey;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.audomsak.poc.promptpay.reconcile.util.DateTimeUtils.toLocalDateTime;

@ApplicationScoped
public class InputProcessor implements Processor {
    @SuppressWarnings("unchecked")
    @Override
    public void process(Exchange exchange) throws Exception {
        Map.Entry<InputHeader, List<InputBody>> inputData;
        inputData = (Map.Entry<InputHeader, List<InputBody>>) exchange.getMessage().getBody();
        List<InputBody> inputBodyList = inputData.getValue();

        // Group input list by proxy type and proxy value to get unique inputs that need to be processed
        Map<InputKey, List<InputBody>> groupedInput = inputBodyList.stream()
                .collect(Collectors.groupingBy(input -> new InputKey(input.getProxyType(), input.getProxyValue())));

        // Sort input list by transaction date and time as descending order
        groupedInput.forEach((key, inputList) ->
                inputList.sort((input1, input2) -> {
                    LocalDateTime input1TrxDateTime = toLocalDateTime(input1.getTransactionDate(), input1.getTransactionTime());
                    LocalDateTime input2TrxDateTime = toLocalDateTime(input2.getTransactionDate(), input2.getTransactionTime());
                    return input2TrxDateTime.compareTo(input1TrxDateTime);
                }));
    }
}
