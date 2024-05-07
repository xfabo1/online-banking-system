package cz.muni.fi.obs.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.obs.Application;
import cz.muni.fi.obs.api.DailySummaryRequest;
import cz.muni.fi.obs.api.DailySummaryResult;
import cz.muni.fi.obs.api.MonthlySummaryRequest;
import cz.muni.fi.obs.api.MonthlySummaryResult;
import cz.muni.fi.obs.controller.AnalyticsController;
import cz.muni.fi.obs.facade.AnalyticsFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnalyticsController.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
class AnalyticsControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private AnalyticsFacade analyticsFacade;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "test", authorities = {"SCOPE_test_read"})
    @Test
    void getDailySummary_validRequest_returnsASummary() throws Exception {
        when(analyticsFacade.getDailySummary(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new DailySummaryResult(LocalDate.now(), new ArrayList<>()));

        mockMvc.perform(post("/v1/12345/daily-summary")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new DailySummaryRequest(2021, 1))))
               .andExpect(status().isOk());

        verify(analyticsFacade).getDailySummary("12345", 2021, 1);
    }

    @WithMockUser(username = "test", authorities = {"SCOPE_test_read"})
    @Test
    void getDailySummary_badRequest_throwsException() throws Exception {
        when(analyticsFacade.getDailySummary(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new DailySummaryResult(LocalDate.now(), new ArrayList<>()));

        mockMvc.perform(post("/v1/12345/daily-summary")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new DailySummaryRequest(2021, -1))))
               .andExpect(status().isBadRequest());

    }

    @WithMockUser(username = "test", authorities = {"SCOPE_test_read"})
    @Test
    void getMonthlySummary_validRequest_returnsASummary() throws Exception {
        when(analyticsFacade.getMonthlySummary(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new MonthlySummaryResult(LocalDate.now(), null));

        mockMvc.perform(post("/v1/12345/monthly-summary")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new MonthlySummaryRequest(2021, 1))))
               .andExpect(status().isOk());

        verify(analyticsFacade).getMonthlySummary("12345", 2021, 1);
    }

    @WithMockUser(username = "test", authorities = {"SCOPE_test_read"})
    @Test
    void getMonthlySummary_badRequest_throwsException() throws Exception {
        when(analyticsFacade.getMonthlySummary(any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(new MonthlySummaryResult(LocalDate.now(), null));

        mockMvc.perform(post("/v1/12345/monthly-summary")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new MonthlySummaryRequest(-10, 1))))
               .andExpect(status().isBadRequest());
    }
}