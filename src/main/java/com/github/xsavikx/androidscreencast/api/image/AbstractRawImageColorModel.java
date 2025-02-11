/*
 * Copyright 2020 Yurii Serhiichuk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.xsavikx.androidscreencast.api.image;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;

/**
 * Abstract color model for adb RawImage
 */
abstract class AbstractRawImageColorModel extends ColorModel {

    private static final int PIXEL_BITS = 32;
    private static final boolean HAS_ALPHA = true;
    private static final boolean IS_ALPHA_PRE_MULTIPLIED = false;
    private static final int[] BITS = {
            8, 8, 8, 8,
    };

    AbstractRawImageColorModel() {
        super(PIXEL_BITS, BITS, ColorSpace.getInstance(ColorSpace.CS_sRGB),
                HAS_ALPHA, IS_ALPHA_PRE_MULTIPLIED, Transparency.TRANSLUCENT,
                DataBuffer.TYPE_BYTE);
    }

    protected abstract int getPixel(byte[] data);

    protected int getPixel(Object inData) {
        return getPixel((byte[]) inData);
    }

    protected int getMask(int length) {
        int res = 0;
        for (int i = 0; i < length; i++) {
            res = (res << 1) + 1;
        }
        return res;
    }

    @Override
    public boolean isCompatibleRaster(Raster raster) {
        return true;
    }

    @Override
    public int getAlpha(int pixel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBlue(int pixel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getGreen(int pixel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRed(int pixel) {
        throw new UnsupportedOperationException();
    }
}
