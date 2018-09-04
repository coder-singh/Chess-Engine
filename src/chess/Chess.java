package chess;
import javax.swing.*;

class abreturn{
    /*
    We need to return 2 values
      1) Move (String)
      2) Score (Integer)
    Object of this class can be returned
    */
    abreturn(){};
    abreturn(String move, int score){
        this.move=move;
        this.score=score;
    }
    String move;
    int score;
}

public class Chess {
    static int globalDepth=1;
    static int First=1;
    
    //2d String array denoting Chess-board
    static String board[][]={
        {"r","k","b","q","a","b","k","r"},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"},
        {"R","K","B","Q","A","B","K","R"}};

    static int aiKingPosition,humanKingPosition;
    public static void main(String[] args) {
        
        while(!"A".equals(board[aiKingPosition/8][aiKingPosition%8])){
            //Searching Computer's King position.
            aiKingPosition++;
        }
        
        while(!"a".equals(board[humanKingPosition/8][humanKingPosition%8])){
            //Searching Human's King position
            humanKingPosition++;
        }
        
        JFrame f = new JFrame("Chess engine");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        User_interface ui = new User_interface();
        f.add(ui);
        f.setSize(User_interface.squareSize*8+16,User_interface.squareSize*8+39);
        f.setVisible(true);
        
        //Selecting First Player
        Object option[] = {"AI", "Human"};
        First=JOptionPane.showOptionDialog(null, "Who should play first ?","Select",JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,option,option[1]);
        
        //Setting Depth (1-4)
        globalDepth = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the depth (1-4)"));
        
        if(First==0){
            /*
            AI plays first
            startTime and endTime used to calculate the time elapsed to compute single move.
            */
            long startTime = System.currentTimeMillis();
            abreturn x = new abreturn();
            
            //Calling Alpha Beta algorithm with globalDepth
            x=alphaBeta(globalDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, "", -1);
            makeMove(x.move);
            long endTime = System.currentTimeMillis();
            System.out.println("it took "+(endTime-startTime)+" milli seconds");
            flipSides();
            f.repaint();
            see();
        }
    }

