package ingram.andrew;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class HorizontalNotification extends BorderPane {
	
	protected Pane parentPane;
	private CrimeData crimeData;
	private Image image;
	private ImageView imageView;
	private HBox buttonHBox;
	private Button dismissButton;
	private Button showButton;
	private HorizontalNotification self;
	private Label descriptionLabel;
	private String type = "unknown";
	private final Effect effect = new DropShadow(BlurType.GAUSSIAN, Color.LIGHTBLUE, 12, 0.8, 0, 0);
	
	public HorizontalNotification(int type, String description, CrimeData crimeData) {
		HBox.setHgrow(this, Priority.ALWAYS);
		this.crimeData = crimeData;
		setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		self = this;
		//setMinWidth(400);
		
		// set image of notification
		String imagePath = "file:images/unknownImage.png";
		switch (type) {
		case WeehWooh.SHOOTING:
			imagePath = "file:images/gun.png";
			this.type = "Shooting";
			break;
		case WeehWooh.STABBING:
			imagePath = "file:images/knife.png";
			this.type = "Stabbing";
			break;
		case WeehWooh.ROBBERY:
			imagePath = "file:images/robber.png";
			this.type = "Robbery";
			break;
		}
		image = new Image(imagePath,64, 64, true, true);
		imageView = new ImageView(image);
		setLeft(imageView);
		
		
		// make anchor pane and add it to the right
		//anchor = new AnchorPane();
		//setRight(anchor);
		
		// make button h-box
		buttonHBox = new HBox(8);
		buttonHBox.setAlignment(Pos.TOP_RIGHT);
		setRight(buttonHBox);
		//anchor.getChildren().add(buttonHBox);
		//AnchorPane.setRightAnchor(buttonHBox, 10.0);
		
		// make dismiss button
		dismissButton = new Button("Close");
		dismissButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// release focus to not scroll to top of notifications
						Main.programObject.getRootNode().requestFocus();
						
						// dismiss notification
						Main.programObject.dismissNotification(self);
					}
				});
				//parentPane.getChildren().remove(self);
			}
		});
		buttonHBox.getChildren().add(dismissButton);
		
		// make show button
		showButton = new Button("Show");
		
		showButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						
						Main.programObject.getCrimeDataView().setData(self.crimeData);
						self.requestFocus();
					}
					
				});
			}
		});
		buttonHBox.getChildren().add(showButton);
		
		// add description label
		descriptionLabel = new Label(description);
		descriptionLabel.setMaxWidth(210);
		descriptionLabel.setWrapText(true);
		setCenter(descriptionLabel);
		
		// listen for losing focus
		focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> obervableValue, Boolean onHidden, Boolean onShown) {
				self.setEffect(onShown ? effect : null);
			}
			
		});
	}
	
	public String toString() {
		String returnString = "{";
		returnString = returnString + "type : " + type;
		returnString = returnString + "}";
		return returnString;
	}
	
	public CrimeData getCrimeData() {
		return crimeData;
	}
}
