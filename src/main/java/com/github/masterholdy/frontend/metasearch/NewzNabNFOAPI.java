package com.github.masterholdy.frontend.metasearch;

import com.github.masterholdy.backend.metasearch.Release;
import com.github.masterholdy.backend.metasearch.SearchJob;
import com.github.masterholdy.backend.metasearch.SearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.predbdotorg.PreDbDotOrgSearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.predotcburnsdotcodotuk.PreDotCBurnsDotCoDotUk;
import com.github.masterholdy.backend.metasearch.nfo.srrDBDotCom.srrDBDotComSearchWrapper;
import com.github.masterholdy.backend.metasearch.nfo.xreldotto.xrelDotToSearchWrapper;
import com.github.masterholdy.backend.metasearch.nzb.scenenzbdottop.scenenzbDotTopSearchWrapper;
import org.nzbhydra.mapping.newznab.xml.*;
import org.nzbhydra.mapping.newznab.xml.caps.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController()
public class NewzNabNFOAPI {
    static SearchWrapper[] nfoSearchWrappers = new SearchWrapper[]{
            new PreDbDotOrgSearchWrapper(),
            new PreDotCBurnsDotCoDotUk(),
            new xrelDotToSearchWrapper(),
            new srrDBDotComSearchWrapper(),
            new scenenzbDotTopSearchWrapper()
            //new PreDotCorruptNetDotOrg() <- //TODO defect
            //new PreDbDotMeSearchWrapper() //TODO<-- cloudflare
    };

    @GetMapping(value = "/api", produces = { MediaType.APPLICATION_RSS_XML_VALUE })
    public String api(
                      HttpServletRequest request,
                      @RequestParam(value = "t", defaultValue = "") String t,
                      @RequestParam(value = "o", defaultValue = "XML") String o,
                      @RequestParam(value = "apikey", defaultValue = "") String apikey,
                      @RequestParam(value = "q", defaultValue = "") String query,
                      @RequestParam(value = "limit", defaultValue = "") String limit,
                      @RequestParam(value = "cat", defaultValue = "") String cat,
                      @RequestParam(value = "attrs", defaultValue = "") String attrs,
                      @RequestParam(value = "extended", defaultValue = "") String extended,
                      @RequestParam(value = "del", defaultValue = "") String del,
                      @RequestParam(value = "maxage", defaultValue = "") String maxage,
                      @RequestParam(value = "offset", defaultValue = "") String offset
    ){
        if(t.isBlank()) return new NewznabXmlError("200","Missing parameter").toXmlString();
        if(t.equals("caps")) return caps().toXmlString();
        else if(t.equals("register")) return new NewznabXmlError("203","Function not available").toXmlString();
        else if(t.equals("search")) return search(query, nfoSearchWrappers).toXmlString();

        return new NewznabXmlError("200","Missing parameter").toXmlString();
    }

    public static CapsXmlRoot caps(){
        CapsXmlRoot root = new CapsXmlRoot();

        CapsXmlServer server = new CapsXmlServer();

        server.setVersion("1.0");
        server.setTitle("MetaSearch");
        server.setStrapline("Metasearch");
        server.setEmail("google@google.com");
        server.setUrl("LOCALHOST");
        //Server.setImage();

        root.setServer(server);

        CapsXmlLimits limits = new CapsXmlLimits(202020, 202020);
        root.setLimits(limits);

        CapsXmlRetention retention = new CapsXmlRetention(990000);
        root.setRetention(retention);

        //    <registration available="no" open="no" /> <- ???

        CapsXmlSearching searching = new CapsXmlSearching();
        searching.setSearch(new CapsXmlSearch("true", "q"));
        searching.setMovieSearch(new CapsXmlSearch("true", "q"));
        searching.setTvSearch(new CapsXmlSearch("true", "q"));
        searching.setBookSearch(new CapsXmlSearch("true", "q"));
        searching.setAudioSearch(new CapsXmlSearch("true", "q"));
        root.setSearching(searching);

        //CapsXmlCategories categories = new CapsXmlCategories();
        /* bspw.
        <categories>
        <category id="2000" name="Movies">
          <subcat id="2000" name="Foreign"/>
          <subcat id="2010" name="Foreign"/>
        </category>
        <category id="5000" name="TV"    description="TV">
          <subcat id="5020	" name="Foreign" description="Foreign"/>
        </category>
    </categories>  </caps>*/

        return root;
    }

    public static List<Release> query(String query, SearchWrapper[] nfoSearchWrappers){
        List<Release> result = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        List<SearchJob> searchjobs = new ArrayList<>();

        for(SearchWrapper sw: nfoSearchWrappers) {
            SearchJob searchJob = new SearchJob(sw, query);
            Thread t = new Thread(searchJob);
            t.start();
            threads.add(t);
            searchjobs.add(searchJob);
        }

        try{
            for (Thread thread : threads) {
                thread.join();
            }
        }catch(Exception e){}

        for (SearchJob sj : searchjobs) {
            List<Release> rls = sj.getResult().orElse(null);
            if(rls!=null) result.addAll(rls);
        }

        return result;
    }

    public static NewznabXmlRoot search(String query, SearchWrapper[] nfoSearchWrappers){
        NewznabXmlRoot rssRoot = new NewznabXmlRoot();
        rssRoot.setVersion("2.0");

        NewznabXmlChannel channel = new NewznabXmlChannel();
        channel.setTitle("Metasearch");
        channel.setDescription("MetasearchAPI results");
        channel.setLanguage("en-gb");
        channel.setWebMaster("google@google.com");
        channel.setLink("http://www.127.0.0.1");
        channel.setNewznabResponse(new NewznabXmlResponse(-1, nfoSearchWrappers.length));

        List<Release> rls = query(query, nfoSearchWrappers);
        List<NewznabXmlItem> items = new ArrayList<>();

        rls.forEach(items::add);

        channel.setItems(items);

        rssRoot.setRssChannel(channel);

        return rssRoot;
    }
}