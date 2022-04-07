package ru.renett.services.crawl.serialization;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import ru.renett.models.Link;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListLinkJsonSerializator {
    private final Gson gson;

    public ListLinkJsonSerializator() {
        this.gson = new Gson();
    }

    public String serializeListToString(List<Link> list) {
        return gson.toJson(list);
    }

    public List<Link> getListFromJson(String json) {
        Type listType = new TypeToken<ArrayList<Link>>() {}.getType();

        return gson.fromJson(json, listType);
    }
}
