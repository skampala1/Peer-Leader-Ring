package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.AppContext;
import dto.Group;
import handler.GetGroupsHandler;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import response.Response;
import util.Helper;

public class StudentGroupController {

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Label label;

        @FXML
        private Button SignOutButton;

        @FXML
        private TableView<Group> tableView;

        @FXML
        private TableColumn<Group, String> StudentGroupGroupIDcolumn;

        @FXML
        private TableColumn<Group, String> StudentGroupGroupNameColumn;

        @FXML
        private TableColumn<Group, String> StudentGroupPeerLeaderColumn;

        @FXML
        private TableColumn<Group, String> StudentGroupTeamLeaderColumn;

        @FXML
        private Button BackButton;

        @FXML
        private Button SaveButton;

        @FXML
        private Button DeleteButton;

        int editRow = -1;

        Boolean rejectChange = false;

        @FXML
        void BackButtonOnClick(ActionEvent event) throws IOException {
                Helper.loadView(getClass().getResource("Admin.fxml"));
        }

        @FXML
        void SignOutButtonOnClick(ActionEvent event) throws IOException {
                Helper.loadView(getClass().getResource("Login.fxml"));

        }

        @FXML
        void DeleteButtonOnClick(ActionEvent event) {
                if (!Helper.onDeleteCheck(editRow))
                        return;

                var toDelete = tableView.getSelectionModel().getSelectedItem();

                int index = tableView.getItems().indexOf(toDelete);

                if (index + 1 == tableView.getItems().size()) {
                        Helper.createAlert("Error in Deletion", "Invalid Selection");
                        return;
                }

                var response = toDelete.delete();

                if (response.success()) {
                        tableView.getItems().remove(index);
                        tableView.refresh();
                } else
                        Helper.createAlert("Error in Deletion", response.getException().getMessage());
        }

        @FXML
        void SaveButtonOnClick(ActionEvent event) {

                if (editRow == -1) {
                        Helper.createAlert("Error", "No row was been modified");
                } else {
                        var respone = tableView.getItems().get(editRow).updateOrSave();

                        if (respone.hasException()) {
                                Helper.createAlert("Error", respone.getException().getMessage());

                                var resetResponse = tableView.getItems().get(editRow).reset();

                                if (resetResponse.hasException()) {
                                        Helper.createAlert("Database Error", resetResponse.getException().getMessage());
                                        tableView.getItems().remove(editRow);
                                }
                        } else if (editRow + 1 == tableView.getItems().size()) {
                                tableView.getItems().add(new Group("<Default>", "<Insert>", "<Insert>", "<Insert>"));
                        }

                        tableView.refresh();

                        editRow = -1;
                }
        }

        @FXML
        void idEditStart(CellEditEvent<Group, String> t) {
                if (editRow != -1) {
                        Helper.createAlert("Error", "Save changes first");
                        return;
                }

                if (Helper.getRow(t) + 1 == tableView.getItems().size()) {
                        Helper.createAlert("Error", "Group id is created by default");
                        return;
                }

                AppContext.put("groupId", Long.parseLong(tableView.getSelectionModel().getSelectedItem().getId()));

                Helper.loadView(getClass().getResource("GroupList.fxml"));

        }

        @FXML
        void groupnameEditStart(CellEditEvent<Group, String> t) {

                Helper.onEditStartCheck(t, editRow);
        }

        @FXML
        void groupnameEditCommit(CellEditEvent<Group, String> t) {

                if (!Helper.onEditCommitCheck(t, editRow)) {
                        tableView.refresh();
                        return;
                }

                // to do your valiidation
                System.out.println(t.getNewValue());
                // FOR SOME REASON THIS CHECKING CRITERIA SHOWS FUNCTION DEFINITON NOT FOUND
                if (t.getNewValue().length() > 20) {
                        Helper.createAlert("Cannot Edit", "Please follow the constraint requirements");
                        tableView.refresh();
                } else {
                        editRow = Helper.getRow(t);
                        tableView.getSelectionModel().getSelectedItem().setName(t.getNewValue());
                }
        }

        @FXML
        void grouppeerleaderEditStart(CellEditEvent<Group, String> t) {
                Helper.onEditStartCheck(t, editRow);
        }

