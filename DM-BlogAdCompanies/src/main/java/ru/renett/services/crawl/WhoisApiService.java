package ru.renett.services.crawl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.renett.config.root.RootConfig;
import ru.renett.models.Company;
import ru.renett.models.Link;
import ru.renett.repository.database.CompanyRepository;
import ru.renett.repository.database.CompanyRepositoryImpl;
import ru.renett.repository.database.LinkRepository;
import ru.renett.repository.database.LinkRepositoryImpl;
import ru.renett.repository.net.WhoisRepository;
import ru.renett.repository.net.WhoisRepositoryImpl;
import ru.renett.services.crawl.serialization.ListLinkJsonSerializator;
import ru.renett.services.crawl.serialization.SimpleFileReader;

import java.util.*;

public class WhoisApiService {
    // было более 3к линков для обработки, стало 43+1470 АУАва
    private static String SHERBAKOV_FILE = "/home/renett/Repositories/java/4th-semester-programming/DM-BlogAdCompanies/src/main/java/ru/renett/services/crawl/files/sherbakov.json";
    private static String USAHEV_FILE = "/home/renett/Repositories/java/4th-semester-programming/DM-BlogAdCompanies/src/main/java/ru/renett/services/crawl/files/usachev.json";

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);

        LinkRepository linkRepository = context.getBean(LinkRepositoryImpl.class);

//        // Щербаков
//        String sherbakov = "UCbvXcK_Yu6E42Y3MLHNfKiw";
//        List<Link> linksSherbakov = linkRepository.getLinksByOwnerId(sherbakov);
//        List<Link> linksSherbakovFinal = new ArrayList<>();
//
//        for (Link sherbakLink : linksSherbakov) {
//            if (!sherbakLink.isBlackListed()) {
//                sherbakLink.setValue("");
//                linksSherbakovFinal.add(sherbakLink);
//            }
//        }

        // Усачев
        String usachev = "UCDaIW2zPRWhzQ9Hj7a0QP1w";
        SimpleFileReader reader = context.getBean(SimpleFileReader.class);
        ListLinkJsonSerializator serializator = context.getBean(ListLinkJsonSerializator.class);
        List<Link> linksUsachev = serializator.getListFromJson(reader.readLinesFromFile(USAHEV_FILE));

        System.out.println(linksUsachev.size());

        Map<String, Company> domains = new HashMap<>();
        for (Link l : linksUsachev) {
            domains.put(l.getValue(), null);
        }

        System.out.println("Total amount of domains: " + domains.keySet().size());

        WhoisRepository whoisRepository = context.getBean(WhoisRepositoryImpl.class);
        CompanyRepository companyRepository = context.getBean(CompanyRepositoryImpl.class);

        System.out.println("Searching for companies!!");
        for (String domain : domains.keySet()) {
            Company company;
            try {
                company = whoisRepository.getCompanyByDomain(domain);
            } catch (Throwable e) {
                e.printStackTrace();
                System.out.println("Unable to get company for " + domain + ", creating default");
                company = new Company(domain, "", domain);
            }
            if (company.getTitle() == null || company.getTitle().isEmpty())
                company.setTitle(domain);
            companyRepository.save(company);
            domains.replace(domain, company);
        }

        System.out.println("Found, updating database");
        for (Link link : linksUsachev) {
            Company company = domains.get(link.getValue());
            link.setCompanyId(company.getId());
            linkRepository.update(link);
            companyRepository.saveCompanyVideoLink(link.getVideoId(), company.getId(), link.getId());
        }
    }
}
