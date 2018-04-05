package datacrawler;

import CLIInterface.CLIMenu;
import Crawler.BasicCrawler;
import Crawler.BasicIndexer;
import Crawler.Indexer;
import Crawler.PageInfo;
import Crawler.PathInfo;
import Crawler.URLIndexer;
import SearchFiles.SearchFiles;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import java.io.InputStreamReader;
import java.util.List;
/*
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License, version 3,
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
/**
 *
 * @author Hossein Ebrahimi
 */
public class DataCrawler{

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws Exception {
        /*
         * crawlStorageFolder is a folder where intermediate crawl data is
         * stored.
         */        
        String crawlStorageFolder;
        //if (args == null) 
            crawlStorageFolder = "data/";
        //else 
         //1   crawlStorageFolder = args[0];
        
        
        /*
         * numberOfCrawlers shows the number of concurrent threads that should
         * be initiated for crawling.
         */
        int numberOfCrawlers = Integer.parseInt(/*args[1]*/"1");

        CrawlConfig config = new CrawlConfig();

        config.setCrawlStorageFolder(crawlStorageFolder);        

        /*
         * Be polite: Make sure that we don't send more than 1 request per
         * second (1000 milliseconds between requests).
         */
        config.setPolitenessDelay(500);

        /*
         * You can set the maximum crawl depth here. The default value is -1 for
         * unlimited depth
         */
        config.setMaxDepthOfCrawling(2);

        /*
         * You can set the maximum number of pages to crawl. The default value
         * is -1 for unlimited number of pages
         */
        config.setMaxPagesToFetch(1000);

        /*
         * Do you need to set a proxy? If so, you can use:
         * config.setProxyHost("proxyserver.example.com");
         * config.setProxyPort(8080);
         * 
         * If your proxy also needs authentication:
         * config.setProxyUsername(username); config.getProxyPassword(password);
         */

        /*
         * This config parameter can be used to set your crawl to be resumable
         * (meaning that you can resume the crawl from a previously
         * interrupted/crashed crawl). Note: if you enable resuming feature and
         * want to start a fresh crawl, you need to delete the contents of
         * rootFolder manually.
         */
        config.setResumableCrawling(false);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */

        controller.addSeed("http://apache.org/");


        
        try {
            CLIMenu myclimenu = new CLIMenu("DataCrawler");
            myclimenu.add("Crawle");
            myclimenu.add("Index");
            myclimenu.add("Quary without simulatery");
            myclimenu.add("Quary quary with simulatery");
            myclimenu.add("To Quit");
            int selectedcmd = 0;
            while((selectedcmd = myclimenu.run()) != 5){
                long startTime,endTime,totalTime;                
                switch(selectedcmd){
                    case 1://Crawle
                        startTime = System.currentTimeMillis();
                        controller.start(BasicCrawler.class, numberOfCrawlers);
                        endTime   = System.currentTimeMillis();
                        totalTime = endTime - startTime;

                        System.out.println("total time: " + totalTime/1000);                          
                        break;
                    case 2://Index
                        startTime = System.currentTimeMillis();
                        URLIndexer.getInstance().indexer = new Indexer("data/", true);
                        controller.start(BasicIndexer.class, numberOfCrawlers);

//                        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
//                        long totalLinks = 0;
//                        long totalTextSize = 0;
//                        int totalProcessedPages = 0;
//                        for (Object localData : crawlersLocalData) {
//                              CrawlStat stat = (CrawlStat) localData;
//                              totalLinks += stat.getTotalLinks();
//                              totalTextSize += stat.getTotalTextSize();
//                              totalProcessedPages += stat.getTotalProcessedPages();
//                        }
//
//
//                        controller.start(BasicCrawler.class, numberOfCrawlers);
//                        controller.waitUntilFinish();

                        System.out.println("--------------------------------");
                        System.out.println("Unique pages : " + URLIndexer.getInstance().pageinfoset.size());
                        System.out.println("Subdirectories : " + URLIndexer.getInstance().pathinfoset.size());
                        for (PathInfo mypath : URLIndexer.getInstance().pathinfoset) {
                            System.out.println("\t" + mypath.getPage().getUrl());
                        }
                        PageInfo pi = null;
                        for (PageInfo p : URLIndexer.getInstance().pageinfoset) {
                            if (pi == null) pi = p;
                            if (pi != null && p != null && pi.nbWord < p.nbWord) {
                                pi = p;
                            }
                        }
                        System.out.println("--------------------------------");
                        System.out.println("Unique pages : " + URLIndexer.getInstance().pageinfoset.size());
                        System.out.println("Subdirectories : " + URLIndexer.getInstance().pathinfoset.size());

                        System.out.println("Longest page/total words : " + pi.getUrl() + "/" + pi.nbWord);
                        System.out.println("--------------------------------");
                        URLIndexer.getInstance().generateIndex();
                        URLIndexer.getInstance().close();
                        endTime   = System.currentTimeMillis();
                        totalTime = endTime - startTime;
                        System.out.println("total time: " + totalTime/1000);                           
                        break;
                    case 3://Quary without simulatery
                        SearchFiles.main(new String[]{"-n"});
                        break;                        
                    case 4://Quary quary with simulatery
                        SearchFiles.main(new String[]{"-n"});                                
                        break;                              
                }
                
            }
        } catch (Exception e) {
            System.err.println("I/O exception: " + e.toString());
        }
    }   
}