        @FXML
        void grouppeerleaderEditCommit(CellEditEvent<Group, String> t) {
                if (!Helper.onEditCommitCheck(t, editRow)) {
                        tableView.refresh();
                        return;
                }

                // to do your valiidation
                System.out.println(t.getNewValue());
                // FOR SOME REASON THIS CHECKING CRITERIA SHOWS FUNCTION DEFINITON NOT FOUND
                if (!Helper.isNumeric(t.getNewValue()) || t.getNewValue().isEmpty()) {
                        Helper.createAlert("Cannot Edit", "Please follow the constraint requirements");
                        tableView.refresh();
                } else {
                        editRow = Helper.getRow(t);
                        tableView.getSelectionModel().getSelectedItem().setPeerLeaderId(t.getNewValue());
                }
        }

        @FXML
        void groupteamleaderEditStart(CellEditEvent<Group, String> t) {
                Helper.onEditStartCheck(t, editRow);
        }

        @FXML
        void groupteamleaderEditCommit(CellEditEvent<Group, String> t) {
                if (!Helper.onEditCommitCheck(t, editRow)) {
                        tableView.refresh();
                        return;
                }

                // to do your valiidation
                System.out.println(t.getNewValue());
                // FOR SOME REASON THIS CHECKING CRITERIA SHOWS FUNCTION DEFINITON NOT FOUND
                if (!Helper.isNumeric(t.getNewValue()) || t.getNewValue().isEmpty()) {
                        Helper.createAlert("Cannot Edit", "Please follow the constraint requirements");
                        tableView.refresh();
                } else {
                        editRow = Helper.getRow(t);
                        tableView.getSelectionModel().getSelectedItem().setTeamLeaderId(t.getNewValue());
                }
        }

        @FXML
        void initialize() {
                assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert SignOutButton != null
                                : "fx:id=\"SignOutButton\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert tableView != null
                                : "fx:id=\"tableview\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert StudentGroupGroupIDcolumn != null
                                : "fx:id=\"StudentGroupGroupIDcolumn\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert StudentGroupGroupNameColumn != null
                                : "fx:id=\"StudentGroupGroupNameColumn\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert StudentGroupPeerLeaderColumn != null
                                : "fx:id=\"StudentGroupPeerLeaderColumn\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert StudentGroupTeamLeaderColumn != null
                                : "fx:id=\"StudentGroupTeamLeaderColumn\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert BackButton != null
                                : "fx:id=\"BackButton\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert SaveButton != null
                                : "fx:id=\"SaveButton\" was not injected: check your FXML file 'StudentGroup.fxml'.";
                assert DeleteButton != null
                                : "fx:id=\"DeleteButton\" was not injected: check your FXML file 'StudentGroup.fxml'.";

                Response<List<entity.Group>> response = new GetGroupsHandler().handle();

                if (response.success()) {

                        response.getResponse().forEach(dbGroup -> tableView.getItems().add(new Group(dbGroup)));
                        tableView.getItems().add(new Group("<Default>", "<Insert>", "<Insert>", "<Insert>"));

                } else {
                        // todo add alert box
                }

                StudentGroupGroupIDcolumn.setCellValueFactory(
                                new Callback<CellDataFeatures<Group, String>, ObservableValue<String>>() {
                                        public ObservableValue<String> call(CellDataFeatures<Group, String> p) {
                                                return new ReadOnlyObjectWrapper<String>(p.getValue().getId());
                                        }
                                });

                StudentGroupGroupNameColumn.setCellValueFactory(
                                new Callback<CellDataFeatures<Group, String>, ObservableValue<String>>() {
                                        public ObservableValue<String> call(CellDataFeatures<Group, String> p) {
                                                return new ReadOnlyObjectWrapper<String>(p.getValue().getName());
                                        }
                                });

                StudentGroupPeerLeaderColumn.setCellValueFactory(
                                new Callback<CellDataFeatures<Group, String>, ObservableValue<String>>() {
                                        public ObservableValue<String> call(CellDataFeatures<Group, String> p) {
                                                return new ReadOnlyObjectWrapper<String>(
                                                                p.getValue().getPeerLeaderId());
                                        }
                                });

                StudentGroupTeamLeaderColumn.setCellValueFactory(
                                new Callback<CellDataFeatures<Group, String>, ObservableValue<String>>() {
                                        public ObservableValue<String> call(CellDataFeatures<Group, String> p) {
                                                return new ReadOnlyObjectWrapper<String>(
                                                                p.getValue().getTeamLeaderId());
                                        }
                                });

                // Skipped making the ID editable
                StudentGroupGroupNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                StudentGroupPeerLeaderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                StudentGroupTeamLeaderColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        }
}
