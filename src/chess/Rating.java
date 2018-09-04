package chess;
public class Rating {
    static int material;
    static int pawnBoard[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        {50, 50, 50, 50, 50, 50, 50, 50},
        {10, 10, 20, 30, 30, 20, 10, 10},
        { 5,  5, 10, 25, 25, 10,  5,  5},
        { 0,  0,  0, 20, 20,  0,  0,  0},
        { 5, -5,-10,  0,  0,-10, -5,  5},
        { 5, 10, 10,-20,-20, 10, 10,  5},
        { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][]={
        { 0,  0,  0,  0,  0,  0,  0,  0},
        { 5, 10, 10, 10, 10, 10, 10,  5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        {-5,  0,  0,  0,  0,  0,  0, -5},
        { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][]={
        {-50,-40,-30,-30,-30,-30,-40,-50},
        {-40,-20,  0,  0,  0,  0,-20,-40},
        {-30,  0, 10, 15, 15, 10,  0,-30},
        {-30,  5, 15, 20, 20, 15,  5,-30},
        {-30,  0, 15, 20, 20, 15,  0,-30},
        {-30,  5, 10, 15, 15, 10,  5,-30},
        {-40,-20,  0,  5,  5,  0,-20,-40},
        {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][]={
        {-20,-10,-10,-10,-10,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5, 10, 10,  5,  0,-10},
        {-10,  5,  5, 10, 10,  5,  5,-10},
        {-10,  0, 10, 10, 10, 10,  0,-10},
        {-10, 10, 10, 10, 10, 10, 10,-10},
        {-10,  5,  0,  0,  0,  0,  5,-10},
        {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][]={
        {-20,-10,-10, -5, -5,-10,-10,-20},
        {-10,  0,  0,  0,  0,  0,  0,-10},
        {-10,  0,  5,  5,  5,  5,  0,-10},
        { -5,  0,  5,  5,  5,  5,  0, -5},
        {  0,  0,  5,  5,  5,  5,  0, -5},
        {-10,  5,  5,  5,  5,  5,  0,-10},
        {-10,  0,  5,  0,  0,  0,  0,-10},
        {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][]={
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-30,-40,-40,-50,-50,-40,-40,-30},
        {-20,-30,-30,-40,-40,-30,-30,-20},
        {-10,-20,-20,-20,-20,-20,-20,-10},
        { 20, 20,  0,  0,  0,  0, 20, 20},
        { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int kingEndBoard[][]={
        {-50,-40,-30,-20,-20,-30,-40,-50},
        {-30,-20,-10,  0,  0,-10,-20,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 30, 40, 40, 30,-10,-30},
        {-30,-10, 20, 30, 30, 20,-10,-30},
        {-30,-30,  0,  0,  0,  0,-30,-30},
        {-50,-30,-30,-30,-30,-30,-30,-50}};
    
    public static int rate(int depth,int list){
        material=material();
        int AIRating=0;
        int HumanRating=0;
        AIRating=attack()+moveability(list, depth, material)+material()+position();
        Chess.flipSides();
        HumanRating=attack()+moveability(list, depth, material)+material()+position();
        Chess.flipSides();  
        return -((AIRating-HumanRating)+depth*50);
    }
    public static int attack(){
        /*
        This method calculates how many of our pieces are under attack
        This value is subtracted by Computers pieces under attack
        */
        int points=0,temp=Chess.aiKingPosition;
        for(int i=0;i<64;i++){
            switch(Chess.board[i/8][i%8]){
                case "P" : {
                                Chess.aiKingPosition=i;
                                if(!Chess.kingSafe())points-=64;
                            }break;
                case "K" : {
                                Chess.aiKingPosition=i;
                                if(!Chess.kingSafe())points-=300;
                            }break;
                case "B" : {
                                Chess.aiKingPosition=i;
                                if(!Chess.kingSafe())points-=300;
                            }break;
                case "R" : {
                                Chess.aiKingPosition=i;
                                if(!Chess.kingSafe())points-=500;
                            }break;
                case "Q" : {
                                Chess.aiKingPosition=i;
                                if(!Chess.kingSafe())points-=900;
                            }break;
            }
        }
        Chess.aiKingPosition=temp;
        if(!Chess.kingSafe())points-=200;
        return points/2;
    }
    public static int moveability(int listLength, int depth, int material){
        /*
        This rating method calculates how many pieces are movable; 
        More the movability, more the chances of winning!!
        */
        int points=0;
        points+=listLength;//every move in list is 5 characters long
        if(listLength==0){
            if(!(Chess.kingSafe())){//checkmate
                points-=200000*depth;
            }
            else{//stalemate
                points-=150000*depth;
            }
        }
        return points;
    }
    public static int material(){
        /*
        Most important rating, 
        Calculate first players material count, subtract with other player's
        
        */
        int points=0,noOfBishops=0;
        for(int i=0;i<64;i++){
            switch(Chess.board[i/8][i%8]){
                case "P" : points+=100;break;
                case "K" : points+=300;break;
                case "B" : noOfBishops++;break;
                case "R" : points+=500;break;
                case "Q" : points+=900;break;
            }
        }
        if(noOfBishops>=2)points+=noOfBishops*300;
        else points+=noOfBishops*250;
        return points;
    }
    public static int position(){
        /*
        There are certain positions, where pawn proves to be fatal. 
        If pawn is at one of these positions, increase the rating
        
        Same for other materials as well
        */
        int points=0;
        for(int i=0;i<64;i++){
            switch(Chess.board[i/8][i%8]){
                case "P" : points+=pawnBoard[i/8][i%8];break;
                case "K" : points+=knightBoard[i/8][i%8];break;
                case "B" : points+=bishopBoard[i/8][i%8];break;
                case "R" : points+=rookBoard[i/8][i%8];break;
                case "Q" : points+=queenBoard[i/8][i%8];break;
                case "A" : points+=kingMidBoard[i/8][i%8];break;
            }
        }
        return points;
    }
}
