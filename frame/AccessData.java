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
    	return  getSlots(fs, frameName, false);
    }
	// 単体取得
    public String getFrameUnit(String frameName, String slotname) {
    	return  getSlot(fs, frameName, slotname, false);
    }
    // 再びデフォルト値を取得、に対応
    public List<String> getDefaltInstanceFrame(String instanceName) {
    	return getSlots(fs, instanceName, true);
    }
	// 単体デフォルト取得
    public String getDefaltInstanceFrameUnit(String instanceName, String slotname) {
    	return getSlot(fs, instanceName, slotname, true);
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
    // フレームスロット値個別取得
    public String getSlot(AIFrameSystem fs, String instanceName, String slotname, boolean isDefault) {
    	return Objects.toString(fs.readSlotValue( instanceName, slotname, isDefault ));
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

    // インスタンスフレームの削除
    public void deleteInstanceFrame(List<String> instanceList,String instancename) {
    	instanceList.remove(instanceList.indexOf(instancename));
    }
}