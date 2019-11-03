import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RelationGUI extends JFrame implements ActionListener {
    public static void main(String args[]) {
        RelationGUI frame = new RelationGUI("関係図");
        frame.setVisible(true);
    }

    RelationGUI(String title) {

        setTitle(title);
        int appWidth = 1200;
        int appHeight = 700;
        setBounds(100, 100, appWidth, appHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // JPanel menuPanel = new JPanel();

        JPanel mainPanel = new RelationMap();

        Container contentPane = getContentPane();
        // contentPane.add(menuPanel, BorderLayout.EAST);
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
    }
}

class RelationMap extends JPanel{
    private SemanticNet sn;
    private AccessData ad;
    private Map<Node, NodePanel> nodes;
    // private Map<Link, LinkPanel> links;
    private NodePanel dragPanel;

    RelationMap() {
        sn = new SemanticNet();
        ad = new AccessData(sn);
        ad.start("members/yuasa.txt");
        nodes = new HashMap<>();
        ArrayList<Node> nodeList = ad.getNodes();
        for (Node n : nodeList) {
            nodes.put(n, new NodePanel(n));
            System.out.println(n);
        }
        // links = new HashMap<>();
        // ArrayList<Link> linkList = ad.getLinks();
        // for (Link l : linkList) {
        // links.put(l, new linkPanel(l));
        // }

        setLayout(null);
        int xloc = 0;
        int yloc = 0;
        for (NodePanel np : nodes.values()) {
            xloc += 20;
            yloc += 30;
            np.setLocation(xloc, yloc);
            
            add(np);
        }
        // for(LinkPanel lp : links.values()) {
        // mainPanel.add(lp);
        // }
    }
}

class NodePanel extends JPanel {
    private static int counter = 0;
    private int id;
    private Node node;
    private int draggedAtX, draggedAtY;

    NodePanel(Node node) {
        id = counter++;
        this.node = node;
        setSize(180, 30);
        setBackground(Color.ORANGE);
        setBorder(new BevelBorder(BevelBorder.RAISED));

        JLabel label = new JLabel(id + ": " + node.getName());

        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                draggedAtX = e.getX();
                draggedAtY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e){
                // System.out.println(e.getX() +", " + draggedAtX + "," + getLocation().x);
                setLocation(e.getX() - draggedAtX + getLocation().x, e.getY() - draggedAtY + getLocation().y);
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
}

// class LinkPanel extends JPanel {
// private Link link;
// private Point start;
// private Point end;
// private int grace; // パネルの幅の猶予

// LinkPanel(Link link) {
// this.link = link;
// grace = 50;

// setOpaque(false);
// Rectangle source = par.getBounds();
// Rectangle distance = child.getBounds();
// setShortestDistance(source, distance);

// SpringLayout layout = new SpringLayout();
// setLayout(layout);

// JPanel p = new JPanel(new GridLayout(2, 1));
// p.setAlignmentY(Component.CENTER_ALIGNMENT);
// p.setBackground(new Color(0,128,128));
// p.setBorder(new BevelBorder(BevelBorder.LOWERED));
// JLabel label = new JLabel(par.getId() + " to " + child.getId());
// model = new SpinnerNumberModel(par.getNode().getCost(child.getNode()), 0,
// 9999, 1);
// JSpinner spinner = new JSpinner(model);
// spinner.setPreferredSize(new Dimension(40, 25)); // graceは各座標の２倍以上にすること

// p.add(label);
// p.add(spinner);

// layout.putConstraint(SpringLayout.WEST, p, (getRight() - getLeft()) / 2 - 20,
// SpringLayout.WEST, this);
// layout.putConstraint(SpringLayout.NORTH, p, (getBtm() - getTop()) / 2 - 20,
// SpringLayout.NORTH, this);
// add(p);
// }

// @Override
// public void paintComponent(Graphics g) {
// super.paintComponent(g);
// paintArrows(g);
// }

// void paintArrows(Graphics g) {
// int fromX = start.x;
// int fromY = start.y;
// int toX = end.x;
// int toY = end.y;

// int constX = getLeft();
// int constY = getTop();
// g.setColor(Color.BLUE);
// g.drawLine(fromX - constX, fromY - constY, toX - constX, toY - constY);
// }

// void setShortestDistance(Rectangle source, Rectangle distance) {
// Point[] fromMidPoints = getMidPoints(source);
// Point[] toMidPoints = getMidPoints(distance);

// double min = Double.MAX_VALUE;
// for (int i = 0; i < 4; i++) {
// Point from = fromMidPoints[i].getLocation();
// for (int j = 0; j < 4; j++) {
// Point to = toMidPoints[j].getLocation();
// double value = (from.getX() - to.getX()) * (from.getX() - to.getX())
// + (from.getY() - to.getY()) * (from.getY() - to.getY());
// if (value < min) {
// min = value;
// start = from;
// end = to;
// }
// }
// }
// }

// Point[] getMidPoints(Rectangle r) {
// Point[] midPoints = new Point[4];
// for (int i = 0; i < midPoints.length; i++) {
// midPoints[i] = new Point();
// }
// midPoints[0].setLocation(r.x + r.width / 2.0, r.y); // 上の中点
// midPoints[1].setLocation(r.x + r.width, r.y + r.height / 2.0); // 右の中点
// midPoints[2].setLocation(r.x + r.width / 2.0, r.y + r.height / 2.0); // 下の中点
// midPoints[3].setLocation(r.x, r.y + r.height / 2.0); // 左の中点
// return midPoints;
// }

// Point getStart() {
// return start;
// }

// Point getEnd() {
// return end;
// }

// int getLeft() {
// return execHor(true) - grace;
// }

// int getRight() {
// return execHor(false) + grace;
// }

// int getTop() {
// return execVer(true) - grace;
// }

// int getBtm() {
// return execVer(false) + grace;
// }

// int execHor(boolean b) {
// int left = start.x;
// int right = end.x;
// if (left > right) {
// int tmp = left;
// left = right;
// right = tmp;
// }
// if(b) {
// return left;
// } else {
// return right;
// }
// }

// int execVer(boolean b) {
// int top = start.y;
// int btm = end.y;
// if (top > btm) {
// int tmp = top;
// top = btm;
// btm = tmp;
// }
// if(b) {
// return top;
// } else {
// return btm;
// }
// }

// SpinnerNumberModel getModel() {
// return model;
// }

// void update(int value) {
// par.getNode().remakeChild(child.getNode(), value);
// }

// boolean forRepaint(Node to, Node from) {
// pass = (child.getNode() == to && par.getNode() == from);
// return pass;
// }
// }