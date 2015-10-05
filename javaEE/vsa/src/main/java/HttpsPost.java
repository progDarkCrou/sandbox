import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by avorona on 02.10.15.
 */
public class HttpsPost {
    public static void main(String[] args) throws IOException {

        String polVis = "http://www.polandvisa-ukraine.com/English/scheduleappointment_2.html";
        String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/45.0.2454.85 Chrome/45.0.2454.85 Safari/537.36";

        Document polVisDoc = Jsoup.connect(polVis).get();
//
        String url = polVisDoc.select("iframe[src*=\"polandonline\"]").get(0).attr("src");
        String urlBase = url.substring(0, url.lastIndexOf("/") + 1);

        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Referer", polVis);
        connection.connect();

        BufferedReader inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        Pattern inputPattern = Pattern.compile(".*<input.*");
        Pattern namePattern = Pattern.compile(".*name=\"(.+?)\".*");
        Pattern valuePattern = Pattern.compile(".*value=\"(.+?)\".*");
        Pattern formPattern = Pattern.compile(".*<form.*action=\"(.+?)\".*");

        Stream<String> lines = inStream.lines();
        StringBuilder formOut = new StringBuilder();

        ArrayList<String> listOfData = new ArrayList<>();
        String data;
        lines.forEach(s1 -> {
            Matcher form = formPattern.matcher(s1);
            if (form.matches()) {
                formOut.append(form.group(1));
            } else {
                Matcher input = inputPattern.matcher(s1);
                Matcher name = namePattern.matcher(s1);
                Matcher value = valuePattern.matcher(s1);
                if (input.matches() && name.matches()) {
                    String n = name.group(1);
                    switch (n) {
                        case "__EVENTTARGET":
                            try {
                                listOfData.add(URLEncoder.encode("__EVENTTARGET", "UTF-8") + "=ctl00%24plhMain%24lnkSchApp");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "__EVENTARGUMENT":
                            try {
                                listOfData.add(URLEncoder.encode("__EVENTARGUMENT", "UTF-8") + "=");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            try {
                                listOfData.add(URLEncoder.encode(name.group(1), "UTF-8") + "=" +
                                        URLEncoder.encode(value.matches() ? value.group(1) : "", "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }
        });

        System.out.println(data = listOfData.stream().reduce((s, s2) -> s + "&" + s2).get());

        System.out.println(listOfData.size());

        String newUrl = urlBase + formOut.toString();

//        Element form = doc.select("form").get(0);

//        Map<String, String> dataMap = new HashMap<>();
//
//        dataMap.put("__EVENTTARGET", "ctl00$plhMain$lnkSchApp");
//        dataMap.put("__EVENTARGUMENT", "");
//        dataMap.put("__VIEWSTATE", form.select("input[name=\"__VIEWSTATE\"]").get(0).attr("value"));
//        dataMap.put("____Ticket", form.select("input[name=\"____Ticket\"]").get(0).attr("value"));
//        dataMap.put("__VIEWSTATEENCRYPTED", form.select("input[name=\"__VIEWSTATEENCRYPTED\"]").get(0).attr("value"));
//        dataMap.put("__EVENTVALIDATION", form.select("input[name=\"__EVENTVALIDATION\"]").get(0).attr("value"));
//        dataMap.put("ctl00$hidCSRF", form.select("input[name=\"ctl00$hidCSRF\"]").get(0).attr("value"));
//
//        String data = "__EVENTTARGET=ctl00%24plhMain%24lnkSchApp&__EVENTARGUMENT=&__VIEWSTATE=%2BkpYsnr6l4WSuITmNdPnN6130I73yLemrEEikzTXXns674iJqIMqu88iHzH%2BaWfVoncezIBazf5AGyq8evaW9tMx06qc%2BfzW3ON0nORrPbDeloa189OeKFD5ssQIAxc6NbFCo7YDr03SS1w3GDyOS2CoOLhK7ndQZtm47lzhXrVGffaPwACBt433SuLuL9VyhtXWU%2BdFVAA9VgvT7y0hKQs8YoERiRnR%2FEQNCQXfF9ucLpCN2VcPOlJomFf4UmygIyfGUZxkNhkX79fEaSWSg3Qh3FRdv1KRcf%2BPa4skd9BfqMucLfE3umjvC83v2LsBptQuX%2FIm04df5LjNonO%2B5fOYZO42bP3HGAHCP%2B0LLqarZGZrVASuDRxE%2FxfR8E7JcU2Hvly0P29iD4gXOcEZbiE2CWbxEZP%2FrCJLqTs5qUIOU7A71hrtNDt2nth4t6FWFT%2BawRQfOEu7A6739xujAufKFCC5gBJP84A3EJlpb8EMTuHIUPuljuq7sPU8yNY%2F0P8ecN%2FbEPlRJMY858e52%2F7aA2nHWLoVbAg7lDf2yZunN8W7FKh9K4bKdG0FcI4pW3YIoWgjGEOGHq6cHOehjr2csFoYgffvQHl2KsofHxcYCnSN4g3LyKyhGZB%2FVnlXCeyfy8igbDOoyWQPz0QOwi63HCIDh23xbK7BNLs3plSLevl92o8YBibS3sR%2F4wRIpaOI%2BV5v0T%2FZqei1Xyy%2FWeN%2BEBkWP14nKaZqocYnDeIPbxAFIuGaWJ5TBOonL33klgZ18s7xXIDi3V5l%2Fv%2FvA8pXL%2Baf8SXNxJG9R6hIBVO6D4z8NzrtI8laGjqUWStjk%2B4NJqkcmyNoaUyGKfO2xNecu6hLEHWTDPWkW1YJlbopTUPnGzlkCAIqMG5ezfJseZOzz99U9u%2FNo9SblvyShnk2aM8%2FiXsVEBdZ7cY4ry0Sl3nzCHW5abAc65lLBQUX1gqrY3NrtHnzZ3lOsd3gIWGqTeked5hP15%2BwvZVqMym8i93g4NVUvDVR2lkCnYB4vX8qKYZi26tuCyDEHNpqvDCq1Uqfl%2Ftvl5hXM5umGRyRGapEtO9yhXh7SnL%2FKnouMsiAp0Pe%2BAQ4pue7wjvArixrBFbD%2BjoxwXUTnpW%2BDJ9%2Buryt%2BLzWOk3UHkfoqV0F5T4zNyrRL%2FciGV9VczlwC9NNDVz6b1Bgavmws6ZzZHypIpwbI6FuGYZ7MdSOA9pSW68MUkDJqxZMkW4ZKxgeJ8D5Kzkg9OF3GAHNZhwAr0BpHsq5H4FfBmNQgTcttoMmZskVuksa3a6hpytBUruDSjQqosmbD08cbnbzcDF8%2BrO8dDuhyy8MDC7JcA0CMMIx7LZ1et2jHF7JxXDQmS8TQu7TP39nfe%2BVsel%2BdTV%2FAn5SNzT4i291IpRvfICrEAH3x3PxBdvjP210hCpFz2i7MAiIObF%2BFTNEV1kbNm159gNwPBj2j04BON7m%2BlCfNOybA5orymDP7yiN0G40JueOyMDnz9nDLZcylJZfdAlcKFe%2BYoY%2FsKjpRbB7ZsXPolOUt73pFhU%2Bmx1kBo1i3K6EPRGz%2FFr0ytBWAh4fqyAeadm2Oh7%2F&____Ticket=1&__VIEWSTATEENCRYPTED=&__EVENTVALIDATION=CuhE2Knk1BjDvw%2BtJjGisbp2e7b06DDpmw7C2F1x8pDOXZEn8iOjUjqaH7H95KlF3apCFcAoQpbyPYTB1IVNo7A1li57TIJsE1jbp8tIpLIaxY9qXYT89B4Apa9lshy74fHyNA%3D%3D&ctl00%24hidCSRF=AppWelcome.aspx94fe8c17-ac65-492d-a9c4-b6ca579efe44";
//        data = dataMap.entrySet().stream().map(stringStringEntry -> {
//            try {
//                return URLEncoder.encode(stringStringEntry.getKey(), "UTF-8") + "=" + URLEncoder.encode(stringStringEntry.getValue(), "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            return "";
//        }).reduce((s, s2) -> s + "&" + s2).get();
//        url = "https://polandonline.vfsglobal.com/poland-ukraine-appointment/(S(xhoaxp45u1peyi45hrxuy045))/AppScheduling/AppWelcome.aspx?P=s2x6znRcBRv7WQQK7h4MTnRfnp06lzlPrFCdHEUl1mc%3d";

        connection = (HttpsURLConnection) new URL(newUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Connection", "keep-alive");
//        connection.setRequestProperty("Origin", "https://polandonline.vfsglobal.com");
//        connection.setRequestProperty("Host", "polandonline.vfsglobal.com");
        connection.setRequestProperty("Referer", url);

        connection.setDoOutput(true);
//        connection.setDoInput(true);
//        connection.connect();

        DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());

        outStream.writeBytes(data);
        outStream.flush();

        inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        Document document = Jsoup.parse(inStream.lines().reduce((s, s2) -> s + s2).get());

        System.out.println(document);

        inStream.close();

    }
}
