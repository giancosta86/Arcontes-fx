/*§
  ===========================================================================
  Arcontes - FX
  ===========================================================================
  Copyright (C) 2013-2015 Gianluca Costa
  ===========================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ===========================================================================
*/

package info.gianlucacosta.arcontes.fx.rendering.metainfo;

import info.gianlucacosta.helios.fx.serialization.SerializableColor;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * Basic implementation of LinkPointRenderingInfo
 */
public class DefaultLinkPointRenderingInfo implements LinkPointRenderingInfo {

    private SerializableColor color;
    private int radiusFactor;
    private double opacity;

    public DefaultLinkPointRenderingInfo() {
        color = new SerializableColor(Color.TRANSPARENT);
        radiusFactor = 2;
        opacity = 1;
    }

    public DefaultLinkPointRenderingInfo(LinkPointRenderingInfo source) {
        color = new SerializableColor(source.getColor());
        radiusFactor = source.getRadiusFactor();
        opacity = source.getOpacity();
    }

    @Override
    public Color getColor() {
        return color.getFxColor();
    }

    public void setColor(Color color) {
        this.color = new SerializableColor(color);
    }

    @Override
    public int getRadiusFactor() {
        return radiusFactor;
    }

    public void setRadius(int radiusFactor) {
        if (radiusFactor < 0) {
            throw new IllegalArgumentException();
        }

        this.radiusFactor = radiusFactor;
    }

    @Override
    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        if (opacity < 0 || opacity > 1) {
            throw new IllegalArgumentException();
        }

        this.opacity = opacity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LinkPointRenderingInfo)) {
            return false;
        }

        LinkPointRenderingInfo other = (LinkPointRenderingInfo) o;

        return (radiusFactor == other.getRadiusFactor())
                && Objects.equals(color, other.getColor())
                && (opacity == other.getOpacity());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
