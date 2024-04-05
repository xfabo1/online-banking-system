package cz.muni.fi.obs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.CurrencyExchangeRequest;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.facade.CurrencyFacade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyFacade currencyFacade;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void exchange_validRequest_exchangeHappens() throws Exception {
        when(currencyFacade.exchange(any(String.class), any(String.class), any(BigDecimal.class)))
                .thenReturn(new CurrencyExchangeResult("aud", "eur", 10.2, BigDecimal.valueOf(1000), BigDecimal.valueOf(10200)));

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("eur", "aud", BigDecimal.valueOf(1000));

        String responseJson = mockMvc.perform(post("/v1/currencies/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        CurrencyExchangeResult currencyExchangeResult = objectMapper.readValue(responseJson, CurrencyExchangeResult.class);

        verify(currencyFacade).exchange(request.from(), request.to(), request.amount());
        Assertions.assertThat(currencyExchangeResult.destAmount()).isEqualByComparingTo(BigDecimal.valueOf(10200));
    }

    @Test
    void exchange_invalidRequest_validationFails() throws Exception {
        when(currencyFacade.exchange(any(String.class), any(String.class), any(BigDecimal.class)))
                .thenReturn(new CurrencyExchangeResult(null, "eur", 10.2, BigDecimal.valueOf(1000), BigDecimal.valueOf(10200)));

        CurrencyExchangeRequest request = new CurrencyExchangeRequest("eur", "aud", BigDecimal.valueOf(1000));

        String responseJson = mockMvc.perform(post("/v1/currencies/exchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        CurrencyExchangeResult currencyExchangeResult = objectMapper.readValue(responseJson, CurrencyExchangeResult.class);

        verify(currencyFacade).exchange(request.from(), request.to(), request.amount());
        Assertions.assertThat(currencyExchangeResult.destAmount()).isEqualByComparingTo(BigDecimal.valueOf(10200));
    }

    @Test
    void list_validRequest_currenciesListed() throws Exception {
        PageImpl<CurrencyDto> currencyDtos = new PageImpl<>(List.of(new CurrencyDto("eur", "euro")));
        when(currencyFacade.listPage(any(PageRequest.class))).thenReturn(currencyDtos);

        mockMvc.perform(get("/v1/currencies/list?page=0&pageSize=50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }
}