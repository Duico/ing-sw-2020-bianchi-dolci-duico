
package it.polimi.ingsw.client.gui;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.client.gui.event.GuiEventEmitter;
import it.polimi.ingsw.client.gui.event.GuiEventListener;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;


public class MainController implements GuiEventEmitter {

    private GuiEventListener listener;

    private final static int WIDTH = 900;
    private final static int HEIGHT = 500;
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

    private Position startPosition;
    private double onClickXCoord,onClickYCoord;
    private double newOnClickXCoord,newOnClickYCoord;



    private Operation currentOperation;
    private int buildButtonClickCount;

    private boolean isDome;
    private Operation operation;

    public enum Operation {
        MOVE,
        BUILD,
        BUILD_DOME,
        PLACE_WORKER;
    }


    public MainController(){
        setOperation(Operation.PLACE_WORKER);
    }

    ///da spostare in guimodel
    private CoordinateMap map = new CoordinateMap(boardSize, baseZ);
    private Map<Position, Node> workersMap = new LinkedHashMap<>();

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
            //end turn event
        });
    }
    public void addOnClickEventUndoButton(Node node){
        node.setOnMouseClicked(event->{
//            undo();
        });
    }
    public void addOnClickEventMoveButton(Node node){
        node.setOnMouseClicked(event->{
            setOperation(Operation.MOVE);
        });
    }
    public void addOnClickEventBuildButton(Node node){
        node.setOnMouseClicked(event->{
            if(buildButtonClickCount==1)
                setOperation(Operation.BUILD);
            else if(buildButtonClickCount==2)
                setOperation(Operation.BUILD_DOME);
        });
    }


    public void clearBoard(){
        buildings.getChildren().removeAll();
        workers.getChildren().removeAll();
        workersMap.clear();
    }

    public void drawBoardCell(Position position, Level level, boolean isDome){
        int i;
        for(i=0;i<level.getOrd();i++){
            makeBuild(position, Level.fromIntToLevel(i));
        }
        if(isDome)
        {
            if(i==0)
                makeDomeBuild(position, true);
            else
                makeDomeBuild(position, false);
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

        root.getChildren().addAll(board, cliff, islands, innerWalls, sea, workers, buildings);
        workers.getChildren().addAll(myWorkers, opponentWorkers);




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




    private Position getClickCellIndex(double x,double y) {
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
            addOnClickEventWorker(worker);
            if(isMyWorker)
                myWorkers.getChildren().add(worker);
            else
                opponentWorkers.getChildren().add(worker);
            workersMap.put(position, worker);
        });
    }


    private void buildPlatform(Point3D pos){
        Cube platform = new Cube(3,3,0.001,Color.DARKKHAKI);
        platform.getTransforms().add(new Translate(pos.getX(), pos.getY(), pos.getZ()+1));
        buildings.getChildren().add(platform);
    }

    public boolean makeBuild(Position position, Level level){
        Point3D pos = map.getCoordinate(position);
        Group model = Models.fromLevel(level);
        if(model == null)
            return false;

        model.getTransforms().addAll(new Translate(pos.getX(),pos.getY(),pos.getZ()+1), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3,0.3,0.3));
        addOnClickEventBuilding(model);
        buildings.getChildren().add(model);
        buildPlatform(pos);
        return true;
    }

    private void makeDomeBuild(Position position, boolean level0){
        Point3D pos = map.getCoordinate(position);
        Group dome = Models.DOME.getModel();
        dome.getTransforms().addAll(new Translate(pos.getX(),pos.getY(),pos.getZ()+0.8), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3,0.3,0.3));
