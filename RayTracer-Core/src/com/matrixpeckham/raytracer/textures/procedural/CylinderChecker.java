/*
 * Copyright (C) 2015 William Matrix Peckham
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.textures.procedural;

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.logging.Logger;

/**
 * Checker for a generic cylinder
 *
 * @author William Matrix Peckham
 */
public class CylinderChecker implements Texture {

    /**
     * number horizonal checkers
     */
    private int numHorizontalCheckers = 20;

    /**
     * number vertical checkers
     */
    private int numVerticalCheckers = 10;

    /**
     * horizontal line width
     */
    private double horizontalLineWidth = 0;

    /**
     * vertical line width
     */
    private double verticalLineWidth = 0;

    /**
     * first color
     */
    private final RGBColor color1 = new RGBColor(1);

    /**
     * second color
     */
    private final RGBColor color2 = new RGBColor(0.5);

    /**
     * line color
     */
    private final RGBColor lineColor = new RGBColor(0);

    /**
     * default gray and white checkers
     */
    public CylinderChecker() {
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public CylinderChecker(CylinderChecker c) {
        numHorizontalCheckers = c.numHorizontalCheckers;
        numVerticalCheckers = c.numVerticalCheckers;
        horizontalLineWidth = c.horizontalLineWidth;
        verticalLineWidth = c.verticalLineWidth;
        color1.setTo(c.color1);
        color2.setTo(c.color2);
        lineColor.setTo(c.lineColor);
    }

    /**
     * setter
     *
     * @param numHorizontalCheckers
     */
    public void setNumHorizontalCheckers(int numHorizontalCheckers) {
        this.numHorizontalCheckers = numHorizontalCheckers;
    }

    /**
     * setter
     *
     * @param numVerticalCheckers
     */
    public void setNumVerticalCheckers(int numVerticalCheckers) {
        this.numVerticalCheckers = numVerticalCheckers;
    }

    /**
     * setter
     *
     * @param horizontalLineWidth
     */
    public void setHorizontalLineWidth(double horizontalLineWidth) {
        this.horizontalLineWidth = horizontalLineWidth;
    }

    /**
     * setter
     *
     * @param verticalLineWidth
     */
    public void setVerticalLineWidth(double verticalLineWidth) {
        this.verticalLineWidth = verticalLineWidth;
    }

    /**
     * setter
     *
     * @param color1
     */
    public void setColor1(RGBColor color1) {
        this.color1.setTo(color1);
    }

    /**
     * setter
     *
     * @param color2
     */
    public void setColor2(RGBColor color2) {
        this.color2.setTo(color2);
    }

    /**
     * setter
     *
     * @param lineColor
     */
    public void setLineColor(RGBColor lineColor) {
        this.lineColor.setTo(lineColor);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new CylinderChecker(this);
    }

    /**
     * sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //hit point
        double x = sr.localHitPosition.x;
        double y = sr.localHitPosition.y;
        double z = sr.localHitPosition.z;

        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        //normalize y for generic cylinder size
        double yOverTwo = y / 2;
        double theta = yOverTwo + 0.5;//Math.acos(y);

        //find angle
        double phi = Math.atan2(x, z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }

        //find texture size
        double phiSize = Utility.TWO_PI / numHorizontalCheckers;
        double thetaSize = 1.0 / numVerticalCheckers;//Utility.PI / numVerticalCheckers;

        //calculate checkers and line width
        int iphi = (int) Math.floor(phi / phiSize);
        int itheta = (int) Math.floor(theta / thetaSize);

        double fphi = phi / phiSize - iphi;
        double ftheta = theta / thetaSize - itheta;

        double phiLineWidth = 0.5 * verticalLineWidth;
        double thetaLineWidth = 0.5 * horizontalLineWidth;

        //are we in the line
        boolean inOutline = (fphi < phiLineWidth || fphi > 1.0 - phiLineWidth)
                || (ftheta < thetaLineWidth || ftheta > 1.0 - thetaLineWidth);

        //get color
        if (!((iphi + itheta) % 2 == 0)) {
            if (!inOutline) {
                return color1;
            }
        } else {
            if (!inOutline) {
                return color2;
            }
        }
        return lineColor;
    }

    /**
     * setter
     *
     * @param d
     */
    public void setLineWidth(double d) {
        setHorizontalLineWidth(d);
        setVerticalLineWidth(d);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlat(int i) {
        setNumHorizontalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlong(int i) {
        setNumVerticalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumHorizontal(int i) {
        setNumHorizontalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumVertical(int i) {
        setNumVerticalCheckers(i);
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor1(double d, double d0, double d1) {
        setColor1(new RGBColor(d, d0, d1));
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor2(double d, double d0, double d1) {
        setColor2(new RGBColor(d, d0, d1));
    }

    private static final Logger LOG
            = Logger.getLogger(CylinderChecker.class.getName());

}
