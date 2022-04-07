package ru.renett.repository.database;

import ru.renett.models.Company;
import ru.renett.models.Link;
import ru.renett.repository.CRUDRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends CRUDRepository<Company> {
    Optional<Company> getCompanyByTitle(String title);
    Optional<Company> getCompanyById(Long id);

    void saveCompanyVideoLink(String videoId, Long companyId, Long linkId);
    List<Company> getCompaniesByVideoId(String videoId);
}
