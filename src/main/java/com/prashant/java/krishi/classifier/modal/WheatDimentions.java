package com.prashant.java.krishi.classifier.modal;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

/**
 *
 */
@Data
@NoArgsConstructor
public class WheatDimentions {
    private double area;
    private double perim;
    private double bX;
    private double bY;
    private double width;
    private double height;
    private double major;
    private double minor;
    private double angle;
    private double circ;
    private double feret;
    private double feretX;
    private double feretY;
    private double feretAngle;
    private double minFeret;
    private double aR;
    private double round;
    private double solidity;

    public WheatDimentions(WebRequestUtil request) {
        this.area = request.doubleValue("area");
        this.perim = request.doubleValue("perim");
        this.bX = request.doubleValue("bX");
        this.bY = request.doubleValue("bY");
        this.width = request.doubleValue("width");
        this.height = request.doubleValue("height");
        this.major = request.doubleValue("major");
        this.minor = request.doubleValue("minor");
        this.angle = request.doubleValue("angle");
        this.circ = request.doubleValue("circ");
        this.feret = request.doubleValue("feret");
        this.feretX = request.doubleValue("feretX");
        this.feretY = request.doubleValue("feretY");
        this.feretAngle = request.doubleValue("feretAngle");
        this.minFeret = request.doubleValue("minFeret");
        this.aR = request.doubleValue("aR");
        this.round = request.doubleValue("round");
        this.solidity = request.doubleValue("solidity");
    }

    public Instance instance() {
        Instance instance = new SparseInstance(18);
        instance.put(0, area);
        instance.put(1, perim);
        instance.put(2, bX);
        instance.put(3, bY);
        instance.put(4, width);
        instance.put(5, height);
        instance.put(6, major);
        instance.put(7, minor);
        instance.put(8, angle);
        instance.put(9, circ);
        instance.put(10, feret);
        instance.put(11, feretX);
        instance.put(12, feretY);
        instance.put(13, feretAngle);
        instance.put(14, minFeret);
        instance.put(15, aR);
        instance.put(16, round);
        instance.put(17, solidity);
        return instance;
    }
}
