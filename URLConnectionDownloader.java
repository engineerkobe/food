import java.io.File; 
import java.io.FileOutputStream; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.net.URL; 
import java.net.URLConnection; 
import java.net.HttpURLConnection;
import java.io.IOException; 
/** 
* 使用URLConnection下載文件或圖片並保存到本地。 
*/ 
public class URLConnectionDownloader { 
	/*
	public static void main(String[] args) throws Exception { 
		String url = "http://www.dodocook.com/user_upload/recipe/8/08/87/recipe_0080887_600_fit_1470115935.jpg"; 
		String fileDir = "./"; 
		String fileName = "abc.jpg"; 

		makeDir(fileDir); 
		download(url, fileDir+fileName); 

		System.out.println("下載圖片完畢！"); 
	} 
	*/

/** 
* 下載文件到本地 
* 
* @param urlString 
*            被下載的文件地址 
* @param filename 
*            本地文件名 
* @throws Exception 
*             各種異常 
*/ 
	public  void 	download(String urlString, String filename) throws IOException { 
		// 構造URL 
		URL url = new URL(urlString); 
		// 打開連接 
		URLConnection con = url.openConnection(); 


		// 設置Java服務器代理連接，要不然報錯403 
		// 瀏覽器可以訪問此url圖片並顯示，但用Java程序就不行報錯Server returned HTTP response code:403 for URL 
		// 具體原因：服務器的安全設置不接受Java程序作?客戶端訪問(被屏蔽)，解決辦法是設置客戶端的User Agent 
		con.setRequestProperty("User-Agent", "Mozilla/4.0"); 

		InputStream is =  con.getInputStream(); 

		// 1K的數據緩沖 
		byte[] bs = new byte[1024]; 
		// 讀取到的數據長度 
		int len; 	
		// 輸出的文件流 
		OutputStream os = new FileOutputStream(filename); 
		// 開始讀取 
		while ((len = is.read(bs)) != -1) { 
			os.write(bs, 0, len); 
		} 
		// 完畢，關閉所有鏈接 
		os.close(); 
		is.close(); 
	} //download

	private static void makeDir(String fileFolder) { 
		File file = new File(fileFolder); 
		if (!file.exists() && !file.isDirectory()) 
			file.mkdir(); 
	} 
} 
