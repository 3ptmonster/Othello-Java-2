import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
/*
 * file: ReversiGame.java
 * @author: Calvin Gonzalez and Nicholas Lightburn
 *
 * This program launches a GUI where
 * two players play against each other
 * in a game of Reversi.  The objective of 
 * the game is to have more pieces on the
 * board than your opponent by the end of 
 * the game.
 */
public class ReversiGame extends JFrame
{
	//Attributes
	private JPanel board;
	private JButton[][] boardPcs = new JButton[8][8];
	
	private JMenuBar menuBar;
	private JMenu jMenu;
	//private JMenuItem 
	
    //player 1 attributes
    private Icon p1Icon = new ImageIcon("blackPiece.png");
	private int p1Pieces = 30;
    private int p1Score;
	//player 2 attributes
	private Icon p2Icon = new ImageIcon("bluePiece.png");
	private int p2Pieces = 30;
	private int p2Score;
    
    private Icon empty = new ImageIcon("empty");
    private String winner;
    
    //First click attributes
    private Icon selectionIcon;
    private int xPos, yPos, tempXPos, tempYPos;
    private int playerTurn = 1;
    
    //validation Arraylistsr
    private ArrayList<JButton> horizontalPieces = new ArrayList<JButton>();
    private ArrayList<JButton> verticalPieces = new ArrayList<JButton>();
    private ArrayList<JButton> diagonalPieces = new ArrayList<JButton>();
    
    //SOUTH of jframe
    //private JPanel jpControls;
    //private JButton jbReset;
    
    //RIGHT side of jframe
    private JPanel scorePanel;
    private JTextField jtfP1;
    private JTextField jtfP2;
    private JTextField jtfCurrPlayer;
    
	public static void main (String[] args)
	{
		ReversiGame jf = new ReversiGame();
        
        //General setup for JFrame
        jf.setTitle("Reversi");
		jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
      	jf.setVisible(true);
        
	}//end main
	
    /*
        ReversiGame constructor
        invokes a method to setup gameboard
        and display information to players
    */
	public ReversiGame()
	{
		setupGame();
        JOptionPane.showMessageDialog(null, 
            "Player 1 will use black pieces. \n" + 
            "Player 2 will use white pieces. \nPlayer 1 starts.");
    }//end constructor
    
    /*
        setupGame sets up 
        gameboard in order
        for it to be displayed
    */
    public void setupGame()
    {
        createBoard();
        givePiecesActions();
        createScorePanel();
        
        add(board);
        add(scorePanel, BorderLayout.EAST);
    }//end method setupGame
    
