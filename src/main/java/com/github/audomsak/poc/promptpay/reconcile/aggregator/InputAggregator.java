package com.github.audomsak.poc.promptpay.reconcile.aggregator;

import com.github.audomsak.poc.promptpay.reconcile.model.InputBody;
import com.github.audomsak.poc.promptpay.reconcile.model.InputHeader;
import io.quarkus.logging.Log;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.audomsak.poc.promptpay.reconcile.Constant.CURRENT_INPUT_HEADER;
import static com.github.audomsak.poc.promptpay.reconcile.Constant.INPUT_DATA;

@ApplicationScoped
public class InputAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // The oldExchange will be null only for the first call
        if (oldExchange == null) {
            oldExchange = newExchange;
        }

        if (newExchange.getMessage().getBody() instanceof InputHeader) {
            handleHeader(oldExchange, newExchange);
        } else if (newExchange.getMessage().getBody() instanceof InputBody) {
            handleBody(oldExchange, newExchange);
        } else {
            String errorMsg = String.format("Unknown message body type is found. %s",
                    newExchange.getMessage().getBody().getClass());

            Log.errorf(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        return oldExchange;
    }

    @SuppressWarnings("unchecked")
    private void handleHeader(Exchange oldExchange, Exchange newExchange) {
        Map<InputHeader, List<InputBody>> inputData;

        if (oldExchange.getProperty(INPUT_DATA) == null) {
            // Keep the input data Map object in the Exchange's property,
            // so it can be sent along the routes and retrieved later
            inputData = new HashMap<>();
            oldExchange.setProperty(INPUT_DATA, inputData);
        } else {
            inputData = (Map<InputHeader, List<InputBody>>) oldExchange.getProperty(INPUT_DATA);
        }

        InputHeader inputHeader = newExchange.getMessage().getBody(InputHeader.class);
        inputData.put(inputHeader, new ArrayList<>());

        // Keep the InputHeader object in the Exchange's property.
        // The object will be used as a key to retrieve a collection (ArrayList)
        // from the Map when we handle InputBody object
        oldExchange.setProperty(CURRENT_INPUT_HEADER, inputHeader);
    }

    @SuppressWarnings("unchecked")
    private void handleBody(Exchange oldExchange, Exchange newExchange) {
        Map<InputHeader, List<InputBody>> inputData = (Map<InputHeader, List<InputBody>>) oldExchange.getProperty(INPUT_DATA);

        if (inputData == null) {
            Log.errorf("Value of Exchange's property: %s is null", INPUT_DATA);
            throw new IllegalArgumentException("inputData should have been there and not null.");
        }

        InputHeader inputHeader = oldExchange.getProperty(CURRENT_INPUT_HEADER, InputHeader.class);
        InputBody inputBody = newExchange.getMessage().getBody(InputBody.class);
        inputData.get(inputHeader).add(inputBody);
    }
}
