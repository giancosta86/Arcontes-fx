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
import info.gianlucacosta.helios.fx.serialization.SerializableFont;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

/**
 * Basic implementation of VertexRenderingInfo
 */
public class DefaultVertexRenderingInfo implements VertexRenderingInfo {

    private SerializableColor backgroundColor;
    private SerializableColor borderColor;
    private double borderSize;
    private SerializableFont font;
    private SerializableColor fontColor;
    private double padding;

    public DefaultVertexRenderingInfo() {
        backgroundColor = new SerializableColor(Color.ORANGE);
        borderColor = new SerializableColor(Color.BLACK);
        borderSize = 1;
        font = new SerializableFont(new Font("Arial", 14));
        fontColor = new SerializableColor(Color.BLACK);
        padding = 8;
    }

    public DefaultVertexRenderingInfo(VertexRenderingInfo source) {
        backgroundColor = new SerializableColor(source.getBackgroundColor());
        borderColor = new SerializableColor(source.getBorderColor());
        borderSize = source.getBorderSize();
        font = new SerializableFont(source.getFont());
        fontColor = new SerializableColor(source.getFontColor());
        padding = source.getPadding();
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
        if (borderSize < 0) {
            throw new IllegalArgumentException();
        }

        this.borderSize = borderSize;
    }

    @Override
    public Font getFont() {
        return font.getFxFont();
    }

    public void setFont(Font font) {
        this.font = new SerializableFont(font);
    }

    @Override
    public Color getFontColor() {
        return fontColor.getFxColor();
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = new SerializableColor(fontColor);
    }

    @Override
    public double getPadding() {
        return padding;
    }

    public void setPadding(double padding) {
        if (padding < 0) {
            throw new IllegalArgumentException();
        }

        this.padding = padding;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VertexRenderingInfo)) {
            return false;
        }

        VertexRenderingInfo other = (VertexRenderingInfo) obj;
        return Objects.equals(backgroundColor, other.getBackgroundColor())
                && Objects.equals(borderColor, other.getBorderColor())
                && (borderSize == other.getBorderSize())
                && (Objects.equals(fontColor, other.getFontColor()))
                && Objects.equals(font, other.getFont())
                && (padding == other.getPadding());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
