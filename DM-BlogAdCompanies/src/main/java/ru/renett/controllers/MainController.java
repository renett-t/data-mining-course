package ru.renett.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.*;
import ru.renett.models.result.ChannelVideosAds;
import ru.renett.models.result.CompanyLink;
import ru.renett.models.result.CompanyVideo;
import ru.renett.repository.database.*;
import ru.renett.repository.net.WhoisRepository;
import ru.renett.repository.net.YoutubeRepository;
import ru.renett.services.VideoLinksAnalyzer;
import ru.renett.services.WebCrawler;
import ru.renett.services.crawl.serialization.ResultSerializer;
import ru.renett.services.test.TopCompanyComparator;

import java.util.*;

@Controller
@RequestMapping("/main")
public class MainController {
    private final YoutubeRepository youtubeRepository;
    private final WhoisRepository whoisRepository;

    private final ChannelRepository channelRepository;
    private final BlogStatisticsRepository blogStatisticsRepository;
    private final VideoRepository videoRepository;
    private final LinkRepository linkRepository;
    private final BlackListedLinkRepository blackListedLinkRepository;
    private final PlaylistRepository playlistRepository;
    private final CompanyRepository companyRepository;

    private final VideoLinksAnalyzer videosAnalyzer;
    private final WebCrawler webCrawler;

    private final ResultSerializer resultSerializer;

    private final static String SEARCH_PARAM = "searchParam";
    private final static String YOUTUBER_OPTIONS = "youtuberOptions";
    private final static String MESSAGE = "message";
    private final static String CHANNEL = "youtuber";
    private final static String RESULT = "result";
    private final static String TOP_COMPANIES = "topCompanies";

    private final String FILE = "/home/renett/Repositories/java/4th-semester-programming/DM-BlogAdCompanies/src/main/java/ru/renett/models/result/result.json";

    @Autowired
    public MainController(YoutubeRepository youtubeRepositoryImpl, ChannelRepository channelRepositoryImpl, BlogStatisticsRepository blogStatisticsRepositoryImpl, LinkRepository linkRepositoryImpl, BlackListedLinkRepository blackListedLinkRepositoryImpl, PlaylistRepository playlistRepositoryImpl, VideoLinksAnalyzer channelAnalyzerImpl, WebCrawler webCrawlerImpl, WhoisRepository whoisRepositoryImpl, CompanyRepository companyRepositoryImpl, VideoRepository videoRepositoryImpl, ResultSerializer resultSerializer) {
        this.youtubeRepository = youtubeRepositoryImpl;
        this.whoisRepository = whoisRepositoryImpl;
        this.channelRepository = channelRepositoryImpl;
        this.blogStatisticsRepository = blogStatisticsRepositoryImpl;
        this.linkRepository = linkRepositoryImpl;
        this.blackListedLinkRepository = blackListedLinkRepositoryImpl;
        this.playlistRepository = playlistRepositoryImpl;
        this.videoRepository = videoRepositoryImpl;
        this.companyRepository = companyRepositoryImpl;
        this.videosAnalyzer = channelAnalyzerImpl;
        this.webCrawler = webCrawlerImpl;
        this.resultSerializer = resultSerializer;
    }

    @RequestMapping
    public String mainPage(ModelMap map){
        map.put(SEARCH_PARAM, "");
        return "mainPage";
    }

    @PostMapping("/search")         // todo: exceptions handling, differentiating links & usernames, message
    public String youtubersRequest(@RequestParam String youtuberUserName, ModelMap map) {
        youtuberUserName = trimParameter(youtuberUserName);
        System.out.println(youtuberUserName);
        List<Channel> channelList = youtubeRepository.getChannelsBySearchQuery(youtuberUserName);
        if (channelList.size() == 0) {
            map.put(MESSAGE, "Не было найдено каналов по запросу '" + youtuberUserName + "'");
        } else {
            map.put(YOUTUBER_OPTIONS, channelList);
        }
        map.put(SEARCH_PARAM, youtuberUserName);
        return "mainPage";
    }

