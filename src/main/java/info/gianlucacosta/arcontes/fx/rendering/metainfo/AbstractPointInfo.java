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

import info.gianlucacosta.helios.fx.serialization.SerializablePoint2D;
import javafx.geometry.Point2D;

import java.util.Objects;

/**
 * Base class for metainfo classes describing a point
 */
public abstract class AbstractPointInfo implements PointInfo {

    private final SerializablePoint2D point;

    public AbstractPointInfo(Point2D point) {
        this.point = new SerializablePoint2D(point);
    }

    public AbstractPointInfo(double x, double y) {
        this(new Point2D(x, y));
    }

    @Override
    public Point2D getPoint() {
        return point.getFxPoint();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PointInfo)) {
            return false;
        }

        PointInfo other = (PointInfo) obj;

        return Objects.equals(point, other.getPoint());
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }

}
