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

package info.gianlucacosta.arcontes.fx.metainfo.agents;

import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.conditions.CompositeCondition;
import info.gianlucacosta.helios.conditions.Condition;
import info.gianlucacosta.helios.conditions.UnsatisfiedConditionException;
import info.gianlucacosta.helios.metainfo.MetaInfoException;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;

import java.util.Objects;

/**
 * Metainfo agent that asks the user for a unique link name, creates a NameInfo
 * instance and associates it with the link.
 * <p>
 * The agent can be set to allow empty names: in this case, uniqueness is not
 * enforced on empty names.
 */
public class InteractiveUniqueLinkNameInfoAgent extends InteractiveNameInfoAgent<Link> {

    public InteractiveUniqueLinkNameInfoAgent(GraphContext graphContext, CommonInputService commonInputService, String inputPrompt) {
        super(graphContext, commonInputService, inputPrompt);
    }

    public InteractiveUniqueLinkNameInfoAgent(GraphContext graphContext, CommonInputService commonInputService, String inputPrompt, boolean allowEmptyName) {
        super(graphContext, commonInputService, inputPrompt, allowEmptyName);
    }

    @Override
    protected Condition<String> getInputCondition(final Link link) {
        return new CompositeCondition<>(
                super.getInputCondition(link),
                new Condition<String>() {
                    @Override
                    public void verify(String linkName) throws UnsatisfiedConditionException {
                        if (linkName.isEmpty()) {
                            return;
                        }

                        Graph graph = getGraphContext().getGraph();
                        MetaInfoRepository metaInfoRepository = getGraphContext().getMetaInfoRepository();

                        for (Link currentLink : graph.getLinks()) {
                            String currentLinkName;
                            try {
                                currentLinkName = metaInfoRepository.getMetaInfo(currentLink, NameInfo.class).getName();
                            } catch (MetaInfoException ex) {
                                continue;
                            }

                            if (Objects.equals(currentLinkName, linkName) && !Objects.equals(currentLink, link)) {
                                throw new UnsatisfiedConditionException("The link name already exists in the graph!");
                            }
                        }
                    }

                });
    }

}
