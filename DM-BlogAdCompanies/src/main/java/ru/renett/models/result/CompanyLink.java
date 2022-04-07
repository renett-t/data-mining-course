package ru.renett.models.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.renett.models.Company;
import ru.renett.models.Link;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CompanyLink {
    private Company company;
    private Link link;
}
