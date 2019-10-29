import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

class MakeSemanticNet {
    public static void main(String args[]){
        SemanticNet sn = new SemanticNet();
        if(args.length == 1) {
            List<String> statementList = readTextFile("members/"+args[0]+".txt");
            for(String statement: statementList) {
                List<String> splitList = splitStatement(statement);
                addLink(sn, splitList);
            }
            sn.printLinks();
            sn.printNodes();
            
            List<String> queryList = readTextFile("queries/"+args[0]+".txt");
            ArrayList<Link> query = new ArrayList<Link>();
            for(String queryStatement: queryList) {
                List<String> splitList = splitStatement(queryStatement);
                addQuery(query, splitList);
            }
            sn.query(query);
        }
    }

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
            return stateList;
        }
    }

    private static List<String> splitStatement(String statement) {
        return Arrays.asList(statement.split(" "));
    }

    private static void addLink(SemanticNet sn, List<String> list) {
        sn.addLink(new Link(list.get(1),list.get(0),list.get(2),sn));
    }

    private static void addQuery(ArrayList<Link> query, List<String> list) {
        query.add(new Link(list.get(1),list.get(0),list.get(2)));
    }
}