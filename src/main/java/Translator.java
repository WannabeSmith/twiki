import com.detectlanguage.DetectLanguage;
import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;

import java.util.List;

public class Translator {

	private String apiKey = "4e025837fc49f65883621ecd20aef9b4";
	
	public Translator ()
	{
		DetectLanguage.apiKey = this.apiKey;
	}
	
	public String getLanguage (String text)
	{
		String lang;
		try 
		{
			lang = DetectLanguage.simpleDetect(text);
		}
		catch (APIError a) 
		{
			lang = a.toString();
		}
		
		return lang;
	}
}
