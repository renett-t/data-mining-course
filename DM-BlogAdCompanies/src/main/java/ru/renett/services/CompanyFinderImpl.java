package ru.renett.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Company;
import ru.renett.repository.net.WhoisRepository;

@Service
public class CompanyFinderImpl implements CompanyFinder {
    private final WhoisRepository whoisRepository;

    @Autowired
    public CompanyFinderImpl(WhoisRepository whoisRepositoryImpl) {
        this.whoisRepository = whoisRepositoryImpl;
    }

    @Override
    public Company findCompanyByUrl(String domain) {
        Company company = whoisRepository.getCompanyByDomain(domain);
        if (company == null) {
            throw new DataBaseException("Didn't found company for domain '" + domain + "'");
        }

        return company;
    }
}
