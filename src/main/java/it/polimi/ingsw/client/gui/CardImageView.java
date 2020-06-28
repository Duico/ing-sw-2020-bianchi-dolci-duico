package it.polimi.ingsw.client.gui;

import javafx.css.PseudoClass;
import javafx.scene.image.ImageView;

/**
 * class used to set css to card images in choose card game phase
 */
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




}
