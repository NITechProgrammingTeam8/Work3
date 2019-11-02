import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// GUIとの接合部
/*
 * 始めのデータ取得
 * ノードとその関係を取得(既存のメソッド)
 *
 * 拡張機能
 * ノードとその関係性の追加と削除、検索
 * ★追加(ノードとノードの関係性を一つ増やす)
 * 追加自体は既存のメソッドのみで可能
 * 値の返却を、与えらえた新出ノードと、他のノードとの関係性のみにする
 * →今までに存在していなかった部分
 * ★削除(ノードを削除又はノードとノードの関係性を一つ削除)
 * 削除用のメソッドが必要
 * 値の返却を、削除要求されたノード(とその関係性)とするか、逆に残ったノードにするか(これだと再構築か…)
 * ★検索()
 * 検索機能(既存のメソッド)(取得した結果を渡す)
 */

class AccessData {

	private SemanticNet sn;

	// コンストラクタ
	public AccessData(SemanticNet sn) {
        this.sn = sn;
    }


    // GUI起動時にSemanticNetに対してテキストファイルの読み込みを指示する
    // 構築した関係のノードをArrayList<Link>で渡す(渡し方検討中)
    public void start(String filename) {
        //String filename = "ex.txt";
        List<String> statementList = readTextFile(filename);
        for(String statement: statementList) {
            List<String> splitList = splitStatement(statement);
            addLink(sn, splitList);
        }
    }

    public ArrayList<Link> getLinks() {
    	return sn.getLinks();
    }

    public ArrayList<Node> getNodes() {
    	return sn.getNodes();
    }

    // ファイルの読み込み
    private static List<String> readTextFile(String fileName) {
        List<String> stateList = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                //System.out.println(line);
                stateList.add(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	//return stateList;
        }
        return stateList;
    }

    // 受け取った一行を空白で分割
    private static List<String> splitStatement(String statement) {
        return Arrays.asList(statement.split(" "));
    }

    // 受け取った一行をカンマで分割
    private static List<String> splitStatementC(String statementc) {
        return Arrays.asList(statementc.split(",", 0));
    }

    // リンクの作成
    private static void addLink(SemanticNet sn, List<String> list) {
        sn.addLink(new Link(list.get(1),list.get(0),list.get(2),sn));
    }

    // 検索文の挿入
    private static void addQuery(ArrayList<Link> query, List<String> list) {
        query.add(new Link(list.get(1),list.get(0),list.get(2)));
    }

    /*
    // GUI終了時になにかしたいことがあれば…
    public void finish() {

    }

    // 新規データの追加を指示する
    public void addData(String newData) {
        try {
            dao.addData(newData);
            view.successAddData();
        } catch(SQLException e) {
            view.showError(e.toString());
        } catch(ClassNotFoundException e) {
            view.showError(e.toString());
        } catch (Exception e) {
			view.showError(e.toString());
		} finally {
            try {
				dao.conn.close();
			} catch (SQLException e) {
				view.showError(e.toString());
			}
        }
    }
    */

    // 検索を行い結果を渡すように指示する
    // 返すのはノード(ArrayList)？
    public ArrayList searchData(String targetData) { // 複数の質問文は,区切りで与える

    	// 検索文の分割
    	List<String> querysplitList = splitStatementC(targetData);
    	ArrayList<Link> query = new ArrayList<Link>();

    	for(String splitLists: querysplitList) {
            List<String> splitList = splitStatement(splitLists);
            addQuery(query, splitList);
        }
    	// 何を返してる？
    	return sn.getQuery(query);
    }


    /*
    // ノード(又はその関係性)を削除するように指示する
    public void deleteData(int targetData) {
        try {
            dao.deleteData(targetData);
            view.successDeleteData();
        } catch(SQLException e) {
            view.showError(e.toString());
        } catch(ClassNotFoundException e) {
            view.showError(e.toString());
        } catch (Exception e) {
        	view.showError(e.toString());
		} finally {
            try {
				dao.conn.close();
			} catch (SQLException e) {
				view.showError(e.toString());
			}
        }
    }

    // 既存データの受け取り(必要か要検討)
    public void fetchData() {
        List<TextModel> resultList;
    	//List<String> resultList; // 【DBからlineしか取得しない場合】
        try {
            resultList = dao.FetchData();
            if(resultList.size() == 0) {
                view.showNoData();
            } else {
                view.showResultList(resultList);
            }
        } catch(SQLException e) {
            view.showError(e.toString());
        } catch(ClassNotFoundException e) {
            view.showError(e.toString());
        } catch (Exception e) {
        	view.showError(e.toString());
		} finally {
            try {
				dao.conn.close();
			} catch (SQLException e) {
				view.showError(e.toString());
			}
        }
    }
    */
}