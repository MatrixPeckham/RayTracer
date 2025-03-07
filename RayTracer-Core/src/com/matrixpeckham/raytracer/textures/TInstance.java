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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA02111-1307, USA.
 */
package com.matrixpeckham.raytracer.textures;

import com.matrixpeckham.raytracer.util.Matrix;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Texture instance class, transforms and differs to another texture. As with
 * the instance class for geometry holds the inverse of the transforms to apply
 * and applies it to the hit point before differing to the inner texture.
 *
 * @author William Matrix Peckham
 */
public class TInstance implements Texture {

    /**
     * Texture that we are transforming.
     */
    private Texture texture = null;

    /**
     * inverse matrix of the current transform.
     */
    private Matrix invMatrix = new Matrix();

    /**
     * empty texture
     */
    public TInstance() {
        super();
    }

    /**
     * sets the inner texture
     *
     * @param obj
     */
    public TInstance(Texture obj) {
        texture = obj;
    }

    /**
     * copy constructor
     *
     * @param i
     */
    public TInstance(TInstance i) {
        invMatrix.setTo(i.invMatrix);
        if (i.texture != null) {
            texture = i.texture.cloneTexture();
        }
    }

    /**
     * sets the inner texture
     *
     * @param obj
     */
    public void setTexture(Texture obj) {
        texture = obj;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new TInstance(this);
    }

    /**
     * uniform scale
     *
     * @param s
     */
    public void scale(double s) {
        scale(new Vector3D(s, s, s));
    }

    /**
     * non-uniform scale
     *
     * @param s
     */
    public void scale(Vector3D s) {

        Matrix invScalingMatrix = new Matrix();// temporary inverse scaling matrix

        invScalingMatrix.m[0][0] = 1.0 / s.x;
        invScalingMatrix.m[1][1] = 1.0 / s.y;
        invScalingMatrix.m[2][2] = 1.0 / s.z;

        invMatrix = invMatrix.mul(invScalingMatrix);

    }

    /**
     * non-uniform scale
     *
     * @param a
     * @param b
     * @param c
     */
    public void scale(double a, double b, double c) {

        Matrix invScalingMatrix = new Matrix();// temporary inverse scaling matrix

        invScalingMatrix.m[0][0] = 1.0 / a;
        invScalingMatrix.m[1][1] = 1.0 / b;
        invScalingMatrix.m[2][2] = 1.0 / c;

        invMatrix = invMatrix.mul(invScalingMatrix);

    }

    /**
     * translate
     *
     * @param trans
     */
    public void translate(Vector3D trans) {

        Matrix inv_translation_matrix = new Matrix();// temporary inverse translation matrix

        inv_translation_matrix.m[0][3] = -trans.x;
        inv_translation_matrix.m[1][3] = -trans.y;
        inv_translation_matrix.m[2][3] = -trans.z;

        invMatrix = invMatrix.mul(inv_translation_matrix);

    }

    /**
     * Translate
     *
     * @param dx
     * @param dy
     * @param dz
     */
    public void translate(double dx, double dy, double dz) {

        Matrix inv_translation_matrix = new Matrix();// temporary inverse translation matrix

        inv_translation_matrix.m[0][3] = -dx;
        inv_translation_matrix.m[1][3] = -dy;
        inv_translation_matrix.m[2][3] = -dz;

        invMatrix = invMatrix.mul(inv_translation_matrix);

    }

    /**
     * rotate x
     *
     * @param theta
     */
    public void rotateX(double theta) {

        double sin_theta = Math.sin(theta * Utility.PI_ON_180);
        double cos_theta = Math.cos(theta * Utility.PI_ON_180);

        Matrix inv_x_rotation_matrix = new Matrix();// temporary inverse rotation matrix about x axis

        inv_x_rotation_matrix.m[1][1] = cos_theta;
        inv_x_rotation_matrix.m[1][2] = sin_theta;
        inv_x_rotation_matrix.m[2][1] = -sin_theta;
        inv_x_rotation_matrix.m[2][2] = cos_theta;

        invMatrix = invMatrix.mul(inv_x_rotation_matrix);
    }

