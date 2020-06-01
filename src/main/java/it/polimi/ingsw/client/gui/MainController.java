
package it.polimi.ingsw.client.gui;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
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


public class MainController implements GuiEventEmitter {

    private GuiEventListener listener;
    private boolean active = false;
    private final static int WIDTH = 900;
    private final static int HEIGHT = 500;
    private final int TRIGLIPH_HEIGHT = 40;
    final double boardSize=15;
    final double baseZ = 0;
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

    private Position startPosition;
    private double onClickXCoord,onClickYCoord;
    private double newOnClickXCoord,newOnClickYCoord;

    private VBox vbPlayers=new VBox(32);
    private VBox vbButtons=new VBox(28);
    private Pane trigliph = new Pane();


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

    public void setMessage(String message){
        Platform.runLater(()->{
            messageBox.setText(message);
            messageBox.setOpacity(1);
            //TODO clear
            if(messageBoxFade != null) {
                messageBoxFade.playFromStart();
            }
        });
    }

    //TODO css
    private void initMessageBox(){
        messageBoxFade = new FadeTransition(Duration.millis(4000), messageBox);
        messageBoxFade.setDelay(Duration.seconds(4));
        messageBoxFade.setFromValue(1.0);
        messageBoxFade.setToValue(0);
        messageBoxFade.setCycleCount(1);
        messageBox.setLayoutY(50);
        messageBox.setFont(new Font("Arial", 20));
    }

    private boolean isSelectedWorker(){
        return startPosition!=null;
    }

    public void setOperation(Operation operation){
        this.currentOperation=operation;
    }
    public Operation getOperation(){
        return this.currentOperation;
    }

    private void addButtonEvents(){
        addOnClickEventMoveButton(moveButton);
        addOnClickEventUndoButton(undoButton);
        addOnClickEventBuildButton(buildButton);
        addOnClickEventEndTurnButton(endTurnButton);
    }



    public void updateOperationButtons(boolean isAllowedToMove, boolean isAllowedToBuild){
        if(isAllowedToMove)
        {
            moveButton.setGraphic(buttonImage("/textures/move.png"));
            moveButton.setMouseTransparent(false);
        }
        else
        {
            moveButton.setGraphic(buttonImage("/textures/notmove.png"));
            moveButton.setMouseTransparent(true);
        }
        if(isAllowedToBuild)
        {
            buildButton.setGraphic(buttonImage("/textures/build.png"));
            buildButton.setMouseTransparent(false);
        }
        else
        {
            moveButton.setGraphic(buttonImage("/textures/notbuild.png"));
            moveButton.setMouseTransparent(true);
        }
    }

    public void addOnClickEventEndTurnButton(Node node){
        node.setOnMouseClicked(event->{
            System.out.println("endturn");
            emitEndTurn();
        });
    }
    public void addOnClickEventUndoButton(Node node){
        node.setOnMouseClicked(event->{
            emitUndo();
        });
    }

    //TODO activate button only after first Normal turn
    public void addOnClickEventMoveButton(Node node){
        node.setOnMouseClicked(event->{
            if(!getOperation().equals(Operation.MOVE)) {
                startPosition = null;
            }
            System.out.println("move selected");
            setOperation(Operation.MOVE);
        });
    }

    //TODO activate button only after first Normal Turn
    public void addOnClickEventBuildButton(Node node){
        node.setOnMouseClicked(event->{
//            if(!getOperation().equals(Operation.BUILD)) {
//
//            }
            if(!currentOperation.equals(Operation.BUILD)) {
                startPosition = null;
                System.out.println("build selected");
                setOperation(Operation.BUILD);
                isBuildDome = false;
            }else{
                isBuildDome = !isBuildDome;
                if(isBuildDome)
                    buildButton.setGraphic(buttonImage("/textures/builddome.png"));
                else
                    buildButton.setGraphic(buttonImage("/textures/build.png"));
                System.out.println("Build dome: "+isBuildDome);
            }

        });
    }

    public void updateButtons(){
        Platform.runLater(()->{
            buildButton.setGraphic(buttonImage("/textures/build.png"));
        });
    }

