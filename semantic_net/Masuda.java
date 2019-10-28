import java.util.*;

public class Masuda {
    public static void main(String args[]){
        SemanticNet sn = new SemanticNet();
    
    // サッカーはスポーツである.
	sn.addLink(new Link("is-a","soccer","sports",sn));

	// 大輝は名古屋工業大学の学生である.
    sn.addLink(new Link("is-a","Hiroki","NIT-student",sn));

	// 大輝の専門は人工知能である.
	sn.addLink(new Link("speciality","Hiroki","AI",sn));
	
	// クロノ・クロスはゲームである.
	sn.addLink(new Link("is-a","Chrono-Cross","game",sn));

	// ゲームはストーリー持つ.
	sn.addLink(new Link("has-a","game","story",sn));
	
	// 大輝の趣味はサッカーである.
	sn.addLink(new Link("hobby","Hiroki","soccer",sn));
	
	// 大輝はクロノ・クロスで遊ぶ
	sn.addLink(new Link("play","Hiroki","Chrono-Cross",sn));

	// 名古屋工業大学の学生は,学生である.
	sn.addLink(new Link("is-a","NIT-student","student",sn));

	// 学生は勉強しない.
    sn.addLink(new Link("donot","student","study",sn));
    
    // 進撃の巨人はアニメである.
    sn.addLink(new Link("is-a", "Attack-On-Titan", "animation",sn));

    // 大輝は進撃の巨人を視聴する.
    sn.addLink(new Link("watch", "Hiroki", "Attack-On-Titan",sn));

    // 進撃の巨人は世界の一つだ.
    sn.addLink(new Link("is-a", "Attack-On-Titan", "world",sn));

    // 世界は残酷だ
    sn.addLink(new Link("is", "world", "cruel",sn));

	sn.printLinks();
	sn.printNodes();

    ArrayList<Link> query = new ArrayList<Link>();
	query.add(new Link("play","?y","Chrono-Cross"));
	query.add(new Link("is-a","?y","student"));
    query.add(new Link("hobby","?y","soccer"));
    query.add(new Link("watch","?y","Attack-On-Titan"));
	sn.query(query);
    }    
}