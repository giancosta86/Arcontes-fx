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

import info.gianlucacosta.arcontes.fx.rendering.LinkPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Basic implementation of LinkInternalPointsInfo
 */
public class DefaultLinkInternalPointsInfo implements LinkInternalPointsInfo {

    private final List<LinkPoint> internalPoints;

    public DefaultLinkInternalPointsInfo() {
        this.internalPoints = new ArrayList<>();
    }

    public DefaultLinkInternalPointsInfo(List<LinkPoint> internalPoints) {
        this.internalPoints = new ArrayList<>(internalPoints);
    }

    @Override
    public List<LinkPoint> getInternalPoints() {
        return Collections.unmodifiableList(internalPoints);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkInternalPointsInfo)) {
            return false;
        }

        LinkInternalPointsInfo other = (LinkInternalPointsInfo) obj;

        return Objects.equals(internalPoints, other.getInternalPoints());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
