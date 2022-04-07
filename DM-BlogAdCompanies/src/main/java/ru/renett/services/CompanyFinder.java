package ru.renett.services;

import ru.renett.models.Company;

public interface CompanyFinder {
    Company findCompanyByUrl(String domain);
}
