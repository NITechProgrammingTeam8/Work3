public class FrameTest {
    public static void main(String args[]) {
        System.out.println( "Frame" );
      
        // フレームシステムの初期化
        AIFrameSystem fs = new AIFrameSystem();

        String frameName = "game";
        
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
      
        //ここから個別
        // インスタンスフレーム tora の生成
        String instanceName = "Monster-Hanter";
        fs.createInstanceFrame( frameName, instanceName );
      
        // height と weight はデフォルト値
        System.out.println( fs.readSlotValue( instanceName, "device", false ) );
        System.out.println( fs.readSlotValue( instanceName, "value", false ) );
        System.out.println( fs.readSlotValue( instanceName, "charges", false ) );
        System.out.println( fs.readSlotValue( instanceName, "tribute", false ) );
      
        // weight はデフォルト値
        fs.writeSlotValue( instanceName, "value", new Integer( 9800 ) );
        fs.writeSlotValue( instanceName, "charges", new Integer(114514) );
        fs.writeSlotValue( instanceName, "device", new String("PS4"));
        System.out.println( fs.readSlotValue( instanceName, "device", false ) );
        System.out.println( fs.readSlotValue( instanceName, "value", false ) );
        System.out.println( fs.readSlotValue( instanceName, "charges", false ) );
        System.out.println( fs.readSlotValue( instanceName, "tribute", false ) );
      
    }
       
}