    /*
        createBoard uses a 2D Array
        of JButtons and adds each one
        to fill up the gameboard. 
        
    */
    public void createBoard()
    {
        Dimension paneDim = new Dimension(700,700);
        board = new JPanel( new GridLayout(8,8) );
		for(int row = 0; row < boardPcs.length; row++)
		{
			for(int col = 0; col < boardPcs.length; col++)
			{
				boardPcs[row][col] = new JButton(row + "," + col);
                boardPcs[row][col].setBackground(Color.LIGHT_GRAY);
                boardPcs[row][col].setIcon(empty);
                boardPcs[row][col].setOpaque(true);
                if(row == 4)
                {
                    if(col == row)
                    {
                        boardPcs[row - 1][col - 1].setIcon(p1Icon);
                        boardPcs[row][col].setIcon(p1Icon);
                        boardPcs[row - 1][col].setIcon(p2Icon);
                        boardPcs[row][col - 1].setIcon(p2Icon);
                    }
                }
				board.add(boardPcs[row][col]);
			}
		}
        board.setPreferredSize(paneDim);
        board.setMaximumSize(paneDim);
    }
    /*
        givePiecesActions
        iterates through each button
        and gives them on click actions
    */
    public void givePiecesActions()
    {
        for(int row = 0; row < boardPcs.length; row++)
        {
            for(int col = 0; col < boardPcs.length; col++)
            {
                boardPcs[row][col].addActionListener( new ActionListener(){
                    public void actionPerformed(ActionEvent ae) 
                    {
                        if(playerTurn == 1)
                        {
                            String selectionText = ae.getActionCommand();
                            selectionIcon = ((JButton)ae.getSource()).getIcon();
                            String[] coordinates = selectionText.split(",");
                            xPos = Integer.parseInt(coordinates[0]);
                            yPos = Integer.parseInt(coordinates[1]);
                            
                            if(isValidMove())
                            {
                                boardPcs[xPos][yPos].setIcon(p1Icon);
                                updateScore();
                                
                                if(isWinner())//check to see if player has won
                                {
                                    JOptionPane.showMessageDialog(null, winner );
                                }
                                else
                                {
                                    p1Pieces--;
                                    playerTurn++;
                                    jtfCurrPlayer.setText("Player 2");
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "That is not a valid move. Please try again.");
                            }
                        }
                        else if(playerTurn == 2)
                        {
                            String selectionText = ae.getActionCommand();
                            selectionIcon = ((JButton)ae.getSource()).getIcon();
                            String[] coordinates = selectionText.split(",");
                            xPos = Integer.parseInt(coordinates[0]);
                            yPos = Integer.parseInt(coordinates[1]);
                            
                            if(isValidMove())
                            {
                                boardPcs[xPos][yPos].setIcon(p2Icon);
                                updateScore();
                                
                                
                                if(isWinner())//check if player has won
                                {
                                    JOptionPane.showMessageDialog(null, winner );
                                }
                                else
                                {
                                    p2Pieces--;
                                    playerTurn--;
                                    jtfCurrPlayer.setText("Player 1");
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "That is not a valid move. Please try again.");
                            }
                        }
                    }
                });
            }
        }   
    }
    
    /*
        createScorePanel displays
        live score during gameplay
        as well as whose turn it is
    */
    public void createScorePanel()
    {
        Font font1 = new Font("SansSerif", Font.BOLD, 24);
        
        scorePanel = new JPanel( new GridLayout(0,1 ));
            JPanel p1Panel = new JPanel();
                jtfP1 = new JTextField("0", 10);
                p1Panel.add( new JLabel("Player 1 Score", JLabel.RIGHT) );
                p1Panel.add(jtfP1);
            JPanel p2Panel = new JPanel();
                jtfP2 = new JTextField("0", 10);
                p2Panel.add( new JLabel("Player 2 Score", JLabel.RIGHT) );
                p2Panel.add(jtfP2);
            JPanel whoseTurn = new JPanel();
                jtfCurrPlayer = new JTextField("Player 1");
                whoseTurn.add( new JLabel("Player Turn: ", JLabel.RIGHT) );
                whoseTurn.add(jtfCurrPlayer);
                
            
        scorePanel.add(p1Panel, BorderLayout.NORTH);
        scorePanel.add(p2Panel, BorderLayout.CENTER);
        scorePanel.add(whoseTurn, BorderLayout.SOUTH);
            
        jtfP1.setFont(font1);
        jtfP2.setFont(font1);
        jtfCurrPlayer.setFont(font1);
        jtfP1.setHorizontalAlignment(JTextField.CENTER);
        jtfP2.setHorizontalAlignment(JTextField.CENTER);
        jtfCurrPlayer.setHorizontalAlignment(JTextField.CENTER);
        jtfCurrPlayer.setEditable(false);
    }
    
    /*
        updateScore reads how many
        of each piece are on the board
        and attribute one point per piece to 
        each respective player
    */
    public void updateScore()
    {
        p1Score = 0;
        p2Score = 0;
        
        for(int row = 0; row < boardPcs.length; row++)
        {
            for(int col = 0; col < boardPcs.length; col++)
            {
                if(boardPcs[row][col].getIcon().equals(p1Icon))
                {
                    p1Score++;
                }
                else if(boardPcs[row][col].getIcon().equals(p2Icon))
                {
                    p2Score++;
                }
            }
        }
        jtfP1.setText("" + p1Score);
        jtfP2.setText("" + p2Score);
    }
    
