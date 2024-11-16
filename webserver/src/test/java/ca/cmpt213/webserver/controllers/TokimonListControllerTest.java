package ca.cmpt213.webserver.controllers;

import ca.cmpt213.webserver.models.Tokimon;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokimonListControllerTest {
    String FILE_PATH = "src/main/resources/static/tokimons.json";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokimonListController tokimonListController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // clear contents of Json before each test for consistency
        System.out.println("Runs before each test");
        File file = new File(FILE_PATH);
        // if file doesn't exist create it
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
            }
        // if the file does exist, clear it first
        } else {
            try (FileWriter writer = new FileWriter(file, false)) {
                // Writing an empty array to indicate an empty list
                writer.write("[]");
                writer.flush();
            } catch (IOException e) {
                System.out.println("An error occurred while clearing the file.");
                e.printStackTrace();
            }
        }
    }

    @Test
    void contextLoads() {
        assertThat(tokimonListController).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void testAddAndUpdateTokimon() throws Exception {
        Tokimon t1 = new Tokimon("pikachu", "electric", 6);
        t1.setId(1);
        Tokimon t2 = new Tokimon("raichu", "electric", 7);
        t2.setId(1);
        this.mockMvc.perform(
                        post("/tokimon")
                                .content(objectMapper.writeValueAsString(t1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(t1))
                );
        this.mockMvc.perform(put("/tokimon/edit/{id}", 1)
                        .content(objectMapper.writeValueAsString(t2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(t2)));
    }

    @Test
    void testAddTokimon() throws Exception {
        Tokimon t1 = new Tokimon("squirtle", "water", 3);
        t1.setId(2);
        Tokimon t2 = new Tokimon("bulbasaur", "grass", 7);
        t2.setId(3);
        this.mockMvc.perform(
                post("/tokimon")
                        .content(objectMapper.writeValueAsString(t1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"squirtle\",\"type\": \"water\",\"rarityScore\": 3}")
        );
        this.mockMvc.perform(
                        post("/tokimon")
                                .content(new ObjectMapper().writeValueAsString(t2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"bulbasaur\",\"type\": \"grass\",\"rarityScore\": 7}")
                );
    }

    @Test
    void testGetTokimons() throws Exception {
        Tokimon t1 = new Tokimon("squirtle", "water", 3);
        t1.setId(2);
        Tokimon t2 = new Tokimon("bulbasaur", "grass", 7);
        t2.setId(3);
        this.mockMvc.perform(
                        post("/tokimon")
                                .content(objectMapper.writeValueAsString(t1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"squirtle\",\"type\": \"water\",\"rarityScore\": 3}")
                );
        this.mockMvc.perform(
                        post("/tokimon")
                                .content(new ObjectMapper().writeValueAsString(t2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"bulbasaur\",\"type\": \"grass\",\"rarityScore\": 7}")
                );
        this.mockMvc.perform(
                get("/tokimon"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void getTokimonByID() throws Exception {
        Tokimon t1 = new Tokimon("squirtle", "water", 3);
        t1.setId(2);
        Tokimon t2 = new Tokimon("bulbasaur", "grass", 7);
        t2.setId(3);
        this.mockMvc.perform(
                        post("/tokimon")
                                .content(objectMapper.writeValueAsString(t1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"squirtle\",\"type\": \"water\",\"rarityScore\": 3}")
                );
        this.mockMvc.perform(
                        post("/tokimon")
                                .content(new ObjectMapper().writeValueAsString(t2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"bulbasaur\",\"type\": \"grass\",\"rarityScore\": 7}")
                );
        this.mockMvc.perform(get("/tokimon/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(t1))
        );
    }

    @Test
    void testDeleteTokimon() throws Exception {
        Tokimon t1 = new Tokimon("squirtle", "water", 3);
        t1.setId(2);
        Tokimon t2 = new Tokimon("bulbasaur", "grass", 7);
        t2.setId(3);
        this.mockMvc.perform(
                        post("/tokimon")
                                .content(objectMapper.writeValueAsString(t1))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"squirtle\",\"type\": \"water\",\"rarityScore\": 3}")
                );
        this.mockMvc.perform(
                        post("/tokimon")
                                .content(new ObjectMapper().writeValueAsString(t2))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"bulbasaur\",\"type\": \"grass\",\"rarityScore\": 7}")
                );
        this.mockMvc.perform(delete("/tokimon/{id}", 2))
                .andExpect(status().isNoContent()
                );
    }
}