package it.polimi.ingsw.model;

//has now also taken the role of SwapMove
public class PushMove implements MoveStrategy {
    @Override
    public boolean isValidMove(Worker worker, BoardCell cell) {

        try {
            BoardCell startCell = worker.getCell();
            int dx = cell.getPosition().getX() - startCell.getPosition().getX();
            int dy = cell.getPosition().getY() - startCell.getPosition().getY();
            if(dx==0 && dy==0)
                return false;
            else if(dx < -1 || dx > 1 || dy < -1 || dy > 1)
                return false;
            else if(cell.hasDome() == true)
                return false;
            else if((startCell.getLevel().ordinal()+1) >= (cell.getLevel().ordinal()))
                return true;
            else
                return false;
        }catch (NullPointerException e){
            throw new NullPointerException();
        }
        // how to control if the next level is the dome?
        // possibility of exception?

    }

}
