import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.security.KeyManagementException;

/**
 * Created by avorona on 01.10.15.
 */
public class Main {
    public static void main(String[] args) throws KeyManagementException {

        System.setProperty("line.separator", "\r\n");

        String source = "https://polandonline.vfsglobal.com/poland-ukraine-appointment/(S(5ap04a45wo2z3s55ntb1f055))/AppScheduling/AppSchedulingGetInfo.aspx?p=s2x6znRcBRv7WQQK7h4MTnRfnp06lzlPrFCdHEUl1mc%3d";
        Socket income = null;
        try {
//            URL url = new URL(source);
            String data = "EVENTTARGET=ctl00%24plhMain%24cboVisaCategory&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=GidmA9faoJJr%2F%2BrsiPSYESvUCsNttXJdfuyYvoXLbjUpq7M2GiQkhjhxsqZeisSyTX9XKdQZ28tlqKp3mIrbl86yoRrEH39T1nwtWXTXjBTi2VU9NEalVRuB3DJcw6nBjbMco%2FYXBttb7tunRPlVGhQz5NWchRCnHc%2BnsKaAFnB%2Bx1p%2BnNUJ3vEoG95P8l8A9VVX03wDVhUfw%2FwYbOQIzUT3EdAD5w75fQTlFjWXy5KHuyAJ0A4gJz4Zp8w5Dr0t1RMaUpo%2B4tCi3ARNKO7h6pIVJowmUVI0oJ7zjiQlOr3tj%2FslPAgA0fIfhtCK1W6vLXnIBM7%2B8nO4nLFVUa9iPxUL%2B8p3cY47hKz%2BUCGYdce8mlj3kvkEt4jpIJF09I%2Bcerh2meHggM4KPm0PtHdgtW4vRb6Is4MfZjeH%2FSWsPTj2zSWMVvF8tAZx2z3iy8e3tGSs3yLoLG%2F8kuolyhcUkozhpPtUDieYxgwdtGIxrZR48Nz6BYwRfgBTMytSAVJGSz%2FFQUYwecrV%2BVtWrzwWdVKeItZzvkdtZybxHIlLsfwNLZex62qeaPebTbpvzatpsyhwOdJ0QOHDQBsiTa%2F1E6jO0THQvSR7vmq9%2Fc9G6ekuDrkTQYsX2rLNJIwhALTkOPSg97Cq3GXM9BIurnQ%2Fy8uYen0371U6J7akl05ATGtNCf40t72Vhf9mBFrYgvVb6qwj1RbtfTBZEm%2F702s3JRbS5H5zD3CU%2Fug55zKjq91Sm%2F47yKRFmKkfREh6MPnUeC0XMr4JFAVT7fvADK13mv4%2FIfP%2Bde7Az9q73NcdRG5Fv1bJI2lFyQGKad639HGjHp6AJE2bdp83fkkLkqFIpghBa6ceBZ%2B7OGOceafdAiUT1Dc8yXOuJxqZcjcfsi4opKduAHXd3UpF4V7mGPWWkGlIXY3mIhwqgKQB57a0fUJq54Yw%2FEYFjzpbPfSGFJsXiH%2BmZDG7LZw2QNTP418ycJynnP0hl8b1jfJ%2FYNfk7bV8rcn5Mav0w%2F1IoXn1OKcEuwYsU8X5Xub7bX0C03AWZPJ%2FDSRQ0OLrlzVzzu2OBVUGWq7LAjeDUbOcT5TpyyWnEMhLt0U3B0yaCVWPeR3%2B8S%2FpPSdZVOz3ezvnwXlZvAvIBIke9vV0OdaFCGxaQveSFAeUH11Jy8RAUaXMUlQ5bBqagT8DZT3ezsupZIwjES3E%2BT2FLWaerjs9b3dYA2eXg6GtfP6y7N%2BuJwB2WXdUcqoolaPuV%2B1M1cTcce31jsZSkHpkv%2FmGnNUjJ4JvtKKcBLXNXCU3mqlBzR0BmNHMgA0x9dbY9%2BL6Q1EsOqDYGGZMz4WPX3JRa%2Bo905VtMH2LXJeKXI80xBT%2FX2pgtXH9lS%2B8CSlTpT0ovIu3uvft7ZVKYB2%2BZ5aqVSgkQW1TYGhejlyQJlVaSkY5vDeDV0XUqKwEZVB3qSanXlqLoEj%2BwVJWcVPieP7fOyJsGeUFbm6GUh3kZ9R87QUPTDaqEDUSBBVmQ3DtXF0X7ibO8i8QzcoUnCsugnMqgyBfBUSRg%2BDxvxRBWQO5nHJQIqLqCKutMIA3doQYSnBusYDhgD3tgm8V5JIio5aLpurxX350wOpc3f7zxW7RJN1B63mAldF8IEudPFIFJLK2HcdHPCI2wyJ7QjUwEyS7hSXqJcBlxBgmZ2d4BTYptgDRTSJd1nEOsMHgM2TeFqCmUeST3OhG6uEkujp7czecg4CIyw99k4NbWwFlCI2bLfzWXjz2cdd8zQ41L8LRAlnSBNnx7TrHv1cigyZPvTM3efE57AVvd2eyqp6m8kgbUibTZo3HRCofvsA4aQHardzYbN20uZwqtvb7p%2BtL2W5AsCcsFpTeJ7D5QmHxoYVjLvHVYHKlTenILuFWDIN4FOVHStwlAbFnhsb5DvPxfpz8tvtp5sbi78cy0WqeW1zX5wp6yevUsoevpx6Gqh9E%2BymYklNLAYwlutxY2fHqJ3cR8BoSEXhREwwDyn233kh98y4CMfSvLpAUxHr7HfsQ99cHs7spRVeLlM%2BSs7UezJ7NPg9qUxx1FTOv7usfMEhrDnX6y%2BycdWgH%2FmkDKvCHksaSR7Q9Gp%2Fu%2B19EVlyCKDES7idMwbEGQLBT%2BCGlmU5Dfll9ZrgiUoScZbqqT0JV8UNu2m%2Bm1GYOt2GdGfcv8%2Fz4oPILk0bqanKlyJXivP7GfD%2Bgsc%2FbbCJ%2BI1GACY81WyZJ7wvHWtXr5ivgEvoaGA3ThOD2RlQSZCAUgncXZyMG9qmsCo6jVPbWDyw9MMqBH2z%2FzaFTFLs1NgGbfKwoxQezq8RKawb%2FYs9cNfQh43B9gX1iTzH6jE5tiEXMYXyXObsCoTHat7Uo8I6MsNvzmrxC3FAhhGsXscnETI4Xtw%3D%3D&____Ticket=3&__VIEWSTATEENCRYPTED=&__EVENTVALIDATION=nu88aXtMbXW0wMt2EfXUeI24dRwa%2Fp6opVAJkskwfrhhhR2fnX3mc%2BXxqF2E8ww2Yp0kSmOVklWIccMZTknzCVZ7I%2F1ZnITKOjPXCLjIPginVSxoceIdf5t2rfrh0MN8pWN8lr0RMetnI6tf16T696R2zXrKZ0WEcPyK20hoHNn%2BaAsp&ctl00%24hidCSRF=AppSchedulingGetInfo.aspx1ecbf776-f601-40cd-a7ef-5da19ad27569&ctl00%24plhMain%24tbxNumOfApplicants=1&ctl00%24plhMain%24txtChildren=0&recaptcha_challenge_field=03AHJ_Vutr1Ty2aSea9S91jje8CpmKPLoKCLl-pro7f1-yKggYhlWKMe_UL17ge4MmoAE-CO-p3bkq0Kk26LE1mqAf51Kw0Hf-wrJ8qKL3sGSd6HzxX5NQt84BDTChiJRGw0GREHGGudNitpgSllU0tG7E2Gl4NHipeQRJT2NgLHD7gfotLhQhcwCPC32Hobjgwng59IBd1NwztA3V-rQoKB4NugVAPkYHPtWp0meqgFfHCryohd6e7wzoX4S2iKheBi7mSL0ADoaAEiSCICbEavb6WXFqFWo2PQ&recaptcha_response_field=&ctl00%24plhMain%24cboVisaCategory=235";
//
//            HttpsURLConnection connecton = (HttpsURLConnection) url.openConnection();
//
//            connecton.setRequestMethod("POST");
//
//            connecton.setRequestProperty("User-Agent", "HTTPTool/1.0");
//            connecton.setRequestProperty("Content-Lenght", data.length() + "");
//            connecton.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connecton.setRequestProperty("Referer", "https://polandonline.vfsglobal.com/poland-ukraine-appointment/(S(5ap04a45wo2z3s55ntb1f055))/AppScheduling/AppScheduling.aspx?p=s2x6znRcBRv7WQQK7h4MTnRfnp06lzlPrFCdHEUl1mc%3d");
//
//            connecton.setDoOutput(true);
//            connecton.setDoInput(true);

                SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket("polandonline.vfsglobal.com", 443);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(connecton.getOutputStream()));
            writer.println("POST /poland-ukraine-appointment/(S(5ap04a45wo2z3s55ntb1f055))/AppScheduling/AppStatusPage.aspx?P=0TNQYhLqBEjaDGeNk/Yi1Q== HTTP/1.1");
//            writer.println("POST /poland-ukraine-appointment/(S(050ypg55oonkqejsflpgm445))/AppScheduling/AppWelcome.aspx HTTP/1.1");
//            writer.println("From: blabla@com.com");
            writer.println("User-Agent: HTTPTool/1.0");
//            writer.println("User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/45.0.2454.85 Chrome/45.0.2454.85 Safari/537.36");
            writer.println("Host: polandonline.vfsglobal.com");
//            writer.println("Connection: close");
//            writer.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            writer.println("Accept-Language: en-US,en;q=0.8");
            writer.println("Accept-Encoding: gzip, deflate");
            writer.println("Cache-Control: no-cache");
//            writer.println("Upgrade-Insecure-Requests:1");
            writer.println("Content-Type: application/x-www-form-urlencoded");
            writer.println("Referer: https://polandonline.vfsglobal.com/poland-ukraine-appointment/(S(5ap04a45wo2z3s55ntb1f055))/AppScheduling/AppScheduling.aspx?p=s2x6znRcBRv7WQQK7h4MTnRfnp06lzlPrFCdHEUl1mc%3d");
            writer.println();
            writer.println(data);
//            writer.println();
            writer.flush();

//            BufferedReader reader = new BufferedReader(new InputStreamReader(income.getInputStream()));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connecton.getInputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Location")) {
                    System.out.println(line.substring(line.indexOf("/")));
                }
                System.out.println(line);
            }

            reader.lines().forEach(s -> {
                if(s != null) {
                    System.out.println(s);
                }
            });
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (income != null) {
                try {
                    income.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
