package ru.renett.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.renett.api.whois.models.WhoisResponse;

public interface WhoisApi {
    String BASE_URL = "https://www.whoisxmlapi.com/whoisserver/";

    @GET("WhoisService?outputFormat=JSON")
    Call<WhoisResponse> getCompanyByDomain(@Query("domainName") String domainName);
}
