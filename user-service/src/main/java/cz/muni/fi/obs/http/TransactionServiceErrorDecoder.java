package cz.muni.fi.obs.http;

import cz.muni.fi.obs.exceptions.ExternalServiceException;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class TransactionServiceErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());


        if (responseStatus.value() == HttpStatus.NOT_FOUND.value()) {
            return new ResourceNotFoundException(responseBody.toString());
        } else if (responseStatus.value() == HttpStatus.BAD_REQUEST.value()) {
            return defaultErrorDecoder.decode(methodKey, response);
        } else {
            log.error("Server responded with error: {}. Request URL: {}, Response body: {}",
                      responseStatus.value(),
                      requestUrl,
                      responseBody.toString()
            );
            return new ExternalServiceException("Could not connect to the server. Please try again later.");
        }
    }
}
