package it.polimi.ingsw.client.gui;

public class SceneEvent {
    private SceneType sceneType;
    public SceneEvent(SceneType type){
        sceneType = type;
    }

    public SceneType getSceneType() {
        return sceneType;
    }

    public void setSceneType(SceneType sceneType) {
        this.sceneType = sceneType;
    }

    public enum SceneType {
        LOGIN,
        CHOSE_CARDS,
        MAIN
    }


}