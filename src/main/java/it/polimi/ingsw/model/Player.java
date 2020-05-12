package it.polimi.ingsw.model;

        import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;

        import java.io.Serializable;
        import java.util.Objects;
        import java.util.UUID;

public class Player implements Serializable, Cloneable {
    private final String nickName;
    private final UUID uuid;
    private Card card; //FIX add final when tests are over
    private String cardName;
    private FixedArray<Worker> workers;
    private boolean isChallenger;
    private PlayerColor color; //TODO


    public Player(String nickName, PlayerColor color) {
        this.uuid = UUID.randomUUID();
        this.nickName = nickName;
        this.isChallenger = false;
        this.color = color;
        //
        //this.initWorkers(numWorkers);
    }

    //TODO remove
    public Player(String nickName){
        this(nickName, PlayerColor.randomEnum());
    }

//    private void initWorkers(int numWorkers){
//        for(int i=0; i < numWorkers; i++){
//            workers.add(new Worker());
//        }
//    }

    public Card getCard() {
        return card;
    }

    public boolean setCard(Card card) {
        if(this.card!=null)
            return false;
        else {
            this.card = card;
            return true;
        }
    }

    public String getCardName(){
        if(card!=null){
            return card.getName();
        }else{
            return cardName;
        }
    }
    public boolean setCardName(String cardName){
        boolean alreadySet = false;
        if(cardName != null){
            alreadySet = true;
        }
        this.cardName = cardName;
        return alreadySet;
    }
    public void initWorkers(int numWorkers){
        workers = new FixedArray<>(numWorkers);
    }

    public int getNumMovesWorker(int worker) {
        return workers.get(worker).getNumMoves();
    }

    public Operation getLastOperationWorker(int worker) {
        return workers.get(worker).getLastOperation();
    }

    public int getNumBuildsWorker(int worker) {
        return workers.get(worker).getNumBuilds();
    }

    public String getNickName() {
        return nickName;
    }

    public int getNumWorkers() {
        return workers.size();
    }


    public Position getWorkerPosition(int workerId) {
        Worker worker = workers.get(workerId);
        if (worker == null) {
            return null;
        }
        try {
            return worker.getCurrentPosition();
        }catch(WorkerPositionNotSetException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean isWorkerSet(int i) {
        return (workers.get(i) != null);
    }

    public int addWorker(Worker newWorker) {
        int workerId = workers.add(newWorker);
        return workerId;
    }

    public boolean isAnyWorkerNotPlaced() {
        return (workersToPlace() > 0);
    }

    private int workersToPlace() {
        return workers.nullCount();
    }

    public UUID getUuid() {
        return uuid;
    }
    public PlayerColor getColor(){
        return color;
    }

    public void resetAllWorkers() {
        for (int i = 0; i < getNumWorkers(); i++) {
            if (!isWorkerSet(i))
                continue;
            Worker worker = workers.get(i);
            worker.reset();
        }
    }
    public boolean isOwnWorkerInPosition(Position destinationPosition) {

        for (int i = 0; i < getNumWorkers(); i++) {
            try {
                if (workers.get(i).getCurrentPosition().equals(destinationPosition))
                    return true;
            }catch(WorkerPositionNotSetException e){

            }
        }
        return false;
    }

    public void setIsChallenger(boolean isChallenger){
        this.isChallenger=isChallenger;
    }

    public boolean isChallenger(){
        return this.isChallenger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        boolean eq = Objects.equals(getUuid(), player.getUuid()) &&
                Objects.equals(getNickName(), player.getNickName()) &&
                Objects.equals(getCard(), player.getCard()) &&
                Objects.equals(workers, player.workers);
        return eq;
    }
    public boolean equalsUuid(Player player){
        if (this == player) return true;
        return Objects.equals(getUuid(), player.getUuid());
    }

    @Override
    public Player clone(){
        try {
            return (Player) super.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException("Clone not supported in Player");
        }

    }


    public int getWorkerId(Position workerPosition) {
        for(int i=0; i<workers.size(); i++){
            Worker worker = workers.get(i);
            try{
                if(worker!=null && worker.getCurrentPosition().equals(workerPosition)){
                    return i;
                }
            }catch (WorkerPositionNotSetException e){

            }
        }
        return -1;
    }
}
