package com.TrevorBye.web;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RESTControllerTest {
    private MockMvc mockMvc;
    private RESTController restController;

    @Before
    public void setUp() throws Exception {
        restController = new RESTController();
        mockMvc = MockMvcBuilders.standaloneSetup(restController).build();
    }

    @Test
    public void getFullDomStructure_ShouldReturn200StatusAndJsonFormat() throws Exception {
        mockMvc.perform(get(new URI("/getFullDomStructure/https%253A%252F%252Fspring.io%252Fguides%252Fgs%252Fhandling-form-submission%252F")))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void getDomNodeById_And_getDomNodeListByClassStarting_ShouldReturn200StatusForAnyClassOrIdFormatType() throws Exception {
        mockMvc.perform(get(new URI("/getDomNodeById/sidebar/https%253A%252F%252Fspring.io%252Fguides%252Fgs%252Fhandling-form-submission%252F")))
                .andExpect(status().is(200));

        mockMvc.perform(get(new URI("/getDomNodeListByClassStarting/github-actions+https/https%253A%252F%252Fspring.io%252Fguides%252Fgs%252Fhandling-form-submission%252F")))
                .andExpect(status().is(200));

        mockMvc.perform(get(new URI("/getDomNodeListByClassStarting/row-fluid/https%253A%252F%252Fspring.io%252Fguides%252Fgs%252Fhandling-form-submission%252F")))
                .andExpect(status().is(200));
    }

    @Test
    public void getElementById_ShouldReturn204WhenIdDoesNotExistAndErrorResponseJson() throws Exception {
        mockMvc.perform(get(new URI("/getDomNodeById/invalid-id/https%253A%252F%252Fspring.io%252Fguides%252Fgs%252Fhandling-form-submission%252F")))
                .andExpect(status().is(204))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("ID COULD NOT BE FOUND.")));
    }

    @Test
    public void getElementById_ShouldReturn400WhenInvalidUrlAndErrorResponseJson() throws Exception {
        mockMvc.perform(get(new URI("/getDomNodeById/invalid-id/www.testbadurlcom%252Fmalformed")))
                .andExpect(status().is(400))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("MALFORMED URL.")));
    }

    @Test
    public void hRefList_ShouldReturn400StatusWhenInvalidUrl() throws Exception {
        mockMvc.perform(get(new URI("/getAllPageLinks/true/www.testbadurlcom%252Fmalformed")))
                .andExpect(status().is(400));
    }

    @Test
    public void hRefList_ShouldReturnJsonArrayOfHrefs() throws Exception {
        mockMvc.perform(
                get(new URI("/getAllPageLinks/true/https%253A%252F%252Fspring.io%252Fguides%252Fgs%252Fhandling-form-submission%252F")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().encoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(80)));

    }

    @Test
    public void isUrlValid_ShouldReturnStatus200ForInvalidUrl() throws Exception {
        mockMvc.perform(get(new URI("/validUrl/ww.google.com")))
                .andExpect(status().is(200));
    }

    @Test
    public void isUrlValid_ShouldReturnStatus200ForValidUrl() throws Exception {
        mockMvc.perform(get(new URI("/validUrl/https%253A%252F%252Fwww.google.com")))
                .andExpect(status().is(200));
    }

    @Test
    public void isUrlValid_ShouldReturnValidUrlJsonForMalformedUrl() throws Exception {
        mockMvc.perform(get(new URI("/validUrl/ww.google.com")))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validUrl", Matchers.is(false)));
    }

    @Test
    public void isUrlValid_ShouldReturnValidUrlJsonForValidUrl() throws Exception {
        mockMvc.perform(get(new URI("/validUrl/https%253A%252F%252Fwww.google.com")))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validUrl", Matchers.is(true)));
    }
}