    private String trimParameter(String value) {
        if (value.startsWith(",")) {
            value = value.substring(1);
        }

        return value;
    }

    @PostMapping("/setblacklinks")
    public String setBlackListed(@RequestParam String channelId, @RequestParam String list, ModelMap map) {
        if (channelId.startsWith(",")) {
            channelId = channelId.substring(1);
            System.out.println(channelId);
        }
        ArrayList<String> blackLinks = new ArrayList<>();

        for (String link : blackLinks) {
            BlackListedLink blackListedLink = BlackListedLink.builder()
                    .channelId(channelId)
                    .value(link)
                    .build();

            blackListedLinkRepository.save(blackListedLink);
        }

        Optional<Channel> optChannel = channelRepository.getChannelByYoutubeId(channelId);
        if (optChannel.isEmpty()) {
            map.put(MESSAGE, "Unknown channel request. Try again");
            return "mainPage";
        } else {
            map.put(CHANNEL, optChannel.get());
        }

        return "preAnalyzePage";
    }

    @PostMapping("/preanalyze")
    public String preAnalyzePage(@RequestParam String channelId, @RequestParam String ref, ModelMap map) {
        if (channelId.startsWith(",")) {
            channelId = channelId.substring(1);
        }
        Channel channel = youtubeRepository.getChannelById(channelId);
        channel.setGivenURI(ref);

        System.out.println("pre analyze page " + channelId); // todo: only for new channels!
        channelRepository.update(channel);

        map.put(SEARCH_PARAM, ref);
        map.put(CHANNEL, channel);

        return "preAnalyzePage";
    }

    // http://localhost:8081/ho/main/test
    @GetMapping("/test")
    public String test(ModelMap map) {
        String[] testLinks = new String[]{"https://market.yandex.ru/catalog--tovary-dlia-zdorovia/54734", "https://go.sky.pro/pora_valit", "https://www.visitdubai.com/ru", "https://surfshark.deals/USACHEV",
                "https://bit.ly/3L63RXY"};
        List<Company> companies = new ArrayList<>();
        webCrawler.getDomainsByReferral(List.of("https://market.yandex.ru/catalog--tovary-dlia-zdorovia/54734", "https://go.sky.pro/pora_valit", "https://www.visitdubai.com/ru", "https://surfshark.deals/USACHEV",
                "https://bit.ly/3L63RXY"));
        List<String> domains = webCrawler.getCrawledLinks();
        int count = 0;

        System.out.println(Arrays.toString(testLinks));
        System.out.println(domains);

        for (String link : testLinks) {
            System.out.println(link + " ---- " + domains.get(count));
            Company company = whoisRepository.getCompanyByDomain(domains.get(count));
            companies.add(company);
            count++;
        }

        map.put("companies", companies);
        return "test";
    }

    // http://localhost:8081/ho/main/preanalyze?channelId=UCbvXcK_Yu6E42Y3MLHNfKiw
    @GetMapping("/preanalyze")
    public String preAnalyzePageGet(@RequestParam String channelId, ModelMap map) {
        if (channelId.startsWith(",")) {
            channelId = channelId.substring(1);
        }
        Optional<Channel> optChannel = channelRepository.getChannelByYoutubeId(channelId);

        if (optChannel.isEmpty())
            map.put(MESSAGE, "Channel not found.");
        else
            map.put(CHANNEL, optChannel.get());

        return "preAnalyzePage";
    }

