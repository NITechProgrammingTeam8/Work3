import java.io.*;
import java.net.*;
import java.util.*;


public class DBpedia {
	
	 /**
     * 与えられたURLからHTML等のコンテンツを取得し，返す．
     * @param url 取得するコンテンツのURL
     * @param enc コンテンツの文字コード（UTF-8やEUC-JP, Shift_JISなど）
     * @return コンテンツ
     */
    public static String getWebContent(String url, String enc) {
        StringBuffer sb = new StringBuffer();        
        try {          
            URLConnection conn = new URL(url).openConnection();           
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), enc));           
            for (String line = in.readLine(); line != null; line = in.readLine()) {               
                sb.append(line);               
                sb.append("\n");          
            }       
        } catch (IOException e) {          
            e.printStackTrace();      
        }
        return sb.toString();   
    }
    
    
    
    
    
	
	
	public static void main(String args[]) {
		Scanner stdIn = new Scanner(System.in);
        String dbpediaBaseUrl = "http://ja.dbpedia.org/sparql?default-graph-uri=http%3A%2F%2Fja.dbpedia.org&timeout=0&debug=on";
        String format = "text/csv";  
        // formatには，"text/csv" の他にも以下のような形式を指定可能
        //  "text/tsv" 
        //  "application/rdf+xml"
        //  "application/sparql-results+json"
        //  "text/html"
        
        
        int p = 0;
        String zokusei = ""; //？属性
        String word = "";  //？対象の単語
        int n =10; //？出力個数
        String sparql;
        //String text = "フランスの国歌は?";
        //String text = "日本語が公用語は？";
        
        //System.out.println(retext);
        
        
        System.out.print("「〇〇の〇〇は？」という形で質問を入力してください。：");
        String text = stdIn.next();
        
        String retext = text.substring(0,text.length()-1);
        
        DepParser parser = new CaboCha();
		List<Sentence> sentencesz = parser.parseText(text);
		List<Sentence> sentencesw = parser.parseText(retext);
		//System.out.println("係り受け解析結果:" + sentences);
		
		for (Sentence sentence : sentencesz) { // 文を1つずつ処理するループ
            // 主辞（最後の文節）を取得
            Chunk headChunk = sentence.getHeadChunk(); 
            // 主辞に係る文節のリストを返す
            List<Chunk> dependents = headChunk.getDependents();
            //System.out.println(headChunk);
            //System.out.println(dependents);
            
            for (Chunk dependent : dependents) {
                // 文節dependentの主辞（最後の形態素）を取得
                Morpheme headMorph = dependent.getHeadMorpheme();
                // 助詞かどうか判定
                if (headMorph.getPos().equals("助詞")) {
                  // 文節dependent内の助詞以外の形態素をつなげる
                  String caseStr = "";
                  for (int i = 0; i < dependent.size()-1; i++) {
                    caseStr += dependent.get(i).getSurface();
                  }
         	 System.out.println(headMorph.getSurface()+"格: "+caseStr);
         	 zokusei = caseStr;
                }
            }
         }
		
		for (Sentence sentence : sentencesw) { // 文を1つずつ処理するループ
            // 主辞（最後の文節）を取得
            Chunk headChunk = sentence.getHeadChunk(); 
            // 主辞に係る文節のリストを返す
            List<Chunk> dependents = headChunk.getDependents();
            //System.out.println(headChunk);
            //System.out.println(dependents);
            
            for (Chunk dependent : dependents) {
                // 文節dependentの主辞（最後の形態素）を取得
                Morpheme headMorph = dependent.getHeadMorpheme();
                // 助詞かどうか判定
                if (headMorph.getPos().equals("助詞")) {
                  // 文節dependent内の助詞以外の形態素をつなげる
                  String caseStr = "";
                  for (int i = 0; i < dependent.size()-1; i++) {
                    caseStr += dependent.get(i).getSurface();
                  }
         	 System.out.println(headMorph.getSurface()+"格: "+caseStr);
         	 word = caseStr;
                }
            }
         }
		
		
        
        //ああ質問パターンに応じて分岐
        if(p == 0) {
        	//？パターンA
            sparql = 
            	"PREFIX dbp: <http://ja.dbpedia.org/resource/>"+
                "PREFIX dbpprop-ja: <http://ja.dbpedia.org/property/>"+
                "SELECT ?x WHERE { "+
                " dbp:" + word + " dbpprop-ja:" + zokusei + " ?x. "+
                "} LIMIT " + n;
        }
        else {
        	//？パターンB
        	sparql = 
                	"PREFIX dbp: <http://ja.dbpedia.org/resource/>"+
                    "PREFIX dbpprop-ja: <http://ja.dbpedia.org/property/>"+
                    "SELECT ?x WHERE { "+
                    " ?x dbpprop-ja:" + zokusei + " dbp:" + word + "."+
                    "} LIMIT " + n;
        }
        
        String url = dbpediaBaseUrl 
            + "&format=" + URLEncoder.encode(format)
            + "&query=" + URLEncoder.encode(sparql);

        String result = getWebContent(url, "UTF-8");
        //System.out.println(result);
        
        //？出力結果の体裁処理
        
        //System.out.println(result.length());
        
        if(result.length() == 4) {
        	System.out.println("見つかりません。");
        }
        else {
        	result = result.replace("http://ja.dbpedia.org/resource/", " ");
            result = result.substring(4);
            System.out.println(result);
        }
        
        
    }
}
