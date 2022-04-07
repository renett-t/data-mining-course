package ru.renett.api.whois.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WhoisRecord{
	@SerializedName("registryData")
	private RegistryData registryData;
	@SerializedName("registrant")
	private Registrant registrant;
}
