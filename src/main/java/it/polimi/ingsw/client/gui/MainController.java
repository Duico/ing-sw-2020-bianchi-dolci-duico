
package it.polimi.ingsw.client.gui;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.transform.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * main controller class of game scene
 */
public class MainController implements GuiEventEmitter {

    private GuiEventListener listener;
    private boolean active = false;
    private final static int WIDTH = 900;
    private final static int HEIGHT = 500;
    final double boardSize=15;
    final double baseZ = 0;
    private SubScene subScene;
    private final Group root = new Group();
    private final Group workers = new Group();
    private final Group myWorkers= new Group();
    private final Group opponentWorkers = new Group();
    private final Group buildings = new Group();

    private Button undoButton = new Button();
    private Button moveButton = new Button();
    private Button buildButton = new Button();
    private Button endTurnButton = new Button();
    private Label messageBox = new Label();
    private FadeTransition messageBoxFade;

    /**
     * udpdated every time a worker is selected by clicking on it
     */
    private Position startPosition;
    private double onClickXCoord,onClickYCoord;
    private double newOnClickXCoord,newOnClickYCoord;

    private VBox vbPlayers=new VBox(12);
    private VBox vbButtons=new VBox(28);
    private Pane trigliph = new Pane();

    /**
     * updated by game buttons
     */
    private Operation currentOperation;

    /**
     * If true, the next build attempt will be a dome
     */
    private boolean isBuildDome;

    private final CoordinateMap map = new CoordinateMap(boardSize, baseZ);
    private Map<Position, Node> workersMap = new LinkedHashMap<>();
    private Node startPositionIndicator = null;
    private Node hoverPositionIndicator = null;
    private Position hoverPosition;

    public boolean isActive() {
        return active;
    }

    public enum Operation {
        MOVE,
        BUILD,
        PLACE_WORKER;
    }


    public MainController(){
        setOperation(Operation.PLACE_WORKER);
        isBuildDome = false;
    }

    /**
     * updates message shown in message box
     * @param message message to show
     */
    public void setMessage(String message){
        Platform.runLater(()->{
            messageBox.setText(message);
            messageBox.setOpacity(1);
            if(messageBoxFade != null) {
                messageBoxFade.playFromStart();
            }
        });
    }

    /**
     * style game message box
     */
    private void initMessageBox(){
        messageBoxFade = new FadeTransition(Duration.millis(4000), messageBox);
        messageBoxFade.setDelay(Duration.seconds(4));
        messageBoxFade.setFromValue(1.0);
        messageBoxFade.setToValue(0);
        messageBoxFade.setCycleCount(1);
        messageBox.setLayoutY(50);
        messageBox.setFont(new Font("Arial", 20));
    }

    /**
     * @return true if a worker has already been selected
     */
    private boolean isSelectedWorker(){
        return startPosition!=null;
    }

    public void setOperation(Operation operation){
        this.currentOperation=operation;
    }
    public Operation getOperation(){
        return currentOperation;
    }

    private boolean isOperationSet(){
        return currentOperation!=null;
    }

    /**
     * add click events on end turn button
     * @param node end turn button
     */
    public void addOnClickEventEndTurnButton(Node node){
        node.setOnMouseClicked(event->{
            System.out.println("Emit endturn");
            emitEndTurn();
        });
    }

    /**
     * add click events on undo button
     * @param node undo button
     */
    public void addOnClickEventUndoButton(Node node){
        System.out.println("Emit undo");
        node.setOnMouseClicked(event->{
            emitUndo();
        });
    }

    /**
     * add click events on move button
     * @param node move button
     */
    public void addOnClickEventMoveButton(Node node){
        node.setOnMouseClicked(event->{
            if(!isOperationSet() || !currentOperation.equals(Operation.MOVE))
            {
                setOperation(Operation.MOVE);
            }
            refreshButtons();
        });
    }

    /**
     * add click events on build button
     * @param node build button
     */
    public void addOnClickEventBuildButton(Node node){
        node.setOnMouseClicked(event->{
            if(!isOperationSet() || !currentOperation.equals(Operation.BUILD)){
                setOperation(Operation.BUILD);
            }else{
                isBuildDome=!isBuildDome;
            }
            refreshButtons();
        });
    }