    private void setStartPosition(Position position) {
        if(position!= null && !isMyWorker(position)){
            return;
        }
        startPosition = position;
        if (startPosition == null){
            startPositionIndicator.setVisible(false);

        }else{
            if(startPositionIndicator==null) {

                startPositionIndicator = new Cube(3, 3, 0.001, Color.WHITE, "/graphics/red_glow.png");
                root.getChildren().add(startPositionIndicator);
                startPositionIndicator.setMouseTransparent(true);
            }
            Point3D pos = map.getCoordinate(position);
            startPositionIndicator.getTransforms().clear();
            startPositionIndicator.getTransforms().add(new Translate(pos.getX(), pos.getY(), pos.getZ()+0.98));
            startPositionIndicator.setVisible(true);
        }
    }

    private void showCellHoverIndicator(Position position){
        if(hoverPosition==null || !hoverPosition.equals(position)){
            setHoverPosition(position);
        }
    }

    private void setHoverPosition(Position position){
        hoverPosition = position;
        if (hoverPosition == null)
            hoverPositionIndicator.setVisible(false);
        else{
            if(hoverPositionIndicator==null) {
                hoverPositionIndicator = new Cube(3, 3, 0.001, Color.WHITE, "/graphics/blue_glow.png");
                root.getChildren().add(hoverPositionIndicator);
                hoverPositionIndicator.setMouseTransparent(true);
            }
            Point3D pos = map.getCoordinate(position);
            hoverPositionIndicator.getTransforms().clear();
            hoverPositionIndicator.getTransforms().add(new Translate(pos.getX(), pos.getY(), pos.getZ()+0.98));
            hoverPositionIndicator.setVisible(true);
        }
    }

    public void clearBoard(){
            myWorkers.getChildren().clear();
            opponentWorkers.getChildren().clear();
            buildings.getChildren().clear();
//        workers.getChildren().removeAll();
            workersMap.clear();
            map.clearHeight();

    }

    public void drawBoardCell(Position position, Level level, boolean isDome){
        int i;
        for(i=0;i<=level.getOrd();i++){
            System.out.print("drawBoardCell "+position.getX()+" "+position.getY()+" level "+Level.fromIntToLevel(i));
            makeBlockBuild(position, Level.fromIntToLevel(i));
        }
        if(isDome)
        {
            makeDomeBuild(position);
        }else
            return;
    }



    private void create3DScene() {
//        initBoard();
        Cube sea = new Cube(200, 200, 0.4, Color.BLUE, "/textures/sea.jpg");
        sea.getTransforms().add(new Translate(0, 0, 3.5));
        //this.board must refer to this one
        Cube board = new Cube(boardSize, boardSize, 0.001, Color.DARKKHAKI);
        board.getTransforms().add(new Translate(0, 0, 1));
        addOnClickEventBoard(board);
//        String outerWallUrl = "models/outerwall.obj";
        String cliffUrl = "/models/Cliff.obj";
        String islandUrl = "/models/Islands.obj";
        String innerWallsUrl = "/models/InnerWalls.obj";
//        Group outerWall = loadModel(getClass().getResource(outerWallUrl), "sample/textures/cliff.png");
//        outerWall.getTransforms().addAll(new Rotate(+90, Rotate.X_AXIS), new Translate(0, 1.4, 0), new Scale(1.1, 1.2, 1.1));

        Group cliff = loadModel(getClass().getResource(cliffUrl), "/textures/cliff.png");
        cliff.getTransforms().addAll(new Rotate(+90, Rotate.X_AXIS), new Translate(0, 3.1, 0.05), new Scale(9.3, 8, 9.3));
        Group islands = loadModel(getClass().getResource(islandUrl), "/textures/islands.png");
        islands.getTransforms().addAll(new Translate(-14, 4, 0), new Rotate(+90, Rotate.X_AXIS));
        Group innerWalls = loadModel(getClass().getResource(innerWallsUrl), "/textures/walls.png");
        innerWalls.getTransforms().addAll(new Translate(9.9, -10, 4.5), new Rotate(+90, Rotate.X_AXIS), new Scale(1.15, 1, 1.15));

        PointLight light = new PointLight();
        light.setColor(new Color(1, 1, 0.8, 1)); //yellow
        Group lightGroup = new Group();
        lightGroup.getChildren().add(light);
        lightGroup.setTranslateZ(-120);
        lightGroup.setTranslateX(40);
        lightGroup.setTranslateY(40);
        light.setRotate(45);

        workers.getChildren().addAll(myWorkers, opponentWorkers);
        root.getChildren().addAll(board, cliff, islands, innerWalls, sea, workers, buildings);


    }

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
                        //Point3D verticalAxis = localTransform.inverseDeltaTransform(xAxis); //X axis
                        //System.out.println(verticalAxis);

