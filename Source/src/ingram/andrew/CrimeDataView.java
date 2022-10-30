package ingram.andrew;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CrimeDataView extends Pane {
	
	private VBox viewHBox;
	private HBox agencyBox;
	private Label agencyLabel;
	private HBox serviceBox;
	private Label serviceLabel;
	private HBox natureBox;
	private Label natureLabel;
	private HBox startTimeBox;
	private Label startTimeLabel;
	private HBox addressBox;
	private Label addressLabel;
	
	public CrimeDataView() {
		viewHBox = new VBox();
		getChildren().add(viewHBox);
		
		agencyBox = new HBox();
		agencyBox.getChildren().add(new Label("Agency : "));
		
		serviceBox = new HBox();
		serviceBox.getChildren().add(new Label("Service : "));
		
		natureBox = new HBox();
		natureBox.getChildren().add(new Label("Nature : "));
		
		startTimeBox = new HBox();
		startTimeBox.getChildren().add(new Label("Start Time : "));
		
		addressBox = new HBox();
		addressBox.getChildren().add(new Label("Address : "));
		
		viewHBox.getChildren().addAll(agencyBox, serviceBox, natureBox, startTimeBox, addressBox);
		
		agencyLabel = new Label();
		serviceLabel = new Label();
		natureLabel = new Label();
		startTimeLabel = new Label();
		addressLabel = new Label();
		
		agencyBox.getChildren().add(agencyLabel);
		serviceBox.getChildren().add(serviceLabel);
		natureBox.getChildren().add(natureLabel);
		startTimeBox.getChildren().add(startTimeLabel);
		addressBox.getChildren().add(addressLabel);
		
	}
	
	public void setAgency(String agency) {
		agencyLabel.setText(agency);
	}
	
	public void setService(String service) {
		serviceLabel.setText(service);
	}
	
	public void setNature(String nature) {
		natureLabel.setText(nature);
	}
	
	public void setStartTime(String startTime) {
		startTimeLabel.setText(startTime);
	}
	
	public void setAddress(String address) {
		addressLabel.setText(address);
	}
	
	public void setData(CrimeData crimeData) {
		agencyLabel.setText(crimeData.agency);
		serviceLabel.setText(crimeData.service);
		natureLabel.setText(crimeData.nature);
		startTimeLabel.setText(crimeData.starttime);
		addressLabel.setText(crimeData.address);
	}
	
}
