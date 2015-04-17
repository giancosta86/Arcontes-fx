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

import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultNameInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.GraphContextBasedMetaInfoAgent;
import info.gianlucacosta.arcontes.graphs.metainfo.NameInfo;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.conditions.Condition;
import info.gianlucacosta.helios.conditions.PredicateBasedCondition;
import info.gianlucacosta.helios.conditions.SatisfiedCondition;
import info.gianlucacosta.helios.metainfo.MetaInfoException;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import info.gianlucacosta.helios.predicates.Predicate;

/**
 * Abstract base class for metainfo agents that should ask the user for a name
 * metainfo instance
 */
public abstract class InteractiveNameInfoAgent<THandle> extends GraphContextBasedMetaInfoAgent<THandle> {

    private final CommonInputService commonInputService;
    private final String inputPrompt;
    private final boolean allowEmptyName;

    public InteractiveNameInfoAgent(GraphContext graphContext, CommonInputService commonInputService, String inputPrompt) {
        this(graphContext, commonInputService, inputPrompt, false);
    }

    public InteractiveNameInfoAgent(GraphContext graphContext, CommonInputService commonInputService, String inputPrompt, boolean allowEmptyName) {
        super(false, graphContext);

        this.commonInputService = commonInputService;
        this.inputPrompt = inputPrompt;
        this.allowEmptyName = allowEmptyName;
    }

    @Override
    protected boolean doAct(MetaInfoRepository metaInfoRepository, THandle handle) {
        String currentName;
        try {
            currentName = metaInfoRepository.getMetaInfo(handle, NameInfo.class).getName();
        } catch (MetaInfoException ex) {
            currentName = "";
        }

        String newName = commonInputService.askForString(
                inputPrompt,
                currentName,
                getInputCondition(handle));

        if (newName == null) {
            return false;
        }

        metaInfoRepository.putMetaInfo(handle, new DefaultNameInfo(newName));

        return true;
    }

    protected Condition<String> getInputCondition(THandle handle) {
        if (allowEmptyName) {
            return new SatisfiedCondition<>();
        }

        return new PredicateBasedCondition<>(
                new Predicate<String>() {
                    @Override
                    public boolean evaluate(String name) {
                        return !name.isEmpty();
                    }

                },
                "The name cannot be empty");
    }

}
