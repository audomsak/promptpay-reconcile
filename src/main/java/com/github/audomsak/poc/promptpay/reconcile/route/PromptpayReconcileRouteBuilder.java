package com.github.audomsak.poc.promptpay.reconcile.route;

import com.github.audomsak.poc.promptpay.reconcile.aggregator.InputAggregator;
import com.github.audomsak.poc.promptpay.reconcile.processor.InputProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.SftpEndpointBuilderFactory.SftpEndpointConsumerBuilder;
import org.apache.camel.builder.endpoint.dsl.SftpEndpointBuilderFactory.SftpEndpointProducerBuilder;
import org.apache.camel.component.file.GenericFileExist;
import org.apache.camel.component.file.remote.FtpConstants;
import org.apache.camel.dataformat.bindy.fixed.BindyFixedLengthDataFormat;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.github.audomsak.poc.promptpay.reconcile.Constant.INPUT_DATA;
import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.LoggingLevel.ERROR;
import static org.apache.camel.LoggingLevel.INFO;

@Slf4j // Need SLF4J logger because Camel framework uses it. Though, the logs will be redirected to JBoss Log Manager
@ApplicationScoped
public class PromptpayReconcileRouteBuilder extends EndpointRouteBuilder {

    // ********* Endpoint path/name *********
    public static final String DIRECT_PARSE_INPUT = "direct:parse-input";
    public static final String DIRECT_UPDATE_TABLE = "direct:update-table";
    public static final String DIRECT_PROCESS_INPUT = "direct:process-input";
    public static final String DIRECT_SEND_EMAIL = "direct:send-email";
    public static final String CALL_API = "direct:call-api";

    public static final String DIRECT_ERROR_HANDLER = "direct:error-handler";

    // **************************************

    // ************* Route ID ***************
    public static final String SFTP_CONSUMER_ROUTE = "sftp-consumer-route";
    public static final String UPDATE_TABLE_ROUTE = "update-table-route";
    public static final String SEND_EMAIL_ROUTE = "send-email-route";
    public static final String CALL_API_ROUTE = "call-api-route";
    public static final String PROCESS_INPUT_ROUTE = "process-input-route";
    public static final String PARSE_INPUT_ROUTE = "parse-input-route";
    public static final String CALL_CBS_ROUTE = "call-cbs-api-route";
    public static final String ADD_RELATIONSHIP_RM_ROUTE = "add-relationship-rm-route";
    public static final String ADD_ITMX_ROUTE = "add-itmx-route";
    public static final String INQUIRY_ITMX_ROUTE = "inquiry-itmx-route";
    public static final String DELETE_ITMX_ROUTE = "delete-itmx-route";
    private static final String ERROR_HANDLER_ROUTE = "error-handler-route";
    // **************************************

    @Inject
    @Named("inputHeaderFormat")
    BindyFixedLengthDataFormat inputHeaderFormat;

    @Inject
    @Named("inputBodyFormat")
    BindyFixedLengthDataFormat inputBodyFormat;

    @Inject
    InputAggregator inputAggregator;

    @Inject
    InputProcessor inputProcessor;

    @ConfigProperty(name = "parallel.processing.thread", defaultValue = "1")
    int threadPoolSize;

    @Override
    public void configure() throws Exception {
        //@formatter:off

        // General global exception handling
        onException(Exception.class)
                .handled(true)
                .logHandled(true)
                .logStackTrace(true)
                .log(ERROR, log, "There was an error occurred. ${exception}")
                .to(DIRECT_ERROR_HANDLER);

        from(DIRECT_ERROR_HANDLER)
                .routeId(ERROR_HANDLER_ROUTE)
                .setHeader(FtpConstants.FILE_NAME, simple("TBC"))
                .to(sftpErrorEndpoint());


        // Processing steps:
        // 1. Read file(s) from SFTP server
        // 2. Split the file's content line by line with `\n` delimiter
        // 3. Send to `PARSE_INPUT_ROUTE` route to pare each line to Java object
        // 4. Aggregate Java objects in a Map<InputHeader, List<InputBody>> object. See: InputAggregator.java
        // 5. Replace the Exchange's body with the Map object
        // 6. Send to `PROCESS_INPUT_ROUTE` route to process the next step
        from(sftpInputEndpoint())
                .routeId(SFTP_CONSUMER_ROUTE)
                .log(INFO, log, "Processing file: ${file:name}")
                .split(body().tokenize("\n"), inputAggregator)
                    .to(DIRECT_PARSE_INPUT)
                .end()
                .setBody(exchangeProperty(INPUT_DATA)) // The Exchange body will be the Map<InputHeader, List<InputBody>> object
                .to(DIRECT_PROCESS_INPUT);


        // Processing steps:
        // 1. Consume input string from Direct component (parallel)
        // 2. If the Exchange's body (input string) contains `head` then parse to InputHeader object.
        //    Otherwise, InputBody object.
        // 3. Return the Exchange to the caller route/node
        from(DIRECT_PARSE_INPUT)
                .routeId(PARSE_INPUT_ROUTE)
                .threads(threadPoolSize)
                .choice()
                    .when(body().contains("head"))
                        .log(DEBUG, log, "Header line: ${body}")
                        .unmarshal(inputHeaderFormat)
                        .log(DEBUG, log, "Header object: ${body}")
                    .otherwise()
                        .log(DEBUG, log, "Body line: ${body}")
                        .unmarshal(inputBodyFormat)
                        .log(DEBUG, log, "Body object: ${body}")
                .end();

        //TODO: Code implementation from here on.

        // Processing steps:
        // 1. Consumed input from Direct component
        // 2. Split the body. The Exchange body will be an output of splitter `.split(body())` which represents
        //    each chunk/section of input with Map's entry i.e. Map.Entry<InputHeader, List<InputBody>>
        from(DIRECT_PROCESS_INPUT)
                .routeId(PROCESS_INPUT_ROUTE)
                .split(body())
                    .process(inputProcessor)
                .end();


        from(DIRECT_UPDATE_TABLE)
                .routeId(UPDATE_TABLE_ROUTE)
                .transacted()
                .log("test");


        from(DIRECT_SEND_EMAIL)
                .routeId(SEND_EMAIL_ROUTE)
                .log("test");

        from(CALL_API)
                .routeId(CALL_API_ROUTE)
                .log("test");

        //@formatter:on
    }

    private SftpEndpointConsumerBuilder sftpInputEndpoint() {
        return sftp("{{ftp.server.input.endpoint}}")
                .username("{{ftp.server.username}}")
                .password("{{ftp.server.password}}")
                .move(".done");
    }

    private SftpEndpointProducerBuilder sftpErrorEndpoint() {
        return sftp("{{ftp.server.error.endpoint}}")
                .username("{{ftp.server.username}}")
                .password("{{ftp.server.password}}")
                .fileExist(GenericFileExist.Append);
    }
}
