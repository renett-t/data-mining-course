package ru.renett.api.youtube.models.playlist;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PageInfo{

	@SerializedName("totalResults")
	private int totalResults;

	@SerializedName("resultsPerPage")
	private int resultsPerPage;
}