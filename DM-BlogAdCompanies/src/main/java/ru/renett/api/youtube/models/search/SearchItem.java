package ru.renett.api.youtube.models.search;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SearchItem {

	@SerializedName("snippet")
	private Snippet snippet;

	@SerializedName("id")
	private ItemId itemId;
}