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
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.Ontology;

public class CostOperator extends Operator {

    private InputPort exampleSetInput = getInputPorts().createPort("example set");
    private OutputPort exampleSetOutput = getOutputPorts().createPort("example set");
    public static final String stopNum = "stopNum";
    public static final String stationNum = "stationNum";
    public static final String lineNum = "lineNum";
    public static final String lineFre = "lineFre";
    public static final String parkNum = "parkNum";
    public static final String population = "population";
    public static final String distance = "distance";
    public static final String weight1 = "weight1";
    public static final String weight2 = "weight2";
    public static final String weight3 = "weight3";
    public static final String weight4 = "weight4";
    public static final String weight5 = "weight5";
    public static final String weight7 = "weight7";
    public static final String weight8 = "weight8";

    public CostOperator(OperatorDescription description) {
        super(description);
        getTransformer().addPassThroughRule(exampleSetInput, exampleSetOutput);
    }

    @Override
    public void doWork() throws OperatorException {
        ExampleSet exampleSet = exampleSetInput.getData(ExampleSet.class);
        String AttributeName = getParameterAsString(stopNum);
        String AttributeName2 = getParameterAsString(stationNum);
        String AttributeName3 = getParameterAsString(lineNum);
        String AttributeName4 = getParameterAsString(lineFre);
        String AttributeName5 = getParameterAsString(parkNum);
        String AttributeName7 = getParameterAsString(population);
        String AttributeName8 = getParameterAsString(distance);
        double weightNum1 = getParameterAsDouble(weight1);
        double weightNum2 = getParameterAsDouble(weight2);
        double weightNum3 = getParameterAsDouble(weight3);
        double weightNum4 = getParameterAsDouble(weight4);
        double weightNum5 = getParameterAsDouble(weight5);
        double weightNum7 = getParameterAsDouble(weight7);
        double weightNum8 = getParameterAsDouble(weight8);
        Attributes attributes = exampleSet.getAttributes();
        Attribute attribute = attributes.get(AttributeName);
        Attribute attribute2 = attributes.get(AttributeName2);
        Attribute attribute3 = attributes.get(AttributeName3);
        Attribute attribute4 = attributes.get(AttributeName4);
        Attribute attribute5 = attributes.get(AttributeName5);
        Attribute attribute7 = attributes.get(AttributeName7);
        Attribute attribute8 = attributes.get(AttributeName8);
        try {
            String newName = "costResult";
            Attribute targetAttribute = AttributeFactory.createAttribute(newName, Ontology.INTEGER);
            attributes.addRegular(targetAttribute);
            exampleSet.getExampleTable().addAttribute(targetAttribute);
            for (Example example : exampleSet) {
                double stop_num = example.getNumericalValue(attribute);
                double station_num = example.getNumericalValue(attribute2);
                double line_num = example.getNumericalValue(attribute3);
                double line_fre = example.getNumericalValue(attribute4);
                double park_num = example.getNumericalValue(attribute5);
                double population_num = example.getNumericalValue(attribute7);
                double distance_num = example.getNumericalValue(attribute8);
                double score = stop_num * weightNum1 + station_num * weightNum2 + line_num * weightNum3 + line_fre * weightNum4
                        + park_num * weightNum5  + population_num* weightNum7 + distance_num * weightNum8;
                example.setValue(targetAttribute, score);
            }
        } catch (Exception e) {
            LogService.getRoot().log(Level.WARNING, e.toString());
        }
        exampleSetOutput.deliver(exampleSet);
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeAttribute(stopNum,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeAttribute(stationNum,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeAttribute(lineNum,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeAttribute(lineFre,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeAttribute(parkNum,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeAttribute(population,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeAttribute(distance,
                "The attribute which contains the names of the annotations to be created. Should be unique.",
                exampleSetInput, false ));
        types.add(new ParameterTypeDouble(weight1,
                " Should be unique.",Double.MIN_VALUE, Double.MAX_VALUE, 0, false
        ));
        types.add(new ParameterTypeDouble(weight2,
                " Should be unique.",Double.MIN_VALUE, Double.MAX_VALUE, 0, false
        ));
        types.add(new ParameterTypeDouble(weight3,
                " Should be unique.",Double.MIN_VALUE, Double.MAX_VALUE, 0, false
        ));
        types.add(new ParameterTypeDouble(weight4,
                " Should be unique.",Double.MIN_VALUE, Double.MAX_VALUE, 0, false
        ));
        types.add(new ParameterTypeDouble(weight5,
                " Should be unique.",Double.MIN_VALUE, Double.MAX_VALUE, 0, false
        ));
        types.add(new ParameterTypeDouble(weight7,
                " Should be unique.",Double.MIN_VALUE, Double.MAX_VALUE, 0, false
        ));
        types.add(new ParameterTypeDouble(weight8,
                " Should be unique.",Double.MIN_VALUE, Double.MAX_VALUE, 0, false
        ));
        return types;
    }

}