package ru.renett.api.youtube.models.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo{
	private int totalResults;
	private int resultsPerPage;
}
