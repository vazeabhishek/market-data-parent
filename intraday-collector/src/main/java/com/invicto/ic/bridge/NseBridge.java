package com.invicto.ic.bridge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invicto.ic.model.EquitySnapVo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class NseBridge {

    private final ObjectMapper objectMapper;
    private final String baselink = "https://www.nseindia.com/market-data/equity-derivatives-watch";
    private final String equityApiLink = "https://www.nseindia.com/api/live-analysis-oi-spurts-underlyings?type=underlying";


    public NseBridge(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public EquitySnapVo fetchIndexAndStockFutures() throws IOException {
        Connection.Response equityData = Jsoup.connect(equityApiLink).timeout(10000).ignoreHttpErrors(true).validateTLSCertificates(true).followRedirects(true).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36").headers(getHeaders()).method(Connection.Method.GET).cookies(getCookies())
                .ignoreContentType(true).execute();
        EquitySnapVo equitySnap = objectMapper.readValue(equityData.body(), EquitySnapVo.class);
        return equitySnap;
    }

    private Map<String, String> getCookies() throws IOException {
        Connection.Response response = Jsoup.connect(this.baselink).timeout(10000).ignoreHttpErrors(true).validateTLSCertificates(true).followRedirects(true).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36").headers(this.getHeaders()).method(Connection.Method.GET).execute();
        return response.cookies();
    }

    private Map<String, String> getHeaders() {
        Map<String, String> header = new HashMap();
        header.put("Connection", "keep-alive");
        header.put("Upgrade-Insecure-Requests", "1");
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header.put("Sec-Fetch-Site", "none");
        header.put("Sec-Fetch-Mode", "navigate");
        header.put("Sec-Fetch-User", "?1");
        header.put("Sec-Fetch-Dest", "document");
        header.put("Accept-Encoding", "kgzip, deflate, br");
        header.put("Accept-Language", "en-US,en;q=0.9,sv;q=0.8");
        return header;
    }
}
