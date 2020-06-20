package it.polimi.ingsw.client.gui;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.image.ImageView;


public class CardImageView extends ImageView {
    private static PseudoClass SELECTED_PSEUDOCLASS = PseudoClass.getPseudoClass("selected");

    public CardImageView(String path) {
        super(path);
        setPreserveRatio(true);
        setFitHeight(141);
        setFitWidth(84);
        getStyleClass().addAll("card_image");
        setIsSelected(false);
    }

    public void setIsSelected(boolean isSelected){
        pseudoClassStateChanged(SELECTED_PSEUDOCLASS, isSelected);
    }

//    public void setHover(boolean isHover){
//        pseudoClassStateChanged(HOVER_PSEUDOCLASS, isHover);
//    }


}
