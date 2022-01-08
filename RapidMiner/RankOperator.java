package com.rapidminer.extension.operator;

import java.util.List;
import java.util.logging.Level;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeAttribute;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.Ontology;

public class RankOperator extends Operator {
    private InputPort exampleSetInput = getInputPorts().createPort("example set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");
    public static final String RANK_ATTRIBUTE = "Rank_Attribute";

    public RankOperator(OperatorDescription description) {
        super(description);
        getTransformer().addPassThroughRule(exampleSetInput, exampleSetOutput);
    }
    @Override
    public void doWork() throws OperatorException {
        ExampleSet exampleSet = exampleSetInput.getData(ExampleSet.class);
        String AttributeName = getParameterAsString(RANK_ATTRIBUTE);
        Attributes attributes = exampleSet.getAttributes();
        Attribute attribute = attributes.get(AttributeName);
        try {
            String newName = "RANK_" + AttributeName;
            Attribute targetAttribute = AttributeFactory.createAttribute(newName, Ontology.NUMERICAL);
            attributes.addRegular(targetAttribute);
            exampleSet.getExampleTable().addAttribute(targetAttribute);
            Integer a =1;
            double y = 0.0;
            Integer check =0;
            for (Example example : exampleSet) {
                double x = example.getValue(attribute);
                if(y == 0.0){
                    y = x;
                }else{
                    if( x == y){
                        check++;
                    }else{
                        a++;
                        a += check;
                        check =0;
                    }
                    y = x;
                }
                example.setValue(targetAttribute, a);
            }
        } catch (Exception e) {
            LogService.getRoot().log(Level.WARNING, e.toString());
        }
        exampleSetOutput.deliver(exampleSet);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeAttribute(RANK_ATTRIBUTE,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        return types;
    }
}

