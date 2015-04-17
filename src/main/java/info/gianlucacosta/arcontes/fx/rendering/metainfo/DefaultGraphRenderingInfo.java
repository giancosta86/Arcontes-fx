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
 * Basic implementation of GraphRenderingInfo
 */
public class DefaultGraphRenderingInfo implements GraphRenderingInfo {

    private SerializableColor backgroundColor;
    private double width;
    private double height;

    public DefaultGraphRenderingInfo() {
        backgroundColor = new SerializableColor(Color.WHITE);

        this.width = 800;
        this.height = 550;
    }

    public DefaultGraphRenderingInfo(GraphRenderingInfo source) {
        backgroundColor = new SerializableColor(source.getBackgroundColor());
        width = source.getWidth();
        height = source.getHeight();
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor.getFxColor();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = new SerializableColor(backgroundColor);
    }

    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        if (width < 0) {
            throw new IllegalArgumentException();
        }

        this.width = width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        if (height < 0) {
            throw new IllegalArgumentException();
        }

        this.height = height;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphRenderingInfo)) {
            return false;
        }

        GraphRenderingInfo other = (GraphRenderingInfo) obj;
        return Objects.equals(backgroundColor, other.getBackgroundColor())
                && (width == other.getWidth())
                && (height == other.getHeight());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
