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
package com.matrixpeckham.raytracer.tracers;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.World;
import java.util.logging.Logger;

/**
 * Tracer for casting rays into the world taking depth into account. Basically
 * the same as the whitted.
 *
 * @author William Matrix Peckham
 */
public class RayCast extends Tracer {

    /**
     * default constructor.
     */
    public RayCast() {
        super();
    }

    /**
     * World setting constructor.
     *
     * @param world
     */
    public RayCast(World world) {
        super(world);
    }

    /**
     * Trace a ray, without depth.
     *
     * @param ray
     * @return
     */
    @Override
    public RGBColor traceRay(Ray ray) {
        return traceRay(ray, 0);
    }

    /**
     * Trace a ray, stopping at world.vp.maxDepth.
     *
     * @param ray
     * @param depth
     * @return
     */
    @Override
    public RGBColor traceRay(Ray ray, int depth) {
        if (depth > world.vp.maxDepth) {//depth bailout
            return Utility.BLACK;
        }
        //gets closest intersection
        ShadeRec sr = new ShadeRec(world.hitObjects(ray));
        if (sr.hitAnObject) {//book keep and shade
            sr.ray.setTo(ray);
            sr.depth = depth;
            return sr.material.shade(sr);
        } else {
            return world.backgroundColor;
        }
    }

    private static final Logger LOG = Logger.getLogger(RayCast.class.getName());

}
