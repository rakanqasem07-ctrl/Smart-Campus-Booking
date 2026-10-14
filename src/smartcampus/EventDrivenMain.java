package smartcampus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Smart Campus Activity Booking System - Event-Driven version (Java Swing).
 * Every button has its own ActionListener; the program only reacts to user events.
 */
public class EventDrivenMain {

    // ---------- Colour palette ----------
    static final Color NAVY       = new Color(0x1E293B);
    static final Color NAVY_LIGHT = new Color(0x334155);
    static final Color BACKGROUND = new Color(0xF1F5F9);
    static final Color CARD       = Color.WHITE;
    static final Color BORDER     = new Color(0xE2E8F0);
    static final Color TEXT       = new Color(0x0F172A);
    static final Color MUTED      = new Color(0x64748B);
    static final Color TEAL       = new Color(0x0D9488);
    static final Color BLUE       = new Color(0x2563EB);
    static final Color ORANGE     = new Color(0xEA580C);
    static final Color RED        = new Color(0xDC2626);
    static final Color GREEN      = new Color(0x16A34A);

    static final Font TITLE_FONT  = new Font("Segoe UI", Font.BOLD, 22);
    static final Font BODY_FONT   = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font BOLD_FONT   = new Font("Segoe UI", Font.BOLD, 14);
    static final Font SMALL_FONT  = new Font("Segoe UI", Font.PLAIN, 12);

    static ArrayList<Activity> activities = new ArrayList<>();

    static DefaultTableModel tableModel;
    static JTable table;
    static JTextArea output;
    static JLabel statusLabel;

