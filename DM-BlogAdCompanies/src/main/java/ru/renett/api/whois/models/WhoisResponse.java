package ru.renett.api.whois.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhoisResponse{
	@SerializedName("WhoisRecord")
	private WhoisRecord whoisRecord;
}
