package com.topaz.support.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Integración HTTP -> casos de uso -> H2. Usa los datos sembrados en data.sql. */
@SpringBootTest
@AutoConfigureMockMvc
class SupportOrderControllerTest {

    private static final String URL = "/api/support-orders";

    @Autowired MockMvc mvc;

    private static String body(String ref, String description) {
        return """
            {"externalReference":"%s","customerId":"C-1","category":"PAYMENT",
             "description":"%s","channel":"MOBILE_APP","priority":"HIGH"}""".formatted(ref, description);
    }

    @Test
    void createsValidOrderAsOpen() throws Exception {
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body("T-OK", "Descripción válida de prueba")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void rejectsInvalidRequest() throws Exception {
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body("T-BAD", "corta")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsDuplicateActiveReference() throws Exception {
        mvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(body("MOB-1001", "Descripción válida de prueba")))
                .andExpect(status().isConflict());
    }

    @Test
    void changesStatusWhenTransitionIsValid() throws Exception {
        // id 5 (MOB-1005) está OPEN
        mvc.perform(patch(URL + "/5/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void rejectsInvalidTransition() throws Exception {
        // id 3 (MOB-1003) está RESOLVED, estado final
        mvc.perform(patch(URL + "/3/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void getsExistingAndMissingOrder() throws Exception {
        mvc.perform(get(URL + "/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.externalReference").value("MOB-1001"));
        mvc.perform(get(URL + "/9999")).andExpect(status().isNotFound());
    }
}