//        addOnClickEventBuilding(dome);
        if(level0) {
            buildPlatform(pos);
        }
        buildings.getChildren().add(dome);
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


    private void addOnClickEventBuilding(Node node){
        node.setOnMouseClicked(e-> {
            newOnClickXCoord = node.getBoundsInParent().getCenterX();
            newOnClickYCoord = node.getBoundsInParent().getCenterY();
            Position destinationPosition = getClickCellIndex(newOnClickXCoord,newOnClickYCoord);
//            if(isSelectedWorker()){
//                if(checkDistance(startPosition, destinationPosition)){
//                    if(operation.equals(Operation.MOVE))
//                        emitViewEvent(new MoveViewEvent(startPosition, destinationPosition));
//                    else if(operation.equals(Operation.BUILD))
//                        emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, false));
//                    else if(operation.equals(Operation.BUILD_DOME))
//                        emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, true));
//                }
//            }
        });
    }


    private void addOnClickEventBoard(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PickResult pr = mouseEvent.getPickResult();
                Position destinationPosition = getClickCellIndex(pr.getIntersectedPoint().getX(), pr.getIntersectedPoint().getY());
                if (isSelectedWorker()) {
//                    if (operation.equals(Operation.MOVE))
//                            emitViewEvent(new MoveViewEvent(startPosition, destinationPosition));
//                    else if (operation.equals(Operation.BUILD))
//                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, false));
//                    else if (operation.equals(Operation.BUILD_DOME))
//                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, true));
                } else {
                    if (currentOperation.equals(Operation.PLACE_WORKER)) {
                        System.out.println(destinationPosition.getX()+" "+destinationPosition.getY());
                        emitPlaceWorker(destinationPosition);
                    }

                }
            }
        });
    }





    private void addOnClickEventWorker(Node node){
        node.setOnMousePressed(e->{
                onClickXCoord = node.getBoundsInParent().getCenterX();
                onClickYCoord = node.getBoundsInParent().getCenterY();
                if(!isSelectedWorker()){
                    startPosition = getClickCellIndex(onClickXCoord, onClickYCoord);
                }else{
                    Position destinationPosition = getClickCellIndex(onClickXCoord, onClickYCoord);
//                    if(checkDistance(startPosition, destinationPosition)){
//                        if(operation.equals(Operation.MOVE))
//                            emitViewEvent(new MoveViewEvent(startPosition, destinationPosition));
//                        else if(operation.equals(Operation.BUILD))
//                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, false));
//                        else if(operation.equals(Operation.BUILD_DOME))
//                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, true));
//                    }
                }
        });

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
//                    }
        if(tmpWorker == null){
            return;
        }
//            double dx = tmpWorker.getBoundsInParent().getCenterX()-dest.getCenterX();
//            double dy = tmpWorker.getBoundsInParent().getCenterY()-dest.getCenterY();
//            double dz = tmpWorker.getBoundsInParent().getCenterZ()-dest.getCenterZ();

            Point3D destCenter1 = map.getCoordinate(destinationPosition);
            Point3D destCenter2 = map.getCoordinate(pushPosition);
            Bounds startBounds1 = tmpWorker.getBoundsInParent();
            Point3D startCenter1 = new Point3D(startBounds1.getCenterX(), startBounds1.getCenterY(), startBounds1.getCenterZ());
            Bounds startBounds2 = pushedWorker.getBoundsInParent();
            Point3D startCenter2 = new Point3D(startBounds2.getCenterX(), startBounds2.getCenterY(), startBounds2.getCenterZ());
            translateWorker(pushedWorker, startCenter2, destCenter2);
            translateWorker(tmpWorker, startCenter1, destCenter1);
//            board[startPosition.getX()][startPosition.getY()].setWorker(null);
//            board[startPosition.getX()][startPosition.getY()].deleteWorker();
//            board[destinationPosition.getX()][destinationPosition.getY()].setWorker(worker);

    }

    public void build(Position start, Position destination, boolean isDome, Level level){
        if(isDome){
            makeDomeBuild(destination, level.equals(Level.EMPTY));
        }else{
            makeBuild(destination, level);
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
        background.setPrefSize(650, 650);


        Label username1 = new Label("Pippo FANCLUB");
        username1.setFont(new Font("Arial", 20));
//        username1.setTextFill(Color.WHITE);
        username1.setStyle("-fx-background-color:white;");
//        Pane cardImage1 = new Pane(cardImage(GuiModel.getInstance().getCurrentCard()));

        //manca il collegamento tra carta scelta e players corrispondente
        //TODO

//        Label username2 = new Label(GuiModel.getInstance().getPlayer(1));
//        username2.setFont(new Font("Arial", 20));
////        username2.setTextFill(Color.WHITE);
//        username2.setStyle("-fx-background-color:white;");
//        Pane cardImage2 = new Pane(cardImage(GuiModel.getInstance().getCard(1)));




        undoButton.setGraphic(buttonImage("/textures/Undo.png"));
        undoButton.setPrefSize(70,70);
        moveButton.setGraphic(buttonImage("/textures/notmove.png"));
        moveButton.setPrefSize(70,70);
        buildButton.setGraphic(buttonImage("/textures/notbuild.png"));
        buildButton.setPrefSize(70,70);
        endTurnButton.setGraphic(buttonImage("/textures/notendTurn.png"));
        endTurnButton.setPrefSize(100, 70);
        addButtonEvents();

        VBox vbButtons = new VBox(30);
        vbButtons.setPadding(new Insets(10,10,10,10));
        Pane entTurnPane = new Pane(endTurnButton);
        Pane undoPane = new Pane(undoButton);
        Pane movePane = new Pane(moveButton);
        Pane buildPane = new Pane(buildButton);
        vbButtons.getChildren().addAll(entTurnPane,undoPane, movePane, buildPane);

        VBox vbPlayers = new VBox(5);
        vbPlayers.setPadding(new Insets(10,10,10,10));
        vbPlayers.getChildren().addAll(username1);
//        if(GuiModel.getInstance().getNumPlayers()==3){
//            Label username3 = new Label(GuiModel.getInstance().getPlayer(2));
//            username3.setFont(new Font("Arial", 20));
////            username3.setTextFill(Color.WHITE);
//            username3.setStyle("-fx-background-color:white;");
//            Pane cardImage3 = new Pane(cardImage(GuiModel.getInstance().getCard(2)));
//            vbPlayers.getChildren().addAll(username1, cardImage1, username2, cardImage2, username3, cardImage3);
//        }
//        else if(GuiModel.getInstance().getNumPlayers()==2)
//            vbPlayers.getChildren().addAll(username1, cardImage1, username2, cardImage2);


        background.setCenter(subScene);
        background.setLeft(vbPlayers);
        background.setRight(vbButtons);

        //TODO css
        background.setStyle("-fx-background-color:black;");
        background.getLeft().setStyle("-fx-background-color:black;");
        background.getRight().setStyle("-fx-background-color:black;");



        Scene scene = new Scene(background);

        vbButtons.prefHeightProperty().bind(scene.heightProperty());
        vbPlayers.prefHeightProperty().bind(scene.heightProperty());
        background.prefHeightProperty().bind(scene.heightProperty());
        background.prefWidthProperty().bind(scene.widthProperty());
        subScene.heightProperty().bind(background.heightProperty());
//        subScene.widthProperty().bind(background.widthProperty());

        addSubSceneCameraEvents(scene,camera);
//        operation = Operation.PLACE_WORKER;
        return scene;
    }



    private ImageView cardImage(String name){
        ImageView imageView = new ImageView(name);
        imageView.setFitHeight(150);
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
        listener.onMove(startPosition, destinationPosition);
    }
    public void emitBuild(Position workerPosition, Position buildPosition, boolean isDome){
        listener.onBuild(workerPosition, buildPosition, isDome);
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
