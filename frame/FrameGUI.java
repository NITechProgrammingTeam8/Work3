import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FrameGUI extends JFrame {
    public static void main(String args[]) {
        FrameGUI frame = new FrameGUI("フレーム");
        frame.setVisible(true);
    }

    FrameGUI(String title) {

        setTitle(title);
        int appWidth = 1500;
        int appHeight = 700;
        setBounds(100, 100, appWidth, appHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel menuPanel = new JPanel();
        JButton btn = new JButton("Push!!");

        menuPanel.add(btn);

        JPanel mainPanel = new FrameMap();

        Container contentPane = getContentPane();
        contentPane.add(menuPanel, BorderLayout.WEST);
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }
}

class FrameMap extends JPanel {
    private AIFrameSystem fs;
    private AccessData ad;
    // private Map<Node, NodePanel> nodes; // NodeからNodePanelへのポインタは無いためこれで代用
    // private Map<Link, LinkPanel> links; // LinkからLinkPanelも同様
    // private NodePanel dragPanel;

    FrameMap() {
        fs = new AIFrameSystem();
        ad = new AccessData(fs);
        ad.start();


        // スーパフレーム
        String frameName = ad.getFrameName();
        List<String> slotList = ad.getSrotListName();
        List<String> slotValue = ad.getFrame(frameName);
        FramePanel sfp = new FramePanel(frameName, slotList, slotValue);

        add(sfp);

        // インスタンスフレーム
        String name = "Azurlane";
        ad.makeInstance(name);
        // FramePanel ifp = new FramePanel(name, sfp);
        // List<String> defalt = ad.getFrame(name); // デフォルト値

        List<String> readData = ad.readTextFile("games/"+name+".txt");
        ad.addInstanceData(name, readData);  // デフォルト値を変更
        List<String> instanceValue = ad.getFrame(name);
        FramePanel ifp = new FramePanel(name, slotList, instanceValue);

        add(ifp);

        // setLayout(null);
        // nodes = new HashMap<>();
        // ArrayList<Node> nodeList = ad.getNodes();
        // int xloc = 100;
        // int yloc = 100;
        // for (Node n : nodeList) {
        //     NodePanel np = new NodePanel(n);
        //     np.setBounds(xloc, yloc, 180, 30);
        //     xloc += 50;
        //     yloc += 20;

        //     add(np);
        //     nodes.put(n, np);
        // }
        // links = new HashMap<>();
        // ArrayList<Link> linkList = ad.getLinks();
        // for (Link l : linkList) {
        //     NodePanel tail = nodes.get(l.getTail());
        //     NodePanel head = nodes.get(l.getHead());
        //     LinkPanel lp = new LinkPanel(l, tail, head);
        //     tail.addDepartFromMeLinks(lp);
        //     head.addArriveAtMeLinks(lp);

        //     add(lp);
        //     links.put(l, lp);
        // }
    }
}

class FramePanel extends JPanel {
    private static int counter = 0;
    private int id;
    private String name;
    private Map<String, String> slots;
    private int draggedAtX, draggedAtY;

    FramePanel(String name, List<String> list, List<String> values) {
        id = counter++;
        this.name = name;
        slots = new LinkedHashMap<>();
        for(int i = 0; i < list.size(); i++) {
            slots.put(list.get(i), values.get(i));
        }

        setBackground(Color.ORANGE);

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
                setLocation(e.getX() - draggedAtX + getLocation().x, e.getY() - draggedAtY + getLocation().y);

                // for (LinkPanel lp : departFromMeLinks) {
                //     lp.update();
                //     lp.repaint();
                // }
                // for (LinkPanel lp : arriveAtMeLinks) {
                //     lp.update();
                //     lp.repaint();
                // }
            }
        });

        BevelBorder inborder = new BevelBorder(BevelBorder.LOWERED);
        String label = id + ": " + name;
        TitledBorder border = new TitledBorder(inborder, label, TitledBorder.CENTER, TitledBorder.ABOVE_TOP);

        setLayout(new GridLayout(slots.size() ,2));
        for (Map.Entry<String, String> entry : slots.entrySet()) {
            add(new JLabel(entry.getKey()));
            add(new JTextField(entry.getValue()));
        }

        setBorder(border);
    }

    int getId() {
        return id;
    }

    Map<String, String> getSlots() {
        return slots;
    }
}

// class LinkPanel extends JPanel {
//     private Link link;
//     private NodePanel tail;
//     private NodePanel head;
//     private Point start;
//     private Point end;
//     private int margin; // パネルの幅の猶予(パネルが端折れないようにするため)
//     private JPanel mainPanel;

