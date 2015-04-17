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

import info.gianlucacosta.arcontes.formatting.CommonDecimalFormat;
import info.gianlucacosta.arcontes.graphs.metainfo.DefaultWeightInfo;
import info.gianlucacosta.arcontes.graphs.metainfo.WeightInfo;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.conditions.Condition;
import info.gianlucacosta.helios.conditions.PredicateBasedCondition;
import info.gianlucacosta.helios.metainfo.AbstractMetaInfoAgent;
import info.gianlucacosta.helios.metainfo.MetaInfoException;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import info.gianlucacosta.helios.predicates.DoubleRangePredicate;

import java.text.DecimalFormat;

/**
 * Sets the double weight metainfo of the given handle, after asking its value
 * to the user and validating it
 */
public class InteractiveDoubleWeightInfoAgent<THandle> extends AbstractMetaInfoAgent<THandle> {

    private final CommonInputService commonInputService;
    private final String inputPrompt;
    private final double minimum;
    private final double maximum;
    private final double defaultValue;

    public InteractiveDoubleWeightInfoAgent(CommonInputService commonInputService) {
        this(commonInputService, "Weight:");
    }

    public InteractiveDoubleWeightInfoAgent(CommonInputService commonInputService, String inputPrompt) {
        this(commonInputService, inputPrompt, 0, Double.POSITIVE_INFINITY, 0);
    }

    public InteractiveDoubleWeightInfoAgent(CommonInputService commonInputService, String inputPrompt, double minimum, double maximum, double defaultValue) {
        super(false);

        this.commonInputService = commonInputService;
        this.inputPrompt = inputPrompt;
        this.minimum = minimum;
        this.maximum = maximum;
        this.defaultValue = defaultValue;
    }

    @Override
    protected boolean doAct(MetaInfoRepository metaInfoRepository, THandle handle) {
        double currentWeight;
        try {
            WeightInfo<Double> weightInfo = metaInfoRepository.getMetaInfo(handle, WeightInfo.class);
            currentWeight = weightInfo.getWeight();
        } catch (MetaInfoException ex) {
            currentWeight = defaultValue;
        }

        Double newWeight = commonInputService.askForDouble(
                inputPrompt,
                currentWeight,
                getInputCondition(handle));

        if (newWeight == null) {
            return false;
        }

        metaInfoRepository.putMetaInfo(handle, new DefaultWeightInfo<>(newWeight));

        return true;
    }

    /**
     * @param handle the handle
     * @return the condition checked by the input dialog
     */
    protected Condition<Double> getInputCondition(THandle handle) {
        DecimalFormat decimalFormat = CommonDecimalFormat.getDecimalFormat();

        return new PredicateBasedCondition<>(
                new DoubleRangePredicate(minimum, maximum),
                String.format("Invalid weight value! It must be between %s and %s", decimalFormat.format(minimum), decimalFormat.format(maximum)));
    }

}
