import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by avorona on 02.10.15.
 */
public class HttpsPost {
    public static void main(String[] args) throws IOException {

        String polVis = "http://www.polandvisa-ukraine.com/English/scheduleappointment_2.html";

        Document polVisDoc = Jsoup.connect(polVis).get();

        String url = polVisDoc.select("iframe[src*=\"polandonline\"]").get(0).attr("src");

        int searchString = url.indexOf("?P=");
        System.out.println(url = url.substring(0, searchString + 3) + URLEncoder.encode(url.substring(searchString + 3, url.length()), "UTF-8"));

        url = "https://polandonline.vfsglobal.com/poland-ukraine-appointment/(S(4vxsxh4533vsix45cyhotqqj))/AppScheduling/AppSchedulingGetInfo.aspx?p=s2x6znRcBRv7WQQK7h4MTnRfnp06lzlPrFCdHEUl1mc%3d";
//        Connection con = Jsoup.connect(url);
//        con.execute();
//        Document body = con.response().parse();
//        System.out.println(body);

//        System.out.println(con.response().hasHeader("Location"));

//        Map<String, String> dataMap = new HashMap<>();
//
//        dataMap.put("__EVENTTARGET", "ctl00$plhMain$lnkSchApp");
//        dataMap.put("__EVENTARGUMENT", "");
//        dataMap.put("__VIEWSTATE", body.select("input[name*=\"__VIEWSTATE\"]").get(0).attr("value"));
//        dataMap.put("____Ticket", body.select("input[name*=\"____Ticket\"]").get(0).attr("value"));
//        dataMap.put("__VIEWSTATEENCRYPTED", "");
//        dataMap.put("ctl00$hidCSRF", body.select("input[name*=\"ctl00$hidCSRF\"]").get(0).attr("value"));
//        dataMap.put("__EVENTVALIDATION", body.select("input[name*=\"__EVENTVALIDATION\"]").get(0).attr("value"));

//        System.setProperty("javax.net.debug", "all");
        String data = "__EVENTTARGET=ctl00%24plhMain%24cboVisaCategory&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=Z8KpfhR5icbZxV9Dy0N60dIbQELW3wWje6FEKt6uKq0Ed7CM5YUAxoMrG9ZpKcnJigiETp%2FXhlcwDI%2BgcFS%2Bs09sMM%2FssJmi6xtII3FHGh5p0O1JZbkaltHc%2FWcuYCN%2FnIeL%2FEyCykXjDtjlBLgBjnVbOQ6R1lU7eF2v%2BZ%2BFH9uwT%2BVaLLyeOLk4hI3NmNWPzzxtAFZmxOZeDAW9PX0iBXXfNSy1JXixXQqlYPWwzBbdCx7YT1Kde53DAmfILGVzo2is7GxqoMrRiG80hhzVQDfpOLX3A0fji5PxyFpG%2B%2FgJJvSnmU5hWakMUMv0ggiQRjYriDds15QAOSQ0qWHx6wq9%2BiEh5bpiiyi5jbbylPbVQHWWobrvcsL8yXcugO2gkWsrsChM%2FVca5nTTEiYh1dinm7XxzNPWSZ4yl8QuLXqElIDLhRjWKc5fJv26U%2Fzc4IYgDdsl3lpCDPtXLi7ZROivf1rjM1OXrGRpTw3AL%2FEHzi1pWwbudUbGfNwlwnjoLmMgF5vYrliYlUWsW3qNfshd5PPJ8DTBC%2BYx81aRcO28s3pVhB37%2F6WqI3AGhWujAAuyq6BZQn3psg8w8iLOYVRX%2BHwStBt9pf%2FBP6iGTtTDtf15%2FZg5BeTQK2k01adJnfH9iiD3wGcssrk%2F%2FfIutIBORKG0rYwCZXzqAYO5xsODQrlyB9YJZu%2FrkAGt9uyPoS5Mb4ZDnnquJPbcjrPxtvEHiG1ZwTWdOWozX5W7ZZ5X94KLmsFNIciqoOlWhUicIi2XbvABx%2BYb8aUSxQ9SeOLoDY970T0sz4WRVMB3ixhCFFo%2F2CpzId%2BSIxS5bRrIdm5KaJ7HpGc%2B2UAsBEAQg1FPepI7BrhaX2OoNyStFqtP3yxwsVJS%2FXlI9s3DkXURJ9YLI99M84uH5%2BGW2qkaGzF39QfiEPS17uHRv%2FZXdWBA%2BwY9Rsy5ItHOZNhaOyXetrHr1fMBoGh7V7Kn%2B1ap022L%2BEQVBWUGJEyKDbYiO%2BqV3ws1ct2YR0jhzvn9%2B4FbwLCU%2F5wnVfnyG3aoEy3uWk7je7n6qxzxMpdMCI5Vp2mmfW2LhHS6lDArxmOTTQgr6W3OQeiV5v%2FGtU9qZ1%2FigvFnbDQuk3bvycykv50y%2Fg55R4KRIGwDjAn%2F6Dkyd7jHlj%2BLY%2F772x0OYTIRgu4iV8L51eehtHCYAAMaebyC9h%2BvzWr5MDesvQ2L35YNdjtmI8QwvoBcrQz4m7p3dvNGZeIkT4UNewxD%2FP9c6C3i3zeGSFTrIDm6c9aSPKDZeX3TFnjVih9FtCV39MFzVSd1RvCEnFVvFtU95aTiRM2QQbjqO%2BlAyFgG8ug7WKs57EOEWPjbs%2FHC%2BKCD4TJ91qDTueCU0%2FyRZI3eSHvLd7FWBNzmuP5pPPZq9yu1pDrlkeg6tNm78qeyjnIvOfpW90TyR%2BPKrBm7cAALCtjJGRvzGLjqacmky2HHepow2rvV5%2FHrwlyrdVOMeUC3TKoxYScxq06cIs7NqOglio9r6YOmaMHABBXVEdgxWAkauVgBgP8vd4dDhtyAgUbs%2BX0zymT7uFn66uUQhux7xdMcxp8IM4ljOgIrOWzbOnT6091LdJV5JxLbBy%2F0BhlNVvZgbsIwiFQo0Py5Z%2FzqMR9m2GdbolWBEOpkvjSkCMuDC308gxBX1Uo3p1u5F3%2FQT7UGGZ4e0VBrf6J02157kdT3UkqSsvodK7xtkX2uuGYKZON%2BP7j8q0o5k%2FePbAfnfGcsTqZ0HNWGg%2F%2B5zt54GDVXpMVUvQVVMLXGp7QXXZKuuH2jT5g5hLnAR%2FDgm9sTN02ZCIVFw0rM0TJKM7Ei8w27XTuu7oZuBhZRp2ey6tKODGIlg%2Fe5%2Fq6Lo5RimVzgbkP%2BytntWpAx15%2FQu%2BaxqLGXKCUsVFm0jDkUL6%2FSqx07f%2B%2BUdiyczzQ1Rq%2FGDuP9xEk4ggUUpItyP2JQ10VmqxioTwhmV6g8Pxi6GraspdmzhNDdNcDo8De0czClym3XfjN36vqsi%2F1LZRycBy2msKhs0%2Bv5pWbNyfqZIcA4C9g11t3DB8JoYWhg1z4e53vi9K8z%2BAwFp9s1K%2FFfbmvxPsm4FucCmqklhHSM4dfjqjnLSPg4wpoFNVRNkls%2Bnw64UsRjP4obpbVMNAForpRkXOJRpUDcbDvFQ2zUzy8Sga%2FN6U63OOrIsTrZtvYlB2omxnc4eC5ptTu0tAwM20vnDJa8s9jBHzlzidWa5h5S7m%2FVxMWsTmY7ZZjghX2wL8r8BLHEhs1WS3icO%2FI37cbOpTW0rPOKCv9te0YtU%2FwAijtePfqiPCsutkXMoB2gY5Qew8qFKUWC%2B7qTksIAPLXoCDtWj0XXDH6ske3hdC2PNMuicF4tlmcCOv3wrA%3D%3D&____Ticket=3&__VIEWSTATEENCRYPTED=&__EVENTVALIDATION=xei7FyMTphxekpET%2FCFJAIV5yQt2CLyf53W3sdZQIpyWP7W4QebnYgU3jSXsjtqq2hYirN87V%2FAGplrkuQ%2Fb8JUAjbAy1o007dMhetklc2YdjlK%2BzGZG8NVguzM9xciGtN1zn0hpRVMWhSMiwuYXo0ecIE8qXxHBLXWIFiOk9jr3kTKe&ctl00%24hidCSRF=AppSchedulingGetInfo.aspx9786e9e6-5484-48ce-a671-3eab32b00913&ctl00%24plhMain%24tbxNumOfApplicants=1&ctl00%24plhMain%24txtChildren=0&recaptcha_challenge_field=03AHJ_Vut_dtoKyVun4jO5w0odZ7ZNMvIK-ugi7KyyMdVXzoePX3bb_xlwgJ9tHLdqmfX0PoI3HFYWUAIQC4nKMxDghn8odmO_nIdzatdFe4_3esTrRTsP7LRoGiOqVb52Ik0wRff1z6wTGLAE1PbHF6Ad-a2I4zzEYxcLhMyi6Q1CyaSvMyUbqmVMO1cPvqNJS7D2VHlXfYeh&recaptcha_response_field=&ctl00%24plhMain%24cboVisaCategory=235";

//        data = dataMap.entrySet().stream()
//                .map(entry -> {
//                    try {
//                        return URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                })
//                .reduce((s, s2) -> s + "&" + s2)
//                .get();

        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", "" + data.length());

        connection.setDoOutput(true);
//        connection.setDoInput(true);
//        connection.connect();

        System.out.println(data);

        DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());

        outStream.writeBytes(data);
        outStream.close();

        BufferedReader inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        Document document = Jsoup.parse(inStream.lines().reduce((s, s2) -> s + s2).get());

        System.out.println(document);

        inStream.close();

    }
}
