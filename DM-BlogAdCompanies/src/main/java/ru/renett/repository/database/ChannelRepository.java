package ru.renett.repository.database;

import ru.renett.models.Channel;
import ru.renett.repository.CRUDRepository;

import java.util.Optional;

public interface ChannelRepository extends CRUDRepository<Channel> {
    Optional<Channel> getChannelByYoutubeId(String youtubeId);
    Optional<Channel> getChannelByUserName(String username);
}
