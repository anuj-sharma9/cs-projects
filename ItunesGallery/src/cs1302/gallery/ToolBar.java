package cs1302.gallery;


import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Represents the custom component for the toolbar on
 * top of the page.
 */
public class ToolBar extends HBox {

    Button play;
    Label search;
    TextField searchField;
    ComboBox<String> dropdown;
    Button getImages;

    /**
     * Constructor for the ToolBar custom component.
     *
     * @param spacing is the HBox spacing.
     */
    public ToolBar(double spacing) {
        super(spacing);
        this.play = new Button("Play");
        this.play.setDisable(true);
        this.search = new Label("Search:");
        this.searchField = new TextField();
        this.searchField.setText("daft punk");
        this.dropdown = new ComboBox<String>();
        this.dropdown.getItems().addAll(
            "movie",
            "podcast",
            "music",
            "musicVideo",
            "audiobook",
            "shortFilm",
            "tvShow",
            "software",
            "ebook",
            "all");
        this.dropdown.setValue("music");
        this.getImages = new Button("Get Images");
        this.getChildren().addAll(play, search, searchField, dropdown, getImages);

    } // ToolBar

} // ToolBar
