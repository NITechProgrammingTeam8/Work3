import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class FrameTest {
    public static void main(String args[]) {
        // フレームシステムの初期化
        AIFrameSystem fs = new AIFrameSystem();

        String frameName = "game";
        System.out.println( "クラスフレーム："+frameName );
        System.out.println();
        
        defineClassFrame(fs, frameName);
      
        //ここから個別
        // インスタンスフレームを引数から生成
        if (args.length == 1) {
            String instanceName = args[0];
            fs.createInstanceFrame( frameName, instanceName );
            System.out.println("インスタンスフレーム："+instanceName);
            System.out.println();

            List<String> slotList = readTextFile("games/"+instanceName+".txt");

            // device,value,charges,tribute はデフォルト値
            System.out.println( "device,value,charges,tribute はデフォルト値" );
            outputSlots(fs, instanceName, false);
            System.out.println();

            // tribute はデフォルト値
            System.out.println( "tribute はデフォルト値" );
            fs.writeSlotValue( instanceName, "device", new String(slotList.get(0)));
            fs.writeSlotValue( instanceName, "value", new Integer(slotList.get(1)) );
            fs.writeSlotValue( instanceName, "charges", new Integer(slotList.get(2)) );
            outputSlots(fs, instanceName, false);
            System.out.println();

            // 再びデフォルト値
            System.out.println( "再びデフォルト値" );
            fs.writeSlotValue( instanceName, "tribute", new Integer(slotList.get(3)) );
            outputSlots(fs, instanceName, true);
        }
      
    }

    private static List<String> readTextFile(String fileName) {
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
            return statementList;
        }
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

    private static void outputSlots(AIFrameSystem fs, String instanceName, boolean isDefault) {
        System.out.println( "スロット値一覧" );
        System.out.print( "device:" );
        System.out.println( fs.readSlotValue( instanceName, "device", isDefault ) );
        System.out.print( "value:" );
        System.out.println( fs.readSlotValue( instanceName, "value", isDefault ) );
        System.out.print( "charges:" );
        System.out.println( fs.readSlotValue( instanceName, "charges", isDefault ) );
        System.out.print( "tribute:" );
        System.out.println( fs.readSlotValue( instanceName, "tribute", isDefault ) );
    }
       
}