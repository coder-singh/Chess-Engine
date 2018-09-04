package chess;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
public class User_interface extends JPanel implements MouseListener, MouseMotionListener{
    static int x=0,y=0;
    static int mouseX, mouseY, newMouseX, newMouseY;
    public static int squareSize=64;
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.yellow);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        for(int i=0;i<64;i+=2){
            Color dark = new Color(255,200,100);
            Color light = new Color(150,50,30);
            if((i/8)%2==0)
                g.setColor(dark);
            else g.setColor(light);
            g.fillRect((i%8)*squareSize, (i/8)*squareSize, squareSize, squareSize);
            if((i/8)%2==0)
                g.setColor(light);
            else g.setColor(dark);
            g.fillRect(((i%8)+1)*squareSize, (i/8)*squareSize, squareSize, squareSize);
        }
        Image img = new ImageIcon("ChessPieces.png").getImage();
        //all pieces in this png is 32*32
        for(int i=0;i<64;i++){
        int imagex=-1,imagey=-1;
            switch(Chess.board[i/8][i%8]){
                case "A" : imagex=0;imagey=0;break;
                case "Q" : imagex=1;imagey=0;break;
                case "R" : imagex=2;imagey=0;break;
                case "B" : imagex=3;imagey=0;break;
                case "K" : imagex=4;imagey=0;break;
                case "P" : imagex=5;imagey=0;break;
                case "a" : imagex=0;imagey=1;break;
                case "q" : imagex=1;imagey=1;break;
                case "r" : imagex=2;imagey=1;break;
                case "b" : imagex=3;imagey=1;break;
                case "k" : imagex=4;imagey=1;break;
                case "p" : imagex=5;imagey=1;break;
                            
            }
            g.drawImage(img,(i%8)*64,(i/8)*64,(i%8)*64+64,(i/8)*64+64,imagex*64,imagey*64,(imagex+1)*64,(imagey+1)*64,this);
        }
//        System.out.print(mouseX+"*"+mouseY+"*"+newMouseX+"*"+newMouseY);
    }
    public void mouseMoved(MouseEvent e){repaint();}
    public void mousePressed(MouseEvent e){
        x=e.getX();
        y=e.getY();
        if((x<squareSize*8 && y<squareSize*8)){
            mouseX=x;
            mouseY=y;
            repaint();
        }
    }
    public void mouseReleased(MouseEvent e){
        if(x<squareSize*8 && y<squareSize*8){
            newMouseX=e.getX();
            newMouseY=e.getY();
            if(e.getButton()==MouseEvent.BUTTON1){
                String dragMove;
                if(newMouseY/squareSize==0 && mouseY/squareSize==1 && "P".equals(Chess.board[mouseY/squareSize][mouseX/squareSize])){
                    //pawn promotion
                    //captured->
                    dragMove=""+mouseX/squareSize+newMouseX/squareSize+Chess.board[0][newMouseX/64]+"QP";
//                    System.out.println(dragMove);
                }
                else{
                    //regular move
                    dragMove=""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+Chess.board[newMouseY/squareSize][newMouseX/squareSize];
                }
                String list = Chess.possibleMoves();
                if((list.replaceAll(dragMove, "")).length() < list.length()){
                    Chess.makeMove(dragMove);
                    Chess.see();
                    System.out.println(list);
                    Chess.flipSides();
                    Chess.makeMove(Chess.alphaBeta(Chess.globalDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, "", -1).move);
                    Chess.flipSides();
                    repaint();
                }
            }
        }
    }
    public void mouseClicked(MouseEvent e){}
    public void mouseDragged(MouseEvent e){
        
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
