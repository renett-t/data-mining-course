package ru.renett.repository.database;

import ru.renett.models.BlackListedLink;
import ru.renett.repository.CRUDRepository;

import java.util.List;
import java.util.Optional;

public interface BlackListedLinkRepository extends CRUDRepository<BlackListedLink> {
    Optional<BlackListedLink> getBlackListedLinkById(Long id);
    List<BlackListedLink> getLinksBlackListedForChannel(String channelId);
}
