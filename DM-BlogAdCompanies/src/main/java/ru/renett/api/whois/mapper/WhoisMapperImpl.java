package ru.renett.api.whois.mapper;

import org.springframework.stereotype.Component;
import ru.renett.api.whois.models.Registrant;
import ru.renett.api.whois.models.WhoisResponse;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Company;

@Component
public class WhoisMapperImpl implements WhoisMapper {

    @Override
    public Company mapToCompany(WhoisResponse response) {
        System.out.println(response);
        String title = "";
        try {
            if (response == null) {
                return null;
            } else {
                Registrant registrant = response.getWhoisRecord().getRegistryData().getRegistrant();
                if (registrant != null) {
                    title = response.getWhoisRecord().getRegistryData().getRegistrant().getOrganization();
                } else {
                    registrant = response.getWhoisRecord().getRegistrant();
                    if (registrant != null) {
                        title = registrant.getOrganization();
                    } else {
                        title = response.getWhoisRecord().getRegistryData().getDomainName();
                    }
                }
            }
        } catch (Throwable e) {
            throw new DataBaseException(e);
        }

        return Company.builder()
                    .title(title)
                    .logoUrl("")
                    .domain(response.getWhoisRecord().getRegistryData().getDomainName())
                    .build();
    }
}
