package com.consume.api.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AllCountries {
	private String name;
	private List<Currencies> currencies;
	private List<String> timezones;
	private int population;
}