    /**
     * rotate y
     *
     * @param theta
     */
    public void rotateY(double theta) {

        double sin_theta = Math.sin(theta * Utility.PI / 180.0);
        double cos_theta = Math.cos(theta * Utility.PI / 180.0);

        Matrix inv_y_rotation_matrix = new Matrix();// temporary inverse rotation matrix about y axis

        inv_y_rotation_matrix.m[0][0] = cos_theta;
        inv_y_rotation_matrix.m[0][2] = -sin_theta;
        inv_y_rotation_matrix.m[2][0] = sin_theta;
        inv_y_rotation_matrix.m[2][2] = cos_theta;

        invMatrix = invMatrix.mul(inv_y_rotation_matrix);

    }

    /**
     * rotate z
     *
     * @param theta
     */
    public void rotateZ(double theta) {
        double sin_theta = Math.sin(theta * Utility.PI / 180.0);
        double cos_theta = Math.cos(theta * Utility.PI / 180.0);

        Matrix inv_z_rotation_matrix = new Matrix();// temporary inverse rotation matrix about y axis

        inv_z_rotation_matrix.m[0][0] = cos_theta;
        inv_z_rotation_matrix.m[0][1] = sin_theta;
        inv_z_rotation_matrix.m[1][0] = -sin_theta;
        inv_z_rotation_matrix.m[1][1] = cos_theta;

        invMatrix = invMatrix.mul(inv_z_rotation_matrix);

    }

    /**
     * shear
     *
     * @param s
     */
    public void shear(Matrix s) {

        Matrix inverse_shearing_matrix = new Matrix();// inverse shear matrix

        // discriminant
        double d = 1.0 - s.m[1][0] * s.m[0][1] - s.m[2][0] * s.m[0][2]
                - s.m[2][1] * s.m[1][2]
                + s.m[1][0] * s.m[2][1] * s.m[0][2] + s.m[2][0] * s.m[0][1]
                * s.m[2][1];

        // diagonals
        inverse_shearing_matrix.m[0][0] = 1.0 - s.m[2][1] * s.m[1][2];
        inverse_shearing_matrix.m[1][1] = 1.0 - s.m[2][0] * s.m[0][2];
        inverse_shearing_matrix.m[2][2] = 1.0 - s.m[1][0] * s.m[0][1];
        inverse_shearing_matrix.m[3][3] = d;

        // first row
        inverse_shearing_matrix.m[0][1] = -s.m[1][0] + s.m[2][0] * s.m[1][2];
        inverse_shearing_matrix.m[0][2] = -s.m[2][0] + s.m[1][0] * s.m[2][1];

        // second row
        inverse_shearing_matrix.m[1][0] = -s.m[0][1] + s.m[2][1] * s.m[0][2];
        inverse_shearing_matrix.m[1][2] = -s.m[2][1] + s.m[2][0] * s.m[0][1];

        // third row
        inverse_shearing_matrix.m[2][0] = -s.m[0][2] + s.m[0][1] * s.m[1][2];
        inverse_shearing_matrix.m[2][1] = -s.m[1][2] + s.m[1][0] * s.m[0][2];

        // divide by discriminant
        inverse_shearing_matrix = inverse_shearing_matrix.div(d);

        invMatrix = invMatrix.mul(inverse_shearing_matrix);

    }

    /**
     * Sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //copy shadrec
        ShadeRec local = new ShadeRec(sr);
        //transform local hit point by inverse matrix
        local.localHitPosition.setTo(Point3D.mul(invMatrix,
                local.localHitPosition));
        //forward translated shaderec to inner texture
        return texture.getColor(local);
    }

    private static final Logger LOG
            = Logger.getLogger(TInstance.class.getName());

}
