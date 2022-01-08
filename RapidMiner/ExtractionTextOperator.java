package com.rapidminer.extension.operator;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.parameter.*;
import com.rapidminer.tools.LogService;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractionTextOperator extends Operator {
    private InputPort exampleSetInput = getInputPorts().createPort("example set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");

    public static final String PARAMETER_ATTR = "attribute";
    private static final Pattern PATTERN_BRACKET = Pattern.compile("\\([^\\(\\)]+\\)");

    public ExtractionTextOperator(OperatorDescription description) {
        super(description);
        getTransformer().addPassThroughRule(exampleSetInput, exampleSetOutput);
    }

    @Override
    public List<ParameterType> getParameterTypes(){
        List<ParameterType> types = super.getParameterTypes();

        types.add(new ParameterTypeAttributes(
                PARAMETER_ATTR,
                "This parameter shows which attribute is selected to extract brackets.",
                exampleSetInput,
                false
        ));
        return types;
    }

    @Override
    public void doWork() throws OperatorException {
        LogService.getRoot().log(Level.INFO, "Start TextExtractor Operator ...");

        // fetch example set from input port
        ExampleSet exampleSet = exampleSetInput.getData(ExampleSet.class);

        // get attributes from example set
        Attributes attributes = exampleSet.getAttributes();
        // get parameter values of target attributes
        String attrText = getParameterAsString(PARAMETER_ATTR);
        LogService.getRoot().log(Level.INFO, attrText);

        String[] targetAttrs = attrText.split("\\|");
        for (String attr: targetAttrs) {
            attributes = exampleSet.getAttributes();
            Attribute attribute = attributes.get(attr);

            try {
              for(Example example:exampleSet) {
                    String temp = example.getNominalValue(attribute);

                    Matcher matcher = PATTERN_BRACKET.matcher(temp);

                    String result = temp;
                    String removeTextArea = new String();

                    while (matcher.find()) {
                        int startIndex = matcher.start();
                        int endIndex = matcher.end();

                        removeTextArea = result.substring(startIndex, endIndex);
                        result = result.replace(removeTextArea, "");
                        matcher = PATTERN_BRACKET.matcher(result);
                    }

                    example.setValue(attribute, result);
                }
            } catch (Exception e) {
                LogService.getRoot().log(Level.WARNING, e.toString());
            }
        }
        // deliver the example set to the output port
        exampleSetOutput.deliver(exampleSet);
    }
}