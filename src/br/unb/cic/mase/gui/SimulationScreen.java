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
	private final int BAIXA_FI = 1;
	private final int BAIXA_FI_COOP = 2;
	private final int BAIXA_FC = 3;
	private final int BAIXA_FC_COOP = 4;
	private final int BAIXA_MC = 5;
	private final int BAIXA_MC_COOP = 6;
	private final int BAIXA_SC = 7;
	private final int BAIXA_SC_COOP = 8;
	
	private final int MEDIA_FC = 9;
	private final int MEDIA_FC_COOP = 10;
	private final int MEDIA_MC = 11;
	private final int MEDIA_MC_COOP = 12;
	private final int MEDIA_SC = 13;
	private final int MEDIA_SC_COOP = 14;
	
	private final int ALTA_MC = 15;
	private final int ALTA_MC_COOP = 16;
	private final int ALTA_SC = 17;
	private final int ALTA_SC_COOP = 18;
	
	private int[] states = { UNOCCUPIED, BAIXA_FI, BAIXA_FI_COOP, BAIXA_FC, BAIXA_FC_COOP, BAIXA_MC, BAIXA_MC_COOP, BAIXA_SC, BAIXA_SC_COOP,
							 MEDIA_FC, MEDIA_FC_COOP, MEDIA_MC, MEDIA_MC_COOP, MEDIA_SC, MEDIA_SC_COOP,
							 ALTA_MC, ALTA_MC_COOP, ALTA_SC, ALTA_SC_COOP};
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
					}
					// RENDA BAIXA
					else if (spaces[i][j] == BAIXA_FI) {
						g.setColor(new Color(60,0,0));
					} else if (spaces[i][j] == BAIXA_FC) {
						g.setColor(new Color(100,0,0));
					} else if (spaces[i][j] == BAIXA_MC) {
						g.setColor(new Color(160,0,0));
					} else if (spaces[i][j] == BAIXA_SC) {
						g.setColor(new Color(255,0,0));
					} else if (spaces[i][j] == BAIXA_FI_COOP) {
						g.setColor(new Color(0, 40, 0));
					} else if (spaces[i][j] == BAIXA_FC_COOP) {
							g.setColor(new Color(0, 100, 0));
					} else if (spaces[i][j] == BAIXA_MC_COOP) {
						g.setColor(new Color(0, 160, 0));
					} else if (spaces[i][j] == BAIXA_SC_COOP) {
						g.setColor(new Color(0, 255, 0));
					}
					// RENDA MEDIA
					else if (spaces[i][j] == MEDIA_FC) {
						g.setColor(new Color(100, 0, 100));
					} else if (spaces[i][j] == MEDIA_FC_COOP) {
						g.setColor(new Color(0, 0, 110));
					} else if (spaces[i][j] == MEDIA_MC) {
						g.setColor(new Color(150, 0, 150));
					} else if (spaces[i][j] == MEDIA_MC_COOP) {
						g.setColor(new Color(0, 0, 175));	
					} else if (spaces[i][j] == MEDIA_SC) {
						g.setColor(new Color(255, 0, 255));
					} else if (spaces[i][j] == MEDIA_SC_COOP) {
						g.setColor(new Color(0, 0, 255));
					}
					
					// RENDA ALTA
					
					else if (spaces[i][j] == ALTA_MC) {
						g.setColor(new Color(150, 90, 0));
					} else if (spaces[i][j] == ALTA_MC_COOP) {
						g.setColor(new Color(150, 150, 0));
					} else if (spaces[i][j] == ALTA_SC) {
						g.setColor(new Color(255, 150, 0));
						
					} else if (spaces[i][j] == ALTA_SC_COOP) {
						g.setColor(new Color(255, 255, 0));
					}
					
					
					
					g.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
				}

				g.setColor(new Color(0, 0, 0));
				g.drawRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
				
			}
		}
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

	public void setColumnsAndRows(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
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


}
