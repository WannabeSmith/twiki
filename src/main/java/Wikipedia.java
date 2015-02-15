import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Wikipedia {
	
	private static String searchTerm;
	
	public Wikipedia(String msg) {
		searchTerm = msg;
	}
	public static String getSummary() throws IOException{
		
		URL url = new URL(makeUrl(searchTerm));
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        
        String data = convertStreamToString(is);
        data = parseData(data);
        is.close();
        return data;
	}
	
	private static String convertStreamToString(java.io.InputStream inputstream) {
	   Scanner s = new java.util.Scanner(inputstream).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
    
    private static String parseData(String file) throws IOException {
    	   int indexnum = file.indexOf("extract");
    	   String extract = file.substring(indexnum+10, file.length()-5);
    	   String extractNoMarkUp = removeMarkUp(extract);
    	   return extractNoMarkUp;
    }
    
    private static String removeMarkUp(String file)  {
    	file = file.replace("\\u", "");
    	file = file.replace("\\b", "");
    	file = file.replace("\\n", "");
    	file = file.replace("\\", "");
		return file;	
    }
    
    private static String getString(int numString, String name)
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
    
    
    private static String makeUrl(String name) {
    	return "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="+name;
    }
}


/*
 * public class webPageSaver {
    public static void main(String args[]) throws Exception {
    	
        OutputStream out = new FileOutputStream("test.html");

        URL url = new URL("http://en.wikipedia.org/wiki/1330s_in_England");
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();

        copy(is, out);
        is.close();
        out.close();
    }

    private static void copy(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[4096];
        while (true) {
            int numBytes = from.read(buffer);
            if (numBytes == -1) {
                break;
            }
            to.write(buffer, 0, numBytes);
        }
    }
}
 */
