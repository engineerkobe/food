//讀網頁
import edu.duke.URLResource;
//寫入檔案
import java.io.FileWriter;
//建立文件(txt),建立資料夾
import java.io.File;
//讀入文件(txt)
import java.io.FileReader;
import java.io.BufferedReader;
//不知道
import java.io.IOException;



//建立資料夾
class CreatDIR {

	CreatDIR() throws IOException {
		File dir = new File("./image");
		dir.mkdir();
	}

	CreatDIR(String Path) throws IOException {
		File dir = new File(Path);
		dir.mkdir();
	}

}

//建立TXT
class CreatTXT {
	
	CreatTXT() throws IOException {
		File txt = new File("./a_herf.txt");
		//txt.delete();
		txt.createNewFile();
	}

	CreatTXT(String Path) throws IOException {
		File txt = new File(Path);
		txt.delete();
		txt.createNewFile();
	}

}

//寫入TXT
class WriteTXT {
	
	FileWriter FW;
	File file;
	String Path;
	//設定檔案路徑	
	WriteTXT(String Path) throws IOException {
		FW = new FileWriter(Path, true);
		file = new File(Path);	
		this.Path = Path;
	}
	
	//寫入檔案	
	void write(String Str) throws IOException {
		//"\r\n"換行
		FW.write(Str + "\r\n");
		FW.close();
	}

	//清除檔案
	/*
	void clear() throws IOException {
		file.delete();
		new CreatTXT(Path);
	}*/

}//class

//讀TXT
class ReadTXT {	

	FileReader FW;
	BufferedReader BR;

	//設定檔案路徑	
	ReadTXT(String Path) throws IOException {
		FW = new FileReader(Path);
		BR = new BufferedReader(FW);
	}	
	
	//回傳檔案裡所以文字	
	String read() throws IOException {
		String s;
		String subs = "";
		while( (s = BR.readLine()) != null){
			//System.out.println(s); 	
			subs += s;
			subs += "\r\n";
		}
		BR.close();
		return subs;
	}
 
}//class 

//網頁主目錄原始碼找各種許要的東西
class FindHtml {
	ReadTXT a;
	String buffer = " ";	
	String Path = "";
	FindHtml(String Path) throws IOException {
		a = new ReadTXT(Path);
		//讀入全部原始碼
		buffer = a.read();
		//鍵立存放連結.txt
	}

	//搜尋subject找出主葉面全部食譜網址
	void subject_ahref_and_write(String Path) throws IOException {
		//寫路檔案	
		WriteTXT word = new WriteTXT(Path);

		String subject = "subject";
		String href = "href";
		String allURL = "";	

		int subject_step = 0;
		int href_step = 0;
		int semicolon_step = 0;//前分號
		int after_semicolon_step = 0;//後分號
		//System.out.println(buffer);
		for(;subject_step != -1;) {

			subject_step = buffer.indexOf(subject, subject_step);
			
			if(subject_step == -1)//ˋ找不到就退出
				subject_step = -1;
			else{	//從subject下找href
				href_step = buffer.indexOf(href,subject_step);
				//在從href下找(")
				semicolon_step = buffer.indexOf("\"",href_step);	
				//在從(")找下一個(")，在把往只剪下來
				after_semicolon_step = buffer.indexOf("\"",semicolon_step + 1);

				
				allURL += "http://www.dodocook.com" + (buffer.substring(semicolon_step + 1, after_semicolon_step)) + "\r\n";
				subject_step += subject.length();
			}//else
		}
		word.write(allURL);
	}

}

class GetHtml {
	public static void main(String args[]) throws IOException{
	//建立資料夾	
	new CreatDIR("./image");
	//鍵立寫入網址TXT
	new CreatTXT("./image/mainhref.txt");

	for(int i = 1; i < 10; i++) {
		String TXTname = "./image/part" + new Integer(i) + ".txt";
		//鍵立網頁檔名1~9
		new CreatTXT(TXTname);

		String URL = 	"http://www.dodocook.com/dish/236/" + new Integer(i) + "?order_type=new";
		URLResource MainHtml = new URLResource(URL);
	
		for(String s : MainHtml.lines()) {
			//System.out.println(s);
			WriteTXT word =	new WriteTXT(TXTname);
			word.write(s);
		}

		FindHtml Html = new FindHtml( TXTname);
		Html.subject_ahref_and_write("./image/mainhref.txt");
		
		File file = new File(TXTname);
		file.delete();
	}	
	}//main
}//class