    public static void see(){
        /*
        This function prints present board condition in the console
        */
        System.out.println("  --  --  --  --  --  --  --  -- ");
        for(int i=0;i<8;i++){
            System.out.print("| ");
            for(int j=0;j<8;j++){
                System.out.print(board[i][j]+" | ");
            }
            System.out.println("\n  --  --  --  --  --  --  --  -- ");
        }
    }
    public static abreturn alphaBeta(int depth, int alpha, int beta, String move, int player){
        //Call alpha beta function
        String list = possibleMoves();
        //-1 for ai and +1 for human
        if(depth==0 || list.length()==0){
            return new abreturn(move,Rating.rate(depth,list.length())*player);
        }
        
        /*
        If current player = 1(human), set it to -1 (Computer)
        If current player = -1(Computer), set it to 1 (human)
        */
        if(player==-1)
            player=1;
        else
            player=-1; 
        
        for(int i=0;i<list.length();i+=5){
            String newMove = list.substring(i,i+5);
            makeMove(newMove);
            
            /*
            flipSides function to play role of other player to calculate score.
            */
            flipSides();
            abreturn a = new abreturn();
            a = alphaBeta(depth-1, alpha, beta, newMove, player);
            int score = a.score;
            flipSides();
            undoMove(newMove);
            
            if(player==-1){
                if(score<=beta){
                    beta=score;
                    if(depth==globalDepth)
                        move=a.move;
                }
            }
            else{
                if(score>alpha){
                    alpha=score;
                    if(depth==globalDepth)
                        move=a.move;
                }
            }
            if(alpha>=beta){
                if(player==-1)
                    return new abreturn(move,beta);
                else return new abreturn(move,alpha);
            }
        }
        //sorting the moves will increase efficiency of the game
        if(player==-1)return new abreturn(move,beta);
        else return new abreturn(move,alpha);
    }
    public static int rating(){
        return 0;
    }
    public static void flipSides(){
        for(int i=0;i<4;i++){
            for(int j=0;j<8;j++){
                String temp = board[7-i][7-j];
                if(Character.isUpperCase(temp.charAt(0)))
                    temp=temp.toLowerCase();
                else
                    temp=temp.toUpperCase();
                if(Character.isUpperCase(board[i][j].charAt(0)))
                    board[7-i][7-j]=board[i][j].toLowerCase();
                else
                    board[7-i][7-j]=board[i][j].toUpperCase();
                board[i][j]=temp;
            }
        }
        int temp = 63-aiKingPosition;    //this is enemy king's position
        aiKingPosition = 63-humanKingPosition;   //this is our king's position
        humanKingPosition = temp;
    }
    public static void makeMove(String move){
        /*
        move is a 5 character string
        (x,y) of initial position
        (x,y) of final position
        last character is for a piece captured; If piece is captured, piece is last character else ' '.
        
        Character.getNumericValue(move.charAt(i)) will give integer value of character at position i
        */
        if(move.charAt(4)!='P'){
            board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
            board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=" ";
            if("A".equals(board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])){
                aiKingPosition = 8*Character.getNumericValue(move.charAt(2))+Character.getNumericValue(move.charAt(3));
            }
        }
        else{
            board[1][Character.getNumericValue(move.charAt(0))]=" ";
            board[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(3));
        }
    }
    public static void undoMove(String move){
        /*
        After calculating move for single step, board should be set to previous state
        */
        if(move.charAt(4)!='P'){
            board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))]=board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
            board[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))]=String.valueOf(move.charAt(4));
            if("A".equals(board[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])){
                aiKingPosition = 8*Character.getNumericValue(move.charAt(0))+Character.getNumericValue(move.charAt(1));
            }
        }
        else{
            board[1][Character.getNumericValue(move.charAt(0))]="P";
            board[0][Character.getNumericValue(move.charAt(1))]=String.valueOf(move.charAt(2));
        }
    }

    public static String possibleMoves(){
        String list="";
        for(int i=0;i<64;i++){
            /*
            Iterate through every position on the board
            */
            switch(board[i/8][i%8]){
                
                //If PAWN is encountered, append all possible pawn moves to list
                case "P" : list+=possibleP(i);
                    break;
                    
                //If ROOK is encountered, append all possible rook moves to list                    
                case "R" : list+=possibleR(i);
                  break;
                    
                //If KNIGHT is encountered, append all possible knight moves to list                    
                case "K" : list+=possibleK(i);
                  break;
                    
                //If BISHOP is encountered, append all possible bishop moves to list                    
                case "B" : list+=possibleB(i);
                break;
                    
                //If QUEEN is encountered, append all possible QUEEN moves to list                    
                case "Q" : list+=possibleQ(i);
                  break;
                    
                //If KING is encountered, append all possible king moves to list                    
                case "A" : list+=possibleA(i);
                  break;
            }
        }
        return list;
    }

        //returns total possible pawn moves
    public static String possibleP(int i){
       // System.out.println("inside possible p");
        String list = "",oldPiece="";
        if((i/8)==6){
            if(" ".equals(board[i/8-2][i%8]) && " ".equals(board[i/8-1][i%8])){
            board[i/8-2][i%8]="P";
            board[i/8][i%8]=" ";
            if(kingSafe())
                list=list+(i/8)+(i%8)+(i/8-2)+(i%8)+" ";
            board[i/8][i%8]="P";
            board[i/8-2][i%8]=" ";
        }
        }
        if(" ".equals(board[i/8-1][i%8]) && i>=16){
            board[i/8-1][i%8]="P";
            board[i/8][i%8]=" ";
            if(kingSafe())
                list=list+(i/8)+(i%8)+(i/8-1)+(i%8)+" ";
            board[i/8][i%8]="P";
            board[i/8-1][i%8]=" ";
        }
        if(" ".equals(board[i/8-1][i%8]) && i<16){
            String[] tmp = {"Q","R","B","K"};
            for(int loop=0;loop<4;loop++){
                board[i/8][i%8]=" ";
                board[i/8-1][i%8]=tmp[loop];
                if(kingSafe())
                    list=list+(i%8)+(i%8)+" "+tmp[loop]+"P";
                board[i/8][i%8]="P";
                board[i/8-1][i%8]=" ";
            }
        }
        for(int j=-1;j<=1;j+=2){
                //for enemy piece capturing
            try{
            if(Character.isLowerCase(board[i/8-1][i%8+j].charAt(0)) && i>=16){
                board[i/8][i%8]=" ";
                oldPiece=board[i/8-1][i%8+j];
                board[i/8-1][i%8+j]="P";
                if(kingSafe())
                    list=list+(i/8)+(i%8)+(i/8-1)+(i%8+j)+oldPiece;
                board[i/8][i%8]="P";
                board[i/8-1][i%8+j]=oldPiece;
                }
            }catch(Exception e){}
                //for piece capturing by pawn and promotion
            try{
                if(Character.isLowerCase(board[i/8-1][i%8+j].charAt(0)) && i<16){
                    String[] tmp = {"Q","R","B","K"};
                    for(int loop=0;loop<4;loop++){
                        board[i/8][i%8]=" ";
                        oldPiece=board[i/8-1][i%8+j];
                        board[i/8-1][i%8+j]=tmp[loop];
                        if(kingSafe())
    /*  we are having slight change from the other lists which we have returned,
    other lists return row,column,new row,new column,captured piece,
    here since during pawn promotion row will be decremented by one, we have to jst care about the column
    so we return column,new column,captured piece,new piece,"P" which is for promotion*/
                            list=list+(i%8)+(i%8+j)+oldPiece+tmp[loop]+"P";
                        board[i/8][i%8]="P";
                        board[i/8-1][i%8+j]=oldPiece;
                    }
                }
            }catch(Exception e){}
        }
        return list;
    }

        //return total possible rook moves
    public static String possibleR(int i){
        //System.out.println("inside possible r");
        String list = "",oldPiece;
        int r=i/8,c=i%8;
        int temp=1;
        for(int j=-1;j<=1;j++){
            for(int k=-1;k<=1;k++){
                if(Math.abs(j-k)==1)
                try{
                    while(" ".equals(board[r+temp*j][c+temp*k])){
                        oldPiece=board[r+temp*j][c+temp*k];
                        board[r][c]=" ";
                        board[r+temp*j][c+temp*k]="R";
                        if(kingSafe()){
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        board[r][c]="R";
                        board[r+temp*j][c+temp*k]=oldPiece;
                        temp++;
                    }
                    if(Character.isLowerCase(board[r+temp*j][c+temp*k].charAt(0))){
                        oldPiece=board[r+temp*j][c+temp*k];
                        board[r][c]=" ";
                        board[r+temp*j][c+temp*k]="R";
                        if(kingSafe()){
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        board[r][c]="R";
                        board[r+temp*j][c+temp*k]=oldPiece;
                    }
                }
                catch(Exception e){}
            temp=1;
            }
        }
        return list;
    }

        //returns total possible knight moves
    public static String possibleK(int i){
        //System.out.println("inside possible k");
        String list = "",oldPiece;
        int r=i/8,c=i%8;
        for(int j=-1;j<=1;j+=2){
            for(int k=-1;k<=1;k+=2){
                try{
                if(" ".equals(board[r+j][c+2*k]) || Character.isLowerCase(board[r+j][c+2*k].charAt(0))){
                    oldPiece=board[r+j][c+2*k];
                    board[r][c]=" ";
                    board[r+j][c+2*k]="K";
                    if(kingSafe()){
                        list=list+r+c+(r+j)+(c+2*k)+oldPiece;
                    }
                    board[r+j][c+2*k]=oldPiece;
                    board[r][c]="K";
                }
                }
                catch(Exception e){}
                try{
                if(" ".equals(board[r+2*j][c+k]) || Character.isLowerCase(board[r+2*j][c+k].charAt(0))){
                    //System.out.print("\n"+r+"*"+c+"->"+(r+2*j)+"*"+(c+k)+"\n");
                    oldPiece=board[r+2*j][c+k];
                    board[r][c]=" ";
                    board[r+2*j][c+k]="K";
                    if(kingSafe()){
                        list=list+r+c+(r+2*j)+(c+k)+oldPiece;
                    }
                    board[r+2*j][c+k]=oldPiece;
                    board[r][c]="K";
                }
                }
                catch(Exception e){}
            }
        }
        return list;
    }

        //returns total possible bishop moves
    public static String possibleB(int i){
        //System.out.println("inside possible b");
        String list = "",oldPiece;
        int r=i/8,c=i%8;
        int temp=1;
        for(int j=-1;j<=1;j++){
            for(int k=-1;k<=1;k++){
                if(Math.abs(j-k)!=1)
                try{
                    while(" ".equals(board[r+temp*j][c+temp*k])){
                        oldPiece=board[r+temp*j][c+temp*k];
                        board[r][c]=" ";
                        board[r+temp*j][c+temp*k]="B";
                        if(kingSafe()){
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        board[r][c]="B";
                        board[r+temp*j][c+temp*k]=oldPiece;
                        temp++;
                    }
                    if(Character.isLowerCase(board[r+temp*j][c+temp*k].charAt(0))){
                        oldPiece=board[r+temp*j][c+temp*k];
                        board[r][c]=" ";
                        board[r+temp*j][c+temp*k]="B";
                        if(kingSafe()){
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        board[r][c]="B";
                        board[r+temp*j][c+temp*k]=oldPiece;
                    }
                }
                catch(Exception e){}
            temp=1;
            }
        }
        return list;
    }

        //returns total possible queen moves
    public static String possibleQ(int i){
        //System.out.println("inside possible q");
        String list = "",oldPiece;
        int r=i/8,c=i%8;
        int temp=1;
        for(int j=-1;j<=1;j++){
            for(int k=-1;k<=1;k++){
                try{
                    while(" ".equals(board[r+temp*j][c+temp*k])){
                        oldPiece=board[r+temp*j][c+temp*k];
                        board[r][c]=" ";
                        board[r+temp*j][c+temp*k]="Q";
                        if(kingSafe()){
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        board[r][c]="Q";
                        board[r+temp*j][c+temp*k]=oldPiece;
                        temp++;
                    }
                    if(Character.isLowerCase(board[r+temp*j][c+temp*k].charAt(0))){
                        oldPiece=board[r+temp*j][c+temp*k];
                        board[r][c]=" ";
                        board[r+temp*j][c+temp*k]="Q";
                        if(kingSafe()){
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        board[r][c]="Q";
                        board[r+temp*j][c+temp*k]=oldPiece;
                    }
                }
                catch(Exception e){}
            temp=1;
            }
        }
        return list;
    }

        //returns total possible king moves
     public static String possibleA(int i){
        //System.out.println("inside possible a");
        String list = "",oldPiece;
        int r=i/8,c=i%8;
        for(int j=0;j<9;j++){
            if(j!=4){
                try{
                if(Character.isLowerCase(board[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(board[r-1+j/3][c-1+j%3])){
                    oldPiece=board[r-1+j/3][c-1+j%3];
                    board[r][c]=" ";
                    board[r-1+j/3][c-1+j%3]="A";
                    int kingTemp = aiKingPosition;
                    aiKingPosition=i+(j/3)*8+j%3-9;
                    if(kingSafe()){
                        list=list+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;
                    }
                    board[r][c]="A";
                    board[r-1+j/3][c-1+j%3]=oldPiece;
                    aiKingPosition=kingTemp;
                }
            }
                catch(Exception e){}
            }
        }
            
        return list;
    }

        //returns a boollean value after checking if the king is safe or not
     public static boolean kingSafe(){
        //System.out.println("the king position is "+aiKingPosition);
        int temp=1;
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(Math.abs(i-j)==1)
                try{
                    while(" ".equals(board[aiKingPosition/8+temp*i][aiKingPosition%8+temp*j]))
                        temp++;
                    if(("r".equals(board[aiKingPosition/8+temp*i][aiKingPosition%8+temp*j].charAt(0))) ||
                            "q".equals(board[aiKingPosition/8+temp*i][aiKingPosition%8+temp*j])){
                        return false;
                    }
                }
                catch(Exception e){}
            temp=1;
            }
        }
        
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(Math.abs(i-j)!=1)
                try{
                    while(" ".equals(board[aiKingPosition/8+temp*i][aiKingPosition%8+temp*j]))
                        temp++;
                    if(("b".equals(board[aiKingPosition/8+temp*i][aiKingPosition%8+temp*j])) ||
                            "q".equals(board[aiKingPosition/8+temp*i][aiKingPosition%8+temp*j])){
                        return false;
                    }
                }
                catch(Exception e){}
            temp=1;
            }
        }
        
        for(int i=-1;i<=1;i+=2){
            for(int j=-1;j<=1;j+=2){
                try{
                if("k".equals(board[aiKingPosition/8+i][aiKingPosition%8+2*j]))
                    return false;
                }
                catch(Exception e){}
                try{
                if("k".equals(board[aiKingPosition+2*i][aiKingPosition+j]))
                    return false;
                }
                catch(Exception e){}
            }
        }
        if(aiKingPosition>15){
            try{
                if("p".equals(board[aiKingPosition/8-1][aiKingPosition%8-1]))
                    return false;
            }catch(Exception e){}
            try{
                if("p".equals(board[aiKingPosition/8-1][aiKingPosition%8+1]))
                    return false;
            }catch(Exception e){}
        }
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(i!=0 || j!=0){
                    try{
                        if("k".equals(board[aiKingPosition+i][aiKingPosition+j]))
                            return false;
                    }
                    catch(Exception e){}
                }
            }
        }
         return true;
     }
     public static String sortMoves(String list){
         int[] score = new int[list.length()/5];
         for(int i=0;i<list.length();i+=5){
             makeMove(list.substring(i, i+5));
             score[i/5]=-Rating.rate(0,-1);
             undoMove(list.substring(i,i+5));
         }
         String newListA="", newListB=list;
         for(int i=0;i<Math.min(6, list.length()/5);i++){
             int max=-1000000, maxLocation=0;
             for(int j=0;j<list.length()/5;j++){
                 if(score[j]>max){max=score[j];maxLocation=j;}
             }
             score[maxLocation]=-1000000;
             newListA+=list.substring(maxLocation*5, maxLocation*5+5);
             newListB=newListB.replace(list.substring(maxLocation*5, maxLocation*5+5), "");
         }
         return newListA+newListB;
     }
}
