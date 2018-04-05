/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

//import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicIndexer extends WebCrawler {
        URLIndexer myurllist = new URLIndexer();
        Integer i = 0;
        private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
                        + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

        /**
         * You should implement this function to specify whether the given url
         * should be crawled or not (based on your crawling logic).
         */
        public BasicIndexer() {
        }

        
        @Override
        public boolean shouldVisit(WebURL url) {
                String href = url.getURL().toLowerCase();
                 //System.out.println("hello");
                 //i++;
                return !FILTERS.matcher(href).matches() && href.startsWith("http://hj.se/jth/en/education");
        }

        int CountWords (String in) {
           String trim = in.trim();
           if (trim.isEmpty()) return 0;
           return trim.split("\\s+").length; //separate string around spaces
        }        
        /**
         * This function is called when a page is fetched and ready to be processed
         * by your program.
         */
        @Override
        public void visit(Page page) {
//                int docid = page.getWebURL().getDocid();
//                String url = page.getWebURL().getURL();
//                String domain = page.getWebURL().getDomain();
//                String path = page.getWebURL().getPath();
//                String subDomain = page.getWebURL().getSubDomain();
//                String parentUrl = page.getWebURL().getParentUrl();

//                i++;
//                System.out.println(i +", Docid: " + docid + " - URL: " + url);
//                System.out.println("URL: " + url);
//                System.out.println(docid + " - Domain: '" + domain + "'");
//                System.out.println("Sub-domain: '" + subDomain + "'");
//                System.out.println("Path: '" + path + "'");
//                System.out.println("Parent page: " + parentUrl);

                if (page.getParseData() instanceof HtmlParseData) {
//                        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//                        String text = htmlParseData.getText();
//                        String html = htmlParseData.getHtml();
//                        List<WebURL> links = htmlParseData.getOutgoingUrls();
                        URLIndexer.getInstance().addPage(page);
                        //System.out.println("CountWords: " + CountWords(text));
//                        System.out.println("Text length: " + text.length());
//                        System.out.println("Html length: " + html.length());
//                        System.out.println("Number of outgoing links: " + links.size());
                }

//                System.out.println("=============");
        }
}