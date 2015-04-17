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
 * Basic implementation of LinkRenderingInfo
 */
public class DefaultLinkRenderingInfo implements LinkRenderingInfo {

    private SerializableColor color;
    private double lineSize;
    private double opacity;

    public DefaultLinkRenderingInfo() {
        color = new SerializableColor(Color.MEDIUMSEAGREEN);
        lineSize = 4;
        opacity = 1;
    }

    public DefaultLinkRenderingInfo(LinkRenderingInfo source) {
        color = new SerializableColor(source.getColor());
        lineSize = source.getLineSize();
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
    public double getLineSize() {
        return lineSize;
    }

    public void setLineSize(double lineSize) {
        if (lineSize < 0) {
            throw new IllegalArgumentException();
        }

        this.lineSize = lineSize;
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
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkRenderingInfo)) {
            return false;
        }

        LinkRenderingInfo other = (LinkRenderingInfo) obj;

        return Objects.equals(color, other.getColor())
                && (lineSize == other.getLineSize())
                && (opacity == other.getOpacity());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
