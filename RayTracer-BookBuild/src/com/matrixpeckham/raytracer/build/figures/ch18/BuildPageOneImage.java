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
import com.matrixpeckham.raytracer.geometricobjects.compound.SolidCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Box;
import com.matrixpeckham.raytracer.geometricobjects.primitives.ConcaveSphere;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Plane;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.AmbientOccluder;
import com.matrixpeckham.raytracer.lights.EnvironmentLight;
import com.matrixpeckham.raytracer.lights.JitteredDirectional;
import com.matrixpeckham.raytracer.materials.Emissive;
import com.matrixpeckham.raytracer.materials.Matte;
import com.matrixpeckham.raytracer.materials.Phong;
import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.tracers.Whitted;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class BuildPageOneImage implements BuildWorldFunction{

    @Override
    public void build(World w) {
int numSamples = 256;
	
	w.vp.setHres(600);
	w.vp.setVres(400);
	w.vp.setSamples(numSamples);
	w.vp.setMaxDepth(0);
		
	w.tracer = new Whitted(w);	
		
	AmbientOccluder ambientOccluderPtr = new AmbientOccluder();
	ambientOccluderPtr.setSampler(new MultiJittered(numSamples));
	ambientOccluderPtr.setMinAmount(0.5);
	w.setAmbient(ambientOccluderPtr);

			
	Pinhole pinholePtr = new Pinhole();
	pinholePtr.setEye(100, 45, 100);  
	pinholePtr.setLookat(-10, 40, 0);   
	pinholePtr.setViewDistance(400);   
	pinholePtr.computeUVW(); 
	w.setCamera(pinholePtr);
		
	JitteredDirectional lightPtr = new JitteredDirectional();
	lightPtr.setDirection(150, 50, -50);   
	lightPtr.setColor(1.0, 0.25, 0.0);  // orange
	lightPtr.setJitterAmount(0.2);
	lightPtr.scaleRadiance(4.0);       
	lightPtr.setShadows(true);
	w.addLight(lightPtr);
	
	Emissive emissivePtr = new Emissive();
	emissivePtr.setCe(1.0, 1.0, 0.5); 	// lemon
	emissivePtr.scaleRadiance(0.9);
	
	ConcaveSphere hemispherePtr = new ConcaveSphere();
	hemispherePtr.setRadius(1000000.0);
	hemispherePtr.setMaterial(emissivePtr);
	hemispherePtr.setShadows(false);
	w.addObject(hemispherePtr);
	
	EnvironmentLight environmentLightPtr = new EnvironmentLight();
	environmentLightPtr.setMaterial(emissivePtr);
	environmentLightPtr.setSampler(new MultiJittered(numSamples));
	environmentLightPtr.setShadows(true);
	w.addLight(environmentLightPtr);
	
	
	double ka = 0.2;  // commom ambient reflection coefficient
	
	// large sphere
		
	Matte mattePtr1 = new Matte();			
	mattePtr1.setKa(ka); 
	mattePtr1.setKd(0.60);
	mattePtr1.setCd(0.75);
	
	Sphere spherePtr1 = new Sphere(new Point3D(38, 20, -24), 20);
	spherePtr1.setMaterial(mattePtr1);
	w.addObject(spherePtr1);
	
	
	// small sphere
	
	Matte mattePtr2 = new Matte();			
	mattePtr2.setKa(ka); 
	mattePtr2.setKd(0.4);
	mattePtr2.setCd(0.25, 1.0, 0.35);       // green
	
	Sphere spherePtr2 = new Sphere(new Point3D(34, 12, 13), 12);
	spherePtr2.setMaterial(mattePtr2);
	w.addObject(spherePtr2);
	
	
	// medium sphere
	
	Matte mattePtr3 = new Matte();			
	mattePtr3.setKa(ka); 
	mattePtr3.setKd(0.5);
	mattePtr3.setCd(0.75);
	
	Sphere spherePtr3 = new Sphere(new Point3D(-7, 15, 42), 16);
	spherePtr3.setMaterial(mattePtr3);
	w.addObject(spherePtr3);

	
	// cylinder
	
	Phong phongPtr = new Phong();			
	phongPtr.setKa(ka); 
	phongPtr.setKd(0.25);
	phongPtr.setCd(new RGBColor(0.60));
	phongPtr.setKs(0.5);
	phongPtr.setExp(100);
	
	double bottom 	= 0.0;
	double top 		= 85; 
	double radius	= 22;
	SolidCylinder cylinderPtr = new SolidCylinder(bottom, top, radius);
	cylinderPtr.setMaterial(phongPtr);
	w.addObject(cylinderPtr);

	
	// box
	
	Matte mattePtr4 = new Matte();			
	mattePtr4.setKa(ka); 
	mattePtr4.setKd(0.5);
	mattePtr4.setCd(0.95);
	
	Box boxPtr = new Box(new Point3D(-35, 0, -110), new Point3D(-25, 60, 65));
	boxPtr.setMaterial(mattePtr4);
	w.addObject(boxPtr);
	

	// ground plane 
		
	Matte mattePtr5 = new Matte();			
	mattePtr5.setKa(0.15); 
	mattePtr5.setKd(0.5);	
	mattePtr5.setCd(0.7);    
	
	Plane planePtr = new Plane(new Point3D(0, 0.01, 0), new Normal(0, 1, 0));
	planePtr.setMaterial(mattePtr5);
	w.addObject(planePtr);    }
    
}
