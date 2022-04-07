package ru.renett.api.whois.mapper;

import ru.renett.api.whois.models.WhoisResponse;
import ru.renett.models.Company;

public interface WhoisMapper {
    Company mapToCompany(WhoisResponse response);
}
