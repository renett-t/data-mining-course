package ru.renett.models.result;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.renett.models.Company;
import ru.renett.models.Video;

import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CompanyVideo {
    private Company company;
    private Video video;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyVideo)) return false;
        CompanyVideo that = (CompanyVideo) o;
        return Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company);
    }
}
