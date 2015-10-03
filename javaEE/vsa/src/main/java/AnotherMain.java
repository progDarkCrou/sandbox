import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by avorona on 01.10.15.
 */
public class AnotherMain {
    public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public static void main(String[] args) {
//        String [][] req = new String[][]sun.net.www.MessageHeader@1ff8b8f6 pairs: {GET / HTTP/1.1: null}{Accept-Encoding: gzip}{User-Agent: Java/1.8.0_60}{Host: polandonline.vfsglobal.com}{Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2}{Connection: keep-alive};
        Connection connection = Jsoup.connect("https://polandonline.vfsglobal.com/poland-ukraine-appointment/(S(bptaxojh3n5rfv2iylgbz455))/AppScheduling/AppWelcome.aspx?P=s2x6znRcBRv7WQQK7h4MTnRfnp06lzlPrFCdHEUl1mc%3d");

        connection.header("Content-Type", "application/x-www-form-urlencoded");
        String data = "__EVENTTARGET=ctl00%24plhMain%24lnkSchApp&__EVENTARGUMENT=&__VIEWSTATE=tjpYNFtNTB0Wsq%2FWz9%2Btkg149VXuBAaz3hblBwl5%2BopCCiqn1kE%2BH3NOquG3U4B6fZA4Ll61oyTDqE3Vor%2FeT6U8KAnX6JsnCJGuWEMRDOPtxPGS%2BY%2BPMtqpNg%2FW59LOybs2C%2FWg7YaQ%2BJKDQ4vyLDAd2HVxijnFaPIR%2BFhds4tWnD8boHkDYBE2z32gKyU7S%2FjSEVCye5e4IV2t1%2FAeTVhDIhZkVJxbjBqrz5olO6mqtRk61CAVVO8PMsXrhADJ8Y5bOwuZ8R80KSuwuxOUdtU3%2FWn8draRudcl5jA6BnCKZArN4WnOSDlBMxIRrbNOdQL76jXus%2BCQuZWsEObTQrFlLRbIUyYcnYv81PS%2ByCMc83lQKZH1B37SPO6cHW8HCrYmN4zeo1FkHokRQTVbFNkmZdUuxPd6MdTgSEn%2BO9lbSI%2Fw0s1Fi8Xxo7eycsI5jIbvQy6xpR5AmtNbqFZRJyhYMhJt6N9FNBacZSZbEQrUirN%2F6CdpLL6L9B9zkWPeNBIer52PYX9uw98iF3sORq%2B3lQpP1P3EBSUiqxF4VnOXSsfoWdQSFozcI91rj5mkD5Vu2lt5Wl7dMgM1h638tSjr7aB8b9EQZJrinrQgu69ihyNkSw9vWNsq%2BvrTgNfwjepAtpQG9ZTapybOq6jj743Dp24KWVn28%2F1ApGmgOMLabChKw6bk%2FNXE7%2BBK4DTZt2FnJeDxIb1YyYCydwTO809RK4A9hKONp9X%2BrOacjNhDQVeQFk5u1fckf9C%2FiN06gvw7ivYRcw7zG0G2E78JXpJwZWB2GNEq9Pppd%2FyokJzhWAF9fBWi90lp3qIm5aV6Re1Ql%2FEWhSZlbv09NMtzJzwjrnobQcr34F28gEzyv1W6yp6eNBbCmcc38gna8KDRPMQT0%2BByjI89rPiPmj8fzaT9p%2FKA0prA5b75D75Weeu1b9Oqeb0c6LU%2FPx7FTWT3Kty4esa3B%2FiOV1V7LQ53tqhm3VEPe9klGal2%2BKhWtZOVpM4SA4OODqRunDMF81J37yr7NwEXCvEZHyWGSq93TR80ifpVdRARWtallqA7Gfg8NQxRgYU0Ad2cluEIcjqicspRmch6qjBO00JsyOF1iJEQ4%2FubG5ZWx1UPlBdufraujSHtP4mc7O0huG7wiGIYmp9QGiyqIWPr8xtLYUD%2BUs7bI6e0OWM9oYAzQ4Wv%2BDC0vOQO7nBZql%2BFWpuhcRA52YaOkuCQLxuyDWdkPA93P8R59%2Be1rpyu5yGCFehICIrUjZ9P3gMLEyQDbTsVgYeJMIHH%2BJMZUehWUVvv%2FkwQ%2BbBQRxvQUak9zmEZkf%2FJ6PwkdePE%2BnDavCK2DwSEOaw5ASG9G%2FuQtdSInH2ms1rRukPh1w2MurM5JiTRov7LwHS5eWJtvtchRV5GqNGAfxgtqb%2FDTso8vAXQBQ9CS3C3VcAcjmYUgzjdt3QTXH%2FEr6D%2BHNhxkhPXYEeuhuBUfSbRxRCIanx7ON6CQiE537blmtGVYmVfXnwjaUXO8KxyOwSkICALqZYNe3mRpzRnwS3lvyroLQ%2BArwGIdhdAkoV%2BYiLeT3F3U6iTF3WUI4pAXp%2Bweq2k&____Ticket=1&__VIEWSTATEENCRYPTED=&__EVENTVALIDATION=a8HWi6TH1H79JTOy7J%2FtFc2sjTzu4McJ%2FmkVBhMoxCVRKyW9nz6keEhROvrz9aLyZbWiwWjKfwVRqkFGHS63VrwHOHWUQd6RfXQMn8w%2Fc0ixyqIQoxwZF49mBypa23AawY2GmA%3D%3D&ctl00%24hidCSRF=AppWelcome.aspxa35703f7-7f22-4afe-b01d-f537589d0ac5";
//
        String [] dataSeparated = data.split("&");
        Map<String, String> dataSeparated2 = new HashMap<>();

        Arrays.stream(dataSeparated).forEach(s -> {
            String [] splited = s.split("=");
            dataSeparated2.put(splited[0], splited.length == 1? "": splited[1]);
        });

        connection.data(dataSeparated2);

        try {
            System.out.println(connection.post());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
