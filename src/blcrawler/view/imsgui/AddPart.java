package blcrawler.view.imsgui;

import java.util.Collections;

import blcrawler.model.CatalogPart;
import blcrawler.model.ConsoleGUIModel;
import blcrawler.model.bsx.inventorylot.InventoryLocation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddPart
{
    static ListView<String> categoryList;
    static TableView<CatalogPart> partTable;
    static String categoryToDisplay;
    static ObservableList<CatalogPart> partSubList;
    static ListView<String> colorList;
    static ObservableList<String> colors;
    static ChoiceBox<String> itemTypes;
    static Label filterLabel;
    static TextField filter;
    static ChoiceBox<String> colorDisplayMode;
    
    public static void display()
    {
        Stage window = new Stage();
        
        window.setTitle("Add part");
        
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());
        
        itemTypes = new ChoiceBox<>();
        itemTypes.getItems().add("Book");
        itemTypes.getItems().add("Catalog");
        itemTypes.getItems().add("Gear");
        itemTypes.getItems().add("Instruction");
        itemTypes.getItems().add("Minifig");
        itemTypes.getItems().add("Original Box");
        itemTypes.getItems().add("Part");
        itemTypes.getItems().add("Set");
        itemTypes.getItems().add("Unsorted Lot");
        
        itemTypes.setValue("Part");
        itemTypes.setMinWidth(200);
        
        filterLabel = new Label("filter: ");
        filterLabel.setPadding(new Insets(0, 20, 0, 10));
        
        filter = new TextField();
        filter.setMinWidth(500);
        
        colorDisplayMode = new ChoiceBox<>();
        colorDisplayMode.getItems().add("Show All");
        colorDisplayMode.getItems().add("BL For Sale");
        colorDisplayMode.getItems().add("BL Menu");
        colorDisplayMode.getItems().add("BL Known");
        colorDisplayMode.getItems().add("BL Price Guide");
        colorDisplayMode.getItems().add("BL Wanted");
        
        colorDisplayMode.setValue("BL Menu");
        
        categoryList = new ListView<>();
        categoryList.getItems().add("All Items");
        categoryList.getItems().addAll(ConsoleGUIModel.getDatabase().getPartCategories());
        categoryList.setPrefHeight(500);
        partSubList = FXCollections.observableArrayList();
        colors = FXCollections.observableArrayList();
        
        // Selection listening code
        categoryList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>()
                {
                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                            String oldValue, String newValue)
                    {
                        // Set action on selection change here
                        categoryToDisplay = newValue;
                        partSubList.clear();
                        for (CatalogPart part : ConsoleGUIModel.getDatabase().getCatalogParts())
                        {
                            if (newValue.equals("All Items")
                                    || part.getCategoryName().equals(newValue))
                                partSubList.add(part);
                        }
                        partTable.scrollTo(0);
                        System.out.println("ListView selection changed from oldValue = " + oldValue
                                + " to newValue = " + newValue);
                    }
                });
        
        TableColumn<CatalogPart, String> Number = new TableColumn<>("Part ID");
        Number.setMinWidth(50);
        Number.setCellValueFactory(new PropertyValueFactory<>("partNumber"));
        
        TableColumn<CatalogPart, String> Name = new TableColumn<>("Name");
        Name.setMinWidth(550);
        Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        partTable = new TableView<>();
        partTable.setItems(partSubList);
        partTable.getColumns().add(Number);
        partTable.getColumns().add(Name);
        partTable.setPrefHeight(500);
        
        partTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<CatalogPart>()
                {
                    @Override
                    public void changed(ObservableValue<? extends CatalogPart> observable,
                            CatalogPart oldValue, CatalogPart newValue)
                    {
                        // Set action on selection change here
                        colors.clear();
                        colorList.getItems().clear();
                        if (oldValue == null)
                        {
                            System.out.println("CatalogPart selection changed from oldValue = "
                                    + "null" + " to newValue = " + newValue.getName());
                            for (String color : newValue.getKnownColorsBLMenu())
                            {
                                colors.add(color);
                                
                            }
                            Collections.sort(colors);
                            colorList.getItems().addAll(colors);
                        }
                        else if (newValue == null)
                        {
                            System.out.println("CatalogPart selection changed from oldValue = "
                                    + oldValue.getName() + " to newValue = " + "null");
                        }
                        else
                        {
                            System.out.println("CatalogPart selection changed from oldValue = "
                                    + oldValue.getName() + " to newValue = " + newValue.getName());
                            for (String color : newValue.getKnownColorsBLMenu())
                            {
                                colors.add(color);
                                
                            }
                            Collections.sort(colors);
                            colorList.getItems().addAll(colors);
                        }
                        
                    }
                });
        
        colorList = new ListView<>();
        // colorList.getItems().addAll(colors);
        colorList.setPrefHeight(500);
        
        BorderPane mainLayout = new BorderPane();
        HBox top = new HBox();
        HBox bottom = new HBox();
        VBox left = new VBox();
        VBox middle = new VBox();
        VBox right = new VBox();
        mainLayout.setPadding(new Insets(20, 20, 20, 20));
        
        bottom.getChildren().add(closeButton);
        left.getChildren().add(categoryList);
        middle.getChildren().add(partTable);
        right.getChildren().add(colorList);
        top.getChildren().add(itemTypes);
        top.getChildren().add(filterLabel);
        top.getChildren().add(filter);
        top.getChildren().add(colorDisplayMode);
        mainLayout.setTop(top);
        mainLayout.setBottom(bottom);
        mainLayout.setLeft(left);
        mainLayout.setCenter(middle);
        mainLayout.setRight(right);
        
        Scene scene = new Scene(mainLayout);
        window.setScene(scene);
        window.show();
    }
}
