import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by avorona on 05.10.15.
 */
public class TestMain {
    public static void main(String[] args) throws IOException, InterruptedException {

        Long latency = 1000L;
        Long start = null;
        int fails = 0;
        int maxFails = 20;

        while (true) {
            String url = "https://polandonline.vfsglobal.com/poland-ukraine-appointment/(S(un3j1h55bszukw452uqacz55))/AppScheduling/AppSchedulingGetInfo.aspx?p=s2x6znRcBRv7WQQK7h4MTnRfnp06lzlPrFCdHEUl1mc%3d";

            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Referer", "https://polandonline.vfsglobal.com/poland-ukraine-appointment/(S(un3j1h55bszukw452uqacz55))/AppScheduling/AppScheduling.aspx?p=s2x6znRcBRv7WQQK7h4MTnRfnp06lzlPrFCdHEUl1mc%3d");

            connection.setDoOutput(true);

            DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());

            outStream.writeBytes("__EVENTTARGET=ctl00%24plhMain%24cboVisaCategory&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=loP6gSjlanrwQcQy55SSPIg1%2BFTauqvZxcs87ve9SYtWo5sQHnTJZjeQtplsF0AfPtI3mcaJ%2FWEDC8%2FQm0kT3L0QNOkv%2Bv%2Fe%2FdbZObtfJyVPDfop1cTSYTbt69I8hy1dgrvZlvfmsNZn1snaQQROf%2Fkp8FJifytLidFRj84o%2FBkJD5CXHjf06Fpx85weFXpi7SHGAL9fevXtaihY%2B7DDsKkH8JUjdeqwJ%2BaxeO4YegdP4S%2BTugZIp4Lr9ebk%2FcP%2Fn7NxiPRdhZ0CvMC65tsgLfDrkysSGRK12lvfQoK3BuhctA%2FM%2BvdSB1U1ZVwDFgwUkQ%2BDlyHzTFGEfAghcSPhl%2BAQcUYqZS3tgVdn9kfeL4QYpkPRIXpBCur7OPa2jZhbckFGG3i8Y4A8A%2FxispKBjo3Jr9iGZK9gGj%2BieqDZqZYejY2%2BNu1wTZvuEF%2BZIW3ILYCsno8VOz2nOFzGSosJsDhyFCKLJV9qYMfzleVF2itEYz7%2BW%2FM%2FOC7iKMWsdB3%2Fy%2FgGer9%2BhhCRL1duwUH0hwkPjLlBnLHWeeaFstu1c1jMfhePiMJm6jBW77Dz1bLLcYdaanKq5Tu0NnROv7Bdg2M%2BeupuNQfYevyMV4khww3CeQmDeB4DRY4lm%2FD1GFMx8Ink4lNPPbVSW8g%2FSsqL%2F6Od12aVFaXH%2B4vrQ2ccPvqKBWOtutZV9dOahxonK0OeZy85k5DfpfmXIikafao4YtpIkxj9R8H8jlYAoepBiI6QeH4%2BHdetdCvA7qEZCAWmLDIBvUaj0pZAfntf4yzU3lheFla7vdC9NhuNXC5Qe0hpuMPY6UrU9zGTfV90V5%2FNBxu0h8xnFlegwJcZ1BKnNxXSrnO6bz0IXLOZmeM3bGHVz3iB5ZU2Iq83%2F%2FL19KmZc9sYW1w333HGzCxNXADZuPpXckaOntaZVtaB0s4pp8JQAzfcnaWG5MgTvwqTp72Adu7Oq4imH6KutZx4%2B4A3qbON3CRQduPzwzO%2ByPBsObBe84JzTqeKKUB1r9fW7DXpdOe6pOvglVPG5j2IU0%2BRzAhqzAmrZxo9wEJXFA9esqxaUr4OHD3bj9VBn%2BcuNdWOZL1htvbHj5Mabes6YJVLMdQVDTqmrn4Gv%2Fvtgj0kBxaTm%2F3TzaUC5PO3JFUa1D9PDZ%2FXsdJ0bMa5W5wHiwxAG8%2BZW675OJbLFlHW0A%2BLACNlERGXwiJCZeFizRyoKHEP3ewejdOLJ17Kb6iBbu14bvy6BI556tBqSssy8o%2BdO3RRmzCQcFzlpiOfa93GRFxTJOYs%2BmRX9pHsT6fj42uamtuvf%2Fn9wXfz2EQ3n9HyX1MqwHLZkjVm9zmfGVruGgnz3Kr4yacjMmLwPnCfuBVZZ7a6EvseoGIQrI2RbPNJOXPGoLiRi9GtP2qk6t3JoUTYqS3jTWeHpjU%2FadB4xayZ2YDkRZJMG8NcUBlT%2FYYXHp%2Br7WwRRZg%2F%2FCac5%2FfJH%2FglVcur6z6eZwKKvPaO2wQEnEoEGfcaySwq35Q66MljWdu1khikWQYvrbCLfgHd0kuvMyJ%2Bmuqk2H7mvC7u0H6TMD%2FwMjLAszUkcY%2FbYUuca2nV7hS1FFIFUYSJ9YRfc3xd7lODUZdBhD9lK%2ByeBd63EE%2BSV5kcU%2BwCvwSy71EPtQCLYW3TsA%2BSRr5KyvTNF3KYCCyy3C1vhK%2BPK8HWBbNWuf8XLyNdtqXEFSLzXSaiutkLxf%2F9g3zUGfu7lGcXOGPHmca0j%2Bg%2BQ%2B3TP5oWYz57U%2Fhcqw7Cdrx%2Fw28C%2FIxA1%2FB69O%2FGWFBjiRL2YwGLJmKzpCbBQm8QcdgR3uOYiSFvddh%2BMYwtdZlVvO38ibU%2B9GIYe%2FhvGlrCECz3mTF%2BLNPPbglIUycDct0w9um5rmrKVFVnG9tmQQHTxn6bz9NQtQOdWfNnkgbKL734GNf%2BcHYy7TarjecZ6xcDZPMjIaZLylnwjVn1Qn9FqCwgNMMq0jsdAT9rbTo5HkA58TyHgSSpKMTGkFoDlpqH1uhovfAgAq5xDXRW62eAuo%2FKJ2CDQ%2BGo8X0ZrpJGkcqR5sktOnhqLLMrbQURKmw7rh2cp5REKMbjihoALu%2FtCmZHtaS1mCh%2FAVGyX0FTtPhx7XErNjoQhsqGHx8gU7Y6nzilB4cQYbTNBn10K%2F2sRy%2Bt9qrKz94f18X8C48swIdxRVmS56bARpDDIPSRGcZpvM6nXaPaFrWNGXjKsb3lq3KPgq3ntTRRT4Knj3PDdoSv5qwG%2BARC47F5ONp30NfTBGee5iz%2BD4x9pVXg76x0AmruImQOfWmg1XqNXc0VGS%2FM%2BB936kAT1RN2QVqiu9UyZMKCFmAkKzuQChDOZESFAYtGXNDEwCV4rubrQzUCQKCkK5ehptDNsTBObQ%3D%3D&____Ticket=3&__VIEWSTATEENCRYPTED=&__EVENTVALIDATION=zXDKTRajQ71rf%2FVYUJDxune2PjyNPyJlmA7VhetbSfrBGkhzpiXwKaWggN9R35BmIealHVKx5GsD8gVpfEeotW3Sdn8OAe6%2FMTcjJ3qhcexljmTswENxzefBrpVsGYLa7PlUIpbtbi1kZN715otpMhr7MIgnKYPYfkL3zdg9g0wCNK0u&ctl00%24hidCSRF=AppSchedulingGetInfo.aspx99f89cb7-6d75-4575-a797-91c2bbeedb2f&ctl00%24plhMain%24tbxNumOfApplicants=1&ctl00%24plhMain%24txtChildren=0&recaptcha_challenge_field=03AHJ_Vus19_-sBRQzaGA_gGSDbBXbcFyi3NcBw_G0CadEyv9JFYW1aqc0_ZeJbFG2iYG9KsHliA8HvhuAM1BQicwDA3V3plkknZwCLXU-L64Z8t5BJ1iXdHl-ZnaYLCIq4Z-grjmwmpf2clKYgKMk-wZPY85QfT7GFgu7Xb_tuL1JgUFFnTgp3TgJ1RpI1qMFLkcu1OOJOIMsOOg-OIzRpQuGN_jqrGRgAw1KAkSV5xEXI3MqyQrwJ4EiDYtRtovVx0STwq2fkCqbpxPaVBYkimPn0mI8zBHBzg&recaptcha_response_field=&ctl00%24plhMain%24cboVisaCategory=235");
            outStream.flush();
            outStream.close();

            try {
                BufferedReader inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                Document document = Jsoup.parse(inStream.lines().reduce((s, s2) -> s + s2).get());

                System.out.println(document.select("#ctl00_plhMain_lblMsg").get(0).html());

                Long time = start != null ? System.currentTimeMillis() - start : 0;
                start = System.currentTimeMillis();

                inStream.close();
                fails = 0;
            } catch (IOException e) {
                if (fails >= maxFails) {
                    throw new RuntimeException("Too many fails occurred. Please restart application");
                }
                fails++;
                continue;
            }
            Thread.currentThread().sleep(latency);
        }

    }

}
