import java.util.List;

class AccessDataTest {
    public static void main(String args[]){
    	//SemanticNet sn = new SemanticNet();
    	AccessData ad = new AccessData(new AIFrameSystem());

    	ad.start();
    	String frameName = ad.getFrameName();
    	System.out.println( "クラスフレーム："+frameName );
        System.out.println();
        // インスタンスフレーム名の取得方法(検討中)
        String instanceName = args[0];
        System.out.println("インスタンスフレーム："+instanceName);
        System.out.println();
        ad.makeInstance(instanceName);
        // スロット名をリストで取得
        List<String> slotlistname = ad.getSrotListName();

        // クラスフレームのデフォルト値を取得
        List<String> classframe = ad.getFrame(frameName);
        System.out.println( "クラスフレームのデフォルト値" );
        outputSlots(slotlistname, classframe);
        System.out.println();
        List<String> defalt = ad.getFrame(instanceName);
        System.out.println( "device,value,charges,tribute はデフォルト値" );
        outputSlots(slotlistname, defalt);
        System.out.println();
        // インスタンスフレームの値をテキストファイルから取得
        List<String> slotList = ad.readTextFile("games/"+instanceName+".txt");
        // インスタンスフレームの値を格納
        ad.addInstanceData(instanceName, slotList);
        // インスタンスフレームの値を取得(使用するか)
        List<String> instance = ad.getFrame(instanceName);
        System.out.println( "テキストファイルから取得した値" );
        outputSlots(slotlistname, instance);
        System.out.println();
        // 再びデフォルト値を取得
        List<String> defaltinstance = ad.getDefaltInstanceFrame(instanceName);
        System.out.println( "再びデフォルト値" );
        outputSlots(slotlistname, defaltinstance);
        System.out.println();


        // フレームの個別値の変更
        String name = instanceName;
        String slotname = "charges";
        String change = "10000";
        ad.changeData(name,slotname,change);
        List<String> change1 = ad.getFrame(instanceName);
        System.out.println( "chargesを10000に変更" );
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
        name = instanceName;
        slotname = "device";
        change = "PSP";
        ad.changeData(name,slotname,change);
        change1 = ad.getFrame(instanceName);
        System.out.println( "deviceをPSPに変更" );
        outputSlots(slotlistname, change1);
        System.out.println();
        name = instanceName;
        slotname = "tribute";
        change = "10";
        ad.changeData(name,slotname,change);
        change1 = ad.getFrame(instanceName);
        System.out.println( "tributeを10に変更" );
        outputSlots(slotlistname, change1);
        System.out.println();
        defaltinstance = ad.getDefaltInstanceFrame(instanceName);
        System.out.println( "再びデフォルト値" );
        outputSlots(slotlistname, defaltinstance);

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