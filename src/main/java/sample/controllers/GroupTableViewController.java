package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import sample.entities.Subject;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupTableViewController implements Initializable {

    @FXML
    TableView<Subject> groupTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