//     LinkPanel(Link link, NodePanel tail, NodePanel head) { // tail =label=> head
//         this.link = link;
//         this.tail = tail;
//         this.head = head;
//         margin = 100;

//         // setBackground(Color.BLACK);
//         setOpaque(false); // パネルの透過
//         Rectangle source = tail.getBounds();
//         Rectangle distance = head.getBounds();
//         setShortestDistance(source, distance);

//         setSize();

//         setLayout(null);

//         mainPanel = new JPanel();
//         mainPanel.setBackground(new Color(0, 128, 128));
//         mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
//         JLabel label = new JLabel(tail.getId() + " = " + link.getLabel() + " => " + head.getId());

//         mainPanel.add(label);

//         int mainWidth = 120;
//         int mainHeight = 30;
//         int fitX = -(mainWidth / 2);
//         int fitY = -(mainHeight / 4);
//         mainPanel.setBounds((getRight() - getLeft()) / 2 + fitX, (getBtm() - getTop()) / 2 + fitY, mainWidth,
//                 mainHeight);
//         add(mainPanel);
//     }

//     void setShortestDistance(Rectangle source, Rectangle distance) {
//         Point[] fromMidPoints = getMidPoints(source);
//         Point[] toMidPoints = getMidPoints(distance);

//         double min = Double.MAX_VALUE;
//         for (int i = 0; i < 4; i++) {
//             Point from = fromMidPoints[i].getLocation();
//             for (int j = 0; j < 4; j++) {
//                 Point to = toMidPoints[j].getLocation();
//                 double value = (from.getX() - to.getX()) * (from.getX() - to.getX())
//                         + (from.getY() - to.getY()) * (from.getY() - to.getY());
//                 if (value < min) {
//                     min = value;
//                     start = from;
//                     end = to;
//                 }
//             }
//         }
//     }

//     Point[] getMidPoints(Rectangle r) {
//         Point[] midPoints = new Point[4];
//         for (int i = 0; i < midPoints.length; i++) {
//             midPoints[i] = new Point();
//         }
//         midPoints[0].setLocation(r.x + r.width / 2.0, r.y); // 上の中点
//         midPoints[1].setLocation(r.x + r.width, r.y + r.height / 2.0); // 右の中点
//         midPoints[2].setLocation(r.x + r.width / 2.0, r.y + r.height / 2.0); // 下の中点
//         midPoints[3].setLocation(r.x, r.y + r.height / 2.0); // 左の中点
//         return midPoints;
//     }

//     @Override
//     public void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         paintArrows(g);
//     }

//     void paintArrows(Graphics g) {
//         int fromX = start.x;
//         int fromY = start.y;
//         int toX = end.x;
//         int toY = end.y;

//         int constX = getLeft();
//         int constY = getTop();

//         g.setColor(Color.BLUE);
//         g.drawLine(fromX - constX, fromY - constY, toX - constX, toY - constY);
//     }

//     Point getStart() {
//         return start;
//     }

//     Point getEnd() {
//         return end;
//     }

//     int getLeft() {
//         return execHor(true) - margin;
//     }

//     int getRight() {
//         return execHor(false) + margin;
//     }

//     int getTop() {
//         return execVer(true) - margin;
//     }

//     int getBtm() {
//         return execVer(false) + margin;
//     }

//     int execHor(boolean b) {
//         int left = start.x;
//         int right = end.x;
//         if (left > right) {
//             int tmp = left;
//             left = right;
//             right = tmp;
//         }
//         if (b) {
//             return left;
//         } else {
//             return right;
//         }
//     }

//     int execVer(boolean b) {
//         int top = start.y;
//         int btm = end.y;
//         if (top > btm) {
//             int tmp = top;
//             top = btm;
//             btm = tmp;
//         }
//         if (b) {
//             return top;
//         } else {
//             return btm;
//         }
//     }

//     void setSize() {
//         int lpX = getLeft();
//         int lpY = getTop();
//         int lpWidth = getRight() - lpX;
//         int lpHeight = getBtm() - lpY;
//         setBounds(lpX, lpY, lpWidth, lpHeight);
//         // System.out.println(lp);
//         // System.out.println(nodes.get(l.getTail()));
//         // System.out.println(nodes.get(l.getHead()));
//         // System.out.println();
//     }

//     void update() {
//         Rectangle source = tail.getBounds();
//         Rectangle distance = head.getBounds();
//         setShortestDistance(source, distance);
//         setSize();

//         int fitX = -(mainPanel.getWidth() / 2);
//         int fitY = -(mainPanel.getHeight() / 4);
//         mainPanel.setLocation((getRight() - getLeft()) / 2 + fitX, (getBtm() - getTop()) / 2 + fitY);
//     }
// }