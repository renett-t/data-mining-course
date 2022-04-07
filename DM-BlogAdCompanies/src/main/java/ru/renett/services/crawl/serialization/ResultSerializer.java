package ru.renett.services.crawl.serialization;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ru.renett.models.Channel;
import ru.renett.models.Video;
import ru.renett.models.result.ChannelVideosAds;
import ru.renett.models.result.CompanyLink;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ResultSerializer {
    private final Gson gson;
    private static final String CSV_FILE_NAME = "/home/renett/Repositories/java/4th-semester-programming/DM-BlogAdCompanies/src/main/java/ru/renett/services/crawl/files/result.csv";

    public ResultSerializer() {
        this.gson = new Gson();
    }
    public String getJsonResult(ChannelVideosAds result) {
        return gson.toJson(result);
    }

//      private Channel channel;
//    private Map<Video, List<CompanyLink>> map;
//    private List<Video> orderedVideos;
    public String getCsvResult(ChannelVideosAds result) {
        File csvOutputFile = new File(CSV_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            List<String[]> dataLines = new ArrayList<>();
            Channel channel = result.getChannel();

            dataLines.add(new String[]{
                    channel.getUserName(), channel.getYoutubeId()
            });

            for (Video video :result.getOrderedVideos()) {
                List<CompanyLink> companyLinks = result.getMap().get(video);
                for (CompanyLink companyLink : companyLinks) {
                    dataLines.add(new String[]{
                            video.getYoutubeId(),
                            video.getTitle(),
                            String.valueOf(video.getViewCount()),
                            companyLink.getCompany().getTitle(),
                            companyLink.getLink().getRawValue()
                    });
                }
            }

            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ho";
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
