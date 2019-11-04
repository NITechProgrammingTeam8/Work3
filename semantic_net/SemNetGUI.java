import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SemNetGUI extends JFrame {
    JLabel status;
    RelationMap rMap;
    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("USAGE: java SemNetGUI [name]");
            System.exit(1);
        }
        SemNetGUI frame = new SemNetGUI("セマンティックネット", args[0]);
        frame.setVisible(true);
    }

    SemNetGUI(String title, String name) {

        setTitle(title + " (" + name + ")");
        int appWidth = 1500;
        int appHeight = 700;
        setBounds(100, 100, appWidth, appHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rMap = new RelationMap("members/" + name + ".txt");
        JPanel mainPanel = rMap;
        JPanel menuPanel = new MenuPanel();
        status = new JLabel();

        Container contentPane = getContentPane();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(menuPanel, BorderLayout.NORTH);
        contentPane.add(status, BorderLayout.SOUTH);
    }

    class MenuPanel extends JPanel implements ActionListener {
        JTextField text;

        MenuPanel() {
            text = new JTextField(30);
            JButton[] btns = new JButton[3];
            btns[0] = new JButton("検索");
            btns[1] = new JButton("追加");
            btns[2] = new JButton("削除");

            add(text);
            for (JButton b : btns) {
                add(b);
                b.addActionListener(this);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            String arg = text.getText();

            if (cmd.equals("検索")) {
                rMap.searchNode(arg);
                status.setText("検索の実行: " + arg);
            } else if (cmd.equals("追加")) {
                rMap.addNode(arg);
                status.setText("追加の実行: " + arg);
            } else if (cmd.equals("削除")) {
                rMap.removeNode(arg);
                status.setText("削除の実行: " + arg);
            }
        }
    }
}

class RelationMap extends JPanel {
    private SemanticNet sn;
    private AccessData ad;
    private Map<Node, NodePanel> nodes; // NodeからNodePanelへのポインタは無いためこれで代用
    private Map<Link, LinkPanel> links; // LinkからLinkPanelも同様

    RelationMap(String filename) {
        sn = new SemanticNet();
        ad = new AccessData(sn);
        ad.start(filename);  // セマンティックネットの構築

        setLayout(null);
        nodes = new HashMap<>();
        ArrayList<Node> nodeList = ad.getNodes();  // セマンティックネットからノードの取得
        int xloc = 100;
        int yloc = 100;
        for (Node n : nodeList) {
            NodePanel np = new NodePanel(n);
            np.setBounds(xloc, yloc, 180, 30);
            xloc += 50;
            yloc += 20;

            add(np);
            nodes.put(n, np);
        }
        links = new HashMap<>();
        ArrayList<Link> linkList = ad.getLinks();  // セマンティックネットからリンクの取得
        for (Link l : linkList) {
            NodePanel tail = nodes.get(l.getTail());
            NodePanel head = nodes.get(l.getHead());
            LinkPanel lp = new LinkPanel(l, tail, head);
            tail.addDepartFromMeLinks(lp);
            head.addArriveAtMeLinks(lp);

            add(lp);
            links.put(l, lp);
        }
    }

    void searchNode(String text) {
        ArrayList<Node> result = ad.searchNaturalData(text);
        for(Node n : result) {
            // ノードの色または枠を変更する
        }
    }

    void addNode(String text) {

    }

    void removeNode(String text) {

    }
}

class NodePanel extends JPanel {
    private static int counter = 0;
    private int id;
    private Node node;
    private ArrayList<LinkPanel> departFromMeLinks; // 自分から出ていくリンク
    private ArrayList<LinkPanel> arriveAtMeLinks; // 自分に入ってくるリンク
    private int draggedAtX, draggedAtY;

    NodePanel(Node node) {
        id = counter++;
        this.node = node;
        departFromMeLinks = new ArrayList<>();
        arriveAtMeLinks = new ArrayList<>();

        setBackground(Color.ORANGE);
        setBorder(new BevelBorder(BevelBorder.RAISED));

        JLabel label = new JLabel(id + ": " + node.getName());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                draggedAtX = e.getX();
                draggedAtY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // System.out.println(e.getX() +", " + draggedAtX + "," + getLocation().x);
                setLocation(e.getX() - draggedAtX + getLocation().x, e.getY() - draggedAtY + getLocation().y);

                for (LinkPanel lp : departFromMeLinks) {
                    lp.update();
                    lp.repaint();
                }
                for (LinkPanel lp : arriveAtMeLinks) {
                    lp.update();
                    lp.repaint();
                }
            }
        });

        add(label);
    }

    int getId() {
        return id;
    }

    Node getNode() {
        return node;
    }

    public void addDepartFromMeLinks(LinkPanel theLink) {
        departFromMeLinks.add(theLink);
    }

    public void addArriveAtMeLinks(LinkPanel theLink) {
        arriveAtMeLinks.add(theLink);
    }
}