    @PostMapping("/analyze")
    public String analyze(@RequestParam String channelId, ModelMap map) {
        if (channelId.startsWith(",")) {
            channelId = channelId.substring(1);
        }
        Optional<Channel> optChannel = channelRepository.getChannelByYoutubeId(channelId);
        if (optChannel.isEmpty()) {
            map.put(MESSAGE, "Unknown channel request. Try again");
            return "mainPage";
        } else {
            Channel channel = optChannel.get();
            List<Video> videos = youtubeRepository.getVideosByPlaylistId(channel.getUploadsPlaylistId());

            if (videos.size() == 0) {
                map.put(MESSAGE, "Не было найдено ни одного видео.");
                return "mainPage";
            } else {
                System.out.println("Videos found: " + videos.size());

                playlistRepository.save(new Playlist(channel.getUploadsPlaylistId(), channelId, videos));
                for (Video v : videos) {
                    videoRepository.save(v);
                    playlistRepository.saveNewPlaylistVideo(channel.getUploadsPlaylistId(), v.getYoutubeId());
                }

                List<Link> links = videosAnalyzer.getAllLinksFromVideos(videos);

                List<BlackListedLink> blackLinks = blackListedLinkRepository.getLinksBlackListedForChannel(channelId);
                List<String> blackListedLinks = new ArrayList<>();
                for (BlackListedLink link : blackLinks) {
                    blackListedLinks.add(link.getValue());
                }

//                List<String> readyLinks = new ArrayList<>();
//                for (Link link : links) {
//                    readyLinks.add(link.getRawValue());
//                }
//
//                webCrawler.getDomainsByReferral(readyLinks);
//
//                List<String> domains = webCrawler.getCrawledLinks();
//                int count = 0;

                for (Link link : links) {
                    System.out.println("ANALYZING LINK - " + link.getRawValue());

                    if (blackListedLinks.contains(link.getRawValue())) {
                        link.setBlackListed(true);
                        System.out.println("found blacklisted link! " + link.getRawValue());
                    }
//                    String domain = domains.get(count++);
//                    link.setValue(domain);

//                    System.out.println("__________________________\n FINDING COMPANY");
//                    Company company = whoisRepository.getCompanyByDomain(domain);
//                    companyRepository.save(company);
//                    System.out.println("Company found - " + company.getTitle());
//                    link.setCompanyId(company.getId());

                    linkRepository.save(link);
                }

//                generateResultAndPutIntoMap(channel, videos, map);
                map.put(MESSAGE, "ВСТАВИЛИ ВСЕ ССЫЛКИ В БД");
            }
        }

        return "analysisPage";
    }

    private ChannelVideosAds generateResultAndPutIntoMap(Channel channel, List<Video> videos, ModelMap map) {
        System.out.println("________________________\n generating result");
        Map<CompanyVideo, Integer> companiesCounter = new HashMap<>();
        ChannelVideosAds result = new ChannelVideosAds();

        result.setChannel(channel);
        Map<Video, List<CompanyLink>> videoCompanies = new HashMap<>();
        List<Video> orderedVideos = new ArrayList<>();

        for (Video video : videos) {
            List<Company> companiesList = companyRepository.getCompaniesByVideoId(video.getYoutubeId());

            for (Company c : companiesList) {
                CompanyVideo cv = new CompanyVideo(c, video);
                if (companiesCounter.containsKey(cv)) {
                    companiesCounter.replace(cv, companiesCounter.get(cv)+1);
                } else {
                    companiesCounter.put(cv, 0);
                }
            }

            Set<Company> companies = new HashSet<>(companiesList);
            if (companies.isEmpty()) {
//                System.out.println("Video " + video.getYoutubeId() + " " + video.getTitle() + " contains no ads ^^");
                continue;
            }

            List<CompanyLink> companyLinks = new ArrayList<>();
            for (Company company : companies) {
                Link link = null;
                try {
                    link = linkRepository.getLinkByCompanyAndVideoId(company.getId(), video.getYoutubeId());
                } catch (DataBaseException e) {
                    System.out.println("Didn't found link for " + company + " from video " + video.getYoutubeId());
                    link = new Link(company.getDomain());
                }
                CompanyLink companyLink = new CompanyLink(company, link);
                companyLinks.add(companyLink);
            }

            videoCompanies.put(video, companyLinks);
            orderedVideos.add(video);
        }

        // ordering result
        Comparator<Video> videoViewsComparator = Comparator.comparingLong(Video::getViewCount);
        Comparator<Video> videoComparatorReversed = videoViewsComparator.reversed();

        Video[] array = new Video[orderedVideos.size()];
        orderedVideos.toArray(array);
        Arrays.sort(array, videoComparatorReversed);

        orderedVideos = List.of(array);

        System.out.println("Videos in total: " + orderedVideos.size());
        System.out.println("RETURNING RESULT");
        result.setMap(videoCompanies);
        result.setOrderedVideos(orderedVideos);

        map.put(TOP_COMPANIES, getTopCompanies(companiesCounter));
        map.put(RESULT, result);

        return result;
    }

