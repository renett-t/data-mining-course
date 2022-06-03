package ru.renett.repository.database;

import ru.renett.models.BlogStatistics;
import ru.renett.repository.CRUDRepository;

import java.util.Optional;

public interface BlogStatisticsRepository extends CRUDRepository<BlogStatistics> {
    Optional<BlogStatistics> getBlogStatisticsById(Long id);
    Optional<BlogStatistics> getBlogStatisticsByOwnerId(String ownerId);
}
