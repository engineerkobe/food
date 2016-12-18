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
		FW.close();
		return subs;
	}
 
}//class 

//網頁主目錄原始碼找各種許要的東西
class FindHtml {
	ReadTXT a;
	String buffer = " ";//放緩存	
	String name = "part";//分頁緩存名稱	
	String mkdir = "";//本目錄下的新鍵資料夾
	String txt = "";//
	int vicepage = 132;	
	FindHtml(String md , String tx) {
		mkdir = md;
		txt = tx;
	}
	//搜尋從原始碼找出主葉面全部食譜網址  
	//subject_ahref_and_write(建立資料夾、寫入TXT)
	void subject_ahref_and_write() throws IOException {
		System.out.println("下載所有主頁面");		
		System.out.println("從所有主頁面搜尋所有分頁連結(分頁連結保存在當前目路/" + mkdir + "/" + txt + ")");
		//建立資料夾	
		new CreatDIR("./" + mkdir);	
		//鍵立寫入副頁連結網址TXT
		new CreatTXT("./" + mkdir + "/" + txt );
		for(int i = 1; i < 10; i++) {

			//網頁暫存原始
			String TXTname = "./" + mkdir + "/" + "part" + i + ".txt";
			new CreatTXT(TXTname);

			URLResource MainHtml = new URLResource("http://www.dodocook.com/dish/236/" + i + "?order_type=new");
	
			for(String s : MainHtml.lines()) {
				//寫入網頁暫存檔
				WriteTXT word =	new WriteTXT(TXTname);
				word.write(s);
			}
			//讀入全部原始碼
			a = new ReadTXT(TXTname);
			buffer = a.read();

			//寫路新建的	
			WriteTXT word = new WriteTXT("./" + mkdir + "/" + txt );
	
			String subject = "subject";
			String href = "href";
			String allURL = "";	

			int subject_step = 0;//subject的位置
	 		int href_step = 0;//subject下的第一個href
		 	int semicolon_step = 0;//href後的第一個分號
			int after_semicolon_step = 0;//href後分號
			for(;subject_step != -1;) {

			 	subject_step = buffer.indexOf(subject, subject_step);
			
				if(subject_step == -1)//找不到就退出
					subject_step = -1;
				else{//從subject下找href
					href_step = buffer.indexOf(href,subject_step);
					//在從href下找(")
					semicolon_step = buffer.indexOf("\"",href_step);	
					//在從(")找下一個(")
					after_semicolon_step = buffer.indexOf("\"",semicolon_step + 1);
					//，在把往只剪下來
					allURL += "http://www.dodocook.com" + (buffer.substring(semicolon_step + 1, after_semicolon_step)) + "\r\n";
					//加上本字串長度才可以搜到下一個	
					subject_step += subject.length();
				}//else
			}//for
		//刪除佔存的網頁原始碼	
		File fil = new File(TXTname);
		fil.delete();

		System.out.print("主頁面第" + i +"頁" + "\r");
		System.out.print("");
		word.write(allURL);
		}//for
	System.out.println("主頁面分析分析分頁連節完成");
	}

	//下載全部副分葉源始碼
	void dowhnload_vice () throws IOException {
		System.out.println("下載全部分頁源始碼(原始碼在當前目路/" + mkdir + "底下)");	
		//讀分頁連結檔	
		a = new ReadTXT("./" + mkdir + "/" + txt);
		buffer = a.read();

		int after_httpstep = 0; //	
		int http_step = 0; //http出線的位置
		int part = 0;	
		while( http_step != -1) {
			String http = "http";
			String downloadURL = "";
			
			http_step = buffer.indexOf(http , http_step);	
			
			if(http_step == -1) 	
				http_step = -1;	
			else{	//避免讀到尾出現錯誤
				if((buffer.indexOf(http,http_step + 4) == -1)) 	
					downloadURL = buffer.substring(http_step);
				else{
					after_httpstep = buffer.indexOf(http,http_step + 4);
					downloadURL = buffer.substring(http_step,after_httpstep);
					}//else

					part ++;	

					URLResource MainHtml = new URLResource(downloadURL);

					new CreatTXT("./" + mkdir + "/" + name + part + ".txt");

					for(String s : MainHtml.lines()) {
						//寫入網頁暫存檔
						WriteTXT word =	new WriteTXT("./" + mkdir + "/"+ name + part + ".txt");
						word.write(s);
					}
				http_step += http.length();	
				System.out.print("第" + part + "頁" + "\r");	
				System.out.print("");
			}//else
		}
		System.out.print("完成");
	}

	//從副頁搜尋首圖網址
	void FindMainImage() throws IOException {
	
		System.out.println( "從分頁搜尋首圖連結(連結保存在image資料夾下的imagehref.txt)");
		String image = "\"image\"";

		
		int image_step = 0; //image 所在位置
		int semicolon_step = 0;//前分號
		int after_semicolon_step = 0;//後分號

		new CreatDIR("./image");
		new CreatTXT("./image/imagehref.txt");
		for(int i = 1; i <= vicepage; i++) {
			
			ReadTXT parthtml = new ReadTXT("./" + mkdir + "/" + name + i + ".txt" );
			buffer = parthtml.read();
			//json下有首圖
			
			//從"image"
			image_step = buffer.indexOf(image);
			//在image下找圖片連結的第一個"
			semicolon_step = buffer.indexOf("\"",image_step + image.length());	
			//在從(")找下一個(")
			after_semicolon_step = buffer.indexOf("\"",semicolon_step + 1);

			WriteTXT word = new WriteTXT("./image/imagehref.txt");
			word.write(buffer.substring(semicolon_step + 1, after_semicolon_step));
		}
	}

