package com.consume.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.consume.api.bean.Contries;
import com.consume.api.bean.CurrenySymbols;
import com.consume.api.bean.Response;
import com.consume.api.service.RestCountriesService;

@RestController
public class ApiController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RestCountriesService restCountriesService;
	
	
	@GetMapping("/getAllCurrencySymbols")
	public CurrenySymbols getAllCurrencySymbols(){
		String url = "https://restcountries.eu/rest/v2/all";
		Object[] contries = restTemplate.getForObject(url, Object[].class);
		return restCountriesService.fetchAllCurrencyCodes(contries);
	}
	
	@GetMapping("/getTimeDifference")
	public Response getTimeDifference(@RequestParam(value="fromCountry",defaultValue = "null") String fromCountry, @RequestParam(value="toCountry",defaultValue = "null") String toCountry){
		Response response = new Response();
		if("null".equalsIgnoreCase(fromCountry) || "null".equalsIgnoreCase(toCountry)) {
			response.setResponseMessage("Please provide Country Codes in URL Parameter e.g: /getTimeDifference?fromCountry=BI&toCountry=LAO");
			return response;
		}
		
		String url = "https://restcountries.eu/rest/v2/alpha?codes="+fromCountry+";"+toCountry;
		Object[] contries = restTemplate.getForObject(url, Object[].class);
		response.setResponseMessage(restCountriesService.getTimeDifference(contries));
		return response;
	}
	
	
	@GetMapping("/getCountriesInRegion/{region}")
	public Contries getCountriesInRegion(@PathVariable("region") String region){
		String url = "https://restcountries.eu/rest/v2/region/"+region;
		Object[] contries = restTemplate.getForObject(url, Object[].class);
		return restCountriesService.getCountriesInRegion(contries);
	}
}
