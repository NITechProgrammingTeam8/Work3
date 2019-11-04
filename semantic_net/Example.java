import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/***
 * Semantic Net の使用例
 */
public class Example {
    public static void main(String args[]){
	SemanticNet sn = new SemanticNet();

	// 野球はスポーツである．
	sn.addLink(new Link("is-a","baseball","sports",sn));

	// 太郎は名古屋工業大学の学生である．
	sn.addLink(new Link("is-a","Taro","NIT-student",sn));

	// 太郎の専門は人工知能である．
	sn.addLink(new Link("speciality","Taro","AI",sn));

	// フェラーリは車である．
	sn.addLink(new Link("is-a","Ferrari","car",sn));

	// 車はエンジンを持つ．
	sn.addLink(new Link("has-a","car","engine",sn));

	// 太郎の趣味は野球である．
	sn.addLink(new Link("hobby","Taro","baseball",sn));

	// 太郎はフェラーリを所有する．
	sn.addLink(new Link("own","Taro","Ferrari",sn));

	// 名古屋工業大学の学生は，学生である．
	sn.addLink(new Link("is-a","NIT-student","student",sn));

	// 学生は勉強しない．
	sn.addLink(new Link("donot","student","study",sn));

	sn.printLinks();
	sn.printNodes();


	Scanner stdIn0 = new Scanner(System.in);
	int question;
	do {
	/***
	 * 課題2で扱ったような変数を含むパターン (クエリー)による質問応答システム
	 * "?x is-a sports"と"?y hobby ?x"をとらえる
	 * → 質問は3つのトークンに分けられる
	 */
	Scanner stdIn1 = new Scanner(System.in);	//文字列読み込み
	Scanner stdIn2 = new Scanner(System.in);	//数値読み込み
	ArrayList<ArrayList<String>> queryList = new ArrayList<ArrayList<String>>(); //質問(query)を入れる
	StringTokenizer st;		//トークンごとに分解
//	int retry;
//	do {
		ArrayList<String> tokenList = new ArrayList<>();
		System.out.println("質問を入力してください");
		String s = stdIn1.nextLine(); 	//質問文がここに入り,
		//System.out.println("s = " + s);
		st = new StringTokenizer(s);	//トークンごとに分解し,
		//System.out.println("st.countTokens() =  " + st.countTokens());

		String firstToken = st.nextToken();
		String secondToken = st.nextToken();
		if(firstToken.equals("What")) {
			if(secondToken.equals("does")) {
				String thirdToken = st.nextToken();
				if(thirdToken.equals("the")) {
					thirdToken = st.nextToken();
				}
				tokenList.add(thirdToken);

				String forthToken = st.nextToken();
				if(forthToken.equals("have")) {
					tokenList.add("has-a");
				}
				else {
					tokenList.add(forthToken);
				}
				tokenList.add("?x");
			}

			else if(secondToken.equals("is")) {
				String thirdToken = st.nextToken().replace("'s", "");
				tokenList.add(thirdToken);
				tokenList.add(st.nextToken());
				tokenList.add("?x");
			}
		}

		else if(firstToken.equals("Is")) {
			if(secondToken.contains("'s")) {
				tokenList.add(secondToken.replace("'s", ""));
				tokenList.add(st.nextToken());
			}
			else {
				tokenList.add(secondToken);
				tokenList.add(firstToken.replace("Is", "is-a"));
			}
			st.nextToken();
			tokenList.add(st.nextToken());
		}

		else if(firstToken.equals("Does")) {
			if(secondToken.equals("the") || secondToken.equals("a")) {
				tokenList.add(st.nextToken());
			}
			else {
				tokenList.add(secondToken);
			}

			String thirdToken = st.nextToken();
			if(thirdToken.equals("have")) {
				tokenList.add("has-a");
			}
			else {
				tokenList.add(thirdToken);
			}

			String forthToken = st.nextToken();
			if(forthToken.equals("a")) {
				forthToken = st.nextToken();
			}
			tokenList.add(forthToken);
		}

		queryList.add(tokenList);
//		System.out.println("もう１つ? 1...Yes/ 0...No");
//		retry = stdIn2.nextInt();
//	}while(retry == 1);

	ArrayList<Link> query = new ArrayList<Link>();
	for(int i=0; i<queryList.size(); i++) {
		query.add(new Link(queryList.get(i).get(1), queryList.get(i).get(0), queryList.get(i).get(2)));
	}

	/* 参考にとっておこう...
	ArrayList<Link> query = new ArrayList<Link>();
	//query.add(new Link("own","?y","Ferrari"));
	//query.add(new Link("is-a","?y","student"));
	//query.add(new Link("hobby","?y","base	ball"));
	query.add(new Link("is-a", "?x", "sports"));
	query.add(new Link("hobby", "?y", "?x"));
	 */
	sn.query(query);

	System.out.print("もう１回? 1...Yes/ 0...No ");
	question = stdIn0.nextInt();
    } while(question == 1);
		System.out.println("これで終了します");
    }
}

