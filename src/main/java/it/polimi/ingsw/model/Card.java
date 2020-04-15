package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import it.polimi.ingsw.model.strategy.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a God card with strategies to be used by the worker
 */
public class Card implements Serializable {
    private String name;
    private MoveStrategy moveStrategy;
    private BuildStrategy buildStrategy;
    private WinStrategy winStrategy;
    private BlockStrategy blockStrategy;
    private OpponentStrategy opponentStrategy;

    /**
     *
     * @param name Name of the God
     * @param moveStrategy  Name of the strategy for movement
     * @param buildStrategy Name of the strategy for building
     * @param winStrategy Name of the strategy for winning
     * @param blockStrategy Name of the strategy for blocking next player's movement
     * @param opponentStrategy Name of the strategy for opponents that occupy the desired cell
     */
    public Card (String name, String moveStrategy, String buildStrategy, String winStrategy, String blockStrategy, String opponentStrategy) throws StrategyNameNotFound {
        this.name=name;
        setMoveStrategy(moveStrategy);
        setBuildStrategy(buildStrategy);
        setBlockStrategy(blockStrategy);
        setOpponentStrategy(opponentStrategy);
        setWinStrategy(winStrategy);

    }

    public static Card getDefaultCard(){
        try {
            Card defaultCard = new Card("Default", "Default", "Default", "Default", "Default", "Default");
            return defaultCard;
        }catch(StrategyNameNotFound e){
            throw new RuntimeException("Couldn't create default Card");
        }
    }

    public String getName() {
        return name;
    }
    public MoveStrategy getMoveStrategy() {
        return moveStrategy;
    }
    public BuildStrategy getBuildStrategy() {
        return buildStrategy;
    }
    public WinStrategy getWinStrategy() {
        return winStrategy;
    }
    public BlockStrategy getBlockStrategy() {
        return blockStrategy;
    }
    public OpponentStrategy getOpponentStrategy() {
        return opponentStrategy;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setMoveStrategy(String moveStrategy) throws StrategyNameNotFound {

        if (moveStrategy.equals("Default")){
            this.moveStrategy = new DefaultMove();
        }
        else if(moveStrategy.contentEquals("Multiple")){
            this.moveStrategy = new MultipleMove();
        }
        else if(moveStrategy.contentEquals("Push")){
            this.moveStrategy = new PushMove();
        }
        else if(moveStrategy.contentEquals("BuildAtFirst")){
            this.moveStrategy = new BuildAtFirstMove();
        }else {
            throw new StrategyNameNotFound();
        }
    }

    private void setBuildStrategy(String buildStrategy) throws StrategyNameNotFound {

        if (buildStrategy.contentEquals("Default")){
            this.buildStrategy = new DefaultBuild();
        }
        else if (buildStrategy.contentEquals("Dome")){
            this.buildStrategy = new DomeBuild();
        }
        else if (buildStrategy.contentEquals("Multiple")){
            this.buildStrategy = new MultipleBuild();
        }
        else if (buildStrategy.contentEquals("BuildAtFirst")){
            this.buildStrategy = new BuildAtFirstBuild();
        }else {
            throw new StrategyNameNotFound();
        }
    }

    private void setWinStrategy(String winStrategy) throws StrategyNameNotFound {
        if (winStrategy.contentEquals("Default")){
            this.winStrategy = new DefaultWin();
        }
        else if (winStrategy.contentEquals("DownMove")){
            this.winStrategy = new DownMoveWin();
        }else {
            throw new StrategyNameNotFound();
        }
    }

    private void setBlockStrategy(String blockStrategy) throws StrategyNameNotFound {
        if (blockStrategy.contentEquals("Default")){
            this.blockStrategy = new DefaultBlock();
        }
        else if (blockStrategy.contentEquals("UpMove")){
            this.blockStrategy = new UpMoveBlock();
        }else {
            throw new StrategyNameNotFound();
        }
    }

    private void setOpponentStrategy(String opponentStrategy) throws StrategyNameNotFound {

        if (opponentStrategy.contentEquals("Default")){
            this.opponentStrategy = new DefaultOpponent();
        }
        else if (opponentStrategy.contentEquals("Swap")){
            this.opponentStrategy = new SwapOpponent();
        }
        else if (opponentStrategy.contentEquals("Push")){
            this.opponentStrategy = new PushOpponent();
        }else {
            throw new StrategyNameNotFound();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(getName(), card.getName()) &&
                Objects.equals(getMoveStrategy(), card.getMoveStrategy()) &&
                Objects.equals(getBuildStrategy(), card.getBuildStrategy()) &&
                Objects.equals(getBlockStrategy(), card.getBlockStrategy()) &&
                Objects.equals(getOpponentStrategy(), card.getOpponentStrategy()) &&
                Objects.equals(getWinStrategy(), card.getWinStrategy());

        //no need to check functions
    }

}
