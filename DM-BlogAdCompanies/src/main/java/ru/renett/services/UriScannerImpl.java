package ru.renett.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UriScannerImpl implements UriScanner {
    private static final String LINK_REGEX = "(http[s]?:\\/\\/[\\wА-Яа-я.\\/-]*)";
    private final Pattern pattern = Pattern.compile(LINK_REGEX, Pattern.DOTALL);

    @Override
    public List<String> getUriFromString(String body) {
        Matcher matcher = pattern.matcher(body);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }

        return list;
    }
}
