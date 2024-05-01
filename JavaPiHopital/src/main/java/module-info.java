module JavaPiHopital {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.apache.commons.codec;
    requires java.xml.bind;
    requires kernel;
    requires layout;
    requires chatgpt;
    requires fastjson;

    //  exports votre.package.contenant.les.classes; // Spécifiez les packages que vous souhaitez exporter


    exports Controllers;
    opens  Controllers to javafx.fxml;

    exports Models;
    opens  Models to javafx.fxml;



    exports Services;
    opens  Services to javafx.fxml;

    exports Test;
    opens  Test to javafx.fxml;

    exports Util;
    opens Util to javafx.fxml;
}