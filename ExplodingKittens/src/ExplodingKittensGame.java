/*
 * Nicholas Mair
 * Card Game Collection #1
 * Exploding Kittens
 * Started January 26th, 2019
 * 
 * IDs: STF=SeeTheFuture, FVR=Favor, SKP=Skip, ATK=Attack, EXP=Exploding Kitten, SHF=Shuffle, NPE=Nope, DFS=Defuse, HPC=HairyPotatoCat, BRD=BeardCat
 * RRC=RainbowRalphingCat, TCT=Tacocat, CTM=Cattermelon
 * 
 * What's Left?
 * Error messages for using Defuse and Nope too early
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
//import javax.swing.Timer;

//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.MouseEvent; //Mouse and Button Listeners
import java.awt.event.MouseListener;

public class ExplodingKittensGame extends JPanel implements MouseListener{
	private static final long serialVersionUID = 1L;
	public static Image image; //Image Variable
	public Color explodingRed = new Color(97, 16, 12); //Colours
	public Color backgroundBlue = new Color(39, 170, 225);
	
	public static List<String> Cards = new ArrayList<String>(); //All of our String Lists
	public static List<String> HandP1 = new ArrayList<String>();
	public static List<String> HandP2 = new ArrayList<String>();
	public static List<String> Discard = new ArrayList<String>();
	public static List<String> Shuff = new ArrayList<String>();
	public static List<String> DFSEXP = new ArrayList<String>();
	public static List<String> Storage = new ArrayList<String>();
	
	public static List<Integer> CardsPos1 = new ArrayList<Integer>(); //All of our Integer Lists
	public static List<Integer> CardsPos2 = new ArrayList<Integer>();
	
	public static ImageObserver IO;//Image Observer for implementing images
	
	public static boolean win = false; //Booleans
	public static boolean first = true;
	public static boolean draw = false;
	public static boolean changeTurns = false;
	public static boolean black = false;
	public static boolean see = false;
	public static boolean boom = false;
	public static boolean playCard = false;
	public static boolean confirmBox = false;
	public static boolean bombPlant = false;
	public static boolean favor = false;
	public static boolean swap = false;
	public static boolean exception = false;
	public static boolean disable = false;
	
	public static Random rn = new Random(); //Random Number Generator
	
	public static String lastAction = "null"; //Strings
	public static String activeCard = "null";
	
	public static int initialDeal; //Integers
	public static int players=2;
	public static int mouseX = 0;
	public static int mouseY = 0;
	public static int Turn = 1;
	public static int cardSelect = 0;
	public static int reTurn = 0; //If 0 - no attack card, If 2 - attack card played, If 1 - attack card active
	
	public ExplodingKittensGame() {
		addMouseListener(this); //Required for mouse to work
	}
	
	public void paintComponent(Graphics g) {
		if (first) {
			Discard.add("null"); //Resolves index errors
			g.setFont(new Font("serif",Font.BOLD,18));
			askPack(g);
			TakeOutDFSEXP();
			GiveDefuseCards();
			Shuffle(10);
			Deal(players,initialDeal);
			Cards.addAll(DFSEXP);
			DFSEXP.removeAll(DFSEXP);
			Shuffle(10);
			drawBG(g);
			first = false;
		}
		//pos1 = (425,650)
		//pos2 = (425,100)
		g.setFont(new Font("serif",Font.BOLD,18));
		
		//Game actually starts here:
		if (!win) { //Game has not been Won Yet
		if (Turn == 1) {
			if (playCard && HandP1.get(cardSelect-1).equals("DFS") && !boom) { // Does not play DFS card if no bomb exists
				playCard = false;
			}
		} else if (Turn == 2) {
			if (playCard && HandP2.get(cardSelect-1).equals("DFS") && !boom) { // Does not play DFS card if no bomb exists
				playCard = false;
			}
		}
			
		if (playCard && Turn==1) { //Moves the card from HandP1() to Discard()
			Discard.add(HandP1.get(cardSelect-1));
			HandP1.remove(cardSelect-1);
			activeCard = Discard.get(Discard.size()-1);
			playCard = false;
			cardSelect = 0;
		} else if (playCard && Turn==2) { //Moves the card from HandP2() to Discard()
			Discard.add(HandP2.get(cardSelect-1));
			HandP2.remove(cardSelect-1);
			activeCard = Discard.get(Discard.size()-1);
			playCard = false;
			cardSelect = 0;
		}
		if (draw && Cards.size()!=0 && Turn==1) { //Moves the card from Cards() to HandP1()
			if (Cards.get(0).equals("EXP")) {
				black=true;
				boom=true;
			} else {
				HandP1.add(Cards.get(0));
				Cards.remove(0);
				draw = false;
				if (!bombPlant) {changeTurns = true;}
			}

		} else if (draw && Cards.size()!=0 && Turn==2) { //Moves the card from Cards() to HandP2()
			if (Cards.get(0).equals("EXP")) {
				black=true;
				boom=true;
			} else {
				HandP2.add(Cards.get(0));
				Cards.remove(0);
				draw = false;
				if (!bombPlant) {changeTurns = true;}
			}
		}
		if (cardSelect>0 && Turn == 1) { //Draw the information box and confirm button
			if (boom && HandP1.get(cardSelect-1).equals("DFS")) {
				confirmBox = true;
			} else if (!boom) {
				confirmBox = true;
			}
		}
		if (cardSelect>0 && Turn == 2) { //Draw the information box and confirm button
			if (boom && HandP2.get(cardSelect-1).equals("DFS")) {
				confirmBox = true;
			} else if (!boom) {
				confirmBox = true;
			}
		}
	
	//Begin Specific Card Rules
		if (activeCard.equals("ATK")) { //Attack Card - Skip your turn, next player goes twice
			changeTurns = true;
			reTurn = 3;
			lastAction = "ATK";
		}
		if (activeCard.equals("SKP")) { //Skip Card - Skip your turn
			changeTurns = true;
			lastAction = "SKP";
		}
		if (activeCard.equals("STF")) { //See the Future - Privately view the top three cards
			black = true;
			see = true;
			lastAction = "STF";
		}
		if (activeCard.equals("SHF")) { //Shuffle - Shuffle the draw pile
			Shuffle(10);
			lastAction = "SHF";
		}
		if (activeCard.equals("DFS") && boom) { //Defuse - prevents you from exploding
			boom = false;
			black = false;
			bombPlant = true;
			draw = false;
			Cards.remove(0);
		}
		if (activeCard.equals("FVR")) { //Favor - Steal a card from your opponent
			favor = true;
			exception = true;
			black = true;
			lastAction = "FVR";
		}
		if (activeCard.equals("NPE")) { //Nope - cancels out Attack, Skip and Favor Cards
			Undo(lastAction);
			lastAction = "NPE";
		}
		activeCard = "null";
		
		//Turn Changer------------------Needs Improvement----------------------
		if (changeTurns) {
			if (Turn==1 && reTurn!=2) {
				Turn = 2;
			} else if (Turn==2 && reTurn!=2) {
				Turn = 1;
			}
			if (reTurn>0){
				reTurn -= 1;
			}
		}
		
		UpdatePos(g);
		
		} else {
			imageGrab("assets/black.png");
			g.drawImage(image,0,0,1500,1000,IO);
			g.setColor(Color.WHITE);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("CONGRATULATIONS", 600, 400);
		}
	}		
	
	public static void Shuffle(int timesShuffle) { //Method to randomize the order of the cards in the draw pile
		for(int i = 0;i<timesShuffle;i++) {
			int size = Cards.size();
			for(int j = 0;j<size;j++) {
				int ranNum = rn.nextInt(size-j);
				String idNew = Cards.get(ranNum);
				Cards.remove(ranNum);Shuff.add(idNew);
			}
			Cards = Shuff;
		}
		System.out.println(Cards);

	}
	public static void Deal(int players,int crds) { //Method to deal the cards to the number of players and number of cards per player
		for(int i = 0;i<crds-1;i++) {
			String idNew = Cards.get(0);
			Cards.remove(0);HandP1.add(idNew); 
			idNew = Cards.get(0);
			Cards.remove(0);HandP2.add(idNew);
		}
	}
	public static void TakeOutDFSEXP() { //Method to remove DFS and EXP form the Cards ArrayList
		for(int i = 0;i<10;i++) {
			String idNew = Cards.get(0);
			Cards.remove(0);DFSEXP.add(idNew);
		}
	}
	public static void GiveDefuseCards() { //Adds 1 DFS Card to each player's deck
		String idNew = DFSEXP.get(0);
		DFSEXP.remove(0);HandP1.add(idNew);
		idNew = DFSEXP.get(0);
		DFSEXP.remove(0);HandP2.add(idNew);
	}
	public static void Undo(String action) {
		System.out.println(action);
		if (action.equals("ATK")) {
			changeTurns = true;
			reTurn = 0;
		}
		if (action.equals("SKP")) {
			changeTurns = true;
		}
	}
	
	public void drawBG (Graphics g) { //Draws the background, wow im lazy
		imageGrab("assets/backdrop.png");
		g.drawImage(image,0,0,1500,1000,IO);
	}
	
    public void drawCard (int x, int y, String id, Graphics g) { //Draws a Card at a specific coordinate with a specific ID
    	//Card Dimensions W = 132 H = 172
    	g.setColor(Color.white);
		g.fillOval(x,y,32,32);
		g.fillOval(x+100,y,32,32);
		g.fillOval(x,y+140,32,32);
		g.fillOval(x+100,y+140,32,32);
		g.setColor(Color.black);
		g.drawOval(x,y,32,32);
		g.drawOval(x+100,y,32,32);
		g.drawOval(x,y+140,32,32);
		g.drawOval(x+100,y+140,32,32);
		g.drawLine(x+16,y,x+116,y);
		g.drawLine(x,y+16,x,y+156);
		g.drawLine(x+132,y+16,x+132,y+156);
		g.drawLine(x+16,y+172,x+116,y+172);
		g.setColor(Color.white);
		g.fillRect(x+1,y+16,131,30);
		g.fillRect(x+16,y+1,97,30);
		g.fillRect(x+1,y+140,131,20);
		g.fillRect(x+16,y+150,97,22);
		g.fillRect(x+1,y+44,131,100);
		drawID(x,y,id,g);
    }
    public void drawHighlight (int x, int y, String id, Graphics g) { //Draws a highlighted card
    	//Card Dimensions W = 132 H = 172
    	g.setColor(Color.yellow);
		g.fillOval(x,y,32,32);
		g.fillOval(x+100,y,32,32);
		g.fillOval(x,y+140,32,32);
		g.fillOval(x+100,y+140,32,32);
		g.setColor(Color.black);
		g.drawOval(x,y,32,32);
		g.drawOval(x+100,y,32,32);
		g.drawOval(x,y+140,32,32);
		g.drawOval(x+100,y+140,32,32);
		g.drawLine(x+16,y,x+116,y);
		g.drawLine(x,y+16,x,y+156);
		g.drawLine(x+132,y+16,x+132,y+156);
		g.drawLine(x+16,y+172,x+116,y+172);
		g.setColor(Color.yellow);
		g.fillRect(x+1,y+16,131,30);
		g.fillRect(x+16,y+1,97,30);
		g.fillRect(x+1,y+140,131,20);
		g.fillRect(x+16,y+150,97,22);
		g.fillRect(x+1,y+44,131,100);
		drawID(x,y,id,g);
    }
    public void drawID(int x,int y,String id,Graphics g) { //Determines which ID draws which graphical element
    	if (id.equals("DFS")) {
			g.setColor(Color.black);
			g.drawString("DFS",x+8,y+21);
			g.drawString("DFS",x+92,y+162);
			imageGrab("assets/defuse.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
			
		}
		if (id.equals("STF")) {
			g.setColor(Color.black);
			g.drawString("STF",x+8,y+21);
			g.drawString("STF",x+92,y+162);
			imageGrab("assets/seethefuture.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("ATK")) {
			g.setColor(Color.black);
			g.drawString("ATK",x+8,y+21); //Outline
			g.drawString("ATK",x+92,y+162);
			imageGrab("assets/attack.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("SKP")) {
			g.setColor(Color.black);
			g.drawString("SKP",x+8,y+21); //Outline
			g.drawString("SKP",x+92,y+162);
			imageGrab("assets/skip.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("EXP")) {
			g.setColor(Color.black);
			g.drawString("EXP!",x+8,y+21); //Outline
			g.drawString("EXP!",x+92,y+162);
			imageGrab("assets/explode.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("SHF")) {
			g.setColor(Color.black);
			g.drawString("SHF",x+8,y+21); //Outline
			g.drawString("SHF",x+92,y+162);
			imageGrab("assets/shuffle.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("NPE")) {
			g.setColor(Color.black);
			g.drawString("NPE",x+8,y+21); //Outline
			g.drawString("NPE",x+92,y+162);
			imageGrab("assets/nope.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("FVR")) {
			g.setColor(Color.black);
			g.drawString("FVR",x+8,y+21); //Outline
			g.drawString("FVR",x+92,y+162);
			imageGrab("assets/favor.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("HPC")) {
			g.setColor(Color.black);
			g.drawString("HPC",x+8,y+21); //Outline
			g.drawString("HPC",x+89,y+162);
			imageGrab("assets/hairypotato.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("BRD")) {
			g.setColor(Color.black);
			g.drawString("BRD",x+8,y+21); //Outline
			g.drawString("BRD",x+89,y+162);
			imageGrab("assets/trash.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("TCT")) {
			g.setColor(Color.black);
			g.drawString("TCT",x+8,y+21); //Outline
			g.drawString("TCT",x+89,y+162);
			imageGrab("assets/trash.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("RRC")) {
			g.setColor(Color.black);
			g.drawString("RRC",x+8,y+21); //Outline
			g.drawString("RRC",x+89,y+162);
			imageGrab("assets/trash.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("CTM")) {
			g.setColor(Color.black);
			g.drawString("CTM",x+8,y+21); //Outline
			g.drawString("CTM",x+89,y+162);
			imageGrab("assets/cattermelon.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
		}
		if (id.equals("HPCHPC")) {
			g.setColor(Color.black);
			g.drawString("HPC",x+8,y+21); //Outline
			g.drawString("HPC",x+89,y+162);
			imageGrab("assets/hairypotato.png");
			g.drawImage(image,x+16,y+36,100,100,IO);	
			//drawCard(x,y+20,"HPC",g);
		}
		if (id.equals("BRDBRD")) {
			g.setColor(Color.black);
			g.drawString("BRD",x+8,y+21); //Outline
			g.drawString("BRD",x+89,y+162);
			imageGrab("assets/trash.png");
			//drawCard(x,y+20,"BRD",g);
		}
		if (id.equals("TCTTCT")) {
			g.setColor(Color.black);
			g.drawString("TCT",x+8,y+21); //Outline
			g.drawString("TCT",x+89,y+162);
			imageGrab("assets/trash.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
			//drawCard(x,y+20,"TCT",g);
		}
		if (id.equals("RRCRRC")) {
			g.setColor(Color.black);
			g.drawString("RRC",x+8,y+21); //Outline
			g.drawString("RRC",x+89,y+162);
			imageGrab("assets/trash.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
			//drawCard(x,y+20,"RRC",g);
		}
		if (id.equals("CTMCTM")) {
			g.setColor(Color.black);
			g.drawString("CTM",x+8,y+21); //Outline
			g.drawString("CTM",x+89,y+162);
			imageGrab("assets/cattermelon.png");
			g.drawImage(image,x+16,y+36,100,100,IO);
			//drawCard(x,y+20,"CTM",g);
		}
	}
    public void drawBack (int x, int y, Graphics g) { //Draws the back of the card
    	//Card Dimensions W = 132 H = 172
    	//Red Color 	R: 97 G: 16 B: 12
    	
    	g.setColor(explodingRed);
		g.fillOval(x,y,32,32);
		g.fillOval(x+100,y,32,32);
		g.fillOval(x,y+140,32,32);
		g.fillOval(x+100,y+140,32,32);
		g.setColor(Color.black);
		g.drawOval(x,y,32,32);
		g.drawOval(x+100,y,32,32);
		g.drawOval(x,y+140,32,32);
		g.drawOval(x+100,y+140,32,32);
		g.drawLine(x+16,y,x+116,y);
		g.drawLine(x,y+16,x,y+156);
		g.drawLine(x+132,y+16,x+132,y+156);
		g.drawLine(x+16,y+172,x+116,y+172);
		g.setColor(explodingRed);
		g.fillRect(x+1,y+16,131,30);
		g.fillRect(x+16,y+1,97,30);
		g.fillRect(x+1,y+140,131,20);
		g.fillRect(x+16,y+150,97,22);
		g.fillRect(x+1,y+44,131,100);
		imageGrab("assets/icon.png");
		g.drawImage(image,x+16,y+36,100,100,IO);
	}
    
    public void drawP1Hand(Graphics g, boolean face) { //Draw P1's Hand, can be face up or face down
    	int x=390;
    	for (int i=0;i<HandP1.size();i++) { //Draw Every Card in P1's Hand
			if (face) {
				drawCard(x,650,HandP1.get(i),g);
				CardsPos1.add(x);
				x+=700/HandP1.size();
			} else {
				drawBack(x,650,g);
				CardsPos1.add(x);
				x+=700/HandP1.size();
			}
    		
		}
    }
    public void drawP2Hand(Graphics g, boolean face) { //Draw P2's Hand, can be face up or face down
    	int x = 390;
    	for (int i=0;i<HandP2.size();i++) { //Draw Every Card in P2's Hand
			if (face) {
				drawCard(x,100,HandP2.get(i),g);
				CardsPos2.add(x);
				x+=700/HandP2.size();
			} else {
				drawBack(x,100,g);
				CardsPos2.add(x);
				x+=700/HandP2.size();
			}
			
		}
    }
    public void drawP1HandH(Graphics g) { //Draw the highlighted defuse card
    	int x=390;
    	for (int i=0;i<HandP1.size();i++) { //Draw Every Card in P1's Hand
			if (Turn==1) {
				if(HandP1.get(i).equals("DFS")){
					drawHighlight(x,650,HandP1.get(i),g);
				} else {
					drawCard(x,650,HandP1.get(i),g);
				}
				CardsPos1.add(x);
				x+=700/HandP1.size();
			} else {
				drawBack(x,650,g);
				CardsPos1.add(x);
				x+=700/HandP1.size();
			}
    		
		}
    }
    public void drawP2HandH(Graphics g) { //Draw the highlighted defuse card
    	int x = 390;
    	for (int i=0;i<HandP2.size();i++) { //Draw Every Card in P2's Hand
			if (Turn == 2) {
				if(HandP2.get(i).equals("DFS")){
					drawHighlight(x,100,HandP2.get(i),g);
				} else {
					drawCard(x,100,HandP2.get(i),g);
				}
				CardsPos2.add(x);
				x+=700/HandP2.size();
			} else {
				drawBack(x,100,g);
				CardsPos2.add(x);
				x+=700/HandP2.size();
			}
			
		}
    }

    
    public void UpdatePos(Graphics g) { //Draws the new position of cards, uses variables to update UI
    	drawBG(g);
    	CardsPos1.clear();
    	CardsPos2.clear();
    	if (Turn==1) {
    		drawP1Hand(g,true);
    		drawP2Hand(g,false);
    	} else if (Turn==2) {
    		drawP1Hand(g,false);
    		drawP2Hand(g,true);
    	}
		
		if(Discard.size()>1) { //Draw discard pile if there are cards in it
			drawCard(743,375,Discard.get(Discard.size()-1),g);
		}
		
		if(Cards.size()>0) { //Draw draw pile if there are cards in it
			drawBack(605,375,g);
		}
		
		if (black) {//There is some reason for there to be a black screen
			imageGrab("assets/black.png");
			g.drawImage(image,0,0,1500,1000,IO);
			if (boom) {                                  //Exploding Kitten is picked up
				if (Turn==1) {
					drawP1HandH(g);
				} else if (Turn==2) {
					drawP2HandH(g);
				}
				drawCard(605,375,"EXP",g);
				if (cardSelect>0) {
					if (HandP1.get(cardSelect-1).equals("DFS") && playCard) {
						boom = false;
					}
				}
			} else if (see) {                               //See the Future is played
				if (Cards.size()>0) {
					drawCard(525,375,Cards.get(0),g);
				}
				if (Cards.size()>1) {
					drawCard(675,375,Cards.get(1),g);
				}
				if (Cards.size()>2) {
					drawCard(825,375,Cards.get(2),g);
				}
				imageGrab("assets/Arrow.png");
				g.drawImage(image,435,300,100,100,IO);
				g.setColor(backgroundBlue);
				g.drawString("TOP CARD", 410, 285);
			} else if (favor) {                             //Favor is Played
				if (Turn == 2) {
					drawP2Hand(g,false);
					drawP1Hand(g,true);
					g.setColor(Color.WHITE);
					g.drawString("Player 2, Select a Card to give to Player 1:",400,600);
				} else if (Turn == 1) {
					drawP1Hand(g,false);
					drawP2Hand(g,true);
					g.setColor(Color.WHITE);
					g.drawString("Player 1, Select a Card to give to Player 2:",400,75);
				}
			}
			
			g.setColor(Color.BLACK);
			black=false;
			see=false;
		}
		if (confirmBox && Turn == 1 && !favor && !changeTurns) {
			g.setColor(Color.WHITE);
			g.fillRect(1150,600,300,280);
			g.setColor(Color.BLACK);
			g.drawRect(1150,600,300,280);
			determineCard();
			g.drawImage(image,1150,600,300,280,IO);
		} else if (confirmBox && Turn == 2 && !favor && !changeTurns) {
			g.setColor(Color.WHITE);
			g.fillRect(1150,50,300,280);
			g.setColor(Color.BLACK);
			g.drawRect(1150,50,300,280);
			determineCard();
			g.drawImage(image,1150,50,300,280,IO);
		}
		if (bombPlant) {               //draws the Bomb Planter Window
			imageGrab("assets/planter.png");
			g.drawImage(image,465,361,132,200,IO);
		}
		if (changeTurns) {                     //Pauses before the turn changes to review deck
			if (reTurn == 1) {
				if (Turn==2) {
					drawP1Hand(g,false);
					imageGrab("assets/black.png");
					g.drawImage(image,0,0,1500,1000,IO);
					drawP2Hand(g,true);
				} else if (Turn == 1) {
					drawP2Hand(g,false);
					imageGrab("assets/black.png");
					g.drawImage(image,0,0,1500,1000,IO);
					drawP1Hand(g,true);
				}
			} else {
				if (Turn==1) {
					drawP1Hand(g,false);
					imageGrab("assets/black.png");
					g.drawImage(image,0,0,1500,1000,IO);
					drawP2Hand(g,true);
				} else if (Turn == 2) {
					drawP2Hand(g,false);
					imageGrab("assets/black.png");
					g.drawImage(image,0,0,1500,1000,IO);
					drawP1Hand(g,true);
				}
			}
			g.setColor(Color.WHITE);
			g.drawString("CLICK ANYWHERE TO CHANGE TURNS",600,300);
			g.setColor(Color.BLACK);
			changeTurns = false;
			exception = false;
			disable = true;
		}
    }
    
    public static void determineCard() {
    	if (Turn==1) {
    		if (HandP1.get(cardSelect-1).equals("DFS")){
    			imageGrab("assets/infoDFS.png");
    		}
    		if (HandP1.get(cardSelect-1).equals("SKP")){
    			imageGrab("assets/infoSKP.png");
    		}
    		if (HandP1.get(cardSelect-1).equals("ATK")){
    			imageGrab("assets/infoATK.png");
    		}
    		if (HandP1.get(cardSelect-1).equals("STF")){
    			imageGrab("assets/infoSTF.png");
    		}
    		if (HandP1.get(cardSelect-1).equals("SHF")){
    			imageGrab("assets/infoSHF.png");
    		}
    		if (HandP1.get(cardSelect-1).equals("NPE")){
    			imageGrab("assets/infoNPE.png");
    		}
    		if (HandP1.get(cardSelect-1).equals("FVR")){
    			imageGrab("assets/infoFVR.png");
    		}
    		if (HandP1.get(cardSelect-1).equals("CTM")||HandP1.get(cardSelect-1).equals("HPC")||HandP1.get(cardSelect-1).equals("RRC")||HandP1.get(cardSelect-1).equals("BRD")||HandP1.get(cardSelect-1).equals("TCT")){
    			imageGrab("assets/infoCAT.png");
    		}
    	} else if (Turn==2) {
    		if (HandP2.get(cardSelect-1).equals("DFS")){
    			imageGrab("assets/infoDFS.png");
    		}
    		if (HandP2.get(cardSelect-1).equals("SKP")){
    			imageGrab("assets/infoSKP.png");
    		}
    		if (HandP2.get(cardSelect-1).equals("ATK")){
    			imageGrab("assets/infoATK.png");
    		}
    		if (HandP2.get(cardSelect-1).equals("STF")){
    			imageGrab("assets/infoSTF.png");
    		}
    		if (HandP2.get(cardSelect-1).equals("SHF")){
    			imageGrab("assets/infoSHF.png");
    		}
    		if (HandP2.get(cardSelect-1).equals("NPE")){
    			imageGrab("assets/infoNPE.png");
    		}
    		if (HandP2.get(cardSelect-1).equals("FVR")){
    			imageGrab("assets/infoFVR.png");
    		}
    		if (HandP2.get(cardSelect-1).equals("CTM")||HandP2.get(cardSelect-1).equals("HPC")||HandP2.get(cardSelect-1).equals("RRC")||HandP2.get(cardSelect-1).equals("BRD")||HandP2.get(cardSelect-1).equals("TCT")){
    			imageGrab("assets/infoCAT.png");
    		}
    	}
    	
    }
    public static void placeCard (int pos) {
    	Storage.addAll(Cards);
    	Cards.removeAll(Cards);
    	for (int i = 0;i<pos;i++) {
    		Cards.add(Storage.get(0));
    		Storage.remove(Storage.get(0));
    	}
    	Cards.add("EXP");
    	Cards.addAll(Storage);
    	Storage.removeAll(Storage);
    }
    
    public static void newPack1() {
    	initialDeal=5;
    	for (int i = 0;i<56;i++) {
    		if (i > -1 && i < 6) {
    			Cards.add("DFS");
    		}
    		if (i > 5 && i < 10) {
    			Cards.add("EXP");
    		}
    		if (i > 9 && i < 14) {
    			Cards.add("SKP");
    		}
    		if (i > 13 && i < 18) {
    			Cards.add("ATK");
    		}
    		if (i > 17 && i < 23) {
    			Cards.add("STF");
    		}
    		if (i > 22 && i < 27) {
    			Cards.add("SHF");
    		}
    		if (i > 26 && i < 32) {
    			Cards.add("NPE");
    		}
    		if (i > 31 && i < 36) {
    			Cards.add("FVR");
    		}
    		if (i > 35 && i < 40) {
    			Cards.add("BRD");
    		}
    		if (i > 39 && i < 44) {
    			Cards.add("TCT");
    		}
    		if (i > 43 && i < 48) {
    			Cards.add("CTM");
    		}
    		if (i > 47 && i < 52) {
    			Cards.add("RRC");	
    		}
    		if (i > 51 && i < 56) {
    			Cards.add("HPC");
    		}
    	}
    }
    public static void newPack2() {
    	initialDeal=7;
    	for (int i = 0;i<76;i++) {
    		if (i > -1 && i < 6) {
    			Cards.add("DFS");
    		}
    		if (i > 5 && i < 10) {
    			Cards.add("EXP");
    		}
    		if (i > 9 && i < 14) {
    			Cards.add("SKP");
    		}
    		if (i > 13 && i < 18) {
    			Cards.add("ATK");
    		}
    		if (i > 17 && i < 23) {
    			Cards.add("STF");
    		}
    		if (i > 22 && i < 27) {
    			Cards.add("SHF");
    		}
    		if (i > 26 && i < 32) {
    			Cards.add("NPE");
    		}
    		if (i > 31 && i < 36) {
    			Cards.add("FVR");
    		}
    		if (i > 35 && i < 40) {
    			Cards.add("BRD");
    		}
    		if (i > 39 && i < 44) {
    			Cards.add("TCT");
    		}
    		if (i > 43 && i < 48) {
    			Cards.add("CTM");
    		}
    		if (i > 47 && i < 52) {
    			Cards.add("RRC");	
    		}
    		if (i > 51 && i < 56) {
    			Cards.add("HPC");
    		}
    		if (i > 55 && i < 57) {
    			Cards.add("IMP");
    		}
    		if (i > 56 && i < 61) {
    			Cards.add("REV");
    		}
    		if (i > 60 && i < 65) {
    			Cards.add("DFB");
    		}
    		if (i > 64 && i < 69) {
    			Cards.add("ATF");
    		}
    		if (i > 68 && i < 72) {
    			Cards.add("TAR");
    		}
    		if (i > 71 && i < 76) {
    			Cards.add("FRL");
    		}
    	}
    }
	public static void newPack3() {
		initialDeal=7;
		for (int i = 0;i<91;i++) {
    		if (i > -1 && i < 6) {
    			Cards.add("DFS");
    		}
    		if (i > 5 && i < 10) {
    			Cards.add("EXP");
    		}
    		if (i > 9 && i < 14) {
    			Cards.add("SKP");
    		}
    		if (i > 13 && i < 18) {
    			Cards.add("ATK");
    		}
    		if (i > 17 && i < 23) {
    			Cards.add("STF");
    		}
    		if (i > 22 && i < 27) {
    			Cards.add("SHF");
    		}
    		if (i > 26 && i < 32) {
    			Cards.add("NPE");
    		}
    		if (i > 31 && i < 36) {
    			Cards.add("FVR");
    		}
    		if (i > 35 && i < 40) {
    			Cards.add("BRD");
    		}
    		if (i > 39 && i < 44) {
    			Cards.add("TCT");
    		}
    		if (i > 43 && i < 48) {
    			Cards.add("CTM");
    		}
    		if (i > 47 && i < 52) {
    			Cards.add("RRC");	
    		}
    		if (i > 51 && i < 56) {
    			Cards.add("HPC");
    		}
    		if (i > 55 && i < 57) {
    			Cards.add("IMP");
    		}
    		if (i > 56 && i < 61) {
    			Cards.add("REV");
    		}
    		if (i > 60 && i < 65) {
    			Cards.add("DFB");
    		}
    		if (i > 64 && i < 69) {
    			Cards.add("ATF");
    		}
    		if (i > 68 && i < 72) {
    			Cards.add("TAR");
    		}
    		if (i > 71 && i < 76) {
    			Cards.add("FRL");
    		}
    		if (i > 71 && i < 76) {
    			Cards.add("FRL");
    		}
    		if (i > 75 && i < 77) {
    			Cards.add("EXP");
    		}
    		if (i > 76 && i < 78) {
    			Cards.add("CAT");
    		}
    		if (i > 77 && i < 79) {
    			Cards.add("STR");
    		}
    		if (i > 78 && i < 80) {
    			Cards.add("SSKP");
    		}
    		if (i > 79 && i < 81) {
    			Cards.add("GAR");
    		}
    		if (i > 80 && i < 82) {
    			Cards.add("STF5");
    		}
    		if (i > 81 && i < 83) {
    			Cards.add("ATF5");
    		}
    		if (i > 82 && i < 86) {
    			Cards.add("MRK");
    		}
    		if (i > 85 && i < 89) {
    			Cards.add("SWP");
    		}
    		if (i > 88 && i < 91) {
    			Cards.add("CUR");
    		}
		}
	}	
	public void askPack (Graphics g) {
		//imageGrab("assets/ask.png");
		//g.drawImage(image,0,0,1500,1000,IO);
		
		newPack1();
	}
	
	public static void imageGrab(String s) {
		try {
		    File pathToFile = new File(s);
		    image = ImageIO.read(pathToFile);
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
	}

	public static void selectCard(List<String> a, int tempY1, int tempY2, int tempY3) {
		if (disable) {
		    mouseX = 0;
		    mouseY = 0;
		    disable = false;
		}
		if (a.size()!=0) {
			int inc = 700/a.size();
			if(mouseY>tempY1 && mouseY<tempY2) {
				for (int i = 0;i<a.size();i++) {
					if(mouseX>(inc*(i)+389) && mouseX<(inc*(i+1)+389)) {
						mouseX=inc*i+389;
						mouseY=tempY3;
						cardSelect=i+1;
					}
				}
			}
		// f(x) = (700/x)*i + 389 **function for Finding Card Value
		}
	}
	
	public void mousePressed(MouseEvent e) {//Used as soon as mouse is pressed
		mouseX = e.getX();
		mouseY = e.getY();
		if (Turn==1) {
			selectCard(HandP1,649,823,736);
			if ((mouseX>605 && mouseX<737) && (mouseY>375 && mouseY<547)) { //Draw in the right field
				mouseX=671;
				mouseY=823;
				if (!bombPlant) {
					draw=true;
				}
			}
			if (confirmBox && (mouseX>1159 && mouseX<1296) && (mouseY>838 && mouseY<864)) { //Confirm in confirmBox
				if (!bombPlant) {playCard = true;}
			}
			if (confirmBox && (mouseX>1303 && mouseX<1439) && (mouseY>838 && mouseY<864)) { //Cancel in confirmBox
				cardSelect = 0;
			}
			confirmBox = false;
		} else if (Turn==2) {
			selectCard(HandP2,99,273,186);
			if ((mouseX>605 && mouseX<737) && (mouseY>375 && mouseY<547)) {
				mouseX=671;
				mouseY=823;
				if (!bombPlant) {
					draw=true;
				}
			}
			if (confirmBox && (mouseX>1159 && mouseX<1296) && (mouseY>289 && mouseY<314)) { //Confirm in confirmBox
				if (!bombPlant) {playCard = true;}
			}
			if (confirmBox && (mouseX>1303 && mouseX<1439) && (mouseY>289 && mouseY<314)) { //Cancel in confirmBox
				cardSelect = 0;
			}
			confirmBox = false;
		}
		                        //These statements utilize the bomb plant window if it is drawn to place the EXP... 
		if (bombPlant && (mouseX>472 && mouseX<589) && (mouseY>398 && mouseY<417)) { //On top
			placeCard(0); //Place the bomb in slot 1
			bombPlant = false;
			changeTurns = true;
		}
		if (bombPlant && (mouseX>472 && mouseX<589) && (mouseY>421 && mouseY<440)) { //Second
			placeCard(1); //Place the bomb in slot 2
			bombPlant = false;
			changeTurns = true;
		}
		if (bombPlant && (mouseX>472 && mouseX<589) && (mouseY>444 && mouseY<463)) { //Third
			placeCard(2); //Place the bomb in slot 3
			bombPlant = false;
			changeTurns = true;
		}
		if (bombPlant && (mouseX>472 && mouseX<589) && (mouseY>467 && mouseY<486)) { //Fourth
			placeCard(3); //Place the bomb in slot 4
			bombPlant = false;
			changeTurns = true;
		}
		if (bombPlant && (mouseX>472 && mouseX<589) && (mouseY>490 && mouseY<509)) { //Fifth
			placeCard(4); //Place the bomb in slot 5
			bombPlant = false;
			changeTurns = true;
		}
		if (bombPlant && (mouseX>472 && mouseX<589) && (mouseY>513 && mouseY<532)) { //Random
			int r = rn.nextInt(Cards.size());
			placeCard(r); //Place the bomb in slot random
			bombPlant = false;
			changeTurns = true;
		}
		if (bombPlant && (mouseX>472 && mouseX<589) && (mouseY>536 && mouseY<555)) { //Bottom
			Cards.add("EXP"); //Simply add the card back into the deck (Adding immediately sends to bottom)
			bombPlant = false;
			changeTurns = true;
		}
		if (favor && (mouseX>389 && mouseX<1125) && ((Turn==2 && mouseY>649 && mouseY<823)||(Turn==1 && mouseY>99 && mouseY<273))) {
			swap = true;
			favor = false;
			
		}
		if (swap) {
			if (Turn==1) {
				selectCard(HandP2,99,273,186);
				HandP1.add(HandP2.get(cardSelect-1));
				HandP2.remove(HandP2.get(cardSelect-1));
			} else if(Turn==2) {
				selectCard(HandP1,649,823,736);
				HandP2.add(HandP1.get(cardSelect-1));
				HandP1.remove(HandP1.get(cardSelect-1));
			}
			swap = false;
			cardSelect=0;
		}
		System.out.println("("+mouseX+", "+mouseY+")");
		repaint();
	}
	public void mouseReleased(MouseEvent e) {//Don't Need - used if the mouse is released
	}
	public void mouseEntered(MouseEvent e) { //Don't Need - used if the mouse enters the window
	}
	public void mouseClicked(MouseEvent e) { //Don't Need - used if the mouse is pressed then released quickly
	}
	public void mouseExited(MouseEvent e) {//Don't Need - used if a mouse exits the window
	}
}