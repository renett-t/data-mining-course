package ru.renett.repository.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.renett.api.WhoisApi;
import ru.renett.api.whois.mapper.WhoisMapper;
import ru.renett.api.whois.models.WhoisResponse;
import ru.renett.exceptions.DataBaseException;
import ru.renett.models.Company;

import java.io.IOException;

import static ru.renett.api.WhoisApi.BASE_URL;

@Repository
public class WhoisRepositoryImpl implements WhoisRepository {
    private final Retrofit retrofit;
    private final OkHttpClient client;
    private final WhoisApi api;
    private final WhoisMapper mapper;

    @Autowired
    public WhoisRepositoryImpl(WhoisMapper mapper, String whoisApiKey) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        client = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request origRequest = chain.request();
                        HttpUrl newUrl = origRequest.url().newBuilder()
                                .addQueryParameter("apiKey", whoisApiKey)
                                .build();
                        return chain.proceed(origRequest.
                                newBuilder()
                                .url(newUrl)
                                .build());
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(WhoisApi.class);

        this.mapper = mapper;
    }

    @Override
    public Company getCompanyByDomain(String domain) {
        System.out.println(domain);
        retrofit2.Call<WhoisResponse> responseCall = api.getCompanyByDomain(domain);
        try {
            return mapper.mapToCompany(responseCall.execute().body());
        } catch (IOException e) {
            throw new DataBaseException(e);
        }
    }
}
