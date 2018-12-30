package webcrawler;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class WebCrawler
{
    public static void main(String[] args)
    {
        int c = 0;

        String site = getSite("https://en.wikipedia.org/");
        List<String> prev = extractUrls(site);

        while(true) {
            try
            {
                int rand = ThreadLocalRandom.current().nextInt(0, prev.size() + 1);
                String url = prev.get(rand);

                if(url.contains("w3.org") ||
                url.contains(".js") ||
                url.contains(".cs")) continue;

                System.out.println(c + ": " + url);
                String ns = getSite(url);

                List<String> thing = extractUrls(ns);

                if(thing.size() > 10) {
                    prev = thing;
                } else {
                    continue;
                }

                c++;

                /*try
                {
                    Thread.sleep(1);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }*/
            }
            catch(Exception e)
            {
                continue;
            }
        }
    }

    public static String getSite(String site)
    {
        StringBuilder builder = new StringBuilder("");
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL(site);
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
               builder.append(line);
            }
        } catch (MalformedURLException mue) {
             mue.printStackTrace();
        } catch (IOException ioe) {
             ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {

            }
        }

        return builder.toString();
    }

    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file)" +
            ":((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";

        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while(urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }
}
