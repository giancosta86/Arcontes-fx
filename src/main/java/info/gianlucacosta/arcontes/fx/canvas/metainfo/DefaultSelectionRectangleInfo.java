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

package info.gianlucacosta.arcontes.fx.canvas.metainfo;

import info.gianlucacosta.helios.fx.serialization.SerializableColor;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * Implementation of SelectionRectangleInfo
 */
public class DefaultSelectionRectangleInfo implements SelectionRectangleInfo {

    private SerializableColor backgroundColor;
    private SerializableColor borderColor;
    private double borderSize;
    private double opacity;

    public DefaultSelectionRectangleInfo() {
        this.backgroundColor = new SerializableColor(Color.LIGHTPINK);
        this.borderColor = new SerializableColor(Color.VIOLET);
        this.borderSize = 1;
        this.opacity = 0.45;
    }

    public DefaultSelectionRectangleInfo(SelectionRectangleInfo source) {
        this.backgroundColor = new SerializableColor(source.getBackgroundColor());
        this.borderColor = new SerializableColor(source.getBorderColor());
        this.borderSize = source.getBorderSize();
        this.opacity = source.getOpacity();
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor.getFxColor();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = new SerializableColor(backgroundColor);
    }

    @Override
    public Color getBorderColor() {
        return borderColor.getFxColor();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = new SerializableColor(borderColor);
    }

    @Override
    public double getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(double borderSize) {
        this.borderSize = borderSize;
    }

    @Override
    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SelectionRectangleInfo)) {
            return false;
        }

        SelectionRectangleInfo other = (SelectionRectangleInfo) obj;

        return Objects.equals(backgroundColor, other.getBackgroundColor())
                && Objects.equals(borderColor, other.getBorderColor())
                && (borderSize == other.getBorderSize())
                && (opacity == other.getOpacity());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
