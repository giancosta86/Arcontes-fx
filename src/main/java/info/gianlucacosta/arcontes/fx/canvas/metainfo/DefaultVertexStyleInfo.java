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

import info.gianlucacosta.arcontes.fx.rendering.metainfo.DefaultVertexRenderingInfo;
import info.gianlucacosta.arcontes.fx.rendering.metainfo.VertexRenderingInfo;

import java.util.Objects;

/**
 * Implementation of VertexStyleInfo
 */
public class DefaultVertexStyleInfo implements VertexStyleInfo {

    private final DefaultVertexRenderingInfo nonSelectedRenderingInfo;
    private final DefaultVertexRenderingInfo selectedRenderingInfo;

    public DefaultVertexStyleInfo() {
        this.nonSelectedRenderingInfo = new DefaultVertexRenderingInfo();
        this.selectedRenderingInfo = new DefaultVertexRenderingInfo();
    }

    public DefaultVertexStyleInfo(VertexStyleInfo source) {
        this.nonSelectedRenderingInfo = new DefaultVertexRenderingInfo(source.getNonSelectedRenderingInfo());
        this.selectedRenderingInfo = new DefaultVertexRenderingInfo(source.getSelectedRenderingInfo());
    }

    public DefaultVertexStyleInfo(VertexRenderingInfo nonSelectedRenderingInfo, VertexRenderingInfo selectedRenderingInfo) {
        this.nonSelectedRenderingInfo = new DefaultVertexRenderingInfo(nonSelectedRenderingInfo);
        this.selectedRenderingInfo = new DefaultVertexRenderingInfo(selectedRenderingInfo);
    }

    @Override
    public DefaultVertexRenderingInfo getNonSelectedRenderingInfo() {
        return nonSelectedRenderingInfo;
    }

    @Override
    public DefaultVertexRenderingInfo getSelectedRenderingInfo() {
        return selectedRenderingInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VertexStyleInfo)) {
            return false;
        }

        VertexStyleInfo other = (VertexStyleInfo) obj;

        return Objects.equals(nonSelectedRenderingInfo, other.getNonSelectedRenderingInfo())
                && Objects.equals(selectedRenderingInfo, other.getSelectedRenderingInfo());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
