/*
 * Copyright (c) 2013-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Project BUBO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bubo.maps.d3.grid.impl;

/**
 * A 3D convolution kernel for doubles.
 *
 * @author Peter Abeles
 */
public class Kernel3D_F64 {
	public double data[];
	public int radius;
	public int width;

	public Kernel3D_F64(int radius) {
		this.radius = radius;
		this.width = radius*2+1;

		data = new double[width*width*width];
	}

	/**
	 * Returns the element at coordinate (i,j,k) in the kernel
	 */
	public double get( int i , int j , int k ) {
		return data[ i*width*width + j*width + k];
	}

	/**
	 * The radius of the kernel.
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * Total number of elements in the kernel.
	 */
	public int getTotalElements() {
		return data.length;
	}

	/**
	 * Creates a kernel with a symmetric normal distribution
	 * @param sigma standard deviation of the distribution
	 * @param radius Radius in array elements
	 * @return the kernel
	 */
	public static Kernel3D_F64 gaussian( double sigma, int radius ) {

		Kernel3D_F64 ret = new Kernel3D_F64(radius);

		int index = 0;
		int w = radius*2 + 1;
		double total = 0;
		for (int i = 0; i < w; i++) {
			int disti = i-radius;
			for (int j = 0; j < w; j++) {
				int distj = j-radius;
				for (int k = 0; k < w; k++, index++) {
					int distk = k-radius;

					double d = Math.sqrt(disti*disti + distj*distj + distk*distk);
					total += ret.data[index] = Math.exp(-d*d/(2.0*sigma*sigma));
				}
			}
		}

		// the sum should add up to one
		for (int i = 0; i < ret.data.length; i++) {
			ret.data[i] /= total;
		}

		return ret;
	}

	/**
	 * Creates a Gaussian kernel using a sigma computed
	 * @param radius Radius in array elements
	 * @return the kernel
	 */
	public static Kernel3D_F64 gaussian( int radius ) {
		double sigma = (radius* 2.0 + 1.0 ) / 5.0;
		return gaussian(sigma,radius);
	}

	public int getWidth() {
		return width;
	}
}
