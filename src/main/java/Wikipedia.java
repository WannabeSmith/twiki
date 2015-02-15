import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class Wikipedia {

	private String searchTerm = "";
	private String language = "en";

	public Wikipedia() {}

	public String search(String query) throws IOException {
		String encodedUrl = "";

		try {
			encodedUrl = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException ignored) {
			// Can be safely ignored because UTF-8 is always supported
		}
		try {
			URL url = new URL(makeUrl(encodedUrl));
			searchTerm = encodedUrl;
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			String data = convertStreamToString(is);
			is.close();
			return data;
		}
		catch (Exception e) {
			return "Error calling Wikipedia API:\n\n" + e;
		}

	}

	private static String phonetic(String name) {
		StringBuffer strbuf = null;
		String output;

		strbuf = new StringBuffer(name);
		int counter = 0;

		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '(' && name.charAt(i + 1) == '/') {
				counter++;
			}
		}

		for (int m = 0; m < counter; m++) {
			strbuf = new StringBuffer(strbuf);
			if (strbuf.indexOf("),") + 1 > strbuf.length()) {
				strbuf.replace(strbuf.indexOf("(/"), strbuf.indexOf("),"), "");
			} else {
				strbuf.replace(strbuf.indexOf("(/"), strbuf.indexOf("),") + 3,
						"");
			}
		}
		for (int m = 0; m < strbuf.length(); m++) {

			if (name.charAt(m) == '\\' && name.charAt(m + 1) == 'u') {
				strbuf.replace(m, m + 4, "");
			}
		}

		output = strbuf.toString();
		return output;
	}

	public String getSummary(String query) throws IOException {
		return getSummary(query, 0);
	}

	private boolean checkMultiples(String data) throws IOException {

		if (data.contains(searchTerm + " may refer to:")) {
			return true;
		}
		return false;
	}

	public String getSummary(String query, int sentences) throws IOException {
		searchTerm=query;
		String data = search(searchTerm);
		//Translator trans = new Translator();
		language = "en";
		data = parseData(data);
		if (!checkMultiples(data)) {

			if (sentences > 0) {
				data = getString(sentences, data);
			}

			return data;
		}
		return "More than one option";
	}

	private String convertStreamToString(java.io.InputStream inputstream) {
		Scanner s = new java.util.Scanner(inputstream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private String parseData(String file) throws IOException {
		int indexnum = file.indexOf("extract");
		String extract = file.substring(indexnum + 10, file.length() - 5);
		String extractNoMarkUp = removeMarkUp(extract);
		return extractNoMarkUp;
	}

	private String removeMarkUp(String file) {
		file = file.replace("\\u", "");
		file = file.replace("\\b", "");
		file = file.replace("\\n", "");
		file = file.replace("\\", "");
		file = phonetic(file);
		return file;
	}

	private String getString(int numString, String name) {
		int k = 0;
		int index = 0;
		for (int i = 0; k < numString; i++) {
			if (name.charAt(i) == '.') {
				k++;
			}
			index++;
		}

		name = name.substring(0, index);
		return name;

	}

	private String makeUrl(String name) {
		language = "en";
		return "https://"+language+".wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&continue=&titles="
				+ name;
	}
}