    private List<CompanyLink> getTopCompanies(Map<CompanyVideo, Integer> map) {
        System.out.println("TOP5 FROM " + map.size());
        List<CompanyLink> top5 = new ArrayList<>();
        TopCompanyComparator comparator = new TopCompanyComparator(map);
        TreeMap<CompanyVideo, Integer> sortedMap = new TreeMap<CompanyVideo, Integer>(comparator);
        sortedMap.putAll(map);

        int i = 0;
        CompanyVideo[] ordered = sortedMap.keySet().toArray(new CompanyVideo[0]);
        while (i < 5) {
            CompanyVideo companyVideo = ordered[i];

            Link link = null;
            try {
                link = linkRepository.getLinkByCompanyAndVideoId(companyVideo.getCompany().getId(), companyVideo.getVideo().getYoutubeId());
            } catch (DataBaseException e) {
                System.out.println("Didn't found link for " + companyVideo + " from video " +  companyVideo.getVideo().getYoutubeId());
                link = new Link(companyVideo.getCompany().getDomain());
            }
            CompanyLink companyLink = new CompanyLink(companyVideo.getCompany(), link);
            top5.add(companyLink);
            i++;
        }

        System.out.println(top5);
        return top5;
    }

    // RESULT !!!!!!!!
    // http://localhost:<port>/dm/main/analyze?id=UCDaIW2zPRWhzQ9Hj7a0QP1w
    @GetMapping("/analyze")
    public String preparedAnalysis(@RequestParam String id, ModelMap map) {
       String format = "html";
       getResult(id, format, map);

       return "analysisPage";
    }

    private ChannelVideosAds getResult(String id, String format, ModelMap map) {
        if (id.startsWith(",")) {
            id = id.substring(1);
        }
        Optional<Channel> optionalChannel = channelRepository.getChannelByYoutubeId(id);
        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();

            // blog stats saving:
            Channel channel1 = youtubeRepository.getChannelById(id);
            blogStatisticsRepository.update(channel1.getStatistics());
            channel.setStatistics(channel1.getStatistics());

            // getting views for videos:
            List<Video> videos = videoRepository.getVideosByOwnerId(id);
            System.out.println("Videos found = " + videos.size());
//            for (Video video : videos) {
//                Video youtubeVideo = youtubeRepository.getVideoById(video.getYoutubeId());
//                if (video.getYoutubeId().equals(youtubeVideo.getYoutubeId())) {
//                    video.setViewCount(youtubeVideo.getViewCount());
//                    videoRepository.update(video);
//                } else {
//                    System.out.println("Something wrong with video " + video.getYoutubeId() + " " + video.getTitle());
//                    System.out.println("from youtube: " + youtubeVideo.getYoutubeId() + " " + youtubeVideo.getTitle());
//                }
//            }

            System.out.println("Processed all videos. About to generate result");
            return generateResultAndPutIntoMap(channel, videos, map);

        } else {
            map.put(MESSAGE, "No channel with ID = " + id + " found");
            return null;
        }
    }


    // http://localhost:<port>/dm/main/analyze?id=UCDaIW2zPRWhzQ9Hj7a0QP1w&format=csv
//    @GetMapping(value = "/analyze", produces = MediaType.APPLICATION_JSON_VALUE)
//    public String preparedAnalysisToCsv(@RequestParam String id, @RequestParam String format, ModelMap map) {
//        if (format.equals("csv")) {
//            ChannelVideosAds result = getResult(id, format, map);
//            return resultSerializer.getCsvResult(result);
//        }
//
//        return "analysisPage";
//    }

}
