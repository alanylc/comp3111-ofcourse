package ofcoursegui;

import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.factories.FormFactory;

import java.awt.Color;
import java.awt.Dimension;

public class TimeTableGUI {
	private static int rows = 27;
	private static int cols = 6;
	
	private JLabel[][] jlabelarray = new JLabel[cols][rows];
	private HashMap<String, ArrayList<JLabel>> filledSlots = new HashMap<String, ArrayList<JLabel>>();

	public static String[] weekDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

	public TimeTableGUI() {
		
	}
	
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
		//add filled slots to list
		if (filledSlots.containsKey(key)) {
			ArrayList<JLabel> old = filledSlots.get(key);
			old.addAll(labels);
			filledSlots.put(key, old);
		}
		else filledSlots.put(key, labels);
	}
	
	//Whether the slots are occupied are not managed in this class. Only the visual is changed.
	public void unfillSlots(String key) {
		ArrayList<JLabel> labels = filledSlots.get(key);
		if (labels == null || labels.size() == 0) return;
		for(JLabel l : labels) {
			l.setText("");
			l.setOpaque(false);
		}
		filledSlots.remove(key);
	}
	
	public static Color getRandomBgColor() {
		return Color.CYAN;
	}
	
	public static Dimension getNewStandardCellDimensionInstance() {
		return new Dimension(100, 18);
	}

	public JPanel initilizeGUIComponent() {
		JPanel panel_2 = new JPanel();
		ColumnSpec[] cs = new ColumnSpec[cols + 1];
		for (int i = 0; i < cols + 1; i ++) {
			cs[i] = new ColumnSpec(Sizes.pixel(100));
		}
		RowSpec[] rs = new RowSpec[rows + 1];
		for(int i = 0; i < rows + 1; i++) {
			rs[i] = FormFactory.DEFAULT_ROWSPEC;
		}
		
		FormLayout fl_panel  = new FormLayout(cs, rs);
		fl_panel.setHonorsVisibility(false);
		panel_2.setLayout(fl_panel);
		

		JLabel lblTL = new JLabel("");
		panel_2.add(lblTL, "1, 1");
		lblTL.setBorder(new LineBorder(new Color(99, 130, 191)));
		lblTL.setMinimumSize(getNewStandardCellDimensionInstance());
		lblTL.setMaximumSize(getNewStandardCellDimensionInstance());
		lblTL.setPreferredSize(getNewStandardCellDimensionInstance());

		// Add the week days slots on the top row
		for (int i = 0; i < 6; i++) {
			JLabel label = new JLabel(weekDays[i]);
			panel_2.add(label, Integer.toString((2 + i)) + ", 1");
			label.setBorder(new LineBorder(new Color(99, 130, 191)));
			label.setPreferredSize(getNewStandardCellDimensionInstance());
			label.setMinimumSize(getNewStandardCellDimensionInstance());
			label.setMaximumSize(getNewStandardCellDimensionInstance());

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

			labelTime.setPreferredSize(getNewStandardCellDimensionInstance());
			labelTime.setMinimumSize(getNewStandardCellDimensionInstance());
			labelTime.setMaximumSize(getNewStandardCellDimensionInstance());
			labelTime.setBorder(new LineBorder(new Color(99, 130, 191)));

			panel_2.add(labelTime, "1, " + i);

		}

		// Add each cell as a JLabel
		for (int r = 2; r <= TimeTableGUI.rows + 1; r++) {
			for (int c = 2; c <= TimeTableGUI.cols + 1; c++) {
				this.jlabelarray[c - 2][r - 2] = new JLabel(
						Integer.toString((c - 1) * 100 + (r - 2)));
				this.jlabelarray[c - 2][r - 2].setText("");
				String constraint = c + ", " + r;
				panel_2.add(this.jlabelarray[c - 2][r - 2], constraint);
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
				
			}
		}

		return panel_2;
	}
	
	public static TimeTableGUI createNewTimetableGUIPanel() {
		TimeTableGUI newPage = new TimeTableGUI();
		MainWindow.tabpage.add(newPage.initilizeGUIComponent());
		return newPage;
	}
}