                        //TODO rotate by a variable amount dependent on tan()
                        //Point3D minusY = localTransform.deltaTransform(new Point3D(0,-1,0));
                        //System.out.println(minusY);

                        //ANGLE
                        Point3D camPosition = toSceneTransform.transform(new Point3D(0,0,0));
                        Point3D camXYProjection = new Point3D(camPosition.getX(), camPosition.getY(), 0);
                        double angle = Point3D.ZERO.angle(camPosition, camXYProjection);

                        if(angle<2*deltaAngle){
                            //return;
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
                        //Point3D verticalAxis = localTransform.inverseDeltaTransform(xAxis); //X axis
                        //System.out.println(verticalAxis);

                        //TODO rotate by a variable amount dependent on 1/tan()
                        //Point3D minusY = localTransform.deltaTransform(new Point3D(0,-1,0));


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

    private void buildPlatform(Point3D pos){
        Color transparentKhaki = new Color(Color.DARKKHAKI.getRed(), Color.DARKKHAKI.getGreen(), Color.DARKKHAKI.getBlue(), 0);
        Cube platform = new Cube(3,3,0.001, transparentKhaki);
        platform.getTransforms().add(new Translate(pos.getX(), pos.getY(), pos.getZ()+1));
        buildings.getChildren().add(platform);
    }

    //previously was boolean return type
//    public boolean makeBuild(Position position, Level level){
//       Platform.runLater(()->{
//           Point3D pos = map.getCoordinate(position);
//
//           Group model = Models.fromLevel(level);
//           if(model == null)
//               return false;
//
//           model.getTransforms().addAll(new Translate(pos.getX(),pos.getY(),pos.getZ()+1), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3,0.3,0.3));
//           addOnClickEventBuilding(model);
//           buildings.getChildren().add(model);
//
//           buildPlatform(pos);
//       });
//        return true;
//    }

    public void makeBuild(Position position, Level level, boolean isDome){
        if(isDome){
            makeDomeBuild(position);
        }else{
            makeBlockBuild(position, level);
        }
    }

    private void makeBlockBuild(Position position, Level level){
        if(level == null || level.equals(Level.EMPTY)){
            return;
        }
        Platform.runLater(()->{
            Point3D pos = map.getCoordinate(position);
            double height= pos.getZ();
            System.out.println(height);
            Group model = Models.fromLevel(level);
//            if(model == null)
//                return false;

            model.getTransforms().addAll(new Translate(pos.getX(),pos.getY(),height+1), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3,0.3,0.3));
            addOnClickEventBuilding(model);
            ///
            map.setHeight(position, Level.fromLevelToBuildingHeight(level));
            ///
            buildings.getChildren().add(model);
//            if(level.equals(Level.BASE)) {
//                buildPlatform(pos);
//            }
        });

    }

