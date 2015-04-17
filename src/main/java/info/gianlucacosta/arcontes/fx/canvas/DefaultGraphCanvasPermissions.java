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

package info.gianlucacosta.arcontes.fx.canvas;

/**
 * Basic implementation of GraphCanvasPermissions
 */
public class DefaultGraphCanvasPermissions implements GraphCanvasPermissions {

    private boolean canCreateVertexes;
    private boolean canCreateLinks;
    private boolean canEditVertexes;
    private boolean canEditLinks;
    private boolean canCreateLinkPoints;
    private boolean canDragVertexes;
    private boolean canDragLinkPoints;
    private boolean canDragLinkLabels;
    private boolean canRemoveSelectedItems;
    private boolean canRemoveLinkPoints;

    public DefaultGraphCanvasPermissions() {
        enableAll();
    }

    @Override
    public void enableAll() {
        canCreateVertexes = true;
        canCreateLinks = true;

        canEditVertexes = true;
        canEditLinks = true;

        canCreateLinkPoints = true;

        canDragVertexes = true;
        canDragLinkPoints = true;
        canDragLinkLabels = true;

        canRemoveSelectedItems = true;
        canRemoveLinkPoints = true;
    }

    @Override
    public void disableAll() {
        canCreateVertexes = false;
        canCreateLinks = false;

        canEditVertexes = false;
        canEditLinks = false;

        canCreateLinkPoints = false;

        canDragVertexes = false;
        canDragLinkPoints = false;
        canDragLinkLabels = false;

        canRemoveSelectedItems = false;
        canRemoveLinkPoints = false;
    }

    @Override
    public boolean isCanCreateVertexes() {
        return canCreateVertexes;
    }

    @Override
    public void setCanCreateVertexes(boolean canCreateVertexes) {
        this.canCreateVertexes = canCreateVertexes;
    }

    @Override
    public boolean isCanCreateLinks() {
        return canCreateLinks;
    }

    @Override
    public void setCanCreateLinks(boolean canCreateLinks) {
        this.canCreateLinks = canCreateLinks;
    }

    @Override
    public boolean isCanEditVertexes() {
        return canEditVertexes;
    }

    @Override
    public void setCanEditVertexes(boolean canEditVertexes) {
        this.canEditVertexes = canEditVertexes;
    }

    @Override
    public boolean isCanEditLinks() {
        return canEditLinks;
    }

    @Override
    public void setCanEditLinks(boolean canEditLinks) {
        this.canEditLinks = canEditLinks;
    }

    @Override
    public boolean isCanCreateLinkPoints() {
        return canCreateLinkPoints;
    }

    @Override
    public void setCanCreateLinkPoints(boolean canCreateLinkPoints) {
        this.canCreateLinkPoints = canCreateLinkPoints;
    }

    @Override
    public boolean isCanDragVertexes() {
        return canDragVertexes;
    }

    @Override
    public void setCanDragVertexes(boolean canDragVertexes) {
        this.canDragVertexes = canDragVertexes;
    }

    @Override
    public boolean isCanDragLinkPoints() {
        return canDragLinkPoints;
    }

    @Override
    public void setCanDragLinkPoints(boolean canDragLinkPoints) {
        this.canDragLinkPoints = canDragLinkPoints;
    }

    @Override
    public boolean isCanDragLinkLabels() {
        return canDragLinkLabels;
    }

    @Override
    public void setCanDragLinkLabels(boolean canDragLinkLabels) {
        this.canDragLinkLabels = canDragLinkLabels;
    }

    @Override
    public boolean isCanRemoveSelectedItems() {
        return canRemoveSelectedItems;
    }

    @Override
    public void setCanRemoveSelectedItems(boolean canRemoveSelectedItems) {
        this.canRemoveSelectedItems = canRemoveSelectedItems;
    }

    @Override
    public boolean isCanRemoveLinkPoints() {
        return canRemoveLinkPoints;
    }

    @Override
    public void setCanRemoveLinkPoints(boolean canRemoveLinkPoints) {
        this.canRemoveLinkPoints = canRemoveLinkPoints;
    }

    @Override
    public GraphCanvasPermissionsSnapshot takeSnapshot() {
        GraphCanvasPermissionsSnapshot snapshot = new GraphCanvasPermissionsSnapshot();

        snapshot.canCreateVertexes = canCreateVertexes;
        snapshot.canCreateLinks = canCreateLinks;
        snapshot.canEditVertexes = canEditVertexes;
        snapshot.canEditLinks = canEditLinks;
        snapshot.canCreateLinkPoints = canCreateLinkPoints;
        snapshot.canDragVertexes = canDragVertexes;
        snapshot.canDragLinkPoints = canDragLinkPoints;
        snapshot.canDragLinkLabels = canDragLinkLabels;
        snapshot.canRemoveSelectedItems = canRemoveSelectedItems;
        snapshot.canRemoveLinkPoints = canRemoveLinkPoints;

        return snapshot;
    }

    @Override
    public void restoreSnapshot(GraphCanvasPermissionsSnapshot snapshot) {
        canCreateVertexes = snapshot.canCreateVertexes;
        canCreateLinks = snapshot.canCreateLinks;
        canEditVertexes = snapshot.canEditVertexes;
        canEditLinks = snapshot.canEditLinks;
        canCreateLinkPoints = snapshot.canCreateLinkPoints;
        canDragVertexes = snapshot.canDragVertexes;
        canDragLinkPoints = snapshot.canDragLinkPoints;
        canDragLinkLabels = snapshot.canDragLinkLabels;
        canRemoveSelectedItems = snapshot.canRemoveSelectedItems;
        canRemoveLinkPoints = snapshot.canRemoveLinkPoints;
    }
}
