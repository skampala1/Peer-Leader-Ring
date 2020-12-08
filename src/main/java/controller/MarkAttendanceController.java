package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.AppContext;
import dto.Attendace;
import entity.Activity;
import handler.GetActivitiesHandler;
import handler.GetGroupHandler;
import handler.GetStudentsFromGroupHandler;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import response.Response;
import util.Helper;

public class MarkAttendanceController {

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private Label label;

        @FXML
        private Label GroupListNameLabel;

        @FXML
        private Label GroupListGroupNameLabel;

        @FXML
        private Label GroupListTeamLeaderLabel;

        @FXML
        private TableView<Attendace> tableView;

        @FXML
        private TableColumn<Attendace, Long> idColumn;

        @FXML
        private TableColumn<Attendace, String> nameColumn;

        @FXML
        private Button GroupListMarkAttendButton;

        @FXML
        private Button GroupListViewActButton;

        @FXML
        private Button SignOutButton;

        final GetGroupHandler groupHandler;

        public MarkAttendanceController() {
                groupHandler = new GetGroupHandler();
        }

        @FXML
        void GroupListMarkAttendOnClick(ActionEvent event) {
                // todo
        }

        @FXML
        void GroupListViewActButtonOnClick(ActionEvent event) throws IOException {
                Helper.loadView(getClass().getResource("ActivityList.fxml"));
        }

        @FXML
        void SignOutButtonOnClick(ActionEvent event) throws IOException {
                Helper.loadView(getClass().getResource("Login.fxml"));
        }

        @FXML
        void initialize() {
                assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert GroupListNameLabel != null
                                : "fx:id=\"GroupListNameLabel\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert GroupListGroupNameLabel != null
                                : "fx:id=\"GroupListGroupNameLabel\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert GroupListTeamLeaderLabel != null
                                : "fx:id=\"GroupListTeamLeaderLabel\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert tableView != null
                                : "fx:id=\"tableView\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert idColumn != null : "fx:id=\"IdColumn\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert nameColumn != null
                                : "fx:id=\"nameColumn\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert GroupListMarkAttendButton != null
                                : "fx:id=\"GroupListMarkAttendButton\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert GroupListViewActButton != null
                                : "fx:id=\"GroupListViewActButton\" was not injected: check your FXML file 'GroupList.fxml'.";
                assert SignOutButton != null
                                : "fx:id=\"SignOutButton\" was not injected: check your FXML file 'GroupList.fxml'.";

                // GroupListNameLabel.setText(AppContext.getUser().getFullName());
                // GroupListGroupNameLabel.setText(groupResponse.getResponse().getName());
                // GroupListTeamLeaderLabel
                // .setText(groupResponse.getResponse().getTeamLeader().getUserDetail().getFullName());
                // GroupListTeamLeaderLabel.setText(String.valueOf(response.getResponse().getTeamLeader()));

                Response<List<Activity>> activities = new GetActivitiesHandler().handle();

                for (var activity : activities.getResponse()) {

                        TableColumn<Attendace, String> col = new TableColumn<Attendace, String>(
                                        String.valueOf(activity.getId()));

                        col.setMinWidth(50);

                        col.setCellValueFactory(
                                        new Callback<CellDataFeatures<Attendace, String>, ObservableValue<String>>() {
                                                public ObservableValue<String> call(
                                                                CellDataFeatures<Attendace, String> p) {

                                                        if (p.getValue().getActivity().containsKey(activity)) {
                                                                return new ReadOnlyObjectWrapper<String>(
                                                                                p.getValue().getActivity().get(activity)
                                                                                                ? "Yes"
                                                                                                : "No");
                                                        } else {
                                                                p.getValue().getActivity().put(activity,
                                                                                Boolean.valueOf(false));
                                                                return new ReadOnlyObjectWrapper<String>("No");
                                                        }
                                                }
                                        });

                        col.setCellFactory(TextFieldTableCell.forTableColumn());

                        tableView.getColumns().add(col);
                }

                Response<List<entity.Student>> students = new GetStudentsFromGroupHandler().handle(1L);

                students.getResponse().forEach(student -> tableView.getItems().add(new Attendace(student)));

                idColumn.setCellValueFactory(new Callback<CellDataFeatures<Attendace, Long>, ObservableValue<Long>>() {
                        public ObservableValue<Long> call(CellDataFeatures<Attendace, Long> p) {
                                return new ReadOnlyObjectWrapper<Long>(Long.valueOf(p.getValue().getStudent().getId()));
                        }
                });

                nameColumn.setCellValueFactory(
                                new Callback<CellDataFeatures<Attendace, String>, ObservableValue<String>>() {
                                        public ObservableValue<String> call(CellDataFeatures<Attendace, String> p) {
                                                return new ReadOnlyObjectWrapper<String>(p.getValue().getStudent()
                                                                .getUserDetail().getFullName());
                                        }
                                });

                // Making the columns editable except the ID field
                // nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

                // nameColumn.setOnEditCommit(new EventHandler<CellEditEvent<Student, String>>()
                // {
                // public void handle(CellEditEvent<Student, String> t) {
                // System.out.println("It works1!");
                // }

                // });

        }

}