class LinkPanel extends JPanel {
    private Link link;
    private NodePanel tail;
    private NodePanel head;
    private Point start;
    private Point end;
    private int margin; // パネルの幅の猶予(パネルが端折れないようにするため)
    private JPanel mainPanel;

    LinkPanel(Link link, NodePanel tail, NodePanel head) { // tail =label=> head
        this.link = link;
        this.tail = tail;
        this.head = head;
        margin = 100;

        // setBackground(Color.BLACK);
        setOpaque(false); // パネルの透過
        Rectangle source = tail.getBounds();
        Rectangle distance = head.getBounds();
        setShortestDistance(source, distance);

        setSize();

        setLayout(null);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0, 128, 128));
        mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JLabel label = new JLabel(tail.getId() + " = " + link.getLabel() + " => " + head.getId());

        mainPanel.add(label);

        int mainWidth = 120;
        int mainHeight = 30;
        int fitX = -(mainWidth / 2);
        int fitY = -(mainHeight / 2);
        mainPanel.setBounds((getRight() - getLeft()) / 2 + fitX, (getBtm() - getTop()) / 2 + fitY, mainWidth,
                mainHeight);
        add(mainPanel);
    }

    void setShortestDistance(Rectangle source, Rectangle distance) {
        Point[] fromMidPoints = getMidPoints(source);
        Point[] toMidPoints = getMidPoints(distance);

        double min = Double.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            Point from = fromMidPoints[i].getLocation();
            for (int j = 0; j < 4; j++) {
                Point to = toMidPoints[j].getLocation();
                double value = (from.getX() - to.getX()) * (from.getX() - to.getX())
                        + (from.getY() - to.getY()) * (from.getY() - to.getY());
                if (value < min) {
                    min = value;
                    start = from;
                    end = to;
                }
            }
        }
    }

    Point[] getMidPoints(Rectangle r) {
        Point[] midPoints = new Point[4];
        for (int i = 0; i < midPoints.length; i++) {
            midPoints[i] = new Point();
        }
        midPoints[0].setLocation(r.x + r.width / 2.0, r.y); // 上の中点
        midPoints[1].setLocation(r.x + r.width, r.y + r.height / 2.0); // 右の中点
        midPoints[2].setLocation(r.x + r.width / 2.0, r.y + r.height); // 下の中点
        midPoints[3].setLocation(r.x, r.y + r.height / 2.0); // 左の中点
        return midPoints;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintArrows(g);
    }

    void paintArrows(Graphics g) {
        int fromX = start.x;
        int fromY = start.y;
        int toX = end.x;
        int toY = end.y;

        int constX = getLeft();
        int constY = getTop();

        g.setColor(Color.BLUE);
        g.drawLine(fromX - constX, fromY - constY, toX - constX, toY - constY);
    }

    Point getStart() {
        return start;
    }

    Point getEnd() {
        return end;
    }

    int getLeft() {
        return execHor(true) - margin;
    }

    int getRight() {
        return execHor(false) + margin;
    }

    int getTop() {
        return execVer(true) - margin;
    }

    int getBtm() {
        return execVer(false) + margin;
    }

    int execHor(boolean b) {
        int left = start.x;
        int right = end.x;
        if (left > right) {
            int tmp = left;
            left = right;
            right = tmp;
        }
        if (b) {
            return left;
        } else {
            return right;
        }
    }

    int execVer(boolean b) {
        int top = start.y;
        int btm = end.y;
        if (top > btm) {
            int tmp = top;
            top = btm;
            btm = tmp;
        }
        if (b) {
            return top;
        } else {
            return btm;
        }
    }

    void setSize() {
        int lpX = getLeft();
        int lpY = getTop();
        int lpWidth = getRight() - lpX;
        int lpHeight = getBtm() - lpY;
        setBounds(lpX, lpY, lpWidth, lpHeight);
        // System.out.println(lp);
        // System.out.println(nodes.get(l.getTail()));
        // System.out.println(nodes.get(l.getHead()));
        // System.out.println();
    }

    void update() {
        Rectangle source = tail.getBounds();
        Rectangle distance = head.getBounds();
        setShortestDistance(source, distance);
        setSize();

        int fitX = -(mainPanel.getWidth() / 2);
        int fitY = -(mainPanel.getHeight() / 2);
        mainPanel.setLocation((getRight() - getLeft()) / 2 + fitX, (getBtm() - getTop()) / 2 + fitY);
    }
}