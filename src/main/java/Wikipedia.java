import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Wikipedia {
	
	private String searchTerm;
	
	public Wikipedia(String msg) {
		searchTerm = msg;
	}
	public String getSummary() throws IOException{
		
		URL url = new URL(makeUrl(searchTerm));
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        
        String data = convertStreamToString(is);
        data = parseData(data);
        is.close();
        return data;
	}
	
	private String convertStreamToString(java.io.InputStream inputstream) {
	   Scanner s = new java.util.Scanner(inputstream).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
    
    private String parseData(String file) throws IOException {
    	   int indexnum = file.indexOf("extract");
    	   String extract = file.substring(indexnum+10, file.length()-5);
    	   String extractNoMarkUp = removeMarkUp(extract);
    	   return extractNoMarkUp;
    }
    
    private String removeMarkUp(String file)  {
    	file = file.replace("\\u", "");
    	file = file.replace("\\b", "");
    	file = file.replace("\\n", "");
    	file = file.replace("\\", "");
		return file;	
    }
    
    private String getString(int numString, String name)
    {
    	int k =0;
    	int index = 0;
    	for (int i = 0; k < numString; i++)
    	{
    		if (name.charAt(i) == '.' )
    		{
    			k++;
    		}
    		index++;
    	}
    	
    	name = name.substring(0,index);
		return name;
    	
    }
    
    
    private String makeUrl(String name) {
    	return "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="+name;
    }
}
