package com.prashant.java.krishi.classifier.controllers;

import com.prashant.java.krishi.classifier.modal.wheat.ImmatureStatus;
import com.prashant.java.krishi.classifier.modal.wheat.ParticleType;
import com.prashant.java.krishi.classifier.modal.wheat.WheatDimension;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

/**
 *
 */
@RequiredArgsConstructor
public class NativeClassifier {

    private static final double aream = 2135.5625;
    private static final double areal = 29.99502473;
    private static final double arear = 27.4137376;
    private static final double areal1 = aream - (aream * areal / 60);
    private static final double areal2 = aream + (aream * arear / 100);
    private static final double majm = 74.0927;
    private static final double majl = 17.82994779;
    private static final double majr = 21.48162395;
    private static final double majl1 = majm - (majm * majl / 100);
    private static final double majl2 = majm + (majm * majr / 100);
    private static final double minm = 36.5298;
    private static final double minl = 20.2102396;
    private static final double minr = 12.15242371;
    private static final double minl1 = minm - (minm * minl / 100);
    private static final double minl2 = minm + (minm * minr / 100);
    private static final double solidm = 0.9656;
    private static final double solidl = 2.03236246;
    private static final double solidr = 0.556634304;
    private static final double solidl1 = solidm - (solidm * solidl / 100);
    private static final double solidl2 = solidm + (solidm * solidr / 100);
    private final String rdata;
    private final WheatDimension dimension;
    ArrayList good_arr = new ArrayList();
    ArrayList bad_arr = new ArrayList();
    ArrayList other_arr = new ArrayList();
    private int tot_particle = 0;
    private int good_particle = 0;
    private int immeture_count = 0;
    private int bad_particle = 0;
    private int other_particle = 0;
    private long main3 = 0;
    private String bad = "";
    private String other = "";
    private String immeture = "";

    public WheatDimension processWheatDimension() {
        ParticleType particleType = ParticleType.GRAIN;
        ImmatureStatus immatureStatus = ImmatureStatus.HEALTHY_WHEAT;
        // This is the place to inject the naive code

        //1000	120	47	34
        long area = dimension.getArea().longValue();

        Float maj0 = dimension.getMajor().floatValue();
        Float min0 = dimension.getMinor().floatValue();

        Float solid0 = dimension.getSolidity().floatValue();

        int bx = dimension.getBx().intValue();
        int by = dimension.getBy().intValue();
        int width = dimension.getWidth().intValue();
        int heigth = dimension.getHeight().intValue();

        if (bx < 800 && by < 100 && width < 30 && heigth < 25) {
            bad_particle = bad_particle + 1;
            particleType = ParticleType.BAD_WHEAT;
        }
        if (bx > 800 && bx < 1100 && by > 100 && by < 150 && width > 30 && width < 70 && heigth > 25 && heigth < 60) {
            good_particle = good_particle + 1;
        }
        if (bx > 1100 && by > 150 && width > 70 && heigth > 60) {
            other_particle = other_particle + 1;
            particleType = ParticleType.UNKNOWN;
        }
        if (area >= (areal1) && area <= (areal2)) {

            if ((area < aream - (aream * minl / 100)) && (maj0 < majm || min0 < minm)) {
                immatureStatus = ImmatureStatus.BROKEN_WHEAT;
                immeture_count++;
                immeture += dimension.getFileParticleName() + ",";
            } else if (solid0 < (solidm)) {
                particleType = ParticleType.BAD_WHEAT;
                bad += dimension.getFileParticleName() + ",";
                bad_particle++;
            } else {
                good_arr.add(dimension.getFileParticleName());
                good_particle++;
            }

        } else if (area < (areal1)) {
            particleType = ParticleType.BAD_WHEAT;
            bad += dimension.getFileParticleName() + ",";
            bad_particle++;
        } else if (area > (areal2)) {
            other += dimension.getFileParticleName() + ",";
            other_particle++;
            particleType = ParticleType.UNKNOWN;
        }
        tot_particle = tot_particle + 1;
        System.out.println("Total particles = " + tot_particle);

        System.out.println("Total left_particle = " + bad_particle);
        System.out.println(bad + " " + ((double) bad_particle / (double) tot_particle) * 100 + "%");

        System.out.println("Total immeture  = " + immeture_count);
        System.out.println(immeture + " " + ((double) immeture_count / (double) tot_particle) * 100 + "%");

        System.out.println("Total right_particle = " + other_particle);
        System.out.println(other + " " + ((double) other_particle / (double) tot_particle) * 100 + "%");

        System.out.println("Completed Classification");

        // Classification completed

        return dimension.withParticleType(particleType.getStringValue())
            .withImmatureStatus(immatureStatus.getStringValue());
    }
}
