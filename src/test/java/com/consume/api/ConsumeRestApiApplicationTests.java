package com.consume.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.consume.api.bean.Contries;
import com.consume.api.bean.CurrenySymbols;
import com.consume.api.bean.Response;
import com.consume.api.util.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class ConsumeRestApiApplicationTests {

	private MockMvc mockMvc;
	
	@Autowired
	
	private WebApplicationContext context;
	
	@BeforeAll
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	
	@Test
	public void getAllCurrencySymbolsTest() {
		try {
			List<String> aspectedResult = Arrays.asList("Æ", "Â£", "$", "Â¥", "â¨", "â©", "âª", "â¬", "â¹", "Ø¯.Ø¬", "L", "P", "R", "Rs", "kr", "Fr", "Br", "Sh", "Ø¯.Ù.", "ï·¼");
			MvcResult result = mockMvc
					.perform(get("/getAllCurrencySymbols").content(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk()).andReturn();
			String resultContent = result.getResponse().getContentAsString();
			JsonNode jsonNode = new ObjectMapper().readTree(resultContent);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(jsonNode.get("CurrenySymbols"));
			CurrenySymbols currenySymbols = Utils.jsonToJavaObject(json, CurrenySymbols.class);
			
			assertTrue(currenySymbols.getCurrencySymbols().equals(aspectedResult));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getTimeDifferenceTest() {
		try {
			String aspectedResult = "Burundi is 3:0 behind of Pakistan";
			MvcResult result = mockMvc
					.perform(get("/getTimeDifference?fromCountry=BI&toCountry=PAK").content(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk()).andReturn();
			String resultContent = result.getResponse().getContentAsString();
			JsonNode jsonNode = new ObjectMapper().readTree(resultContent);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(jsonNode.get("response"));
			Response response = Utils.jsonToJavaObject(json, Response.class);
			assertTrue(response.getResponseMessage().equalsIgnoreCase(aspectedResult));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getCountriesInRegionTest() {
		try {
			List<String> aspectedResult = Arrays.asList("China", "India", "Indonesia", "Pakistan", "Bangladesh", "Japan", "Philippines", "Viet Nam", "Iran (Islamic Republic of)", "Turkey", "Thailand", "Myanmar", "Korea (Republic of)", "Iraq", "Saudi Arabia", "Uzbekistan", "Malaysia", "Nepal", "Afghanistan", "Yemen", "Korea (Democratic People's Republic of)", "Taiwan", "Sri Lanka", "Syrian Arab Republic", "Kazakhstan", "Cambodia", "United Arab Emirates", "Azerbaijan", "Jordan", "Tajikistan", "Israel", "Hong Kong", "Lao People's Democratic Republic", "Kyrgyzstan", "Lebanon", "Singapore", "Turkmenistan", "Palestine, State of", "Oman", "Kuwait", "Georgia", "Mongolia", "Armenia", "Qatar", "Bahrain", "Timor-Leste", "Bhutan", "Macao", "Brunei Darussalam", "Maldives");
			MvcResult result = mockMvc
					.perform(get("/getCountriesInRegion/Asia").content(MediaType.APPLICATION_JSON_VALUE))
					.andExpect(status().isOk()).andReturn();
			String resultContent = result.getResponse().getContentAsString();
			JsonNode jsonNode = new ObjectMapper().readTree(resultContent);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(jsonNode.get("listOfCountries"));
			Contries countries = Utils.jsonToJavaObject(json, Contries.class);
			assertTrue(countries.getContries().equals(aspectedResult));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
