import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// GUIとの接合部
/* クラスフレームを定義する(これは規定にするか否か。GUIで定義はしない、せめてファイル読み込み)
 * テキストファイルからデータ取得
 * ノードとその関係を取得(既存のメソッド？)
 *
 * 拡張機能
 * ノードとその関係性の追加と削除、検索
 * ★追加(フレームを一つ増やす)
 * 追加自体は既存のメソッドのみで可能
 * 値の返却を、与えらえた新出フレームと、他のフレームとの関係性のみにする(各スロットの関係性の矢印)
 * →今までに存在していなかった部分
 * 追加はインスタンスフレームのみにする(とりあえず)
 * ★削除(ノードを削除又はノードとノードの関係性を一つ削除)
 * 削除用のメソッドが必要
 * 削除するのは、インスタンスフレームのスロットの中身だと既存のメソッドで可能
 * フレームごと削除は検討中
 * 値の返却を、削除要求されたフレーム(とその関係性)とするか、逆に残ったフレームにするか(これだと再構築か…)
 * 削除もインスタンスフレームのみにする(とりあえず)
 * ★検索()
 * 検索機能(取得した結果を渡す)
 * 何の検索をする？？？検索用メソッドを新しく作成する必要あり？？
 */

class AccessData {
	// フレームシステムの初期化
	private AIFrameSystem fs;
	String frameName = "game";

	// コンストラクタ
	public AccessData(AIFrameSystem fs) {
        this.fs = fs;
    }

    // GUI起動時にクラスフレームを定義(規定値)
    public void start() {
    	defineClassFrame(fs, frameName);
    }

    // インスタンスフレームを引数から生成
    public void makeInstance(String instanceName) {
        fs.createInstanceFrame( frameName, instanceName );
    }

    /*
    // テキストファイルからデータを読み込む
    public  List<String> readData(String filename) {
    	List<String> statementList = readTextFile(filename);
        return statementList;
    }
    */

    private static void defineClassFrame(AIFrameSystem fs, String frameName) {
        // クラスフレーム game の生成
        fs.createClassFrame( frameName );
        // device スロットを設定
        fs.writeSlotValue( frameName, "device", new String("device") );
        // value スロットを設定
        fs.writeSlotValue( frameName, "value", new Integer(0));
        // charges スロットを設定
        fs.writeSlotValue( frameName , "charges", new Integer(0));
        // value と charges から tribute を計算するための式 tribute = value + charges を
        // when-requested demon として tribute スロットに割り当てる
        fs.setWhenRequestedProc( frameName, "tribute", new AIDemonProcReadTest() );
    }

    // クラスフレーム名を取得
    public String getFrameName() {
    	return frameName;
    }

    // スロット名をリストで取得
    public List<String> getSrotListName() {
    	List<String> srotlist = new ArrayList();
    	srotlist.add("device");
    	srotlist.add("value");
    	srotlist.add("charges");
    	srotlist.add("tribute");
    	return srotlist;
    }

    // クラスフレームの規定値を取得
    public List<String> getFrame(String frameName) {
    	List<String> frame = getSlots(fs, frameName, false);
    	return frame;
    }

    /*
    // インスタンスフレームの格納値を取得
    public List<String> getInstanceFrame(String instanceName) {
    	List<String> instance = getSlots(fs, instanceName, false);
    	return instance;
    }
    */

    // 再びデフォルト値を取得、に対応
    public List<String> getDefaltInstanceFrame(String instanceName) {
    	List<String> defaltinstance = getSlots(fs, instanceName, true);
    	return defaltinstance;
    }

    // スロット名の受け取り方法検討中
    // フレームのスロット値取得
    public List<String> getSlots(AIFrameSystem fs, String instanceName, boolean isDefault) {
    	List<String> slots = new ArrayList();
    	//Object test = fs.readSlotValue( instanceName, "tribute", isDefault );
    	//Objects.toString(test);
    	//System.out.println(test);
    	slots.add( (String)fs.readSlotValue( instanceName, "device", isDefault ) );
    	slots.add( Objects.toString(fs.readSlotValue( instanceName, "value", isDefault )) );
    	slots.add( Objects.toString(fs.readSlotValue( instanceName, "charges", isDefault )) );
    	slots.add( Objects.toString(fs.readSlotValue( instanceName, "tribute", isDefault )) );
    	return slots;
    }

    // スロットリスト受け取り方検討中
    // テキストファイルから読み込んだデータの格納
    public void addInstanceData(String instanceName, List<String> slotList) {
    	fs.writeSlotValue( instanceName, "device", new String(slotList.get(0)));
        fs.writeSlotValue( instanceName, "value", new Integer(slotList.get(1)) );
        fs.writeSlotValue( instanceName, "charges", new Integer(slotList.get(2)) );
        fs.writeSlotValue( instanceName, "tribute", new Integer(slotList.get(3)) );
    }

    // ファイルの読み込み
    public List<String> readTextFile(String fileName) {
        List<String> statementList = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                statementList.add(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //return statementList;
        }
        return statementList;
    }

    // 値の個別書き換え
    public void changeData(String name, String slotname, String change) {
    	if (slotname.equals("device")) {
    		fs.writeSlotValue( name, slotname, change );
    	} else {
    		fs.writeSlotValue( name, slotname, Integer.parseInt( change ) );
    	}
    }

    /*
    // 受け取った一行を空白で分割
    private static List<String> splitStatement(String statement) {
        return Arrays.asList(statement.split(" "));
    }

    // 受け取った一行をカンマで分割
    private static List<String> splitStatementC(String statementc) {
        return Arrays.asList(statementc.split(",", 0));
    }
    */

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

    /*
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
	*/

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