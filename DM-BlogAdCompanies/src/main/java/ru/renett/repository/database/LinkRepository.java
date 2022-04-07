package ru.renett.repository.database;

import ru.renett.models.Link;
import ru.renett.repository.CRUDRepository;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends CRUDRepository<Link> {
    Optional<Link> getLinkById(Long id);
    List<Link> getLinksByOwnerId(String ownerId);
    List<Link> getLinksByVideoId(String videoId);
    void setBlackListedById(Long id);
    void setUpdatedLink(Long id, String link);

    Link getLinkByCompanyAndVideoId(Long id, String videoId);
}
