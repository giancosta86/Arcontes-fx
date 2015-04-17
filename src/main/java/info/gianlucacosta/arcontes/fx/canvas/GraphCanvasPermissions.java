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
 * The set of permissions that are checked by a graph canvas before allowing a
 * user to perform any interaction with the graph
 */
public interface GraphCanvasPermissions {

    void disableAll();

    void enableAll();

    boolean isCanCreateLinkPoints();

    boolean isCanCreateLinks();

    boolean isCanCreateVertexes();

    boolean isCanDragLinkLabels();

    boolean isCanDragLinkPoints();

    boolean isCanDragVertexes();

    boolean isCanEditLinks();

    boolean isCanEditVertexes();

    boolean isCanRemoveLinkPoints();

    boolean isCanRemoveSelectedItems();

    void setCanCreateLinkPoints(boolean canCreateLinkPoints);

    void setCanCreateLinks(boolean canCreateLinks);

    void setCanCreateVertexes(boolean canCreateVertexes);

    void setCanDragLinkLabels(boolean canDragLinkLabels);

    void setCanDragLinkPoints(boolean canDragLinkPoints);

    void setCanDragVertexes(boolean canDragVertexes);

    void setCanEditLinks(boolean canEditLinks);

    void setCanEditVertexes(boolean canEditVertexes);

    void setCanRemoveLinkPoints(boolean canRemoveLinkPoints);

    void setCanRemoveSelectedItems(boolean canRemoveSelectedItems);

    GraphCanvasPermissionsSnapshot takeSnapshot();

    void restoreSnapshot(GraphCanvasPermissionsSnapshot snapshot);
}
