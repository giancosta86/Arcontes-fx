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

import info.gianlucacosta.arcontes.fx.rendering.metainfo.*;

import java.util.Objects;

/**
 * Implementation of LinkStyleInfo
 */
public class DefaultLinkStyleInfo implements LinkStyleInfo {

    private final DefaultLinkRenderingInfo nonSelectedRenderingInfo;
    private final DefaultLinkLabelRenderingInfo nonSelectedLabelRenderingInfo;
    private final DefaultLinkLabelConnectorRenderingInfo nonSelectedLabelConnectorRenderingInfo;
    private final DefaultLinkTailRenderingInfo nonSelectedTailRenderingInfo;
    private final DefaultLinkRenderingInfo selectedRenderingInfo;
    private final DefaultLinkLabelRenderingInfo selectedLabelRenderingInfo;
    private final DefaultLinkLabelConnectorRenderingInfo selectedLabelConnectorRenderingInfo;
    private final DefaultLinkTailRenderingInfo selectedTailRenderingInfo;

    public DefaultLinkStyleInfo() {
        this.nonSelectedRenderingInfo = new DefaultLinkRenderingInfo();
        this.nonSelectedLabelRenderingInfo = new DefaultLinkLabelRenderingInfo();
        this.nonSelectedLabelConnectorRenderingInfo = new DefaultLinkLabelConnectorRenderingInfo();
        this.nonSelectedTailRenderingInfo = new DefaultLinkTailRenderingInfo();

        this.selectedRenderingInfo = new DefaultLinkRenderingInfo();
        this.selectedLabelRenderingInfo = new DefaultLinkLabelRenderingInfo();
        this.selectedLabelConnectorRenderingInfo = new DefaultLinkLabelConnectorRenderingInfo();
        this.selectedTailRenderingInfo = new DefaultLinkTailRenderingInfo();
    }

    public DefaultLinkStyleInfo(LinkStyleInfo source) {
        this.nonSelectedRenderingInfo = new DefaultLinkRenderingInfo(source.getNonSelectedRenderingInfo());
        this.nonSelectedLabelRenderingInfo = new DefaultLinkLabelRenderingInfo(source.getNonSelectedLabelRenderingInfo());
        this.nonSelectedLabelConnectorRenderingInfo = new DefaultLinkLabelConnectorRenderingInfo(source.getNonSelectedLabelConnectorRenderingInfo());
        this.nonSelectedTailRenderingInfo = new DefaultLinkTailRenderingInfo(source.getNonSelectedTailRenderingInfo());

        this.selectedRenderingInfo = new DefaultLinkRenderingInfo(source.getSelectedRenderingInfo());
        this.selectedLabelRenderingInfo = new DefaultLinkLabelRenderingInfo(source.getSelectedLabelRenderingInfo());
        this.selectedLabelConnectorRenderingInfo = new DefaultLinkLabelConnectorRenderingInfo(source.getSelectedLabelConnectorRenderingInfo());
        this.selectedTailRenderingInfo = new DefaultLinkTailRenderingInfo(source.getSelectedTailRenderingInfo());
    }

    public DefaultLinkStyleInfo(LinkRenderingInfo nonSelectedRenderingInfo, LinkLabelRenderingInfo nonSelectedLabelRenderingInfo, LinkLabelConnectorRenderingInfo nonSelectedLabelConnectorRenderingInfo, LinkTailRenderingInfo nonSelectedTailRenderingInfo, LinkRenderingInfo selectedRenderingInfo, LinkLabelRenderingInfo selectedLabelRenderingInfo, LinkLabelConnectorRenderingInfo selectedLabelConnectorRenderingInfo, LinkTailRenderingInfo selectedTailRenderingInfo) {
        this.nonSelectedRenderingInfo = new DefaultLinkRenderingInfo(nonSelectedRenderingInfo);
        this.nonSelectedLabelRenderingInfo = new DefaultLinkLabelRenderingInfo(nonSelectedLabelRenderingInfo);
        this.nonSelectedLabelConnectorRenderingInfo = new DefaultLinkLabelConnectorRenderingInfo(nonSelectedLabelConnectorRenderingInfo);
        this.nonSelectedTailRenderingInfo = new DefaultLinkTailRenderingInfo(nonSelectedTailRenderingInfo);

        this.selectedRenderingInfo = new DefaultLinkRenderingInfo(selectedRenderingInfo);
        this.selectedLabelRenderingInfo = new DefaultLinkLabelRenderingInfo(selectedLabelRenderingInfo);
        this.selectedLabelConnectorRenderingInfo = new DefaultLinkLabelConnectorRenderingInfo(selectedLabelConnectorRenderingInfo);
        this.selectedTailRenderingInfo = new DefaultLinkTailRenderingInfo(selectedTailRenderingInfo);
    }

    @Override
    public DefaultLinkRenderingInfo getNonSelectedRenderingInfo() {
        return nonSelectedRenderingInfo;
    }

    @Override
    public DefaultLinkLabelRenderingInfo getNonSelectedLabelRenderingInfo() {
        return nonSelectedLabelRenderingInfo;
    }

    @Override
    public DefaultLinkLabelConnectorRenderingInfo getNonSelectedLabelConnectorRenderingInfo() {
        return nonSelectedLabelConnectorRenderingInfo;
    }

    @Override
    public DefaultLinkTailRenderingInfo getNonSelectedTailRenderingInfo() {
        return nonSelectedTailRenderingInfo;
    }

    @Override
    public DefaultLinkRenderingInfo getSelectedRenderingInfo() {
        return selectedRenderingInfo;
    }

    @Override
    public DefaultLinkLabelRenderingInfo getSelectedLabelRenderingInfo() {
        return selectedLabelRenderingInfo;
    }

    @Override
    public DefaultLinkLabelConnectorRenderingInfo getSelectedLabelConnectorRenderingInfo() {
        return selectedLabelConnectorRenderingInfo;
    }

    @Override
    public DefaultLinkTailRenderingInfo getSelectedTailRenderingInfo() {
        return selectedTailRenderingInfo;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkStyleInfo)) {
            return false;
        }

        LinkStyleInfo other = (LinkStyleInfo) obj;

        return Objects.equals(nonSelectedRenderingInfo, other.getNonSelectedRenderingInfo())
                && Objects.equals(nonSelectedLabelRenderingInfo, other.getNonSelectedLabelRenderingInfo())
                && Objects.equals(nonSelectedLabelConnectorRenderingInfo, other.getNonSelectedLabelConnectorRenderingInfo())
                && Objects.equals(nonSelectedTailRenderingInfo, other.getNonSelectedTailRenderingInfo())
                && Objects.equals(selectedRenderingInfo, other.getSelectedRenderingInfo())
                && Objects.equals(selectedLabelRenderingInfo, other.getSelectedLabelRenderingInfo())
                && Objects.equals(selectedLabelConnectorRenderingInfo, other.getSelectedLabelConnectorRenderingInfo())
                && Objects.equals(selectedTailRenderingInfo, other.getSelectedTailRenderingInfo());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
