package ofcoursegui;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.factories.FormFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

public class TimeTableGUI extends JPanel{
	private static int rows = 28;
	private static int cols = 6;
	
	private JLabel[][] jlabelarray = new JLabel[cols][rows];
	//private HashMap<String, ArrayList<JLabel>> filledSlots_old = new HashMap<String, ArrayList<JLabel>>();
	private HashMap<String, ArrayList<ArrayList<JLabel>>> filledSlots_new = new HashMap<String, ArrayList<ArrayList<JLabel>>>();
	
	private ArrayList<CourseSelectListener> courseSelectListeners = new ArrayList<CourseSelectListener>();
	
	private MouseListener mouselistener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			for(Entry<String, ArrayList<ArrayList<JLabel>>> kvpair : filledSlots_new.entrySet()) {
				for(ArrayList<JLabel> lession : kvpair.getValue()){
					for(JLabel slot : lession) {
						if(e.getSource() == slot) {
							for(CourseSelectListener csl : courseSelectListeners) {
								csl.courseSelected(kvpair.getKey());
							}
						}
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	
	//private JPanel parentPanel = new JPanel();

	public static String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

	public TimeTableGUI() {
		initilizeGUIComponent();
	}
	
	public void addcourseSelectListener(CourseSelectListener listener) {
		courseSelectListeners.add(listener);
	}
	
	/*public JPanel getParentPanel() {
		return parentPanel;
	}*/

	public JLabel[][] getSlots() {
		return jlabelarray;
	}
	
	public JLabel getSlot(int slotNumber) {
		//detect invalid number
		if (slotNumber < 0) return null;
		return getSlot(slotNumber / 100, slotNumber % 100); 
	}
	
	public JLabel getSlot(int weekDay, int timeID) {
		//detect invalid number
		if (weekDay < 0 || timeID < 0 || weekDay >= cols || timeID >= rows) return null;
		//array index is zero-based, but weekDay counts from 1 to 7
		return getSlots()[weekDay - 1][timeID]; 
	}
	
	//Whether the slots are occupied are not managed in this class. Only the visual is changed.
	//Only start slot and end slot are specified, so the slots filled are continuous.
	//TODO: Slots specified are allowed to run across columns, and the text is written on each continuous section in each column
	//Each element of the text array is one line. Each will be written in one slot. 
	//The lines are written in the middle slots of the continuous slots.
	//If there are not enough slots for the lines, the last slots will hold all the remaining lines.
	//Therefore it is the user's responsibility not to write too much.
	//If the provided key already exists, those slots are untouched, and the provided ones are filled and added to same key.
	public void fillSlots(int startSlot, int endSlot, Color color, String[] text, String key) throws Exception {
		
		int maxTime = rows - 1;
		int maxDay = cols - 1;
		//find all target slots
		//invalid slots are null
		ArrayList<JLabel> labels = new ArrayList<JLabel>();
		for (int i = startSlot; i <= endSlot; i++) {
			if (i < 0 || i > maxDay * 100 + maxTime) 
				throw new Exception("Slot number must be between." + 100 + " and " + (maxDay * 100 + maxTime));
			JLabel label = getSlot(i);
			if (label == null) throw new Exception("Invalid slot number.");
			labels.add(label);
		}
		if (labels.size() <= 0) return;
		//fill color
		for (JLabel l : labels) {
			l.setBorder(new LineBorder(color));
			l.setBackground(color);
			l.setOpaque(true);
		}
		int lineCount = text.length;
		//determine where to put the text
		if (lineCount <= labels.size()) {
			JLabel[] textLabels = new JLabel[lineCount];
			int start = (labels.size() - lineCount) / 2;
			textLabels = labels.subList(start, start + lineCount).toArray(textLabels);
			for(int i = 0 ; i < lineCount; i++) {
				textLabels[i].setText(text[i]);
			}
		}
		//add click listener to each filled slot
		for(JLabel l : labels) {
			l.addMouseListener(mouselistener);
		}
		//add filled slots to list
		if (filledSlots_new.containsKey(key)) {
			ArrayList<ArrayList<JLabel>> old = filledSlots_new.get(key);
			old.add(labels);
			filledSlots_new.put(key, old);
		}
		else {
			ArrayList<ArrayList<JLabel>> newlist = new ArrayList<ArrayList<JLabel>>();
			newlist.add(labels);
			filledSlots_new.put(key, newlist);
		}
	}
	
	//Whether the slots are occupied are not managed in this class. Only the visual is changed.
	public void unfillSlots(String key) {
		ArrayList<ArrayList<JLabel>> labels = filledSlots_new.get(key);
		if (labels == null || labels.size() == 0) return;
		for(ArrayList<JLabel> lession : labels) {
			for(JLabel l : lession) {
				l.setText("");
				l.setBackground(Color.WHITE);
				l.setOpaque(true);
				//remove any click listener
				l.removeMouseListener(mouselistener);
			}
		}
		filledSlots_new.remove(key);
	}
	
	//Whether the slots are selected are not managed in this class. Only the visual is changed.
	public void selectSlots(String key) {
		ArrayList<ArrayList<JLabel>> labels = filledSlots_new.get(key);
		for(ArrayList<JLabel> lession : labels) {
			for(int i = 0; i < lession.size(); i++) {
				//Other than those needed, remaining should be background color to "hide" itself

				/*lblTL.setBorder(new LineBorder(new Color(99, 130, 191)));
				Border oldBorder = lblTL.getBorder();
				Border redBorder = BorderFactory.createMatteBorder(0, 0, 0, 5, Color.RED);
				Border newBorder = BorderFactory.createCompoundBorder(redBorder, oldBorder);
				lblTL.setBorder(newBorder);*/
				JLabel target = lession.get(i);
				target.setBorder(new LineBorder(target.getBackground()));
				Border oldBorder = target.getBorder();
				if(i == 0) { //border is top , left and right.
					Border neededBorder = BorderFactory.createMatteBorder(2, 2, 0, 2, Color.RED);
					Border newBorder = BorderFactory.createCompoundBorder(neededBorder, oldBorder);
					target.setBorder(newBorder);
				}
				else if(i == lession.size() - 1) { //border is down , left and right.
					Border neededBorder = BorderFactory.createMatteBorder(0, 2, 2, 2, Color.RED);
					Border newBorder = BorderFactory.createCompoundBorder(neededBorder, oldBorder);
					target.setBorder(newBorder);
				}
				else { //border is left and right
					Border neededBorder = BorderFactory.createMatteBorder(0, 2, 0, 2, Color.RED);
					Border newBorder = BorderFactory.createCompoundBorder(neededBorder, oldBorder);
					target.setBorder(newBorder);
				}
			}
		}
	}
	
	//Whether the slots are selected are not managed in this class. Only the visual is changed.
	public void unselectSlots(String key) {
		
	}
	
	private static Color[] preset = {new Color(167, 252, 250),
			new Color(167, 237, 71),
			new Color(254, 218, 152),
			new Color(176, 176, 255),
			new Color(243, 205, 186),
			new Color(255, 204, 255),
			new Color(204, 255, 153),
			new Color(255, 254, 206),
			new Color(60, 217, 173),
			new Color(252, 117, 255),
			new Color(130, 244, 190),
			new Color(225, 254, 214),
			new Color(255, 204, 255),
			new Color(253, 157, 191),
			new Color(252, 255, 191)};
	private static int presetUsed = 0;
	
	public static Color getRandomBgColor() {
		Random r = new Random();
		return preset[r.nextInt(preset.length)];
	}
	
	public static Dimension getNewStandardCellDimensionInstance() {
		return new Dimension(100, 18);
	}

	public TimeTableGUI initilizeGUIComponent() {
		ColumnSpec[] cs = new ColumnSpec[cols + 1];
		for (int i = 0; i < cols + 1; i ++) {
			cs[i] = new ColumnSpec(Sizes.pixel(100));
		}
		RowSpec[] rs = new RowSpec[rows + 1];
		for(int i = 0; i < rows + 1; i++) {
			rs[i] = FormFactory.DEFAULT_ROWSPEC;
		}
		
		FormLayout fl_panel  = new FormLayout(cs, rs);

		/*FormLayout fl_panel_new  = new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("100px"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,});*/
		fl_panel.setHonorsVisibility(false);
		//parentPanel.setLayout(fl_panel);
		setLayout(fl_panel);

		//Top-left cell, which is a placeholder
		JLabel lblTL = new JLabel("");
		//parentPanel.add(lblTL, "1, 1");
		add(lblTL, "1, 1");
		
		lblTL.setBackground(Color.WHITE);
		lblTL.setOpaque(true);
		lblTL.setMinimumSize(getNewStandardCellDimensionInstance());
		lblTL.setMaximumSize(getNewStandardCellDimensionInstance());
		lblTL.setPreferredSize(getNewStandardCellDimensionInstance());

		// Add the week days slots on the top row
		for (int i = 0; i < 6; i++) {
			//Create the weekday table with the text
			JLabel label = new JLabel(weekDays[i]);
			//parentPanel.add(label, Integer.toString((2 + i)) + ", 1");
			add(label, Integer.toString((2 + i)) + ", 1");
			label.setBorder(new LineBorder(new Color(99, 130, 191)));
			label.setPreferredSize(getNewStandardCellDimensionInstance());
			label.setMinimumSize(getNewStandardCellDimensionInstance());
			label.setMaximumSize(getNewStandardCellDimensionInstance());
			label.setBackground(Color.WHITE);
			label.setOpaque(true);
			label.setHorizontalAlignment(SwingConstants.CENTER);
		}

		// Add the time periods slots on the left
		java.text.DecimalFormat df = new java.text.DecimalFormat("0000");
		int a = 830; // the first time slot will then be 0900-0930,
		int b = 900; // 30 min will be added before showing each time
		//FormLayout counts from 1
		for (int i = 2; i <= TimeTableGUI.rows + 1; i++) {
			JLabel labelTime = new JLabel("");
			//check for 60 mins, which is 1 hour
			a += 30;
			if (a - a / 100 * 100 == 60)
				a = a - 60 + 100;
			b += 30;
			if (b - b / 100 * 100 == 60)
				b = b - 60 + 100;
			String s = df.format(a) + "-" + df.format(b);
			labelTime.setText(s);
			labelTime.setBackground(Color.WHITE);
			labelTime.setOpaque(true);
			labelTime.setPreferredSize(getNewStandardCellDimensionInstance());
			labelTime.setMinimumSize(getNewStandardCellDimensionInstance());
			labelTime.setMaximumSize(getNewStandardCellDimensionInstance());
			labelTime.setBorder(new LineBorder(new Color(99, 130, 191)));

			//parentPanel.add(labelTime, "1, " + i);
			add(labelTime, "1, " + i);

		}

		// Add each cell as a JLabel
		for (int r = 2; r <= TimeTableGUI.rows + 1; r++) {
			for (int c = 2; c <= TimeTableGUI.cols + 1; c++) {
				this.jlabelarray[c - 2][r - 2] = new JLabel(
						Integer.toString((c - 1) * 100 + (r - 2)));
				this.jlabelarray[c - 2][r - 2].setText("");
				String constraint = c + ", " + r;
				//parentPanel.add(this.jlabelarray[c - 2][r - 2], constraint);
				add(this.jlabelarray[c - 2][r - 2], constraint);
				this.jlabelarray[c - 2][r - 2]
						.setMaximumSize(getNewStandardCellDimensionInstance());
				this.jlabelarray[c - 2][r - 2]
						.setMinimumSize(getNewStandardCellDimensionInstance());
				this.jlabelarray[c - 2][r - 2]
						.setPreferredSize(getNewStandardCellDimensionInstance());
				this.jlabelarray[c - 2][r - 2]
						.setSize(getNewStandardCellDimensionInstance());
				this.jlabelarray[c - 2][r - 2].setBorder(new LineBorder(new Color(239, 228, 176)));
				this.jlabelarray[c - 2][r - 2].setHorizontalAlignment(SwingConstants.CENTER);

				this.jlabelarray[c - 2][r - 2].setBackground(Color.WHITE);
				this.jlabelarray[c - 2][r - 2].setOpaque(true);
			}
		}

		return this;
	}
}
