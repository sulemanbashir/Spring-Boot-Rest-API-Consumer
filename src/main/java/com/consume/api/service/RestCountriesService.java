package com.consume.api.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.consume.api.bean.AllCountries;
import com.consume.api.bean.Contries;
import com.consume.api.bean.Currencies;
import com.consume.api.bean.CurrenySymbols;
import com.consume.api.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class RestCountriesService {
	
	
	public CurrenySymbols fetchAllCurrencyCodes(Object[] contries) {

		List<AllCountries> allCountries = null;
		CurrenySymbols currenySymbols = new CurrenySymbols();
		try {
			allCountries = getAllCountries(contries);
			currenySymbols.setCurrencySymbols(fetchAllCurrencyCodesUsedByMoreThenOneCountry(allCountries));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currenySymbols;
	}
	
	public Contries getCountriesInRegion(Object[] contries) {
		List<AllCountries> allCountries = null;
		Contries contriesList = new Contries();

		allCountries = getAllCountries(contries);
		if (null != allCountries) {
			allCountries.sort(Comparator.comparing(AllCountries::getPopulation,
					Comparator.nullsFirst(Comparator.reverseOrder())));
			contriesList.setContries(allCountries.stream().map(AllCountries::getName).collect(Collectors.toList()));
		}
		return contriesList;
	}
	
	public String getTimeDifference(Object[] contries) {
		List<AllCountries> allCountries = null;
		String country1 =null;
		Double country1Hour =null;
		Double country1Min =null;
		String country2 =null;
		Double country2Hour =null;
		Double country2Min =null;
		allCountries = getAllCountries(contries);
		
		if (null != allCountries) {
			for (AllCountries country : allCountries) {
				if(null != country && null != country.getTimezones()) {
					if (country1 == null) {
						country1= country.getTimezones().get(0).substring(3, country.getTimezones().get(0).length());
						String[] timezone = country1.substring(1,country1.length()).split(":");
						country1Hour = "-".equalsIgnoreCase(country1.substring(0, 1))? -1* Double.parseDouble(timezone[0]) :
							Double.parseDouble(timezone[0]);
						country1Min = "-".equalsIgnoreCase(country1.substring(0, 1))? -1* Double.parseDouble(timezone[1]) :
							Double.parseDouble(timezone[1]);
						continue;
					}
					
					if (country2 == null) {
						country2= country.getTimezones().get(0).substring(3, country.getTimezones().get(0).length());
						String[] timezone = country2.substring(1,country2.length()).split(":");
						country2Hour = "-".equalsIgnoreCase(country2.substring(0, 1))? -1* Double.parseDouble(timezone[0]) :
							Double.parseDouble(timezone[0]);
						country2Min  = "-".equalsIgnoreCase(country2.substring(0, 1))? -1* Double.parseDouble(timezone[1]) :
							Double.parseDouble(timezone[1]);
						continue;
					}
				}
			}
		}
		Double minDiff = country1Min - country2Min;		
		Double hourDiff = country1Hour - country2Hour;
		
		if(hourDiff==0 && minDiff>0) {
			return allCountries.get(0).getName()+" is "+ String.valueOf(hourDiff.intValue())+":"+String.valueOf(minDiff.intValue()) +" ahead of "+allCountries.get(1).getName();
		}
		
		if(hourDiff==0 && minDiff<0) {
			return allCountries.get(0).getName()+" is "+ String.valueOf(hourDiff.intValue())+":"+String.valueOf(minDiff.intValue() * -1) +" behind of "+allCountries.get(1).getName();
		}
		
		if(hourDiff>0 && minDiff<0) {
			return allCountries.get(0).getName()+" is "+ String.valueOf(minDiff.intValue() * -1) +" minutes ahead of "+allCountries.get(1).getName();
		}
		
		if(hourDiff<0 && minDiff>0) {
			return allCountries.get(0).getName()+" is "+ String.valueOf(minDiff.intValue()) +" minutes behind of "+allCountries.get(1).getName();
		}
		
		if(hourDiff>0) {
			return allCountries.get(0).getName()+" is "+ String.valueOf(hourDiff.intValue())+":"+String.valueOf(minDiff.intValue()) +" ahead of "+allCountries.get(1).getName();
		}
		
		if(hourDiff<0) {
			return allCountries.get(0).getName()+" is "+ String.valueOf(hourDiff.intValue() * -1)+":"+String.valueOf(minDiff.intValue()) +" behind of "+allCountries.get(1).getName();
		}
		
		
		
		return null;
	}
	
	private List<AllCountries> getAllCountries(Object[] contries){
		List<AllCountries> allCountries = null;
		AllCountries country = null;
		try {
			if (null != contries) {
				allCountries = new ArrayList<AllCountries>();
				for (Object object : contries) {
					if (null != object) {
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json = ow.writeValueAsString(object);
						country = Utils.jsonToJavaObject(json, AllCountries.class);
						allCountries.add(country);
					}
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return allCountries;
	}
	
	private List<String> fetchAllCurrencyCodesUsedByMoreThenOneCountry(List<AllCountries> allCountries) {
		List<String> CurrencySymbols = new ArrayList<String>();
		Map<String, List<String>> currencySymbolMap = new ConcurrentHashMap<String, List<String>>();
		if (null != allCountries) {
			for (AllCountries country : allCountries) {
				if (null != country.getCurrencies()) {
					for (Currencies currency : country.getCurrencies()) {
						if (null != currency.getSymbol()) {
							if (currencySymbolMap.get(currency.getSymbol()) == null) {
								List<String> countryName = new ArrayList<String>();
								countryName.add(country.getName());
								currencySymbolMap.put(currency.getSymbol(), countryName);
							} else {
								currencySymbolMap.get(currency.getSymbol()).add(country.getName());
							}
						}
					}
				}
			}
		}

		for (Map.Entry<String, List<String>> entry : currencySymbolMap.entrySet()) {
			if (entry.getValue().size() > 1) {
				CurrencySymbols.add(entry.getKey());
			}
		}

		return CurrencySymbols;
	}

}