    public static void main(String[] args) {
        activities.add(new AcademicActivity("AI Workshop", "Dr. Ahmad", 101, 20));
        activities.add(new SportActivity("Football", "Coach Samir", "Stadium", 30));
        activities.add(new AcademicActivity("Career Day", "Dr. Sara", 202, 50));
        activities.add(new AcademicActivity("Robotics Club", "Dr. Khalid", 303, 15));

        // Swing components must be created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                buildGui();
            }
        });
    }

    // =====================================================================
    //  GUI construction
    // =====================================================================
    static void buildGui() {
        JFrame frame = new JFrame("Smart Campus Activity Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 640);
        frame.setMinimumSize(new Dimension(860, 560));
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        frame.setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildContent(), BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);

        refreshTable();
        setStatus("Welcome! Choose an action from the menu on the left.", MUTED);

        frame.setVisible(true);   // show the window only after everything is added
    }

    static JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Smart Campus");
        title.setFont(TITLE_FONT);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Activity Booking System");
        subtitle.setFont(BODY_FONT);
        subtitle.setForeground(new Color(0x94A3B8));

        JPanel texts = new JPanel(new GridLayout(2, 1));
        texts.setOpaque(false);
        texts.add(title);
        texts.add(subtitle);
        header.add(texts, BorderLayout.WEST);
        return header;
    }

    static JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(NAVY_LIGHT);
        sidebar.setBorder(new EmptyBorder(20, 16, 20, 16));
        sidebar.setPreferredSize(new Dimension(230, 0));

        JLabel menu = new JLabel("MENU");
        menu.setFont(new Font("Segoe UI", Font.BOLD, 11));
        menu.setForeground(new Color(0x94A3B8));
        menu.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.setBorder(new EmptyBorder(0, 4, 10, 0));
        sidebar.add(menu);

        RoundedButton display    = new RoundedButton("Display Activities", BLUE);
        RoundedButton register   = new RoundedButton("Register Student", TEAL);
        RoundedButton unregister = new RoundedButton("Unregister Student", RED);
        RoundedButton view       = new RoundedButton("View Students", BLUE);
        RoundedButton sort       = new RoundedButton("Sort By Popularity", ORANGE);
        RoundedButton report     = new RoundedButton("Activity Report", BLUE);

        RoundedButton[] buttons = {display, register, unregister, view, sort, report};
        for (RoundedButton b : buttons) {
            sidebar.add(b);
            sidebar.add(Box.createVerticalStrut(10));
        }

        // ---------------- Event handlers ----------------
        display.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTable();
                StringBuilder s = new StringBuilder("Activities:\n\n");
                for (int i = 0; i < activities.size(); i++) {
                    Activity a = activities.get(i);
                    s.append(String.format("%d. [%s] %s  (%d/%d)%n",
                            i + 1, typeOf(a), a.getName(), a.getBooked(), a.getCapacity()));
                }
                output.setText(s.toString());
                setStatus("Showing all " + activities.size() + " activities.", BLUE);
            }
        });

        register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = askStudentName("Register Student");
                if (name == null) return;

                int index = chooseActivity("Register Student");
                if (index < 0) return;

                Activity a = activities.get(index);
                if (a.getStudents().contains(name)) {
                    setStatus(name + " is already registered in " + a.getName() + ".", ORANGE);
                } else if (!a.register(name)) {
                    setStatus("Activity Full: " + a.getName() + " has no seats left.", RED);
                } else {
                    setStatus("Registered Successfully: " + name + " → " + a.getName(), GREEN);
                }
                refreshTable();
            }
        });

        unregister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = askStudentName("Unregister Student");
                if (name == null) return;

                int index = chooseActivity("Unregister Student");
                if (index < 0) return;

                Activity a = activities.get(index);
                if (!a.unregister(name)) {
                    setStatus("Student Not Found: " + name + " is not in " + a.getName() + ".", RED);
                } else {
                    setStatus("Unregistered Successfully: " + name + " removed from " + a.getName(), GREEN);
                }
                refreshTable();
            }
        });

        view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = chooseActivity("View Students");
                if (index < 0) return;

                Activity a = activities.get(index);
                StringBuilder s = new StringBuilder("Students in " + a.getName() + ":\n\n");
                if (a.getBooked() == 0) {
                    s.append("No Students Registered");
                } else {
                    for (int i = 0; i < a.getStudents().size(); i++) {
                        s.append(i + 1).append(". ").append(a.getStudents().get(i)).append("\n");
                    }
                }
                output.setText(s.toString());
                setStatus(a.getName() + ": " + a.getBooked() + " student(s) registered.", BLUE);
            }
        });

        sort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Selection Sort (descending by number of booked students)
                int n = activities.size();
                for (int i = 0; i < n - 1; i++) {
                    int max = i;
                    for (int j = i + 1; j < n; j++) {
                        if (activities.get(j).getBooked() > activities.get(max).getBooked())
                            max = j;
                    }
                    Activity tmp = activities.get(i);
                    activities.set(i, activities.get(max));
                    activities.set(max, tmp);
                }
                refreshTable();

                StringBuilder s = new StringBuilder("Activities Sorted By Popularity:\n\n");
                for (int i = 0; i < activities.size(); i++) {
                    Activity a = activities.get(i);
                    s.append(String.format("%d. %s  (%d/%d)%n",
                            i + 1, a.getName(), a.getBooked(), a.getCapacity()));
                }
                output.setText(s.toString());
                setStatus("Activities sorted using Selection Sort.", ORANGE);
            }
        });

        report.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder s = new StringBuilder("Activity Report:\n\n");
                int total = 0;
                for (Activity a : activities) {
                    int percent = a.getBooked() * 100 / a.getCapacity();
                    s.append(String.format("%-15s %3d students   (%d%% full)%n",
                            a.getName(), a.getBooked(), percent));
                    total += a.getBooked();
                }
                s.append("\nTotal registrations: ").append(total);
                output.setText(s.toString());
                setStatus("Report generated.", BLUE);
            }
        });

        return sidebar;
    }

    static JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(20, 20, 12, 20));

        // ----- Activities table -----
        String[] columns = {"#", "Type", "Activity", "Supervisor", "Location", "Booked", "Capacity"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(BODY_FONT);
        table.setRowHeight(38);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(0xDBEAFE));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(BOLD_FONT);
        header.setBackground(new Color(0xF8FAFC));
        header.setForeground(MUTED);
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer padded = new DefaultTableCellRenderer();
        padded.setBorder(new EmptyBorder(0, 10, 0, 10));
        for (int i = 0; i < columns.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(padded);
        }
        table.getColumnModel().getColumn(1).setCellRenderer(new TypeRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new ProgressRenderer());

        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setMaxWidth(80);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        content.add(card("Upcoming Activities", tableScroll), BorderLayout.CENTER);

        // ----- Output area -----
        output = new JTextArea(8, 20);
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        output.setForeground(TEXT);
        output.setBorder(new EmptyBorder(10, 12, 10, 12));
        output.setText("Results will appear here.");

        JScrollPane outScroll = new JScrollPane(output);
        outScroll.setBorder(BorderFactory.createEmptyBorder());
        JPanel outCard = card("Output", outScroll);
        outCard.setPreferredSize(new Dimension(0, 210));
        content.add(outCard, BorderLayout.SOUTH);

        return content;
    }

    static JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(8, 20, 8, 20)));
        statusLabel = new JLabel(" ");
        statusLabel.setFont(BOLD_FONT);
        bar.add(statusLabel, BorderLayout.WEST);
        return bar;
    }

    /** White rounded "card" with a small title on top. */
    static JPanel card(String title, JComponent body) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(TEXT);
        label.setBorder(new EmptyBorder(0, 4, 10, 0));

        card.add(label, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    // =====================================================================
    //  Helpers
    // =====================================================================
    static void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < activities.size(); i++) {
            Activity a = activities.get(i);
            String supervisor, location;
            if (a instanceof AcademicActivity) {
                AcademicActivity ac = (AcademicActivity) a;
                supervisor = ac.getInstructor();
                location = "Room " + ac.getRoomNumber();
            } else if (a instanceof SportActivity) {
                SportActivity sp = (SportActivity) a;
                supervisor = sp.getCoach();
                location = sp.getLocation();
            } else {
                supervisor = "-";
                location = "-";
            }
            tableModel.addRow(new Object[]{i + 1, typeOf(a), a.getName(), supervisor, location,
                    a, a.getCapacity()});
        }
    }

    static String typeOf(Activity a) {
        if (a instanceof AcademicActivity) return "Academic";
        if (a instanceof SportActivity) return "Sport";
        return "General";
    }

    static void setStatus(String message, Color color) {
        statusLabel.setText("●  " + message);
        statusLabel.setForeground(color);
    }

    /** Returns a trimmed student name, or null if the user cancelled / left it empty. */
    static String askStudentName(String title) {
        String name = JOptionPane.showInputDialog(null, "Student Name:", title, JOptionPane.PLAIN_MESSAGE);
        if (name == null) return null;
        name = name.trim();
        if (name.isEmpty()) {
            setStatus("Please enter a student name.", RED);
            return null;
        }
        return name;
    }

    /** Lets the user pick an activity. The selected table row is used as the default. Returns -1 if cancelled. */
    static int chooseActivity(String title) {
        String[] names = new String[activities.size()];
        for (int i = 0; i < activities.size(); i++) {
            names[i] = activities.get(i).getName();
        }
        int selected = table.getSelectedRow();
        String def = names[selected >= 0 ? selected : 0];

        String chosen = (String) JOptionPane.showInputDialog(null, "Choose Activity:", title,
                JOptionPane.PLAIN_MESSAGE, null, names, def);
        if (chosen == null) return -1;

        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(chosen)) return i;
        }
        return -1;
    }

    // =====================================================================
    //  Custom components
    // =====================================================================

    /** Flat rounded button with a hover effect. */
    static class RoundedButton extends JButton {
        private final Color base;
        private boolean hover = false;

        RoundedButton(String text, Color base) {
            super(text);
            this.base = base;
            setFont(BOLD_FONT);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(0, 16, 0, 16));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            setPreferredSize(new Dimension(198, 42));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = base;
            if (getModel().isPressed()) c = base.darker();
            else if (hover) c = base.brighter();
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Coloured text for the activity type column. */
    static class TypeRenderer extends DefaultTableCellRenderer {
        TypeRenderer() {
            setBorder(new EmptyBorder(0, 10, 0, 10));
        }

        public Component getTableCellRendererComponent(JTable t, Object value, boolean sel,
                                                       boolean focus, int row, int col) {
            super.getTableCellRendererComponent(t, value, sel, false, row, col);
            setBorder(new EmptyBorder(0, 10, 0, 10));
            setFont(BOLD_FONT);
            setForeground("Sport".equals(value) ? ORANGE : TEAL);
            return this;
        }
    }

    /** Shows booked/capacity as a coloured progress bar (green → orange → red). */
    static class ProgressRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JProgressBar bar = new JProgressBar();

        ProgressRenderer() {
            super(new BorderLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));
            bar.setStringPainted(true);
            bar.setFont(SMALL_FONT);
            bar.setBorderPainted(false);
            bar.setBackground(new Color(0xE2E8F0));
            bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
                protected Color getSelectionForeground() { return Color.WHITE; }
                protected Color getSelectionBackground() { return TEXT; }
            });
            add(bar, BorderLayout.CENTER);
        }

        public Component getTableCellRendererComponent(JTable t, Object value, boolean sel,
                                                       boolean focus, int row, int col) {
            Activity a = (Activity) value;
            bar.setMaximum(a.getCapacity());
            bar.setValue(a.getBooked());
            bar.setString(a.getBooked() + " / " + a.getCapacity());

            double ratio = (double) a.getBooked() / a.getCapacity();
            if (ratio >= 1.0)      bar.setForeground(RED);
            else if (ratio >= 0.75) bar.setForeground(ORANGE);
            else                    bar.setForeground(GREEN);

            setBackground(sel ? t.getSelectionBackground() : t.getBackground());
            return this;
        }
    }
}
