package com.theone.differentialequationresolver;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.*;

public class GraphicWindow extends Application {

    final int GRAPH_SIZE_X = 600;
    final int GRAPH_SIZE_Y = 400;

    public static void main( String argc ) {

        launch(argc);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Resolver.startResolving();

        primaryStage.setTitle("Graphic for resolver");

        final NumberAxis xAxis = new NumberAxis( );
        final NumberAxis yAxis = new NumberAxis( );
        xAxis.setLabel( "Particle coordinate" );
        yAxis.setLabel( "Particle deviation" );
        final LineChart< Number, Number > graph = new LineChart<Number, Number>( xAxis, yAxis );

        //graph.setTitle( "Particles" );

        XYChart.Series< Number, Number > series = new XYChart.Series<Number, Number>( );
        series.setName( "Particles" );

        //series.getData().add( new XYChart.Data<Number, Number>( 0, 0 ) );
        //series.getData().add( new XYChart.Data<Number, Number>( 100, 100 ) );

        renewGraphPoints( series );

        Scene scene = new Scene( graph, GRAPH_SIZE_X, GRAPH_SIZE_Y );
        primaryStage.setScene(scene);
        graph.getData().add( series );
        primaryStage.show();

        //Closing
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            Resolver.active = false;
            primaryStage.close();
        });

        //Animating
        Timeline timeline = new Timeline();
        timeline.setCycleCount( Timeline.INDEFINITE );
        timeline.setAutoReverse( false );
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis( 100 ),
                        event -> {
                            series.getData().remove( 0, 10 );
                            Resolver.run( Resolver.particles );
                            renewGraphPoints( series );
                        })
        );
        timeline.play();
    }

    private void renewGraphPoints( XYChart.Series series ) {

        for ( int i = 0; i < Resolver.particleAmount; i++ ) {

            double x = Resolver.particles.get( i ).coordinate;
            double y = Resolver.particles.get( i ).deviation;
            series.getData().add( new XYChart.Data<>( x, y ));
        }
    }
}
