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
        int appWidth = 1200;
        int appHeight = 700;
        setBounds(100, 100, appWidth, appHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new FrameMap();

        Container contentPane = getContentPane();
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

        // クラスフレーム
        String frameName = ad.getFrameName();
        List<String> slotList = ad.getSrotListName();
        List<String> slotValue = ad.getFrame(frameName);
        ClassFramePanel cfp = new ClassFramePanel(frameName, slotList, slotValue);

        // インスタンスフレーム
        String[] nameList = { "Azurlane", "Dragon-Quest", "Hearts-of-Iron-4", "Monster-Hunter", "Pawapuro-Kun" };
        ArrayList<InstanceFramePanel> ifpList = new ArrayList<>();

        for (String name : nameList) {
            ad.makeInstance(name);
            // List<String> defalt = ad.getFrame(name); // デフォルト値(不要)

            List<String> readData = ad.readTextFile("games/" + name + ".txt");
            ad.addInstanceData(name, readData); // デフォルト値を変更
            List<String> instanceValue = ad.getFrame(name);
            ifpList.add(new InstanceFramePanel(name, cfp, instanceValue));
        }

        setLayout(null);
        int xloc = 200;
        int yloc = 100;

        cfp.setBounds(xloc, yloc, 100, 100);
        xloc -= 70;
        yloc += 200;
        add(cfp);
        for (InstanceFramePanel ifp : ifpList) {
            ifp.setBounds(xloc, yloc, 100, 100);
            xloc += 200;
            add(ifp);

            LinkPanel lp = new LinkPanel(ifp, cfp);
            ifp.addDepartFromMeLinks(lp);
            cfp.addArriveAtMeLinks(lp);

            add(lp);
        }

    }
}

class FramePanel extends JPanel {
    private static int counter = 0;
    private int id;
    private String name;
    private LinkedHashMap<String, String> slots;
    private ArrayList<LinkPanel> departFromMeLinks; // 自分から出ていくリンク
    private ArrayList<LinkPanel> arriveAtMeLinks; // 自分に入ってくるリンク
    private int draggedAtX, draggedAtY;

    FramePanel(String name, List<String> list, List<String> values) {
        id = counter++;
        this.name = name;
        slots = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            slots.put(list.get(i), values.get(i));
        }
        departFromMeLinks = new ArrayList<>();
        arriveAtMeLinks = new ArrayList<>();

        setBackground(Color.ORANGE);

        BevelBorder inborder = new BevelBorder(BevelBorder.LOWERED);
        String label = id + ": " + name;
        TitledBorder border = new TitledBorder(inborder, label, TitledBorder.CENTER, TitledBorder.ABOVE_TOP);

        setLayout(new GridLayout(slots.size(), 2));
        for (Map.Entry<String, String> entry : slots.entrySet()) {
            add(new JLabel(entry.getKey()));
            add(new JTextField(entry.getValue()));
        }

        setBorder(border);

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
    }

    int getId() {
        return id;
    }

    LinkedHashMap<String, String> getSlots() {
        return slots;
    }

    public void addDepartFromMeLinks(LinkPanel theLink) {
        departFromMeLinks.add(theLink);
    }

    public void addArriveAtMeLinks(LinkPanel theLink) {
        arriveAtMeLinks.add(theLink);
    }
}

class ClassFramePanel extends FramePanel {
    private ArrayList<InstanceFramePanel> list;

    ClassFramePanel(String name, List<String> slotLabels, List<String> values) {
        super(name, slotLabels, values);
        list = new ArrayList<>();
    }

    void addInstance(InstanceFramePanel ifp) {
        list.add(ifp);
    }
}

class InstanceFramePanel extends FramePanel {
    private ClassFramePanel par;

    InstanceFramePanel(String name, ClassFramePanel cfp, List<String> values) {
        super(name, new ArrayList(cfp.getSlots().keySet()), values);
        par = cfp;
        cfp.addInstance(this);
    }
}

class LinkPanel extends JPanel {
    private FramePanel child;
    private FramePanel par;
    private Point start;
    private Point end;
    private int margin; // パネルの幅の猶予(パネルが端折れないようにするため)
    private JPanel mainPanel;

    LinkPanel(FramePanel child, FramePanel par) {
        this.child = child;
        this.par = par;
        margin = 100;

        // setBackground(Color.BLACK);
        setOpaque(false); // パネルの透過
        Rectangle source = child.getBounds();
        Rectangle distance = par.getBounds();
        setPoints(source, distance);

        setSize();

        setLayout(null);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0, 128, 128));
        mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JLabel label = new JLabel(child.getId() + " => " + par.getId());

        mainPanel.add(label);

        int mainWidth = 50;
        int mainHeight = 30;
        int fitX = -(mainWidth / 2);
        int fitY = -(mainHeight / 2);
        mainPanel.setBounds((getRight() - getLeft()) / 2 + fitX, (getBtm() - getTop()) / 2 + fitY, mainWidth,
                mainHeight);
        add(mainPanel);
    }

    void setPoints(Rectangle source, Rectangle distance) {
        start = getMidPoint(source, false).getLocation();
        end = getMidPoint(distance, true).getLocation();
    }

    Point getMidPoint(Rectangle r, boolean upside) {
        Point midPoint = new Point();
        if (upside) {
            midPoint.setLocation(r.x + r.width / 2.0, r.y + r.height); // 下の中点
        } else {
            midPoint.setLocation(r.x + r.width / 2.0, r.y); // 上の中点
        }
        return midPoint;
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
        Rectangle source = child.getBounds();
        Rectangle distance = par.getBounds();
        setPoints(source, distance);
        setSize();

        int fitX = -(mainPanel.getWidth() / 2);
        int fitY = -(mainPanel.getHeight() / 2);
        mainPanel.setLocation((getRight() - getLeft()) / 2 + fitX, (getBtm() - getTop()) / 2 + fitY);
    }
}