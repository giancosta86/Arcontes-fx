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

import info.gianlucacosta.helios.collections.general.CollectionItems;
import info.gianlucacosta.helios.fx.serialization.SerializableColor;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Basic implementation of LinkLabelConnectorRenderingInfo
 */
public class DefaultLinkLabelConnectorRenderingInfo implements LinkLabelConnectorRenderingInfo {

    private SerializableColor color;
    private double lineSize;
    private double opacity;
    private Collection<Double> lineDashItems;

    public DefaultLinkLabelConnectorRenderingInfo() {
        color = new SerializableColor(Color.RED);
        lineSize = 1;
        opacity = 0;

        lineDashItems = new ArrayList<>();
        lineDashItems.add(5.0);
        lineDashItems.add(5.0);
    }

    public DefaultLinkLabelConnectorRenderingInfo(LinkLabelConnectorRenderingInfo source) {
        color = new SerializableColor(source.getColor());
        lineSize = source.getLineSize();
        opacity = source.getOpacity();
        lineDashItems = new ArrayList<>(source.getLineDashItems());
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
    public Collection<Double> getLineDashItems() {
        return Collections.unmodifiableCollection(lineDashItems);
    }

    public void setLineDashItems(Collection<Double> lineDashItems) {
        this.lineDashItems = lineDashItems;
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
        if (!(obj instanceof LinkLabelConnectorRenderingInfo)) {
            return false;
        }

        LinkLabelConnectorRenderingInfo other = (LinkLabelConnectorRenderingInfo) obj;

        return Objects.equals(color, other.getColor())
                && (lineSize == other.getLineSize())
                && (opacity == other.getOpacity())
                && CollectionItems.equals(getLineDashItems(), other.getLineDashItems());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
