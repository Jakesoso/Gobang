import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;

public class GobangIntro extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个WebView组件
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        // 加载指定的HTML页面
        File htmlFile = new File("resource/index.html");
        webView.getEngine().load(htmlFile.toURI().toString());

        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // WebView加载完页面后执行回调函数
                JSObject window = (JSObject) engine.executeScript("window");
                Board board = new Board();
                GobangPattern pattern = new GobangPattern();
                GobangAI gobangAI = new GobangAI(pattern, board);
                Point point = new Point();
                window.setMember("board", board);
                window.setMember("gobangAI", gobangAI);
                window.setMember("point", point);
            }
        });

        webView.getEngine().setOnAlert((WebEvent<String> wEvent) -> {
            System.out.println("JS msg: " + wEvent.getData());
        });

        // 创建一个Scene对象并将WebView添加到其中
        Scene scene = new Scene(webView);

        // 设置Stage的标题和大小，并将Scene设置为它的场景
        primaryStage.setTitle("五子棋对战");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(1000);

        // 显示窗口
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
