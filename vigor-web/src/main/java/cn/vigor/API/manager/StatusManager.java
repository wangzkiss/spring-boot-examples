package cn.vigor.API.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import cn.vigor.API.model.metrics.manager.CheckState;
import cn.vigor.API.util.HttpTools;
import cn.vigor.API.util.JSONTools;
import cn.vigor.API.util.ManagerHandler;

public class StatusManager {

	/**
	 * 根据id返回checkstate类
	 * 
	 * @param host
	 * @param port
	 * @param clustername
	 * @param id
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static CheckState recheckStateById(String host, String port,
			String clustername, String id) throws ClientProtocolException,
			UnsupportedEncodingException, URISyntaxException, IOException {
		String url = ManagerHandler.returnReqiestCheckUrl(host, port,
				clustername, id);
		String content = HttpTools.getInstance().getContent(url);
		CheckState checkState = parseCheckState(content);
		return checkState;
	}

	/**
	 * 根据url返回checkstate类
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static CheckState recheckStateByUrl(String url)
			throws ClientProtocolException, UnsupportedEncodingException,
			URISyntaxException, IOException {
		String content = HttpTools.getInstance().getContent(url);
		CheckState checkState = parseCheckState(content);
		return checkState;
	}

	/**
	 * 解析checkstate类
	 * @param content
	 * @return
	 */
	private static CheckState parseCheckState(String content) {
		Map m = JSONTools.fromJson(content, Map.class);
		String[] statearr = { "Requests", "request_status" };
		String state = JSONTools.search(m, statearr, 0, statearr.length);
		String[] progress_percentarr = { "Requests", "progress_percent" };
		String progress_percent = JSONTools.search(m, progress_percentarr, 0,
				progress_percentarr.length);
		String[] idarr = { "Requests", "id" };
		String id = JSONTools.search(m, idarr, 0, idarr.length);
		String[] hrefarr = { "href" };
		String href = JSONTools.search(m, hrefarr, 0, hrefarr.length);
		CheckState checkState = new CheckState();
		checkState.setId(id);
		checkState.setProgress_percent(progress_percent);
		checkState.setState(state);
		checkState.setHref(href);
		return checkState;
	}
}
