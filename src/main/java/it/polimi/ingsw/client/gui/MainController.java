
package it.polimi.ingsw.client.gui;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.view.event.BuildViewEvent;
import it.polimi.ingsw.view.event.MoveViewEvent;
import it.polimi.ingsw.view.event.PlaceViewEvent;
import javafx.event.EventHandler;
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


public class MainController extends ClientEventEmitter {

    private final static int WIDTH = 900;
    private final static int HEIGHT = 500;
    final double boardSize=15;
    private final Group root = new Group();
    private final Group workers = new Group();
    private final Group buildings = new Group();

    private Button undoButton = new Button();
    private Button moveButton = new Button();
    private Button buildButton = new Button();

    private Position startPosition;
    private double onClickXCoord,onClickYCoord;
    private double newOnClickXCoord,newOnClickYCoord;
    private boolean isAllowedToMove = true;
    private boolean isAllowedToBuild;
    private boolean isDome;
    private Operation operation;



    private enum Operation{
        MOVE,
        BUILD,
        BUILD_DOME,
        PLACE_WORKER;
        private Operation(){}


    }


    private int count=0;



    ///da spostare in guimodel
    private GuiCell[][] board= new GuiCell[5][5];
    private CoordinateMap map = new CoordinateMap(boardSize);




    private boolean isSelectedWorker(){
        return startPosition!=null;
    }


    private void initBoard(){
        for(int i=0;i<5;i++)
            for(int j=0;j<5;j++){
                board[i][j]=new GuiCell();
                Coordinate coord = map.getCoordinate(i,j);
                board[i][j].setCoordinate(new Coordinate(coord.getCenterX(),coord.getCenterY(),0));
            }
    }
    ////////////////////////




    private void create3DScene() {
        initBoard();
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

        root.getChildren().add(board);
        root.getChildren().add(cliff);
//        root.getChildren().add(outerWall);
        root.getChildren().add(islands);
        root.getChildren().add(innerWalls);
        root.getChildren().add(sea);
        root.getChildren().add(workers);
        root.getChildren().add(buildings);



    }

    private void addSubSceneCameraEvents(Scene scene, PerspectiveCamera camera){
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event-> {
            Transform toSceneTransform = camera.getLocalToSceneTransform();
            double deltaAngle;
            switch(event.getCode()) {
//                case W:
//                    camera.translateYProperty().set(camera.getTranslateY() + 1);
//                    break;
//                case S:
//                    camera.translateYProperty().set(camera.getTranslateY() - 1);
//                    break;
//                case R:
//                    camera.translateZProperty().set(camera.getTranslateZ() + 1);
//                    break;
//                case T:
//                    camera.translateZProperty().set(camera.getTranslateZ() - 1);
//                    break;
//                case Q:
//                    root.rotateByX(10);
//                    break;
//                case E:
//                    root.rotateByX(-10);
//                    break;
//                case A:
//                    root.rotateByY(10);
//                    break;
//                case D:
//                    root.rotateByY(-10);
//                    break;
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


    private Position getClickCellIndex(double x,double y)  {
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++) {
                if (x >= map.getLeft(i, j) && x <= map.getRight(i, j) && y >= map.getTop(i, j) && y <= map.getDown(i, j))
                    try{
                        return new Position(i, j);
                    }catch(PositionOutOfBoundsException e){

                    }
            }
        }
        return null;
    }




    public void placeWorker(Position position){
        String workerUrl = "/models/MaleBuilder_Blue.obj";
        Group worker = loadModel(getClass().getResource(workerUrl),"/textures/workerblue.png");
        Coordinate pos = board[position.getX()][position.getY()].getCoordinate();
        worker.getTransforms().addAll(new Translate(pos.getCenterX(), pos.getCenterY(), pos.getCenterZ()), new Rotate(+90, Rotate.X_AXIS));
        addOnClickEventWorker(worker);
        workers.getChildren().add(worker);
        board[position.getX()][position.getY()].setWorker(worker);
    }


//    private void placeWorker2(Position position){
//        String workerUrl = "/models/MaleBuilder_Blue.obj";
//        Group worker = loadModel(getClass().getResource(workerUrl),"/textures/workerpink.png");
//        Coordinate pos = board[position.getX()][position.getY()].getCoordinate();
//        worker.getTransforms().addAll(new Translate(pos.getCenterX(), pos.getCenterY(), pos.getCenterZ()), new Rotate(+90, Rotate.X_AXIS));
//        addOnClickEventWorker(worker);
//        workers.getChildren().add(worker);
//        board[position.getX()][position.getY()].setWorker(worker);
//    }


    private void buildBase(Position position){
        Coordinate pos = board[position.getX()][position.getY()].getCoordinate();
        Cube platform = new Cube(3,3,0.001,Color.DARKKHAKI);
        platform.getTransforms().add(new Translate(pos.getCenterX(), pos.getCenterY(), pos.getCenterZ()+1));
        String baseUrl = "/models/Base.obj";
        Group model = loadModel(getClass().getResource(baseUrl),"/textures/base.png");
        model.getTransforms().addAll(new Translate(pos.getCenterX(), pos.getCenterY(), pos.getCenterZ()+1), new Rotate(+90, Rotate.X_AXIS), new Scale(0.4,0.4,0.4));
        addOnClickEventBuilding(model);
        buildings.getChildren().add(platform);
        buildings.getChildren().add(model);
        board[position.getX()][position.getY()].setLevel(Dimension.BASE);
        board[position.getX()][position.getY()].setLastBuilding(model);
    }


    private void buildMiddle(Position position){
        Coordinate pos = board[position.getX()][position.getY()].getCoordinate();
        String baseUrl = "/models/Middle.obj";
        Group model = loadModel(getClass().getResource(baseUrl),"/textures/middle.png");
        model.getTransforms().addAll(new Translate(pos.getCenterX(),pos.getCenterY(),pos.getCenterZ()+1), new Rotate(+90, Rotate.X_AXIS), new Scale(0.32,0.32,0.32));
        addOnClickEventBuilding(model);
        buildings.getChildren().add(model);
        board[position.getX()][position.getY()].setLevel(Dimension.MID);
        board[position.getX()][position.getY()].setLastBuilding(model);
    }


    private void buildTop(Position position){
        Coordinate pos = board[position.getX()][position.getY()].getCoordinate();
        String baseUrl = "/models/Top.obj";
        Group model = loadModel(getClass().getResource(baseUrl),"/textures/top.png");
        model.getTransforms().addAll(new Translate(pos.getCenterX(),pos.getCenterY(),pos.getCenterZ()+1), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3,0.3,0.3));
        addOnClickEventBuilding(model);
        buildings.getChildren().add(model);
        board[position.getX()][position.getY()].setLevel(Dimension.TOP);
        board[position.getX()][position.getY()].setLastBuilding(model);

    }


