import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;

public class Main extends Application {
    //core properties
    File image;
    File [] imageList;
    int pos;
    // core Controllers
    Button open = new Button("打开图片");
    Button next = new Button("下一张");
    Button last = new Button("上一张");
    ImageView imageView = new ImageView();

    public static void main(String[] args) {
        System.out.println("Loading");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 控件初始化
        next.setDisable(true);
        last.setDisable(true);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please choose the Photo");
        try{
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+"/Pictures"));
        }catch (Exception e){
            System.out.println("failed to find your photos directory,so I set the default directory as your users home");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("all file","*.*"),
                new FileChooser.ExtensionFilter("jpg","*.jpg"),
                new FileChooser.ExtensionFilter("png","*.png"),
                new FileChooser.ExtensionFilter("jpeg","*.jpeg")
        );
        open.setOnAction(event -> {
            image=fileChooser.showOpenDialog(primaryStage);
            if(image!=null){
                System.out.println("Success");
            }
            refreshImageList();
            refreshImage();
        });
        next.setOnAction(event -> {
            pos++;
            image=imageList[pos];
            refreshImage();
        });
        last.setOnAction(event -> {
            pos--;
            image=imageList[pos];
            refreshImage();
        });
        //Container organization
        BorderPane container = new BorderPane();
        FlowPane bottom = new FlowPane(last,next);
        container.setTop(open);
        container.setCenter(imageView);
        container.setBottom(bottom);

        Scene scene = new Scene(container,600,400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PhotoViewer");
        primaryStage.show();
    }
    private void refreshImage(){
        imageView.setImage(new Image("file:"+image.getPath()));
        if(pos<=0){
            last.setDisable(true);
        }
        if(pos>= imageList.length-1){
            next.setDisable(true);
        }
        if(pos>0&&pos<imageList.length-1){
            last.setDisable(false);
            next.setDisable(false);
        }

    }
    private void refreshImageList(){
        File folder= new File(image.getParent());
        imageList=folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg")||name.endsWith(".png")||name.endsWith(".jpeg");
            }
        });
        pos=0;
        for(File theImage:imageList){
            if(theImage.equals(image)){
                break;
            }
            pos++;
        }
    }
}