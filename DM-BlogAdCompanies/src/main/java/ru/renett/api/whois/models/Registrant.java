package ru.renett.api.whois.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Registrant{
	@SerializedName("organization")
	private String organization;
	@SerializedName(value = "country", alternate = "NOT SET")
	private String country;
}
