package ru.renett.api.whois.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistryData {
	@SerializedName("createdDate")
	private String createdDate;
	@SerializedName("registrarName")
	private String registrarName;
	@SerializedName("whoisServer")
	private String whoisServer;
	@SerializedName("domainName")
	private String domainName;
	@SerializedName("registrant")
	private Registrant registrant;
	@SerializedName("status")
	private String status;
}
