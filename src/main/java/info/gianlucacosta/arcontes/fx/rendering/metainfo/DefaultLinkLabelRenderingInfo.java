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
 * Basic implementation of LinkLabelRenderingInfo
 */
public class DefaultLinkLabelRenderingInfo implements LinkLabelRenderingInfo {

    private SerializableFont font;
    private SerializableColor fontColor;

    public DefaultLinkLabelRenderingInfo() {
        font = new SerializableFont(new Font("Arial", 14));
        fontColor = new SerializableColor(Color.BLACK);
    }

    public DefaultLinkLabelRenderingInfo(LinkLabelRenderingInfo source) {
        font = new SerializableFont(source.getFont());
        fontColor = new SerializableColor(source.getFontColor());
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
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkLabelRenderingInfo)) {
            return false;
        }

        LinkLabelRenderingInfo other = (LinkLabelRenderingInfo) obj;

        return Objects.equals(font, other.getFont())
                && Objects.equals(fontColor, other.getFontColor());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
