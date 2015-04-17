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

import java.util.Objects;

/**
 * Implementation of GraphStyle
 */
public class DefaultGraphStyle implements GraphStyle {

    private final DefaultVertexStyleInfo vertexStyleInfo;
    private final DefaultLinkStyleInfo linkStyleInfo;

    public DefaultGraphStyle(GraphStyle source) {
        this.vertexStyleInfo = new DefaultVertexStyleInfo(source.getVertexStyleInfo());
        this.linkStyleInfo = new DefaultLinkStyleInfo(source.getLinkStyleInfo());
    }

    public DefaultGraphStyle(VertexStyleInfo vertexStyleInfo, LinkStyleInfo linkStyleInfo) {
        this.vertexStyleInfo = new DefaultVertexStyleInfo(vertexStyleInfo);
        this.linkStyleInfo = new DefaultLinkStyleInfo(linkStyleInfo);
    }

    @Override
    public VertexStyleInfo getVertexStyleInfo() {
        return vertexStyleInfo;
    }

    @Override
    public LinkStyleInfo getLinkStyleInfo() {
        return linkStyleInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphStyle)) {
            return false;
        }

        GraphStyle other = (GraphStyle) obj;

        return Objects.equals(vertexStyleInfo, other.getVertexStyleInfo())
                && Objects.equals(linkStyleInfo, other.getLinkStyleInfo());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
