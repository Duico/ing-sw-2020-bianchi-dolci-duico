package it.polimi.ingsw.model;

public class Card {
    private String name;
    private MoveStrategy moveStrategy;
    private BuildStrategy buildStrategy;
    private WinStrategy winStrategy;
    private BlockStrategy blockStrategy;
    private OpponentStrategy opponentStrategy;

    public Card(String name, String moveStrategy, String buildStrategy, String winStrategy, String blockStrategy, String opponentStrategy){

        this.name=name;
        setMoveStrategy(moveStrategy);
        setBuildStrategy(buildStrategy);
        setBlockStrategy(blockStrategy);
        setOpponentStrategy(opponentStrategy);
        setWinStrategy(winStrategy);
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

    private void setMoveStrategy(String moveStrategy) {

        if (moveStrategy.contentEquals("Default")){
            this.moveStrategy = new DefaultMove();
        }
        else if(moveStrategy.contentEquals("Multiple")){
            this.moveStrategy = new MultipleMove();
        }
        else if(moveStrategy.contentEquals("Swap")){
            this.moveStrategy = new SwapMove();
        }
        else if(moveStrategy.contentEquals("Push")){
            this.moveStrategy = new PushMove();
        }
        else if(moveStrategy.contentEquals("BuildAtFirst")){
            this.moveStrategy = new BuildAtFirstMove();
        }
    }

    private void setBuildStrategy(String buildStrategy) {

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
        }
    }

    public void setWinStrategy(String winStrategy) {
        if (winStrategy.contentEquals("Default")){
            this.winStrategy = new DefaultWin();
        }
        else if (winStrategy.contentEquals("DownMove")){
            this.winStrategy = new DownMoveWin();
        }
    }

    public void setBlockStrategy(String blockStrategy) {
        if (blockStrategy.contentEquals("Default")){
            this.blockStrategy = new DefaultBlock();
        }
        else if (blockStrategy.contentEquals("UpMove")){
            this.blockStrategy = new UpMoveBlock();
        }
    }

    public void setOpponentStrategy(String opponentStrategy) {

        if (opponentStrategy.contentEquals("Default")){
            this.opponentStrategy = new DefaultOpponent();
        }
        else if (opponentStrategy.contentEquals("Swap")){
            this.opponentStrategy = new SwapOpponent();
        }
        else if (opponentStrategy.contentEquals("Push")){
            this.opponentStrategy = new PushOpponent();
        }
    }
}