    /*
        isValidMove checks horizontally,
        vertically, and diagonally for an
        opponents piece, stores them and later
        flips the stored pieces to the other player's 
        icon
        @return true or false
    */
    public boolean isValidMove()
    {
        int trueCounter = 0;
        if(selectionIcon.equals(empty))
        {
            if(isVerticalValid())
            {
                flipPieces(verticalPieces);
                trueCounter++;
            }
            if(isHorizontalValid())
            {
                flipPieces(horizontalPieces);
                trueCounter++;
            }
            if(isDiagonalValid())
            {
                flipPieces(diagonalPieces);
                trueCounter++;
            }
            if(trueCounter > 0)
            {
                return true;
            }
            else
            {
                return false;
            }    
        }    
        return false;
    }
    
    /*
        isHorizontalValid checks to the
        left and right of a spot
        for an opponents piece and then 
        one of their own beside that
    */
    public boolean isHorizontalValid()
    {
        if(playerTurn == 1)
        {
            if(yPos < 6 && yPos >= 0)
            {   //check right side of spot
                if(boardPcs[xPos][yPos + 1].getIcon().equals(p2Icon))
                {
                    for(int i = yPos + 1; i < boardPcs.length; i++)
                    {
                        if(i + 1 != 8)
                        {
                            if(boardPcs[xPos][i + 1].getIcon().equals(p1Icon))
                            {
                                horizontalPieces.add(boardPcs[xPos][i]);
                                return true;
                            }
                            else if(boardPcs[xPos][i + 1].getIcon().equals(empty))
                            {
                                horizontalPieces.clear();
                                return false;
                            }
                            else
                            {
                                horizontalPieces.add(boardPcs[xPos][i]);
                            }
                        }
                        else
                        {
                            horizontalPieces.clear();
                            return false;
                        }
                    }
                }
            }
            if(yPos > 1 && yPos <= 7)
            {   //check left side of spot
                if(boardPcs[xPos][yPos - 1].getIcon().equals(p2Icon))
                {
                    for(int i = yPos - 1; i >= 0; i--)
                    {
                        if(i - 1 != -1)
                        {
                            if(boardPcs[xPos][i - 1].getIcon().equals(p1Icon))
                            {
                                horizontalPieces.add(boardPcs[xPos][i]);
                                return true;
                            }
                            else if(boardPcs[xPos][i - 1].getIcon().equals(empty))
                            {
                                horizontalPieces.clear();
                                return false;
                            }
                            else
                            {
                                horizontalPieces.add(boardPcs[xPos][i]);
                            }
                        }
                        else
                        {
                            horizontalPieces.clear();
                            return false;
                        }
                    }
                }
            }
            
        }
        else if(playerTurn == 2)
        {
            if(yPos < 6 && yPos >= 0)
            {   //check right side of spot
                if(boardPcs[xPos][yPos + 1].getIcon().equals(p1Icon))
                {
                    for(int i = yPos + 1; i < boardPcs.length; i++)
                    {
                        if(i + 1 != 8)
                        {
                            if(boardPcs[xPos][i + 1].getIcon().equals(p2Icon))
                            {
                                horizontalPieces.add(boardPcs[xPos][i]);
                                return true;
                            }
                            else if(boardPcs[xPos][i + 1].getIcon().equals(empty))
                            {
                                horizontalPieces.clear();
                                return false;
                            }
                            else
                            {
                                horizontalPieces.add(boardPcs[xPos][i]);
                            }
                        }
                        else
                        {
                            horizontalPieces.clear();
                            return false;
                        }
                    }
                }
            }
            if(yPos > 1 && yPos <= 7)
            {   //check left side of spot
                if(boardPcs[xPos][yPos - 1].getIcon().equals(p1Icon))
                {
                    for(int i = yPos - 1; i >= 0; i--)
                    {
                        if(i - 1 != -1)
                        {
                            if(boardPcs[xPos][i - 1].getIcon().equals(p2Icon))
                            {
                                horizontalPieces.add(boardPcs[xPos][i]);
                                return true;
                            }
                            else if(boardPcs[xPos][i - 1].getIcon().equals(empty))
                            {
                                horizontalPieces.clear();
                                return false;
                            }
                            else
                            {
                                horizontalPieces.add(boardPcs[xPos][i]);
                            }
                        }
                        else
                        {
                            horizontalPieces.clear();
                            return false;
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    /*
        isVerticalValid checks above
        and below a spot for an opponents piece
        and then one of their own beside that
    */
    public boolean isVerticalValid()
    {
        if(playerTurn == 1)
        {
            if(xPos < 6 && xPos >= 0)
            {   //check below spot
                if(boardPcs[xPos + 1][yPos].getIcon().equals(p2Icon))
                {
                    for(int i = xPos + 1; i < boardPcs.length; i++)
                    {
                        if(i + 1 != 8)
                        {
                            if(boardPcs[i + 1][yPos].getIcon().equals(p1Icon))
                            {
                                verticalPieces.add(boardPcs[i][yPos]);
                                return true;
                            }
                            else if(boardPcs[i+1][yPos].getIcon().equals(empty))
                            {
                                verticalPieces.clear();
                                return false;
                            }
                            else
                            {
                                verticalPieces.add(boardPcs[i][yPos]);
                            }
                        }
                        else
                        {
                            verticalPieces.clear();
                            return false;
                        }
                    }
                }
            }
            if(xPos > 1 && xPos <= 7)
            {   //check above spot
                if(boardPcs[xPos - 1][yPos].getIcon().equals(p2Icon))
                {
                    for(int i = xPos - 1; i >= 0; i--)
                    {
                        if(i - 1 != -1)
                        {
                            if(boardPcs[i-1][yPos].getIcon().equals(p1Icon))
                            {
                                verticalPieces.add(boardPcs[i][yPos]);
                                return true;
                            }
                            else if(boardPcs[i-1][yPos].getIcon().equals(empty))
                            {
                                verticalPieces.clear();
                                return false;
                            }
                            else
                            {
                                verticalPieces.add(boardPcs[i][yPos]);
                            }
                        }
                        else
                        {
                            verticalPieces.clear();
                            return false;
                        }
                    }
                }
            }
        }
        else if(playerTurn == 2)
        {
            if(xPos < 6 && xPos >= 0)
            {   //check below spot
                if(boardPcs[xPos + 1][yPos].getIcon().equals(p1Icon))
                {
                    for(int i = xPos + 1; i < boardPcs.length; i++)
                    {
                        if(i + 1 != 8)
                        {
                            if(boardPcs[i + 1][yPos].getIcon().equals(p2Icon))
                            {
                                verticalPieces.add(boardPcs[i][yPos]);
                                return true;
                            }
                            else if(boardPcs[i+1][yPos].getIcon().equals(empty))
                            {
                                verticalPieces.clear();
                                return false;
                            }
                            else
                            {
                                verticalPieces.add(boardPcs[i][yPos]);
                            }
                        }
                        else
                        {
                            verticalPieces.clear();
                            return false;
                        }
                    }
                }
            }
            if(xPos > 1 && xPos <= 7)
            {   //check above spot
                if(boardPcs[xPos - 1][yPos].getIcon().equals(p1Icon))
                {
                    for(int i = xPos - 1; i >= 0; i--)
                    {
                        if(i - 1 != -1)
                        {
                            if(boardPcs[i-1][yPos].getIcon().equals(p2Icon))
                            {
                                verticalPieces.add(boardPcs[i][yPos]);
                                return true;
                            }
                            else if(boardPcs[i-1][yPos].getIcon().equals(empty))
                            {
                                verticalPieces.clear();
                                return false;
                            }
                            else
                            {
                                verticalPieces.add(boardPcs[i][yPos]);
                            }
                        }
                        else
                        {
                            verticalPieces.clear();
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /*
        isDiagonalValid returns a boolean
        and checks diagonally around a spot
        for an opponents piece and another 
        piece trapping that piece
    */
    public boolean isDiagonalValid()
    {
        if(playerTurn == 1)
        {   //check top left corner
            if(yPos > 0 && xPos > 0)
            {
                if(boardPcs[xPos-1][yPos-1].getIcon().equals(p2Icon))
                {
                    tempXPos = xPos - 1;
                    tempYPos = yPos - 1;
                    while(!boardPcs[tempXPos][tempYPos].getIcon().equals(empty))
                    {
                        if(tempYPos > 0 && tempXPos > 0)
                        {
                            if(boardPcs[tempXPos - 1][tempYPos - 1].getIcon().equals(p1Icon))
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                return true;
                            }
                            else
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                tempXPos--;
                                tempYPos--;
                            }
                        }
                        else
                        {
                            diagonalPieces.clear();
                            return false;
                        }    
                    }
                    diagonalPieces.clear();
                    return false;                }
            }
            //check top right corner
            if(yPos < 7 && xPos > 0)
            {
                if(boardPcs[xPos-1][yPos+1].getIcon().equals(p2Icon))
                {
                    tempXPos = xPos - 1;
                    tempYPos = yPos + 1;
                    while(!boardPcs[tempXPos][tempYPos].getIcon().equals(empty))
                    {
                        if(tempYPos < 7 && tempXPos > 0)
                        {
                            if(boardPcs[tempXPos - 1][tempYPos + 1].getIcon().equals(p1Icon))
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                return true;
                            }
                            else
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                tempXPos--;
                                tempYPos++;
                            }
                        }
                        else
                        {
                            diagonalPieces.clear();
                            return false;
                        }    
                    }
                    diagonalPieces.clear();
                    return false;
                }
            }
            //check bottom left corner
            if(yPos > 0 && xPos < 7)
            {
                if(boardPcs[xPos+1][yPos-1].getIcon().equals(p2Icon))
                {
                    tempXPos = xPos + 1;
                    tempYPos = yPos - 1;
                    while(!boardPcs[tempXPos][tempYPos].getIcon().equals(empty))
                    {
                        if(tempXPos < 7 && tempYPos > 0)
                        {
                            if(boardPcs[tempXPos + 1][tempYPos - 1].getIcon().equals(p1Icon))
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                return true;
                            }
                            else
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                tempXPos++;
                                tempYPos--;
                            }
                        }
                        else
                        {
                            diagonalPieces.clear();
                            return false;
                        }    
                    }
                    diagonalPieces.clear();
                    return false;
                }
            }
            //check bottom right corner
            if(yPos < 7 && xPos < 7)
            {
                if(boardPcs[xPos+1][yPos+1].getIcon().equals(p2Icon))
                {
                    tempXPos = xPos + 1;
                    tempYPos = yPos + 1;
                    while(!boardPcs[tempXPos][tempYPos].getIcon().equals(empty))
                    {
                        if(tempXPos  < 7 && tempYPos < 7)
                        {
                            if(boardPcs[tempXPos + 1][tempYPos + 1].getIcon().equals(p1Icon))
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                return true;
                            }
                            else
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                tempXPos++;
                                tempYPos++;
                            }
                        }
                        else
                        {
                            diagonalPieces.clear();
                            return false;
                        }    
                    }
                    diagonalPieces.clear();
                    return false;
                }
            }
        }
        else if(playerTurn == 2)
        {
            //check top left corner
            if(yPos > 0 && xPos > 0)
            {
                if(boardPcs[xPos-1][yPos-1].getIcon().equals(p1Icon))
                {
                    tempXPos = xPos - 1;
                    tempYPos = yPos - 1;
                    while(!boardPcs[tempXPos][tempYPos].getIcon().equals(empty))
                    {
                        if(tempXPos > 0 && tempYPos > 0)
                        {
                            if(boardPcs[tempXPos - 1][tempYPos - 1].getIcon().equals(p2Icon))
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                return true;
                            }
                            else
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                tempXPos--;
                                tempYPos--;
                            }
                        }
                        else
                        {
                            diagonalPieces.clear();
                            return false;
                        }
                    }
                    diagonalPieces.clear();
                    return false;
                }
            }
            //check top right corner
            if(yPos < 7 && xPos > 0)
            {
                if(boardPcs[xPos-1][yPos+1].getIcon().equals(p1Icon))
                {
                    tempXPos = xPos - 1;
                    tempYPos = yPos + 1;
                    while(!boardPcs[tempXPos][tempYPos].getIcon().equals(empty))
                    {
                        if(tempYPos < 7 && tempXPos > 0)
                        {
                            if(boardPcs[tempXPos - 1][tempYPos + 1].getIcon().equals(p2Icon))
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                return true;
                            }
                            else
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                tempXPos--;
                                tempYPos++;
                            }
                        }
                        else
                        {
                            diagonalPieces.clear();
                            return false;
                        }    
                    }
                    diagonalPieces.clear();
                    return false;
                }
            }
            //check bottom left corner
            if(yPos > 0 && xPos < 7)
            {
                if(boardPcs[xPos+1][yPos-1].getIcon().equals(p1Icon))
                {
                    tempXPos = xPos + 1;
                    tempYPos = yPos - 1;
                    while(!boardPcs[tempXPos][tempYPos].getIcon().equals(empty))
                    {
                        if(tempXPos < 7 && tempYPos > 0)
                        {
                            if(boardPcs[tempXPos + 1][tempYPos - 1].getIcon().equals(p2Icon))
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                return true;
                            }
                            else
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                tempXPos++;
                                tempYPos--;
                            }
                        }
                        else
                        {
                            diagonalPieces.clear();
                            return false;
                        }    
                    }
                    diagonalPieces.clear();
                    return false;
                }
            }
            //check bottom right corner
            if(yPos < 7 && xPos < 7)
            {
                if(boardPcs[xPos+1][yPos+1].getIcon().equals(p1Icon))
                {
                    tempXPos = xPos + 1;
                    tempYPos = yPos + 1;
                    while(!boardPcs[tempXPos][tempYPos].getIcon().equals(empty))
                    {
                        if(tempXPos < 7 && tempYPos < 7)
                        {
                            if(boardPcs[tempXPos + 1][tempYPos + 1].getIcon().equals(p2Icon))
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                return true;
                            }
                            else
                            {
                                diagonalPieces.add(boardPcs[tempXPos][tempYPos]);
                                tempXPos++;
                                tempYPos++;
                            }
                        }
                        else
                        {
                            diagonalPieces.clear();
                            return false;
                        }    
                    }
                    diagonalPieces.clear();
                    return false;
                }
            }
        }
        return false;
    }
    
    /*
        flipPieces changes the icons
        of jbuttons in an arraylist
        to that of the opposing player
    */
    public void flipPieces(ArrayList<JButton> flipList)
    {
        if(playerTurn == 1)
        {
            for(JButton flipPiece: flipList)
            {
                flipPiece.setIcon(p1Icon);
            }
            flipList.clear();
        }
        else if(playerTurn == 2)
        {
            for(JButton flipPiece: flipList)
            {
                flipPiece.setIcon(p2Icon);
            }
            flipList.clear();
        }
    }
    
    /*
        isWinner returns a boolean
        and determines the winner when 
        there are no empty spaces on the board.
    */
    public boolean isWinner()
    {
        int emptyIconCounter = 0;
        
        for(int row = 0; row < boardPcs.length; row++)
        {
            for(int col = 0; col < boardPcs.length; col++)
            {
                if(boardPcs[row][col].getIcon().equals(empty))
                {
                    emptyIconCounter++;
                }
            }
        }
        
        if(emptyIconCounter == 0)//
        {
            if(p1Score > p2Score)
            {
                winner = "Player 1 has won with " + p1Score + " points!";
                return true;
            }
            else if(p2Score > p1Score)
            {
                winner = "Player 2 has won with " + p2Score + " points!";
                return true;
            }
            else if(p1Score == p2Score)
            {
                winner = "Player 1 and Player 2 have tied with " + p1Score + " points!";
                return true;
            }
        }
        return false;
    }
}//end class