    private void buildDome(Position position){
        Coordinate pos = board[position.getX()][position.getY()].getCoordinate();
        String baseUrl = "/models/Dome.obj";
        Group dome = loadModel(getClass().getResource(baseUrl),"/textures/Dome.png");
        dome.getTransforms().addAll(new Translate(pos.getCenterX(),pos.getCenterY(),pos.getCenterZ()+0.8), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3,0.3,0.3));
//        addOnClickEventBuilding(dome);
        buildings.getChildren().add(dome);
        board[position.getX()][position.getY()].setDome();
    }



    private void buildDomeLevel0(Position position){
        Coordinate pos = board[position.getX()][position.getY()].getCoordinate();
        Cube platform = new Cube(3,3,0.001,new Color(0,0,0,0));
        platform.getTransforms().add(new Translate(pos.getCenterX(), pos.getCenterY(), pos.getCenterZ()+1));
        String baseUrl = "/models/Dome.obj";
        Group dome = loadModel(getClass().getResource(baseUrl),"/textures/Dome.png");
        dome.getTransforms().addAll(new Translate(pos.getCenterX(),pos.getCenterY(),pos.getCenterZ()+0.8), new Rotate(+90, Rotate.X_AXIS), new Scale(0.3,0.3,0.3));
//        addOnClickEventBuilding(dome);
        buildings.getChildren().add(platform);
        buildings.getChildren().add(dome);
        board[position.getX()][position.getY()].setDome();
    }

    private void addOnClickEventBuilding(Node node){
        node.setOnMouseClicked(e-> {
            newOnClickXCoord = node.getBoundsInParent().getCenterX();
            newOnClickYCoord = node.getBoundsInParent().getCenterY();
            Position destinationPosition = getClickCellIndex(newOnClickXCoord,newOnClickYCoord);
            if(isSelectedWorker()){
                if(checkDistance(startPosition, destinationPosition)){
                    if(operation.equals(Operation.MOVE))
                        emitViewEvent(new MoveViewEvent(startPosition, destinationPosition));
                    else if(operation.equals(Operation.BUILD))
                        emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, false));
                    else if(operation.equals(Operation.BUILD_DOME))
                        emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, true));
                }
            }
        });
    }





    private void addOnClickEventBoard(Node node){
        node.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PickResult pr = mouseEvent.getPickResult();
                Position destinationPosition = getClickCellIndex(pr.getIntersectedPoint().getX(),pr.getIntersectedPoint().getY());
                if(isSelectedWorker()) {
                    if(checkDistance(startPosition, destinationPosition)){
                        if(operation.equals(Operation.MOVE))
                            emitViewEvent(new MoveViewEvent(startPosition, destinationPosition));
                        else if(operation.equals(Operation.BUILD))
                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, false));
                        else if(operation.equals(Operation.BUILD_DOME))
                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, true));
                    }
                }else{
                    if(operation.equals(Operation.PLACE_WORKER)){
                        emitViewEvent(new PlaceViewEvent(destinationPosition));
                    }
                }

            }
        });
    }

    private void printBoard(){
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<5;j++){
                System.out.print(board[i][j].isWorkerSet()+" "+board[i][j].getCoordinate().getCenterZ()+"      ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void addOnClickEventWorker(Node node){
        node.setOnMousePressed(e->{
                onClickXCoord = node.getBoundsInParent().getCenterX();
                onClickYCoord = node.getBoundsInParent().getCenterY();
                if(!isSelectedWorker()){
                    startPosition = getClickCellIndex(onClickXCoord, onClickYCoord);
                }else{
                    Position destinationPosition = getClickCellIndex(onClickXCoord, onClickYCoord);
                    if(checkDistance(startPosition, destinationPosition)){
                        if(operation.equals(Operation.MOVE))
                            emitViewEvent(new MoveViewEvent(startPosition, destinationPosition));
                        else if(operation.equals(Operation.BUILD))
                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, false));
                        else if(operation.equals(Operation.BUILD_DOME))
                            emitViewEvent(new BuildViewEvent(startPosition, destinationPosition, true));
                    }
                }
        });

    }


    private boolean checkDistance(Position start, Position destination){
        int dx = start.getX()-destination.getX();
        int dy = start.getY()-destination.getY();
        if(dx<=1 && dx>=-1 && dy<=1 && dy>=-1)
            return true;
        return false;
    }




    private void translateWorker(Node node, double dx, double dy, double dz)  {
        try{
            Transform localTransform = node.getLocalToSceneTransform();
            Point3D newCoords = localTransform.inverseDeltaTransform(dx,dy,dz);
            node.getTransforms().add(new Translate(newCoords.getX(),newCoords.getY(),newCoords.getZ()));
        }catch(NonInvertibleTransformException e){
            return;
        }
    }

    public void moveWorker(Position start, Position destination){
        if(board[start.getX()][start.getY()].isWorkerSet())
        {
            Node worker = board[start.getX()][start.getY()].getWorker();
            Coordinate dest = board[destination.getX()][destination.getY()].getCoordinate();
            double dx = worker.getBoundsInParent().getCenterX()-dest.getCenterX();
            double dy = worker.getBoundsInParent().getCenterY()-dest.getCenterY();
            double dz = worker.getBoundsInParent().getCenterZ()-dest.getCenterZ();
            translateWorker(worker, -dx,-dy,-dz);
            board[start.getX()][start.getY()].setWorker(null);
            board[start.getX()][start.getY()].deleteWorker();
            board[destination.getX()][destination.getY()].setWorker(worker);
        }
    }

    public void build(Position start, Position destination, boolean isDome){
        GuiCell destGuiCell = board[destination.getX()][destination.getY()];

        if(destGuiCell.getLevel().equals(Dimension.EMPTY))
        {
            if(isDome)
                buildDomeLevel0(destination);
            else
                buildBase(destination);
        }
        else if(destGuiCell.getLevel().equals(Dimension.BASE))
        {
            if(isDome)
            {
                board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
                buildDome(destination);
            }
            else {
                    board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
                    buildMiddle(destination);
                }
            }
        else if(destGuiCell.getLevel().equals(Dimension.MID))
        {
            if(isDome)
            {
                board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
                buildDome(destination);
            }
            else {
                board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
                buildTop(destination);
            }
        }
        else {
            board[destination.getX()][destination.getY()].getLastBuilding().setMouseTransparent(true);
            buildDome(destination);
        }
    }


    //eventi per pulsanti sulla schermata
    private void addButtonEvents(){

    }

    private Group loadModel(URL url, String diffuseTexture){
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


        Label username1 = new Label(GuiModel.getInstance().getCurrentUsername());
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
        moveButton.setGraphic(buttonImage("/textures/move.png"));
        moveButton.setPrefSize(70,70);
        buildButton.setGraphic(buttonImage("/textures/build.png"));
        buildButton.setPrefSize(70,70);
        addButtonEvents();

        VBox vbButtons = new VBox(30);
        vbButtons.setPadding(new Insets(10,10,10,10));
        Pane undoPane = new Pane(undoButton);
        Pane movePane = new Pane(moveButton);
        Pane buildPane = new Pane(buildButton);
        vbButtons.getChildren().addAll(undoPane, movePane, buildPane);

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
        operation=Operation.PLACE_WORKER;

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






}
