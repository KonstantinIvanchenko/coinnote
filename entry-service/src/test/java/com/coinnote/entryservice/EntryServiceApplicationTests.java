package com.coinnote.entryservice;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/*
@AutoConfigureMockMvc(addFilters=false)
@RunWith(SpringRunner.class)
@WebMvcTest(value = DataUpdateController.class)
class EntryServiceApplicationTests {

	@Autowired
	private MockMvc	mockMvc;

	@MockBean
	private DataUpdateService dataUpdateService;
	@MockBean
	protected KeycloakAdminService keycloakAdminService;

	@Test
	public void dataUpdateGetByDefaultApi() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Access to /api OK")));
	}
}

 */

@SpringBootTest(classes = EntryServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc(addFilters=false)
@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude=SecurityAutoConfiguration.class)
@ActiveProfiles("test")
class EntryServiceApplicationTests {

	@LocalServerPort
	private int port;

	private final static String host = "http://localhost:";

	private String getHost(){
		return host+port;
	}

	@Autowired
	private DataUpdateController dataUpdateController;

	@Before
	public void cleanDatabase(){

	}

	@Test
	public void contextLoads() throws Exception {
		assertThat(dataUpdateController).isNotNull();
	}

	@Test
	public void testAccess(){
		RestAssured.get(this.getHost() + "/api/").then().statusCode(200);
	}


	/*
	@Autowired
	private MockMvc	mockMvc;

	@MockBean
	private DataUpdateService dataUpdateService;
	@MockBean
	protected KeycloakAdminService keycloakAdminService;

	@Test
	public void dataUpdateGetByDefaultApi() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Access to /api OK")));
	}

	 */
}