	//下載所有首圖
	void downloadMainImage() throws IOException {
		System.out.println("下載所有首圖(首圖保存在image資料夾底下)");
		URLConnectionDownloader downer = new URLConnectionDownloader();
		ReadTXT txt = new ReadTXT("./image/imagehref.txt");
		//System.out.println(txt.read());	
		buffer = txt.read();
		String http = "http";
		String jpg = "jpg";
		String URL = "";	
		int URL_step = 0;
		int jpg_step = 0;
		for(int i = 1;; i++) {
			URL_step = buffer.indexOf(http , URL_step);
			jpg_step = buffer.indexOf(jpg , jpg_step);

			if(jpg_step== -1)
				break;
				
			URL = buffer.substring(URL_step, jpg_step + 3 );
			
			//System.out.println(URL);	
			
			System.out.print("下載第" + i + "張" + "\r");
			System.out.print("");

			URL_step += http.length();
			jpg_step += jpg.length();	
			downer.download(URL, "./image/mainimage" + i + ".jpg" );

		}
		System.out.println("完成");

	}
	//分析所有步驟圖連結
	void findstepimagehref() throws IOException {
		System.out.println("分析所有步驟圖連結(檔案在/stepimage/href/)");
		
		new CreatDIR("./stepimage/");		
		new CreatDIR("./stepimage/href/");		
		ReadTXT word;
		WriteTXT hrefword; 
		String Sno = "\"Sno\"";
		String href = "href=\"";
		String jpg = "jpg";
		String stepURL = "";
		String src = "src=\"";
		int Sno_step = 0;
		int href_step = 0;
		int jpg_step = 0;	
		int src_step = 0;	
		for(int i = 1 ; i <= vicepage ; i++){
			System.out.print("分析步驟圖片網址中第" + i + "分頁" + "\r");
			new CreatTXT("./stepimage/href/parthref" + i + ".txt");
			hrefword = new WriteTXT("./stepimage/href/parthref" + i + ".txt");
			
			word = new ReadTXT("./href/part" + i + ".txt");
			buffer = word.read();
			for(;;){//"Sno"往下找的第一個src就是步驟圖網址
				Sno_step = buffer.indexOf(Sno , Sno_step);	
				src_step = buffer.indexOf(src,Sno_step);	
				href_step = buffer.indexOf(href, Sno_step);
				jpg_step = buffer.indexOf(jpg, src_step);
				if(Sno_step == -1)
					break;	
				stepURL += "http://media.dodocook.com";
				stepURL += buffer.substring(src_step + src.length(), jpg_step + jpg.length());
				stepURL += "\r\n";
				Sno_step += Sno.length();
			}//for
				hrefword.write(stepURL);
				stepURL = "";
		}//for
	

	}//method
	//下載所有副圖
	void downloadstepimage ()throws IOException {
		ReadTXT word;
			
		for(int i=1; i<= vicepage; i++){
			word = new ReadTXT("./stepimage/href/parthref" + i + ".txt");
			System.out.print(word.read());
		}
	}	
	//搜字串各種字串
	void substring() throws IOException {
		System.out.println("搜尋所有需要的字串(字串保存在當前目錄下的main.txt)");
		new CreatTXT("./main.txt");
		WriteTXT word = new WriteTXT("./main.txt");
		ReadTXT parthtml = new ReadTXT("./" + mkdir + "/" + name + 1 + ".txt" );
		buffer = parthtml.read();

	
		String h1 = "h1"; // 菜名
		//System.out.println(buffer.substring(buffer.indexOf(h1) + 3, buffer.lastIndexOf(h1)-2 ));
		buffer.substring(buffer.indexOf(h1) + 3, buffer.lastIndexOf(h1)-2 );
		
		//剪下食材清單
		String recip = "recipeIngredient";
		int recip_step = buffer.indexOf(recip);
		String material = buffer.substring(buffer.indexOf("[",recip_step) + 1, buffer.indexOf("]", recip_step));
		//食材	
		int semicolon_step = 0;
		int after_semicolon_step = 0;
		String a =  "";
		for(;;) {
			semicolon_step = material.indexOf("\"", semicolon_step);
			//System.out.println(semicolon_step);
			after_semicolon_step = material.indexOf("\"" , semicolon_step + 1);

			if(semicolon_step == -1)
				break;
			else{
				a += material.substring(semicolon_step+1, after_semicolon_step);
				a += ", ";
				semicolon_step = after_semicolon_step + 3;
			}
			
		}
				//System.out.print(a);
		//System.out.println(material);

	}
	
}//class


class GetHtml {
	public static void main(String args[]) throws IOException{	
	//	String 	
		//搜尋從原始碼找出主葉面全部食譜網址  
		//subject_ahref_and_write(建立資料夾、寫入TXT)
		
		FindHtml Html = new FindHtml("href", "mainhref.txt");
		//分析主頁連結
		//Html.subject_ahref_and_write();
		//下載所有分頁	
		//Html.dowhnload_vice();
		//找主要圖片網址
		//Html.FindMainImage();
		//下載所有首圖
		//Html.downloadMainImage();
		
		////找所有字串還   .....沒完成
		//Html.substring();
		//搜尋小步驟圖
		Html.findstepimagehref();
		//
		Html.downloadstepimage();
	
	}
}
