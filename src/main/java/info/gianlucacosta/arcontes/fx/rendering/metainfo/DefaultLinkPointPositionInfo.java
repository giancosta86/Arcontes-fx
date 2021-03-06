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

import javafx.geometry.Point2D;

/**
 * Basic implementation of LinkPointPositionInfo
 */
public class DefaultLinkPointPositionInfo extends AbstractPointInfo implements LinkPointPositionInfo {

    public DefaultLinkPointPositionInfo(Point2D point) {
        super(point);
    }

    public DefaultLinkPointPositionInfo(double x, double y) {
        super(x, y);
    }

}
