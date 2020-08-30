package com.coinnote.entryservice;

import com.coinnote.entryservice.dto.MobilityEntity;
import com.coinnote.entryservice.dto.mobility.MobilityDto;
import com.coinnote.entryservice.entity.mobility.MobilityInstance;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

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
@AutoConfigureDataMongo
class EntryServiceApplicationTests {

	@LocalServerPort
	private int port;

	private final static String host = "http://localhost:";
	private final static int maxDoubleLim = 1000000;

	private String getHost(){
		return host+port;
	}

	@Autowired
	private MobilityApiTestIntegration mobilityApiTestIntegration;

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
	public void testAccess() throws Exception {
		RestAssured.get(this.getHost() + "/api/").then().statusCode(200);
	}

	@Test
	public void testCreateData() throws Exception {
		MobilityDto mobilityDtoReturn1 = mobilityApiTestIntegration.createMobilityEntity(getHost());

		String hashCode = Integer.toString(mobilityDtoReturn1.hashcode());

		Assert.assertEquals(mobilityDtoReturn1.getId(), Integer.toString(mobilityDtoReturn1.hashcode()));

		MobilityEntity mobilityEntity = MobilityEntity.builder()
				.accountDelta(new Random().nextLong())
				.partSpending(new Random().nextDouble())
				.petrolSpending(new Random().nextDouble())
				.serviceSpending(new Random().nextDouble())
				.title("mobility-entity1")
				.build();

		MobilityDto mobilityDtoReturn2 = mobilityApiTestIntegration.createMobilityEntity(getHost(), mobilityEntity);

		Assert.assertEquals(mobilityDtoReturn2.partSpending, 0D, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.petrolSpending, 0D, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.serviceSpending, 0D, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.insuranceSpending, 0D, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.publicSpending, 0D, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.otherSpending, 0D, 0.001);
		Assert.assertEquals(0L, (long) mobilityDtoReturn2.getAccountDelta());
	}

	@Test
	public void testUpdateData() throws Exception {
		MobilityDto mobilityDtoReturn1 = mobilityApiTestIntegration.createMobilityEntity(getHost());

		Double adder = new Random().nextDouble()*maxDoubleLim;

		//Check calculation of total
		mobilityDtoReturn1.setPartSpending(mobilityDtoReturn1.getPartSpending() + adder);
		mobilityDtoReturn1.setPetrolSpending(mobilityDtoReturn1.getPetrolSpending() + adder);
		mobilityDtoReturn1.setServiceSpending(mobilityDtoReturn1.getServiceSpending() + adder);
		mobilityDtoReturn1.setInsuranceSpending(mobilityDtoReturn1.getInsuranceSpending() + adder);
		mobilityDtoReturn1.setPublicSpending(mobilityDtoReturn1.getPublicSpending() + adder);
		mobilityDtoReturn1.setOtherSpending(mobilityDtoReturn1.getOtherSpending() + adder);
		mobilityDtoReturn1.setAccountDelta((long)(6*adder));

		int statusCode = mobilityApiTestIntegration.updateMobilityEntity(getHost(), mobilityDtoReturn1);
		Assert.assertEquals(HttpStatus.SC_CREATED, statusCode);
		MobilityDto mobilityDtoReturn2 = mobilityApiTestIntegration.getMobilityEntityById(getHost(),
				mobilityDtoReturn1.getId());

		Assert.assertEquals(mobilityDtoReturn2.partSpending, adder, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.petrolSpending, adder, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.serviceSpending, adder, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.insuranceSpending, adder, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.publicSpending, adder, 0.001);
		Assert.assertEquals(mobilityDtoReturn2.otherSpending, adder, 0.001);
		Assert.assertEquals( 6*adder.longValue(), (long)mobilityDtoReturn2.getAccountDelta());
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