package com.theone.differentialequationresolver;

import sun.java2d.cmm.ProfileDeferralInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class Resolver {

    static class Particle {

        Particle( double c, double d, double s) {
            this.coordinate = c;
            this.deviation = d;
            this.speed = s;
            this.acceleration = 0;
        }
        Particle( ) {
            this( 0.0f, 0.0f, 0.0f);
        }

        double coordinate;
        double deviation;
        double speed;
        double acceleration;

        @Override
        public String toString( ) {

            String info = "x = " + Double.toString( coordinate ) +
                            " y = " + Double.toString( deviation ) +
                            " v = " + Double.toString( speed ) + "\n";

            return info;
        }
    }

    static ArrayList<Particle> particles;

    static int particleAmount;
    static double alpha = 0.4;
    static double timeStep = 0.1;

    static boolean active = true;

    public static void startResolving() {

        particles = new ArrayList<Particle>( );
        init( particles );
        //run( particles );

    }

    private static void init( ArrayList<Particle> particles ) {

        Scanner scan = new Scanner( System.in );

        System.out.printf( "Enter amount of particles: " );
        particleAmount = scan.nextInt( );

        if ( particleAmount < 3 ) {

            particleAmount = 3;
        }
        System.out.printf( "Particle amount is set to %d...\n", particleAmount );

        initParticleCoordinates( particles );
    }

    private static void initParticleCoordinates( ArrayList<Particle> particles ) {

        System.out.printf( "Initializing particles...\n");

        double A = 0.5;
        double a = 0.1;

        //particles.add( new Particle( ) );

        for ( int i = 0; i < particleAmount; i++ ) {

            double deviation = A * sin( PI * i / ( particleAmount - 1 ) );
            double coordinate = i * a;

            particles.add( new Particle( coordinate, deviation, 0.0) );
        }

        //particles.add( new Particle( particleAmount * a, 0.0, 0.0) );

        System.out.printf( "Logging particles...\n");
        for ( int i = 0; i < particleAmount; i++ ) {

            System.out.printf( particles.get( i ).toString( ) );
        }
    }

    static void run( ArrayList<Particle> particles ) {

        //System.out.printf( "Doing an iteration\n" );

        boolean temp = true;

        iterateRKMethod( particles );
        /*try {
            TimeUnit.SECONDS.sleep( 5 );
        } catch ( InterruptedException e ) {
            e.printStackTrace( );
        }*/
    }

    static private void iterateRKMethod( ArrayList<Particle> particles ) {

        for ( int i = 1; i < particleAmount - 1; i++ ) {

            particles.get( i ).deviation = calculateDeviationInStep( particles, i );
            particles.get( i ).speed = calculateSpeedInStep( particles, i );

            //System.out.printf( particles.get( i ).toString( ) );
        }

    }

    static private double calculateDeviationInStep( ArrayList<Particle> particles, int index ) {

        double derivative = calculateFirstDerivative( particles, index );
        double[] K = calculateRKCoefficients( derivative );

        double oldDeviation = particles.get( index ).deviation;

        return oldDeviation + 1.0/6.0 * ( K[ 0 ] + 4 * K[ 3 ] + K[ 4 ] );
    }

    private static double calculateSpeedInStep( ArrayList<Particle> particles, int index ) {

        double derivative = calculateSecondDerivative( particles, index );
        double[] K = calculateRKCoefficients( derivative );

        double oldSpeed = particles.get( index ).speed;

        return oldSpeed + 1.0/6.0 * ( K[ 0 ] + 4 * K[ 3 ] + K[ 4 ] );

    }

    private static double calculateSecondDerivative( ArrayList<Particle> particles, int index ) {

        double prevCoordinate = particles.get( index - 1 ).deviation;
        double nextCoordinate = particles.get( index + 1 ).deviation;
        double thisCoordinate = particles.get( index ).deviation;

        double derivative;

        derivative =
                ( 1 + alpha * ( nextCoordinate - prevCoordinate ) )
                * ( nextCoordinate + prevCoordinate - 2 * thisCoordinate );

        //System.out.printf( "Acceleration " + Integer.toString( index ) + ": " + Double.toString( derivative ) + "\n" );

        return derivative;
    }
    private static double calculateFirstDerivative( ArrayList<Particle> particles, int index ) {

        double speed = particles.get( index ).speed;

        //System.out.printf( "Speed " + Integer.toString( index ) + ": " + Double.toString( speed ) + "\n" );

        return particles.get( index ).speed;
    }

    static private double[] calculateRKCoefficients( double derivative ) {

        double[] K = new double[ 5 ];

        for ( int i = 0; i < 5; i++ ) {

            K[ i ] = timeStep * derivative;
        }

        return K;
    }
}
