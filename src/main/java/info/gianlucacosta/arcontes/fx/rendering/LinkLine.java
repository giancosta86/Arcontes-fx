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

package info.gianlucacosta.arcontes.fx.rendering;

import info.gianlucacosta.arcontes.fx.rendering.metainfo.LinkRenderingInfo;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * Visually represents the segment of a link or of a link endpoint (for example,
 * the segments making up an arrow)
 */
class LinkLine extends Line {

    public LinkLine() {
        setStrokeLineCap(StrokeLineCap.ROUND);
    }

    public void applyRenderingInfo(LinkRenderingInfo linkRenderingInfo) {
        setOpacity(linkRenderingInfo.getOpacity());
        setStroke(linkRenderingInfo.getColor());
        setStrokeWidth(linkRenderingInfo.getLineSize());
    }

}
