package ingram.andrew;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.Timer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WeehWooh extends Application {
	
	// maybe re-factor and use enumerator later?
	public static final int SHOOTING = 0;
	public static final int STABBING = 1;
	public static final int ROBBERY = 2;
	
	private int checkAmount = 10; // how many calls the check at a time (Maximum = 10????)
	private int initialWindowWidth = 800;
	private int initialWindowHeight = 600;
	private int checkInterval = (int) (60000 * 2); // two minutes
	private boolean automaticOutputScrollEnabled = true;
	
	private List<String> checkedCrimes; // crimes already checked (using string id's from BPD)
	private Date date;
	private BorderPane rootNode;
	private VBox bottomVBox;
	private HBox bottomButtonHBox;
	private ScrollPane outputScroll;
	private VBox outputVBox;
	private Scene mainScene;
	private Timer checkTimer;
	private ScrollPane notificationScroll;
	private VBox notificationVBox;
	private CrimeDataView crimeDataView;
	private HBox toolBar;
	private HBox centerBox;
	private Spinner<Integer> checkIntervalSpin;
	private Spinner<Integer> callCountSpin;
	private URL url;
	private Media sirenSound;
	private MediaPlayer sirenSoundPlayer;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		// give Main a reference to this application
		Main.programObject = this;
		
		// initialize date
		date = new Date(System.currentTimeMillis());
		
		// initialize URL
		url = new URL("https://p2c.beaumonttexas.gov/p2c/cad/cadHandler.ashx?op=s");
		
		// initialize crime list
		checkedCrimes = new ArrayList<String>();
		
		// initialize siren sound
		sirenSound = new Media(getClass().getResource("/audio/siren.mp3").toURI().toString());
		sirenSoundPlayer = new MediaPlayer(sirenSound);
		
		// root node of scene
		rootNode = new BorderPane();
		
		// add tool-bar
		toolBar = new HBox(10);
		toolBar.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		toolBar.setMinHeight(60);
		rootNode.setTop(toolBar);
		
		// add interval spinner
		VBox checkIntervalVBox = new VBox();
		checkIntervalVBox.setAlignment(Pos.CENTER);
		toolBar.getChildren().add(checkIntervalVBox);
		
		checkIntervalVBox.getChildren().add(new Label("Check Interval (Minutes)"));
		checkIntervalSpin = new Spinner<Integer>();
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
		valueFactory.setValue(checkInterval / 60000);
		checkIntervalSpin.setValueFactory(valueFactory);
		toolBar.getChildren().add(checkIntervalSpin);
		checkIntervalSpin.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				checkInterval = checkIntervalSpin.getValue() * 60000;
			}
		});
		checkIntervalVBox.getChildren().add(checkIntervalSpin);
		
		// add call-count spinner
		VBox callCountVBox = new VBox();
		callCountVBox.setAlignment(Pos.CENTER);
		toolBar.getChildren().add(callCountVBox);
		
		callCountVBox.getChildren().add(new Label("Call Count\n(Calls per Check)"));
		callCountSpin = new Spinner<Integer>();
		valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20);
		valueFactory.setValue(checkAmount);
		callCountSpin.setValueFactory(valueFactory);
		toolBar.getChildren().add(callCountSpin);
		callCountSpin.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				checkAmount = callCountSpin.getValue();
			}
		});
		callCountVBox.getChildren().add(callCountSpin);
		
		
		// add center-box
		centerBox = new HBox();
		centerBox.setAlignment(Pos.CENTER);
		rootNode.setCenter(centerBox);
		
		// add crime data view node
		crimeDataView = new CrimeDataView();
		//crimeDataView.setTranslateX(20);
		//crimeDataView.setTranslateY(20);
		//crimeDataView.setMaxHeight(200);
		crimeDataView.setMinWidth(325);
		crimeDataView.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		centerBox.getChildren().add(crimeDataView);
		HBox.setHgrow(crimeDataView, Priority.ALWAYS);

		// scroll-pane for notification v-box
		notificationScroll = new ScrollPane();
		notificationScroll.setFitToWidth(true);
		notificationScroll.setPrefWidth(415);
		notificationScroll.setPrefHeight(350);
		centerBox.getChildren().add(notificationScroll);
		//HBox.setHgrow(notificationScroll, Priority.ALWAYS);
		
		// holds notification objects
		notificationVBox = new VBox(6);
		notificationScroll.setContent(notificationVBox);
		
		// contains things from the bottom-left to the bottom-right (output, buttons above the output, . . .)
		bottomVBox = new VBox();
		rootNode.setBottom(bottomVBox);
		
		// contains buttons above the output
		bottomButtonHBox = new HBox(8);
		bottomVBox.getChildren().add(bottomButtonHBox);
		
		// pause / resume automatic output scrolling button
		CheckBox automaticScrollButton = new CheckBox("Automatic Output Scrolling");
		automaticScrollButton.setSelected(true);
		automaticScrollButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				automaticOutputScrollEnabled = !automaticOutputScrollEnabled;
				if (automaticOutputScrollEnabled) setOutputMaxScroll();
			}
			
		});
		bottomButtonHBox.getChildren().add(automaticScrollButton);
		
		// output v-box scroll container
		outputScroll = new ScrollPane();
		outputScroll.setPrefHeight(210);
		outputScroll.setPrefWidth(640);
		bottomVBox.getChildren().add(outputScroll);
		
		// output v-box
		outputVBox = new VBox(6);
		outputScroll.setContent(outputVBox);
		
		// the main scene
		mainScene = new Scene(rootNode);
		
		// setting stage stuff
		stage.setScene(mainScene);
		stage.setWidth(initialWindowWidth);
		stage.setHeight(initialWindowHeight);
		stage.setTitle("Weeh-Wooh!");
		
		// event for checking web-site
		ActionListener webChecker = new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				checkWebsite();
			}
		};
		
		// timer for checking web-site
		checkTimer = new Timer(checkInterval, webChecker);
		checkTimer.setRepeats(true);
		
		// "starting" program
		stage.show();
		output("Welcome to Weeh-Wooh!");
		checkWebsite();
		checkTimer.start();
		
	}
	
	public void checkWebsite() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				output("Attempting connection . . .");
				
				URLConnection connection;
				try {
					// try opening a connection to the web-site
					connection = url.openConnection();
					
					output("Connected!");
					
					// get connection ready to receive data
					connection.setDoOutput(true);
					String postData = "t=ccc&_search=false&nd=" + Long.toString(System.currentTimeMillis()) + "&rows=" + Integer.toString(checkAmount) + "&page=1&sidx=starttime&sord=desc";
					connection.setRequestProperty("Content-Length", Integer.toString(postData.length()));
					
					// get data from connection
					DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
					dos.writeBytes(postData);
					
					// read data as string from server
					BufferedReader buffReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String line;
					String jsonString = "";
					while ((line = buffReader.readLine()) != null)
						jsonString = jsonString + line;
					
					// build data into a JSON object
					GsonBuilder builder = new GsonBuilder();
					builder.setPrettyPrinting();
					
					Gson gson = builder.create();
					CrimeDataHolder crimeDataHolder = gson.fromJson(jsonString, CrimeDataHolder.class);
					
					
					
					// loop through data and look for desired outputs
					dataLoop: for (int i = 0; i < crimeDataHolder.rows.size(); i++) {
						
						CrimeData crimeData = crimeDataHolder.rows.get(i);
						
						// check if crimeData was already checked (if so, ignore)
						for (String crimeID : checkedCrimes) {
							if (crimeID.equals(crimeData.id)) {
								// crime already notified, go next crimeData
								continue dataLoop;
							}
						}
						
						// add id to list of checked crime data id's
						checkedCrimes.add(crimeData.id);
						
						// check for shooting
						if (crimeData.nature.contains("SHOOT")) {
							output("Found a shooting!");
							createNotification(SHOOTING, crimeData);
						}
						// check for robberies
						else if (crimeData.nature.contains("ROBB")) {
							output("Found a robbery!");
							createNotification(ROBBERY, crimeData);
							
						}
						// check for stabbing
						else if (crimeData.nature.contains("STAB")) {
							output("Found a stabbing!");
							createNotification(STABBING, crimeData);
							
						}
						// check for burglary ? ? ? 
						else if (crimeData.nature.contains("BURGLAR")) {
							//TODO should I access this data
						}
					}
					
				} catch (IOException e) {
					output("Failed to connect to web-site!");
					e.printStackTrace();
				}
				
				output("Next check will be in " + (double) checkInterval / 60000.0 + " minutes.");
			}
		});
	}
	
	// outputs message to the output panel with time and date
	public void output(String outputText) {
		output(outputText, true);
	}
	
	// outputs message to the output panel with option to show time and date
	public void output(String outputText, boolean showTimeAndDate) {
		
		if (showTimeAndDate) {
			date.setTime(System.currentTimeMillis());
			outputText = date.toString() + "  :  " + outputText;
		}
		
		Label newLabel = new Label(outputText);
		outputVBox.getChildren().add(newLabel);
		
		if (automaticOutputScrollEnabled) setOutputMaxScroll();
	}
	
	// scrolls as far as possible through the output
	public void setOutputMaxScroll() {
		outputScroll.applyCss();
		outputScroll.layout();
		outputScroll.setVvalue(outputScroll.getVmax());
	}
	
	// scrolls as far as possible through the notifications
	public void setNotificationMaxScroll() {
		notificationScroll.applyCss();
		notificationScroll.layout();
		notificationScroll.setVvalue(notificationScroll.getVmax());
	}
	
	// closes/deletes a notification
	public void dismissNotification(HorizontalNotification notif) {
		
		output("Dismissed notification : " + notif.toString());
		
		notificationVBox.getChildren().remove(notif);
	}
	
	// create a notification, given a CrimeData object
	public void createNotification(int type, CrimeData crimeData) {
		
		String description = "";
		
		description = description + crimeData.nature;

		HorizontalNotification notif = new HorizontalNotification(type, description, crimeData);
		
		notificationVBox.getChildren().add(notif);
		notif.parentPane = notificationVBox;
		
		sirenSoundPlayer.seek(Duration.ZERO);
		sirenSoundPlayer.play();
		
	}
	
	// method to obtain reference of crime-data-viewer node
	public CrimeDataView getCrimeDataView() {
		return crimeDataView;
	}
	
	// method to obtain reference to root-node
	public Pane getRootNode() {
		return rootNode;
	}
}
