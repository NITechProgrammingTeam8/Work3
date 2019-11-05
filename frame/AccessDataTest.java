import java.util.ArrayList;
import java.util.List;

class AccessDataTest {
    public static void main(String args[]){
    	if (args.length <= 1) {
    		System.out.println("引数を一つ以上指定してください");
    		System.exit(1);
    	}
    	AccessData ad = new AccessData(new AIFrameSystem());
        ad.start();
        
    	String frameName = ad.getFrameName();
    	System.out.println( "クラスフレーム：" + frameName );
        System.out.println();

        // インスタンスフレーム名の取得方法(現在は引数でファイル名指定)
        List<String> instanceName = new ArrayList<String>();
        for (int i = 0; i < args.length; i++) {
        	instanceName.add(args[i]);
        }

        // スロット名をリストで取得
        List<String> slotlistname = ad.getSrotListName();
        
        // クラスフレームのデフォルト値を取得
    	List<String> classframe = ad.getFrame(frameName);
    	System.out.println( "クラスフレームのデフォルト値" );
    	outputSlots(slotlistname, classframe);
        System.out.println();
        
        for (int i = 0; i < instanceName.size(); i++) {
        	System.out.println("インスタンスフレーム" + (i+1) + "："+instanceName.get(i));
            System.out.println();
            
        	ad.makeInstance(instanceName.get(i));
        	List<String> defalt = ad.getFrame(instanceName.get(i));
        	System.out.println( "device,value,charges,tribute はデフォルト値" );
        	outputSlots(slotlistname, defalt);
            System.out.println();
            
        	// インスタンスフレームの値をテキストファイルから取得
        	List<String> slotList = ad.readTextFile("games/"+instanceName.get(i)+".txt");
        	// インスタンスフレームの値を格納
        	ad.addInstanceData(instanceName.get(i), slotList);
        	// インスタンスフレームの値を取得(使用するか)
        	List<String> instance = ad.getFrame(instanceName.get(i));
        	System.out.println( "テキストファイルから取得した値" );
        	outputSlots(slotlistname, instance);
            System.out.println();
            
        	// 再びデフォルト値を取得
        	List<String> defaltinstance = ad.getDefaltInstanceFrame(instanceName.get(i));
        	System.out.println( "再びデフォルト値" );
        	outputSlots(slotlistname, defaltinstance);
        	System.out.println();
        }

        // フレームの個別値の変更
        String name = instanceName.get(0);
        String slotname = "charges";
        String change = "10000";
        ad.changeData(name,slotname,change);
        List<String> change1 = ad.getFrame(instanceName.get(0));
        System.out.println( name + "のchargesを10000に変更" );
        outputSlots(slotlistname, change1);
        System.out.println();
        name = frameName;
        slotname = "value";
        change = "1234";
        ad.changeData(name,slotname,change);
        change1 = ad.getFrame(frameName);
        System.out.println( "クラスフレームのvalueを1234に変更" );
        outputSlots(slotlistname, change1);
        System.out.println();
        name = instanceName.get(1);
        slotname = "device";
        change = "PSP";
        ad.changeData(name,slotname,change);
        change1 = ad.getFrame(instanceName.get(1));
        System.out.println( name + "のdeviceをPSPに変更" );
        outputSlots(slotlistname, change1);
        System.out.println();
        name = instanceName.get(2);
        slotname = "tribute";
        change = "10";
        ad.changeData(name,slotname,change);
        change1 = ad.getFrame(instanceName.get(2));
        System.out.println( name + "のtributeを10に変更" );
        outputSlots(slotlistname, change1);
        System.out.println();
        for (String in : instanceName) {
        	List<String> defaltinstance = ad.getDefaltInstanceFrame(in);
        	System.out.println( "再びデフォルト値：" + in  );
        	outputSlots(slotlistname, defaltinstance);
        	System.out.println();
        }
        System.out.println("値の個別取得");
        System.out.println(instanceName.get(0) + "：" + slotlistname.get(3) + "：" + "not defalt");
        System.out.println(ad.getFrameUnit(instanceName.get(0), slotlistname.get(3)));
        System.out.println(instanceName.get(0) + "：" + slotlistname.get(3) + "：" + "defalt");
        System.out.println(ad.getDefaltInstanceFrameUnit(instanceName.get(0), slotlistname.get(3)));
        System.out.println();
        // インスタンスフレームの追加
        System.out.println("インスタンスフレームの追加");
        String newinstance = "Pokemon";
        instanceName.add(newinstance);
        ad.makeInstance(newinstance);
        ad.changeData(newinstance,"device","Wii");
        ad.changeData(newinstance,"charges","5432");
        //List<String> newdata = Arrays.asList("1000", "500", "1500");
        //ad.addInstanceData(newinstance, newdata);
        List<String> instance = ad.getFrame(newinstance);
    	System.out.println( "追加されたインスタンスフレームフレーム：" + newinstance);
    	outputSlots(slotlistname, instance);
    	System.out.println();
    	System.out.println("インスタンスフレームの削除");
    	ad.deleteInstanceFrame(instanceName, "Dragon-Quest");
    	for (String in : instanceName) {
        	List<String> afterdelete = ad.getFrame(in);
        	System.out.println( "Dragon-Quest削除後のインスタンスフレーム一覧：" + in );
        	outputSlots(slotlistname, afterdelete);
        	System.out.println();
        }
        /*
         * それぞれ個別にデフォルト値かどうかを選択できるように書き換えることも可能(メソッド作成済)
         *
         * ★nullと入力すると、インスタンスフレームの初期化が行えるようにしたい
         * ★削除
         * インスタンスフレームリストから削除することでアクセス出来ないように変更
         * この方法で問題は起きないか
         */
        // 検索はメソッド次第
    }

    // スロット値表示用メソッド(確認のため)
    private static void outputSlots(List<String> slotname, List<String> slotvalue) {
        System.out.println( "スロット値一覧" );
        System.out.print( slotname.get(0) + ":" );
        System.out.println( slotvalue.get(0) );
        System.out.print( slotname.get(1) + ":" );
        System.out.println( slotvalue.get(1) );
        System.out.print( slotname.get(2) + ":" );
        System.out.println( slotvalue.get(2) );
        System.out.print( slotname.get(3) + ":" );
        System.out.println( slotvalue.get(3) );
    }
}