package ru.renett.repository.net;

import ru.renett.models.Company;

public interface WhoisRepository {
    Company getCompanyByDomain(String domain);
}
