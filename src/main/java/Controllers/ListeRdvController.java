package Controllers;

import entities.RendezVous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.MyDataBase;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ListeRdvController implements Initializable {

    @FXML
    private TableColumn<RendezVous, LocalDate> dateLabel;

    @FXML
    private TableColumn<RendezVous, String> descLabel;

    @FXML
    private TableColumn<RendezVous, String> fileLabel;

    @FXML
    private TableColumn<RendezVous, String> heureLabel;

    @FXML
    private TextField keywordTextField;

    @FXML
    private TableColumn<RendezVous, Integer> rdvID;

    @FXML
    private TableView<RendezVous> rdvTableView;

    ObservableList<RendezVous> rendezVousObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final Connection connection = MyDataBase.getInstance().getConnection();

        String query = "SELECT id, daterendezvous, heurerendezvous, description, file FROM rendezvous ";

        try {
            Statement statement = connection.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next()){
                Integer queryRdvID = queryOutput.getInt("id");
                String queryDesc = queryOutput.getString("description");
                String queryHeure = queryOutput.getString("heurerendezvous");
                String queryFile = queryOutput.getString("file");
                LocalDate queryDate = queryOutput.getDate("daterendezvous").toLocalDate();
                Button accept = new Button();
                Button delte = new Button();
                rendezVousObservableList.add(new RendezVous(queryRdvID,queryDesc,queryDate,queryHeure,queryFile));
                rendezVousObservableList.add(new RendezVous(queryRdvID,queryDesc,queryDate,queryHeure,queryFile));
            }

            rdvID.setCellValueFactory(new PropertyValueFactory<>("id"));
            dateLabel.setCellValueFactory(new PropertyValueFactory<>("daterdv"));
            heureLabel.setCellValueFactory(new PropertyValueFactory<>("heurerdv"));
            descLabel.setCellValueFactory(new PropertyValueFactory<>("description"));
            fileLabel.setCellValueFactory(new PropertyValueFactory<>("file"));
            rdvTableView.setItems(rendezVousObservableList);

            // Create a filtered list
            FilteredList<RendezVous> filteredData = new FilteredList<>(rendezVousObservableList, b -> true);

            // Add listener to keywordTextField to filter data
            keywordTextField.textProperty().addListener((observable, oldvalue, newvalue) -> {
                filteredData.setPredicate(rendezVous -> {
                    if (newvalue == null || newvalue.trim().isEmpty()) {
                        // If text field is empty, show all items
                        return true;
                    }

                    String searchText = newvalue.toLowerCase();

                    // Filter based on search text
                    return rendezVous.getHeurerdv().toLowerCase().contains(searchText) ||
                            rendezVous.getDescription().toLowerCase().contains(searchText) ||
                            rendezVous.getFile().toLowerCase().contains(searchText) ||
                            rendezVous.getDaterdv().toString().contains(searchText);

                });
            });

            SortedList<RendezVous> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(rdvTableView.comparatorProperty());

            rdvTableView.setItems(sortedData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
