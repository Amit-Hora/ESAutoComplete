package hello;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
    @RequestMapping("/search")
	 
    public void index(@RequestParam(value="data",required=true) String data,HttpServletResponse servletResponse) {
	 Settings settings = ImmutableSettings.settingsBuilder()
			    .put("cluster.name", "XXX").build();
	 TransportClient client = new TransportClient(settings);
	 try {
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("XXX"), Integer.parseInt("9300")));
		QueryBuilder qb = QueryBuilders.matchQuery("name.raw", data);
		SearchResponse response = client.prepareSearch("XXX") //
			    .setQuery(qb).setFrom(0).setSize(10) // Query
			    .setFetchSource(new String[]{"name"}, null)
			    .execute().actionGet();
		
		List<String> nameLst=new ArrayList<String>();
		for (SearchHit hit : response.getHits()){
			   Map<String,Object> map = hit.getSource();
			   nameLst.add(map.get("name").toString());
			}
		client.close();
		String json = new Gson().toJson(nameLst);
		servletResponse.setContentType("application/json");
		servletResponse.setCharacterEncoding("UTF-8");
		try {
			servletResponse.getWriter().write(json);
			System.out.println(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	return nameLst;
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
//		return null;
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
//		return null;
	}	
        
    }
    @RequestMapping("/getdata")
	 
    public void getdata(@RequestParam(value="data",required=true) String data,HttpServletResponse servletResponse) {
	 Settings settings = ImmutableSettings.settingsBuilder()
			    .put("cluster.name", "XXX").build();
	 TransportClient client = new TransportClient(settings);
	 try {
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("XXX"), Integer.parseInt("9300")));
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("XXX1"), Integer.parseInt("9300")));
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("XXX2"), Integer.parseInt("9300")));
		QueryBuilder qb = QueryBuilders.matchQuery("name.raw",data);
		SearchResponse response = client.prepareSearch("XXX") //
			    .setQuery(qb) // Query
			    //.setFetchSource(new String[]{"name"}, null)
			    .execute().actionGet();
		List<Map<String,Object>> nameLst=new ArrayList<Map<String,Object>>();
		for (SearchHit hit : response.getHits()){
			   Map<String,Object> map = hit.getSource();
			   nameLst.add(map);
			}
		
		String json = new Gson().toJson(nameLst);
		servletResponse.setContentType("application/json");
		servletResponse.setCharacterEncoding("UTF-8");
		try {
			servletResponse.getWriter().write(json);
			System.out.println(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	return nameLst;
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
//		return null;
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
//		return null;
	}	
        
    }

}
