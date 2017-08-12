package br.unb.cic.mase.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class SimulationScreen extends JPanel implements MouseListener, ComponentListener {

	private static final long serialVersionUID = 1L;
	private static SimulationScreen instance;
	private int[][] spaces;
	private final int UNOCCUPIED = 0;
	private final int POOR = 1;
	private final int POOR_COOPERATIVE = 2;
	private final int MIDDLECLASS = 3;
	private final int MIDDLECLASS_COOPERATIVE = 4;
	private final int RICH = 5;
	private final int RICH_COOPERATIVE = 6;
	private int[] states = { UNOCCUPIED, POOR, POOR_COOPERATIVE, MIDDLECLASS, MIDDLECLASS_COOPERATIVE, RICH, RICH_COOPERATIVE};
	private int columns;
	private int rows;
	private int height;
	private int width;
	private int cellHeight;
	private int cellWidth;

	private SimulationScreen(int length, int width, String initialSpaceSrc) {

	}

	private SimulationScreen() {

		this.addMouseListener(this);
		this.addComponentListener(this);

	}

	public static SimulationScreen getInstance() {
		if (instance == null) {
			instance = new SimulationScreen();
		}
		return instance;
	}

	private void calculateMatrix() {
		Dimension d = this.getSize();
		height = (int) d.getHeight();
		width = (int) d.getWidth();

		cellHeight = height / columns;
		cellWidth = width / rows;

		if (spaces == null) {
			spaces = new int[rows][columns];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					spaces[i][j] = 0;
				}
			}
		}
	}
	
	public void paint(Graphics g) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				
			

				if (spaces != null) {
					if (spaces[i][j] == UNOCCUPIED) {
						g.setColor(new Color(255, 255, 255));
					} else if (spaces[i][j] == POOR) {
						g.setColor(new Color(0, 102, 0));
					} else if (spaces[i][j] == POOR_COOPERATIVE) {
						g.setColor(new Color(0, 255, 0));
					} else if (spaces[i][j] == MIDDLECLASS) {
						g.setColor(new Color(0, 0, 255));
					} else if (spaces[i][j] == MIDDLECLASS_COOPERATIVE) {
						g.setColor(new Color(102, 255, 255));
					} else if (spaces[i][j] == RICH) {
						g.setColor(new Color(255, 0, 0));
					} else if (spaces[i][j] == RICH_COOPERATIVE) {
							g.setColor(new Color(255, 0, 255));
					}
					g.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
				}

				g.setColor(new Color(0, 0, 0));
				g.drawRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
				
			}
		}
	}
	
	public void mouseClicked(MouseEvent arg0) {
		int mouseX = arg0.getX();
		int mouseY = arg0.getY();

		if (cellHeight != 0 && cellWidth != 0) {
			int indexX = mouseX / cellWidth;
			int indexY = mouseY / cellHeight;

			spaces[indexX][indexY]++;
			if (spaces[indexX][indexY] >= states.length) {
				spaces[indexX][indexY] = 0;
			}
			repaint();
		}
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {

	}

	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void componentResized(ComponentEvent arg0) {
		calculateMatrix();
		repaint();
	}

	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setColumnsAndRows(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
	}

	public int[][] getSpaces() {
		return spaces;
	}
	
	public void printSpaces () {
		System.out.println("rows = " + spaces[0].length);
		System.out.println("columns = " + spaces.length);
//		System.out.println("spaces[][] =  {");
		
		System.out.print("grid = [");
		for (int i = 0; i < spaces.length; i++) {
		    //System.out.print("{");
			for (int j = 0; j < spaces[i].length; j++) {
		        System.out.print(spaces[i][j]);
		        if (j < spaces[i].length -1)
		        	System.out.print(",");
			}
		   // System.out.print("}");
		    if (i < spaces.length-1)
		    {
	        	System.out.print(",");
		    }
		}
		System.out.print("]");
		System.out.println();
	}
	public void setSpaces(int i, int j, int type){
		this.spaces[i][j]=type;
	}
	
	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}
}
