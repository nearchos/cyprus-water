/*
 * Copyright (c) 2018.
 *
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR
 *  "LICENSE"). THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW. ANY USE OF THE WORK OTHER
 *   THAN AS AUTHORIZED UNDER THIS LICENSE OR COPYRIGHT LAW IS PROHIBITED.
 *
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND AGREE TO BE BOUND BY THE TERMS OF THIS
 *  LICENSE. TO THE EXTENT THIS LICENSE MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS
 *   CONTAINED HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 *
 * The complete license is available at https://creativecommons.org/licenses/by/3.0/legalcode
 */

package io.github.nearchos.water.data;

import java.io.Serializable;

public class MeasureUnit implements Serializable {

    private String nameEl;
    private String nameEn;
    private String imageUrl;
    private double ratio; // how many cubic meters in one unit of this

    public MeasureUnit(String nameEl, String nameEn, String imageUrl, double ratio) {
        this.nameEl = nameEl;
        this.nameEn = nameEn;
        this.imageUrl = imageUrl;
        this.ratio = ratio;
    }

    public String getNameEl() {
        return nameEl;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getRatio() {
        return ratio;
    }

    public static final MeasureUnit [] DEFAULT_MEASURE_UNITS = {
            new MeasureUnit("Σταγόνα", "Drop", "/images/units/drop.png", .00000005d),
            new MeasureUnit("Κουταλάκι Τσαγιού", "Tea Spoon", "/images/units/teaspoon.png", .000005d),
            new MeasureUnit("Κουταλάκι Σούπας", "Soup Spoon", "/images/units/soupspoon.png", .000015d),
            new MeasureUnit("Φλυτζάνι", "Cup", "/images/units/coffee-cup.png", .000284d),
            new MeasureUnit("Λίτρο", "Liter", "/images/units/fresh-milk-box.png", 0.001d),
            new MeasureUnit("Κυβικό Μέτρο", "Cubic Meter", "/images/units/cube.png", 1d),
            new MeasureUnit("Ελέφαντας", "Elephant", "/images/units/elephant.png", 6d),
            new MeasureUnit("Κοντέϊνερ 20 Ποδών", "20 Feet Container", "/images/units/container.png", 38.267525764d),
            new MeasureUnit("Πισίνα Ολυμπιακών Διαστάσεων", "Olympic Size Pool", "/images/units/pool.png", 4687.5d),
            new MeasureUnit("Χωρητικότητα Φράγματος Κούρη", "Kouris Dam Capacity", "/images/units/dam.png", 115000000d),
            new MeasureUnit("Κυβικό Χιλιόμετρο", "Cubic Kilometer", "/images/units/cube_km.png",1000000000d)
    };
}