    /**
     * refreshes game buttons loading css style
     */
    public void refreshButtons(){
        moveButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), currentOperation!=null && currentOperation.equals(Operation.MOVE));
        buildButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), currentOperation!=null && currentOperation.equals(Operation.BUILD));
        buildButton.getStyleClass().removeAll("buildDome_button", "build_button");
        buildButton.getStyleClass().add(isBuildDome?"buildDome_button":"build_button");
    }

    /**
     * updates game buttons disabling operations that player can't do
     * @param isMyTurn true if it is current player's turn
     * @param turnPhase current turn phase
     * @param isAllowedToMove true if current player's worker is allowed to move
     * @param isAllowedToBuild true if current player's worker is allowed to build
     * @param isRequiredToMove true if current player's worker is required to move
     * @param isRequiredToBuild true if current player's worker is required to build
     */
    public void updateButtons(boolean isMyTurn, TurnPhase turnPhase, boolean isAllowedToMove, boolean isAllowedToBuild, boolean isRequiredToMove, boolean isRequiredToBuild){
        if(turnPhase.equals(TurnPhase.PLACE_WORKERS)){
            currentOperation = Operation.PLACE_WORKER;
        }else if(isAllowedToMove){
            currentOperation = Operation.MOVE;
        }else if(isAllowedToBuild) {
            currentOperation = Operation.BUILD;
        }else{
            currentOperation = null;
        }
        isBuildDome = false;

        disableButtons(isRequiredToMove||isRequiredToBuild, false, !isAllowedToMove, !isAllowedToBuild);
    }

    /**
     * disables game buttons
     * @param endTurnButtonDisabled true if end turn button has to be disabled
     * @param undoButtonDisabled true if undo button has to be disabled
     * @param moveButtonDisabled true if move button has to be disabled
     * @param buildButtonDisabled true if build button has to be disabled
     */
    public void disableButtons(boolean endTurnButtonDisabled, boolean undoButtonDisabled, boolean moveButtonDisabled, boolean buildButtonDisabled){
        Platform.runLater(()-> {
            refreshButtons();
            setDisabledButton(endTurnButton, endTurnButtonDisabled);
            setDisabledButton(undoButton, undoButtonDisabled);
            setDisabledButton(moveButton, moveButtonDisabled);
            setDisabledButton(buildButton, buildButtonDisabled);
        });
    }

    /**
     * changes style to disable buttons
     * @param button button to change
     * @param disabled true if button is currently disabled
     */
    private void setDisabledButton(Button button, boolean disabled){
        button.setOpacity(disabled?0.5:1);
        button.setMouseTransparent(disabled);
    }

    /**
     * disable all game buttons and resets current operation
     */
    public void disableAllButtons(){
        currentOperation = null;
        disableButtons(true, true, true, true);
    }

    /**
     * disables all game buttons and removes click events from game scene
     */
    public void endGame() {
        subScene.setMouseTransparent(true);
        disableAllButtons();
    }

    /**
     * sets start position to null -> there are no workers selected
     */
    public void clearStartPosition() {
        Platform.runLater( () -> {
            setStartPosition(null);
        });
    }

    /**
     * updates start position of operation and apply graphic on click position
     * @param position start position of new operation
     */
    private void setStartPosition(Position position) {
        if(position!=null && !isMyWorker(position)){
            return;
        }
        if(startPositionIndicator==null) {
            startPositionIndicator = new Cube(3, 3, 0.001, Color.WHITE, "/graphics/red_glow.png");
            root.getChildren().add(startPositionIndicator);
            startPositionIndicator.setMouseTransparent(true);
        }
        startPosition = position;
        drawIndicator(startPosition, startPositionIndicator);
    }


    /**
     * applies hover indicator graphic
     * @param position position where cursor is currently located at
     */
    private void showCellHoverIndicator(Position position){
        if(hoverPosition==null || !hoverPosition.equals(position)){
            setHoverPosition(position);
        }
    }

    /**
     * set hover position based on cursor position
     * @param position position of cursor
     */
    private void setHoverPosition(Position position){
        hoverPosition = position;

        if(hoverPositionIndicator==null) {
            hoverPositionIndicator = new Cube(3, 3, 0.001, Color.WHITE, "/graphics/blue_glow.png");
            root.getChildren().add(hoverPositionIndicator);
            hoverPositionIndicator.setMouseTransparent(true);
        }
        drawIndicator(hoverPosition, hoverPositionIndicator);
    }

    /**
     * draws hover graphic effect on cursor position
     * @param position hover position
     * @param indicator hover glow 3d object
     */
    private void drawIndicator(Position position, Node indicator) {
        if (position == null){
            indicator.setVisible(false);
        }else{
            Point3D pos = map.getCoordinate(position);
            indicator.getTransforms().clear();
            indicator.getTransforms().add(new Translate(pos.getX(), pos.getY(), pos.getZ()+0.98));
            indicator.setVisible(true);
        }
    }

    /**
     * removes all 3d objects from 3d board
     */
    public void clearBoard(){
            myWorkers.getChildren().clear();
            opponentWorkers.getChildren().clear();
            buildings.getChildren().clear();
            workersMap.clear();
            map.clearHeight();

    }

    /**
     * draws buildings block for every level reached in current boardcell
     * @param position current position of boardcell
     * @param level level of boardcell
     * @param isDome true if a dome has to be drawn
     */
    public void drawBoardCell(Position position, Level level, boolean isDome){
        int i;
        for(i=0;i<=level.getOrd();i++){
            makeBlockBuild(position, Level.fromIntToLevel(i));
        }
        if(isDome)
        {
            makeDomeBuild(position);
        }else
            return;
    }


    /**
     * creates and imports all 3D objects for game scene, putting all of them into root element
     */
    private void create3DScene() {
        Box closeSea = new Box(72, 72, 0.4);
        closeSea.setRotate(-45);
        PhongMaterial seaTexture = new PhongMaterial();
        seaTexture.setDiffuseMap(new Image("/textures/sea.png"));
        closeSea.setMaterial(seaTexture);
        Group sea = new Group();
        sea.getChildren().addAll(closeSea);
        sea.getTransforms().add(new Translate(0, 0, 3.5));
        Cube board = new Cube(boardSize, boardSize, 0.001, Color.WHITE, "/textures/walls_crop.png");
        board.getTransforms().add(new Translate(0, 0, 1));
        addOnClickEventBoard(board);

        String cliffUrl = "/models/Cliff.obj";
        String islandUrl = "/models/Islands.obj";
        String innerWallsUrl = "/models/InnerWalls.obj";
        Group cliff = loadModel(getClass().getResource(cliffUrl), "/textures/cliff.png");
        cliff.getTransforms().addAll(new Rotate(+90, Rotate.X_AXIS), new Translate(-0.03, 3.02, 0.05), new Scale(9.3, 8, 9.3));
        Group islands = loadModel(getClass().getResource(islandUrl), "/textures/islands.png");
        islands.getTransforms().addAll(new Translate(-14, 4, 0), new Rotate(+90, Rotate.X_AXIS));
        Group innerWalls = loadModel(getClass().getResource(innerWallsUrl), Color.MINTCREAM, null);
        innerWalls.getTransforms().addAll(new Translate(9.9, -10, 4.5), new Rotate(+90, Rotate.X_AXIS), new Rotate(90, Rotate.Y_AXIS), new Translate(19.97,0,0.06), new Scale(1.15, 1, 1.15));

        PointLight light = new PointLight();
        AmbientLight ambientLight = new AmbientLight(new Color(.6,.6,.6, 1));
        light.setColor(new Color(.7, .7, 0.56, 1)); //yellow
        Group lightGroup = new Group();
        lightGroup.getChildren().addAll(light, ambientLight);
        lightGroup.setTranslateZ(-120);
        lightGroup.setTranslateX(40);
        lightGroup.setTranslateY(40);
        light.setRotate(45);

        workers.getChildren().addAll(myWorkers, opponentWorkers);
        root.getChildren().addAll(board, cliff, islands, innerWalls, sea, workers, buildings, lightGroup);
    }

    /**
     * allows player to change angle or perspective of 3D gui by typing on keyboard
     * @param scene current scene
     * @param camera camera set on scene
     */
    private void addSubSceneCameraEvents(Scene scene, PerspectiveCamera camera){
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event-> {
            Transform toSceneTransform = camera.getLocalToSceneTransform();
            double deltaAngle;
            switch(event.getCode()) {
                case E: { //zoom in
                    double camDistance = Point3D.ZERO.distance(toSceneTransform.transform(Point3D.ZERO));
                    if (camDistance <= 25) {
                        break;
                    }
                    camera.getTransforms().add(new Translate(0, 0, 0.75));
                    break;
                }
                case Q: { //zoom out
                    double camDistance = Point3D.ZERO.distance(toSceneTransform.transform(Point3D.ZERO));
                    if (camDistance >= 80) {
                        break;
                    }
                    camera.getTransforms().add(new Translate(0, 0, -0.75));
                    break;
                }
                case D:
                    try{
                        Point3D sceneZero = toSceneTransform.inverseTransform(Point3D.ZERO);
                        Point3D zAxis = new Point3D(0,0,1);
                        Point3D verticalAxis = toSceneTransform.inverseDeltaTransform(zAxis); //Z axis
                        camera.getTransforms().add(new Rotate(-11.25, sceneZero.getX(),sceneZero.getY(),sceneZero.getZ(), verticalAxis));
                    }catch (NonInvertibleTransformException e) {
                        e.printStackTrace();
                    }
                    break;
                case A:
                    //da spostare in una funzione
                    try{
                        Point3D sceneZero = toSceneTransform.inverseTransform(Point3D.ZERO);
                        Point3D zAxis = new Point3D(0,0,1);
                        Point3D verticalAxis = toSceneTransform.inverseDeltaTransform(zAxis); //Z axis
                        camera.getTransforms().add(new Rotate(11.25, sceneZero.getX(),sceneZero.getY(),sceneZero.getZ(), verticalAxis));
                    }catch (NonInvertibleTransformException e) {
                        e.printStackTrace();
                    }
                    break;
                case S:
                    deltaAngle = 4;
                    try{
                        Point3D sceneZero = toSceneTransform.inverseTransform(Point3D.ZERO);
                        Point3D xAxis = new Point3D(1,0,0);

                        //ANGLE
                        Point3D camPosition = toSceneTransform.transform(new Point3D(0,0,0));
                        Point3D camXYProjection = new Point3D(camPosition.getX(), camPosition.getY(), 0);
                        double angle = Point3D.ZERO.angle(camPosition, camXYProjection);

                        if(angle<2*deltaAngle){
                            break;
                        }
                        double pitchCoeff = Math.tan( Math.toRadians(45-Math.abs(45-angle)));

                        camera.getTransforms().add(new Rotate(deltaAngle*pitchCoeff, sceneZero.getX(),sceneZero.getY(),sceneZero.getZ(), xAxis));
                    }catch (NonInvertibleTransformException e) {
                        e.printStackTrace();
                    }
                    break;
                case W:
                    deltaAngle = 4;
                    try{
                        Point3D sceneZero = toSceneTransform.inverseTransform(Point3D.ZERO);
                        Point3D xAxis = new Point3D(1,0,0);

                        //ANGLE
                        Point3D camPosition = toSceneTransform.transform(new Point3D(0,0,0));
                        Point3D camXYProjection = new Point3D(camPosition.getX(), camPosition.getY(), 0);
                        double angle = Point3D.ZERO.angle(camPosition, camXYProjection);

                        if(angle>90-2*deltaAngle){
                            //return;
                            break;
                        }
                        double pitchCoeff = Math.tan( Math.toRadians(45-Math.abs(45-angle)));

                        camera.getTransforms().add(new Rotate(-deltaAngle*pitchCoeff, sceneZero.getX(),sceneZero.getY(),sceneZero.getZ(), xAxis));
                    }catch (NonInvertibleTransformException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        } );


    }


    /**
     * searches for position related to X and Y coordinates in coordinate map
     * @param x click X coordinate
     * @param y click Y coordinate
     * @return position related to click coordinates
     */
    private Position getClickPosition(double x, double y) {
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++) {
                if (map.getBoundingBox(i,j).contains(x,y))
                {
                    try{
                        return new Position(i, j);
                    }catch(PositionOutOfBoundsException e){
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * draws 3d worker on selected position on board
     * @param position place position
     * @param isMyWorker true if it's current player's worker
     * @param color current player's color
     */
    public void placeWorker(Position position, boolean isMyWorker, PlayerColor color){
        Platform.runLater(()->{
            Group worker = Models.fromColor(color);
            Point3D pos = map.getCoordinate(position);
            worker.getTransforms().addAll(new Translate(pos.getX(), pos.getY(), pos.getZ()), new Rotate(+90, Rotate.X_AXIS));
            addOnClickEventWorker(worker, isMyWorker);
            if(isMyWorker)
                myWorkers.getChildren().add(worker);
            else
                opponentWorkers.getChildren().add(worker);
            workersMap.put(position, worker);
        });
    }

    /**
     * removes 3d worker from scene
     * @param position worker position
     * @param color current player's color
     */
    public void removeWorker(Position position, PlayerColor color){
        Platform.runLater(()->{
            Node removeWorker = workersMap.get(position);
            opponentWorkers.getChildren().remove(Models.fromColor(color));
            myWorkers.getChildren().remove(Models.fromColor(color));
            opponentWorkers.getChildren().remove((removeWorker));
            myWorkers.getChildren().remove(removeWorker);
            workersMap.remove(position, removeWorker);
        });
    }

//    private void buildPlatform(Point3D pos){
//        Color transparentKhaki = new Color(Color.DARKKHAKI.getRed(), Color.DARKKHAKI.getGreen(), Color.DARKKHAKI.getBlue(), 0);
//        Cube platform = new Cube(3,3,0.001, transparentKhaki);
//        platform.getTransforms().add(new Translate(pos.getX(), pos.getY(), pos.getZ()+1));
//        buildings.getChildren().add(platform);
//    }

    /**
     * draws a building on 3d board in a certain position
     * @param position build position
     * @param level board cell building level
     * @param isDome true if a dome has to be built
     */
    public void makeBuild(Position position, Level level, boolean isDome){
        if(isDome){
            makeDomeBuild(position);
        }else{
            makeBlockBuild(position, level);
        }
    }

    /**
     * draws building (not dome) on a certain position on board, adds on click events on it and updates coordinate map
     * based on height reached by the block
     * @param position position relate to 3d board
     * @param level current level of boarcell related to position
     */
    private void makeBlockBuild(Position position, Level level){
        if(level == null || level.equals(Level.EMPTY)){
            return;
        }
        Platform.runLater(()->{
            Point3D pos = map.getCoordinate(position);
            double height= pos.getZ();
            System.out.println(height);
            Group model = Models.fromLevel(level);
            model.getTransforms().addAll(new Translate(pos.getX(),pos.getY(),height+1), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3,0.3,0.3));
            addOnClickEventBuilding(model);
            map.setHeight(position, Level.fromLevelToBuildingHeight(level));
            buildings.getChildren().add(model);
        });

    }

    /**
     * draws dome building on top of all blocks at the boardcell related to position
     * @param position position on 3d board
     */
    private void makeDomeBuild(Position position){
        Platform.runLater(()-> {
            Point3D pos = map.getCoordinate(position);
            Group dome = Models.DOME.getModel();
            dome.getTransforms().addAll(new Translate(pos.getX(), pos.getY(), pos.getZ() + 0.86), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3, 0.3, 0.3));
            buildings.getChildren().add(dome);
        });
    }

    /**
     * enum class used to import 3d objects and put texture on them by using URL
     */
    public enum Models{
        BASE("/models/Base.obj","/textures/base.png"),
        MID("/models/Middle.obj","/textures/middle.png"),
        TOP("/models/Top.obj","/textures/top.png"),
        DOME("/models/Dome.obj", "/textures/Dome.png"),
        BLUE_WORKER("/models/MaleBuilder.obj","/textures/blueworker.png"),
        YELLOW_WORKER("/models/MaleBuilder.obj","/textures/yellowworker.png"),
        GRAY_WORKER("/models/MaleBuilder.obj","/textures/grayworker.png"),
        ;

        private final String baseUrl;
        private final String diffuseUrl;

        private Models(String baseUrl, String diffuseUrl){
            this.baseUrl = baseUrl;
            this.diffuseUrl = diffuseUrl;
        }

        public Group getModel() {
            return loadModel(getClass().getResource(baseUrl), diffuseUrl);
        }

        /**
         * import block building 3d object related to a certain building level
         * @param level current boardcell level
         * @return block building 3d object
         */
        public static Group fromLevel(Level level){
            Group model = null;
            switch (level){
                case BASE:
                    model = Models.BASE.getModel();
                    break;
                case MID:
                    model = Models.MID.getModel();
                    break;
                case TOP:
                    model = Models.TOP.getModel();
                    break;
            }
            return model;
        }

        /**
         * import worker 3d object related to player color in game
         * @param color current player's color
         * @return coloured worker 3d object
         */
        public static Group fromColor(PlayerColor color){
            if(color.equals(PlayerColor.BLUE))
                return Models.BLUE_WORKER.getModel();
            else if(color.equals(PlayerColor.YELLOW))
                return Models.YELLOW_WORKER.getModel();
            else if(color.equals(PlayerColor.GRAY))
                return Models.GRAY_WORKER.getModel();
            else
                return null;
        }
    }

    /**
     * checks in workers map if worker just clicked is one of current player's workers
     * @param position click position where a 3d worker is located
     * @return true if worker just clicked is one of current player's workers
     */
    private boolean isMyWorker(Position position){
        Node worker = workersMap.get(position);
        return myWorkers.getChildren().contains(worker);
    }

    /**
     * checks if a worker has already been selected and emits move or build view event based on
     * which operation is currently selected
     * @param destinationPosition destination position of selected operation
     */
    private void handleCellClickEvent(Position destinationPosition){
        if (isSelectedWorker() && !isMyWorker(destinationPosition)) {
           if(currentOperation!=null){
               if (currentOperation.equals(Operation.MOVE)) {
                   emitMove(startPosition, destinationPosition);
               } else if (currentOperation.equals(Operation.BUILD)){
                   emitBuild(startPosition, destinationPosition, isBuildDome);
               }
           }
        } else {
            setStartPosition(destinationPosition);
        }
    }

    /**
     * adds on click event on 3d building block object just created
     * allows it to update start position if there's not a selected worker or to set destination
     * position in order to launch events
     * @param node 3d building block object just created
     */
    private void addOnClickEventBuilding(Node node){
        node.setOnMousePressed(e-> {
            newOnClickXCoord = node.getBoundsInParent().getCenterX();
            newOnClickYCoord = node.getBoundsInParent().getCenterY();
            Position destinationPosition = getClickPosition(newOnClickXCoord,newOnClickYCoord);
            handleCellClickEvent(destinationPosition);
        });
        addHoverIndicator(node, pr -> getClickPosition(node.getBoundsInParent().getCenterX(), node.getBoundsInParent().getCenterY()));
    }

    /**
     * adds on click event on 3d game Board object, allows it to place worker on board at PlaceWorker turn phase
     * or update start position in a normal turn if there are no worker's selected
     * @param node 3d board object
     */
    private void addOnClickEventBoard(Node node) {
        node.setOnMousePressed(e -> {
            PickResult pr = e.getPickResult();
            Position destinationPosition = getClickPosition(pr.getIntersectedPoint().getX(), pr.getIntersectedPoint().getY());
           if(currentOperation!=null){
               if (currentOperation.equals(Operation.PLACE_WORKER)) {
                   emitPlaceWorker(destinationPosition);
               }else{
                   handleCellClickEvent(destinationPosition);
               }
           }
        });
        addHoverIndicator(node, pr -> getClickPosition(pr.getIntersectedPoint().getX(), pr.getIntersectedPoint().getY()));
    }

    /**
     * adds event to 3d object which shows hover indicator graphic in a certain position on 3d board
     * @param node 3d object to set hover indicator on
     * @param pickResultConsumer coordinates on 3d board related to where the cursor is currently located
     */
    private void addHoverIndicator(Node node, Function<PickResult, Position> pickResultConsumer){
        node.setOnMouseMoved(e -> {
            PickResult pr = e.getPickResult();
            Position destinationPosition = pickResultConsumer.apply(pr);
            showCellHoverIndicator(destinationPosition);
        });
        node.setOnMouseExited( e -> {
            showCellHoverIndicator(null);
        });
    }


    /**
     * adds on click event to 3d worker on baord, allows it to set start position or do other operations
     * @param node 3d worker just created
     * @param isMyWorker true if clicked worker one of current player's worker
     */
    private void addOnClickEventWorker(Node node, boolean isMyWorker){
            node.setOnMousePressed(e->{
                onClickXCoord = node.getBoundsInParent().getCenterX();
                onClickYCoord = node.getBoundsInParent().getCenterY();
                Position workerPosition = getClickPosition(onClickXCoord, onClickYCoord);
                if(isSelectedWorker() || isMyWorker){
                    handleCellClickEvent(workerPosition);
                }else{
                    setMessage("You can't move the opponent's workers.");
                }
            });
            addHoverIndicator(node, pr -> getClickPosition(node.getBoundsInParent().getCenterX(), node.getBoundsInParent().getCenterY()));
    }

    /**
     * updates workers map when a 3d worker is moved on the board, changing his position on map
     * @param startPosition position where a worker is located
     * @param destinationPosition destination position of worker's move
     * @param pushPosition position where a worker located in destination position is pushed to
     */
    private void updateWorkersMap(Position startPosition, Position destinationPosition, Position pushPosition){
        Node worker1 = workersMap.get(startPosition);
        Node worker2 = workersMap.get(destinationPosition);
        workersMap.remove(startPosition);
        if(worker2!=null) {
            workersMap.remove(destinationPosition);
            workersMap.put(pushPosition, worker2);
        }
        workersMap.put(destinationPosition, worker1);
    }

    /**
     * translates 3d worker object by coordinates got from transform
     * @param node 3d worker object to translate
     * @param start starting point of translation
     * @param dest destination point of translation
     */
    private void translateWorker(Node node, Point3D start, Point3D dest)  {
        try{
            Transform localTransform = node.getLocalToSceneTransform();
            Point3D delta = dest.subtract(start);
            Point3D newCoords = localTransform.inverseDeltaTransform(delta);
            node.getTransforms().add(new Translate(newCoords.getX(),newCoords.getY(),newCoords.getZ()));
        }catch(NonInvertibleTransformException e){
            return;
        }
    }

    /**
     * moves 3d worker on board handling possible push movements of other 3d workers on different position
     * @param startPosition start position on movement
     * @param destinationPosition destination position of movement
     * @param pushPosition position where a worker located in destination position is pushed to
     */
    public void moveWorker(Position startPosition, Position destinationPosition, Position pushPosition){
        Node tmpWorker = workersMap.get(startPosition);
        boolean isMyWorker = myWorkers.getChildren().contains(tmpWorker);
        Node pushedWorker = workersMap.get(destinationPosition);
        updateWorkersMap(startPosition, destinationPosition, pushPosition);
        if(isMyWorker){
            setStartPosition(destinationPosition);
        }
        Platform.runLater(()-> {
                    if (tmpWorker == null) {
                        return;
                    }
                    Point3D destCenter1 = map.getCoordinate(destinationPosition);
                    Bounds startBounds1 = tmpWorker.getBoundsInParent();
                    Point3D startCenter1 = new Point3D(startBounds1.getCenterX(), startBounds1.getCenterY(), startBounds1.getCenterZ());
                    translateWorker(tmpWorker, startCenter1, destCenter1);
                    if(pushPosition!=null) {
                        if(pushedWorker == null)
                            throw new RuntimeException();
                        Point3D destCenter2 = map.getCoordinate(pushPosition);
                        Bounds startBounds2 = pushedWorker.getBoundsInParent();
                        Point3D startCenter2 = new Point3D(startBounds2.getCenterX(), startBounds2.getCenterY(), startBounds2.getCenterZ());
                        translateWorker(pushedWorker, startCenter2, destCenter2);
                    }
                });
    }

    /**
     * moves 3d worker on board from his current position to another
     * @param startPosition position where a 3d worker is currently located
     * @param destinationPosition position where 3d worker is moving to
     */
    public void moveWorker(Position startPosition, Position destinationPosition){
        moveWorker(startPosition, destinationPosition, null);
    }


//    public void build(Position start, Position destination, boolean isDome, Level level){
//        if(isDome){
//            makeDomeBuild(destination);
//        }else{
//            makeBlockBuild(destination, level);
//        }
//    }

    /**
     * loads 3d object and applies texture on it
     * @param url url related to 3d object to import
     * @param diffuseTexture url related to texture to apply
     * @return 3d object imported with texture applied on
     */
    private static Group loadModel(URL url, String diffuseTexture){
        return loadModel(url, null, diffuseTexture);
    }

    /**
     * loads 3d object, sets his color and applies texture on it
     * @param url url related to 3d object to import
     * @param color color to set on 3d object
     * @param diffuseTexture  url related to texture to apply
     * @return 3d object imported with texture applied on
     */
    private static Group loadModel(URL url, Color color, String diffuseTexture){
        Group modelRoot = new Group();
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for(MeshView view : importer.getImport()){
            PhongMaterial material;
            if(color!=null){
                material = new PhongMaterial(color);
            }else{
                material = new PhongMaterial();
            }
            if(diffuseTexture!=null) {
                material.setDiffuseMap(new Image(diffuseTexture));
            }
            view.setMaterial(material);
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }


    /**
     * class used to generate 3d cubes with different size, color or texture
     */
    private class Cube extends Box {
        public Cube(double dx, double dy, double dz, Color color, String diffuseTexture){
            super(dx,dy,dz);
            PhongMaterial texture = new PhongMaterial(color);
            texture.setDiffuseMap(new Image(diffuseTexture));
            setMaterial(texture);
        }

        public Cube(double dx, double dy, double dz, Color color){
            super(dx, dy, dz);
            setMaterial(new PhongMaterial(color));
        }

        public void setLocation(Point3D p){
            setTranslateX(p.getX());
            setTranslateY(p.getY());
            setTranslateZ(p.getZ());
        }
    }


    /**
     * creates full game scene having on his left side all player's nicknames with related Card image,
     * on right side game buttons to selected current operation
     * and game Subscene on center containing 3d scene with all 3d imported objects put in the right position
     * @return full game scene
     */
    public Scene gameScene(){
        final int TRIGLIPH_HEIGHT = 40;
        create3DScene();
        subScene = new SubScene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        double cameraAngle = 50;
        double perfectX = 25 / Math.tan(cameraAngle / 180 * Math.PI);
        camera.getTransforms().addAll(new Translate(0, perfectX, -25), new Rotate(cameraAngle - 10, Rotate.X_AXIS));
        camera.setFarClip(500);
        subScene.setFill(new Color(59.0/255, 191.0/255, 241.0/255, 1));
        subScene.setCamera(camera);

        BorderPane background = new BorderPane();

        initGameButtons();
        vbButtons.getChildren().addAll(endTurnButton,undoButton, moveButton, buildButton);

        initMessageBox();
        Pane foreground = new Pane(messageBox);
        foreground.getStylesheets().add("/css/foreground.css");
        messageBox.getStyleClass().add("foreground_messagebox");

        background.setCenter(subScene);
        background.setLeft(vbPlayers);
        background.setRight(vbButtons);
        background.setTop(trigliph);
        background.getChildren().add(foreground);

        background.getStylesheets().add("/css/mainscene.css");
        background.getStyleClass().add("background");
        BorderPane.setAlignment(subScene, Pos.TOP_LEFT);
        vbPlayers.getStyleClass().add("left_vbox");
        vbButtons.getStyleClass().add("right_vbox");
        trigliph.getStyleClass().add("trigliph");

        Scene scene = new Scene(background);

        foreground.layoutXProperty().bind(scene.widthProperty().subtract(foreground.prefWidthProperty()).divide(2));
        vbButtons.prefHeightProperty().bind(scene.heightProperty().subtract(TRIGLIPH_HEIGHT));
        vbPlayers.prefHeightProperty().bind(scene.heightProperty().subtract(TRIGLIPH_HEIGHT));
        vbPlayers.maxHeightProperty().bind(scene.heightProperty().subtract(TRIGLIPH_HEIGHT));
        scene.heightProperty().addListener( e ->{
           resizePlayers();
        });
        background.maxHeightProperty().bind(scene.heightProperty());
        background.maxWidthProperty().bind(scene.widthProperty());
        subScene.heightProperty().bind(background.heightProperty().subtract(TRIGLIPH_HEIGHT));
        subScene.widthProperty().bind(background.widthProperty().subtract(vbPlayers.widthProperty()).subtract(vbButtons.widthProperty()));

        addSubSceneCameraEvents(scene,camera);
        active = true;
        return scene;
    }

    /**
     * resizes username labels and card images when window is resized
     */
    private void resizePlayers(){
        int numPlayers = vbPlayers.getChildren().size();
        if(numPlayers > 2 && subScene.getHeight()< 680){
            vbPlayers.getChildren().forEach( child -> {
                child.setScaleY(.75);
                child.setScaleX(.75);
            });
            vbPlayers.getStyleClass().add("left_vbox_tiny");
        }else{
            vbPlayers.getChildren().forEach( child -> {
                child.setScaleY(1);
                child.setScaleX(1);
            });
            vbPlayers.getStyleClass().remove("left_vbox_tiny");

        }
    }

    /**
     * sizes game buttons adding them css style and on click events
     */
    private void initGameButtons(){
        undoButton.setPrefSize(80,80);
        undoButton.getStyleClass().addAll("undo_button","santorini_button");
        moveButton.setPrefSize(80,80);
        moveButton.getStyleClass().addAll("move_button","santorini_button");
        buildButton.setPrefSize(80,80);
        buildButton.getStyleClass().addAll("build_button", "santorini_button");
        endTurnButton.setPrefSize(80, 80);
        endTurnButton.getStyleClass().addAll("endTurn_button", "santorini_button");
        addOnClickEventEndTurnButton(endTurnButton);
        addOnClickEventUndoButton(undoButton);
        addOnClickEventMoveButton(moveButton);
        addOnClickEventBuildButton(buildButton);
    }


    /**
     * shows player's usernames with related card image on left side of game scene
     * @param players list of players in the game
     */
    public void displayPlayers(List<Player> players){
        Platform.runLater(()->{
                vbPlayers.getChildren().clear();
                if (players == null) {
                    return;
                }
                for (Player player : players) {
                    VBox username = new VBox();
                    username.getChildren().add(new Label(player.getNickName()));
                    ImageView cardImage = cardImage(player.getCard().getName());
                    VBox addPlayer = new VBox(5);
                    Pane playerPane = new Pane();
                    addPlayer.getStyleClass().add("player_box");
                    addPlayer.getChildren().addAll(username, cardImage);
                    playerPane.getChildren().add(addPlayer);
                    vbPlayers.getChildren().add(playerPane);
                }
                resizePlayers();
        });
    }


    /**
     * sizes image for player's card
     * @param name url of card image
     * @return player's card image
     */
    private ImageView cardImage(String name){
        ImageView imageView = new ImageView("/textures/"+name+".png");
        imageView.setFitHeight(168);
        imageView.setFitWidth(100);
        return imageView;
    }

    /**
     * emits move event to gui model
     * @param startPosition start position of movement
     * @param destinationPosition destination position of movement
     */
    public void emitMove(Position startPosition, Position destinationPosition){
        listener.onMove(startPosition, destinationPosition);
    }

    /**
     * emits build event to gui model
     * @param workerPosition position where a worker is located
     * @param buildPosition position where a worker il building at
     * @param isDome true if the worker is building a dome
     */
    public void emitBuild(Position workerPosition, Position buildPosition, boolean isDome){
        listener.onBuild(workerPosition, buildPosition, isDome);
    }

    /**
     * emits undo event to gui model
     */
    private void emitUndo() {
        listener.onUndo();
    }

    /**
     * emits end turn event to gui model
     */
    public void emitEndTurn(){
        listener.onEndTurn();
    }

    /**
     * emits place worker event to gui model
     * @param position position where a worker is going to be placed
     */
    public void emitPlaceWorker(Position position){
        listener.onPlaceWorker(position);
    }

    @Override
    public void setEventListener(GuiEventListener listener) {
        this.listener = listener;
    }


}
