package de.switajski.priebes.flexibleorders.web.debug;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.chemistry.opencmis.commons.impl.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static Logger log = Logger.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        ClientHttpResponse response = execution.execute(request, body);

        log(request, body, response);

        return response;
    }

    private void log(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
        log.debug(new StringBuilder().append("REQUEST: ")
                .append(new String(body, StandardCharsets.UTF_8))
                .append(", RESPONSE: ")
                .append(response.getStatusText())
                .append(IOUtils.readAllLines(response.getBody()))
                .toString());
    }
}
