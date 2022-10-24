package cs1302.gallery;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLEncoder;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.control.ProgressBar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Represents an iTunes Gallery App.
 */
public class GalleryApp extends Application {

    private static final String ITUNES_API = "https://itunes.apple.com/search";

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object


    Stage stage;
    Scene scene;
    VBox root;
    ToolBar topBar;
    Label instructions;
    HBox textBar;
    ImageView[] imageFrame;
    TilePane imageTiles;
    Image[] imageSet;
    HBox bottomBar;
    ProgressBar progress;
    Label bottomInfo;
    ArrayList<String> imageUrls;
    int numOfResults;
    boolean isPlaying;
    Timeline timeline;
    Thread thread;
    String uri;

    /**
     * Constructs a {@code GalleryApp} object}.
     */
    public GalleryApp() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox(10);
        this.topBar = new ToolBar(10);
        this.instructions = new Label("Type a request, select a media type and click the button");
        this.instructions.setAlignment(Pos.BASELINE_CENTER);
        this.topBar.setAlignment(Pos.BASELINE_CENTER);
        this.imageFrame = this.setDefImage(new Image("file:resources/default.png"));
        this.imageTiles = new TilePane();
        this.imageTiles.setAlignment(Pos.BASELINE_CENTER);
        this.bottomBar = new HBox(10);
        this.bottomBar.setAlignment(Pos.BOTTOM_CENTER);
        this.progress = new ProgressBar(0);
        this.progress.setPrefWidth(250.0);
        this.bottomInfo = new Label("Images provided by iTunes API");
        this.bottomInfo.setAlignment(Pos.BASELINE_CENTER);
        this.topBar.getImages.setOnAction((ae) -> {
            thread = new Thread(() ->
            getAndSetImages(ae));
            thread.setDaemon(true);
            thread.start();
        });
        this.topBar.play.setOnAction((ae) -> this.playImages(ae));
    } // GalleryApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        // feel free to modify this method
        System.out.println("init() called");
        this.imageTiles.setPrefColumns(5);
        this.imageTiles.setPrefRows(4);
        for (int i = 0; i < 20; i++) {
            this.imageTiles.getChildren().add(imageFrame[i]); // Adding default image to each tile
        } // for
        this.root.getChildren().addAll(topBar, instructions, imageTiles, bottomBar);
        this.bottomBar.getChildren().addAll(progress, bottomInfo);

    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(this.root, 540, 550);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("GalleryApp!");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));
    } // start

    /** {@inheritDoc} */
    @Override
    public void stop() {
        // feel free to modify this method
        System.out.println("stop() called");
        System.exit(0);
    } // stop


    /**
     * This method checks whether the play button is pressed
     * and sets the text accordingly.
     */
    private void playStatus() {
        if (this.isPlaying) {
            this.topBar.play.setText("Pause");
        } else {
            this.topBar.play.setText("Play");
        } // if
    } // playStatus

    /**
     * Switches the lambda to be set to pause or play depending
     * on the state of the boolean.
     *
     * @return action is the EventHandler object being returned.
     */
    private EventHandler<ActionEvent> decideAction() {
        EventHandler<ActionEvent> action;
        if (!this.isPlaying) {
            action = (ae) -> this.playImages(ae);
        } else {
            action = (ae) -> this.pauseImages(ae);
        } // if
        return action;
    } // decideAction

    /**
     * Puts a random image in the imageUrls list to a random imageview.
     *
     * @param ae is the Action Event when the button is pressed.
     */
    public void playImages(ActionEvent ae) {
        this.isPlaying = true;
        this.playStatus(); // Changes the status
        Random random = new Random();
        EventHandler<ActionEvent> playIt = (a) -> {
            int randomIndexTo19 = random.nextInt(20); // Picks random number
            int randomIndexToMax = random.nextInt(20, this.numOfResults);
            this.imageFrame[randomIndexTo19]
                .setImage(new Image(this.imageUrls.get(randomIndexToMax)));
        }; // Sets the random imageview to a random image
        this.setTimeline(playIt);
        this.topBar.play.setOnAction(this.decideAction());

    } // playImages

    /**
     * Stops the playImages functinality by getting the button pressed again.
     *
     * @param ae is the Action Even when the button is pressed.
     */
    public void pauseImages(ActionEvent ae) {
        this.isPlaying = false;
        this.playStatus();
        this.timeline.pause();
        this.topBar.play.setOnAction(this.decideAction());
    } // pauseImages

    /**
     * Set a delay of 2 seconds when a block of code is executed.
     *
     * @param handler is the block of code
     */
    private void setTimeline(EventHandler<ActionEvent> handler) {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), handler);
        this.timeline = new Timeline();
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.getKeyFrames().add(keyFrame);
        this.timeline.play();

    } // setTimeLine


    /**
     * Method called by the {@code getImages} button event handler. This method
     * gathers a json from itunes and gets the uri of the images of each itunes
     * object and sets it to the imageview.
     *
     * @param ae is the ActionEvent of the button.
     */
    public void getAndSetImages(ActionEvent ae) {
        try {
            Platform.runLater(() -> {
                this.instructions.setText("Loading Images...");
                this.topBar.getImages.setDisable(true);
                this.topBar.play.setDisable(true);
                this.pauseImages(ae);
            });
            // encodes the parameters and creates a uri link
            String searchText = this.topBar.searchField.getText();
            String searchTerm = URLEncoder.encode(searchText, StandardCharsets.UTF_8);
            String mediaType = URLEncoder.encode(this.topBar
                .dropdown.getValue(), StandardCharsets.UTF_8);
            String limit = URLEncoder.encode("200", StandardCharsets.UTF_8);
            String query = String
                .format("?term=%s&media=%s&limit=%s", searchTerm, mediaType, limit);
            this.uri = ITUNES_API + query;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();  // Building the request
            // Sending request and receiving response as string
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            // Throwing IOException if response is not good
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            } // if
            String json = response.body();
            ItunesResponse ir = parseItunesJson(json); // Parsing json string
            this.numOfResults = ir.resultCount;
            this.imageUrls = retrieveURLPictures(ir);
            if (numOfResults < 21) {
                throw new IllegalArgumentException(" " + numOfResults +
                    " distinct results found, but 21 or are needed"); // If there are < 21 results
            } // if
            // Setting each image frame to the newly gathered image from itunes
            this.setProgress(0);
            for (int i = 0; i < 20; i++) {
                this.setProgress(1.0 * i / 20); // updates progress bar
                this.imageFrame[i].setImage(new Image(this.imageUrls.get(i))); // sets new image
            } // for
            this.setProgress(1); // ProgressBar goes to full
            Platform.runLater(() -> {
                this.instructions.setText(this.uri); // Sets text to the url
                this.topBar.getImages.setDisable(false); // Enables buttons
                this.topBar.play.setDisable(false);
            });
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            //System.err.println(ioe);
            this.topBar.getImages.setDisable(false);
            this.topBar.play.setDisable(false);
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR,
                    "URI: " + this.uri + "\n" + e.toString() + e.getMessage());
                this.instructions.setText("Last attempt to get images failed...");
                alert.showAndWait();
                this.topBar.play.setText("Play");
            });
        } // try
    } // handle


    /**
     * Sets the progress bar's progress.
     *
     * @param loadProgress is the incremented progress.
     */
    private void setProgress(final double loadProgress) {
        Platform.runLater(() -> this.progress.setProgress(loadProgress));
    } // setProgress

    /**
     * {@code retrieveURLPictures} retrieves the {@code artworkUrl100} link from
     * the json.
     *
     * @param ir is the ItunesResponse parsed json.
     * @return imageUrls is the String array of artworkUrl100 links.
     */
    public ArrayList<String> retrieveURLPictures(ItunesResponse ir) {
        ArrayList<String> imageUrls = new ArrayList<String>();
        // Collecting the Image URLs for artworkUrl100
        for (int i = 0; i < this.numOfResults; i++) {
            imageUrls.add(i, ir.results[i].artworkUrl100);
        } // for

        // Removes the duplicate strings urls
        for (int i = 0; i < imageUrls.size() - 1; i++) {
            for (int j = i + 1; j < imageUrls.size(); j++) {
                if (imageUrls.get(i).equals(imageUrls.get(j))) {
                    imageUrls.remove(j);
                    this.numOfResults--;
                    j--; // Start from the same index after duplicate element is removed
                } // if
            } // for
        } // for
        return imageUrls;
    } // retrieveURLPictures


    /**
     * Sets the imageview calling object to the default image.
     *
     * @param img is the image being set.
     * @return the ImageView object with the {@code img} in it.
     */
    private ImageView[] setDefImage(Image img) {
        ImageView[] frames = new ImageView[20];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new ImageView(img);
        } // for
        return frames;
    } // setDefaultImage

    /**
     * This method returns a parsed ItunesResponse object where it parses
     * the json string to be able to gather each of the object's attributes.
     *
     * @param jsonStr is the string to be parsed into an object.
     * @return the parsed ItunesResponse object.
     */
    public static ItunesResponse parseItunesJson(String jsonStr) {
        return GSON.fromJson(jsonStr, ItunesResponse.class);
    } // parseItunesJson


} // GalleryApp
