import java.util.ArrayList;

class AccessDataTest {
    public static void main(String args[]){

    	//SemanticNet sn = new SemanticNet();
    	AccessData ad = new AccessData(new SemanticNet());

    	// ファイルの読み込みと、読み込んだデータの取得
    	// 取得方法によっては変更あり(startに引数では難しい)
    	ad.start("members/"+args[0]+".txt");
    	ArrayList<Link> links = ad.getLinks();
    	printLinks(links);
    	ArrayList<Node> nodes = ad.getNodes();
    	printNodes(nodes);

    	// 検索文の取得(手動入力)と検索結果のみの出力
    	ArrayList query = ad.searchData("?y reads GOSICK,?y is-a girl,?y watches Demon-Slayer,?y hobby reading-book");
    	query(query);

    }

    // 関係性表示用メソッド(確認のため)
    public static void printLinks(ArrayList<Link> links) {
    	System.out.println("*** Links ***");
    	for(int i = 0 ; i < links.size() ; i++){
    		System.out.println(((Link)links.get(i)).toString());
    	}
    }

    // ノード表示用メソッド(確認のため)
    public static void printNodes(ArrayList<Node> nodes){
    	System.out.println("*** Nodes ***");
    	for(int i = 0 ; i < nodes.size() ; i++){
    	    System.out.println(((Node)nodes.get(i)).toString());
    	}
    }

    // 検索結果表示用メソッド(確認のため)
    public static void query(ArrayList<Link> theQueries){
    	System.out.println("*** Query ***");
    	/*
    	for(int i = 0 ; i < theQueries.size() ; i++){
    		System.out.println(((Link)theQueries.get(i)).toString());
    	}
    	*/
    	// 解が無いとき[]
    	// 解があるとき[{?変数 = 解,・・・}]
    	System.out.println(theQueries.toString());

    	// 表示方法検討用
    	/*
    	String test = theQueries.toString();
    	System.out.println(test);
    	*/

    }

}