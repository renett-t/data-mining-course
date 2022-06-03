package ru.renett.services.test;

import org.springframework.stereotype.Component;
import ru.renett.models.result.CompanyVideo;

import java.util.Comparator;
import java.util.Map;

@Component
public class TopCompanyComparator implements Comparator<CompanyVideo> {
    private final Map<CompanyVideo, Integer> base;

    public TopCompanyComparator(Map<CompanyVideo, Integer> base) {
        this.base = base;
    }

    @Override
    public int compare(CompanyVideo company1, CompanyVideo company2) {
        if (base.get(company1) >= base.get(company2)) {
            return -1;
        } else {
            return 1;
        }
    }
}
