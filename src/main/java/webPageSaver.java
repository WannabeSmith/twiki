import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class webPageSaver {
    private static Scanner sc;

	public static void main(String args[]) throws Exception {
    	
        OutputStream out = new FileOutputStream("test.txt");
        
        sc = new Scanner(System.in);
        String searchTerm = sc.nextLine();
        
        
        URL url = new URL(makeUrl(searchTerm));
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();

        copy(is, out);
        getData("test.txt");
        is.close();
        out.close();
        sc.close();
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
    
    private static void getData(String file) throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader(file));
    	String line;
    	while ((line = br.readLine()) != null) {
    	   int indexnum = line.indexOf("extract");
    	   String extract = line.substring(indexnum+10, line.length()-5);
    	   System.out.println(extract);
    	   String extractNoMarkUp = removeMarkUp(extract);
    	   System.out.println(extractNoMarkUp);
    	}
    	br.close();
    }
    
    private static String removeMarkUp(String file)  {
    	file = file.replace("\\u", "");
    	file = file.replace("\\b", "");
    	file = file.replace("\\n", "");
    	file = file.replace("\\", "");
		return file;	
    }
    private static String makeUrl(String name) {
    	return "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="+name;
    }
    
    private static String getString(int numString, String name)
    {
    	int k =0;
    	int i;
    	for ( i = 0; k < numString; i++)
    	{
    		if (name.charAt(i) == '.' )
    		{
    			k++;
    		}
    	}
    	
    	name = name.substring(0,i);
		return null;
    	
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
