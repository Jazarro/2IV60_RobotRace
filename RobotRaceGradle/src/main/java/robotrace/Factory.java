package robotrace;

import javax.media.opengl.*;
import robotrace.bender.*;

public class Factory{

    private final Bender bender;

    public Factory(){
        bender = new Bender();
    }

    public void initialize(GL2 gl){
        bender.initialize(gl);
    }

    public Robot createRobot(Material material){
        Robot robot = new Robot(material, bender);
        //robot.initialize();
        return robot;
    }

    public Robot createRobot(Material material, int robotType){
        Robot robot = new Robot(material, robotType, bender);
        //robot.initialize();
        return robot;
    }
}
