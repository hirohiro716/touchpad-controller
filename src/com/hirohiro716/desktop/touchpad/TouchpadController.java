package com.hirohiro716.desktop.touchpad;

import java.io.IOException;

import com.hirohiro716.javafx.FXMLLoader;
import com.hirohiro716.javafx.GenerationalRunLater;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * タッチパッドの有効無効をGUIで操作するアプリケーション.
 * @author hiro
 */
public class TouchpadController extends Application {
    
    /**
     * アプリケーションの開始.
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @FXML
    private AnchorPane paneRoot;
    
    @FXML
    private Button buttonEnable;
    
    @FXML
    private Button buttonDisable;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        if (Screen.getScreens().size() == 0 || this.getParameters().getRaw().size() != 1) {
            System.out.println("Device is not specified.");
            Platform.exit();
            return;
        }
        Screen screen = Screen.getPrimary();
        FXMLLoader.load(this.getClass().getResource(this.getClass().getSimpleName() + ".fxml"), this);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Touchpad Controller");
        primaryStage.setWidth(screen.getVisualBounds().getWidth());
        primaryStage.setHeight(screen.getVisualBounds().getHeight());
        Scene scene = new Scene(this.paneRoot);
        scene.setFill(null);
        primaryStage.setScene(scene);
        GenerationalRunLater.runLater(500, new Runnable() {
            @Override
            public void run() {
                primaryStage.requestFocus();
            }
        });
        // 有効ボタン
        String device = this.getParameters().getRaw().get(0);
        this.buttonEnable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Runtime.getRuntime().exec(new String[] {"/usr/bin/xinput", "set-prop", device, "Device Enabled", "1"});
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                primaryStage.close();
            }
        });
        // 無効ボタン
        this.buttonDisable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Runtime.getRuntime().exec(new String[] {"/usr/bin/xinput", "set-prop", device, "Device Enabled", "0"});
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                primaryStage.close();
            }
        });
        primaryStage.show();
    }

}
