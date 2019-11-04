	import java.util.ArrayList;
import java.util.List;

class AccessDataTest {
    public static void main(String args[]){
    	AccessData ad = new AccessData(new SemanticNet());
    	/*
    	// ファイルの読み込みと、読み込んだデータの取得
    	// 取得方法によっては変更あり(startに引数では難しい)
    	ad.start("members/"+args[0]+".txt");
    	*/
    	List<String> list = new ArrayList<>();
    	list.add("baseball = is-a => sports");
    	//list.add("student = donot => study");
    	//list.add("NIT-student = is-a => student");
    	list.add("Taro = is-a => NIT-student");
    	list.add("Taro = speciality => AI");
    	list.add("Ferrari = is-a => car");
    	list.add("car = has-a => engine");
    	for (int i = 0; i < list.size(); i++) {
    		if (!(ad.addData(list.get(i)))) {
    			System.out.println("追加失敗");
    		} else {
    			System.out.println("追加成功");
    		}
    	}
    	/*
    	ArrayList<Link> links = ad.getLinks();
    	printLinks(links);
    	ArrayList<Node> nodes = ad.getNodes();
    	printNodes(nodes);
    	*/
    	List<String> lists = new ArrayList<>();
    	lists.add("Taro = hobby => baseball");
    	lists.add("Taro = own => Ferrari");
    	lists.add("NIT-student = is-a => student");
    	lists.add("Taro = is-a => NIT-student");
    	lists.add("student = donot => study");
    	lists.add("Ferrari = has-a => engine");
    	for (int i = 0; i < lists.size(); i++) {
    		if (!(ad.addData(lists.get(i)))) {
    			System.out.println("追加失敗");
    		} else {
    			System.out.println("追加成功");
    		}
    	}
    	ArrayList<Link> links = ad.getLinks();
    	printLinks(links);
    	ArrayList<Node> nodes = ad.getNodes();
    	printNodes(nodes);
    	/*
    	// 検索文の取得(手動入力)と検索結果のみの出力
    	//ArrayList query = ad.searchData("?y reads GOSICK,?y is-a girl,?y watches Demon-Slayer,?y hobby reading-book");
    	ArrayList query = ad.searchData("?y own Ferrari,?y is-a student,?y hobby baseball,?x is-a sports,?y hobby ?x");
    	query(query);
    	ArrayList query1 = ad.searchData("?z donot study,?z is-a student");
    	query(query1);
    	*/

    	/*(削除)
    	if (!(ad.deleteData("Taro = speciality => AI"))) {
			System.out.println("削除失敗");
		} else {
			System.out.println("削除成功");
		}
    	links = ad.getLinks();
    	printLinks(links);
    	nodes = ad.getNodes();
    	printNodes(nodes);
    	ArrayList query2 = ad.searchData("?a donot study,?a is-a student");
    	query(query2);
    	ArrayList query3 = ad.searchData("?b speciality AI");
    	query(query3);
    	*/

    	ArrayList query1 = ad.searchData("?z donot study,?z is-a student");
    	query(query1);
    	System.out.println("削除開始");
    	boolean delete = ad.deleteData2("Taro = is-a => student");
    	System.out.println("削除：" + delete);
    	//ArrayList<Link> links = ad.getLinks();
    	links = ad.getLinks();
    	printLinks(links);
    	//ArrayList<Node> nodes = ad.getNodes();
    	nodes = ad.getNodes();
    	printNodes(nodes);
    	ArrayList query2 = ad.searchData("?z donot study,?z is-a student");
    	query(query2);
    	/*
    	boolean delete2 = ad.deleteData2("Noriko = is-a => student");
    	System.out.println("削除：" + delete2);
    	boolean delete3 = ad.deleteData2("student = donot => study");
    	links = ad.getLinks();
    	printLinks(links);
    	//ArrayList<Node> nodes = ad.getNodes();
    	nodes = ad.getNodes();
    	printNodes(nodes);
    	System.out.println("削除：" + delete3);
    	ArrayList query3 = ad.searchData("?a donot study");
    	query(query3);
    	*/
    	boolean add = ad.addData("Taro = is-a => student");
    	System.out.println("追加：" + add);
    	links = ad.getLinks();
    	printLinks(links);
    	//ArrayList<Node> nodes = ad.getNodes();
    	nodes = ad.getNodes();
    	printNodes(nodes);
    	ArrayList query4 = ad.searchData("?z donot study,?z is-a student");
    	query(query4);
    	boolean add2 = ad.addData("Taro = donot => study");
    	System.out.println("追加：" + add2);

    	/*
    	// 自然言語での検索
    	ArrayList naturalquery = ad.searchNaturalData("What is Yuuki's hobby ?");
    	query(naturalquery);
    	// Yes!の出力は出来てない
    	naturalquery = ad.searchNaturalData("Is GOSICK a book ?");
    	query(naturalquery);
    	*/
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
    	System.out.println(theQueries.toString());
    	// 表示方法検討
    	/*
    	String test = theQueries.toString();
    	System.out.println(test);
    	*/
    }
}