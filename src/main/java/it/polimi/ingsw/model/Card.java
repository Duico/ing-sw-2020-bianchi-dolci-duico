package it.polimi.ingsw.model;

public class Card {
    private String name;
    private MoveStrategy moveStrategy;
    private BuildStrategy buildStrategy;
    private WinStrategy winStrategy;
    private BlockStrategy blockStrategy;
    private OpponentStrategy opponentStrategy;

    public Card(String name, MoveStrategy moveStrategy, BuildStrategy buildStrategy, WinStrategy winStrategy, BlockStrategy blockStrategy, OpponentStrategy opponentStrategy){
        this.name=name;
        this.moveStrategy=moveStrategy;
        this.buildStrategy=buildStrategy;
        this.winStrategy=winStrategy;
        this.blockStrategy=blockStrategy;
        this.opponentStrategy=opponentStrategy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoveStrategy() {
        return moveStrategy;
    }

    public void setMoveStrategy(String moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    public String getBuildStrategy() {
        return buildStrategy;
    }

    public void setBuildStrategy(String buildStrategy) {
        this.buildStrategy = buildStrategy;
    }

    public String getWinStrategy() {
        return winStrategy;
    }

    public void setWinStrategy(String winStrategy) {
        this.winStrategy = winStrategy;
    }

    public String getBlockStrategy() {
        return blockStrategy;
    }

    public void setBlockStrategy(String blockStrategy) {
        this.blockStrategy = blockStrategy;
    }

    public String getOpponentStrategy() {
        return opponentStrategy;
    }

    public void setOpponentStrategy(String opponentStrategy) {
        this.opponentStrategy = opponentStrategy;
    }
}
