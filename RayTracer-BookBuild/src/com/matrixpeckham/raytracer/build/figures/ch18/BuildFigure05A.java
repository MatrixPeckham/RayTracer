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
package com.matrixpeckham.raytracer.build.figures.ch18;

import com.matrixpeckham.raytracer.cameras.Pinhole;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Disk;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.lights.AreaLight;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildFigure05A implements BuildWorldFunction {

    @Override
    public void build(World w) {
        int numSamples = 100;

        Sampler samplerPtr = new MultiJittered(numSamples);

        w.vp.setHres(600);
        w.vp.setVres(600);
        w.vp.setMaxDepth(0);
        w.vp.setSampler(samplerPtr);

        w.backgroundColor = new RGBColor(0.5);

        w.tracer = new Whitted(w);

        Pinhole camera = new Pinhole();
        camera.setEye(-20, 10, 20);
        camera.setLookat(0, 2, 0);
        camera.setViewDistance(1080);
        camera.computeUVW();
        w.setCamera(camera);

        Emissive emissivePtr = new Emissive();
        emissivePtr.scaleRadiance(40.0);
        emissivePtr.setCe(Utility.WHITE);

        // disk for the disk light
        Point3D center = new Point3D(0.0, 7.0, -7.0);
        double width = 4.0;
        double radius = 0.56 * width;
        Normal normal = new Normal(0, 0, 1);

        Disk diskPtr = new Disk(center, normal, radius);
        diskPtr.setMaterial(emissivePtr);
        diskPtr.setSampler(samplerPtr);
        diskPtr.setShadows(false);
        diskPtr.computeUVW();
        w.addObject(diskPtr);

        AreaLight areaLightPtr = new AreaLight();
        areaLightPtr.setObject(diskPtr);
        areaLightPtr.setShadows(true);
        w.addLight(areaLightPtr);

	// Four axis aligned boxes
        double boxWidth = 1.0; 		// x dimension
        double boxDepth = 1.0; 		// z dimension
        double boxHeight = 4.5; 		// y dimension
        double gap = 3.0;

        Matte mattePtr1 = new Matte();
        mattePtr1.setKa(0.25);
        mattePtr1.setKd(0.75);
        mattePtr1.setCd(0.4, 0.7, 0.4);     // green

        Box boxPtr0 = new Box(new Point3D(-1.5 * gap - 2.0 * boxWidth, 0.0, -0.5
                * boxDepth),
                new Point3D(-1.5 * gap - boxWidth, boxHeight, 0.5 * boxDepth));
        boxPtr0.setMaterial(mattePtr1);
        w.addObject(boxPtr0);

        Box boxPtr1 = new Box(new Point3D(-0.5 * gap - boxWidth, 0.0, -0.5
                * boxDepth),
                new Point3D(-0.5 * gap, boxHeight, 0.5 * boxDepth));
        boxPtr1.setMaterial(mattePtr1);
        w.addObject(boxPtr1);

        Box boxPtr2 = new Box(new Point3D(0.5 * gap, 0.0, -0.5 * boxDepth),
                new Point3D(0.5 * gap + boxWidth, boxHeight, 0.5 * boxDepth));
        boxPtr2.setMaterial(mattePtr1);
        w.addObject(boxPtr2);

        Box boxPtr3 = new Box(new Point3D(1.5 * gap + boxWidth, 0.0, -0.5
                * boxDepth),
                new Point3D(1.5 * gap + 2.0 * boxWidth, boxHeight, 0.5
                        * boxDepth));
        boxPtr3.setMaterial(mattePtr1);
        w.addObject(boxPtr3);

	// ground plane
        Matte mattePtr2 = new Matte();
        mattePtr2.setKa(0.1);
        mattePtr2.setKd(0.90);
        mattePtr2.setCd(Utility.WHITE);

        Plane planePtr = new Plane(new Point3D(0.0), new Normal(0, 1, 0));
        planePtr.setMaterial(mattePtr2);
        w.addObject(planePtr);
    }

}
