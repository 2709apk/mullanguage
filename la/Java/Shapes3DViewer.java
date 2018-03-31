import javafx.application.Application;  
import javafx.scene.*;  
import javafx.scene.paint.Color;  
import javafx.scene.paint.PhongMaterial;  
import javafx.scene.shape.*;  
import javafx.stage.Stage;  
   
public class Shapes3DViewer extends Application {  
    @Override public void start(Stage stage) {  
        PhongMaterial material = new PhongMaterial();  
        material.setDiffuseColor(Color.LIGHTGRAY);  
        material.setSpecularColor(Color.rgb(30, 30, 30));  
   
        Shape3D[] meshView = new Shape3D[10000];
        for(int i=0; i<meshView.length; i++)
            meshView[i] = new Box(200, 10, 5);
        // {  
        //     new Box(200, 200, 200),  
        //     new Sphere(100),  
        //     new Cylinder(100, 200),  
        // };  
   
        for (int i=0; i<meshView.length; i++) {  
            meshView[i].setMaterial(material);  
            meshView[i].setTranslateX(220);  
            meshView[i].setTranslateY(500);  
            meshView[i].setTranslateZ(20+22*i);  
            meshView[i].setDrawMode(DrawMode.FILL);  
            meshView[i].setCullFace(CullFace.BACK);  
        };  
   
        PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);  
        pointLight.setTranslateX(800);  
        pointLight.setTranslateY(-100);  
        pointLight.setTranslateZ(-1000);  
   
        Group root = new Group(meshView);  
        root.getChildren().add(pointLight);  
           
        Scene scene = new Scene(root, 800, 800, true);  
        scene.setFill(Color.rgb(10, 10, 40));  
        scene.setCamera(new PerspectiveCamera(false));  
        stage.setScene(scene);  
        stage.show();  
    }  
   
    public static void main(String[] args) {  
        launch(args);  
    }  
}  