    private void makeDomeBuild(Position position){
        Platform.runLater(()-> {
            System.out.println("dovrei disegnare una dome");
            Point3D pos = map.getCoordinate(position);
            Group dome = Models.DOME.getModel();
            dome.getTransforms().addAll(new Translate(pos.getX(), pos.getY(), pos.getZ() + 0.8), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3, 0.3, 0.3));
//        addOnClickEventBuilding(dome);
//            if (level0) {
//                buildPlatform(pos);
//            }
            buildings.getChildren().add(dome);
        });
    }

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

    private boolean isMyWorker(Position position){
        Node worker = workersMap.get(position);
        return myWorkers.getChildren().contains(worker);
    }

    private void handleCellClickEvent(Position destinationPosition){
        if (isSelectedWorker() && !isMyWorker(destinationPosition)) {
            if (currentOperation.equals(Operation.MOVE)) {
                emitMove(startPosition, destinationPosition);
                setStartPosition(null);
            } else if (currentOperation.equals(Operation.BUILD)){
                emitBuild(startPosition, destinationPosition, isBuildDome);
                setStartPosition(null);
            }
//                    else if (operation.equals(Operation.BUILD_DOME))

//                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, true));
        } else {
            //set start position
            setStartPosition(destinationPosition);
        }
    }


    private void addOnClickEventBuilding(Node node){
        node.setOnMousePressed(e-> {
            System.out.println(e.getSource());
            newOnClickXCoord = node.getBoundsInParent().getCenterX();
            newOnClickYCoord = node.getBoundsInParent().getCenterY();
            Position destinationPosition = getClickPosition(newOnClickXCoord,newOnClickYCoord);
            handleCellClickEvent(destinationPosition);
        });
        addHoverIndicator(node, pr -> getClickPosition(node.getBoundsInParent().getCenterX(), node.getBoundsInParent().getCenterY()));
    }

    private void addOnClickEventBoard(Node node) {
        node.setOnMousePressed(e -> {
            System.out.println(e.getSource());
            PickResult pr = e.getPickResult();
            Position destinationPosition = getClickPosition(pr.getIntersectedPoint().getX(), pr.getIntersectedPoint().getY());
            //The position should be taken from an apposite map and not from the intersectedPoint

            if (currentOperation.equals(Operation.PLACE_WORKER)) {
                System.out.println(destinationPosition.getX()+" "+destinationPosition.getY());
                emitPlaceWorker(destinationPosition);
            }else{
                handleCellClickEvent(destinationPosition);
            }
        });
        addHoverIndicator(node, pr -> getClickPosition(pr.getIntersectedPoint().getX(), pr.getIntersectedPoint().getY()));
    }

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


    private void addOnClickEventWorker(Node node, boolean isMyWorker){
            node.setOnMousePressed(e->{
                System.out.println(e.getSource());
                onClickXCoord = node.getBoundsInParent().getCenterX();
                onClickYCoord = node.getBoundsInParent().getCenterY();
                Position workerPosition = getClickPosition(onClickXCoord, onClickYCoord);
                if(isSelectedWorker() || isMyWorker){
                    handleCellClickEvent(workerPosition);
                }else{
                    setMessage("Not your worker");
                    //TODO multiline
//                    setMessage("You can't move the opponent's workers.");
                }
            });
            addHoverIndicator(node, pr -> getClickPosition(node.getBoundsInParent().getCenterX(), node.getBoundsInParent().getCenterY()));
    }

    //TODO
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

    public void moveWorker(Position startPosition, Position destinationPosition, Position pushPosition){
        Node tmpWorker = workersMap.get(startPosition);
        Node pushedWorker = workersMap.get(destinationPosition);
        updateWorkersMap(startPosition, destinationPosition, pushPosition);
        Platform.runLater(()-> {

//                    }
                    if (tmpWorker == null) {
                        return;
                    }
//            double dx = tmpWorker.getBoundsInParent().getCenterX()-dest.getCenterX();
//            double dy = tmpWorker.getBoundsInParent().getCenterY()-dest.getCenterY();
//            double dz = tmpWorker.getBoundsInParent().getCenterZ()-dest.getCenterZ();

                    Point3D destCenter1 = map.getCoordinate(destinationPosition);
//                    double height = map.getHeight(destinationPosition.getX(), destinationPosition.getY());
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

    public void moveWorker(Position startPosition, Position destinationPosition){
        moveWorker(startPosition, destinationPosition, null);
    }

    public void build(Position start, Position destination, boolean isDome, Level level){
        if(isDome){
            makeDomeBuild(destination);
        }else{
            makeBlockBuild(destination, level);
        }



//        if(destGuiCell.getLevel().equals(Dimension.EMPTY))
//        {
//            if(isDome)
//                buildDome(destination, true);
//            else
//                buildBase(destination);
//        }
//        else if(destGuiCell.getLevel().equals(Dimension.BASE))
//        {
//            if(isDome)
//            {
//                board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
//                buildDome(destination, false);
//            }
//            else {
//                    board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
//                    buildMiddle(destination);
//                }
//            }
//        else if(destGuiCell.getLevel().equals(Dimension.MID))
//        {
//            if(isDome)
//            {
//                board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
//                buildDome(destination, false);
//            }
//            else {
//                board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
//                buildTop(destination);
//            }
//        }
//        else {
//            board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
//            buildDome(destination, false);
//        }
    }



    private static Group loadModel(URL url, String diffuseTexture){
        Group modelRoot = new Group();
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for(MeshView view : importer.getImport()){
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseMap(new Image(diffuseTexture));
            view.setMaterial(material);
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }




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



    public Scene gameScene(){
        create3DScene();

        SubScene subScene = new SubScene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        double cameraAngle = 50;
        double perfectX = 25 / Math.tan(cameraAngle / 180 * Math.PI);
        camera.getTransforms().addAll(new Translate(0, perfectX, -25), new Rotate(cameraAngle - 10, Rotate.X_AXIS));
        camera.setFarClip(500);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(camera);

        BorderPane background = new BorderPane();

        undoButton.setGraphic(buttonImage("/textures/Undo.png"));
        undoButton.setPrefSize(70,70);
        moveButton.setGraphic(buttonImage("/textures/move.png"));
        moveButton.setPrefSize(70,70);
        buildButton.setGraphic(buttonImage("/textures/build.png"));
        buildButton.setPrefSize(70,70);
        endTurnButton.setGraphic(buttonImage("/textures/notendturn.png"));
        endTurnButton.setPrefSize(70, 70);
        addButtonEvents();

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
        background.maxHeightProperty().bind(scene.heightProperty());
        background.maxWidthProperty().bind(scene.widthProperty());
        subScene.heightProperty().bind(background.heightProperty().subtract(TRIGLIPH_HEIGHT));
        subScene.widthProperty().bind(background.widthProperty().subtract(vbPlayers.widthProperty()).subtract(vbButtons.widthProperty()));

        addSubSceneCameraEvents(scene,camera);
//        operation = Operation.PLACE_WORKER;
        active = true;
        return scene;
    }


    public void displayPlayers(List<Player> players){
        Platform.runLater(()->{
            vbPlayers.getChildren().clear();
            if(players==null){
                return;
            }
            System.out.print("diplayPlayers( ");
            System.out.println(players);
            for(Player player:players){
                System.out.println(player.getNickName()+" "+player.getCard().getName());
                VBox username = new VBox();
                username.getChildren().add(new Label(player.getNickName()));
                ImageView cardImage = cardImage(player.getCard().getName());
                VBox addPlayer = new VBox(5);
                addPlayer.getStyleClass().add("player_box");
                addPlayer.getChildren().addAll(username, cardImage);
                vbPlayers.getChildren().add(addPlayer);
            }
        });
    }


    private ImageView cardImage(String name){
        ImageView imageView = new ImageView("/textures/"+name+".png");
        imageView.setFitHeight(168);
        imageView.setFitWidth(100);
        return imageView;
    }

    private ImageView buttonImage(String name){
        ImageView imageView = new ImageView(name);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        return imageView;
    }

    public void emitMove(Position startPosition, Position destinationPosition){
        System.out.println(startPosition.getX()+" "+startPosition.getY());
        System.out.println(destinationPosition.getX()+" "+destinationPosition.getY());
        listener.onMove(startPosition, destinationPosition);
    }
    public void emitBuild(Position workerPosition, Position buildPosition, boolean isDome){
        System.out.println(workerPosition.getX()+" "+workerPosition.getY());
        System.out.println(buildPosition.getX()+" "+buildPosition.getY());
        listener.onBuild(workerPosition, buildPosition, isDome);
    }

    private void emitUndo() {
        listener.onUndo();
    }

    public void emitEndTurn(){
        listener.onEndTurn();
    }

    public void emitPlaceWorker(Position position){
        listener.onPlaceWorker(position);
    }

    @Override
    public void setEventListener(GuiEventListener listener) {
        this.listener = listener;
    }


}
